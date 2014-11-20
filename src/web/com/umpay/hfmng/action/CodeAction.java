package com.umpay.hfmng.action;

import java.io.File;
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
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.model.FeeCode;
import com.umpay.hfmng.service.FeeCodeService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;

@Controller
@RequestMapping("/code")
public class CodeAction extends BaseAction{
	@Autowired
	private FeeCodeService feeCodeService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private MessageService messageService;
	 /**
	  * ********************************************
	  * method name   : index 
	  * description   : 跳转到计费代码管理页面
	  * @return       : String
	  * @param        : @param modeMap
	  * @param        : @return
	  * @param        : @throws DataAccessException
	  * modified      : xhf ,  2012-10-29  下午05:34:02
	  * *******************************************
	  */
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap){
		//查找权限
//		String opts="001,002,003,004,005,007,008,010"; // 权限测试
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("codemng");
		modeMap.addAttribute("opts", opts);
		return "code/index";
	}
	/**
	 * ********************************************
	 * method name   : queryAndView 
	 * description   :  查询方法
	 * @return       : ModelAndView
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-10-29  下午05:33:43
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> cateGorymap = optionService.getFeeCodeCategoryMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map code = data.get(i);
					String category = cateGorymap.get(code.get("CATEGORY"));
					code.put("CATEGORY", category);
					data.set(i, code);
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
	 * method name   : addFeeCode 
	 * description   : 跳转到添加计费代码页面
	 * @return       : String
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xhf ,  2012-10-29  下午05:32:58
	 * *******************************************
	 */
	@RequestMapping(value = "/addFeeCode")
	public String addFeeCode(ModelMap modeMap) {
		return "code/addFeeCode";
	}
	/**
	 * ********************************************
	 * method name   : export 
	 * description   : 导出方法
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xhf ,  2012-10-29  下午05:32:31
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map map = this.getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		Map<String, String> cateGorymap = optionService.getFeeCodeCategoryMap();
		List<FeeCode> listFeeCode = new ArrayList<FeeCode>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			List<Map<String, Object>> data = service.query(queryKey, map);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
			    FeeCode feeCode = new FeeCode();
			    feeCode.setServiceId((String) data.get(i).get("serviceId"));
			    feeCode.setDetail((String) data.get(i).get("detail"));
			    feeCode.setModTime((Timestamp) data.get(i).get("modTime"));
			    String feeType = String.valueOf(data.get(i).get("feeType"));
			    feeCode.setFeeType(feeType);
			    String category = cateGorymap.get(data.get(i).get("CATEGORY"));
			    feeCode.setCategory(category);
			    feeCode.setAmount(String.valueOf(data.get(i).get("amount")));
			    feeCode.setState(String.valueOf(data.get(i).get("state")));
			    listFeeCode.add(feeCode);
			}
		}
		return new ModelAndView("excelFeeCode", "excel", listFeeCode);
	}
	/**
	 * ********************************************
	 * method name   : checkServiceId 
	 * description   : 代码号唯一性验证
	 * @return       : ModelAndView
	 * @param        : @param serviceId
	 * @param        : @return
	 * modified      : xhf ,  2012-10-26  下午04:42:30
	 * *******************************************
	 */
	@RequestMapping(value = "/checkServiceId")
	public ModelAndView checkServiceId(String serviceId) {
		String msg = "1";     // 默认结果 1表示不存在, 0 表示存在
		FeeCode code = feeCodeService.load(StringUtils.trim(serviceId));
		if (code != null){
			msg = "0";
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}
	/**
	 * ********************************************
	 * method name   : save 
	 * description   : 保存方法
	 * @return       : ModelAndView
	 * @param        : @param feeCode
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-10-29  下午05:31:45
	 * *******************************************
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(FeeCode feeCode){
		User user = this.getUser();
		feeCode.setModUser(user.getId()); // 新增修改人操作
		feeCode.trim();
		String result="0";
		FeeCode code = feeCodeService.load(feeCode.getServiceId());
		try{
			if(code != null){
				result = "当前计费代码已存在!";
				log.error(result + feeCode.toString());
			}else{
				result = feeCodeService.saveFeeCode(feeCode);
				log.info("新增计费代码成功");
			}
		}catch(Exception e){
			log.error("计费代码添加操作失败",e);			
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	/**
	 * ********************************************
	 * method name   : load 
	 * description   : 跳转到修改页面方法
	 * @return       : String
	 * @param        : @param id
	 * @param        : @param modeMap
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : xhf ,  2012-10-29  下午05:31:01
	 * *******************************************
	 */
	@RequestMapping(value = "/modify")
	public String modify(String id, ModelMap modeMap) {
		FeeCode feeCode = feeCodeService.load(id);
		feeCode.trim();
    	modeMap.addAttribute("feeCode", feeCode);
		return "code/modifyFeeCode";
	}
	/**
	 * ********************************************
	 * method name   : update 
	 * description   : 修改方法
	 * @return       : ModelAndView
	 * @param        : @param feeCode
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xhf ,  2012-10-29  下午05:30:41
	 * *******************************************
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(FeeCode feeCode,ModelMap modeMap) {
		User user = this.getUser();
		feeCode.setModUser(user.getId()); // 修改人
		String result = "0";
		try {
         	result = feeCodeService.modifyFeeCode(feeCode); // 修改操作,string表示操作结果,返回值为1或者0
		} catch (Exception e) {
			log.error("修改计费代码失败",e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	/**
	 * ********************************************
	 * method name   : enable 
	 * description   : 启用方法
	 * @return       : ModelAndView
	 * @param        : @param ID
	 * @param        : @return
	 * modified      : xhf ,  2012-10-29  下午05:29:52
	 * *******************************************
	 */
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String ID) {
		User user = this.getUser(); //获取当前登录用户
		ID = ID.substring(0, ID.length() - 1); // 去掉最后的一个逗号
		String[] array = ID.split(",");
		String result = "0";
		try {
			result = feeCodeService.enableAndDisable(array, user, "2");  //启用操作,目标状态为 "2"
		} catch (Exception e) {
			log.error("启用/禁用计费代码失败",e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	/**
	 * ********************************************
	 * method name   : disable 
	 * description   : 禁用方法
	 * @return       : ModelAndView
	 * @param        : @param ID
	 * @param        : @return
	 * modified      : xhf ,  2012-10-29  下午05:29:29
	 * *******************************************
	 */
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String ID) {
		ID = ID.substring(0, ID.length() - 1); // 去掉最后的一个逗号
		String[] array = ID.split(",");
		User user = this.getUser(); // 获取当前登录用户
		String result = "0";
		try {
			result = feeCodeService.enableAndDisable(array, user, "4");  //禁用操作,目标状态为"4"
		} catch (Exception e) {
			log.error("启用/禁用计费代码失败",e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}
	/**
	 * ********************************************
	 * method name   : batchImport 
	 * description   : 导入
	 * @return       : ModelAndView
	 * @param        : @param request
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:38:34
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
        	List<FeeCode> feeCodeList = feeCodeService.upload(file);
        	if(feeCodeList.size() != 0){
        		File fl = File.createTempFile(TimeUtil.datetime14(), ".xls");
        		feeCodeService.writeFile(fl, feeCodeList);
        		String fileName = fl.getAbsolutePath();
        		log.info("计费代码导入失败列表保存位置为" + fl.getPath());
        		result.put("fileName", fileName);
        	}else{
        		result.put("result", "yes");
        		log.info("处理上传文件成功!");
        	}
        }catch (Exception e) {
        	log.error("处理上传文件失败", e);
        	result.put("result", "处理上传文件失败,请仔细检查上传文件内容!");
		}
        return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(result));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/download")
	public ModelAndView download(String fileName){
		List<FeeCode> feeCodeList = feeCodeService.readFile(fileName);
		Map map = new HashMap();
		map.put("feeCodeList", feeCodeList);
		return new ModelAndView("excelViewFeeCodeImportError", "excel", map);
	}
	
	/**
	 * ********************************************
	 * method name   : openDetail 
	 * description   : 跳转到操作记录页面
	 * @return       : String
	 * @param        : @param serviceId
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:38:47
	 * *******************************************
	 */
	@RequestMapping(value = "/opendetail")
	public String openDetail(String serviceId,ModelMap modeMap){
		modeMap.addAttribute("serviceId", StringUtils.trim(serviceId));
		return "code/detail";
	}
	/**
	 * ********************************************
	 * method name   : detail 
	 * description   : 查询操作日志
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:39:59
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/detail")
	public ModelAndView detail(){
		Map users = optionService.getAllUserIdAndName();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
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
	/**
	 * ********************************************
	 * method name   : statIndex 
	 * description   : 计费代码使用查询页面
	 * @return       : String
	 * @param        : @param modeMap
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:40:28
	 * *******************************************
	 */
	@RequestMapping(value = "/stat/index")
	public String statIndex(ModelMap modeMap) {
		//查找权限
//		String opts="001,002"; // 权限测试
		HfCache hfCache=HfCacheUtil.getCache(); //zhao 获取cache
		String opts = hfCache.getUrlAcl("codestat");
		modeMap.addAttribute("opts", opts);
		return "code/stat/index";
	}
	/**
	 * ********************************************
	 * method name   : statQuery 
	 * description   : 计费代码使用查询的统一查询
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:40:51
	 * @see          : 
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/stat/query")
	public ModelAndView statQuery() {
		Map<String, String> cateGorymap = optionService.getFeeCodeCategoryMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					Map code = data.get(i);
					String category = cateGorymap.get(code.get("CATEGORY"));
					code.put("CATEGORY", category);
					data.set(i, code);
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
	 * method name   : exportStat 
	 * description   : 计费代码使用查询的导出
	 * @return       : ModelAndView
	 * @param        : @return
	 * modified      : xhf ,  2012-11-21  下午05:41:19
	 * *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/stat/export")
	public ModelAndView exportStat() {
		Map map = this.getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		Map<String, String> cateGorymap = optionService.getFeeCodeCategoryMap();
		List<FeeCode> listFeeCode = new ArrayList<FeeCode>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			List<Map<String, Object>> data = service.query(queryKey, map);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
			    FeeCode feeCode = new FeeCode();
			    feeCode.setServiceId((String) data.get(i).get("serviceId"));
			    feeCode.setDetail((String) data.get(i).get("detail"));
			    feeCode.setModTime((Timestamp) data.get(i).get("modTime"));
			    String feeType = String.valueOf(data.get(i).get("feeType"));
			    feeCode.setFeeType(feeType);
			    String category = cateGorymap.get(data.get(i).get("CATEGORY"));
			    feeCode.setCategory(category);
			    feeCode.setAmount(String.valueOf(data.get(i).get("amount")));
			    feeCode.setState(String.valueOf(data.get(i).get("state")));
			    String useCount = String.valueOf(data.get(i).get("useCount"));
			    feeCode.setUseCount(useCount=="null"?"0":useCount);
			    feeCode.setMatchType(String.valueOf(data.get(i).get("matchType")));
			    listFeeCode.add(feeCode);
			}
		}
		return new ModelAndView("excelFeeCodeUseQuery", "excel", listFeeCode);
	}
	
}
