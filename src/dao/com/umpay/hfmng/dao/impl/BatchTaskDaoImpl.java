package com.umpay.hfmng.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.dao.BatchTaskDao;
import com.umpay.hfmng.model.BatchTask;

@Repository("batchTaskDaoImpl")
public class BatchTaskDaoImpl extends EntityOffLineDaoImpl<BatchTask> implements BatchTaskDao {

	public void insertBatchTask(BatchTask batchTask) {
		super.save("BatchTask.insert", batchTask);
	}

	public BatchTask get(Map<String, String> mapWhere) {
		return (BatchTask) this.get("BatchTask.get", mapWhere);
	}

	public void updateBatchTask(Map<String, Object> paraMap) {
		this.update("BatchTask.updateBatchTask", paraMap);
	}

	@SuppressWarnings("unchecked")
	public List<BatchTask> findAll() {
		return super.find("BatchTask.findAll");
	}
}
