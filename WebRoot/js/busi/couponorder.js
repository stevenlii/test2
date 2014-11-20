 function getURL(url){
	 var params = $("#form").serialize();
	 url += "&"+ params;
	 return url;
 }
 
 function doQuery(url){
	 url = getURL(url);
	 $('#grid').omGrid("setData", url);
 }

//显示订单详情方法
 function showOrderDetailDialog(orderId, orderState) {
 	$("#dialog").omDialog("option", "width", '670');
 	$("#dialog").omDialog("option", "title", '订单详情信息');
 	if(orderState == 2 || orderState == 4){
 		$("#dialog").omDialog("option", "height", '480');
 	}else{
 		$("#dialog").omDialog("option", "height", '190');
 	}
 	$("#dialog").omDialog("option", "modal", true);
 	$("#dialog").omDialog('open');
 	var frameLoc = window.frames[0].location;
 	frameLoc.href = 'orderDetail.do?orderId=' + orderId;
 	$("#dialog").omDialog( {
 		onClose : function(event) {
 			window.frames[0].location.href = '../wait.htm';
 		}
 	});
 	return false;
 }
 
 // 显示取码订单详情方法
 function showGcOrderDetail(orderId, orderState) {
	    $("#dialog").omDialog("option", "width", '670');
	    $("#dialog").omDialog("option", "title", '订单详情信息');
	    if (orderState == 2 || orderState == 4) {
	        $("#dialog").omDialog("option", "height", '480');
	    } else {
	        $("#dialog").omDialog("option", "height", '190');
	    }
	    $("#dialog").omDialog("option", "modal", true);
	    $("#dialog").omDialog('open');
	    var frameLoc = window.frames[0].location;
	    frameLoc.href = 'gcdetail.do?orderId=' + orderId;
	    $("#dialog").omDialog({
	        onClose: function(event) {
	            window.frames[0].location.href = '../wait.htm';
	        }
	    });
	    return false;
	}
 
//iframe窗口调用的关闭操作，返回值是dialog出口的id 例如详情dialog 则为#dialog-detail
 function closeDialog(msg) {
 	$(msg).omDialog('close');
 }
 
//兑换码补发
function regive(inCouponCode) {
 	$.omMessageBox.confirm( {
 		title : '确认操作',
 		content : '你确定要执行补发吗？',
 		onClose : function(value) {
 			if (value) {
 				$("#couponCode").val(inCouponCode);
 		   		$("#op").val('1');
 		   		$('#formID').submit();
 			}
 		}
 	});
 }

//兑换码注销
function cancel(inCouponCode) {
 	$.omMessageBox.confirm( {
 		title : '确认操作',
 		content : '你确定要执行此注销吗？',
 		onClose : function(value) {
 			if (value) {
 				$("#couponCode").val(inCouponCode);
 		   		$("#op").val('0');
 		   		$('#formID').submit();
 			}
 		}
 	});
 }

//代金劵回退
function revert(inCouponCode) {
 	$.omMessageBox.confirm( {
 		title : '确认操作',
 		content : '你确定要执行此回退吗？',
 		onClose : function(value) {
 			if (value) {
 				$("#couponCode").val(inCouponCode);
 		   		$("#op").val('2');
 		   		$('#formID').submit();
 			}
 		}
 	});
 }