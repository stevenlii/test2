package com.umpay.hfmng.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.dao.HFTaskDao;
import com.umpay.hfmng.model.HFTask;

/**
 * @ClassName: HFTaskDaoImpl
 * @Description: 定时任务数据库操作实现
 * @author helin
 * @date 2013-1-15 下午9:25:41
 */
@Repository
public class HFTaskDaoImpl extends EntityDaoImpl<HFTask> implements HFTaskDao {
	/**
	 * @Title: getAllTaskList
	 * @Description: 获得所有任务的列表
	 * @param 
	 * @return
	 * @author helin
	 * @date 2013-1-16 下午7:53:52
	 */
	@SuppressWarnings("unchecked")
	public List<HFTask> getAllTaskList(){
		return (List<HFTask>) super.find("HFTask.getTaskList");
	}
}
