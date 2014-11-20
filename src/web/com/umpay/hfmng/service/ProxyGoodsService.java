/** *****************  JAVA头文件说明  ****************
 * file name  :  ProxyGoodsService.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-11
 * *************************************************/ 

package com.umpay.hfmng.service;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.ProxyGoods;
import com.umpay.hfmng.model.ProxyGoodsLimit;


/** ******************  类说明  *********************
 * class       :  ProxyGoodsService
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface ProxyGoodsService {
	
	public ProxyGoods load(String merId, String subMerId, String goodsId);
	
	public List<ProxyGoods> findByKey(String merId, String subMerId);
	
	public Map<String, Audit> getAuditMap(String merId, String subMerId);
	
	public String save(ProxyGoodsLimit proxyGoodsLimit) throws Exception;
	
	public String update(ProxyGoodsLimit proxyGoodsLimit) throws Exception;
	
	public String enableAndDisable(String[] ids,int action) throws Exception;
	
	public String batchSave(ProxyGoodsLimit proxyGoodsLimit) throws Exception;
	
    public String auditPass(String[] ids, String resultDesc) throws Exception;
	
	public String auditNotPass(String[] ids, String resultDesc) throws Exception;
	
	public String auditPassByBatchId(String batchId, String resultDesc) throws Exception;
	
	public String auditNotPassByBatchId(String batchId, String resultDesc) throws Exception;
	
	public String checkKey(String subMerId) throws Exception; 
	
	public ProxyGoodsLimit loadGoodsLimit(String merId, String subMerId,String goodsId);
	
	public List<ProxyGoods> findEnableBySubMer(String subMerId);

}
