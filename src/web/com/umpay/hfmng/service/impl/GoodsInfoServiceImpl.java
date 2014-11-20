package com.umpay.hfmng.service.impl;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.common.FileUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.common.ZipUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.GoodsInfoDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.service.GoodsInfoService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.sso.org.User;

@Service
public class GoodsInfoServiceImpl implements GoodsInfoService {
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private GoodsInfoDao goodsInfoDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private MessageService messageService;

	public GoodsInfo load(Map<String, String> mapWhere)
			throws DataAccessException {
		GoodsInfo goodsInfo = (GoodsInfo) goodsInfoDao.get(mapWhere);
          log.info("查询商品信息成功："+mapWhere.toString());
		return goodsInfo;
	}

	

	/**
	 * ******************************************** method name :
	 * insertGoodsAudit 插入商品信息审核表方法 modified : Administrator , 2012-8-15
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.service.GoodsInfoService#insertGoodsAudit(com.
	 *      umpay.hfmng.model.GoodsInfo)
	 *      *******************************************
	 */
	
	public String insertGoodsAudit(GoodsInfo goodsInfo, String auditType )
			throws Exception {
		String result="1"; //操作结果 1表示成功 0 表示失败
		String jsonString = JsonHFUtil.getJsonArrStrFrom(goodsInfo);
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_GOODS_INF");
		audit.setModData(jsonString);
		audit.setState("0");
		audit.setAuditType(auditType);
    	audit.setCreator(goodsInfo.getModUser()); //  创建人 是当前登录用户
		audit.setIxData2(goodsInfo.getBusiType());
		audit.setModTime(null); //修改时间设置为null
		audit.setResultDesc("");
		audit.setIxData(goodsInfo.getMerId().trim() + "-"
				+ goodsInfo.getGoodsId().trim());
		try{
		 auditDao.insert(audit);
		 log.info("审核表中成功插入商品信息"+audit.toString());
		 return result;  //返回成功
		}
		catch(Exception e){	
			log.error("审核表插入信息出错",e);
			result="0";
			throw new DAOException("插入信息出错！");		
		}
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public String batchModGoods(List<GoodsInfo> goodsList, GoodsInfo goodsInfoNew) throws Exception {
		try {
			for (GoodsInfo goodsInfo : goodsList) {
				goodsInfo.setCusPhone(goodsInfoNew.getCusPhone());
				goodsInfoDao.updateGoodsCusPhone(goodsInfo);
			}
			log.info("成功修改商品客服电话！");
			return "1";
		} catch (DataAccessException e) {
			log.error("修改商品客服电话失败！", e);
			throw new DAOException("修改商品客服电话失败！");
		}
	}
 
	
	
	/** ********************************************
	 * method name   : modifyGoodsInfo 
	 * modified      : zhaojunbao ,  2012-8-30
	 * @see          : @see com.umpay.hfmng.service.GoodsInfoService#modifyGoodsInfo(com.umpay.hfmng.model.HfauditInfo, com.umpay.hfmng.model.GoodsInfo)
	 * ********************************************/     
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String modifyGoodsInfo(GoodsInfo goodsInfo) throws Exception {
		String result = "0";
		int modLock = goodsInfo.getModLock();
		if (modLock == 1){  //判断当前的商品锁定状态 如果是已经锁定 则提示出错
			log.error("当前商品已处于锁定状态，无法操作");
			throw new DAOException("当前商品已处于锁定状态，无法操作");		
		}
		goodsInfo.setModLock(1); //更新锁状态	
		goodsInfo.setInTime(null);  //重置入库时间为空 避免序列化时出错
		goodsInfo.setModTime(null);  // 重置修改时间为空 避免序列化时出错
		String jsonString = JsonHFUtil.getJsonArrStrFrom(goodsInfo);
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_GOODS_INF");
		audit.setModData(jsonString);
		audit.setState("0");
		audit.setAuditType("2"); // 审核状态为2 表示修改
		audit.setCreator(goodsInfo.getModUser()); // 创建人是当前登录用户
		audit.setModUser(goodsInfo.getModUser()); // 修改人也为当前登录用户
		audit.setIxData2(goodsInfo.getBusiType());
		audit.setModTime(null); //修改时间设置为null
		audit.setResultDesc("");
		audit.setIxData(goodsInfo.getMerId().trim() + "-"
				+ goodsInfo.getGoodsId().trim());						
		auditDao.insert(audit); // 入审核表		
	    int rs=goodsInfoDao.updateGoodsLock(goodsInfo); // 更新锁状态
		if(rs==1){
			log.info("修改商品信息已入审核表，详细内容"+audit.toString());
			log.info("商品状态锁已修改");
			result="1";
		}
	    if(rs==0){
	    	result="0";
	    	log.info("修改商品锁失败，修改商品信息不成功！");
	    	throw new DAOException("修改商品信息失败！") ;
	    }
		return result;

	}

	/**
	 *   ********************************************
	 * method name   : enableAnddisable 
	 * modified      : zhaojunbao ,  2012-9-3
	 * @see          : @see com.umpay.hfmng.service.GoodsInfoService#enableAnddisable(java.lang.String[], com.umpay.sso.org.User, java.lang.String)
	 * *******************************************
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW) 
	public String enableAnddisable(String[] array,User user,String action)throws Exception{	
		String result="0";
		for (int i = 0; i < array.length; i++) {
			result="0"; //循环操作时，每次操作之前 应该先将操作状态置为失败（”0“）
			String[] MerGoods = array[i].split("-");
			// result= goodsInfoService.updateGoodsInfo(goodsInfo);
			Map<String, String> mapWhere = new HashMap<String, String>();
			mapWhere.put("merId", MerGoods[0]);
			mapWhere.put("goodsId", MerGoods[1]);
			GoodsInfo goodsInfo = load(mapWhere);//先查找要修改的内容对象		
			 if(goodsInfo != null){
					if(goodsInfo.getServType()=="3"||goodsInfo.getServType().equals("3")){
						GoodsInfo monGoodsInfo=loadMonGoods(mapWhere);
						goodsInfo.setServMonth(monGoodsInfo.getServMonth());
						goodsInfo.setConMode(monGoodsInfo.getConMode());
						goodsInfo.setInterval(monGoodsInfo.getInterval());
						
					}
				    }
			goodsInfo.trim();  // 删除对象里字段空值
			//以下为初始化修改数据  包括查询参数 和要修改的目标状态
			goodsInfo.setModLock(1);     //更新锁状态
			goodsInfo.setMerId(goodsInfo.getMerId().trim());
			goodsInfo.setGoodsId(goodsInfo.getGoodsId().trim());
		    goodsInfo.setModTime(null); //入库时间和修改时间 都置为空 防止JSON序列化时出错
		    goodsInfo.setInTime(null);			
			goodsInfo.setState(action);  // 目标设置状态 2为启用 、4为禁用
			//以下输将goodsInfo数据转化为audit数据对象进行保存
			String jsonString = JsonHFUtil.getJsonArrStrFrom(goodsInfo);
			Audit audit = new Audit();
			audit.setTableName("UMPAY.T_GOODS_INF");
			audit.setModData(jsonString);
			audit.setState("0");    //审核表中审核状态  为待审核
			 if(action.equals("2")){
				 audit.setAuditType("3") ; // 目标状态为2  启用操作，审核类型为启用（3）
			 }
			 if(action.equals("4")){
				 audit.setAuditType("4"); //  目标状态为4 禁用操作，审核类型为禁用（4）
			 }
			audit.setCreator(user.getId()); // 提交人
			audit.setModUser(user.getId()); // 修改人
			audit.setIxData2(goodsInfo.getBusiType());
			audit.setResultDesc("");
			audit.setIxData(goodsInfo.getMerId().trim() + "-"
					+ goodsInfo.getGoodsId().trim());
		    auditDao.insert(audit);	
			int rs=goodsInfoDao.updateGoodsLock(goodsInfo);   //执行修改操作
			if (rs == 1) {
				log.info("修改商品锁操作成功！"+goodsInfo.toString());
				log.info("入审核表操作成功！"+audit.toString());
				result = "1";
			
			} else {
				result = "0";
				log.info("操作失败！");
				throw new DAOException("操作失败");
			}
		}		
		return result;		
	}
	
	/** ********************************************
	 * method name   : loadMonGoods 
	 * modified      : zhaojunbao ,  2012-9-12
	 * @see          : @see com.umpay.hfmng.service.GoodsInfoService#loadMonGoods(java.util.Map)
	 * ********************************************/     
	public GoodsInfo loadMonGoods(Map<String, String> mapWhere)
			throws DataAccessException {
		GoodsInfo goodsInfo = (GoodsInfo) goodsInfoDao.getMonGoods(mapWhere);
		return goodsInfo;
	}



	
	/** ********************************************
	 * method name   : getDownloadFile 
	 * modified      : xuhuafeng ,  2013-6-14
	 * @see          : @see com.umpay.hfmng.service.GoodsInfoService#getDownloadFile(java.lang.String)
	 * ********************************************/     
	public File getDownloadFile(String bankidtype) throws Exception {
		//获取下载路径
		String path = messageService.getSystemParam("Goods.DownloadFilePath");;
		// 如果路径不存在 或者配置为空
		if("".equals(path)){
			throw new Exception("下载路径为空。");
		}
		File dir = null;
		try {
			dir = new File(path); // 初始化文件
		} catch (Exception e) {
			log.error("初始化下载路径[ " + path + " ] 失败。", e);
			throw new Exception("初始化下载路径[ " + path + " ] 失败。");
		}
		File resultFile = null;
		if(dir != null){
			if(!dir.exists()){
				throw new Exception(path + "不存在。");
			}
			if(!dir.isDirectory()){
				throw new Exception(path + "不是目录。");
			}
			
			/**
			 * 因为使用了ZipUtil，这个功能会在解压缩完成之后修改文件名后缀
			 * 	例如  XE_20120108_week.txt.gz --> XE_20120108_week.txt.zip
			 */
			// 先判断是否已经存在下载文件了
			// 得到当天的上一个周日
			//String preSunday = TimeUtil.getPreSunday();
			//String fileName = bankidtype + "_" + preSunday + "_week";
			//获取前一天的日期
			String preDay = TimeUtil.getPreDay();
			String fileName = bankidtype + "_" + preDay + "_day"; 
			
			File downloadFile = new File(path + File.separator + fileName + ".zip");
			if(downloadFile.exists()){
				log.info("下载文件已存在:" + downloadFile.getPath());
				return downloadFile;
			}
			
			/**
			 * 1、列出所有文件
			 * 2、判断个数  如果只有1个文件 直接返回
			 * 3、多个文件取最后生成的 根据文件名格式XE_20120108_week.txt.gz | MW_20120108_week.txt.gz
			 */
			
			// 2、获得上周生成的文件
			// 2、修改为获得前一天生成的文件
			resultFile = new File(path + File.separator + fileName + ".txt.gz");
			// 解压
			ZipUtil.decompress(resultFile.getPath(), path);
			log.info("文件" + resultFile.getPath() + "解压完成，路径为" + path);
			
			// 生成excel
			WritableWorkbook wwb = null;
			String txtName = "";
			String zipName = "";
			String toFileName = "";
			ByteArrayOutputStream targetFile = null;
			String sheetNameString = "列表";
			try {
				// 开始读取resultFile的每一行的数据   文件名为XE_20120108_week.txt | MW_20120108_week.txt
				// 根据gz格式的文件名得到txt的文件名
				int index = resultFile.getName().lastIndexOf(".");
				txtName = resultFile.getName().substring(0, index);
				log.info("需要将文件" + txtName + "转化成excel。");
				zipName = txtName.replaceAll(".txt", ".zip");
				toFileName = txtName.replaceAll(".txt", ".xls");
				
				File txtFile = new File(path + File.separator + txtName);
				targetFile = new ByteArrayOutputStream();
				wwb = Workbook.createWorkbook(targetFile);

				// 添加第一个工作表并设置第一个Sheet的名字
				WritableSheet sheet = wwb.createSheet(sheetNameString, 0);
				BufferedReader in = new BufferedReader(new FileReader(txtFile));
				int row = 0;
				int count = 0;
				int sheetNum = 0;
				while(true){
					String line = in.readLine();
					if(line == null){
						log.info("共写入数据 " + count + " 行。");
						break;
					}
					//每65000行换一页
					if(row > 65000){
						row = 0;
						sheetNum++;
						sheet = wwb.createSheet(sheetNameString+sheetNum, sheetNum);
					}
					// 数据的数据
					String[] dataArray = line.split("\\|");
					for(int i = 0; i <dataArray.length; i++){
						sheet.addCell(new Label(i, row, dataArray[i]));
					}
					row++;
					count++;
				}
				in.close();
			} catch (RowsExceededException e1) {
				log.error("写入行异常", e1);
			} catch (WriteException e1) {
				log.error("写excel文件异常", e1);
			}
			try {
				wwb.write();
				wwb.close();
				File resultExcelFile = new File(path + File.separator + toFileName);
				FileOutputStream fos = new FileOutputStream(resultExcelFile);
				targetFile.writeTo(fos);
				targetFile.flush();
				targetFile.close();
				fos.close();
				//将excel文件压缩
				List<String> files = new ArrayList<String>();
				files.add(resultExcelFile.getPath());
				try {
					//下面步骤会将.xls文件压缩为.zip文件path + File.separator + txtName
					ZipUtil.makeZipFile(files, path, zipName.substring(0, zipName.indexOf(".")));
					//删除解压出的txt文件
					FileUtil.deleteFile(path + File.separator + txtName);
					//删除生成的xls文件,只需保留压缩后的文件
					resultExcelFile.delete();
				} catch (Exception e) {
					log.error("生成压缩文件失败：",e);
				}
				log.info("生成压缩文件成功！");
				return new File(path + File.separator + zipName);
			} catch (IOException ex) {
				log.error("写入Excel文件IOException异常：",ex);
			} catch (Exception e) {
				log.error("未知异常", e);
			}
			
		}
		return resultFile;
	}
	
}
