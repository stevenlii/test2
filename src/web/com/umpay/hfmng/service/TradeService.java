package com.umpay.hfmng.service;

import java.util.Map;

/**
 * 
 * @Title: TradeService.java
 * @Package com.umpay.hfmng.service
 * @Description: 业务层相关处理
 * @author yangwr
 * @date Nov 1, 2011 10:53:25 PM
 * @version V1.0
 * modified chenwei ,  2013-7-10
 */
public interface TradeService {

	/**
	 * ********************************************
	 * method name   : xeRevoke 
	 * description   : 小额解冻请求业务层
	 * modified      : chenwei
	 * @see          : 2013-7-10
	 * ********************************************
	 */
	public String xeRevoke(Map<String,String> sendMap, String batchProv);

	public String hnDataSync(Map<String, String> sendMap);

}
