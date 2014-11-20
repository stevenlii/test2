	  //当前页面弹出操作提示框
	  function showSimpleTip(result){
		  if(result !='1') {
	        $.omMessageBox.alert({
	        	type:'error',
	            title : '操作提示',
	            content : '操作失败！'
	        });
		  }
	  }

	//查询方法
	 function query(){
		 var feeType=jQuery('#feeType').val();
         var amount=jQuery('#amount').val();
         var category=$('#category').omCombo('value') ;
         var serviceId=jQuery('#serviceId').val();
         var detail=jQuery('#detail').val();
         var state=jQuery('#state').val();
     
         var key="";
        if(serviceId != ""){ //没要有查询条件，要显示全部数据
             key=key+'&serviceId='+encodeURI(serviceId);
         }
         if(feeType !=""){ 
              key=key+'&feeType='+encodeURI(feeType);
            }
          if(amount!=""){ 
            key=key+'&amount='+encodeURI(amount);
           }
          if(detail!=""){ 
             key=key+'&detail='+encodeURI(detail);
           }
           if(state!=""){ 
            key=key+'&state='+encodeURI(state);
           }
           if(category!="" && category!="全部"){ 
               key=key+'&category='+encodeURI(category);
              }
         
          $('#grid').omGrid("setData", url+key);
	 }
	
	
	//添加计费代码方法
	function addCode(){
		 $( "#dialog").omDialog("option","width",'450');
	      $( "#dialog").omDialog("option","title",'添加计费代码');
	      $( "#dialog").omDialog("option","height",'280');
	      $( "#dialog").omDialog("option", "modal", true);
		 $( "#dialog").omDialog('open');
		   var frameLoc=window.frames[0].location;
           frameLoc.href='addFeeCode.do';
           $("#dialog").omDialog({onClose : function(event) {
          		window.frames[0].location.href='../wait.htm';
           }});
	}
	
	//修改方法
	function showModifyDialog(id){
		 $( "#dialog").omDialog("option","width",'450');
	      $( "#dialog").omDialog("option","title",'修改计费代码');
	      $( "#dialog").omDialog("option","height",'280');
	      $( "#dialog").omDialog("option", "modal", true);
		 $( "#dialog").omDialog('open');
		var frameLoc=window.frames[0].location;
        frameLoc.href='modify.do?id='+id;
        $("#dialog").omDialog({onClose : function(event) {
       		window.frames[0].location.href='../wait.htm';
        }});
	}
	
	//打开操作记录窗口
	function showDetailDialog(serviceId){
		 $( "#dialog").omDialog("option","width",'650');
	      $( "#dialog").omDialog("option","title",'操作记录');
	      $( "#dialog").omDialog("option","height",'400');
	      $( "#dialog").omDialog("option", "modal", true);
		 $( "#dialog").omDialog('open');
		var frameLoc=window.frames[0].location;
        frameLoc.href='opendetail.do?serviceId='+serviceId;
        $("#dialog").omDialog({onClose : function(event) {
       		window.frames[0].location.href='../wait.htm';
        }});
	}
	
	//关闭窗口方法
	function closeDialog(){
		try{
			window.parent.fillBackAndCloseDialog();
		}catch(e){
			fillBackAndCloseDialog();
		} 
	}	
	//iframe窗口调用的关闭操作
	function fillBackAndCloseDialog(){
		$("#dialog").omDialog("close");
		$("#dialog-upload").omDialog("close");
	}
	
	function modify(state){
        showModifyDialog(state);//显示dialog
	}
	
	//导出计费代码方法
	function expFeeCode(){
        var serviceId=jQuery('#serviceId').val();
        var feeType=jQuery('#feeType').omCombo('value');
        var amount=jQuery('#amount').val();
        var category=jQuery('#category').omCombo('value');
        var detail=jQuery('#detail').val();
        var state=jQuery('#state').omCombo('value');
        
        var key="export.do?queryKey=code.allCodeMngs";
        if(serviceId != ""){ //没要有查询条件，要显示全部数据
            key=key+'&serviceId='+encodeURI(serviceId);
        }
        if(feeType !=""){ 
             key=key+'&feeType='+encodeURI(feeType);
           }
         if(amount!=""){ 
           key=key+'&amount='+encodeURI(amount);
          }
         if(detail!=""){ 
            key=key+'&detail='+encodeURI(detail);
          }
          if(state!=""){ 
           key=key+'&state='+encodeURI(state);
          }
          if(category!="" && category!="全部"){ 
              key=key+'&category='+encodeURI(category);
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
                if(selections[0].STATE!=4){
                    alert('请选择禁用状态的记录进行启用操作！');
                    return false;
                }
                var check=0;//校验值 默认为0 表示正常
                var length= selections.length;
                var  ID="";
                for(i=0;i<length;i++){
                	if (selections[i].STATE!=selections[0].STATE){
                		check=1;
                		break;
                	}
	                ID=trim(selections[i].SERVICEID)+','+ID;
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
              if(selections[0].STATE!=2){
                  alert('请选择启用状态的记录进行禁用操作！');
                  return false;
            }
            var check=0;//校验值 默认为0 表示正常
            var length= selections.length;
            var  ID="";
            for(i=0;i<length;i++){
	            ID=trim(selections[i].SERVICEID)+','+ID;
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
	      function singleEnable(serviceId,state){  
	    	  var flag = confirm("确认启用该条记录？");
	      	  if (flag){
	              var ID=serviceId+",";
	              if(state=='2'){
	                  alert("当前记录处于启用状态，无法进行该操作！");
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
	         }
	      	}
         }
	
     //单条禁用
	   function singleDisable(serviceId,state){
		   var flag = confirm("确认禁用该条记录？");
	    	if (flag){
	            var ID=serviceId+",";
	            if(state=='4'){
	                alert("当前记录处于禁用状态，无法进行该操作！");
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
	      
	      //检查当前商户下的商户号是否已经存在
	      function checkServiceId(value){
	         var serviceId=value;
	         var res = true;
         	   $.ajax({
         		   url : 'checkServiceId.do',
         		   type : 'POST',
         		   dataType: 'text',
         		   async:false,
         		   data : {
         		   		'serviceId' : serviceId
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