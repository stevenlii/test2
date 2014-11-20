package com.umpay.hfmng.service;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.hfmng.model.GoodsTypeModel;

public interface OptionService {

	/**
	 * @Title: getGoodsList
	 * @Description: 根据商户ID获取商品ID及名称列表接口
	 * @param
	 * @param merId
	 * @return
	 * @author wanyong
	 * @date 2012-12-18 下午09:52:41
	 */
	public List<GoodsInfo> getGoodsList(Map<String, String> merId);

	public List<GoodsTypeModel> getGoodsType();

	public Map<String, String> getGoodsCategoryMap();

	// public List<MerTypeModel> getMerType();

	public Map<String, String> getMerCategoryMap();

	/**
	 * ******************************************** method name : getAllUserlist
	 * description : 获取本平台的所有用户
	 * 
	 * @return : List
	 * @param : @return modified : lz , 2012-9-10 下午05:12:39
	 * @see : *******************************************
	 */
	public List<?> getAllUserlist();

	/**
	 * ******************************************** method name : getModUserlist
	 * description : 获取审核人列表
	 * 
	 * @return : List
	 * @param : @return modified : lz , 2012-9-10 下午05:12:58
	 * @see : *******************************************
	 */
	public List<?> getModUserlist();

	/**
	 * ******************************************** method name : getCreatorlist
	 * description : 获取提交人列表
	 * 
	 * @return : List
	 * @param : @return modified : lz , 2012-9-10 下午05:13:13
	 * @see : *******************************************
	 */
	public List<?> getCreatorlist();

	/**
	 * ******************************************** method name :
	 * getAllUserIdAndName description : 获取所有用户的id和用户名，key为id，value为用户名
	 * 
	 * @return : Map
	 * @param : @return modified : lz , 2012-9-10 下午05:13:42
	 * @see : *******************************************
	 */
	public Map<String, String> getAllUserIdAndName();

	public String getMerBankIdList();

	/**
	 * ******************************************** method name : getFeeCodeMap
	 * description : 获取配置文件中计费代码的分类
	 * 
	 * @return : Map<String,String>
	 * @param : @return modified : zhaojunbao , 2012-11-8 上午11:52:47
	 * @see : *******************************************
	 */
	public Map<String, String> getFeeCodeCategoryMap();

	public Map<String, String> getBankTypeMap();
	/**
	 * *****************  方法说明  *****************
	 * method name   :  getBankTypeMap
	 * @param		 :  @param bankTypeMessage 配置文件参数
	 * @param		 :  @return
	 * @return		 :  Map<String,String>
	 * @author       :  lizhiqiang 2014年10月23日 下午2:47:07
	 * description   :  
	 * @see          :  
	 * **********************************************
	 */
	public Map<String, String> getBankTypeMap(String bankTypeMessage);
	
	public Map<String, String> getBusinessTypeMap();
	
	public Map<String, String> getMerBizTypeMap();
	
	public Map<String, String> getMerBusiTypeMap();
	/**
	 * ********************************************
	 * method name   : getGateMap 
	 * description   : 综合支付通道map
	 * @return       : Map<String,String>
	 * @param        : @return
	 * modified      : xuhuafeng ,  2014-9-26  下午03:44:05
	 * *******************************************
	 */
	public Map<String, String> getGateMap();
}
