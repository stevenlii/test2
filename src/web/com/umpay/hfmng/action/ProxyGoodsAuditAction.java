/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerAuditAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-25
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.ProxyGoodsLimit;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.hfmng.service.ProxyGoodsService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  SecMerAuditAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  二级商户审核页面
 * ************************************************/
@Controller
@RequestMapping("/proxygoodsaudit")
public class ProxyGoodsAuditAction extends BaseAction {
	
	@Autowired
	private OptionService optionService;
	@Autowired
	private ProxyGoodsService proxyService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private AuditService auditService;
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("proxygoods");
		modeMap.addAttribute("opts", opts);
		return "proxygoodsaudit/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query(){
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> users = optionService.getAllUserIdAndName();
				Map<String, Object> goodsMap=HfCacheUtil.getCache().getGoodsInfoMap(); //获取商品缓存信息
				Map<String, String> GoodsCateGorymap = HfCacheUtil.getCache().getGoodsCategoryMap(); //商品类型缓存
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map<String, Object> audit = data.get(i);
					String creatorId = (String) audit.get("creator");
					audit.put("CREATOR", users.get(creatorId));
					if("0".equals(audit.get("state").toString())){
						audit.put("MODTIME", "");
						audit.put("MODUSER", "");
					}else{
						String modUserId = (String) audit.get("modUser");
						audit.put("MODUSER", users.get(modUserId));
					}
					
