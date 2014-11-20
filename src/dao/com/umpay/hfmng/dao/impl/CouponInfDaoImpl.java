package com.umpay.hfmng.dao.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.CouponInfDao;
import com.umpay.hfmng.model.CouponInf;

/**
 * @ClassName: CouponInfDaoImpl
 * @Description: 兑换券信息数据库操作实现类
 * @author wanyong
 * @date 2012-11-20 下午01:57:40
 */
@Repository("couponInfDaoImpl")
public class CouponInfDaoImpl extends EntityDaoImpl<CouponInf> implements
		CouponInfDao {

	/**
	 * @Title: save
	 * @Description: 保存兑换券实体
	 * @param
	 * @param couponInf
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-11-20 下午02:01:05
	 */
	public void insertCouponInf(CouponInf couponInf) {
		super.save("CouponInf.insert", couponInf);
	}

	/**
	 * @Title: updateCouponInf
	 * @Description: 更新兑换券实体
	 * @param
	 * @param couponInf
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 上午11:27:52
	 */
	public int updateCouponInf(CouponInf couponInf) {
		return super.update("CouponInf.updateCouponInf", couponInf);
	}

	/**
	 * @Title: getCouponInf
	 * @Description: 根据ID获取兑换券实体
	 * @param
	 * @param couponId
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 上午11:27:17
	 */
	public CouponInf getCouponInf(String couponId) {
		return (CouponInf) super.get("CouponInf.get", couponId);
	}

	/**
	 * @Title: getCouponInfs
	 * @Description: 获取所有兑换券实体
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午06:12:15
	 */
	@SuppressWarnings("unchecked")
	public List<CouponInf> getCouponInfs() {
		return (List<CouponInf>) super.find("CouponInf.getCouponInfs");
	}

}
