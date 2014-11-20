package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.ExportUtil;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.OptionService;
import com.umpay.hfmng.service.ReportOptService;
import com.umpay.uniquery.util.JsonUtil;
import com.umpay.uniquery.util.StringUtil;

/**
 * 
 * @ClassName: ReportOptAction
 * @Description: 报备操作action
 * @author wangyuxin
 * @date 2014-7-15 下午05:38:02
 * 
 */
@Controller
@RequestMapping("/reportOpt")
public class ReportOptAction extends BaseAction {
	@Autowired
	private OptionService optionService;
	@Autowired
	private ReportOptService reportOptService;

	/**
	 * @Title: reportopt
	 * @Description: 报备操作记录处理
	 * @return
	 * @author panyouliang
	 * @date 2014-7-18 下午6:03:08
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/reportopt")
	public ModelAndView reportopt() {
		Map params = this.getParametersFromRequest(this.getHttpRequest());
		StringBuilder sb = new StringBuilder("{");
		String opid = (String) params.get("opid");
		Integer optype = new Integer((String) params.get("optype"));
		String reason = (String) params.get("reason");
		if (StringUtil.isEmpty(opid) || StringUtil.isEmpty(optype)) {
			sb.append("\"code\":" + "0," + "\"msg\":\"操作失败, 数据不完整\"}");
		}
		try {
			boolean result = reportOptService.operation(opid, optype, reason);
			if (result) {
				sb.append("\"code\":" + "1," + "\"msg\":\"处理成功\"}");
			} else {
				sb.append("\"code\":" + "0," + "\"msg\":\"处理失败,请重试\"}");
			}
		} catch (Exception e) {
			sb.append("\"code\":" + "0," + "\"msg\":\"" + e.getMessage() + "\"}");
		}
		return new ModelAndView("jsonView", "ajax_json", sb.toString());
	}

	@RequestMapping(value = "/toQuery")
	public String toQuery() {
		return "reportopt/index";
	}

	/**
	 * 列表页面查询
	 * 
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
		HashMap map = (HashMap) getParametersFromRequest(super.getHttpRequest());
		// String loginName =
		// this.getUser()==null?"":this.getUser().getLoginName();
		// if(!"admin".equals(loginName)){
		// map.put("is_valid", "2");
		// }
		String queryKey = (String) map.get("queryKey");
		String startDate = (String) map.get("startDate");
		if (startDate != null && !"".equals(startDate)) {
			startDate = startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8)
					+ " 00:00:00";
			map.put("startDate", startDate);
		}
		String endDate = (String) map.get("endDate");
		if (endDate != null && !"".equals(endDate)) {
			endDate = endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6, 8)
					+ " 23:59:59";
			map.put("endDate", endDate);
		}
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, false);
				// 格式化数据
				ObjectUtil.trimData(data);
				transQueryList(data);
				long count = queryCount(queryKey, map, false);
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
	 * 
	 * @Title: export
	 * @Description:
	 * @return
	 * @return ModelAndView
	 * @throws
	 * @author wangyuxin
	 * @date 2014-07-21 上午11:38:32
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String startDate = (String) map.get("startDate");
		if (startDate != null && !"".equals(startDate)) {
			startDate = startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8)
					+ " 00:00:00";
			map.put("startDate", startDate);
		}
		String endDate = (String) map.get("endDate");
		if (endDate != null && !"".equals(endDate)) {
			endDate = endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6, 8)
					+ " 23:59:59";
			map.put("endDate", endDate);
		}
		@SuppressWarnings("unused")
		String msg = "";
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = query(queryKey, map, false);
				// 格式化数据
				ObjectUtil.trimData(data);
				transQueryList(data);
				String[] title = { "SUB_TIME:提交时间", "VER_TIME:审核时间", "BAK_TIME:报备时间", "SUB_OPR_NAME:提交人",
						"BANK_NAME:支付银行", "MER_ID:商户号", "MER_NAME:商户名称", "MER_CATEGORY:商户分类", "MER_BUSITYPE:业务属性",
						"GOODS_ID:商品号", "GOODS_NAME:商品名称", "GOODS_CATEGORY:商品分类", "GOODS_BUSITYPE:服务类型",
						"AMOUNT:价格（分）:number", "COM_DESC:公司介绍", "MER_WEB:商户网址", "REG_CPT:注册资本", "REG_TIEM:公司成立时间",
						"YEAR_PROFIT:年收益:number", "USER_SCALE:用户数:number", "BUSI_DESC:业务介绍", "SUPPORT:运营支撑（人）:number",
						"SALE_CHN:自有渠道", "SRC_MER:商户来源", "SHARED_RATE:分成比例", "OPR_NAME:操作人", "BAK_STAT_NAME:报备状态",
						"REASON:操作原因" };// 设置表头
				String remark = "报备操作记录查询";
				String reportName = "报备操作记录查询";
				Map<String, Object> modelMap = ExportUtil.getExportModel(data, title, remark, reportName);
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

	@SuppressWarnings("unchecked")
	private void transQueryList(List<Map<String, Object>> list) {
		for (Map<String, Object> m : list) {
			int bak_stat = (Integer) m.get("BAK_STAT");// 报备状态
			String sub_time = m.get("SUB_TIME") == null ? "" : String.valueOf(m.get("SUB_TIME"));
			m.put("SUB_TIME", sub_time.substring(0, sub_time.lastIndexOf(".")));
			String ver_time = m.get("SUB_TIME") == null ? "" : String.valueOf(m.get("VER_TIME"));
			if (bak_stat != 1) {
				m.put("VER_TIME", ver_time.substring(0, ver_time.lastIndexOf(".")));
			} else {
				m.put("VER_TIME", "");
			}
			String bak_time = m.get("SUB_TIME") == null ? "" : String.valueOf(m.get("BAK_TIME"));
			if (bak_stat == 6) {
				m.put("BAK_TIME", bak_time.substring(0, bak_time.lastIndexOf(".")));
			} else {
				m.put("BAK_TIME", "");
			}

			String sub_opr_id = (String) m.get("SUB_OPR_ID");// 提交人
			if (sub_opr_id != null && !"".equals(sub_opr_id)) {
				Map<String, Object> user = (Map<String, Object>) HfCacheUtil.getCache().getUserInfoMap()
						.get(sub_opr_id);
				if (user != null) {
					String sub_opr_name = (String) user.get("userName");
					m.put("SUB_OPR_NAME", sub_opr_name);
				}
			}

			String opr_id = (String) m.get("OPR_ID");// 操作人
			if (opr_id != null && !"".equals(opr_id)) {
				Map<String, Object> user = (Map<String, Object>) HfCacheUtil.getCache().getUserInfoMap().get(opr_id);
				if (user != null) {
					String opr_name = (String) user.get("userName");
					m.put("OPR_NAME", opr_name);
				}
			}

			String bank_id = (String) m.get("BANK_ID");// 支付银行
			if (bank_id != null && !"".equals(bank_id)) {
				BankInfo bankInfo = (BankInfo) HfCacheUtil.getCache().getBankInfoMap().get(bank_id);
				String bank_name = bankInfo == null ? "" : bankInfo.getBankName();
				m.put("BANK_NAME", bank_name);
			}

			String mer_id = (String) m.get("MER_ID");// 商户号
			if (mer_id != null && !"".equals(mer_id)) {
				MerInfo merInfo = (MerInfo) HfCacheUtil.getCache().getMerInfoMap().get(mer_id);
				String mer_name = merInfo == null ? "" : merInfo.getMerName();
				m.put("MER_NAME", mer_name);
			}

			String mer_category = (String) m.get("MER_CATEGORY");// 商户分类
			if (mer_category != null && !"".equals(mer_category)) {
				Map<String, String> merCateGorymap = optionService.getMerCategoryMap();
				m.put("MER_CATEGORY", merCateGorymap.containsKey(mer_category) ? merCateGorymap.get(mer_category)
						: mer_category);
			}

			String mer_busitype = (String) m.get("MER_BUSITYPE");// 商户业务属性
			if (mer_busitype != null && !"".equals(mer_busitype)) {
				Map<String, String> merBusiTypeMap = optionService.getMerBusiTypeMap();
				m.put("MER_BUSITYPE", merBusiTypeMap.containsKey(mer_busitype) ? merBusiTypeMap.get(mer_busitype)
						: mer_busitype);
			}

			String goods_id = (String) m.get("GOODS_ID");// 商品号
			if (goods_id != null && !"".equals(goods_id)) {
				GoodsInfo goodsInfo = (GoodsInfo) HfCacheUtil.getCache().getGoodsInfoMap().get(mer_id + "-" + goods_id);
				String goods_name = goodsInfo == null ? "" : goodsInfo.getGoodsName();
				m.put("GOODS_NAME", goods_name);
			}

			String goods_category = (String) m.get("GOODS_CATEGORY");// 商品分类
			if (goods_category != null && !"".equals(goods_category)) {
				Map<String, String> GoodsCateGorymap = HfCacheUtil.getCache().getGoodsCategoryMap();
				m.put("GOODS_CATEGORY",
						GoodsCateGorymap.containsKey(goods_category) ? GoodsCateGorymap.get(goods_category)
								: goods_category);
			}

			String goods_busitype = (String) m.get("GOODS_BUSITYPE");// 服务类型
			if (goods_busitype != null && "10".equals(goods_busitype)) {
				goods_busitype = "全网";
			} else if (goods_busitype != null && "11".equals(goods_busitype)) {
				goods_busitype = "全网、省网";
			} else if (goods_busitype != null && "01".equals(goods_busitype)) {
				goods_busitype = "省网";
			} else {
				goods_busitype = "异常类型";
			}
			m.put("GOODS_BUSITYPE", goods_busitype);

			String bak_stat_name = null;// 报备状态名称
			// 翻译报备状态
			if (bak_stat == 0) {
				bak_stat_name = "未报备";
			} else if (bak_stat == 1) {
				bak_stat_name = "提交";
			} else if (bak_stat == 2) {
				bak_stat_name = "通过";
			} else if (bak_stat == 4) {
				bak_stat_name = "不通过";
			} else if (bak_stat == 5) {
				bak_stat_name = "拒绝";
			} else if (bak_stat == 6) {
				bak_stat_name = "已报备";
			} else if (bak_stat == 9) {
				bak_stat_name = "预备";
			} else {
				bak_stat_name = "异常状态";
			}
			m.put("BAK_STAT_NAME", bak_stat_name);
		}
	}
}