					String [] keys=audit.get("IXDATA").toString().split("-"); //IXDATA 格式 merid-submerid-goodsid
					String key=null;
					try{
						key=keys[0]+"-"+keys[2];
						audit.put("MERID", keys[0]);
						audit.put("SUBMERID", keys[1]);
						audit.put("GOODSID", keys[2]);
					}
					catch(Exception e){
						log.info("审核表入的主键数据有误，IXDATA:"+audit.get("IXDATA")+"错误");
						break;
					}
					GoodsInfo goods=(GoodsInfo)goodsMap.get(key);
					if(goods!=null){
						String GoodsCategory = GoodsCateGorymap.get(StringUtils.trim(goods.getCategory()));
						audit.put("CATEGORY", GoodsCategory);//商品分类
						audit.put("PRICEMODE", goods.getPriceMode());  //价格模式
						audit.put("SERVTYPE", goods.getServType());  //服务类型
						audit.put("GOODSNAME", goods.getGoodsName());  //商品名称
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
	@RequestMapping(value = "/batch")
	public String batchList(ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("proxygoods");
		modeMap.addAttribute("opts", opts);
		return "proxygoodsaudit/batch";
	}
	/**
	 * ********************************************
	 * method name   : batchList 
	 * description   : 批量查询
	 * @return       : ModelAndView
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : zhaojunbao ,  2013-10-14  下午09:31:45
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/batchQuery")
	public ModelAndView batchQuery()  {
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
					String creatorId = (String) audit.get("creator");
					audit.put("CREATOR", users.get(creatorId));
					if(audit.get("CREATOR") == null){
						audit.put("CREATOR", messageService.getSystemParam(creatorId));
					}
					//根据审核状态渲染审核人和审核时间
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
	
	@RequestMapping(value = "/auditPass")
	public ModelAndView auditPass(String id,String resultDesc, ModelMap modeMap){
		String[] ids = id.split(",");
		String res = "1";
		try{
			res = proxyService.auditPass(ids, resultDesc);
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
			res = proxyService.auditNotPass(ids, resultDesc);
		}catch (Exception e) {
			res = "0";
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",res);
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap){
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("proxygoods");
		modeMap.addAttribute("opts", opts);
		
		Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();  // 获取商品缓存信息
		Map<String, String> secondMerInfoMap=HfCacheUtil.getCache().getSecMerNameMap(); //二级商户缓存 取名字使用
		Map mapId = new HashMap();
		mapId.put("id", id);
		Audit au = auditService.load(mapId);
		au.trim();
		modeMap.addAttribute("auditType", au.getAuditType());
		//反序列化审核信息
		ProxyGoodsLimit  proxyGoodsLimitAudit = (ProxyGoodsLimit) JsonHFUtil
						.getObjFromJsonArrStr(au.getModData(),ProxyGoodsLimit.class);
		proxyGoodsLimitAudit.trim(); //去首尾空格
		modeMap.addAttribute("id",id);
		modeMap.addAttribute("audit",proxyGoodsLimitAudit);
		modeMap.addAttribute("goodsName", ((GoodsInfo)goodsMap.get(proxyGoodsLimitAudit.getMerId()+"-"+proxyGoodsLimitAudit.getGoodsId())).getGoodsName());
		modeMap.addAttribute("subMerName", secondMerInfoMap.get(proxyGoodsLimitAudit.getSubMerId()));
		modeMap.addAttribute("resultDesc", au.getResultDesc());
		String state = au.getState();
		if("3".equals(au.getAuditType())||"4".equals(au.getAuditType())){
			ProxyGoodsLimit pg = proxyService.loadGoodsLimit(proxyGoodsLimitAudit.getMerId(), proxyGoodsLimitAudit.getSubMerId(), proxyGoodsLimitAudit.getGoodsId());
			modeMap.addAttribute("audit",pg);
		}
		if ("0".equals(state)) { //是否为待审核状态
			if ("2".equals(au.getAuditType())) {  //审核类型为修改,需获取历史数据展示到页面
				ProxyGoodsLimit oldProxyGoodsLimit=proxyService.loadGoodsLimit(proxyGoodsLimitAudit.getMerId(), proxyGoodsLimitAudit.getSubMerId(), proxyGoodsLimitAudit.getGoodsId());
				modeMap.addAttribute("audit",proxyGoodsLimitAudit);
				modeMap.addAttribute("dayLimitOld", oldProxyGoodsLimit.getDayLimit());
				modeMap.addAttribute("alarmLimitOld", oldProxyGoodsLimit.getAlarmLimit());
				modeMap.addAttribute("alarmTelOld", oldProxyGoodsLimit.getAlarmTel());
				return "proxygoodsaudit/detailMod";  //修改详情页面
			} else {
				return "proxygoodsaudit/detailAdd";  //新增，启用/禁用详情页面
			}
		} else {
			return "proxygoodsaudit/showDetail";  //审核后详情页面
		}		
	}
	@RequestMapping(value = "/toShowBatchDetail")
	public String toShowBatchDetail(String batchId,ModelMap modeMap) throws DataAccessException {
		//查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("proxygoods");
		modeMap.addAttribute("opts", opts);
		modeMap.addAttribute("batchId", batchId);
		return "proxygoodsaudit/showBatchDetail";
	}
	//批量
	@RequestMapping(value = "/showBatchDetail")
	public ModelAndView showBatchDetail(String batchId, ModelMap modeMap) throws Exception {
		List list = auditService.findByBatchId(batchId);
		modeMap.addAttribute("batchId", batchId);
		List<Map<String, Object>> batchDetail = new ArrayList<Map<String, Object>>();
		String msg=null;
		for(int i=0;i<list.size();i++){
			Audit au = (Audit)list.get(i);
			ProxyGoodsLimit modData = (ProxyGoodsLimit)JsonHFUtil.getObjFromJsonArrStr(au.getModData(),ProxyGoodsLimit.class);
			String[] arr = au.getIxData().split("-");
			Map<String,Object> audit=new HashMap<String, Object>();
			audit.put("MERID", arr[0]);
			audit.put("SUBMERID", arr[1]);
			audit.put("GOODSID", arr[2]);
			audit.put("DAYLIMIT", modData.getDayLimit());
			audit.put("ALARMLIMIT", modData.getAlarmLimit());
			audit.put("ALARMTEL", modData.getAlarmTel());
			audit.put("INTIME",au.getInTime());
			batchDetail.add(audit);
		}
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("total", batchDetail.size());
		map.put("rows",batchDetail);
		msg = JsonUtil.toJson(batchDetail.size(), batchDetail);
		modeMap.addAttribute("batchDetail", msg);
		return new ModelAndView("jsonView","ajax_json",msg);
	}
	
	//批量审核通过
	@RequestMapping(value = "/goPass")
	public ModelAndView goPass(String batchId,String resultDesc,ModelMap modeMap){
		String string = "1";
		try {
			string = proxyService.auditPassByBatchId(batchId, resultDesc);
		} catch (Exception e) {
			string = "0";
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	//批量审核不通过
	@RequestMapping(value = "/notPass")
	public ModelAndView notPass(String batchId,String resultDesc, ModelMap modeMap){
		String string = "1";
		try {
			string = proxyService.auditNotPassByBatchId(batchId, resultDesc);
		} catch (Exception e) {
			string = "0";
			log.error("审核不通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	@RequestMapping(value = "/toAuditBatchDetail")
	public String toAuditBatchDetail(String batchId,ModelMap modeMap) throws DataAccessException {
		modeMap.addAttribute("batchId", batchId);
		return "proxygoodsaudit/showBatchAddDetail";
	}
}
