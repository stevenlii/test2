package com.umpay.hfmng.dao;

import java.util.List;
import java.util.Map;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.HNSynTask;


/** ******************  类说明  *********************
 * class       :  HenanDataSyncDao
 * @author     :  chenwei
 * @version    :  1.0  
 * description :  2013.8.7
 * @see        :                        
 * ************************************************/

public interface HenanDataSyncDao extends EntityDao<HNSynTask> {
		
	/**
	 * @Title: insertSyncBatch
	 * @Description: 新增一条全量同步任务记录
	 * @param
	 * @author chewnei
	 */
	public void insertSyncBatch(HNSynTask HNSynTask);

	public HNSynTask get(Map<String, String> mapWhere);

	public void updateSynTask(Map<String, Object> mapWhere);

	public List<HNSynTask> findAll();


}
