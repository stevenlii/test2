//start 展示树形结构的业务属性
function beforeClick2(treeId, treeNode) {
	var check = (treeNode && !treeNode.isParent);
	return check;
}
function onClick2(e, treeId, treeNode) {
	v = treeNode.name;
	$("#ixData2").val(treeNode.id);
	$("#merBusiType").attr("value", v);
}

function showMenu2() {
	var cityObj = $("#merBusiType");
	var cityOffset = $("#merBusiType").offset();
	$("#menuContent2").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

	$("body").bind("mousedown", onBodyDown2);
}
function hideMenu2() {
	$("#menuContent2").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown2);
}
function onBodyDown2(event) {
	if (!(event.target.id == "merBusiType" || event.target.id == "menuContent2"
		|| $(event.target).parents("#menuContent2").length>0 )) {
		hideMenu2();
	}
}
//end 展示树形结构的业务属性
function refuseBackspace(){
	if(event.keyCode==8)
		event.keyCode=0;
	return false;
}
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
	if(result !='yes'){
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
     
  var ixData = jQuery('#ixData').val();
  var state = jQuery('#state').omCombo('value');
  var creator = jQuery('#creator').omCombo('value');
  var modUser = jQuery('#modUser').omCombo('value');
  var auditType = jQuery('#auditType').omCombo('value');
  var ixData2 = jQuery('#ixData2').val();

  var key = "?queryKey=audit.allMerAudits";
  if(ixData != ""){ //没有查询条件，要显示全部数据
   
	  key = key + '&ixData=' + encodeURI(ixData);
  }
   if(state != ""){ 
    	key = key + '&state=' + encodeURI(state);
   }
   if(creator != "" && creator != "全部"){ 
        key = key + '&creator=' + encodeURI(creator);
   }
   if(modUser != "" && modUser != "全部"){ 
   		key = key + '&modUser=' + encodeURI(modUser);
   }
   if(auditType != ""){ 
   		key = key + '&auditType=' + encodeURI(auditType);
   }
   if(ixData2 != ""){ 
   		key = key + '&ixData2=' + encodeURI(ixData2);
   }
   $('#grid').omGrid("setData", url+key);
}    
 
 
//展示详情方法
function show(id, auditType, state){
	var width = 600;
	var height = 480;
	var title = "";
	if (auditType == 1){
		title = "商户新增审核";
	} else if (auditType == 2){
		title = "商户修改审核";
		if (state == 0){
			width = 638;
			height = 480;
		}
	} else if (auditType != 0){
		title = "商户启用/禁用审核";
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
//商户审核不通过
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
			url : 'merAuditNotPass.do',
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