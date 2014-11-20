//刷新页面
function shuaxin(){    	
	jQuery('#grid').omGrid('reload');
}
// 当前页面弹出操作提示框
function showSimpleTip(result){
	if(result=='1'){
		$.omMessageBox.alert({
			type:'success',
			title : '操作提示',
			content : '操作成功'
       	});
		shuaxin();
	}else{
		var msg = '操作失败';
		if(result != '0'){
			msg = result;
		}
		$.omMessageBox.alert({
			type:'error',
			title : '操作提示',
			content : msg
		});
	}
}
// 增加方法
function show(){
	$( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='add.do';
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
	return false;
}
 // 查询方法
function querygoodsbank(){
	var url ="query.do";
	var bankId=$('#bankId').omCombo('value');
	var state=$('#state').omCombo('value');
	var merId=$('#merId').omCombo('value');
	var goodsId=$('#goodsId').omCombo('value');
	var isRealTime=$('#isRealTime').omCombo('value');
	
	var key="?queryKey=goodsbank.allgoodsbank";
	if(bankId != "" &bankId !="全部"){
		key= key +'&bankId='+encodeURI(bankId);
	}
	if(state != ""){ // 没要有查询条件，要显示全部数据
		
		key=key+'&state='+encodeURI(state); 
	}
	if(merId !=""&merId !="全部")
	{ 
		key=key+'&merId='+encodeURI(merId);
	}
	if(goodsId !=""&goodsId !="全部")
	{ 
		key=key+'&goodsId='+encodeURI(goodsId);
	}
	if(isRealTime !=""&isRealTime !="全部")
	{ 
		key=key+'&isRealTime='+encodeURI(isRealTime);
	}
	$('#grid').omGrid("setData", url+key);
}
 
// 查询方法
function queryupgoodsbank(){
	var url ="query.do";
	var bankId=$('#bankId').omCombo('value');
	var state=$('#state').omCombo('value');
	var merId=$('#merId').omCombo('value');
	var goodsId=$('#goodsId').omCombo('value');
	var isRealTime=$('#isRealTime').omCombo('value');
	
	var key="?queryKey=goodsbank.allupgoodsbank";
	if(bankId != "" &bankId !="全部"){
		key= key +'&bankId='+encodeURI(bankId);
	}
	if(state != ""){ // 没要有查询条件，要显示全部数据
		
		key=key+'&state='+encodeURI(state);
	}
	if(merId !=""&merId !="全部")
	{ 
		key=key+'&merId='+encodeURI(merId);
	}
	if(goodsId !=""&goodsId !="全部")
	{ 
		key=key+'&goodsId='+encodeURI(goodsId);
	}
	if(isRealTime !=""&isRealTime !="全部")
	{ 
		key=key+'&isRealTime='+encodeURI(isRealTime);
	}
	$('#grid').omGrid("setData", url+key);
}

// 显示修改方法
function showModifyDialog(merId,goodsId,bankId){
	$( "#dialog").omDialog("option","width",'550');
	$( "#dialog").omDialog("option","title",'修改商品银行信息');
	$( "#dialog").omDialog("option","height",'500');
	$( "#dialog").omDialog("option", "modal", true);
	$( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='modify.do?merId='+merId+'&goodsId='+goodsId+'&bankId='+bankId;
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
	return false;
}
// 显示详情方法
function showDetailDialog(merId,goodsId,bankId){
	$( "#dialog").omDialog("option","width",'450');
	$( "#dialog").omDialog("option","title",'显示商品银行信息');
	$( "#dialog").omDialog("option","height",'370');
	$( "#dialog").omDialog("option", "modal", true);
	$( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='detail.do?merId='+merId+'&goodsId='+goodsId+'&bankId='+bankId;
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
	return false;
}
// iframe窗口调用的关闭操作，返回值是dialog出口的id 例如详情dialog 则为#dialog-detail
function fillBackAndCloseDialog(msg){		
// jQuery('#grid').omGrid('reload',1);
	$( msg).omDialog('close');
}
// 删除空格
function trim(str) {
	if (str == null) {
		return "";
	}
	return str.replace(/^\s*(.*?)[\s\n]*$/g,'$1');   
}

// 多条件查询方法 -- add by wanyong 2014-04-25
function queryOptLog() {
    var merId = jQuery('#merId').val();
    var goodsName = jQuery('#goodsName').val();
    var bankId = jQuery('#bankId').val();
    var goodsId = jQuery('#goodsId').val();
    var creator = jQuery('#creator').val();
    var from = jQuery('#from').val();
    var to = jQuery('#to').val();

    var ixData = "";
    var key = "";

    if (bankId == "全部") {
        bankId = "";
    }
    if (creator == "全部") {
        creator = "";
    }

    if (goodsName != "") {
        // 数据库 ixData2 字段实际上是放的商品名称，提供操作日志页面查询索引
        key = key + '&ixData2=' + encodeURI(goodsName);
    }
    if (creator != "") {
        key = key + '&creator=' + encodeURI(creator);
    }
    if (from != "" && to == "") {
        alert("请填写提交时间的结束时间");
        return false;
    } else if (from == "" && to != "") {
        alert("请填写提交时间的开始时间");
        return false;
    } else if (from != "" && to != "") {
    	// 查询时间验证：只提供最近半年操作日志查询
    	var dtTmp = new Date();
    	var minTime = new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) - 6, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
    	if( minTime.format("yyyy-MM-dd 00:00:00") > from ){
    		$.omMessageBox.alert({
    			type:'error',
    			title : '操作提示',
    			content : '超出范围，请查询最近半年的操作日志！'
           	});
    		return false;
    	}
        key = key + '&from=' + encodeURI(from);
        key = key + '&to=' + encodeURI(to);
    }
    if (goodsId == "") {
    	goodsId = "%";
    }
    ixData = merId + "-" + goodsId + "-" + bankId;
    if (ixData != "") {
        key = key + '&ixData=' + encodeURI(ixData);
    }
    $('#grid').omGrid("setData", url + key);
}

//多条件查询方法 -- add by wanyong 2014-04-25
function exportOptLog() {
    var merId = jQuery('#merId').val();
    var goodsName = jQuery('#goodsName').val();
    var bankId = jQuery('#bankId').val();
    var goodsId = jQuery('#goodsId').val();
    var creator = jQuery('#creator').val();
    var from = jQuery('#from').val();
    var to = jQuery('#to').val();

    var ixData = "";
    var key = "exportoptlog.do?queryKey=goodsbank.optlog";

    if (bankId == "全部") {
        bankId = "";
    }
    if (creator == "全部") {
        creator = "";
    }

    if (goodsName != "") {
        // 数据库 ixData2 字段实际上是放的商品名称，提供操作日志页面查询索引
        key = key + '&ixData2=' + encodeURI(goodsName);
    }
    if (creator != "") {
        key = key + '&creator=' + encodeURI(creator);
    }
    if (from != "" && to == "") {
        alert("请填写提交时间的结束时间");
        return false;
    } else if (from == "" && to != "") {
        alert("请填写提交时间的开始时间");
        return false;
    } else if (from != "" && to != "") {
    	// 查询时间验证：只提供最近半年操作日志查询
    	var dtTmp = new Date();
    	var minTime = new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) - 6, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
    	if( minTime.format("yyyy-MM-dd 00:00:00") > from ){
    		$.omMessageBox.alert({
    			type:'error',
    			title : '操作提示',
    			content : '超出范围，请查询最近半年的操作日志！'
           	});
    		return false;
    	}
        key = key + '&from=' + encodeURI(from);
        key = key + '&to=' + encodeURI(to);
    }
    if (goodsId == "") {
    	goodsId = "%";
    }
    ixData = merId + "-" + goodsId + "-" + bankId;
    if (ixData != "") {
        key = key + '&ixData=' + encodeURI(ixData);
    }
    window.location.href = key;
}

// 日期格式化 -- add by wanyong 2014-04-25
Date.prototype.format = function(format) {
    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds() // millisecond
    };
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

//多条件查询方法 -- add by wanyong 2014-04-25
function queryBatchOptLog() {
    var creator = jQuery('#creator').val();
    var from = jQuery('#from').val();
    var to = jQuery('#to').val();
    var key = "";
    if (creator == "全部") {
        creator = "";
    }
    if (creator != "") {
        key = key + '&creator=' + encodeURI(creator);
    }
    if (from != "" && to == "") {
        alert("请填写提交时间的结束时间");
        return false;
    } else if (from == "" && to != "") {
        alert("请填写提交时间的开始时间");
        return false;
    } else if (from != "" && to != "") {
    	// 查询时间验证：只提供最近半年操作日志查询
    	var dtTmp = new Date();
    	var minTime = new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) - 6, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
    	if( minTime.format("yyyy-MM-dd 00:00:00") > from ){
    		$.omMessageBox.alert({
    			type:'error',
    			title : '操作提示',
    			content : '超出范围，请查询最近半年的操作日志！'
           	});
    		return false;
    	}
        key = key + '&from=' + encodeURI(from);
        key = key + '&to=' + encodeURI(to);
    }
    $('#grid').omGrid("setData", url + key);
}

