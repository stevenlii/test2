/** *****************  JAVA头文件说明  ****************
 * file name  :  MerOperService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-3-3
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.util.List;

import com.umpay.hfmng.model.HfMerOper;


/** ******************  类说明  *********************
 * class       :  MerOperService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface MerOperService {
	
	public List<HfMerOper> findByMerId(String merId);
	
	public String getOperStrByMerId(String merId);
	/**
	 * ********************************************
	 * method name   : batchUpdate 
	 * description   : 更新一个（一级或二级）商户号下的所有运营负责人
	 * @return       : void
	 * @param        : @param merId
	 * @param        : @param operator
	 * @param        : @param userId
	 * modified      : xuhuafeng ,  2014-3-10  上午10:15:57
	 * *******************************************
	 */
	public void batchUpdate(String merId, String[] operator, String userId);

	public String getOperNameStrByOperStr(String operIdStr);
}
