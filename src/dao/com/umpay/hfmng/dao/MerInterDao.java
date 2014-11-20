/** *****************  JAVA头文件说明  ****************
 * file name  :  MerInterDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-12-19
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.MerInter;


/** ******************  类说明  *********************
 * class       :  MerInterDao
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface MerInterDao extends EntityDao<MerInter> {
	
	public MerInter get(Map<String, String> mapWhere);
	
	public int updateState(MerInter merInter) throws Exception;

}
