package com.umpay.hfmng.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.dao.BatchTaskDao;
import com.umpay.hfmng.model.BatchTask;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.SegMblService;
import com.umpay.hfmng.thread.SegMblImportThread;

@Service
public class SegMblServiceImpl implements SegMblService {
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private BatchTaskDao batchTaskDao;
	@Autowired
	private MessageService messageService;
	
	public String handleSegMbls(MultipartFile file,String flag,String netType) throws Exception {
		String result = "1";
		
		String taskId = SequenceUtil.formatSequence(SequenceUtil.getInstance().getSequence4File("hnsync"), 8);
		// 获取上传路径
		String path = messageService.getSystemParam("SegMbl.DownloadFilePath");
		// 如果路径不存在 或者配置为空
		if(path == null || "".equals(path)){
			result = "下载路径配置错误,请检查配置";
			log.error(result);
		}else{
			File tempFile = new File(path + taskId + ".txt");
			try {
				FileCopyUtils.copy(file.getBytes(), tempFile); //上传文件
				log.info("上传文件成功:"+tempFile.getName());
				//新增任务
				BatchTask batchTask=new BatchTask();
				batchTask.setTaskId(taskId);
				batchTask.setCreator(LoginUtil.getUser().getId());
				batchTask.setTaskType(6); //6为更新号段任务类型
				batchTaskDao.insert(batchTask);
			}catch(IOException e){
				log.info("文件写入失败",e);
				result ="上传文件失败";
			}catch(Exception e){
				FileUtils.deleteQuietly(tempFile);
				log.error("新增任务失败", e);
				throw e;
			}
		}
		//启动线程处理上传的文件
		if("1".equals(result)){
			SegMblImportThread thread=new SegMblImportThread(taskId,flag,netType);
			Thread t=new Thread(thread);
			t.start();
		}
		return result;
	}
	
}
