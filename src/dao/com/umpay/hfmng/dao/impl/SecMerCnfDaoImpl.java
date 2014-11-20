/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-24
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.SecMerCnfDao;
import com.umpay.hfmng.model.SecMerCnf;


@Repository
public class SecMerCnfDaoImpl extends EntityDaoImpl<SecMerCnf> implements SecMerCnfDao {

	@SuppressWarnings("unchecked")
	public List<SecMerCnf> findBySubMerId(String subMerId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("subMerId", subMerId);
		return this.find("SecMerCnf.findBySubMerId", map);
	}
}
