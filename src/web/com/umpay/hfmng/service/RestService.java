package com.umpay.hfmng.service;

import java.util.Map;

import com.umpay.hfmng.model.PayltdRollback;

public interface RestService {
	/**
	 * ********************************************
	 * method name   : updateRestCache 
	 * description   : 更新资源层缓存
	 * @return       : String
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : lz ,  2013-4-9  下午07:57:46
	 * @see          : 
	 * *******************************************
	 */
	String updateRestCache() throws Exception;
	/**
	 * ********************************************
	 * method name   : revokePayLtd 
	 * description   : 限额回滚
	 * @return       : String
	 * @param        : @param payltdRollback
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : lz ,  2013-4-15  下午03:04:11
	 * @see          : 
	 * *******************************************
	 */
	public String revokePayLtd(PayltdRollback payltdRollback) throws Exception;
	/**
	 * ********************************************
	 * method name   : queryPayed 
	 * description   : 根据手机号查询月累计额
	 * @return       : Map<String,String>
	 * @param        : @param mobileId
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-4-16  上午09:32:22
	 * *******************************************
	 */
	public Map<String, String> queryPayed(String mobileId);

}
