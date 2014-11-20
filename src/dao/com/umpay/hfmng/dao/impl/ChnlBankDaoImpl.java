/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlBankDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-19
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.ChnlBankDao;
import com.umpay.hfmng.model.ChnlBank;


/** ******************  类说明  *********************
 * class       :  ChnlBankDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Repository
public class ChnlBankDaoImpl extends EntityDaoImpl<ChnlBank> implements ChnlBankDao {

	/** ********************************************
	 * method name   : findBankByChnlId 
	 * modified      : xuhuafeng ,  2013-3-19
	 * @see          : @see com.umpay.hfmng.dao.ChnlBankDao#findBankByChnlId(java.lang.String)
	 * ********************************************/
	public List<ChnlBank> findBankByChnlId(String channelId) {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("channelId", channelId);
		return super.find("ChnlBank.Find", mapWhere);
	}

	/** ********************************************
	 * method name   : get 
	 * modified      : xuhuafeng ,  2013-3-19
	 * @see          : @see com.umpay.hfmng.dao.ChnlBankDao#get(java.util.Map)
	 * ********************************************/
	public ChnlBank get(Map<String, String> mapWhere) {
		return (ChnlBank) this.get("ChnlBank.Get", mapWhere);
	}

	
	/** ********************************************
	 * method name   : updateModLock 
	 * modified      : xuhuafeng ,  2013-3-19
	 * @see          : @see com.umpay.hfmng.dao.ChnlBankDao#updateModLock(com.umpay.hfmng.model.ChnlBank)
	 * ********************************************/     
	public int updateModLock(ChnlBank chnlBank) {
		return this.update("ChnlBank.updateModLock", chnlBank);
	}

}
