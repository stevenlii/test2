/** *****************  JAVA头文件说明  ****************
 * file name  :  AuditDaoImpl.java
 * owner      :  Administrator
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-8-30
 * *************************************************/

package com.umpay.hfmng.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.TimeUtil;
import com.umpay.hfmng.dao.AuditDao;
import com.umpay.hfmng.model.Audit;

/**
 * ****************** 类说明 ********************* class : AuditDaoImpl
 * 
 * @author : xhf
 * @version : 1.0 description :
 * @see : *
 ***********************************************/
@Repository
public class AuditDaoImpl extends EntityDaoImpl<Audit> implements AuditDao {

	/**
	 * ******************************************** method name : get modified :
	 * Administrator , 2012-8-30
	 * 
	 * @see : @see com.umpay.hfmng.dao.AuditDao#get(java.util.Map) *
	 *******************************************/
	public Audit get(Map<String, String> mapWhere) {
		return (Audit) this.get("Audit.Get", mapWhere);
	}
	
	public Audit getOneObj(Map<String, String> mapWhere) {
		return (Audit) this.get("Audit.getOneObj", mapWhere);
	}

	public Audit getBatchId(Map<String, String> mapWhere) {
		return (Audit) this.get("Audit.GetBatchId", mapWhere);
	}

	@SuppressWarnings("unchecked")
	public List<Audit> findByBatchId(String batchId) {
		Map map = new HashMap();
		map.put("batchId", batchId);
		return super.find("Audit.GetBatchId", map);
	}

	/**
	 * ******************************************** method name : insert
	 * modified : Administrator , 2012-8-30
	 * 
	 * @see : @see com.umpay.hfmng.base.EntityDao#insert(java.lang.Object) *
	 *******************************************/
	public void insert(Audit audit) throws DataAccessException {
		audit.setId(TimeUtil.date8().substring(2, 8)
				+ SequenceUtil.formatSequence(SequenceUtil.getInstance()
						.getSequence4File(Const.SEQ_FILENAME_AUDIT), 10));
		super.insert(audit);

	}

	/**
	 * ******************************************** method name : updateState
	 * modified : xhf , 2012-8-30
	 * 
	 * @see : @see
	 *      com.umpay.hfmng.dao.AuditDao#updateState(com.umpay.hfmng.model
	 *      .Audit) *
	 *******************************************/
	public int updateState(Audit audit) {
		return this.update("Audit.updateState", audit);
	}

	/**
	 * ******************************************** method name :
	 * getCheckFromAudit modified : zhaojunbao , 2012-9-3
	 * 
	 * @see : @see com.umpay.hfmng.dao.AuditDao#getCheckFromAudit(java.util.Map)
	 *      *
	 *******************************************/
	public List getCheckFromAudit(Map<String, String> mapWhere)
			throws DataAccessException {
		return this.find("Audit.checkFromAudit", mapWhere);
	}

	/**
	 * ******************************************** method name : getMerBankMap
	 * modified : xhf , 2012-11-19
	 * 
	 * @see : @see com.umpay.hfmng.dao.AuditDao#getMerBankMap(java.util.Map) *
	 *******************************************/
	public List getMerBankList(Map<String, String> mapWhere) {
		return this.find("Audit.checkAudit", mapWhere);
	}

	/**
	 * ******************************************** method name :
	 * checkModMerName modified : xuhuafeng , 2012-12-20
	 * 
	 * @see : @see com.umpay.hfmng.dao.AuditDao#checkModMerName(java.util.Map) *
	 *******************************************/
	public List checkModMerName(Map<String, String> mapWhere) {
		return this.find("Audit.checkMerName", mapWhere);
	}

	
	/** ********************************************
	 * method name   : checkMerGrade 
	 * modified      : xuhuafeng ,  2013-3-8
	 * @see          : @see com.umpay.hfmng.dao.AuditDao#checkMerGrade(java.util.Map)
	 * ********************************************/     
	public Audit checkMerGrade(Map<String, String> mapWhere) {
		return (Audit) this.get("Audit.checkMerGrade", mapWhere);
	}

	@SuppressWarnings("unchecked")
	public List getCheckExactlyFromAudit(Map<String, String> mapWhere) {
		return this.find("Audit.checkExactlyFromAudit", mapWhere);
	}
	
	public List checkModChnlName(Map<String, String> mapWhere) {
		return this.find("Audit.checkModChnlName", mapWhere);
	}

	
	/** ********************************************
	 * method name   : checkGoodsBankAdd 
	 * modified      : xuhuafeng ,  2013-5-3
	 * @see          : @see com.umpay.hfmng.dao.AuditDao#checkGoodsBankAdd(java.util.Map)
	 * ********************************************/     
	public int checkDataAdd(Map<String, String> mapWhere) {
		List list = this.find("Audit.checkDataAdd", mapWhere);
		return list == null ? 0 : list.size();
	}

	
	/** ********************************************
	 * method name   : batchUpdateState 
	 * modified      : xuhuafeng ,  2013-10-15
	 * @see          : @see com.umpay.hfmng.dao.AuditDao#batchUpdateState(com.umpay.hfmng.model.Audit)
	 * ********************************************/     
	public int batchUpdateState(Audit audit) {
		return this.update("Audit.batchUpdateState", audit);
	}
}
