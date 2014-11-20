package com.umpay.hfmng.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.FeeCodeDao;
import com.umpay.hfmng.model.FeeCode;


@Repository("feeCodeDaoImpl")
public class FeeCodeDaoImpl extends EntityDaoImpl<FeeCode> implements FeeCodeDao{
	public void saveFeeCode(FeeCode feeCode) {
		this.save("FeeCode.insertFeeCode", feeCode);
	}
	
	public FeeCode get(String serviceId){
		return (FeeCode)this.get("FeeCode.Get",serviceId);
	}
	public int update(FeeCode feeCode){
		return  this.update("FeeCode.updateFeeCode", feeCode);
	}
	public FeeCode get(Map<String, String> mapWhere) {
		return (FeeCode) this.get("FeeCode.Get",mapWhere);
	}
	public int updateFeeCodeLock(FeeCode feeCode) {
		return  this.update("FeeCode.updateFeeCodeState", feeCode);
	}
}
