/** *****************  JAVA头文件说明  ****************
 * file name  :  ProxyGoodsLimitDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-11
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.ProxyGoodsLimitDao;
import com.umpay.hfmng.model.ProxyGoods;
import com.umpay.hfmng.model.ProxyGoodsLimit;


/** ******************  类说明  *********************
 * class       :  ProxyGoodsLimitDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Repository
public class ProxyGoodsLimitDaoImpl extends EntityDaoImpl<ProxyGoods> implements
		ProxyGoodsLimitDao {

	
	/** ********************************************
	 * method name   : insert 
	 * modified      : zhaojunbao ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.dao.ProxyGoodsLimitDao#insert(com.umpay.hfmng.model.ProxyGoodsLimit)
	 * ********************************************/     
	public void insert(ProxyGoodsLimit proxyGoodsLimit) {
		this.save("ProxyGoodsLimit.Insert", proxyGoodsLimit);
		
	}

	
	/** ********************************************
	 * method name   : update 
	 * modified      : zhaojunbao ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.dao.ProxyGoodsLimitDao#update(com.umpay.hfmng.model.ProxyGoodsLimit)
	 * ********************************************/     
	public int update(ProxyGoodsLimit proxyGoodsLimit) {
		return this.update("ProxyGoodsLimit.Update", proxyGoodsLimit);
	}
	/**
	 * ********************************************
	 * method name   : isOrNotAble 
	 * description   : 启用禁用操作
	 * @return       : int
	 * @param        : @param proxyGoodsLimit
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhaojunbao ,  2013-10-14  下午04:07:04
	 * @see          : 
	 * *******************************************
	 */
	public int isOrNotAble(ProxyGoodsLimit proxyGoodsLimit) throws Exception {
			return this.update("ProxyGoodsLimit.isOrNotAble", proxyGoodsLimit);
		}


	
	/** ********************************************
	 * method name   : setLock 
	 * modified      : zhaojunbao ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.dao.ProxyGoodsLimitDao#setLock(java.lang.String, int)
	 * ********************************************/     
	public int setLock(String key, int modLock) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String [] keys=key.split("-");
		map.put("merId", keys[0]);
		map.put("subMerId", keys[1]);
		map.put("goodsId", keys[2]);
		map.put("modLock", String.valueOf(modLock));
		return this.update("ProxyGoodsLimit.setLock", map);

	}


	
	/** ********************************************
	 * method name   : loadProxGoodsLimit 
	 * modified      : zhaojunbao ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.dao.ProxyGoodsLimitDao#loadProxGoodsLimit(java.lang.String, java.lang.String)
	 * ********************************************/     
	public ProxyGoodsLimit loadProxGoodsLimit(String merId, String subMerId, String goodsId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("merId", merId);
		map.put("subMerId", subMerId);
		map.put("goodsId", goodsId);
		return (ProxyGoodsLimit) this.get("ProxyGoodsLimit.Get", map);
	}

}
