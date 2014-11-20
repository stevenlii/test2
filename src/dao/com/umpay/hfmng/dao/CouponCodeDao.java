package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CouponCode;

/**
 * @ClassName: CouponCodeDao
 * @Description: 兑换码数据库处理接口
 * @author wanyong
 * @date 2012-12-28 上午11:13:47
 */
public interface CouponCodeDao extends EntityDao<CouponCode> {

	public void insertCouponCodeBatch(List<CouponCode> list)throws Exception;
	/**
	 * @Title: insertCouponCode
	 * @Description: 新增一条兑换码信息接口
	 * @param
	 * @param couponCode
	 * @author wanyong
	 * @date 2012-12-28 上午11:13:31
	 */
	public void insertCouponCode(CouponCode couponCode);

	/**
	 * @Title: updateCouponCode
	 * @Description: 根据主键更新兑换码接口
	 * @param
	 * @param couponCode
	 * @return
	 * @author wanyong
	 * @date 2012-12-28 下午05:11:00
	 */
	public int updateCouponCode(CouponCode couponCode);

	/**
	 * @Title: getCouponCodeCount
	 * @Description: 查询商户ID、兑换码查询记录数接口
	 * @param
	 * @param enCode
	 *            加密后的兑换码
	 * @param merId
	 * @return
	 * @author wanyong
	 * @date 2012-12-28 上午11:46:15
	 */
	public int getCouponCodeCount(String enCode, String merId);

	/**
	 * @Title: updateSelfDefine
	 * @Description: 根据自定义条件更新自定义兑换码列接口
	 * @param
	 * @param map
	 *            待更新列数据KEY=列名,VALUE=值
	 * @param whereMap
	 *            where条件列数据KEY=列名,VALUE=值
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 下午06:23:56
	 */
	public int updateSelfDefine(Map<String, Object> map, Map<String, Object> whereMap);

	/**
	 * @Title: getCouponCode
	 * @Description: 查询一个兑换码实体接口
	 * @param code
	 *            加密后的兑换码
	 * @param merId
	 *            商户ID
	 * @return
	 * @author wanyong
	 * @date 2013-5-31 下午4:59:06
	 */
	public CouponCode getCouponCode(String code, String merId);

}
