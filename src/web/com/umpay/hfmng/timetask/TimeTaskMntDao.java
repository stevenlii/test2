package com.umpay.hfmng.timetask;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityDaoImpl;
import com.umpay.hfmng.model.HFTaskMnt;

/**
 * 
 * @ClassName: TimeTaskMntDao
 * @Description: 定时任务监控数据库操作接口
 * @author helin
 * @date 2013-1-19 下午4:38:51
 */
@Repository("timeTaskMntDao")
public class TimeTaskMntDao extends EntityDaoImpl<HFTaskMnt> implements TimeTaskMntDaoIfc {

	/**
	 * @Title: loadTaskMntList
	 * @Description: 获取计划触发时间在未来35分钟以内的，并且任务状态为：0-初始，
	 * @Description: 或者 1-触发失败，3-反馈超时，4-执行失败，并且异常次数<重试次数 而且需要重试的
	 * @param
	 * @return
	 * @author helin
	 * @date 2013-1-20 下午10:37:47
	 */
	@SuppressWarnings("unchecked")
	public List<HFTaskMnt> loadTaskMntList() {
		return (List<HFTaskMnt>) super.find("HFTaskMnt.loadTaskMntList");
	}
}
