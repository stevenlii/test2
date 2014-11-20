package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.Para;

public interface ParameterLoadDao extends EntityDao<Para> {

	/**
	 * @Title: findAllParas
	 * @Description: 根据系统编号查询所有参数接口
	 * @param
	 * @param sysId
	 * @return
	 * @author wanyong
	 * @date 2013-1-7 下午09:42:50
	 */
	public List<Para> findAllParas(String sysId);
}
