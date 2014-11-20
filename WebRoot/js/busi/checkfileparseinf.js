//当前页面弹出操作提示框
function showSimpleTip(result) {
	// result 结果为1表示成功，结果为0表示失败
	if (result == '1') {
		$.omMessageBox.alert( {
			type : 'success',
			title : '操作提示',
			content : '操作成功！'
		});
	}
	if (result != '1') {
		$.omMessageBox.alert( {
			type : 'error',
			title : '操作提示',
			content : '操作失败！'
		});
	}
}

// 当前页面弹出操作提示框
function showSimpleTip1(result) {
	// result 结果为1表示成功，结果为0表示失败
	if (result == '1') {
		$.omMessageBox.alert( {
			type : 'success',
			title : '操作提示',
			content : '操作成功！'
		});
	}
	if (result != '1') {
		$.omMessageBox.alert( {
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
	$(msg).omDialog('close');
}

// 链接到添加任务页面
function add() {
	$("#dialog").omDialog("option", "width", '650');
	$("#dialog").omDialog("option", "title", '添加任务');
	$("#dialog").omDialog("option", "height", '380');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'add.do';
}

// 查询方法
function query() {
	var merId = jQuery('#merId').val();
	var fileType = jQuery('#fileType').val();
	var fileState = jQuery("#fileState").val();
	var startDate = jQuery("#startDate").val();
	var endDate = jQuery("#endDate").val();
	var key = "query.do?queryKey=checkfileparseinf.all";
	if (merId != "") {
		key = key + '&merId=' + encodeURI(merId);
	}
	if (fileType != "") {
		key = key + '&fileType=' + encodeURI(fileType);
	}
	if (fileState != "") {
		key = key + '&fileState=' + encodeURI(fileState);
	}
	if (startDate != "") {
		key = key + '&startDate=' + encodeURI(startDate);
	}
	if (endDate != "") {
		key = key + '&endDate=' + encodeURI(endDate);
	}
	$('#grid').omGrid("setData", key);
}

function deleteTask(fileName1, fileType1) {
	$.omMessageBox.confirm( {
		title : '确认删除',
		content : '你确定要删除该任务吗？',
		onClose : function(value) {
			if (value) {
				$.get("/hfMngBusi/checkfileparseinf/delete.do", {
					fileName : fileName1,
					fileType : fileType1
				}, function(result) {
					if (result == '1') {
						showSimpleTip1(result);
						shuaxin();
					} else if (result == '0') {
						showSimpleTip1(result);
					} else {
						$.omMessageBox.alert( {
							type : 'error',
							title : '操作提示',
							content : result
						});
					}
				});
			}
		}
	});
}

function resetDealTimes(fileName1, fileType1) {
	$.omMessageBox.confirm( {
		title : '确认置零',
		content : '你确定要置零错误次数吗？',
		onClose : function(value) {
			if (value) {
				$.get("/hfMngBusi/checkfileparseinf/resetdealtimes.do", {
					fileName : fileName1,
					fileType : fileType1
				}, function(result) {
					if (result == '1') {
						showSimpleTip1(result);
						shuaxin();
					} else if (result == '0') {
						showSimpleTip1(result);
					} else {
						$.omMessageBox.alert( {
							type : 'error',
							title : '操作提示',
							content : result
						});
					}
				});
			}
		}
	});
}

function reset(fileName1, fileType1) {
	$.omMessageBox.confirm( {
		title : '确认重置',
		content : '你确定要重置任务吗？',
		onClose : function(value) {
			if (value) {
				$.get("/hfMngBusi/checkfileparseinf/reset.do", {
					fileName : fileName1,
					fileType : fileType1
				}, function(result) {
					if (result == '1') {
						showSimpleTip1(result);
						shuaxin();
					} else if (result == '0') {
						showSimpleTip1(result);
					} else {
						$.omMessageBox.alert( {
							type : 'error',
							title : '操作提示',
							content : result
						});
					}
				});
			}
		}
	});
}
