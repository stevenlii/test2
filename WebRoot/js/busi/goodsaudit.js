function showResponse(responseText, statusText) {
	var result = eval("("+responseText+")");
	if(result.code=='0'){
	  	window.parent.fillBackAndCloseDialog(result.msg);
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
//result 结果为yes表示成功，结果为no表示失败
	 if(result !='yes') {
		 var msg = '操作失败，请稍后再试。';
		 if(result != 'no'){
			 msg = result;
		 }
		 $.omMessageBox.alert({
			 type:'error',
			 title : '操作提示',
			 content : msg
		 });
	 }
}
 //多条件查询方法
 function query(){
     
      var state = jQuery('#state').val();
	  var creator = jQuery('#creator').val();
	  var modUser = jQuery('#modUser').val();
	  var auditType = jQuery('#auditType').val();
	  var merId = jQuery('#merId').val();
	  var goodsId = jQuery('#goodsId').val();
	  
	  var ixData = "";
	  var key = "?queryKey=audit.allGoodsAudits";
	  
	  if(modUser =="全部"){
		  modUser = "";
	  }
	  if(creator =="全部"){
		  creator = "";
	  }
	  
	  if(merId != ""){
		  if(goodsId != ""){
			  ixData = merId + "-" + goodsId;
		  }
		  else {
			  ixData = merId + "-";
		  }
	  }else if(goodsId != ""){
		  ixData = "-" + goodsId;
	  }
	  
	  if(ixData != ""){ //没要有查询条件，要显示全部数据
		  key = key + '&ixData=' + encodeURI(ixData);
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
	
	  $('#grid').omGrid("setData", url+key);
 }    
//展示详情方法
 function show(id, auditType, state, servType){
 	var width = 450;
	var height = 530;
	if(servType == 2){
		height = 480;
	}
	var title = "";
	if (auditType == 1){
		title = "商品新增审核";
	} else if (auditType == 2){
		title = "商品修改审核";
		if (state == 0){
			width = 600;
			height = 620;
			if(servType == 2){
				height = 570;
			}
		}
	} else if (auditType != 0){
		title = "商品启用/禁用审核";
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
    

 
function fillBackAndCloseDialog(){
	$("#dialog").omDialog('close');
	$("#dialog-notPass").omDialog("close");
}
//商品审核不通过
function goNotPass(id){
	var flag = true;
	var resultDesc = document.getElementById("resultDesc").value;
	if(resultDesc == ""){
		alert("请先输入审核意见！");
		flag = false;
	}
	if(byteCount(resultDesc)>64){
		alert("审核意见最大长度为64个字符！")
		flag = false;
	}
	if(flag){
		$.ajax({
				url : 'goodsAuditNotPass.do',
				type : 'POST',
				dataType: 'text',
				data : {
						'id' : id,
						'resultDesc' : resultDesc
				},
				success : function(data){
					closeDialog();
					try{
						window.parent.shuaxin();
						window.parent.showSimpleTip(data);
					}catch(e){
						shuaxin();
						showSimpleTip(data);
					}
				}
		});
	}
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