/** *****************  JAVA头文件说明  ****************
 * file name  :  FeeCodeAction.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-24
 * *************************************************/ 

package com.umpay.hfmng.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.FeeCode;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.FeeCodeService;
import com.umpay.hfmng.service.GoodsFeeCodeService;
import com.umpay.hfmng.service.GoodsInfoService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  FeeCodeAction
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Controller
@RequestMapping("/goodscodeadd")
public class GoodsCodeAddAction extends BaseAction{
	@Autowired
	private OptionService optionService;
	@Autowired
	private GoodsFeeCodeService goodsFeeCodeService;
	@Autowired
	private  GoodsInfoService goodsInfoService;
	@Autowired
	private  FeeCodeService feeCodeService;
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		String tree = ZTreeUtil.getGoodsCategoryWithAll();
		modeMap.addAttribute("zNodes", tree);
		//查找权限
//		String opts="001,002,007,008"; // 权限测试
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("goodscodeadd");
		modeMap.addAttribute("opts", opts);
		return "goodscodeadd/index";
	}
	@RequestMapping(value = "/det")
	public String index2() {
		return "goodscodeadd/detail";
	}
	/**
	 * ********************************************
	 * method name   : querycode 
	 * description   : 查询计费代码方法
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-10-24  下午10:05:57
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/querycode")
	public ModelAndView querycode(){
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
	    Map<String,String>  feeCodeCategoryMap=optionService.getFeeCodeCategoryMap();
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).get("CATEGORY") != null) {
						String feeCodeCategory = feeCodeCategoryMap.get(data
								.get(i).get("CATEGORY").toString().trim());
						data.get(i).remove("CATEGORY");
						data.get(i).put("CATEGORY", feeCodeCategory);
					}
					if(data.get(i).get("USECOUNT") ==null ){
						data.get(i).remove("USECOUNT");
						data.get(i).put("USECOUNT", "0");
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
	 * ********************************************
	 * method name   : quercode 
	 * description   : 查询已经配置的商品计费代码
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-10-30  下午09:15:13
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/querygoodsFeeCode")
	public ModelAndView querygoodscode(){
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
	    Map<String,String>  feeCodeCategoryMap=optionService.getFeeCodeCategoryMap();
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).get("CATEGORY") != null) {
						String feeCodeCategory = feeCodeCategoryMap.get(data
								.get(i).get("CATEGORY").toString().trim());
						data.get(i).remove("CATEGORY");
						data.get(i).put("CATEGORY", feeCodeCategory);
					}
					if(data.get(i).get("USECOUNT") ==null ){
						data.get(i).remove("USECOUNT");
						data.get(i).put("USECOUNT", "0");
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/querygoodscode")
	public ModelAndView queryGoodsCode(){
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		//获取商品分类map
		Map<String, String> GoodsCateGorymap = HfCacheUtil.getCache().getGoodsCategoryMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map feeCode = data.get(i);
					MerInfo mer = (MerInfo) merMap.get(feeCode.get("MERID"));
					if(mer != null){
						feeCode.put("MERNAME", mer.getMerName());
					}
					feeCode.put("CATEGORY2", feeCode.get("CATEGORY")); //隐形添加一个分类信息，表示的是数字     --赵俊宝
					String category = GoodsCateGorymap.get(feeCode.get("CATEGORY"));
					feeCode.put("CATEGORY", category);
				
					data.set(i, feeCode);
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		//获取商品分类map
		Map<String, String> GoodsCateGorymap = HfCacheUtil.getCache().getGoodsCategoryMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		Map<String, Object> map2 = new HashMap();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			List<Map<String, Object>> data = service.query(queryKey, map);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
				Map feeCode = data.get(i);
				MerInfo mer = (MerInfo) merMap.get(feeCode.get("MERID"));
				if(mer != null){
					feeCode.put("MERNAME", mer.getMerName());
				}
				//商品分类分级显示,目前可最多显示3级
				String category = (String) feeCode.get("CATEGORY");
				if(category != null){
					String categoryName = "";
					for(int j = category.length()/2;j>=1;j--){
						categoryName = GoodsCateGorymap.get(category);
						feeCode.put("CATEGORY"+j, categoryName);
						category = category.substring(0, category.length()-2);
					}
				}
				int servType = (Integer) feeCode.get("SERVTYPE");
				feeCode.put("SERVTYPE", servType==2?"按次":"包月");
				data.set(i, feeCode);
			}
			map2.put("feeCode", data);
		}
		return new ModelAndView("excelViewGoodsFeeCodeAdd", "excel", map2);
	}

	/**
	 * ********************************************
	 * method name   : add 
	 * description   : 实际保存方法
	 * @return       : ModelAndView
	 * @param        : @param feeCodes
	 * @param        : @param merId
	 * @param        : @param goodsId
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-10-30  下午08:56:01
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/add")
	public ModelAndView add(String feeCodes, String merId, String goodsId) {
		String res = "0";
		String msg = "";
		String insertRes = "no"; // 记录插入操作结果
		User user = getUser();// 获取当前登录
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merId", merId);
		mapWhere.put("goodsId", goodsId);
		GoodsInfo goodsInfo = goodsInfoService.load(mapWhere);
		String busiType = "01";
		String amount = "";
		if (goodsInfo != null) {
			busiType = goodsInfo.getBusiType();
			amount = goodsInfo.getAmount();
		}
		FeeCode feeCode = new FeeCode();
		feeCode.setMerId(merId);
		feeCode.setGoodsId(goodsId);
		// 根据商户号，商品号获取已经配置过得计费代码
		List<String> nFeeCodesList = new ArrayList<String>(); // 新计费代码集合
		feeCodes = feeCodes.substring(0, feeCodes.length() - 1); // 去掉前台传过来的最后一个逗号
		String[] nFeeCode = feeCodes.split(",");
		for (int j = 0; j < nFeeCode.length; j++) {
			nFeeCodesList.add(nFeeCode[j].trim()); // 放入list中
			FeeCode fCode = feeCodeService.load(nFeeCode[j]);
			if (busiType.equals("10") || busiType.equals("11")) {
				if (!amount.equals(fCode.getAmount())) {
					JSONObject json = new JSONObject();
					try {
						json.put("res", "0");
						json.put("msg", "计费代码与商品金额不一致，请重新添加");
					} catch (JSONException e) {
					}
					return new ModelAndView("jsonView", "ajax_json", json
							.toString()); // 金额不一致 返回失败操作
				}
			}
			// fList.add(feeCodeService.load(nFeeCode[j]));
			// //通过计费代码查询计费代码对象，放入list中
		}
		// 添加操作 begin
		if (nFeeCodesList != null && nFeeCodesList.size() != 0) {
			try {
				insertRes = goodsFeeCodeService.insertGoodsFeeCode(nFeeCodesList,
						feeCode, user);
			} catch (Exception e) {
				log.error("添加商品计费代码失败", e);
			}
		}
		// end
		if (insertRes.equals("yes")) {
			res = "1";
			msg = "添加计费代码成功";
		} else {
			res = "0";
			msg = "添加计费代码" + nFeeCodesList.toString() + "失败";
		}
		JSONObject json2 = new JSONObject();
		try {
			json2.put("res", res);
			json2.put("msg", msg);
		} catch (JSONException e) {
		}
		return new ModelAndView("jsonView", "ajax_json", json2.toString());
	}
	/**
	 * ********************************************
	 * method name   : showDetail 
	 * description   : 添加商品计费代码功能中的详情显示按钮
	 * @return       : String
	 * @param        : @param merId
	 * @param        : @param goodsId
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-11-1  下午03:17:54
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/detail")
	public String showDetail(String merId, String goodsId, ModelMap modeMap)
			 {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merId", merId);
		mapWhere.put("goodsId", goodsId);
		GoodsInfo goods = goodsInfoService.load(mapWhere);
		goods.trim();     //将对象中属性值去空
		if(goods != null){
			if(goods.getServType()=="3"||goods.getServType().equals("3")){
				GoodsInfo monGoodsInfo=goodsInfoService.loadMonGoods(mapWhere);
				goods.setServMonth(monGoodsInfo.getServMonth());
				goods.setConMode(monGoodsInfo.getConMode());
				goods.setInterval(monGoodsInfo.getInterval());
			}
		    }
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		String merid=goods.getMerId(); 
		MerInfo merInfo = (MerInfo) merMap.get(merid);
		String merName = "";
		if(merInfo != null){
			merName = merInfo.getMerName();
		}
		String categoryName = HfCacheUtil.getCache().getCategoryAbsoluteName(goods.getCategory());
		goods.setCategory(categoryName);
		goods.setMerName(merName); // 手动设置 修改页面展示商户名称
		goods.setPushInf(goods.getPushInf()+goods.getMtNum());//处理页面展示短信下发信息
		modeMap.addAttribute("goods", goods);
		return "goodscodeadd/detail";
	}
}
