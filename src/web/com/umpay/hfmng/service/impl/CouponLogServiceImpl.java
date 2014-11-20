package com.umpay.hfmng.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.dao.CouponLogDao;
import com.umpay.hfmng.model.CouponLog;
import com.umpay.hfmng.model.CouponMerSet;
import com.umpay.hfmng.service.CouponLogService;

@Service
public class CouponLogServiceImpl implements CouponLogService {

	@Autowired
	private CouponLogDao logDao;
	
	public void addLog(CouponLog log) throws Exception {
		log.setLogid(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())+
				SequenceUtil.getInstance().getSequence(
				Const.SEQ_FILENAME_COUPONLOG, 16));
		log.setModuser(LoginUtil.getUser().getName().trim());
		logDao.addCouponLog(log);
	}

	public void addLog(CouponMerSet couponmer, String opType, String opData, String result){
		CouponLog oplog = new CouponLog();
		oplog.setBusinessobject("UMPAY.T_COUPON_MERSET");
		oplog.setResultdesc(result);// 操作结果描述
		oplog.setIxdata1(couponmer.getStlDate()); // 结算周期
		oplog.setIxdata2(couponmer.getGoodsId() + ""); // 子商户ID
		oplog.setIxdata3(couponmer.getStlCycle() + ""); // 结算账期
		if(StringUtil.validateNull(couponmer.getAccName()) && StringUtil.validateNull(couponmer.getAccId())){
			oplog.setIxdata4(couponmer.getAccName() + "(" + couponmer.getAccId() + ")"); // 商户
		}
		oplog.setOperdata(opData);// 操作内容
		oplog.setOpertype(opType); // 操作类型
		oplog.setLogid(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())+
				SequenceUtil.getInstance().getSequence(
				Const.SEQ_FILENAME_COUPONLOG, 16));
		oplog.setModuser(LoginUtil.getUser().getName().trim());
		try {
			logDao.addCouponLog(oplog);
		} catch (Exception e) {
		}
	}
}
