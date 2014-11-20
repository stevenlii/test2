package com.umpay.hfmng.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.CouponCodeDao;
import com.umpay.hfmng.dao.CouponRMGDao;
import com.umpay.hfmng.dao.CouponRuleDao;
import com.umpay.hfmng.dao.GoodsBankDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.CouponCode;
import com.umpay.hfmng.model.CouponRMG;
import com.umpay.hfmng.model.CouponRule;
import com.umpay.hfmng.service.CouponRuleService;

/**
 * @ClassName: CouponRuleServiceImpl
 * @Description: 兑换券规则业务处理实现类
 * @author wanyong
 * @date 2012-12-16 下午10:10:45
 */
@Service
public class CouponRuleServiceImpl implements CouponRuleService {

	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private AuditDao auditDao;

	@Autowired
	private CouponRuleDao couponRuleDao;

	@Autowired
	private GoodsBankDao goodsBankDao;
	
	@Autowired
	private CouponCodeDao couponCodeDao;
	@Autowired
	private CouponRMGDao couponRMGDao;

	/**
	 * @Title: saveCouponAudit
	 * @Description: 保存待审核兑换券规则实现
	 * @param
	 * @param couponRule
	 * @return
	 * @author wanyong
	 * @date 2012-12-17 下午05:37:44
	 */
	public void saveCouponAudit(CouponRule couponRule, List<CouponRMG> list) throws BusinessException, DataAccessException {
		// 判断商品是否存在价格
		int openCount = goodsBankDao.findOpenCount(couponRule.getMerId(), couponRule.getGoodsId());
		if (openCount == 0) {
			throw new BusinessException("该商品没有价格，不能建立规则");
		}

		// 获取兑换券序列号
		String ruleid = "RU" + SequenceUtil.getInstance().getSequence(Const.SEQ_FILENAME_COUPONRULE, 8);
		couponRule.setRuleId(ruleid);
		String jsonString = JsonHFUtil.getJsonArrStrFrom(couponRule);
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_COUPON_RULE");
		audit.setModData(jsonString);
		audit.setState(Const.AUDIT_STATE_WAIT); // 审核状态: 0待审核 1审核不通过 2审核通过
		audit.setAuditType("1"); // 审核类型 1：新增:2：修改:3：启用:4：禁用 0:未知
		audit.setCreator(couponRule.getSubmitUser()); // 创建人 是当前登录用户
		audit.setIxData2(couponRule.getState().toString());
		audit.setModTime(null); // 修改时间设置为null
		audit.setResultDesc("");
		audit.setIxData(couponRule.getRuleId());
		auditDao.insert(audit);
		if(list != null && list.size() > 0){
			for(CouponRMG rmg : list){
				rmg.setRuleid(ruleid);
			}
			try {
				couponRMGDao.insertCouponRMGBatch(list);
			} catch (Exception e) {
				throw new BusinessException("插入RMG表异常");
			}
		}
		log.info("审核表中成功插入兑换券规则信息" + audit.toString());
	}

	/**
	 * @Title: load
	 * @Description: 根据规则ID获取兑换券规则实现
	 * @param
	 * @param ruleId
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午01:37:53
	 */
	public CouponRule load(String ruleId) throws BusinessException, DataAccessException {
		CouponRule couponRule = couponRuleDao.getCouponRule(ruleId);
		if (couponRule != null)
			couponRule.trim();
		return couponRule;
	}

	/**
	 * @Title: modifyCouponRule
	 * @Description: 修改兑换券规则实体
	 * @param
	 * @param couponRule
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-4 上午10:44:19
	 */
	public void modifyCouponRule(CouponRule couponRule) throws BusinessException, DataAccessException {
		Audit audit = new Audit();
		CouponRule checkCouponRule = load(couponRule.getRuleId());
		if (checkCouponRule == null) {
			// 该兑换券规则属于新增但未审核，继续修改的，依然为新增审核类型
			audit.setAuditType(Const.AUDIT_TYPE_ADD);
			couponRule.setInTime(new Timestamp(System.currentTimeMillis())); // 重新设置插入时间
			couponRule.setModTime(new Timestamp(System.currentTimeMillis())); // 重新设置更新时间
		} else
			audit.setAuditType(Const.AUDIT_TYPE_MOD);
		String jsonString = JsonHFUtil.getJsonArrStrFrom(couponRule);
		audit.setTableName("UMPAY.T_COUPON_RULE");
		audit.setModData(jsonString);
		audit.setState(Const.AUDIT_STATE_WAIT); // 审核状态 0：待审核；1：审核通过；2：审核不通过
		audit.setCreator(couponRule.getSubmitUser()); // 创建人是当前登录用户
		audit.setIxData(couponRule.getRuleId());
		auditDao.insert(audit); // 入审核表
		log.info("入审核表成功, 数据" + audit.toString());
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.service.CouponRuleService#updateCouponRule(com.umpay.hfmng.model.CouponRule, java.util.List)
	 */
	public boolean updateCouponRule(CouponRule couponRule, List<CouponCode> list) throws Exception {
		couponCodeDao.insertCouponCodeBatch(list);
		couponRuleDao.updateCouponRule(couponRule);
		return true;
	}
}
