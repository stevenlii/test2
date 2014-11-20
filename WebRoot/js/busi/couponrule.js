var timer;
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

function closeBySub() {
	parent.fillBackAndCloseDialog("#dialog");
	//parent.shuaxin();
}
// 链接到添加兑换券规则页面
function add() {
	$("#dialog").omDialog("option", "width", '670');
	$("#dialog").omDialog("option", "title", '添加兑换券规则');
	$("#dialog").omDialog("option", "height", '520');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'add.do';
}
function expt(ruleid, exp) {
	var time = new Date().getTime();
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'export.do?ruleid=' + ruleid + "&exp=" + exp + "&time=" + time;
}

//链接到添加兑换券规则页面
function exportindex(ruleid, exp) {
	var time = new Date().getTime();
	$("#dialog").omDialog("option", "width", '300');
	$("#dialog").omDialog("option", "title", '导出兑换码');
	$("#dialog").omDialog("option", "height", '150');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'exportindex.do?ruleid=' + ruleid + "&exp=" + exp + "&time=" + time;
}

// 查询方法
function query() {
	var ruleId = jQuery('#ruleId').val();
	var state = jQuery('#state').val();
	var auditState = jQuery("#auditState").val();
	var isAlarm = 0;
	if ($("#isAlarm").is(":checked")) {
		isAlarm = 1;
	}
	var couponId = jQuery('#couponId').val();
	var merId = jQuery('#merId').val();
	var goodsId = jQuery('#goodsId').val();
	var key = "query.do?queryKey=couponrule.all";
	if (ruleId != "") {
		key = key + '&ruleId=' + encodeURI(ruleId);
	}
	if (couponId != "") {
		key = key + '&couponId=' + encodeURI(couponId);
	}
	if (isAlarm != 0) {
		key = key + '&isAlarm=' + encodeURI(isAlarm);
	}
	if (merId != "") {
		key = key + '&merId=' + encodeURI(merId);
	}
	if (goodsId != "") {
		key = key + '&goodsId=' + encodeURI(goodsId);
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
function showModifyDialog(ruleId, auditId) {
	$("#dialog").omDialog("option", "width", '680'); 
	$("#dialog").omDialog("option", "title", '修改兑换券规则');
	$("#dialog").omDialog("option", "height", '520');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'modify.do?ruleId=' + encodeURI(ruleId) + "&auditId="
			+ encodeURI(auditId);
	$("#dialog").omDialog( {
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}

// 显示详情方法
function showDetailDialog(ruleId, auditId) {
	$("#dialog").omDialog("option", "width", '550');
	$("#dialog").omDialog("option", "title", '兑换券规则');
	$("#dialog").omDialog("option", "height", '520');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'detail.do?ruleId=' + encodeURI(ruleId) + "&auditId="
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
	$("#dialog").omDialog("option", "width", '670');
	$("#dialog").omDialog("option", "title", '审核兑换券规则');
	$("#dialog").omDialog("option", "height", '540');
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

// 显示批次页面方法
function showCodeBatchDialog(ruleId) {
	$("#dialog").omDialog("option", "width", '760');
	$("#dialog").omDialog("option", "title", '批次信息');
	$("#dialog").omDialog("option", "height", '480');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc = window.frames[0].location;
	frameLoc.href = 'goCodeBatch.do?ruleId=' + ruleId;
	$("#dialog").omDialog( {
		onClose : function(event) {
			window.frames[0].location.href = '../wait.htm';
		}
	});
	return false;
}

// 兑换券规则审核通过方法
function goPass(id) {
	window.parent.$.omMessageBox.confirm( {
		title : '确认审核',
		content : '确认审核通过此信息？',
		onClose : function(value) {
			if (value) {
				var resultDesc = $("#resultDesc").val();
				if (resultDesc.length > 64) {
					window.parent.$.omMessageBox.alert( {
						type : 'error',
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
						'resultDesc' : resultDesc
					},
					success : function(data) {
						if(data == '1'){
	    					window.parent.showSimpleTip1(data);
	             			closeDialog();
	    					window.parent.shuaxin();
	             		}else if(data == '0'){
	             			window.parent.showSimpleTip1(data);
	             		}else{
	             			window.parent.$.omMessageBox.alert({
	        		    		type:'error',
	        		            title : '操作提示',
	        		            content : data
	        		        });
	             		}
						// 设置按钮可用
						$("#btn_pass").removeAttr("disabled");
					}
				});
			}
		}
	});
}

// 兑换券规则审核不通过
function goNotPass(id) {
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
			type : 'error',
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
				'resultDesc' : resultDesc
			},
			success : function(data) {
				if(data == '1'){
					window.parent.showSimpleTip1(data);
         			closeDialog();
					window.parent.shuaxin();
         		}else if(data == '0'){
         			window.parent.showSimpleTip1(data);
         		}else{
         			window.parent.$.omMessageBox.alert({
    		    		type:'error',
    		            title : '操作提示',
    		            content : data
    		        });
         		}
				// 设置按钮可用
				$("#btn_nopass").removeAttr("disabled");
			}
		});
	}
}
