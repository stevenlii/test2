//多条件查询方法
function query(){
	var ixData = jQuery('#ixData').val();
	var ixData2 = jQuery('#ixData2').val();
	var state = jQuery('#state').val();
	var creator = jQuery('#creator').val();
	var modUser = jQuery('#modUser').val();
	var auditType = jQuery('#auditType').val();
	var key="";
	
	if(auditType != "" && creator != "全部"){ //没有查询条件，要显示全部数据
		key = key + '&auditType=' + encodeURI(auditType);
	}
	if(ixData != ""){ 
		key = key + '&ixData=' + encodeURI(ixData);
	}
	if(ixData2 != "" && ixData2 != "全部"){ 
		key = key + '&ixData2=' + encodeURI(ixData2);
	}
	if(state != "" && creator != "全部"){ 
		key = key + '&state=' + encodeURI(state);
	}
	if(creator != "" && creator != "全部"){ 
		key = key + '&creator=' + encodeURI(creator);
	}
	if(modUser != "" && modUser != "全部"){ 
		key = key + '&modUser=' + encodeURI(modUser);
	}
	$('#grid').omGrid("setData", url+key);
} 

//审核通过方法
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

//弹出审核不通过对话框
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