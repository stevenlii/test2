package com.umpay.hfmng.action;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.umpay.coupon.system.security.URLMixer;
import com.umpay.hfmng.common.Const;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.thread.StlMergerTask;

/**
 * 
 * 
 * @ClassName:StlMergerTaskAction
 * @Description: 结算数据整合（定时任务）
 * @version: 1.0
 * @author: lituo
 * @Create: 2013-12-20
 * 
 */
@Controller
@RequestMapping("/stlMergerTask")
public class StlMergerTaskAction extends BaseAction {

	private static XStream xstream = new XStream(new DomDriver());
	private static StlMergerTask task = new StlMergerTask();// 单一模式，不可以同步执行，以免任务重跑
	
	/**
	 * 
	 * @Title: mergerData
	 * @Description: 结算数据整合
	 * @param response
	 * @throws Exception
	 * @author lituo
	 * @date 2014-1-6 上午09:33:01
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/mergerData")
	public void mergerData(HttpServletResponse response) throws Exception {
		log.info("触发结算数据整合任务-开始");
		String retCode = "";
		String retMsg = "";
		InputStream is = this.getHttpRequest().getInputStream();
		String reqStr = StringUtil.inputStream2String(is);
		Map<String, Object> req = (Map<String, Object>) xstream.fromXML(reqStr);
		// 为了保证系统接口的安全性，进行简单的签名认证
		URLMixer urlx = URLMixer.getInstance();
		if (!urlx.decrypt2String(req.get("COUPON_SIGN").toString()).equals(
				"" + req.get("COUPON_TOKEN"))
				|| 5 * 60 * 1000 < Math.abs((Long) req.get("COUPON_TOKEN")
						- System.currentTimeMillis())) {
			retCode = Const.RETCODE_TIMER_FAILED;
			retMsg = "结算数据整合任务触发失败－验签失败";
			log.info("结算数据整合任务触发失败－验签失败");
		} else {
			// 任务启动
			String taskRpid = (String) req.get("taskRpid");
			// StlMergerTask task = new StlMergerTask(taskRpid);
			// task.start();
			String taskState = task.getState().toString();
			log.info("----------task：" + taskState);
			// 判断线程是否已经异常，异常回收并重新创建。
			if (taskState.equals("WAITING")) {
				task.notifyAll();
			} else if (taskState.equals("NEW")) {
				task.setTaskRpid(taskRpid);
				task.start();
			} else if (taskState.equals("TERMINATED")) {
				task.interrupt();
				task = new StlMergerTask();
				task.setTaskRpid(taskRpid);
				task.start();
			} else {
				log.info("-----task--还在运行。-");
			}
			log.info("结算数据整合任务触发成功");
			retCode = Const.RETCODE_TIMER_RECSUCC;
			retMsg = "结算数据整合任务触发成功";
		}
		writeResponse(response, retCode, retMsg);
		log.info("触发结算数据整合任务-结束");
	}

	/**
	 * 
	 * @Title: handlerException
	 * @Description: 异常处理方法，通知任务监控平台任务执行失败
	 * @param ex
	 * @param request
	 * @param response
	 * @author lituo
	 * @date 2013-12-20 上午09:43:54
	 */
	@ExceptionHandler(Exception.class)
	public void handlerException(Exception ex, HttpServletRequest request,
			HttpServletResponse response) {
		String msg = "结算数据整合任务触发失败:" + ex.getMessage();
		if (msg.length() > 512) {// 异常反馈信息最长支持521位
			msg = msg.substring(0, 512);
		}
		writeResponse(response, Const.RETCODE_TIMER_FAILED, msg);
	}

	/**
	 * 
	 * @Title: writeResponse
	 * @Description: 回复消息
	 * @param response
	 * @param retCode
	 * @param retMsg
	 * @author lituo
	 * @date 2013-12-20 上午09:43:25
	 */
	private void writeResponse(HttpServletResponse response, String retCode,
			String retMsg) {
		Map<String, String> res = new HashMap<String, String>();
		res.put("retMsg", retMsg);
		res.put("retCode", retCode);
		try {
			String reqXml = xstream.toXML(res);
			response.setHeader("ContentType", "text/html");
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().print(reqXml);
		} catch (Exception e) {
			log.error("回复失败响应异常", e);
		}
	}
}
