/**
 * @ClassName: CouponOrderItemDaoImpl
 * @Description: 订单项明细数据访问实现类
 * @author panyouliang
 * @date 2013-1-14 下午2:34:08
 */
package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.base.PageBean;
import com.umpay.hfmng.dao.CouponOrderItemDao;
import com.umpay.hfmng.model.CouponOrderItem;

@Repository("couponOrderItemDaoImpl")
public class CouponOrderItemDaoImpl extends EntityDaoImpl<CouponOrderItem>
		implements CouponOrderItemDao {

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.dao.CouponOrderItemDao#list(java.lang.String)
	 */
	public List<CouponOrderItem> list(String orderId) {
		Map params = new HashMap();
		params.put("orderId", orderId);
		List<CouponOrderItem> items = super.find("CouponOrder.items", orderId);
		return items;
	}
	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#findBy(java.lang.Object)
	 */
	public List<CouponOrderItem> findBy(CouponOrderItem t)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#pagedFindBy(com.umpay.hfmng.base.PageBean, java.lang.Object)
	 */
	public void pagedFindBy(PageBean pageBean, CouponOrderItem t)
			throws DataAccessException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#insert(java.lang.Object)
	 */
	public void insert(CouponOrderItem t) throws DataAccessException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#update(java.lang.Object)
	 */
	public int update(CouponOrderItem t) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#delete(java.lang.Object)
	 */
	public void delete(CouponOrderItem t) throws DataAccessException {
		// TODO Auto-generated method stub

	}

	

}
