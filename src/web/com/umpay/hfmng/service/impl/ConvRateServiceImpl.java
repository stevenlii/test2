package com.umpay.hfmng.service.impl;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.ZipUtil;
import com.umpay.hfmng.service.ConvRateService;
import com.umpay.hfmng.service.MessageService;

@Service
public class ConvRateServiceImpl implements ConvRateService {
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MessageService messageService;
	
	public File getDownloadFile(String flag, String date) throws Exception {
		String path="";
		if("0".equals(flag))
			path= messageService.getSystemParam("ConvRate.Summary.Path");
		else if("1".equals(flag))
			path= messageService.getSystemParam("ConvRate.Detail.Path");
		if("".equals(path)){
			throw new Exception("下载路径未配置。");
		}
		if("0".equals(flag)){
			path=path+date+".indexSum.txt";//汇总文件，如20131208.indexSum.txt
			return new File(path);
		}
		//明细文件为文件夹，需压缩为zip
		path+=date;
		File dir = new File(path);
		if(!dir.exists()){
			throw new Exception(path + "不存在。");
		}
		if(!dir.isDirectory()){
			throw new Exception(path + "不是目录。");
		}
		// 先判断下载文件是否已经存在
		File downloadFile = new File(path+".zip");
		if(downloadFile.exists()){
			log.info("下载文件已存在:" + downloadFile.getPath());
			return downloadFile;
		}
		File zipFile=new File(path+".zip");
		try {
			ZipUtil.makeDirectoryToZip(dir, zipFile, "", 9);
			return zipFile;
		} catch(Exception e){
			log.error("生成压缩文件失败：",e);
			throw e;
		}
	}

}
