package com.umpay.hfmng.timetask;

import org.apache.log4j.Logger;

import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.model.HFTaskMnt;

/**
 * @ClassName: TimeTaskDispatch
 * @Description: 进行报文发送或返回响应超时处理。
 * @author helin
 * @date 2013-1-20 下午3:14:36
 */
public class TimeTaskSend extends Thread {

	protected Logger log = Logger.getLogger(this.getClass());

	public TimeTaskSend(String threadName) {
		super(threadName);
	}

	/**
	 * @Title: run
	 * @Description: 进行报文发送或返回响应超时处理。据从数据库加载到缓存中，然后再从缓存中获得数据进行处理。 缓存的加载没30分钟一次，处理每分钟一次。
	 * @param
	 * @author helin
	 * @date 2013-1-20 下午3:21:51
	 */
	@Override
	public synchronized void run() {
		int loadCount = 0;
		TimeTaskService ttService = (TimeTaskService) SpringContextUtil.getBean("timeTaskService");
		while (true) {
			try {
				// 将数据从数据库加载到缓存中。
				if (loadCount % 30 == 0) {// 30分钟加载一次，不能随便改这个时间间隔，会影响逻辑
					log.info("********开始加载定时任务监控缓存。***");
					ttService.loadTimeTaskMntInfo();
					loadCount = 1;
					log.info("********定时任务监控缓存加载完毕。***");
				} else {
					loadCount++;
				}

				// 再从缓存中获得数据进行处理。处理每分钟一次。
				// 遍历缓存中的定时任务监控信息
				for (HFTaskMnt hfTaskMnt : ParameterPool.hfTaskMnts.values()) {
					try {
						// 首先判断任务执行状态，如果是2-触发成功，就表示需要做定时反馈超时监控。
						if (Const.TASK_STATE_SENDSUCCEED == hfTaskMnt.getState()) {
							// 判断是否超时
							if (0 < StringUtil.get19Time().compareTo(hfTaskMnt.getDeadTime())) {
								// 如果超时更新状态，错误次数等。
								HFTaskMnt updateHFTaskMnt = new HFTaskMnt();
								updateHFTaskMnt.setTaskRpid(hfTaskMnt.getTaskRpid());
								updateHFTaskMnt.setState(Const.TASK_STATE_FBTIMEOUT);
								updateHFTaskMnt.setModTime(StringUtil.getTSTime());
								// 这里没多线程的问题，可以不考虑并发。
								if (-1 == hfTaskMnt.getErrorTimes())
									updateHFTaskMnt.setErrorTimes(1);
								else
									updateHFTaskMnt.setErrorTimes(hfTaskMnt.getErrorTimes() + 1);
								// 如果需要错误重试，则要修改计划执行时间
								if (Const.TISTRY_YES == hfTaskMnt.getIsReTry()) {
									updateHFTaskMnt.setPlanRunTime(StringUtil.getTSTime_SpaceXMin(hfTaskMnt.getReTryInterval()));
								}
								
								ttService.modifyTaskMnt(updateHFTaskMnt);
								log.info("********定时任务[" + hfTaskMnt.getTaskRpid() + "]反馈超时。************");
							}
						} else if (Const.TASK_STATE_START == hfTaskMnt.getState()// 如果不是就是判断是否状态为初始或者错误，并且需要重试，并且错误次数小于可重试次数
								|| ((Const.TASK_STATE_SENDERROR == hfTaskMnt.getState()
										|| Const.TASK_STATE_FBTIMEOUT == hfTaskMnt.getState() || Const.TASK_STATE_RUNERROR == hfTaskMnt
										.getState()) && hfTaskMnt.getErrorTimes() <= hfTaskMnt.getReTryTimes() && Const.TISTRY_YES == hfTaskMnt
										.getIsReTry())) {
							// 判断计划执行时间：planRunTime是否小于等于当前时间，是就就进行报文发送，否则直接略过。
							if (hfTaskMnt.getPlanRunTime().after(StringUtil.getTSTime()))
								continue;
							// 满足派发要求，进行报文发送，这里为了避免对方无响应造成线程堵塞，采用多线程的方式进行发送。
							DoTimeTaskSend sendThread = new DoTimeTaskSend("TaskSend" + hfTaskMnt.getTaskRpid(),
									hfTaskMnt);
							sendThread.start();
						}

					} catch (Exception e) {
						log.error("[定时任务监控失败,TaskRpid:" + hfTaskMnt.getTaskRpid() + "]", e);
					}
				}
				log.info("定时任务任务监控完毕，开始休眠***");
				this.wait();
			} catch (InterruptedException e) {
				log.info("定时任务任务监控完毕，休眠失败***", e);
				return;
			} catch (Exception e) {
				log.info("定时任务任务监控失败!", e);
			}
		}
	}

	public synchronized void restart() {
		this.notifyAll();
	}

}