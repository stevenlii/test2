package com.umpay.hfmng.service;

import com.umpay.hfmng.model.CouponLog;
import com.umpay.hfmng.model.CouponMerSet;

public interface CouponLogService {

	public void addLog(CouponLog log) throws Exception;
	public void addLog(CouponMerSet couponmer, String opType, String opData, String result);
}
