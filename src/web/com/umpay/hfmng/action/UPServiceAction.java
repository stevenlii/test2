/** *****************  JAVA头文件说明  ****************
 * file name  :  UpServiceAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-9-26
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.model.UPService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.hfmng.service.UPServiceService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  UpServiceAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  综合支付计费代码配置
 * ************************************************/
@Controller
@RequestMapping("/upservice")
public class UPServiceAction extends BaseAction {
	
	@Autowired
	private OptionService optionService;
	@Autowired
	private UPServiceService upServiceService;
	
	/**
	 * ********************************************
	 * method name   : index 
	 * description   : 查询页
	 * @return       : String
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-20  上午11:37:36
	 * *******************************************
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		String tree = ZTreeUtil.getGoodsCategoryWithAll();
		modeMap.addAttribute("zNodes", tree);
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("upservice");
		modeMap.addAttribute("opts", opts);
		return "upservice/index";
	}
	/**
	 * ********************************************
	 * method name   : query 
	 * description   : 分页查询方法
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-20  上午11:37:49
	 * *******************************************
	 */
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				dealData(data);
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
	 * method name   : dealData 
	 * description   : 对查询到的数据做处理
	 * @return       : void
	 * @param        : @param data
	 * modified      : xuhuafeng ,  2014-10-20  上午11:38:06
	 * *******************************************
	 */
	private void dealData(List<Map<String, Object>> data){
		ObjectUtil.trimData(data);
		Map<String, String> users = optionService.getAllUserIdAndName();
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Map<String, GoodsInfo> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap2();
		Map<String, String> categoryMap = HfCacheUtil.getCache().getGoodsCategoryMap();
		Map<String, String> gateMap = optionService.getGateMap();
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> db = data.get(i);
			MerInfo mer = (MerInfo) merMap.get(db.get("MERID"));
			if(mer != null){
				db.put("MERNAME", mer.getMerName());
			}
			GoodsInfo goods = goodsMap.get(db.get("MERID") + "-" + db.get("GOODSID"));
			if(goods != null){
				db.put("GOODSNAME", goods.getGoodsName());
			}
			Object gateId = db.get("GATEID");
			db.put("GATENAME", gateMap.containsKey(gateId) ? gateMap.get(gateId) : gateId);
			
			String feeType = db.get("FEETYPE").toString();
			if("2".equals(feeType)){
				feeType = "按次";
			}else if("3".equals(feeType)){
				feeType = "包月";
			}
			db.put("FEETYPE", feeType);
			
			String state = db.get("STATE").toString();
			if("2".equals(state)){
				state = "启用";
			}else{
				state = "禁用";
			}
			db.put("STATENAME", state);
			
			db.put("MODUSER", users.get(db.get("MODUSER")));
			Object category = db.get("CATEGORY");
			if(category == null){
				category = "";
			}
			db.put("CATEGORY", categoryMap.containsKey(category) ? categoryMap.get(category) : category);
		}
	}
	/**
	 * ********************************************
	 * method name   : add 
	 * description   : 新增页
	 * @return       : String
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-20  上午11:40:00
	 * *******************************************
	 */
	@RequestMapping(value = "/add")
	public String add()  {
		return "upservice/add";
	}
	/**
	 * ********************************************
	 * method name   : save 
	 * description   : 新增入库方法
	 * @return       : ModelAndView
	 * @param        : @param upService
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-20  上午11:40:16
	 * *******************************************
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(UPService upService) {
		upService.setModUser(this.getUser().getId()); // 新增修改人操作
		String res = "0";
		try {
			res = upServiceService.save(upService); // 1表示添加成功
		} catch (Exception e) {
			log.error("添加商品计费代码失败" + upService.toString(), e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	/**
	 * ********************************************
	 * method name   : modify 
	 * description   : 修改页
	 * @return       : String
	 * @param        : @param merId
	 * @param        : @param goodsId
	 * @param        : @param gateId
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-20  上午11:41:05
	 * *******************************************
	 */
	@RequestMapping(value = "/modify")
	public String modify(String merId, String goodsId, String gateId, ModelMap modeMap)  {
		UPService us = upServiceService.load(merId, goodsId, gateId);
		modeMap.addAttribute("us", us);
		
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Map<String, GoodsInfo> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap2();
		Map<String, String> gateMap = optionService.getGateMap();
		
		MerInfo mer = (MerInfo) merMap.get(merId);
		String merName = "";
		if(mer != null){
			merName = mer.getMerName();
		}
		modeMap.addAttribute("merName", merName);
		
		GoodsInfo goods = goodsMap.get(merId+"-"+goodsId);
		String goodsName = "";
		if(goods != null){
			goodsName = goods.getGoodsName();
		}
		modeMap.addAttribute("goodsName", goodsName);
		
		modeMap.addAttribute("gateName", gateMap.get(gateId));
		return "upservice/modify";
	}
	/**
	 * ********************************************
	 * method name   : update 
	 * description   : 修改入库方法
	 * @return       : ModelAndView
	 * @param        : @param upService
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-20  上午11:41:54
	 * *******************************************
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(UPService upService) {
		upService.setModUser(this.getUser().getId()); // 新增修改人操作
		String res = "0";
		try {
			res = upServiceService.update(upService); // 1表示修改成功
		} catch (Exception e) {
			log.error("更新商品计费代码失败" + upService.toString(), e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	/**
	 * ********************************************
	 * method name   : enable 
	 * description   : 启用方法
	 * @return       : ModelAndView
	 * @param        : @param ID
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-20  上午11:42:17
	 * *******************************************
	 */
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String ID) {
		String res = "0";
		try {
			res = upServiceService.updateState(ID, "2");
		} catch (Exception e) {
			log.error("启用商品计费代码失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	/**
	 * ********************************************
	 * method name   : disable 
	 * description   : 禁用方法
	 * @return       : ModelAndView
	 * @param        : @param ID
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-20  上午11:42:28
	 * *******************************************
	 */
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String ID) {
		String res = "0";
		try {
			res = upServiceService.updateState(ID, "4");
		} catch (Exception e) {
			log.error("禁用商品计费代码失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	/**
	 * ********************************************
	 * method name   : export 
	 * description   : 导出方法
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-10-20  上午11:42:38
	 * *******************************************
	 */
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		List<Map<String, Object>> data = null;
		if (queryKey != null) {
			try {
				data = query(queryKey, map, false);;
				dealData(data);
			} catch (Exception e) {
				log.error("导出失败", e);
			}
		} 
		return new ModelAndView("upServiceView", "excel", data);
	}

}
