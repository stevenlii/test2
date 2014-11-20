package com.umpay.hfmng.timetask;

import org.apache.log4j.Logger;

import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.model.HFTask;

/**
 * @ClassName: TimeTaskDispatch
 * @Description: 定时根据任务的定时规则进行任务派发
 * @author helin
 * @date 2013-1-20 下午3:14:36
 */
public class TimeTaskDispatch extends Thread {

	protected Logger log = Logger.getLogger(this.getClass());

	public TimeTaskDispatch(String threadName) {
		super(threadName);
	}

	/**
	 * @Title: run
	 * @Description: 实际的任务派发处理方法，先将数据从数据库加载到缓存中，然后再从缓存中获得数据进行处理。 缓存的加载没30分钟一次，处理每分钟一次。
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
					log.info("********开始加载定时任务配置缓存。***");
					ttService.loadTimeTaskInfo();
					loadCount = 1;
					log.info("********定时任务配置缓存加载完毕。***");
				} else {
					loadCount++;
				}
				// 再从缓存中获得数据进行处理。处理每分钟一次。
				// 遍历缓存中的定时任务信息
				for (HFTask hfTask : ParameterPool.hfTasks.values()) {
					try {
						// 首先判断下次执行时间：nextRunTime是否小于当前时间，小于就进行派发，否则直接略过。
						if (hfTask.getNextRunTime().after(StringUtil.getTSTime()))
							continue;
						// 满足派发要求，向监控表插入记录。计算新的下次派发时间，并更新到任务表。
						ttService.doDispatch(hfTask);
					} catch (Exception e) {
						log.error("[定时任务派发失败,taskId:" + hfTask.getTaskId() + "]", e);
					}
				}
				log.info("定时任务入队列完毕，开始休眠***");
				this.wait();
			} catch (InterruptedException e) {
				log.info("定时任务入队列完毕，休眠失败***", e);
				return;
			} catch (Exception e) {
				log.info("定时任务入队列失败!", e);
			}
		}
	}

	public synchronized void restart() {
		this.notifyAll();
	}

}