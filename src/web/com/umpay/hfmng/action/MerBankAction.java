/** *****************  JAVA头文件说明  ****************
 * file name  :  MerBankAction.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-9-20
 * *************************************************/

package com.umpay.hfmng.action;

import java.sql.Timestamp;
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
import com.umpay.hfmng.common.ObjectUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.ZTreeUtil;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.MerBank;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.service.AuditService;
import com.umpay.hfmng.service.BankService;
import com.umpay.hfmng.service.MerBankService;
import com.umpay.hfmng.service.MerInfoService;
import com.umpay.hfmng.service.MerOperService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;
import com.umpay.uniquery.IUniQueryService;
import com.umpay.uniquery.util.JsonUtil;

/**
 * ****************** 类说明 ********************* class : MerBankAction
 * 
 * @author : xhf
 * @version : 1.0 description : 商户银行管理
 * ************************************************/
@Controller
@RequestMapping("/merbank")
public class MerBankAction extends BaseAction {

	@Autowired
	private OptionService optionService;
	@Autowired
	private MerBankService merBankService;
	@Autowired
	private BankService bankService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private MerOperService merOperService;

	/**
	 * ******************************************** method name : index
	 * description : 商户银行管理首页
	 * 
	 * @return : String
	 * @param : @return modified : xhf , 2012-11-21 下午04:13:06
	 *        *******************************************
	 */
	@RequestMapping(value = "/index")
	public String index(ModelMap modeMap) {
		// String opts="001,002,003,004,005,007,009"; //权限测试
		HfCache hfCache = HfCacheUtil.getCache(); // zhao 获取cache
		String opts = hfCache.getUrlAcl("merbank");
		modeMap.addAttribute("opts", opts);
		return "merbank/index";
	}

	/**
	 * ******************************************** method name : query
	 * description : 统一查询
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : xhf , 2012-11-13 下午05:46:56
	 *        *******************************************
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/query")
	public ModelAndView query() {
		Map<String, String> bankNameMap = HfCacheUtil.getCache().getBankNameMap();
		Map map = getParametersFromRequest(super.getHttpRequest());
		String queryKey = (String) map.get("queryKey");
		String msg = null;
		if (queryKey != null) {
			try {
				List<Map<String, Object>> data = queryPageList(queryKey, map);
				ObjectUtil.trimData(data);
				for (int i = 0; i < data.size(); i++) {
					// 渲染数据
					Map merBank = data.get(i);
					String bankId = (String) merBank.get("bankId");
					String bankName = bankNameMap.get(bankId);
					merBank.put("BANKNAME", bankName);
					String merId = (String) merBank.get("MERID");
					merBank.put("OPERATOR", merOperService.getOperStrByMerId(merId));
					data.set(i, merBank);
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
	 * ******************************************** method name : openOneMer
	 * description : 跳转到配置商户银行页面
	 * 
	 * @return : String
	 * @param : @param modeMap
	 * @param : @return modified : xhf , 2012-11-13 下午05:48:07
	 *        *******************************************
	 */
	@RequestMapping(value = "/modify")
	public String modify(ModelMap modeMap) {
		String merBankId = optionService.getMerBankIdList();
		Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap();
		List<String> merBankName = new ArrayList<String>();
		String bankId = "";
		if (merBankId != null) {
			String[] merBankIds = merBankId.split(",");
			for (int i = 0; i < merBankIds.length; i++) {
				BankInfo bank = (BankInfo) bankMap.get(merBankIds[i]);
				boolean flag = false;
				if (i == merBankIds.length - 1) {
					flag = true;
				}
				if (bank != null) {
					merBankName.add(bank.getBankName().trim());
					if (flag) {
						bankId += merBankIds[i];
					} else {
						bankId += merBankIds[i] + ",";
					}
				} else {
					log.error("银行号[" + merBankIds[i] + "]找不到匹配的银行");
				}
			}
		}
		modeMap.addAttribute("merBankId", bankId);
		modeMap.addAttribute("merBankName", merBankName);
		
		// 通过读取配置，获得Tree配置信息。{BS=61, XE=6, GM=62}
		Map<String, String> upBankTypeMap = optionService
				.getBankTypeMap("merbankType");
		Map<String, String> bankTypeMap = optionService.getBankTypeMap();
		StringBuffer zTreeNodes = new StringBuffer("[");// 构造树节点
		for (Map.Entry<String, String> entry : upBankTypeMap.entrySet()) {
			String upbanktype = entry.getKey();// GM
			String dbcode = entry.getValue();// 62
			String upbanktypeNode = "{ id:'" + upbanktype
					+ "', pId:'0', name:'" + bankTypeMap.get(dbcode)
					+ "', isParent:true}";
			StringBuffer nodes = ZTreeUtil.getUPSubBankTree(dbcode, upbanktype);
			zTreeNodes.append(upbanktypeNode).append(nodes).append(",");
		}
		zTreeNodes = zTreeNodes.replace(zTreeNodes.length() - 1,
				zTreeNodes.length(), "]"); // 加两个父节点 再加节点内容
		modeMap.addAttribute("zNodes", zTreeNodes);
		modeMap.addAttribute("zNodes", zTreeNodes.toString());
		return "merbank/oneMer";
	}

