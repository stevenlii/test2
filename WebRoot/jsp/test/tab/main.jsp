<%@ page language="java"  pageEncoding="UTF-8" 	contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title> system main </title>
</head>
<frameset rows="127,*,40" tophide="true" cols="*" frameborder="no" border="0" framespacing="0" id="frame1" name="frame1">
 <frame id="banner" name="banner" scrolling="no" noresize src="" frameborder="0">
   <frameset rows="*" lefthide="true" cols="200,10,*" frameborder="no" border="0" framespacing="0" id="frame2" name="frame2">
    <frame id="left" name="left" scrolling="no" noresize src="" frameborder="0">
    <frame src="" name="hiddenLeftFrame" scrolling="NO" noresize>
    <frame id="main" name="main" scrolling="auto" noresize src="${pageContext.request.contextPath}/jsp/test/tab/borderlayouttab.jsp" frameborder="0">
    <!--
    <frame id="right" name="right" scrolling="no" noresize src="right.html" frameborder="0">
    -->
   </frameset>
 <frame id="bottom" name="bottom" scrolling="no" noresize src="" frameborder="0">
</frameset>
</html>
