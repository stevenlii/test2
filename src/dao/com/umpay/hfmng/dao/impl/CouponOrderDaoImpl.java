/**
 * @ClassName: CouponOrderDaoImpl
 * @Description: 兑换劵数据访问实现类
 * @author panyouliang
 * @date 2013-1-14 上午11:56:32
 */
package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.base.PageBean;
import com.umpay.hfmng.dao.CouponOrderDao;
import com.umpay.hfmng.model.CouponOrder;
import com.umpay.hfmng.model.CouponOrderItem;
@Repository("couponOrderDaoImpl")
public class CouponOrderDaoImpl extends EntityDaoImpl<CouponOrder> implements
		CouponOrderDao {
	/* (non-Javadoc)
	 * @see com.umpay.hfmng.dao.CouponOrderDao#get(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public CouponOrder get(String orderId) {
		return (CouponOrder) super.get("CouponOrder.detail", orderId);
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#findBy(java.lang.Object)
	 */
	public List<CouponOrder> findBy(CouponOrder t) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#pagedFindBy(com.umpay.hfmng.base.PageBean, java.lang.Object)
	 */
	public void pagedFindBy(PageBean pageBean, CouponOrder t)
			throws DataAccessException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#insert(java.lang.Object)
	 */
	public void insert(CouponOrder t) throws DataAccessException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#update(java.lang.Object)
	 */
	public int update(CouponOrder t) throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.umpay.hfmng.base.EntityDao#delete(java.lang.Object)
	 */
	public void delete(CouponOrder t) throws DataAccessException {
		// TODO Auto-generated method stub

	}

	
}
