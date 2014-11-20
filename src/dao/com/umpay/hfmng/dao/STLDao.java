/**
 * 
 */
package com.umpay.hfmng.dao;

import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.CouponMerSet;

/**
 * @ClassName: STLDao
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @version: 1.0
 * @author: wangyuxin
 * @Create: 2013-12-9
 */

public interface STLDao extends EntityDao<CouponMerSet>{
	public CouponMerSet getQW(String accId,String stlCycle,String stlDate,String goodsId,String state);
	public CouponMerSet getSW(String accId,String stlCycle,String stlDate,String goodsId,String state);
	public CouponMerSet getXE(String accId,String stlCycle,String stlDate,String goodsId,String state);
	public int changeBillSettStatus(String accId,String stlCycle,String stlDate,String goodsId,String billType,String settStatus);
	public int updateStatus(CouponMerSet couponMerSet);
	public int update(CouponMerSet couponMerSet);
	public int updateIsvalid(CouponMerSet couponMerSet);
	/**
	 * @Title: updateStlStatus2payed
	 * @Description: 将结算状态变更为已付款
	 * @param targetDate 截止日期 
	 * @return 影响行数
	 * @author jxd
	 * @date 2013-12-17 下午5:32:22
	 */
	int updateStlStatus2payed(String targetDate);
	
	/**
	 * @Title: updateStlStatus2paying
	 * @Description: 将结算状态变更为付款中<br/>
	 *               更新平台T_COUPON_MERSET表结算状态为2（商户已确认）
	 *               且财务平台T_HS_COSTANALYZE对应付款状态为1（已付款）的结算状态为3（付款中）
	 * @return
	 * @author jxd
	 * @date 2013-12-17 下午8:40:59
	 */
	int updateStlStatus2paying();
	
	/**
	 * 
	 * @Title: getMersetDetail
	 * @Description: 获取结算明细数据(存储过程)
	 * @param m
	 * @return
	 * @author lituo
	 * @date 2013-12-20 上午09:35:13
	 */
	public int getMersetDetail(Map<String, Object> m);
	
	/**
	 * 
	 * @Title: getMersetMerger
	 * @Description: 获取结算整合数据（存储过程）
	 * @param m
	 * @return
	 * @author lituo
	 * @date 2013-12-20 上午09:35:29
	 */
	public int getMersetMerger(Map<String, Object> m);
}
