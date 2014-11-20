package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.ChnlGoods;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.ChnlGoodsService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * ******************  类说明  *********************
 * class       :  ChnlGoodsAuditAction
 * @author     :  lz
 * @version    :  1.0  
 * description :  渠道商品审核
 * @see        :                        
 * ***********************************************
 */
@Controller
@RequestMapping("/chnlgoodsaudit")
public class ChnlGoodsAuditAction extends BaseAction {
	@Autowired
	private AuditService auditService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private ChnlGoodsService chnlGoodsService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("chnlgoods");
		modeMap.addAttribute("opts", opts);
		return "chnlgoodsaudit/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query(){
		Map<String, String>  users = optionService.getAllUserIdAndName();
		Map<String, String>  map = getParametersFromRequest(super.getHttpRequest());
		HfCache cache = HfCacheUtil.getCache();
		Map<String, ChnlInf> chnlInfMap = cache.getChnlInfMap();
		Map<String, Object> goodsInfMap = cache.getGoodsInfoMap();
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> merNameMap = cache.getMerNameMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map<String, Object> audit = data.get(i);
					//解析json串
					ChnlGoods modData = (ChnlGoods) JsonHFUtil.getObjFromJsonArrStr(
											(String) audit.get("modData"), ChnlGoods.class);
					String channelId = modData.getChannelId();
					audit.put("CHANNELID", channelId);
					audit.put("CHANNELNAME", chnlInfMap.get(channelId).getChannelName());
					String merId = modData.getMerId();
					audit.put("MERID", merId);
					String merName = merNameMap.get(merId);
					audit.put("MERNAME", merName);
					String goodsId = modData.getGoodsId();
					audit.put("GOODSID", goodsId);
					String goodsName = "";
					Object obj = goodsInfMap.get(merId+"-"+goodsId);
					if(obj!=null){
						goodsName = ((GoodsInfo)obj).getGoodsName();
					}
					audit.put("GOODSNAME", goodsName);
					String creatorId = (String) audit.get("creator");
					audit.put("CREATOR", users.get(creatorId));
					if(audit.get("CREATOR") == null){
						audit.put("CREATOR", messageService.getSystemParam(creatorId));
					}
					if("0".equals(audit.get("state").toString())){
						audit.put("MODTIME", "");
						audit.put("MODUSER", "");
					}else{
						String modUserId = (String) audit.get("modUser");
						audit.put("MODUSER", users.get(modUserId));
						if(audit.get("MODUSER") == null){
							audit.put("MODUSER", messageService.getSystemParam(modUserId));
						}
					}
					data.set(i, audit);
				}
				long count = queryCount(queryKey, map);
				ObjectUtil.trimData(data);
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
	
	@RequestMapping(value = "/auditPass")
	public ModelAndView auditPass(String id,String resultDesc){
		String string = "no";
		try{
			String[] ids = id.split(",");
			String userId = getUser().getId();
			string = chnlGoodsService.auditPass(ids, userId, resultDesc);  //返回值为yes或者no
		}catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	@RequestMapping(value = "/auditNotPass")
	public ModelAndView auditNotPass(String id, String resultDesc){
		String string = "no";
		try{
			String[] ids = id.split(",");
			String userId = getUser().getId();
			string = chnlGoodsService.auditNotPass(ids, userId, resultDesc);  //返回值为yes或者no
		}catch (Exception e) {
			log.error("审核不通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("chnlgoods");
		modeMap.addAttribute("opts", opts);
		Map mapId = new HashMap();
		mapId.put("id", id);
		Audit au = auditService.load(mapId);
		au.trim();
		modeMap.addAttribute("audit", au);
		//反序列化审核信息
		ChnlGoods chnlGoods = (ChnlGoods) JsonHFUtil.getObjFromJsonArrStr(au.getModData(),ChnlGoods.class);
		chnlGoods.trim();
		Map<String, ChnlInf> chnlInfMap = HfCacheUtil.getCache().getChnlInfMap();
		ChnlInf chnlInf = chnlInfMap.get(chnlGoods.getChannelId());
		if(chnlInf != null){
			chnlInf.trim();
			String chnlName = chnlInf.getChannelName();
			log.info("获取到渠道名称：" + chnlName);
			modeMap.addAttribute("chnlName", chnlName);
		}
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		MerInfo mer = (MerInfo) merMap.get(chnlGoods.getMerId());
		mer.trim();
		log.info("获取到商户：" + mer.toString());
		String merName = mer.getMerName();
		modeMap.addAttribute("merName", merName);
		String merState = mer.getState();
		merState = "2".equals(merState)?"启用":"禁用";
		modeMap.addAttribute("merState", merState);
		
		Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
		GoodsInfo goods = (GoodsInfo) goodsMap.get(chnlGoods.getMerId() + "-" + chnlGoods.getGoodsId());
		goods.trim();
		log.info("获取到商品：" + goods.toString());
		String goodsName = goods.getGoodsName();
		modeMap.addAttribute("goodsName", goodsName);
		String goodsState = goods.getState();
		goodsState = "2".equals(goodsState)?"启用":"禁用";
		modeMap.addAttribute("goodsState", goodsState);
		
		Map users = optionService.getAllUserIdAndName();
		String userName = (String) users.get(chnlGoods.getService_user());
		chnlGoods.setService_user(userName);
		log.info("获取到渠道银行数据：" + chnlGoods.toString());
		modeMap.addAttribute("chnlGoods", chnlGoods);
		return "chnlgoodsaudit/detail";
	}

}
