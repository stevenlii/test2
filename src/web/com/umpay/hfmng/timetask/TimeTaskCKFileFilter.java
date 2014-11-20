package com.umpay.hfmng.timetask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.umpay.coupon.system.security.URLMixer;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.exception.BusinessException;
import com.umpay.hfmng.service.FileTaskService;
import com.umpay.hfmng.thread.StlMergerTask;
import com.umpay.hfmng.thread.StlPayBackTask;
import com.umpay.hfmng.thread.StlStateChange2payedTask;
import com.umpay.hfmng.thread.StlStateChange2payingTask;

public class TimeTaskCKFileFilter implements Filter {
	protected Logger log = Logger.getLogger(this.getClass());
	
	private static XStream xstream = new XStream(new DomDriver());
	private static StlPayBackTask stlPayBackTask = new StlPayBackTask();//单一模式，不可以同步执行，以免任务重跑
	private static StlMergerTask stlMergerTask = new StlMergerTask();// 单一模式，不可以同步执行，以免任务重跑
	
	/**
	 * @Title: destroy
	 * @Description: 销毁方法
	 * @param
	 * @author wangyuxin
	 * @date 2013-3-15 下午14:58:12
	 */
	public void destroy() {
	}

	/**
	 * @Title: doFilter
	 * @Description: 实际的过滤逻辑
	 * @param
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 * @author wangyuxin
	 * @date 2013-3-15 下午14:58:12
	 */
	@SuppressWarnings({ "unchecked", "finally" })
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		long token = 0;
		String sign = "";
		String retCode = "";
		String retMsg = "";
		String taskRpid = "";
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> reqData = new HashMap<String, Object>();
		try {
			// 获取参数
			try {
				InputStreamReader reader = null;
				reader = new InputStreamReader(req.getInputStream(), "GBK");
				reqData = (Map<String, Object>) xstream.fromXML(reader);
				token = (Long) reqData.get("COUPON_TOKEN");
				sign = reqData.get("COUPON_SIGN").toString();
			} catch (Exception e) {
				throw new BusinessException("参数获取失败：", e);
			}
			// 为了保证系统接口的安全性，进行简单的签名认证
			try {
				URLMixer urlx = URLMixer.getInstance();
				if (!urlx.decrypt2String(sign).equals("" + token)
						|| 5 * 60 * 1000 < Math.abs(token - System.currentTimeMillis())) {
					throw new BusinessException("验签失败!");
				}
			} catch (Exception e) {
				throw new BusinessException("验签失败!", e);
			}
			
			//判断URL，以决定启动什么任务
			String uri = req.getRequestURI();
			if(uri.contains("?")){
				uri = uri.substring(0, uri.indexOf("?"));
			}
			log.info("uri:" + uri);
			taskRpid = (String)reqData.get("taskRpid");
			if(uri.endsWith("stl/payBackTask")){//退费明细文件入库任务启动
				startStlPayBackTask(taskRpid);
				retCode = Const.RETCODE_TIMER_RECSUCC;
				retMsg = "退费明细文件入库任务触发成功";
				log.info("退费明细文件入库任务触发成功");
			} else if(uri.endsWith("stl/stageChange2payingTask")){//结算状态变更付款中任务
				StlStateChange2payingTask task = new StlStateChange2payingTask(taskRpid);
				task.start();
				log.info("结算状态变更为付款中任务触发成功");
				retCode = Const.RETCODE_TIMER_RECSUCC;
				retMsg = "结算状态变更为付款中任务触发成功";
			} else if(uri.endsWith("stl/stageChange2payedTask")){//结算状态变更已付款任务
				StlStateChange2payedTask task = new StlStateChange2payedTask(taskRpid);
				task.start();
				retCode = Const.RETCODE_TIMER_RECSUCC;
				retMsg = "结算状态变更为已付款任务触发成功";
				log.info("结算状态变更为已付款任务触发成功");
			} else if(uri.endsWith("stl/mergerTask")){//结算数据抽取整合任务
				startStlMergerTask(taskRpid);
				retCode = Const.RETCODE_TIMER_RECSUCC;
				retMsg = "结算数据整合任务触发成功";
				log.info("结算数据整合任务触发成功");
			} else {
//				 调用相应的服务进行反馈入库操作
//				TimeTaskService ttService = (TimeTaskService) SpringContextUtil.getBean("timeTaskService");
//				ttService.taskRunFeefback(taskRpid, retCode, retMsg);
				FileTaskService fileTaskService = (FileTaskService) SpringContextUtil.getBean("fileTaskService");
				fileTaskService.initFileTask();
				retCode = "0000";
				retMsg = "反馈成功";
			}
			ret.put("retCode", retCode);
			ret.put("retMsg", retMsg);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("定时反馈错误:" + StringUtil.ExceptionToString(e));
			ret.put("retCode", "4444");
			ret.put("retMsg", e.getMessage());
		} finally {
			resp.getOutputStream().write(xstream.toXML(ret).getBytes("GBK"));
			return;
		}

	}

	/**
	 * @Title: startStlPayBackTask
	 * @Description: 退费明细文件入库任务启动
	 * @param taskRpid
	 * @author jxd
	 * @date 2014-1-8 下午3:36:24
	 */
	private void startStlPayBackTask(String taskRpid) {
		String taskState = stlPayBackTask.getState().toString();
		log.info("----------task：" + taskState);
		// 判断线程是否已经异常，异常回收并重新创建。
		if (taskState.equals("WAITING")) {
			stlPayBackTask.notifyAll();
		} else if (taskState.equals("NEW")) {
			stlPayBackTask.setTaskRpid(taskRpid);
			stlPayBackTask.start();
		} else if (taskState.equals("TERMINATED")) {
			stlPayBackTask.interrupt();
			stlPayBackTask = new StlPayBackTask();
			stlPayBackTask.setTaskRpid(taskRpid);
			stlPayBackTask.start();
		} else {
			log.info("-----task--还在运行。-");
		}
	}
	
	/**
	 * @Title: startStlMergerTask
	 * @Description: 退费明细文件入库任务启动
	 * @param taskRpid
	 * @author jxd
	 * @date 2014-1-8 下午3:36:24
	 */
	private void startStlMergerTask(String taskRpid) {
		String taskState = stlMergerTask.getState().toString();
		log.info("----------task：" + taskState);
		// 判断线程是否已经异常，异常回收并重新创建。
		if (taskState.equals("WAITING")) {
			stlMergerTask.notifyAll();
		} else if (taskState.equals("NEW")) {
			stlMergerTask.setTaskRpid(taskRpid);
			stlMergerTask.start();
		} else if (taskState.equals("TERMINATED")) {
			stlMergerTask.interrupt();
			stlMergerTask = new StlMergerTask();
			stlMergerTask.setTaskRpid(taskRpid);
			stlMergerTask.start();
		} else {
			log.info("-----task--还在运行。-");
		}
	}

	/**
	 * @Title: init
	 * @Description: 初始化方法
	 * @param
	 * @param filterConfig
	 * @throws ServletException
	 * @author wangyuxin
	 * @date 2013-3-15 下午14:58:12
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
