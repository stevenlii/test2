/** *****************  JAVA头文件说明  ****************
 * file name  :  MerOperDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-3-3
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.HfMerOper;


/** ******************  类说明  *********************
 * class       :  MerOperDao
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface MerOperDao extends EntityDao<HfMerOper> {
	
	public void bacthInsert(List<HfMerOper> merOperList);
	
	public int bacthUpdate(Map<String, Object> merOperMap);

}
