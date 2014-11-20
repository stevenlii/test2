package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.CouponGcOrderDao;
import com.umpay.hfmng.model.CouponGcOrder;

/**
 * @ClassName: CouponGcOrderImpl
 * @Description: 兑换券取码订单表数据库管理实现类
 * @author wanyong
 * @date 2013-4-3 上午10:33:30
 */
@Repository("couponGcOrderImpl")
public class CouponGcOrderImpl extends EntityDaoImpl<CouponGcOrder> implements CouponGcOrderDao {

	/**
	 * @Title: getCouponGcOrder
	 * @Description: 根据取码订单ID获取一条取码订单数据实现
	 * @param
	 * @param orderId
	 * @return
	 * @author wanyong
	 * @date 2013-4-3 上午10:30:55
	 */
	public CouponGcOrder getCouponGcOrder(String orderId) {
		Map<String, String> pkMap = new HashMap<String, String>();
		pkMap.put("orderId", orderId);
		return (CouponGcOrder) super.get("CouponGcOrder.get", pkMap);
	}

}
