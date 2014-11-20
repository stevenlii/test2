/** *****************  JAVA头文件说明  ****************
 * file name  :  ReduceDataDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-7-16
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.HSMerSet;


public interface ReduceDataDao extends EntityDao<HSMerSet> {
	
	/**
	 * *****************  方法说明  *****************
	 * method name   :  getXEReduceData
	 * @param		 :  @param lastLMonth
	 * @param		 :  @return
	 * @return		 :  List<HSMerSet>
	 * @author       :  LiZhen 2013-10-12 上午11:40:13
	 * description   :  获取小额外地交易金额和核减金额
	 * @see          :  
	 * **********************************************
	 */
	public List<HSMerSet> getXEReduceData(String lastLMonth);
	/**
	 * *****************  方法说明  *****************
	 * method name   :  getMWReduceData
	 * @param		 :  @param lastLMonth
	 * @param		 :  @return
	 * @return		 :  List<HSMerSet>
	 * @author       :  LiZhen 2013-10-12 上午11:40:56
	 * description   :  获取全网外地交易金额和核减金额
	 * @see          :  
	 * **********************************************
	 */
	public List<HSMerSet> getMWReduceData(String lastLMonth);
}
