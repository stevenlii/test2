package com.umpay.hfmng.dao.impl;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.CouponLogDao;
import com.umpay.hfmng.model.CouponLog;

@Repository("CouponLogDaoImpl")
public class CouponLogDaoImpl extends EntityDaoImpl<CouponLog> implements CouponLogDao {

	public void addCouponLog(CouponLog log) throws Exception {
		super.insert(log);
	}
}
