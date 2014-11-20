//start 展示树形结构的运营负责人
var setting = {
	check: {enable: true,chkboxType: {"Y":"", "N":""}},
	data: {simpleData: {enable: true}},
	callback: {beforeClick: beforeClick,onCheck: onCheck}
};
function beforeClick(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}
function onCheck(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getCheckedNodes(true),
	v = "";
	var id = "";
	if(nodes.length != 0){
		v=nodes[0].name;
		id=nodes[0].id;
	}
	for (var i=1; i<nodes.length; i++) {
		v += "," + nodes[i].name;
		id += "," + nodes[i].id;
	}
	$("#showOperator").attr("value", v);
	$("#operator").val(id);
}

function showMenu() {
	var cityObj = $("#showOperator");
	var cityOffset = cityObj.offset();
	$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
	$("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "showOperator"
		|| event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
}
// end 展示树形结构的运营负责人
// start 展示树形结构的业务属性
function beforeClick2(treeId, treeNode) {
	var check = (treeNode && !treeNode.isParent);
	return check;
}
function onClick2(e, treeId, treeNode) {
	v = treeNode.name;
	$("#busiType").val(treeNode.id);
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
// end 展示树形结构的业务属性
function refuseBackspace(){
	if(event.keyCode==8)
		event.keyCode=0;
	return false;
}

// 当前页面弹出操作提示框
function showSimpleTip(result){
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

// 查询方法
function query(){
	var merId=jQuery('#merId').val();
	var merName=jQuery('#merName').val();
	var interType=jQuery('#interType').val();
	var operator=jQuery('#operator').omCombo('value');
	var category=$('#category').omCombo('value') ;
	var chnlCheck=jQuery('#chnlCheck').val();
	var addWay=jQuery('#addWay').val();
	var state=jQuery('#state').val();
	var busiType=jQuery('#busiType').val();  
	var key="";
	if(merId != ""){ // 没要有查询条件，要显示全部数据
		key=key+'&merId='+encodeURI(merId);
	}
	if(merName !=""){ 
		key=key+'&merName='+encodeURI(merName);
	}
	if(category !="" && category != "全部"){
		key=key + '&category='+encodeURI(category);
	}
	if(interType!=""){ 
		key=key+'&interType='+encodeURI(interType);
	}
	if(addWay!=""){ 
		key=key+'&addWay='+encodeURI(addWay);
	}
	if(chnlCheck!=""){ 
		key=key+'&chnlCheck='+encodeURI(chnlCheck);
	}
	if(operator!="" && operator != "全部"){ 
		key=key+'&operator='+encodeURI(operator);
	}
	if(state!=""){ 
		key=key+'&state='+encodeURI(state);
	}
	if(busiType!=""){ 
		key=key+'&busiType='+encodeURI(busiType);
	}
	$('#grid').omGrid("setData", url+key);
}


// 增加方法
function show(){
	$( "#dialog").omDialog("option","width",'750');
	$( "#dialog").omDialog("option","title",'添加商户信息');
	$( "#dialog").omDialog("option","height",'480');
	$( "#dialog").omDialog("option", "modal", true);
	$( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='add.do';
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
}
// 修改方法
function showModifyDialog(id){
	$( "#dialog").omDialog("option","width",'750');
	$( "#dialog").omDialog("option","title",'修改商户信息');
	$( "#dialog").omDialog("option","height",'480');
	$( "#dialog").omDialog("option", "modal", true);
	$( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='modify.do?id='+id;
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
}

// iframe窗口调用的关闭操作，返回值是dialog出口的id
function fillBackAndCloseDialog(msg){
	$(msg).omDialog('close');
}
function closeDialog(msg){
	$("#"+msg).omDialog('close');
}

function modify(state){
	showModifyDialog(state);// 显示dialog
}

// 显示详情方法
function shows(id){
	$( "#dialog").omDialog("option","width",'600');
	$( "#dialog").omDialog("option","title",'查看商户信息');
	$( "#dialog").omDialog("option","height",'480');
	$( "#dialog").omDialog("option", "modal", true);
	$( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='detail.do?id='+id ;
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
}

// 导出方法
function expMer(){
	var merId=jQuery('#merId').val();
	var merName=jQuery('#merName').val();
	var busiType=jQuery("#busiType").val();
	var category=jQuery("#category").val(); 
	var interType=jQuery("#interType").val();
	var chnlCheck=jQuery("#chnlCheck").val();
	var operator=jQuery("#operator").val();
	var addWay=jQuery("#addWay").val();
	var state=jQuery("#state").val();
	var key="export.do?queryKey=mers.allMers";
	if(merId != ""){ // 没要有查询条件，要显示全部数据
		key=key+'&merId='+encodeURI(merId);
	}
	if(merName !=""){ 
		key=key+'&merName='+encodeURI(merName);
	}
	if(category !="" && category != "全部"){
		key=key + '&category='+encodeURI(category);
	}
	if(interType!=""){ 
		key=key+'&interType='+encodeURI(interType);
	}
	if(addWay!=""){ 
		key=key+'&addWay='+encodeURI(addWay);
	}
	if(chnlCheck!=""){ 
		key=key+'&chnlCheck='+encodeURI(chnlCheck);
	}
	if(operator!="" && operator != "全部"){ 
		key=key+'&operator='+encodeURI(operator);
	}
	if(state!=""){ 
		key=key+'&state='+encodeURI(state);
	}
	if(busiType!=""){ 
		key=key+'&busiType='+encodeURI(busiType);
	}
	window.location.href=key;
}

// 批量启用方法
function batchstart(){
	try{
		var selections=$('#grid').omGrid('getSelections',true); 
	}catch( e){
		alert("没有数据！！！");
		return false;
	}
	if (selections.length == 0) {
		alert('请至少选择一行记录！');
		return false;
	}
	if(selections[0].STATE!=4){
		alert('请选择禁用状态的记录进行启用操作！');
		return false;
	}
	if(selections[0].MODLOCK=='1'){
		alert("当前记录处于锁定状态，无法进行该操作！");
		return false;
	}
	var check=0;// 校验值 默认为0 表示正常
	var length= selections.length;
	var  ID="";
	for(i=0;i<length;i++){
		ID=trim(selections[i].MERID)+','+ID;
		if (selections[i].STATE!=selections[0].STATE)
			check=1;
	}
	if(check==1){ 
		alert("请选择相同的状态类型进行操作");
		return false;
	} 
	else{
		if(confirm("确认启用这些记录？")){
			$.ajax({
				url : 'enable.do',
				type : 'POST',
				dataType: 'text',
				data : {
				'ID' : ID
			},
			success : function(data){
				showSimpleTip(data);  
				jQuery('#grid').omGrid('reload');
			}
			});
		}
	}
	
}

// 批量禁用方法
function batchstop(){
	try{
		var selections=$('#grid').omGrid('getSelections',true); 
	}catch(e){
		alert("没有数据！！！");
		return false;
	}
	if (selections.length == 0) {
		alert('请至少选择一行记录');
		return false;
	}
	if(selections[0].STATE!=2){
		alert('请选择启用状态的记录进行禁用操作！');
		return false;
	}
	if(selections[0].MODLOCK=='1'){
		alert("当前记录处于锁定状态，无法进行该操作！");
		return false;
	}
	var check=0;// 校验值 默认为0 表示正常
	var length= selections.length;
	var  ID="";
	for(i=0;i<length;i++){
		ID=trim(selections[i].MERID)+','+ID;
		if (selections[i].STATE!=selections[0].STATE)
			check=1;
	}
	if(check==1){ 
		alert("请选择相同的状态类型进行操作");
		return false;
	} 
	else{
		if(confirm("您确定要禁用商户？禁用后商户对账文件只出示到当日凌晨02:00之前。如有对账文件的使用需求，请先关闭商户银行，24小时后再禁用商户。")){
			$.ajax({
				url : 'disable.do',
				type : 'POST',
				dataType: 'text',
				data : {
				'ID' : ID
			},
			success : function(data){
				showSimpleTip(data);  
				jQuery('#grid').omGrid('reload');
			}
			});     
		}
	}
}

// 单条启用
function singleEnable(merId,modLock){  
	var flag = confirm("确认启用该条记录？");
	if (flag){
		var ID=merId+",";
		if(modLock=='1'){
			alert("当前记录处于锁定状态，无法进行该操作！");
			return false;
		}
		else {
			$.ajax({
				url : 'enable.do',
				type : 'POST',
				dataType: 'text',
				data : {
				'ID' : ID
			},
			success : function(data){
				showSimpleTip(data);  
				jQuery('#grid').omGrid('reload');
			}
			});
			// jQuery('#grid').omGrid('reload',1);
		}
	}
}

// 单条禁用
function singleDisable(merId,modLock){
	var flag = confirm("您确定要禁用商户？禁用后商户对账文件只出示到当日凌晨02:00之前。如有对账文件的使用需求，请先关闭商户银行，24小时后再禁用商户。");
	if (flag){
		var ID=merId+",";
		if(modLock=='1'){
			alert("当前记录处于锁定状态，无法进行该操作！");
			return false;
		}
		else{
			$.ajax({
				url : 'disable.do',
				type : 'POST',
				dataType: 'text',
				data : {
				'ID' : ID
			},
			success : function(data){
				showSimpleTip(data);  
				jQuery('#grid').omGrid('reload');
			}
			});
		}
	}
}

// 删除空格
function trim(str) {
	if (str == null){
		return "";
	}
	return str.replace(/^\s*(.*?)[\s\n]*$/g,'$1');   
}

// 检查当前商户号是否已经存在
function checkMerId(merId){
	var res;
	$.ajax({
		url : 'checkMerId.do',
		type : 'POST',
		dataType: 'text',
		async:false, // 同步提交
		data : {
		'merId' : merId
	},
	success : function(data){
		res = data;
	}
	});  
	if(res == 1){
		return true;
	}else {
		return false;
	}
}

// //新增时检查当前商户下的商户名称是否已经存在
// function checkMerName(merName){
// var merId = jQuery('#merId').val();;
// var res;
// $.ajax({
// url : 'checkMerName.do',
// type : 'POST',
// dataType: 'text',
// async:false, //同步提交
// data : {
// 'merId' : merId,
// 'merName' : merName
// },
// success : function(data){
// res = data;
// }
// });
// if(res == 1){
// return true;
// }else {
// return false;
// }
// }

// 刷新页面
function shuaxin(){
	jQuery('#grid').omGrid('reload');
}
	      