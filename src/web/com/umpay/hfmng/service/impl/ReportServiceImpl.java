package com.umpay.hfmng.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.dao.ReportDao;
import com.umpay.hfmng.model.ReportInf;
import com.umpay.hfmng.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
	
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private ReportDao reportDao;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addBackUpData(String backupid, String userid) {
		// 新增预备操作数据
		reportDao.addBackUpData(backupid, userid);
		// 更新报备信息表报备状态为：预备
		reportDao.updateBackupStatByBackupId(backupid, 9);
	}

	/** ********************************************
	 * method name   : queryForBank 
	 * modified      : xuhuafeng ,  2014-9-23
	 * @see          : @see com.umpay.hfmng.service.ReportService#queryForBank()
	 * ********************************************/     
	public Map<String, String> queryForBank() throws Exception {
		List<ReportInf> reportList = reportDao.queryForBank();
		Map<String, String> map = new HashMap<String, String>();
		for(ReportInf report : reportList){
			map.put(report.getMerId().trim()+"-"+report.getGoodsId().trim()+"-"+report.getBankId().trim(), report.getBackupId().trim());
		}
		return map;
	}
	
	/** ********************************************
	 * method name   : insertBackupBank 
	 * modified      : xuhuafeng ,  2014-10-8
	 * @see          : @see com.umpay.hfmng.service.ReportService#insertBackupBank()
	 * ********************************************/     
	public String insertBackupBank(ReportInf report) throws Exception {
		String backupId = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())
			+ SequenceUtil.getInstance().getSequence(Const.SEQ_FILENAME_REPORTINF, 10);
		report.setBackupId(backupId);
		reportDao.insertReportInf(report);
		log.info("新增商户商品报备信息成功"+report);
		return backupId;
	}

}
