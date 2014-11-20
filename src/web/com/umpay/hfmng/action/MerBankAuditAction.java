/** *****************  JAVA头文件说明  ****************
 * file name  :  MerBankAuditAction.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-9
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
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  MerBankAuditAction
 * @author     :  xhf
 * @version    :  1.0  
 * description :  商户银行审核页面
 * ************************************************/
@Controller
@RequestMapping("/merbankaudit")
public class MerBankAuditAction extends BaseAction {
	
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private AuditService auditService;
	/**
	 * ********************************************
	 * method name   : index 
	 * description   : 商户银行审核首页
	 * @return       : String
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-11-21  下午05:06:35
	 * *******************************************
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		String opts = HfCacheUtil.getCache().getUrlAcl("merbank");
		modeMap.addAttribute("opts", opts);
		return "merbankaudit/index";
	} 
	/**
	 * ********************************************
	 * method name   : query 
	 * description   : 统一查询
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:08:01
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query(){
		Map users = optionService.getAllUserIdAndName();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> merNameMap = HfCacheUtil.getCache().getMerNameMap();
				Map<String, String> bankNameMap = HfCacheUtil.getCache().getBankNameMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map audit = data.get(i);
					String ixData = (String) audit.get("ixData");
					String[] merBank = ixData.split("-");
					String merId = merBank[0];
					audit.put("MERID", merId);
					String merName = merNameMap.get(merId);
					audit.put("MERNAME", merName);	
					String bankId = merBank[1];
					audit.put("BANKID", bankId);
					String bankName = bankNameMap.get(bankId);
					audit.put("BANKNAME", bankName);
					String creatorId = ((String) audit.get("creator")).trim();
					audit.put("CREATOR", users.get(creatorId));
					if(audit.get("CREATOR") == null){
						audit.put("CREATOR", messageService.getSystemParam(creatorId));
					}
					if("0".equals(audit.get("state").toString())){
						audit.put("MODTIME", "");
						audit.put("MODUSER", "");
					}else{
						String modUserId = ((String) audit.get("modUser")).trim();
						audit.put("MODUSER", users.get(modUserId));
						if(audit.get("MODUSER") == null){
							audit.put("MODUSER", messageService.getSystemParam(modUserId));
						}
					}
					data.set(i, audit);
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
	/**
	 * ********************************************
	 * method name   : merBankAuditPass 
	 * description   : 审核通过
	 * @return       : ModelAndView
	 * @param        : @param id
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:08:32
	 * *******************************************
	 */
	@RequestMapping(value = "/merbankauditpass")
	public ModelAndView merBankAuditPass(String id,ModelMap modeMap){
		String[] array = id.split(",");
		User user=this.getUser();   //获取当前登录用户
		String string = "no";
		try{
			string = auditService.merBankauditPass(array, user);  //返回值为yes或者no
		}catch (Exception e) {
			log.error("审核通过失败",e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	/**
	 * ********************************************
	 * method name   : merBankAuditNotPass 
	 * description   : 审核不通过
	 * @return       : ModelAndView
	 * @param        : @param id
	 * @param        : @param resultDesc
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:08:44
	 * *******************************************
	 */
	@RequestMapping(value = "/merbankauditnotpass")
	public ModelAndView merBankAuditNotPass(String id,String resultDesc, ModelMap modeMap){
		String[] array = id.split(",");
		User user=this.getUser();   //获取当前登录用户
		String string = "no";
		try{
			string = auditService.merBankauditNotPass(array, user, resultDesc);
		}catch (Exception e) {
			log.error("审核不通过失败",e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}

}
