/**
 * @ClassName: OrderAction
 * @Description: 订单管理Action
 * @author panyouliang
 * @date 2013-1-11 下午3:16:33
 */
package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.coupon.system.restclient.CouponRestRequester;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.HfCodec;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.model.ChannelInf;
import com.umpay.hfmng.model.CouponGcOrder;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.model.CouponLog;
import com.umpay.hfmng.model.CouponOrder;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.CouponLogService;
import com.umpay.hfmng.service.CouponOrderService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.util.JsonUtil;
import com.umpay.uniquery.util.StringUtil;

@Controller
@RequestMapping("/couponorder")
public class CouponOrderAction extends BaseAction {
	@Autowired
	private CouponOrderService couponOrderServiceImpl;
	@Autowired
	private CouponLogService couponLogService;

	/**
	 * @Title: index
	 * @Description: 链接到订单查询页面
	 * @param
	 * @return 视图
	 * @author panyouliang
	 * @date 2013-1-11 下午3:20:00
	 */
	@RequestMapping(value = "/index")
	public String index() {
		return "couponorder/index";
	}

	/**
	 * @Title: index
	 * @Description: 链接到订单操作日志页面
	 * @param
	 * @return 视图
	 * @author wangliangyi
	 * @date 2013年1月18日12:01:58
	 */
	@RequestMapping(value = "/toquerylog")
	public String toquerylog() {
		return "couponorder/querylog";
	}

	/**
	 * @Title: couponOp
	 * @Description: 兑换码补发/注销操作
	 * @return
	 * @author wanyong
	 * @date 2013-6-4 下午4:01:59
	 */
	@RequestMapping(value = "/couponOp")
	public ModelAndView couponOp() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String op = (String) map.get("op");
		String couponCode = (String) map.get("couponCode");
		String merId = (String) map.get("merId");
		String orderId = (String) map.get("orderId");
		String phone = (String) map.get("phone");
		String type = (String) map.get("type");
		String uri = null;
		StringBuilder sb = new StringBuilder("{");
		Map<String, Object> params = new HashMap<String, Object>();
		// 操作日志
		CouponLog couponlog = new CouponLog();
		if (StringUtil.isEmpty(type)) {
			couponlog.setBusinessobject("umpay.T_COUPON_ORDER");
		} else {
			couponlog.setBusinessobject("umpay.T_COUPON_GCORDER");
		}

		couponlog.setOpertype(Const.LOG_OPT_CREATE);
		couponlog.setResultdesc(Const.LOG_RES_SUCC);
		// 索引字段 用于查找
		couponlog.setIxdata1(orderId); // 订单编号
		couponlog.setIxdata2(phone); // 手机号
		couponlog.setIxdata3(HfCodec.encrypt(couponCode)); // 兑换码
		String optdata = "对兑换码【" + couponCode + "】";

