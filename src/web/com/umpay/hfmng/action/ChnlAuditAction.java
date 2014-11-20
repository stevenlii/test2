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

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.ChnlInfService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/chnlaudit")
public class ChnlAuditAction extends BaseAction{
	@Autowired
	private AuditService auditService;
	@Autowired
	private ChnlInfService chnlInfService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value = "/index")
	public String list(ModelMap modeMap) throws DataAccessException {
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("chnlinf");
		modeMap.addAttribute("opts", opts);
		return "chnlaudit/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query(){
		Map<String, String> users = optionService.getAllUserIdAndName();
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map<String, Object> audit = data.get(i);
					//解析json串
					ChnlInf modData = (ChnlInf) JsonHFUtil.getObjFromJsonArrStr(
											(String) audit.get("modData"), ChnlInf.class);
					String channelId = modData.getChannelId();
					audit.put("CHANNELID", channelId);
					audit.put("CHANNELNAME", modData.getChannelName());//名称必须来自修改的数据
					String creatorId = (String) audit.get("creator");
					audit.put("CREATOR", users.get(creatorId));
					if(audit.get("CREATOR") == null){
						audit.put("CREATOR", messageService.getSystemParam(creatorId));
					}
					if("0".equals(audit.get("state").toString())){
						audit.put("MODTIME", "");
						audit.put("MODUSER", "");
					}else{
						String modUserId = (String) audit.get("modUser");
						audit.put("MODUSER", users.get(modUserId));
						if(audit.get("MODUSER") == null){
							audit.put("MODUSER", messageService.getSystemParam(modUserId));
						}
					}
					data.set(i, audit);
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
	
	@RequestMapping(value = "/chnlAuditPass")
	public ModelAndView AuditPass(String id,String resultDesc, ModelMap modeMap){
		String[] array = id.split(",");
		User user=this.getUser();
		String res = "no";
		try{
			res = chnlInfService.chnlAuditPass(array, user, resultDesc);//返回值为yes或者no
		}catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",res);
	}
	
	@RequestMapping(value = "/chnlAuditNotPass")
	public ModelAndView AuditNotPass(String id, String resultDesc, ModelMap modeMap) throws DataAccessException{
		String[] array = id.split(",");
		User user=this.getUser();   //获取当前登录用户
		String res = "no";
		try{
			res = chnlInfService.chnlAuditNotPass(array, user, resultDesc);//返回值为yes或者no
		}catch (Exception e) {
			log.error("审核不通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",res);
	}
	
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("chnlinf");
		modeMap.addAttribute("opts", opts);
		Map<String,String> mapId = new HashMap<String,String>();
		mapId.put("id", id);
		Audit au = auditService.load(mapId);
		modeMap.addAttribute("resultDesc", au.getResultDesc());
		modeMap.addAttribute("auditType", au.getAuditType());
		modeMap.addAttribute("auditId", id);
		//获取新增或修改后的渠道信息
		ChnlInf newChnl = (ChnlInf) JsonHFUtil.getObjFromJsonArrStr(au.getModData(),ChnlInf.class);
		newChnl.trim();
		//渲染运营负责人
		Map<String,String> operatorMap= HfCacheUtil.getCache().getOperIdAndName();
		newChnl.setService_user((String) operatorMap.get(newChnl.getService_user()));
		modeMap.addAttribute("newChnl", newChnl);
		String state = au.getState();
		if ("0".equals(state)) {  //是否为待审核状态
			if ("2".equals(au.getAuditType())) {  //审核类型为修改,需获取历史数据展示到页面
				ChnlInf oldChnl = chnlInfService.load(au.getIxData());
				oldChnl.trim();
				//渲染运营负责人
				oldChnl.setService_user((String) operatorMap.get(oldChnl.getService_user()));
				modeMap.addAttribute("oldChnl", oldChnl);
				return "chnlaudit/detailMod";  //修改详情页面
			} else {
				return "chnlaudit/detailAdd";  //新增，启用/禁用详情页面
			}
		} else {
			return "chnlaudit/showDetail";  //审核后详情页面
		}
	}
	
}
