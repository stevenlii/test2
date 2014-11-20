package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.ParaDao;
import com.umpay.hfmng.model.Para;

@Repository("paraDaoImpl")
public class ParaDaoImpl extends EntityDaoImpl<Para> implements ParaDao {

	public void inserPara(Para para) {
		this.save("Para.insert", para);
	}

	public void updatePara(Para para) {
		this.update("Para.update", para);
	}

	public Para getPara(String paraType, String paraCode) {
		Map<String, String> pkMap = new HashMap<String, String>();
		pkMap.put("paraType", paraType);
		pkMap.put("paraCode", paraCode);
		return (Para) this.get("Para.get", pkMap);
	}

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

	@SuppressWarnings("unchecked")
	public List<Para> getTypes(String paraType) {
		Map<String, String> pkMap = new HashMap<String, String>();
		pkMap.put("paraType", paraType);
		return this.find("Para.getTypes", pkMap);
	}
}
