package com.umpay.hfmng.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/proxyorder")
public class ProxyOrderAction extends BaseAction{
//	@Autowired
//	private ChnlInfService chnlInfService;
	@Autowired
	private OptionService optionService;
	
	@RequestMapping(value = "/index")
	public String list(ModelMap modeMap) throws DataAccessException {
		String tree = ZTreeUtil.getGoodsCategoryWithAll();
		modeMap.addAttribute("zNodes", tree);
		//查找权限
		HfCache hfCache=HfCacheUtil.getCache();
		String opts = hfCache.getUrlAcl("proxyorder");
		modeMap.addAttribute("opts", opts);
		return "proxyorder/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> secMerNameMap = HfCacheUtil.getCache().getSecMerNameMap();
				Map<String, GoodsInfo> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap2();
				Map<String, String> merBizTypeMap = optionService.getMerBizTypeMap();
				//增加年份
				int year=Calendar.getInstance().get(Calendar.YEAR);
				map.put("year", String.valueOf(year));
				List<Map<String, Object>> data = queryPageList(queryKey, map,true);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map<String, Object> proxyOrder = data.get(i);
					//添加二级商户名称
					String secMerName = "";
					String secMerId = (String) proxyOrder.get("SUBMERID");
					if(secMerId != null){
						secMerName = secMerNameMap.get(secMerId);
					}
					proxyOrder.put("SUBMERNAME",secMerName);
					//添加商品名称
					String goodsName = "";
					String merId = (String) proxyOrder.get("MERID");
					String goodsId = (String) proxyOrder.get("GOODSID");
					if(merId != null && goodsId != null){
						GoodsInfo goods=goodsMap.get(merId+"-"+goodsId);
			            if(goods!=null)
			            	goodsName=goods.getGoodsName();
					}
					proxyOrder.put("GOODSNAME",goodsName);
					//转换支付类型id为名称
					String businessType = (String) proxyOrder.get("BUSINESSTYPE");
					proxyOrder.put("BUSINESSTYPE", merBizTypeMap.get(businessType));
				}
				long count = queryCount(queryKey, map,true);
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
	
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		List<Map<String, Object>> data=null;
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		if (queryKey != null) {
			//增加年份
			int year=Calendar.getInstance().get(Calendar.YEAR);
			map.put("year", String.valueOf(year));
			data = query(queryKey, map,true);
		}
		return new ModelAndView("excelProxyOrder", "excel", data);
	}
}
