<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Language" content="zh-cn">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>新建网页 1</title>
</head>

<body topmargin="0" leftmargin="0" bgcolor="#5A5665">

<table border="0" width="195" cellspacing="0" cellpadding="0">
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="report/toQueryDataPage.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>报备信息管理</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponinf/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>兑换券管理</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponinf/log.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>兑换券操作日志</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponrule/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>兑换券规则管理</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponrule/log.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>兑换券规则操作日志</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponbatch/importLog.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>兑换码批量导入日志</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponbatch/importOptLog.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>兑换码批量导入操作日志</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponcode/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>兑换码管理</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="timetask/taskList.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>定时任务管理</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="timetask/taskMonitor.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>定时任务监控</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponstats/gatherList.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>兑换劵汇总统计</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponstats/monthdayList.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>兑换劵日/月统计</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponstats/detailList.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>兑换劵兑换明细统计</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponstats/mergatherList.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>商户兑换劵汇总统计</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponstats/merDetailList.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>商户兑换劵日/月统计</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="para/indexs.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>系统参数管理</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="para/indexb.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>业务参数管理</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponorder/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>订单查询</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="couponorder/toquerylog.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>订单操作日志</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="stl/toquerylog.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>商户结算操作日志</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="stl/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>结算账单</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="goodsbank/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>商品银行</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="goodsbankaudit/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>商品银行审核</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="merbank/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>商户银行</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="merbankaudit/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>商户银行审核</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="merbankoplog/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>商户银行操作日志</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="proxygoods/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>代理商品管理</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="proxygoods/add.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>代理商品增加</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="chnlgoods/modify.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>渠道商品配置</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="mersetswitch/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>商户账单配置</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="log/index.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>业务日志查询</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="log/index_plat.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>平台日志查询</b></font></span></a></td>
	</tr>
	<tr width="195" height="25">
		<td>&nbsp;
		<a target="mainFrame" href="reportOpt/toQuery.do">
		<span style="text-decoration: none"><font color="#FFFFFF" size="2"><b>报备操作记录</b></font></span></a></td>
	</tr>
</table>

</body>

</html>