	/**
	 * ******************************************** method name : disableMerBank
	 * description : 禁用方法
	 * 
	 * @return : ModelAndView
	 * @param : @param id
	 * @param : @return modified : xhf , 2012-11-13 下午05:49:44
	 *        *******************************************
	 */
	@RequestMapping(value = "/disable")
	public ModelAndView disable(String id) {
		String string = "no";
		String[] array = id.split(",");
		User user = this.getUser(); // 获取当前登录用户
		try {
			string = merBankService.enableAnddisable(array, user, "4"); // 禁用操作，目标状态为
																		// ”4“
			log.info("商户银行禁用操作成功！");
		} catch (Exception e) {
			log.error("商户银行禁用操作失败！", e);
		}
		return new ModelAndView("jsonView", "ajax_json", string);

	}

	/**
	 * ******************************************** method name : enableMerBank
	 * description : 启用方法
	 * 
	 * @return : ModelAndView
	 * @param : @param id
	 * @param : @return modified : xhf , 2012-11-13 下午05:50:40
	 *        *******************************************
	 */
	@RequestMapping(value = "/enable")
	public ModelAndView enable(String id) {
		String string = "no";
		String[] array = id.split(",");
		User user = this.getUser(); // 获取当前登录用户
		try {
			string = merBankService.enableAnddisable(array, user, "2"); // 启用操作，目标状态为
																		// ”2“
		} catch (Exception e) {
			log.error("商户银行管理action中启用操作失败！", e);
		}
		return new ModelAndView("jsonView", "ajax_json", string);

	}

	/**
	 * ******************************************** method name : getMerName
	 * description : 根据商户号获取商户名
	 * 
	 * @return : ModelAndView
	 * @param : @param merId
	 * @param : @return modified : xhf , 2012-11-13 下午05:51:38
	 *        *******************************************
	 */
	@RequestMapping(value = "/getmername")
	public ModelAndView getMerName(String merId) {
		String merName = HfCacheUtil.getCache().getMerName(merId);
		return new ModelAndView("jsonView", "ajax_json", merName);
	}

