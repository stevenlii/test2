//当前页面弹出操作提示框
function showSimpleTip(result){
//result 结果为yes表示成功，结果为no表示失败
	if(result == 'yes'){
		jQuery('#grid').omGrid('reload');
	}else if(result =='no'){
		 $.omMessageBox.alert({
             type:'error',
             title:'失败',
             content:'操作失败，请稍后再试。'
         });
	}else{
		$.omMessageBox.alert({
            type:'error',
            title:'失败',
            content:result
        });
	}
}
//查询方法
 function query(flag){
	 var merName=jQuery('#merName').val();
	 var merId=jQuery('#merId').val();
	 var operState=jQuery('#operState').val();
	 var operator=jQuery('#operator').omCombo('value');
	 var total=jQuery('#total').val();
	 var key='';
	 if(flag == 1){
		 var month=jQuery('#month').val();
		 if(month !=""){ 
		      key=key+'&month='+encodeURI(month);
	     }
	 }
	 if(merId != ""){ //没要有查询条件，要显示全部数据
	     key=key+'&merId='+encodeURI(merId);
	 }
	 if(merName !=""){ 
	      key=key+'&merName='+encodeURI(merName);
     }
	 if(operator!="" && operator != "全部"){ 
		 key=key+'&operator='+encodeURI(operator);
	 }
	 if(operState!="" && operState != "全部"){ 
		 key=key+'&operState='+encodeURI(operState);
	 }
	 //总分范围  low:最低分    high:最高分
	 var low = "";
	 var high = "";
	 if(total == 5){
		 low = 90;
	 }else if(total == 4){
		 low = 80;
		 high = 90;
	 }else if(total == 3){
		 low = 70;
		 high = 80;
	 }else if(total == 2){
		 low = 60;
		 high = 70;
	 }else if(total == 1){
		 high = 60;
	 }
	 if(low!=""){
		 key=key+'&low='+encodeURI(low);
	 }
	 if(high!=""){
		 key=key+'&high='+encodeURI(high);
	 }
	 $('#grid').omGrid("setData", url+key);
 }

