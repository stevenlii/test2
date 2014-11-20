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
		 $.omMessageBox.alert({
             type:'error',
             title:'失败',
             content:'操作失败！'
         });
	 }
}
function showSuccess(){
	window.parent.$.omMessageBox.alert({
		type : 'success',
		title : '操作提示',
		content : '操作成功！'
	});
}

//多条件查询方法
function query(){
     
  var merName = jQuery('#merName').val();
  var state = jQuery('#state').val();
  var merId = jQuery('#merId').val();
  var operator = jQuery('#operator').val();
  var bankId = jQuery('#bankId').val();

  var key = "?queryKey=merbank.allMerBanks";
  if(merName != ""){ //没有查询条件，要显示全部数据
   
	  key = key + '&merName=' + encodeURI(merName);
  }
   if(state != ""){ 
    	key = key + '&state=' + encodeURI(state);
   }
   if(merId != ""){ 
        key = key + '&merId=' + encodeURI(merId);
   }
   if(operator != "" && operator != "全部"){ 
   		key = key + '&operator=' + encodeURI(operator);
   }
   if(bankId != "" && bankId != "全部"){ 
   		key = key + '&bankId=' + encodeURI(bankId);
   }
   $('#grid').omGrid("setData", url+key);
   return false;
} 
//单条禁用
function singleDisable(merId,bankId,modLock){
	flag=confirm("确定禁用此支付银行？");
	if(flag){
		if(modLock == '1'){
			alert("当前记录处于锁定状态，无法进行该操作！");
			return false;
		}
		var id = merId + "-" + bankId;
		$.ajax({
			url : 'disable.do',
			type : 'POST',
			dataType: 'text',
			data : {
				'id' : id
			},
			success : function(data){
				showSimpleTip(data);
				jQuery('#grid').omGrid('reload');
			}
		});
	}
}
//单条启用
function singleEnable(merId,bankId,modLock){
	flag=confirm("确定启用此支付银行？");
	if(flag){
		if(modLock == '1'){
			alert("当前记录处于锁定状态，无法进行该操作！");
			return false;
		}
		var id = merId + "-" + bankId;
		$.ajax({
			url : 'enable.do',
			type : 'POST',
			dataType: 'text',
			data : {
				'id' : id
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
        alert('请选择处于启用状态的记录进行禁用操作！');
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
    ID=trim(selections[0].MERID)+'-'+trim(selections[0].BANKID);
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
     		check=1;
     		break;
     	}
	    ID=trim(selections[i].MERID)+'-'+trim(selections[i].BANKID)+','+ID;
    }
    if(check==1){ 
        alert("请选择相同的状态类型进行操作！");
        return false;
    }else{
       $.ajax({
			url : 'disable.do',
			type : 'POST',
			dataType: 'text',
			data : {
					'id' : ID
			},
			success : function(data){
				showSimpleTip(data);  
				jQuery('#grid').omGrid('reload');
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
    var check=0;//校验值 默认为0 表示正常
    var length= selections.length;
    var ID="";
    for(var j=0;j<length;j++){
    	if(selections[j].MODLOCK == 1){
    		alert("当前记录已被锁定，无法进行操作！");
    		return false;
    	}
    }
    ID=trim(selections[0].MERID)+'-'+trim(selections[0].BANKID);
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
     		check=1;
     		break;
     	}
	    ID=trim(selections[i].MERID)+'-'+trim(selections[i].BANKID)+','+ID;
    }
    if(check==1){ 
        alert("请选择相同的状态类型进行操作！");
        return false;
    }else{
       $.ajax({
			url : 'enable.do',
			type : 'POST',
			dataType: 'text',
			data : {
					'id' : ID
			},
			success : function(data){
				showSimpleTip(data);  
				jQuery('#grid').omGrid('reload');
			}
		});
    }
}

//跳转到配置商户银行页面
function openone(){
	$("#dialog").omDialog("option","width",450);
    $("#dialog").omDialog("option","title",'商户银行配置');
    $("#dialog").omDialog("option","height",505);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='modify.do';
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}

//跳转到批量配置商户银行页面
function openBatchMer(){
	$("#dialog").omDialog("option","width",520);
    $("#dialog").omDialog("option","title",'批量配置商户银行');
    $("#dialog").omDialog("option","height",500);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='tobatchupdate.do';
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
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
  
//商户银行配置
function oneMer(){
	var merId = document.getElementById("merId");
	if(merId.value=='请选择'){
		merId.innerTEXT='';
	}
	if (!jQuery("#dataform").valid()) { 
        return false;
    }
    if(!checkedNodes()){
        return false;
    }
    var merId=merId.value;
	var bankid = document.getElementById("bankId").value;
    $.ajax({
		url : 'update.do',
		type : 'POST',
		dataType: 'text',
		data : {
				'merId' : merId,
				'bankid' : bankid
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
//根据商户号获取商户名称
function getMerName(merId){
	if(merId==""||merId=="请选择"){
		return false;
	}
	var zTreeObj = $.fn.zTree.getZTreeObj("tree");
	zTreeObj.destroy();
	$.ajax({
			url : 'getmername.do',
			type : 'POST',
			dataType: 'text',
			data : {
					'merId' : merId
			},
			success : function(merName){
				document.getElementById("merName").innerHTML=merName;
			}
	});
	$.ajax({
			url : 'getnewtree.do',
			type : 'POST',
			dataType: 'text',
			data : {
					'merId' : merId
			},
			success : function(data,status,xhr){
				 var zNodes2=eval(data);
				 tree=$.fn.zTree.init($("#tree"), setting, zNodes2);
				setCheck();
				$("#py").bind("change", setCheck);
				$("#sy").bind("change", setCheck);
				$("#pn").bind("change", setCheck);
				$("#sn").bind("change", setCheck); 
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

//验证后提交批量配置商户银行表单
function batchMer(){
	if (!jQuery("#dataform").valid()){ 
		return false;
 	}
 	if (!checkMerId()) { 
        return false;
    }
    if(!checkedNodes()){
        return false;
    }else{
    	submitBatchMerForm();  //提交表单
    }

}	
//验证商户号
function checkMerId(){
	var merId = document.getElementById("merId").value;
	var id = merId.split(",");
	for(var i=0;i<id.length-1;i++){
		for(var j=i+1;j<id.length;j++){
			if(id[i]==id[j]){
				var flag = confirm("有重复的商户号"+id[i]+"，您确定继续提交？");
				return flag;
			}
		}
	}
	return true;
}

//提交批量配置商户银行表单
function submitBatchMerForm(){
	var merId = document.getElementById("merId").value;
	var state = document.getElementById("state").value;
	var bankId = document.getElementById("bankId").value;
	$.ajax({
		url : 'batchupdate.do',
		type : 'POST',
		dataType: 'text',
		data : {
				'merId' : merId,
				'state' : state,
				'bankId' : bankId
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

//导出
function expMerBank(){
    var merName=jQuery('#merName').val();
    var merId=jQuery('#merId').val();
    var bankId=$('#bankId').omCombo('value');
    var operator=$('#operator').omCombo('value');
    var state=jQuery("#state").omCombo('value');
    var key="export.do?queryKey=merbank.allMerBanks";
    if(merName != ""){ //没有查询条件，要显示全部数据
	  key = key + '&merName=' + encodeURI(merName);
  }
   if(state != ""){ 
    	key = key + '&state=' + encodeURI(state);
   }
   if(merId != ""){ 
        key = key + '&merId=' + encodeURI(merId);
   }
   if(operator != "" && operator != "全部"){ 
   		key = key + '&operator=' + encodeURI(operator);
   }
   if(bankId != "" && operator != "全部"){ 
   		key = key + '&bankId=' + encodeURI(bankId);
   }
   window.location.href=key;
}

//开通默认小额银行
function autoCheck(field){
	var merId = document.getElementById("merId").value;
	var xeBank = document.getElementById("xeBank");
	if(merId==""){
		xeBank.checked=false;
		alert("请先选择商户号!");
		return false;
	}
	if(xeBank.value == ""){
		return false;
	}
	var bankIds = xeBank.value.split(",");
	var treeObj = $.fn.zTree.getZTreeObj("tree");
	if(field.checked){
		for(var i=0;i<bankIds.length;i++){
			var node = treeObj.getNodeByParam("id", bankIds[i], null);
			treeObj.checkNode(node, true, true);
		}
	}else{
		for(var i=0;i<bankIds.length;i++){
			var node = treeObj.getNodeByParam("id", bankIds[i], null);
			treeObj.checkNode(node, false, true);
		}
	}
}
//刷新页面
function shuaxin(){
	jQuery('#grid').omGrid('reload');
}