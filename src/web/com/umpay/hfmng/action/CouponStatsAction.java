package com.umpay.hfmng.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.model.ChannelInf;
import com.umpay.hfmng.model.CouponExchangeDetailStats;
import com.umpay.hfmng.model.CouponGatherStats;
import com.umpay.hfmng.model.CouponGoodsMonthDayStats;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.model.CouponMerDayMonthStats;
import com.umpay.hfmng.model.CouponMerGatherStats;
import com.umpay.hfmng.model.CouponMonthDayStats;
import com.umpay.hfmng.model.CouponRuleStats;
import com.umpay.hfmng.model.CouponStatsChannelGather;
import com.umpay.hfmng.model.CouponStatsProvGather;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerChannelStats;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;
import com.umpay.uniquery.util.StringUtil;

/**
 * @ClassName: CouponStatsAction
 * @Description: 兑换劵统计Action
 * @author panyouliang
 * @date 2012-12-27
 */
@Controller
@RequestMapping("/couponstats")
public class CouponStatsAction extends BaseAction {

	/**
	 * @Title: 导出商户兑换劵日月统计
	 * @Description: 导出商户兑换劵日月统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportMerDetail")
	public ModelAndView exportMerDetail() {
		Map map = this.getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String statsType = (String) map.get("statsType");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		if (statsType != null && "day".equals(statsType)) {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += " 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				endDate += " 23:59:59";
				map.put("endDate", endDate);
			}
		} else {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += "-01 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				String date = getLastDay(endDate + "-01");
				date += " 23:59:59";
				map.put("endDate", date);
			}
		}
		List<CouponMerDayMonthStats> list = new ArrayList<CouponMerDayMonthStats>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
			List<Map<String, Object>> data = service.query(queryKey, map);
			for (int i = 0; i < data.size(); i++) {
				CouponMerDayMonthStats stats = new CouponMerDayMonthStats();
				Map obj = data.get(i);
				stats.setMd(obj.get("MD").toString().trim());
				stats.setMerId(obj.get("MERID").toString().trim());
				stats.setMerName(obj.get("MERNAME").toString().trim());
				if (obj.get("exchangedNum") != null) {
					stats.setExchangedNum((Integer) obj.get("exchangedNum"));
				} else {
					stats.setExchangedNum(0);
				}
				if (obj.get("exchangedSum") != null) {
					stats.setExchangedSum((Double) obj.get("exchangedSum"));
				} else {
					stats.setExchangedSum(new Double(0));
				}
				if (obj.get("saledNum") != null) {
					stats.setSaledNum((Integer) obj.get("saledNum"));
				} else {
					stats.setSaledNum(0);
				}
				if (obj.get("saledSum") != null) {
					stats.setSaledSum((Double) obj.get("saledSum"));
				} else {
					stats.setSaledSum(new Double(0));
				}

				list.add(stats);
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", CouponMerDayMonthStats.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}

	/**
	 * @Title: 导出商户兑换劵汇总统计
	 * @Description: 导出商户兑换劵汇总统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportMerGether")
	public ModelAndView exportMerGether() {
		Map map = this.getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		List<CouponMerGatherStats> list = new ArrayList<CouponMerGatherStats>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
			List<Map<String, Object>> data = service.query(queryKey, map);
			for (int i = 0; i < data.size(); i++) {
				CouponMerGatherStats stats = new CouponMerGatherStats();
				Map obj = data.get(i);
				stats.setMerId(obj.get("MERID").toString().trim());
				stats.setMerName(obj.get("MERNAME").toString().trim());
				if (obj.get("exchangedNum") != null) {
					stats.setExchangedNum((Integer) obj.get("exchangedNum"));
				} else {
					stats.setExchangedNum(0);
				}
				if (obj.get("exchangedSum") != null) {
					stats.setExchangedSum((Double) obj.get("exchangedSum"));
				} else {
					stats.setExchangedSum(new Double(0));
				}
				if (obj.get("saledNum") != null) {
					stats.setSaledNum((Integer) obj.get("saledNum"));
				} else {
					stats.setSaledNum(0);
				}
				if (obj.get("saledSum") != null) {
					stats.setSaledSum((Double) obj.get("saledSum"));
				} else {
					stats.setSaledSum(new Double(0));
				}

				list.add(stats);
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", CouponMerGatherStats.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}

	/**
	 * @Title: 查询商户兑换劵汇总统计
	 * @Description: 查询商户兑换劵汇总统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/couponmergatherStats")
	public ModelAndView couponmergatherStats() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
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
	 * @Title: 查询商户兑换劵汇总统计
	 * @Description: 查询商户兑换劵汇总统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/couponmerdetailStats")
	public ModelAndView couponmerdetailStats() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String statsType = (String) map.get("statsType");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		if (statsType != null && "day".equals(statsType)) {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += " 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				endDate += " 23:59:59";
				map.put("endDate", endDate);
			}
		} else {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += "-01 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				String date = getLastDay(endDate + "-01");
				date += " 23:59:59";
				map.put("endDate", date);
			}
		}
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
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
	 * @Title: 链接到商户兑换劵日月统计页面
	 * @Description: 链接到商户兑换劵日月统计页面
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@RequestMapping(value = "/merDetailList")
	public String merDetailList() {
		return "couponstats/couponmerdetailstats";
	}

	/**
	 * @Title: 链接到商户兑换劵汇总统计页面
	 * @Description: 链接到商户兑换劵汇总统计页面
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@RequestMapping(value = "/mergatherList")
	public String mergatherList() {
		return "couponstats/couponmergatherstats";
	}

	/**
	 * @Title: 导出兑换劵明细统计
	 * @Description: 导出兑换劵明细统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/expDetailReport")
	public ModelAndView expDetailReport() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		if (!StringUtil.isEmpty(startDate)) {
			startDate += " 00:00:00";
			map.put("startDate", startDate);
		}
		if (!StringUtil.isEmpty(endDate)) {
			endDate += " 23:59:59";
			map.put("endDate", endDate);
		}
		List<CouponExchangeDetailStats> list = new ArrayList<CouponExchangeDetailStats>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
			List<Map<String, Object>> data = service.query(queryKey, map);
			if(data != null && data.size() > 0){
				Map<String, Object> goodsInfo = HfCacheUtil.getCache().getGoodsInfoMap();
				Map<String, Object> merInfo = HfCacheUtil.getCache().getMerInfoMap();
				Map<String, Object> couponInfo = HfCacheUtil.getCache().getCouponInfMap();
				for (int i = 0; i < data.size(); i++) {
					CouponExchangeDetailStats stats = new CouponExchangeDetailStats();
					Map obj = data.get(i);
					String couponId = obj.get("COUPONID").toString().trim();
					CouponInf cinfo = (CouponInf)couponInfo.get(couponId);
					String merId = obj.get("MERID").toString().trim();
					String goodsId = obj.get("GOODSID").toString().trim();
					MerInfo minfo = (MerInfo) merInfo.get(merId);
					GoodsInfo ginfo = (GoodsInfo) goodsInfo.get(merId + "-" + goodsId);
					String couponName = cinfo.getCouponName().trim();
					String merName = minfo.getMerName().trim();
					String goodsName = ginfo.getGoodsName().trim();
					stats.setCouponCode(couponId);
					stats.setCouponName(couponName);
					stats.setDate(obj.get("EDATE").toString());
					stats.setGoodsCode(goodsId);
					stats.setGoodsName(goodsName);
					stats.setMerCode(merId);
					stats.setMerName(merName);
					stats.setPhone(obj.get("PAIDMOBILEID").toString());
					if (obj.get("TOTAL") != null) {
						stats.setTotal((Double) obj.get("TOTAL"));
					} else {
						stats.setTotal(new Double(0));
					}
					list.add(stats);
				}
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", CouponExchangeDetailStats.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}

	/**
	 * @Title: 链接到兑换劵明细统计页面
	 * @Description: 链接到兑换劵明细兑换统计页面
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@RequestMapping(value = "/detailList")
	public String detailList() {
		return "couponstats/coupondetailstats";
	}

	/**
	 * @Title: 查询兑换劵明细统计
	 * @Description: 查询兑换劵明细统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/couponDetailStats")
	public ModelAndView couponDetailStats() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		if (!StringUtil.isEmpty(startDate)) {
			startDate += " 00:00:00";
			map.put("startDate", startDate);
		}
		if (!StringUtil.isEmpty(endDate)) {
			endDate += " 23:59:59";
			map.put("endDate", endDate);
		}
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
				if(data != null && data.size() > 0){
					ObjectUtil.trimData(data);
					Map<String, Object> goodsInfo = HfCacheUtil.getCache().getGoodsInfoMap();
					Map<String, Object> merInfo = HfCacheUtil.getCache().getMerInfoMap();
					Map<String, Object> couponInfo = HfCacheUtil.getCache().getCouponInfMap();
					for(Map<String, Object> obj : data){
						CouponInf cinfo = (CouponInf)couponInfo.get(obj.get("COUPONID").toString().trim());
						MerInfo minfo = (MerInfo) merInfo.get(obj.get("MERID").toString().trim());
						GoodsInfo ginfo = (GoodsInfo) goodsInfo.get(obj.get("MERID").toString().trim() + "-" + obj.get("GOODSID").toString().trim());
						obj.put("COUPONNAME", cinfo.getCouponName().trim());
						obj.put("MERNAME", minfo.getMerName().trim());
						obj.put("GOODSNAME", ginfo.getGoodsName().trim());
					}
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
	 * @Title: 链接到兑换劵汇总统计页面
	 * @Description: 链接到兑换劵汇总统计页面
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@RequestMapping(value = "/gatherList")
	public String gatherList() {
		return "couponstats/coupongatherstats";
	}

	/**
	 * @Title: 链接到兑换劵日月统计页面
	 * @Description: 链接到兑换劵日月统计页面
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@RequestMapping(value = "/monthdayList")
	public String monthdayList() {
		return "couponstats/couponmonthdaystats";
	}

	/**
	 * @Title: 链接到商户商品渠道统计页面
	 * @Description: 链接到商户商品渠道统计页面
	 * @author panyouliang
	 * @date 2013-03-25
	 */
	@RequestMapping(value = "/merChannelList")
	public String merChannelList() {
		return "couponstats/merchannelstats";
	}

