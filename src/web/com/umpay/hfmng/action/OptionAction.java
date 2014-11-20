package com.umpay.hfmng.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.PinYinUtil;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.ChannelInf;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.model.CouponInf;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.GoodsTypeModel;
import com.umpay.hfmng.model.MerInfo;
import com.umpay.hfmng.model.SecMerInf;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;
import com.umpay.sso.org.User;

/**
 * ****************** 类说明 ********************* class : OptionAction
 * 
 * @author : Administrator
 * @version : 1.0 description : 公用方法类，供前台页面动态获取数据
 * @see : ***********************************************
 */
@Controller
@RequestMapping("/option")
public class OptionAction extends BaseAction {
	@Autowired
	private MessageService messageService;
	@Autowired
	private OptionService optionService;

	/**
	 * ******************************************** method name : getMerList
	 * description : 获取商户列表
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : Administrator , 2012-8-14 下午04:02:52
	 * @see : *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/merlist")
	public ModelAndView getMerList() {
		JSONArray result = new JSONArray();
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Iterator it = merMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			String value = ((MerInfo) entry.getValue()).getMerName().toString();
			result.add(buildSelect(key.trim(), key.trim() + "-" + value.trim()));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name :
	 * getOperatorList description : 获取运营人员列表
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : lizhen , 2012-8-29 下午01:55:16
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/operatorlist")
	public ModelAndView getOperatorList() {
		/*
		 * String operatorRole=messageService.getSystemParam("OperatorRole");
		 * //查找运营人员角色 List roleList=null; JSONArray result=new JSONArray();
		 * result.add(buildSelect("", "全部")); if(operatorRole!=null &&
		 * !operatorRole.equals("")){
		 * roleList=WsRoleClient.findByRoleName(operatorRole); }
		 * if(roleList!=null && roleList.size()>0){ Map
		 * mapRole=(Map)(roleList.get(0)); String
		 * id=mapRole.get("id").toString(); // String
		 * id="8a8aa656396bd50201396bd5020b0000"; //根据角色id查找用户 List
		 * operUserList=WsUserClient.findByRoleId(id);
		 */
		JSONArray result = new JSONArray();
		// result.add(buildSelect("", "全部"));
		List operUserList = HfCacheUtil.getCache().getOperRoleList(); // 缓存获取运营人员信息
		if (operUserList != null && operUserList.size() > 0) {
			for (Object object : operUserList) {
				Map m = (Map) object;
				result.add(buildSelect(StringUtils.trim(m.get("id").toString()), m.get("userName").toString()));
			}
		}
		// }
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name : getCreatorlist
	 * description : 获取提交人列表
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : lizhen , 2012-8-15 下午02:05:32
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/creatorlist")
	public ModelAndView getCreatorlist() {
		// TODO
		// 获取拥有该平台权限的所有用户
		JSONArray result = new JSONArray();
		// result.add(buildSelect("", "全部"));
		List userList = HfCacheUtil.getCache().getCreatorList(); // 缓存获取创建人员列表
		// zhao
		// List userList=optionService.getCreatorlist();
		if (userList != null && userList.size() > 0) {
			for (Object object : userList) {
				Map m = (Map) object;
				result.add(buildSelect(m.get("id").toString(), m.get("userName").toString()));
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name :
	 * getOperatorList2 description : 商户评分，根据用户角色筛选下拉列表
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : xuhuafeng , 2013-2-21 下午08:09:07
	 *        *******************************************
	 */
	@RequestMapping(value = "/operatorlist2")
	public ModelAndView getOperatorList2() {
		JSONArray result = new JSONArray();
		User user = getUser();
		List<?> roleList = HfCacheUtil.getCache().getRolesByUser(user.getId());
		if (roleList != null && roleList.size() == 1) { // 只拥有一个角色
			String operatorRole = messageService.getSystemParam("OperatorRole"); // 运营人员角色
			Map m = (Map) roleList.get(0);
			Object roleName = m.get("roleName");
			if (roleName != null && operatorRole != "" && StringUtils.trim(roleName.toString()).equals(operatorRole)) {
				result.add(buildSelect(user.getId(), user.getName()));
				return new ModelAndView("jsonView", "ajax_json", result.toString());
			}
		}
		List operUserList = HfCacheUtil.getCache().getOperRoleList(); // 缓存获取运营人员信息
		if (operUserList != null && operUserList.size() > 0) {
			for (Object object : operUserList) {
				Map m = (Map) object;
				result.add(buildSelect(m.get("id").toString(), m.get("userName").toString()));
			}
		}

		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ***************** 方法说明 ***************** method name : getOperatorList3
	 * 
	 * @param : @return
	 * @return : ModelAndView
	 * @author : LiZhen 2013-11-14 上午9:47:36 description :
	 *         可把所有用户作为运营负责人，增加拼音首字母检索
	 * @see : **********************************************
	 */
	@RequestMapping(value = "/operatorlist3")
	public ModelAndView getOperatorList3() {
		JSONArray result = new JSONArray();
		Map<String, Object> allUserMap = HfCacheUtil.getCache().getUserInfoMap();
		if (allUserMap != null && allUserMap.size() > 0) {
			for (Map.Entry<String, Object> entry : allUserMap.entrySet()) {
				@SuppressWarnings("unchecked")
				Map<String, Object> m = (Map<String, Object>) entry.getValue();
				List<String> initialList = PinYinUtil.getFirstSpellList(m.get("userName").toString());
				String initials = StringUtils.join(initialList.toArray(), ",");
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", m.get("id").toString());
				jsonObj.put("name", m.get("userName").toString());
				jsonObj.put("initials", initials);
				result.add(jsonObj);
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name : getModUserlist
	 * description : 获取审核人列表
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : lizhen , 2012-8-15 下午02:05:32
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/modUserlist")
	public ModelAndView getModUserlist() {
		// TODO
		// 获取拥有该平台权限的所有用户
		JSONArray result = new JSONArray();
		// result.add(buildSelect("", "全部"));
		List userList = optionService.getModUserlist();
		if (userList != null && userList.size() > 0) {
			for (Object object : userList) {
				Map m = (Map) object;
				if (m.get("id") != null && m.get("userName") != null)
					result.add(buildSelect(m.get("id").toString(), m.get("userName").toString()));
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name : getGoodsType
	 * description : 前台页面动态获取商品类型方法（风控类型）
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : Administrator , 2012-8-14 下午05:13:54
	 * @throws Exception
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/goodstype")
	public ModelAndView getGoodsType() throws Exception {
		List<GoodsTypeModel> data = HfCacheUtil.getCache().getGoodsTypeList();
		JSONArray result = new JSONArray();
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				GoodsTypeModel goodsType = data.get(i);
				if (goodsType != null) {
					goodsType.trim();
				}
				result.add(buildSelect(goodsType.getGoodsType(), goodsType.getDetail()));
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name : getMerType
	 * description : 配置文件中获取商户类型
	 * 
	 * @return : ModelAndView
	 * @param : @return
	 * @param : @throws Exception modified : zhaojunbao , 2012-8-31 上午10:30:31
	 * @see : *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/mercategory")
	public ModelAndView getMerType() throws Exception {
		JSONArray result = new JSONArray();
		String merType = messageService.getMessage("merType");
		List list = JsonHFUtil.getListFromJsonArrStr(merType, Map.class);
		for (int i = 0; i < list.size(); i++) {
			// list中的字符串形如 {001=软件}
			String goodsStr = list.get(i).toString().trim();
			int index = goodsStr.indexOf("=");
			String key = null;
			String value = null;
			if (index > 1) {
				key = goodsStr.substring(1, index);
				value = goodsStr.substring(index + 1, goodsStr.length() - 1);
				result.add(buildSelect(key, value));
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name : buildSelect
	 * description : 构造select中得text和value键值对
	 * 
	 * @return : JSONObject
	 * @param : @param id
	 * @param : @param name
	 * @param : @return modified : Administrator , 2012-8-14 下午04:48:38
	 * @see : *******************************************
	 */
	public static JSONObject buildSelect(String id, String name) {
		JSONObject result = new JSONObject();
		result.put("value", id);
		result.put("text", name);
		return result;
	}

	public static JSONObject buildTreeSelect(String id, String pid, String name) {
		StringBuffer sb = new StringBuffer();
		JSONObject result = new JSONObject();
		result.put("text", name);
		result.put("pid", pid);
		result.put("id", id);
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/meridlist")
	public ModelAndView getMerIdList() {
		JSONArray result = new JSONArray();
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Iterator it = merMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			result.add(buildSelect(key.trim(), key.trim()));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name : getBank
	 * description : 缓存获取银行列表信息
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : zhaojunbao , 2012-10-8 下午01:36:37
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/banklist")
	public ModelAndView getBank() {
		JSONArray result = new JSONArray();
		Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap();
		Iterator it = bankMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			String value = ((BankInfo) entry.getValue()).getBankName().toString();
			result.add(buildSelect(key.trim(), key.trim() + "-" + value.trim()));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	@RequestMapping(value = "/xebanklist")
	public ModelAndView getXeBank() {
		JSONArray result = new JSONArray();
		// result.add(buildSelect("", "全部"));
		Map<String, Object> bankMap = HfCacheUtil.getCache().getBankInfoMap();
		Iterator it = bankMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String bankType = ((BankInfo) entry.getValue()).getBankType();
			if ("6".equals(bankType)) {
				String key = entry.getKey().toString();
				String value = ((BankInfo) entry.getValue()).getBankName().toString();
				result.add(buildSelect(key.trim(), key.trim() + "-" + value.trim()));
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * @Title: getSharedRateXeBank
	 * @Description: 专门为添加商户时分成比例字段操作的小额支付银行列表
	 * @return
	 * @author wanyong
	 * @date 2014-7-21 下午9:09:16
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/shareratexebanklist")
	public ModelAndView getSharedRateXeBank() {
		JSONArray result = new JSONArray();
		Map<String, Object> bankMap = HfCacheUtil.getCache().getXeBankInfoMap();
		Iterator it = bankMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String bankType = ((BankInfo) entry.getValue()).getBankType();
			if ("6".equals(bankType)) {
				String key = entry.getKey().toString().trim();
				String value = ((BankInfo) entry.getValue()).getBankName().toString().trim();
				if (value.indexOf("小额") == -1) {
					result.add(buildSelect(value, key + "-" + value));
				} else {
					String perFixName = value.substring(0, value.indexOf("小额"));
					result.add(buildSelect(perFixName, key + "-" + value));
				}
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name : getGoodsInfo
	 * description : 缓存获取商品信息
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : zhaojunbao , 2012-10-8 下午01:37:23
	 * @see : *******************************************
	 */
	@RequestMapping(value = "/goodslist")
	public ModelAndView getGoodsInfo() {

		JSONArray result = new JSONArray();
		// result.add(buildSelect("", "全部"));
		Map<String, Object> goodsMap = HfCacheUtil.getCache().getGoodsInfoMap();
		Iterator it = goodsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			String[] arStrings = key.split("-");
			String goodsId = arStrings[1];
			String value = ((GoodsInfo) entry.getValue()).getGoodsName().toString();
			result.add(buildSelect(goodsId, goodsId + "-" + value.trim()));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name : getFeeCategory
	 * description : 获取计费代码分类方法
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : zhaojunbao , 2012-11-6 下午06:26:30
	 * @see : *******************************************
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/feecategory")
	public ModelAndView getFeeCategory() {
		JSONArray result = new JSONArray();
		String merType = messageService.getMessage("feeCodeCategory");
		List list = JsonHFUtil.getListFromJsonArrStr(merType, Map.class);
		for (int i = 0; i < list.size(); i++) {
			// list中的字符串形如 {001=软件}
			String goodsStr = list.get(i).toString().trim();
			int index = goodsStr.indexOf("=");
			String key = null;
			String value = null;
			if (index > 1) {
				key = goodsStr.substring(1, index);
				value = goodsStr.substring(index + 1, goodsStr.length() - 1);
				result.add(buildSelect(key, value));
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/feecategory2")
	public ModelAndView getFeeCategory2() {
		JSONArray result = new JSONArray();
		String merType = messageService.getMessage("feeCodeCategory");
		List list = JsonHFUtil.getListFromJsonArrStr(merType, Map.class);
		Map merTypeMap = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			merTypeMap.putAll((Map) list.get(i));
		}
		Iterator it = merTypeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString().trim();
			String value = entry.getValue().toString().trim();
			result.add(buildSelect(key, value));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/banktype")
	public ModelAndView getBankType() {
		JSONArray result = new JSONArray();
		String bankType = messageService.getMessage("bankType");
		List list = JsonHFUtil.getListFromJsonArrStr(bankType, Map.class);
		Map bankTypeMap = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			bankTypeMap.putAll((Map) list.get(i));
		}
		Iterator it = bankTypeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString().trim();
			String value = entry.getValue().toString().trim();
			result.add(buildSelect(key, value));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/upbanktype")
	public ModelAndView getUPBankType() {
		JSONArray result = new JSONArray();
		String bankType = messageService.getMessage("upbankType");
		List list = JsonHFUtil.getListFromJsonArrStr(bankType, Map.class);
		Map bankTypeMap = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			bankTypeMap.putAll((Map) list.get(i));
		}
		Iterator it = bankTypeMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString().trim();
			String value = entry.getValue().toString().trim();
			result.add(buildSelect(key, value));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}
	/**
	 * @Title: getCouponList
	 * @Description: 获取兑换券编号及名称列表
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午07:27:13
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/couponinflist")
	public ModelAndView getCouponInfList() {
		JSONArray result = new JSONArray();
		Map<String, Object> couponInfMap = HfCacheUtil.getCache().getCouponInfMap();
		Iterator it = couponInfMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			String value = ((CouponInf) entry.getValue()).getCouponName().toString();
			result.add(buildSelect(key.trim(), value.trim()));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * @Title: getCouponMerInfListByChannel
	 * @Description: 电子兑换券功能根据渠道获取商户缓存数据（过滤掉未启用的商户）
	 * @param
	 * @return
	 * @author panyouliang
	 * @date 2013-3-26
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/channelmerinflist")
	public ModelAndView getCouponMerInfListByChannel() {
		JSONArray result = new JSONArray();
		Map params = this.getParametersFromRequest(getHttpRequest());
		String channelId = (String) params.get("channelId");
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMapByChannel(channelId);
		Iterator it = merMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if ("2".equals(((MerInfo) entry.getValue()).getState())) {
				// 只需要开通的商户
				String key = entry.getKey().toString();
				String value = ((MerInfo) entry.getValue()).getMerName().toString();
				result.add(buildSelect(key.trim(), key.trim() + "-" + value.trim()));
			}

		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * @Title: getCouponMerInfListByChannel
	 * @Description: 电子兑换券功能根据渠道获取商户缓存数据（过滤掉未启用的商户）
	 * @param
	 * @return
	 * @author panyouliang
	 * @date 2013-3-26
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/channelmergoodsinflist")
	public ModelAndView getGoodsInfListByChannel() {
		JSONArray result = new JSONArray();
		Map params = this.getParametersFromRequest(getHttpRequest());
		String channelId = (String) params.get("channelId");
		String merId = (String) params.get("merId");
		Map<String, Object> merMap = HfCacheUtil.getCache().getGoodsInfoMapByChannel(channelId, merId);
		Iterator it = merMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			String value = ((GoodsInfo) entry.getValue()).getGoodsName().toString();
			result.add(buildSelect(key.trim(), key.trim() + "-" + value.trim()));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * @Title: getCouponMerInfList
	 * @Description: 获取电子兑换券功能商户缓存数据（过滤掉未启用的商户）
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-1-10 下午05:13:28
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/couponmerinflist")
	public ModelAndView getCouponMerInfList() {
		JSONArray result = new JSONArray();
		Map<String, Object> merMap = HfCacheUtil.getCache().getMerInfoMap();
		Iterator it = merMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if ("2".equals(((MerInfo) entry.getValue()).getState())) {
				// 只需要开通的商户
				String key = entry.getKey().toString();
				String value = ((MerInfo) entry.getValue()).getMerName().toString();
				result.add(buildSelect(key.trim(), key.trim() + "-" + value.trim()));
			}

		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * @Title: getChannelInfList
	 * @Description: 获取兑换券渠道下拉列表缓存数据（过滤掉未启用的商户）
	 * @param
	 * @return
	 * @author wanyong
	 * @date 2013-1-10 下午05:13:28
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/channelinflist")
	public ModelAndView getChannelInfList() {
		JSONArray result = new JSONArray();
		Map<String, Object> channelMap = HfCacheUtil.getCache().getChannelInfMap();
		Iterator it = channelMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString();
			String value = ((ChannelInf) entry.getValue()).getChannelName();
			result.add(buildSelect(key.trim(), key.trim() + "-" + value.trim()));

		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/channellist")
	public ModelAndView getChannellist() {
		JSONArray result = new JSONArray();
		Map<String, Object> channelMap = HfCacheUtil.getCache().getChannelInfMap();
		Iterator it = channelMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString().trim();
			String value = ((ChannelInf) entry.getValue()).getChannelName().trim();
			result.add(buildSelect(key + "-" + value, key + "-" + value));

		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * 
	 * @Title: getCouponGoodsInfList
	 * @Description: 根据商户ID获取商品ID及名称列表（过滤掉未启用的商品）
	 * @param
	 * @param merId
	 * @return
	 * @author wanyong
	 * @date 2013-1-10 下午05:23:08
	 */
	@RequestMapping(value = "/getcoupongoodsinflist")
	public ModelAndView getCouponGoodsInfList(String merId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("merId", merId);
		List<GoodsInfo> list = optionService.getGoodsList(map);
		JSONArray result = new JSONArray();
		if (list != null && list.size() > 0) {
			for (GoodsInfo goodsInfo : list) {
				if ("2".equals(goodsInfo.getState())) {
					// 只需要启用的商品
					result.add(buildSelect(goodsInfo.getGoodsId(),
							goodsInfo.getGoodsId() + "-" + goodsInfo.getGoodsName()));
				}
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * @Title: getDropDownList
	 * @Description: 根据参数类型获取参数表下拉列表数据
	 * @param
	 * @param paraType
	 * @return
	 * @author wanyong
	 * @date 2013-1-11 下午03:41:31
	 */
	@RequestMapping(value = "/dropdownlist")
	public ModelAndView getDropDownList(String paraType) {
		JSONArray result = new JSONArray();
		Map<String, String> map = new HashMap<String, String>();
		if ("couponInfStates".equals(paraType)) {
			map = ParameterPool.couponInfStates;
		} else if ("couponInfStates_MODIFY".equals(paraType)) { // 特殊兑换券状态，去除【待启用】状态，专门提供给兑换券修改页面使用
			map.putAll(ParameterPool.couponInfStates); // 这里不能使用等号，使用等号说明两个map指针都指向同一个内存块，修改其中一个另外一个也改变
			map.remove(Const.COUPON_INFSTATE_INIT + "");
		} else if ("couponInfTypes".equals(paraType)) {
			map = ParameterPool.couponInfTypes;
		} else if ("couponRuleStates".equals(paraType)) {
			map = ParameterPool.couponRuleStates;
		} else if ("couponRuleStates_MODIFY".equals(paraType)) { // 特殊兑换券规则状态，去除【待启用】状态，专门提供给兑换券规则修改页面使用
			map.putAll(ParameterPool.couponRuleStates); // 这里不能使用等号，使用等号说明两个map指针都指向同一个内存块，修改其中一个另外一个也改变
			map.remove(Const.COUPON_RULESTATE_NOENABLE + "");
		} else if ("couponBatchStates".equals(paraType)) {
			map = ParameterPool.couponBatchStates;
		} else if ("couponCodeStates".equals(paraType)) {
			map = ParameterPool.couponCodeStates;
		} else if ("auditStates".equals(paraType)) {
			map = ParameterPool.auditStates;
		} else if ("couponCodeMethod".equals(paraType)) {
			map = ParameterPool.couponCodeMethod;
		} else if ("couponCodeEffTypes".equals(paraType)) {
			map = ParameterPool.couponCodeEffTypes;
		} else if ("couponCodeTypes".equals(paraType)) {
			map = ParameterPool.couponCodeTypes;
		} else if ("couponLogOptTypes".equals(paraType)) {
			map = ParameterPool.couponLogOptTypes;
		} else if ("couponOrderStates".equals(paraType)) {
			map = ParameterPool.couponOrderStates;
		} else if ("couponTransStates".equals(paraType)) {
			map = ParameterPool.couponTransStates;
		} else if ("timerTaskStates".equals(paraType)) {
			map = ParameterPool.timerTaskStates;
		} else if ("platNames".equals(paraType)) {
			map = ParameterPool.platNames;
		} else if ("couponCodeExchangeTypes".equals(paraType)) {
			map = ParameterPool.couponCodeExchangeTypes;
		} else if ("couponCodeExchangeTypes_MERTEL".equals(paraType)) { // 特殊兑换方式，去除【自服务平台兑换】、【渠道兑换】方式，专门提供给商户电话管理功能使用
			map.putAll(ParameterPool.couponCodeExchangeTypes); // 这里不能使用等号，使用等号说明两个map指针都指向同一个内存块，修改其中一个另外一个也改变
			map.remove("1"); // 移除【自服务平台兑换】方式
			map.remove("4"); // 移除【渠道兑换】方式
		} else if ("couponMerTelStates".equals(paraType)) {
			map = ParameterPool.couponMerTelStates;
		} else if ("stlStates".equals(paraType)) {
			map = ParameterPool.stlStates;
		} else if ("stlCycles".equals(paraType)) {
			map = ParameterPool.stlCycles;
		}
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = entry.getKey().toString().trim();
			String value = entry.getValue().toString().trim();
			result.add(buildSelect(key, value));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name :
	 * getOpenChnlList description : 获取启用状态的渠道编号列表
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : xuhuafeng , 2013-3-19 上午09:40:00
	 *        *******************************************
	 */
	@RequestMapping(value = "/getopenchnlidlist")
	public ModelAndView getOpenChnlIdList() {
		JSONArray result = new JSONArray();
		Map<String, ChnlInf> chnlInfMap = HfCacheUtil.getCache().getChnlInfMap();
		if (chnlInfMap == null) {
			log.error("缓存中获取渠道信息失败");
		} else {
			Iterator it = chnlInfMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = entry.getKey().toString();
				ChnlInf chnlInf = (ChnlInf) entry.getValue();
				if (chnlInf.getState() == 2) {
					result.add(buildSelect(key, key));
				}
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	/**
	 * ******************************************** method name : getChnlIdList
	 * description : 获取渠道编号列表
	 * 
	 * @return : ModelAndView
	 * @param : @return modified : xuhuafeng , 2013-3-27 下午04:42:13
	 *        *******************************************
	 */
	@RequestMapping(value = "/getchnlidlist")
	public ModelAndView getChnlIdList() {
		JSONArray result = new JSONArray();
		Map<String, ChnlInf> chnlInfMap = HfCacheUtil.getCache().getChnlInfMap();
		if (chnlInfMap == null) {
			log.error("缓存中获取渠道信息失败");
		} else {
			Iterator it = chnlInfMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = entry.getKey().toString();
				ChnlInf chnlInf = (ChnlInf) entry.getValue();
				if (chnlInf.getState() == 2) {
					result.add(buildSelect(key, chnlInf.getChannelName().trim()));
				}
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	@RequestMapping(value = "/submeridlist")
	public ModelAndView getSubMerIdList() {
		JSONArray result = new JSONArray();
		Map<String, SecMerInf> merMap = HfCacheUtil.getCache().getSecMerMap();
		for (Entry<String, SecMerInf> entry : merMap.entrySet()) {
			String key = entry.getKey();
			result.add(buildSelect(key, key));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	@RequestMapping(value = "/enablesubmeridlist")
	public ModelAndView getEnableSubMerIdList() {
		JSONArray result = new JSONArray();
		Map<String, SecMerInf> merMap = HfCacheUtil.getCache().getSecMerMap();
		for (Entry<String, SecMerInf> entry : merMap.entrySet()) {
			String key = entry.getKey();
			SecMerInf smi = entry.getValue();
			if (smi.getState() != 4) {
				result.add(buildSelect(key, key));
			}
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	@RequestMapping(value = "/businessTypeList")
	public ModelAndView getBusinessTypeList() {
		JSONArray result = new JSONArray();
		Map<String, String> businessTypeMap = optionService.getBusinessTypeMap();
		for (Entry<String, String> entry : businessTypeMap.entrySet()) {
			result.add(buildSelect(entry.getKey(), entry.getValue()));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}

	@RequestMapping(value = "/merBizType")
	public ModelAndView getMerBizType() {
		JSONArray result = new JSONArray();
		Map<String, String> merBizTypeMap = optionService.getMerBizTypeMap();
		for (Entry<String, String> entry : merBizTypeMap.entrySet()) {
			result.add(buildSelect(entry.getKey(), entry.getValue()));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}
	
	@RequestMapping(value = "/gateList")
	public ModelAndView getGateList() {
		JSONArray result = new JSONArray();
		Map<String, String> gateMap = optionService.getGateMap();
		for (Entry<String, String> entry : gateMap.entrySet()) {
			result.add(buildSelect(entry.getKey(), entry.getValue()));
		}
		return new ModelAndView("jsonView", "ajax_json", result.toString());
	}
}
