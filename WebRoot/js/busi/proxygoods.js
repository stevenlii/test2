var tree;
function setCheck() {
	var zTree = $.fn.zTree.getZTreeObj("tree"),
	type = { "Y": "ps", "N": "ps" };
	zTree.setting.check.chkboxType = type;
}

//页面校验方法
$.validator.addMethod("isEqual1", function(value) {
	var dayLimit = jQuery('#dayLimit').val();
	if(dayLimit==-1 && value!=-1){
		return false;
	}else{
		return true;
	}
}, "日销量为-1时，预警值必须为-1");
$.validator.addMethod("compareVal", function(value) {
		var dayLimit = jQuery('#dayLimit').val();
		if(dayLimit!=-1 && eval(dayLimit) < eval(value)){
			return false;
		}else{
			return true;
		}
   }, "预警值不能大于日销量");
$.validator.addMethod("zhengshu", function(value) {
	var reg = /^-1$|^(\d+)$/;
	if(reg.test(value)){
		if(value>=-1 && value<=32767){
			return true;
		}
	}
	return false;
}, "填写-1~32767的整数");
$.validator.addMethod("isEmpty", function(value) {
	var alarmLimit = jQuery('#alarmLimit').val();
	if(alarmLimit==-1||alarmLimit==''){
		return true;
	}
	if(value==''){
		return false;
	}
	return true;
 }, "预警数不为-1时,预警号码不能为空");
$.validator.addMethod("mobile", function(value) {
	var alarmLimit = jQuery('#alarmLimit').val();
	if(alarmLimit==-1||alarmLimit==''){
		return true;
	}
	var reg = /^1\d{10}$|^1\d{10}((,1\d{10})+)$/;
	return reg.test(value);
}, "请填写正确的号码格式,半角逗号分隔");

//多条件查询方法
function query(){
  var merId = jQuery('#merId').val();
  var subMerId = jQuery('#subMerId').val();
  var goodsId = jQuery('#goodsId').val();
  var state = jQuery('#state').val();
  var goodsName = jQuery('#goodsName').val();
  var servType = jQuery('#servType').val();

  var key = "";
  if(merId != ""){ //没有查询条件，要显示全部数据
	  key = key + '&merId=' + encodeURI(merId);
  }
  if(subMerId != ""){ //没有查询条件，要显示全部数据
	  key = key + '&subMerId=' + encodeURI(subMerId);
  }
   if(state != "" && state != "全部"){ 
    	key = key + '&state=' + encodeURI(state);
   }
   if(goodsId != ""){ 
        key = key + '&goodsId=' + encodeURI(goodsId);
   }
   if(servType != "" && servType != "全部"){ 
   		key = key + '&servType=' + encodeURI(servType);
   }
   if(goodsName != ""){ 
  		key = key + '&goodsName=' + encodeURI(goodsName);
  }
   $('#grid').omGrid("setData", url+key);
   return false;
}

//跳转增加页面
function add(){
	$("#dialog").omDialog("option","width",520);
    $("#dialog").omDialog("option","title",'新增代理商品');
    $("#dialog").omDialog("option","height",570);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='add.do';
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}
//跳转批量增加页面
function batchAdd(){
	$("#dialog").omDialog("option","width",520);
    $("#dialog").omDialog("option","title",'批量新增代理商品');
    $("#dialog").omDialog("option","height",570);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='batchAdd.do';
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}

function getMerIdAndTree(subMerId){
	$.ajax({
		url : 'getMerIdAndTree.do',
		type : 'POST',
		dataType: 'json',
		data : {
				'subMerId' : subMerId
		},
		success : function(res){
			$("#subMerName").html(res.subMerName);
			var data = $.parseJSON(res.merIdMap);
			$('#merId').omCombo('setData', data);
			var zNodes2=eval(res.tree);
			tree=$.fn.zTree.init($("#tree"), setting, zNodes2);
			setCheck();
		}
	});
}
//动态生成树，isBatch 为是否批量添加标志，true为批量添加。false为单个添加
function getTree(merId,isBatch){
	var subMerId = jQuery('#subMerId').val();
	$.ajax({
		url : 'getTree.do',
		type : 'POST',
		dataType: 'text',
		data : {
				'subMerId' : subMerId,
				'merId' : merId,
				'isBatch' : isBatch
		},
		success : function(res){
			var zNodes2=eval(res);
			tree=$.fn.zTree.init($("#tree"), setting, zNodes2);
			setCheck();
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

//跳转到修改页面
function modify(merId,subMerId,goodsId){
	$("#dialog").omDialog("option","width",500);
    $("#dialog").omDialog("option","title",'修改代理商品');
    $("#dialog").omDialog("option","height",400);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='modifySecMerInf.do?merId='+merId+'&subMerId='+subMerId+'&goodsId='+goodsId;
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
}

//单条禁用
function singleDisable(merId,subMerId,goodsId){
	flag=confirm("确定禁用此商品？");
	if(flag){
		var ID = merId + "-" + subMerId + "-" + goodsId;
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
function singleEnable(merId,subMerId,goodsId){
	flag=confirm("确定启用此商品？");
	if(flag){
		var ID = merId + "-" + subMerId + "-" + goodsId;
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
    		alert("商品为["+selections[j].MERID+","+selections[j].SUBMERID+","+selections[j].GOODSID+"]的数据已被锁定，无法进行操作");
    		return false;
    	}
    }
    ID=trim(selections[0].MERID)+'-'+trim(selections[0].SUBMERID)+'-'+trim(selections[0].GOODSID);
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
     		check=1;
     		break;
     	}
	    ID=ID+','+trim(selections[i].MERID)+'-'+trim(selections[i].SUBMERID)+'-'+trim(selections[i].GOODSID);
    }
    if(check==1){ 
        alert("请选择相同的状态类型进行操作！");
        return false;
    }else{
    	flag=confirm("确定禁用选中的商品？");
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
    		alert("商品为["+selections[j].MERID+","+selections[j].SUBMERID+","+selections[j].GOODSID+"]的数据已被锁定，无法进行操作");
    		return false;
    	}
    }
    ID=trim(selections[0].MERID)+'-'+trim(selections[0].SUBMERID)+'-'+trim(selections[0].GOODSID);
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
     		check=1;
     		break;
     	}
	    ID=ID+','+trim(selections[i].MERID)+'-'+trim(selections[i].SUBMERID)+'-'+trim(selections[i].GOODSID);
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