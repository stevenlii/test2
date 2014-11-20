/** *****************  JAVA头文件说明  ****************
 * file name  :  BusiConfDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-1-15
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.BusiConfDao;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.MerBusiConf;


/** ******************  类说明  *********************
 * class       :  BusiConfDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Repository
public class BusiConfDaoImpl extends EntityDaoImpl<MerBusiConf> implements BusiConfDao {

	/** ********************************************
	 * method name   : updateLock 
	 * modified      : xuhuafeng ,  2014-1-15
	 * @see          : @see com.umpay.hfmng.dao.BusiConfDao#updateLock(com.umpay.hfmng.model.MerBusiConf)
	 * ********************************************/
	public int updateLock(MerBusiConf merBusiConf) {
		return this.update("MerBusiConf.updateLock", merBusiConf);
	}

	@SuppressWarnings("unchecked")
	public List<Audit> findAuditingBizType(String merId) {
		Map<String,String> mapWhere=new HashMap<String, String>();
		mapWhere.put("merId", merId);
		return (List<Audit>)this.find("MerBusiConf.checkDataAdd", mapWhere);
	}

}
