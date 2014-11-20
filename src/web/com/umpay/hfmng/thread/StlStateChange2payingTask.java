package com.umpay.hfmng.thread;

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
 * 结算状态变更为付款中任务类
 * <p>
 * 创建日期：2013-12-16
 * </p>
 * 
 * @version V1.0
 * @author jxd
 * @see
 */
public class StlStateChange2payingTask extends Thread {
	private static final Logger log = Logger.getLogger(StlPayBackTask.class);
	private static String CACHEKEY_TIMTERADDR="BP_后台参数-TIMERADDR";//缓存键——定时调度器URL地址
	
	private String taskRpid = "";// 任务流水，通知定时监控服务器执行结果时使用

	/**
	 * 构造方法
	 * 
	 * @param taskRpid 任务编号
	 */
	public StlStateChange2payingTask(String taskRpid) {
		this.taskRpid = taskRpid;
	}

	/**
	 * @Title: run
	 * @Description: 任务主方法 <br/>
	 *               基础数据为T_COUPON_MERSET状态为2（商户已确认）的数据集合，
	 *               然后判断财务平台表T_HS_COSTANALYZE的PAYFLAG（0-未付;
	 *               1-已付）字段值是否为已付，如果是已付则修改平台状态为3，
	 * @author jxd
	 * @date 2013-12-16 下午4:15:18
	 */
	@Override
	public void run() {
		log.debug("结算状态变更为付款中任务，开始执行……");
		String retCode = Const.RETCODE_TIMER_FAILED;
		String retMsg = "结算状态变更为付款中任务执行失败";
		try {
			/*
			 * 财务平台T_HS_COSTANALYZE表字段：MERID + STLMONTH + LOCALFLAG + TRANSTYPE
			 * 对应平台T_COUPON_MERSET表字段：ACCID + STLDATE + LOCALFLAG + BILLTYPE
			 * SQL: UPDATE UMPAY.T_COUPON_MERSET A SET SETTSTATUS=3 WHERE
			 * SETTSTATUS=2 AND ISUSE=2 AND EXISTS (SELECT 1 FROM
			 * UMPAY.T_HS_COSTANALYZE B WHERE B.MERID=A.ACCID AND B.STLMONTH=
			 * A.STLDATE AND B.LOCALFLAG = A.LOCALFLAG AND B.TRANSTYPE =
			 * DECODE(A.BILLTYPE,1,'MV','XE') AND B.PAYFLAG=1 AND A.ISUSE=2)
			 */
			STLService stlService = (STLService) SpringContextUtil.getBean("STLServiceImpl");
			int num = stlService.change2paying();
			// 设置任务执行成功返回信息
			retCode = Const.RETCODE_TIMER_SUCCESS;
			retMsg = "结算状态变更为付款中任务执行成功，成功更新条数：" + num;
			log.info(retMsg);
		} catch (Exception e) {
			log.error("结算状态变更为付款中任务执行失败", e);
		} finally {
			try {
				// 通知定时监控服务器执行结果
				sendResponseToTimerInvoker(retCode, retMsg);
			} catch (Exception e) {
				log.error("任务执行结果通知定时监控服务器失败", e);
			}
		}
		log.debug("结算状态变更为付款中任务，结束执行……");
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
