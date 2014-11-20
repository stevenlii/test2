package com.umpay.hfmng.service;

import java.util.List;

import com.umpay.hfmng.model.HFTask;
import com.umpay.hfmng.model.HFTaskMnt;
import com.umpay.hfmng.model.TaskRule;

/**
 * @ClassName: CouponTaskService
 * @Description: 定时任务相关服务接口
 * @author helin
 * @date 2013-1-11 下午3:39:10
 */
public interface HFTaskService {

	/**
	 * @Title: modifyTask
	 * @Description: 修改一个定时任务
	 * @param hFTask
	 * @param taskRule
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-11 下午3:39:41
	 */
	public void modifyTask(HFTask hFTask, TaskRule taskRule) throws Exception;

	/**
	 * @Title: createTask
	 * @Description: 创建一个定时任务
	 * @param hFTask
	 * @param taskRule
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-11 下午3:39:45
	 */
	public void createTask(HFTask hFTask, TaskRule taskRule) throws Exception;

	/**
	 * @Title: getTask
	 * @Description: 用主键获得一个定时任务
	 * @param taskId
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-11 下午3:39:48
	 */
	public HFTask getTask(String taskId) throws Exception;

	/**
	 * @Title: manualRunTask
	 * @Description: 手动执行一个任务，其实就是往任务监控表中插入一条记录。然后定时再跑。
	 * @param
	 * @param taskId
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-15 下午8:51:50
	 */
	public void manualRunTask(String taskId) throws Exception;

	/**
	 * @Title: getAllTaskList
	 * @Description: 获得所有任务的列表
	 * @param
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-17 下午2:24:40
	 */
	public List<HFTask> getAllTaskList() throws Exception;

	/**
	 * @Title: getTask
	 * @Description: 用任务流水号获得一个任务监控信息
	 * @param taskId
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-11 下午3:39:48
	 */
	public HFTaskMnt getTaskMntInfo(String taskRpid) throws Exception;
}
