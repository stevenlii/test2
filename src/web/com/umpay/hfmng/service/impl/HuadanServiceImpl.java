/** *****************  JAVA头文件说明  ****************
 * file name  :  HuadanServiceImpl.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-1-14
 * *************************************************/ 

package com.umpay.hfmng.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.dao.HuadanDao;
import com.umpay.hfmng.exception.DAOException;
import com.umpay.hfmng.model.Huadan;
import com.umpay.hfmng.service.HuadanService;


/** ******************  类说明  *********************
 * class       :  HuadanServiceImpl
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Service
public class HuadanServiceImpl implements HuadanService{

	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private HuadanDao huadanDao;
	/** ********************************************
	 * method name   : saveHuadan 
	 * modified      : zhaojunbao ,  2013-1-14
	 * @see          : @see com.umpay.hfmng.service.HuadanService#saveHuadan(com.umpay.hfmng.model.Huadan)
	 * ********************************************/     
	public String saveHuadan(Huadan huadan) throws DataAccessException,
			Exception {
		String result="0";
		try {
			huadanDao.saveHuadan(huadan);
			result="1";
			log.info("话单信息保存成功"+huadan.toString());
			return result; // 返回成功
		} catch (Exception e) {//TODO
			log.error("话单信息保存失败"+huadan.toString(),e);
			throw new DAOException("保存失败！");	
		}
	}

}
