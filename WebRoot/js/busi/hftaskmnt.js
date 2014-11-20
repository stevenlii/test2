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


//查询方法
function queryTaskMntInfo() {
	var taskRpid = jQuery('#taskRpid').val();
	var taskId = jQuery('#taskId').val();
	var state = jQuery("#state").val();
	var sendType = jQuery("#sendType").val();
	var key = "queryTaskMntInfo.do?queryKey=hfTask.queryTaskMntInfo";
	if (taskRpid != "") {
		key = key + '&taskRpid=' + encodeURI(taskRpid);
	}
	if (taskId != "") {
		key = key + '&taskId=' + encodeURI(taskId);
	}
	if (state != "") {
		key = key + '&state=' + encodeURI(state);
	}
	if (sendType != "") {
		key = key + '&sendType=' + encodeURI(sendType);
	}
	$('#grid').omGrid("setData", key);
}

// 显示监控详细信息方法
function showDetailDialog(taskRpid) {
	$("#dialog").omDialog("option", "width", '630');
	$("#dialog").omDialog("option", "title", '定时任务监控信息');
	$("#dialog").omDialog("option", "height", '390');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'taskMntDetail.do?taskRpid=' + encodeURI(taskRpid); 
	$("#dialog").omDialog( {
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}
