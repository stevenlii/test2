package com.umpay.hfmng.common;


/**
 * 区分值常量存储类
 * @company:    UMPAY
 * @author:     Yangwr
 * @date:       Aug 25, 2009 10:45:55 AM 
 * @version     V1.0
 */
public interface DivisionValue {
	
	/**
	 * 审核状态
	 */
	public interface CheckStatus{
		//待审核
		short ST_WAIT = 0;
		//审核通过
		short ST_OK = 1;
		//审核未通过
		short ST_NO = 2;
	}
}
