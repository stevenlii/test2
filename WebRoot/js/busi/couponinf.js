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

// 链接到添加兑换券页面
function add() {
	$("#dialog").omDialog("option", "width", '650');
	$("#dialog").omDialog("option", "title", '添加兑换券信息');
	$("#dialog").omDialog("option", "height", '320');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'add.do';
}

// 查询方法
function query() {
	var couponId = jQuery('#couponId').val();
	var state = jQuery('#state').val();
	var auditState = jQuery("#auditState").val();
	var key = "query.do?queryKey=couponinf.all";
	if (couponId != "") {
		key = key + '&couponId=' + encodeURI(couponId);
	}
	if (state != "") {
		key = key + '&state=' + encodeURI(state);
	}
	if (auditState != "") {
		key = key + '&auditState=' + encodeURI(auditState);
	}
	$('#grid').omGrid("setData", key);
}

// 显示修改方法
function showModifyDialog(couponId, auditId) {
	$("#dialog").omDialog("option", "width", '750');
	$("#dialog").omDialog("option", "title", '修改兑换券信息');
	$("#dialog").omDialog("option", "height", '320');
	$("#dialog").omDialog("option", "modal", true);

	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'modify.do?couponId=' + encodeURI(couponId) + "&auditId="
			+ encodeURI(auditId);
	$("#dialog").omDialog( {
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}
// 显示详情方法
function showDetailDialog(couponId, auditId) {
	$("#dialog").omDialog("option", "width", '500');
	$("#dialog").omDialog("option", "title", '兑换券信息');
	$("#dialog").omDialog("option", "height", '320');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'detail.do?couponId=' + encodeURI(couponId) + "&auditId="
			+ encodeURI(auditId);
	$("#dialog").omDialog( {
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}
// 显示审核页面方法
function showAuditDialog(auditId) {
	$("#dialog").omDialog("option", "width", '750');
	$("#dialog").omDialog("option", "title", '审核兑换券信息');
	$("#dialog").omDialog("option", "height", '460');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'auditDetail.do?auditId=' + auditId;
	$("#dialog").omDialog( {
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}

// 兑换券审核通过方法
function goPass(id,couponId) {
	window.parent.$.omMessageBox.confirm( {
		title : '确认审核',
		content : '确认审核通过此信息？',
		onClose : function(value) {
			if (value) {
				var resultDesc = document.getElementById("resultDesc").value;
				if (resultDesc.length > 64) {
					window.parent.$.omMessageBox.alert( {
						type: 'error',
						title : '操作提示',
						content : '审核意见最大长度为64个字符！'
					});
					return false;
				}
				// 设置按钮不可用，防止重复提交
				$("#btn_pass").attr("disabled", "disabled");
				$.ajax( {
					url : 'auditPass.do',
					type : 'POST',
					dataType : 'text',
					data : {
						'auditId' : id,
						'couponId' : couponId,
						'resultDesc' : resultDesc
					},
					success : function(data) {
						window.parent.showSimpleTip1(data);
						window.parent.shuaxin();
						closeDialog();
					}
				});
			}
		}
	});
}

// 兑换券审核不通过
function goNotPass(id,couponId) {
	var flag = true;
	var resultDesc = document.getElementById("resultDesc").value;
	if (resultDesc == "") {
		window.parent.$.omMessageBox.alert( {
			title : '操作提示',
			content : '请先输入审核意见！'
		});
		flag = false;
	}
	if (resultDesc.length > 64) {
		window.parent.$.omMessageBox.alert( {
			type: 'error',
			title : '操作提示',
			content : '审核意见最大长度为64个字符！'
		});
		flag = false;
	}
	if (flag) {
		// 设置按钮不可用，防止重复提交
		$("#btn_nopass").attr("disabled", "disabled");
		$.ajax( {
			url : 'auditNoPass.do',
			type : 'POST',
			dataType : 'text',
			data : {
				'auditId' : id,
				'couponId' : couponId,
				'resultDesc' : resultDesc
			},
			success : function(data) {
				window.parent.showSimpleTip1(data);
				window.parent.shuaxin();
				closeDialog();
			}
		});
	}
}