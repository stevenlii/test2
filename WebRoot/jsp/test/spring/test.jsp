<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.umpay.hfmng.common.SpringContextUtil"%>
<%@ page import="org.springframework.stereotype.Controller"%>
<%
  Object obj = null;
  		try{
  			
			//System.out.println(obj);
			String[] beanNames = SpringContextUtil.getApplicationContext().getBeanDefinitionNames();
			//SpringContextUtil.getApplicationContext().getAutowireCapableBeanFactory().
			//obj = SpringContextUtil.getApplicationContext().getAutowireCapableBeanFactory().getBean("demoAction");
			//obj = SpringContextUtil.getApplicationContext().findAnnotationOnBean("com.umpay.hfmng.action.DemoAction",Controller.class);
			System.out.println(beanNames.length);
			for(String bn:beanNames){
				System.out.println(bn);
			}
			
			obj = SpringContextUtil.getBean("demoAction");
			
		}catch(Exception e){
			e.printStackTrace();
		}
  

%>
<html>
<head>
<title></title>
</head>
<body>
"<%=obj%>"
</body>
</html>
