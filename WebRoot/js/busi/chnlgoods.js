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
function query(){
	var channelId=jQuery('#channelId').val();
	var merId=jQuery('#merId').val();
	var goodsId=jQuery('#goodsId').val();
	var service_user=$('#service_user').omCombo('value') ;
	var state=jQuery('#state').val();
	var merState=jQuery('#merState').val();
	var goodsState=jQuery('#goodsState').val();
	var from=jQuery('#from').val();
	var to=jQuery('#to').val();
	var key="";
    if(channelId != ""){ //没要有查询条件，要显示全部数据
    	key=key+'&channelId='+encodeURI(channelId);
    }
    if(merId !=""){ 
    	key=key+'&merId='+encodeURI(merId);
    }
    if(goodsId !=""){ 
    	key=key+'&goodsId='+encodeURI(goodsId);
    }
    if(merState !="" && merState != "全部"){
    	key=key + '&merState='+encodeURI(merState);
    }
    if(goodsState !="" && goodsState != "全部"){
    	key=key + '&goodsState='+encodeURI(goodsState);
    }
    if(service_user!="" && service_user != "全部"){ 
        key=key+'&service_user='+encodeURI(service_user);
    }
    if(state!="" && state != "全部"){ 
    	key=key+'&state='+encodeURI(state);
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
    $('#grid').omGrid("setData", url+key);
}
//根据渠道号获取渠道名称和树
function getChnlId(channelId){
	if(channelId==""){
		return false;
	}
	$("#showChnlId").text(channelId);
	var merId = jQuery('#merId').val();
	if(merId != ""){
		getNewTree(channelId, merId);
	}
}
//根据商户号获取商品树
function getGoodsTree(merId){
	if(merId==""){
		return false;
	}
	var channelId = jQuery('#channelId').val();
	getNewTree(channelId, merId);
}
//生成商品树
function getNewTree(channelId, merId){
	var zTreeObj = $.fn.zTree.getZTreeObj("tree");
	zTreeObj.destroy();
	$.ajax({
		url : 'gettree.do',
		type : 'POST',
		dataType: 'text',
		data : {
			'channelId':channelId,
			'merId' : merId
		},
		success : function(res){
			var zNodes2=eval(res);
			tree=$.fn.zTree.init($("#tree"), setting, zNodes2);
			setCheck();
		}
	});
}

//配置
function update(){
	var channelId = document.getElementById("channelId");
	if(channelId.value=='请选择'){
		channelId.innerTEXT='';
	}
	if (!jQuery("#dataform").valid()) { 
        return false;
    }
    if(!checkedNodes()){
        return false;
    }
    var channelId=channelId.value;
    var merId = jQuery('#merId').val();
    var goodsIds = jQuery('#goodsId').val();
    $.ajax({
		url : 'update.do',
		type : 'POST',
		dataType: 'text',
		data : {
				'channelId' : channelId,
				'merId' : merId,
				'goodsIds' : goodsIds
		},
		success : function(result){
			if(result == "yes"){
				closeDialog();
				showSuccess();
				window.parent.shuaxin();
			}else{
				showSimpleTip(result);
			}
		}
	});
}

//获取树结构发生修改的节点内容
function checkedNodes(){
	var checkedNodes = tree.getChangeCheckedNodes();
	if(checkedNodes.length==0){
		 alert("没有修改商品列表，不需提交！");
		 return false;
	 }else {
	 	var relatedMenuIds = [];
		for(var i = 0; i < checkedNodes.length; i++){
			var node = checkedNodes[i];
			relatedMenuIds[relatedMenuIds.length] = node.id;
			}
		var ids = relatedMenuIds.join(",");
		$("#goodsId").val(ids); 
		 return true;
	}
}
//打开配置页面
function modify(){
	$("#dialog").omDialog("option","width","450");
    $("#dialog").omDialog("option","title","渠道商品配置");
    $("#dialog").omDialog("option","height","450");
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='modify.do';
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}
//打开详情页面
function detail(channelId,merId,goodsId){
	$("#dialog").omDialog("option","width","400");
    $("#dialog").omDialog("option","title","渠道商品详情");
    $("#dialog").omDialog("option","height","360");
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='detail.do?channelId='+channelId+'&merId='+merId+'&goodsId='+goodsId;
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}

//单条禁用
function singleDisable(channelId,merId,goodsId){
	flag=confirm("确定禁用此渠道商品？");
	if(flag){
		var ID = channelId + "-" + merId + "-" + goodsId;
		$.ajax({
			url : 'disable.do',
			type : 'POST',
			dataType: 'text',
			data : {
				'ID' : ID
			},
			success : function(data){
				showSimpleTip(data);
				shuaxin();
			}
		});
	}
}
//单条启用
function singleEnable(channelId,merId,goodsId){
	flag=confirm("确定启用此渠道商品？");
	if(flag){
		var ID = channelId + "-" + merId + "-" + goodsId;
		$.ajax({
			url : 'enable.do',
			type : 'POST',
			dataType: 'text',
			data : {
				'ID' : ID
			},
			success : function(data){
				showSimpleTip(data);
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
    var check=0;//校验值 默认为0 表示正常
    var length= selections.length;
    var ID="";
    for(var j=0;j<length;j++){
    	if(selections[j].MODLOCK == 1){
    		alert("渠道商品为["+selections[j].CHANNELID+","+selections[j].MERID+","+selections[j].GOODSID+"]的数据已被锁定，无法进行操作");
    		return false;
    	}
    }
    ID=trim(selections[0].CHANNELID)+'-'+trim(selections[0].MERID)+'-'+trim(selections[0].GOODSID);
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
     		check=1;
     		break;
     	}
	    ID=trim(selections[i].CHANNELID)+'-'+trim(selections[i].MERID)+'-'+trim(selections[i].GOODSID)+','+ID;
    }
    if(check==1){ 
        alert("请选择相同的状态类型进行操作！");
        return false;
    }else{
    	flag=confirm("确定禁用选中的渠道商品？");
    	if(flag){
    		$.ajax({
    			url : 'disable.do',
    			type : 'POST',
    			dataType: 'text',
    			data : {
    				'ID' : ID
	    		},
	    		success : function(data){
	    			showSimpleTip(data);  
	    			shuaxin();
	    		}
    		});
    	}
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
    var check=0;//校验值 默认为0 表示正常
    var length= selections.length;
    var ID="";
    for(var j=0;j<length;j++){
    	if(selections[j].MODLOCK == 1){
    		alert("当前记录已被锁定，无法进行操作！");
    		return false;
    	}
    }
    ID=trim(selections[0].CHANNELID)+'-'+trim(selections[0].MERID)+'-'+trim(selections[0].GOODSID);
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
     		check=1;
     		break;
     	}
	    ID=trim(selections[i].CHANNELID)+'-'+trim(selections[i].MERID)+'-'+trim(selections[i].GOODSID)+','+ID;
    }
    if(check==1){ 
        alert("请选择相同的状态类型进行操作！");
        return false;
    }else{
    	flag=confirm("确定启用选中的渠道商品？");
    	if(flag){
    		$.ajax({
    			url : 'enable.do',
    			type : 'POST',
    			dataType: 'text',
    			data : {
    				'ID' : ID
	    		},
	    		success : function(data){
	    			showSimpleTip(data);  
	    			shuaxin();
	    		}
    		});
    	}
    }
}
//渠道商品导出
function exportGoods(){
	var key = "exportgoods.do?queryKey=chnlgoods.allChnlGoods";
	exportData(key);
}
//渠道商户导出
function exportMer(){
	var key = "exportmer.do?queryKey=chnlgoods.allChnlMer";
	exportData(key);
}

//导出方法
function exportData(key){
	var channelId=jQuery('#channelId').val();
	var merId=jQuery('#merId').val();
	var goodsId=jQuery('#goodsId').val();
	var service_user=$('#service_user').omCombo('value') ;
	var state=jQuery('#state').val();
	var merState=jQuery('#merState').val();
	var goodsState=jQuery('#goodsState').val();
	var from=jQuery('#from').val();
	var to=jQuery('#to').val();
	if(channelId != ""){ //没要有查询条件，要显示全部数据
    	key=key+'&channelId='+encodeURI(channelId);
    }
    if(merId !=""){ 
    	key=key+'&merId='+encodeURI(merId);
    }
    if(goodsId !=""){ 
    	key=key+'&goodsId='+encodeURI(goodsId);
    }
    if(merState !="" && merState != "全部"){
    	key=key + '&merState='+encodeURI(merState);
    }
    if(goodsState !="" && goodsState != "全部"){
    	key=key + '&goodsState='+encodeURI(goodsState);
    }
    if(service_user!="" && service_user != "全部"){ 
        key=key+'&service_user='+encodeURI(service_user);
    }
    if(state!="" && state != "全部"){ 
    	key=key+'&state='+encodeURI(state);
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
    window.location.href=key;
}