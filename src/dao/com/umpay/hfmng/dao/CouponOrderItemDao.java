/**
 * @ClassName: CouponOrderItemDao
 * @Description: TODO
 * @author panyouliang
 * @date 2013-1-14 下午2:32:11
 */
package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CouponOrderItem;

/**
 * @author MARCO_PAN
 *
 */
public interface CouponOrderItemDao extends EntityDao<CouponOrderItem> {
	/**
	 * @Title: list
	 * @Description: 根据订单号,获取订单项明细
	 * @param orderId 订单号
	 * @return 订单项明细
	 * @author panyouliang
	 * @date 2013-1-14 下午2:32:56
	 */
	public List<CouponOrderItem> list(String orderId);
}
