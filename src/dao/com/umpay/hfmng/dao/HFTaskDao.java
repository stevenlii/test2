package com.umpay.hfmng.dao;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.HFTask;

/**
 * @ClassName: HFTaskDao
 * @Description: 定时任务数据库操作接口
 * @author helin
 * @date 2013-1-15 下午9:24:53
 */
public interface HFTaskDao extends EntityDao<HFTask> {

	/**
	 * @Title: getAllTaskList
	 * @Description: 获得所有任务的列表
	 * @param
	 * @return
	 * @author helin
	 * @date 2013-1-16 下午7:53:52
	 */
	public List<HFTask> getAllTaskList();
}
