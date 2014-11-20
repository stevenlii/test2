package com.umpay.hfmng.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.dao.STLDao;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.CouponMerSet;
import com.umpay.hfmng.service.CouponLogService;
import com.umpay.hfmng.service.STLService;

/**
 * @ClassName: STLServiceImpl
 * @Description: 财务结算功能Service
 * @version: 1.0
 * @author: wangyuxin
 * @Create: 2013-12-13
 */
@Service
public class STLServiceImpl implements STLService{
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private CouponLogService couponLogService;
	@Autowired
	private STLDao stlDao;
	@Autowired
	private AuditDao auditDao;
	
	public CouponMerSet getQW(String accId, String stlCycle, String stlDate, String goodsId,String state) {
		CouponMerSet couponMerSet = stlDao.getQW(accId, stlCycle, stlDate, goodsId,state);
		if(couponMerSet!=null){
			couponMerSet.trim();
		}
		return couponMerSet;
	}

	public CouponMerSet getSW(String accId, String stlCycle, String stlDate, String goodsId,String state) {
		CouponMerSet couponMerSet = stlDao.getSW(accId, stlCycle, stlDate, goodsId,state);
		if(couponMerSet!=null){
			couponMerSet.trim();
		}
		return couponMerSet;
	}

	public CouponMerSet getXE(String accId, String stlCycle, String stlDate, String goodsId,String state) {
		CouponMerSet couponMerSet = stlDao.getXE(accId, stlCycle, stlDate, goodsId,state);
		if(couponMerSet!=null){
			couponMerSet.trim();
		}
		return couponMerSet;
	}
	
	/**
	 * 账单确认
	 */
	public int sureBill(String accId,String stlCycle,String stlDate,String goodsId,String billType,String settStatus){

		if("0".equals(settStatus)) {
			settStatus = "1";
			return stlDao.changeBillSettStatus(accId, stlCycle, stlDate, goodsId, billType,settStatus);
		}
		return 0;
	}
	
	/**
	 * 账单关闭
	 * @param accId
	 * @param stlCycle
	 * @param stlDate
	 * @param goodsId
	 * @param billType
	 * @param settStatus
	 * @return
	 */
	public int closeBill(String accId,String stlCycle,String stlDate,String goodsId,String billType,String settStatus){

		if("0".equals(settStatus)||"11".equals(settStatus)||"21".equals(settStatus)||"22".equals(settStatus)) {
			settStatus = "5";
			return stlDao.changeBillSettStatus(accId, stlCycle, stlDate, goodsId, billType,settStatus);
		}
		return 0;
	}
	
	/**
	 * 账单作废
	 * @param accId
	 * @param stlCycle
	 * @param stlDate
	 * @param goodsId
	 * @param billType
	 * @param settStatus
	 * @return
	 */
	public int invalidBill(String accId,String stlCycle,String stlDate,String goodsId,String billType,String settStatus){

		if("1".equals(settStatus)||"2".equals(settStatus)) {
			settStatus = "6";
			return stlDao.changeBillSettStatus(accId, stlCycle, stlDate, goodsId, billType,settStatus);
		}
		return 0;
	}
	
	public void updateStatus(CouponMerSet couponMerSet){
		
		couponMerSet.setState(0);
		//根据前结算状态值修改结算状态
		if(couponMerSet.getSettStatus()==0||couponMerSet.getSettStatus()==11){
			//【业管：待运营确认；自服务：无】或者【业管：账单修改中；自服务：无】改成【业管：账单修改中；自服务：无】
			couponMerSet.setSettStatus(11);
		}else if(couponMerSet.getSettStatus()==1||couponMerSet.getSettStatus()==21){
			//【业管：待商户确认；自服务：账单待确认】或者【业管：账单修改中；自服务：账单修改中】改成【业管：账单修改中；自服务：账单修改中】
			couponMerSet.setSettStatus(21);
		}
		String jsonString = JsonHFUtil.getJsonArrStrFrom(couponMerSet);
		Audit audit = new Audit();
		audit.setTableName("UMPAY.T_COUPON_MERSET");
		audit.setModData(jsonString);
		audit.setState(Const.AUDIT_STATE_WAIT); // 审核状态: 0待审核 1审核不通过 2审核通过
		audit.setAuditType("1"); // 审核类型 1：新增:2：修改:3：启用:4：禁用 0:未知
		audit.setCreator(couponMerSet.getModUser()); // 创建人 是当前登录用户
		audit.setIxData(couponMerSet.getAccId() + CouponMerSet.SPLIT_IXDATA + couponMerSet.getStlCycle());
		audit.setIxData2(couponMerSet.getGoodsId() + CouponMerSet.SPLIT_IXDATA + couponMerSet.getStlDate()+ CouponMerSet.SPLIT_IXDATA + couponMerSet.getBillType());
		audit.setModTime(null); // 修改时间设置为null
		audit.setBatchId(String.valueOf(couponMerSet.getLocalFlag()));
		audit.setResultDesc("");
		auditDao.insert(audit);
		stlDao.updateStatus(couponMerSet);
	}
	
