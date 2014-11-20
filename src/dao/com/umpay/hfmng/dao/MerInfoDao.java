package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.MerInfo;

public interface MerInfoDao extends EntityDao<MerInfo> {

	/**
	 * ******************************************** method name : get
	 * description : 根据主键获取商户
	 * 
	 * @return : MerInfo
	 * @param : @param id
	 * @param : @return modified : xhf , 2012-11-22 下午06:31:12
	 *        *******************************************
	 */
	public MerInfo get(String id);

	/**
	 * ******************************************** method name : saveMerInfo
	 * description : 新增数据到t_mer_inf表
	 * 
	 * @return : void
	 * @param : @param merInfo modified : xhf , 2012-9-4 上午10:47:44
	 *        *******************************************
	 */
	public void saveMerInfo(MerInfo merInfo);

	/**
	 * ******************************************** method name : saveMerExp
	 * description : 新增数据到t_mer_exp表
	 * 
	 * @return : void
	 * @param : @param merInfo modified : xhf , 2012-9-4 上午10:48:37
	 *        *******************************************
	 */
	public void saveMerExp(MerInfo merInfo);

	/**
	 * @Title: saveMerExpAttr
	 * @Description: 新增数据到T_HFMER_EXP_ATTR表（为满足商户商品报备平台化功能）
	 * @param merInfo
	 * @author wanyong
	 * @date 2014-7-18 下午5:33:07
	 */
	public void saveMerExpAttr(MerInfo merInfo);

	/**
	 * @Title: updateMerExpAttr
	 * @Description: 修改T_HFMER_EXP_ATTR表数据（为满足商户商品报备平台化功能）
	 * @param merInfo
	 * @return
	 * @author wanyong
	 * @date 2014-7-21 下午1:50:18
	 */
	public int updateMerExpAttr(MerInfo merInfo);

	/**
	 * ******************************************** method name : updateMerLock
	 * description : 修改商户锁
	 * 
	 * @return : int
	 * @param : @param merInfo
	 * @param : @return modified : xhf , 2012-11-22 下午06:31:35
	 *        *******************************************
	 */
	public int updateMerLock(MerInfo merInfo);

	/**
	 * ******************************************** method name : getMerInfos
	 * description : 缓存获取商户信息
	 * 
	 * @return : List<MerInfo>
	 * @param : @return
	 * @param : @ modified : zhaojunbao , 2012-8-23 下午02:03:46
	 * @see : *******************************************
	 */
	public List<MerInfo> getMerInfos();

	/**
	 * ******************************************** method name :
	 * getChannelMerInfos description : 缓存获取商户信息
	 * 
	 * @return : List<MerInfo>
	 * @param : @return
	 * @param : @ modified : panyouliang
	 * @see : *******************************************
	 */
	public List<MerInfo> getChannelMerInfos(String channelId);

	/**
	 * ******************************************** method name : updateMerInfo
	 * description : 修改数据到t_mer_inf表
	 * 
	 * @return : int
	 * @param : @param merInfo
	 * @param : @return modified : xhf , 2012-9-4 上午10:48:19
	 *        *******************************************
	 */
	public int updateMerInfo(MerInfo merInfo);

	/**
	 * ******************************************** method name : updateMerExp
	 * description : 修改数据到t_mer_exp表
	 * 
	 * @return : int
	 * @param : @param merInfo
	 * @param : @return modified : xhf , 2012-9-4 上午10:49:07
	 *        *******************************************
	 */
	public int updateMerExp(MerInfo merInfo);

	/**
	 * ******************************************** method name : isOrNotAble
	 * description : 启用/禁用方法
	 * 
	 * @return : int
	 * @param : @param merInfo
	 * @param : @return modified : xhf , 2012-9-4 上午10:49:32
	 *        *******************************************
	 */
	public int isOrNotAble(MerInfo merInfo);

	/**
	 * ******************************************** method name :
	 * getCheckFromMers description : 商户号唯一性验证
	 * 
	 * @return : Object
	 * @param : @param mapWhere
	 * @param : @return
	 * @param : @ modified : anshuqiang , 2012-9-4 上午11:27:26
	 *        *******************************************
	 */
	public List getCheckFromMers(Map<String, String> mapWhere);

	public List checkModMerName(Map<String, String> mapWhere);

	public MerInfo loadMerInf(String merId);

	public MerInfo loadMerExp(String merId);

	public List<MerInfo> selectAll();

	public List<MerInfo> filtrationMerByName(String merName);
}
