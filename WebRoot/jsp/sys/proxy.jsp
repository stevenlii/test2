<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String url=(String)request.getAttribute("com.umpay.sso.$URL$");
	if(url== null){
		out.println("无法获得跳转的页面url");
		return;
	}
	String target=(String)request.getAttribute("com.umpay.sso.$URL_TARGET$");
	if(target==null){
		%>
<script>
		window.location.href="<%=url%>";
</script>		
		<%
	}else if("top".equals(target)){
		%>
		<script>
				top.location.href="<%=url%>";
		</script>		
		<%
	}
%>
