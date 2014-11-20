/** *****************  JAVA头文件说明  ****************
 * file name  :  FeeCodeCountDao.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-11-7
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.model.FeeCodeCount;


/** ******************  类说明  *********************
 * class       :  FeeCodeCountDao
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public interface FeeCodeCountDao {
	/**
	 * ********************************************
	 * method name   : getUseCount 
	 * description   :  获取已经使用的计费代码次数
	 * @return       : List<FeeCodeCount>
	 * @param        : @return
	 * modified      : zhaojunbao ,  2012-11-7  上午10:47:36
	 * @see          : 
	 * *******************************************
	 */
public List<FeeCodeCount> getUseCount();
/**
 * ********************************************
 * method name   : getMatch 
 * description   : 获取完全匹配的计费代码id
 * @return       : List<FeeCodeCount>
 * @param        : @return
 * modified      : zhaojunbao ,  2012-11-7  上午10:48:18
 * @see          : 
 * *******************************************
 */
public List<FeeCodeCount> getMatch();
/**
 * ********************************************
 * method name   : update 
 * description   :  更新计费代码统计表信息
 * @return       : int
 * @param        : @param feeCodeCount
 * @param        : @return
 * modified      : zhaojunbao ,  2012-11-7  上午11:18:30
 * @see          : 
 * *******************************************
 */
public int updateCount(FeeCodeCount feeCodeCount);
/**
 * ********************************************
 * method name   : insert 
 * description   : 插入计费代码表信息
 * @return       : void
 * @param        : @param feeCodeCount
 * modified      : zhaojunbao ,  2012-11-7  上午11:19:09
 * @see          : 
 * *******************************************
 */
public void insertCount(FeeCodeCount feeCodeCount);
}
