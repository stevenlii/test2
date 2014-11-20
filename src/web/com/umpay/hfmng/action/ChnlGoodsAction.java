/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlGoodsAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-27
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.ChnlGoods;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.ChnlGoodsService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  ChnlGoodsAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  渠道商品管理
 * ************************************************/
@Controller
@RequestMapping("/chnlgoods")
public class ChnlGoodsAction extends BaseAction {
	
	@Autowired
	private OptionService optionService;
	@Autowired
	private ChnlGoodsService chnlGoodsService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("chnlgoods");
		modeMap.addAttribute("opts", opts);
		return "chnlgoods/index";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		Map users = optionService.getAllUserIdAndName();
		Map<String, ChnlInf> chnlInfMap = HfCacheUtil.getCache().getChnlInfMap();
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map chnlGoods = data.get(i);
					ChnlInf chnlInf = chnlInfMap.get(chnlGoods.get("CHANNELID"));
					if(chnlInf != null){
						chnlGoods.put("CHANNELNAME", chnlInf.getChannelName());
					}
					chnlGoods.put("SERVICE_USER", users.get(chnlGoods.get("SERVICE_USER")));
					data.set(i, chnlGoods);
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
	
	@RequestMapping(value = "/modify")
	public String modify(ModelMap modeMap){
		String zTreeNodes = "[{ id:'goods', pId:'all', name:'全部商品', nocheck:true }]";  //构造树节点
		modeMap.addAttribute("zNodes", zTreeNodes);
		return "chnlgoods/modify";
	}
	
	@RequestMapping(value = "/gettree")
	public ModelAndView getTree(String channelId, String merId){
		String zTreeNodes="";  //构造树节点
		if("".equals(merId)){
			zTreeNodes = "[{id:'goods', pId:'all', name:'全部商品', nocheck:true}]";  //构造树节点
		}else{
			String MWNode = "{id:'goods', pId:'all', name:'全部商品', open:true}";
			StringBuffer nodes = new StringBuffer(); // 节点内容
			Map<String, String> map = new HashMap<String, String>();
			map.put("merId", merId);
			List<GoodsInfo> goodsList = optionService.getGoodsList(map);
			if(goodsList != null && goodsList.size() != 0){
				if("".equals(channelId)){
					for(GoodsInfo goods : goodsList){
						goods.trim();
						StringBuffer node = new StringBuffer();
						node.append("{id:'").append(goods.getGoodsId()).append("', pId:'goods', name:'").append(goods.getGoodsName()).append("'}");
						nodes.append(",").append(node);
					}
				}else{
					Map<String, ChnlGoods> chnlGoodsMap = chnlGoodsService.getMap(channelId, merId);
					if(chnlGoodsMap != null){
						Map audit = chnlGoodsService.getAuditMap(channelId, merId);
						for(GoodsInfo goods : goodsList){
							goods.trim();
							StringBuffer node = new StringBuffer();
							node.append("{id:'").append(goods.getGoodsId()).append("', pId:'goods', name:'").append(goods.getGoodsName()).append("'");
							ChnlGoods chnlGoods = chnlGoodsMap.get(goods.getGoodsId());
							if(chnlGoods == null){
								//检查是否处于待审核
								Object o = audit.get(goods.getGoodsId()); // 查询是否存在
								if (o != null) {
									node.append(", chkDisabled:true");
								}
							}else{
								if(chnlGoods.getModLock()==0){
									if(chnlGoods.getState() == 2){
										node.append(", checked:true");
									}
								}else{
									node.append(", chkDisabled:true");
									if(chnlGoods.getState() == 2){
										node.append(", checked:true");
									}
								}
							}
							node.append("}");
							nodes.append(",").append(node);
						}
					}
				}
			}else{
				MWNode = "{id:'goods', pId:'all', name:'全部商品', nocheck:true}";
			}
			zTreeNodes = "["+MWNode+nodes+"]";  //加两个父节点  再加节点内容
		}
		return new ModelAndView("jsonView", "ajax_json", zTreeNodes);
	}
	
