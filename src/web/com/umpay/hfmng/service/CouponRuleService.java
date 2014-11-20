package com.umpay.hfmng.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponCode;
import com.umpay.hfmng.model.CouponRMG;
import com.umpay.hfmng.model.CouponRule;

/**
 * @ClassName: CouponRuleService
 * @Description: 兑换券规则业务处理接口
 * @author wanyong
 * @date 2012-12-16 下午10:00:50
 */
public interface CouponRuleService {

	/**
	 * @Title: saveCouponAudit
	 * @Description: 保存待审核兑换券规则接口
	 * @param
	 * @param couponRule
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-4 上午10:43:09
	 */
	public void saveCouponAudit(CouponRule couponRule, List<CouponRMG> list)
			throws BusinessException, DataAccessException;

	/**
	 * @Title: load
	 * @Description: 根据规则ID获取兑换券规则实体接口
	 * @param
	 * @param ruleId
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-4 上午10:43:25
	 */
	public CouponRule load(String ruleId) throws BusinessException,
			DataAccessException;

	/**
	 * @Title: modifyCouponRule
	 * @Description: 修改兑换券规则实体接口
	 * @param
	 * @param couponRule
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-4 上午10:43:42
	 */
	public void modifyCouponRule(CouponRule couponRule)
			throws BusinessException, DataAccessException;
	
	public boolean updateCouponRule(CouponRule couponRule, List<CouponCode> list)throws Exception;
}
