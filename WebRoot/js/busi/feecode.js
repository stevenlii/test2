   //查询方法
    function query(){
    	 var url ="querycode.do";
      //   var bankId=jQuery("#bankId  option:selected").val();
        
      //   var feetype=jQuery("#feetype option:selected").val();
         var feetype=$('#feetype').omCombo('value') ;
         var category=$('#category').omCombo('value') ;
         var amount=$('#amount').val() ;
         var serviceid=$('#serviceid').val();
         var detail=$('#detail').val();
         var key="?queryKey=code.feecode";
                
               if(feetype != "" & feetype !="全部"){
                    key= key +'&feetype='+encodeURI(feetype);
                   }
               if(category != "" & category !="全部"){
                   key= key +'&category='+encodeURI(category);
                  }
               if(amount != ""){ //没要有查询条件，要显示全部数据
                   
                    key=key+'&amount='+encodeURI(amount);
                }
               if(serviceid != ""){ //没要有查询条件，要显示全部数据
                   
                   key=key+'&serviceid='+encodeURI(serviceid);
               }
               if(detail != ""){ //没要有查询条件，要显示全部数据
                   
                   key=key+'&detail='+encodeURI(detail);
               }
              // alert(url+key);
                 $('#grid').omGrid("setData", url+key);
        }
        
	//关闭窗口操作
	function  closeDialog(){
			window.parent.fillBackAndCloseDialog();
   }
	function fillBackAndCloseDialog(){
		$("#dialog").omDialog('close');
	}
	  function shuaxin(){
	      	jQuery('#grid').omGrid('reload');
	      }