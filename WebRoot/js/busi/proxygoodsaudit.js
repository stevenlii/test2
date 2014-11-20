function showResponse(responseText, statusText) {
	var result = eval("("+responseText+")");
	if(result.code=='0'){
	  	window.parent.fillBackAndCloseDialog(result.msg);
	}
}    
//当前页面弹出操作提示框
function showAuditTip(result){
	if(result !='1') {
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
//刷新页面
function shuaxin(){
	jQuery('#grid').omGrid('reload');
}

//关闭窗口方法
function closeDialog(){
	try{
		window.parent.fillBackAndCloseDialog();
	}catch(e){
		fillBackAndCloseDialog();
	} 
}  

//当前页面弹出操作提示框
function showSimpleTip(result){
	 if(result !='yes') {
		 $.omMessageBox.alert({
             type:'error',
             title:'失败',
             content:'操作失败！'
         });
	 }
}
 //多条件查询方法
 function query(){
      var state = jQuery('#state').val();
      var auditType = jQuery('#auditType').val();
	  var creator = jQuery('#creator').val();
	  var modUser = jQuery('#modUser').val();
	  var from=jQuery('#from').val();
	  var to=jQuery('#to').val();
	  var merId=jQuery('#merId').val();
	  var goodsId=jQuery('#goodsId').val();
	  var subMerId=jQuery('#subMerId').val();
	  var ixData = "";
	  var key = "?queryKey=audit.allProxyGoodsAudits";
	  
	  if(modUser =="全部"){
		  modUser = "";
	  }
	  if(creator =="全部"){
		  creator = "";
	  }
	  if(state =="全部"){
		  state = "";
	  }
	  if(auditType =="全部"){
		  auditType = "";
	  }
	  if(state != ""){ 
		  key = key + '&state=' + encodeURI(state);
	  }
	  if(creator != ""){ 
		  key = key + '&creator=' + encodeURI(creator);
	  }
	  if(modUser != ""){ 
		  key = key + '&modUser=' + encodeURI(modUser);
	  }
	  if(auditType != ""){ 
		  key = key + '&auditType=' + encodeURI(auditType);
	  }
	  if(from != "" && to == ""){
		  alert("请填写提交时间的结束时间");
		  return false;
	  }else if(from == "" && to != ""){
		  alert("请填写提交时间的开始时间");
		  return false;
	  }
	  else if(from != "" && to != ""){ 
		  key = key + '&from=' + encodeURI(from);
		  key = key + '&to=' + encodeURI(to);
	  }
	  if(merId !=""){
		  if(subMerId !=""){
			  if(goodsId !=""){
				  ixData=merId+"-"+subMerId+"-"+goodsId;
			  }
			  else {
				  ixData=merId+"-"+subMerId+"-"+"%";
			  }
		  }
		  if(subMerId ==""){
			  if(goodsId !=""){
				  ixData=merId+"-"+"%"+"-"+goodsId;
			  }
			  else {
				  ixData=merId+"-%-%";
			  }
		  }
	  }
	  if(merId ==""){
		  if(subMerId !=""){
			  if(goodsId !=""){
				  ixData="%-"+subMerId+"-"+goodsId;
			  }
			  else {
				  ixData="%-"+subMerId+"-"+"%";
			  }
		  }
		  if(subMerId ==""){
			  if(goodsId !=""){
				  ixData="%-"+"%"+"-"+goodsId;
			  }
			  else {
				  ixData="%-%-%";
			  }
		  }
	  }
	  if(ixData != ""){
		  key = key + '&ixData=' + encodeURI(ixData);
	  }
	  key = key + '&ixData=' + encodeURI(ixData);
	  $('#grid').omGrid("setData", url+key);
 }    
//展示详情方法
 function show(id, auditType, state){
 	var width = 500;
	var height = 420;
	var title = "";
	if (auditType == 1){
		title = "代理商品新增审核";
	} else if (auditType == 2){
		title = "代理商品修改审核";
		if (state == 0){
			width = 620;
			height = 500;
		}
	} else if (auditType != 0){
		title = "代理商品启用/禁用审核";
	}
	$("#dialog").omDialog("option","width",width);
    $("#dialog").omDialog("option","title",title);
    $("#dialog").omDialog("option","height",height);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='detail.do?id=' + id;
	$("#dialog").omDialog({onClose : function(event) {
  		window.frames[0].location.href='../wait.htm';
	}});
	return false;
}
    
//展示批量详情方法
 function showBatchDetail(batchId, auditType, state){
 	var width = 700;
	var height = 500;
	var title = "";
	if (auditType == 1){
		title = "批量代理商品新增审核";
	} else if (auditType == 2){
		title = "批量代理商品修改审核";
		if (state == 0){
			width = 600;
			height = 500;
		}
	} else if (auditType != 0){
		title = "代理商品启用/禁用审核";
	}
	$("#dialog").omDialog("option","width",width);
    $("#dialog").omDialog("option","title",title);
    $("#dialog").omDialog("option","height",height);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='toAuditBatchDetail.do?batchId='+batchId;
	$("#dialog").omDialog({onClose : function(event) {
  		window.frames[0].location.href='../wait.htm';
	}});
	return false;
}
 
//展示审核后批量详情方法
 function showBatchDetailAudit(batchId, auditType, state){
 	var width = 700;
	var height = 500;
	var title = "";
	if (auditType == 1){
		title = "批量代理商品新增审核";
	} else if (auditType == 2){
		title = "批量代理商品修改审核";
		if (state == 0){
			width = 600;
			height = 500;
		}
	} else if (auditType != 0){
		title = "禁用商品启用/禁用审核";
	}
	$("#dialog").omDialog("option","width",width);
    $("#dialog").omDialog("option","title",title);
    $("#dialog").omDialog("option","height",height);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='toShowBatchDetail.do?batchId='+batchId;
	$("#dialog").omDialog({onClose : function(event) {
  		window.frames[0].location.href='../wait.htm';
	}});
	return false;
}
 
 
function fillBackAndCloseDialog(){
	$("#dialog").omDialog('close');
	$("#dialog-notPass").omDialog("close");
}

//弹出审核不通过对话框
function openNotPass(id){
	$.omMessageBox.prompt({
        title:'审核不通过',
        content:'请输入您的审核意见：',
        onClose:function(value){
            if(value===false){ //按了取消或ESC
                return;
            }
            if(value==''){
                alert('审核意见不能为空');
                return false; //不关闭弹出窗口
            }
            if(byteCount(value)>64){
        		alert("审核意见最大长度为64个字符！")
        		return false;
        	}
    		$.ajax({
    				url : 'auditNotPass.do',
    				type : 'POST',
    				dataType: 'text',
    				data : {
    						'id' : id,
    						'resultDesc' : value
    				},
    				success : function(data){
    					shuaxin();
    					showAuditTip(data);
    				}
    		});
        }
    });
}


//弹出审核不通过对话框
function openNotPassBatch(batchId){
	$.omMessageBox.prompt({
        title:'审核不通过',
        content:'请输入您的审核意见：',
        onClose:function(value){
            if(value===false){ //按了取消或ESC
                return;
            }
            if(value==''){
                alert('审核意见不能为空');
                return false; //不关闭弹出窗口
            }
            if(byteCount(value)>64){
        		alert("审核意见最大长度为64个字符！")
        		return false;
        	}
    		$.ajax({
    				url : 'auditNotPass.do',
    				type : 'POST',
    				dataType: 'text',
    				data : {
    						'batchId' : batchId,
    						'resultDesc' : value
    				},
    				success : function(data){
    					shuaxin();
    					showAuditTip(data);
    				}
    		});
        }
    });
}


//批量校验
function batchVolid(){
	try{
		var selections=$('#grid').omGrid('getSelections',true);     
	}catch(e){
		alert("没有数据！！！");
		return false;
	}
    var length= selections.length;
	if (length == 0) {
        alert('请至少选择一行记录！');
        return false;
    }
    if(selections[0].STATE!=0){
        alert('请选择处于待审核状态的记录操作！');
        return false;
    }
    var check=0;//校验值 默认为0 表示正常
    var ID="";
    for(var j=0;j<length;j++){
    	if(selections[j].STATE != 0){
    		alert("代理商户号-二级商户号为  "+selections[j].MERID+"-"+selections[j].SUBMERID+" 的记录已审核，无法再次进行审核操作！");
    		return false;
    	}
    }
    ID=trim(selections[0].ID);
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
     		check=1;
     		break;
     	}
	    ID=trim(selections[i].ID)+','+ID;
    }
    return ID;
   
}
 
