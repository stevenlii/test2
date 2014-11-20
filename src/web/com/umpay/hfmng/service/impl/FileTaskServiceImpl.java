package com.umpay.hfmng.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.dao.FileTaskDao;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.FileTaskService;

@Service("fileTaskService")
public class FileTaskServiceImpl implements FileTaskService{

//	private FileTaskDao fileTaskDao;
	/**
	 * 
	 * @Title: initFileTask
	 * @Description: TODO
	 * @param 
	 * @param listMer
	 * @param startTime "yyyy-MM-dd"
	 * @param endTime "yyyy-MM-dd"
	 * @param fileType (1-日交易,2-日清算,3月清算)
	 * @return
	 * @throws Exception
	 * @author wangyuxin
	 * @date 2013-2-28 上午11:47:39
	 */
	public void initFileTask(){
		FileTaskDao fileTaskDao = (FileTaskDao)SpringContextUtil.getBean("fileTaskDao");
		List<MerInfo> listMer = fileTaskDao.queryMerInf();
		Timestamp[] yesterday = new Timestamp[]{new Timestamp(getYesterday(new Date()).getTime())};
		fileTaskDao.insertFileTask(listMer, yesterday, new int[]{1, 2});
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		//每月1日则插入月清算文件记录
		if(day ==1){
			fileTaskDao.insertFileTask(listMer, yesterday, new int[]{3});
		}
	}
	
	/**
	 * 获得昨天的日期字符串。
	 * 
	 * @param Exception
	 * @return String
	 */
	public Date getYesterday(Date beginDate){
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
		return date.getTime();
	}
}
