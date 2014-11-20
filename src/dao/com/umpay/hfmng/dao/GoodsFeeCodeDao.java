/** *****************  JAVA头文件说明  ****************
 * file name  :  GoodeFeeCodeDao.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-29
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;
import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.FeeCode;


/** ******************  类说明  *********************
 * class       :  GoodeFeeCodeDao
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public interface GoodsFeeCodeDao extends EntityDao<FeeCode>{

	/**
	 * ********************************************
	 * method name   : getGoodsFeeCodesByMerGoods 
	 * description   : 获取已经配置的商品计费代码
	 * @return       : List<FeeCode>
	 * @param        : @param mapWhere
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-10-29  下午03:01:22
	 * @see          : 
	 * *******************************************
	 */
	public List<FeeCode> getGoodsFeeCodesByMerGoods(Map<String, String> mapWhere);
	/**
	 * ********************************************
	 * method name   : updateGoodsFeeCode 
	 * description   :  更新商品计费代码信息
	 * @return       : int
	 * @param        : @param goodsFeeCode
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-10-29  下午03:01:44
	 * @see          : 
	 * *******************************************
	 */
	public int updateGoodsFeeCode(FeeCode goodsFeeCode);
	/**
	 * ********************************************
	 * method name   : saveGoodsFeeCode 
	 * description   : 保存计费代码方法
	 * @return       : void
	 * @param        : @param feeCode
	 * modified      : zhaojunbao ,  2012-10-31  下午02:08:10
	 * @see          : 
	 * *******************************************
	 */
	public void saveGoodsFeeCode(FeeCode feeCode);
}
