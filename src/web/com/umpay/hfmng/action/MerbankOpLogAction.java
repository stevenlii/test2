/**
 * @Title: MerbankOpLogAction.java
 * @Package com.umpay.hfmng.action
 * @Description: 商户银行操作日志
 * @author MARCO
 * @date 2014-4-25 上午9:05:36
 * @version V1.0
 */

package com.umpay.hfmng.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.MerbankOpLog;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;
import com.umpay.uniquery.util.StringUtil;
@Controller
@RequestMapping("/merbankoplog")
public class MerbankOpLogAction extends BaseAction{
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private AuditService auditService;
	/**
	 * ********************************************
	 * method name   : index 
	 * description   : 商户银行操作日志首页
	 * @return       : String
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : panyouliang ,  2014-4-25 上午9:05:36
	 * *******************************************
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		return "merbankaudit/oplogindex";
		
	} 
	/**
	 * ********************************************
	 * method name   : query 
	 * description   : 统一查询
	 * @return       : ModelAndView
	 * @param        : @return
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query(){
		Map users = optionService.getAllUserIdAndName();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String start = (String)map.get("submitstart");
		String end = (String)map.get("submitend");
		if(!StringUtil.isEmpty(start)){
			start += " 00:00:00";
			map.put("submitstart", start);
		}
		if(!StringUtil.isEmpty(end)){
			end += " 23:59:59";
			map.put("submitend", end);
		}
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
	 * @Title: 导出商户商户银行操作日志
	 * @Description: 导出商户商户银行操作日志
	 * @author panyouliang
	 * @date 2014-4-25 上午9:05:36
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map users = optionService.getAllUserIdAndName();
		Map map = this.getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String start = (String)map.get("submitstart");
		String end = (String)map.get("submitend");
		if(!StringUtil.isEmpty(start)){
			start += " 00:00:00";
			map.put("submitstart", start);
		}
		if(!StringUtil.isEmpty(end)){
			end += " 23:59:59";
			map.put("submitend", end);
		}
		
		
		List<MerbankOpLog> list = new ArrayList<MerbankOpLog>();
		if (queryKey != null) {
			Map<String, String> merNameMap = HfCacheUtil.getCache().getMerNameMap();
			Map<String, String> bankNameMap = HfCacheUtil.getCache().getBankNameMap();
			List<Map<String, Object>> data = this.query(queryKey, map, false);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
				MerbankOpLog stats = new MerbankOpLog();
				Map audit = data.get(i);
				String ixData = (String) audit.get("ixData");
				Integer type = (Integer)audit.get("auditType");
				switch(type){
					case 1:
						stats.setAuditType("新增");
						break;
					case 3:
						stats.setAuditType("启用");
						break;
					case 4:
						stats.setAuditType("禁用");
						break;
					default:
						stats.setAuditType("未知");
						break;
				}
				String[] merBank = ixData.split("-");
				String merId = merBank[0];
				String bankId = merBank[1];
				stats.setBank(bankNameMap.get(bankId) + "-" + merBank[1]);
				String creatorId = ((String) audit.get("creator")).trim();
				if(users.get(creatorId) == null){
					if(messageService.getSystemParam(creatorId) == null){
						stats.setCreator("");
					}else{
						stats.setCreator(messageService.getSystemParam(creatorId));
					}
				}else{
					if((String)users.get(creatorId) == null){
						stats.setCreator("");
					}else{
						stats.setCreator((String)users.get(creatorId));
					}
				}
				
				stats.setIntime(((Timestamp) audit.get("inTime")).toString().substring(0, 19));
				stats.setMerId(new Integer(merId));
				stats.setMerName(merNameMap.get(merId));
				list.add(stats);
			}
		}
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("type", MerbankOpLog.class);
		map2.put("data", list);
		return new ModelAndView("excelStatsView", "excelStats", map2);
	}
}
