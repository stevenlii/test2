/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlGoodsDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-27
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.ChnlGoodsDao;
import com.umpay.hfmng.model.ChnlGoods;


/** ******************  类说明  *********************
 * class       :  ChnlGoodsDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Repository
public class ChnlGoodsDaoImpl extends EntityDaoImpl<ChnlGoods> implements ChnlGoodsDao {

	/** ********************************************
	 * method name   : get 
	 * modified      : xuhuafeng ,  2013-3-27
	 * @see          : @see com.umpay.hfmng.dao.ChnlGoodsDao#get(java.lang.String, java.lang.String, java.lang.String)
	 * ********************************************/
	public ChnlGoods get(String channelId, String merId, String goodsId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("channelId", channelId);
		map.put("merId", merId);
		map.put("goodsId", goodsId);
		return (ChnlGoods) this.get("ChnlGoods.Get", map);
	}

	/** ********************************************
	 * method name   : getList 
	 * modified      : xuhuafeng ,  2013-3-27
	 * @see          : @see com.umpay.hfmng.dao.ChnlGoodsDao#getList(java.lang.String, java.lang.String)
	 * ********************************************/
	public List<ChnlGoods> getList(String channelId, String merId) {
		Map<String, String> mapWhere = new HashMap<String, String>();
		mapWhere.put("channelId", channelId);
		mapWhere.put("merId", merId);
		return super.find("ChnlGoods.Find", mapWhere);
	}

	
	/** ********************************************
	 * method name   : updateModLock 
	 * modified      : xuhuafeng ,  2013-3-28
	 * @see          : @see com.umpay.hfmng.dao.ChnlGoodsDao#updateModLock(com.umpay.hfmng.model.ChnlGoods)
	 * ********************************************/     
	public int updateModLock(ChnlGoods chnlGoods) {
		return this.update("ChnlGoods.updateModLock", chnlGoods);
	}

}
