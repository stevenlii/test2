/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-24
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.model.SecMerCnf;
import com.umpay.hfmng.model.SecMerInf;


/** ******************  类说明  *********************
 * class       :  SecMerService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface SecMerService {
	/**
	 * ********************************************
	 * method name   : load 
	 * description   : 根据主键secMerId获取数据
	 * @return       : SecMerInf
	 * @param        : @param secMerId
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-9-24  下午04:30:35
	 * *******************************************
	 */
	public SecMerInf load(String subMerId);
	
	public String save(SecMerInf secMerInf) throws Exception;
	
	public String updateSecMerInf(SecMerInf secMerInf) throws Exception;
	
	public String enableAndDisable(String[] ids,int action) throws Exception;
	
	public String auditPass(String[] ids, String resultDesc) throws Exception;
	
	public String auditNotPass(String[] ids, String resultDesc) throws Exception;
	
	public String checkKey(String subMerId) throws Exception;
	
	public List<SecMerCnf> loadCnf(String subMerId) throws Exception;
	/**
	 * *****************  方法说明  *****************
	 * method name   :  addConf
	 * @param		 :  @param file
	 * @param		 :  @param smc
	 * @param		 :  @return
	 * @param		 :  @throws Exception
	 * @return		 :  String “1”：成功；“2”：证书超过了4000个字节
	 * @author       :  LiZhen 2013-10-14 下午3:21:36
	 * description   :  添加二级商户配置
	 * @see          :  
	 * **********************************************
	 */
	public String addConf(MultipartFile file,SecMerCnf smc) throws Exception;
	/**
	 * *****************  方法说明  *****************
	 * method name   :  updateConf
	 * @param		 :  @param file
	 * @param		 :  @param smc
	 * @param		 :  @return
	 * @param		 :  @throws Exception
	 * @return		 :  String “1”：成功；“2”：证书超过了4000个字节
	 * @author       :  LiZhen 2013-10-14 下午3:19:06
	 * description   :  修改二级商户配置
	 * @see          :  
	 * **********************************************
	 */
	public String updateConf(MultipartFile file,SecMerCnf smc) throws Exception;

}
