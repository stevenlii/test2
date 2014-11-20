package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.MerInfoDao;
import com.umpay.hfmng.model.MerInfo;

@Repository("merInfoDaoImpl")
public class MerInfoDaoImpl extends EntityDaoImpl<MerInfo> implements MerInfoDao {

	public MerInfo get(String id) {
		return (MerInfo) this.get("MerInfo.Get", id);
	}

	/**
	 * ******************************************** method name : saveMerExp
	 * modified : xhf , 2012-9-4
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.MerInfoDao#saveMerExp(com.umpay.hfmng.model
	 *      .MerInfo) *******************************************
	 */
	public void saveMerExp(MerInfo merInfo) {
		this.save("MerInfo.insertMerExp", merInfo);
	}

	/**
	 * @Title: saveMerExpAttr
	 * @Description: 新增数据到T_HFMER_EXP_ATTR表（为满足商户商品报备平台化功能）
	 * @param merInfo
	 * @author wanyong
	 * @date 2014-7-18 下午5:34:40
	 */
	public void saveMerExpAttr(MerInfo merInfo) {
		this.save("MerInfo.insertMerExpAttr", merInfo);
	}

	/**
	 * @Title: updateMerExpAttr
	 * @Description: 修改T_HFMER_EXP_ATTR表数据（为满足商户商品报备平台化功能）
	 * @param merInfo
	 * @return
	 * @author wanyong
	 * @date 2014-7-21 下午1:50:18
	 */
	public int updateMerExpAttr(MerInfo merInfo) {
		return this.update("MerInfo.updateMerExpAttr", merInfo);
	}

	/**
	 * ******************************************** method name : saveMerInfo
	 * modified : xhf , 2012-9-4
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.MerInfoDao#saveMerInfo(com.umpay.hfmng.model
	 *      .MerInfo) *******************************************
	 */
	public void saveMerInfo(MerInfo merInfo) {
		this.save("MerInfo.insertMerInf", merInfo);
	}

	public int updateMerLock(MerInfo merInfo) {
		return this.update("MerInfo.updateMerLock", merInfo);
	}

	/**
	 * ******************************************** method name : updateMerInfo
	 * modified : xhf , 2012-9-4
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.MerInfoDao#updateMerInfo(com.umpay.hfmng.model
	 *      .MerInfo) *******************************************
	 */
	public int updateMerInfo(MerInfo merInfo) {
		return this.update("MerInfo.updateMerInfo", merInfo);
	}

	/**
	 * ******************************************** method name : getMerInfos
	 * 缓存获取商户信息 modified : zhaojunbao , 2012-8-23
	 * 
	 * @see : @see com.umpay.hfmng.dao.MerInfoDao#getMerInfos() *
	 *******************************************/
	@SuppressWarnings("unchecked")
	public List<MerInfo> getMerInfos() {
		List<MerInfo> list = null;
		try {
			list = super.find("MerInfo.getMerInfo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<MerInfo> getChannelMerInfos(String channelId) {
		List<MerInfo> list = null;
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("channelid", channelId);
			list = super.find("MerInfo.getChannelMerInfo", param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ******************************************** method name : updateMerExp
	 * modified : xhf , 2012-9-4
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.MerInfoDao#updateMerExp(com.umpay.hfmng.model
	 *      .MerInfo) *******************************************
	 */
	public int updateMerExp(MerInfo merInfo) {
		return this.update("MerInfo.updateMerExp", merInfo);
	}

	/**
	 * ******************************************** method name : isOrNotAble
	 * modified : Administrator , 2012-8-31
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.MerInfoDao#isOrNotAble(com.umpay.hfmng.model
	 *      .MerInfo)
	 * ********************************************/
	public int isOrNotAble(MerInfo merInfo) {
		return this.update("MerInfo.isOrNotAble", merInfo);
	}

	public List getCheckFromMers(Map<String, String> mapWhere) {
		return this.find("MerInfo.checkFromMers", mapWhere);
	}

	/**
	 * ******************************************** method name :
	 * checkModMerName modified : xuhuafeng , 2012-12-20
	 * 
	 * @see : @see com.umpay.hfmng.dao.MerInfoDao#checkModMerName(java.util.Map)
	 * ********************************************/
	public List checkModMerName(Map<String, String> mapWhere) {
		return this.find("MerInfo.checkMerName", mapWhere);
	}

	/**
	 * ******************************************** method name : loadMerInf
	 * modified : xuhuafeng , 2013-1-30
	 * 
	 * @see : @see com.umpay.hfmng.dao.MerInfoDao#loadMerInf(java.lang.String)
	 * ********************************************/
	public MerInfo loadMerInf(String merId) {
		return (MerInfo) this.get("MerInfo.loadMerInf", merId);
	}

	/**
	 * ******************************************** method name : loadMerExp
	 * modified : xuhuafeng , 2013-1-30
	 * 
	 * @see : @see com.umpay.hfmng.dao.MerInfoDao#loadMerExp(java.lang.String)
	 * ********************************************/
	public MerInfo loadMerExp(String merId) {
		return (MerInfo) this.get("MerInfo.loadMerExp", merId);
	}

	public List<MerInfo> selectAll() {
		return this.find("MerInfo.selectAll");
	}

	/*
	 * 
	 * 
	 * @return
	 * 
	 * @see com.umpay.hfmng.dao.MerInfoDao#filtrationMerByName()
	 */

	public List<MerInfo> filtrationMerByName(String merName) {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("merName", merName);
		return this.find("MerInfo.filtrationMerByName", mapWhere);
	}
}