//批量通过
function batchPass(){
	var id = batchVolid();
	if(id){
	    singlePass(id);
	}
}
//批量不通过
function batchNotPass(){
	var id = batchVolid();
	if(id){
		openNotPass(id);
	}
}

//审核不通过
function singleNotPass(){
	var id = $('#id').val();
	var resultDesc = $('#resultDesc').val();
	if(resultDesc == ""){
		alert("请先输入审核意见！");
		return false;
	}
	var len=byteCount(resultDesc);
	if(len>64){
		alert("字符不能多于64个！");
		return false;
	}
	
	$.ajax({
		url : 'auditNotPass.do',
		type : 'POST',
		dataType: 'text',
		data : {
				'id' : id,
				'resultDesc' : resultDesc
		},
		success : function(data){
			$("#dialog-notPass").omDialog("close");  //关闭窗口方法
			jQuery('#grid').omGrid('reload');
			showAuditTip(data);
		}
	});
}

//审核通过方法
function singlePass(id){
	 
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
	            		jQuery('#grid').omGrid('reload');
	            		showAuditTip(data);  
	            	}
            	});
            }
        }
    });
}

//批量审核通过方法
function goPass(batchId){
	$.omMessageBox.confirm({
        title:'审核通过',
        content:'确认审核通过此信息？',
        onClose:function(value){
            if (value){
            	$.ajax({
        			url : 'goPass.do',
        			type : 'POST',
        			dataType: 'text',
        			data : {
        					'batchId' : batchId
        			},
        			success : function(data){
        					showAuditTip(data);  
        			}
        		});
            }
        }
    });
}
//批量审核不通过方法             
function notPass(){
	var id = $('#id').val();
	var resultDesc = $('#resultDesc').val();
	if(resultDesc == ""){
		alert("请先输入审核意见！");
		return false;
	}
	var len=byteCount(resultDesc);
	if(len>64){
		alert("字符不能多于64个！");
		return false;
	}
	
	$.ajax({
		url : 'notPass.do',
		type : 'POST',
		dataType: 'text',
		data : {
				'batchId' : id,
				'resultDesc' : resultDesc
		},
		success : function(data){
			$("#dialog-notPass").omDialog("close");  //关闭窗口方法
			jQuery('#grid').omGrid('reload');
			showAuditTip(data);
		}
	});
}

