package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/merset")
public class MerSetAction extends BaseAction {

	/**
	 * 
	 * @Title: mersetmid
	 * @Description: 跳转到结算抽数临时表页面
	 * @param modelMap
	 * @return
	 * @author lituo
	 * @date 2014-8-13 上午10:26:33
	 */
	@RequestMapping(value = "/mersetmid")
	public String mersetmid(ModelMap modelMap) {
		return "merset/mersetmid";
	}
	
	/**
	 * 
	 * @Title: merset
	 * @Description: 整合数据页
	 * @param modelMap
	 * @return
	 * @author lituo
	 * @date 2014-8-13 下午02:08:57
	 */
	@RequestMapping(value = "/mersetmerger")
	public String merset(ModelMap modelMap) {
		return "merset/merset";
	}
	
	/**
	 * 
	 * @Title: queryMid
	 * @Description: 查询临时表
	 * @return
	 * @author lituo
	 * @date 2014-8-13 上午10:27:14
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView queryMid() {
		HashMap map = (HashMap) getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				// 格式化数据
				ObjectUtil.trimData(data);
				long count = queryCount(queryKey, map, true);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					e.printStackTrace();
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
}
