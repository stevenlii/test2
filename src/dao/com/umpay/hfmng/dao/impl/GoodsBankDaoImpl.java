/** *****************  JAVA头文件说明  ****************
 * file name  :  GoodsBankDaoImpl.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-9
 * *************************************************/

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.GoodsBankDao;
import com.umpay.hfmng.model.GoodsBank;

/**
 * ****************** 类说明 ********************* class : GoodsBankDaoImpl
 * 
 * @author : zhaojunbao
 * @version : 1.0 description :
 * @see : *
 ***********************************************/
@Repository
public class GoodsBankDaoImpl extends EntityDaoImpl<GoodsBank> implements
		GoodsBankDao {

	/**
	 * ******************************************** method name : get modified :
	 * zhaojunbao , 2012-10-9
	 * 
	 * @see : @see com.umpay.hfmng.dao.GoodsBankDao#get(java.util.Map) *
	 *******************************************/
	public GoodsBank get(Map<String, String> mapWhere)
			throws DataAccessException {
		return (GoodsBank) this.get("GoodsBank.Get", mapWhere);
	}

	/**
	 * ******************************************** method name :
	 * updateGoodsBankLock modified : zhaojunbao , 2012-10-15
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.GoodsBankDao#updateGoodsBankLock(com.umpay
	 *      .hfmng.model.GoodsBank) *
	 *******************************************/
	public int updateGoodsBankLock(GoodsBank goodsBank) {
		return this.update("GoodsBank.updateGoodsBankLock", goodsBank);
	}

	public void saveGoodsBank(GoodsBank goodsBank) {
		this.save("GoodsBank.insertGoodsBank", goodsBank);
	}

	public int updateGoodsBank(GoodsBank goodsBank) {
		int rs = this.update("GoodsBank.updateGoodsBank", goodsBank);
		return rs;
	}

	/**
	 * @Title: findOpenCount
	 * @Description: 根据商户、商品ID查询已开通的商品银行记录数
	 * @param
	 * @param merId
	 * @param goodsId
	 * @return
	 * @author wanyong
	 * @date 2013-1-11 下午02:54:12
	 */
	@SuppressWarnings("unchecked")
	public int findOpenCount(String merId, String goodsId) {
		Map<String, String> whereMap = new HashMap<String, String>();
		whereMap.put("merId", merId);
		whereMap.put("goodsId", goodsId);
		Map<String, Integer> reMap = (HashMap<String, Integer>) super.get(
				"GoodsBank.queryOpenCount", whereMap);
		return reMap.get("NUM");
	}

	@SuppressWarnings("unchecked")
	public List<GoodsBank> findAll() {
		return super.find("GoodsBank.FindHN");
	}

	/** ********************************************
	 * method name   : findGoodsBanks 
	 * modified      : xuhuafeng ,  2014-10-17
	 * @see          : @see com.umpay.hfmng.dao.GoodsBankDao#findGoodsBanks(java.util.Map)
	 * ********************************************/     
	@SuppressWarnings("unchecked")
	public List<GoodsBank> findGoodsBanks(Map<String, String> mapWhere) {
		return super.find("GoodsBank.Find", mapWhere);
	}
}
