package com.umpay.hfmng.dao;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CouponMerTel;

/**
 * @ClassName: CouponMerTelDao
 * @Description: 商户兑换电话信息数据库处理接口
 * @version: 1.0
 * @author: wanyong
 * @Create: 2013-6-5
 */
public interface CouponMerTelDao extends EntityDao<CouponMerTel> {

	/**
	 * @Title: getCouponMerTel
	 * @Description: 根据ID获取兑换电话信息接口
	 * @param merTelId
	 * @return
	 * @author wanyong
	 * @date 2013-6-5 下午5:26:07
	 */
	public CouponMerTel getCouponMerTel(String merTelId);

	/**
	 * @Title: insertCouponMerTel
	 * @Description: 新增商户兑换电话信息接口
	 * @param couponMerTel
	 * @author wanyong
	 * @date 2013-6-5 下午5:26:19
	 */
	public void insertCouponMerTel(CouponMerTel couponMerTel);

	/**
	 * @Title: updateCouponMerTel
	 * @Description: 更新商户兑换电话信息接口
	 * @param couponMerTel
	 * @return
	 * @author wanyong
	 * @date 2013-6-5 下午5:26:42
	 */
	public int updateCouponMerTel(CouponMerTel couponMerTel);

	/**
	 * @Title: findCouponMerTelCount
	 * @Description: 根据实体属性查询记录数接口
	 * @param couponMerTel
	 * @return
	 * @author wanyong
	 * @date 2013-7-3 下午2:19:33
	 */
	public int findCouponMerTelCount(CouponMerTel couponMerTel);

}
