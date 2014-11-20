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
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.GoodsBank;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.GoodsBankService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/goodsbankaudit")
public class GoodsBankAuditAction extends BaseAction{
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private GoodsBankService goodsBankService;
	
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		String opts = HfCacheUtil.getCache().getUrlAcl("goodsbank");
		modeMap.addAttribute("opts", opts);
		return "goodsbankaudit/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> users = optionService.getAllUserIdAndName();
		Map<String, String> map = getParametersFromRequest(super
				.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				HfCache cache = HfCacheUtil.getCache();
				Map bankMap = cache.getBankInfoMap();
				for (int i = 0; i < data.size(); i++) {
					// 渲染数据
					Map<String, Object> audit = data.get(i);

					String ixData = (String) audit.get("ixData");
					String[] arr = ixData.split("-"); // ixData是以 merId-goodsId
														// 的格式存放
					audit.put("MERID", arr[0]);
					audit.put("GOODSID", arr[1]);

					// 解析json串
					GoodsBank modData = (GoodsBank) JsonHFUtil
							.getObjFromJsonArrStr(
									(String) audit.get("modData"),
									GoodsBank.class);
					// 渲染提交人
					String creatorId = ((String) audit.get("creator")).trim();
					String bankId = modData.getBankId();
					audit.put("BANKID", bankId);
					String bankName = "";
					// 渲染银行名称
					if (bankId != null && !bankId.equals("")) {
						BankInfo bankInfo = (BankInfo) bankMap.get(bankId.trim());
						if (bankInfo != null) {
							bankName = bankInfo.getBankName();
						}
						audit.put("BANKNAME", bankName);
					}
					audit.put("CREATOR", users.get(creatorId));
					if (audit.get("CREATOR") == null) {
						audit.put("CREATOR", messageService
								.getSystemParam(creatorId));
					}
					// 根据审核状态渲染审核人和审核时间
					if ("0".equals(audit.get("state").toString())) {
						audit.put("MODTIME", "");
						audit.put("MODUSER", "");
					} else {
						String modUserId = ((String) audit.get("modUser"))
								.trim();
						audit.put("MODUSER", users.get(modUserId));
						if (audit.get("MODUSER") == null) {
							audit.put("MODUSER", messageService
									.getSystemParam(modUserId));
						}
					}

					data.set(i, audit);
				}
				long count = queryCount(queryKey, map);
				ObjectUtil.trimData(data);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				log.info("查询失败", e);
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					log.info("json序列化失败", e);
				}
			}
		} else {
			log.info("无法获得查询key");
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				log.info("json序列化失败", e);
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	@RequestMapping(value = "/batch")
	public String batchIndex(ModelMap modeMap) {
		String opts = HfCacheUtil.getCache().getUrlAcl("goodsbank");
		modeMap.addAttribute("opts", opts);
		return "goodsbankaudit/batch";
	}
	
	@RequestMapping(value = "/batchQuery")
	public ModelAndView batchQuery() {
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
					//渲染提交人
					String creatorId = ((String) audit.get("creator")).trim();
					audit.put("CREATOR", users.get(creatorId));
					if(audit.get("CREATOR") == null){
						audit.put("CREATOR", messageService.getSystemParam(creatorId));
					}
					//根据审核状态渲染审核人和审核时间
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
				log.info("查询失败", e);
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					log.info("json序列化失败", e);
				}
			}
		} else {
			log.info("无法获得查询key");
			try {
				msg = JsonUtil.jsonError("-1", "无法获得查询key");
			} catch (Exception e) {
				log.info("json序列化失败", e);
			}
		}
		log.debug("json：" + msg);
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	
	//审核通过
	@RequestMapping(value = "/goodsbankauditpass")
	public ModelAndView goodsBankAuditPass(String id,ModelMap modeMap){
		String string = "no";
		try {
			String[] array = id.split(",");
			User user=this.getUser();   //获取当前登录用户
			string = auditService.goodsBankauditPass(array, user);  //返回值为yes或者no
		} catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	//审核不通过
	@RequestMapping(value = "/goodsbankauditnotpass")
	public ModelAndView goodsBankAuditNotPass(String id,String resultDesc, ModelMap modeMap){
		String string = "no";
		try {
			String[] array = id.split(",");
			User user=this.getUser();   //获取当前登录用户
			string = auditService.goodsBankauditNotPass(array, user, resultDesc);
		} catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	/**
	 * ********************************************
	 * method name   : detail 
	 * description   : 显示详情方法
	 * @return       : String
	 * @param        : @param id
	 * @param        : @param modeMap
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : anshuqiang ,  2012-10-19  下午04:04:08
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap) {
		String opts = HfCacheUtil.getCache().getUrlAcl("goodsbank");
		modeMap.addAttribute("opts", opts);
	//	HfCache cache = (HfCache) SpringContextUtil.getBean("HfCache"); // 获取商户缓存信息
	//	Map<String, Object> merMap = cache.getMerInfoMap();
		//获取商品分类map
	//	Map<String, String> GoodsCateGorymap = optionService.getGoodsCategoryMap();
		Map mapId = new HashMap();
		mapId.put("id", id);
		Audit au = auditService.load(mapId);
		//反序列化审核信息
	//	GoodsInfo audit = (GoodsInfo) JsonHFUtil.getObjFromJsonArrStr(au.getModData(),GoodsInfo.class);
	    GoodsBank audit = (GoodsBank)JsonHFUtil.getObjFromJsonArrStr(au.getModData(),GoodsBank.class);
	//	audit.trim(); //去首尾空格
	//	audit.setId(id);
	    HfCache cache=HfCacheUtil.getCache();
	    Map bankMap=cache.getBankInfoMap();
	    String bankName="";
	    if(audit.getBankId() != null){
	    	 //bankName=((BankInfo)bankMap.get(audit.getBankId().trim())).getBankName(); //获取银行名
	    	 BankInfo bank=(BankInfo)bankMap.get(audit.getBankId().trim());
		    	if(bank!=null)
		    		bankName=bank.getBankName();
	    }
	    audit.setBankName(bankName);
	    modeMap.addAttribute("id", id);
		modeMap.addAttribute("audit", audit);
		modeMap.addAttribute("resultDesc", au.getResultDesc());
		String state = au.getState();
		 //是否为待审核状态
		if ("0".equals(state)) {
			//审核类型为修改,需获取历史数据展示到页面
			if ("2".equals(au.getAuditType())) {  
				Map map = new HashMap();
				String[] arr = au.getIxData().split("-");
				map.put("merId", arr[0]);
				map.put("goodsId", arr[1]);
				map.put("bankId", arr[2]);
				GoodsBank oldGoods = goodsBankService.load(map);
			    String bankNameOld="";
			    if(oldGoods.getBankId() != null){
			    	//bankNameOld=((BankInfo)bankMap.get(oldGoods.getBankId().trim())).getBankName(); //获取银行名
			    	BankInfo bank=(BankInfo)bankMap.get(oldGoods.getBankId().trim());
			    	if(bank!=null)
			    		bankNameOld=bank.getBankName();
			    }
			    oldGoods.setBankName(bankNameOld);
				modeMap.addAttribute("oldGoods", oldGoods);
				return "goodsbankaudit/detailMod";  //修改详情页面
			} else {
				return "goodsbankaudit/detailAdd";  //新增，启用/禁用详情页面
			}
		} else {
			return "goodsbankaudit/showDetail";  //审核后详情页面
		}
	}
	/**
	 * ********************************************
	 * method name   : toAuditBatchDetail 
	 * description   : 批量审核页面，跳转到审核详情页面
	 * @return       : String
	 * @param        : @param batchId
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-11-13  下午03:27:33
	 * *******************************************
	 */
	@RequestMapping(value = "/toAuditBatchDetail")
	public String toAuditBatchDetail(String batchId,ModelMap modeMap) {
		String opts = HfCacheUtil.getCache().getUrlAcl("goodsbank");
		modeMap.addAttribute("opts", opts);
		
		modeMap.addAttribute("batchId", batchId);
		List list = auditService.findByBatchId(batchId);
		if(list != null){
			Audit au = (Audit)list.get(0);
			modeMap.addAttribute("state", au.getState());
			List<GoodsBank> modData = (List<GoodsBank>)JsonHFUtil.getListFromJsonArrStr(au.getModData(),GoodsBank.class);
			GoodsBank bank = modData.get(0);
			modeMap.addAttribute("newVerifyTag", bank.getVerifyTag());
			modeMap.addAttribute("newCheckDay", bank.getCheckDay());
			modeMap.addAttribute("newkState", bank.getkState());
			modeMap.addAttribute("newIsRealTime", bank.getIsRealTime());
			modeMap.addAttribute("newBankMerId", bank.getBankMerId());
			modeMap.addAttribute("newBankPosId", bank.getBankPosId());
		}
		return "goodsbankaudit/detailBatchMod";
	}
	/**
	 * ********************************************
	 * method name   : auditBatchDetail 
	 * description   : 批量审核页面的详情页面的统一查询方法
	 * @return       : ModelAndView
	 * @param        : @param batchId
	 * @param        : @param modeMap
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xuhuafeng ,  2013-11-13  下午03:32:14
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/auditBatchDetail")
	public ModelAndView auditBatchDetail(String batchId, ModelMap modeMap) {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map<String, Object> audit = data.get(i);
					List<GoodsBank> list = (List<GoodsBank>)JsonHFUtil.getListFromJsonArrStr((String) audit.get("MODDATA"),GoodsBank.class);
					GoodsBank newData = list.get(0);
					audit.put("MERID", newData.getMerId());
					audit.put("GOODSID", newData.getGoodsId());
					audit.put("BANKNAME", newData.getBankName());
					audit.put("AMOUNT", newData.getAmount());
					
					if("2".equals(audit.get("AUDITTYPE").toString())){
						GoodsBank old = list.get(1);
						audit.put("VERIFYTAG", old.getVerifyTag());
						audit.put("CHECKDAY", old.getCheckDay());
						audit.put("KSTATE", old.getkState());
						audit.put("ISREALTIME", old.getIsRealTime());
						audit.put("BANKMERID", old.getBankMerId());
						audit.put("BANKPOSID", old.getBankPosId());
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
		return new ModelAndView("jsonView","ajax_json",msg);
	}
	
	/**
	 * ********************************************
	 * method name   : toShowBatchDetail 
	 * description   : 批量审核的打开详情页面
	 * @return       : String
	 * @param        : @param batchId
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-11-12  下午03:46:01
	 * *******************************************
	 */
	@RequestMapping(value = "/toShowBatchDetail")
	public String toShowBatchDetail(String batchId,ModelMap modeMap) {
		modeMap.addAttribute("batchId", batchId);
		return "goodsbankaudit/showBatchDetail";
	}
	
	/**
	 * ********************************************
	 * method name   : showBatchDetail 
	 * description   : 批量审核的详情页面查询方法
	 * @return       : ModelAndView
	 * @param        : @param batchId
	 * @param        : @param modeMap
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xuhuafeng ,  2013-11-12  下午03:46:58
	 * *******************************************
	 */
	@RequestMapping(value = "/showBatchDetail")
	public ModelAndView showBatchDetail(String batchId, ModelMap modeMap) throws Exception {
//		List list = auditService.findByBatchId(batchId);
//		modeMap.addAttribute("batchId", batchId);
//		List<Map<String, Object>> batchDetail = new ArrayList<Map<String, Object>>();
		String msg=null;
//		for(int i=0;i<list.size();i++){
//			Audit au = (Audit)list.get(i);
//			au.trim();
//			GoodsBank modData = (GoodsBank)JsonHFUtil.getObjFromJsonArrStr(au.getModData(),GoodsBank.class);
//			String[] arr = au.getIxData().split("-");
//			Map<String,Object> audit=new HashMap<String, Object>();
//			audit.put("AUDITTYPE", au.getAuditType());
//			audit.put("MERID", arr[0]);
//			audit.put("GOODSID", arr[1]);
//			audit.put("AMOUNT", modData.getAmount());
//			audit.put("BANKNAME", modData.getBankName());
//			audit.put("KSTATE",modData.getkState());
//			batchDetail.add(audit);
//		}
//		Map<String,Object> map=new HashMap<String, Object>();
//		map.put("total", batchDetail.size());
//		map.put("rows",batchDetail);
//		msg = JsonUtil.toJson(batchDetail.size(), batchDetail);
//		modeMap.addAttribute("batchDetail", msg);
		return new ModelAndView("jsonView","ajax_json",msg);
	}
	
	//批量审核通过
	@RequestMapping(value = "/goPass")
	public ModelAndView goPass(String batchId,ModelMap modeMap){
		String string = "no";
		User user=this.getUser();   //获取当前登录用户
		try {
			string = auditService.goodsBankauditGoPass(batchId, user);
		} catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	//批量审核不通过
	@RequestMapping(value = "/notPass")
	public ModelAndView notPass(String batchId,String resultDesc, ModelMap modeMap){
		String string = "no";
		User user=this.getUser();   //获取当前登录用户
		try {
			string = auditService.goodsBankAuditNotPass(batchId, user, resultDesc);
		} catch (Exception e) {
			log.error("审核不通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
}
