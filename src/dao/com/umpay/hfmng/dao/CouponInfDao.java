package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CouponInf;

/**
 * @ClassName: CouponInfDao
 * @Description: 兑换券信息数据库操作接口
 * @author wanyong
 * @date 2012-11-20 下午01:56:33
 */
public interface CouponInfDao extends EntityDao<CouponInf> {

	/**
	 * @Title: insertCouponInf
	 * @Description: 新增兑换券实体
	 * @param couponInf
	 * @author wanyong
	 * @date 2012-11-21 上午10:00:30
	 */
	public void insertCouponInf(CouponInf couponInf);

	/**
	 * @Title: updateCouponInf
	 * @Description: 更新兑换券实体
	 * @param
	 * @param couponInf
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午01:52:35
	 */
	public int updateCouponInf(CouponInf couponInf);

	/**
	 * @Title: getCouponInf
	 * @Description: 根据ID获取兑换券实体
	 * @param
	 * @param couponId
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午01:52:47
	 */
	public CouponInf getCouponInf(String couponId);

	/**
	 * @Title: getCouponInfs
	 * @Description: 获取所有兑换券实体接口
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午05:58:14
	 */
	public List<CouponInf> getCouponInfs();
}