	/**
	 * @Title: 查询商品日/月统计
	 * @Description: 查询商品日/月统计
	 * @author panyouliang
	 * @date 2013-03-25
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/merChannelStats")
	public ModelAndView merChannelStats() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		String statsType = (String) map.get("statsType");
		if (statsType != null && "day".equals(statsType)) {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += " 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				endDate += " 23:59:59";
				map.put("endDate", endDate);
			}
		} else {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += "-01 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				String date = getLastDay(endDate + "-01");
				date += " 23:59:59";
				map.put("endDate", date);
			}
		}
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
				ObjectUtil.trimData(data);
				for (Map<String, Object> map2 : data) {
					ChannelInf channelInf = (ChannelInf) HfCacheUtil.getCache().getChannelInfMap()
							.get(map2.get("CHANNELID"));
					map2.put("CHANNELNAME", null == channelInf ? null : channelInf.getChannelName());
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
	 * @Title: 导出兑换劵日月统计
	 * @Description: 导出兑换劵日月统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/expMerChannelReport")
	public ModelAndView expMerChannelReport() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		String statsType = (String) map.get("statsType");
		if (statsType != null && "day".equals(statsType)) {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += " 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				endDate += " 23:59:59";
				map.put("endDate", endDate);
			}
		} else {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += "-01 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				String date = getLastDay(endDate + "-01");
				date += " 23:59:59";
				map.put("endDate", date);
			}
		}
		List<MerChannelStats> list = new ArrayList<MerChannelStats>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
			List<Map<String, Object>> data = service.query(queryKey, map);
			for (int i = 0; i < data.size(); i++) {
				MerChannelStats stats = new MerChannelStats();
				Map obj = data.get(i);
				stats.setDate(obj.get("md").toString().trim());
				String channelid = obj.get("channelid").toString().trim();
				stats.setChannelId(channelid);
				ChannelInf channelInf = (ChannelInf) HfCacheUtil.getCache().getChannelInfMap().get(channelid);
				if (channelInf == null) {
					stats.setChannelName("");
				} else {
					stats.setChannelName(channelInf.getChannelName());
				}
				stats.setMerId(obj.get("merid").toString().trim());
				stats.setMerName(obj.get("mername").toString().trim());
				stats.setGoodsId(obj.get("goodsid").toString().trim());
				stats.setGoodsName(obj.get("goodsname").toString().trim());
				if (obj.get("saledNum") != null) {
					stats.setSaledCount((Integer) obj.get("saledNum"));
				} else {
					stats.setSaledCount(0);
				}
				if (obj.get("saledSum") != null) {
					stats.setSaledSum((Double) obj.get("saledSum"));
				} else {
					stats.setSaledSum(new Double(0));
				}
				list.add(stats);
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", MerChannelStats.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}

	/**
	 * @Title: 链接到兑换券规则统计页面
	 * @Description: 链接到兑换券规则统计页面
	 * @author panyouliang
	 * @date 2013-03-25
	 */
	@RequestMapping(value = "/couponRuleList")
	public String couponRuleList() {
		return "couponstats/couponrulestats";
	}

