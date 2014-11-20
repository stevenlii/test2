/** *****************  JAVA头文件说明  ****************
 * file name  :  HudanDao.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-1-14
 * *************************************************/ 

package com.umpay.hfmng.dao;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.Huadan;


/** ******************  类说明  *********************
 * class       :  HudanDao
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public interface HuadanDao extends EntityDao<Huadan>{
	public void saveHuadan(Huadan huadan)throws DataAccessException;

}
