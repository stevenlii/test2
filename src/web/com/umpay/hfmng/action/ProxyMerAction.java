/** *****************  JAVA头文件说明  ****************
 * file name  :  ProxyMerAction.java
 * owner      :  lizhen
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-24
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
import com.umpay.hfmng.service.MerOperService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  ProxyMerAction
 * @author     :  lizhen
 * @version    :  1.0  
 * description :  代理商户管理
 * ************************************************/
@Controller
@RequestMapping("/proxymer")
public class ProxyMerAction extends BaseAction {
	
	@Autowired
	private MerOperService merOperService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("proxymer");
		modeMap.addAttribute("opts", opts);
		return "proxymer/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map<String, Object> mer = data.get(i);
					String merId = (String) mer.get("MERID");
					mer.put("OPERATOR", merOperService.getOperStrByMerId(merId));
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
}
