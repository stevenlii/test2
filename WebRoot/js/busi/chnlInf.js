	  //当前页面弹出操作提示框
	  function showSimpleTip(result){
		  if(result !='1') {
		  		var msg = '操作失败';
		  		if(result != '0'){
		  			msg = result;
		  		}
		        $.omMessageBox.alert({
		        	type:'error',
		            title : '操作提示',
		            content : msg
		        });
		   }
		  }

	//查询方法
	 function query(){
		 var channelId=jQuery('#channelId').val();
         var channelName=jQuery('#channelName').val();
         var service_user=jQuery('#service_user').omCombo('value');
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
          if(service_user!="" && service_user != "全部"){ 
             key=key+'&service_user='+encodeURI(service_user);
           }
           if(state!=""){ 
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
	
	
	//增加方法
	function toAdd(){
		 $( "#dialog").omDialog("option","width",'470');
	      $( "#dialog").omDialog("option","title",'添加渠道信息');
	      $( "#dialog").omDialog("option","height",'275');
	      $( "#dialog").omDialog("option", "modal", true);
		 $( "#dialog").omDialog('open');
		   var frameLoc=window.frames[0].location;
           frameLoc.href='add.do';
           $("#dialog").omDialog({onClose : function(event) {
          		window.frames[0].location.href='../wait.htm';
           }});
	}
	
	//修改方法
	function showModifyDialog(id){
		 $( "#dialog").omDialog("option","width",'500');
	      $( "#dialog").omDialog("option","title",'修改渠道信息');
	      $( "#dialog").omDialog("option","height",'275');
	      $( "#dialog").omDialog("option", "modal", true);
		 $( "#dialog").omDialog('open');
		var frameLoc=window.frames[0].location;
        frameLoc.href='mod.do?id='+id;
        $("#dialog").omDialog({onClose : function(event) {
       		window.frames[0].location.href='../wait.htm';
        }});
	}
	//配置渠道的通知地址、下单地址和证书
	function confChnl(id){
		 $( "#dialog").omDialog("option","width",'500');
	      $( "#dialog").omDialog("option","title",'配置渠道信息');
	      $( "#dialog").omDialog("option","height",'275');
	      $( "#dialog").omDialog("option", "modal", true);
		 $( "#dialog").omDialog('open');
		var frameLoc=window.frames[0].location;
        frameLoc.href='conf.do?id='+id;
        $("#dialog").omDialog({onClose : function(event) {
       		window.frames[0].location.href='../wait.htm';
        }});
	}
	
	//iframe窗口调用的关闭操作，返回值是dialog出口的id
	function fillBackAndCloseDialog(msg){
		$(msg).omDialog('close');
	}
	function closeDialog(msg){
		$("#"+msg).omDialog('close');
	}
	
	function modify(id){
        showModifyDialog(id);//显示dialog
	}
	
	//显示详情方法
	function showDetail(id){
		  $( "#dialog").omDialog("option","width",'350');
	      $( "#dialog").omDialog("option","title",'查看渠道信息');
	      $( "#dialog").omDialog("option","height",'255');
	      $( "#dialog").omDialog("option", "modal", true);
		  $( "#dialog").omDialog('open');
		  var frameLoc=window.frames[0].location;
          frameLoc.href='detail.do?id='+id ;
          $("#dialog").omDialog({onClose : function(event) {
         		window.frames[0].location.href='../wait.htm';
          }});
	}
	
	//导出方法
	function expChnl(){
		var channelId=jQuery('#channelId').val();
        var channelName=jQuery('#channelName').val();
        var service_user=jQuery('#service_user').omCombo('value');
        var state=jQuery('#state').val();
        var from=jQuery('#from').val();
  	  	 var to=jQuery('#to').val();
  	  	var key="export.do?queryKey=chnls.allChnls";
       if(channelId != ""){ //没要有查询条件，要显示全部数据
            key=key+'&channelId='+encodeURI(channelId);
        }
        if(channelName !=""){ 
             key=key+'&channelName='+encodeURI(channelName);
           }
         if(service_user!="" && service_user != "全部"){ 
            key=key+'&service_user='+encodeURI(service_user);
          }
          if(state!=""){ 
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
            if(selections[0].MODLOCK=='1'){
                 alert("当前记录处于锁定状态，无法进行该操作！");
                 return false;
            }
            if(selections[0].STATE!=4){
                alert('请选择禁用状态的记录进行启用操作！');
                return false;
            }
            var check=0;//校验值 默认为0 表示正常
            var length= selections.length;
            var  ID="";
            for(i=0;i<length;i++){
            ID=trim(selections[i].CHANNELID)+','+ID;
            if (selections[i].STATE!=selections[0].STATE)
             check=1;
            }
           if(check==1){ 
                  alert("请选择相同的状态类型进行操作");
                  return false;
              } 
            else{
               $.ajax({
		       url : 'enable.do',
		       type : 'POST',
		       dataType: 'text',
		       data : {
				'ID' : ID
		       },
		       success : function(data){
			     showSimpleTip(data);  
			     jQuery('#grid').omGrid('reload');
				 }
			   });
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
            if(selections[0].MODLOCK=='1'){
               alert("当前记录处于锁定状态，无法进行该操作！");
               return false;
            }
            if(selections[0].STATE!=2){
                alert('请选择启用状态的记录进行禁用操作！');
                return false;
            }
            var check=0;//校验值 默认为0 表示正常
            var length= selections.length;
            var  ID="";
            for(i=0;i<length;i++){
            ID=trim(selections[i].CHANNELID)+','+ID;
            if (selections[i].STATE!=selections[0].STATE)
             check=1;
            }
           if(check==1){ 
              alert("请选择相同的状态类型进行操作");
              return false;
           } 
           else{
               $.ajax({
		         url : 'disable.do',
		         type : 'POST',
		         dataType: 'text',
		         data : {
				       'ID' : ID
		             },
		         success : function(data){
			           showSimpleTip(data);  
			           jQuery('#grid').omGrid('reload');
			    }
		      });     
             }
	  }
	      
      //单条启用
	      function singleEnable(channelId,modLock){  
	    	  var flag = confirm("确认启用该条记录？");
	      	  if (flag){
	              var ID=channelId+",";
	              if(modLock=='1'){
	                  alert("当前记录处于锁定状态，无法进行该操作！");
	                  return false;
	                }
	             else {
	               $.ajax({
				       url : 'enable.do',
				       type : 'POST',
				       dataType: 'text',
				       data : {
						 'ID' : ID
				        },
				       success : function(data){
					     showSimpleTip(data);  
					     jQuery('#grid').omGrid('reload');
				    }
			     });
            // jQuery('#grid').omGrid('reload',1);
	         }
	      	}
         }
	
//单条禁用
   function singleDisable(channelId,modLock){
	   var flag = confirm("确认禁用该条记录？");
    	if (flag){
            var ID=channelId+",";
            if(modLock=='1'){
                alert("当前记录处于锁定状态，无法进行该操作！");
                return false;
             }
           else{
             $.ajax({
			    url : 'disable.do',
			    type : 'POST',
			    dataType: 'text',
			    data : {
					'ID' : ID
			    },
			    success : function(data){
				  showSimpleTip(data);  
				  jQuery('#grid').omGrid('reload');
				}
		  });
          }
    	 }
   }
   
 //删除空格
  function trim(str) {
      if (str == null){
          return "";
       }
      return str.replace(/^\s*(.*?)[\s\n]*$/g,'$1');   
    }
  
  //检查当前渠道编号是否已经存在
  function checkChnlId(channelId){
  	 var res;
     $.ajax({
  		url : 'checkChnlId.do',
  		type : 'POST',
  		dataType: 'text',
  		async:false, //同步提交
  		data : {
  				'channelId' : channelId
  		},
	 	success : function(data){
	  		res = data;
  		}
	 });  
     if(res == 1){
    	 return true;
     }else {
    	 return false;
     }
  }
  
  //检查当前渠道名称是否已经存在
  function checkChnlName(channelName){
  	 var res;
     $.ajax({
  		url : 'checkChnlName.do',
  		type : 'POST',
  		dataType: 'text',
  		async:false, //同步提交
  		data : {
  				'channelName' : channelName
  		},
	 	success : function(data){
	  		res = data;
  		}
	 });  
     if(res == 1){
    	 return true;
     }else {
    	 return false;
     }
  }
	      
//刷新页面
function shuaxin(){
	jQuery('#grid').omGrid('reload');
}
