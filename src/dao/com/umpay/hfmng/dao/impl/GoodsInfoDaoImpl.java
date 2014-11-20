package com.umpay.hfmng.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.GoodsInfoDao;
import com.umpay.hfmng.model.GoodsInfo;

@Repository("goodsInfoDaoImpl")
public class GoodsInfoDaoImpl extends EntityDaoImpl<GoodsInfo> implements
		GoodsInfoDao {

	public GoodsInfo get(Map<String, String> mapWhere)
			throws DataAccessException {
		return (GoodsInfo) this.get("GoodsInfo.Get", mapWhere);
	}

	/**
	 * ******************************************** method name :
	 * updateGoodsLock 更改商品锁，在对商品信息做修改后，需要更改商品扩展表中锁定状态 modified : Administrator
	 * , 2012-8-15
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsInfoDao#updateGoodsLock(com.umpay.hfmng
	 *      .model.GoodsInfo) *******************************************
	 */
	public int updateGoodsLock(GoodsInfo goodsInfo) throws DataAccessException {

		return this.update("GoodsInfo.updateGoodsLock", goodsInfo);

	}

	public String updateGoodsCusPhone(GoodsInfo goodsInfo)
			throws DataAccessException {
		this.update("GoodsInfo.updateGoodsCusPhone", goodsInfo);
		return null;
	}

	/**
	 * ******************************************** method name :
	 * getAllGoodsInfos modified : zhaojunbao , 2012-8-27
	 * 
	 * @see : @see com.umpay.hfmng.dao.GoodsInfoDao#getAllGoodsInfos()
	 * ********************************************/
	@SuppressWarnings("unchecked")
	public List<GoodsInfo> getAllGoodsInfos() {
		try {
			return (List<GoodsInfo>) this.get("GoodsInfo.GetMerIdGoodsId");
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * ******************************************** method name :
	 * getCheckFromTgoods modified : zhaojunbao , 2012-8-30
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.HfauditInfoDao#getCheckFromTgoods(java.util.Map)
	 * ********************************************/

	public Object getCheckFromTgoods(Map<String, String> mapWhere)
			throws DataAccessException {

		return this.find("GoodsInfo.checkFromTgoods", mapWhere);

	}

	/**
	 * ******************************************** method name : isOrNotAble
	 * modified : xhf , 2012-9-4
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsInfoDao#isOrNotAble(com.umpay.hfmng.model
	 *      .GoodsInfo) *******************************************
	 */
	public int isOrNotAble(GoodsInfo goodsInfo) {
		return this.update("GoodsInfo.isOrNotAble", goodsInfo);
	}

	/**
	 * ******************************************** method name : saveGoodsExp
	 * modified : Administrator , 2012-8-31
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsInfoDao#saveGoodsExp(com.umpay.hfmng.
	 *      model.GoodsInfo)
	 * ********************************************/
	public void saveGoodsExp(GoodsInfo goodsInfo) {
		this.save("GoodsInfo.insertGoodsExp", goodsInfo);
	}

	/**
	 * ******************************************** method name : saveGoodsInf
	 * modified : Administrator , 2012-8-31
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsInfoDao#saveGoodsInf(com.umpay.hfmng.
	 *      model.GoodsInfo)
	 * ********************************************/
	public void saveGoodsInf(GoodsInfo goodsInfo) {
		this.save("GoodsInfo.insertGoodsInf", goodsInfo);
	}

	/**
	 * ******************************************** method name : saveMonGoods
	 * modified : Administrator , 2012-8-31
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsInfoDao#saveMonGoods(com.umpay.hfmng.
	 *      model.GoodsInfo)
	 * ********************************************/
	public void saveMonGoods(GoodsInfo goodsInfo) {
		this.save("GoodsInfo.insertMonGoods", goodsInfo);
	}

	/**
	 * ******************************************** method name : updateGoodsExp
	 * modified : xhf , 2012-9-4
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsInfoDao#updateGoodsExp(com.umpay.hfmng
	 *      .model.GoodsInfo) *******************************************
	 */
	public int updateGoodsExp(GoodsInfo goodsInfo) {
		return this.update("GoodsInfo.updateGoodsExp", goodsInfo);
	}

	/**
	 * ******************************************** method name : updateGoodsInf
	 * modified : xhf , 2012-9-4
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsInfoDao#updateGoodsInf(com.umpay.hfmng
	 *      .model.GoodsInfo) *******************************************
	 */
	public int updateGoodsInf(GoodsInfo goodsInfo) {
		return this.update("GoodsInfo.updateGoodsInf", goodsInfo);
	}

	/**
	 * ******************************************** method name : updateMonGoods
	 * modified : xhf , 2012-9-4
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsInfoDao#updateMonGoods(com.umpay.hfmng
	 *      .model.GoodsInfo) *******************************************
	 */
	public int updateMonGoods(GoodsInfo goodsInfo) {
		return this.update("GoodsInfo.updateMonGoods", goodsInfo);
	}

	/**
	 * ******************************************** method name : getMonGoods
	 * modified : zhaojunbao , 2012-9-12
	 * 
	 * @see : @see com.umpay.hfmng.dao.GoodsInfoDao#getMonGoods(java.util.Map)
	 * ********************************************/
	public GoodsInfo getMonGoods(Map<String, String> mapWhere) {

		return (GoodsInfo) this.get("GoodsInfo.GetMon", mapWhere);
	}

	/**
	 * ******************************************** method name : getGoodsInfos
	 * modified : zhaojunbao , 2012-10-8
	 * 
	 * @see : @see com.umpay.hfmng.dao.GoodsInfoDao#getGoodsInfos()
	 * ********************************************/
	public List<GoodsInfo> getGoodsInfos() {
		List<GoodsInfo> list = null;
		try {
			list = super.find("GoodsInfo.Get");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ******************************************** method name :
	 * getGoodsIdByMerId modified : zhaojunbao , 2012-10-9
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsInfoDao#getGoodsIdByMerId(java.lang.String)
	 * ********************************************/
	public List<GoodsInfo> getGoodsIdByMerId(Map mapwhere) {
		List<GoodsInfo> list = null;
		try {
			list = super.find("GoodsInfo.GetGoodsByMerId", mapwhere);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ******************************************** method name : loadGoodsInf
	 * modified : xuhuafeng , 2013-1-30
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsInfoDao#loadGoodsInf(java.lang.String)
	 * ********************************************/
	@SuppressWarnings("unchecked")
	public List<GoodsInfo> loadGoodsInf(String merId) {
		List<GoodsInfo> list = null;
		Map mapwhere = new HashMap();
		mapwhere.put("merId", merId);
		try {
			list = super.find("GoodsInfo.loadGoodsInf", mapwhere);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<String> loadGoodsExp(String merId) {
		List<GoodsInfo> list = null;
		List<String> goodsId = new ArrayList<String>();
		Map mapwhere = new HashMap();
		mapwhere.put("merId", merId);
		try {
			list = super.find("GoodsInfo.loadGoodsExp", mapwhere);
			if (list != null) {
				for (GoodsInfo g : list) {
					goodsId.add(g.getGoodsId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goodsId;
	}

	public List<GoodsInfo> getAllGoodsInfoByChannelMer(String channelId,
			String merId) {
		List<GoodsInfo> list = null;
		try {
			Map param = new HashMap();
			param.put("channelid", channelId);
			param.put("merid", merId);
			list = super.find("GoodsInfo.getChannelGoodsInfo", param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
