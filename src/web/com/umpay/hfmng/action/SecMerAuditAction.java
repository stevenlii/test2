/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerAuditAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-25
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.SecMerInf;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MerOperService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.hfmng.service.SecMerService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  SecMerAuditAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  二级商户审核页面
 * ************************************************/
@Controller
@RequestMapping("/secmeraudit")
public class SecMerAuditAction extends BaseAction {
	@Autowired
	private AuditService auditService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private SecMerService secMerService;
	@Autowired
	private MerOperService merOperService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("secmer");
		modeMap.addAttribute("opts", opts);
		return "secmeraudit/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query(){
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> users = optionService.getAllUserIdAndName();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map<String, Object> audit = data.get(i);
					//解析json串
					SecMerInf modData = (SecMerInf) JsonHFUtil.getObjFromJsonArrStr(
											(String) audit.get("modData"), SecMerInf.class);
					audit.put("SUBMERNAME", modData.getSubMerName());//名称必须来自修改的数据
					String creatorId = (String) audit.get("creator");
					audit.put("CREATOR", users.get(creatorId));
					if("0".equals(audit.get("state").toString())){
						audit.put("MODTIME", "");
						audit.put("MODUSER", "");
					}else{
						String modUserId = (String) audit.get("modUser");
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
		String[] ids = id.split(",");
		String res = "1";
		try{
			res = secMerService.auditPass(ids, resultDesc);
		}catch (Exception e) {
			res = "0";
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",res);
	}
	
	@RequestMapping(value = "/auditNotPass")
	public ModelAndView auditNotPass(String id, String resultDesc, ModelMap modeMap) throws DataAccessException{
		String[] ids = id.split(",");
		String res = "1";
		try{
			res = secMerService.auditNotPass(ids, resultDesc);
		}catch (Exception e) {
			res = "0";
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",res);
	}
	
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("secmer");
		modeMap.addAttribute("opts", opts);
		Map<String,String> mapId = new HashMap<String,String>();
		mapId.put("id", id);
		Audit au = auditService.load(mapId);
		modeMap.addAttribute("resultDesc", au.getResultDesc());
		modeMap.addAttribute("auditType", au.getAuditType());
		modeMap.addAttribute("auditId", id);
		//获取新增或修改后的渠道信息
		SecMerInf newSecMer = (SecMerInf) JsonHFUtil.getObjFromJsonArrStr(au.getModData(),SecMerInf.class);
		newSecMer.trim();
		//渲染运营负责人
		String operNameStr = merOperService.getOperNameStrByOperStr(newSecMer.getOperator());
		newSecMer.setOperator(operNameStr);
		modeMap.addAttribute("newSecMer", newSecMer);
		String state = au.getState();
		if ("0".equals(state)) {  //是否为待审核状态
			if ("2".equals(au.getAuditType())) {  //审核类型为修改,需获取历史数据展示到页面
				SecMerInf oldSecMer = secMerService.load(au.getIxData());
				oldSecMer.trim();
				//渲染运营负责人
				String oldOperNameStr = merOperService.getOperStrByMerId(oldSecMer.getSubMerId());
				oldSecMer.setOperator(oldOperNameStr);
				modeMap.addAttribute("oldSecMer", oldSecMer);
				return "secmeraudit/detailMod";  //修改详情页面
			} else {
				return "secmeraudit/detailAdd";  //新增，启用/禁用详情页面
			}
		} else {
			return "secmeraudit/showDetail";  //审核后详情页面
		}
	}

}
