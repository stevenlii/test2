package com.umpay.hfmng.service.impl;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
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

import com.umpay.hfmng.common.CheckDataUtil;
import com.umpay.hfmng.dao.FeeCodeDao;
import com.umpay.hfmng.dao.OperLogDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.FeeCode;
import com.umpay.hfmng.model.OperLog;
import com.umpay.hfmng.service.FeeCodeService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;

@Service
public class FeeCodeServiceImpl implements FeeCodeService{
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private FeeCodeDao feeCodeDao;
	@Autowired
	private OperLogDao operLogDao;
	@Autowired
	private OptionService optionService;
	
	/**
	 * ********************************************
	 * method name   : saveFeeCode 
	 * modified      : xhf ,  2012-11-21
	 * @see          : @see com.umpay.hfmng.service.FeeCodeService#saveFeeCode(com.umpay.hfmng.model.FeeCode)
	 * *******************************************
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String saveFeeCode(FeeCode feeCode) {
		String result = "0";
		feeCodeDao.saveFeeCode(feeCode);
		log.info("计费代码添加成功：" + feeCode.toString());
		operateLog(feeCode, 1);
		result = "1"; // 返回成功
		return result;
	}
	
	/**
	 * ********************************************
	 * method name   : load 
	 * modified      : xhf ,  2012-11-21
	 * @see          : @see com.umpay.hfmng.service.FeeCodeService#load(java.lang.String)
	 * *******************************************
	 */
	public FeeCode load(String serviceId) {
		FeeCode feeCode = (FeeCode)feeCodeDao.get(serviceId);
		return feeCode;
	}
	
