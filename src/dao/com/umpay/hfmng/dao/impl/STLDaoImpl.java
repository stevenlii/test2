/**
 * 
 */
package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.dao.STLDao;
import com.umpay.hfmng.model.CouponMerSet;

/**
 * @ClassName: STLDaoImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @version: 1.0
 * @author: wangyuxin
 * @Create: 2013-12-10
 */
@Repository("stlDaoImpl")
public class STLDaoImpl extends EntityOffLineDaoImpl<CouponMerSet> implements STLDao {

	public CouponMerSet getQW(String accId,String stlCycle,String stlDate,String goodsId,String state) {
		Map<String, String> pkMap = new HashMap<String, String>();
		pkMap.put("accId", accId);
		pkMap.put("stlCycle", stlCycle);
		pkMap.put("stlDate", stlDate);
		pkMap.put("goodsId", goodsId);
		pkMap.put("state", state);
		return (CouponMerSet)this.get("stl.getQW",pkMap);
	}
	
	public CouponMerSet getSW(String accId,String stlCycle,String stlDate,String goodsId,String state) {
		Map<String, String> pkMap = new HashMap<String, String>();
		pkMap.put("accId", accId);
		pkMap.put("stlCycle", stlCycle);
		pkMap.put("stlDate", stlDate);
		pkMap.put("goodsId", goodsId);
		pkMap.put("state", state);
		return (CouponMerSet)this.get("stl.getSW",pkMap);
	}
	
	public CouponMerSet getXE(String accId,String stlCycle,String stlDate,String goodsId,String state) {
		Map<String, String> pkMap = new HashMap<String, String>();
		pkMap.put("accId", accId);
		pkMap.put("stlCycle", stlCycle);
		pkMap.put("stlDate", stlDate);
		pkMap.put("goodsId", goodsId);
		pkMap.put("state", state);
		return (CouponMerSet)this.get("stl.getXE",pkMap);
	}
	
	public int changeBillSettStatus(String accId,String stlCycle,String stlDate,String goodsId,String billType,String settStatus){
		Map<String, String> pkMap = new HashMap<String, String>();
		pkMap.put("accId", accId);
		pkMap.put("stlCycle", stlCycle);
		pkMap.put("stlDate", stlDate);
		pkMap.put("goodsId", goodsId);
		pkMap.put("billType", billType);
		pkMap.put("settStatus", settStatus);
		return this.update("stl.changeBillSettStatus",pkMap);
	}
	
	/**
	 * @Title: updateStlStatus2payed
	 * @Description: 将结算状态变更为已付款
	 * @param targetDate 截止日期 
	 * @return 影响行数
	 * @author jxd
	 * @date 2013-12-17 下午5:32:22
	 */
	public int updateStlStatus2payed(String targetDate){
		Map<String, String> para = new HashMap<String, String>();
		para.put("targetDate", targetDate);
		return this.update("stl.updateStlStatus2payed", para);
	}
	
	/**
	 * @Title: updateStlStatus2paying
	 * @Description: 将结算状态变更为付款中<br/>
	 *               更新平台T_COUPON_MERSET表结算状态为2（商户已确认）
	 *               且财务平台T_HS_COSTANALYZE对应付款状态为1（已付款）的结算状态为3（付款中）
	 * @return 影响行数
	 * @author jxd
	 * @date 2013-12-17 下午8:41:54
	 */
	public int updateStlStatus2paying(){
		Map<String, String> para = new HashMap<String, String>();
		return this.update("stl.updateStlStatus2paying", para);
	}

