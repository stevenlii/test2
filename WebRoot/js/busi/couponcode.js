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
			content : '操作失败，系统繁忙请稍后再试！'
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

// 查询方法
function query() {
	var couponCode = jQuery('#couponCode').val();
	var paidMobileId = jQuery('#paidMobileId').val();
	var state = jQuery('#state').val();
	var couponId = jQuery('#couponId').val();
	var merId = jQuery('#merId').val();
	var goodsId = jQuery('#goodsId').val();
	var channelId = jQuery('#channel').val();
	var key = "query.do?queryKey=couponcode.all";
	if (couponCode != "") {
		key = key + '&couponCode=' + encodeURI(couponCode);
	}
	if (paidMobileId != "") {
		key = key + '&paidMobileId=' + encodeURI(paidMobileId);
	}
	if (state != "") {
		key = key + '&state=' + encodeURI(state);
	}
	if (couponId != 0) {
		key = key + '&couponId=' + encodeURI(couponId);
	}
	if (merId != "") {
		key = key + '&merId=' + encodeURI(merId);
	}
	if (goodsId != "") {
		key = key + '&goodsId=' + encodeURI(goodsId);
	}
	if (channelId != "") {
		key = key + '&channelId=' + encodeURI(channelId);
	}
	$('#grid').omGrid("setData", key);
}

// 导出
function exportCouponCode() {
	var couponCode = jQuery('#couponCode').val();
	var paidMobileId = jQuery('#paidMobileId').val();
	var state = jQuery('#state').val();
	var couponId = jQuery('#couponId').val();
	var merId = jQuery('#merId').val();
	var goodsId = jQuery('#goodsId').val();
	var key = "export.do?queryKey=couponcode.all";
	var channelId = jQuery('#channel').val();
	if (couponCode != "") {
		key = key + '&couponCode=' + encodeURI(couponCode);
	}
	if (paidMobileId != "") {
		key = key + '&paidMobileId=' + encodeURI(paidMobileId);
	}
	if (state != "") {
		key = key + '&state=' + encodeURI(state);
	}
	if (couponId != 0) {
		key = key + '&couponId=' + encodeURI(couponId);
	}
	if (merId != "") {
		key = key + '&merId=' + encodeURI(merId);
	}
	if (goodsId != "") {
		key = key + '&goodsId=' + encodeURI(goodsId);
	}
	if (channelId != "") {
		key = key + '&channelId=' + encodeURI(channelId);
	}
	window.location.href = key;
}

// 显示详情方法
function showDetailDialog(merId, couponCode) {
	$("#dialog").omDialog("option", "width", '550');
	$("#dialog").omDialog("option", "title", '兑换码详情');
	$("#dialog").omDialog("option", "height", '520');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'detail.do?merId=' + encodeURI(merId) + '&couponCode='
			+ encodeURI(couponCode);
	$("#dialog").omDialog({
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}

// 显示修改方法
function showModifyDialog(merId, couponCode) {
	$("#dialog").omDialog("option", "width", '650');
	$("#dialog").omDialog("option", "title", '兑换码修改');
	$("#dialog").omDialog("option", "height", '350');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'modify.do?merId=' + encodeURI(merId) + '&couponCode='
			+ encodeURI(couponCode);
	$("#dialog").omDialog({
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}

// 兑换码补发
// inMerId：商户ID、inCouponCode：未加密兑换码、inOrderId：订单号、inPhone：手机号、inType：日志业务对象、inOp：补发操作为1/注销操作为0
function regive(inMerId, inCouponCode, inOrderId, inPhone, inType, inOp) {
	$.omMessageBox.confirm({
		title : '确认操作',
		content : '你确定要执行补发吗？',
		onClose : function(value) {
			if (value) {
				$.post("/hfMngBusi/couponorder/couponOp.do", {
					op : inOp,
					merId : inMerId,
					couponCode : inCouponCode,
					orderId : inOrderId,
					phone : inPhone,
					type : inType
				}, function(result) {
					var resultJson = eval('(' + result + ')');
					$.omMessageBox.alert({
						content : resultJson.msg,
						onClose : function(v) {
							shuaxin();
						}

					});
				});
			}
		}
	});
}

// 兑换码注销
// inMerId：商户ID、inCouponCode：未加密兑换码、inOrderId：订单号、inPhone：手机号、inType：日志业务对象、inOp：补发操作为1/注销操作为0
function cancel(inMerId, inCouponCode, inOrderId, inPhone, inType, inOp) {
	$.omMessageBox.confirm({
		title : '确认操作',
		content : '你确定要执行此注销吗？',
		onClose : function(value) {
			if (value) {
				$.post("/hfMngBusi/couponorder/couponOp.do", {
					op : inOp,
					merId : inMerId,
					couponCode : inCouponCode,
					orderId : inOrderId,
					phone : inPhone,
					type : inType
				}, function(result) {
					var resultJson = eval('(' + result + ')');
					$.omMessageBox.alert({
						content : resultJson.msg,
						onClose : function(v) {
							shuaxin();
						}

					});
				});
			}
		}
	});
}
