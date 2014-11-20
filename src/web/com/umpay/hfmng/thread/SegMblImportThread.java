package com.umpay.hfmng.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.dao.BatchTaskDao;
import com.umpay.hfmng.dao.SegMblDao;
import com.umpay.hfmng.model.SegMbl;
import com.umpay.hfmng.service.MessageService;
/**
 * ******************  类说明  ******************
 * class       :  SegMblImportThread
 * date        :  2013-12-27 
 * @author     :  LiZhen
 * @version    :  V1.0  
 * description :  处理上传的号段文件
 * @see        :                         
 * **********************************************
 */
public class SegMblImportThread implements Runnable {
	protected Logger log = Logger.getLogger(this.getClass());
//	private int size=10*1024*1024;//读取文件时buffer默认值，10M
	private BatchTaskDao batchTaskDao;
	private SegMblDao segMblDao;
	private MessageService messageService;
	private Map<String,String> provMap;//省名称和省代码的映射集合
	private String taskId;
	private String flag;
	private int sum=0;//处理的总行数
	private int succSum=0;//成功行数
	private int failSum=0;//失败行数
	private int insertSum=0;//新增行数
	private int updateSum=0;//更新行数
	private String path;//需处理的文件的全路径
	private String succPath;//成功文件的全路径
	private String failPath;//成功文件的全路径
	private List<String> failList;//暂存失败数据
	private String netType;//网络类型,0为boss网 1为智能网
	
	public SegMblImportThread(String taskId,String flag){
		this.taskId=taskId;
		this.flag=flag;
	}
	
	public SegMblImportThread(String taskId,String flag,String netType){
		this.taskId=taskId;
		this.flag=flag;
		this.netType = netType;
	}
	
	public void run() {
		//1-初始化
		preHandleFile();
		log.info(String.format("号段更新任务%s初始化完毕..",taskId));
		//2-设置任务状态为处理中
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("taskId", taskId);
		map.put("synState", 1);//1表示处理中
		batchTaskDao.updateBatchTask(map);
		log.info(String.format("号段更新任务%s状态已置为处理中，state=%s",taskId,1));
		//3-开始处理文件
		handleFile();
		
		log.info(String.format("号段更新任务%s状态已置为处理中，state=%s",taskId,1));
		//4-处理完毕后把错误数据写入文件，更新任务状态
		postHandleFile();
	}
	
	protected void handleFile(){
		BufferedReader reader = null;
		BufferedWriter succWriter=null;
		String segMblStr=null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),"GBK"));
			succWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(succPath,true),"GBK"));
			String addDesc="|号段已入库";
			String updateDesc="|号段已更新";
			String noUpdateDesc="|号段已存在，不更新";
			while ((segMblStr = reader.readLine()) != null) {
				sum++;
				//校验格式
				String[] strs=segMblStr.split(",");
				if(strs.length!=4){
					failList.add(segMblStr+"|格式错误");
					failSum++;
					continue;
				}
				SegMbl segMbl=new SegMbl();
				segMbl.setProvName(strs[0]);
				segMbl.setAreaName(strs[1]);
				String areaCode=strs[2];
				if(areaCode.length()==2)
					areaCode="0"+areaCode;
				segMbl.setAreaCode(areaCode);
				segMbl.setMobileId(strs[3]);
				String provCode=provMap.get(segMbl.getProvName());
				segMbl.setProvCode(provCode);
				//入库
				if("1".equals(flag)){//全部更新 如库内有相应号段则覆盖，没有则插入
					int i=segMblDao.update(segMbl);
					if(i==0){
						//以下五个字段均设置默认值
						segMbl.setDealerName("中国移动");
						segMbl.setCardName("全球通");
						segMbl.setCardType("0");
						segMbl.setDealerCode("0");
						if(null!=netType && !"".equals(netType)){
							segMbl.setNetType(netType);
						}else{
							segMbl.setNetType("1");
						}
						segMblDao.insert(segMbl);//更新0条，说明不存在，可以插入
						succWriter.write(segMblStr+addDesc);
						succWriter.newLine();
						insertSum++;
						succSum++;
					}else{
						succWriter.write(segMblStr+updateDesc);
						succWriter.newLine();
						updateSum++;
						succSum++;
					}
				}else if("0".equals(flag)){//部分更新  如库内有相应号段则以原库数据为准不做变动，同时如无此号段则插入数据
					SegMbl data=segMblDao.get(segMbl);
					if(data==null){
						//以下五个字段均设置默认值
						segMbl.setDealerName("中国移动");
						segMbl.setCardName("全球通");
						segMbl.setCardType("0");
						segMbl.setDealerCode("0");
						if(null!=netType && !"".equals(netType)){
							segMbl.setNetType(netType);
						}else{
							segMbl.setNetType("1");
						}
						segMblDao.insert(segMbl);
						succWriter.write(segMblStr+addDesc);
						succWriter.newLine();
						insertSum++;
					}else{
						succWriter.write(segMblStr+noUpdateDesc);
						succWriter.newLine();
					}
					succSum++;
				}
			}
		} catch (IOException e) {
			log.info("文件流出错", e);
			failList.add(segMblStr+"|格式错误");
			failSum++;
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					log.error("读文件流关闭失败", e);
				}
			}
			if(succWriter!=null){
				try {
					succWriter.close();
				} catch (IOException e) {
					log.error("写文件流关闭失败", e);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void preHandleFile(){
		batchTaskDao=(BatchTaskDao)SpringContextUtil.getBean("batchTaskDaoImpl");
		segMblDao=(SegMblDao)SpringContextUtil.getBean("segMblDaoImpl");
		messageService=(MessageService)SpringContextUtil.getBean("messageService");
		String dir=ObjectUtil.trim(messageService.getSystemParam("SegMbl.DownloadFilePath"));
		path=dir+taskId+".txt";
		succPath = dir+taskId+"_succ.txt";
		failPath = dir+taskId+"_fail.txt";
		failList = new ArrayList<String>();
		String provinces = messageService.getMessage("Province");
		provMap = JsonHFUtil.getMapFromJsonArrStr(provinces);
		
	}
	
	protected void postHandleFile(){
		Map<String,Object> map=new HashMap<String,Object>();
		String resultDesc="";
		if(failSum!=0){
			writeErrorMsg(failPath,failList);
			log.info("失败数据已写入文件："+failPath);
			map.put("synResult", 2);//2表示部分失败
			resultDesc=String.format("共处理%s行，成功%s行，其中更新%s行，新增%s行",sum,succSum,updateSum,insertSum);
		}else{
			map.put("synResult", 1);//2表示任务成功
			resultDesc=String.format("共处理%s行，成功%s行",sum,succSum);
		}
		if(succSum==0){
			map.put("synResult", 3);//3表示任务失败
			resultDesc=String.format("共处理%s行，成功0行",sum);
		}
		map.put("resultDesc", resultDesc);
		map.put("taskId", taskId);
		map.put("synState", 2);//2表示完成
		batchTaskDao.updateBatchTask(map);
		
		log.info(String.format("号段更新任务%s完成。任务结果：%s",taskId,resultDesc));
	}
	
	protected void writeErrorMsg(String failPath,List<String> failList){
		BufferedWriter failWriter=null;
		try {
			failWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(failPath,true),"GBK"));
			for(String failData : failList){
				failWriter.write(failData);
				failWriter.newLine();
			}
		} catch (IOException e) {
			log.info("文件流出错", e);
		} finally {
			if(failWriter!=null){
				try {
					failWriter.close();
				} catch (IOException e) {
					log.error("写文件流关闭失败", e);
				}
			}
		}
	}
	
}
