/** *****************  JAVA头文件说明  ****************
 * file name  :  HuadanDaoImpl.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-1-14
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.HuadanDao;
import com.umpay.hfmng.model.Huadan;



/** ******************  类说明  *********************
 * class       :  HuadanDaoImpl
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Repository("huadanDaoImpl")
public class HuadanDaoImpl extends EntityDaoImpl<Huadan> implements HuadanDao{

	
	/** ********************************************
	 * method name   : saveHuadan 
	 * modified      : zhaojunbao ,  2013-1-14
	 * @see          : @see com.umpay.hfmng.dao.HuadanDao#saveHuadan(com.umpay.hfmng.model.Huadan)
	 * ********************************************/     
	
	public void saveHuadan(Huadan huadan) throws DataAccessException {
		this.save("Huadan.Insert",huadan);
	}

}
