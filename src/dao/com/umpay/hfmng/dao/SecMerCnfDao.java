/** *****************  JAVA头文件说明  ****************
 * file name  :  SecMerDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-9-24
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.SecMerCnf;


public interface SecMerCnfDao extends EntityDao<SecMerCnf> {
	/**
	 * *****************  方法说明  *****************
	 * method name   :  findBySubMerId
	 * @param		 :  @param subMerId
	 * @param		 :  @return
	 * @return		 :  List<SecMerCnf>
	 * @author       :  LiZhen 2013-10-14 下午2:21:13
	 * description   :  根据二级商户号查找
	 * @see          :  
	 * **********************************************
	 */
	public List<SecMerCnf> findBySubMerId(String subMerId);
}
