package com.umpay.hfmng.dao;

import com.umpay.hfmng.base.EntityDao;

/**
 * 
*    
* @ClassName:BackUpDao 	      
* @Description: 商户/商品报备Dao      
* @version: 1.0   
* @author: lituo  
* @Create: 2014-7-15    
*
 */
public interface BackUpDao extends EntityDao<String>{
	/**
	 * 
	 * @Title: deleteBackUpOper
	 * @Description: 删除商户商品报备信息
	 * @param operList
	 * @return
	 * @author lituo
	 * @date 2014-7-15 下午05:35:30
	 */
	public int deleteBackUpOper(String operList,String userid);
	
	/**
	 * 
	 * @Title: submitBackUpOper
	 * @Description: 修改商户商品报备操作表信息
	 * @param operList
	 * @return
	 * @author lituo
	 * @date 2014-7-16 上午10:48:18
	 */
	public int updateBackUpOper(String operList,String userid);
	
	/**
	 * 
	 * @Title: updateBackUpInfo
	 * @Description: 修改商户商品报备信息表信息
	 * @param operList
	 * @return
	 * @author lituo
	 * @date 2014-7-16 上午10:50:29
	 */
	public int updateBackUpInfo(String operList,String userid,int backupstat);
	
}
