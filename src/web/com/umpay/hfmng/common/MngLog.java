package com.umpay.hfmng.common;

import org.aspectj.lang.JoinPoint;

public class MngLog {

	public void before(JoinPoint joinPoint) {
		joinPoint.getArgs();//此方法返回的是一个数组，数组中包括request以及ActionCofig等类对象
		System.out.println("被拦截方法调用之前调用此方法，输出此语句");
	}

	public void after(JoinPoint joinPoint) {
		 Object[] args=joinPoint.getArgs();
		  for(int i=0;i<args.length;i++)
		  {
		   System.out.println(args[i]);
		  }

		System.out.println(joinPoint.getTarget().getClass().getName());
		System.out.println(joinPoint.getSignature().getName());
		System.out.println("被拦截方法调用之后调用此方法，输出此语句");
	}
}
