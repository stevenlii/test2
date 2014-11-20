/** *****************  JAVA头文件说明  ****************
 * file name  :  MerInterAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-12-19
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
import com.umpay.hfmng.model.MerInter;
import com.umpay.hfmng.service.MerInterService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  MerInterAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  商户接口信息管理
 * ************************************************/
@Controller
@RequestMapping("/merinter")
public class MerInterAction extends BaseAction {
	
	@Autowired
	private MerInterService merInterService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("merinter");
		modeMap.addAttribute("opts", opts);
		return "merinter/index";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		Map<String, String> merMap = HfCacheUtil.getCache().getMerNameMap();
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map db = data.get(i);
					db.put("MERNAME", merMap.get(db.get("MERID")));
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
	public String add() {
		return "merinter/add";
	}
	
	@RequestMapping(value = "/save")
	public ModelAndView save(MerInter merInter) {
		String res = "0";
		User user = getUser();
		try {
			res = merInterService.save(merInter);
		} catch (Exception e) {
			log.error("新增商户接口信息失败。" + merInter+"。操作人:"+user.getId()+user.getName(), e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	
	@RequestMapping(value = "/detail")
	public String detail(String merId, String inFunCode, String inVersion, ModelMap modeMap) {
		MerInter mer = merInterService.load(merId, inFunCode, inVersion);
		Map<String, String> merMap = HfCacheUtil.getCache().getMerNameMap();
		mer.setMerId("[" + merId+ "]" + merMap.get(merId));
		modeMap.addAttribute("mer", mer);
		return "merinter/detail";
	}
	
	@RequestMapping(value = "/modify")
	public String modify(String merId, String inFunCode, String inVersion, ModelMap modeMap) {
		MerInter mer = merInterService.load(merId, inFunCode, inVersion);
		Map<String, String> merMap = HfCacheUtil.getCache().getMerNameMap();
		modeMap.addAttribute("merName", merMap.get(merId));
		modeMap.addAttribute("mer", mer);
		return "merinter/modify";
	}
	
	@RequestMapping(value = "/update")
	public ModelAndView update(MerInter merInter) {
		String res = "0";
		User user = getUser();
		try {
			res = merInterService.update(merInter);
		} catch (Exception e) {
			log.error("修改商户接口信息失败。" + merInter+"。操作人:"+user.getId()+user.getName(), e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String merId, String inFunCode, String inVersion) {
		String res = "0";
		User user = getUser();
		try {
			res = merInterService.enableAndDisable(merId, inFunCode, inVersion, 4);
		} catch (Exception e) {
			log.error("禁用商户接口信息失败。"+"。操作人:"+user.getId()+user.getName(), e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String merId, String inFunCode, String inVersion) {
		String res = "0";
		User user = getUser();
		try {
			res = merInterService.enableAndDisable(merId, inFunCode, inVersion, 2);
		} catch (Exception e) {
			log.error("启用商户接口信息失败。"+"。操作人:"+user.getId()+user.getName(), e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}

};
