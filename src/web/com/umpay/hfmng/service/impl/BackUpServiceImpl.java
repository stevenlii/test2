package com.umpay.hfmng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.umpay.hfmng.dao.BackUpDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.service.BackUpService;

/**
 * 
 * 
 * @ClassName:BackUpServiceImpl
 * @Description: 商户/商品报备Service
 * @version: 1.0
 * @author: lituo
 * @Create: 2014-7-15
 * 
 */
@Service
public class BackUpServiceImpl implements BackUpService {

	@Autowired
	private BackUpDao backupdao;

	/**
	 * 
	 * @Title: deleteBackUpOper
	 * @Description: 删除商户商品报备信息
	 * @param operList
	 * @return
	 * @author lituo
	 * @date 2014-7-15 下午05:31:32
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int deleteBackUpOper(String operList, String userid, int backupstat) throws BusinessException,
			DataAccessException {
		int result = backupdao.deleteBackUpOper(operList, userid);
		if (result == 0) {
			return result;
		}
		return backupdao.updateBackUpInfo(operList, userid, backupstat);
	}

	/**
	 * 
	 * @Title: submitBackUpOper
	 * @Description: 提交商户商品报备信息
	 * @param operList
	 * @return
	 * @author lituo
	 * @date 2014-7-15 下午05:31:32
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int submitBackUpOper(String operList, String userid, int backupstat) {
		int result = backupdao.updateBackUpOper(operList, userid);
		if (result == 0) {
			return result;
		}
		return backupdao.updateBackUpInfo(operList, userid, backupstat);
	}
}
