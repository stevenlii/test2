/** *****************  JAVA头文件说明  ****************
 * file name  :  BatchTaskServiceImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-12-26
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.dao.BankDao;
import com.umpay.hfmng.dao.BatchTaskDao;
import com.umpay.hfmng.model.BatchTask;
import com.umpay.hfmng.service.BatchTaskService;


/** ******************  类说明  *********************
 * class       :  BatchTaskServiceImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Service
public class BatchTaskServiceImpl implements BatchTaskService {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private BatchTaskDao batchTaskDao;

	/** ********************************************
	 * method name   : load 
	 * modified      : xuhuafeng ,  2013-12-26
	 * @see          : @see com.umpay.hfmng.service.BatchTaskService#load(java.lang.String)
	 * ********************************************/
	public BatchTask load(String taskId) {
		BatchTask bt = new BatchTask();
		bt.setTaskId(taskId);
		BatchTask task = batchTaskDao.get(bt);
		if(task != null){
			task.trim();
		}
		return task;
	}

}
