package com.umpay.hfmng.action;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.umpay.hfmng.common.FileUtil;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.HfCodec;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.LoginUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.RandomUtil;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.CouponCode;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.model.CouponLog;
import com.umpay.hfmng.model.CouponRMG;
import com.umpay.hfmng.model.CouponRule;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.CouponLogService;
import com.umpay.hfmng.service.CouponRMGService;
import com.umpay.hfmng.service.CouponRuleService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: CouponRuleAction
 * @Description: 兑换券规则管理
 * @author wanyong
 * @date 2012-12-16 下午10:03:03
 */
@Controller
@RequestMapping("/couponrule")
public class CouponRuleAction extends BaseAction {
	@Autowired
	private CouponRuleService couponRuleService;

	@Autowired
	private AuditService auditService;

	@Autowired
	private CouponLogService couponLogService;

	@Autowired
	private CouponRMGService couponRMGService;
	/**
	 * @Title: list
	 * @Description: 链接到兑换券规则管理页面
	 * @return String
	 * @author wanyong
	 * @date 2012-12-16 下午10:04:54
	 */
	@RequestMapping(value = "/index")
	public String list(ModelMap modelMap) {
		// 查找按钮权限
		String opts = HfCacheUtil.getCache().getUrlAcl("couponrule");
		modelMap.addAttribute("opts", opts);
		return "couponrule/index";
	}

	/**
	 * 到兑换券规则操作日志页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/log")
	public String toLog() {
		return "couponrule/querylog";
	}

	/**
	 * @Title: add
	 * @Description: 链接到新增兑换券规则页面
	 * @param
	 * @return String
	 * @throws DataAccessException
	 * @author wanyong
	 * @date 2012-11-21 下午01:18:45
	 */
	@RequestMapping(value = "/add")
	public String add(ModelMap modelMap) {
		CouponRule couponRule = new CouponRule();
		// 设置【兑换码形式】字段默认值：电子码
		couponRule.setCouponCodeType(Const.COUPON_CODEMODEL_DZM);
		// 设置【兑换码生成方式】字段默认值：本平台全数字自动生成
		couponRule.setGenerateMethod(Const.COUPON_CODEGENMETHOD_LOCALN);
		// 设置【兑换码有效期类型】字段默认值
		couponRule.setEffType(Const.COUPON_CODEEFFTYPE_DAY);
		// 设置【库存告警阀值】字段默认值
		couponRule.setSwitchAmt(Integer.parseInt(ParameterPool.couponBusiBackParas.get("INVTHRDEF")));
		modelMap.addAttribute("couponRule", couponRule);
		// 设置【兑换码生成方式】：第三方导入字段到request中
		modelMap.addAttribute("codeGenerateMethodUnLocal", Const.COUPON_CODEGENMETHOD_UNLOCAL);

		return "couponrule/addcouponrule";
	}

