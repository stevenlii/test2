package com.umpay.hfmng.service;

import java.io.File;
import java.util.List;
import java.util.Map;


import org.springframework.dao.DataAccessException;
import com.umpay.hfmng.model.GoodsInfo;
import com.umpay.sso.org.User;


public interface GoodsInfoService {

	public GoodsInfo load(Map<String, String> mapWhere)
			throws DataAccessException;

	public String insertGoodsAudit(GoodsInfo goodsInfo,String auditType)
			throws  Exception;
	
	/**
	 * ********************************************
	 * method name   : batchModGoods 
	 * description   : service 批量修改商品的属性
	 * @return       : void
	 * @param        : @param goodsList
	 * @param        : @param goodsInfo
	 * @param        : @throws Exception
	 * modified      : Administrator ,  2012-8-22  上午10:02:17
	 * @throws Exception 
	 * @see          : 
	 * *******************************************
	 */
	public String batchModGoods (List<GoodsInfo> goodsList,GoodsInfo goodsInfo) throws  Exception;
	
	/**
	 * ********************************************
	 * method name   : modifyGoodsInfo 
	 * description   :  修改商品信息，修改操作包括更新锁定状态，以及将修改的数据插入审核表中
	 * @return       : String
	 * @param        : @param goodsInfo
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-8-30  下午03:06:35
	 * @throws Exception 
	 * @see          : 
	 * *******************************************
	 */
	public String modifyGoodsInfo(GoodsInfo goodsInfo) throws Exception ;
	
	/**
	 * ********************************************
	 * method name   : enableAnddisable 
	 * description   : 启用禁用操作，包括批量启用禁用和单个启用禁用，参数为数组，
	 * 数组第一位表示商户号merId第二位表示商品号goodsId 两个是联合主键
	 * @return       : String
	 * @param        : @param array
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-8-30  下午03:49:14
	 * @throws Exception 
	 * @see          : 
	 * *******************************************
	 */
	public  String enableAnddisable(String [] array, User user,String action) throws Exception;
	/**
	 * ********************************************
	 * method name   : loadMonGoods 
	 * description   : 加载包月商品信息
	 * @return       : GoodsInfo
	 * @param        : @param mapWhere
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : zhaojunbao ,  2012-9-12  上午11:05:04
	 * @see          : 
	 * *******************************************
	 */
	public GoodsInfo loadMonGoods(Map<String, String> mapWhere)
	throws DataAccessException;
	/**
	 * ********************************************
	 * method name   : getDownloadFile 
	 * description   : 获取全网或小额商品下载文件
	 * @return       : File
	 * @param        : @param bankidtype
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : xuhuafeng ,  2013-6-14  上午11:55:44
	 * @see          : 
	 * *******************************************
	 */
	public File getDownloadFile(String bankidtype) throws Exception;
}