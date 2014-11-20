package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.model.ReportInf;

/**
 * @ClassName: ReportDao
 * @Description: 商户商品报备信息数据库接口
 * @version: 1.0
 * @author: wanyong
 * @Create: 2014-7-21
 */
public interface ReportDao {

	void addBackUpData(String backupid, String userid);

	/**
	 * @Title: updateBackupStatByBackupId
	 * @Description: 根据报备信息ID更新报备状态
	 * @param backupId
	 * @param backupStat
	 * @return
	 * @author wanyong
	 * @date 2014-7-25 下午4:20:10
	 */
	public int updateBackupStatByBackupId(String backupId, int backupStat);

	public boolean operation(String idStr, Integer optype, String reason, String userID);

	/**
	 * @Title: insertReportInf
	 * @Description: 新增商户商品报备信息接口
	 * @param reportInf
	 * @author wanyong
	 * @date 2014-7-21 下午4:11:03
	 */
	public void insertReportInf(ReportInf reportInf);

	/**
	 * @Title: updateReport
	 * @Description: 更新INFO
	 * @param idStr
	 *            ID列表
	 * @param optype
	 *            状态
	 * @param userID
	 *            用户ID
	 * @author panyouliang
	 * @date 2014-7-25 上午9:55:53
	 */
	public boolean updateReport(String idStr, Integer optype, String userID);
	/**
	 * ********************************************
	 * method name   : queryForBank 
	 * description   : 查询分省的报备状态
	 * @return       : List<ReportInf>
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xuhuafeng ,  2014-9-23  下午03:54:22
	 * *******************************************
	 */
	public List<ReportInf> queryForBank() throws Exception;
}
