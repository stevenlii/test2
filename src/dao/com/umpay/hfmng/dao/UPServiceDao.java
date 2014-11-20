/** *****************  JAVA头文件说明  ****************
 * file name  :  UpServiceDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-9-29
 * *************************************************/ 

package com.umpay.hfmng.dao;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.UPService;


/** ******************  类说明  *********************
 * class       :  UpServiceDao
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface UPServiceDao extends EntityDao<UPService> {
	
	public int updateState(UPService upService) throws Exception;

}
