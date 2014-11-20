package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.BackUpDao;

/**
 * 
*    
* @ClassName:BackUpDaoImpl 	      
* @Description: 商户/商品报备Dao      
* @version: 1.0   
* @author: lituo  
* @Create: 2014-7-15    
*
 */
@Repository("backUpDaoImpl")
public class BackUpDaoImpl extends EntityDaoImpl<String> implements BackUpDao{
	/**
	 * 
	 * @Title: deleteBackUpOper
	 * @Description: 删除商户商品报备信息
	 * @param operList
	 * @return
	 * @author lituo
	 * @date 2014-7-15 下午05:35:30
	 */
	public int deleteBackUpOper(String operList,String userid){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operList", operList);
		para.put("userid", userid);
		int result = this.update("BackUp.updateValid", para);
		return result;
	}
	
	/**
	 * 
	 * @Title: updateBackUpOper
	 * @Description: 修改报备操作表数据
	 * @param operList
	 * @return
	 * @author lituo
	 * @date 2014-7-16 上午10:49:26
	 */
	public int updateBackUpOper(String operList,String userid){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operList", operList);
		para.put("userid", userid);
		int result = this.update("BackUp.updateBackUpOper", para);
		return result;
	}
	
	/**
	 * 
	 * @Title: updateBackUpInfo
	 * @Description: 修改报备信息表数据
	 * @param operList
	 * @return
	 * @author lituo
	 * @date 2014-7-16 上午10:49:48
	 */
	public int updateBackUpInfo(String operList,String userid,int backupstat){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operList", operList);
		para.put("userid", userid);
		para.put("backupstat", backupstat);
		int result = this.update("BackUp.updateBackUpInfo", para);
		return result;
	}
}
