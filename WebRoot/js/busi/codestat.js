	  //当前页面弹出操作提示框
	  function showSimpleTip(result){
		     //result 结果为1表示成功，结果为0表示失败
		   /*  if(result=='1'){
		    	 $.omMessageBox.alert({
		    		type:'success',
		            title : '操作提示',
		            content : '操作成功！'
		        });
		        } */
		  
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
         var matchType=jQuery('#matchType').val();
         var category=jQuery('#category').val();
         var serviceId=jQuery('#serviceId').val();
         var detail=jQuery('#detail').val();
         var state=jQuery('#state').val();
     
         var key="";
        if(serviceId != ""){ //没要有查询条件，要显示全部数据
             key=key+'&serviceId='+encodeURI(serviceId);
         }
        if(category != "" && category!="全部"){ //没要有查询条件，要显示全部数据
            key=key+'&category='+encodeURI(category);
        }
        if(matchType != ""){ //没要有查询条件，要显示全部数据
            key=key+'&matchType='+encodeURI(matchType);
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
          $('#grid').omGrid("setData", url+key);
	 }
	
	
	//iframe窗口调用的关闭操作，返回值是dialog出口的id
	function fillBackAndCloseDialog(msg){
		//jQuery('#grid').omGrid('reload',1);
		$(msg).omDialog('close');
	}
	
	//导出计费代码使用查询方法
	function expFeeCode(){
		var feeType=jQuery('#feeType').val();
        var amount=jQuery('#amount').val();
        var matchType=jQuery('#matchType').val();
        var category=jQuery('#category').val();
        var serviceId=jQuery('#serviceId').val();
        var detail=jQuery('#detail').val();
        var state=jQuery('#state').val();
    
        var key="export.do?queryKey=code.FeeCodeStats";
       if(serviceId != ""){ //没要有查询条件，要显示全部数据
            key=key+'&serviceId='+encodeURI(serviceId);
        }
       if(category != "" && category!="全部"){ //没要有查询条件，要显示全部数据
           key=key+'&category='+encodeURI(category);
       }
       if(matchType != ""){ //没要有查询条件，要显示全部数据
           key=key+'&matchType='+encodeURI(matchType);
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
          window.location.href=key;
	}
	   
	     //删除空格
	      function trim(str) {
              if (str == null){
                  return "";
               }
              return str.replace(/^\s*(.*?)[\s\n]*$/g,'$1');   
            }
	      
	    //刷新页面
	      function shuaxin(){
	      	jQuery('#grid').omGrid('reload');
	      }