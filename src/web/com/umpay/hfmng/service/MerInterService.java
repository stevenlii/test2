/** *****************  JAVA头文件说明  ****************
 * file name  :  MerInterService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-12-23
 * *************************************************/ 

package com.umpay.hfmng.service;

import com.umpay.hfmng.model.MerInter;


/** ******************  类说明  *********************
 * class       :  MerInterService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface MerInterService {
	
	public MerInter load(String merId, String inFunCode, String inVersion);
	
	public String save(MerInter merInter) throws Exception;
	
	public String update(MerInter merInter) throws Exception;
	
	public String enableAndDisable(String merIds, String inFunCodes, String inVersions, int action) throws Exception;

}
