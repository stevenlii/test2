function showResponse(responseText, statusText) {
    var result = eval("(" + responseText + ")");
    if (result.code == '0') {
        window.parent.fillBackAndCloseDialog(result.msg);
    }
}

// 关闭窗口方法
function  closeDialog(){
    window.parent.fillBackAndCloseDialog("#dialog");
    }

  // 当前页面弹出操作提示框
function showSimpleTip(result) {
    // result 结果为1表示成功，结果为0表示失败
    if (result == '1') {
        $.omMessageBox.alert({
            type: 'success',
            title: '操作提示',
            content: '操作成功！'
        });
    }
    if (result != '1') {
        $.omMessageBox.alert({
            type: 'error',
            title: '操作提示',
            content: '操作失败！'
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
    if (display == "1") tempTd.style.display = ""
    else tempTd.style.display = "none"
    var tempTd = tempTable.getElementsByTagName("tr")[num + 1]
    if (display == "1") tempTd.style.display = ""
    else tempTd.style.display = "none"
    var tempTd = tempTable.getElementsByTagName("tr")[num + 2]
    if (display == "1") tempTd.style.display = ""
    else tempTd.style.display = "none"
}

function HiddenTr1(num, display) {
    var tempTable = document.getElementsByTagName("table")[0] // 表示页面中第几个表格，在页面中从上往下数
    var tempTd = tempTable.getElementsByTagName("tr")[num]
    if (display == "1") tempTd.style.display = ""
    else tempTd.style.display = "none"
}

var timer;

// iframe窗口调用的关闭操作，返回值是dialog出口的id 例如详情dialog 则为#dialog-detail
function fillBackAndCloseDialog(msg) {
	$(msg).omDialog('close');
}


function closeBySub() {
	parent.fillBackAndCloseDialog("#dialog");
}

function getURL(url){
	 var params = $("#form").serialize();
	 url += "&"+ params;
	 return url;
}

function doQuery(url){
	 url = getURL(url);
	 $('#grid').omGrid("setData", url);
}
function doExport(url){
	 var start = $("#startDate").val();
	 var end = $("#endDate").val();
	 if(start == ''){
		 $("#result").html('请选择开始时间');
		 $( "#dialog-modal").omDialog('open');
		 return;
	 }
	 if(end == ''){
		 $("#result").html('请选择结束时间');
		 $( "#dialog-modal").omDialog('open');
		 return;
	 }
	 $('#form')[0].action= getURL(url);
	 $('#form')[0].submit();
}
//审核通过方法
function goPass() {
	window.parent.$.omMessageBox.confirm( {
		title : '确认审核',
		content : '确认审核通过此信息？',
		onClose : function(value) {
			if (value) {
				var resultDesc = document.getElementById("resultDesc").value;
				var auditId = document.getElementById("auditId").value;
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
						'auditId' : auditId,
						'resultDesc' : resultDesc
					},
					success : function(data) {
						window.parent.showSimpleTip(data);
						window.parent.shuaxin();
						closeDialog();
					}
				});
			}
		}
	});
}

//审核不通过
function goNotPass() {
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
		var auditId = document.getElementById("auditId").value;
		$.ajax( {
			url : 'auditNotPass.do',
			type : 'POST',
			dataType : 'text',
			data : {
				'auditId' : auditId,
				'resultDesc' : resultDesc
			},
			success : function(data) {
				window.parent.showSimpleTip(data);
				window.parent.shuaxin();
				closeDialog();
			}, 
			error: function(data,e) { 
				//alert(data.responseText);
			}
		});
	}
}
	function clickTime(){
		var stlCycle = $("#stlCycle").val();
		if(stlCycle=='1'||stlCycle=='2'||stlCycle=='5'||stlCycle==null||stlCycle==''){
			WdatePicker({dateFmt:'yyyy-MM',vel:'noticeDate',maxDate: new Date()});
		}else{
			WdatePicker({dateFmt:'yyyy-MM-dd',vel:'noticeDate',maxDate: new Date()});
		}
	}
	

	
