package com.umpay.hfmng.common;

import java.util.HashMap;
import java.util.Map;
/**
 * 区分值名称
 * @company:    UMPAY
 * @author:     Yangwr
 * @date:       Oct 22, 2009 4:08:30 PM 
 * @version     V1.0
 */
public class DivisionName {
	//订单服务状态
	private static final Map<Short,String> CheckStatusName = new HashMap<Short,String>(); 
	static{
		CheckStatusName.put(DivisionValue.CheckStatus.ST_WAIT, "待审核");
		CheckStatusName.put(DivisionValue.CheckStatus.ST_OK, "审核通过");
		CheckStatusName.put(DivisionValue.CheckStatus.ST_NO, "审核未通过");
	}
	/**
	 * ********************************************
	 * method name   : getCheckStatusName 
	 * description   : 取得审核状态名称
	 * @return       : 
	 * *******************************************
	 */
	public static String getCheckStatusName(Short divValue){
		return CheckStatusName.get(divValue);
	}

}
