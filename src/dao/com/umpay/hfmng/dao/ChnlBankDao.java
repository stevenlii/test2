/** *****************  JAVA头文件说明  ****************
 * file name  :  ChnlBankDao.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-3-19
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.ChnlBank;


/** ******************  类说明  *********************
 * class       :  ChnlBankDao
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public interface ChnlBankDao extends EntityDao<ChnlBank>{
	
	public ChnlBank get(Map<String, String> mapWhere);
	
	public List<ChnlBank> findBankByChnlId(String channelId);
	
	public int updateModLock(ChnlBank chnlBank);

}
