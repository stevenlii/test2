package com.umpay.hfmng.dao;

import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CouponBatch;

/**
 * @ClassName: CouponBatchDao
 * @Description: 兑换码批量导入批次数据库处理接口
 * @author wanyong
 * @date 2012-12-22 下午03:13:24
 */
public interface CouponBatchDao extends EntityDao<CouponBatch> {

	/**
	 * @Title: getCouponBatch
	 * @Description: 根据批次ID获取批次信息接口
	 * @param
	 * @param batchId
	 * @return
	 * @author wanyong
	 * @date 2012-12-22 下午03:23:16
	 */
	public CouponBatch getCouponBatch(String batchId);

	/**
	 * @Title: insertCouponBatch
	 * @Description: 新增一条批次信息接口
	 * @param
	 * @param couponBatch
	 * @author wanyong
	 * @date 2012-12-22 下午03:23:53
	 */
	public void insertCouponBatch(CouponBatch couponBatch);

	/**
	 * @Title: updateCouponBatch
	 * @Description: 更新一条批次信息接口
	 * @param
	 * @param couponBatch
	 * @return
	 * @author wanyong
	 * @date 2012-12-22 下午03:24:14
	 */
	public int updateCouponBatch(CouponBatch couponBatch);

	/**
	 * @Title: updateGoodsSum
	 * @Description: 更新批次库存接口
	 * @param
	 * @param couponBatch
	 * @return
	 * @author wanyong
	 * @date 2012-12-31 上午11:57:18
	 */
	public int updateGoodsSum(CouponBatch couponBatch);

	/**
	 * @Title: queryDisBatchCount
	 * @Description: 查询兑换码销售期时间有重叠的批次数量接口
	 * @param
	 * @param whereMap
	 *            查询条件数据
	 * @return 返回重叠批次数量（0-没有重叠、大于0-有重叠）
	 * @author wanyong
	 * @date 2013-1-5 下午09:31:23
	 */
	public int queryDisBatchCount(Map<String, Object> whereMap);

	/**
	 * @Title: findMaxBatchId
	 * @Description: 根据规则ID获取最大批次ID接口
	 * @param
	 * @param ruleId
	 * @return
	 * @author wanyong
	 * @date 2013-1-15 下午09:09:00
	 */
	public String findMaxBatchId(String ruleId);
}