	/**
	 * ******************************************** method name : getNewTree
	 * description : 刷新树
	 * 
	 * @return : ModelAndView
	 * @param : @param merId
	 * @param : @return modified : xhf , 2012-11-13 下午05:52:20
	 *        *******************************************
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getnewtree")
	public ModelAndView getNewTree(String merId) {
		// 通过读取配置，获得Tree配置信息。{BS=61, XE=6, GM=62}
		Map<String, String> upBankTypeMap = optionService.getBankTypeMap("merbankType");
		// [{"3":"移动话费"},{"6":"小额"},{"61":"博升"},{"62":"游戏基地"}]
		Map<String, String> bankTypeMap = optionService.getBankTypeMap();
		//get bankInfo from cache
		List<BankInfo> list = HfCacheUtil.getCache().getBankList();
		merId = StringUtils.trim(merId);
		Map merBankMap = merBankService.findByMerId(merId);
		StringBuffer zTreeNodes = new StringBuffer("[");// 构造树节点

		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("ixData", merId + "-");
		mapWhere.put("tableName", "UMPAY.T_MER_BANK");
		Map audit = auditService.getMerBankAudit(mapWhere);
		for (Map.Entry<String, String> entry : upBankTypeMap.entrySet()) {
			String upbanktype = entry.getKey();// GM
			String dbcode = entry.getValue();// 62
			String upbanktypeNode = "{ id:'"+upbanktype+"', pId:'0', name:'"+bankTypeMap.get(dbcode)+"', isParent:true}"; 
			
			StringBuffer nodes = new StringBuffer(); // 节点内容
			for (BankInfo bankInfo : list) {
				String bankType = bankInfo.getBankType();
				StringBuffer node = new StringBuffer();
				if (bankType == dbcode || bankType.equals(dbcode)) {
					MerBank bank = (MerBank) merBankMap.get(bankInfo.getBankId());
					node.append("{ id:'").append(bankInfo.getBankId()).append("', pId:'"+upbanktype+"', name:'")
							.append(bankInfo.getBankName());
					if (!"2".equals(bankInfo.getState())) {
						node.append("(已禁用)");
					}
					node.append("'");
					if (bank == null) {
						Object o = audit.get(bankInfo.getBankId().trim()); // 查询是否存在
						if (o != null) {
							node.append(", chkDisabled:true");
						}
					} else {
						if (bank.getModLock() == 0) {// 未锁定
							if ("2".equals(bank.getState())) {
								node.append(", checked:true, chkDisabled:true");
							}
						} else {
							// 锁定则变灰不可改
							node.append(", chkDisabled:true");
							if ("2".equals(bank.getState())) {
								node.append(", checked:true");
							}
						}
					}
					node.append("}");
					nodes.append(",").append(node);
				} 
			}
			zTreeNodes.append(upbanktypeNode).append(nodes).append(",");
		}
		zTreeNodes = zTreeNodes.replace(zTreeNodes.length() - 1, zTreeNodes.length(), "]"); // 加两个父节点 再加节点内容
		
		return new ModelAndView("jsonView", "ajax_json", zTreeNodes.toString());
	}

	/**
	 * ******************************************** method name : oneMer
	 * description : 配置商户银行
	 * 
	 * @return : ModelAndView
	 * @param : @param merId
	 * @param : @param bankid
	 * @param : @return modified : xhf , 2012-11-13 下午05:52:54
	 *        *******************************************
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/update")
	public ModelAndView update(String merId, String bankid) {
		// 通过读取配置，获得Tree配置信息。{BS=61, XE=6, GM=62}
		Map<String, String> upBankTypeMap = optionService.getBankTypeMap("merbankType");
		List<String> newOpen = new ArrayList(); // 新增开通
		List<String> modOpen = new ArrayList(); // 修改开通
		List<String> modClose = new ArrayList(); // 修改关闭
		// 1-获取页面的商户号、小额银行串a
		String[] bankId = bankid.split(",");
		// 2-获取此商户号在库里的小额银行状态b和锁定状态
		Map<String, MerBank> banks = merBankService.findByMerId(merId);
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("ixData", StringUtils.trim(merId) + "-");
		mapWhere.put("tableName", "UMPAY.T_MER_BANK");
		Map audit = auditService.getMerBankAudit(mapWhere);
		for (int i = 0; i < bankId.length; i++) {
			if (upBankTypeMap.containsKey(bankId[i])) { // 若为根节点则直接下一次循环
				continue;
			}
			MerBank bank = (MerBank) banks.get(bankId[i]);
			if (bank == null) {
				// 3-1 从a中循环取出一个元素i，与b比较，若b中不含i，则为新增开通；
				Object o = audit.get(bankId[i]); // 查询是否存在
				if (o == null) {
					newOpen.add(bankId[i]);
				} else {
					log.error("银行号" + bankId[i] + "为的支付银行已送交审核，无法重复提交！");
					return new ModelAndView("jsonView", "ajax_json", "no");
				}
			} else {
				// 3-2 若b中含i,则再看锁定状态，若为0，则再看state，若为4，则为修改开通；若为2，则为修改关闭
				if (bank.getModLock() == 1) {
					log.error("银行号为" + bankId[i] + "的支付银行已经锁定！");
					return new ModelAndView("jsonView", "ajax_json", "no");
				} else {
					if ("2".equals(bank.getState())) {
						modClose.add(bankId[i]);
					} else if ("4".equals(bank.getState())) {
						modOpen.add(bankId[i]);
					}
				}
			}
		}
		String result = "no";
		MerBank merBank = new MerBank();
		merBank.setMerId(merId);
		merBank.setModUser(getUser().getId());
		merBank.setModLock(1);
		// 向审核表插入数据
		try {
			result = merBankService.saveMerBank(merBank, newOpen, modOpen, modClose);
		} catch (Exception e) {
			log.error("商户银行配置失败", e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}

	/**
	 * ******************************************** 
	 * method name : openBatchMer
	 * @Description : 跳转到批量配置商户银行页面
	 * @Description: 更改树显示
	 *               （由于之前是MW和XE两类银行，综合支付需要增加银行树。重构了此方法（by lizhiqiang.2014年10月23日））
	 * @return : String
	 * @param :  modeMap
	 * modified : xhf , 2012-11-13 下午05:53:42
	 * *******************************************
	 */
	@RequestMapping(value = "/tobatchupdate")
	public String toBatchUpdate(ModelMap modeMap) {
		// 通过读取配置，获得Tree配置信息。{BS=61, XE=6, GM=62}
		Map<String, String> upBankTypeMap = optionService
				.getBankTypeMap("merbankType");
		Map<String, String> bankTypeMap = optionService.getBankTypeMap();
		StringBuffer zTreeNodes = new StringBuffer("[");// 构造树节点
		for (Map.Entry<String, String> entry : upBankTypeMap.entrySet()) {
			String upbanktype = entry.getKey();// GM
			String dbcode = entry.getValue();// 62
			String upbanktypeNode = "{ id:'" + upbanktype
					+ "', pId:'0', name:'" + bankTypeMap.get(dbcode)
					+ "', isParent:true}";
			StringBuffer nodes = ZTreeUtil.getUPSubBankTree(dbcode, upbanktype);
			zTreeNodes.append(upbanktypeNode).append(nodes).append(",");
		}
		zTreeNodes = zTreeNodes.replace(zTreeNodes.length() - 1,
				zTreeNodes.length(), "]"); // 加父节点 再加节点内容
		modeMap.addAttribute("zNodes", zTreeNodes);
		return "merbank/batchMer";
	}

