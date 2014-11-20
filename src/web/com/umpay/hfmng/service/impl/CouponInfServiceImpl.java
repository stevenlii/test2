package com.umpay.hfmng.service.impl;

import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.CouponInfDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.service.CouponInfService;

/**
 * @ClassName: CouponInfServiceImpl
 * @Description: 兑换券管理业务处理
 * @author wanyong
 * @date 2013-1-9 下午10:41:28
 */
@Service
public class CouponInfServiceImpl implements CouponInfService {
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private AuditDao auditDao;

	@Autowired
	private CouponInfDao couponInfDao;

	/**
	 * @Title: saveCouponAudit
	 * @Description: 保存待审核兑换券
	 * @param couponInf
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-22 下午05:54:47
	 */
	public void saveCouponAudit(CouponInf couponInf) throws BusinessException,
			DataAccessException {
		String jsonString = JsonHFUtil.getJsonArrStrFrom(couponInf);
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_COUPON_INF");
		audit.setModData(jsonString);
		audit.setState(Const.AUDIT_STATE_WAIT); // 审核状态: 0待审核 1审核不通过 2审核通过
		audit.setAuditType("1");
		audit.setCreator(couponInf.getSubmitUser()); // 创建人 是当前登录用户
		audit.setIxData2(couponInf.getState().toString());
		audit.setModTime(null); // 修改时间设置为null
		audit.setResultDesc("");
		audit.setIxData(couponInf.getCouponId());
		auditDao.insert(audit);
		log.info("审核表中成功插入兑换券信息" + audit.toString());
	}

	/**
	 * @Title: modifyCouponInf
	 * @Description: 修改兑换券信息
	 * @param couponInf
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-22 下午05:55:34
	 */
	public void modifyCouponInf(CouponInf couponInf) throws BusinessException,
			DataAccessException {
		Audit audit = new Audit();
		CouponInf checkCouponInf = loadCouponInf(couponInf.getCouponId());
		if (checkCouponInf == null) {
			// 该兑换券属于新增但未审核，继续修改的，依然为新增审核类型
			audit.setAuditType(Const.AUDIT_TYPE_ADD);
			couponInf.setInTime(new Timestamp(System.currentTimeMillis())); // 重新设置插入时间
			couponInf.setModTime(new Timestamp(System.currentTimeMillis())); // 重新设置更新时间
		} else
			audit.setAuditType(Const.AUDIT_TYPE_MOD);
		String jsonString = JsonHFUtil.getJsonArrStrFrom(couponInf);
		audit.setTableName("UMPAY.T_COUPON_INF");
		audit.setModData(jsonString);
		audit.setState(Const.AUDIT_STATE_WAIT); // 审核状态 0：待审核；1：审核通过；2：审核不通过
		audit.setCreator(couponInf.getSubmitUser()); // 创建人是当前登录用户
		audit.setIxData2(couponInf.getState().toString());
		audit.setResultDesc("");
		audit.setIxData(couponInf.getCouponId());
		auditDao.insert(audit); // 入审核表
		log.info("入审核表成功, 数据" + audit.toString());
	}

	/**
	 * @Title: saveCouponInf
	 * @Description: 保存兑换券实体
	 * @param couponInf
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-22 下午05:55:51
	 */
	public void saveCouponInf(CouponInf couponInf) throws BusinessException,
			DataAccessException {
		// 预留接口
	}

	/**
	 * @Title: loadCouponInf
	 * @Description: 根据兑换券ID加载兑换券信息
	 * @param couponId
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-22 下午05:56:21
	 */
	public CouponInf loadCouponInf(String couponId) throws BusinessException,
			DataAccessException {
		CouponInf couponInf = couponInfDao.getCouponInf(couponId);
		if (couponInf != null)
			couponInf.trim();
		return couponInf;
	}
}
