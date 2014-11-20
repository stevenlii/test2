var chnlIdFlag = false;//批量渠道银行配置渠道号验证结果
var tree;
var setting = {
	check: {
		enable: true,
		chkStyle: "checkbox"
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};
function setCheck() {
	var zTree = $.fn.zTree.getZTreeObj("tree"),
	type = { "Y": "ps", "N": "ps" };
	zTree.setting.check.chkboxType = type;
}
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
	var channelName=jQuery('#channelName').val();
	var bankId=jQuery('#bankId').omCombo('value');
	var service_user=$('#service_user').omCombo('value') ;
	var state=jQuery('#state').val();
	var from=jQuery('#from').val();
	var to=jQuery('#to').val();
	var key="";
    if(channelId != ""){ //没要有查询条件，要显示全部数据
    	key=key+'&channelId='+encodeURI(channelId);
    }
    if(channelName !=""){ 
    	key=key+'&channelName='+encodeURI(channelName);
    }
    if(bankId !="" && bankId != "全部"){
    	key=key + '&bankId='+encodeURI(bankId);
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
function getChnlNameAndTree(channelId){
	if(channelId==""||channelId=="请选择"){
		return false;
	}
	var zTreeObj = $.fn.zTree.getZTreeObj("tree");
	zTreeObj.destroy();
	$.ajax({
		url : 'getchnlnameandtree.do',
		type : 'POST',
		dataType: 'json',
		data : {
				'channelId' : channelId
		},
		success : function(res){
			document.getElementById("channelName").innerHTML=res.chnlName;
			var zNodes2=eval(res.tree);
			tree=$.fn.zTree.init($("#tree"), setting, zNodes2);
			setCheck();
		}
	});
}

//商户银行配置
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
	var bankIds = document.getElementById("bankId").value;
    $.ajax({
		url : 'update.do',
		type : 'POST',
		dataType: 'text',
		data : {
				'channelId' : channelId,
				'bankIds' : bankIds
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
		 alert("没有修改支付银行，不需提交！");
		 return false;
	 }else {
	 	var relatedMenuIds = [];
		for(var i = 0; i < checkedNodes.length; i++){
			var node = checkedNodes[i];
			relatedMenuIds[relatedMenuIds.length] = node.id;
			}
		var ids = relatedMenuIds.join(",");
		$("#bankId").val(ids); 
		 return true;
	}
}
//打开渠道银行配置页面
function modify(){
	$("#dialog").omDialog("option","width","450");
    $("#dialog").omDialog("option","title","渠道银行配置");
    $("#dialog").omDialog("option","height","470");
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='modify.do';
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}
//打开批量渠道银行配置页面
function batchMod(){
	$("#dialog").omDialog("option","width","530");
    $("#dialog").omDialog("option","title","批量渠道银行配置");
    $("#dialog").omDialog("option","height","500");
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='batchMod.do';
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}
//根据渠道号获取名称
function getChnlName(){
	if(chnlIdFlag){
		var channelId = document.getElementById("channelId").value;
		$.ajax({
			url : 'getchnlname.do',
			type : 'POST',
			dataType: 'json',
			data : {
			'channelId' : channelId
			},
			success : function(res){
				if(res.result == "no"){
					$("#errorChnlId").text(res.error);
				}else if(res.result == "yes"){
					$("#errorChnlId").text("");
					var chnlName = res.chnlName;
					if(chnlName.length > 30){
						chnlName = chnlName.substring(0,26) + "...";
					}
					$("#channelName").text(chnlName);
					$("#channelName").attr("title",res.chnlName);
				}
			}
		});
	}else{
		$("#errorChnlId").text("");
	}
}
//批量配置
function batchUpdate(){
	if (!jQuery("#dataform").valid()){ 
		return false;
 	}
	if(!checkedNodes()){
		return false;
	}
 	if (!checkChnlId()) { 
        return false;
    }
    if(chnlIdFlag){
    	var channelId = document.getElementById("channelId").value;
    	var state = document.getElementById("state").value;
    	var bankId = document.getElementById("bankId").value;
    	$.ajax({
    		url : 'batchupdate.do',
    		type : 'POST',
    		dataType: 'text',
    		data : {
    				'channelId' : channelId,
    				'bankId' : bankId,
    				'state' : state
    		},
    		success : function(result){
    			if(result == "no"){
    				showSimpleTip(result);
    			}else if(result!="yes"){
    				 $.omMessageBox.alert({
    		             type:'error',
    		             title:'失败',
    		             content:result
    		         });
    			}else{
    				closeDialog();
    				showSuccess();
    				window.parent.shuaxin();
    			}
    		}
    	});
    }
}

//验证渠道号是否有重复
function checkChnlId(){
	var channelId = document.getElementById("channelId").value;
	var id = channelId.split(",");
	for(var i=0;i<id.length-1;i++){
		for(var j=i+1;j<id.length;j++){
			if(id[i]==id[j]){
				var flag = confirm("有重复的渠道编号"+id[i]+"，您确定继续提交？");
				return flag;
			}
		}
	}
	return true;
}
//打开详情页面
function detail(channelId,bankId){
	$("#dialog").omDialog("option","width","400");
    $("#dialog").omDialog("option","title","渠道银行详情");
    $("#dialog").omDialog("option","height","250");
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='detail.do?channelId='+channelId+'&bankId='+bankId;
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}

//单条禁用
function singleDisable(channelId,bankId){
	flag=confirm("确定禁用此渠道银行？");
	if(flag){
		var ID = channelId + "-" + bankId;
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
function singleEnable(channelId,bankId){
	flag=confirm("确定启用此渠道银行？");
	if(flag){
		var ID = channelId + "-" + bankId;
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
    		alert("渠道编号为["+selections[j].CHANNELID+"]的数据已被锁定，无法进行操作");
    		return false;
    	}
    }
    ID=trim(selections[0].CHANNELID)+'-'+trim(selections[0].BANKID);
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
     		check=1;
     		break;
     	}
	    ID=trim(selections[i].CHANNELID)+'-'+trim(selections[i].BANKID)+','+ID;
    }
    if(check==1){ 
        alert("请选择相同的状态类型进行操作！");
        return false;
    }else{
    	flag=confirm("确定禁用选中的渠道银行？");
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
    ID=trim(selections[0].CHANNELID)+'-'+trim(selections[0].BANKID);
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
     		check=1;
     		break;
     	}
	    ID=trim(selections[i].CHANNELID)+'-'+trim(selections[i].BANKID)+','+ID;
    }
    if(check==1){ 
        alert("请选择相同的状态类型进行操作！");
        return false;
    }else{
    	flag=confirm("确定启用选中的渠道银行？");
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

function exportData(){
	var channelId=jQuery('#channelId').val();
	var channelName=jQuery('#channelName').val();
	var bankId=jQuery('#bankId').omCombo('value');
	var service_user=$('#service_user').omCombo('value') ;
	var state=jQuery('#state').val();
	var from=jQuery('#from').val();
	var to=jQuery('#to').val();
	var key="export.do?queryKey=chnlbank.allChnlBank";
    if(channelId != ""){ //没要有查询条件，要显示全部数据
    	key=key+'&channelId='+encodeURI(channelId);
    }
    if(channelName !=""){ 
    	key=key+'&channelName='+encodeURI(channelName);
    }
    if(bankId !="" && bankId != "全部"){
    	key=key + '&bankId='+encodeURI(bankId);
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