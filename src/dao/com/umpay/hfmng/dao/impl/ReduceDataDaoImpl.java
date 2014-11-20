/** *****************  JAVA头文件说明  ****************
 * file name  :  ReduceDataDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-7-16
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.dao.ReduceDataDao;
import com.umpay.hfmng.model.HSMerSet;


/** ******************  类说明  *********************
 * class       :  ReduceDataDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Repository("reduceDataDaoImpl")
public class ReduceDataDaoImpl extends EntityOffLineDaoImpl<HSMerSet> implements ReduceDataDao {

	@SuppressWarnings("unchecked")
	public List<HSMerSet> getXEReduceData(String lastLMonth) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lastLMonth", lastLMonth);
		return (List<HSMerSet>) this.find("MerGrade.getXEReduceData", map);
	}

	@SuppressWarnings("unchecked")
	public List<HSMerSet> getMWReduceData(String lastLMonth) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lastLMonth", lastLMonth);
		return (List<HSMerSet>) this.find("MerGrade.getMWReduceData", map);
	}

}
