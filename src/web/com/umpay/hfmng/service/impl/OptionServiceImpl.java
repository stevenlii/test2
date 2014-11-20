package com.umpay.hfmng.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.JsonHFUtil;
import com.umpay.hfmng.dao.OptionDao;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.GoodsTypeModel;
import com.umpay.hfmng.service.MessageService;
import com.umpay.hfmng.service.OptionService;

@Service
public class OptionServiceImpl implements OptionService {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MessageService messageService;
	@Autowired
	private OptionDao optionDao;
	
	/**
	 * @Title: getGoodsList
	 * @Description: 根据商户ID获取商品ID及名称列表
	 * @param
	 * @param merId
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午09:53:53
	 */
	public List<GoodsInfo> getGoodsList(Map<String, String> merId) {
		return optionDao.getGoodsList(merId);
	}

	public List<GoodsTypeModel> getGoodsType() {

		List<GoodsTypeModel> data = HfCacheUtil.getCache().getGoodsTypeList();
		return data;
	}
/**
 * ********************************************
 * method name   : getGoodsCategoryMap 获取商品分类集合
 * modified      : LiZhen ,  2013-5-3
 * @see          : @see com.umpay.hfmng.service.OptionService#getGoodsCategoryMap()
 * *******************************************
 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getGoodsCategoryMap() {
		Map<String,String> gCMap=new HashMap<String, String>();
		try {
			String goodsCategory = messageService.getMessage("goodsCategory");
			gCMap=JsonHFUtil.getMapFromJsonArrStr(goodsCategory);
		} catch (Exception e) {
			log.info("获取商品分类集合失败", e);
		}
		return gCMap;
	}
/**
 * ********************************************
 * method name   : getMerCategoryMap 获取商户分类集合
 * modified      : LiZhen ,  2013-5-3
 * @see          : @see com.umpay.hfmng.service.OptionService#getMerCategoryMap()
 * *******************************************
 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getMerCategoryMap() {
		Map<String,String> mCMap=new HashMap<String, String>();
		try {
			String merCategory = messageService.getMessage("merType");
			mCMap=JsonHFUtil.getMapFromJsonArrStr(merCategory);
		} catch (Exception e) {
			log.info("获取商户分类集合失败", e);
		}
		return mCMap;
	}

	public List<?> getAllUserlist() {
		// app=通信账户业管平台,app=应用系统,o=联动优势
		String systemDN = messageService.getSystemParam("System.DN");
		log.debug("System DN:" + systemDN);
		return HfCacheUtil.getCache().getUserList();
	}

	public List<?> getModUserlist() {
		// 审核人列表默认所有用户
		return getAllUserlist();
	}

	public List<?> getCreatorlist() {
		// 提交人列表默认所有用户
		return getAllUserlist();
	}

	public Map<String, String> getAllUserIdAndName() {
		List<?> userList = getAllUserlist();
		Map<String, String> map = new HashMap<String, String>();
		if (userList != null && userList.size() > 0) {
			for (Object object : userList) {
				Map m = (Map) object;
				if (m.get("id") != null && m.get("userName") != null)
					map.put(StringUtils.trim(m.get("id").toString()),
							StringUtils.trim(m.get("userName").toString()));
			}
		}
		return map;
	}

	public String getMerBankIdList() {
		String merBank = messageService.getMessage("defaultOpenBankId");
		return merBank;
	}

	/**
	 * ******************************************** method name : getFeeCodeMap
	 * modified : zhaojunbao , 2012-11-8
	 * 
	 * @see : @see com.umpay.hfmng.service.OptionService#getFeeCodeMap() *
	 *******************************************/
	@SuppressWarnings("unchecked")
	public Map<String, String> getFeeCodeCategoryMap() {
		String feeCodeCategory = messageService.getMessage("feeCodeCategory");
		List list = JsonHFUtil
				.getListFromJsonArrStr(feeCodeCategory, Map.class);
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			map.putAll((Map) list.get(i));
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getBankTypeMap() {
		String bankType = messageService.getMessage("bankType");
		List list = JsonHFUtil.getListFromJsonArrStr(bankType, Map.class);
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			map.putAll((Map) list.get(i));
		}
		return map;
	}
	public Map<String, String> getBankTypeMap(String bankTypeMessage) {
		String bankType = messageService.getMessage(bankTypeMessage);
		List<?> list = JsonHFUtil.getListFromJsonArrStr(bankType, Map.class);
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			map.putAll((Map<String, String>) list.get(i));
		}
		return map;
	}
	
	public Map<String, String> getBusinessTypeMap() {
		String businessType = messageService.getMessage("businessType");
		List list = JsonHFUtil.getListFromJsonArrStr(businessType, Map.class);
		Map map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			map.putAll((Map) list.get(i));
		}
		return map;
	}
	
	public Map<String, String> getMerBizTypeMap() {
		Map<String, String> bizType = new HashMap<String, String>();
		String merBizType = messageService.getMessage("merBizType");
		if(merBizType != null){
			bizType = JsonHFUtil.getMapFromJsonArrStr(merBizType);
		}
		return bizType;
	}

	/** ********************************************
	 * method name   : getMerBusiTypeMap 
	 * modified      : xuhuafeng ,  2014-3-11
	 * @see          : @see com.umpay.hfmng.service.OptionService#getMerBusiTypeMap()
	 * ********************************************/     
	public Map<String, String> getMerBusiTypeMap() {
		Map<String, String> bizType = new HashMap<String, String>();
		String merBizType = messageService.getMessage("merBusiType");
		if(merBizType != null){
			bizType = JsonHFUtil.getMapFromJsonArrStr(merBizType);
		}
		return bizType;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getGateMap() {
		Map<String,String> mCMap=new HashMap<String, String>();
		try {
			String gateList = messageService.getMessage("gateList");
			mCMap=JsonHFUtil.getMapFromJsonArrStr(gateList);
		} catch (Exception e) {
			log.info("获取综合支付通道集合失败", e);
		}
		return mCMap;
	}
}
