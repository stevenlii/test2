package com.umpay.hfmng.service;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponMerTel;

/**
 * @ClassName: CouponMerTelService
 * @Description: 商户兑换电话管理业务处理接口
 * @version: 1.0
 * @author: wanyong
 * @Create: 2013-6-5
 */
public interface CouponMerTelService {

	/**
	 * @Title: saveCouponMerTel
	 * @Description: 保存商户兑换电话信息接口
	 * @param couponMerTel
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-6-5 下午5:39:13
	 */
	public void saveCouponMerTel(CouponMerTel couponMerTel) throws BusinessException, DataAccessException;

	/**
	 * @Title: modifyCouponMerTel
	 * @Description: 修改商户兑换电话信息接口
	 * @param couponMerTel
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-6-5 下午5:39:38
	 */
	public void modifyCouponMerTel(CouponMerTel couponMerTel) throws BusinessException, DataAccessException;

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
	public CouponMerTel loadCouponMerTel(String merTelId) throws BusinessException, DataAccessException;

}
