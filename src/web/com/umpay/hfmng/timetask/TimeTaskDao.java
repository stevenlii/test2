package com.umpay.hfmng.timetask;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.model.HFTask;

/**
 * @ClassName: TimeTaskDao
 * @Description: 定时任务数据库操作实现
 * @author helin
 * @date 2013-1-15 下午9:25:41
 */
@Repository("timeTaskDao")
public class TimeTaskDao extends EntityDaoImpl<HFTask> implements TimeTaskDaoIfc {
	/**
	 * @Title: getAllTaskList
	 * @Description: 获得所有任务的列表
	 * @param
	 * @return
	 * @author helin
	 * @date 2013-1-16 下午7:53:52
	 */
	@SuppressWarnings("unchecked")
	public List<HFTask> getAllTaskList() {
		return (List<HFTask>) super.find("HFTask.getTaskList");
	}

	/**
	 * @Title: loadTaskList
	 * @Description: 获取下次运行时间在未来35分钟以内的，并且启用的任务
	 * @param
	 * @return List<HFTask>
	 * @author helin
	 * @date 2013-1-20 下午3:50:29
	 */
	@SuppressWarnings("unchecked")
	public List<HFTask> loadTaskList() {
		return (List<HFTask>) super.find("HFTask.loadTaskList");
	}

}
