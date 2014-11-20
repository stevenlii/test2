//页面自定义校验方法
$.validator.addMethod("maxByte", function(value , element ,params) {
	return byteCount(value)<=params;
}, $.validator.format("最多能输入{0}个字符"));
//关闭窗口方法
function closeDialog(){
	try{
		window.parent.fillBackAndCloseDialog();
	}catch(e){
		fillBackAndCloseDialog();
	} 
} 
//当前页面弹出操作提示框
function showSimpleTip(res,noAuidt){
	//res 结果为1表示成功，结果为0表示失败
	//noAuidt 为true，表示不需要审核，提示语中不出现"审核"二字
	var msg = "";
	var ty = "";
	if(res !='1') {
		ty = 'error';
  		var msg = '操作失败';
  		if(res != '0'){
  			msg = res;
  		}
   }else{
	   ty = 'success';
	   msg = '操作成功，已提交审核';
	   if(noAuidt){
		   msg = '操作成功';
	   }
	   closeDialog(); 
   }
	$.omMessageBox.alert({
		type : ty,
		title : '操作提示',
		content : msg
	});
}
function showSuccess(){
	window.parent.$.omMessageBox.alert({
		type : 'success',
		title : '操作提示',
		content : '操作成功，已提交审核'
	});
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

function fillBackAndCloseDialog(){
	$("#dialog").omDialog('close');
}
  //删除空格
  function trim(str) {
     if (str == null) {
        return "";
     }
     return str.replace(/^\s*(.*?)[\s\n]*$/g,'$1');   
  }
//刷新页面
  function shuaxin(){
  	jQuery('#grid').omGrid('reload');
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
//start 展示树形结构的商品分类
  function backfill(e, treeId, treeNode) {
		v = treeNode.name;
		$("#category").val(treeNode.id);
		while(true){
			treeNode = treeNode.getParentNode();
	    	if(treeNode == null){
	    		break;
	    	}
	    	v = treeNode.name+"-"+v;
	    }
		$("#goodsCategory").attr("value", v);
		hideMenu();
	}
	function showMenu() {
		var cityObj = $("#goodsCategory");
		var cityOffset = $("#goodsCategory").offset();
		$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

		$("body").bind("mousedown", onBodyDown);
	}
	function refuseBackspace(){
		if(event.keyCode==8)
			event.keyCode=0;
		return false;
	}
	function hideMenu() {
		$("#menuContent").fadeOut("fast");
		$("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		if (!(event.target.id == "goodsCategory" || event.target.id == "menuContent"
			|| $(event.target).parents("#menuContent").length>0 )) {
			hideMenu();
		}
	}
//end 展示树形结构的商品分类