	/**
	 * ********************************************
	 * method name   : modifyFeeCode 
	 * modified      : xhf ,  2012-11-21
	 * @see          : @see com.umpay.hfmng.service.FeeCodeService#modifyFeeCode(com.umpay.hfmng.model.FeeCode)
	 * *******************************************
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String modifyFeeCode(FeeCode feeCode) {
		String result = "0";
		int update = feeCodeDao.update(feeCode);
		if(update == 1){
			log.info("计费代码修改成功：" + feeCode.toString());
		}else{
			log.error("计费代码修改失败：" + feeCode.toString());
			throw new DAOException("计费代码修改失败!");
		}
		operateLog(feeCode, 2);
		result = "1"; // 返回成功
		return result;
	}
	
	/**
	 * ********************************************
	 * method name   : enableAndDisable 
	 * modified      : xhf ,  2012-11-21
	 * @see          : @see com.umpay.hfmng.service.FeeCodeService#enableAndDisable(java.lang.String[], com.umpay.sso.org.User, java.lang.String)
	 * *******************************************
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String enableAndDisable(String[] serviceIds, User user,String action) {
	    String result="0";
	    try{
	    	for (int i = 0; i < serviceIds.length; i++) {
	    		result="0";   //循环操作时，每次操作之前应该先将操作状态置为失败（"0"）
	    		FeeCode feeCode = load(serviceIds[i]);   // 先查找要修改的内容对象
	    		// 以下为初始化修改数据 包括查询参数 和要修改的目标状态
	    		feeCode.setState(action); // 目标设置状态 2为启用 ,4为禁用
	    		feeCode.trim();
	    		int lock=feeCodeDao.updateFeeCodeLock(feeCode);
	    		if (lock == 1) {
	    			log.info("启用/禁用成功！" + feeCode.toString());
	    		} else {
	    			result = "0";
	    			log.error("启用/禁用失败！" + feeCode.toString());
	    			throw new DAOException("操作失败！" + feeCode.toString());
	    		}
	    		if("2".equals(action)){
	    			operateLog(feeCode, 5);  //5：启用
	    		}else{
	    			operateLog(feeCode, 6);  //6：禁用
	    		}
	    		result = "1"; // 返回成功
	    	}	
	    }catch (Exception e) {
	    	log.error("启用/禁用失败",e);
			result="0";
			throw new DAOException("启用/禁用失败！");
		}
		return result;
		
	}
	/**
	 * ********************************************
	 * method name   : upload 
	 * modified      : xhf ,  2012-11-21
	 * @see          : @see com.umpay.hfmng.service.FeeCodeService#upload(org.springframework.web.multipart.MultipartFile)
	 * *******************************************
	 */
	public List<FeeCode> upload(MultipartFile file) throws Exception {
		List<FeeCode> feeCodeExportList = new ArrayList<FeeCode>();
		String fileName = file.getOriginalFilename();
		if(fileName.endsWith(".xlsx")){
			feeCodeExportList.addAll(uploadXlsx(file));
		}else if(fileName.endsWith(".xls")){
			feeCodeExportList.addAll(uploadXls(file));
		}else{
			log.error("错误的文件类型！");
			throw new Exception("错误的文件类型！");
		}
		return feeCodeExportList;
	}
	/**
	 * ********************************************
	 * method name   : uploadXls 
	 * description   : 解析excel2003文件
	 * @return       : List<FeeCode>
	 * @param        : @param file
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xhf ,  2012-11-21  下午05:33:45
	 * *******************************************
	 */
	private List<FeeCode> uploadXls(MultipartFile file) throws Exception{
		List<FeeCode> feeCodeExportList = new ArrayList<FeeCode>();
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
                	String serviceId = StringUtils.trim(sheet.getCell(0, i).getContents());//第几列第几行的数据
                	String detail = StringUtils.trim(sheet.getCell(1, i).getContents());
                	String feeType = StringUtils.trim(sheet.getCell(2, i).getContents());
                	String category = StringUtils.trim(sheet.getCell(3, i).getContents());
                	String amount = StringUtils.trim(sheet.getCell(4, i).getContents());
                	if("".equals(serviceId) || "".equals(detail) || "".equals(feeType) || "".equals(category) || "".equals(amount)){
                		continue;
                	}
                	validData++;
                	FeeCode feeCode = new FeeCode();
                	feeCode.setServiceId(serviceId);
                	feeCode.setDetail(detail);
                	feeCode.setFeeType(feeType);
                	feeCode.setCategory(category);
                	feeCode.setAmount(amount);
                	
                	feeCode.setFailReason(checkData(feeCode)); //校验数据
                	if(feeCode.getFailReason() != "" || feeCode.getFailReason() != null){
                		feeCodeExportList.add(feeCode);
                		log.error("失败数据："+feeCode.toString());
                		continue;
                	}
                	try{
                		//判断是否数据重复
                		FeeCode feeCodeList = load(serviceId);
                		if(feeCodeList != null){
                			feeCode.setFailReason("计费代码已存在");
                			feeCodeExportList.add(feeCode);
                			log.info("重复数据："+feeCode.toString());
                		}else{
                			//入表
                			saveFeeCode(feeCode);
                			log.info("解析数据成功："+feeCode.toString());
                		}
                	}catch (Exception e) {
                		feeCode.setFailReason("未知错误");
                		feeCodeExportList.add(feeCode);
                		log.error("失败数据："+feeCode.toString(),e);
					}
                }
			}
			book.close();
			if(validData == 0){
				log.info("上传文件中无有效数据");
				throw new Exception();
			}
		} catch (Exception e) {
    		log.error("解析数据失败："+feeCodeExportList.toString(),e);
    		throw e;
		} 
		return feeCodeExportList;
	}
	/**
	 * ********************************************
	 * method name   : uploadXlsx 
	 * description   : 解析excel2007文件
	 * @return       : List<FeeCode>
	 * @param        : @param file
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xhf ,  2012-11-21  下午05:35:46
	 * *******************************************
	 */
	// 使用poi处理excel文件 解决jxl无法处理xlsx版本的excel
	private List<FeeCode> uploadXlsx(MultipartFile file) throws Exception{
		boolean hasText = false;
		List<FeeCode> feeCodeExportList = new ArrayList<FeeCode>();
		try{
			InputStream is=file.getInputStream();
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			// 循环工作表Sheet
			int validData = 0;
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
						String serviceId = StringUtils.trim(getValue(xssfRow.getCell(0)));
	                	String detail = StringUtils.trim(getValue(xssfRow.getCell(1)));
	                	String feeType = StringUtils.trim(getValue(xssfRow.getCell(2)));
	                	String category = StringUtils.trim(getValue(xssfRow.getCell(3)));
	                	String amount = StringUtils.trim(getValue(xssfRow.getCell(4)));
	                	if("".equals(serviceId) || "".equals(detail) || "".equals(feeType) || "".equals(category) || "".equals(amount)){
	                		continue;
	                	}
	                	validData++;
	                	FeeCode feeCode = new FeeCode();
	                	feeCode.setServiceId(serviceId);
	                	feeCode.setDetail(detail);
	                	feeCode.setFeeType(feeType);
	                	feeCode.setCategory(category);
	                	feeCode.setAmount(amount);
	                	
	                	feeCode.setFailReason(checkData(feeCode)); //校验数据
	                	if(feeCode.getFailReason() != ""){
	                		
	                		feeCodeExportList.add(feeCode);
	                		log.error("失败数据："+feeCode.toString());
	                		continue;
	                	}
	                	try{
	                		//判断是否数据重复
	                		FeeCode feeCodeList = load(serviceId);
	                		if(feeCodeList != null){
	                			feeCode.setFailReason("计费代码已存在");
	                			feeCodeExportList.add(feeCode);
	                			log.info("重复数据："+feeCode.toString());
	                		}else{
	                			//入表
	                			saveFeeCode(feeCode);
	                			log.info("解析数据成功："+feeCode.toString());
	                		}
	                	}catch (Exception e) {
	                		feeCode.setFailReason("未知错误");
	                		feeCodeExportList.add(feeCode);
	                		log.error("失败数据："+feeCode.toString(),e);
						}
	                }
				}
			}
			if(validData == 0){
				log.info("上传文件中无有效数据");
				throw new Exception();
			}
		} catch (Exception e) {
    		log.error("解析数据失败："+feeCodeExportList.toString(),e);
    		throw e;
		} 
		return feeCodeExportList;
	}
	/**
	 * ********************************************
	 * method name   : getValue 
	 * description   : 判断单元格中的数据类型并取值
	 * @return       : String
	 * @param        : @param xssfCell
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:36:09
	 * *******************************************
	 */
	@SuppressWarnings("static-access")
	private String getValue(XSSFCell xssfCell) {
		if (xssfCell != null) {
			if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(xssfCell.getBooleanCellValue());
			} else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
				return String.valueOf((int)xssfCell.getNumericCellValue());
			} else {
				return String.valueOf(xssfCell.getStringCellValue());
			}
		}
		return "";
	}
	/**
	 * ********************************************
	 * method name   : operateLog 
	 * description   : 写日志
	 * @return       : void
	 * @param        : @param feeCode
	 * @param        : @param action
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-11-21  下午05:36:41
	 * *******************************************
	 */
	private void operateLog(FeeCode feeCode,int action){
		OperLog operLog = new OperLog();
		operLog.setTableName("UMPAY.T_SERVICE_INF");
		operLog.setDataId(feeCode.getServiceId());
		if(action == 1){
			operLog.setOperType("1");  //1:添加
		}else if(action == 2){
			operLog.setOperType("2");  //2：修改
		}else if(action == 5){
			operLog.setOperType("5");  //5：启用
		}else if(action == 6){
			operLog.setOperType("6");  //6：禁用
		}
		operLog.setModUser(feeCode.getModUser());
		Map<String, String> categoryMap = optionService.getFeeCodeCategoryMap();
		String category = categoryMap.get(feeCode.getCategory());
		category = (category == null) ? "" : category;
		String feeType = "";
		if("2".equals(feeCode.getFeeType())){
			feeType = "按次";
		}else if("3".equals(feeCode.getFeeType())){
			feeType ="包月";
		}
		if(action != 5 && action != 6){
			operLog.setDetail(feeCode.getDetail()+"|"+feeCode.getServiceId()+"|"+feeType+"|"+category+"|"+feeCode.getAmount());
		}
		operLogDao.insert(operLog);
		log.info("写入操作记录成功！");
	}
	/**
	 * ********************************************
	 * method name   : checkData 
	 * description   : 校验数据
	 * @return       : String
	 * @param        : @param feeCode
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:37:24
	 * *******************************************
	 */
	private String checkData(FeeCode feeCode){
		feeCode.setFailReason("");
		String[] feeTypeId = {"2","3"};
    	if(!CheckDataUtil.check(feeCode.getServiceId(), "^([A-Za-z]{4}|-[A-Za-z]{4})$")){
    		feeCode.setFailReason("计费代码不合法");
    	}else if(!CheckDataUtil.check(feeCode.getDetail(), "^.{1,13}$")){
    		feeCode.setFailReason("代码名称不能超过13个字符");
    	}else if(!CheckDataUtil.check(feeCode.getFeeType(), feeTypeId)){
    		feeCode.setFailReason("类型只能为2或3");
    	}else if(!CheckDataUtil.check(feeCode.getCategory(), "^[0-9]{3}$")){
    		feeCode.setFailReason("分类只能为3位数字");
    	}else if(!CheckDataUtil.check(feeCode.getAmount(), "^[0-9]{1,13}$")){
    		feeCode.setFailReason("金额长度为不超过13位的正整数");
    	}
    	return feeCode.getFailReason();
	}
	
	public void writeFile(File fl,List<FeeCode> feeCodeList){
		try {
			String[] title = {"代码", "代码名称", "类型", "分类", "金额(分)", "失败原因"};
			String sheetNameString = "导入失败列表";
			WritableWorkbook wwb = Workbook.createWorkbook(fl);
			WritableSheet sheet = wwb.createSheet(sheetNameString, 0);
			Label label;
			jxl.write.WritableFont wfont = new jxl.write.WritableFont(WritableFont
					.createFont("宋体"), 12);
			WritableCellFormat font = new WritableCellFormat(wfont);
			for (int i = 0; i < title.length; i++) {
				// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
				// 在Label对象的子对象中指明单元格的位置和内容
				label = new Label(i, 0, title[i]);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label);
			}
			// 下面是填充数据
			for(int i=1;i<feeCodeList.size()+1;i++){
				FeeCode fee = feeCodeList.get(i-1);
				label = new Label(0, i, fee.getServiceId());
				sheet.addCell(label);
				label = new Label(1, i, fee.getDetail());
				sheet.addCell(label);
				label = new Label(2, i, fee.getFeeType());
				sheet.addCell(label);
				label = new Label(3, i, fee.getCategory());
				sheet.addCell(label);
				label = new Label(4, i, fee.getAmount());
				sheet.addCell(label);
				label = new Label(5, i, fee.getFailReason());
				sheet.addCell(label);
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
		} finally {
			if (null != fl) {
				fl.deleteOnExit(); // 令临时文件在JVM关闭的时候自动删除
			}
		}
	}
	
	public List<FeeCode> readFile(String fileName){
		List<FeeCode> feeCodeExportList = new ArrayList<FeeCode>();
		try {
			File file = new File(fileName);
			Workbook book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(0);// 获得第一个sheet,默认有三个
			int rows = sheet.getRows();// 一共有多少行多少列数据
			for (int i = 1; i < rows; i++) {
				String serviceId = StringUtils.trim(sheet.getCell(0, i).getContents());//第几列第几行的数据
				String detail = StringUtils.trim(sheet.getCell(1, i).getContents());
				String feeType = StringUtils.trim(sheet.getCell(2, i).getContents());
				String category = StringUtils.trim(sheet.getCell(3, i).getContents());
				String amount = StringUtils.trim(sheet.getCell(4, i).getContents());
				String failReason = StringUtils.trim(sheet.getCell(5, i).getContents());
				
				FeeCode feeCode = new FeeCode();
				feeCode.setServiceId(serviceId);
				feeCode.setDetail(detail);
				feeCode.setFeeType(feeType);
				feeCode.setCategory(category);
				feeCode.setAmount(amount);
				feeCode.setFailReason(failReason);
				
				feeCodeExportList.add(feeCode);
			}
			book.close();
		} catch (Exception e) {
    		log.error("解析数据失败："+feeCodeExportList.toString(),e);
		}
		return feeCodeExportList;
	}
	
}
