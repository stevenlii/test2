<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>jquery操作json对象</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/jquery.min.js"></script>
<script>
function extend(des, src, override){
    if(src instanceof Array){
        for(var i = 0, len = src.length; i < len; i++)
             extend(des, src[i], override);
    }
    for( var i in src){
        if(override || !(i in des)){
            des[i] = src[i];
        }
    } 
    return des;
}
</script>
  </head>
  
  <script>
	  //var json_data = $.getJSON();
	  //alert(json_data);
	  var data = $.parseJSON('{"name":"John1"}');
	  
	  
	  //alert(data);
	 // alert( data.name);
	  var data2 = $.parseJSON('{"name":"John2"}');
	  var arrayObj = new Array();
	  arrayObj.unshift(data2);
	  alert(arrayObj);
	  var c = extend({},[data2,data]);
	  alert(c[1].name);
	  
	  var A={a:"1",b:"2",c:"3"};
		var B={c:"4",d:"5",e:"6"};
		var C=$.extend({}, A,B);
		alert(C.d);
	  
  </script>
  <body>
    This is my JSP page. <br>
  </body>
</html>
