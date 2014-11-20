/** *****************  JAVA头文件说明  ****************
 * file name  :  BusiConfAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-1-15
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.MerBusiConf;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.BusiConfService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  BusiConfAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Controller
@RequestMapping("/merbusiconf")
public class BusiConfAction extends BaseAction {
	
	@Autowired
	private OptionService optionService;
	@Autowired
	private BusiConfService busiConfService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		Map<String, String> map = optionService.getMerBusiTypeMap();
		String tree = ZTreeUtil.buildZTree(map);
		tree = "["+tree+"]";
		modeMap.addAttribute("busiType", tree);
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("merbusiconf");
		modeMap.addAttribute("opts", opts);
		return "merbusiconf/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> users = optionService.getAllUserIdAndName();
				Map<String, String> bizTypeMap = optionService.getMerBizTypeMap();
				Map<String, String> busiTypeMap = optionService.getMerBusiTypeMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map<String, Object> db = data.get(i);
					db.put("BIZTYPENAME", bizTypeMap.get(db.get("BIZTYPE")));
					db.put("MODUSER", users.get(db.get("MODUSER")));
					String busiType = (String) db.get("BUSITYPE");
					db.put("BUSITYPE", busiTypeMap.get(busiType));
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
//		Map<String, String> bizTypeMap = optionService.getMerBizTypeMap();
//		modeMap.addAttribute("bizTypeMap", bizTypeMap);
		
		return "merbusiconf/add";
	}
	
	@RequestMapping(value = "/getconf")
	public ModelAndView getConf(String merId){
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Map<String,Object> map=new HashMap<String, Object>();
		if(merMap.get(merId)!=null){
			MerInfo mer=(MerInfo)merMap.get(merId);
			map.put("merName", mer.getMerName());
			map.put("busiType", mer.getBusiType());
		}
		List<MerBusiConf> mbcList=busiConfService.getListByMerId(merId);
		List<String> list=new ArrayList<String>();
		for(MerBusiConf mbc:mbcList){
			list.add(mbc.getBizType());
		}
		map.put("bizTypes", list);
		String jsonString = JsonHFUtil.getJsonArrStrFrom(map);
		return new ModelAndView("jsonView", "ajax_json", jsonString);
	}
	
	@RequestMapping(value = "/save")
	public ModelAndView save(MerBusiConf merBusiConf){
		String res = "0";
		if(merBusiConf.getMerId()==null || merBusiConf.getMerId().equals("")){
			res="请选择商户号";
		}else if(merBusiConf.getBizType()==null || merBusiConf.getBizType().equals("")){
			res="请选择新的支付类型";
		}else{
			try {
				res = busiConfService.save(merBusiConf);
			} catch (Exception e) {
				log.error("新增业务类型失败", e);
			}
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		if (queryKey != null) {
			Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
			Map<String, String> bizTypeMap = optionService.getMerBizTypeMap();
			data = query(queryKey, map, false);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
				Map<String, Object> db = data.get(i);
				db.put("BIZTYPENAME", bizTypeMap.get(db.get("BIZTYPE")));
				MerInfo merInfo = (MerInfo) merMap.get(db.get("MERID"));
				if(merInfo!=null){
					db.put("MERNAME", merInfo.getMerName());
					db.put("BUSITYPE", merInfo.getBusiType());
				}
			}
		}
		return new ModelAndView("excelMerBusiConf", "excel", data);
	}
	
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String merId,String bizType) {
		String[] array = bizType.split(",");
		String result = "0";
		try {
			result = busiConfService.enableAndDisable(merId,array, 4); // 禁用操作,目标状态为"4"
		} catch (Exception e) {
			log.error("禁用操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String merId,String bizType) {
		String[] array = bizType.split(",");
		String result = "0";
		try {
			result = busiConfService.enableAndDisable(merId,array, 2); // 禁用操作,目标状态为"4"
		} catch (Exception e) {
			log.error("启用操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
}
