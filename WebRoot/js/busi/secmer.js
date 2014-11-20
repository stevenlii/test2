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
//end 展示树形结构的运营负责人
//多条件查询方法
function query(){
     
  var subMerId = jQuery('#subMerId').val();
  var subMerName = jQuery('#subMerName').val();
  var state = jQuery('#state').val();
  var operator = jQuery('#operator').val();

  var key = "";
  if(subMerId != ""){ //没有查询条件，要显示全部数据
	  key = key + '&subMerId=' + encodeURI(subMerId);
  }
   if(state != ""){ 
    	key = key + '&state=' + encodeURI(state);
   }
   if(subMerName != ""){ 
        key = key + '&subMerName=' + encodeURI(subMerName);
   }
   if(operator != "" && operator != "全部"){ 
   		key = key + '&operator=' + encodeURI(operator);
   }
   $('#grid').omGrid("setData", url+key);
   return false;
} 
//单条禁用
function singleDisable(subMerId,modLock){
	flag=confirm("确定禁用此商户？");
	if(flag){
		if(modLock == '1'){
			alert("当前记录处于锁定状态，无法进行该操作！");
			return false;
		}
		$.ajax({
			url : 'disable.do',
			type : 'POST',
			dataType: 'text',
			data : {
				'subMerId' : subMerId
			},
			success : function(data){
				showSimpleTip(data);
				jQuery('#grid').omGrid('reload');
			}
		});
	}
}
//单条启用
function singleEnable(subMerId,modLock){
	flag=confirm("确定启用此商户？");
	if(flag){
		if(modLock == '1'){
			alert("当前记录处于锁定状态，无法进行该操作！");
			return false;
		}
		$.ajax({
			url : 'enable.do',
			type : 'POST',
			dataType: 'text',
			data : {
				'subMerId' : subMerId
			},
			success : function(data){
				showSimpleTip(data);
				jQuery('#grid').omGrid('reload');
			}
		});
	}
}
//批量禁用
function batchDisable(){
	try{
		var selections=$('#grid').omGrid('getSelections',true);     
	}catch(e){
		alert("没有数据！！！");
		return false;
	}
	if (selections.length == 0) {
        alert('请至少选择一行记录！');
        return false;
    }
    if(selections[0].STATE!=2){
        alert('商户'+selections[0].SUBMERID+'已处于禁用状态，不可再次禁用');
        return false;
    }
    var length = selections.length;
    for(var j=0;j<length;j++){
    	if(selections[j].MODLOCK == 1){
    		alert("商户["+selections[j].SUBMERID+"]已被锁定，无法进行操作！");
    		return false;
    	}
    }
    var subMerId=selections[0].SUBMERID;
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
	    	alert("请选择相同的状态类型进行操作！");
	        return false;
     	}
	    subMerId=subMerId+","+selections[i].SUBMERID;
    }
    $.ajax({
    	url : 'disable.do',
    	type : 'POST',
    	dataType: 'text',
    	data : {
    		'subMerId' : subMerId
	    },
	    success : function(data){
	    	showSimpleTip(data);  
	    	jQuery('#grid').omGrid('reload');
	    }
    });
}
//批量启用
function batchEnable(){
	try{
		var selections=$('#grid').omGrid('getSelections',true);     
	}catch(e){
		alert("没有数据！！！");
		return false;
	}
	if (selections.length == 0) {
        alert('请至少选择一行记录！');
        return false;
    }
    if(selections[0].STATE!=4){
    	alert('商户'+selections[0].SUBMERID+'已处于启用状态，不可再次启用');
        return false;
    }
    var length = selections.length;
    for(var j=0;j<length;j++){
    	if(selections[j].MODLOCK == 1){
    		alert("商户["+selections[j].SUBMERID+"]已被锁定，无法进行操作！");
    		return false;
    	}
    }
    var subMerId=selections[0].SUBMERID;
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
	    	alert("请选择相同的状态类型进行操作！");
	        return false;
     	}
	    subMerId=subMerId+","+selections[i].SUBMERID;
    }
    $.ajax({
    	url : 'enable.do',
    	type : 'POST',
    	dataType: 'text',
    	data : {
	    	'subMerId' : subMerId
	    },
	    success : function(data){
	    	showSimpleTip(data);  
	    	jQuery('#grid').omGrid('reload');
	    }
    });
}

//跳转增加页面
function add(){
	$("#dialog").omDialog("option","width",500);
    $("#dialog").omDialog("option","title",'新增二级商户');
    $("#dialog").omDialog("option","height",250);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='add.do';
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}

//跳转到修改页面
function modify(subMerId){
	$("#dialog").omDialog("option","width",500);
    $("#dialog").omDialog("option","title",'修改二级商户');
    $("#dialog").omDialog("option","height",250);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='modifySecMerInf.do?subMerId='+subMerId;
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}

//跳转到配置页面
function confSubMer(subMerId){
	$("#dialog").omDialog("option","width",500);
    $("#dialog").omDialog("option","title",'配置二级商户');
    $("#dialog").omDialog("option","height",280);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='confSecMerInf.do?subMerId='+subMerId;
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}

//导出方法
function expSecMer(){
	var subMerId=jQuery('#subMerId').val();
    var subMerName=jQuery('#subMerName').val();
    var operator=jQuery('#operator').omCombo('value');
    var state=jQuery('#state').val();
	var key="export.do?queryKey=secmerinf.exportInfWithCnf";
    if(subMerId != ""){ //没要有查询条件，要显示全部数据
        key=key+'&subMerId='+encodeURI(subMerId);
    }
    if(subMerName !=""){ 
        key=key+'&subMerName='+encodeURI(subMerName);
    }
    if(operator!="" && operator != "全部"){ 
        key=key+'&operator='+encodeURI(operator);
    }
    if(state!=""){ 
        key=key+'&state='+encodeURI(state);
    }
    window.location.href=key;
}
