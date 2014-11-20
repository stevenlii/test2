package com.umpay.hfmng.service;

import java.util.Map;

import com.umpay.hfmng.model.ReportInf;

public interface ReportService {

	void addBackUpData(String backupid, String userid);
	/**
	 * ********************************************
	 * method name   : queryForBank 
	 * description   : 查询分省的报备状态map<merid-goodsid-bankid, backupid-backstat>
	 * @return       : Map<String,String>
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xuhuafeng ,  2014-9-23  下午03:59:39
	 * *******************************************
	 */
	public Map<String, String> queryForBank() throws Exception;
	/**
	 * ********************************************
	 * method name   : insertBackupBank 
	 * description   : 新增一条报备信息并返回backupId
	 * @return       : String
	 * @param        : @param report
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xuhuafeng ,  2014-10-8  下午04:39:08
	 * *******************************************
	 */
	public String insertBackupBank(ReportInf report) throws Exception;

}
