/** *****************  JAVA头文件说明  ****************
 * file name  :  GoodsBankDao.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-9
 * *************************************************/

package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;
import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.GoodsBank;

/**
 * ****************** 类说明 ********************* class : GoodsBankDao
 * 
 * @author : zhaojunbao
 * @version : 1.0 description :
 * @see : *
 ***********************************************/

public interface GoodsBankDao extends EntityDao<GoodsBank> {
	/**
	 * ******************************************** method name : get
	 * description : 获取商品银行信息
	 * 
	 * @return : GoodsBank
	 * @param : @param mapWhere
	 * @param : @return modified : zhaojunbao , 2012-10-15 上午11:36:57
	 * @see : *******************************************
	 */
	public GoodsBank get(Map<String, String> mapWhere);

	/**
	 * ******************************************** method name :
	 * updateGoodsBankLock description : 更新锁状态
	 * 
	 * @return : int
	 * @param : @param goodsInfo
	 * @param : @return modified : zhaojunbao , 2012-10-15 上午11:37:34
	 * @see : *******************************************
	 */
	public int updateGoodsBankLock(GoodsBank goodsBank);

	public void saveGoodsBank(GoodsBank goodsBank);

	public int updateGoodsBank(GoodsBank goodsBank);

	/**
	 * @Title: findOpenCount
	 * @Description: 根据商户、商品ID查询已开通的商品银行记录数接口
	 * @param
	 * @param merId
	 * @param goodsId
	 * @return
	 * @author wanyong
	 * @date 2013-1-11 下午02:53:23
	 */
	public int findOpenCount(String merId, String goodsId);
	
	public List<GoodsBank> findAll();
	
	public List<GoodsBank> findGoodsBanks(Map<String, String> mapWhere);
}
