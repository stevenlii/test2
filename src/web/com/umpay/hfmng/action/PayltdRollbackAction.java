/** *****************  JAVA头文件说明  ****************
 * file name  :  PayltdRollbackAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-4-10
 * *************************************************/ 

package com.umpay.hfmng.action;

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

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.PayltdRollback;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.hfmng.service.PayltdRollbackService;
import com.umpay.hfmng.service.RestService;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  PayltdRollbackAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  限额回滚
 * ************************************************/
@Controller
@RequestMapping("/payltdrollback")
public class PayltdRollbackAction extends BaseAction {
	
	@Autowired
	private RestService restService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private PayltdRollbackService service;
	@Autowired
	private AuditService auditService;
	
	@RequestMapping(value = "/index")
	public String index() {
		return "payltdrollback/index";
	}
	
	@RequestMapping(value = "/query")
	public ModelAndView query(String mobileId) {
		mobileId = StringUtils.trim(mobileId);
		Map<String, String> res = new HashMap<String, String>();
		res.put("result", "no");
		if(mobileId == null || "".equals(mobileId)){
			log.info("手机号为空,无法查询月累计额");
		}else{
			try{
				res = restService.queryPayed(mobileId);
				if("yes".equals(res.get("result"))){
					log.info("查询月累计额成功");
				}else{
					log.info("查询月累计额失败");
				}
			}catch (Exception e) {
				log.error("查询月累计额失败", e);
			}
		}
		return new ModelAndView("jsonView","ajax_json",JsonHFUtil.getJsonArrStrFrom(res));
	}
	
	@RequestMapping(value = "/rollback")
	public ModelAndView rollback(PayltdRollback payltdRollback, double amt) {
		String res = "0";
		try{
			res = service.save(payltdRollback);
		}catch (Exception e) {
			log.error("提交回滚入审核表失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",res);
	}
	
	@RequestMapping(value = "/auditindex")
	public String auditIndex(ModelMap modeMap) {
		String opts = HfCacheUtil.getCache().getUrlAcl("payltdrollback");
		modeMap.addAttribute("opts", opts);
		return "payltdrollback/auditIndex";
	}
	
	@RequestMapping(value = "/auditQuery")
	public ModelAndView auditQuery(){
		Map<String, String> map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> users = optionService.getAllUserIdAndName();
				Map<String, String> merNameMap = HfCacheUtil.getCache().getMerNameMap();
				Map<String, GoodsInfo> goodsInfoMap = HfCacheUtil.getCache().getGoodsInfoMap2();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map<String, Object> audit = data.get(i);
					//解析json串
					String ixData2 = (String) audit.get("IXDATA2");
					String[] merGoodsId = ixData2.split("-");
					audit.put("MERID", merGoodsId[0]);
					audit.put("GOODSID", merGoodsId[1]);
					audit.put("MERNAME", merNameMap.get(merGoodsId[0]));
					GoodsInfo goods = goodsInfoMap.get(ixData2);
					if(goods != null){
						audit.put("GOODSNAME", goods.getGoodsName());
					}
					String creatorId = (String) audit.get("CREATOR");
					audit.put("CREATOR", users.get(creatorId));
					if("0".equals(audit.get("STATE").toString())){
						audit.put("MODTIME", "");
						audit.put("MODUSER", "");
					}else{
						String modUserId = (String) audit.get("MODUSER");
						audit.put("MODUSER", users.get(modUserId));
					}
				}
				long count = queryCount(queryKey, map);
				ObjectUtil.trimData(data);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
					log.error("查询失败", e);
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
	public ModelAndView auditPass(String id,String resultDesc, ModelMap modeMap){
		String res = "0";
		try{
			res = service.auditPass(id, resultDesc);
		}catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",res);
	}
	
	@RequestMapping(value = "/auditNotPass")
	public ModelAndView auditNotPass(String id, String resultDesc, ModelMap modeMap) throws DataAccessException{
		String res = "0";
		try{
			res = service.auditNotPass(id, resultDesc);
		}catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",res);
	}
	
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap){
		HfCache hfCache=HfCacheUtil.getCache();
		String opts = hfCache.getUrlAcl("payltdrollback");
		modeMap.addAttribute("opts", opts);
		Map<String,String> mapId = new HashMap<String,String>();
		mapId.put("id", id);
		Audit audit = auditService.load(mapId);
		audit.trim();
		modeMap.addAttribute("audit", audit);
		PayltdRollback payltd = (PayltdRollback) JsonHFUtil.getObjFromJsonArrStr(audit.getModData(),PayltdRollback.class);
		payltd.trim();
		modeMap.addAttribute("payltd", payltd);
		Map<String, String> merNameMap = hfCache.getMerNameMap();
		Map<String, GoodsInfo> goodsInfoMap = hfCache.getGoodsInfoMap2();
		modeMap.addAttribute("merName", merNameMap.get(payltd.getMerId()));
		GoodsInfo goods = goodsInfoMap.get(audit.getIxData2());
		String goodsName = "";
		if(goods != null){
			goodsName = goods.getGoodsName();
		}
		modeMap.addAttribute("goodsName", goodsName);
		String bankName = hfCache.getBankName(payltd.getBankId());
		modeMap.addAttribute("bankName", bankName);
		return "payltdrollback/detail";  //审核后详情页面
	}
	
}
