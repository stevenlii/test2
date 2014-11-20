//当前页面弹出操作提示框
function showSimpleTip(result) {
	// result 结果为1表示成功，结果为0表示失败
	if (result == '1') {
		$.omMessageBox.alert( {
			type : 'success',
			title : '操作提示',
			content : '操作成功！'
		});
	}else if(result == '2'){
		$.omMessageBox.alert( {
			type : 'success',
			title : '操作提示',
			content : '操作成功！请到任监控页面查看。'
		});
	}else{
		$.omMessageBox.alert( {
			type : 'error',
			title : '操作提示',
			content : '操作失败！'
		});
	}
}

// 刷新页面
function gridReload() {
	jQuery('#grid').omGrid('reload');
}

// iframe窗口调用的关闭操作，返回值是dialog出口的id 例如详情dialog 则为#dialog-detail
function fillBackAndCloseDialog(msg) {
	$(msg).omDialog('close');
}

// 链接到添加定时任务页面
function add(){
	$( "#dialog").omDialog("option","width",'680');
    $( "#dialog").omDialog("option","title",'添加新定时任务');
    $( "#dialog").omDialog("option","height",'500');
    $( "#dialog").omDialog("option", "modal", true);
    $( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
    frameLoc.href='add.do';
}

// 查询方法
function queryTaskInfo() {
	var platName = jQuery('#platName').val();
	var taskDesc = jQuery('#taskDesc').val();
	var state = jQuery("#state").val();
	var key = "queryTaskInfo.do?queryKey=hfTask.queryTaskInfo";
	if (platName != "") {
		key = key + '&platName=' + encodeURI(platName);
	}
	if (taskDesc != "") {
		key = key + '&taskDesc=' + encodeURI(taskDesc);
	}
	if (state != "") {
		key = key + '&state=' + encodeURI(state);
	}
	$('#grid').omGrid("setData", key);
}

// 显示修改方法
function showModifyDialog(taskId) {
	$( "#dialog").omDialog("option","width",'680');
    $( "#dialog").omDialog("option","title",'修改定时任务信息');
    $( "#dialog").omDialog("option","height",'500');
    $( "#dialog").omDialog("option", "modal", true);
    $( "#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'toModify.do?taskId=' + encodeURI(taskId); 
	$("#dialog").omDialog( {
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}

// 显示详情方法
function showDetailDialog(taskId) {
	$("#dialog").omDialog("option", "width", '550');
	$("#dialog").omDialog("option", "title", '定时任务信息');
	$("#dialog").omDialog("option", "height", '450');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'detail.do?taskId=' + encodeURI(taskId); 
	$("#dialog").omDialog( {
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}

//手动执行任务方法
function showManualRunTaskDialog(taskId) {
	$("#dialog").omDialog("option", "width", '550');
	$("#dialog").omDialog("option", "title", '手动执行任务确认');
	$("#dialog").omDialog("option", "height", '320');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'taskRunConfirm.do?taskId=' + encodeURI(taskId); 
	$("#dialog").omDialog( {
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}
