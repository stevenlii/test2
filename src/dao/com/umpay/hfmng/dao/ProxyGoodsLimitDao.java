/** *****************  JAVA头文件说明  ****************
 * file name  :  ProxyGoodsInfDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-11
 * *************************************************/ 

package com.umpay.hfmng.dao;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.ProxyGoods;
import com.umpay.hfmng.model.ProxyGoodsLimit;


/** ******************  类说明  *********************
 * class       :  ProxyGoodsInfDao
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface ProxyGoodsLimitDao extends EntityDao<ProxyGoods> {
	
 public void insert(ProxyGoodsLimit proxyGoodsLimit)throws Exception;
 public int update(ProxyGoodsLimit proxyGoodsLimit) throws Exception;
 public int isOrNotAble(ProxyGoodsLimit proxyGoodsLimit) throws Exception;
 public int setLock(String key, int modLock) throws Exception;
 public ProxyGoodsLimit loadProxGoodsLimit(String merId, String subMerId,String goodsId);
}
