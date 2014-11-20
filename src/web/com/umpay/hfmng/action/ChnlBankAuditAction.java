/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlBankAudit.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-19
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.ChnlBank;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  ChnlBankAudit
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  渠道银行审核
 * ************************************************/
@Controller
@RequestMapping("/chnlbankaudit")
public class ChnlBankAuditAction extends BaseAction {
	
	@Autowired
	private AuditService auditService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("chnlbank");
		modeMap.addAttribute("opts", opts);
		return "chnlbankaudit/index";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query(){
		Map users = optionService.getAllUserIdAndName();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, ChnlInf> chnlInfMap = HfCacheUtil.getCache().getChnlInfMap();
				Map<String, String> bankNameMap = HfCacheUtil.getCache().getBankNameMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map audit = data.get(i);
					//解析json串
					ChnlBank modData = (ChnlBank) JsonHFUtil.getObjFromJsonArrStr(
											(String) audit.get("modData"), ChnlBank.class);
					String channelId = modData.getChannelId();
					audit.put("CHANNELID", channelId);
					audit.put("CHANNELNAME", chnlInfMap.get(channelId).getChannelName());
					String bankId = modData.getBankId();
					String bankName = bankNameMap.get(bankId);
					audit.put("BANKNAME", bankName);
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
	
	@RequestMapping(value = "/auditPass")
	public ModelAndView auditPass(String id,String resultDesc){
		String string = "no";
		try{
			String[] ids = id.split(",");
			String userId = getUser().getId();
			string = auditService.chnlBankAuditPass(ids, userId, resultDesc);  //返回值为yes或者no
		}catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	@RequestMapping(value = "/auditNotPass")
	public ModelAndView auditNotPass(String id, String resultDesc){
		String string = "no";
		try{
			String[] ids = id.split(",");
			String userId = getUser().getId();
			string = auditService.chnlBankAuditNotPass(ids, userId, resultDesc);  //返回值为yes或者no
		}catch (Exception e) {
			log.error("审核不通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("chnlbank");
		modeMap.addAttribute("opts", opts);
		
		Map mapId = new HashMap();
		mapId.put("id", id);
		Audit au = auditService.load(mapId);
		au.trim();
		modeMap.addAttribute("audit", au);
		//反序列化审核信息
		ChnlBank chnlBank = (ChnlBank) JsonHFUtil.getObjFromJsonArrStr(au.getModData(),ChnlBank.class);
		chnlBank.trim();
		Map<String, ChnlInf> chnlInfMap = HfCacheUtil.getCache().getChnlInfMap();
		ChnlInf chnlInf = chnlInfMap.get(chnlBank.getChannelId());
		if(chnlInf != null){
			chnlInf.trim();
			String chnlName = chnlInf.getChannelName();
			log.info("获取到渠道名称：" + chnlName);
			modeMap.addAttribute("chnlName", chnlName);
		}
		String bankName = HfCacheUtil.getCache().getBankName(chnlBank.getBankId());
		log.info("获取到银行名称：" + bankName);
		modeMap.addAttribute("bankName", bankName);
		Map users = optionService.getAllUserIdAndName();
		String userName = (String) users.get(chnlBank.getService_user());
		chnlBank.setService_user(userName);
		log.info("获取到渠道银行数据：" + chnlBank.toString());
		modeMap.addAttribute("chnlBank", chnlBank);
		return "chnlbankaudit/detail";
	}

}
