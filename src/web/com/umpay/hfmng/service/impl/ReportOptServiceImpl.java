package com.umpay.hfmng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.dao.ReportDao;
import com.umpay.hfmng.service.ReportOptService;
import com.umpay.uniquery.util.StringUtil;

/**
 * @ClassName: ReportOptServiceImpl
 * @Description: 报备操作记录服务实现类
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2014-7-19上午12:49:12
 * @tag
 */
@Service
public class ReportOptServiceImpl implements ReportOptService {
	@Autowired
	private ReportDao reportDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.umpay.hfmng.service.ReportOptService#operation(java.lang.String,
	 * java.lang.Integer, java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean operation(String idlist, Integer optype, String reason) throws Exception {
		StringBuilder idStr = new StringBuilder("(");
		if (idlist.endsWith(",")) {
			idlist = idlist.substring(0, idlist.length() - 1);
		}
		String[] idArr = idlist.split(",");
		for (String id : idArr) {
			idStr.append("'" + id + "',");
		}
		int length = idStr.length();
		if (length > 1) {
			idStr.setLength(length - 1);
			idStr.append(")");
		}
		if (StringUtil.isEmpty(reason)) {
			reason = "";
		}
		String userID = LoginUtil.getUser().getId();
		if (StringUtil.isEmpty(userID)) {
			throw new Exception("用户未登陆");
		}
		boolean result1 = reportDao.operation(idStr.toString(), optype, reason, userID);
		boolean result2 = reportDao.updateReport(idStr.toString(), optype, userID);
		return result1 && result2;
	}

}
