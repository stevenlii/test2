package com.umpay.hfmng.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.model.MerCnf;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MerCnfService;
import com.umpay.hfmng.service.MerInfoService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/mercnf")
public class MerCnfAction extends BaseAction {
	@Autowired
	private MerCnfService merCnfService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		String opts = HfCacheUtil.getCache().getUrlAcl("mercnf");
		modeMap.addAttribute("opts", opts);
		return "mercnf/index";
	}

	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
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
	
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap) {
		MerCnf merCnf = merCnfService.load(id);
		merCnf.trim();
		Map<String, Object> allUserMap = HfCacheUtil.getCache().getUserInfoMap();
		@SuppressWarnings("unchecked")
		Map<String, Object> m = (Map<String, Object>) allUserMap.get(merCnf.getModUser());
		if(m!=null){
			String userName=String.valueOf(m.get("userName"));
			merCnf.setModUser(StringUtils.trim(userName));
		}
		modeMap.addAttribute("merCnf", merCnf);
		return "mercnf/detail";
	}
	
	@RequestMapping(value = "/add")
	public String add(ModelMap modeMap) {
		return "mercnf/add";
	}
	
	@RequestMapping(value = "/modify")
	public String modify(String id, ModelMap modeMap) {
		MerCnf merCnf = merCnfService.load(id);
		merCnf.trim();
		modeMap.addAttribute("merCnf", merCnf);
		return "mercnf/modify";
	}
	
	@RequestMapping(value = "/save")
	public ModelAndView save(MerCnf merCnf) {
		merCnf.trim();
		User user = this.getUser();
		merCnf.setModUser(user.getId());
		merCnf.setMerCert(new byte[0]);
		String rs = "0";
		try {
			rs = merCnfService.saveMerCnf(merCnf); // 1表示添加成功,0代表添加失败
		} catch (Exception e) {
			log.error("添加商户配置失败", e);
			rs = "0";
		}
		return new ModelAndView("jsonView", "ajax_json", rs);
	}
	
	@RequestMapping(value = "/update")
	public ModelAndView update(MerCnf merCnf, ModelMap modeMap) {
		merCnf.trim();
		User user = this.getUser();
		merCnf.setModUser(user.getId()); // 修改人
		String result = "0";
		try {
			result = merCnfService.modifyMerCnf(merCnf); // 修改操作,string表示操作结果,返回值为1或者0
		} catch (Exception e) {
			log.error("修改商户配置失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/checkMerCnf")
	public ModelAndView checkMerCnf(String merId) {
		String msg = "1"; // 默认结果 1表示不存在 0 表示存在
		MerCnf merCnf = merCnfService.load(merId);
		if (merCnf != null) {
			msg = "0";
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	@RequestMapping(value = "/conf")
	public String loadConfig(String id, ModelMap modeMap) {
		MerCnf merCnf = merCnfService.load(id);
		merCnf.trim();
		modeMap.addAttribute("merCnf", merCnf);
		return "mercnf/modCert";
	}
	
	@RequestMapping(value = "/modCert")
	public ModelAndView modCert(HttpServletRequest request,MerCnf merCnf) {
		merCnf.trim();
		String result = "0";//操作结果 1表示成功 0 表示失败 2表示大小超过了4000字节
		MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
		User user = this.getUser();
		merCnf.setModUser(user.getId());
		MultipartFile file = req.getFile("file");
		try {
			result = merCnfService.updateCert(file, merCnf);
		} catch (Exception e) {
			log.error("更新商户证书失败",e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
}
