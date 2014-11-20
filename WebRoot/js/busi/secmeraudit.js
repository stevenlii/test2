//多条件查询方法
function query(){
  var ixData = jQuery('#ixData').val();
  var state = jQuery('#state').omCombo('value');
  var creator = jQuery('#creator').omCombo('value');
  var modUser = jQuery('#modUser').omCombo('value');
  var auditType = jQuery('#auditType').omCombo('value');

  var key = "?queryKey=audit.allSecMerAudits";
  if(ixData != ""){ //没有查询条件，要显示全部数据
	  key = key + '&ixData=' + encodeURI(ixData);
  }
   if(state != ""){ 
    	key = key + '&state=' + encodeURI(state);
   }
   if(creator != "" && creator != "全部"){ 
        key = key + '&creator=' + encodeURI(creator);
   }
   if(modUser != "" && modUser != "全部"){ 
   		key = key + '&modUser=' + encodeURI(modUser);
   }
   if(auditType != ""){ 
   		key = key + '&auditType=' + encodeURI(auditType);
   }
   $('#grid').omGrid("setData", url+key);
}    
 
 
//展示详情方法
function show(id, auditType, state){
	var width = 480;
	var height = 420;
	var title = "";
	if (auditType == 1){
		title = "二级商户新增审核";
		height = 320;
	} else if (auditType == 2){
		title = "二级商户修改审核";
		if (state == 0){
			width = 618;
			height = 480;
		}
	} else if (auditType != 0){
		title = "二级商户启用/禁用审核";
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
 
//function fillBackAndCloseDialog(){
//	$("#dialog").omDialog('close');
//	$("#dialog-notPass").omDialog("close");
//}
//详情页面不通过
function goNotPass(id){
	var flag = true;
	var resultDesc = document.getElementById("resultDesc").value;
	if(resultDesc == ""){
		alert("请先输入审核意见！");
		flag = false;
	}
	if(byteCount(resultDesc)>64){
		alert("审核意见最大长度为64个字符！")
		flag = false;
	}
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
				try{
					window.parent.shuaxin();
					window.parent.showAuditTip(data);
				}catch(e){
					shuaxin();
					showAuditTip(data);
				}
			}
		});
	}
}

//弹出审核不通过对话框
function openNotPass(id){
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
    					try{
    						window.parent.shuaxin();
    						window.parent.showSimpleTip(data);
    					}catch(e){
    						shuaxin();
    						showSimpleTip(data);
    					}
    				}
    		});
        }
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

//获取字节长度
function byteCount(s){
	var c = 0;
	var a = s.split("");
	for (var i=0;i<a.length;i++) {
		c=(a[i].charCodeAt(0)<299)?(c+1):(c+2);
	}
	return c;
}