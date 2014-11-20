package com.umpay.hfmng.dao;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CouponRule;

/**
 * @ClassName: CouponRuleDao
 * @Description: 兑换券规则数据库处理接口
 * @author wanyong
 * @date 2012-12-16 下午10:02:07
 */
public interface CouponRuleDao extends EntityDao<CouponRule> {

	/**
	 * @Title: getCouponRule
	 * @Description: 根据规则ID获取兑换券规则实体接口
	 * @param
	 * @param ruleId
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午01:40:46
	 */
	public CouponRule getCouponRule(String ruleId);

	/**
	 * @Title: insertCouponRule
	 * @Description: 新增兑换券规则实体接口
	 * @param
	 * @param couponRule
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午01:49:04
	 */
	public void insertCouponRule(CouponRule couponRule);

	/**
	 * @Title: updateCouponRule
	 * @Description: 更新兑换券规则实体接口
	 * @param
	 * @param couponRule
	 * @return 影响行数
	 * @author wanyong
	 * @date 2012-12-18 下午01:49:33
	 */
	public int updateCouponRule(CouponRule couponRule);

	/**
	 * @Title: updateGoodsSum
	 * @Description: 更新兑换券规则库存接口
	 * @param
	 * @param couponRule
	 * @return
	 * @author wanyong
	 * @date 2013-1-2 下午10:47:44
	 */
	public int updateGoodsSum(CouponRule couponRule);

	/**
	 * @Title: findCouponRuleCount
	 * @Description: 根据实体属性查询记录数接口
	 * @param
	 * @param couponRule
	 * @return
	 * @author wanyong
	 * @date 2013-1-14 下午04:52:33
	 */
	public int findCouponRuleCount(CouponRule couponRule);

}
