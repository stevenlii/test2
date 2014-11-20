package com.umpay.hfmng.thread;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.umpay.coupon.system.restclient.CouponRestRequester;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.model.Para;
import com.umpay.hfmng.service.impl.STLServiceImpl;

/**
 * 
 * 
 * @ClassName:StlMergerTask
 * @Description: 结算数据整合任务
 * @version: 1.0
 * @author: lituo
 * @Create: 2013-12-20
 * 
 */
public class StlMergerTask extends Thread {
	private static final Logger log = Logger.getLogger(StlMergerTask.class);
	private static String CACHEKEY_TIMTERADDR = "BP_后台参数-TIMERADDR";// 缓存键——定时调度器URL地址

	private String taskRpid = "";// 任务流水，通知定时监控服务器执行结果时使用

	public void setTaskRpid(String taskRpid) {
		this.taskRpid = taskRpid;
	}

	public StlMergerTask() {
	}

	public StlMergerTask(String taskRpid) {
		this.taskRpid = taskRpid;
	}

	public void run() {
		log.info("结算数据整合任务，开始执行……");
		String retCode = Const.RETCODE_TIMER_FAILED;
		String retMsg = "结算数据整合任务失败";
		try {
			STLServiceImpl stlService = (STLServiceImpl) SpringContextUtil
					.getBean("STLServiceImpl");
			log.info("结算明细数据整合任务开始执行......");
			int count = 1;
			while (count > 0) {
				count = stlService.getMersetDetail();
				log.info("剩余明细行：" + count);
			}
			log.info("结算明细数据整合任务执行完成");

			try {
				log.info("全网数据整合任务开始执行......");
				int mwCount = 1;
				while (mwCount > 0) {
					mwCount = stlService.getMersetMerger(1, "1100");
					log.info("剩余全网数据：" + mwCount);
				}
				log.info("全网数据整合任务开始执行完成");
			} catch (Exception e) {
				log.error("全网数据整合任务执行失败", e);
			}

			try {
				log.info("省网数据整合任务开始执行......");
				int xeCount = 1;
				while (xeCount > 0) {
					xeCount = stlService.getMersetMerger(2, "1010");
					log.info("剩余省网数据：" + xeCount);
				}
				log.info("省网数据整合任务开始执行完成");
			} catch (Exception e) {
				log.error("省网数据整合任务执行失败", e);
			}

			try {
				log.info("北京小额数据整合任务开始执行......");
				int bjCount = 1;
				while (bjCount > 0) {
					bjCount = stlService.getMersetMerger(3, "1001");
					log.info("剩余北京小额数据：" + bjCount);
				}
				log.info("北京小额数据整合任务开始执行完成");
			} catch (Exception e) {
				log.error("北京小额数据整合任务执行失败", e);
			}
			retCode = Const.RETCODE_TIMER_SUCCESS;
			retMsg = "结算数据整合任务执行成功";
			log.info(retMsg);
		} catch (Exception e) {
			log.error("结算数据整合任务执行失败", e);
		} finally {
			try {
				// 通知定时监控服务器执行结果
				sendResponseToTimerInvoker(retCode, retMsg);
			} catch (Exception e) {
				log.error("任务执行结果通知定时监控服务器失败", e);
			}
		}
		log.info("结算数据整合任务，结束执行……");
	}

	/**
	 * @Title: sendResponseToTimerInvoker
	 * @Description: 通知定时监控服务器执行结果
	 * @param retCode
	 * @param retMsg
	 * @author jxd
	 * @throws Exception
	 * @date 2013-12-12 下午5:37:52
	 */
	private void sendResponseToTimerInvoker(String retCode, String retMsg)
			throws Exception {
		Map<String, Object> paraMap = HfCacheUtil.getCache().getParaMap();
		Para cp = (Para) paraMap.get(CACHEKEY_TIMTERADDR);
		if (null == cp || "".equals(cp.getParaValue())) {
			log.error("定时调度器参数在参数表中没有配置");
			return;
		}
		String uri = cp.getParaValue();
		// String uri =
		// "http://192.168.1.252:8787/hfMngBusi/timetaskfeedback/id.xml" ;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskRpid", taskRpid);
		map.put("retCode", retCode);
		map.put("retMsg", retMsg);
		CouponRestRequester crr = new CouponRestRequester();
		Map<String, Object> res = crr.sendRequestForPost(uri, map);
		log.info("收到定时调度器的相应报文" + res);
	}
}