	/**
	 * ******************************************** method name : batchUpdate
	 * description : 批量配置商户银行
	 * 
	 * @return : ModelAndView
	 * @param : @param merId
	 * @param : @param state
	 * @param : @param bankId
	 * @param : @return modified : xuhuafeng , 2013-4-24 上午11:32:05
	 *        *******************************************
	 */
	@RequestMapping(value = "/batchupdate")
	public ModelAndView batchUpdate(String merId, String state, String bankId) {
		String result = "yes";
		String userId = getUser().getId();
		String[] merIds = merId.split(",");
		Set<String> merIdSet = new HashSet<String>();
		for (int i = 0; i < merIds.length; i++) {
			merIdSet.add(StringUtils.trim(merIds[i]));
		}
		String[] mersId = merIdSet.toArray(new String[0]);
		try {
			result = merBankService.batchUpdate(mersId, bankId, state, userId);
		} catch (Exception e) {
			result = "操作失败，请稍后再试";
			log.error(result, e);
		}
		return new ModelAndView("jsonView", "ajax_json", result);
	}

	/**
	 * ******************************************** method name : export
	 * description : 导出方法
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : xhf , 2012-11-13 下午05:54:20
	 *        *******************************************
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/export")
	public ModelAndView export() {
		Map map = this.getParametersFromRequest(super.getHttpRequest());
		Map<String, String> bankNameMap = HfCacheUtil.getCache().getBankNameMap();
		String queryKey = (String) map.get("queryKey");
		List<MerBank> listMerBank = new ArrayList<MerBank>();
		if (queryKey != null) {
			IUniQueryService service = (IUniQueryService) SpringContextUtil.getBean("uniQueryService");
			List<Map<String, Object>> data = service.query(queryKey, map);
			ObjectUtil.trimData(data);
			for (int i = 0; i < data.size(); i++) {
				ObjectUtil.trimData(data);
				MerBank merBank = new MerBank();
				merBank.setModTime((Timestamp) data.get(i).get("modTime"));
				merBank.setMerName((String) data.get(i).get("merName"));
				merBank.setMerId((String) data.get(i).get("merId"));
				String bankId = (String) data.get(i).get("bankId");
				String bankName = bankNameMap.get(bankId);
				merBank.setBankId(bankName + bankId);
				String merId = merBank.getMerId();
				merBank.setOperator(merOperService.getOperStrByMerId(merId));
				int state = (Integer) data.get(i).get("STATE");
				merBank.setState(state == 2 ? "启用" : "禁用");
				listMerBank.add(merBank);
			}
		}
		List<Object> l = new ArrayList<Object>();
		Map<String, Object> map2 = new HashMap();
		map2.put("merBank", listMerBank);
		l.add(listMerBank);
		return new ModelAndView("excelViewMerBank", "excel", map2);
	}

	/**
	 * @Title: synMwMerBank
	 * @Description: 同步商户梦网银行
	 *               （由于之前商户银行管理只针对小额银行，工单：PKS00005782需增加对商户梦网银行管理，专门增加一个同步方法
	 *               ：针对老商户默认全部开通启用的梦网银行。）
	 * @return
	 * @author wanyong
	 * @date 2014-8-14 下午2:23:43
	 */
	@RequestMapping(value = "/synmwmerbank")
	public ModelAndView synMwMerBank() {
		String result = "操作失败"; // 操作失败
		if ("admin".equals(getUser().getLoginName())) {
			// 查询所有商户信息
			MerInfoService merInfoService1 = (MerInfoService) SpringContextUtil.getBean("merInfoServiceImpl");
			List<MerInfo> merInfoList = merInfoService1.loadAll();
			// 查询所有梦网银行
			BankService bankService1 = (BankService) SpringContextUtil.getBean("bankServiceImpl");
			List<BankInfo> bankInfoList = bankService1.getBankInfos();
			// 新增商户梦网银行
			MerBankService merBankService1 = (MerBankService) SpringContextUtil.getBean("merBankServiceImpl");
			MerBank merBank = new MerBank();
			for (MerInfo merInfo : merInfoList) {
				for (BankInfo bankInfo : bankInfoList) {
					bankInfo.trim();
					if ("3".equals(bankInfo.getBankType())) { // 梦网银行
						merBank.setMerId(merInfo.getMerId());
						merBank.setBankId(bankInfo.getBankId());
						merBank.setState("2"); // 启用
						merBankService1.saveMerBankForSynMwMerBank(merBank);
					}
				}
			}
			result = "操作成功"; // 操作成功
		} else {
			result = "没有权限执行此操作，请联系管理员！";
		}

		return new ModelAndView("jsonView", "ajax_json", result);
	}

}
