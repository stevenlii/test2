package com.umpay.hfmng.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.umpay.hfmng.common.LoginUtil;

public class DaoLoggerInterceptor implements MethodInterceptor{
	
	public static final Logger log = Logger.getLogger("operateDaoLog");

	public Object invoke(MethodInvocation arg0) throws Throwable {
		Object[] args = arg0.getArguments();
		String output = getUserName()+","+getRequestUri()+","+arg0.getMethod().getDeclaringClass().getSimpleName()+"."+arg0.getMethod().getName()+",{";
		Object rval = null;
		try{
			String argsStr = "";
			for(Object obj:args){
				argsStr+=obj+",";
			}
			if(argsStr.indexOf(",")!=-1)
				argsStr = argsStr.substring(0,argsStr.lastIndexOf(","));
			output +=argsStr+"},";
			long start=System.currentTimeMillis();
			rval =  arg0.proceed();
			long interval=System.currentTimeMillis()-start;
			output+=interval;
//			output+=rval;
			log.info(output);
			return rval;
		}catch(Exception e){
			log.info(output, e);
			throw e;
		}
	}
	
	private String getUserName(){
		return LoginUtil.getUser().getLoginName();
	}
	
	private String getRequestUri(){
		ServletRequestAttributes servletRequestAttributes=(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if(servletRequestAttributes==null)
			return null;
		return "{"+servletRequestAttributes.getRequest().getRequestURI()+"}";
	}
}
