function onClick(e, treeId, treeNode) {
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
}

//自定义校验规则
$.validator.addMethod("leng", function(value) {
	if(value==""){
		return true;
	}
	var reg = new RegExp("^-[a-zA-Z0-9]{1,12}$|^[a-zA-Z0-9]{1,12}$");
	return reg.test(value);
}, "代码最多为12位，可以'-'开头");

//多条件查询方法
function query(){
	var merId = jQuery('#merId').val();
	var goodsId = jQuery('#goodsId').val();
	var gateId = jQuery('#gateId').val();
	var serviceId = jQuery('#serviceId').val();
	var serviceName = jQuery('#serviceName').val();
	var state = jQuery('#state').val();
	var category = jQuery('#category').val();
	var feeType = jQuery('#feeType').val();
	
	var key = "";
	if(merId != "" && merId != "全部"){ //没有查询条件，要显示全部数据
		key = key + '&merId=' + encodeURI(merId);
	}
	if(goodsId != "" && goodsId != "全部"){ 
		key = key + '&goodsId=' + encodeURI(goodsId);
	}
	if(gateId != "" && gateId != "全部"){
		key = key + '&gateId=' + encodeURI(gateId);
	}
	if(serviceId != ""){ 
		key = key + '&serviceId=' + encodeURI(serviceId);
	}
	if(serviceName != ""){ 
		key = key + '&serviceName=' + encodeURI(serviceName);
	}
	if(state != "" && state != "全部"){ 
		key = key + '&state=' + encodeURI(state);
	}
	if(category != "" && category != "全部"){ 
		key = key + '&category=' + encodeURI(category);
	}
	if(feeType != "" && feeType != "全部"){ 
		key = key + '&feeType=' + encodeURI(feeType);
	}
	$('#grid').omGrid("setData", url+key);
	return false;
}

//跳转增加页面
function add(){
	$("#dialog").omDialog("option","width",520);
    $("#dialog").omDialog("option","title",'新增商品计费代码');
    $("#dialog").omDialog("option","height",450);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='add.do';
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}
//跳转修改页面
function modify(merId,goodsId,gateId){
	$("#dialog").omDialog("option","width",520);
    $("#dialog").omDialog("option","title",'修改商品计费代码');
    $("#dialog").omDialog("option","height",450);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='modify.do?merId='+merId+'&goodsId='+goodsId+'&gateId='+gateId;
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}
//单条禁用
function singleDisable(ID){
	if(confirm("确定禁用此商品？")){
		$.ajax({
			url : 'disable.do',
			type : 'POST',
			dataType: 'text',
			data : {
				'ID' : ID
			},
			success : function(data){
				showSimpleTip(data, true);
				shuaxin();
			}
		});
	}
}
//单条启用
function singleEnable(ID){
	if(confirm("确定启用此商品？")){
		$.ajax({
			url : 'enable.do',
			type : 'POST',
			dataType: 'text',
			data : {
				'ID' : ID
			},
			success : function(data){
				showSimpleTip(data, true);
				shuaxin();
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
        alert('请选择处于启用状态的记录进行禁用操作！');
        return false;
    }
    var ID = dealKey(selections);
    if(ID==false){
    	return false;
    }
    if(confirm("确定禁用选中的数据？")){
    	$.ajax({
    		url : 'disable.do',
    		type : 'POST',
    		dataType: 'text',
    		data : {
	    		'ID' : ID
	    	},
	    	success : function(data){
	    		showSimpleTip(data, true);
	    		shuaxin();
	    	}
    	});
    }
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
        alert('请选择处于禁用状态的记录进行启用操作！');
        return false;
    }
    var ID = dealKey(selections);
    if(ID==false){
    	return false;
    }
    if(confirm("确定启用选中的渠道商品？")){
    	$.ajax({
    		url : 'enable.do',
    		type : 'POST',
    		dataType: 'text',
    		data : {
	    		'ID' : ID
	    	},
	    	success : function(data){
	    		showSimpleTip(data, true);
	    		shuaxin();
	    	}
    	});
    }
}
//启用禁用时拼装主键方法
function dealKey(selections){
	var ID=trim(selections[0].MERID)+'#'+trim(selections[0].GOODSID)+'#'+trim(selections[0].GATEID);
    for(var i=1;i<selections.length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
	    	alert("请选择相同的状态类型进行操作！");
	        return false;
     	}
	    ID=ID+','+trim(selections[i].MERID)+'#'+trim(selections[i].GOODSID)+'#'+trim(selections[i].GATEID);
    }
    return ID;
}
//校验3组计费代码
function checkServiceId(){
	var id = [$("#serviceId").val(),$("#serviceId1").val(),$("#serviceId2").val()];
	var name = [$("#serviceName").val(),$("#serviceName1").val(),$("#serviceName2").val()];
	var flag = false;
	var msg = "";
	for(var i=0;i<id.length;i++){
		id[i] = trim(id[i]);
		name[i] = trim(name[i]);
		if(id[i]!=""&&name[i]==""){
			msg = "计费代码对应的名称不能为空";
			flag = false
			break;
		}
		if(id[i]==""&&name[i]!=""){
			msg = "名称对应的计费代码不能为空";
			flag = false
			break;
		}
		if(id[i]!=""&&name[i]!=""){
			flag = true;
		}
	}
	if(msg!=""){
		$.omMessageBox.alert({
			type:'error',
			title : '操作提示',
			content : msg
		});
		return false;
	}
	if(!flag){
		$.omMessageBox.alert({
			type:'error',
			title : '操作提示',
			content : "请至少填写一个计费代码"
		});
		return false;
	}
	return true;
}

function expService(){
	var merId = jQuery('#merId').val();
	var goodsId = jQuery('#goodsId').val();
	var gateId = jQuery('#gateId').val();
	var serviceId = jQuery('#serviceId').val();
	var serviceName = jQuery('#serviceName').val();
	var state = jQuery('#state').val();
	var category = jQuery('#category').val();
	var feeType = jQuery('#feeType').val();
	
	var key = "export.do?queryKey=upservice.query";
	if(merId != "" && merId != "全部"){ //没有查询条件，要显示全部数据
		key = key + '&merId=' + encodeURI(merId);
	}
	if(goodsId != "" && goodsId != "全部"){ 
		key = key + '&goodsId=' + encodeURI(goodsId);
	}
	if(gateId != "" && gateId != "全部"){
		key = key + '&gateId=' + encodeURI(gateId);
	}
	if(serviceId != ""){ 
		key = key + '&serviceId=' + encodeURI(serviceId);
	}
	if(serviceName != ""){ 
		key = key + '&serviceName=' + encodeURI(serviceName);
	}
	if(state != "" && state != "全部"){ 
		key = key + '&state=' + encodeURI(state);
	}
	if(category != "" && category != "全部"){ 
		key = key + '&category=' + encodeURI(category);
	}
	if(feeType != "" && feeType != "全部"){ 
		key = key + '&feeType=' + encodeURI(feeType);
	}
	window.open(key);
}