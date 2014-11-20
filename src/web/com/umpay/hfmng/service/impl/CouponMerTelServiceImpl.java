package com.umpay.hfmng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.dao.CouponMerTelDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponMerTel;
import com.umpay.hfmng.service.CouponMerTelService;

/**
 * @ClassName: CouponMerTelServiceImpl
 * @Description: 商户兑换电话管理业务处理实现类
 * @version: 1.0
 * @author: wanyong
 * @Create: 2013-6-5
 */
@Service
public class CouponMerTelServiceImpl implements CouponMerTelService {

	@Autowired
	private CouponMerTelDao couponMerTelDao;

	/**
	 * @Title: saveCouponMerTel
	 * @Description: 保存商户兑换电话信息接口
	 * @param couponMerTel
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-6-5 下午5:39:13
	 */
	public void saveCouponMerTel(CouponMerTel couponMerTel) throws BusinessException, DataAccessException {
		// 验证唯一性，组合唯一字段：【商户】+【兑换电话】
		if (couponMerTelDao.findCouponMerTelCount(couponMerTel) > 0)
			throw new BusinessException("该商户已录入此兑换电话！");
		couponMerTelDao.insertCouponMerTel(couponMerTel);
	}

	/**
	 * @Title: modifyCouponMerTel
	 * @Description: 修改商户兑换电话信息接口
	 * @param couponMerTel
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-6-5 下午5:39:38
	 */
	public void modifyCouponMerTel(CouponMerTel couponMerTel) throws BusinessException, DataAccessException {
		couponMerTelDao.updateCouponMerTel(couponMerTel);
	}

	/**
	 * @Title: loadCouponMerTel
	 * @Description: 加载商户兑换电话信息接口
	 * @param merTelId
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-6-5 下午5:40:00
	 */
	public CouponMerTel loadCouponMerTel(String merTelId) throws BusinessException, DataAccessException {
		CouponMerTel couponMerTel = couponMerTelDao.getCouponMerTel(merTelId);
		if (couponMerTel != null)
			couponMerTel.trim();
		return couponMerTel;
	}

}
