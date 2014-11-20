/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-24
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.SecMerCnf;
import com.umpay.hfmng.model.SecMerInf;
import com.umpay.hfmng.service.MerOperService;
import com.umpay.hfmng.service.SecMerService;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  SecMerAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  二级商户管理
 * ************************************************/
@Controller
@RequestMapping("/secmer")
public class SecMerAction extends BaseAction {
	
	@Autowired
	private SecMerService secMerService;
	@Autowired
	private MerOperService merOperService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("secmer");
		modeMap.addAttribute("opts", opts);
		return "secmer/index";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map mer = data.get(i);
					String merId = (String) mer.get("SUBMERID");
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
	
	@RequestMapping(value = "/add")
	public String add(ModelMap modeMap) {
		modeMap.addAttribute("zNodes", ZTreeUtil.getOperatorTree());
		return "secmer/add";
	}
	
	@RequestMapping(value = "/save")
	public ModelAndView save(SecMerInf secMerInf) {
		secMerInf.setModUser(this.getUser().getId()); // 新增修改人操作
		String res = "0";
		try {
			res = secMerService.save(secMerInf); // 1表示添加成功,0代表添加失败
		} catch (Exception e) {
			log.error("添加商户操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	
	@RequestMapping(value = "/checkKey")
	public ModelAndView checkKey(String subMerId) {
		String res = "0";
		try {
			res = secMerService.checkKey(subMerId);
		} catch (Exception e) {
			log.error("添加商户操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	
	@RequestMapping(value = "/modifySecMerInf")
	public String modifySecMerInf(String subMerId, ModelMap modeMap) {
		SecMerInf mer = HfCacheUtil.getCache().getSecMerMap().get(subMerId);
		modeMap.addAttribute("mer", mer);
		modeMap.addAttribute("zNodes", ZTreeUtil.getModOperatorTree(subMerId));
		return "secmer/modifySecMerInf";
	}
	
	@RequestMapping(value = "/updateSecMerInf")
	public ModelAndView updateSecMerInf(SecMerInf mer, ModelMap modeMap) {
		mer.trim();
		mer.setModUser(getUser().getId()); // 修改人
		String result = "0";
		try {
			result = secMerService.updateSecMerInf(mer); // 修改操作,string表示操作结果,返回值为1或者0
		} catch (Exception e) {
			log.error("修改商户操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String subMerId) {
		String[] array = subMerId.split(",");
		String result = "0";
		try {
			result = secMerService.enableAndDisable(array, 4); // 禁用操作,目标状态为"4"
		} catch (Exception e) {
			log.error("禁用操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String subMerId) {
		String[] array = subMerId.split(",");
		String result = "0";
		try {
			result = secMerService.enableAndDisable(array, 2); // 禁用操作,目标状态为"4"
		} catch (Exception e) {
			log.error("启用操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/confSecMerInf")
	public String confSecMerInf(String subMerId, ModelMap modeMap) {
		SecMerInf mer = HfCacheUtil.getCache().getSecMerMap().get(subMerId);
		modeMap.addAttribute("subMerId",subMerId);
		modeMap.addAttribute("subMerName",mer.getSubMerName());
		
		List<SecMerCnf> smcList=null;
		try {
			smcList = secMerService.loadCnf(subMerId);
			modeMap.addAttribute("op2","0");//按次，标识本次配置为添加
			modeMap.addAttribute("op3","0");//包月，标识本次配置为添加
			if(smcList!=null){
				for (SecMerCnf smc : smcList) {
					smc.trim();
					if(smc!=null){
						if(smc.getInFunCode().equals("2")){//按次
							modeMap.addAttribute("op2","1");//标识本次配置为修改
							modeMap.addAttribute("smc2", smc);
						}else if(smc.getInFunCode().equals("3")){//包月
							modeMap.addAttribute("op3","1");//标识本次配置为修改
							modeMap.addAttribute("smc3", smc);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("打开二级商户配置页面失败", e);
		}
		return "secmer/conf";
	}
	
	@RequestMapping(value = "/addConf")
	public ModelAndView addConf(HttpServletRequest request,SecMerCnf smc) {
		smc.trim();
		String result = "0";
		if(smc.getSubMerId()!=null && smc.getInFunCode()!=null){
			smc.setModUser(getUser().getId());
			MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
			MultipartFile file = req.getFile("file");
			try {
				result = secMerService.addConf(file, smc); // 修改操作,string表示操作结果,返回值为1或者0
			} catch (Exception e) {
				log.error("添加二级商户配置失败", e);
			}
		}else{
			result="3";
			log.info("二级商户号或接口标识为null");
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/updateConf")
	public ModelAndView updateConf(HttpServletRequest request,SecMerCnf smc) {
		smc.trim();
		String result = "0";
		if(smc.getSubMerId()!=null && smc.getInFunCode()!=null){
			smc.setModUser(getUser().getId());
			MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
			MultipartFile file = req.getFile("file");
			try {
				result = secMerService.updateConf(file, smc);
			} catch (Exception e) {
				log.error("修改二级商户配置失败", e);
			}
		}else{
			result="3";
			log.info("二级商户号或接口标识为null");
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		List<Map<String, Object>> chnlList = new ArrayList<Map<String, Object>>();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			List<Map<String, Object>> data = service.query(queryKey, map);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
				//渲染数据
				Map<String, Object> m = data.get(i);
				String merId = (String) m.get("SUBMERID");
				m.put("OPERATOR", merOperService.getOperStrByMerId(merId));
				chnlList.add(m);
			}
		}
		return new ModelAndView("excelViewSecMer", "excel", chnlList);
	}

}
