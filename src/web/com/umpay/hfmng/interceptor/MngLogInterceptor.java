package com.umpay.hfmng.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MngLogInterceptor implements MethodInterceptor {

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		String info = methodInvocation.getMethod().getDeclaringClass() + "." + methodInvocation.getMethod().getName() + "()";
		System.out.println(info);
		Object result = methodInvocation.proceed();
		System.out.println("======MngLogInterceptor invoke=======");
		return result;
	}
}