	@RequestMapping(value = "/update")
	public ModelAndView update(String channelId, String merId, String goodsIds){
		channelId = StringUtils.trim(channelId);
		List<String> newOpen = new ArrayList<String>(); //新增开通
		List<String> modOpen = new ArrayList<String>(); //修改开通
		List<String> modClose = new ArrayList<String>(); //修改关闭
		//1-获取页面的商户号、小额银行串a
		String[] goodsId = goodsIds.split(",");
		//2-获取此商户号在库里的小额银行状态b和锁定状态
		Map<String, ChnlGoods> chnlGoods = chnlGoodsService.getMap(channelId, merId);
		Map audit = chnlGoodsService.getAuditMap(channelId, merId);
		for(int i=0;i<goodsId.length;i++){
			if("goods".equals(goodsId[i])){  //若为根节点则直接下一次循环
				continue;
			}
			ChnlGoods goods = chnlGoods.get(goodsId[i]);
			if(goods == null){
				//3-1    从a中循环取出一个元素i，与b比较，若b中不含i，则为新增开通；
				Object o = audit.get(goodsId[i]); // 查询是否存在
				if(o == null){
					newOpen.add(goodsId[i]);
				}else{
					log.error("商品号为"+goodsId[i]+"的商品已送交审核，无法重复提交！");
					return new ModelAndView("jsonView", "ajax_json", "no"); 
				}
			}else{
				goods.trim();
				// 3-2     若b中含i,则再看锁定状态，若为0，则再看state，若为4，则为修改开通；若为2，则为修改关闭
				if(goods.getModLock() == 1){
					log.error("银行号为"+goodsId[i]+"的支付银行已经锁定！");
					return new ModelAndView("jsonView", "ajax_json", "no"); 
				}else{
					if(goods.getState() == 2){
						modClose.add(goodsId[i]);
					}else if(goods.getState() == 4){
						modOpen.add(goodsId[i]);
					}
				}
			}
		}
		String result = "no";
		ChnlGoods chnlGood = new ChnlGoods();
		chnlGood.setChannelId(channelId);
		chnlGood.setMerId(merId);
		chnlGood.setService_user(getUser().getId());
		chnlGood.setModLock(1);
		//向审核表插入数据
		try{
			result = chnlGoodsService.save(chnlGood, newOpen, modOpen, modClose);
		}catch (Exception e) {
			log.error("渠道商品配置失败",e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String ID) {
		String result="no";
		String userId = this.getUser().getId();   //获取当前登录用户
	    //禁用操作，目标状态为  ”4“  
		try{
			result = chnlGoodsService.enableAnddisable(ID, userId, 4);   //rusult 为操作结果显示
		}catch(Exception e){
			log.error("禁用操作失败" + e);
		}
		 return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String ID) {
		String result="no";
		String userId = this.getUser().getId();   //获取当前登录用户
	    //禁用操作，目标状态为  ”4“  
		try{
			result = chnlGoodsService.enableAnddisable(ID, userId, 2);   //rusult 为操作结果显示
		}catch(Exception e){
			log.error("启用操作失败" + e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/detail")
	public String detail(String channelId, String merId, String goodsId, ModelMap modeMap){
		ChnlGoods chnlGoods = chnlGoodsService.load(channelId, merId, goodsId);
		if(chnlGoods != null){
			chnlGoods.trim();
			Map<String, ChnlInf> chnlInfMap = HfCacheUtil.getCache().getChnlInfMap();
			ChnlInf chnlInf = chnlInfMap.get(channelId);
			if(chnlInf != null){
				chnlInf.trim();
				String chnlName = chnlInf.getChannelName();
				log.info("获取到渠道名称：" + chnlName);
				modeMap.addAttribute("chnlName", chnlName);
			}
			
			Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
			MerInfo mer = (MerInfo) merMap.get(merId);
			mer.trim();
			log.info("获取到商户：" + mer.toString());
			String merName = mer.getMerName();
			modeMap.addAttribute("merName", merName);
			String merState = mer.getState();
			merState = "2".equals(merState)?"启用":"禁用";
			modeMap.addAttribute("merState", merState);
			
			Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
			GoodsInfo goods = (GoodsInfo) goodsMap.get(merId + "-" + goodsId);
			goods.trim();
			log.info("获取到商品：" + goods.toString());
			String goodsName = goods.getGoodsName();
			modeMap.addAttribute("goodsName", goodsName);
			String goodsState = goods.getState();
			goodsState = "2".equals(goodsState)?"启用":"禁用";
			modeMap.addAttribute("goodsState", goodsState);
			
			Map<String, String> users = optionService.getAllUserIdAndName();
			String userName = users.get(chnlGoods.getService_user());
			chnlGoods.setService_user(userName);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String dateString = formatter.format(chnlGoods.getModTime());
			modeMap.addAttribute("modTime", dateString);
			log.info("获取到渠道商品数据：" + chnlGoods.toString());
			modeMap.addAttribute("chnlGoods", chnlGoods);
		}
		return "chnlgoods/detail";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportgoods")
	public ModelAndView exportGoods() {
		Map<String, String> users = optionService.getAllUserIdAndName();
		Map<String, ChnlInf> chnlMap = HfCacheUtil.getCache().getChnlInfMap();
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		if (queryKey != null) {
			data = query(queryKey, map, false);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
				Map chnlGoods = data.get(i);
				ChnlInf chnlInf = chnlMap.get(chnlGoods.get("CHANNELID"));
				if(chnlInf != null){
					chnlGoods.put("CHANNELNAME", chnlInf.getChannelName());
				}
				chnlGoods.put("SERVICE_USER", users.get(chnlGoods.get("SERVICE_USER")));
				int state = (Integer) chnlGoods.get("STATE");
				chnlGoods.put("STATE", getStateName(state));
				int merState = (Integer) chnlGoods.get("MERSTATE");
				chnlGoods.put("MERSTATE", getStateName(merState));
				int goodsState = (Integer) chnlGoods.get("GOODSSTATE");
				chnlGoods.put("GOODSSTATE", getStateName(goodsState));
				data.set(i, chnlGoods);
			}
		}
		return new ModelAndView("excelChnlGoods", "excel", data);
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportmer")
	public ModelAndView exportMer() {
		Map<String, ChnlInf> chnlMap = HfCacheUtil.getCache().getChnlInfMap();
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		if (queryKey != null) {
			data = query(queryKey, map, false);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
				Map chnlGoods = data.get(i);
				ChnlInf chnlInf = chnlMap.get(chnlGoods.get("CHANNELID"));
				if(chnlInf != null){
					chnlGoods.put("CHANNELNAME", chnlInf.getChannelName());
				}
				int merState = (Integer) chnlGoods.get("MERSTATE");
				chnlGoods.put("MERSTATE", getStateName(merState));
				data.set(i, chnlGoods);
			}
		}
		return new ModelAndView("excelChnlMer", "excel", data);
	}
	
	private String getStateName(int state){
		String stateName = "";
		if(state == 2){
			stateName ="启用";
		}else if(state == 4){
			stateName ="禁用";
		}
		return stateName;
	}

}
