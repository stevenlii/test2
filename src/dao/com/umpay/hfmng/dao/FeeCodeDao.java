package com.umpay.hfmng.dao;

import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.FeeCode;


public interface FeeCodeDao extends EntityDao<FeeCode>{
	public void saveFeeCode(FeeCode feeCode);
	public FeeCode get(String serviceId);
	public int update(FeeCode feeCode);
	public FeeCode get(Map<String, String> mapWhere);
	public int updateFeeCodeLock(FeeCode feeCode);
}
