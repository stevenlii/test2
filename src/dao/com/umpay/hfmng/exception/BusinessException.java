package com.umpay.hfmng.exception;

import org.springframework.dao.DataAccessException;

/**
 * @ClassName: DataException
 * @Description: 数据异常（DAO数据库层SQL级处理正常，但结果不是预期）
 * @author wanyong
 * @date 2012-12-22 下午05:35:33
 */
public class BusinessException extends DataAccessException {

	private static final long serialVersionUID = 1L;

	public BusinessException(String msg) {
		super(msg);
	}

	public BusinessException(String msg, Throwable e) {
		super(msg, e);
	}
}
