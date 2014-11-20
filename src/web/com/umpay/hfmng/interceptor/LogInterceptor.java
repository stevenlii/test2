package com.umpay.hfmng.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.umpay.hfmng.action.BaseAction;
import com.umpay.sso.org.User;

public class LogInterceptor extends HandlerInterceptorAdapter{
	private Logger log = Logger.getLogger("operateLog");
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)throws Exception {
		try{
			Object obj=request.getAttribute("LogInterceptorReqTime");
			long reqTime=0;;
			 if (obj!=null)
				 reqTime=Long.valueOf(obj+"");
			long interval=System.currentTimeMillis()-reqTime;
			String loginedUserName = "";
			if(handler instanceof BaseAction){
				BaseAction action = (BaseAction)handler;
				User user = action.getUser();
				loginedUserName = user.getLoginName()+"-"+user.getName();
			}
			log.info(String.format("END--操作人:%s,消耗时间:%s,请求功能:%s,请求数据:%s",loginedUserName,interval,request.getRequestURI(),request.getQueryString()));
		}catch(Exception e){
			log.error("操作日志打印失败",e);
		}
	}
	
	/**
	 * This implementation always returns <code>true</code>.
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	    throws Exception {
		try{
			request.setAttribute("LogInterceptorReqTime", System.currentTimeMillis());
			String loginedUserName = "";
			if(handler instanceof BaseAction){
				BaseAction action = (BaseAction)handler;
				User user = action.getUser();
				loginedUserName = user.getLoginName()+"-"+user.getName();
			}
			log.info(String.format("START--操作人:%s,请求功能:%s,请求数据:%s",loginedUserName,request.getRequestURI(),request.getQueryString()));
		
		}catch(Exception e){
			log.error("操作日志打印失败",e);
		}
		return true;
	}
}
