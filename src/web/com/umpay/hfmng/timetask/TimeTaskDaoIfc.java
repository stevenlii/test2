package com.umpay.hfmng.timetask;

import java.util.List;

import com.umpay.hfmng.base.EntityDao;
import com.umpay.hfmng.model.HFTask;

/**
 * @ClassName: TimeTaskDaoIfc
 * @Description: 定时任务数据库操作接口
 * @author helin
 * @date 2013-1-15 下午9:24:53
 */
public interface TimeTaskDaoIfc extends EntityDao<HFTask> {

	/**
	 * @Title: getAllTaskList
	 * @Description: 获得所有任务的列表
	 * @param
	 * @return
	 * @author helin
	 * @date 2013-1-16 下午7:53:52
	 */
	public List<HFTask> getAllTaskList();

	/**
	 * @Title: loadTaskList
	 * @Description: 获取下次运行时间在未来35分钟以内的，并且启用的任务
	 * @param
	 * @return List<HFTask>
	 * @author helin
	 * @date 2013-1-20 下午3:50:29
	 */
	public List<HFTask> loadTaskList();
}
