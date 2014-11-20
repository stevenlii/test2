/**
 * @FileName: testfilet.java
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author helin
 * @date 2013-1-18 下午8:52:04
 */
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
import com.umpay.hfmng.common.SpringContextUtil;
import com.umpay.hfmng.common.StringUtil;
import com.umpay.hfmng.exception.BusinessException;

/**
 * @ClassName: testfilet
 * @Description: 用于专门处理定时任务反馈
 * @author helin
 * @date 2013-1-18 下午8:52:04
 */
public class TimeTaskFeedbackFilter implements Filter {
	protected Logger log = Logger.getLogger(this.getClass());

	/**
	 * @Title: destroy
	 * @Description: 销毁方法
	 * @param
	 * @author helin
	 * @date 2013-1-18 下午8:58:12
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
	 * @author helin
	 * @date 2013-1-18 下午8:58:12
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
		XStream xstream = new XStream(new DomDriver());
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			// 获取参数
			try {
				InputStreamReader reader = null;
				reader = new InputStreamReader(req.getInputStream(), "GBK");
				Map<String, Object> reqData = (Map<String, Object>) xstream.fromXML(reader);
				token = (Long) reqData.get("COUPON_TOKEN");
				sign = reqData.get("COUPON_SIGN").toString();
				taskRpid = reqData.get("taskRpid").toString();
				retCode = reqData.get("retCode").toString();
				retMsg = reqData.get("retMsg").toString();
			} catch (Exception e) {
				throw new BusinessException("参数获取失败：", e);
			}
			log.error("定时任务反馈信息:[taskRpid:"+taskRpid+"],[retCode:"+retCode+"],[retMsg:"+retMsg+"]");
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

			// 调用相应的服务进行反馈入库操作
			TimeTaskService ttService = (TimeTaskService) SpringContextUtil.getBean("timeTaskService");
			ttService.taskRunFeefback(taskRpid, retCode, retMsg);
			ret.put("retCode", "0000");
			ret.put("retMsg", "反馈成功。");
		} catch (Exception e) {
			log.error("定时反馈错误:" + StringUtil.ExceptionToString(e));
			ret.put("retCode", "4444");
			ret.put("retMsg", e.getMessage());
		} finally {
			resp.getOutputStream().write(xstream.toXML(ret).getBytes("GBK"));
			return;
		}

	}

	/**
	 * @Title: init
	 * @Description: 初始化方法
	 * @param
	 * @param filterConfig
	 * @throws ServletException
	 * @author helin
	 * @date 2013-1-18 下午8:58:12
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
