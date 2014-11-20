package com.umpay.hfmng.dao.impl;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.dao.ReportDao;
import com.umpay.hfmng.model.HfMerOper;
import com.umpay.hfmng.model.ReportInf;

/**
 * @ClassName: ReportDaoImpl
 * @Description: 商户商品报备信息数据操作实现类
 * @version: 1.0
 * @author: wanyong
 * @Create: 2014-7-21
 */
@Repository("ReportDaoImpl")
public class ReportDaoImpl extends EntityDaoImpl<HfMerOper> implements ReportDao {

	public void addBackUpData(String backupid, String userid) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("backupopid", new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())
				+ SequenceUtil.getInstance().getSequence(Const.SEQ_BACKUPID, 10));
		para.put("backupid", backupid);
		para.put("userid", userid);
		// 先判断操作表中是否已经报备过。报备过的，更新下时间和操作人，没报备过的查新的报备记录
		if (this.update("report.updateBackupOp", para) < 1) {
			this.update("report.insertBackupOp", para);
		}
	}

	public int updateBackupStatByBackupId(String backupId, int backupStat) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("backupId", backupId);
		para.put("backupStat", backupStat);
		return this.update("report.updateReportInfBackupStatByBackupId", para);
	}

	public boolean operation(String idStr, Integer optype, String reason, String userID) {
		try {
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("records", idStr);
			para.put("optype", optype);
			para.put("reason", reason);
			para.put("user", userID);
			para.put("updatetime", new Date());
			if (optype.intValue() == 2 || optype.intValue() == 4) {
				this.update("report.update24", para);
			} else if (optype.intValue() == 5 || optype.intValue() == 6) {
				this.update("report.update56", para);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * @Title: insertReportInf
	 * @Description: 新增商户商品报备信息实现
	 * @param reportInf
	 * @author wanyong
	 * @date 2014-7-21 下午4:11:03
	 */
	public void insertReportInf(ReportInf reportInf) {
		super.save("report.insertReportInf", reportInf);
	}

	public boolean updateReport(String idStr, Integer optype, String userID) {
		try {
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("records", idStr);
			para.put("optype", optype);
			para.put("user", userID);
			para.put("updatetime", new Date());
			this.update("report.updateInfo", para);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/** ********************************************
	 * method name   : queryForBank 
	 * modified      : xuhuafeng ,  2014-9-23
	 * @see          : @see com.umpay.hfmng.dao.ReportDao#queryForBank()
	 * ********************************************/     
	@SuppressWarnings("unchecked")
	public List<ReportInf> queryForBank() throws Exception {
		return super.find("report.queryForBank");
	}

}
