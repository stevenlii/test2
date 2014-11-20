package com.umpay.hfmng.service;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponCode;

/**
 * @ClassName: CouponCodeService
 * @Description: 兑换码管理业务处理接口
 * @author wanyong
 * @date 2012-12-25 下午05:20:29
 */
public interface CouponCodeService {

	/**
	 * @Title: saveCouponCode
	 * @Description: 保存兑换码接口
	 * @param couponCode
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-25 下午06:09:19
	 */
	public void saveCouponCode(CouponCode couponCode) throws BusinessException, DataAccessException;

	/**
	 * @Title: exists
	 * @Description: 检查兑换码是否存在接口
	 * @param code
	 * @param merId
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-28 上午11:42:25
	 */
	public boolean exists(String code, String merId) throws BusinessException, DataAccessException;

	/**
	 * @Title: loadCouponCode
	 * @Description: 加载一个兑换码实体接口
	 * @param code
	 *            加密后的兑换码
	 * @param merId
	 *            商户ID
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-5-31 下午4:22:24
	 */
	public CouponCode loadCouponCode(String code, String merId) throws BusinessException, DataAccessException;

	/**
	 * @Title: modifyCouponInf
	 * @Description: 修改兑换码接口
	 * @param couponCode
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-6-5 上午11:06:37
	 */
	public void modifyCouponCode(CouponCode couponCode) throws BusinessException, DataAccessException;
}
