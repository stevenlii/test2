/** *****************  JAVA头文件说明  ****************
 * file name  :  BusiConfService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-1-15
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.util.List;

import com.umpay.hfmng.model.MerBusiConf;


/** ******************  类说明  *********************
 * class       :  BusiConfService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface BusiConfService {
	
	public MerBusiConf load(String merId, String bizType);
	
	public String save(MerBusiConf merBusiConf) throws Exception;
	
	public List<MerBusiConf> getListByMerId(String merId);
	
	public String auditPass(String id, String resultDesc) throws Exception;
	
	public String auditNotPass(String id, String resultDesc) throws Exception;
	
	public String enableAndDisable(String merId, String[] bizTypes, int action) throws Exception;

}
