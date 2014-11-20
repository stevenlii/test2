/** *****************  JAVA头文件说明  ****************
 * file name  :  MerOperDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-3-3
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.MerOperDao;
import com.umpay.hfmng.model.HfMerOper;


/** ******************  类说明  *********************
 * class       :  MerOperDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Repository
public class MerOperDaoImpl extends EntityDaoImpl<HfMerOper> implements MerOperDao {
	
	/** ********************************************
	 * method name   : bacthInsert 
	 * modified      : xuhuafeng ,  2014-3-3
	 * @see          : @see com.umpay.hfmng.dao.MerOperDao#bacthInsert(java.util.List)
	 * ********************************************/     
	public void bacthInsert(List<HfMerOper> merOperList) {
		save("HfMerOper.batchInsert",merOperList);
	}
	
	/** ********************************************
	 * method name   : bacthUpdate 
	 * modified      : xuhuafeng ,  2014-3-3
	 * @see          : @see com.umpay.hfmng.dao.MerOperDao#bacthUpdate(java.util.List)
	 * ********************************************/     
	public int bacthUpdate(Map<String, Object> merOperMap) {
		return update("HfMerOper.batchUpdate", merOperMap);
	}

}
