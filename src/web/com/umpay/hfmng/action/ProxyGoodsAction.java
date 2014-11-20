/** *****************  JAVA头文件说明  ****************
 * file name  :  ProxyGoodsAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-11
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.ProxyGoods;
import com.umpay.hfmng.model.ProxyGoodsLimit;
import com.umpay.hfmng.model.SecMerInf;
import com.umpay.hfmng.service.OptionService;
import com.umpay.hfmng.service.ProxyGoodsService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  ProxyGoodsAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  代理商品管理
 * ************************************************/
@Controller
@RequestMapping("/proxygoods")
public class ProxyGoodsAction extends BaseAction {
	
	@Autowired
	private OptionService optionService;
	@Autowired
	private ProxyGoodsService proxyGoodsService;
	
	private final String ORIGINALTREE = "[{id:'goods', pId:'all', name:'全部商品', nocheck:true}]";
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("proxygoods");
		modeMap.addAttribute("opts", opts);
		return "proxygoods/index";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> secMerNameMap = HfCacheUtil.getCache().getSecMerNameMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map goods = data.get(i);
					goods.put("SUBMERNAME", secMerNameMap.get(goods.get("SUBMERID")));
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
	
	@RequestMapping(value = "/add")
	public String add(ModelMap modeMap){
		modeMap.addAttribute("zNodes", ORIGINALTREE);
		return "proxygoods/add";
	}
	
	@RequestMapping(value = "/save")
	public ModelAndView save(ProxyGoodsLimit proxyGoodsLimit) {
		proxyGoodsLimit.setModUser(this.getUser().getId()); // 新增修改人操作
		String res = "0";
		try {
			res = proxyGoodsService.save(proxyGoodsLimit); // 1表示添加成功,0代表添加失败
		} catch (Exception e) {
			log.error("添加代理商品操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	
	@RequestMapping(value = "/batchAdd")
	public String batchAdd(ModelMap modeMap){
		modeMap.addAttribute("zNodes", ORIGINALTREE);
		return "proxygoods/batchAdd";
	}
	
	@RequestMapping(value = "/batchSave")
	public ModelAndView batchSave(ProxyGoodsLimit proxyGoodsLimit) {
		proxyGoodsLimit.setModUser(this.getUser().getId()); // 新增修改人操作
		String res = "0";
		try {
			res = proxyGoodsService.batchSave(proxyGoodsLimit); // 1表示添加成功,0代表添加失败
		} catch (Exception e) {
			log.error("添加代理商品操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	
	@RequestMapping(value = "/getMerIdAndTree")
	public ModelAndView getMerIdAndTree(String subMerId) {
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, String> secMerNameMap = HfCacheUtil.getCache().getSecMerNameMap();
		res.put("subMerName", secMerNameMap.get(subMerId));
		List<ProxyGoods> goodsList = proxyGoodsService.findEnableBySubMer(subMerId);
		if(goodsList != null){
			JSONArray json = new JSONArray();
			boolean isOnlyOne=false;
			if(goodsList.size() != 0){
				for(ProxyGoods pg : goodsList){
					//如果有启用的，则只显示所属商户
					isOnlyOne=true;
					String merId = StringUtils.trim(pg.getMerId());
					json.add(OptionAction.buildSelect(merId, merId));
				}
			}
			
			if(!isOnlyOne){
				Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
				for(Entry<String, Object> entry : merMap.entrySet()){
					String key=entry.getKey().trim();
					json.add(OptionAction.buildSelect(key, key));
				}
			}
			res.put("merIdMap", json.toString());
			res.put("tree", ORIGINALTREE);
		}
		return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(res));
	}
	
	@RequestMapping(value = "/getTree")
	public ModelAndView getTree(String subMerId, String merId, boolean isBatch){
		String zTreeNodes="";  //构造树节点
		if("".equals(merId)){
			zTreeNodes = ORIGINALTREE;  //构造树节点
		}else{
			String MWNode = "{id:'goods', pId:'all', name:'全部商品', open:true";
			if(!isBatch){
				MWNode += ", nocheck:true";
			}
			MWNode += "}";
			StringBuffer nodes = new StringBuffer(); // 节点内容
			Map<String, String> map = new HashMap<String, String>();
			map.put("merId", merId);
			List<GoodsInfo> goodsList = optionService.getGoodsList(map);
			List<ProxyGoods> proxyGoodsList = proxyGoodsService.findByKey(merId, null);
			Map<String, ProxyGoods> proxyGoodsMap = new HashMap<String, ProxyGoods>();
			for(ProxyGoods g : proxyGoodsList){
				g.trim();
				proxyGoodsMap.put(g.getGoodsId(), g);
			}
			if(goodsList != null && goodsList.size() != 0){
				Map<String, Audit> auditMap = proxyGoodsService.getAuditMap(merId, subMerId);
				boolean isEnable = true;
				for(GoodsInfo goods : goodsList){
					goods.trim();
					String goodsId = goods.getGoodsId();
					ProxyGoods pGoods = proxyGoodsMap.get(goodsId);
					StringBuffer node = new StringBuffer();
					node.append("{id:'").append(goodsId).append("', pId:'goods', name:'")
					.append(goodsId).append("-").append(goods.getGoodsName());
					if(pGoods != null && subMerId.equals(pGoods.getSubMerId()) && pGoods.getState()==4){
						node.append("(已禁用)");
						isEnable = false;
					}
					node.append("'");
					if(pGoods == null){
						//检查是否处于待审核
						Audit a = auditMap.get(goodsId);
						if (a != null) {
							node.append(", chkDisabled:true");
						}
					}else{
						if(!isEnable || pGoods.getState() == 2){
							node.append(", chkDisabled:true");
						}
					}
					node.append("}");
					nodes.append(",").append(node);
					isEnable = true;
				}
			}
			zTreeNodes = "["+MWNode+nodes+"]";  //加父节点  再加节点内容
		}
		return new ModelAndView("jsonView", "ajax_json", zTreeNodes);
	}
	
	@RequestMapping(value = "/modifySecMerInf")
	public String modify(String merId, String subMerId, String goodsId, ModelMap modeMap) {
		SecMerInf mer = HfCacheUtil.getCache().getSecMerMap().get(subMerId);
		modeMap.addAttribute("subMerName", mer.getSubMerName());
		ProxyGoodsLimit proxyGoods = proxyGoodsService.loadGoodsLimit(merId, subMerId, goodsId);
		modeMap.addAttribute("proxyGoods", proxyGoods);
		Map<String,Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
		GoodsInfo goods = (GoodsInfo) goodsMap.get(merId + "-" + goodsId);
		goods.trim();
		modeMap.addAttribute("goods", goods);
		return "proxygoods/modify";
	}
	
	@RequestMapping(value = "/update")
	public ModelAndView update(ProxyGoodsLimit proxyGoods, ModelMap modeMap) {
		proxyGoods.trim();
		proxyGoods.setModUser(getUser().getId()); // 修改人
		String result = "0";
		try {
			result = proxyGoodsService.update(proxyGoods); // 修改操作,string表示操作结果,返回值为1或者0
		} catch (Exception e) {
			log.error("修改商户操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String ID) {
		String result = "0";
		try {
			String[] array = ID.split(",");
			result = proxyGoodsService.enableAndDisable(array, 4); // 禁用操作,目标状态为"4"
		} catch (Exception e) {
			log.error("禁用操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String ID) {
		String result = "0";
		try {
			String[] array = ID.split(",");
			result = proxyGoodsService.enableAndDisable(array, 2); // 禁用操作,目标状态为"4"
		} catch (Exception e) {
			log.error("启用操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}

}
