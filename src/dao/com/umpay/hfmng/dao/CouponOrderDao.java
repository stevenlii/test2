/**
 * @ClassName: CouponOrderDao
 * @Description: 兑换劵订单数据访问接口
 * @author panyouliang
 * @date 2013-1-14 上午11:53:29
 */
package com.umpay.hfmng.dao;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CouponOrder;

public interface CouponOrderDao extends EntityDao<CouponOrder> {
	/**
	 * @Title: get
	 * @Description: 获取订单详情
	 * @param orderId 订单ID
	 * @return 订单详情信息
	 * @author panyouliang
	 * @date 2013-1-14 上午11:55:12
	 */
	public CouponOrder get(String orderId);
}