	public int updateStatus(CouponMerSet couponMerSet){
		if(couponMerSet!=null){
			String accId = couponMerSet.getAccId();
			int stlCycle = couponMerSet.getStlCycle();
			String stlDate = couponMerSet.getStlDate();
			String goodsId = couponMerSet.getGoodsId();
			int billType = couponMerSet.getBillType();
			int state = couponMerSet.getState();
			
			Map<String, String> pkMap = new HashMap<String, String>();
			pkMap.put("accId", accId);
			pkMap.put("stlCycle", stlCycle + "");
			pkMap.put("stlDate", stlDate);
			pkMap.put("goodsId", goodsId);
			pkMap.put("billType", billType + "");
			if(state!=Const.COUPON_MERSET_INIT_NUM){
				pkMap.put("state", state + "");
			}
			pkMap.put("settStatus", ""+couponMerSet.getSettStatus());
			
			return this.update("stl.updateStatus",pkMap);
		}
		return 0;
	}
	
	public int updateIsvalid(CouponMerSet couponMerSet){
		if(couponMerSet!=null){
			String accId = couponMerSet.getAccId();
			int stlCycle = couponMerSet.getStlCycle();
			String stlDate = couponMerSet.getStlDate();
			String goodsId = couponMerSet.getGoodsId();
			int billType = couponMerSet.getBillType();
			
			Map<String, String> pkMap = new HashMap<String, String>();
			pkMap.put("accId", accId);
			pkMap.put("stlCycle", stlCycle + "");
			pkMap.put("stlDate", stlDate);
			pkMap.put("goodsId", goodsId);
			pkMap.put("billType", billType + "");
			pkMap.put("is_valid", couponMerSet.getIs_valid()+"");
			
			return this.update("stl.updateIsvalid",pkMap);
		}
		return 0;
	}
	
