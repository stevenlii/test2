package com.umpay.hfmng.timetask;

import java.sql.Timestamp;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.SequenceUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.model.HFTask;
import com.umpay.hfmng.model.HFTaskMnt;
import com.umpay.hfmng.model.TaskRule;

@Service("timeTaskService")
public class TimeTaskService {
	protected Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TimeTaskDaoIfc timeTaskFBDao;
	@Autowired
	private TimeTaskMntDaoIfc timeTaskFBMntDao;

	/**
	 * @Title: TaskRunFeefback
	 * @Description: 保存定时任务反馈信息
	 * @param
	 * @param taskRpid
	 * @param retCode
	 * @param retMsg
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-19 下午6:03:13
	 */
	public void taskRunFeefback(String taskRpid, String retCode, String retMsg) throws Exception {
		HFTaskMnt hfTaskMnt = timeTaskFBMntDao.get(taskRpid);
		if (null == hfTaskMnt)
			throw new BusinessException("没有找到对应任务，请检查任务流水！");
		// 只有发送过触发报文的任务才能进行反馈。
		if (Const.TASK_STATE_SENDSUCCEED != hfTaskMnt.getState() && Const.TASK_STATE_SENDERROR != hfTaskMnt.getState())
			throw new BusinessException("任务状态为：" + ParameterPool.timerTaskStates.get("" + hfTaskMnt.getState())
					+ "，不能被反馈！");

		HFTaskMnt updateTaskMnt = new HFTaskMnt();
		updateTaskMnt.setTaskRpid(taskRpid);
		if ("0000".equals(retCode)) {
			// 执行成功直接更新状态。
			updateTaskMnt.setState(Const.TASK_STATE_RUNSUCCEED);
		} else {
			// 如果执行失败,还要看是否错误重试。
			updateTaskMnt.setState(Const.TASK_STATE_RUNERROR);
			// 这里没多线程的问题，可以不考虑并发。
			// 这里没多线程的问题，可以不考虑并发。
			if (-1 == hfTaskMnt.getErrorTimes())
				updateTaskMnt.setErrorTimes(1);
			else
				updateTaskMnt.setErrorTimes(hfTaskMnt.getErrorTimes() + 1);
			// 如果需要错误重试，则要修改计划执行时间
			if (Const.TISTRY_YES == hfTaskMnt.getIsReTry()) {
				updateTaskMnt.setPlanRunTime(StringUtil.getTSTime_SpaceXMin(hfTaskMnt.getReTryInterval()));
			}
		}
		if (255 < retMsg.length())
			retMsg = retMsg.substring(0, 253) + "...";
		updateTaskMnt.setErrorInf(retMsg);
		updateTaskMnt.setModTime(StringUtil.getTSTime());
		if (1 != timeTaskFBMntDao.update(updateTaskMnt)) {
			throw new BusinessException("任务监控更新失败!");
		}
		// 刷新监控信息缓存。
		refHFTaskMnts(updateTaskMnt);

	}

	// ***********************任务派发入队**************************************************************************************************

	/**
	 * @Title: loadTimeTaskInfo
	 * @Description: 用数据库的数据，刷新任务信息缓存
	 * @param
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-20 下午4:10:17
	 */
	public void loadTimeTaskInfo() throws Exception {
		// 首先查询定时任务信息。
		List<HFTask> hfTasks = timeTaskFBDao.loadTaskList();
		// 再清空MAP，重新put。
		ParameterPool.hfTasks.clear();

		for (HFTask hfTask : hfTasks) {
			try {
				ParameterPool.hfTasks.put(hfTask.getTaskId(), hfTask);
			} catch (Exception e) {
				log.error("定时任务缓存信息加载失败！", e);
			}
		}
		hfTasks = null;
	}

