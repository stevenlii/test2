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
import com.umpay.hfmng.thread.StlStateChange2payedTask;
import com.umpay.hfmng.thread.StlStateChange2payingTask;

/**
 * 结算状态变更任务触发类
 * <p>
 * 创建日期：2013-12-16
 * </p>
 * 
 * @version V1.0
 * @author jxd
 * @see
 */
@Controller
@RequestMapping("/stlStateChangeTask")
public class StlStateChangeTaskAction extends BaseAction {
	private static XStream xstream = new XStream(new DomDriver());

	/**
	 * @Title: change2paying
	 * @Description: 触发结算状态变更为付款中的任务<br/>
	 *               基础数据为T_COUPON_MERSET状态为2（商户已确认）的数据集合，
	 *               然后判断财务平台表T_HS_COSTANALYZE的PAYFLAG（0-未付;
	 *               1-已付）字段值是否为已付，如果是已付则修改平台状态为3，
	 * @param response
	 * @throws Exception
	 * @author jxd
	 * @date 2013-12-16 下午3:48:30
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/change2paying")
	public void change2paying(HttpServletResponse response) throws Exception {
		log.info("触发结算状态变更为付款中任务-开始");
		String retCode = "";
		String retMsg = "";
		InputStream is = this.getHttpRequest().getInputStream();
		String reqStr = StringUtil.inputStream2String(is);
		Map<String, Object> req = (Map<String, Object>) xstream.fromXML(reqStr);
		// 为了保证系统接口的安全性，进行简单的签名认证
		URLMixer urlx = URLMixer.getInstance();
		if (!urlx.decrypt2String(req.get("COUPON_SIGN").toString()).equals("" + req.get("COUPON_TOKEN"))
				|| 5 * 60 * 1000 < Math.abs((Long) req.get("COUPON_TOKEN") - System.currentTimeMillis())) {
			retCode = Const.RETCODE_TIMER_FAILED;
			retMsg = "结算状态变更为付款中任务触发失败－验签失败";
			log.info("结算状态变更为付款中任务触发失败－验签失败");
		} else {
			// 任务启动
			String taskRpid = (String) req.get("taskRpid");
			StlStateChange2payingTask task = new StlStateChange2payingTask(taskRpid);
			task.start();
			log.info("结算状态变更为付款中任务触发成功");
			retCode = Const.RETCODE_TIMER_RECSUCC;
			retMsg = "结算状态变更为付款中任务触发成功";
		}
		writeResponse(response, retCode, retMsg);
		log.info("触发结算状态变更为付款中任务-结束");
	}

	/**
	 * @Title: change2payed
	 * @Description: 触发结算状态变更为已付款的任务<br/>
	 *               基础数据为T_COUPON_MERSET状态为3（付款中）的数据集合，
	 *               然后判断该数据是否已过三天，如果已过三天则修改平台状态为4，
	 * @param response
	 * @throws Exception
	 * @author jxd
	 * @date 2013-12-16 下午3:48:30
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/change2payed")
	public void change2payed(HttpServletResponse response) throws Exception {
		log.info("触发结算状态变更为已付款任务-开始");
		String retCode = "";
		String retMsg = "";
		InputStream is = this.getHttpRequest().getInputStream();
		String reqStr = StringUtil.inputStream2String(is);
		Map<String, Object> req = (Map<String, Object>) xstream.fromXML(reqStr);
		// 为了保证系统接口的安全性，进行简单的签名认证
		URLMixer urlx = URLMixer.getInstance();
		if (!urlx.decrypt2String(req.get("COUPON_SIGN").toString()).equals("" + req.get("COUPON_TOKEN"))
				|| 5 * 60 * 1000 < Math.abs((Long) req.get("COUPON_TOKEN") - System.currentTimeMillis())) {
			retCode = Const.RETCODE_TIMER_FAILED;
			retMsg = "结算状态变更为已付款任务触发失败－验签失败";
			log.info("结算状态变更为已付款任务触发失败－验签失败");
		} else {
			// 任务启动
			String taskRpid = (String) req.get("taskRpid");
			StlStateChange2payedTask task = new StlStateChange2payedTask(taskRpid);
			task.start();
			log.info("结算状态变更为已付款任务触发成功");
			retCode = Const.RETCODE_TIMER_RECSUCC;
			retMsg = "结算状态变更为已付款任务触发成功";
		}
		writeResponse(response, retCode, retMsg);
		log.info("结算状态变更为已付款任务-结束");
	}

	/**
	 * 异常处理方法
	 * 
	 * @Title: handlerException
	 * @Description: 异常处理方法，通知任务监控平台任务执行失败
	 * @param ex
	 * @param request
	 * @param response
	 * @author jxd
	 * @date 2013-12-13 上午11:10:54
	 */
	@ExceptionHandler(Exception.class)
	public void handlerException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
		String msg = "结算状态变更任务触发失败:" + ex.getMessage();
		if (msg.length() > 512) {// 异常反馈信息最长支持521位
			msg = msg.substring(0, 512);
		}
		writeResponse(response, Const.RETCODE_TIMER_FAILED, msg);
	}

	/**
	 * @Title: writeResponse
	 * @Description: 回复响应
	 * @param response
	 * @param retCode
	 * @param retMsg
	 * @author jxd
	 * @date 2013-12-13 上午11:21:36
	 */
	private void writeResponse(HttpServletResponse response, String retCode, String retMsg) {
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
