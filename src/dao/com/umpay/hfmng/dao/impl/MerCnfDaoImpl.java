package com.umpay.hfmng.dao.impl;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.MerCnfDao;
import com.umpay.hfmng.model.MerCnf;

@Repository("merCnfDaoImpl")
public class MerCnfDaoImpl extends EntityDaoImpl<MerCnf> implements MerCnfDao {

	public MerCnf get(String id)  {
		return (MerCnf) this.get("MerCnf.Get", id);
	}
	public void saveMerCnf(MerCnf merCnf)  {
		this.save("MerCnf.insertMerCnf", merCnf);
	}
	public int updateCert(MerCnf merCnf){
		return this.update("MerCnf.updateCert", merCnf);
	}
}
