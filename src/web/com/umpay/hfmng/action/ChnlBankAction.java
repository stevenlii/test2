/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlBankAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-18
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.ChnlBank;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.ChnlBankService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  ChnlBankAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Controller
@RequestMapping("/chnlbank")
public class ChnlBankAction extends BaseAction {
	
	@Autowired
	private OptionService optionService;
	@Autowired
	private ChnlBankService chnlBankService;
	@Autowired
	private AuditService auditService;
//	@Autowired
//	private ChnlInfService chnlInfService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("chnlbank");
		modeMap.addAttribute("opts", opts);
		return "chnlbank/index";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		Map bankMap = HfCacheUtil.getCache().getBankInfoMap();
		Map users = optionService.getAllUserIdAndName();
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map chnlBank = data.get(i);
					BankInfo bank = (BankInfo) bankMap.get(chnlBank.get("BANKID"));
					if(bank != null){
						chnlBank.put("BANKNAME", bank.getBankName());
					}
					chnlBank.put("SERVICE_USER", users.get(chnlBank.get("SERVICE_USER")));
					data.set(i, chnlBank);
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
	
	@RequestMapping(value = "/modify")
	public String modify(ModelMap modeMap){
		String zTreeNodes = ZTreeUtil.getBankTree();
		modeMap.addAttribute("zNodes", zTreeNodes);
		return "chnlbank/modify";
	}
	
