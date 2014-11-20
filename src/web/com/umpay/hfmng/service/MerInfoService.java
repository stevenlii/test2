package com.umpay.hfmng.service;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.model.MerInfo;
import com.umpay.sso.org.User;

public interface MerInfoService{
	/**
	 * ********************************************
	 * method name   : load 
	 * description   : 根据主键查询商户
	 * @return       : MerInfo
	 * @param        : @param merInfoId
	 * @param        : @return
	 * modified      : xhf ,  2012-11-22  下午06:27:22
	 * *******************************************
	 */
	public MerInfo load(String merInfoId) ;
	/**
	 * ********************************************
	 * method name   : saveMerAudit 
	 * description   : 插入商户信息审核表方法
	 * @return       : String
	 * @param        : @param merInfo
	 * @param        : @param auditType
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : anshuqiang ,  2012-9-4  下午04:56:44
	 * @throws Exception 
	 * @see          : 
	 * *******************************************
	 */
	public String saveMerAudit (MerInfo merInfo) throws Exception ;
	/**
	 * ********************************************
	 * method name   : modifyMerInfo 
	 * description   : 修改商户信息，修改操作包括更新锁定状态，以及将修改的数据插入审核表中
	 * @return       : String
	 * @param        : @param merInfo
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : anshuqiang ,  2012-8-31  下午04:26:14
	 * @see          : 
	 * *******************************************
	 */
	public String  modifyMerInfo(MerInfo merInfo);
	/**
	 * ********************************************
	 * method name   : enable 
	 * description   : 启用禁用操作，包括批量启用禁用和单个启用禁用
	 * @return       : String
	 * @param        : @param merId
	 * @param        : @param user
	 * @param        : @return
	 * modified      : anshuqiang ,  2012-8-31  下午05:39:38
	 * @see          : 
	 * *******************************************
	 */
	public String enableAndDisable(String [] array,User user,String action);
	
	public Map<String, String> importMer(String merId, String userId);
	public List<MerInfo> loadAll();
	public List<MerInfo> filtrationMerByName(String name);
}
