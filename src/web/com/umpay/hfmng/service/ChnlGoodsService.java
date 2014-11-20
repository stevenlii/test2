/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlGoodsService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-27
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.model.ChnlBank;
import com.umpay.hfmng.model.ChnlGoods;


/** ******************  类说明  *********************
 * class       :  ChnlGoodsService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface ChnlGoodsService {
	
	public ChnlGoods load(String channelId, String merId, String goodsId);
	
	public List<ChnlGoods> getList(String channelId, String merId);
	
	public Map<String, ChnlGoods> getMap(String channelId, String merId);
	
	public Map getAuditMap(String channelId, String merId);
	
	public String save(ChnlGoods chnlGoods, List<String> newOpen,
			List<String> modOpen, List<String> modClose);
	
	public String enableAnddisable(String ID, String userId, int action);
	
	public String auditPass(String[] id, String userId, String resultDesc);
	
	public String auditNotPass(String[] id, String userId, String resultDesc);

}
