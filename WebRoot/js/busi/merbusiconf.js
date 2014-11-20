//start 展示树形结构的业务属性
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
//end 展示树形结构的业务属性
function refuseBackspace(){
	if(event.keyCode==8)
		event.keyCode=0;
	return false;
}
//查询方法
function query(){
	var merName=$('#merName').val();
	var bizType=$('#bizType').val();
	var state=$('#state').val();
    var merId=$('#merId').val();
    var busiType=$('#busiType').val();
    var modUser=$('#modUser').val();  
	var key="";
	if(merName !=""){ 
		key=key+'&merName='+encodeURI(merName);
	}
     if(bizType !="" && bizType != "全部"){
        key=key + '&bizType='+encodeURI(bizType);
       }
     if(state!="" && state != "全部"){ 
    	 key=key+'&state='+encodeURI(state);
     }
     if(merId != ""){
    	 key=key+'&merId='+encodeURI(merId);
     }
     if(busiType!="" && busiType != "全部"){ 
    	 key=key+'&busiType='+encodeURI(busiType);
     }
     if(modUser!="" && modUser != "全部"){ 
        key=key+'&modUser='+encodeURI(modUser);
      }
	$('#grid').omGrid("setData", url+key);
}
//增加方法
function add(){
	 $( "#dialog").omDialog("option","width",'610');
      $( "#dialog").omDialog("option","title",'添加商户支付类型');
      $( "#dialog").omDialog("option","height",'480');
      $( "#dialog").omDialog("option", "modal", true);
	 $( "#dialog").omDialog('open');
	   var frameLoc=window.frames[0].location;
       frameLoc.href='add.do';
       $("#dialog").omDialog({onClose : function(event) {
      		window.frames[0].location.href='../wait.htm';
       }});
}
//根据商户号获取商户名称
function getConf(merId){
	if(merId==""||merId=="请选择"){
		return false;
	}
	$.ajax({
			url : 'getconf.do',
			type : 'POST',
			dataType: 'json',
			data : {'merId' : merId},
			success : function(data){
				var merName=data.merName;
				var bizTypes=data.bizTypes;
				$("#merName")[0].innerHTML=merName;
				$(":checkbox").removeAttr("checked");
				$(":checkbox").removeAttr("disabled"); 
				for(var i=0;i<bizTypes.length;i++){
					$("#bizType"+bizTypes[i]).attr("checked",'true');
					$("#bizType"+bizTypes[i]).attr("disabled",'disabled');
				}
			}
	});
}
//导出
function expMerBusiConf(){
	var merName=$('#merName').val();
	var bizType=$('#bizType').val();
	var state=$('#state').val();
    var merId=$('#merId').val();
    var busiType=$('#busiType').val();
    var modUser=$('#modUser').val();
	var key="export.do?queryKey=merbusiconf.allBusiConf";
	if(merName !=""){ 
		key=key+'&merName='+encodeURI(merName);
	}
	if(bizType !="" && bizType != "全部"){
		key=key + '&bizType='+encodeURI(bizType);
	}
	if(state!="" && state != "全部"){ 
		key=key+'&state='+encodeURI(state);
	}
	if(merId != ""){
		key=key+'&merId='+encodeURI(merId);
	}
	if(busiType!="" && busiType != "全部"){ 
		key=key+'&busiType='+encodeURI(busiType);
	}
	if(modUser!="" && modUser != "全部"){ 
		key=key+'&modUser='+encodeURI(modUser);
	}
    window.location.href=key;
}

//单条禁用
function singleDisable(merId,bizType){
	flag=confirm("确定禁用？");
	if(flag){
		$.ajax({
			url : 'disable.do',
			type : 'POST',
			dataType: 'text',
			data : {'merId' : merId,'bizType' : bizType},
			success : function(data){
				showSimpleTip(data);
				jQuery('#grid').omGrid('reload');
			}
		});
	}
}
//单条启用
function singleEnable(merId,bizType){
	flag=confirm("确定启用？");
	if(flag){
		$.ajax({
			url : 'enable.do',
			type : 'POST',
			dataType: 'text',
			data : {'merId' : merId,'bizType' : bizType},
			success : function(data){
				showSimpleTip(data);
				jQuery('#grid').omGrid('reload');
			}
		});
	}
}