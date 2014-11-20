/** *****************  JAVA头文件说明  ****************
 * file name  :  LastMonthGradeAction.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-2-21
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.GradeRule;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.MerGrade;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MerGradeService;
import com.umpay.hfmng.service.MerOperService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.util.JsonUtil;


/** ******************  类说明  *********************
 * class       :  LastMonthGradeAction
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  上月评分管理
 * ************************************************/
@Controller
@RequestMapping("/mergrade")
public class MerGradeAction extends BaseAction {
	private static BigDecimal defaultGrade=new BigDecimal(Const.MERGRADE_DEFAULT);
	private static int earliesttime=0;//导入非自动计算的评分的最早时间，也是审核的最早时间
	private static int latesttime=0;//导入非自动计算的评分的最迟时间，也是审核的最迟时间
	
	@Autowired
	private MessageService messageService;
	@Autowired
	private MerGradeService merGradeService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private MerOperService merOperService;

	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("lastmonthgrade");
		modeMap.addAttribute("opts", opts);
		return "mergrade/index";
	}
	
	/**
	 * ********************************************
	 * method name   : query 
	 * description   : 查询上月或历史评分
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-3-7  下午03:48:44
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String lastMonth = getLastMonth();
		map.put("lastMonth", lastMonth);
		if(isOperator()){ //是否只属于运营人员
			map.put("operator", getUser().getId());
		}
		map = setOperState(map);
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map,true);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map merGrade = data.get(i);
					Timestamp inTime = (Timestamp)merGrade.get("INTIME");
					merGrade.put("OPERSTATE", GradeRule.getOperState(inTime));
					merGrade.put("RISERATEINDEX",setDefaultValue(merGrade.get("RISERATEINDEX")));
					merGrade.put("TURNOVERINDEX",setDefaultValue(merGrade.get("TURNOVERINDEX")));
					merGrade.put("FALSETRADEINDEX",setDefaultValue(merGrade.get("FALSETRADEINDEX")));
					merGrade.put("COMPLAINTINDEX",setDefaultValue(merGrade.get("COMPLAINTINDEX")));
					merGrade.put("SYSSTABINDEX",setDefaultValue(merGrade.get("SYSSTABINDEX")));
					merGrade.put("COOPERATEINDEX",setDefaultValue(merGrade.get("COOPERATEINDEX")));
					merGrade.put("BREACHINDEX",setDefaultValue(merGrade.get("BREACHINDEX")));
					merGrade.put("UPGRADEINDEX",setDefaultValue(merGrade.get("UPGRADEINDEX")));
					merGrade.put("MARKETINGINDEX",setDefaultValue(merGrade.get("MARKETINGINDEX")));
					merGrade.put("SUPPORTINDEX",setDefaultValue(merGrade.get("SUPPORTINDEX")));
					merGrade.put("TOTAL",setDefaultValue(merGrade.get("TOTAL")));
					String merId = (String) merGrade.get("MERID");
					merGrade.put("OPERATOR", merOperService.getOperStrByMerId(merId));
					data.set(i, merGrade);
				}
				long count = queryCount(queryKey, map,true);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					log.error("查询失败", e);
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
	 * 获取上个月yyyy-MM
	 */
	private String getLastMonth() {
		// 获取上个月
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		String lastMonth = df.format(c.getTime());
		return lastMonth;
	}
	
	@SuppressWarnings("unchecked")
	private Map setOperState(Map map){
		String operState = (String)map.get("operState");
		if(operState == null){
			return map;
		}
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		c.setTime(new Date());
		c.add(Calendar.MONTH, -3);
		String inTimeMonth = df.format(c.getTime());
		Timestamp lastInTime = Timestamp.valueOf(inTimeMonth
				+ "-01 00:00:00");// 试运营状态的商户号最晚的上线的时间
		if("0".equals(operState)){
			map.put("after", lastInTime);
		}else if("1".equals(operState)){
			map.put("befor", lastInTime);
		}
		return map;
	}
	
	private BigDecimal setDefaultValue(Object o){
		BigDecimal value = (BigDecimal) o;
		if(defaultGrade.compareTo(value) == 0){
			return null;
		}
		return value;
	}
	private BigDecimal setTurnoverValue(BigDecimal value){
		if(value != null){
			if(defaultGrade.compareTo(value) == 0){
				return null;
			}else{
				value = value.divide(new BigDecimal(100),Const.MERGRADE_SCALE,BigDecimal.ROUND_HALF_UP);
			}
		}
		return value;
	}
	
	/**
	 * 是否只属于运营人员角色
	 */
	@SuppressWarnings("unchecked")
	private Boolean isOperator(){
		User user = getUser();
		List<?> roleList = HfCacheUtil.getCache().getRolesByUser(user.getId());
		if(roleList != null && roleList.size() == 1){ //只拥有一个角色
			String operatorRole = messageService.getSystemParam("OperatorRole"); //运营人员角色
			Map m = (Map) roleList.get(0);
			Object roleName=m.get("roleName");
			if(roleName!=null && operatorRole!="" && StringUtils.trim(roleName.toString()).equals(operatorRole)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否在3-25日之间，
	 * @throws Exception 
	 */
	private Boolean isOperateDate() throws Exception{
		String canOperateDate = messageService.getMessage("canOperateDate");
		if(canOperateDate != null){
			String[] OperateDate = canOperateDate.split(",");
			if(OperateDate.length == 2){
				earliesttime = Integer.valueOf(OperateDate[0]);
				latesttime = Integer.valueOf(OperateDate[1]);
				log.info("获取到最早时间earliesttime="+earliesttime+"，最晚时间latesttime="+latesttime);
			}else{
				log.error("配置文件配置错误，错误key=canOperateDate");
				throw new Exception("配置文件配置错误，错误key=canOperateDate");
			}
		}else{
			log.error("读取配置文件出错，查询key=canOperateDate");
			throw new Exception("读取配置文件出错，查询key=canOperateDate");
		}
		Calendar now = Calendar.getInstance();
		int day=now.get(Calendar.DAY_OF_MONTH);
		//只能在最早时间和最迟时间导入、或审核评分
		if(day<earliesttime || day>latesttime){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * ********************************************
	 * method name   : exportMerGrade 
	 * description   : 导出上月或历史评分
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-3-7  下午03:43:28
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportMerGrade")
	public ModelAndView exportMerGrade() {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		map = setOperState(map);
		String lastMonth = getLastMonth();
		map.put("lastMonth", lastMonth);
		if(isOperator()){ //是否只属于运营人员
			map.put("operator", getUser().getId());
		}
		if (queryKey != null) {
			data = query(queryKey, map, true);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
				Map merGrade = data.get(i);
				Timestamp inTime = (Timestamp)merGrade.get("INTIME");
				merGrade.put("OPERSTATE", GradeRule.getOperState(inTime));
				merGrade.put("RISERATEINDEX",setDefaultValue(merGrade.get("RISERATEINDEX")));
				merGrade.put("TURNOVERINDEX",setDefaultValue(merGrade.get("TURNOVERINDEX")));
				merGrade.put("FALSETRADEINDEX",setDefaultValue(merGrade.get("FALSETRADEINDEX")));
				merGrade.put("COMPLAINTINDEX",setDefaultValue(merGrade.get("COMPLAINTINDEX")));
				merGrade.put("SYSSTABINDEX",setDefaultValue(merGrade.get("SYSSTABINDEX")));
				merGrade.put("COOPERATEINDEX",setDefaultValue(merGrade.get("COOPERATEINDEX")));
				merGrade.put("BREACHINDEX",setDefaultValue(merGrade.get("BREACHINDEX")));
				merGrade.put("UPGRADEINDEX",setDefaultValue(merGrade.get("UPGRADEINDEX")));
				merGrade.put("MARKETINGINDEX",setDefaultValue(merGrade.get("MARKETINGINDEX")));
				merGrade.put("SUPPORTINDEX",setDefaultValue(merGrade.get("SUPPORTINDEX")));
				merGrade.put("TOTAL",setDefaultValue(merGrade.get("TOTAL")));
				String merId = (String) merGrade.get("MERID");
				merGrade.put("OPERATOR", merOperService.getOperStrByMerId(merId));
				data.set(i, merGrade);
			}
		}
		return new ModelAndView("excelMerGrade", "excel", data);
	}
	
	/**
	 * ********************************************
	 * method name   : detail 
	 * description   : 展示详情页面
	 * @return       : String
	 * @param        : @param merId
	 * @param        : @param month
	 * @param        : @param merName
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-2-25  上午11:53:51
	 * *******************************************
	 */
	@RequestMapping(value = "/detail")
	public String detail(String merId, String month, ModelMap modeMap) {
		MerGrade merGrade = merGradeService.load(merId, month);
		merGrade.trim();
		merGrade = dealIndex(merGrade);
		BigDecimal turnover = setTurnoverValue(merGrade.getTurnover());
		modeMap.addAttribute("turnover",turnover == null ? null : turnover + "元");
		modeMap.addAttribute("merGrade",merGrade);
		return "mergrade/detail";
	}
	
	/**
	 * ********************************************
	 * method name   : modify 
	 * description   : 展示修改页面
	 * @return       : String
	 * @param        : @param merId
	 * @param        : @param month
	 * @param        : @param merName
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-2-25  上午11:54:37
	 * *******************************************
	 */
	@RequestMapping(value = "/modify")
	public String modify(String merId, String month, ModelMap modeMap) {
		MerGrade merGrade = merGradeService.load(merId, month);
		merGrade.trim();
		merGrade = dealIndex(merGrade);
		BigDecimal turnover = setTurnoverValue(merGrade.getTurnover());
		modeMap.addAttribute("turnover",turnover == null ? null : turnover + "元");
		modeMap.addAttribute("merGrade",merGrade);
		return "mergrade/modify";
	}
	
	/**
	 * ********************************************
	 * method name   : update 
	 * description   : 修改入审核
	 * @return       : ModelAndView
	 * @param        : @param merGrade
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-3-1  上午10:24:05
	 * *******************************************
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(MerGrade merGrade, ModelMap modeMap){
		String res = "no";
		try{
			if(!isOperateDate()){
				res = "仅能在"+earliesttime+"到"+latesttime+"日修改数据";
				log.error(res);
				return new ModelAndView("jsonView","ajax_json",res);
			}
			merGrade.trim();
			merGrade.setModUser(getUser().getId());
			res = merGradeService.update(merGrade);
		}catch (Exception e) {
			log.error("修改商户评分失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", res);
	}
	
	/**
	 * ********************************************
	 * method name   : batchImport 
	 * description   : 导入手工评分
	 * @return       : ModelAndView
	 * @param        : @param request
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-3-1  上午10:24:20
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/batchImport")
	public ModelAndView batchImport(HttpServletRequest request){
		Map result = new HashMap();
		result.put("result", "no");
		MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
		 // 获得文件：     
        MultipartFile file = req.getFile("file");
        try{
        	if(!isOperateDate()){
				String res = "仅能在"+earliesttime+"到"+latesttime+"日导入数据";
				log.error(res);
				result.put("result", res);
			}else{
				List<List<String>> merGradeList = merGradeService.uploadGrades(file);
				if(merGradeList.size() != 0){
					File fl = File.createTempFile("导入手工评分" + TimeUtil.datetime14(), ".xls");
					merGradeService.writeFile(fl, merGradeList);
					String fileName = fl.getAbsolutePath();
					log.info("手工导入上月评分失败列表保存位置为" + fl.getPath());
					result.put("fileName", fileName);
				}else{
					result.put("result", "yes");
					log.info("处理上传文件成功!");
				}
			}
        }catch (Exception e) {
        	log.error("处理上传文件失败", e);
        	result.put("result", "处理上传文件失败,请仔细检查上传文件!");
		}
        return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(result));
	}
	
	/**
	 * ********************************************
	 * method name   : downloadFailList 
	 * description   : 下载导入失败列表
	 * @return       : ModelAndView
	 * @param        : @param fileName
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-3-1  上午10:54:17
	 * *******************************************
	 */
	@RequestMapping(value = "/downloadFailList")
	public ModelAndView downloadFailList(String fileName){
		List<List<String>> merGradeList = merGradeService.readFile(fileName);
		return new ModelAndView("excelMerGradeImportError", "excel", merGradeList);
	}
	
	/**
	 * ********************************************
	 * method name   : openModHistory 
	 * description   : 打开修改记录页面
	 * @return       : String
	 * @param        : @param merId
	 * @param        : @param month
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-3-4  上午10:51:06
	 * *******************************************
	 */
	@RequestMapping(value = "/openModHistory")
	public String openModHistory(String merId, String month, ModelMap modeMap){
		modeMap.addAttribute("dataId", StringUtils.trim(merId) + "-" + StringUtils.trim(month));
		return "mergrade/modHistory";
	}
	/**
	 * ********************************************
	 * method name   : detail 
	 * description   : 查询修改记录
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-3-4  上午10:51:32
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/modHistory")
	public ModelAndView modHistory(){
		Map users = optionService.getAllUserIdAndName();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map operLog = data.get(i);
					String modUserId = ((String) operLog.get("modUser")).trim();
					operLog.put("MODUSER", users.get(modUserId));
					if(operLog.get("MODUSER") == null){
						operLog.put("MODUSER", messageService.getSystemParam(modUserId));
					}
					data.set(i, operLog);
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
	 * method name   : gradeAudit 
	 * description   : 打开审核页
	 * @return       : String
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-2-27  下午02:41:12
	 * *******************************************
	 */
	@RequestMapping(value = "/gradeaudit")
	public String gradeAudit(ModelMap modeMap){
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("lastmonthgrade");
		modeMap.addAttribute("opts", opts);
		return "mergrade/gradeAudit";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryaudit")
	public ModelAndView queryAudit(){
		Map users = optionService.getAllUserIdAndName();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		map = setOperState(map);
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					//渲染数据
					Map audit = data.get(i);
					//解析json串
					List<MerGrade> modData = (List<MerGrade>) JsonHFUtil.getListFromJsonArrStr(
											(String) audit.get("modData"), MerGrade.class);
					Timestamp inTime = modData.get(0).getMerInTime();
					audit.put("OPERSTATE", GradeRule.getOperState(inTime));
					Map<String,String> comValue = compareValue(modData);
					if(comValue != null){
						audit.put("OLDVALUE", comValue.get("oldsb"));
						audit.put("NEWVALUE", comValue.get("newsb"));
						audit.put("TITLE", comValue.get("title"));
					}
					if(!"0".equals(audit.get("state").toString())){
						String modUserId = (String) audit.get("modUser");
						audit.put("MODUSER", users.get(modUserId));
						if(audit.get("MODUSER") == null){
							audit.put("MODUSER", messageService.getSystemParam(modUserId));
						}
					}
					String merId = (String) audit.get("IXDATA");
					audit.put("OPERATOR", merOperService.getOperStrByMerId(merId));
					data.set(i, audit);
				}
				long count = queryCount(queryKey, map);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				log.error("查询失败", e);
				try {
					msg = JsonUtil.jsonError("-1", "查询失败" + e.getMessage());
				} catch (Exception e1) {
					log.error("数据转换异常", e1);
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/auditPass")
	public ModelAndView auditPass(String id,String resultDesc, ModelMap modeMap){
		String string = "no";
		try{
			if(!isOperateDate()){
				string = "仅能在"+earliesttime+"到"+latesttime+"日审核数据";
				log.error(string);
				return new ModelAndView("jsonView","ajax_json",string);
			}
			Map map = new HashMap();
			map.put("id", id);
			Audit audit = auditService.load(map);
			audit.trim();  //去空格
			if(!"0".equals(audit.getState())){
				string = "该数据已审核，不能再次审核";
				log.error(string + audit);
				return new ModelAndView("jsonView","ajax_json",string);
			}
			if(!auditService.checkAuditUser(audit.getCreator())){
				return new ModelAndView("jsonView","ajax_json","您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。");
			}
			audit.setModUser(getUser().getId());
			if(resultDesc != null){
				audit.setResultDesc(resultDesc);
			}
			string = auditService.gradeAuditPass(audit);  //返回值为yes或者no 
		}catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/auditNotPass")
	public ModelAndView auditNotPass(String id, String resultDesc, ModelMap modeMap){
		String string = "no";
		try{
			if(!isOperateDate()){
				string = "仅能在"+earliesttime+"到"+latesttime+"日审核数据";
				log.error(string);
				return new ModelAndView("jsonView","ajax_json",string);
			}
			Map map = new HashMap();
			map.put("id", id);
			Audit audit = auditService.load(map);
			audit.trim();
			if(!"0".equals(audit.getState())){
				string = "该数据已审核，不能再次审核";
				log.error(string + audit);
				return new ModelAndView("jsonView","ajax_json",string);
			}
			if(!auditService.checkAuditUser(audit.getCreator())){
				return new ModelAndView("jsonView","ajax_json","您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。");
			}
			audit.setModUser(getUser().getId());
			audit.setResultDesc(resultDesc);
			string = auditService.gradeAuditNotPass(audit);  //返回值为yes或者no
		}catch (Exception e) {
			log.error("审核不通过失败", e);
		}
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/auditDetail")
	public String auditDetail(String id, ModelMap modeMap){
		String opts = HfCacheUtil.getCache().getUrlAcl("lastmonthgrade");
		modeMap.addAttribute("opts", opts);
		Map mapId = new HashMap();
		mapId.put("id", id);
		Audit audit = auditService.load(mapId);
		MerGrade oldGrade = new MerGrade();
		MerGrade newGrade = new MerGrade();
		if(audit != null){
			audit.trim();
			List<MerGrade> merGrade = (List<MerGrade>) JsonHFUtil.getListFromJsonArrStr(
					audit.getModData(), MerGrade.class);
			oldGrade = merGrade.get(0);  //历史数据
			newGrade = merGrade.get(1);  //新数据
//			oldGrade = dealIndex(oldGrade);
			oldGrade.setOperState(GradeRule.getOperState(oldGrade.getMerInTime()));
			BigDecimal turnover = setTurnoverValue(oldGrade.getTurnover());
			modeMap.addAttribute("turnover",turnover == null ? null : turnover + "元");
		}
		modeMap.addAttribute("defGrade", defaultGrade);
		modeMap.addAttribute("oldGrade", oldGrade);
		modeMap.addAttribute("newGrade", newGrade);
		modeMap.addAttribute("audit", audit);
		return "mergrade/auditDetail";
	}
	
	@RequestMapping(value = "/historyGrade")
	public String historyGrade(ModelMap modeMap){
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("historygrade");
		modeMap.addAttribute("opts", opts);
		return "mergrade/historyGrade";
	}
	
	/**
	 * ********************************************
	 * method name   : gradeDownload 
	 * description   : 打开评分等级下载页面
	 * @return       : String
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-3-7  上午09:30:54
	 * *******************************************
	 */
	@RequestMapping(value = "/gradeDownload")
	public String gradeDownload(ModelMap modeMap){
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("gradedownload");
		modeMap.addAttribute("opts", opts);
		return "mergrade/gradeDownload";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryDownload")
	public ModelAndView queryDownload(){
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map, true);
				ObjectUtil.trimData(data);
				long count = queryCount(queryKey, map, true);
				msg = JsonUtil.toJson(count, data);
			} catch (Exception e) {
				try {
					log.error("查询失败", e);
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/downloadDoc")
	public ModelAndView downloadDoc(String docName) {
		List<List<String>> gradeList = new ArrayList<List<String>>();
		Map gradeMap = new HashMap();
		gradeMap.put("docName", docName);
		try {
			// path是指欲下载的文件的路径。
			String path = messageService.getSystemParam("MerGrade.GradeRankFilePath");
			path += docName;
			gradeList = merGradeService.readFile(path);
			gradeMap.put("gradeList", gradeList);
			log.info("读取文件成功：" + path);
		} catch (Exception e) {
			log.error("读取文件失败", e);
		}
		return new ModelAndView("excelDownloadDoc", "excel", gradeMap);
	}
	
	/**
	 * ********************************************
	 * method name   : compareValue 
	 * description   : 比较修改前后的数据
	 * @return       : Map<String,String>
	 * @param        : @param modData
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-2-26  下午05:21:22
	 * *******************************************
	 */
	private Map<String,String> compareValue(List<MerGrade> modData){
		Map<String,String> comValue = new HashMap<String,String>();
		if(modData != null && modData.size() == 2){
			MerGrade oldGrade = modData.get(0);
			MerGrade newGrade = modData.get(1);
			StringBuffer oldsb = new StringBuffer();
			StringBuffer newsb = new StringBuffer();
			StringBuffer title = new StringBuffer();
			if(newGrade.getTurnoverIndex().compareTo(oldGrade.getTurnoverIndex())!=0){
				oldsb.append(" 交易额:").append(returnValue(oldGrade.getTurnoverIndex()));
				newsb.append(" 交易额:<span style='color:red'>").append(returnValue(newGrade.getTurnoverIndex())).append("</span>");
				title.append(" 交易额:").append(returnValue(newGrade.getTurnoverIndex()));
			}
			if(newGrade.getRiseRateIndex().compareTo(oldGrade.getRiseRateIndex())!=0){
				oldsb.append(" 增产率:").append(returnValue(oldGrade.getRiseRateIndex()));
				newsb.append(" 增产率:<span style='color:red'>").append(returnValue(newGrade.getRiseRateIndex())).append("</span>");
				title.append(" 增产率:").append(returnValue(newGrade.getRiseRateIndex()));
			}
			
			if(newGrade.getFalseTradeIndex().compareTo(oldGrade.getFalseTradeIndex())!=0){
				oldsb.append(" 虚假交易:").append(returnValue(oldGrade.getFalseTradeIndex()));
				newsb.append(" 虚假交易:<span style='color:red'>").append(returnValue(newGrade.getFalseTradeIndex())).append("</span>");
				title.append(" 虚假交易:").append(returnValue(newGrade.getFalseTradeIndex()));
			}
			if(newGrade.getComplaintIndex().compareTo(oldGrade.getComplaintIndex())!=0){
				oldsb.append(" 客诉:").append(returnValue(oldGrade.getComplaintIndex()));
				newsb.append(" 客诉:<span style='color:red'>").append(returnValue(newGrade.getComplaintIndex())).append("</span>");
				title.append(" 客诉:").append(returnValue(newGrade.getComplaintIndex()));
			}
			if(newGrade.getSysStabIndex().compareTo(oldGrade.getSysStabIndex())!=0){
				oldsb.append(" 系统稳定性:").append(returnValue(oldGrade.getSysStabIndex()));
				newsb.append(" 系统稳定性:<span style='color:red'>").append(returnValue(newGrade.getSysStabIndex())).append("</span>");
				title.append(" 系统稳定性:").append(returnValue(newGrade.getSysStabIndex()));
			}
			if(newGrade.getCooperateIndex().compareTo(oldGrade.getCooperateIndex())!=0){
				oldsb.append(" 配合力度:").append(returnValue(oldGrade.getCooperateIndex()));
				newsb.append(" 配合力度:<span style='color:red'>").append(returnValue(newGrade.getCooperateIndex())).append("</span>");
				title.append(" 配合力度:").append(returnValue(newGrade.getCooperateIndex()));
			}
			if(newGrade.getBreachIndex().compareTo(oldGrade.getBreachIndex())!=0){
				oldsb.append(" 违约情况:").append(returnValue(oldGrade.getBreachIndex()));
				newsb.append(" 违约情况:<span style='color:red'>").append(returnValue(newGrade.getBreachIndex())).append("</span>");
				title.append(" 违约情况:").append(returnValue(newGrade.getBreachIndex()));
			}
			if(newGrade.getUpgradeIndex().compareTo(oldGrade.getUpgradeIndex())!=0){
				oldsb.append(" 重大投诉:").append(returnValue(oldGrade.getUpgradeIndex()));
				newsb.append(" 重大投诉:<span style='color:red'>").append(returnValue(newGrade.getUpgradeIndex())).append("</span>");
				title.append(" 重大投诉:").append(returnValue(newGrade.getUpgradeIndex()));
			}
			if(newGrade.getMarketingIndex().compareTo(oldGrade.getMarketingIndex())!=0){
				oldsb.append(" 营销活动:").append(returnValue(oldGrade.getMarketingIndex()));
				newsb.append(" 营销活动:<span style='color:red'>").append(returnValue(newGrade.getMarketingIndex())).append("</span>");
				title.append(" 营销活动:").append(returnValue(newGrade.getMarketingIndex()));
			}
			if(newGrade.getSupportIndex().compareTo(oldGrade.getSupportIndex())!=0){
				oldsb.append(" 资源支持:").append(returnValue(oldGrade.getSupportIndex()));
				newsb.append(" 资源支持:<span style='color:red'>").append(returnValue(newGrade.getSupportIndex())).append("</span>");
				title.append(" 资源支持:").append(returnValue(newGrade.getSupportIndex()));
			}
			
			comValue.put("oldsb", oldsb.toString());
			comValue.put("newsb", newsb.toString());
			comValue.put("title", title.toString());
		}
		return comValue;
	}
	
	/**
	 * ********************************************
	 * method name   : dealIndex 
	 * description   : 把默认值-99修改为null
	 * @return       : MerGrade
	 * @param        : @param merGrade
	 * @param        : @return
	 * modified      : xuhuafeng ,  2013-2-25  下午04:14:22
	 * *******************************************
	 */
	private MerGrade dealIndex(MerGrade merGrade){
		merGrade.setOperState(GradeRule.getOperState(merGrade.getMerInTime()));
		merGrade.setTurnoverIndex(setDefaultValue(merGrade.getTurnoverIndex()));
		merGrade.setRiseRateIndex(setDefaultValue(merGrade.getRiseRateIndex()));
		merGrade.setFalseTradeIndex(setDefaultValue(merGrade.getFalseTradeIndex()));
		merGrade.setComplaintIndex(setDefaultValue(merGrade.getComplaintIndex()));
		merGrade.setSysStabIndex(setDefaultValue(merGrade.getSysStabIndex()));
		merGrade.setCooperateIndex(setDefaultValue(merGrade.getCooperateIndex()));
		merGrade.setBreachIndex(setDefaultValue(merGrade.getBreachIndex()));
		merGrade.setUpgradeIndex(setDefaultValue(merGrade.getUpgradeIndex()));
		merGrade.setMarketingIndex(setDefaultValue(merGrade.getMarketingIndex()));
		merGrade.setSupportIndex(setDefaultValue(merGrade.getSupportIndex()));
		return merGrade;
	}
	
	private String returnValue(BigDecimal value){
		if(value.compareTo(defaultGrade)!=0){
			return value.toString();
		}else{
			return "-";
		}
	}
	
	/**
	 * ********************************************
	 * method name   : loadTradeGrade 
	 * description   : 交易数据手工入库
	 * @return       : ModelAndView
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : lz ,  2013-3-5  上午10:17:53
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/loadTradeGrade")
	public ModelAndView loadTradeGrade(){
		String string = "no";
		string = merGradeService.loadTradeGrade();//返回值为yes或者no
		log.info("交易数据手工入库完成");
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	/**
	 * ********************************************
	 * method name   : loadReduceDataGrade 
	 * description   : 核减数据入库
	 * @return       : ModelAndView
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : lz ,  2013-3-5  上午10:18:58
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/loadReduceDataGrade")
	public ModelAndView loadReduceDataGrade(){
		String string = "no";
		string = merGradeService.loadReduceDataGrade();//返回值为yes或者no
		log.info("核减数据入库完成");
		return new ModelAndView("jsonView","ajax_json",string);
	}
	
	/**
	 * ********************************************
	 * method name   : calculateGrade 
	 * description   : 手工计算评分
	 * @return       : ModelAndView
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : lz ,  2013-3-5  上午10:19:56
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/calculateGrade")
	public ModelAndView calculateGrade(){
		String string = "no";
		string = merGradeService.calculateGrade();//返回值为yes或者no
		log.info("手工计算评分完成");
		return new ModelAndView("jsonView","ajax_json",string);
	}
}
