package com.umpay.hfmng.thread;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.umpay.coupon.system.restclient.CouponRestRequester;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.HfCacheUtil;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.model.Para;
import com.umpay.hfmng.service.STLService;

/**
 * 结算状态变更为已付款任务类
 * <p>
 * 创建日期：2013-12-16
 * </p>
 * 
 * @version V1.0
 * @author jxd
 * @see
 */
public class StlStateChange2payedTask extends Thread {
	private static final Logger log = Logger.getLogger(StlPayBackTask.class);
	private static String CACHEKEY_TIMTERADDR="BP_后台参数-TIMERADDR";//缓存键——定时调度器URL地址
	
	private String taskRpid = "";//任务流水，通知定时监控服务器执行结果时使用
	
	/**
	 * 构造方法
	 * @param taskRpid 任务编号
	 */
	public StlStateChange2payedTask(String taskRpid){
		this.taskRpid = taskRpid;
	}
	
	/**
	 * @Title: run
	 * @Description: 任务主方法<br/>
	 *               基础数据为T_COUPON_MERSET状态为3（付款中）的数据集合，
	 *               然后判断该数据是否已过三个工作日，如果已过三个工作日则修改平台状态为4，
	 * @author jxd
	 * @date 2013-12-16 下午4:36:20
	 */
	@Override
	public void run() {
		log.debug("结算状态变更为已付款任务，开始执行……");
		String retCode = Const.RETCODE_TIMER_FAILED;
		String retMsg = "结算状态变更为已付款任务执行失败";
		try {
			// 计算三个工作前日期字符串，格式如：2013-12-16 00:00:00.000001
			String targetDate = calDate();
			/*
			 * 更新状态为已付款 
			 * 更新SQL: update UMPAY.T_COUPON_MERSET set SETTSTATUS = 4
			 * where SETTSTATUS = 3 AND ISUSE=2 and MODTIME <= '2013-12-16
			 * 00:00:00.000001'
			 */
			STLService stlService = (STLService) SpringContextUtil.getBean("STLServiceImpl");
			int num = stlService.change2payed(targetDate);
			// 设置任务执行成功返回信息
			retCode = Const.RETCODE_TIMER_SUCCESS;
			retMsg = "结算状态变更为已付款任务执行成功，成功更新条数：" + num;
			log.info(retMsg);
		} catch (Exception e) {
			log.error("结算状态变更为已付款任务执行失败", e);
		} finally {
			try {
				// 通知定时监控服务器执行结果
				sendResponseToTimerInvoker(retCode, retMsg);
			} catch (Exception e) {
				log.error("任务执行结果通知定时监控服务器失败", e);
			}
		}
		log.debug("结算状态变更为已付款任务，结束执行……");
	}

	/**
	 * @Title: calDate
	 * @Description: 计算三个工作前日期字符串，如任务时间为：2013-12-13日，则结果为 2013-12-10
	 *               23:59:59.999999 任务日期如果是星期六、星期日、星期一，则为上星期三；如果为星期
	 * @return
	 * @author jxd
	 * @date 2013-12-16 下午4:47:30
	 */
	private String calDate() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		if (Calendar.SUNDAY == dayOfWeek) {// 星期日，日期减5，上星期二
			cal.add(Calendar.DAY_OF_MONTH, -5);
		} else if (Calendar.MONDAY == dayOfWeek) {// 星期一，日期减5，上星期三
			cal.add(Calendar.DAY_OF_MONTH, -5);
		} else if (Calendar.TUESDAY == dayOfWeek) {// 星期二，日期减5，上星期四
			cal.add(Calendar.DAY_OF_MONTH, -5);
		} else if (Calendar.WEDNESDAY == dayOfWeek) {// 星期三，日期减5，上星期五
			cal.add(Calendar.DAY_OF_MONTH, -5);
		} else if (Calendar.THURSDAY == dayOfWeek) {// 星期四，日期减3，本星期一
			cal.add(Calendar.DAY_OF_MONTH, -3);
		} else if (Calendar.FRIDAY == dayOfWeek) {// 星期五，日期减3，本星期二
			cal.add(Calendar.DAY_OF_MONTH, -3);
		} else if (Calendar.SATURDAY == dayOfWeek) {// 星期六，日期减4，本星期三
			cal.add(Calendar.DAY_OF_MONTH, -4);
		}
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		dateStr = dateStr + " 23:59:59.999999";
		return dateStr;
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
	private void sendResponseToTimerInvoker(String retCode, String retMsg) throws Exception {
		Map<String, Object> paraMap=HfCacheUtil.getCache().getParaMap();
		Para cp = (Para)paraMap.get(CACHEKEY_TIMTERADDR);
		if( null == cp || "".equals(cp.getParaValue())) {
			log.error("定时调度器参数在参数表中没有配置");
			return;
		}
		String uri = cp.getParaValue();
		//uri = "http://192.168.1.252:8787/hfMngBusi/timetaskfeedback/id.xml" ;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskRpid", taskRpid);
		map.put("retCode",retCode);
		map.put("retMsg", retMsg);
		CouponRestRequester crr=new CouponRestRequester();
		Map<String, Object> res = crr.sendRequestForPost(uri, map);
		log.info("收到定时调度器的相应报文"+res);
	}

}
