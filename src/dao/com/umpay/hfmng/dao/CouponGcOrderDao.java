package com.umpay.hfmng.dao;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CouponGcOrder;

/**
 * @ClassName: CouponGcOrderDao
 * @Description: 兑换券取码订单表数据库管理接口
 * @author wanyong
 * @date 2013-4-3 上午10:28:30
 */
public interface CouponGcOrderDao extends EntityDao<CouponGcOrder> {

	/**
	 * @Title: getCouponGcOrder
	 * @Description: 根据取码订单ID获取一条取码订单数据接口
	 * @param
	 * @param orderId
	 * @return
	 * @author wanyong
	 * @date 2013-4-3 上午10:30:55
	 */
	public CouponGcOrder getCouponGcOrder(String orderId);
}
