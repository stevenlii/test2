package com.umpay.hfmng.action;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.ExportUtil;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.CouponLog;
import com.umpay.hfmng.model.CouponMerSet;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.CouponLogService;
import com.umpay.hfmng.service.STLService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: STLAction
 * @Description: 财务结算
 * @version: 1.0
 * @author: wangyuxin
 * @Create: 2013-12-9
 */
@Controller
@RequestMapping("/stl")
public class STLAction extends BaseAction {
	@Autowired
	private CouponLogService couponLogService;
	@Autowired
	private STLService stlService;
	@Autowired
	private AuditService auditService;
	
	private String madeDate = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());

	/**
	 * @Title: toLog
	 * @Description: 跳转至日志首页
	 * @return String
	 * @author panyouliang
	 * @date 2013-12-10 上午9:36:15
	 */
	@RequestMapping(value = "/toquerylog")
	public String toLog() {
		return "stl/querylog";
	}

	/**
	 * @Title: addLog
	 * @Description: 添加日志
	 * @param stldate
	 *            结算周期
	 * @param subMerId
	 *            子商户ID
	 * @param stlCycle
	 *            结算账期
	 * @param merId
	 *            商户ID
	 * @param opdata
	 *            操作内容
	 * @param optype
	 *            操作类型 AUDIT|MODIFY|CONFIRM
	 * @param result
	 *            操作结果 SUCC|FAIL
	 * @return Boolean
	 * @author panyouliang
	 * @date 2013-12-10 上午11:01:55
	 */
	private Boolean addLog(String stldate, String subMerId, String stlCycle, String accId, String accName, String opdata,
			String optype, String result) {
		CouponLog oplog = new CouponLog();
		oplog.setBusinessobject("UMPAY.T_COUPON_MERSET");
		oplog.setResultdesc(result);// 操作结果描述
		oplog.setIxdata1(stldate); // 结算周期
		oplog.setIxdata2(subMerId); // 子商户ID
		oplog.setIxdata3(stlCycle); // 结算账期
		if(StringUtil.validateNull(accName) && StringUtil.validateNull(accId)){
			oplog.setIxdata4(accName + "(" + accId + ")"); // 商户ID
		}
		oplog.setOperdata(opdata);// 操作内容
		oplog.setOpertype(optype); // 操作类型
		try {
			couponLogService.addLog(oplog);
		} catch (Exception e) {
			log.error("添加日志异常", e);
			return false;
		}
		return true;
	}

	/**
	 * @Title: queryLog
	 * @Description: 查询日志
	 * @return ModelAndView
	 * @author panyouliang
	 * @date 2013-12-10 上午9:36:37
	 */
	@RequestMapping(value = "/querylog")
	public ModelAndView queryLog() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		String start = (String) map.get("cycleStart");
		String end = (String) map.get("cycleEnd");
		if(start != null && !"".equals(start.trim())){
			map.put("cycleStart", start.replace("-", ""));
		}
		if(end != null && !"".equals(end.trim())){
			map.put("cycleEnd", end.replace("-", ""));
		}
		
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				String cycle = "";
				for (Map<String, Object> subMap : data) {
					Integer idx3 = new Integer((String) subMap.get("IXDATA3"));
					String idx1 = (String) subMap.get("IXDATA1");
					/*if(idx3 == 1){ //日结算
						if(idx1 != null && idx1.length() == 7){
							cycle = idx1.substring(0, 4) + "-" + idx1.substring(4, 6) + "-" + idx1.substring(6, 8) + " 至 " + idx1.substring(0, 4) + "-" + idx1.substring(4, 6) + "-" + idx1.substring(6, 8);
						}else{
							cycle = idx1;
						}
					}else if(idx3 == 2){//周结算
						if(idx1 != null && idx1.length() == 7){
							String temp = TimeUtil.calcDay(idx1, -7);
							cycle = temp.substring(0, 4) + "-" + temp.substring(4, 6) + "-" + temp.substring(6, 8) + " 至 " + idx1.substring(0, 4) + "-" + idx1.substring(4, 6) + "-" + idx1.substring(6, 8);
						}else{
							cycle = idx1;
						}
					}else{//其他类型结算
						cycle = idx1.substring(0, 4) + "-" + idx1.substring(4, 6) + "-01"  + " 至 " + idx1.substring(0, 4) + "-" + idx1.substring(4, 6) + "-" + TimeUtil.getDaysInAmonth(idx1);
					}*/
					subMap.put("STLCYCLE", idx1); // 周期
					String mer = (String) subMap.get("IXDATA4");
					if(StringUtil.validateNull(mer)){
						subMap.put("MER", mer);// 商户
					}else{
						subMap.put("MER", "-");// 商户
					}
					String subMerId = (String) subMap.get("IXDATA2");
					if (subMerId == null || "".equals(subMerId)) {
						subMap.put("SUBMER", "");// 子商户
					} else {
						subMap.put("SUBMER", subMerId);// 子商户
					}
					subMap.put("OPERDATA", subMap.get("OPERDATA"));// 操作内容
					subMap.put("OPERTYPE", ParameterPool.stlStates.get(subMap.get("OPERTYPE")));// 操作类型
				}
				long count = queryCount(queryKey, map);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else {
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * 跳转到账单查询列表页面
	 * @Title: index
	 * @Description: 
	 * @return
	 * @return String
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:39:44
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap modelMap) {
		// 查找按钮权限
		String opts = HfCacheUtil.getCache().getUrlAcl("stl");
//		String opts = "001,002,003";
		modelMap.addAttribute("opts", opts);
		return "stl/index";
	}
	

	/**
	 * 跳转到查看页面
	 * @Title: queryDetail
	 * @Description: 
	 * @param billType
	 * @param accId
	 * @param stlCycle
	 * @param stlDate
	 * @param goodsId
	 * @param modelMap
	 * @return
	 * @return String
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:39:19
	 */
	@RequestMapping(value = "/queryDetail")
	public String queryDetail(int billType, String accId, String accName, String stlCycle, String stlDate, String goodsId,
			ModelMap modelMap) {
		String opts = HfCacheUtil.getCache().getUrlAcl("stl");
		modelMap.addAttribute("opts", opts);
		modelMap.addAttribute("accId", accId);
		modelMap.addAttribute("accName", accName);
		modelMap.addAttribute("stlCycle", stlCycle);
		modelMap.addAttribute("stlDate", stlDate);
		modelMap.addAttribute("goodsId", goodsId);
		modelMap.addAttribute("madeDate", madeDate);
		modelMap.addAttribute("billCycle", getBillCycle(stlDate));
		if (billType == 1) {
			CouponMerSet couponMerSet = stlService.getQW(accId, stlCycle, stlDate, goodsId,null);
			modelMap.addAttribute("couponMerSet", couponMerSet);
			if(couponMerSet != null){
				modelMap.addAttribute("settStatus", couponMerSet.getSettStatus());
			}
			return "stl/queryQW";
		}
		if (billType == 2) {
			CouponMerSet couponMerSet = stlService.getSW(accId, stlCycle, stlDate, goodsId,null);
			modelMap.addAttribute("couponMerSet", couponMerSet);
			modelMap.addAttribute("settStatus", couponMerSet.getSettStatus());
			return "stl/querySW";
		}
		if (billType == 3) {
			CouponMerSet couponMerSet = stlService.getXE(accId, stlCycle, stlDate, goodsId,null);
			modelMap.addAttribute("couponMerSet", couponMerSet);
			modelMap.addAttribute("settStatus", couponMerSet.getSettStatus());
			return "stl/queryXE";
		}
		return null;
	}
	/**
	 * 跳转到审核页面
	 * @Title: auditDetail
	 * @Description: 
	 * @param billType
	 * @param accId
	 * @param stlCycle
	 * @param stlDate
	 * @param goodsId
	 * @param modelMap
	 * @return
	 * @return String
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:39:08
	 */
	@RequestMapping(value = "/auditDetail")
	public String auditDetail(int billType, String accId, String accName, String stlCycle, String stlDate, String goodsId,
			ModelMap modelMap){
//		modelMap.addAttribute("accId", accId);
//		modelMap.addAttribute("accName", accName);
//		modelMap.addAttribute("stlCycle", stlCycle);
//		modelMap.addAttribute("stlDate", stlDate);
//		modelMap.addAttribute("goodsId", goodsId);
		modelMap.addAttribute("madeDate", madeDate);
		modelMap.addAttribute("billCycle", getBillCycle(stlDate));
		String state = "0";
		Map<String, String> mapWhere = new HashMap<String, String>();
		String ixData = accId + CouponMerSet.SPLIT_IXDATA + stlCycle;
		String ixData2 = goodsId + CouponMerSet.SPLIT_IXDATA + stlDate + CouponMerSet.SPLIT_IXDATA + billType;
		mapWhere.put("ixData", ixData);
		mapWhere.put("ixData2", ixData2);
		//全网
		if (billType == 1) {
			CouponMerSet couponMerSet = stlService.getQW(accId, stlCycle, stlDate, goodsId,state);
			if(couponMerSet!=null){
				couponMerSet.trim();
				// 从审核表取出信息
				mapWhere.put("batchId", "0");
				Audit audit = auditService.getOneObj(mapWhere);
				CouponMerSet auditCouponMerSet = (CouponMerSet)JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(),CouponMerSet.class);
//				//设置包月成功计费比率为修改后的显示在页面上
//				if(auditCouponMerSet.getBillSuccAmtm()!=Const.COUPON_MERSET_INIT_NUM){
//					if(couponMerSet.getSuccAmtm()==0){
//						couponMerSet.setExt1(0);
//					}else{
//						couponMerSet.setExt1(auditCouponMerSet.getBillSuccAmtm()/couponMerSet.getSuccAmtm()*100);
//					}
//				}
//				//设置移动核减比率为修改后的显示在页面上
//				if(auditCouponMerSet.getMuteAmt()!=Const.COUPON_MERSET_INIT_NUM){
//					if(couponMerSet.getSuccAmtm()==0){
//						couponMerSet.setExt2(0);
//					}else{
//						couponMerSet.setExt2(auditCouponMerSet.getMuteAmt()/couponMerSet.getSuccAmtm()*100);
//					}
//				}
				//设置商户分成比率为修改后的显示在页面上
				if(auditCouponMerSet.getMerStlPay()!=Const.COUPON_MERSET_INIT_NUM && auditCouponMerSet.getCmccAmount()!=Const.COUPON_MERSET_INIT_NUM){
					if(auditCouponMerSet.getCmccAmount()==0){
						couponMerSet.setExt3(0);
					}else{
						couponMerSet.setExt3(auditCouponMerSet.getMerStlPay()/auditCouponMerSet.getCmccAmount()*100);						
					}
				}else if(auditCouponMerSet.getMerStlPay()==Const.COUPON_MERSET_INIT_NUM && auditCouponMerSet.getCmccAmount()!=Const.COUPON_MERSET_INIT_NUM){
					if(couponMerSet.getCmccAmount()==0){						
						couponMerSet.setExt3(0);
					}else{
						couponMerSet.setExt3(couponMerSet.getMerStlPay()/couponMerSet.getCmccAmount()*100);
					}
				}else if(auditCouponMerSet.getMerStlPay()!=Const.COUPON_MERSET_INIT_NUM && auditCouponMerSet.getCmccAmount()==Const.COUPON_MERSET_INIT_NUM){
					if(couponMerSet.getCmccAmount()==0){
						couponMerSet.setExt3(0);
					}else{
						couponMerSet.setExt3(auditCouponMerSet.getMerStlPay()/couponMerSet.getCmccAmount()*100);
					}
				}
				modelMap.addAttribute("couponMerSet", couponMerSet);
				modelMap.addAttribute("auditCouponMerSet", auditCouponMerSet);
				modelMap.addAttribute("auditId", audit.getId());
			}
			
//			double totalAmt = 0;
//			if(couponMerSet!=null){
//				totalAmt = totalAmt + couponMerSet.getMerStlAmt();
//			}
//			modelMap.addAttribute("totalAmt", totalAmt);
			return "stl/auditQW";
		}
		else if (billType == 2) {
			CouponMerSet couponMerSet = stlService.getSW(accId, stlCycle, stlDate, goodsId,state);
			if(couponMerSet!=null){
				couponMerSet.trim();
				mapWhere.put("batchId", "1");
				Audit audit = auditService.getOneObj(mapWhere);
				CouponMerSet auditCouponMerSet = (CouponMerSet)JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(),CouponMerSet.class);
				//设置商户分成比率为修改后的显示在页面上
				if(auditCouponMerSet.getMerStlPay()!=Const.COUPON_MERSET_INIT_NUM){
					if(couponMerSet.getSuccAmts()==0){
						couponMerSet.setExt1(0);
					}else{						
						couponMerSet.setExt1(auditCouponMerSet.getMerStlPay()/couponMerSet.getSuccAmts()*100);
					}
				}
				modelMap.addAttribute("couponMerSet", couponMerSet);
				modelMap.addAttribute("auditCouponMerSet", auditCouponMerSet);
				modelMap.addAttribute("auditId", audit.getId());
			}
			return "stl/auditSW";
		}
		else if (billType == 3) {
			CouponMerSet couponMerSet = stlService.getXE(accId, stlCycle, stlDate, goodsId,state);
			if(couponMerSet!=null){
				couponMerSet.trim();
				mapWhere.put("batchId", "1");
				Audit audit = auditService.getOneObj(mapWhere);
				CouponMerSet auditCouponMerSet = (CouponMerSet)JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(),CouponMerSet.class);
				//设置商户分成比率为修改后的显示在页面上
				double merStlPay = couponMerSet.getMerStlPay();
				double nbsmAmt = couponMerSet.getNbsmAmt();
				double paybackAmt = couponMerSet.getPaybackAmt();
				double billSuccAmtm = couponMerSet.getBillSuccAmtm();
				if(auditCouponMerSet.getMerStlPay()!=Const.COUPON_MERSET_INIT_NUM){
					merStlPay = auditCouponMerSet.getMerStlPay();
				}
				if(auditCouponMerSet.getNbsmAmt()!=Const.COUPON_MERSET_INIT_NUM){
					nbsmAmt = auditCouponMerSet.getNbsmAmt();
				}
				if(auditCouponMerSet.getPaybackAmt()!=Const.COUPON_MERSET_INIT_NUM){
					paybackAmt = auditCouponMerSet.getPaybackAmt();
				}
				if(auditCouponMerSet.getBillSuccAmtm()!=Const.COUPON_MERSET_INIT_NUM){
					billSuccAmtm = auditCouponMerSet.getBillSuccAmtm();
				}
				if(billSuccAmtm==0){
					couponMerSet.setExt1(0);
				}else{
					couponMerSet.setExt1((merStlPay+nbsmAmt+paybackAmt)/billSuccAmtm*100);
				}
				
				modelMap.addAttribute("couponMerSet", couponMerSet);
				modelMap.addAttribute("auditCouponMerSet", auditCouponMerSet);
				modelMap.addAttribute("auditId", audit.getId());
			}
			return "stl/auditXE";
		}
		return null;
	}

	/**
	 * 跳转到修改页面
	 * @Title: toMod
	 * @Description: 
	 * @param billType
	 * @param accId
	 * @param stlCycle
	 * @param stlDate
	 * @param goodsId
	 * @param modelMap
	 * @return
	 * @return String
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:38:55
	 */
	@RequestMapping(value = "/toMod")
	public String toMod(int billType, String accId, String accName, String stlCycle, String stlDate, String goodsId,String settStatus, ModelMap modelMap){
//		modelMap.addAttribute("accId", accId);
//		modelMap.addAttribute("accName", accName);
//		modelMap.addAttribute("stlCycle", stlCycle);
//		modelMap.addAttribute("stlDate", stlDate);
//		modelMap.addAttribute("goodsId", goodsId);
//		modelMap.addAttribute("settStatus", settStatus);
		modelMap.addAttribute("madeDate", madeDate);
		modelMap.addAttribute("billCycle", getBillCycle(stlDate));
		if (billType == 1) {
			CouponMerSet couponMerSet = stlService.getQW(accId, stlCycle, stlDate, goodsId,null);
			modelMap.addAttribute("couponMerSet", couponMerSet);
			double totalAmt = 0;
			if(couponMerSet!=null){
				totalAmt = totalAmt + couponMerSet.getMerStlAmt();
			}
			modelMap.addAttribute("totalAmt", totalAmt);
			return "stl/modQW";
		}
		if (billType == 2) {
			CouponMerSet couponMerSet = stlService.getSW(accId, stlCycle, stlDate, goodsId,null);
			modelMap.addAttribute("couponMerSet", couponMerSet);
			return "stl/modSW";
		}
		if (billType == 3) {
			CouponMerSet couponMerSet = stlService.getXE(accId, stlCycle, stlDate, goodsId,null);
			modelMap.addAttribute("couponMerSet", couponMerSet);
			return "stl/modXE";
		}
		return null;
	}

	/**
	 * 账单确认
	 * @Title: sureBill
	 * @Description: 
	 * @param accId
	 * @param stlCycle
	 * @param stlDate
	 * @param goodsId
	 * @param billType
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:38:47
	 */
	@RequestMapping(value = "/sureBill")
	public ModelAndView sureBill(String accId,String accName, String stlCycle, String stlDate, String goodsId, String billType, String settStatus)
			throws Exception {
		// User user = this.getUser();
		String msg = "0";
		String logFlag = "FAIL";
		int count = 0;
		try {
			count = stlService.sureBill(accId, stlCycle, stlDate, goodsId, billType, settStatus);
			if(count >0){
				logFlag = "SUCC";
				msg = "1";
			}
		} catch (Exception e) {
			log.error(StringUtil.ExceptionToString(e));
		} finally{
			this.addLog(stlDate, goodsId, stlCycle, accId, accName, "账单{商户:" + accName + "(" + accId + "),子商户:" + goodsId
					+ ",结算周期:" + stlDate + ",结算帐期:" + stlCycle + "}修改", "CONFIRM", logFlag);
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	@RequestMapping(value = "/closeBill")
	public ModelAndView closeBill(String accId,String accName, String stlCycle, String stlDate, String goodsId, String billType, String settStatus)
		throws Exception {
		// User user = this.getUser();
		String msg = "0";
		String logFlag = "FAIL";
		int count = 0;
		try {
			count = stlService.closeBill(accId, stlCycle, stlDate, goodsId, billType, settStatus);
			if(count >0){
				logFlag = "SUCC";
				msg = "1";
			}
		} catch (Exception e) {
			log.error(StringUtil.ExceptionToString(e));
		} finally{
			this.addLog(stlDate, goodsId, stlCycle, accId, accName, "账单{商户:" + accName + "(" + accId + "),子商户:" + goodsId
					+ ",结算周期:" + stlDate + ",结算帐期:" + stlCycle + "}修改", "CONFIRM", logFlag);
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	@RequestMapping(value = "/invalidBill")
	public ModelAndView invalidBill(String accId,String accName, String stlCycle, String stlDate, String goodsId, String billType, String settStatus)
		throws Exception {
	// User user = this.getUser();
		String msg = "0";
		String logFlag = "FAIL";
		int count = 0;
		try {
			count = stlService.invalidBill(accId, stlCycle, stlDate, goodsId, billType, settStatus);
			if(count >0){
				logFlag = "SUCC";
				msg = "1";
			}
		} catch (Exception e) {
			log.error(StringUtil.ExceptionToString(e));
		} finally{
			this.addLog(stlDate, goodsId, stlCycle, accId, accName, "账单{商户:" + accName + "(" + accId + "),子商户:" + goodsId
					+ ",结算周期:" + stlDate + ",结算帐期:" + stlCycle + "}修改", "CONFIRM", logFlag);
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * 列表页面查询
	 * @Title: query
	 * @Description: 
	 * @return
	 * @return ModelAndView
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-10 下午04:21:46
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		HashMap map = (HashMap)getParametersFromRequest(super.getHttpRequest());
		String loginName = this.getUser()==null?"":this.getUser().getLoginName();
		if(!"admin".equals(loginName)){
			map.put("is_valid", "2");
		}
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
				ObjectUtil.trimData(data);
				transQueryList(data);
				long count = queryCount(queryKey, map, true);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else {
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	/**
	 * 列表页面导出
	 * @Title: export
	 * @Description: 
	 * @return
	 * @return ModelAndView
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:38:32
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		@SuppressWarnings("unused")
		String msg = "";
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = query(queryKey, map, true);
				// 格式化数据
				ObjectUtil.trimData(data);
				List<Map<String,Object>> list = transQueryList(data);
				String[] title = {"stldate:结算周期","stlcycleName:结算账期","accid:商户号","accname:商户名称","goodsid:子商户号","succnum:成功交易笔数:number","succamt:成功交易金额:number","muteamt:核减金额:number","nbsmamt:不均衡金额:number","paybackamt:退费金额:number","merstlpay:结算金额:number","settstatusName:结算状态","stateName:审核状态"};// 设置表头
				String remark = "统一管理平台结算数据查询";
				String reportName = "统一管理平台结算数据查询";
				Map<String,Object> modelMap = ExportUtil.getExportModel(list, title,remark,reportName);
				return new ModelAndView("xlsView", "modelMap", modelMap);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else {
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 修改账单值
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(CouponMerSet couponMerSet) throws Exception {
	String modUser = this.getUser().getName();
	String logFlag = "FAIL";
	String msg = "0";
	if(couponMerSet!=null){
		couponMerSet.setModUser(modUser);
		try{
			stlService.updateStatus(couponMerSet);
			logFlag = "SUCC";
			msg = "1";
		}catch (Exception e) {
			log.error(StringUtil.ExceptionToString(e));
		} finally{
			this.addLog(couponMerSet.getStlDate(), couponMerSet.getGoodsId(), couponMerSet.getStlCycle()+"", couponMerSet.getAccId(), couponMerSet.getAccName(), "账单{商户:" + couponMerSet.getAccName() + "(" + couponMerSet.getAccId() + "),子商户:" + couponMerSet.getGoodsId()
					+ ",结算周期:" + couponMerSet.getStlDate() + ",结算帐期:" + couponMerSet.getStlCycle() + "}修改", "MODIFY", logFlag);
		}
	}
	return new ModelAndView("jsonView", "ajax_json", msg);
}
	
	@RequestMapping(value = "/updateIsvalid")
	public ModelAndView updateIsvalid(CouponMerSet couponMerSet)
		throws Exception {
	// User user = this.getUser();
		String msg = "0";
		String logFlag = "FAIL";
		int count = 0;
		try {
			count = stlService.updateIsvalid(couponMerSet);
			if(count >0){
				logFlag = "SUCC";
				msg = "1";
			}
		} catch (Exception e) {
			log.error(StringUtil.ExceptionToString(e));
		} finally{
			this.addLog(couponMerSet.getStlDate(), couponMerSet.getGoodsId(), couponMerSet.getStlCycle()+"", couponMerSet.getAccId(), couponMerSet.getAccName(), "账单{商户:" + couponMerSet.getAccName() + "(" + couponMerSet.getAccId() + "),子商户:" + couponMerSet.getGoodsId()
					+ ",结算周期:" + couponMerSet.getStlDate() + ",结算帐期:" + couponMerSet.getStlCycle() +  ",是否隐藏:" + couponMerSet.getIs_valid() + "}修改", "MODIFY", logFlag);
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	

	/**
	 * 审核通过
	 * @Title: auditPass
	 * @Description: 
	 * @param auditId
	 * @param auditId0
	 * @param resultDesc
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:38:07
	 */
	@RequestMapping(value = "/auditPass")
	public ModelAndView auditPass(String auditId,String resultDesc)
			throws Exception {
		// User user = this.getUser();
		String msg = "0";
		try {
			stlService.auditPass(auditId,resultDesc);
			msg = "1";
		} catch (Exception e) {
			log.error(StringUtil.ExceptionToString(e));
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	/**
	 * 审核不通过
	 * @Title: auditNotPass
	 * @Description: 
	 * @param auditId
	 * @param auditId0
	 * @param resultDesc
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:38:00
	 */
	@RequestMapping(value = "/auditNotPass")
	public ModelAndView auditNotPass(String auditId,String resultDesc)
			throws Exception {
		// User user = this.getUser();
		String msg = "0";
		try {
			stlService.auditNotPass(auditId,resultDesc);
			msg = "1";
		} catch (Exception e) {
			log.error(StringUtil.ExceptionToString(e));
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: allBillExport
	 * @Description: 全网账单导出
	 * @return
	 * @author wanyong
	 * @date 2013-12-17 下午5:42:06
	 */
	@RequestMapping(value = "/allbillexport")
	public ModelAndView allBillExport(String accId, String stlCycle, String stlDate, String goodsId) {
		CouponMerSet localCms = stlService.getQW(accId, stlCycle, stlDate, goodsId,null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOCALCMS", localCms);
		return new ModelAndView("stlAllBillExcelView", "excel", map);
	}

	/**
	 * @Title: allBillExport
	 * @Description: 北京小额账单导出
	 * @return
	 * @author wanyong
	 * @date 2013-12-17 下午5:42:06
	 */
	@RequestMapping(value = "/bjbillexport")
	public ModelAndView bjBillExport(CouponMerSet couponMerSet) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("COUPONMERSET", couponMerSet);
		return new ModelAndView("stlBjBillExcelView", "excel", map);
	}

	/**
	 * @Title: swBillExport
	 * @Description: 省网账单导出
	 * @return
	 * @author wanyong
	 * @date 2013-12-17 下午5:42:06
	 */
	@RequestMapping(value = "/swbillexport")
	public ModelAndView swBillExport(CouponMerSet couponMerSet) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("COUPONMERSET", couponMerSet);
		return new ModelAndView("stlSwBillExcelView", "excel", map);
	}
	
	/**
	 * 结算账期Map
	 * @Title: getStlCycle
	 * @Description: 
	 * @return
	 * @return Map<String,String>
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-24 下午08:19:59
	 */
	public Map<String,String> getStlCycle(){
		Map<String,String> m = new TreeMap<String, String>();
		m.put("", "全部");
		m.put("1", "月底");
		m.put("2", "月7日");
		m.put("3", "周结");
		m.put("4", "日结");
		m.put("5", "单商户结算");
		return m;
	}
	
	/**
	 * 结算状态Map
	 * @Title: getSettStatus
	 * @Description: 
	 * @return
	 * @return Map<String,String>
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-24 下午08:24:24
	 */
	public Map<String,String> getSettStatus(){
		Map<String,String> m = new TreeMap<String, String>();
		m.put("", "全部");
		m.put("0", "待运营确认");
		m.put("1", "待商户确认");
		m.put("11", "账单修改中");
		m.put("2", "商户已确认");
		m.put("21", "账单修改中(自服务：账单修改中)");
		m.put("22", "待运营确认(自服务：账单修改中)");
		m.put("3", "付款中");
		m.put("4", "已付款");
		m.put("5", "已关闭");
		m.put("6", "已作废");
		return m;
	}
	
	/**
	 * 审核状态Map
	 * @Title: getState
	 * @Description: 
	 * @return
	 * @return Map<String,String>
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:37:43
	 */
	public Map<String,String> getState(){
		Map<String,String> m = new TreeMap<String, String>();
		m.put("", "全部");
		m.put("0", "待审核");
		m.put("1", "审核未通过");
		m.put("2", "审核通过");
		return m;
	}
	
	/**
	 * 翻译显示数据
	 * @Title: tranQueryList
	 * @Description: 
	 * @param data
	 * @return void
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-24 下午08:39:01
	 */
	public List<Map<String, Object>> transQueryList(List<Map<String, Object>> data){
		for(Map<String,Object> m: data){
			m.put("stlCycleName", this.getStlCycle().get(m.get("STLCYCLE").toString()));
			m.put("settStatusName", this.getSettStatus().get(m.get("SETTSTATUS").toString()));
			m.put("stateName", this.getState().get(m.get("STATE").toString()));
			 DecimalFormat df1 = new DecimalFormat("0.00");
			 m.put("SUCCAMT", df1.format(m.get("SUCCAMT")));
			 m.put("MUTEAMT", df1.format(m.get("MUTEAMT")));
			 m.put("NBSMAMT", df1.format(m.get("NBSMAMT")));
			 m.put("PAYBACKAMT", df1.format(m.get("PAYBACKAMT")));
			 m.put("MERSTLPAY", df1.format(m.get("MERSTLPAY")));
		}
		return data;
	}
	
	/**
	 * 审核状态下拉列表
	 * @Title: listState
	 * @Description: 
	 * @return
	 * @return ModelAndView
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:37:28
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listState")
	public ModelAndView listState() {
		JSONArray result = new JSONArray();
		Map<String, String> map = getState();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString().trim();
			String value = entry.getValue().toString().trim();
			result.add(OptionAction.buildSelect(key, value));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}
	
	/**
	 * 结账状态下拉列表
	 * @Title: listSettStatus
	 * @Description: 
	 * @return
	 * @return ModelAndView
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:37:20
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listSettStatus")
	public ModelAndView listSettStatus() {
		JSONArray result = new JSONArray();
		Map<String, String> map = getSettStatus();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString().trim();
			String value = entry.getValue().toString().trim();
			result.add(OptionAction.buildSelect(key, value));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}
	
	/**
	 * 结算账期下拉列表
	 * @Title: listStlCycle
	 * @Description: 
	 * @return
	 * @return ModelAndView
	 * @throws
	 * @author wangyuxin
	 * @date 2013-12-25 上午11:36:49
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listStlCycle")
	public ModelAndView listStlCycle() {
		JSONArray result = new JSONArray();
		Map<String, String> map = getStlCycle();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString().trim();
			String value = entry.getValue().toString().trim();
			result.add(OptionAction.buildSelect(key, value));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}
	
	private String getBillCycle(String date){
		String billCycle;
		try {
			billCycle = StringUtil.get11Date_SpaceXMonStart(date, 0)+ "至" + StringUtil.get11Date_SpaceXMonEnd(date, 0);
		} catch (Exception e) {
			billCycle  ="";
		}
		return billCycle;
	}
}