		if (!StringUtil.isEmpty(op) && "1".equals(op)) {
			// 补发
			uri = ParameterPool.couponSystemBackParas.get("CODEREGIVEURL");
			optdata += "执行补发兑换码操作";
		} else if (!StringUtil.isEmpty(op) && "0".equals(op)) {
			// 注销
			User user = this.getUser();
			params.put("operator", user.getLoginName());
			uri = ParameterPool.couponSystemBackParas.get("CODECANCELURL");
			optdata += "执行兑换码注销操作";
		}else if(!StringUtil.isEmpty(op) && "2".equals(op)) {
			// 回退 
			uri = ParameterPool.couponSystemBackParas.get("CODEREVERTURL");
			optdata += "执行代金劵回退操作";
		}
		params.put("couponCode", couponCode);
		params.put("merId", merId);
		CouponRestRequester crr = new CouponRestRequester();
		try {
			Map<String, Object> res = crr.sendRequestForPost(uri, params);
			String code = (String) res.get("retCode");
			String msg = (String) res.get("retMsg");
			if ("0000".equals(code)) {
				if (!StringUtil.isEmpty(op) && "1".equals(op)) {
					sb.append("\"code\":" + "1," + "\"msg\":\"补发成功\"}");
				} else if (!StringUtil.isEmpty(op) && "0".equals(op)) {
					sb.append("\"code\":" + "1," + "\"msg\":\"注销成功\"}");
				} else if (!StringUtil.isEmpty(op) && "2".equals(op)) {
					sb.append("\"code\":" + "1," + "\"msg\":\"回退成功\"}");
				}
				optdata += " 成功";
			} else {
				sb.append("\"code\":" + "0," + "\"msg\":\"" + msg + "\"}");
				optdata += " 失败";
			}
			couponlog.setOperdata(optdata);
			couponLogService.addLog(couponlog);
		} catch (Exception e) {
			sb.append("\"code\":" + "0," + "\"msg\":\"" + e.getMessage() + "\"}");
		}
		log.debug(sb.toString());
		return new ModelAndView("jsonView", "ajax_json", sb.toString());
	}

	/**
	 * @Title: orderDetail
	 * @Description: 订单详情
	 * @param
	 * @return
	 * @author panyouliang
	 * @date 2013-1-14 上午9:57:18
	 */
	@RequestMapping(value = "/orderDetail")
	public String orderDetail(String orderId, ModelMap modelMap) {
		// 查找按钮权限
		String opts = HfCacheUtil.getCache().getUrlAcl("couponorder");
		modelMap.addAttribute("opts", opts);
		CouponOrder order = couponOrderServiceImpl.get(orderId);
		modelMap.addAttribute("couponOrder", order);
		return "couponorder/detail";
	}

	/**
	 * @Title: query
	 * @Description: 查询订单列表
	 * @param
	 * @return 视图
	 * @author panyouliang
	 * @date 2013-1-11 下午5:10:37
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
				String code = null;
				for (Map<String, Object> m : data) {
					code = m.get("ACCESSTYPE") + "";
					m.put("ACCESSTYPE", ParameterPool.couponJoinTypes.get(code.trim()));
					code = m.get("STATE") + "";
					m.put("STATE", ParameterPool.couponOrderStates.get(code.trim()));
					m.put("ORDERSTATE", code.trim());
					code = m.get("TRANSTATE") + "";
					m.put("TRANSTATE", ParameterPool.couponTransStates.get(code.trim()));
				}
				ObjectUtil.trimData(data);
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
	 * 查询兑换券操作日志
	 * 
	 * @return
	 */
	@RequestMapping(value = "/querylog")
	public ModelAndView queryLog() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		if (map.get("couponCode") != null && !"".equals(map.get("couponCode").toString()))
			// 加密查询条件中的兑换码
			map.put("couponCode", HfCodec.encrypt(map.get("couponCode").toString()));
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				if (data != null && data.size() > 0) {
					for (Map<String, Object> map2 : data) {
						// 解密兑换码
						map2.put("COUPONCODE", HfCodec.decrypt(map2.get("COUPONCODE").toString()));
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

	/**
	 * @Title: queryCouponCodes
	 * @Description: 根据订单编号查询订单下所有兑换码信息
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-1-20 下午10:08:32
	 */
	@RequestMapping(value = "/querycouponcodes")
	public ModelAndView queryCouponCodes() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				if (data != null && data.size() > 0) {
					for (Map<String, Object> map2 : data) {
						// 解密兑换码
						map2.put("COUPONCODE", HfCodec.decrypt(map2.get("COUPONCODE").toString()));
						// 渲染兑换码形式
						map2.put("COUPONSTYLE", ParameterPool.couponCodeTypes.get(map2.get("COUPONSTYLE").toString()));
						// 渲染兑换码状态
						map2.put("COUPONSTATENAME",
								ParameterPool.couponCodeStates.get(map2.get("COUPONSTATE").toString()));
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

	/**
	 * @Title: gcIndex
	 * @Description: 链接到取码订单查询页面
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-4-3 上午11:02:22
	 */
	@RequestMapping(value = "/gcindex")
	public String gcIndex() {
		return "couponorder/gcindex";
	}

	/**
	 * @Title: gcQuery
	 * @Description: 取码订单查询列表
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-4-3 上午11:03:56
	 */
	@RequestMapping(value = "/gcquery")
	public ModelAndView gcQuery() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				ChannelInf channelInf = null;
				CouponInf couponInf = null;
				MerInfo merInfo = null;
				GoodsInfo goodsInfo = null;
				for (Map<String, Object> subMap : data) {
					if (subMap.get("COUPONID") != null)
						couponInf = (CouponInf) HfCacheUtil.getCache().getCouponInfMap()
								.get(subMap.get("COUPONID").toString());
					subMap.put("COUPONNAME", null == couponInf ? null : couponInf.getCouponName());
					if (subMap.get("CHANNELID") != null)
						channelInf = (ChannelInf) HfCacheUtil.getCache().getChannelInfMap()
								.get(subMap.get("CHANNELID").toString());
					subMap.put("CHANNELNAME", null == channelInf ? null : channelInf.getChannelName());
					if (subMap.get("MERID") != null)
						merInfo = (MerInfo) HfCacheUtil.getCache().getMerInfoMap().get(subMap.get("MERID").toString());
					subMap.put("MERNAME", null == merInfo ? null : merInfo.getMerName());
					if (subMap.get("GOODSID") != null)
						goodsInfo = (GoodsInfo) HfCacheUtil.getCache().getGoodsInfoMap()
								.get(subMap.get("MERID").toString() + "-" + subMap.get("GOODSID").toString());
					subMap.put("GOODSNAME", null == goodsInfo ? null : goodsInfo.getGoodsName());
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
	 * @Title: gcDetail
	 * @Description: 链接到订单详情页面
	 * @param orderId
	 * @param modelMap
	 * @return
	 * @author wanyong
	 * @date 2013-4-3 下午05:29:34
	 */
	@RequestMapping(value = "/gcdetail")
	public String gcDetail(String orderId, ModelMap modelMap) {
		// 查找按钮权限
		String opts = HfCacheUtil.getCache().getUrlAcl("coupongcorder");
		modelMap.addAttribute("opts", opts);
		CouponGcOrder couponGcOrder = couponOrderServiceImpl.loadCouponGcOrder(orderId);
		if (couponGcOrder != null) {
			// 渲染数据
			ChannelInf channelInf = (ChannelInf) HfCacheUtil.getCache().getChannelInfMap()
					.get(couponGcOrder.getChannelId());
			couponGcOrder.setChannelName(null == channelInf ? "" : channelInf.getChannelName());
			MerInfo merInfo = (MerInfo) HfCacheUtil.getCache().getMerInfoMap().get(couponGcOrder.getMerId());
			couponGcOrder.setMerName(null == merInfo ? "" : merInfo.getMerName());
			GoodsInfo goodsInfo = (GoodsInfo) HfCacheUtil.getCache().getGoodsInfoMap()
					.get(couponGcOrder.getMerId() + "-" + couponGcOrder.getGoodsId());
			couponGcOrder.setGoodsName(null == goodsInfo ? "" : goodsInfo.getGoodsName());
			couponGcOrder.setStateName("取码失败");
			if (couponGcOrder.getState() == 2)
				couponGcOrder.setStateName("取码成功");
		}
		modelMap.addAttribute("couponGcOrder", couponGcOrder);
		return "couponorder/gcdetail";
	}
}
