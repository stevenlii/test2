package com.umpay.hfmng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.dao.CouponCodeDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponCode;
import com.umpay.hfmng.service.CouponCodeService;

/**
 * @ClassName: CouponCodeServiceImpl
 * @Description: 兑换码管理业务处理
 * @author wanyong
 * @date 2012-12-26 下午03:46:42
 */
@Service
public class CouponCodeServiceImpl implements CouponCodeService {

	@Autowired
	private CouponCodeDao couponCodeDao;

	/**
	 * @Title: saveCouponCode
	 * @Description: 保存兑换码
	 * @param
	 * @param couponCode
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-28 上午11:17:56
	 */
	public void saveCouponCode(CouponCode couponCode) throws BusinessException, DataAccessException {
		couponCodeDao.insertCouponCode(couponCode);
	}

	/**
	 * @Title: exists
	 * @Description: 检查兑换码是否存在
	 * @param
	 * @param code
	 * @param merId
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-28 上午11:43:00
	 */
	public boolean exists(String code, String merId) throws BusinessException, DataAccessException {
		if (couponCodeDao.getCouponCodeCount(code, merId) > 0)
			return true;
		return false;
	}

	/**
	 * @Title: loadCouponCode
	 * @Description: 加载一个兑换码实体实现
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
	public CouponCode loadCouponCode(String code, String merId) throws BusinessException, DataAccessException {
		CouponCode couponCode = couponCodeDao.getCouponCode(code, merId);
		if (couponCode != null)
			couponCode.trim();
		return couponCode;
	}

	/**
	 * @Title: modifyCouponInf
	 * @Description: 修改兑换码实现
	 * @param couponCode
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-6-5 上午11:06:37
	 */
	public void modifyCouponCode(CouponCode couponCode) throws BusinessException, DataAccessException {
		couponCodeDao.updateCouponCode(couponCode);
	}
}