	/**
	 * @Title: 查询兑换券规则统计
	 * @Description: 查询兑换券规则统计
	 * @author panyouliang
	 * @date 2013-03-25
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/couponRuleStats")
	public ModelAndView couponRuleStats() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
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
	 * @Title: 导出兑换券规则统计
	 * @Description: 导出兑换券规则统计
	 * @author panyouliang
	 * @date 2013-03-25
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/expCouponRuleReport")
	public ModelAndView expCouponRuleReport() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		List<CouponRuleStats> list = new ArrayList<CouponRuleStats>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
			List<Map<String, Object>> data = service.query(queryKey, map);
			for (int i = 0; i < data.size(); i++) {
				CouponRuleStats stats = new CouponRuleStats();
				Map obj = data.get(i);
				stats.setRuleId(obj.get("ruleid").toString().trim());
				stats.setMerId(obj.get("merid").toString().trim());
				stats.setMerName(obj.get("mername").toString().trim());
				stats.setGoodsId(obj.get("goodsid").toString().trim());
				stats.setGoodsName(obj.get("goodsname").toString().trim());
				if (obj.get("saledNum") != null) {
					stats.setSaledCount((Integer) obj.get("saledNum"));
				} else {
					stats.setSaledCount(0);
				}
				if (obj.get("saledSum") != null) {
					stats.setSaledSum((Double) obj.get("saledSum"));
				} else {
					stats.setSaledSum(new Double(0));
				}
				if (obj.get("exchangedNum") != null) {
					stats.setExchangedCount((Integer) obj.get("exchangedNum"));
				} else {
					stats.setExchangedCount(0);
				}
				if (obj.get("exchangedSum") != null) {
					stats.setExchangedSum((Double) obj.get("exchangedSum"));
				} else {
					stats.setExchangedSum(new Double(0));
				}
				if (obj.get("cancelnum") != null) {
					stats.setCancelCount((Integer) obj.get("cancelnum"));
				} else {
					stats.setCancelCount(0);
				}
				if (obj.get("cancelSum") != null) {
					stats.setCancelSum((Double) obj.get("cancelSum"));
				} else {
					stats.setCancelSum(new Double(0));
				}
				if (obj.get("overduenum") != null) {
					stats.setOverdueCount((Integer) obj.get("overduenum"));
				} else {
					stats.setOverdueCount(0);
				}
				if (obj.get("overdueSum") != null) {
					stats.setOverdueSum((Double) obj.get("overdueSum"));
				} else {
					stats.setOverdueSum(new Double(0));
				}
				list.add(stats);
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", CouponRuleStats.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}

	/**
	 * @Title: 链接到商品日/月统计页面
	 * @Description: 链接到商品日/月统计页面
	 * @author panyouliang
	 * @date 2013-03-25
	 */
	@RequestMapping(value = "/goodsmonthdayList")
	public String goodsmonthdayList() {
		return "couponstats/goodsmothdaystats";
	}

