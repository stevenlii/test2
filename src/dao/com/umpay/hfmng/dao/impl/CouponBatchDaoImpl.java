package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.CouponBatchDao;
import com.umpay.hfmng.model.CouponBatch;

/**
 * @ClassName: CouponBatchDaoImpl
 * @Description: 兑换码批量导入批次数据库处理
 * @author wanyong
 * @date 2012-12-26 下午03:48:38
 */
@Repository("couponBatchDaoImpl")
public class CouponBatchDaoImpl extends EntityDaoImpl<CouponBatch> implements
		CouponBatchDao {

	/**
	 * @Title: getCouponBatch
	 * @Description: 根据批次ID获取批次信息
	 * @param
	 * @param batchId
	 * @return
	 * @author wanyong
	 * @date 2012-12-29 下午05:02:45
	 */
	public CouponBatch getCouponBatch(String batchId) {
		return (CouponBatch) super.get("CouponBatch.get", batchId);
	}

	/**
	 * @Title: insertCouponBatch
	 * @Description: 新增一条批次信息
	 * @param
	 * @param couponBatch
	 * @author wanyong
	 * @date 2012-12-28 上午11:05:28
	 */
	public void insertCouponBatch(CouponBatch couponBatch) {
		super.save("CouponBatch.insert", couponBatch);
	}

	public int updateCouponBatch(CouponBatch couponBatch) {
		return super.update("CouponBatch.update", couponBatch);
	}

	/**
	 * @Title: updateGoodsSum
	 * @Description: 更新批次库存
	 * @param
	 * @param couponBatch
	 * @return
	 * @author wanyong
	 * @date 2013-1-1 上午01:12:46
	 */
	public int updateGoodsSum(CouponBatch couponBatch) {
		return super.update("CouponBatch.updategoodssum", couponBatch);
	}

	/**
	 * @Title: 查询兑换码销售期时间有重叠的批次数量
	 * @Description: TODO
	 * @param
	 * @param whereMap
	 *            查询条件数据
	 * @return 返回重叠批次数量（0-没有重叠、大于0-有重叠）
	 * @author wanyong
	 * @date 2013-1-5 下午09:30:42
	 */
	@SuppressWarnings("unchecked")
	public int queryDisBatchCount(Map<String, Object> whereMap) {
		Map<String, Integer> reMap = (HashMap<String, Integer>) super.get(
				"CouponBatch.querydisbatchcount", whereMap);
		return reMap.get("NUM");
	}

	/**
	 * @Title: findMaxBatchId
	 * @Description: 根据规则ID获取最大批次ID
	 * @param
	 * @param ruleId
	 * @return
	 * @author wanyong
	 * @date 2013-1-15 下午09:09:38
	 */
	@SuppressWarnings("unchecked")
	public String findMaxBatchId(String ruleId) {
		Map<String, String> reMap = (HashMap<String, String>) super.get(
				"CouponBatch.findMaxBatchId", ruleId);
		return null == reMap.get("BATCHID") ? null : reMap.get("BATCHID")
				.trim();
	}

}
