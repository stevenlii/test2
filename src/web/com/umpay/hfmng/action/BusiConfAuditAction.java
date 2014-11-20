/** *****************  JAVA头文件说明  ****************
 * file name  :  BusiConfAuditAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-1-15
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.service.BusiConfService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  BusiConfAuditAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  商户业务类型审核页面
 * ************************************************/
@Controller
@RequestMapping("/merbusiconfaudit")
public class BusiConfAuditAction extends BaseAction {
	
	@Autowired
	private OptionService optionService;
	@Autowired
	private BusiConfService busiConfService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("merbusiconf");
		modeMap.addAttribute("opts", opts);
		return "merbusiconfaudit/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query(){
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> users = optionService.getAllUserIdAndName();
				Map<String, String> bizTypeMap = optionService.getMerBizTypeMap();
				Map<String, String> merNameMap = HfCacheUtil.getCache().getMerNameMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map<String, Object> audit = data.get(i);
					//解析json串
					audit.put("MERNAME", merNameMap.get(audit.get("IXDATA")));
					audit.put("BIZTYPE", bizTypeMap.get(audit.get("IXDATA2")));
					String creatorId = (String) audit.get("CREATOR");
					audit.put("CREATOR", users.get(creatorId));
					if("0".equals(audit.get("STATE").toString())){
						audit.put("MODTIME", "");
						audit.put("MODUSER", "");
					}else{
						String modUserId = (String) audit.get("MODUSER");
						audit.put("MODUSER", users.get(modUserId));
					}
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
	public ModelAndView auditPass(String id,String resultDesc, ModelMap modeMap){
		String res = "0";
		try{
			res = busiConfService.auditPass(id, resultDesc);
		}catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",res);
	}
	
	@RequestMapping(value = "/auditNotPass")
	public ModelAndView auditNotPass(String id, String resultDesc, ModelMap modeMap) throws DataAccessException{
		String res = "0";
		try{
			res = busiConfService.auditNotPass(id, resultDesc);
		}catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",res);
	}

}
