//当前页面弹出操作提示框
function showSimpleTip(result){
//result 结果为yes表示成功，结果为no表示失败
	if(result == 'yes'){
		jQuery('#grid').omGrid('reload');
	}else{
		var msg = '操作失败，请稍后再试。';
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
//关闭窗口方法
function closeDialog(){
	try{
		window.parent.fillBackAndCloseDialog();
	}catch(e){
		fillBackAndCloseDialog();
	} 
}
//删除空格
function trim(str) {
   if (str == null) {
      return "";
   }
   return str.replace(/^\s*(.*?)[\s\n]*$/g,'$1');   
}
function fillBackAndCloseDialog(){
	$("#dialog").omDialog('close');
}
//获取字节长度
function byteCount(s){
	var c = 0;
	var a = s.split("");
	for (var i=0;i<a.length;i++) {
		c=(a[i].charCodeAt(0)<299)?(c+1):(c+2);
	}
	return c;
}
//刷新页面
function shuaxin(){
	jQuery('#grid').omGrid('reload');
}
//多条件查询方法
function query(){
     
  var channelId = jQuery('#channelId').val();
  var bankId = jQuery('#bankId').val();
  var state = jQuery('#state').val();
  var creator = jQuery('#creator').val();
  var modUser = jQuery('#modUser').val();
  var auditType = jQuery('#auditType').val();
  var key = "";
  if(auditType != ""){ //没有查询条件，要显示全部数据
	  key = key + '&auditType=' + encodeURI(auditType);
  }
   if(state != ""){ 
    	key = key + '&state=' + encodeURI(state);
   }
   if(bankId == "全部"){
	   bankId = "";
   }
   if(channelId != "" || bankId != ""){
   		ixData = channelId + "-" + bankId;
        key = key + '&ixData=' + encodeURI(ixData);
   }
   if(creator != "" && creator != "全部"){ 
   		key = key + '&creator=' + encodeURI(creator);
   }
   if(modUser != "" && modUser != "全部"){ 
   		key = key + '&modUser=' + encodeURI(modUser);
   }
   $('#grid').omGrid("setData", url+key);
} 

//查询页面审核通过方法
function goPass(id){
	$.omMessageBox.confirm({
        title:'审核通过',
        content:'确认审核通过此信息？',
        onClose:function(value){
            if (value){
            	$.ajax({
            		url : 'auditPass.do',
            		type : 'POST',
            		dataType: 'text',
            		data : {
            			'id' : id
            	},
            	success : function(data){
    				showSimpleTip(data);
            	}
            	});
            }
        }
    });
}
//详情页面审核通过方法
function goPass1(id){
	var resultDesc = document.getElementById("resultDesc").value;
	if(byteCount(resultDesc)>64){
		alert("审核意见最大长度为64个字符！")
		return false;
	}
	if(confirm("确定审核通过此信息？")){
		$.ajax({
			url : 'auditPass.do',
			type : 'POST',
			dataType: 'text',
			data : {
					'id' : id,
					'resultDesc' : resultDesc
			},
			success : function(data){
				if(data == "yes"){
					closeDialog();
					window.parent.shuaxin();
				}
				window.parent.showSimpleTip(data);
			}
		});
	}
}

//查询页面审核不通过
function notPass(id){
	$.omMessageBox.prompt({
        title:'审核不通过',
        content:'请输入您的审核意见：',
        onClose:function(value){
            if(value===false){ //按了取消或ESC
                return;
            }
            if(value==''){
                alert('审核意见不能为空');
                return false; //不关闭弹出窗口
            }
            if(byteCount(value)>64){
        		alert("审核意见最大长度为64个字符！")
        		return false;
        	}
            $.ajax({
            	url : 'auditNotPass.do',
            	type : 'POST',
            	dataType: 'text',
            	data : {
            		'id' : id,
            		'resultDesc' : value
	            },
	            success : function(data){
	            	showSimpleTip(data);
	            }
            });
        }
    });
}
//详情页面审核不通过
function goNotPass(id){
	var flag = true;
	var resultDesc = document.getElementById("resultDesc").value;
	if(resultDesc == ""){
		alert("请先输入审核意见！");
		return false;
	}
	if(byteCount(resultDesc)>64){
		alert("审核意见最大长度为64个字符！")
		return false;
	}
	flag=confirm("确定审核不通过此信息？");
	if(flag){
		$.ajax({
			url : 'auditNotPass.do',
			type : 'POST',
			dataType: 'text',
			data : {
					'id' : id,
					'resultDesc' : resultDesc
			},
			success : function(data){
				closeDialog();
				window.parent.shuaxin();
				window.parent.showSimpleTip(data);
			}
		});
	}
}

function detail(id,auditType){
	var width = 450;
	var height = 300;
	var title = "";
	if (auditType == 1){
		title = "渠道银行配置审核";
	} else if (auditType != 0){
		title = "渠道银行启用/禁用审核";
	}
	$("#dialog").omDialog("option","width",width);
    $("#dialog").omDialog("option","title",title);
    $("#dialog").omDialog("option","height",height);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='detail.do?id=' + id;
	$("#dialog").omDialog({onClose : function(event) {
   		window.frames[0].location.href='../wait.htm';
    }});
	return false;
}
//批量校验
function batchVolid(){
	try{
		var selections=$('#grid').omGrid('getSelections',true);     
	}catch(e){
		alert("没有数据！！！");
		return false;
	}
    var length= selections.length;
	if (length == 0) {
        alert('请至少选择一行记录！');
        return false;
    }
    if(selections[0].STATE!=0){
        alert('请选择处于待审核状态的记录操作！');
        return false;
    }
    var check=0;//校验值 默认为0 表示正常
    var ID="";
    for(var j=0;j<length;j++){
    	if(selections[j].STATE != 0){
    		alert("渠道编号-上线省份为["+selections[j].CHANNELID+"-"+selections[j].BANKNAME+"]的记录已审核，无法再次进行审核操作！");
    		return false;
    	}
    }
    ID=trim(selections[0].ID);
    for(var i=1;i<length;i++){
	    if (selections[i].STATE!=selections[0].STATE){
     		check=1;
     		break;
     	}
	    ID=trim(selections[i].ID)+','+ID;
    }
    return ID;
}
 
//批量通过
function batchPass(){
	var id = batchVolid();
	if(id){
		goPass(id);
	}
}
//批量不通过
function batchNotPass(){
	var id = batchVolid();
	if(id){
		notPass(id);
	}
}
