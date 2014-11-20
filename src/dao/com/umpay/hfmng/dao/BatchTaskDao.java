package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.BatchTask;


/**
 * ******************  类说明  ******************
 * class       :  BatchTaskDao
 * date        :  2013-12-26 
 * @author     :  LiZhen
 * @version    :  V1.0  
 * description :  
 * @see        :                         
 * **********************************************
 */
public interface BatchTaskDao extends EntityDao<BatchTask> {
		
	public void insertBatchTask(BatchTask batchTask);

	public BatchTask get(Map<String, String> mapWhere);

	public void updateBatchTask(Map<String, Object> mapWhere);

	public List<BatchTask> findAll();

}
