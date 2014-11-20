package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.CouponMerTelDao;
import com.umpay.hfmng.model.CouponMerTel;

/**
 * @ClassName: CouponMerTelDaoImpl
 * @Description: 商户兑换电话数据库处理实现类
 * @version: 1.0
 * @author: wanyong
 * @Create: 2013-6-5
 */
@Repository("couponMerTelDaoImpl")
public class CouponMerTelDaoImpl extends EntityDaoImpl<CouponMerTel> implements CouponMerTelDao {

	/**
	 * @Title: getCouponMerTel
	 * @Description: 根据ID获取兑换电话信息
	 * @param merTelId
	 * @return
	 * @author wanyong
	 * @date 2013-6-5 下午5:26:07
	 */
	public CouponMerTel getCouponMerTel(String merTelId) {
		Map<String, String> pkMap = new HashMap<String, String>();
		pkMap.put("merTelId", merTelId);
		return (CouponMerTel) super.get("CouponMerTel.get", pkMap);
	}

	/**
	 * @Title: insertCouponMerTel
	 * @Description: 新增商户兑换电话信息
	 * @param couponMerTel
	 * @author wanyong
	 * @date 2013-6-5 下午5:26:19
	 */
	public void insertCouponMerTel(CouponMerTel couponMerTel) {
		super.save("CouponMerTel.insert", couponMerTel);
	}

	/**
	 * @Title: updateCouponMerTel
	 * @Description: 更新商户兑换电话信息
	 * @param couponMerTel
	 * @return
	 * @author wanyong
	 * @date 2013-6-5 下午5:26:42
	 */
	public int updateCouponMerTel(CouponMerTel couponMerTel) {
		return super.update("CouponMerTel.update", couponMerTel);
	}

	/**
	 * @Title: findCouponMerTelCount
	 * @Description: 根据实体属性查询记录数
	 * @param couponMerTel
	 * @return
	 * @author wanyong
	 * @date 2013-7-3 下午2:19:33
	 */
	@SuppressWarnings("unchecked")
	public int findCouponMerTelCount(CouponMerTel couponMerTel) {
		Map<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.put("merId", couponMerTel.getMerId());
		whereMap.put("linkTel", couponMerTel.getLinkTel());
		Map<String, Integer> reMap = (HashMap<String, Integer>) super.get("CouponMerTel.find_count", whereMap);
		return reMap.get("NUM");
	}

}
