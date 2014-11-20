//刷新页面
function shuaxin(){
	jQuery('#grid').omGrid('reload');
}
//当前页面弹出操作提示框
function showSimpleTip(result){
	if(result !='yes') {
		var msg = '操作失败';
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
function showSuccess(){
	window.parent.$.omMessageBox.alert({
		type : 'success',
		title : '操作提示',
		content : '操作成功，已提交审核'
	});
}
//关闭窗口方法
function closeDialog(){
	try{
		window.parent.fillBackAndCloseDialog();
	}catch(e){
		fillBackAndCloseDialog();
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
//查询方法
function queryGoodsBank(){
	var merId=jQuery('#merId').val();
	var goodsId=jQuery('#goodsId').val();
	var kstate=jQuery('#kState').val();
	var key="";
    if(merId != ""){ //没要有查询条件，要显示全部数据
    	key=key+'&merId='+encodeURI(merId);
    }
    if(goodsId !=""){ 
    	key=key+'&goodsId='+encodeURI(goodsId);
    }
    if(kstate !="" && kstate != "全部"){ 
    	key=key+'&kstate='+encodeURI(kstate);
    }
    $('#grid').omGrid("setData", url+key);
}

//多条件查询方法
function queryMerBank(){
	var merId = jQuery('#merId').val();
	var key = "";
	if(merId != ""){ //没有查询条件，要显示全部数据
        key = key + '&merId=' + encodeURI(merId);
	}
	$('#grid').omGrid("setData", url+key);
}