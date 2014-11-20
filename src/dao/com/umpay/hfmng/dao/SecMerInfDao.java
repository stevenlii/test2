/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-24
 * *************************************************/ 

package com.umpay.hfmng.dao;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.SecMerInf;


/** ******************  类说明  *********************
 * class       :  SecMerDao
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface SecMerInfDao extends EntityDao<SecMerInf> {
	
	public SecMerInf get(String subMerId);
	
	public int updateLock(SecMerInf secMerInf) throws Exception;
	
	public int isOrNotAble(SecMerInf secMerInf) throws Exception;
	
	public int setLock(String subMerId, int modLock) throws Exception;

}
