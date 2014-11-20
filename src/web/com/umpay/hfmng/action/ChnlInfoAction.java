package com.umpay.hfmng.action;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.ChnlInfService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/chnlinf")
public class ChnlInfoAction extends BaseAction{
	@Autowired
	private ChnlInfService chnlInfService;
	
	@RequestMapping(value = "/index")
	public String list(ModelMap modeMap) throws DataAccessException {
		//查找权限
		HfCache hfCache=HfCacheUtil.getCache();
		String opts = hfCache.getUrlAcl("chnlinf");
		modeMap.addAttribute("opts", opts);
		return "chnlinf/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		Map<String, String> operatorNameMap = HfCacheUtil.getCache().getOperIdAndName();
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map<String, Object> chnl = data.get(i);
					String userName = "";
					String operatorId = (String) chnl.get("service_user");
					if(operatorId != null){
						userName = operatorNameMap.get(operatorId.trim());
					}
					chnl.put("SERVICE_USER",userName);
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
	public String add(ModelMap modeMap) {
		return "chnlinf/addChnl";
	}
	
	@RequestMapping(value = "/save")
	public ModelAndView save(ChnlInf chnlInf){
		chnlInf.trim();
		String rs ="0";
		try{
			Map<String, String> mapWhere = new HashMap<String, String>();
			mapWhere.put("channelId", chnlInf.getChannelId());
			String check = chnlInfService.getCheckChnlIdOrName(mapWhere);
			if(!"1".equals(check)){
				log.info("该渠道已存在或正在审核"+ chnlInf.toString());
				rs = "该渠道已存在或正在审核";
			}else{
				rs = chnlInfService.saveChnlAudit(chnlInf);  // 1表示添加成功,0代表添加失败
			}
		}catch(Exception e){
			log.info("添加渠道操作失败",e);			
		}
		return new ModelAndView("jsonView", "ajax_json", rs);
	}
	
	@RequestMapping(value = "/checkChnlId")
	public ModelAndView checkChnlId(String channelId) {
		String msg = "1"; // 默认结果 1表示不存在 0 表示存在
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("channelId", channelId.trim());
		String audit = chnlInfService.getCheckChnlIdOrName(mapWhere); // 查询是否存在
		if (audit != "1") {
			msg = "0";
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	@RequestMapping(value = "/checkChnlName")
	public ModelAndView checkChnlName(String channelName) {
		String msg = "1"; // 默认结果 1表示不存在 0 表示存在
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("channelName", channelName.trim());
		String audit = chnlInfService.getCheckChnlIdOrName(mapWhere); // 查询是否存在
		if (audit != "1") {
			msg = "0";
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	@RequestMapping(value = "/checkModChnlName")
	public ModelAndView checkModChnlName(String channelId, String channelName) {
		String msg = "1"; // 默认结果 1表示不存在 0 表示存在
		if(channelId!=null && !channelId.trim().equals("") && channelName!=null && !channelName.trim().equals("")){
			Map<String, String> mapWhere = new HashMap<String, String>();
			mapWhere.put("channelId", channelId.trim());
			mapWhere.put("channelName", channelName.trim());
			String audit = chnlInfService.checkModChnlName(mapWhere); // 查询是否存在
			if (audit != "1") {
				msg = "0";
			}
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	@RequestMapping(value = "/mod")
	public String load(String id, ModelMap modeMap) {
		ChnlInf chnl = chnlInfService.load(id);
		chnl.trim();
		modeMap.addAttribute("chnl", chnl);
		return "chnlinf/modChnl";
	}
	
	@RequestMapping(value = "/update")
	public ModelAndView update(ChnlInf chnl,ModelMap modeMap) {
		chnl.trim();
		String result = "0";
		try {
         	result = chnlInfService.modifyChnlInf(chnl); // 修改操作,string表示操作结果,返回值为1或者0
		} catch (Exception e) {
			log.error("修改渠道操作失败",e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap) {
		
		ChnlInf chnl = chnlInfService.load(id);
		chnl.trim();
		String operName = (String)HfCacheUtil.getCache().getOperIdAndName().get(chnl.getService_user());
		chnl.setService_user(operName);
		modeMap.addAttribute("chnl", chnl);
		return "chnlinf/detail";
	}
	
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String ID) {
		User user = this.getUser(); //获取当前登录用户
		ID = ID.substring(0, ID.length() - 1); // 去掉最后的一个逗号
		String[] array = ID.split(",");
		String result = "0";
		try{
			result = chnlInfService.enableAndDisable(array, user, 2);  //启用操作,目标状态为 "2"
		}catch (Exception e) {
			log.error("启用渠道操作失败",e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String ID) {
		ID = ID.substring(0, ID.length() - 1); // 去掉最后的一个逗号
		String[] array = ID.split(",");
		User user = this.getUser(); // 获取当前登录用户
		String result = "0";
		try{
			result = chnlInfService.enableAndDisable(array, user, 4);  //禁用操作,目标状态为"4"
		}catch (Exception e) {
			log.error("禁用渠道操作失败",e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		List<ChnlInf> chnlList = new ArrayList<ChnlInf>();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			List<Map<String, Object>> data = service.query(queryKey, map);
			ObjectUtil.trimData(data);
			Map<String, String>  userMap= HfCacheUtil.getCache().getOperIdAndName();
			for (int i = 0; i < data.size(); i++) {
				//渲染数据
				Map<String, Object> m = data.get(i);
				String userName = "";
				String operatorId = (String) m.get("service_user");  //获取操作人ID
				if(operatorId != null && !operatorId.equals("")){
					if(userMap != null){
						userName = (String)userMap.get(operatorId);
					}
				}
				ChnlInf chnl = new ChnlInf();
				chnl.setChannelId(m.get("channelId") == null?"" : m.get("channelId").toString());
				chnl.setChannelName(m.get("channelName") == null?"" : m.get("channelName").toString());
				chnl.setState(Integer.valueOf(m.get("state").toString()));
				chnl.setModTime((Timestamp) m.get("modTime"));
				chnl.setService_user(userName == null?"" : userName);
				chnl.setContact(m.get("contact") == null?"" : m.get("contact").toString());
				chnl.setLinkTel(m.get("linkTel") == null?"" : m.get("linkTel").toString());
				chnl.setEmail(m.get("email") == null?"" : m.get("email").toString());
				chnlList.add(chnl);
			}
		}
		return new ModelAndView("excelViewChnl", "excel", chnlList);
	}
	
	@RequestMapping(value = "/conf")
	public String loadConfig(String id, ModelMap modeMap) {
		ChnlInf chnl = chnlInfService.load(id);
		chnl.trim();
		modeMap.addAttribute("chnl", chnl);
		return "chnlinf/confChnl";
	}
	
	@RequestMapping(value = "/updateConf")
	public ModelAndView updateConf(HttpServletRequest request,ChnlInf chnl){
        chnl.trim();
		String result = "0";//操作结果 1表示成功 0 表示失败 2表示大小超过了4000字节
		MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
		 // 获得文件：
        MultipartFile file = req.getFile("file");
        try {
        	result = chnlInfService.updateConf(file, chnl); // 修改操作,string表示操作结果,返回值为1或者0
		} catch (Exception e) {
			log.error("配置渠道操作失败",e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
}
