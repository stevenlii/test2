//批量查询方法
function queryBatchSync(){
	var synState=jQuery('#synState').val();
	var synResult=jQuery('#synResult').val();
	var key="";
	if(synState !=""){
		key=key+'&synState='+encodeURI(synState);
	}
	if(synResult !=""){
		key=key+'&synResult='+encodeURI(synResult);
	}
	$('#grid').omGrid("setData", url+key);
}

//显示详情方法
function showDetail(taskId){
	$("#dialog").omDialog("option","width",'450');
	$("#dialog").omDialog("option","title",'批量任务信息');
	$("#dialog").omDialog("option","height",'400');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='detail.do?taskId='+taskId;
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
	return false;
}
//导入号段文件
function add(){
	$("#dialog").omDialog("option","width",'500');
	$("#dialog").omDialog("option","title",'导入号段文件');
	$("#dialog").omDialog("option","height",'300');
	$("#dialog").omDialog("option", "modal", true);
	$("#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='add.do';
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
	return false;
}

function downloadDoc(taskId){
	window.location.href="downloadDoc.do?fileName="+taskId+".txt";
}
function downloadSuccDoc(taskId){
	window.location.href="downloadDoc.do?fileName="+taskId+"_succ.txt";
}
function downloadFailDoc(taskId){
	window.location.href="downloadDoc.do?fileName="+taskId+"_fail.txt";
}