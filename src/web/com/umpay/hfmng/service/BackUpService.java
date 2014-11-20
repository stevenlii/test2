package com.umpay.hfmng.service;

/**
 * 
*    
* @ClassName:BackUpService 	      
* @Description: 商户/商品报备Service      
* @version: 1.0   
* @author: lituo  
* @Create: 2014-7-15    
*
 */

public interface BackUpService {
	/**
	 * 
	 * @Title: deleteBackUpOper
	 * @Description: 删除商户商品报备信息
	 * @param operList
	 * @return
	 * @author lituo
	 * @date 2014-7-15 下午05:30:44
	 */
	public int deleteBackUpOper(String operList,String userid,int backupstat);
	
	/**
	 * 
	 * @Title: submitBackUpOper
	 * @Description: 提交商户商品报备信息
	 * @param operList
	 * @return
	 * @author lituo
	 * @date 2014-7-16 上午10:47:35
	 */
	public int submitBackUpOper(String operList,String userid,int backupstat);
}
