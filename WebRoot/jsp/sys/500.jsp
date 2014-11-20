<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ page isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@ include file="/jsp/sys/common/meta.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</head> 
<body leftmargin="0" bottommargin="0" rightmargin="0">
<br/>
<table border="0" align="center">
  <tr>
    <td align="center" nowrap="nowrap" style="font-size:14px; font-weight:bold; color:#FF6600" ><img src='<c:url value="/profile/default/images/sys/error.gif"/>'>对不起，访问出错了，异常如下</td>
  </tr>
</table>
<div style="margin:20px auto 0px auto; border:#CCCCCC 1px solid; overflow:auto; color:#CC3333; font-size:13px">
<%=exception.getMessage()%><br/>
<%
    java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(cw,true);
    exception.printStackTrace(pw);
    out.println(cw.toString());
    %>
</div>
</body>
</html> 
