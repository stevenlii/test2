/** *****************  JAVA头文件说明  ****************
 * file name  :  GoodsFeeCodeDaoImpl.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-10-29
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;



import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.GoodsFeeCodeDao;
import com.umpay.hfmng.model.FeeCode;

/** ******************  类说明  *********************
 * class       :  GoodsFeeCodeDaoImpl
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Repository("goodsFeeCodeDaoImpl")
public class GoodsFeeCodeDaoImpl extends EntityDaoImpl<FeeCode> implements GoodsFeeCodeDao{

	
	/** ********************************************
	 * method name   : getGoodsFeeCodesByMerGoods 
	 * modified      : zhaojunbao ,  2012-10-29
	 * @see          : @see com.umpay.hfmng.dao.GoodeFeeCodeDao#getGoodsFeeCodesByMerGoods(java.util.Map)
	 * ********************************************/     
	public List<FeeCode> getGoodsFeeCodesByMerGoods(Map<String, String> mapWhere) {
		
		List <FeeCode> list=null;
		try{
			 list = super.find("GoodsFeeCode.GetFeeCode", mapWhere);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}

	
	/** ********************************************
	 * method name   : updateGoodsFeeCode 
	 * modified      : zhaojunbao ,  2012-10-29
	 * @see          : @see com.umpay.hfmng.dao.GoodeFeeCodeDao#updateGoodsFeeCode(com.umpay.hfmng.model.FeeCode)
	 * ********************************************/     
	public int updateGoodsFeeCode(FeeCode goodsFeeCode) {
		return this.update("GoodsFeeCode.updateGoodsFeeCode", goodsFeeCode);
	}


	
	/** ********************************************
	 * method name   : saveGoodsFeeCode 
	 * modified      : zhaojunbao ,  2012-10-31
	 * @see          : @see com.umpay.hfmng.dao.GoodsFeeCodeDao#saveGoodsFeeCode(com.umpay.hfmng.model.FeeCode)
	 * ********************************************/     
	public void saveGoodsFeeCode(FeeCode feeCode){
	      this.save("GoodsFeeCode.Insert", feeCode);
	}
	

}
