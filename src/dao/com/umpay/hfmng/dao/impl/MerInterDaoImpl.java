/** *****************  JAVA头文件说明  ****************
 * file name  :  MerInterDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-12-19
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.MerInterDao;
import com.umpay.hfmng.model.MerInter;


/** ******************  类说明  *********************
 * class       :  MerInterDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Repository
public class MerInterDaoImpl extends EntityDaoImpl<MerInter> implements MerInterDao {

	/** ********************************************
	 * method name   : get 
	 * modified      : xuhuafeng ,  2013-12-19
	 * @see          : @see com.umpay.hfmng.dao.MerInterDao#get(java.util.Map)
	 * ********************************************/
	public MerInter get(Map<String, String> mapWhere) {
		return (MerInter) this.get("MerInter.Get", mapWhere);
	}

	/** ********************************************
	 * method name   : updateState 
	 * modified      : xuhuafeng ,  2013-12-19
	 * @see          : @see com.umpay.hfmng.dao.MerInterDao#updateState(com.umpay.hfmng.model.MerInter)
	 * ********************************************/
	public int updateState(MerInter merInter) throws Exception {
		return this.update("MerInter.updateState", merInter);
	}

}
