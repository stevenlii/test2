package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.MerInfoService;
import com.umpay.hfmng.service.MerOperService;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * ****************** 类说明 ********************* class : MerAuditAction
 * 
 * @author : xhf
 * @version : 1.0 description : 商户审核
 *          ***********************************************
 */
@Controller
@RequestMapping("/meraudit")
public class MerAuditAction extends BaseAction {
	@Autowired
	private AuditService service;

	@Autowired
	private MerInfoService merService;

	@Autowired
	private OptionService optionService;

	@Autowired
	private MessageService messageService;
	@Autowired
	private MerOperService merOperService;

	/**
	 * ******************************************** method name : index
	 * description : 商品审核首页
	 * 
	 * @return : String
	 * @param : @throws DataAccessException modified : xhf , 2012-9-5 上午10:06:26
	 *        *******************************************
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		// 查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("merinf");
		modeMap.addAttribute("opts", opts);
		Map<String, String> map = optionService.getMerBusiTypeMap();
		String tree = ZTreeUtil.buildZTree(map);
		tree = "[{id:'',name:'全部'},"+tree+"]";
		modeMap.addAttribute("busiType", tree);
		return "meraudit/index";
	}

	/**
	 * ******************************************** method name : query
	 * description : 多条件查询
	 * 
	 * @return : ModelAndView
	 * @param : @throws DataAccessException modified : xhf , 2012-9-5 上午10:07:33
	 *        *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map merCategoryMap = optionService.getMerCategoryMap();
		Map users = optionService.getAllUserIdAndName();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				Map<String, String> busiTypeMap = optionService.getMerBusiTypeMap();
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				for (int i = 0; i < data.size(); i++) {
					// 渲染数据
					Map audit = data.get(i);
					// 解析json串
					MerInfo modData = (MerInfo) JsonHFUtil
							.getObjFromJsonArrStr(
									(String) audit.get("modData"),
									MerInfo.class);
					String busiType = (String) audit.get("IXDATA2");
					audit.put("IXDATA2", busiTypeMap.get(busiType));
					audit.put("MERNAME", modData.getMerName());
					if (modData.getCategory() != null) {
						audit.put("CATEGORY", merCategoryMap.get(modData
								.getCategory().trim()));
					}
					String creatorId = ((String) audit.get("creator")).trim();
					audit.put("CREATOR", users.get(creatorId));
					if (audit.get("CREATOR") == null) {
						audit.put("CREATOR", messageService
								.getSystemParam(creatorId));
					}
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
	 * ******************************************** method name : merAuditPass
	 * description : 商户审核通过
	 * 
	 * @return : ModelAndView
	 * @param : @param id
	 * @param : @param resultDesc
	 * @param : @param modeMap
	 * @param : @throws DataAccessException modified : xhf , 2012-9-5 上午10:13:08
	 *        *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/merAuditPass")
	public ModelAndView merAuditPass(String id, String resultDesc,
			ModelMap modeMap) {
		Map map = new HashMap();
		map.put("id", id);
		Audit audit = service.load(map);
		audit.trim(); // 去空格
		if(!"0".equals(audit.getState())){
			String string = "该数据已审核，不能再次审核";
			log.error(string + audit);
			return new ModelAndView("jsonView","ajax_json",string);
		}
		if(!service.checkAuditUser(audit.getCreator())){
			return new ModelAndView("jsonView", "ajax_json", "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。");
		}
		String modUser = getUser().getId();
		audit.setState("2"); // 审核状态改为通过
		audit.setModUser(modUser);
		if (resultDesc != null) {
			audit.setResultDesc(resultDesc);
		}
		String string = "no";
		try {
			string = service.merAuditPass(audit); // 返回值为yes或者no
		} catch (Exception e) {
			log.error("审核通过失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", string);
	}

	/**
	 * ******************************************** method name :
	 * merAuditNotPass description : 商户审核不通过
	 * 
	 * @return : ModelAndView
	 * @param : @param id
	 * @param : @param resultDesc
	 * @param : @param modeMap
	 * @param : @throws DataAccessException modified : xhf , 2012-9-5 上午10:51:32
	 *        *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/merAuditNotPass")
	public ModelAndView merAuditNotPass(String id, String resultDesc,
			ModelMap modeMap) {
		Map map = new HashMap();
		map.put("id", id);
		Audit audit = service.load(map);
		audit.trim();
		if(!"0".equals(audit.getState())){
			String string = "该数据已审核，不能再次审核";
			log.error(string + audit);
			return new ModelAndView("jsonView","ajax_json",string);
		}
		if(!service.checkAuditUser(audit.getCreator())){
			return new ModelAndView("jsonView", "ajax_json", "您不能审核自己提交的数据，请让其他操作员认真审核您提交的数据。");
		}
		audit.setState("1"); // 审核状态改为不通过
		audit.setModUser(getUser().getId());
		audit.setResultDesc(resultDesc);
		String string = "no";
		try {
			string = service.merAuditNotPass(audit); // 返回值为yes或者no
		} catch (Exception e) {
			log.error("审核不通过失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", string);
	}

	/**
	 * ******************************************** method name : detail
	 * description : 详情展示页面
	 * 
	 * @return : String
	 * @param : @param id
	 * @param : @param modeMap
	 * @param : @throws DataAccessException modified : xhf , 2012-9-5 上午10:29:20
	 *        *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/detail")
	public String detail(String id, ModelMap modeMap) {
		// 查找权限
		String opts = HfCacheUtil.getCache().getUrlAcl("merinf");
		modeMap.addAttribute("opts", opts);
		Map merCategoryMap = optionService.getMerCategoryMap(); // 获取商户分类map
		Map<String, String> busiTypeMap = optionService.getMerBusiTypeMap();
		Map mapId = new HashMap();
		mapId.put("id", id);
		Audit au = service.load(mapId);
		modeMap.addAttribute("auditType", au.getAuditType());
		// 反序列化审核信息
		MerInfo audit = (MerInfo) JsonHFUtil.getObjFromJsonArrStr(au
				.getModData(), MerInfo.class);
		audit.trim();
		audit.setId(id);
		// 商户分类渲染
		audit.setCategory(StringUtils.trim((String) merCategoryMap.get(audit
				.getCategory())));
		// 渲染运营负责人
		String operNameStr = merOperService.getOperNameStrByOperStr(audit.getOperator());
		audit.setOperator(operNameStr);
		// 渲染业务覆盖
		audit.setBusiType(busiTypeMap.get(audit.getBusiType()));
		// 渲染接口类型
		if ("0".equals(audit.getInterType())) {
			audit.setInterType("标准");
		} else if ("1".equals(audit.getInterType())) {
			audit.setInterType("特殊");
		}
		// 渲染是否渠道报备
		if ("0".equals(audit.getChnlCheck())) {
			audit.setChnlCheck("否");
		} else if ("1".equals(audit.getChnlCheck())) {
			audit.setChnlCheck("是");
		}

		modeMap.addAttribute("audit", audit);
		modeMap.addAttribute("resultDesc", au.getResultDesc());
		String state = au.getState();
		if ("0".equals(state)) { // 是否为待审核状态
			if ("2".equals(au.getAuditType())) { // 审核类型为修改,需获取历史数据展示到页面

				MerInfo oldMer = merService.load(au.getIxData());
				oldMer.trim();
				// 商户分类渲染
				oldMer.setCategory(StringUtils.trim((String) merCategoryMap
						.get(oldMer.getCategory())));
				// 渲染运营负责人
				String oldOperName = merOperService.getOperStrByMerId(audit.getMerId());
				oldMer.setOperator(oldOperName);
				oldMer.setBusiType(busiTypeMap.get(oldMer.getBusiType()));
				if ("0".equals(oldMer.getInterType())) {
					oldMer.setInterType("标准");
				} else if ("1".equals(oldMer.getInterType())) {
					oldMer.setInterType("特殊");
				}

				if ("0".equals(oldMer.getChnlCheck())) {
					oldMer.setChnlCheck("否");
				} else if ("1".equals(oldMer.getChnlCheck())) {
					oldMer.setChnlCheck("是");
				}

				// 渲染是否开通对账文件下载字段
				if (audit.getFileDL() == 0) {
					audit.setFileDLName("否");
				} else if (audit.getFileDL() == 1) {
					audit.setFileDLName("是");
				}

				modeMap.addAttribute("oldMer", oldMer);
				return "meraudit/detailMod"; // 修改详情页面
			} else {
				return "meraudit/detailAdd"; // 新增，启用/禁用详情页面
			}
		} else {
			return "meraudit/showDetail"; // 审核后详情页面
		}
	}

}
