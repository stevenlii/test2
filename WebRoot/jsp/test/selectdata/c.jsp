<%@ page language="java"  pageEncoding="UTF-8" 	contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html> 
<head><meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/profile/default/css/popwin.css" >
</head>
  <body>
  <div class="divFrame" >
  	<div>
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td height="25" width="7" style='background-image:url("${pageContext.request.contextPath}/profile/default/images/pop/title_bg_left.gif")'>&nbsp;</td>
	    <td   style='background-image:url("${pageContext.request.contextPath}/profile/default/images/pop/title_bg_mid.gif")'>&nbsp;</td>
		<td width="7"  style='background-image:url("${pageContext.request.contextPath}/profile/default/images/pop/title_bg_right.gif")'>&nbsp;</td>
	  </tr>
	  </table>

		<form>
		<table width="90%" border="0" align="center">
		  <tr>
		    <td nowrap="nowrap">商户号:<input name="merId" type="text" id="merId" class="miniInput" value='' size="5" maxlength="8"/>	</td>
			<td nowrap="nowrap">商户名:<input name="merName" type="text" id="merName" class="miniInput" value='' size="10" maxlength="16" /></td>
		    <td nowrap="nowrap">
			<input type="submit" name="queryBtn" value="查询"  class="miniButton"/>&nbsp;
			<input type="submit" name="queryBtn" value="清空"  class="miniButton" onclick="parent.getRetValue('','');"/>
			<input type="hidden" name="currentPage" value="0">	</td>
		  </tr>
		</table>
		</form>
	</div>
	<div style="height:170px; overflow:auto">
		<table width="99%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="C7C7C7" class="Mtable">
	              <tr align="center" bgcolor="#E0E0E0">
	                <td  nowrap class="t3">商户编号</td>
	                <td  nowrap class="t3">商户名称</td>
	              </tr>
	              <tr nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9996','999测试商户');">
	                <td  align="center" class="t3">9996&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	                <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9997','999测试商户');">
	                <td  align="center" class="t3">9997&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              
	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9998','999测试商户');">
	                <td  align="center" class="t3">9998&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              
	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              	                            <tr  nowrap="nowrap" style="cursor:pointer" onMouseOver="this.style.backgroundColor='#CCCCFF';" onMouseOut="this.style.backgroundColor='#ffffff';" onclick="parent.getRetValue('9991','999测试商户');">
	                <td  align="center" class="t3">9991&nbsp;</td>
	                <td  align="center" class="t3">999测试商户&nbsp;</td>
	              </tr>
	              
	  </table>
	  </div>
  </div>
  </body>
</html>