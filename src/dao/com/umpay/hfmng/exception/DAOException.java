package com.umpay.hfmng.exception;

import org.springframework.dao.DataAccessException;
/**
 * ******************  类说明  *********************
 * class       :  DAOException
 * @author     :  yangwr
 * @version    :  1.0  
 * description :  DAO异常                     
 * ***********************************************
 */
public class DAOException extends DataAccessException{

	public DAOException(String msg) {
		super(msg);
	}
	
	public DAOException(String msg,Throwable e) {
		super(msg,e);
	}
}