	/**
	 * @Title: timeTaskDispatch
	 * @Description: 根据任务配置，进行任务派发。
	 * @param
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-20 下午4:15:13
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void doDispatch(HFTask hfTask) throws Exception {

		// 向监控表插入记录。计算新的下次派发时间，并更新到任务表。
		HFTaskMnt hfTaskMnt = new HFTaskMnt();
		hfTaskMnt.setTaskRpid("TRP" + StringUtil.get14Time()
				+ SequenceUtil.getInstance().getSequence(Const.SEQ_TASK, 6));
		hfTaskMnt.setTaskId(hfTask.getTaskId());
		hfTaskMnt.setSendType(Const.TSEND_TYPE_AUTO);
		hfTaskMnt.setPlanRunTime(hfTask.getNextRunTime());
		hfTaskMnt.setTaskRuleDesc(hfTask.getTaskRuleDesc());
		hfTaskMnt.setTaskDesc(hfTask.getTaskDesc());
		hfTaskMnt.setPostUrl(hfTask.getPostUrl());
		hfTaskMnt.setIsReTry(hfTask.getIsReTry());
		hfTaskMnt.setReTryTimes(hfTask.getReTryTimes());
		hfTaskMnt.setReTryInterval(hfTask.getReTryInterval());
		hfTaskMnt.setRetTimeout(hfTask.getRetTimeout());
		hfTaskMnt.setState(Const.TASK_STATE_START);
		timeTaskFBMntDao.insert(hfTaskMnt);

		// 获得定时规则
		TaskRule taskRule = (TaskRule) JSONObject.toBean(JSONObject.fromObject(hfTask.getTaskRule()), TaskRule.class);
		HFTask updateHFTask = new HFTask();
		// 计算新的下次派发时间,更新到任务表
		Timestamp nextRunTime = StringUtil.countNextRunTime(hfTask.getNextRunTime(), taskRule);
		updateHFTask.setNextRunTime(nextRunTime);
		updateHFTask.setModTime(StringUtil.getTSTime());
		updateHFTask.setTaskId(hfTaskMnt.getTaskId());
		if (1 != timeTaskFBDao.update(updateHFTask)) {
			throw new BusinessException("任务下次执行时间更新失败!");
		}
		// 刷新监控信息缓存。
		refHFTaskMnts(hfTaskMnt);
		// 刷新定时信息缓存。
		refHFTasks(updateHFTask);

	}

	// **************************任务报文发送***********************************************************************************************
	/**
	 * @Title: loadTimeTaskMntInfo
	 * @Description: 用数据库的数据，刷新任务监控信息缓存
	 * @param
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-20 下午10:53:51
	 */
	public void loadTimeTaskMntInfo() throws Exception {
		// 首先查询定时任务信息。
		List<HFTaskMnt> hfTaskMnts = timeTaskFBMntDao.loadTaskMntList();
		// 再清空MAP，重新put。
		ParameterPool.hfTaskMnts.clear();

		for (HFTaskMnt hfTaskMnt : hfTaskMnts) {
			try {
				ParameterPool.hfTaskMnts.put(hfTaskMnt.getTaskRpid(), hfTaskMnt);
			} catch (Exception e) {
				log.error("定时任务监控信息加载失败！", e);
			}
		}
		hfTaskMnts = null;
	}

	/**
	 * @Title: modifyTaskMnt
	 * @Description: 修改定时监控任务
	 * @param
	 * @param hfTaskMnt
	 * @throws Exception
	 * @author helin8
	 * @date 2013-1-19 下午4:44:09
	 */
	public void modifyTaskMnt(HFTaskMnt hfTaskMnt) throws Exception {
		hfTaskMnt.setModTime(StringUtil.getTSTime());
		if (1 != timeTaskFBMntDao.update(hfTaskMnt)) {
			throw new BusinessException("任务监控更新失败!");
		}
		// 刷新缓存。
		refHFTaskMnts(hfTaskMnt);
	}

	/**
	 * @Title: refHFTasks
	 * @Description: 用于刷新定时任务缓存。
	 * @param
	 * @param hfTask
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-21 下午1:45:59
	 */
	public synchronized void refHFTasks(HFTask hfTask) throws Exception {
		HFTask oldHFTask = ParameterPool.hfTasks.get(hfTask.getTaskId());
		if (null != oldHFTask) {
			if (null != hfTask.getNextRunTime())
				oldHFTask.setNextRunTime(hfTask.getNextRunTime());
			if (-1 != hfTask.getState())
				oldHFTask.setState(hfTask.getState());
			if (null != hfTask.getModTime())
				oldHFTask.setModTime(hfTask.getModTime());
		} else {
			ParameterPool.hfTasks.put(hfTask.getTaskId(), hfTask);
		}

	}

	/**
	 * @Title: refHFTaskMnts
	 * @Description: 用于刷新定时任务监控缓存。
	 * @param
	 * @param hfTaskMnt
	 * @throws Exception
	 * @author helin
	 * @date 2013-1-21 下午1:59:14
	 */
	public synchronized void refHFTaskMnts(HFTaskMnt hfTaskMnt) throws Exception {
		HFTaskMnt oldHFTaskMnt = ParameterPool.hfTaskMnts.get(hfTaskMnt.getTaskRpid());
		if (null != oldHFTaskMnt) {
			if (-1 != hfTaskMnt.getErrorTimes())
				oldHFTaskMnt.setErrorTimes(hfTaskMnt.getErrorTimes());
			if (null != hfTaskMnt.getPlanRunTime())
				oldHFTaskMnt.setPlanRunTime(hfTaskMnt.getPlanRunTime());
			if (null != hfTaskMnt.getTaskRunTime())
				oldHFTaskMnt.setTaskRunTime(hfTaskMnt.getTaskRunTime());
			if (-1 != hfTaskMnt.getState())
				oldHFTaskMnt.setState(hfTaskMnt.getState());
			if (!"".equals(hfTaskMnt.getDeadTime()))
				oldHFTaskMnt.setDeadTime(hfTaskMnt.getDeadTime());
			if (null != hfTaskMnt.getModTime())
				oldHFTaskMnt.setModTime(hfTaskMnt.getModTime());
			if (!"".equals(hfTaskMnt.getErrorInf()))
				oldHFTaskMnt.setErrorInf(hfTaskMnt.getErrorInf());
		} else {
			ParameterPool.hfTaskMnts.put(hfTaskMnt.getTaskRpid(), hfTaskMnt);
		}
	}

}
