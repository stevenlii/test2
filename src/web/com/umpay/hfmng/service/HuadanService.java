/** *****************  JAVA头文件说明  ****************
 * file name  :  HuadanService.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-1-14
 * *************************************************/ 

package com.umpay.hfmng.service;

import org.springframework.dao.DataAccessException;

import com.umpay.hfmng.model.Huadan;


/** ******************  类说明  *********************
 * class       :  HuadanService
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public interface HuadanService {
	public String saveHuadan(Huadan huadan) throws DataAccessException, Exception;
}
