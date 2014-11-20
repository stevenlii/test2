/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-24
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.SecMerInfDao;
import com.umpay.hfmng.model.SecMerInf;


/** ******************  类说明  *********************
 * class       :  SecMerDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Repository
public class SecMerInfDaoImpl extends EntityDaoImpl<SecMerInf> implements SecMerInfDao {

	/** ********************************************
	 * method name   : get 
	 * modified      : xuhuafeng ,  2013-9-24
	 * @see          : @see com.umpay.hfmng.dao.SecMerDao#get(java.lang.String)
	 * ********************************************/
	public SecMerInf get(String subMerId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("subMerId", subMerId);
		return (SecMerInf) this.get("SecMerInf.Get", map);
	}

	/** ********************************************
	 * method name   : updateLock 
	 * modified      : xuhuafeng ,  2013-9-25
	 * @see          : @see com.umpay.hfmng.dao.SecMerDao#updateLock(com.umpay.hfmng.model.SecMerInf)
	 * ********************************************/     
	public int updateLock(SecMerInf secMerInf) throws Exception {
		return  this.update("SecMerInf.updateLock", secMerInf);
	}

	/** ********************************************
	 * method name   : isOrNotAble 
	 * modified      : xuhuafeng ,  2013-9-25
	 * @see          : @see com.umpay.hfmng.dao.SecMerInfDao#isOrNotAble(com.umpay.hfmng.model.SecMerInf)
	 * ********************************************/     
	public int isOrNotAble(SecMerInf secMerInf) throws Exception {
		return this.update("SecMerInf.isOrNotAble", secMerInf);
	}

	/** ********************************************
	 * method name   : setLock0 
	 * modified      : xuhuafeng ,  2013-9-25
	 * @see          : @see com.umpay.hfmng.dao.SecMerInfDao#setLock0(java.lang.String)
	 * ********************************************/     
	public int setLock(String subMerId, int modLock) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("subMerId", subMerId);
		map.put("modLock", String.valueOf(modLock));
		return this.update("SecMerInf.setLock", map);
	}

}
