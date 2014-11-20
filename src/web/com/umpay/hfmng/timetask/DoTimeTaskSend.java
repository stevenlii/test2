package com.umpay.hfmng.timetask;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.umpay.coupon.system.restclient.CouponRestRequester;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.ParameterPool;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.model.HFTaskMnt;

/**
 * @ClassName: TimeTaskDispatch
 * @Description: 定时根据任务的定时规则进行任务触发报文的发出。
 * @author helin
 * @date 2013-1-20 下午3:14:36
 */
public class DoTimeTaskSend extends Thread {

	protected Logger log = Logger.getLogger(this.getClass());
	private HFTaskMnt hfTaskMnt;

	public DoTimeTaskSend(String threadName, HFTaskMnt hfTaskMnt) {
		super(threadName);
		this.hfTaskMnt = hfTaskMnt;
	}

	/**
	 * @Title: run
	 * @Description: 实际的任务派发处理。
	 * @param
	 * @author helin
	 * @date 2013-1-20 下午3:21:51
	 */
	@Override
	public synchronized void run() {
		TimeTaskMntDaoIfc timeTaskMntDao = (TimeTaskMntDaoIfc) SpringContextUtil
				.getBean("timeTaskMntDao");
		// HFTaskMnt hfTaskMnt = localHfTaskMnt.get();
		// 调用加密客户端进行触发报文发送。
		CouponRestRequester crr = new CouponRestRequester();
		Map<String, Object> reqData = new HashMap<String, Object>();
		reqData.put("taskRpid", hfTaskMnt.getTaskRpid());
		reqData.put("callbackUrl", ParameterPool.couponBusiBackParas.get("TIMERADDR"));// 定时任务反馈回调地址。
		HFTaskMnt updateHFTaskMnt = new HFTaskMnt();
		Map<String, Object> retData = new HashMap<String, Object>();
		try {
			retData = crr.sendRequestForPost(hfTaskMnt.getPostUrl(), reqData);

			if ("8888".equals((String) retData.get("retCode"))) {
				log.info("***定时触发成功[url:" + hfTaskMnt.getPostUrl() + "]"
						+ retData.get("retMsg"));
				// 发送成功，计算最后反馈时间
				updateHFTaskMnt.setState(Const.TASK_STATE_SENDSUCCEED);
				updateHFTaskMnt.setDeadTime(StringUtil
						.getSringTime_SpaceXMin(hfTaskMnt.getRetTimeout()));
			} else if ("0000".equals((String) retData.get("retCode"))) {
				log.info("***定时执行成功[url:" + hfTaskMnt.getPostUrl() + "]"
						+ retData.get("retMsg"));
				// 表示直接执行成功。执行成功直接更新状态。
				updateHFTaskMnt.setState(Const.TASK_STATE_RUNSUCCEED);
				updateHFTaskMnt.setDeadTime(StringUtil
						.getSringTime_SpaceXMin(hfTaskMnt.getRetTimeout()));
			} else {
				log.info("***定时执行失败[url:" + hfTaskMnt.getPostUrl() + "]"
						+ retData.get("retMsg"));
				// 表示执行失败，如果执行失败,还要看是否错误重试。
				updateHFTaskMnt.setState(Const.TASK_STATE_RUNERROR);
				// 这里没多线程的问题，可以不考虑并发。
				if (-1 == hfTaskMnt.getErrorTimes())
					updateHFTaskMnt.setErrorTimes(1);
				else
					updateHFTaskMnt.setErrorTimes(hfTaskMnt.getErrorTimes() + 1);
				// 如果需要错误重试，则要修改计划执行时间
				if (Const.TISTRY_YES == hfTaskMnt.getIsReTry()) {
					updateHFTaskMnt.setPlanRunTime(StringUtil.getTSTime_SpaceXMin(hfTaskMnt.getReTryInterval()));
				}
			}

		} catch (Exception e) {
			log.info("***定时触发失败[url:" + hfTaskMnt.getPostUrl() + "]", e);
			// 表示触发失败。
			updateHFTaskMnt.setState(Const.TASK_STATE_SENDERROR);
			updateHFTaskMnt.setErrorInf(e.getMessage());
			// 这里没多线程的问题，可以不考虑并发。
			if (-1 == hfTaskMnt.getErrorTimes())
				updateHFTaskMnt.setErrorTimes(1);
			else
				updateHFTaskMnt.setErrorTimes(hfTaskMnt.getErrorTimes() + 1);
			// 如果需要错误重试，则要修改计划执行时间
			if (Const.TISTRY_YES == hfTaskMnt.getIsReTry()) {
				updateHFTaskMnt.setPlanRunTime(StringUtil
						.getTSTime_SpaceXMin(hfTaskMnt.getReTryInterval()));
			}
		}

		updateHFTaskMnt.setTaskRunTime(StringUtil.getTSTime());
		updateHFTaskMnt.setModTime(StringUtil.getTSTime());
		updateHFTaskMnt.setTaskRpid(hfTaskMnt.getTaskRpid());
		timeTaskMntDao.update(updateHFTaskMnt);
		// 刷新缓存
		TimeTaskService ttService = (TimeTaskService) SpringContextUtil
				.getBean("timeTaskService");
		try {
			ttService.refHFTaskMnts(updateHFTaskMnt);
		} catch (Exception e) {
			log.error("***缓存刷新失败：[TaskRpid:" + updateHFTaskMnt.getTaskRpid()
					+ "]");
		}
	}

	public synchronized void restart() {
		this.notifyAll();
	}

}