	public int updateIsvalid(CouponMerSet couponMerSet){
		return stlDao.updateIsvalid(couponMerSet);
	}
	
	public void auditPass(String auditId,String resultDesc){
		Map<String, String> mapWhere = new HashMap<String, String>();
		if(StringUtil.validateNull(auditId)){
			mapWhere.put("id", auditId);
			
			Audit audit = auditDao.get(mapWhere);
			audit.setState("2");
			audit.setResultDesc(resultDesc);
			auditDao.updateState(audit);
			
			CouponMerSet auditCouponMerSet = (CouponMerSet)JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(),CouponMerSet.class);
			//审核状态设为通过
			auditCouponMerSet.setState(2);
			//根据前结算状态值修改结算状态
			if(auditCouponMerSet.getSettStatus()==11){
				//【业管：账单修改中；自服务：无】改成【业管：待运营确认；自服务：无】
				auditCouponMerSet.setSettStatus(0);
			}else if(auditCouponMerSet.getSettStatus()==21){
				//【业管：账单修改中；自服务：账单修改中】改成【业管：待商户确认；自服务：账单待确认】
				auditCouponMerSet.setSettStatus(22);
			}
			
			auditCouponMerSet.setLocalFlag(0);
			stlDao.update(auditCouponMerSet);
			couponLogService.addLog(auditCouponMerSet, "AUDIT",
					"账单{商户:" + auditCouponMerSet.getAccName() + "(" + auditCouponMerSet.getAccId() + "),子商户:"
							+ auditCouponMerSet.getGoodsId() + ",结算周期:" + auditCouponMerSet.getStlDate() + ",结算帐期:"
							+ auditCouponMerSet.getStlCycle() + "}审核通过," + resultDesc, "SUCC");
		}
	}
	
	public void auditNotPass(String auditId, String resultDesc){
		Map<String, String> mapWhere = new HashMap<String, String>();
		if(StringUtil.validateNull(auditId)){
			mapWhere.put("id", auditId);
			
			Audit audit = auditDao.get(mapWhere);
			audit.setState("1");
			audit.setResultDesc(resultDesc);
			auditDao.updateState(audit);
			
			CouponMerSet auditCouponMerSet = (CouponMerSet)JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(),CouponMerSet.class);
			//审核状态设为不通过
			auditCouponMerSet.setState(1);
			auditCouponMerSet.setLocalFlag(0);
			stlDao.updateStatus(auditCouponMerSet);
			couponLogService.addLog(auditCouponMerSet, "AUDIT", "账单{商户:" + auditCouponMerSet.getAccName() + "("
					+ auditCouponMerSet.getAccId() + "),子商户:" + auditCouponMerSet.getGoodsId() + ",结算周期:"
					+ auditCouponMerSet.getStlDate() + ",结算帐期:" + auditCouponMerSet.getStlCycle() + ")审核未通过,"
					+ resultDesc, "SUCC");
		}
	}

	/**
	 * @Title: change2payed
	 * @Description: 将结算状态变更为已付款
	 * @param targetDate 截止日期
	 * @return 影响行数
	 * @author jxd
	 * @date 2013-12-17 下午5:36:34
	 */
	public int change2payed(String targetDate) {
		return stlDao.updateStlStatus2payed(targetDate);
	}

	/**
	 * @Title: change2paying
	 * @Description: 将结算状态变更为付款中<br/>
	 *               更新平台T_COUPON_MERSET表结算状态为2（商户已确认）
	 *               且财务平台T_HS_COSTANALYZE对应付款状态为1（已付款）的结算状态为3（付款中）
	 * @return 影响行数
	 * @author jxd
	 * @date 2013-12-17 下午8:39:56
	 */
	public int change2paying() {
		return stlDao.updateStlStatus2paying();
	}
	
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
	public int getMersetDetail(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", 0);
		return stlDao.getMersetDetail(map);
	}
	
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
	public int getMersetMerger(int billtype,String finishSign){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("billtype", billtype);
		map.put("finishsign", finishSign);
		map.put("count", 0);
		return stlDao.getMersetMerger(map);
	}
}
