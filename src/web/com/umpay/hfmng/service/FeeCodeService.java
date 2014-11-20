package com.umpay.hfmng.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.umpay.hfmng.model.FeeCode;
import com.umpay.sso.org.User;

public interface FeeCodeService {
	/**
	 * ********************************************
	 * method name   : saveFeeCode 
	 * description   : 新增计费代码
	 * @return       : String
	 * @param        : @param feeCode
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-11-21  下午05:19:44
	 * *******************************************
	 */
	public String saveFeeCode(FeeCode feeCode);
	/**
	 * ********************************************
	 * method name   : load 
	 * description   : 根据主键查询计费代码
	 * @return       : FeeCode
	 * @param        : @param serviceId
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:20:35
	 * *******************************************
	 */
	public FeeCode load(String serviceId);
	/**
	 * ********************************************
	 * method name   : modifyFeeCode 
	 * description   : 修改计费代码
	 * @return       : String
	 * @param        : @param feeCode
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:20:55
	 * *******************************************
	 */
	public String modifyFeeCode(FeeCode feeCode);
	/**
	 * ********************************************
	 * method name   : enableAndDisable 
	 * description   : 启用/禁用计费代码
	 * @return       : String
	 * @param        : @param array
	 * @param        : @param user
	 * @param        : @param action
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:21:10
	 * *******************************************
	 */
	public String enableAndDisable(String[] array,User user,String action);
	/**
	 * ********************************************
	 * method name   : upload 
	 * description   : 导入计费代码
	 * @return       : List<FeeCode>
	 * @param        : @param file
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:21:27
	 * *******************************************
	 */
	public List<FeeCode> upload(MultipartFile file) throws Exception ;
	
	public void writeFile(File fl,List<FeeCode> feeCodeList);
	public List<FeeCode> readFile(String fileName);
}
