package com.umpay.hfmng.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.dao.MerGradeDao;
import com.umpay.hfmng.model.MerGrade;
@Repository("merGradeDaoImpl")
public class MerGradeDaoImpl extends EntityOffLineDaoImpl<MerGrade> implements MerGradeDao {

	/** ********************************************
	 * method name   : get 
	 * modified      : xuhuafeng ,  2013-2-22
	 * @see          : @see com.umpay.hfmng.dao.MerGradeDao#get(java.util.Map)
	 * ********************************************/     
	public MerGrade get(Map<String, String> mapWhere) {
		return (MerGrade) this.get("MerGrade.Get", mapWhere);
	}
	
	/** ********************************************
	 * method name   : updateModLock 
	 * modified      : xuhuafeng ,  2013-2-25
	 * @see          : @see com.umpay.hfmng.dao.MerGradeDao#updateModLock(int)
	 * ********************************************/     
	public int updateModLock(MerGrade merGrade) {
		return this.update("MerGrade.updateModLock", merGrade);
	}

	public List<MerGrade> find(Map<String,String> mapWhere){
		return (List<MerGrade>) this.find("MerGrade.Find", mapWhere);
	}
	public void saveMerGrade(MerGrade merGrade) {
		this.save("MerGrade.insertMerGrade", merGrade);
	}

	public void updateMerGrade(MerGrade merGrade) {
		this.save("MerGrade.updateMerGrade", merGrade);
	}
	
	public void updateMerGradeNoAudit(MerGrade merGrade) {
		this.save("MerGrade.updateMerGradeNoAudit", merGrade);
	}
}
