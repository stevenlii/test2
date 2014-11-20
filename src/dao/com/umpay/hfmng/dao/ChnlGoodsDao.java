/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlGoodsDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-27
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.ChnlGoods;


/** ******************  类说明  *********************
 * class       :  ChnlGoodsDao
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public interface ChnlGoodsDao extends EntityDao<ChnlGoods> {
	
	public ChnlGoods get(String channelId, String merId, String goodsId);
	
	public List<ChnlGoods> getList(String channelId, String merId);
	
	public int updateModLock(ChnlGoods chnlGoods);

}
