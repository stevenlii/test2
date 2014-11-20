//当前页面弹出操作提示框
function showSimpleTip(result) {
	// result 结果为1表示成功，结果为0表示失败
	if (result == '1') {
		$.omMessageBox.alert({
			type : 'success',
			title : '操作提示',
			content : '操作成功，请等待管理员审核！'
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

// 当前页面弹出操作提示框
function showSimpleTip1(result) {
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
	$(msg).omDialog('close');
}

// 链接到添加兑换券页面
function add() {
	$("#dialog").omDialog("option", "width", '650');
	$("#dialog").omDialog("option", "title", '添加兑换电话信息');
	$("#dialog").omDialog("option", "height", '380');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'add.do';
}

// 查询方法
function query() {
	var merId = jQuery('#merId').val();
	var linkTel = jQuery('#linkTel').val();
	var key = "query.do?queryKey=couponmertel.all";
	if (merId != "") {
		key = key + '&merId=' + encodeURI(merId);
	}
	if (linkTel != "") {
		key = key + '&linkTel=' + encodeURI(linkTel);
	}
	$('#grid').omGrid("setData", key);
}

// 显示修改方法
function showModifyDialog(merTelId) {
	$("#dialog").omDialog("option", "width", '650');
	$("#dialog").omDialog("option", "title", '修改兑换电话信息');
	$("#dialog").omDialog("option", "height", '380');
	$("#dialog").omDialog("option", "modal", true);

	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'modify.do?merTelId=' + encodeURI(merTelId);
	$("#dialog").omDialog({
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}
