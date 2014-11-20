package com.umpay.hfmng.action;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.model.Para;
import com.umpay.hfmng.service.ParaService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/para")
public class ParaAction extends BaseAction {
	@Autowired
	private ParaService paraService;

	/**
	 * @Title: list
	 * @Description: 链接到系统参数管理页面
	 * @param
	 * @return String
	 * @throws DataAccessException
	 * @author wangyuxin
	 * @date 2012-12-22 下午05:36:54
	 */
	@RequestMapping(value = "/indexb")
	public String indexb() {
		return "para/indexb";
	}
	
	/**
	 * @Title: list
	 * @Description: 链接到系统参数管理页面
	 * @param
	 * @return String
	 * @throws DataAccessException
	 * @author wangyuxin
	 * @date 2012-12-22 下午05:36:54
	 */
	@RequestMapping(value = "/indexs")
	public String indexs() {
		return "para/indexs";
	}

	/**
	 * @Title: add
	 * @Description: 链接到新增参数页面
	 * @param
	 * @return String
	 * @throws DataAccessException
	 * @author wangyuxin
	 * @date 2012-12-22 下午01:18:45
	 */
	@RequestMapping(value = "/add")
	public String add(ModelMap modelMap) {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String paraType = (String) map.get("paraType");
		if("B".equals(paraType)){
			modelMap.addAttribute("paraType", "B");
		}
		if("S".equals(paraType)){
			modelMap.addAttribute("paraType", "S");
		}
		return "para/addpara";
	}

	/**
	 * @Title: save
	 * @Description: 新增兑换券信息
	 * @param couponInf
	 * @return ModelAndView
	 * @author wangyuxin
	 * @date 2012-11-22 下午04:55:00
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(Para para, ModelMap modeMap) throws Exception {
		User user = this.getUser();
//		String paraTypeString = para.getParaType().substring(0,2);
		String msg = "0";
		try{
			//SP(系统非转化参数)和BP（业务费转化参数）不可新增
			//去掉新增限制 wangyuxin 2013.4.12
//			if(!"SP".equals(paraTypeString) && !"BP".equals(paraTypeString)){
				paraService.insertPara(para, user);
				msg = "1";
//			}
		}catch (Exception e) {
			log.error(StringUtil.ExceptionToString(e));
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	/**
	 * @Title: query
	 * @Description: 查询业务参数信息
	 * @param
	 * @return ModelAndView
	 * @author wangyuxin
	 * @date 2012-11-25 下午02:31:49
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryb")
	public ModelAndView queryb() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String paraType = (String) map.get("paraType");
		if(paraType==null || "".equals(paraType)){
			map.put("paraType", "B");
		}
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
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
	 * @Title: query
	 * @Description: 查询系统参数信息
	 * @param
	 * @return ModelAndView
	 * @author wangyuxin
	 * @date 2012-11-25 下午02:31:49
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/querys")
	public ModelAndView querys() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String paraType = (String) map.get("paraType");
		if(paraType==null || "".equals(paraType)){
			map.put("paraType", "S");
		}
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				// 格式化数据
				ObjectUtil.trimData(data);
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
	 * @Title: getTypes
	 * @Description: 获取参数类型
	 * @param
	 * @return
	 * @author wangyuxin
	 * @date 2012-12-27 下午07:27:13
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getTypes")
	public ModelAndView getTypes() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String paraType = (String) map.get("paraType");
		JSONArray result = new JSONArray();
		List<Para> listPara = null;
		try {
			listPara = paraService.getTypes(paraType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Para para : listPara) {
			result.add(buildSelect(para.getParaType().trim(), para
					.getParaType().trim()));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name : buildSelect
	 * description : 构造select中得text和value键值对
	 * 
	 * @return : JSONObject
	 * @param : @param id
	 * @param : @param name
	 * @param : @return modified : Administrator , 2012-8-14 下午04:48:38
	 * @see : *******************************************
	 */
	public static JSONObject buildSelect(String id, String name) {
		JSONObject result = new JSONObject();
		result.put("value", id);
		result.put("text", name);
		return result;
	}

	/**
	 * @Title: list
	 * @Description: 链接到修改参数管理页面
	 * @param
	 * @return String
	 * @throws DataAccessException
	 * @author wangyuxin
	 * @date 2012-12-22 下午05:36:54
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/mod")
	public String mod(String paraType, String paraCode, ModelMap modelMap) {
		try {
			Para para = paraService.getPara(paraType, paraCode);
			modelMap.addAttribute("para",para);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "para/modpara";
	}
	
	/**
	 * @Title: save
	 * @Description: 修改参数信息
	 * @param para
	 * @return ModelAndView
	 * @author wangyuxin
	 * @date 2012-11-27 下午04:55:00
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(Para para, ModelMap modeMap) throws Exception {
		User user = this.getUser();
		String msg = "0";
		try{
			paraService.updatePara(para, user);
			msg = "1";
		}catch (Exception e) {
			log.error(StringUtil.ExceptionToString(e));
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	/**
	 * @Title: save
	 * @Description: 验证是否存在兑换券信息
	 * @param para
	 * @return ModelAndView
	 * @author wangyuxin
	 * @date 2012-12-28 下午04:55:00
	 */
	@RequestMapping(value = "/validate")
	public ModelAndView validate(Para para, ModelMap modeMap) throws Exception {
		User user = this.getUser();
		String msg = paraService.validatePara(para, user);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

}