	@RequestMapping(value = "/getchnlnameandtree")
	public ModelAndView getChnlNameAndTree(String channelId){
		channelId = StringUtils.trim(channelId);
		Map<String, String> result = new HashMap<String, String>();
		Map<String, ChnlInf> chnlInfMap = HfCacheUtil.getCache().getChnlInfMap();
		String chnlName = "";
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("ixData", channelId + "-");
		mapWhere.put("tableName", "UMPAY.T_HF_CHNL_BANK");
		Map audit = auditService.getMerBankAudit(mapWhere);
		if(chnlInfMap != null){
			ChnlInf chnlInf = chnlInfMap.get(channelId);
			if(chnlInf != null){
				chnlName = chnlInf.getChannelName();
				log.info("根据渠道号["+channelId+"]获取到渠道名称:"+chnlName);
				result.put("chnlName", chnlName);
				Map<String, ChnlBank> chnlBanks = chnlBankService.findBankByChnlId(channelId);
				String zTreeNodes="";  //构造树节点
				String MWNode="{ id:'1', pId:'0', name:'全网支付银行'}"; //全网银行的父节点
				String XENode="{ id:'2', pId:'0', name:'小额支付银行', open:true}";//小额支付银行父节点
				StringBuffer nodes = new StringBuffer(); //节点内容
				List<BankInfo> list=HfCacheUtil.getCache().getBankList();
				for(BankInfo bankInfo:list){
					bankInfo.trim();
					String bankType=bankInfo.getBankType();
					StringBuffer node = new StringBuffer();
					node.append("{ id:'").append(bankInfo.getBankId()).append("', name:'").append(bankInfo.getBankName());
					if(!"2".equals(bankInfo.getState())){
						node.append("(已禁用)");
					}
					node.append("'");
					if(bankType=="3" ||bankType.equals("3")){
						node.append(", pId:'1'");
					}else if(bankType=="6"||bankType.equals("6")){
						node.append(", pId:'2'");
					}else{
						continue; //其它银行类型则跳过
					}
					ChnlBank chnlBank = chnlBanks.get(bankInfo.getBankId());
					if(chnlBank == null){
						//检查是否处于待审核
						Object o = audit.get(bankInfo.getBankId()); // 查询是否存在
						if (o != null) {
							node.append(", chkDisabled:true");
						}
					}else{
						if(chnlBank.getModLock()==0){
							if(chnlBank.getState() == 2){
								node.append(", checked:true");
							}
						}else{
							node.append(", chkDisabled:true");
							if(chnlBank.getState() == 2){
								node.append(", checked:true");
							}
						}
					}
					node.append("}");
					nodes.append(",").append(node);
				}
				zTreeNodes = "["+MWNode+nodes+","+XENode+"]";  //加两个父节点  再加节点内容
				result.put("tree", zTreeNodes);
			}else{
				log.error("找不到与渠道号["+channelId+"]匹配的渠道信息");
			}
		}else{
			log.error("获取渠道信息缓存失败");
		}
		return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(result));
	}
	
	@RequestMapping(value = "/update")
	public ModelAndView update(String channelId, String bankIds){
		channelId = StringUtils.trim(channelId);
		List<String> newOpen = new ArrayList<String>(); //新增开通
		List<String> modOpen = new ArrayList<String>(); //修改开通
		List<String> modClose = new ArrayList<String>(); //修改关闭
		//1-获取页面的商户号、小额银行串a
		String[] bankId = bankIds.split(",");
		//2-获取此商户号在库里的小额银行状态b和锁定状态
		Map<String, ChnlBank> chnlBanks = chnlBankService.findBankByChnlId(channelId);
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("ixData", channelId + "-");
		mapWhere.put("tableName", "UMPAY.T_HF_CHNL_BANK");
		Map audit = auditService.getMerBankAudit(mapWhere);
		for(int i=0;i<bankId.length;i++){
			if("1".equals(bankId[i]) || "2".equals(bankId[i])){  //若为根节点则直接下一次循环
				continue;
			}
			ChnlBank bank = chnlBanks.get(bankId[i]);
			if(bank == null){
				//3-1    从a中循环取出一个元素i，与b比较，若b中不含i，则为新增开通；
				Object o = audit.get(bankId[i]); // 查询是否存在
				if(o == null){
					newOpen.add(bankId[i]);
				}else{
					log.error("银行号为"+bankId[i]+"的支付银行已送交审核，无法重复提交！");
					return new ModelAndView("jsonView", "ajax_json", "no"); 
				}
			}else{
				bank.trim();
				// 3-2     若b中含i,则再看锁定状态，若为0，则再看state，若为4，则为修改开通；若为2，则为修改关闭
				if(bank.getModLock() == 1){
					log.error("银行号为"+bankId[i]+"的支付银行已经锁定！");
					return new ModelAndView("jsonView", "ajax_json", "no"); 
				}else{
					if(2 == bank.getState()){
						modClose.add(bankId[i]);
					}else if(4 == bank.getState()){
						modOpen.add(bankId[i]);
					}
				}
			}
		}
		String result = "no";
		ChnlBank chnlBank = new ChnlBank();
		chnlBank.setChannelId(channelId);
		chnlBank.setService_user(getUser().getId());
		chnlBank.setModLock(1);
		//向审核表插入数据
		try{
			result = chnlBankService.saveChnlBank(chnlBank, newOpen, modOpen, modClose);
		}catch (Exception e) {
			log.error("渠道银行配置失败",e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/batchMod")
	public String batchMod(ModelMap modeMap){
		String zTreeNodes = ZTreeUtil.getBankTree();
		modeMap.addAttribute("zNodes", zTreeNodes);
		return "chnlbank/batchMod";
	}
	
	@RequestMapping(value = "/batchupdate")
	public ModelAndView batchUpdate(String channelId, String bankId ,int state){
		String result = "yes";
		String userId = getUser().getId();
		String[] channelIds = channelId.split(",");
		Set<String> chnlIdSet = new HashSet<String>();
		for(int i=0;i<channelIds.length;i++){
			chnlIdSet.add(StringUtils.trim(channelIds[i]));
		}
		String[] chnlIds = chnlIdSet.toArray(new String[0]);
		try{
			result = chnlBankService.batchUpdate(chnlIds, bankId, state, userId);
		}catch (Exception e) {
			result = "操作失败，请稍后再试";
			log.error(result, e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/getchnlname")
	public ModelAndView getChnlName(String channelId){
		Map<String, String> res = new HashMap<String, String>();
		res.put("result", "no");
		channelId = StringUtils.trim(channelId);
		if(channelId != null && !"".equals(channelId)){
			Map<String, ChnlInf> chnlInfMap = HfCacheUtil.getCache().getChnlInfMap();
			String[] chnlIds = channelId.split(",");
			String chnlName = "";
			for(int i=0;i<chnlIds.length;i++){
				String chnlId = StringUtils.trim(chnlIds[i]);
				if(!"".equals(chnlId)){
					ChnlInf chnlInf = chnlInfMap.get(chnlId);
					if(chnlInf != null){
						if("".equals(chnlName)){
							chnlName = chnlInf.getChannelName();
						}else{
							chnlName += "," + chnlInf.getChannelName();
						}
					}else{
						res.put("error", "渠道编号["+chnlId+"]不存在");
						return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(res));
					}
				}
			}
			res.put("chnlName", chnlName);
			res.put("result", "yes");
		}
		return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(res));
	}
	
	@RequestMapping(value = "/detail")
	public String detail(String channelId, String bankId, ModelMap modeMap){
		ChnlBank chnlBank = chnlBankService.load(channelId, bankId);
		chnlBank.trim();
		Map<String, ChnlInf> chnlInfMap = HfCacheUtil.getCache().getChnlInfMap();
		ChnlInf chnlInf = chnlInfMap.get(channelId);
		if(chnlInf != null){
			chnlInf.trim();
			String chnlName = chnlInf.getChannelName();
			log.info("获取到渠道名称：" + chnlName);
			modeMap.addAttribute("chnlName", chnlName);
		}
		String bankName = HfCacheUtil.getCache().getBankName(bankId);
		log.info("获取到银行名称：" + bankName);
		modeMap.addAttribute("bankName", bankName);
		Map users = optionService.getAllUserIdAndName();
		String userName = (String) users.get(chnlBank.getService_user());
		chnlBank.setService_user(userName);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateString = formatter.format(chnlBank.getModTime());
		modeMap.addAttribute("modTime", dateString);
		log.info("获取到渠道银行数据：" + chnlBank.toString());
		modeMap.addAttribute("chnlBank", chnlBank);
		return "chnlbank/detail";
	}
	
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String ID) {
		String result="no";
		String userId = this.getUser().getId();   //获取当前登录用户
	    //禁用操作，目标状态为  ”4“  
		try{
			result = chnlBankService.enableAnddisable(ID, userId, 4);   //rusult 为操作结果显示
		}catch(Exception e){
			log.error("禁用操作失败" + e);
		}
		 return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String ID) {
		String result="no";
		String userId = this.getUser().getId();   //获取当前登录用户
	    //禁用操作，目标状态为  ”4“  
		try{
			result = chnlBankService.enableAnddisable(ID, userId, 2);   //rusult 为操作结果显示
		}catch(Exception e){
			log.error("启用操作失败" + e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map<String, String> users = optionService.getAllUserIdAndName();
		Map bankMap = HfCacheUtil.getCache().getBankInfoMap();
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		if (queryKey != null) {
			data = query(queryKey, map, false);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
				Map chnlBank = data.get(i);
				BankInfo bank = (BankInfo) bankMap.get(chnlBank.get("BANKID"));
				if(bank != null){
					chnlBank.put("BANKNAME", bank.getBankName());
				}
				chnlBank.put("SERVICE_USER", users.get(chnlBank.get("SERVICE_USER")));
				int state = (Integer) chnlBank.get("STATE");
				String stateName = "";
				if(state == 2){
					stateName ="启用";
				}else if(state == 4){
					stateName ="禁用";
				} 
				chnlBank.put("STATE", stateName);
				data.set(i, chnlBank);
			}
		}
		return new ModelAndView("excelChnlBank", "excel", data);
	}

}
