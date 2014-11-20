<%@ page language="java"  pageEncoding="UTF-8" 	contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>   
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<style>
html{background-color:white; height:100%;}
body{ height:100%; margin:0px;}
</style>

<script>
function showc(event){
	showWin('c.jsp',400,225,event);
	//showWin('http://www.baidu.com',400,300,event);
}
function getRetValue(id,name){
	document.getElementById("txtID").value=id;
	document.getElementById("txtID").focus();
	hidePop();
}
</script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/window.js' charset="utf-8"></script>
</head>
<body  style="height:100%" onclick="bodyClose(event);">
 要传的值 
 <input type='text' id='txtID' name='txtName' value='aa' onclick="showc(event);"/>  <br/>
<input type='button' value='open简单' onclick="window.open('b.jsp');" /><br/>
<input type='button' value='open' onclick="showc(event);" /><br/>


</body> 
</html> 

