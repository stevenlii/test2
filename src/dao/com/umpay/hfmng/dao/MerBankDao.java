/** *****************  JAVA头文件说明  ****************
 * file name  :  MerBankDao.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-9-25
 * *************************************************/ 

package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.MerBank;


/** ******************  类说明  *********************
 * class       :  MerBankDao
 * @author     :  xhf
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public interface MerBankDao extends EntityDao<MerBank> {
	
	public MerBank get(Map<String, String> mapWhere);
	
	public List<MerBank> findByMerId(String merId);
	
	public int updateMerBankModLock(MerBank merBank);
	
	public void saveMerBank(MerBank merBank);
	
	public int updateMerBank(MerBank merBank);

	public List<MerBank> findAll();
	
	public List<BankInfo> getMerBankInfo(String merId);

}
