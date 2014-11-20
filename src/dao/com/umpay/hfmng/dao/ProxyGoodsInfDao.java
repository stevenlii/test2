/** *****************  JAVA头文件说明  ****************
 * file name  :  ProxyGoodsInfDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-11
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.ProxyGoods;


/** ******************  类说明  *********************
 * class       :  ProxyGoodsInfDao
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface ProxyGoodsInfDao extends EntityDao<ProxyGoods> {
	
	public ProxyGoods get(String merId, String subMerId, String goodsId);
    public void insert(ProxyGoods proxyGoods)throws DataAccessException;
    public int update(ProxyGoods proxyGoods) throws DataAccessException;
    public int isOrNotAble(ProxyGoods proxyGoods) throws Exception;
    public int setLock(String key, int modLock) throws Exception;
    public List<ProxyGoods> findEnable(String merId, String goodsId);
    public List<ProxyGoods> findEnableBySubMer(String subMerId);
}
