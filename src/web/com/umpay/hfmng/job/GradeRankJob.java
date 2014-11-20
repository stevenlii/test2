package com.umpay.hfmng.job;


import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.GradeRule;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.dao.DocDao;
import com.umpay.hfmng.dao.MerGradeDao;
import com.umpay.hfmng.model.DocInfo;
import com.umpay.hfmng.model.MerGrade;
import com.umpay.hfmng.model.MerGradeRank;
import com.umpay.hfmng.service.MessageService;

@Repository("gradeRankJob")
public class GradeRankJob {
	private MerGradeDao merGradeDao;
	private DocDao docDao;
	private MessageService messageService;
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	public void doJob(){
		//获取上月、上上月、上上上月的评分总分
		//获取上个月
		log.info("生成评分等级：开始");
		//加载bean
		merGradeDao=(MerGradeDao)SpringContextUtil.getBean("merGradeDaoImpl");
		docDao=(DocDao)SpringContextUtil.getBean("docDaoImpl");
		messageService=(MessageService)SpringContextUtil.getBean("messageService");
		
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		String lastM=df.format(c.getTime());
		//获取上上月
		c.add(Calendar.MONTH, -1);
		String lastLM=df.format(c.getTime());
		//获取上上上月
		c.add(Calendar.MONTH, -1);
		String lastLLM=df.format(c.getTime());
		//获取所有的商户号（以平台显示出来的商户为标准）
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("month", lastM);
		List<MerGrade> allGrade=null;
		try{
			allGrade=merGradeDao.find(mapWhere);
			log.info("查询上月评过分的所有商户成功");
		}catch(Exception e){
			log.info("查询上月评过分的所有商户失败",e);
		}
		if(allGrade==null || allGrade.size()==0){
			log.info("没有上月评过分的商户");
			return;
		}
		//获取商户名称集合
		Map<String, String> operatorNameMap = HfCacheUtil.getCache().getOperIdAndName();
		//处理商户评分
		List<MerGradeRank> grbList = new ArrayList<MerGradeRank>();
		for (MerGrade gb : allGrade) {
			gb.trim();
			mapWhere.put("merId", gb.getMerId());
			
			mapWhere.put("month", lastM);
			MerGrade lastMGradeBean=getGrade(mapWhere);
			mapWhere.put("month", lastLM);
			MerGrade lastLMGradeBean=getGrade(mapWhere);
			mapWhere.put("month", lastLLM);
			MerGrade lastLLMGradeBean=getGrade(mapWhere);
			if(lastMGradeBean!=null && lastLMGradeBean!=null && lastLLMGradeBean!=null){
				MerGradeRank merGradeRank =new MerGradeRank();
				merGradeRank.setMerId(gb.getMerId());
				merGradeRank.setMerName(gb.getMerName());
				merGradeRank.setMerInTime(gb.getMerInTime());
				merGradeRank.setOperState(GradeRule.getOperState(gb.getMerInTime()));
				merGradeRank.setOperName(operatorNameMap.get(gb.getOperator()));
				merGradeRank.setLastMGrade(lastMGradeBean.getTotal());
				merGradeRank.setLastLMGrade(lastLMGradeBean.getTotal());
				merGradeRank.setLastLLMGrade(lastLLMGradeBean.getTotal());
				
				BigDecimal lastTotal=lastMGradeBean.getTotal();
				BigDecimal lastLTotal=lastLMGradeBean.getTotal();
				BigDecimal lastLLTotal=lastLLMGradeBean.getTotal();
				if(lastTotal!=null && lastLTotal!=null && lastLLTotal!=null){
					//设置是否连续3月呈现上升趋势
					if(lastTotal.compareTo(lastLTotal)==1 &&lastLTotal.compareTo(lastLLTotal)==1)
						merGradeRank.setIsUp("Y");
					else
						merGradeRank.setIsUp("N");
					//设置是否连续3月呈现下降趋势
					if(lastTotal.compareTo(lastLTotal)==-1 &&lastLTotal.compareTo(lastLLTotal)==-1)
						merGradeRank.setIsDown("Y");
					else
						merGradeRank.setIsDown("N");
					//设置等级
					String rank = GradeRule.getRank(lastTotal,lastLTotal,lastLLTotal);
					if(rank!=null){//不在5个所列等级中的商户不予生成
						merGradeRank.setGradeRank(rank);
						grbList.add(merGradeRank);
						log.info(merGradeRank.toString());
					}
				}
			}
		}
		//生成xls文件
		String fileName=lastM+"等级评分.xls";
		try {
			gradeRankBatchExportXls(grbList,fileName);
		} catch (Exception e) {
			log.info("生成上月评分等级文件失败：月份["+lastM+"]",e);
		}
		//保存excel文件到指定地址并在t_doc_inf表中插入记录
		DocInfo doc = new DocInfo();
		doc.setDocName(fileName);
		doc.setDocType(Const.MERGRADE_DOC_TYPE);
		doc.setDocPath(fileName);
		docDao.insert(doc);
		log.info("生成评分等级：结束");
	}
	//生成上月的评分等级xls文件
	private void gradeRankBatchExportXls(List<MerGradeRank> grbList,String fileName) throws Exception{
		 String[] title = {"商户名称","商户号","运营状态","评分等级","运营负责人","上上上月","上上月","上月","连续3月呈现上升趋势","连续3月呈现下降趋势"};
		String gradeRankFilePath = messageService.getSystemParam("MerGrade.GradeRankFilePath");
		if(gradeRankFilePath==null ||gradeRankFilePath.equals("")){
			return;
		}
		//在指定路径下生成新的文件
		//Workbook workbook = Workbook.getWorkbook(new File(gradeRankFilePath));
		WritableWorkbook copy = Workbook.createWorkbook(new File(gradeRankFilePath.trim()+fileName));
		WritableSheet sheet = copy.createSheet(fileName.substring(0,fileName.lastIndexOf(".")),0);
		Label label; 
	    for (int i = 0; i < title.length; i++) {   
	    	// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z   
	    	// 在Label对象的子对象中指明单元格的位置和内容   
	    	label = new Label(i, 0, title[i]);   
	    	// 将定义好的单元格添加到工作表中   
	    	sheet.addCell(label);
	    }
	    // 下面是填充数据
	    MerGradeRank grb=new MerGradeRank();
	    for (int i=1;i<grbList.size()+1;i++) {
	    	grb=grbList.get(i-1);
	    	  label = new Label(0, i, grb.getMerName());   
		      sheet.addCell(label);
		      label = new Label(1, i, grb.getMerId());   
		      sheet.addCell(label);
		      label = new Label(2, i, grb.getOperState());   
		      sheet.addCell(label);
		      label = new Label(3, i, grb.getGradeRank());   
		      sheet.addCell(label);
		      label = new Label(4, i, grb.getOperName());   
		      sheet.addCell(label);
		      label = new Label(5, i, grb.getLastLLMGrade().toString());   
		      sheet.addCell(label);
		      label = new Label(6, i, grb.getLastLMGrade().toString());   
		      sheet.addCell(label);
		      label = new Label(7, i, grb.getLastMGrade().toString());   
		      sheet.addCell(label);
		      label = new Label(8, i, grb.getIsUp());   
		      sheet.addCell(label);
		      label = new Label(9, i, grb.getIsDown());   
		      sheet.addCell(label);
	    }
		try {
			copy.write();
			copy.close();
			//return "上传失败，上传失败数据已导出|"+fileName+"|2|";
		} catch (Exception ex) {
			ex.printStackTrace();
			//return "上传失败数据导出失败|"+fileName+"|2|";
		}
	}
	
	private MerGrade getGrade(Map<String, String> mapWhere){
		try{
			return merGradeDao.get(mapWhere);
		}catch(Exception e){
			log.info("查询商户评分失败：商户号["+mapWhere.get("merId")+"];月份["+mapWhere.get("month")+"]",e);
			return null;
		}
	}
}
