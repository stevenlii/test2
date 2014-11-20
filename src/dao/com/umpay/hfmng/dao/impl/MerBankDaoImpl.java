/** *****************  JAVA头文件说明  ****************
 * file name  :  MerBankDaoImpl.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-9-25
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.MerBankDao;
import com.umpay.hfmng.model.BankInfo;
import com.umpay.hfmng.model.MerBank;


/** ******************  类说明  *********************
 * class       :  MerBankDaoImpl
 * @author     :  xhf
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Repository
public class MerBankDaoImpl extends EntityDaoImpl<MerBank> implements MerBankDao {

	/** ********************************************
	 * method name   : get 
	 * modified      : xhf ,  2012-9-25
	 * @see          : @see com.umpay.hfmng.dao.MerBankDao#get(java.util.Map)
	 * ********************************************/
	public MerBank get(Map<String, String> mapWhere) {
		return (MerBank) this.get("MerBank.Get", mapWhere);
	}

	
	/** ********************************************
	 * method name   : findByMerId 
	 * modified      : xhf ,  2012-9-28
	 * @see          : @see com.umpay.hfmng.dao.MerBankDao#findByMerId(java.lang.String)
	 * ********************************************/     
	@SuppressWarnings("unchecked")
	public List<MerBank> findByMerId(String merId) {
		Map map = new HashMap();
		map.put("merId", merId);
		return super.find("MerBank.Find", map);
	}


	
	/** ********************************************
	 * method name   : updateMerBankModLock 
	 * modified      : xhf ,  2012-10-8
	 * @see          : @see com.umpay.hfmng.dao.MerBankDao#updateMerBankModLock(com.umpay.hfmng.model.MerBank)
	 * ********************************************/     
	public int updateMerBankModLock(MerBank merBank) {
		return this.update("MerBank.updateMerBankLock", merBank);
	}


	
	/** ********************************************
	 * method name   : saveMerBank 
	 * modified      : xhf ,  2012-10-10
	 * @see          : @see com.umpay.hfmng.dao.MerBankDao#saveMerBank(com.umpay.hfmng.model.MerBank)
	 * ********************************************/     
	public void saveMerBank(MerBank merBank) {
		this.save("MerBank.insertMerBank", merBank);
	}


	
	/** ********************************************
	 * method name   : updateMerBank 
	 * modified      : xhf ,  2012-10-10
	 * @see          : @see com.umpay.hfmng.dao.MerBankDao#updateMerBank(com.umpay.hfmng.model.MerBank)
	 * ********************************************/     
	public int updateMerBank(MerBank merBank) {
		int rs = this.update("MerBank.updateMerBank", merBank);
		return rs;
	}

	@SuppressWarnings("unchecked")
	public List<MerBank> findAll() {
		return super.find("MerBank.FindHN");
	}
	
	/**
	 * 
	 * @Title: getMerBankInfo
	 * @Description: 获取商户银行
	 * @param merId
	 * @return
	 * @author lituo
	 * @date 2014-8-11 下午04:56:46
	 */
	@SuppressWarnings("unchecked")
	public List<BankInfo> getMerBankInfo(String merId){
		Map map = new HashMap();
		map.put("merId", merId);
		return super.find("MerBank.getMerBankInfo",map);
	}
}
