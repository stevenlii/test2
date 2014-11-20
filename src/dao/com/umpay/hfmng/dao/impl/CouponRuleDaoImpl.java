package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.CouponRuleDao;
import com.umpay.hfmng.model.CouponRule;

/**
 * @ClassName: CouponRuleDaoImpl
 * @Description: 兑换券规则数据库处理实现类
 * @author wanyong
 * @date 2012-12-16 下午10:11:46
 */
@Repository("couponRuleDaoImpl")
public class CouponRuleDaoImpl extends EntityDaoImpl<CouponRule> implements
		CouponRuleDao {

	/**
	 * @Title: getCouponRule
	 * @Description: 根据规则ID获取兑换券规则实体
	 * @param
	 * @param ruleId
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午01:50:29
	 */
	public CouponRule getCouponRule(String ruleId) {
		Map<String, String> pkMap = new HashMap<String, String>();
		pkMap.put("ruleId", ruleId);
		return (CouponRule) super.get("CouponRule.get", pkMap);
	}

	/**
	 * @Title: insertCouponRule
	 * @Description: 新增兑换券规则实体
	 * @param
	 * @param couponRule
	 * @return 影响行数
	 * @author wanyong
	 * @date 2012-12-18 下午01:50:49
	 */
	public void insertCouponRule(CouponRule couponRule) {
		super.save("CouponRule.insert", couponRule);
	}

	/**
	 * @Title: updateCouponRule
	 * @Description: 更新兑换券规则实体
	 * @param
	 * @param couponRule
	 * @return 影响行数
	 * @author wanyong
	 * @date 2012-12-18 下午01:51:00
	 */
	public int updateCouponRule(CouponRule couponRule) {
		return super.update("CouponRule.update", couponRule);
	}

	/**
	 * @Title: updateGoodsSum
	 * @Description: 更新兑换券规则库存
	 * @param
	 * @param couponRule
	 * @return
	 * @author wanyong
	 * @date 2013-1-2 下午10:48:25
	 */
	public int updateGoodsSum(CouponRule couponRule) {
		return super.update("CouponRule.updategoodssum", couponRule);
	}

	/**
	 * @Title: findCouponRuleCount
	 * @Description: 根据实体属性查询记录数
	 * @param
	 * @param couponRule
	 * @return
	 * @author wanyong
	 * @date 2013-1-14 下午04:54:17
	 */
	@SuppressWarnings("unchecked")
	public int findCouponRuleCount(CouponRule couponRule) {
		Map<String, Object> whereMap = new HashMap<String, Object>();
		//whereMap.put("couponId", couponRule.getCouponId());
		whereMap.put("merId", couponRule.getMerId());
		whereMap.put("goodsId", couponRule.getGoodsId());
		whereMap.put("state", couponRule.getState());
		Map<String, Integer> reMap = (HashMap<String, Integer>) super.get(
				"CouponRule.find_count", whereMap);
		return reMap.get("NUM");
	}
}