//多条件查询方法 -- add by wanyong 2014-04-25
function exportBatchOptLog() {
    var creator = jQuery('#creator').val();
    var from = jQuery('#from').val();
    var to = jQuery('#to').val();
    var key = "exportbatchoptlog.do?queryKey=goodsbank.exportbatchoptlog";
    if (creator == "全部") {
        creator = "";
    }
    if (creator != "") {
        key = key + '&creator=' + encodeURI(creator);
    }
    if (from != "" && to == "") {
        alert("请填写提交时间的结束时间");
        return false;
    } else if (from == "" && to != "") {
        alert("请填写提交时间的开始时间");
        return false;
    } else if (from != "" && to != "") {
    	// 查询时间验证：只提供最近半年操作日志查询
    	var dtTmp = new Date();
    	var minTime = new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) - 6, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
    	if( minTime.format("yyyy-MM-dd 00:00:00") > from ){
    		$.omMessageBox.alert({
    			type:'error',
    			title : '操作提示',
    			content : '超出范围，请查询最近半年的操作日志！'
           	});
    		return false;
    	}
        key = key + '&from=' + encodeURI(from);
        key = key + '&to=' + encodeURI(to);
    }
    window.location.href = key;
}

//展示批量详情方法
function showBatchDetail(batchId, auditType, state) {
    $("#dialog").omDialog("option", "width", 850);
    $("#dialog").omDialog("option", "title", "批量配置商品银行审核详情");
    $("#dialog").omDialog("option", "height", 500);
    $("#dialog").omDialog("option", "modal", true);
    $("#dialog").omDialog('open');
    window.frames[0].location.href = '/hfMngBusi/goodsbankaudit/toAuditBatchDetail.do?batchId=' + batchId;
    $("#dialog").omDialog({
        onClose: function(event) {
            window.frames[0].location.href = '../wait.htm';
        }
    });
    return false;
}
