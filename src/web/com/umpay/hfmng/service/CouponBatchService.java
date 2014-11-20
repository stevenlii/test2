package com.umpay.hfmng.service;

import java.io.InputStream;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.CouponBatch;
import com.umpay.hfmng.model.CouponRule;
import com.umpay.sso.org.User;

/**
 * @ClassName: CouponBatchService
 * @Description: 兑换码批量导入批次业务处理接口
 * @author wanyong
 * @date 2012-12-22 下午03:12:46
 */
public interface CouponBatchService {

	/**
	 * @Title: getBatchId
	 * @Description: 根据规则ID获取批次ID接口
	 * @param
	 * @param ruleId
	 * @return
	 * @author wanyong
	 * @date 2013-1-15 下午08:34:14
	 */
	public String getBatchId(String ruleId) throws BusinessException,
			DataAccessException;

	/**
	 * @Title: saveCouponBatch
	 * @Description: 保存兑换码批量导入批次信息接口
	 * @param couponBatch
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-22 下午05:57:28
	 */
	public void saveCouponBatch(CouponBatch couponBatch)
			throws BusinessException, DataAccessException;

	/**
	 * @Title: modifyCouponBatch
	 * @Description: 修改兑换码批量导入批次信息接口
	 * @param
	 * @param couponBatch
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-22 下午05:58:18
	 */
	public void modifyCouponBatch(CouponBatch couponBatch)
			throws BusinessException, DataAccessException;

	/**
	 * @Title: loadCouponBatch
	 * @Description: 加载批次信息接口
	 * @param
	 * @param batchId
	 * @return
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-29 下午04:30:47
	 */
	public CouponBatch loadCouponBatch(String batchId)
			throws BusinessException, DataAccessException;

	/**
	 * @Title: importBatchForXls
	 * @Description: Excel2003兑换码文件批量导入业务处理接口
	 * @param stream
	 *            文件流
	 * @param couponRule
	 *            所属兑换券规则
	 * @param couponBatch
	 *            批次信息，新批次导入时为NULL
	 * @param type
	 *            导入类型：0-新批次，1-旧批次追加
	 * @param errorPath
	 *            错误文件保存路径
	 * @param user
	 *            session用户
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-1 上午01:20:16
	 */
	public String importBatchForXls(InputStream stream, CouponRule couponRule,
			CouponBatch couponBatch, String type, String errorPath, User user)
			throws BusinessException, DataAccessException;

	/**
	 * @Title: importBatchForXlsx
	 * @Description: Excel2007兑换码文件批量导入业务处理接口
	 * @param stream
	 *            文件流
	 * @param couponRule
	 *            所属兑换券规则
	 * @param couponBatch
	 *            批次信息，新批次导入时为NULL
	 * @param type
	 *            导入类型：0-新批次，1-旧批次追加
	 * @param errorPath
	 *            错误文件保存路径
	 * @param user
	 *            session用户
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2013-1-1 上午01:19:24
	 */
	public String importBatchForXlsx(InputStream stream, CouponRule couponRule,
			CouponBatch couponBatch, String type, String errorPath, User user)
			throws BusinessException, DataAccessException;

	/**
	 * @Title: stopBatch
	 * @Description: 批次停止使用业务处理接口
	 * @param
	 * @param batchId
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-29 下午03:14:02
	 */
	public void stopBatch(String batchId) throws BusinessException,
			DataAccessException;

	/**
	 * @Title: startBatch
	 * @Description: 启用批次业务处理接口
	 * @param
	 * @param batchId
	 * @throws BusinessException
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-12-29 下午03:57:15
	 */
	public void startBatch(String batchId) throws BusinessException,
			DataAccessException;

}
