package com.umpay.hfmng.service;

/**
 * @ClassName: ReportOptService
 * @Description: 报备操作记录服务接口
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2014-7-19上午12:45:54
 * @tag 
 */
public interface ReportOptService {

	/**
	 * @Title: operation
	 * @Description: 报备操作记录处理
	 * @param idlist 逗号分隔id列表
	 * @param optype 操作类型
	 * @param reason 不通过理由
	 * @throws Exception
	 * @author panyouliang
	 * @date 2014-7-19 上午12:47:25
	 */
	public boolean operation(String idlist, Integer optype, String reason)throws Exception;
}
