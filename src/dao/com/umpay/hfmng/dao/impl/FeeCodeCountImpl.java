/** *****************  JAVA头文件说明  ****************
 * file name  :  FeeCodeCountImpl.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-11-7
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.FeeCodeCountDao;
import com.umpay.hfmng.model.FeeCodeCount;


/** ******************  类说明  *********************
 * class       :  FeeCodeCountImpl
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Repository("feeCodeCountImpl")
public class FeeCodeCountImpl extends EntityDaoImpl<FeeCodeCount> implements FeeCodeCountDao{

	
	/** ********************************************
	 * method name   : getUseCount 
	 * modified      : zhaojunbao ,  2012-11-7
	 * @see          : @see com.umpay.hfmng.dao.FeeCodeCountDao#getUseCount()
	 * ********************************************/     
	@SuppressWarnings("unchecked")
	public List<FeeCodeCount> getUseCount() {
		List<FeeCodeCount> list=null;
		list= super.find("FeeCodeCount.getCount");
		return list;
	}

	
	/** ********************************************
	 * method name   : getMatch 
	 * modified      : zhaojunbao ,  2012-11-7
	 * @see          : @see com.umpay.hfmng.dao.FeeCodeCountDao#getMatch()
	 * ********************************************/     
	public List<FeeCodeCount> getMatch() {
		List<FeeCodeCount> list=null;
		list= super.find("FeeCodeCount.getMatch");
		return list;
	}
/**
 * ********************************************
 * method name   : updateCount 
 * modified      : zhaojunbao ,  2012-11-7
 * @see          : @see com.umpay.hfmng.dao.FeeCodeCountDao#updateCount(com.umpay.hfmng.model.FeeCodeCount)
 * *******************************************
 */
	public int updateCount(FeeCodeCount feeCodeCount){
		return super.update("FeeCodeCount.update", feeCodeCount);
	}


	
	/** ********************************************
	 * method name   : insertCount 
	 * modified      : zhaojunbao ,  2012-11-7
	 * @see          : @see com.umpay.hfmng.dao.FeeCodeCountDao#insertCount(com.umpay.hfmng.model.FeeCodeCount)
	 * ********************************************/     
	public void insertCount(FeeCodeCount feeCodeCount) {
		super.save("FeeCodeCount.insert", feeCodeCount);
		
	}
}
