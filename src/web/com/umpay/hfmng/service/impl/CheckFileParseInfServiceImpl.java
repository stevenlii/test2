package com.umpay.hfmng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.dao.CheckFileParseInfDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CheckFileParseInf;
import com.umpay.hfmng.service.CheckFileParseInfService;

/**
 * @ClassName: CheckFileParseInfServiceImpl
 * @Description: 对账文件解析功能任务管理业务处理类
 * @author wanyong
 * @date 2013-3-5 下午03:21:13
 */
@Service
public class CheckFileParseInfServiceImpl implements CheckFileParseInfService {

	@Autowired
	private CheckFileParseInfDao CheckFileParseInfDao;

	public void modifyCheckFileParseInf(CheckFileParseInf checkFileParseInf)
			throws BusinessException, DataAccessException {
		CheckFileParseInfDao.updateCheckFileParseInf(checkFileParseInf);
	}

	public void saveCheckFileParseInf(CheckFileParseInf checkFileParseInf)
			throws BusinessException, DataAccessException {
		// 检查任务是否存在
		int count = CheckFileParseInfDao.findCount(checkFileParseInf
				.getFileName(), checkFileParseInf.getFileType(), -1, null);
		if (count > 0) {
			throw new BusinessException("已存在该任务，不可重复添加");
		}
		CheckFileParseInfDao.insertCheckFileParseInf(checkFileParseInf);
	}

	public void deleteCheckFileParseInf(CheckFileParseInf checkFileParseInf)
			throws BusinessException, DataAccessException {
		CheckFileParseInfDao.deleteCheckFileParseInf(checkFileParseInf);
	}

}
