/** *****************  JAVA头文件说明  ****************
 * file name  :  BusiConfDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2014-1-15
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.Audit;
import com.umpay.hfmng.model.MerBusiConf;


/** ******************  类说明  *********************
 * class       :  BusiConfDao
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/

public interface BusiConfDao extends EntityDao<MerBusiConf> {
	
	public int updateLock(MerBusiConf merBusiConf);
	/**
	 * *****************  方法说明  *****************
	 * method name   :  checkDataAdd
	 * @param		 :  @param merId
	 * @param		 :  @return
	 * @return		 :  List<Audit>
	 * @author       :  LiZhen 2014-1-20 下午2:27:00
	 * description   :  根据商户号查找正在审核中的支付类型
	 * @see          :  
	 * **********************************************
	 */
	public List<Audit> findAuditingBizType(String merId);

}
