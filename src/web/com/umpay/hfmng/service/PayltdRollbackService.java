/** *****************  JAVA头文件说明  ****************
 * file name  :  PayltdRollbackService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-2-13
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.util.Map;

import com.umpay.hfmng.model.PayltdRollback;


/** ******************  类说明  *********************
 * class       :  PayltdRollbackService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface PayltdRollbackService {
	
	public Map<String, String> queryPayed(String mobileId) throws Exception;
	
	public String save(PayltdRollback payltdRollback) throws Exception;
	
	public String auditPass(String id, String resultDesc) throws Exception;
	
	public String auditNotPass(String id, String resultDesc) throws Exception;

}
