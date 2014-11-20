package com.umpay.hfmng.service;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CheckFileParseInf;

/**
 * @ClassName: CheckFileParseInfService
 * @Description: 对账文件解析管理业务处理接口
 * @author wanyong
 * @date 2013-3-5 下午03:14:33
 */
public interface CheckFileParseInfService {

	/**
	 * @Title: saveCheckFileParseInf
	 * @Description: 保存任务信息接口
	 * @param
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-3-5 下午03:15:48
	 */
	public void saveCheckFileParseInf(CheckFileParseInf checkFileParseInf)
			throws BusinessException, DataAccessException;

	/**
	 * @Title: modifyCheckFileParseInf
	 * @Description: 修改任务信息接口
	 * @param
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-3-5 下午03:17:06
	 */
	public void modifyCheckFileParseInf(CheckFileParseInf checkFileParseInf)
			throws BusinessException, DataAccessException;

	/**
	 * @Title: deleteCheckFileParseInf
	 * @Description: 删除任务信息接口
	 * @param
	 * @param CheckFileParseInf
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-3-5 下午05:28:43
	 */
	public void deleteCheckFileParseInf(CheckFileParseInf checkFileParseInf)
			throws BusinessException, DataAccessException;
}
