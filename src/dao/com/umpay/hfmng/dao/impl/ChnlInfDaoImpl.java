/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlInfDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-18
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.ChnlInfDao;
import com.umpay.hfmng.model.ChnlInf;


/** ******************  类说明  *********************
 * class       :  ChnlInfDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Repository("chnlInfDaoImpl")
public class ChnlInfDaoImpl extends EntityDaoImpl<ChnlInf> implements ChnlInfDao {

	/** ********************************************
	 * method name   : getChnlList 
	 * modified      : xuhuafeng ,  2013-3-18
	 * @see          : @see com.umpay.hfmng.dao.ChnlInfDao#getChnlList()
	 * ********************************************/
	@SuppressWarnings("unchecked")
	public List<ChnlInf> getChnlList() {
		return super.find("ChnlInf.Get");
	}
	
	public ChnlInf get(String channelId){
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("channelId", channelId);
		return (ChnlInf) this.get("ChnlInf.Get", mapWhere);
	}
	/**
	 * ********************************************
	 * method name   : getCheckFromChnls 根据渠道编号或名称查找
	 * modified      : lz ,  2013-3-25
	 * @see          : @see com.umpay.hfmng.dao.ChnlInfDao#getCheckFromChnls(java.util.Map)
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getCheckFromChnls(Map<String, String> mapWhere) {
		List list =null;
		try{
			 list = this.find("ChnlInf.checkFromChnls", mapWhere);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void saveChnl(ChnlInf chnlInf) {
		this.save("ChnlInf.insertChnlInf", chnlInf);
	}
	
	public int updateChnl(ChnlInf chnlInf) {
		return this.update("ChnlInf.updateChnlInf", chnlInf);
	}
	
	public int isOrNotAble(ChnlInf chnlInf) {
		return this.update("ChnlInf.isOrNotAble", chnlInf);
	}
	
	public int updateChnlLock(ChnlInf chnlInf)  {
		return  this.update("ChnlInf.updateChnlLock", chnlInf);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> checkModChnlName(Map<String, String> mapWhere) {
		return this.find("ChnlInf.checkModChnlName", mapWhere);
	}

	public void updateConf(ChnlInf chnlInf) {
		this.save("ChnlInf.updateConf", chnlInf);
	}
	
//	public ChnlInf getConfig(String channelId){
//		Map<String, String> mapWhere = new HashMap<String, String>();
//		mapWhere.put("channelId", channelId);
//		return (ChnlInf) this.get("ChnlInf.Get", mapWhere);
//	}

}
