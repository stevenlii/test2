/** *****************  JAVA头文件说明  ****************
 * file name  :  UpServiceDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-9-29
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.UPServiceDao;
import com.umpay.hfmng.model.UPService;


/** ******************  类说明  *********************
 * class       :  UpServiceDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Repository
public class UPServiceDaoImpl extends EntityDaoImpl<UPService> implements UPServiceDao {

	/** ********************************************
	 * method name   : updateState 
	 * modified      : xuhuafeng ,  2014-9-30
	 * @see          : @see com.umpay.hfmng.dao.UpServiceDao#updateState(com.umpay.hfmng.model.UpService)
	 * ********************************************/     
	public int updateState(UPService upService) throws Exception {
		return this.update("UPService.updateState", upService);
	}

}
