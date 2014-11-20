package com.umpay.hfmng.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.model.CouponLog;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.CouponInfService;
import com.umpay.hfmng.service.CouponLogService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: CouponInfAction
 * @Description: 兑换券管理
 * @author wanyong
 * @date 2012-11-19 下午05:26:45
 */
@Controller
@RequestMapping("/couponinf")
public class CouponInfAction extends BaseAction {
	@Autowired
	private CouponInfService couponInfService;

	@Autowired
	private AuditService auditService;

	@Autowired
	private CouponLogService couponLogService;

	/**
	 * @Title: list
	 * @Description: 链接到兑换券管理页面
	 * @param
	 * @return String
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-11-19 下午05:36:54
	 */
	@RequestMapping(value = "/index")
	public String list(ModelMap modelMap) {
		// 查找按钮权限
		String opts = HfCacheUtil.getCache().getUrlAcl("couponinf");
		modelMap.addAttribute("opts", opts);
		return "couponinf/index";
	}

	/**
	 * 到兑换券操作日志页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/log")
	public String toLog() {
		return "couponinf/querylog";
	}

	/**
	 * @Title: add
	 * @Description: 链接到新增兑换券页面
	 * @param
	 * @return String
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-11-21 下午01:18:45
	 */
	@RequestMapping(value = "/add")
	public String add() {
		return "couponinf/addcoupon";
	}