	/**
	 * @Title: 查询商品日/月统计
	 * @Description: 查询商品日/月统计
	 * @author panyouliang
	 * @date 2013-03-25
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/goodsMonthDayStats")
	public ModelAndView goodsMonthDayStats() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		String statsType = (String) map.get("statsType");
		if ("day".equals(statsType)) {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += " 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				endDate += " 23:59:59";
				map.put("endDate", endDate);
			}
		} else {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += "-01 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				String date = getLastDay(endDate + "-01");
				date += " 23:59:59";
				map.put("endDate", date);
			}
		}
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
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
	 * @Title: 导出兑换劵日月统计
	 * @Description: 导出兑换劵日月统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/expGoodsMonthDayReport")
	public ModelAndView expGoodsMonthDayReport() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		String statsType = (String) map.get("statsType");
		if (statsType != null && "day".equals(statsType)) {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += " 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				endDate += " 23:59:59";
				map.put("endDate", endDate);
			}
		} else {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += "-01 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				String date = getLastDay(endDate + "-01");
				date += " 23:59:59";
				map.put("endDate", date);
			}
		}
		List<CouponGoodsMonthDayStats> list = new ArrayList<CouponGoodsMonthDayStats>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
			List<Map<String, Object>> data = service.query(queryKey, map);
			for (int i = 0; i < data.size(); i++) {
				CouponGoodsMonthDayStats stats = new CouponGoodsMonthDayStats();
				Map obj = data.get(i);
				stats.setDate(obj.get("md").toString().trim());
				stats.setMerId(obj.get("merid").toString().trim());
				stats.setMerName(obj.get("mername").toString().trim());
				stats.setGoodsId(obj.get("goodsid").toString().trim());
				stats.setGoodsName(obj.get("goodsname").toString().trim());
				if (obj.get("saledNum") != null) {
					stats.setSaledCount((Integer) obj.get("saledNum"));
				} else {
					stats.setSaledCount(0);
				}
				if (obj.get("saledSum") != null) {
					stats.setSaledSum((Double) obj.get("saledSum"));
				} else {
					stats.setSaledSum(new Double(0));
				}
				if (obj.get("exchangedNum") != null) {
					stats.setExchangedCount((Integer) obj.get("exchangedNum"));
				} else {
					stats.setExchangedCount(0);
				}
				if (obj.get("exchangedSum") != null) {
					stats.setExchangedSum((Double) obj.get("exchangedSum"));
				} else {
					stats.setExchangedSum(new Double(0));
				}
				if (obj.get("cancelnum") != null) {
					stats.setCancelCount((Integer) obj.get("cancelnum"));
				} else {
					stats.setCancelCount(0);
				}
				if (obj.get("cancelSum") != null) {
					stats.setCancelSum((Double) obj.get("cancelSum"));
				} else {
					stats.setCancelSum(new Double(0));
				}
				if (obj.get("overduenum") != null) {
					stats.setOverdueCount((Integer) obj.get("overduenum"));
				} else {
					stats.setOverdueCount(0);
				}
				if (obj.get("overdueSum") != null) {
					stats.setOverdueSum((Double) obj.get("overdueSum"));
				} else {
					stats.setOverdueSum(new Double(0));
				}
				list.add(stats);
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", CouponGoodsMonthDayStats.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}

	/**
	 * @Title: 查询兑换劵日月统计
	 * @Description: 查询兑换劵日月统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/couponMonthDayStats")
	public ModelAndView couponMonthDayStats() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		String statsType = (String) map.get("statsType");
		if (statsType != null && "day".equals(statsType)) {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += " 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				endDate += " 23:59:59";
				map.put("endDate", endDate);
			}
		} else {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += "-01 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				String date = getLastDay(endDate + "-01");
				date += " 23:59:59";
				map.put("endDate", date);
			}
		}
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
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
	 * @Title: 查询兑换劵汇总统计
	 * @Description: 查询兑换劵汇总统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/coupongetherStats")
	public ModelAndView coupongetherStats() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
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
	 * @Title: 导出兑换劵日月统计
	 * @Description: 导出兑换劵日月统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/expMonthDayReport")
	public ModelAndView expMonthDayReport() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		String statsType = (String) map.get("statsType");
		if (statsType != null && "day".equals(statsType)) {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += " 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				endDate += " 23:59:59";
				map.put("endDate", endDate);
			}
		} else {
			if (!StringUtil.isEmpty(startDate)) {
				startDate += "-01 00:00:00";
				map.put("startDate", startDate);
			}
			if (!StringUtil.isEmpty(endDate)) {
				String date = getLastDay(endDate + "-01");
				date += " 23:59:59";
				map.put("endDate", date);
			}
		}
		List<CouponMonthDayStats> list = new ArrayList<CouponMonthDayStats>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
			List<Map<String, Object>> data = service.query(queryKey, map);
			for (int i = 0; i < data.size(); i++) {
				CouponMonthDayStats stats = new CouponMonthDayStats();
				Map obj = data.get(i);
				stats.setDate(obj.get("md").toString().trim());
				stats.setCouponId(obj.get("couponid").toString().trim());
				stats.setCouponName(obj.get("couponname").toString().trim());
				if (obj.get("exchangedNum") != null) {
					stats.setExchangedCount((Integer) obj.get("exchangedNum"));
				} else {
					stats.setExchangedCount(0);
				}
				if (obj.get("exchangedSum") != null) {
					stats.setExchangedSum((Double) obj.get("exchangedSum"));
				} else {
					stats.setExchangedSum(new Double(0));
				}
				if (obj.get("saledNum") != null) {
					stats.setSaledCount((Integer) obj.get("saledNum"));
				} else {
					stats.setSaledCount(0);
				}
				if (obj.get("saledSum") != null) {
					stats.setSaledSum((Double) obj.get("saledSum"));
				} else {
					stats.setSaledSum(new Double(0));
				}

				list.add(stats);
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", CouponMonthDayStats.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}

	/**
	 * @Title: 导出兑换劵汇总统计
	 * @Description: 导出兑换劵汇总统计
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportGether")
	public ModelAndView exportGether() {
		Map map = this.getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		List<CouponGatherStats> list = new ArrayList<CouponGatherStats>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
			List<Map<String, Object>> data = service.query(queryKey, map);
			for (int i = 0; i < data.size(); i++) {
				CouponGatherStats stats = new CouponGatherStats();
				Map obj = data.get(i);
				stats.setCouponId(obj.get("couponid").toString().trim());
				stats.setCouponName(obj.get("couponname").toString().trim());
				if (obj.get("exchangedNum") != null) {
					stats.setExchangedCount((Integer) obj.get("exchangedNum"));
				} else {
					stats.setExchangedCount(0);
				}
				if (obj.get("exchangedSum") != null) {
					stats.setExchangedSum((Double) obj.get("exchangedSum"));
				} else {
					stats.setExchangedSum(new Double(0));
				}
				if (obj.get("saledNum") != null) {
					stats.setSaledCount((Integer) obj.get("saledNum"));
				} else {
					stats.setSaledCount(0);
				}
				if (obj.get("saledSum") != null) {
					stats.setSaledSum((Double) obj.get("saledSum"));
				} else {
					stats.setSaledSum(new Double(0));
				}

				list.add(stats);
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", CouponGatherStats.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}

	/**
	 * @Title: 获取指定月份最后一天
	 * @Description: 获取指定月份最后一天
	 * @param dateFmt
	 *            日期格式 yyyy-MM-dd
	 * @author panyouliang
	 * @date 2013-01-04
	 */
	private static String getLastDay(String dateFmt) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(dateFmt);
		} catch (Exception e) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1); // 加一个月
		calendar.set(Calendar.DATE, 1); // 设置为该月第一天
		calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
		return df.format(calendar.getTime());
	}

	/**
	 * @Title: channelList
	 * @Description: 链接到渠道汇总统计页面
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-3-27 下午12:08:48
	 */
	@RequestMapping(value = "/channelgather")
	public String channelGather() {
		return "couponstats/couponchannelgatherstats";
	}

	/**
	 * @Title: channelGatherStats
	 * @Description: 查询渠道汇总统计
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-3-27 上午11:46:26
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/channelgatherstats")
	public ModelAndView channelGatherStats() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> map2 : data) {
					ChannelInf channelInf = (ChannelInf) HfCacheUtil.getCache().getChannelInfMap()
							.get(map2.get("CHANNELID"));
					map2.put("CHANNELNAME", null == channelInf ? null : channelInf.getChannelName());
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
	 * @Title: channelGatherExport
	 * @Description: 导出渠道汇总统计数据
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-3-27 上午11:57:37
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/channelgatherexport")
	public ModelAndView channelGatherExport() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = map.get("queryKey").toString();
		List<CouponStatsChannelGather> couponStatsChannelGatherList = null;
		if (queryKey != null) {
			// 这里不分页查询
			IUniQueryService uniQueryService = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
			List<Map<String, Object>> mapList = uniQueryService.query(queryKey, map);
			if (mapList != null && mapList.size() > 0) {
				// 格式化数据
				ObjectUtil.trimData(mapList);
				couponStatsChannelGatherList = new ArrayList<CouponStatsChannelGather>();
				for (Map<String, Object> subMap : mapList) {
					CouponStatsChannelGather couponStatsChannelGather = new CouponStatsChannelGather();
					couponStatsChannelGather.setStatDate(null == subMap.get("STATDATE") ? "" : subMap.get("STATDATE")
							.toString());
					if (subMap.get("CHANNELID") != null) {
						couponStatsChannelGather.setChannelId(subMap.get("CHANNELID").toString());
						ChannelInf channelInf = (ChannelInf) HfCacheUtil.getCache().getChannelInfMap()
								.get(subMap.get("CHANNELID"));
						couponStatsChannelGather.setChannelName(null == channelInf ? "" : channelInf.getChannelName());
					}
					couponStatsChannelGather.setSaledNum(Integer.parseInt(subMap.get("SALEDNUM").toString()));
					couponStatsChannelGather.setSaledSum(((java.math.BigDecimal) subMap.get("SALEDSUM")).doubleValue());

					couponStatsChannelGatherList.add(couponStatsChannelGather);
				}
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", CouponStatsChannelGather.class);
		map2.put("data", couponStatsChannelGatherList);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}

	/**
	 * @Title: provGather
	 * @Description: 链接到分省份数据统计页面
	 * @return
	 * @author wanyong
	 * @date 2013-7-25 上午11:40:57
	 */
	@RequestMapping(value = "/provgather")
	public String provGather() {
		return "couponstats/couponprovgatherstats";
	}

	/**
	 * @Title: provGatherStats
	 * @Description: 查询兑换券分省份汇总统计
	 * @return
	 * @author wanyong
	 * @date 2013-7-25 下午2:43:03
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/provgatherstats")
	public ModelAndView provGatherStats() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
				ObjectUtil.trimData(data);
				// 渲染数据
				for (Map<String, Object> map2 : data) {
					ChannelInf channelInf = (ChannelInf) HfCacheUtil.getCache().getChannelInfMap()
							.get(map2.get("CHANNELID"));
					map2.put("CHANNELNAME", null == channelInf ? null : channelInf.getChannelName());
					MerInfo merInfo = (MerInfo) HfCacheUtil.getCache().getMerInfoMap().get(map2.get("MERID"));
					map2.put("MERNAME", null == merInfo ? null : merInfo.getMerName());
					GoodsInfo goodsInfo = (GoodsInfo) HfCacheUtil.getCache().getGoodsInfoMap()
							.get(map2.get("MERID") + "-" + map2.get("GOODSID"));
					map2.put("GOODSNAME", null == goodsInfo ? null : goodsInfo.getGoodsName());
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
	 * @Title: provGatherExport
	 * @Description: 导出兑换券分省份汇总统计数据
	 * @return
	 * @author wanyong
	 * @date 2013-7-25 下午2:53:51
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/provgatherexport")
	public ModelAndView provGatherExport() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = map.get("queryKey").toString();
		List<CouponStatsProvGather> couponStatsProvGatherList = null;
		if (queryKey != null) {
			// 这里不分页查询
			IUniQueryService uniQueryService = (IUniQueryService) SpringContextUtil.getBean("uniQueryServiceOffline");
			List<Map<String, Object>> mapList = uniQueryService.query(queryKey, map);
			if (mapList != null && mapList.size() > 0) {
				// 格式化数据
				ObjectUtil.trimData(mapList);
				couponStatsProvGatherList = new ArrayList<CouponStatsProvGather>();
				for (Map<String, Object> subMap : mapList) {
					CouponStatsProvGather couponStatsProvGather = new CouponStatsProvGather();
					couponStatsProvGather.setStatDate(null == subMap.get("STATDATE") ? "" : subMap.get("STATDATE")
							.toString());
					couponStatsProvGather.setProvName(null == subMap.get("PROVNAME") ? "" : subMap.get("PROVNAME")
							.toString());
					if (subMap.get("CHANNELID") != null) {
						couponStatsProvGather.setChannelId(subMap.get("CHANNELID").toString());
						ChannelInf channelInf = (ChannelInf) HfCacheUtil.getCache().getChannelInfMap()
								.get(subMap.get("CHANNELID"));
						couponStatsProvGather.setChannelName(null == channelInf ? "" : channelInf.getChannelName());
					}
					if (subMap.get("MERID") != null) {
						couponStatsProvGather.setMerId(subMap.get("MERID").toString());
						MerInfo merInfo = (MerInfo) HfCacheUtil.getCache().getMerInfoMap().get(subMap.get("MERID"));
						couponStatsProvGather.setMerName(null == merInfo ? "" : merInfo.getMerName());
					}
					if (subMap.get("GOODSID") != null) {
						couponStatsProvGather.setGoodsId(subMap.get("GOODSID").toString());
						GoodsInfo goodsInfo = (GoodsInfo) HfCacheUtil.getCache().getGoodsInfoMap()
								.get(subMap.get("MERID") + "-" + subMap.get("GOODSID"));
						couponStatsProvGather.setGoodsName(null == goodsInfo ? "" : goodsInfo.getGoodsName());
					}
					couponStatsProvGather.setSaledNum(Integer.parseInt(subMap.get("SALEDNUM").toString()));
					couponStatsProvGather.setSaledSum((Double) subMap.get("SALEDSUM"));
					couponStatsProvGather.setExchangedNum(Integer.parseInt(subMap.get("EXCHANGEDNUM").toString()));
					couponStatsProvGather.setExchangedSum((Double) subMap.get("EXCHANGEDSUM"));

					couponStatsProvGatherList.add(couponStatsProvGather);
				}
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", CouponStatsProvGather.class);
		map2.put("data", couponStatsProvGatherList);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}
}
