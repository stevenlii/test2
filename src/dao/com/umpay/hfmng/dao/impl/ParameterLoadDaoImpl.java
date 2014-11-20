package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.ParameterLoadDao;
import com.umpay.hfmng.model.Para;

/**
 * @ClassName: ParameterLoadDaoImpl
 * @Description: 该DAO实现类不纳入DAO过滤器管理
 * @author wanyong
 * @date 2013-1-15 下午04:56:22
 */
@Repository("parameterLoadDaoImplNoInter")
public class ParameterLoadDaoImpl extends EntityDaoImpl<Para> implements
		ParameterLoadDao {

	/**
	 * @Title: findAllParas
	 * @Description: 根据系统编号查询所有参数
	 * @param
	 * @param sysId
	 * @return
	 * @author wanyong
	 * @date 2013-1-7 下午09:41:33
	 */
	@SuppressWarnings("unchecked")
	public List<Para> findAllParas(String sysId) {
		Map<String, String> pkMap = new HashMap<String, String>();
		pkMap.put("sysId", sysId);
		return (List<Para>) this.find("Para.all", pkMap);
	}
}