	/**
	 * @Title: query
	 * @Description: 查询兑换券信息
	 * @param
	 * @return ModelAndView
	 * @author wanyong
	 * @date 2012-11-22 下午02:31:49
	 */
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> subMap : data) {
					// 渲染兑换券状态
					subMap.put("STATENAME", ParameterPool.couponInfStates.get(subMap.get("STATE").toString()));
					// 渲染审核状态
					subMap.put("AUDITSTATENAME", ParameterPool.auditStates.get(subMap.get("AUDITSTATE").toString()));
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
	 * @Title: save
	 * @Description: 新增兑换券信息
	 * @param couponInf
	 * @return ModelAndView
	 * @author wanyong
	 * @date 2012-11-22 下午04:55:00
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(CouponInf couponInf, ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			couponInf.setCouponId("CP" + SequenceUtil.getInstance().getSequence(Const.SEQ_FILENAME_COUPONINF, 6));
			couponInf.setSubmitUser(getUser().getId());
			/******** start 电子兑换券三期兑换券增加待启用状态2013-6-6 ************/
			// couponInf.setState(Const.COUPON_INFSTATE_NOENABLE); // 默认未启用
			couponInf.setState(Const.COUPON_INFSTATE_INIT); // 默认待启用
			/******** end ******************************************************/
			couponInf.setInTime(new Timestamp(System.currentTimeMillis()));
			couponInf.setModTime(new Timestamp(System.currentTimeMillis()));
			couponInfService.saveCouponAudit(couponInf);

			CouponLog log = new CouponLog();
			log.setBusinessobject("umpay.T_COUPON_INF");
			log.setOpertype(Const.LOG_OPT_CREATE);
			log.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log.setIxdata1(couponInf.getCouponId()); // 兑换券编号
			log.setIxdata2(new SimpleDateFormat("yyyy-MM-dd").format(new Date())); // 操作日期
			log.setIxdata3(LoginUtil.getUser().getId()); // 操作员ID
			log.setIxdata4(Const.LOG_OPT_CREATE); // 操作类型
			log.setIxdata5(couponInf.getCouponName()); // 兑换券名称

			String optdata = "兑换券添加，添加内容为：【兑换券ID:" + couponInf.getCouponId() + ";兑换券名称:" + couponInf.getCouponName()
					+ ";兑换券说明:" + couponInf.getRemark() + "】";
			log.setOperdata(optdata);
			couponLogService.addLog(log);
			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: detail
	 * @Description: 链接到兑换券详情页面
	 * @param couponId
	 *            兑换券编号
	 * @param modeMap
	 * @return String
	 * @author wanyong
	 * @date 2012-11-22 下午09:55:12
	 */
	@RequestMapping(value = "/detail")
	public String detail(String couponId, String auditId, ModelMap modelMap) {
		CouponInf couponInf = null;
		if ("null".equals(couponId)) {
			// 从审核表取出兑换券信息
			Map<String, String> whereMap = new HashMap<String, String>();
			whereMap.put("id", auditId);
			Audit audit = auditService.load(whereMap);
			couponInf = (CouponInf) JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(), CouponInf.class);
		} else {
			// 从兑换券表取出兑换券信息
			couponInf = couponInfService.loadCouponInf(couponId);
		}
		couponInf.setStateName(ParameterPool.couponInfStates.get(couponInf.getState().toString())); // 页面显示使用stateName字符串型字段
		couponInf.setCouponType(ParameterPool.couponInfTypes.get(couponInf.getCouponType()));
		modelMap.addAttribute("couponInf", couponInf);
		return "couponinf/detailcoupon";
	}

	/**
	 * @Title: modify
	 * @Description: 链接到兑换券修改页面
	 * @param couponId
	 *            兑换券编号
	 * @param modelMap
	 * @return String
	 * @author wanyong
	 * @date 2012-11-23 下午08:09:31
	 */
	@RequestMapping(value = "/modify")
	public String modify(String couponId, String auditId, ModelMap modelMap) {
		boolean stateReadOnly = false; // 默认启用状态可以修改
		CouponInf couponInf = null;
		if ("null".equals(couponId)) {
			// 从审核表取出兑换券信息
			Map<String, String> whereMap = new HashMap<String, String>();
			whereMap.put("id", auditId);
			Audit audit = auditService.load(whereMap);
			couponInf = (CouponInf) JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(), CouponInf.class);
			// 新建待审核状态时状态不能修改
			stateReadOnly = true;
		} else {
			// 从兑换券表取出兑换券信息
			couponInf = couponInfService.loadCouponInf(couponId);
		}
		modelMap.addAttribute("couponInf", couponInf);
		modelMap.addAttribute("stateReadOnly", stateReadOnly);
		return "couponinf/modifycoupon";
	}

	/**
	 * @Title: update
	 * @Description: 修改兑换券信息
	 * @param
	 * @param couponInf
	 * @return
	 * @author wanyong
	 * @date 2012-11-24 下午01:20:06
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(CouponInf couponInf, String operdata, ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			couponInf.trim();
			couponInf.setSubmitUser(getUser().getId()); // 修改人
			couponInfService.modifyCouponInf(couponInf);

			CouponLog log = new CouponLog();
			log.setBusinessobject("umpay.T_COUPON_INF");
			log.setOpertype(Const.LOG_OPT_MODIFY);
			log.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log.setIxdata1(couponInf.getCouponId()); // 兑换券编号
			log.setIxdata2(new SimpleDateFormat("yyyy-MM-dd").format(new Date())); // 操作日期
			log.setIxdata3(LoginUtil.getUser().getId()); // 操作员ID
			log.setIxdata4(Const.LOG_OPT_MODIFY); // 操作类型
			log.setIxdata5(couponInf.getCouponName()); // 兑换券名称

			log.setOperdata(operdata);
			couponLogService.addLog(log);
			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: auditDetail
	 * @Description: 链接到审核页面
	 * @param couponId
	 * @param modelMap
	 * @return
	 * @author wanyong
	 * @date 2012-11-26 下午12:21:39
	 */
	@RequestMapping(value = "/auditDetail")
	public String auditDetail(String auditId, ModelMap modelMap) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", auditId);
		Audit audit = auditService.load(map);
		// 反序列化审核信息
		CouponInf auditCouponInf = (CouponInf) JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(), CouponInf.class);
		auditCouponInf.trim();
		// 渲染数据
		auditCouponInf.setCouponType(ParameterPool.couponInfTypes.get(auditCouponInf.getCouponType()));
		auditCouponInf.setStateName(ParameterPool.couponInfStates.get(auditCouponInf.getState().toString()));
		modelMap.addAttribute("auditCouponInf", auditCouponInf);
		modelMap.addAttribute("auditId", auditId);

		// 如果审核类型为修改, 需获取历史数据展示到页面
		if ("2".equals(audit.getAuditType())) {
			CouponInf useCouponInf = couponInfService.loadCouponInf(auditCouponInf.getCouponId());
			// 渲染数据
			useCouponInf.setCouponType(ParameterPool.couponInfTypes.get(useCouponInf.getCouponType()));
			useCouponInf.setStateName(ParameterPool.couponInfStates.get(useCouponInf.getState().toString()));
			modelMap.addAttribute("useCouponInf", useCouponInf);
			return "couponinf/auditmod";
		}
		return "couponinf/auditadd";
	}

	/**
	 * @Title: auditNoPass
	 * @Description: 审核不通过处理
	 * @param auditId
	 * @param resultDesc
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2012-12-3 下午02:45:07
	 */
	@RequestMapping(value = "/auditNoPass")
	public ModelAndView auditNoPass(String auditId, String couponId, String resultDesc, ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", auditId);
			Audit audit = auditService.load(map);
			audit.trim();
			audit.setState(Const.AUDIT_STATE_NOPASS); // 审核状态改为不通过
			audit.setModTime(new Timestamp(System.currentTimeMillis()));
			audit.setModUser(getUser().getId());
			audit.setResultDesc(resultDesc);
			auditService.couponAuditNotPass(audit);

			CouponLog log = new CouponLog();
			log.setBusinessobject("umpay.T_COUPON_INF");
			log.setOpertype(Const.LOG_OPT_AUDIT);
			log.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log.setIxdata1(couponId); // 兑换券编号
			log.setIxdata2(new SimpleDateFormat("yyyy-MM-dd").format(new Date())); // 操作日期
			log.setIxdata3(LoginUtil.getUser().getId()); // 操作员ID
			log.setIxdata4(Const.LOG_OPT_AUDIT); // 操作类型

			String optdata = "兑换券审核，审核状态为：【审核不通过 】；审核意见：" + audit.getResultDesc();
			log.setOperdata(optdata);
			couponLogService.addLog(log);
			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: auditPass
	 * @Description: 审核通过处理
	 * @param auditId
	 * @param resultDesc
	 * @param modeMap
	 * @return
	 * @throws Exception
	 * @author wanyong
	 * @date 2012-12-3 下午02:50:34
	 */
	@RequestMapping(value = "/auditPass")
	public ModelAndView auditPass(String auditId, String couponId, String resultDesc, ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", auditId);
			Audit audit = auditService.load(map);
			audit.trim(); // 去空格
			audit.setState(Const.AUDIT_STATE_PASS); // 审核状态改为通过
			audit.setModTime(new Timestamp(System.currentTimeMillis()));
			audit.setModUser(getUser().getId());
			if (resultDesc != null) {
				audit.setResultDesc(resultDesc);
			}
			auditService.couponAuditPass(audit);

			CouponLog log = new CouponLog();
			log.setBusinessobject("umpay.T_COUPON_INF");
			log.setOpertype(Const.LOG_OPT_AUDIT);
			log.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log.setIxdata1(couponId); // 兑换券编号
			log.setIxdata2(new SimpleDateFormat("yyyy-MM-dd").format(new Date())); // 操作日期
			log.setIxdata3(LoginUtil.getUser().getId()); // 操作员ID
			log.setIxdata4(Const.LOG_OPT_AUDIT); // 操作类型

			String optdata = "兑换券审核，审核状态为：审核通过 ；审核意见  " + audit.getResultDesc();
			log.setOperdata(optdata);
			couponLogService.addLog(log);
			msg = "1"; // 成功
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * 查询兑换券操作日志
	 * 
	 * @return
	 */
	@RequestMapping(value = "/querylog")
	public ModelAndView queryLog() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> subMap : data) {
					// 渲染兑换券状态
					subMap.put("opertype", ParameterPool.couponLogOptTypes.get(subMap.get("opertype").toString()));
					if (subMap.get("COUPONNAME") == null || "".equals(subMap.get("COUPONNAME").toString())) {
						// 渲染兑换券名称
						CouponInf couponInf = (CouponInf) HfCacheUtil.getCache().getCouponInfMap()
								.get(subMap.get("COUPONID").toString());
						subMap.put("COUPONNAME", couponInf == null ? "" : couponInf.getCouponName());
					}

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
}
