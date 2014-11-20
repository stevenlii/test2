/** *****************  JAVA头文件说明  ****************
 * file name  :  MerGradeServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-2-22
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.GradeRule;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.MerGradeDao;
import com.umpay.hfmng.dao.OperLogDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.job.CalculateGradeJob;
import com.umpay.hfmng.job.GradeRankJob;
import com.umpay.hfmng.job.ReduceDataGradeJob;
import com.umpay.hfmng.job.TradeGradeJob;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.MerGrade;
import com.umpay.hfmng.model.OperLog;
import com.umpay.hfmng.service.MerGradeService;


/** ******************  类说明  *********************
 * class       :  MerGradeServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Service
public class MerGradeServiceImpl implements MerGradeService {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private MerGradeDao merGradeDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private OperLogDao operLogDao;
	@Autowired
	private TradeGradeJob tradeGradeJob;
	@Autowired
	private ReduceDataGradeJob reduceDataGradeJob;
	@Autowired
	private CalculateGradeJob calculateGradeJob;
	@Autowired
	private GradeRankJob gradeRankJob;
	
	// 设置系统稳定性指标，加权10%
	private static BigDecimal sysStabWeight=new BigDecimal("0.1");
	// 设置配合力度指标，加权5%
	private static BigDecimal cooperateWeight=new BigDecimal("0.05");
	
	private static BigDecimal defaultGrade=new BigDecimal(Const.MERGRADE_DEFAULT);
	
	/**
	 * ********************************************
	 * method name   : plus 
	 * description   : BigDecimal类型的加法
	 * @return       : BigDecimal
	 * @param        : @param a
	 * @param        : @param total
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-2-26  下午03:55:53
	 * *******************************************
	 */
	private BigDecimal plus(BigDecimal a, BigDecimal b){
		if(a != null && b != null){
			if(a.compareTo(defaultGrade) == 0){
				return b;
			}else if(b.compareTo(defaultGrade) == 0){
				return a;
			}else{
				return a.add(b);
			}
		}else if(a == null){
			return b;
		}else{
			return a;
		}
	}

	/** ********************************************
	 * method name   : get 
	 * modified      : xuhuafeng ,  2013-2-22
	 * @see          : @see com.umpay.hfmng.service.MerGradeService#get(java.lang.String, java.lang.String)
	 * ********************************************/
	public MerGrade load(String merId, String month) {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merId", merId);
		mapWhere.put("month", month);
		return merGradeDao.get(mapWhere); 
	}

	/** ********************************************
	 * method name   : update 
	 * modified      : xuhuafeng ,  2013-2-25
	 * @see          : @see com.umpay.hfmng.service.MerGradeService#update(com.umpay.hfmng.model.MerGrade)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String update(MerGrade merGrade) {
		String res = "no";
		merGrade.setModLock(1);
		try{
			MerGrade mg = load(merGrade.getMerId(), merGrade.getMonth());
			if(mg == null){
				log.error("不存在的商户或月份");
				throw new DAOException("不存在的商户或月份");
			}else{
				if(mg.getModLock() == 1){
					log.error("该数据处于锁定状态,无法修改");
					throw new DAOException("该数据处于锁定状态,无法修改");
				}
				Map<String, String> mapWhere = new HashMap<String, String>();
				mapWhere.put("ixData", merGrade.getMerId());
				mapWhere.put("ixData2", merGrade.getMonth());
				Audit au = auditDao.checkMerGrade(mapWhere);
				if(au != null){
					log.error("该数据已送交审核,无法再次修改");
					throw new DAOException("该数据已送交审核,无法再次修改");
				}
			}
			BigDecimal total = null;
			total = plus(merGrade.getTurnoverIndex(), total);
			total = plus(merGrade.getRiseRateIndex(), total);
			total = plus(merGrade.getComplaintIndex(), total);
			total = plus(merGrade.getSysStabIndex(), total);
			total = plus(merGrade.getCooperateIndex(), total);
			total = plus(merGrade.getBreachIndex(), total);
			total = plus(merGrade.getUpgradeIndex(), total);
			total = plus(merGrade.getSupportIndex(), total);
			total = plus(merGrade.getMarketingIndex(), total);
			total = plus(merGrade.getFalseTradeIndex(), total);
			merGrade.setTotal(total);
			
			//把空值改成默认值-99
			merGrade.setTurnoverIndex(setDefaultValue(merGrade.getTurnoverIndex()));
			merGrade.setRiseRateIndex(setDefaultValue(merGrade.getRiseRateIndex()));
			merGrade.setComplaintIndex(setDefaultValue(merGrade.getComplaintIndex()));
			merGrade.setSysStabIndex(setDefaultValue(merGrade.getSysStabIndex()));
			merGrade.setCooperateIndex(setDefaultValue(merGrade.getCooperateIndex()));
			merGrade.setBreachIndex(setDefaultValue(merGrade.getBreachIndex()));
			merGrade.setUpgradeIndex(setDefaultValue(merGrade.getUpgradeIndex()));
			merGrade.setSupportIndex(setDefaultValue(merGrade.getSupportIndex()));
			merGrade.setMarketingIndex(setDefaultValue(merGrade.getMarketingIndex()));
			merGrade.setFalseTradeIndex(setDefaultValue(merGrade.getFalseTradeIndex()));
			merGrade.setTotal(setDefaultValue(merGrade.getTotal()));
			
			List<MerGrade> json = new ArrayList<MerGrade>();
			mg.trim();
			json.add(mg);
			json.add(merGrade);
			String jsonString = JsonHFUtil.getJson(json);
			Audit audit = new Audit();
			audit.setModData(jsonString);
			audit.setTableName("UMPAY.T_HFMER_GRADE");
			audit.setState("0");          // 审核状态 0：待审核；1：审核通过；2：审核不通过
			audit.setAuditType("2");      // 审核类型 2：修改; 0:未知
			audit.setCreator(merGrade.getModUser()); // 创建人是当前登录用户
			audit.setIxData(merGrade.getMerId());
			audit.setIxData2(merGrade.getMonth());
			auditDao.insert(audit);
			log.info("修改商户评分入审核表成功" + audit.toString());
			//修改锁
			int updateModLock = merGradeDao.updateModLock(merGrade);
			if(updateModLock != 1){
				log.error("修改商户评分的锁状态失败" + merGrade.toString());
				throw new DAOException("修改商户评分的锁失败");
			}
			log.info("修改商户评分的锁状态成功" + merGrade.toString());
			res = "yes";
		}catch (Exception e) {
			log.error("修改商户评分失败" + merGrade.toString(), e);
			throw new DAOException("修改商户评分的锁失败");
		}
		return res;
	}

	
	/** ********************************************
	 * method name   : uploadGrades 
	 * modified      : xuhuafeng ,  2013-2-27
	 * @see          : @see com.umpay.hfmng.service.MerGradeService#uploadGrades(org.springframework.web.multipart.MultipartFile)
	 * ********************************************/     
	public List<List<String>> uploadGrades(MultipartFile file) throws Exception {
		List<List<String>> merGradeExportList = new ArrayList<List<String>>();
		String fileName = file.getOriginalFilename();
		if(fileName.endsWith(".xlsx")){
			merGradeExportList.addAll(uploadXlsx(file));
		}else if(fileName.endsWith(".xls")){
			merGradeExportList.addAll(uploadXls(file));
		}else{
			log.error("错误的文件类型！");
			throw new Exception("错误的文件类型！");
		}
		return merGradeExportList;
	}
	// 处理07以上excel文件
	public List<List<String>> uploadXlsx(MultipartFile file)
			throws Exception {
		List<List<String>> merGradeExportList = new ArrayList<List<String>>();
		boolean hasText = false;
		try {
			InputStream is=file.getInputStream();
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			int validData = 0;
			// 循环工作表Sheet
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					log.info("第"+(numSheet+1)+"个标签页为空");
					continue;
				}
				for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow == null) {
						continue;
					}
					// 过滤掉没有文本内容的行
					for (int cellNum = 0; cellNum <= xssfRow.getLastCellNum(); cellNum++) {
						XSSFCell xssfCell = xssfRow.getCell(cellNum);
						if (xssfCell != null) {
							hasText = true;
							break;
						}
					}
					if (hasText) {
						validData++;
						String merId = "", complaintCount = "", sysStabIndex = "", cooperateIndex = "", breachIndex = "", upgradeIndex = "", marketingIndex = "", supportIndex = "", failReason = "";
						merId = getValue(xssfRow.getCell(0));// 商户号
						complaintCount = getValue(xssfRow.getCell(1));// 客诉数
						sysStabIndex = getValue(xssfRow.getCell(2));// 系统稳定性指标
						cooperateIndex = getValue(xssfRow.getCell(3));// 配合力度指标
						breachIndex = getValue(xssfRow.getCell(4));// 违约情况
						upgradeIndex = getValue(xssfRow.getCell(5));// 升级重大投诉
						marketingIndex = getValue(xssfRow.getCell(6));// 营销活动
						supportIndex = getValue(xssfRow.getCell(7));// 业务资源支持
						
						// 处理导入的评分
						try {
							failReason = doGradeFromUpload(merId, complaintCount,
									sysStabIndex, cooperateIndex, breachIndex,
									upgradeIndex, marketingIndex, supportIndex);
							if(!"yes".equals(failReason)){ //导入评分失败
								throw new Exception();
							}
						} catch (Exception e) {
							// 遇到更新失败的商户评分，则加入失败列表。继续导入其它商户评分。
							List<String> merGrade = new ArrayList<String>();
							merGrade.add(merId);
							merGrade.add(complaintCount);
							merGrade.add(sysStabIndex);
							merGrade.add(cooperateIndex);
							merGrade.add(breachIndex);
							merGrade.add(upgradeIndex);
							merGrade.add(marketingIndex);
							merGrade.add(supportIndex);
							merGrade.add(failReason);
							merGradeExportList.add(merGrade);
							continue;
						}
					}
				}
			}
			if(validData == 0){
				log.info("上传文件中无有效数据");
				throw new Exception();
			}
		} catch (Exception e) {
			log.error("解析手工导入评分数据失败："+merGradeExportList.toString(),e);
    		throw e;
		}
		return merGradeExportList;
	}
	// 处理03版本excel文件
	private List<List<String>> uploadXls(MultipartFile file) throws Exception{
		List<List<String>> merGradeExportList = new ArrayList<List<String>>();
		try {
			InputStream is=file.getInputStream();
			Workbook book = Workbook.getWorkbook(is);
			Sheet sheet = book.getSheet(0);// 获得第一个sheet,默认有三个
			int rows = sheet.getRows();// 一共有多少行多少列数据
			int columns = sheet.getColumns();
			boolean hasText = false;
			int validData = 0;
			for (int i = 1; i < rows; i++) {
				// 过滤掉没有文本内容的行  
                for (int j = 0; j < columns; j++){
                	if (sheet.getCell(j, i).getContents() != "") {  
                		hasText = true;  
                		break;  
                	}
                }
                if (hasText) {
                	validData++; //每读一行则 +1
                	String merId = "", complaintCount = "", sysStabIndex = "", cooperateIndex = "", breachIndex = "", upgradeIndex = "", marketingIndex = "", supportIndex = "", failReason = "";
					merId = StringUtils.trim(sheet.getCell(0, i).getContents());//第几列第几行的数据
					complaintCount = StringUtils.trim(sheet.getCell(1, i).getContents());
					sysStabIndex = StringUtils.trim(sheet.getCell(2, i).getContents());
					cooperateIndex = StringUtils.trim(sheet.getCell(3, i).getContents());
					breachIndex = StringUtils.trim(sheet.getCell(4, i).getContents());
					upgradeIndex = StringUtils.trim(sheet.getCell(5, i).getContents());
					marketingIndex = StringUtils.trim(sheet.getCell(6, i).getContents());
					supportIndex = StringUtils.trim(sheet.getCell(7, i).getContents());
					
					// 处理导入的评分
					try {
						failReason = doGradeFromUpload(merId, complaintCount,
								sysStabIndex, cooperateIndex, breachIndex,
								upgradeIndex, marketingIndex, supportIndex);
						if(!"yes".equals(failReason)){ //导入评分失败
							throw new Exception();
						}
					} catch (Exception e) {
						// 遇到更新失败的商户评分，则加入失败列表。继续导入其它商户评分。
						List<String> merGrade = new ArrayList<String>();
						merGrade.add(merId);
						merGrade.add(complaintCount);
						merGrade.add(sysStabIndex);
						merGrade.add(cooperateIndex);
						merGrade.add(breachIndex);
						merGrade.add(upgradeIndex);
						merGrade.add(marketingIndex);
						merGrade.add(supportIndex);
						merGrade.add(failReason);
						merGradeExportList.add(merGrade);
						continue;
					}
                }
			}
			book.close();
			if(validData == 0){
				log.info("上传文件中无有效数据");
				throw new Exception();
			}
		} catch (Exception e) {
    		log.error("解析手工导入评分数据失败："+merGradeExportList.toString(),e);
    		throw e;
		} 
		return merGradeExportList;
	}
	
	/**
	 * 处理导入的商户评分
	 */
	private String doGradeFromUpload(String merId, String complaintCount,
			String sysStabIndex, String cooperateIndex, String breachIndex,
			String upgradeIndex, String marketingIndex, String supportIndex) {

		if (merId == "") {
			log.error("导入手工评分的商户号不能为空");
			return "商户号不能为空";
		} else if (!isNumeric(complaintCount) || !isNumeric(sysStabIndex)
				|| !isNumeric(cooperateIndex) || !isNumeric(breachIndex)
				|| !isNumeric(upgradeIndex) || !isNumeric(marketingIndex)
				|| !isNumeric(supportIndex)) {
			log.error("商户[" + merId + "]的指标数据只能为数字或为空");
			return "指标数据只能为数字或为空";
		}
		String lastMonth = this.getLastMonth();
		try {
			// 查找已自动生成的该商户上月评分
			MerGrade oldGrade = load(merId, lastMonth);
			if(oldGrade==null){
				log.error("系统生成的商户号["+merId+"]上月评分不存在");
				return "不存在的商户号或上月评分不存在";
			}else{
				if(oldGrade.getModLock() == 1){
					log.error("商户号["+merId+"]上月评分处于锁定状态，可能正在审核");
					return "此商户上月评分处于锁定状态，可能正在审核";
				}
				// 数据传入bean
				MerGrade gradeBean = new MerGrade();
				gradeBean.setMonth(lastMonth);
				gradeBean.setMerId(merId);
				gradeBean.setModUser(LoginUtil.getUser().getId());
				gradeBean.setComplaintCount("".equals(complaintCount) ? null : Integer.valueOf(complaintCount));
				// 设置系统稳定性指标，加权10%
				gradeBean.setSysStabIndex("".equals(sysStabIndex) ? null : (new BigDecimal(sysStabIndex)).multiply(sysStabWeight));
				// 设置配合力度指标，加权5%
				gradeBean.setCooperateIndex("".equals(cooperateIndex) ? null : (new BigDecimal(cooperateIndex)).multiply(cooperateWeight));
				// 设置违约情况、升级重大投诉、营销活动和业务支援支持等指标
				gradeBean.setBreachIndex("".equals(breachIndex) ? null : new BigDecimal(breachIndex));
				gradeBean.setUpgradeIndex("".equals(upgradeIndex) ? null : new BigDecimal(upgradeIndex));
				gradeBean.setMarketingIndex("".equals(marketingIndex) ? null : new BigDecimal(marketingIndex));
				gradeBean.setSupportIndex("".equals(supportIndex) ? null : new BigDecimal(supportIndex));
				// 设置客诉指标
				gradeBean.setComplaintIndex(GradeRule.getComplaintIndex(gradeBean.getComplaintCount(), oldGrade.getTradingCount()));
				// 设置新的总分
				BigDecimal total = null;
				total = plus(oldGrade.getTurnoverIndex(), total);
				total = plus(oldGrade.getRiseRateIndex(), total);
				total = plus(oldGrade.getFalseTradeIndex(), total);
				
				if(gradeBean.getComplaintIndex() != null){
					total = plus(gradeBean.getComplaintIndex(), total);
				}else{
					total = plus(oldGrade.getComplaintIndex(), total);
				}
				if(gradeBean.getSysStabIndex() != null){
					total = plus(gradeBean.getSysStabIndex(), total);
				}else{
					total = plus(oldGrade.getSysStabIndex(), total);
				}
				if(gradeBean.getCooperateIndex() != null){
					total = plus(gradeBean.getCooperateIndex(), total);
				}else{
					total = plus(oldGrade.getCooperateIndex(), total);
				}
				if(gradeBean.getBreachIndex() != null){
					total = plus(gradeBean.getBreachIndex(), total);
				}else{
					total = plus(oldGrade.getBreachIndex(), total);
				}
				if(gradeBean.getUpgradeIndex() != null){
					total = plus(gradeBean.getUpgradeIndex(), total);
				}else{
					total = plus(oldGrade.getUpgradeIndex(), total);
				}
				if(gradeBean.getMarketingIndex() != null){
					total = plus(gradeBean.getMarketingIndex(), total);
				}else{
					total = plus(oldGrade.getMarketingIndex(), total);
				}
				if(gradeBean.getSupportIndex() != null){
					total = plus(gradeBean.getSupportIndex(), total);
				}else{
					total = plus(oldGrade.getSupportIndex(), total);
				}
				gradeBean.setTotal(total);
				
				// 保存到数据库
				int res = merGradeDao.update(gradeBean);
				if(res == 1){
					MerGrade merGrade = load(merId, lastMonth);
					if(merGrade != null){
						merGrade.trim();
						operateLog(merGrade); //写操作日志到日志表
						log.info("评分导入成功：" + gradeBean.toString());
					}
				}else{
					log.error("评分导入失败：" + gradeBean.toString());
					return "上月评分不存在";
				}
			}
		} catch (Exception e) {
			log.error("商户[" + merId + "]上月评分手动导入失败", e);
			return "评分导入失败";
		}
		return "yes";
	}
	
	//判断单元格中的数据类型并取值
	@SuppressWarnings("static-access")
	private String getValue(XSSFCell xssfCell) {
		String value = "";
		if (xssfCell != null) {
			if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
				value = String.valueOf(xssfCell.getBooleanCellValue());
			} else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
				value = String.valueOf((int)xssfCell.getNumericCellValue());
			} else {
				value = String.valueOf(xssfCell.getStringCellValue());
			}
		}
		return StringUtils.trim(value);
	}
	
	// 判断字符串是否为数字或空字符串
	public static boolean isNumeric(String str) {
		if("".equals(str))
			return true;
		else{
			Pattern pattern = Pattern.compile("^[-+]?[0-9]+(\\.[0-9]+)?$");
			return pattern.matcher(str).matches();
		}
	}
	private String getLastMonth() {
		// 获取上个月
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		String lastMonth = df.format(c.getTime());
		return lastMonth;
	}
	
	/** ********************************************
	 * method name   : writeFile 
	 * modified      : xuhuafeng ,  2013-2-27
	 * @see          : @see com.umpay.hfmng.service.MerGradeService#writeFile(java.io.File, java.util.List)
	 * ********************************************/     
	public void writeFile(File fl, List<List<String>> merGradeList) {
		try {
			String[] title = { "商户号", "客诉数", "系统稳定性指标", "配合力度指标", "违约情况指标",
					"升级重大投诉指标", "营销活动指标", "业务资源支持指标", "失败原因" };
			String sheetNameString = "导入手工评分失败列表";
			WritableWorkbook wwb = Workbook.createWorkbook(fl);
			WritableSheet sheet = wwb.createSheet(sheetNameString, 0);
			Label label;
			for (int i = 0; i < title.length; i++) {
				// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
				// 在Label对象的子对象中指明单元格的位置和内容
				label = new Label(i, 0, title[i]);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label);
			}
			// 下面是填充数据
			for(int i=1;i<merGradeList.size()+1;i++){
				List<String> mg = merGradeList.get(i-1);
				for(int j=0;j<mg.size();j++){
					label = new Label(j, i, mg.get(j));
					sheet.addCell(label);
				}
			}
			try {   
				//从内存中写入文件中   
				wwb.write();   
				//关闭资源，释放内存   
				wwb.close();   
			} catch (Exception e) {   
				e.printStackTrace();   
			}    
		} catch (Exception e) {
			log.error("写文件失败", e);
		}
	}

	/** ********************************************
	 * method name   : readFile 
	 * modified      : xuhuafeng ,  2013-3-1
	 * @see          : @see com.umpay.hfmng.service.MerGradeService#readFile(java.lang.String)
	 * ********************************************/     
	public List<List<String>> readFile(String fileName) {
		List<List<String>> merGradeExportList = new ArrayList<List<String>>();
		try {
			File file = new File(fileName);
			Workbook book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(0);// 获得第一个sheet,默认有三个
			int rows = sheet.getRows();// 一共有多少行多少列数据
			int cols = sheet.getColumns();
			for (int i = 1; i < rows; i++) {
				List<String> merGrade = new ArrayList<String>();
				for(int j = 0;j < cols;j++){
					String value = StringUtils.trim(sheet.getCell(j, i).getContents());
					merGrade.add(value);
				}
				
				merGradeExportList.add(merGrade);
			}
			book.close();
			log.info("读文件成功：" + fileName);
		} catch (Exception e) {
    		log.error("解析数据失败："+merGradeExportList.toString(),e);
		}
		return merGradeExportList;
	}
	
	/**
	 * ********************************************
	 * method name   : operateLog 
	 * modified      : xuhuafeng ,  2013-2-24
	 * @see          : @see com.umpay.hfmng.service.MerGradeService#operateLog(com.umpay.hfmng.model.MerGrade)
	 * *******************************************
	 */
	public void operateLog(MerGrade merGrade){
		OperLog operLog = new OperLog();
		operLog.setTableName("UMPAY.T_MER_GRADE");
		operLog.setDataId(merGrade.getMerId() + "-" + merGrade.getMonth());
		operLog.setOperType("2");  //2：修改
		operLog.setModUser(merGrade.getModUser());
		
		StringBuffer sb = new StringBuffer();
		sb.append("交易额:").append(dealIndex(merGrade.getTurnoverIndex()));
		sb.append(" 增产率:").append(dealIndex(merGrade.getRiseRateIndex()));
		sb.append(" 虚假交易:").append(dealIndex(merGrade.getFalseTradeIndex()));
		sb.append(" 客诉:").append(dealIndex(merGrade.getComplaintIndex()));
		sb.append(" 系统稳定性:").append(dealIndex(merGrade.getSysStabIndex()));
		sb.append(" 违约:").append(dealIndex(merGrade.getBreachIndex()));
		sb.append(" 配合力度:").append(dealIndex(merGrade.getCooperateIndex()));
		sb.append(" 重大投诉:").append(dealIndex(merGrade.getUpgradeIndex()));
		sb.append(" 营销:").append(dealIndex(merGrade.getMarketingIndex()));
		sb.append(" 资源支持:").append(dealIndex(merGrade.getSupportIndex()));
		sb.append(" 总分:").append(dealIndex(merGrade.getTotal()));
		operLog.setDetail(sb.toString());
		operLogDao.insert(operLog);
		log.info("写入操作记录成功！" + operLog.toString());
	}
	
	private String dealIndex(BigDecimal index){
		if(defaultGrade.compareTo(index) == 0){
			return "-";
		}else{
			return index.toString();
		}
	}
	
	private BigDecimal setDefaultValue(BigDecimal value){
		if(value == null){
			return defaultGrade;
		}
		return value;
	}

	public String loadTradeGrade() {
		tradeGradeJob.doJob();
//		gradeRankJob.doJob();
		return "yes";
	}

	public String loadReduceDataGrade() {
		reduceDataGradeJob.doJob();
		return "yes";
	}
	
	public String calculateGrade() {
		calculateGradeJob.doJob();
		return "yes";
	}


}
