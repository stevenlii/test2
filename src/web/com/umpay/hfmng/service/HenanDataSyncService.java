package com.umpay.hfmng.service;

import java.util.Map;

import com.umpay.hfmng.model.HNSynTask;
import com.umpay.sso.org.User;

public interface HenanDataSyncService {

	String singleMerBankSync(String id);

	String singleGoodsBankSync(String id);

	public void batchDataSync(String sendFlag, User user);

	HNSynTask get(Map<String, String> mapWhere);

	String checkAll();
	
}