package com.umpay.hfmng.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.dao.HenanDataSyncDao;
import com.umpay.hfmng.model.HNSynTask;

@Repository("henanDataSyncDaoImpl")
public class HenanDataSyncDaoImpl extends EntityOffLineDaoImpl<HNSynTask> implements HenanDataSyncDao {

	public void insertSyncBatch(HNSynTask HNSynTask) {
		super.save("HNSynTask.insert", HNSynTask);
	}

	public HNSynTask get(Map<String, String> mapWhere) {
		return (HNSynTask) this.get("HNSynTask.get", mapWhere);
	}

	public void updateSynTask(Map<String, Object> mapWhere) {
		this.update("HNSynTask.updateSynTask", mapWhere);
	}

	public List<HNSynTask> findAll() {
		return super.find("HNSynTask.findAll");
	}
}
