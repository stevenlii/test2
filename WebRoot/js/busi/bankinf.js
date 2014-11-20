	  //当前页面弹出操作提示框
	  function showSimpleTip(result){
		     if(result !='1'){
		        $.omMessageBox.alert({
		        	type:'error',
		            title : '操作提示',
		            content : '操作失败！'
		        });
		        }
		    }
	   //关闭窗口方法
	   function  closeDialog(){
	          window.parent.fillBackAndCloseDialog("#dialog"); 
	   } 
	//查询方法
	 function query(){
		 var bankId=jQuery('#bankId').val();
         var bankName=jQuery('#bankName').val();
         var bankType=jQuery('#bankType').val();
         var state=jQuery('#state').val();
         var key="";
        if(bankId != ""){ //没要有查询条件，要显示全部数据
             key=key+'&bankId='+encodeURI(bankId);
         }
         if(bankName !=""){ 
              key=key+'&bankName='+encodeURI(bankName);
            }
           if(state!=""){ 
            key=key+'&state='+encodeURI(state);
           }
           if(bankType!=""&&bankType!="全部"){ 
               key=key+'&bankType='+encodeURI(bankType);
              }
          $('#grid').omGrid("setData", url+key);
	 }
	//添加支付服务商方法
	function show(){
		 $( "#dialog").omDialog("option","width",'480');
	      $( "#dialog").omDialog("option","title",'添加支付服务商信息');
	      $( "#dialog").omDialog("option","height",'220');
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
		 $( "#dialog").omDialog("option","width",'480');
	      $( "#dialog").omDialog("option","title",'修改支付服务商信息');
	      $( "#dialog").omDialog("option","height",'220');
	      $( "#dialog").omDialog("option", "modal", true);
		 $( "#dialog").omDialog('open');
		var frameLoc=window.frames[0].location;
        frameLoc.href='modify.do?id='+id;
        $("#dialog").omDialog({onClose : function(event) {
      		window.frames[0].location.href='../wait.htm';
       }});
	}
	//iframe窗口调用的关闭操作，返回值是dialog出口的id
	function fillBackAndCloseDialog(msg){
		$(msg).omDialog('close');
	}
	function modify(state){
        showModifyDialog(state);//显示dialog
	}
	


	

 //批量启用方法
	 function batchstart(){
		 try{
	            var selections=$('#grid').omGrid('getSelections',true); 
                if (selections.length == 0) {
                    alert('请至少选择一行记录！');
                    return false;
                }
                if(selections[0].STATE!=4){
                    alert('请选择禁用状态的记录进行启用操作！');
                    return false;
                }
                
          //      if(selections[0].MODLOCK=='1'){
           //          alert("当前记录处于锁定状态，无法进行该操作！");
	       //          return false;
           //     }
                
                
                var check=0;//校验值 默认为0 表示正常
                var length= selections.length;
                var  ID="";
                for(i=0;i<length;i++){
                    ID=trim(selections[i].BANKID)+','+ID;
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
				     jQuery('#grid').omGrid('reload',1);
				  }
			     });
               // jQuery('#grid').omGrid('reload',1);
                }
		   }catch( e){
			 alert("没有数据！！！");
		   }
	     }
	      
//批量禁用方法
	 function batchstop(){
		 try{
	            var selections=$('#grid').omGrid('getSelections',true);   
                if (selections.length == 0) {
                    alert('请至少选择一行记录');
                    return false;
                }
                  if(selections[0].STATE!=2){
                      alert('请选择启用状态的记录进行禁用操作！');
                      return false;
                }
                  
              //   if(selections[0].MODLOCK=='1'){
	          //     alert("当前记录处于锁定状态，无法进行该操作！");
	          //      return false;
	          //   }
                
                
                var check=0;//校验值 默认为0 表示正常
                var length= selections.length;
                var  ID="";
                for(i=0;i<length;i++){
                   ID=trim(selections[i].BANKID)+','+ID;
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
				           jQuery('#grid').omGrid('reload',1);
				    }
			      });     
                 }
		 }catch(e){
			 alert("没有数据！！！");
		}
	  }
	     
	 
	 
	 
	 
	//批量变更状态方法
	 function change(){
		 alert("批量变更状态！");
	 }
	 
	 

      //单条开通
	      function singleEnable(bankId,state){  
	    	  var flag = confirm("确认启用该条记录？");
	      	  if (flag){
	              var ID=bankId+",";
	              if(state=='2'){
	                  alert("当前记录处于开通状态，无法进行该操作！");
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
					     jQuery('#grid').omGrid('reload',1);
				    }
			     });
            // jQuery('#grid').omGrid('reload',1);
	         }
	      	}
         }
	
     //单条关闭
	   function singleDisable(bankId,state){
		   var flag = confirm("确认禁用该条记录？");
	    	if (flag){
	            var ID=bankId+",";
	            if(state=='4'){
	                alert("当前记录处于关闭状态，无法进行该操作！");
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
					  jQuery('#grid').omGrid('reload',1);
					  jQuery('#grid').omGrid('reload',1);
					}
			  });
           //  jQuery('#grid').omGrid('reload',1);
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
	      function checkBankI33d(){
	         var bankId=$("input[name='bankId']").val();
	         var reg = new RegExp("^MW|XE[0-9]{6}$");
	         if(!reg.test(bankId)){
	        	 $("#error").show();
		         $("#error").html("请输入以MW开头+6位数字或者以XE开头+6位数字!");
               //  alert("请输入以MW开头+6位数字或者以XE开头+6位数字!");
                 return false;
           }
	         
	         if(bankId.length==0){
	         	$("#error").show();
	         	$("#error").html("请填写支付服务商编号！");
	         	return false;
	         }else {
	         	   if(bankId.length!=8){
	         		$("#error").show();
	             	$("#error").html("支付服务商编号为8位！");
	             	return false;
	         	     }
	         else { 
	     //  var merId=$('#merId').omCombo('value') ;
	          $.ajax({
	  			url : 'checkBankId.do',
	  			type : 'POST',
	  			dataType: 'text',
	  			data : {
	  					'bankId' : bankId
	  			},
	  			success : function(data){
	  			//	alert(data);
	  				if(data==0){
	  				$("#error").html("当前支付服务商编号已经存在");  
	  		//	    document.getElementById("merId").focus();
	  				}
	  				else  $("#error").hide();
	  				 return  false;
	  				}
	  			  });
	            }      
	         } 
	         return  true;
           }
 
	    //刷新页面
	      function shuaxin(){
	      	jQuery('#grid').omGrid('reload');
	      }