//删除空格
function trim(str) {
   if (str == null) {
      return "";
   }
   return str.replace(/^\s*(.*?)[\s\n]*$/g,'$1');   
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

//批量审核的多条件查询方法
function batchquery(){
     var state = jQuery('#state').val();
     var auditType = jQuery('#auditType').val();
	  var creator = jQuery('#creator').val();
	  var modUser = jQuery('#modUser').val();
	  var from=jQuery('#from').val();
	  var to=jQuery('#to').val();
	  var merId=jQuery('#merId').val();
	  var goodsId=jQuery('#goodsId').val();
	  var subMerId=jQuery('#subMerId').val();
	  var ixData = "";
	  var key = "?queryKey=audit.allBatchProxyGoodsAudits";
	  
	  if(modUser =="全部"){
		  modUser = "";
	  }
	  if(creator =="全部"){
		  creator = "";
	  }
	  if(state =="全部"){
		  state = "";
	  }
	  if(auditType =="全部"){
		  auditType = "";
	  }
	  if(state != ""){ 
		  key = key + '&state=' + encodeURI(state);
	  }
	  if(creator != ""){ 
		  key = key + '&creator=' + encodeURI(creator);
	  }
	  if(modUser != ""){ 
		  key = key + '&modUser=' + encodeURI(modUser);
	  }
	  if(auditType != ""){ 
		  key = key + '&auditType=' + encodeURI(auditType);
	  }
	  if(from != "" && to == ""){
		  alert("请填写提交时间的结束时间");
		  return false;
	  }else if(from == "" && to != ""){
		  alert("请填写提交时间的开始时间");
		  return false;
	  }
	  else if(from != "" && to != ""){ 
		  key = key + '&from=' + encodeURI(from);
		  key = key + '&to=' + encodeURI(to);
	  }
	  if(merId !=""){
		  if(subMerId !=""){
			  if(goodsId !=""){
				  ixData=merId+"-"+subMerId+"-"+goodsId;
			  }
			  else {
				  ixData=merId+"-"+subMerId+"-"+"%";
			  }
		  }
		  if(subMerId ==""){
			  if(goodsId !=""){
				  ixData=merId+"-"+"%"+"-"+goodsId;
			  }
			  else {
				  ixData=merId+"-%-%";
			  }
		  }
	  }
	  if(merId ==""){
		  if(subMerId !=""){
			  if(goodsId !=""){
				  ixData="%-"+subMerId+"-"+goodsId;
			  }
			  else {
				  ixData="%-"+subMerId+"-"+"%";
			  }
		  }
		  if(subMerId ==""){
			  if(goodsId !=""){
				  ixData="%-"+"%"+"-"+goodsId;
			  }
			  else {
				  ixData="%-%-%";
			  }
		  }
	  }
	  if(ixData != ""){
		  key = key + '&ixData=' + encodeURI(ixData);
	  }
	  key = key + '&ixData=' + encodeURI(ixData);
	  $('#grid').omGrid("setData", url+key);
} 