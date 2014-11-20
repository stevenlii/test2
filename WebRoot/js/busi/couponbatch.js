//当前页面弹出操作提示框
function showSimpleTip(result) {
	// result 结果为1表示成功，结果为0表示失败
	if (result == '1') {
		$.omMessageBox.alert( {
			type : 'success',
			title : '操作提示',
			content : '操作成功，请等待管理员审核！'
		});
	}
	if (result != '1') {
		$.omMessageBox.alert( {
			type : 'error',
			title : '操作提示',
			content : '操作失败，系统繁忙请稍后再试！'
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
			content : '操作失败，系统繁忙请稍后再试！'
		});
	}
}

// iframe窗口调用的关闭操作，返回值是dialog出口的id 例如详情dialog 则为#dialog-detail
function fillBackAndCloseDialog(msg) {
	$(msg).omDialog('close');
}

// 刷新页面
function shuaxin() {
	jQuery('#grid').omGrid('reload');
}

// 链接到添加批次页面
function add() {
	var ruleId = $('#ruleId').val();
	var couponId = $('#couponId').val();
	var merId = $('#merId').val();
	var goodsId = $('#goodsId').val();
	$("#subdialog").omDialog("option", "width", '700');
	$("#subdialog").omDialog("option", "title", '批量导入');
	$("#subdialog").omDialog("option", "height", '250');
	$("#subdialog").omDialog("option", "modal", true);
	$("#subdialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = '/hfMngBusi/couponbatch/add.do?ruleId=' + encodeURI(ruleId)
			+ '&couponId=' + encodeURI(couponId) + '&merId=' + encodeURI(merId)
			+ '&goodsId=' + encodeURI(goodsId);
}

// 链接到批次修改页面
function showModifyDialog(inBatchId, rowIndex) {
	var batchName = "第" + rowIndex + "批次";
	$("#subdialog").omDialog("option", "width", '570');
	$("#subdialog").omDialog("option", "title", '修改批次');
	$("#subdialog").omDialog("option", "height", '200');
	$("#subdialog").omDialog("option", "modal", true);
	$("#subdialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = '/hfMngBusi/couponbatch/modify.do?batchId='
			+ encodeURI(inBatchId) + '&batchName=' + encodeURI(batchName);
}

// 链接到批次追加导入页面
function showAppendDialog(inBatchId) {
	var ruleId = $('#ruleId').val();
	var couponId = $('#couponId').val();
	var merId = $('#merId').val();
	var goodsId = $('#goodsId').val();
	$("#subdialog").omDialog("option", "width", '670');
	$("#subdialog").omDialog("option", "title", '追加导入');
	$("#subdialog").omDialog("option", "height", '200');
	$("#subdialog").omDialog("option", "modal", true);
	$("#subdialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = '/hfMngBusi/couponbatch/add.do?ruleId=' + encodeURI(ruleId)
			+ '&couponId=' + encodeURI(couponId) + '&merId=' + encodeURI(merId)
			+ '&goodsId=' + encodeURI(goodsId) + '&batchId='
			+ encodeURI(inBatchId);
}

// 查询方法
function query() {
	var ruleId = $('#ruleId').val();
	var state = $('#state').val();
	var key = "/hfMngBusi/couponbatch/query.do?queryKey=couponbatch.all";
	key = key + '&ruleId=' + encodeURI(ruleId);
	if (state != "") {
		key = key + '&state=' + encodeURI(state);
	}
	$('#grid').omGrid("setData", key);
}

// 停止批次
function stopBatch(inBatchId) {
	$.omMessageBox.confirm( {
		title : '确认停止',
		content : '批次停止后，它所有相关的兑换码都可不使用，你确定要停止吗？',
		onClose : function(value) {
			if (value) {
				$.get("/hfMngBusi/couponbatch/stopbatch.do", {
					batchId : inBatchId
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

// 启用批次
function startBatch(inBatchId) {
	$.omMessageBox.confirm( {
		title : '确认启用',
		content : '批次启用后，它所有相关的兑换码可销售，你确定要启用吗？',
		onClose : function(value) {
			if (value) {
				$.get("/hfMngBusi/couponbatch/startbatch.do", {
					batchId : inBatchId
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