	/**
	 * @Title: query
	 * @Description: 查询兑换券规则信息
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2012-12-17 上午10:44:37
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
				for (Map<String, Object> map2 : data) {
					String couponId = null;
					String merId = null;
					String goodsId = null;
					if (map2.get("RULEID") == null) {
						CouponRule couponRule = (CouponRule) JsonHFUtil.getObjFromJsonArrStr1(map2.get("MODDATA")
								.toString(), CouponRule.class);
						couponId = couponRule.getCouponId();
						merId = couponRule.getMerId();
						goodsId = couponRule.getMerId() + "-" + couponRule.getGoodsId();
					} else {
						couponId = map2.get("COUPONID").toString();
						merId = map2.get("MERID").toString();
						goodsId = map2.get("MERID").toString() + "-" + map2.get("GOODSID").toString();
					}
					CouponInf couponInf = (CouponInf) HfCacheUtil.getCache().getCouponInfMap().get(couponId);
					map2.put("COUPONNAME", null == couponInf ? null : couponInf.getCouponName());

					MerInfo merInfo = (MerInfo) HfCacheUtil.getCache().getMerInfoMap().get(merId);
					map2.put("MERNAME", null == merInfo ? null : merInfo.getMerName());

					GoodsInfo goodsInfo = (GoodsInfo) HfCacheUtil.getCache().getGoodsInfoMap().get(goodsId);
					map2.put("GOODSNAME", null == goodsInfo ? null : goodsInfo.getGoodsName());
					if (map2.get("STATE") != null)
						map2.put("STATENAME", ParameterPool.couponRuleStates.get(map2.get("STATE").toString()));
					if (map2.get("AUDITSTATE") != null)
						map2.put("AUDITSTATENAME", ParameterPool.auditStates.get(map2.get("AUDITSTATE").toString()));
				}

				long count = queryCount(queryKey, map);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
					e.printStackTrace();
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

	
	@RequestMapping(value = "/checkProgress")
	public ModelAndView checkProgress(String ruleid) {
		String msg = "";
		if(ruleid == null || "".equals(ruleid.trim())){
			msg = "0"; //参数不全
		}else{
			Boolean isfinish = (Boolean)this.getHttpSession().getAttribute("RULE_" + ruleid.trim());
			if(isfinish == null){
				msg = "2";  //处理中
			}else if(isfinish){
				msg = "1"; //处理完成
				this.getHttpSession().removeAttribute("RULE_" + ruleid.trim());
			}else{
				msg = "3"; //导出异常
			}
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	/**
	 * @Title: save
	 * @Description: 新增兑换券规则信息
	 * @param
	 * @param couponRule
	 * @param modeMap
	 * @return
	 * @throws Exception
	 * @author wanyong
	 * @date 2012-12-17 下午05:51:34
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(CouponRule couponRule, Integer userange, String[] merList, String objmer, String objgoods, ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			couponRule.setState(Const.COUPON_RULESTATE_NOENABLE); // 状态为：未启用
			couponRule.setSubmitUser(getUser().getId());
			couponRule.setAlarmCount(Integer.parseInt(ParameterPool.couponBusiBackParas.get("ALARMCOUNTDEF"))); // 剩余告警次
			couponRule.setInTime(new Timestamp(System.currentTimeMillis()));
			couponRule.setModTime(new Timestamp(System.currentTimeMillis()));
			couponRule.setExport(1);
			List<CouponRMG> list = null;
			if(userange != null){
				if(userange == 0){
					list = new ArrayList<CouponRMG>();
					CouponRMG rmg = new CouponRMG();
					rmg.setMerid("ALLMER");
					rmg.setGoodsid("ALLGOODS");
					rmg.setModuser(this.getUser().getId());
					Timestamp now = new Timestamp(System.currentTimeMillis());
					rmg.setModtime(now);
					rmg.setIntime(now);
					rmg.setState(2);
					list.add(rmg);
				}else if(userange == 1){
					if(merList != null && merList.length > 0){
						list = new ArrayList<CouponRMG>();
						Timestamp now = new Timestamp(System.currentTimeMillis());
						for(String mer : merList){
							CouponRMG rmg = new CouponRMG();
							rmg.setMerid(mer);
							rmg.setGoodsid("ALLGOODS");
							rmg.setModuser(this.getUser().getId());
							rmg.setModtime(now);
							rmg.setIntime(now);
							rmg.setState(2);
							list.add(rmg);
						}
					}
				}else if(userange == 2){
					if(objmer != null && !"".equals(objmer) && objgoods != null && !"".equals(objgoods)){
						list = new ArrayList<CouponRMG>();
						CouponRMG rmg = new CouponRMG();
						rmg.setMerid(objmer);
						rmg.setGoodsid(objgoods);
						rmg.setModuser(this.getUser().getId());
						Timestamp now = new Timestamp(System.currentTimeMillis());
						rmg.setModtime(now);
						rmg.setIntime(now);
						rmg.setState(2);
						list.add(rmg);
					}
				}
			}
			couponRuleService.saveCouponAudit(couponRule, list);

			CouponLog log = new CouponLog();
			log.setBusinessobject("umpay.T_COUPON_RULE");
			log.setOpertype(Const.LOG_OPT_CREATE);
			log.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log.setIxdata1(couponRule.getCouponId()); // 兑换券编号
			log.setIxdata2(couponRule.getMerId()); // 商户编号
			log.setIxdata3(LoginUtil.getUser().getId()); // 操作员ID
			log.setIxdata4(couponRule.getGoodsId()); // 商品编号

			/***** start 从缓存中获取ID对应的名称 *************/
			CouponInf couponInf = (CouponInf) HfCacheUtil.getCache().getCouponInfMap().get(couponRule.getCouponId());
			MerInfo merInfo = (MerInfo) HfCacheUtil.getCache().getMerInfoMap().get(couponRule.getMerId());
			if (merInfo != null)
				merInfo.trim();
			GoodsInfo goodsInfo = (GoodsInfo) HfCacheUtil.getCache().getGoodsInfoMap()
					.get(merInfo.getMerId() + "-" + couponRule.getGoodsId());
			if (goodsInfo != null)
				goodsInfo.trim();
			/******* end ************************************/
			String optdata = "商户兑换券规则添加，添加内容为：兑换券规则ID：" + couponRule.getRuleId() + "、兑换券：" + couponInf.getCouponName()
					+ "（" + couponRule.getCouponId() + "）" + "、商户：" + merInfo.getMerName() + "（"
					+ couponRule.getMerId() + "）" + "、商品：" + goodsInfo.getGoodsName() + "（" + couponRule.getGoodsId()
					+ "）" + "、兑换码形式：" + ParameterPool.couponCodeTypes.get(couponRule.getCouponCodeType().toString())
					+ "、兑换码生成方式：" + ParameterPool.couponCodeMethod.get(couponRule.getGenerateMethod().toString())
					+ "、库存告警阀值：" + couponRule.getSwitchAmt() + "、库存告警邮件地址：" + couponRule.getAlarmEmail()
					+ "、库存告警短信手机号：" + couponRule.getAlarmMobile() + "、码过期提醒天数：" + couponRule.getEffWarnDays();
			log.setOperdata(optdata);
			couponLogService.addLog(log);
			msg = "1";
		} catch (BusinessException businessException) {
			businessException.printStackTrace();
			msg = businessException.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: modify
	 * @Description: 链接到兑换券规则修改页面
	 * @param
	 * @param ruleId
	 * @param modelMap
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 上午10:50:50
	 */
	@RequestMapping(value = "/modify")
	public String modify(String ruleId, String auditId, ModelMap modelMap) {
		boolean stateReadOnly = false; // 默认启用状态可以修改
		CouponRule couponRule = null;
		if ("null".equals(ruleId)) {
			// 从审核表取出兑换券规则信息
			Map<String, String> whereMap = new HashMap<String, String>();
			whereMap.put("id", auditId);
			Audit audit = auditService.load(whereMap);
			couponRule = (CouponRule) JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(), CouponRule.class);
			// 新建待审核状态时状态不能修改
			stateReadOnly = true;
		} else {
			// 从兑换券规则表取出兑换券规则信息
			couponRule = couponRuleService.load(ruleId);
		}
		/******** start 从缓存中获取ID对应的名称 *******************/
		Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
		CouponInf couponInf = (CouponInf) couponInfMap.get(couponRule.getCouponId());
		Map<String, Object> merInfoMap = HfCacheUtil.getCache().getMerInfoMap();
		MerInfo merInfo = (MerInfo) merInfoMap.get(couponRule.getMerId());
		Map<String, Object> goodsInfoMap = HfCacheUtil.getCache().getGoodsInfoMap();
		GoodsInfo goodsInfo = (GoodsInfo) goodsInfoMap.get(couponRule.getMerId() + "-" + couponRule.getGoodsId());
		couponRule.setCouponName(null == couponInf ? null : couponInf.getCouponName());
		couponRule.setMerName(null == merInfo ? null : merInfo.getMerName());
		couponRule.setGoodsName(null == goodsInfo ? null : goodsInfo.getGoodsName());
		/******** end 从缓存中获取ID对应的名称 *******************/

		// 渲染数据
		couponRule.setStateName(ParameterPool.couponRuleStates.get(couponRule.getState().toString())); // 页面显示使用stateName字符串型字段
		couponRule.setCouponCodeTypeName(ParameterPool.couponCodeTypes.get(couponRule.getCouponCodeType().toString())); // 页面显示使用couponCodeTypeName字符串型字段
		couponRule.setGenerateMethodName(ParameterPool.couponCodeMethod.get(couponRule.getGenerateMethod().toString())); // 页面显示使用generateMethodName字符串型字段
		couponRule.setEffTypeName(ParameterPool.couponCodeEffTypes.get(couponRule.getEffType().toString())); // 页面显示使用effTypeName字符串型字段
		modelMap.addAttribute("couponRule", couponRule);
		modelMap.addAttribute("stateReadOnly", stateReadOnly);
		if(couponRule.getConsumetype() != null && couponRule.getConsumetype() == 2){
			try {
				List<CouponRMG> list = couponRMGService.queryRMG(couponRule.getRuleId());
				if(list != null && list.size() > 0){
					for(CouponRMG rmg : list){//从缓存中设置商户名称,商品名称
						String merid = rmg.getMerid().trim();
						String goodsid = rmg.getGoodsid().trim();
						if(merid.equalsIgnoreCase("ALLMER")){
							rmg.setMername("全部商户");
						}else{
							MerInfo info = (MerInfo) merInfoMap.get(merid);
							rmg.setMername(info.getMerName() + "（" + merid + "）");
						}
						if(goodsid.equalsIgnoreCase("ALLGOODS")){
							rmg.setGoodsname("可兑换商户的全部商品");
						}else{
							GoodsInfo info = (GoodsInfo) goodsInfoMap.get(merid + "-" + goodsid);
							rmg.setGoodsname(info.getGoodsName() + "（" + goodsid + "）");
						}
					}
				}
				modelMap.addAttribute("list", list);
			} catch (Exception e) {
				return "couponrule/exporterror";
			}
		}
		return "couponrule/modifycouponrule";
	}

	/**
	 * @Title: detail
	 * @Description: 链接到兑换券规则详情页面
	 * @param
	 * @param ruleId
	 * @param modelMap
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 上午10:50:27
	 */
	@RequestMapping(value = "/detail")
	public String detail(String ruleId, String auditId, ModelMap modelMap) {
		CouponRule couponRule = null;
		if ("null".equals(ruleId)) {
			// 从审核表取出兑换券规则信息
			Map<String, String> whereMap = new HashMap<String, String>();
			whereMap.put("id", auditId);
			Audit audit = auditService.load(whereMap);
			couponRule = (CouponRule) JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(), CouponRule.class);
		} else {
			// 从兑换券规则表取出兑换券规则信息
			couponRule = couponRuleService.load(ruleId);
		}
		/******** start 从缓存中获取ID对应的名称 *******************/
		Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
		CouponInf couponInf = (CouponInf) couponInfMap.get(couponRule.getCouponId());
		Map<String, Object> merInfoMap = HfCacheUtil.getCache().getMerInfoMap();
		MerInfo merInfo = (MerInfo) merInfoMap.get(couponRule.getMerId());
		Map<String, Object> goodsInfoMap = HfCacheUtil.getCache().getGoodsInfoMap();
		GoodsInfo goodsInfo = (GoodsInfo) goodsInfoMap.get(couponRule.getMerId() + "-" + couponRule.getGoodsId());
		couponRule.setCouponName(couponInf.getCouponName());
		couponRule.setMerName(merInfo.getMerName());
		couponRule.setGoodsName(goodsInfo.getGoodsName());
		/******** end 从缓存中获取ID对应的名称 *******************/

		// 渲染数据
		couponRule.setStateName(ParameterPool.couponRuleStates.get(couponRule.getState().toString())); // 页面显示使用stateName字符串型字段
		couponRule.setCouponCodeTypeName(ParameterPool.couponCodeTypes.get(couponRule.getCouponCodeType().toString())); // 页面显示使用couponCodeTypeName字符串型字段
		couponRule.setGenerateMethodName(ParameterPool.couponCodeMethod.get(couponRule.getGenerateMethod().toString())); // 页面显示使用generateMethodName字符串型字段
		couponRule.setEffTypeName(ParameterPool.couponCodeEffTypes.get(couponRule.getEffType().toString())); // 页面显示使用effTypeName字符串型字段
		modelMap.addAttribute("couponRule", couponRule);
		if(couponRule.getConsumetype() != null && couponRule.getConsumetype() == 2){
			try {
				List<CouponRMG> list = couponRMGService.queryRMG(couponRule.getRuleId());
				if(list != null && list.size() > 0){
					for(CouponRMG rmg : list){//从缓存中设置商户名称,商品名称
						String merid = rmg.getMerid().trim();
						String goodsid = rmg.getGoodsid().trim();
						if(merid.equalsIgnoreCase("ALLMER")){
							rmg.setMername("全部商户");
						}else{
							MerInfo info = (MerInfo) merInfoMap.get(merid);
							rmg.setMername(info.getMerName() + "（" + merid + "）");
						}
						if(goodsid.equalsIgnoreCase("ALLGOODS")){
							rmg.setGoodsname("可兑换商户的全部商品");
						}else{
							GoodsInfo info = (GoodsInfo) goodsInfoMap.get(merid + "-" + goodsid);
							rmg.setGoodsname(info.getGoodsName() + "（" + goodsid + "）");
						}
					}
				}
				modelMap.addAttribute("list", list);
			} catch (Exception e) {
				return "couponrule/exporterror";
			}
		}
		return "couponrule/detailcouponrule";
	}

	/**
	 * @Title: update
	 * @Description: 修改兑换券规则信息
	 * @param
	 * @param couponRule
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 上午10:49:44
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(CouponRule couponRule, String optdata, ModelMap modeMap) {
		String msg = "0"; // 默认操作失败
		try {
			couponRule.trim();
			couponRule.setSubmitUser(getUser().getId()); // 修改人
			couponRuleService.modifyCouponRule(couponRule);

			// 操作日志
			CouponLog log = new CouponLog();
			log.setBusinessobject("umpay.T_COUPON_RULE");
			log.setOpertype(Const.LOG_OPT_MODIFY);
			log.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log.setIxdata1(couponRule.getCouponId()); // 兑换券编号
			log.setIxdata2(couponRule.getMerId()); // 商户编号
			log.setIxdata3(LoginUtil.getUser().getId()); // 操作员ID
			log.setIxdata4(couponRule.getGoodsId()); // 操作类型
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
	 * @Title: auditDetail
	 * @Description: 链接到审核页面
	 * @param
	 * @param auditId
	 * @param modelMap
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 上午10:49:54
	 */
	@RequestMapping(value = "/auditDetail")
	public String auditDetail(String auditId, ModelMap modelMap) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", auditId);
		Audit audit = auditService.load(map);
		// 反序列化审核信息
		CouponRule auditCouponRule = (CouponRule) JsonHFUtil
				.getObjFromJsonArrStr1(audit.getModData(), CouponRule.class);
		auditCouponRule.trim();

		/******** start 从缓存中获取ID对应的名称 *******************/
		Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
		CouponInf couponInf = (CouponInf) couponInfMap.get(auditCouponRule.getCouponId());
		Map<String, Object> merInfoMap = HfCacheUtil.getCache().getMerInfoMap();
		MerInfo merInfo = (MerInfo) merInfoMap.get(auditCouponRule.getMerId());
		Map<String, Object> goodsInfoMap = HfCacheUtil.getCache().getGoodsInfoMap();
		GoodsInfo goodsInfo = (GoodsInfo) goodsInfoMap.get(auditCouponRule.getMerId() + "-"
				+ auditCouponRule.getGoodsId());
		auditCouponRule.setCouponName(couponInf.getCouponName());
		auditCouponRule.setMerName(merInfo.getMerName());
		auditCouponRule.setGoodsName(goodsInfo.getGoodsName());
		/******** end 从缓存中获取ID对应的名称 *******************/
		// 渲染数据
		auditCouponRule.setStateName(ParameterPool.couponRuleStates.get(auditCouponRule.getState().toString()));
		auditCouponRule.setCouponCodeTypeName(ParameterPool.couponCodeTypes.get(auditCouponRule.getCouponCodeType()
				.toString())); // 页面显示使用couponCodeTypeName字符串型字段
		auditCouponRule.setEffTypeName(ParameterPool.couponCodeEffTypes.get(auditCouponRule.getEffType().toString()));
		auditCouponRule.setGenerateMethodName(ParameterPool.couponCodeMethod.get(auditCouponRule.getGenerateMethod()
				.toString()));
		modelMap.addAttribute("auditCouponRule", auditCouponRule);
		modelMap.addAttribute("auditId", auditId);
		if(auditCouponRule.getConsumetype() == 2){
			try {
				List<CouponRMG> list = couponRMGService.queryRMG(auditCouponRule.getRuleId());
				if(list != null && list.size() > 0){
					for(CouponRMG rmg : list){//从缓存中设置商户名称,商品名称
						String merid = rmg.getMerid().trim();
						String goodsid = rmg.getGoodsid().trim();
						if(merid.equalsIgnoreCase("ALLMER")){
							rmg.setMername("全部商户");
						}else{
							MerInfo info = (MerInfo) merInfoMap.get(merid);
							rmg.setMername(info.getMerName() + "（" + merid + "）");
						}
						if(goodsid.equalsIgnoreCase("ALLGOODS")){
							rmg.setGoodsname("可兑换商户的全部商品");
						}else{
							GoodsInfo info = (GoodsInfo) goodsInfoMap.get(merid + "-" + goodsid);
							rmg.setGoodsname(info.getGoodsName() + "（" + goodsid + "）");
						}
					}
				}
				modelMap.addAttribute("list", list);
			} catch (Exception e) {
				return "couponrule/exporterror";
			}
		}
		// 如果审核类型为修改, 需获取历史数据展示到页面
		if ("2".equals(audit.getAuditType())) {
			CouponRule useCouponRule = couponRuleService.load(auditCouponRule.getRuleId());
			// 渲染数据
			useCouponRule.setStateName(ParameterPool.couponRuleStates.get(useCouponRule.getState().toString()));
			useCouponRule.setCouponCodeTypeName(ParameterPool.couponCodeTypes.get(useCouponRule.getCouponCodeType()
					.toString())); // 页面显示使用couponCodeTypeName字符串型字段
			useCouponRule.setEffTypeName(ParameterPool.couponCodeEffTypes.get(useCouponRule.getEffType().toString()));
			useCouponRule.setGenerateMethodName(ParameterPool.couponCodeMethod.get(useCouponRule.getGenerateMethod().toString()));
			modelMap.addAttribute("useCouponRule", useCouponRule);
			return "couponrule/auditmod";
		}
		return "couponrule/auditadd";
	}

	/**
	 * @Title: auditNoPass
	 * @Description: 审核不通过处理
	 * @param
	 * @param auditId
	 * @param resultDesc
	 * @param modeMap
	 * @return
	 * @throws Exception
	 * @author wanyong
	 * @date 2012-12-18 下午02:08:17
	 */
	@RequestMapping(value = "/auditNoPass")
	public ModelAndView auditNoPass(String auditId, String resultDesc, ModelMap modeMap) {
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
			// 操作日志
			CouponRule auditCouponRule = (CouponRule) JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(),
					CouponRule.class);
			CouponLog log = new CouponLog();
			log.setBusinessobject("umpay.T_COUPON_RULE");
			log.setOpertype(Const.LOG_OPT_AUDIT);
			log.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log.setIxdata1(auditCouponRule.getCouponId()); // 兑换券编号
			log.setIxdata2(auditCouponRule.getMerId()); // 商户ID
			log.setIxdata3(LoginUtil.getUser().getId()); // 操作员ID
			log.setIxdata4(auditCouponRule.getGoodsId()); // 操作类型
			String optdata = "兑换券规则审核，审核状态为：审核不通过 ；审核意见  " + audit.getResultDesc();
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

	@RequestMapping(value = "/exportindex")
	public String exportindex(String ruleid, Integer exp, ModelMap modeMap){
		CouponRule cr = couponRuleService.load(ruleid);
		if(cr == null){
			return "couponrule/exporterror";
		}
		if(cr.getExport() == 0){
			String path = this.getHttpRequest().getSession().getServletContext().getRealPath("WEB-INF/download");
			String fileName = path + "/" + ruleid + ".txt";
			try{
				String channel = FileUtil.getChannel(fileName, 2);
				modeMap.addAttribute("channel", channel);
			}catch(Exception ex){
				ex.printStackTrace();
				return "couponrule/exporterror";
			}
		}
		modeMap.addAttribute("ruleid", ruleid);
		modeMap.addAttribute("exp", cr.getExport());
		return "couponrule/exportindex";
	}
	
	@RequestMapping(value = "/export")
	public ModelAndView expt(String ruleid, Integer exp, String channel) throws Exception{
		String[] channelarr = new String[2];
		if(channel != null && !channel.equals("")){
			channelarr = channel.split("-");
		}
		CouponRule cr = couponRuleService.load(ruleid);
		cr.setMerName(HfCacheUtil.getCache().getMerName(cr.getMerId()));
		CouponLog log = new CouponLog();
		log.setBusinessobject("umpay.T_COUPON_RULE");
		log.setOpertype(Const.LOG_OPT_AUDIT);
		log.setResultdesc(Const.LOG_RES_SUCC);
		// 索引字段 用于查找
		log.setIxdata1(cr.getCouponId()); // 兑换券编号
		log.setIxdata2(cr.getMerId()); // 商户ID
		log.setIxdata3(LoginUtil.getUser().getId()); // 操作员ID
		log.setIxdata4(cr.getGoodsId()); // 操作类型
		String optdata = "批量导出兑换码";
		
		if(exp != null){
			if(cr.getExport() == 0){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ruleid", ruleid);
				map.put("merName", cr.getMerName());
				map.put("channel", channelarr[1]);
				log.setOperdata(optdata + "成功");
				couponLogService.addLog(log);
				return new ModelAndView("textView", "data", map); //直接导出
			}
			Map<String, Object> goodsInfo = HfCacheUtil.getCache().getGoodsInfoMap();
			GoodsInfo goods = (GoodsInfo)goodsInfo.get(cr.getMerId() + "-" + cr.getGoodsId());
			cr.setGoodsName(goods.getGoodsName());
			Map<String, Object> couponinf = HfCacheUtil.getCache().getCouponInfMap();
			CouponInf ci = (CouponInf)couponinf.get(cr.getCouponId());
			cr.setCouponName(ci.getCouponName());
			int amount = new Integer(goods.getAmount());
			List<CouponCode> codeList = new ArrayList<CouponCode>();
			//生成方式为第三方导入, 状态为未启用, 规则类型为不能批量导出, 库存量为0,-1不能被导出
			if(cr.getGenerateMethod() == 1 || cr.getState() != 2 || cr.getRuletype() == 0 || cr.getGoodsSum() < 1){
				log.setOperdata(optdata + "失败");
				couponLogService.addLog(log);
				return new ModelAndView("couponrule/exporterror", null);
			}
			int count = cr.getGoodsSum();
			String path = this.getHttpRequest().getSession().getServletContext().getRealPath("WEB-INF/download");
			String fileName = path + "/" + cr.getRuleId() + ".txt";
			FileUtil.deleteFile(fileName); //若文件存在, 删除文件
			FileUtil.createFile(fileName);
			String valid = "";
			if(cr.getEffType() == 2){//有效天数
				valid = "#有效期：售出后 " + cr.getEffDays() + " 有效";
			}else{
				valid = "#有效期：" + cr.getUseStartDate() + " 至 " + cr.getUseEndDate();
			}
			Timestamp intime = new Timestamp(System.currentTimeMillis());
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true)));
			try{
				FileUtil.writeFile(out, "#说明：", "#渠道：" + channel, "#兑换券名称：" + cr.getCouponName(), "#商户名称：" + cr.getMerName(), "#商品名称：" + cr.getGoodsName(), "#面值：" + (cr.getOriginalprice() / 100.0) + " 元", valid, "#售卖期：" + cr.getSellStartDate() + " 至 " + cr.getSellEndDate(), "");
				List<String> list = genCode(out, cr.getGenerateMethod(), count, fileName);
				for(String couponcode : list){
					String enCode = HfCodec.encrypt(couponcode);
					CouponCode code = new CouponCode(enCode, cr.getMerId(), cr.getGoodsId(), cr.getCouponId(), this.getUser().getId(), 1, 
							2, intime, cr.getUseStartDate(), cr.getUseEndDate(), cr.getCouponCodeType(),
							null, cr.getRuleId(), channelarr[0], amount, cr.getOriginalprice());
					codeList.add(code);
				}
			}catch(Exception ex){
				FileUtil.deleteFile(fileName); //若文件存在, 删除文件
				log.setOperdata(optdata + "失败");
				couponLogService.addLog(log);
				return new ModelAndView("couponrule/exporterror", null);
			}finally{
				if(out != null){
					out.close();
				}
			}
			cr.setSaledSum(count);
			cr.setExport(0);
			cr.setGoodsSum(0);
			cr.setModTime(new Timestamp(System.currentTimeMillis()));
			try{
				couponRuleService.updateCouponRule(cr, codeList);
			}catch(Exception ex){
				log.setOperdata(optdata + "失败");
				couponLogService.addLog(log);
				ex.printStackTrace();
				FileUtil.deleteFile(fileName); //更新不成功,删除文件
				return new ModelAndView("couponrule/exporterror", null);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ruleid", ruleid);
		map.put("merName", cr.getMerName());
		map.put("channel", channelarr[1]);
		log.setOperdata(optdata + "成功");
		couponLogService.addLog(log);
		return new ModelAndView("textView", "data", map);
	}
	
	private List<String> genCode(BufferedWriter out, Integer generateMethod, Integer count, String fileName)throws Exception{
		List<String> codeList = new ArrayList<String>();
		for(int i = 1; i <= count; i++){
			String couponcode = "";
			if(generateMethod == 0){//数字加字母
				couponcode = RandomUtil.randomLettersNumeric(12);
			}else if(generateMethod == 2){//全数字
				couponcode = RandomUtil.randomNumeric(12);
			}
			FileUtil.writeFile(out, couponcode);
			codeList.add(couponcode);
		}
		return codeList;
	}
	/**
	 * @Title: auditPass
	 * @Description: 审核通过处理
	 * @param
	 * @param auditId
	 * @param resultDesc
	 * @param modeMap
	 * @return
	 * @author wanyong
	 * @date 2013-1-4 上午11:09:50
	 */
	@RequestMapping(value = "/auditPass")
	public ModelAndView auditPass(String auditId, String resultDesc, ModelMap modeMap) {
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
			auditService.couponRuleAuditPass(audit);

			// 操作日志
			CouponRule auditCouponRule = (CouponRule) JsonHFUtil.getObjFromJsonArrStr1(audit.getModData(),
					CouponRule.class);
			CouponLog log = new CouponLog();
			log.setBusinessobject("umpay.T_COUPON_RULE");
			log.setOpertype(Const.LOG_OPT_AUDIT);
			log.setResultdesc(Const.LOG_RES_SUCC);
			// 索引字段 用于查找
			log.setIxdata1(auditCouponRule.getCouponId()); // 兑换券编号
			log.setIxdata2(auditCouponRule.getMerId()); // 商户ID
			log.setIxdata3(LoginUtil.getUser().getId()); // 操作员ID
			log.setIxdata4(auditCouponRule.getGoodsId()); // 操作类型
			String optdata = "兑换券规则审核，审核状态为：审核通过 ；审核意见  " + audit.getResultDesc();
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
	 * @Title: goCodeBatch
	 * @Description: 链接到批次信息管理页面
	 * @param ruleId
	 * @param modelMap
	 * @return
	 * @author wanyong
	 * @date 2012-12-20 下午08:55:51
	 */
	@RequestMapping(value = "/goCodeBatch")
	public String goCodeBatch(String ruleId, ModelMap modelMap) {
		CouponRule couponRule = couponRuleService.load(ruleId);
		couponRule.trim();
		/******** start 从缓存中获取ID对应的名称 *******************/
		Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
		CouponInf couponInf = (CouponInf) couponInfMap.get(couponRule.getCouponId());
		Map<String, Object> merInfoMap = HfCacheUtil.getCache().getMerInfoMap();
		MerInfo merInfo = (MerInfo) merInfoMap.get(couponRule.getMerId());
		Map<String, Object> goodsInfoMap = HfCacheUtil.getCache().getGoodsInfoMap();
		GoodsInfo goodsInfo = (GoodsInfo) goodsInfoMap.get(couponRule.getMerId() + "-" + couponRule.getGoodsId());
		modelMap.addAttribute("couponName", couponInf.getCouponName());
		modelMap.addAttribute("merName", merInfo.getMerName());
		modelMap.addAttribute("goodsName", goodsInfo.getGoodsName());
		/******** end 从缓存中获取ID对应的名称 *******************/
		modelMap.addAttribute("couponRule", couponRule);
		// 查找按钮权限
		String opts = HfCacheUtil.getCache().getUrlAcl("couponbatch");
		modelMap.addAttribute("opts", opts);
		return "couponbatch/index";
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
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
		Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> subMap : data) {
					// 渲染日志操作类型
					subMap.put("OPERTYPE", ParameterPool.couponLogOptTypes.get(subMap.get("opertype").toString()));

					// 渲染商户名称
					subMap.put(
							"MERNAME",
							merMap.containsKey(subMap.get("MERID").toString()) ? ((MerInfo) (merMap.get(subMap.get(
									"MERID").toString()))).getMerName() : "无此商户");
					// 渲染商品名称
					subMap.put(
							"GOODSNAME",
							goodsMap.containsKey(subMap.get("MERID").toString() + "-"
									+ subMap.get("GOODSID").toString()) ? ((GoodsInfo) goodsMap.get(subMap.get("MERID")
									.toString() + "-" + subMap.get("GOODSID").toString())).getGoodsName() : "无此商品");
					// 渲染兑换券名称
					subMap.put(
							"COUPONNAME",
							couponInfMap.containsKey(subMap.get("COUPONID").toString()) ? ((CouponInf) couponInfMap
									.get(subMap.get("COUPONID").toString())).getCouponName() : "无此兑换券");
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
