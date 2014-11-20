package com.umpay.hfmng.service;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.exception.BusinessException;

/**
 * @ClassName: ParameterLoadService
 * @Description: 系统及业务参数缓存加载服务接口
 * @author wanyong
 * @date 2013-1-6 下午09:25:35
 */
public interface ParameterLoadService {

	/**
	 * @Title: loadAll
	 * @Description: 加载所有参数接口
	 * @param
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-6 下午09:42:35
	 */
	public void loadAll() throws BusinessException, DataAccessException;
}
