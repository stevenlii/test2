/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlInfDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-18
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.ChnlInf;
import com.umpay.hfmng.model.MerInfo;


/** ******************  类说明  *********************
 * class       :  ChnlInfDao
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface ChnlInfDao extends EntityDao<ChnlInf>{
	
	public List<ChnlInf> getChnlList();
	
	public ChnlInf get(String channelId);
	
	public List<Object> getCheckFromChnls(Map<String, String> mapWhere);
	
	public void saveChnl(ChnlInf chnlInf);
	
	public int updateChnl(ChnlInf chnlInf);
	
	public int isOrNotAble(ChnlInf chnlInf);
	
	public int updateChnlLock(ChnlInf chnlInf);
	
	public List<Object> checkModChnlName(Map<String, String> mapWhere);
//	/**
//	 * ********************************************
//	 * method name   : getConfig 
//	 * description   : 获取渠道的通知地址、下单地址等配置信息
//	 * @return       : ChnlInf
//	 * @param        : @param channelId
//	 * @param        : @return
//	 * modified      : lz ,  2013-3-27  下午09:03:42
//	 * @see          : 
//	 * *******************************************
//	 */
//	public ChnlInf getConfig(String channelId);
	
	public void updateConf(ChnlInf chnlInf);

}
