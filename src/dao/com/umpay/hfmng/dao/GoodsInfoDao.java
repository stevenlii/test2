package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.GoodsInfo;

public interface GoodsInfoDao extends EntityDao<GoodsInfo>{
	
	/**
	/**
	 * 多个条件查询会员
	 */
	
	public GoodsInfo get(Map<String, String> mapWhere) throws DataAccessException;
	
	public int updateGoodsLock(GoodsInfo goodsInfo) throws DataAccessException;
	
	public String updateGoodsCusPhone(GoodsInfo goodsInfo) throws DataAccessException;
	/**
	 * ********************************************
	 * method name   : getAllGoodsInfos 
	 * description   :  用于缓存中保存 商户号-商品号的方法
	 * @return       : List<GoodsInfo>
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : zhaojunbao ,  2012-8-27  下午05:47:32
	 * @see          : 
	 * *******************************************
	 */
	public List<GoodsInfo> getAllGoodsInfos() ;
	
	/**
	 * ********************************************
	 * method name   : getAllGoodsInfoByChannelMer 
	 * @return       : List<GoodsInfo>
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : panyouliang
	 * *******************************************
	 */
	public List<GoodsInfo> getAllGoodsInfoByChannelMer(String channelId, String merId) ;

	/**
	 * ********************************************
	 * method name   : getCheckFromTgoods 
	 * description   : 验证商品表是否存在待插入数据
	 * @return       : Object
	 * @param        : @param mapWhere
	 * @param        : @return
	 * @param        : @throws DataAccessException
	 * modified      : zhaojunbao ,  2012-8-30  下午06:01:06
	 * @see          : 
	 * *******************************************
	 */
	public Object getCheckFromTgoods(Map<String, String> mapWhere) throws DataAccessException;

	/**
	 * ********************************************
	 * method name   : saveGoodsExp 
	 * description   : 插入数据到t_mer_exp表
	 * @return       : void
	 * @param        : @param goodsInfo
	 * modified      : xhf ,  2012-9-4  上午09:41:33
	 * *******************************************
	 */
	public void saveGoodsExp(GoodsInfo goodsInfo);
	
	/**
	 * ********************************************
	 * method name   : saveGoodsInf 
	 * description   : 插入数据到t_mer_inf表
	 * @return       : void
	 * @param        : @param goodsInfo
	 * modified      : xhf ,  2012-9-4  上午09:47:19
	 * *******************************************
	 */
	public void saveGoodsInf(GoodsInfo goodsInfo);
	
	/**
	 * ********************************************
	 * method name   : saveMonGoods 
	 * description   : 插入数据到t_mon_goods表
	 * @return       : void
	 * @param        : @param goodsInfo
	 * modified      : xhf ,  2012-9-4  上午09:47:39
	 * *******************************************
	 */
	public void saveMonGoods(GoodsInfo goodsInfo);
	
	/**
	 * ********************************************
	 * method name   : updateGoodsExp 
	 * description   : 修改数据到t_mer_exp表
	 * @return       : int
	 * @param        : @param goodsInfo
	 * @param        : @return
	 * modified      : xhf ,  2012-9-4  上午09:49:04
	 * *******************************************
	 */
	public int updateGoodsExp(GoodsInfo goodsInfo);
	
	/**
	 * ********************************************
	 * method name   : updateGoodsInf 
	 * description   : 插入数据到t_mer_inf表
	 * @return       : int
	 * @param        : @param goodsInfo
	 * @param        : @return
	 * modified      : xhf ,  2012-9-4  上午09:49:44
	 * *******************************************
	 */
	public int updateGoodsInf(GoodsInfo goodsInfo);
	
	/**
	 * ********************************************
	 * method name   : updateMonGoods 
	 * description   : 插入数据到t_mon_goods表
	 * @return       : int
	 * @param        : @param goodsInfo
	 * @param        : @return
	 * modified      : xhf ,  2012-9-4  上午09:49:56
	 * *******************************************
	 */
	public int updateMonGoods(GoodsInfo goodsInfo);
	
	/**
	 * ********************************************
	 * method name   : isOrNotAble 
	 * description   : 启用/禁用方法
	 * @return       : int
	 * @param        : @param goodsInfo
	 * @param        : @return
	 * modified      : xhf ,  2012-9-4  上午09:50:44
	 * *******************************************
	 */
	public int isOrNotAble(GoodsInfo goodsInfo);
	/**
	 * ********************************************
	 * method name   : getMonGoods 
	 * description   : 获取包月商品的信息
	 * @return       : GoodsInfo
	 * @param        : @param mapWhere
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-9-12  上午11:02:39
	 * @see          : 
	 * *******************************************
	 */
	public GoodsInfo getMonGoods(Map<String, String> mapWhere);
    /**
     * ********************************************
     * method name   : getGoodsInfos 
     * description   : 缓存获取所有商品信息
     * @return       : List<GoodsInfo>
     * @param        : @return
     * modified      : zhaojunbao ,  2012-10-8  下午01:52:15
     * @see          : 
     * *******************************************
     */
	public List<GoodsInfo> getGoodsInfos(); 
	/**
	 * ********************************************
	 * method name   : getGoodsIdByMerId 
	 * description   : 通过商户号获取对应的商品号列表
	 * @return       : List<String>
	 * @param        : @param merId
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-10-9  下午02:24:05
	 * @see          : 
	 * *******************************************
	 */
	public List<GoodsInfo> getGoodsIdByMerId(Map mapwhere);
	
	public List<GoodsInfo> loadGoodsInf(String merId);
	public List<String> loadGoodsExp(String merId);
}
