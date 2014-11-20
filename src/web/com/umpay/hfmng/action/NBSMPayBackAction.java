package com.umpay.hfmng.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.model.CouponMerDayMonthStats;
import com.umpay.hfmng.model.Nbsm;
import com.umpay.hfmng.model.Payback;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * @ClassName: NBSMPayBackAction
 * @Description: 不均衡&退费action
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2013-12-13
 */
@Controller
@RequestMapping("nbsmpayback")
public class NBSMPayBackAction extends BaseAction {
	
	@RequestMapping(value = "/nbsmindex")
	public ModelAndView nbsmindex(String stlCycle, String stlDate, String goodsId, ModelMap model) {
		int cycle = new Integer(stlCycle);
		String start = stlDate;
		String end = stlDate;
		int length = stlDate.trim().length();
		if(length == 6){
			start = stlDate.substring(0, 4) + "-" + stlDate.substring(4, 6) + "-01";
			end = stlDate.substring(0, 4) + "-" + stlDate.substring(4, 6) + "-" + TimeUtil.getDaysInAmonth(stlDate);
		}else if(length == 8){
			if(cycle == 3){ //周结算
				String temp = TimeUtil.calcDay(stlDate, -7);
				start = temp.substring(0, 4) + "-" + temp.substring(4, 6) + "-" + temp.substring(6, 8);
				end = stlDate.substring(0, 4) + "-" + stlDate.substring(4, 6) + "-" + stlDate.substring(6, 8);
			}else{//4 日结算
				start = stlDate.substring(0, 4) + "-" + stlDate.substring(4, 6) + "-" + stlDate.substring(6, 8);
				end = stlDate.substring(0, 4) + "-" + stlDate.substring(4, 6) + "-" + stlDate.substring(6, 8);
			}
		}
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("merid", goodsId);
		return new ModelAndView("nbsmpayback/nbsmindex", model);
	}
	
	@RequestMapping(value = "/paybackindex")
	public ModelAndView paybackindex(String stlCycle, String stlDate, String goodsId, ModelMap model) {
		int cycle = new Integer(stlCycle);
		String start = stlDate;
		String end = stlDate;
		int length = stlDate.trim().length();
		if(length == 6){
			start = stlDate.substring(0, 4) + "-" + stlDate.substring(4, 6) + "-01";
			end = stlDate.substring(0, 4) + "-" + stlDate.substring(4, 6) + "-" + TimeUtil.getDaysInAmonth(stlDate);
		}else if(length == 8){
			if(cycle == 3){ //周结算
				String temp = TimeUtil.calcDay(stlDate, -7);
				start = temp.substring(0, 4) + "-" + temp.substring(4, 6) + "-" + temp.substring(6, 8);
				end = stlDate.substring(0, 4) + "-" + stlDate.substring(4, 6) + "-" + stlDate.substring(6, 8);
			}else{//4日结算
				start = stlDate.substring(0, 4) + "-" + stlDate.substring(4, 6) + "-" + stlDate.substring(6, 8);
				end = stlDate.substring(0, 4) + "-" + stlDate.substring(4, 6) + "-" + stlDate.substring(6, 8);
			}
		}
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("merid", goodsId);
		return new ModelAndView("nbsmpayback/paybackindex", model);
	}
	
	/**
	 * @Title: queryNBSM
	 * @Description: 查询不均衡明细
	 * @return ModelAndView
	 * @throws
	 * @author panyouliang
	 * @date 2013-12-13 下午12:45:40
	 */
	@RequestMapping(value = "/queryNBSM")
	public ModelAndView queryNBSM() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		String start = (String) map.get("startDate");
		String end = (String) map.get("endDate");
		map.put("startDate", start.replace("-", ""));
		map.put("endDate", end.replace("-", ""));
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				ObjectUtil.trimData(data);
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
	 * @Title: exportNBSM
	 * @Description: 导出不均衡短信明细
	 * @return ModelAndView
	 * @throws
	 * @author panyouliang
	 * @date 2013-12-16 下午4:46:55
	 */
	@RequestMapping(value = "/exportNBSM")
	public ModelAndView exportNBSM() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		String start = (String) map.get("startDate");
		String end = (String) map.get("endDate");
		map.put("startDate", start.replace("-", ""));
		map.put("endDate", end.replace("-", ""));
		List<Nbsm> list = new ArrayList<Nbsm>();
		if (queryKey != null) {
				IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
				List<Map<String, Object>> data = service.query(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map obj = data.get(i);
					Nbsm nbsm = new Nbsm();
					nbsm.setDate(obj.get("STLDATE").toString().trim());
					nbsm.setMonum(((BigDecimal)obj.get("MONUM")).intValue());
					nbsm.setMtnum(((BigDecimal)obj.get("MTNUM")).intValue());
					nbsm.setNbsmnum(((BigDecimal)obj.get("NBSMNUM")).intValue());
					list.add(nbsm);
				}
		} 
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", Nbsm.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}
	
	/**
	 * @Title: queryPayback
	 * @Description: 查询退费明细
	 * @return ModelAndView
	 * @author panyouliang
	 * @date 2013-12-13 下午12:46:06
	 */
	@RequestMapping(value = "/queryPayback")
	public ModelAndView queryPayback() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		String start = (String) map.get("startDate");
		String end = (String) map.get("endDate");
		map.put("startDate", start.replace("-", ""));
		map.put("endDate", end.replace("-", ""));
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				ObjectUtil.trimData(data);//
				for (int i = 0; i < data.size(); i++) {
					Map obj = data.get(i);
					obj.put("REFUNDAMOUNT", (new Double(obj.get("REFUNDAMOUNT").toString())) / 100.00);
				}
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
	 * @Title: exportPayBack
	 * @Description: 导出退费明细
	 * @return ModelAndView
	 * @throws
	 * @author panyouliang
	 * @date 2013-12-16 下午4:47:24
	 */
	@RequestMapping(value = "/exportPayBack")
	public ModelAndView exportPayBack() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		String start = (String) map.get("startDate");
		String end = (String) map.get("endDate");
		map.put("startDate", start.replace("-", ""));
		map.put("endDate", end.replace("-", ""));
		List<Payback> list = new ArrayList<Payback>();
		if (queryKey != null) {
				IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
				List<Map<String, Object>> data = service.query(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map obj = data.get(i);
					Payback payback = new Payback();
					payback.setDate(obj.get("UNDODATE").toString());
					payback.setMobile(obj.get("MOBILEID").toString());
					payback.setProname(obj.get("PROVNAME").toString());
					payback.setAmount((new Double(obj.get("REFUNDAMOUNT").toString())) / 100.00);
					payback.setBelongMer(obj.get("MERNAME").toString());
					payback.setMerid(obj.get("ACCID").toString());
					payback.setSubmerid(obj.get("GOODSID").toString());
					payback.setSubmername(obj.get("GOODSNAME").toString());
					payback.setPaybackreason(obj.get("WITHDRAWREASON").toString());
					payback.setDesc(obj.get("DESC").toString());
					list.add(payback);
				}
		} 
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", Payback.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}
}
