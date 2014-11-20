function showResponse(responseText, statusText) {
	var result = eval("(" + responseText + ")");
	if (result.code == '0') {
		window.parent.fillBackAndCloseDialog(result.msg);
	}
}

// 关闭窗口方法
function closeDialog() {
	window.parent.fillBackAndCloseDialog("#dialog");
}

// 当前页面弹出操作提示框
function showSimpleTip(result) {
	// result 结果为1表示成功，结果为0表示失败
	if (result == '1') {
		$.omMessageBox.alert({
			type : 'success',
			title : '操作提示',
			content : '操作成功！'
		});
	}
	if (result != '1') {
		$.omMessageBox.alert({
			type : 'error',
			title : '操作提示',
			content : '操作失败！'
		});
	}
}

// 刷新页面
function shuaxin() {
	jQuery('#grid').omGrid('reload');
}
// iframe窗口调用的关闭操作，返回值是dialog出口的id 例如详情dialog 则为#dialog-detail
function fillBackAndCloseDialog(msg) {
	// jQuery('#grid').omGrid('reload',1);
	$(msg).omDialog('close');
}
// 删除空格
function trim(str) {
	if (str == null) {
		return "";
	}
	return str.replace(/^\s*(.*?)[\s\n]*$/g, '$1');
}

// 隐藏框
function display(servMonth, interval, conMode) {
	var traget = document.getElementById(servMonth);
	traget.style.display = "none";
	var traget = document.getElementById(interval);
	traget.style.display = "none";
	var traget = document.getElementById(conMode);
	traget.style.display = "none";
}
// 显示框
function showTr(servMonth, interval, conMode) {
	var traget = document.getElementById(servMonth);
	traget.style.display = "";
	var traget = document.getElementById(interval);
	traget.style.display = "";
	var traget = document.getElementById(conMode);
	traget.style.display = "";
}

function isNotEmpty(v) {
	if (!v) {
		return false;
	}
	if (v == '') {
		return false;
	}
	return true;
}

// 新增方法js

// 功能：隐藏、显示表格中的一行，行由参数给出
// 参数：num 行；display 1 显示 0 隐藏
// *********************************
function HiddenTr(num, display) {
	var tempTable = document.getElementsByTagName("table")[0] // 表示页面中第几个表格，在页面中从上往下数
	var tempTd = tempTable.getElementsByTagName("tr")[num]
	if (display == "1")
		tempTd.style.display = ""
	else
		tempTd.style.display = "none"
	var tempTd = tempTable.getElementsByTagName("tr")[num + 1]
	if (display == "1")
		tempTd.style.display = ""
	else
		tempTd.style.display = "none"
	var tempTd = tempTable.getElementsByTagName("tr")[num + 2]
	if (display == "1")
		tempTd.style.display = ""
	else
		tempTd.style.display = "none"
}

function HiddenTr1(num, display) {
	var tempTable = document.getElementsByTagName("table")[0] // 表示页面中第几个表格，在页面中从上往下数
	var tempTd = tempTable.getElementsByTagName("tr")[num]
	if (display == "1")
		tempTd.style.display = ""
	else
		tempTd.style.display = "none"
}

var timer;

// iframe窗口调用的关闭操作，返回值是dialog出口的id 例如详情dialog 则为#dialog-detail
function fillBackAndCloseDialog(msg) {
	$(msg).omDialog('close');
}

function closeBySub() {
	parent.fillBackAndCloseDialog("#dialog");
}

function getURL(url) {
	var params = $("#form").serialize();
	url += "&" + params;
	return url;
}

function doQuery(url) {
	url = getURL(url);
	$('#grid').omGrid("setData", url);
}
function doExport(url) {
	var start = $("#startDate").val();
	var end = $("#endDate").val();
	if (start == '') {
		$("#result").html('请选择开始时间');
		$("#dialog-modal").omDialog('open');
		return;
	}
	if (end == '') {
		$("#result").html('请选择结束时间');
		$("#dialog-modal").omDialog('open');
		return;
	}
	$('#form')[0].action = getURL(url);
	$('#form')[0].submit();
}
var ID;
function checkConditions(state, message){
	var selections;
	ID = "";
	try {
		selections = $('#grid').omGrid('getSelections', true);
	} catch (e) {
		$.omMessageBox.alert({
			title : '操作提示',
			content : '没有数据！'
		});
		return false;
	}
	if (selections.length == 0) {
		$.omMessageBox.alert({
			title : '操作提示',
			content : '请至少选择一行记录！'
		});
		return false;
	}
	var check = 0;// 校验值 默认为0 表示正常
	for ( var i = 0; i < selections.length; i++) {
		if (selections[i].BAK_STAT != selections[0].BAK_STAT
				|| selections[i].BAK_STAT != state) {
			check = 1;
			break;
		}
		ID = trim(selections[i].OPID) + "," + ID;
	}
	if (check == 1) {
		$.omMessageBox.alert({
			title : '操作提示',
			content : message
		});
		return false;
	} 
	return true;
}
// 批量通过
function batchPass() {
	if(checkConditions(1, '请选择\"报备状态\"为\"提交\"的数据进行批量操作！')){
		doOPT(ID, '2');
	}
}
// 批量不通过
function batchNotPass() {
	if(checkConditions(1, '请选择\"报备状态\"为\"提交\"的数据进行批量操作！')){
		doOPTWithComments(ID, '4');
	}
}

// 批量拒绝
function batchDecline() {
	if(checkConditions(2, '请选择\"报备状态\"为\"通过\"的数据进行批量操作！')){
		doOPTWithComments(ID, '5');
	}
}

// 批量报备
function batchReport() {
	if(checkConditions(2, '请选择\"报备状态\"为\"通过\"的数据进行批量操作！')){
		doOPT(ID, '6');
	}
}

// 单条操作
function doOPT(id, type) {
	var titleMsg;
	var contentMsg;
	if(type=='2'){
		titleMsg='检查通过';
		contentMsg='确认通过此信息？';
	}else if(type=='6'){
		titleMsg='报备完成';
		contentMsg='您确认该条数据已经报备完成？';
	}
	$.omMessageBox.confirm({
		title : titleMsg,
		content : contentMsg,
		onClose : function(value) {
			if (value) {
				$.ajax({
					url : 'reportopt.do',
					type : 'POST',
					dataType : 'json',
					data : {
						'opid' : id,
						'optype' : type
					},
					success : function(data) {
						showSimpleTip(data.code);
						shuaxin();
					}
				});
			}
		}
	});
}

// 单条操作
function doOPTWithComments(id, type) {
	var titleMsg;
	var contentMsg;
	if(type=='4'){
		titleMsg='检查不通过';
		contentMsg='请输入您的意见：';
	}else if(type=='5'){
		titleMsg='报备拒绝';
		contentMsg='请输入被拒绝的原因：';
	}
	$.omMessageBox.prompt({
		title : titleMsg,
		content : contentMsg,
		onClose : function(value) {
			if (value === false) {
				return;
			}
			if (value == '') {
				alert('意见不能为空');
				return false;
			}
			$.ajax({
				url : 'reportopt.do',
				type : 'POST',
				dataType : 'json',
				data : {
					'opid' : id,
					'optype' : type,
					'reason' : value
				},
				success : function(data) {
					showSimpleTip(data.code);
					shuaxin();
				}
			});
		}
	});
}
