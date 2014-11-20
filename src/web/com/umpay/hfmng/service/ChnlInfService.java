/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlInfService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-20
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.model.ChnlInf;
import com.umpay.sso.org.User;


/** ******************  类说明  *********************
 * class       :  ChnlInfService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface ChnlInfService {
	
	public ChnlInf load(String channelId);
	
	public String saveChnlAudit(ChnlInf chnlInf);
		
	public String getCheckChnlIdOrName(Map<String, String> mapWhere);
	
	public String chnlAuditPass(String[] array, User user, String resultDesc);
	
	public String chnlAuditNotPass(String[] array, User user, String resultDesc);
	
	public String checkModChnlName(Map<String, String> mapWhere);
	
	public String modifyChnlInf(ChnlInf chnl);
	
	public String enableAndDisable(String[] array, User user, int action);
	
//	public ChnlInf loadConfig(String channelId);
	
	public String updateConf(MultipartFile file,ChnlInf chnl) throws Exception;

}
