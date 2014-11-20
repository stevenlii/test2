package com.umpay.hfmng.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ReqContextInterceptor implements HandlerInterceptor{
	private Logger log = Logger.getLogger(ReqContextInterceptor.class);
	//web访问根路径
	private String webUrl = null;
	//private ThreadLocal<WebReqInfo> threadLocal = new ThreadLocal<WebReqInfo>();
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		log.info("======req========执行顺序: 1、preHandle================");
		commonAttributeSet(request);
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
		log.info("=======req=======执行顺序: 2、postHandle================");
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("=======req=======执行顺序: 3、afterCompletion================");
	}
	/**
	 * 共通request attribute设定
	 * @param request
	 */
	private void commonAttributeSet(HttpServletRequest request){
//		if(webUrl == null){
//			int webPort=request.getServerPort();
//			if(webPort==80){
//				webUrl = request.getScheme() + "://"+ request.getServerName()+ request.getContextPath();
//			}else{
//				webUrl = request.getScheme() + "://"+ request.getServerName() + ":"+ webPort + request.getContextPath();
//			}
//		}
//		//web访问根路径
//		request.setAttribute("webUrl", webUrl);
	}

}
