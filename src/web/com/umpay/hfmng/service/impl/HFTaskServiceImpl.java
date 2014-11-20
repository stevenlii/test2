package com.umpay.hfmng.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.dao.HFTaskDao;
import com.umpay.hfmng.dao.HFTaskMntDao;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.HFTask;
import com.umpay.hfmng.model.HFTaskMnt;
import com.umpay.hfmng.model.TaskRule;
import com.umpay.hfmng.service.HFTaskService;

@Service
public class HFTaskServiceImpl implements HFTaskService {
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private HFTaskDao hFTaskDao;

	@Autowired
	private HFTaskMntDao hfTaskMntDao;

	/**
	 * @Title: modifyTask
	 * @Description: 修改一个定时任务
	 * @param hFTask
	 * @param taskRule
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-11 下午3:39:41
	 */
	public void modifyTask(HFTask hFTask, TaskRule taskRule) throws Exception {
		Map<String, Object> returnValues = StringUtil.countFirstRunTime(taskRule);
		hFTask.setNextRunTime((Timestamp) returnValues.get("nextRunTime"));
		hFTask.setModTime(StringUtil.getTSTime());
		String taskRuleJson = JSONObject.fromObject(taskRule).toString();
		hFTask.setTaskRule(taskRuleJson);
		hFTask.setTaskRuleDesc((String) returnValues.get("ruleDesc"));
		if (1 != hFTaskDao.update(hFTask)) {
			throw new BusinessException("定时任务更新失败!");
		}
		// 刷新缓存。
		ParameterPool.hfTasks.put(hFTask.getTaskId(), hFTask);
	}

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
	public void createTask(HFTask hFTask, TaskRule taskRule) throws Exception {
		hFTask.setTaskId("TSK" + StringUtil.get8Date() + SequenceUtil.getInstance().getSequence(Const.SEQ_TASK, 5));
		Map<String, Object> returnValues = StringUtil.countFirstRunTime(taskRule);
		hFTask.setNextRunTime((Timestamp) returnValues.get("nextRunTime"));
		String taskRuleJson = JSONObject.fromObject(taskRule).toString();
		hFTask.setTaskRule(taskRuleJson);
		hFTask.setTaskRuleDesc((String) returnValues.get("ruleDesc"));
		hFTaskDao.insert(hFTask);
		// 刷新缓存。
		ParameterPool.hfTasks.put(hFTask.getTaskId(), hFTask);
	}

	/**
	 * @Title: getTask
	 * @Description: 用主键获得一个定时任务
	 * @param taskId
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-11 下午3:39:48
	 */
	public HFTask getTask(String taskId) throws Exception {
		return hFTaskDao.get(taskId);
	}

	/**
	 * @Title: manualRunTask
	 * @Description: 手动执行一个任务，其实就是往任务监控表中插入一条记录。然后定时再跑。
	 * @param
	 * @param taskId
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-15 下午8:51:50
	 */
	public void manualRunTask(String taskId) throws Exception {
		// 首先查询定时任务信息。
		HFTask hfTask = getTask(taskId);
		HFTaskMnt hfTaskMnt = new HFTaskMnt();
		hfTaskMnt.setTaskRpid("TRP" + StringUtil.get14Time()
				+ SequenceUtil.getInstance().getSequence(Const.SEQ_TASK, 6));
		hfTaskMnt.setTaskId(taskId);
		hfTaskMnt.setTaskRuleDesc(hfTask.getTaskRuleDesc());
		hfTaskMnt.setTaskDesc(hfTask.getTaskDesc());
		hfTaskMnt.setPostUrl(hfTask.getPostUrl());
		hfTaskMnt.setIsReTry(hfTask.getIsReTry());
		hfTaskMnt.setReTryTimes(hfTask.getReTryTimes());
		hfTaskMnt.setReTryInterval(hfTask.getReTryInterval());
		hfTaskMnt.setRetTimeout(hfTask.getRetTimeout());
		hfTaskMnt.setSendType(Const.TSEND_TYPE_MANUAL);
		hfTaskMnt.setPlanRunTime(StringUtil.getTSTime());
		hfTaskMnt.setState(Const.TASK_STATE_START);
		hfTaskMntDao.insert(hfTaskMnt);
		// 刷新缓存。
		ParameterPool.hfTaskMnts.put(hfTaskMnt.getTaskRpid(), hfTaskMnt);
	}

	/**
	 * @Title: getAllTaskList
	 * @Description: 获得所有任务的列表
	 * @param
	 * @return
	 * @author helin
	 * @date 2013-1-17 下午2:21:20
	 */
	public List<HFTask> getAllTaskList() {
		return hFTaskDao.getAllTaskList();
	}

	/**
	 * @Title: getTask
	 * @Description: 用任务流水号获得一个任务监控信息
	 * @param taskId
	 * @return
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-11 下午3:39:48
	 */
	public HFTaskMnt getTaskMntInfo(String taskRpid) throws Exception {
		return hfTaskMntDao.get(taskRpid);
	}
}
