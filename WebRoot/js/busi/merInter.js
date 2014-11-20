//查询方法
function query(){
	var merId=jQuery('#merId').val();
	var state=jQuery('#state').val();
	var key="";
	if(merId != "" && merId != "全部"){ //没要有查询条件，要显示全部数据
		key=key+'&merId='+encodeURI(merId);
	}
	if(state!=""){ 
		key=key+'&state='+encodeURI(state);
	}
	$('#grid').omGrid("setData", url+key);
}
//增加方法
function add(){
	$( "#dialog").omDialog("option","width",'500');
	$( "#dialog").omDialog("option","title",'添加商户接口信息');
	$( "#dialog").omDialog("option","height",'330');
	$( "#dialog").omDialog("option", "modal", true);
	$( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='add.do';
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
}
//修改方法
function modify(merId,inFunCode,inVersion){
	$( "#dialog").omDialog("option","width",'500');
	$( "#dialog").omDialog("option","title",'修改商户接口信息');
	$( "#dialog").omDialog("option","height",'300');
	$( "#dialog").omDialog("option", "modal", true);
	$( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='modify.do?merId='+merId+'&inFunCode='+inFunCode+'&inVersion='+inVersion;
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
}
//显示详情方法
function detail(merId,inFunCode,inVersion){
	$( "#dialog").omDialog("option","width",'450');
	$( "#dialog").omDialog("option","title",'查看商户接口信息');
	$( "#dialog").omDialog("option","height",'300');
	$( "#dialog").omDialog("option", "modal", true);
	$( "#dialog").omDialog('open');
	var frameLoc=window.frames[0].location;
	frameLoc.href='detail.do?merId='+merId+'&inFunCode='+inFunCode+'&inVersion='+inVersion;
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
}
//单条启用
function singleEnable(merId,inFunCode,inVersion){  
	var flag = confirm("确认启用该条记录？");
	if (flag){
		$.ajax({
			url : 'enable.do',
			type : 'POST',
			dataType: 'text',
			data : {
				'merId' : merId,
				'inFunCode' : inFunCode,
				'inVersion' : inVersion
			},
			success : function(data){
				showSimpleTip(data,true);  
				shuaxin();
			}
		});
	}
}

//单条禁用
function singleDisable(merId,inFunCode,inVersion){
	var flag = confirm("确定禁用该条记录？");
	if (flag){
		$.ajax({
			url : 'disable.do',
			type : 'POST',
			dataType: 'text',
			data : {
				'merId' : merId,
				'inFunCode' : inFunCode,
				'inVersion' : inVersion
			},
			success : function(data){
				showSimpleTip(data,true);  
				shuaxin();
			}
		});
	}
}
//批量启用方法
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
	var check=true;//校验值
	var length= selections.length;
	var merId=selections[0].MERID;
	var inFunCode=selections[0].INFUNCODE;
	var inVersion=selections[0].INVERSION;
	for(var i=1;i<length;i++){
		var data=selections[i];
		if (data.STATE!=selections[0].STATE){
			check=false;
			break;
		}
		merId=merId+","+data.MERID;
		inFunCode=inFunCode+","+data.INFUNCODE;
		inVersion=inVersion+","+data.INVERSION;
	}
	if(!check){ 
		alert("请选择相同的状态类型进行操作");
		return false;
	}else{
		if(confirm("确认启用这些记录？")){
			$.ajax({
				url : 'enable.do',
				type : 'POST',
				dataType: 'text',
				data : {
					'merId' : merId,
					'inFunCode' : inFunCode,
					'inVersion' : inVersion
				},
				success : function(data){
					showSimpleTip(data,true);  
					shuaxin();
				}
			});
		}
	}
}
//批量禁用方法
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
	var check=true;//校验值 
	var length= selections.length;
	var merId=selections[0].MERID;
	var inFunCode=selections[0].INFUNCODE;
	var inVersion=selections[0].INVERSION;
	for(var i=0;i<length;i++){
		var data=selections[i];
		if (data.STATE!=selections[0].STATE){
			check=false;
			break;
		}
		merId=merId+","+data.MERID;
		inFunCode=inFunCode+","+data.INFUNCODE;
		inVersion=inVersion+","+data.INVERSION;
	}
	if(!check){ 
		alert("请选择相同的状态类型进行操作");
		return false;
	}else{
		if(confirm("确定禁用这些记录？")){
			$.ajax({
				url : 'disable.do',
				type : 'POST',
				dataType: 'text',
				data : {
					'merId' : merId,
					'inFunCode' : inFunCode,
					'inVersion' : inVersion
				},
				success : function(data){
					showSimpleTip(data,true);  
					shuaxin();
				}
			});     
		}
	}
}
