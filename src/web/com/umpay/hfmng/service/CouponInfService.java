package com.umpay.hfmng.service;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponInf;

/**
 * @ClassName: CouponInfService
 * @Description: 兑换券管理业务处理接口
 * @author wanyong
 * @date 2013-1-9 下午10:41:55
 */
public interface CouponInfService {

	/**
	 * @Title: saveCouponAudit
	 * @Description: 保存待审核兑换券接口
	 * @param couponInf
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-22 下午05:50:32
	 */
	public void saveCouponAudit(CouponInf couponInf)
			throws BusinessException, DataAccessException;

	/**
	 * @Title: modifyCouponInf
	 * @Description: 修改兑换券信息接口
	 * @param couponInf
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-22 下午05:51:26
	 */
	public void modifyCouponInf(CouponInf couponInf) throws BusinessException,
			DataAccessException;

	/**
	 * @Title: saveCouponInf
	 * @Description: 保存兑换券实体接口
	 * @param couponInf
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-22 下午05:52:02
	 */
	public void saveCouponInf(CouponInf couponInf) throws BusinessException,
			DataAccessException;

	/**
	 * @Title: loadCouponInf
	 * @Description: 根据兑换券ID加载兑换券信息接口
	 * @param
	 * @param couponId
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-22 下午05:53:15
	 */
	public CouponInf loadCouponInf(String couponId) throws BusinessException,
			DataAccessException;
}
