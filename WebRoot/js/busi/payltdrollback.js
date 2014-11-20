//多条件查询方法
function auditQuery(){
	var state = jQuery('#state').val();
	var creator = jQuery('#creator').val();
	var modUser = jQuery('#modUser').val();
	var ixData = jQuery('#ixData').val();
	var merId = jQuery('#merId').val();
	var goodsId = jQuery('#goodsId').val();
	var ixData2="";
	var key="";
	
	if(state != "" && creator != "全部"){ 
		key = key + '&state=' + encodeURI(state);
	}
	if(creator != "" && creator != "全部"){ 
		key = key + '&creator=' + encodeURI(creator);
	}
	if(modUser != "" && modUser != "全部"){ 
		key = key + '&modUser=' + encodeURI(modUser);
	}
	if(ixData != ""){ 
		key = key + '&ixData=' + encodeURI(ixData);
	}
	if(merId != ""){
		ixData2 = merId+"-";
	}else{
		ixData2 = "%-";
	}
	if(goodsId != ""){ 
		ixData2 = ixData2+goodsId;
	}else{
		ixData2 = ixData2+"%";
	}
	key = key + '&ixData2=' + encodeURI(ixData2);
	$('#grid').omGrid("setData", url+key);
} 

//展示详情方法
function show(id){
	$("#dialog").omDialog("option","width",480);
	$("#dialog").omDialog("option","title","限额回滚审核");
	$("#dialog").omDialog("option","height",400);
	$("#dialog").omDialog("option","modal", true);
	$("#dialog").omDialog('open');
	window.frames[0].location.href='detail.do?id=' + id;
	$("#dialog").omDialog({onClose : function(event) {
		window.frames[0].location.href='../wait.htm';
	}});
	return false;
}

//查询页面审核通过方法
function pass(id){
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
	            		shuaxin();
	            		showSimpleTip(data,true);  
	            	}
            	});
            }
        }
    });
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
    					shuaxin();
    					showSimpleTip(data,true);
    				}
    		});
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
				if(data == "1"){
					closeDialog();
					window.parent.shuaxin();
				}
				window.parent.showSimpleTip(data,true);
			}
		});
	}
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
				if(data == "1"){
					closeDialog();
					window.parent.shuaxin();
				}
				window.parent.showSimpleTip(data,true);
			}
		});
	}
}