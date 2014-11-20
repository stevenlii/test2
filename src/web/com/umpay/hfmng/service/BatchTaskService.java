/** *****************  JAVA头文件说明  ****************
 * file name  :  BatchTaskService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-12-26
 * *************************************************/ 

package com.umpay.hfmng.service;

import com.umpay.hfmng.model.BatchTask;


/** ******************  类说明  *********************
 * class       :  BatchTaskService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public interface BatchTaskService {
	
	public BatchTask load(String taskId);

}
