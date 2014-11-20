/** *****************  JAVA头文件说明  ****************
 * file name  :  ProxyGoodsInfDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-11
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.ProxyGoodsInfDao;
import com.umpay.hfmng.model.ProxyGoods;


/** ******************  类说明  *********************
 * class       :  ProxyGoodsInfDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Repository
public class ProxyGoodsInfDaoImpl extends EntityDaoImpl<ProxyGoods> implements
		ProxyGoodsInfDao {

	/** ********************************************
	 * method name   : get 
	 * modified      : xuhuafeng ,  2013-10-11
	 * @see          : @see com.umpay.hfmng.dao.ProxyGoodsInfDao#get(java.lang.String, java.lang.String, java.lang.String)
	 * ********************************************/
	public ProxyGoods get(String merId, String subMerId, String goodsId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("merId", merId);
		map.put("subMerId", subMerId);
		map.put("goodsId", goodsId);
		return (ProxyGoods) this.get("ProxyGoods.Get", map);
	}
    public void insert(ProxyGoods proxyGoods) throws DataAccessException{
    	this.save("ProxyGoods.Insert", proxyGoods);
    }
    public int update(ProxyGoods proxyGoods)throws DataAccessException{
    	return this.update("ProxyGoods.Update",proxyGoods);
    }
    /**
     * ********************************************
     * method name   : isOrNotAble 
     * description   : 启用禁用操作
     * @return       : int
     * @param        : @param proxyGoods
     * @param        : @return
     * @param        : @throws Exception
     * modified      : zhaojunbao ,  2013-10-14  下午04:05:24
     * @see          : 
     * *******************************************
     */
    public int isOrNotAble(ProxyGoods proxyGoods) throws Exception {
		return this.update("ProxyGoods.isOrNotAble", proxyGoods);
	}
	
	/** ********************************************
	 * method name   : setLock 
	 * modified      : zhaojunbao ,  2013-10-14
	 * @see          : @see com.umpay.hfmng.dao.ProxyGoodsInfDao#setLock(java.lang.String, int)
	 * ********************************************/     
	public int setLock(String key, int modLock) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String [] keys=key.split("-");
		map.put("merId", keys[0]);
		map.put("subMerId", keys[1]);
		map.put("goodsId", keys[2]);
		map.put("modLock", String.valueOf(modLock));
		return this.update("ProxyGoods.setLock", map);
	}
	
	/** ********************************************
	 * method name   : findEnable 
	 * modified      : xuhuafeng ,  2013-10-16
	 * @see          : @see com.umpay.hfmng.dao.ProxyGoodsInfDao#findEnable(java.lang.String, java.lang.String)
	 * ********************************************/     
	@SuppressWarnings("unchecked")
	public List<ProxyGoods> findEnable(String merId, String goodsId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("merId", merId);
		map.put("goodsId", goodsId);
		return this.find("ProxyGoods.findEnable", map);
	}
	
	/** ********************************************
	 * method name   : findEnableBySubMer 
	 * modified      : xuhuafeng ,  2013-10-17
	 * @see          : @see com.umpay.hfmng.dao.ProxyGoodsInfDao#findEnableBySubMer(java.lang.String)
	 * ********************************************/     
	@SuppressWarnings("unchecked")
	public List<ProxyGoods> findEnableBySubMer(String subMerId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("subMerId", subMerId);
		return this.find("ProxyGoods.findEnableBySubMer", map);
	}
}