	@Override
	public int update(CouponMerSet couponMerSet){
		if(couponMerSet!=null){
			Map<String, String> para = new HashMap<String, String>();
			
			String accId = couponMerSet.getAccId();
			int stlCycle = couponMerSet.getStlCycle();
			String goodsId = couponMerSet.getGoodsId();
			String stlDate = couponMerSet.getStlDate();
			int billType = couponMerSet.getBillType();
			
			int state = couponMerSet.getState();
			int settStatus = couponMerSet.getSettStatus();
			double billSuccAmtm = couponMerSet.getBillSuccAmtm();
			int billSuccNumm = couponMerSet.getBillSuccNumm();
			double cmccAmount = couponMerSet.getCmccAmount();
			double merStlAmt = couponMerSet.getMerStlAmt();
			double merStlPay = couponMerSet.getMerStlPay();
			int moNum = couponMerSet.getMoNum();
			int mtNum = couponMerSet.getMtNum();
			double muteAmt = couponMerSet.getMuteAmt();
			double muteNum = couponMerSet.getMuteNum();
			double nbsmAmt = couponMerSet.getNbsmAmt();
			int nbsmNum = couponMerSet.getNbsmNum();
			double paybackAmt = couponMerSet.getPaybackAmt();
			double paybackNum = couponMerSet.getPaybackNum();
			double shamAmt = couponMerSet.getShamAmt();
			int shamNum = couponMerSet.getShamNum();
			double succAmtm = couponMerSet.getSuccAmtm();
			double succAmts = couponMerSet.getSuccAmts();
			int succNumm = couponMerSet.getSuccNumm();
			int succNums = couponMerSet.getSuccNums();
			double bad_bill = couponMerSet.getBad_bill();
			double ump_income = couponMerSet.getUmp_income();
			double balance_amount = couponMerSet.getBalance_amount();
			
			//where条件
			para.put("accId", accId);
			para.put("stlCycle", stlCycle + "");
			para.put("goodsId", goodsId);
			para.put("stlDate", stlDate);
			para.put("billType", billType + "");
			
			//set的值
			if(state!=Const.COUPON_MERSET_INIT_NUM){
				para.put("state", state + "");
			}
			if(settStatus!=Const.COUPON_MERSET_INIT_NUM){
				para.put("settStatus", settStatus + "");
			}else{
				para.put("settStatus", "0");
			}
			if(billSuccAmtm!=Const.COUPON_MERSET_INIT_NUM){
				para.put("billSuccAmtm", billSuccAmtm*100 + "");
			}
			if(billSuccNumm!=Const.COUPON_MERSET_INIT_NUM){
				para.put("billSuccNumm", billSuccNumm + "");
			}
			if(cmccAmount!=Const.COUPON_MERSET_INIT_NUM){
				para.put("cmccAmount", cmccAmount*100 + "");
			}
			if(merStlAmt!=Const.COUPON_MERSET_INIT_NUM){
				para.put("merStlAmt", merStlAmt*100 + "");
			}
			if(merStlPay!=Const.COUPON_MERSET_INIT_NUM){
				para.put("merStlPay", merStlPay*100 + "");
			}
			if(moNum!=Const.COUPON_MERSET_INIT_NUM){
				para.put("moNum", moNum + "");
			}
			if(mtNum!=Const.COUPON_MERSET_INIT_NUM){
				para.put("mtNum", mtNum + "");
			}
			if(muteAmt!=Const.COUPON_MERSET_INIT_NUM){
				para.put("muteAmt", muteAmt*100 + "");
			}
			if(muteNum!=Const.COUPON_MERSET_INIT_NUM){
				para.put("muteNum", muteNum + "");
			}
			if(nbsmAmt!=Const.COUPON_MERSET_INIT_NUM){
				para.put("nbsmAmt", nbsmAmt*100 + "");
			}
			if(nbsmNum!=Const.COUPON_MERSET_INIT_NUM){
				para.put("nbsmNum", nbsmNum + "");
			}
			if(paybackAmt!=Const.COUPON_MERSET_INIT_NUM){
				para.put("paybackAmt", paybackAmt*100 + "");
			}
			if(paybackNum!=Const.COUPON_MERSET_INIT_NUM){
				para.put("paybackNum", paybackNum + "");
			}
			if(shamAmt!=Const.COUPON_MERSET_INIT_NUM){
				para.put("shamAmt", shamAmt*100 + "");
			}
			if(shamNum!=Const.COUPON_MERSET_INIT_NUM){
				para.put("shamNum", shamNum + "");
			}
			if(succAmtm!=Const.COUPON_MERSET_INIT_NUM){
				para.put("succAmtm", succAmtm*100 + "");
			}
			if(succAmts!=Const.COUPON_MERSET_INIT_NUM){
				para.put("succAmts", succAmts*100 + "");
			}
			if(succNumm!=Const.COUPON_MERSET_INIT_NUM){
				para.put("succNumm", succNumm + "");
			}
			if(succNums!=Const.COUPON_MERSET_INIT_NUM){
				para.put("succNums", succNums + "");
			}
			
			if(bad_bill!=Const.COUPON_MERSET_INIT_NUM){
				para.put("bad_bill", bad_bill*100 + "");
			}
			if(ump_income!=Const.COUPON_MERSET_INIT_NUM){
				para.put("ump_income", ump_income*100 + "");
			}
			if(balance_amount!=Const.COUPON_MERSET_INIT_NUM){
				para.put("balance_amount", balance_amount*100 + "");
			}
			return this.update("stl.update",para);
		}
		return 0;
	}
	
	/**
	 * 
	 * @Title: getMersetDetail
	 * @Description: 获取结算明细数据(存储过程)
	 * @param m
	 * @return
	 * @author lituo
	 * @date 2013-12-20 上午09:35:13
	 */
	public int getMersetDetail(Map<String, Object> m){
		this.save("StlMerger.procCallMerset", m);
		int count = (Integer)m.get("count");
		return count;
	}
	
	/**
	 * 
	 * @Title: getMersetMerger
	 * @Description: 获取结算整合数据（存储过程）
	 * @param m
	 * @return
	 * @author lituo
	 * @date 2013-12-20 上午09:35:29
	 */
	public int getMersetMerger(Map<String, Object> m){
		this.save("StlMerger.procCallMersetMerger", m);
		int count = (Integer)m.get("count");
		return count;
	}
}
