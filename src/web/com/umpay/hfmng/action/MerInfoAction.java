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

import com.umpay.hfmng.cache.HfCache;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MerInfoService;
import com.umpay.hfmng.service.MerOperService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * ****************** 类说明 ********************* class : MerInfoAction
 * 
 * @author : anshuqiang
 * @version : 1.0 description : 商户信息管理
 *          ***********************************************
 */
@Controller
@RequestMapping("/merinf")
public class MerInfoAction extends BaseAction {
	@Autowired
	private MerInfoService merInfoService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private MerOperService merOperService;

	/**
	 * ******************************************** method name : add
	 * description : 跳转到商户添加页面
	 * 
	 * @return : String
	 * @param : @return
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-8-30
	 *        下午03:49:21
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/add")
	public String add(ModelMap modeMap) {
		Map<String, String> map = optionService.getMerBusiTypeMap();
		String tree = ZTreeUtil.buildZTree(map);
		tree = "[" + tree + "]";
		modeMap.addAttribute("busiType", tree);
		Map<String, String> merCategory = optionService.getMerCategoryMap();
		modeMap.addAttribute("merCategory", merCategory);
		modeMap.addAttribute("zNodes", ZTreeUtil.getOperatorTree());
		return "merinf/addMer";
	}

	/**
	 * ******************************************** method name : modify
	 * description : 跳转到商户修改页面
	 * 
	 * @return : String
	 * @param : @param id
	 * @param : @param modeMap
	 * @param : @return
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-8-30
	 *        下午03:55:49
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/modify")
	public String modify(String id, ModelMap modeMap) {
		MerInfo mer = merInfoService.load(id);
		mer.trim();
		modeMap.addAttribute("mer", mer);
		Map<String, String> map = optionService.getMerBusiTypeMap();
		String tree = ZTreeUtil.buildZTree(map);
		tree = "[" + tree + "]";
		modeMap.addAttribute("busiType", tree);
		modeMap.addAttribute("zNodes", ZTreeUtil.getModOperatorTree(id));
		return "merinf/modifyMer";
	}

	/**
	 * ******************************************** method name : list
	 * description : 跳转到商户信息管理首页
	 * 
	 * @return : String
	 * @param : @return
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-8-30
	 *        下午03:50:44
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		Map<String, String> map = optionService.getMerBusiTypeMap();
		String tree = ZTreeUtil.buildZTree(map);
		tree = "[{id:'',name:'全部'}," + tree + "]";
		modeMap.addAttribute("busiType", tree);
		// 查找权限
		HfCache hfCache = HfCacheUtil.getCache(); // zhao 获取cache
		String opts = hfCache.getUrlAcl("merinf");
		modeMap.addAttribute("opts", opts);
		return "merinf/index";
	}

	/**
	 * ******************************************** method name : save
	 * description :添加操作执行的保存商户信息方法
	 * 
	 * @return : String
	 * @param : @param merInfo
	 * @param : @return
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-8-27
	 *        上午11:26:40
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(MerInfo merInfo) {
		merInfo.trim();
		User user = this.getUser();
		merInfo.setModUser(user.getId()); // 新增修改人操作
		String rs = "0";
		try {
			Map<String, String> mapWhere = new HashMap<String, String>();
			mapWhere.put("merId", merInfo.getMerId());
			String check = auditService.getCheckMerId(mapWhere);
			if (!"1".equals(check)) {
				log.error("该商户已存在或正在审核" + merInfo.toString());
				rs = "该商户已存在或正在审核";
			} else {
				rs = merInfoService.saveMerAudit(merInfo); // 1表示添加成功,0代表添加失败
			}
		} catch (Exception e) {
			log.error("添加商户操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", rs);
	}

	/**
	 * ******************************************** method name : update
	 * description : 更新商户信息方法
	 * 
	 * @return : ModelAndView
	 * @param : @param merInfo
	 * @param : @param modeMap
	 * @param : @return
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-8-30
	 *        下午03:51:55
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/update")
	public ModelAndView update(MerInfo merInfo, ModelMap modeMap) {
		merInfo.trim();
		User user = this.getUser();
		merInfo.setModUser(user.getId()); // 修改人
		String result = "0";
		try {
			result = merInfoService.modifyMerInfo(merInfo); // 修改操作,string表示操作结果,返回值为1或者0
		} catch (Exception e) {
			log.error("修改商户操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}

	/**
	 * ******************************************** method name : view
	 * description :查看详情方法
	 * 
	 * @return : String
	 * @param : @param id
	 * @param : @param modeMap
	 * @param : @return
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-8-27
	 *        上午11:25:09
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap) {
		MerInfo mer = merInfoService.load(id);
		mer.trim();
		Map<String, String> busiTypeMap = optionService.getMerBusiTypeMap();
		mer.setBusiType(busiTypeMap.get(mer.getBusiType()));
		String operName = merOperService.getOperStrByMerId(id);
		mer.setOperator(operName);
		Map<String, String> merCateGorymap = optionService.getMerCategoryMap();
		mer.setCategory(merCateGorymap.get(mer.getCategory()));
		modeMap.addAttribute("mer", mer);
		return "merinf/detail";
	}

	/**
	 * ******************************************** method name : queryAndView
	 * description : 查询商户信息方法
	 * 
	 * @return : ModelAndView
	 * @param : @return
	 * @param : @throws DataAccessException modified : anshuqiang , 2012-8-30
	 *        下午03:59:08
	 * @see : *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> merCateGorymap = optionService.getMerCategoryMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> busiTypeMap = optionService.getMerBusiTypeMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					// 渲染数据
					Map merInfo = data.get(i);
					String busiType = (String) merInfo.get("BUSITYPE");
					merInfo.put("BUSITYPE", busiTypeMap.get(busiType));
					String merCategory = merCateGorymap.get(merInfo.get("CATEGORY").toString().trim());
					merInfo.put("CATEGORY", merCategory);
					String merId = (String) merInfo.get("MERID");
					merInfo.put("OPERATOR", merOperService.getOperStrByMerId(merId));
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
	 * ******************************************** method name : export
	 * description : 导出商户信息方法
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : anshuqiang , 2012-8-20 下午01:15:28
	 * @see : *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map<String, String> merCateGorymap = optionService.getMerCategoryMap();
		List<MerInfo> listMerInfos = new ArrayList<MerInfo>();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			List<Map<String, Object>> data = service.query(queryKey, map);
			ObjectUtil.trimData(data);
			Map<String, String> busiTypeMap = optionService.getMerBusiTypeMap();
			for (int i = 0; i < data.size(); i++) {
				// 渲染数据
				Map obj = data.get(i);
				MerInfo merInfo = new MerInfo();
				merInfo.setMerId((String) obj.get("merid"));
				merInfo.setMerName((String) obj.get("merName"));
				merInfo.setModTime((Timestamp) obj.get("modTime"));
				merInfo.setBusiType(obj.get("busiType") == null ? "" : obj.get("busiType").toString());
				String busiType = obj.get("busiType") == null ? "" : obj.get("busiType").toString();
				merInfo.setBusiType(busiTypeMap.get(busiType));
				merInfo.setAddWay(Integer.valueOf(obj.get("addWay") == null ? "" : obj.get("addWay").toString()));
				String merCategory = "";
				if (obj.get("CATEGORY") != null && obj.get("CATEGORY").toString() != "") {
					merCategory = merCateGorymap.get(obj.get("CATEGORY").toString());
				}
				merInfo.setCategory(merCategory);
				merInfo.setInterType(obj.get("interType") == null ? "" : obj.get("interType").toString());
				merInfo.setChnlCheck(obj.get("chnlCheck") == null ? "" : obj.get("chnlCheck").toString());
				String merId = merInfo.getMerId();
				merInfo.setOperator(merOperService.getOperStrByMerId(merId));
				merInfo.setState(obj.get("state") == null ? "" : obj.get("state").toString());
				listMerInfos.add(merInfo);
			}
		}
		return new ModelAndView("excelViewMer", "excel", listMerInfos);
	}

	/**
	 * ******************************************** method name : start
	 * description :启用操作
	 * 
	 * @return : ModelAndView
	 * @param : @param ID
	 * @param : @return modified : anshuqiang , 2012-8-27 上午11:24:11
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String ID) {
		User user = this.getUser(); // 获取当前登录用户
		ID = ID.substring(0, ID.length() - 1); // 去掉最后的一个逗号
		String[] array = ID.split(",");
		String result = "0";
		try {
			result = merInfoService.enableAndDisable(array, user, "2"); // 启用操作,目标状态为
																		// "2"
		} catch (Exception e) {
			log.error("启用商户操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}

	/**
	 * ******************************************** method name : stop
	 * description :禁用操作
	 * 
	 * @return : ModelAndView
	 * @param : @param ID
	 * @param : @return modified : anshuqiang , 2012-8-27 上午11:23:09
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String ID) {
		ID = ID.substring(0, ID.length() - 1); // 去掉最后的一个逗号
		String[] array = ID.split(",");
		User user = this.getUser(); // 获取当前登录用户
		String result = "0";
		try {
			result = merInfoService.enableAndDisable(array, user, "4"); // 禁用操作,目标状态为"4"
		} catch (Exception e) {
			log.error("禁用商户操作失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}

	/**
	 * ******************************************** method name : checkMerId
	 * description : 商户号唯一性验证
	 * 
	 * @return : ModelAndView
	 * @param : @param merId
	 * @param : @return modified : anshuqiang , 2012-8-30 下午03:52:54
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/checkMerId")
	public ModelAndView checkMerId(String merId) {
		String msg = "1"; // 默认结果 1表示不存在 0 表示存在
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merId", merId.trim());
		String audit = auditService.getCheckMerId(mapWhere); // 查询是否存在
		if (audit != "1") {
			msg = "0";
		}
		return new ModelAndView("jsonView", "ajax_json", msg);
	}

	// /**
	// * ********************************************
	// * method name : checkMerName
	// * description : 新增时验证商户名称唯一性
	// * @return : ModelAndView
	// * @param : @param merId
	// * @param : @param merName
	// * @param : @return
	// * modified : xuhuafeng , 2012-12-20 上午11:14:38
	// * *******************************************
	// */
	// @RequestMapping(value = "/checkMerName")
	// public ModelAndView checkMerName(String merId, String merName) {
	// String msg = "1"; // 默认结果 1表示不存在 0 表示存在
	// Map<String, String> mapWhere = new HashMap<String, String>();
	// if(StringUtils.trim(merId) != "" && StringUtils.trim(merId) != null){
	// mapWhere.put("merId", StringUtils.trim(merId));
	// }
	// mapWhere.put("merName", merName.trim());
	// String audit = auditService.getCheckMerName(mapWhere); // 查询是否存在
	// if (audit != "1") {
	// msg = "0";
	// }
	// return new ModelAndView("jsonView", "ajax_json", msg);
	// }
	// /**
	// * ********************************************
	// * method name : checkModMerName
	// * description : 修改时验证商户名称唯一性
	// * @return : ModelAndView
	// * @param : @param merId
	// * @param : @param merName
	// * @param : @return
	// * modified : xuhuafeng , 2012-12-20 上午11:15:02
	// * *******************************************
	// */
	// @RequestMapping(value = "/checkModMerName")
	// public ModelAndView checkModMerName(String merId, String merName) {
	// String msg = "1"; // 默认结果 1表示不存在 0 表示存在
	// Map<String, String> mapWhere = new HashMap<String, String>();
	// if(StringUtils.trim(merId) != "" && StringUtils.trim(merId) != null){
	// mapWhere.put("merId", StringUtils.trim(merId));
	// }
	// mapWhere.put("merName", merName.trim());
	// String audit = auditService.checkModMerName(mapWhere); // 查询是否存在
	// if (audit != "1") {
	// msg = "0";
	// }
	// return new ModelAndView("jsonView", "ajax_json", msg);
	// }

	/**
	 * ******************************************** method name : importMer
	 * description : 导入老商户
	 * 
	 * @return : ModelAndView
	 * @param : @param oldMerId
	 * @param : @return modified : xuhuafeng , 2013-2-19 上午11:47:10
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/importMer")
	public ModelAndView importMer(String oldMerId) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			String userId = getUser().getId();
			result = merInfoService.importMer(oldMerId, userId);
		} catch (Exception e) {
			log.error("导入老商户[" + oldMerId + "]失败", e);
			result.put("flag", "no");
		}
		return new ModelAndView("jsonView", "ajax_json", JsonHFUtil.getJsonArrStrFrom(result));
	}
}
