package com.umpay.hfmng.service;

import com.umpay.hfmng.model.CouponMerSet;

/**
 * @ClassName: STLService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @version: 1.0
 * @author: wangyuxin
 * @Create: 2013-12-9
 */

public interface STLService {
	public CouponMerSet getQW(String accId,String stlCycle,String stlDate,String goodsId,String state);
	public CouponMerSet getSW(String accId,String stlCycle,String stlDate,String goodsId,String state);
	public CouponMerSet getXE(String accId,String stlCycle,String stlDate,String goodsId,String state);
	public int sureBill(String accId,String stlCycle,String stlDate,String goodsId,String billType,String settStatus);
	public int closeBill(String accId,String stlCycle,String stlDate,String goodsId,String billType,String settStatus);
	public int invalidBill(String accId,String stlCycle,String stlDate,String goodsId,String billType,String settStatus);
	public void updateStatus(CouponMerSet couponMerSet);
	public void auditPass(String auditId, String resultDesc);
	public void auditNotPass(String auditId, String resultDesc);
	public int updateIsvalid(CouponMerSet couponMerSet);
	/**
	 * @Title: change2payed
	 * @Description: 将结算状态变更为已付款
	 * @param targetDate 截止日期 
	 * @return 影响行数
	 * @author jxd
	 * @date 2013-12-17 下午5:32:22
	 */
	int change2payed(String targetDate);
	
	/**
	 * @Title: change2paying
	 * @Description: 将结算状态变更为付款中<br/>
	 *               更新平台T_COUPON_MERSET表结算状态为2（商户已确认）
	 *               且财务平台T_HS_COSTANALYZE对应付款状态为1（已付款）的结算状态为3（付款中）
	 * @return 影响行数
	 * @author jxd
	 * @date 2013-12-17 下午8:39:12
	 */
	int change2paying();
	
	/**
	 * 
	 * @Title: getMersetDetail
	 * @Description: 获取结算明细数据(存储过程)
	 * @param startDate
	 * @param count
	 * @return
	 * @author lituo
	 * @date 2013-12-20 上午09:50:20
	 */
	public int getMersetDetail();
	
	/**
	 * 
	 * @Title: getMersetMerger
	 * @Description: 获取结算整合数据（存储过程）
	 * @param billtype
	 * @param finishSign
	 * @param count
	 * @return
	 * @author lituo
	 * @date 2013-12-20 上午09:50:24
	 */
	public int getMersetMerger(int billtype,String finishSign);
}