//导出上月评分列表
function expLastMonth(){
	var key = "exportMerGrade.do?queryKey=mergrade.lastMonthGrade";
	exportMerGrade(key);
}
//导出历史评分列表
function expHistory(){
	var key = "exportMerGrade.do?queryKey=mergrade.historyGrade";
	var month=jQuery('#month').val();
	if(month !=""){ 
		key=key+'&month='+encodeURI(month);
    }
	exportMerGrade(key);
}
//导出方法
function exportMerGrade(key){
	var merName=jQuery('#merName').val();
	var merId=jQuery('#merId').val();
	var operState=jQuery('#operState').val();
	var operator=jQuery('#operator').omCombo('value');
	var total=jQuery('#total').val();
    if(merName != ""){ //没有查询条件，要显示全部数据
    	key = key + '&merName=' + encodeURI(merName);
    }
    if(merId != ""){ 
    	key = key + '&merId=' + encodeURI(merId);
    }
    if(operState != ""){ 
    	key = key + '&operState=' + encodeURI(operState);
    }
    if(operator != "" && operator != "全部"){ 
   		key = key + '&operator=' + encodeURI(operator);
    }
  //总分范围  low:最低分    high:最高分
	 var low = "";
	 var high = "";
	 if(total == 5){
		 low = 90;
	 }else if(total == 4){
		 low = 80;
		 high = 90;
	 }else if(total == 3){
		 low = 70;
		 high = 80;
	 }else if(total == 2){
		 low = 60;
		 high = 70;
	 }else if(total == 1){
		 high = 60;
	 }
	 if(low!=""){
		 key=key+'&low='+encodeURI(low);
	 }
	 if(high!=""){
		 key=key+'&high='+encodeURI(high);
	 }
    window.location.href=key;
}
//打开详情页面
function showDetail(merId,month){
	$("#dialog").omDialog("option","width","500");
    $("#dialog").omDialog("option","title","评分详情");
    $("#dialog").omDialog("option","height","550");
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='detail.do?merId=' + merId + '&month=' + month;
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}
//打开修改页面
function modify(merId,month){
	$("#dialog").omDialog("option","width","500");
    $("#dialog").omDialog("option","title","修改评分");
    $("#dialog").omDialog("option","height","550");
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='modify.do?merId=' + merId + '&month=' + month;
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}
//打开导入手工评分对话框
function openUpload(){
	var file = document.getElementById("file");
	file.outerHTML=file.outerHTML.replace(/(value=\").+\"/i,"$1\"");  //初始化file控件为空值
	$("#dialog-upload").omDialog("open");
}
//打开修改记录页面
function openModHistory(merId,month){
	$( "#dialog").omDialog("option","width",'650');
    $( "#dialog").omDialog("option","title",'修改记录');
    $( "#dialog").omDialog("option","height",'400');
    $( "#dialog").omDialog("option", "modal", true);
	$( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='openModHistory.do?merId='+merId+'&month='+month;
	$("#dialog").omDialog({onClose : function(event) {
 		window.frames[0].location.href='../wait.htm';
	}});
}

//关闭窗口方法
function closeDialog(){
	try{
		window.parent.fillBackAndCloseDialog();
	}catch(e){
		fillBackAndCloseDialog();
	} 
}	
//iframe窗口调用的关闭操作
function fillBackAndCloseDialog(){
	$("#dialog").omDialog("close");
	$("#dialog-upload").omDialog("close");
}
function shuaxin(){
	jQuery('#grid').omGrid('reload');
}


function queryAudit(){
  var merName = jQuery('#merName').val();
  var ixData = jQuery('#ixData').val();
  var state = jQuery('#state').omCombo('value');
  var operState = jQuery('#operState').omCombo('value');
  var modUser = jQuery('#modUser').omCombo('value');
  var operator = jQuery('#operator').omCombo('value');
  var key = "";
  if(merName != ""){ //没有查询条件，要显示全部数据
	  key = key + '&merName=' + encodeURI(merName);
  }
  if(ixData != ""){
	  key = key + '&ixData=' + encodeURI(ixData);
  }
   if(state != "" && state != "全部"){ 
    	key = key + '&state=' + encodeURI(state);
   }
   if(operState != "" && operState != "全部"){ 
        key = key + '&operState=' + encodeURI(operState);
   }
   if(modUser != "" && modUser != "全部"){ 
   		key = key + '&modUser=' + encodeURI(modUser);
   }
   if(operator != "" && operator != "全部"){ 
   		key = key + '&operator=' + encodeURI(operator);
   }
   $('#grid').omGrid("setData", url+key);
}

function showAuditDetail(id,state){
	$("#dialog").omDialog("option","width","500");
    $("#dialog").omDialog("option","title","评分修改详情");
    $("#dialog").omDialog("option","height","550");
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='auditDetail.do?id=' + id;
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}

//查询页面审核通过方法
function goPass(id){
	$.omMessageBox.confirm({
        title:'审核通过',
        content:'确认审核通过此信息？',
        onClose:function(value){
            if (value){
            	$.ajax({
            		url : 'auditPass.do',
            		type : 'POST',
            		dataType: 'text',
            		data : {
            			'id' : id
            	},
            	success : function(data){
    				if(data == "yes"){
    					shuaxin();
    				}
    				showSimpleTip(data);
            	}
            	});
            }
        }
    });
}
//详情页面审核通过方法
function goPass1(id){
	var resultDesc = document.getElementById("resultDesc").value;
	if(byteCount(resultDesc)>64){
		alert("审核意见最大长度为64个字符！")
		flag = false;
	}
	if(confirm("确定审核通过此信息？")){
		$.ajax({
			url : 'auditPass.do',
			type : 'POST',
			dataType: 'text',
			data : {
					'id' : id,
					'resultDesc' : resultDesc
			},
			success : function(data){
				if(data == "yes"){
					closeDialog();
					window.parent.shuaxin();
				}
				window.parent.showSimpleTip(data);
			}
		});
	}
}

//查询页面审核不通过
function notPass(id){
	$.omMessageBox.confirm({
        title:'审核不通过',
        content:'确认审核不通过此信息？',
        onClose:function(value){
            if(value){ 
            	$.ajax({
            		url : 'auditNotPass.do',
            		type : 'POST',
            		dataType: 'text',
            		data : {
	            		'id' : id
	            	},
	            	success : function(data){
	            		shuaxin();
	            		showSimpleTip(data);
	            	}
            	});
            }
        }
    });
}
//详情页面审核不通过
function goNotPass(id){
	var flag = true;
	var resultDesc = document.getElementById("resultDesc").value;
	if(byteCount(resultDesc)>64){
		alert("审核意见最大长度为64个字符！")
		flag = false;
	}
	flag=confirm("确定审核不通过此信息？");
	if(flag){
		$.ajax({
			url : 'auditNotPass.do',
			type : 'POST',
			dataType: 'text',
			data : {
					'id' : id,
					'resultDesc' : resultDesc
			},
			success : function(data){
				closeDialog();
				window.parent.shuaxin();
				window.parent.showSimpleTip(data);
			}
		});
	}
}
//交易数据入库
function loadTradeGrade(){
	$.omMessageBox.confirm({
        title:'交易数据入库',
        content:'确认此操作吗？',
        onClose:function(value){
            if (value){
            	$.ajax({
            		url : 'loadTradeGrade.do',
            		type : 'POST',
            		dataType: 'text',
	            	success : function(data){
            			if(data=='yes')
            				alert("操作完成");
	            		showSimpleTip(data);  
	            	}
            	});
            }
        }
    });
}
//核减数据入库
function loadReduceDataGrade(){
	$.omMessageBox.confirm({
        title:'核减数据入库',
        content:'确认此操作吗？',
        onClose:function(value){
            if (value){
            	$.ajax({
            		url : 'loadReduceDataGrade.do',
            		type : 'POST',
            		dataType: 'text',
	            	success : function(data){
	            		showSimpleTip(data);  
	            	}
            	});
            }
        }
    });
}
//手工计算评分
function calculateGrade(){
	$.omMessageBox.confirm({
        title:'手工计算评分',
        content:'确认此操作吗？',
        onClose:function(value){
            if (value){
            	$.ajax({
            		url : 'calculateGrade.do',
            		type : 'POST',
            		dataType: 'text',
	            	success : function(data){
	            		showSimpleTip(data);  
	            	}
            	});
            }
        }
    });
}
//获取字节长度
function byteCount(s){
	var c = 0;
	var a = s.split("");
	for (var i=0;i<a.length;i++) {
		c=(a[i].charCodeAt(0)<299)?(c+1):(c+2);
	}
	return c;
}

function queryDownload(){
	var docName = jQuery('#docName').val();
	var key = "";
	if(docName != ""){ 
   		key = key + '&docName=' + encodeURI(docName);
	}
	$('#grid').omGrid("setData", url+key);
}
function downloadDoc(docName){
	window.location.href="downloadDoc.do?docName="+docName;
}
