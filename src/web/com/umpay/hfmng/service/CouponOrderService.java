/**
 * @ClassName: CouponOrderService
 * @Description: 兑换劵订单服务层接口
 * @author panyouliang
 * @date 2013-1-14 上午11:59:43
 */
package com.umpay.hfmng.service;

import com.umpay.hfmng.model.CouponGcOrder;
import com.umpay.hfmng.model.CouponOrder;

public interface CouponOrderService {

	/**
	 * @Title: get
	 * @Description: 获取兑换劵订单详情
	 * @param orderId
	 *            订单ID
	 * @return 订单详情
	 * @author panyouliang
	 * @date 2013-1-14 下午12:00:35
	 */
	public CouponOrder get(String orderId);

	/**
	 * @Title: loadCouponGcOrder
	 * @Description: 根据ID获取取码订单记录业务处理接口
	 * @param
	 * @param orderId
	 * @return
	 * @author wanyong
	 * @date 2013-4-3 上午10:57:04
	 */
	public CouponGcOrder loadCouponGcOrder(String orderId);
}
