package com.umpay.hfmng.dao;


import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.MerCnf;

public interface MerCnfDao extends EntityDao<MerCnf>{
	
	public MerCnf get(String id);
	public void saveMerCnf(MerCnf merCnf);
	public int updateCert(MerCnf merCnf);
}
