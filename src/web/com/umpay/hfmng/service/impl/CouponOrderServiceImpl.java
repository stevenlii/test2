/**
 * @ClassName: CouponOrderServiceImpl
 * @Description: TODO
 * @author panyouliang
 * @date 2013-1-14 下午12:01:43
 */
package com.umpay.hfmng.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.dao.CouponGcOrderDao;
import com.umpay.hfmng.dao.CouponOrderDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponGcOrder;
import com.umpay.hfmng.model.CouponOrder;
import com.umpay.hfmng.service.CouponOrderService;

/**
 * @author MARCO_PAN
 * 
 */
@Service
public class CouponOrderServiceImpl implements CouponOrderService {
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private CouponOrderDao couponOrderDaoImpl;

	@Autowired
	private CouponGcOrderDao couponGcOrderDao;

	/**
	 * @Title: get
	 * @Description: TODO
	 * @param
	 * @param orderId
	 * @return
	 * @author panyouliang
	 * @date 2013-1-21 上午09:48:11
	 */
	public CouponOrder get(String orderId) {
		log.info("获取订单详情Service orderId:" + orderId);
		CouponOrder order = couponOrderDaoImpl.get(orderId);
		/** 该部分更新为异步获取 2013-01-20 wanyong **/
		/*
		 * if (order != null) { List<CouponOrderItem> items =
		 * couponOrderItemDaoImpl.list(orderId); order.setItems(items); }
		 */
		return order;
	}

	/**
	 * @Title: loadCouponGcOrder
	 * @Description: 根据ID获取取码订单记录业务处理实现
	 * @param
	 * @param orderId
	 * @return
	 * @author wanyong
	 * @date 2013-4-3 上午10:56:54
	 */
	public CouponGcOrder loadCouponGcOrder(String orderId) throws BusinessException, DataAccessException {
		CouponGcOrder couponGcOrder = couponGcOrderDao.getCouponGcOrder(orderId);
		if (couponGcOrder != null)
			couponGcOrder.trim();
		return couponGcOrder;
	}
}
