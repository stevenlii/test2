function beforeClick(treeId, treeNode) {
	var check = (treeNode && !treeNode.isParent);
	return check;
}

function onClick(e, treeId, treeNode) {
	var type="";
	if(treeId=="treeDemo2"){
		type="2";
	}
	v = treeNode.name;
	$("#category"+type).val(treeNode.id);
	while(true){
		treeNode = treeNode.getParentNode();
    	if(treeNode == null){
    		break;
    	}
    	v = treeNode.name+"-"+v;
    }
	$("#goodsCategory"+type).attr("value", v);
	hideMenu();
}

function showMenu(type) {
	var cityObj = $("#goodsCategory"+type);
	var cityOffset = $("#goodsCategory"+type).offset();
	$("#menuContent"+type).css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

	$("body").bind("mousedown", onBodyDown);
}
function refuseBackspace(){
	if(event.keyCode==8)
		event.keyCode=0;
	return false;
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("#menuContent2").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "goodsCategory" || event.target.id == "goodsCategory2" || 
			event.target.id == "menuContent" || event.target.id == "menuContent2" 
				|| $(event.target).parents("#menuContent").length>0 || 
				$(event.target).parents("#menuContent2").length>0)) {
		hideMenu();
	}
}

     function showSimpleTip(result){
     //result 结果为1表示成功，结果为0表示失败
    /* if(result=='1')
     {
    	 $.omMessageBox.alert({
            title : '操作提示',
            content : '操作成功！',
            type: 'success' 
        });
        } */
    	 if(result !='1')
        {
        $.omMessageBox.alert({
            title : '操作提示',
            content : '操作失败！',
            type : 'error' 
        });
        }
    }
     
   //刷新页面
     function shuaxin(){    	
     	jQuery('#grid').omGrid('reload');
     }


	      //增加方法
	function show(){
		  $( "#dialog").omDialog('open');
		   var frameLoc=window.frames[0].location;
           frameLoc.href='add.do';
           $("#dialog").omDialog({onClose : function(event) {
          		window.frames[0].location.href='../wait.htm';
           }});
		  return false;
	     }
	//导出方法
//	function expGoods(){
//	            var goodsId=jQuery('#goodsId').val();
//                var goodsName=jQuery('#goodsName').val();
//                var merId=jQuery('#merId').val();           
//                var goodsCategory=$('#goodsCategory').omCombo('value') ;
//                var key="export.do?queryKey=goods.exportGoodsRelated";
//                var servType=$('#servType').omCombo('value') ;
//                var priceMode=$('#priceMode').omCombo('value') ; 
//                var goodsCategory=$('#goodsCategory').omCombo('value') ;
//                var busiType=$('#busiType').omCombo('value') ; 
//                var pushInfAndmtNum=$('#pushInfAndmtNum').omCombo('value') ; 
//                var addWay=$('#addWay').omCombo('value') ; 
//                var state=$('#state').omCombo('value') ; 
//                if(goodsId != ""){ //没要有查询条件，要显示全部数据
//                    key=key+'&goodsId='+encodeURI(goodsId);
//                }
//                if(goodsName !="")
//                  { 
//                     key=key+'&goodsName='+encodeURI(goodsName);
//                   }
//                   if(servType !="")
//                  { 
//                     key=key+'&servType='+encodeURI(servType);
//                   }
//                     if(priceMode !="")
//                  { 
//                     key=key+'&priceMode='+encodeURI(priceMode);
//                   }
//                    if(goodsCategory !=""&goodsCategory !="全部"){
//                     key=key + '&cateGory='+encodeURI(goodsCategory);
//                    }
//                   if(busiType !="")
//                    {
//                     key=key+ '&busiType='+encodeURI(busiType);
//                    }
//                  if(pushInfAndmtNum == 01){
//                      key=key + '&pushInf=0&mtNum=1'               
//                     }
//                  if(pushInfAndmtNum == 11){
//                      key=key + '&pushInf=1&mtNum=1'               
//                     }
//                  if(pushInfAndmtNum == 12){
//                      key=key + '&pushInf=1&mtNum=2'               
//                     }	  
//                  if(addWay !="" ){
//                	  key=key + '&addWay='+addWay;
//                  }
//                  if(state !=""){
//                	  key=key + '&state='+state;
//                  }
//	         var ret=$('#resultSize').val();
//	         $('#form')[0].action=key;
//	 		 $('#form')[0].submit();
//	     }
     //查询方法
    function query(){
         var goodsId=jQuery('#goodsId').val();
                var merId=jQuery('#merId').val();
                var goodsName=jQuery('#goodsName').val();
                var servType=$('#servType').omCombo('value');
                var priceMode=$('#priceMode').omCombo('value'); 
                var goodsCategory=jQuery('#category').val();
                var busiType=$('#busiType').omCombo('value'); 
                var pushInfAndmtNum=$('#pushInfAndmtNum').omCombo('value'); 
                var addWay=$('#addWay').omCombo('value'); 
                var state=$('#state').omCombo('value'); 
                var key="?queryKey=goods.allGoods";
                //$('#grid').omGrid("setData", url);
               if(merId != ""){
                    key= key +'&merId='+encodeURI(merId);
                   }
               if(goodsId != ""){ //没要有查询条件，要显示全部数据
                   
                    key=key+'&goodsId='+encodeURI(goodsId);
                }
                if(goodsName !="")
                  { 
                     key=key+'&goodsName='+encodeURI(goodsName);
                   }
                   if(servType !="")
                  { 
                     key=key+'&servType='+encodeURI(servType);
                   }
                     if(priceMode !="")
                  { 
                     key=key+'&priceMode='+encodeURI(priceMode);
                   }
                    if(goodsCategory !=""&goodsCategory !="全部"){
                     key=key + '&cateGory='+encodeURI(goodsCategory);
                    }
                   if(busiType !="")
                    {
                     key=key+ '&busiType='+encodeURI(busiType);
                    }
                  if(pushInfAndmtNum == 01){
                      key=key + '&pushInf=0&mtNum=1'               
                     }
                  if(pushInfAndmtNum == 11){
                      key=key + '&pushInf=1&mtNum=1'               
                     }
                  if(pushInfAndmtNum == 12){
                      key=key + '&pushInf=1&mtNum=2'               
                  }
                  if(addWay !="" ){
                	  key=key + '&addWay='+addWay;
                  }
                  if(state !=""){
                	  key=key + '&state='+state;
                  }
                 $('#grid').omGrid("setData", url+key);
        }
     
	//显示修改方法
	function showModifyDialog(mer,goods,servType){
		var mer=""+mer+"";
		merId=mer.substring(1,mer.length);
	  var l=""+goods+"";
	      goodsId=l.substring(1,l.length);
	      $( "#dialog").omDialog("option","width",'750');
	      $( "#dialog").omDialog("option","title",'修改商品信息');
	      if(servType == 2){
	    	  var height = '440';
	      }else{
	    	  var height = '520';
	      }
	      $( "#dialog").omDialog("option","height",height);
	      $( "#dialog").omDialog("option", "modal", true);
	      $( "#dialog").omDialog('open');
		   var frameLoc=window.frames[0].location;
           frameLoc.href='modifyGoods.do?merId='+merId+'&goodsId='+goodsId;
           $("#dialog").omDialog({onClose : function(event) {
          		window.frames[0].location.href='../wait.htm';
           }});
		  return false;
	   }
	//显示详情方法
	function showDetailDialog(mer,goods,servType){          
		var mer=""+mer+"";
		merId=mer.substring(1,mer.length);
	  var l=""+goods+"";
	      goodsId=l.substring(1,l.length);
	      var height = "420";
	      if(servType==3){
	    	  height = "475";
	      }
	      $( "#dialog").omDialog("option","width",'500');
	      $( "#dialog").omDialog("option","title",'显示商品信息');
	      $( "#dialog").omDialog("option","height",height);
	      $( "#dialog").omDialog("option", "modal", true);
		$( "#dialog").omDialog('open');
		   var frameLoc=window.frames[0].location;
           frameLoc.href='detail.do?merId='+merId+'&goodsId='+goodsId;
           $("#dialog").omDialog({onClose : function(event) {
           		window.frames[0].location.href='../wait.htm';
           }});
		  return false;
	}
	   
	    //iframe窗口调用的关闭操作，返回值是dialog出口的id 例如详情dialog 则为#dialog-detail
	function fillBackAndCloseDialog(msg){		
	//	jQuery('#grid').omGrid('reload',1);
		$( msg).omDialog('close');
	}
	  //批量启用方法
	 function batchstart(){
		 try{
	      var selections=$('#grid').omGrid('getSelections',true);     
		 }catch(e){
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
                for(j=0;j<length;j++){
                	if(selections[j].MODLOCK == 1){
                		alert("当前记录已被锁定，无法进行操作");
                		return false;}
                }
                for(i=0;i<length;i++)
                {
                ID=trim(selections[i].MERID)+'-'+trim(selections[i].GOODSID)+','+ID;
                if (selections[i].STATE!=selections[0].STATE)
                 check=1;
                }
             
               if(check==1)
              { 
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
            // jQuery('#grid').omGrid('reload',1);
                }
		
	 }
	      //批量禁用方法
	 function batchstop(){
		 try{
	      var selections=$('#grid').omGrid('getSelections',true);     
		 }catch(e){
			 alert("请至少选择一行记录");
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
                for(var j=0;j<length;j++){
                	if(selections[j].MODLOCK == 1){
                		alert("当前记录已被锁定，无法进行操作");
                		return false;
                	}
                }
                for(var i=0;i<length;i++)
                {
                ID=trim(selections[i].MERID)+'-'+trim(selections[i].GOODSID)+','+ID;
                if (selections[i].STATE!=selections[0].STATE)
                 check=1;
                }
               
               if(check==1)
              { 
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
           //  jQuery('#grid').omGrid('reload',1);
                }
		 
	   }
	 	          //单条启用
	 	function singleEnable(mer,goods,modLock){    
	 		var flag = confirm("确认启用该条记录？");
	    	if (flag){
	 		var mer=""+mer+"";
			merId=mer.substring(1,mer.length);
	 	  var l=""+goods+"";
	      goodsId=l.substring(1,l.length);
	      var ID=merId+"-"+goodsId+",";
	      if(modLock=='1')
	      {
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
	      function singleDisable(mer,goods,modLock){
	    	  var flag = confirm("确认禁用该条记录？");
	      	if (flag){
	    	  var mer=""+mer+"";
	  		merId=mer.substring(1,mer.length);
	      var l=""+goods+"";
	      goodsId=l.substring(1,l.length);
	   
	      var ID=merId+"-"+goodsId+",";
	         if(modLock=='1'){
	          alert("当前记录处于锁定状态，无法进行该操作！");
	           return false;
	         }
	         else
	         {
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
           //  jQuery('#grid').omGrid('reload',1);
	      } 
	      	}
	  }    
	  //删除空格
	  function trim(str) {
         if (str == null) {
            return "";
         }
         return str.replace(/^\s*(.*?)[\s\n]*$/g,'$1');   
      }
      
    
	
	function batchMod(){
		$('.errorImg').css('display','none');
		$('.errorImg').next().css('display','none');
		try{
		var selections=$('#grid').omGrid('getSelections',true);       
		if(selections.length == 0){
			alert('请至少选择一行记录');
			return false;
		}
		var length= selections.length;
		var goodsView="",ID="";
		for(i=0;i<length;i++){
			goodsView+=trim(selections[i].MERID)+'-'+trim(selections[i].GOODSID)+' '+trim(selections[i].GOODSNAME)+'<br>';
			ID+=trim(selections[i].MERID)+'-'+trim(selections[i].GOODSID)+',';
		}
		$( "#dialog-batchMod").omDialog('open');
		$('#goodsView').html(goodsView);
		$('#ID').val(ID);
		$('#cusPhone').val('');
		}catch(e){
			 alert("没有数据！！！");
		}
	}
	function close(){
		$("#dialog-batchMod").omDialog('close');
	}

	
	//修改方法  js
	
	//隐藏框
    function display(servMonth,interval,conMode){ 
      var traget=document.getElementById(servMonth);
               traget.style.display="none";
       var traget=document.getElementById(interval); 
               traget.style.display="none";
       var traget=document.getElementById(conMode);
               traget.style.display="none";       
       }
       //显示框
     function showTr(servMonth,interval,conMode){         
      var traget=document.getElementById(servMonth);   
               traget.style.display="";  
       var traget=document.getElementById(interval);
                traget.style.display="";
       var traget=document.getElementById(conMode);
               traget.style.display="";
            }
     function isNotEmpty(v){
	    if(!v){
	   	     return false;
	           }
	    if(v == ''){
		      return false;
	          }
	    return true;
        }
	
	
	
	//新增方法js
	
     
  /*
    //检查当前商户下的商品号是否已经存在
    function checkGoodsId(){
       var goodsId=$("input[name='goodsId']").val();
       var servType=$('input:radio[name="servType"]:checked').val();     
      
       var merId=$('#merId').omCombo('value') ;
       var reg = new RegExp("^[0-9]*$");
       if(!reg.test(goodsId)){
    	   $("#error").show();
       	$("#error").html("商品号码应为数字！");
    	   return false;
       }
        if(goodsId.length==0){
        	$("#error").show();
        	$("#error").html("请填写商品号码！");
        	return false;
        }
        else {
        	if(servType==2){
        	   if(goodsId.length!=3){
        		$("#error").show();
            	$("#error").html("按次商品号码应该为3位数字！");
            	return false;
        	     }
        	   else {
                   $.ajax({
    			          url : 'checkgoodsid.do',
    			          type : 'POST',
    			          dataType: 'text',
    			          data : {
    					    'merId' : merId,
    					    'goodsId' : goodsId
    			                  },
    			         async : true ,
    			         success : function(data){
    				             if(data==0){
    				         $("#error").html("当前商户下的商品号已经存在");  			
    			         //   document.getElementById("goodsId").focus();
    				         return false;
    			                   }
    			                	else  $("#error").hide();	 
    			               }
    			           });
                }
        	}
        	else if(servType==3) {
        		  if(goodsId.length!=6){
              		$("#error2").show();
                  	$("#error2").html("包月商品号码应该为6位数字！");
                  	return false;
        	} 
        		  else {
                      $.ajax({
       			          url : 'checkgoodsid.do',
       			          type : 'POST',
       			          dataType: 'text',
       			          data : {
       					    'merId' : merId,
       					    'goodsId' : goodsId
       			                  },
       			         async : true ,
       			         success : function(data){
       				             if(data==0){
       				         $("#error").html("当前商户下的商品号已经存在");  			
       			         //   document.getElementById("goodsId").focus();
       				         return false;
       			                   }
       			                	else  $("#error").hide();	 
       			               }
       			           });
                   }
        }
        else {
        	$("#error").show();
          	$("#error").html("请先选择商品服务类型！");
          	return false;
            }
         }
        return true;
      }
   
    
    //检查包月商品号
    function checkBYGoodsId(){
       var goodsId=$("input[name='goodsId2']").val();
       var servType=$('input:radio[name="servType2"]:checked').val();     
      
       var merId=$('#merId2').omCombo('value') ;
       var reg = new RegExp("^[0-9]*$");
       if(!reg.test(goodsId)){
    	   $("#error2").show();
       	$("#error2").html("商品号码应为数字！");
    	   return false;
       }
        if(goodsId=='undefine' || goodsId.length==0){
        	$("#error2").show();
        	$("#error2").html("请填写商品号码！");
        	return false;
        }
        else {
         if(servType==3) {
        		  if(goodsId.length!=6){
              		$("#error2").show();
                  	$("#error2").html("包月商品号码应该为6位数字！");
                  	return false;
        	   } 
        		  else {
                      $.ajax({
       			          url : 'checkgoodsid.do',
       			          type : 'POST',
       			          dataType: 'text',
       			          data : {
       					    'merId' : merId,
       					    'goodsId' : goodsId
       			                  },
       			         success : function(data){
       				             if(data==0){
       				         $("#error2").html("当前商户下的商品号已经存在");  			
       			         //   document.getElementById("goodsId").focus();
       				         return false;
       			                   }
       			                	else  $("#error2").hide();	 
       			               }
       			           });
                   }
        }
        else {
        	$("#error2").show();
          	$("#error2").html("请先选择商品服务类型！");
          	return false;
            }
         }
        return true;
      }
  */
     //校验按次商品的商品号是否存在
     function checkACGoods(goodsId){
  	  // var merId=$('#merId').omCombo('value') ; 正常获取不到值
  	   var merId=document.getElementById("merId").value;
  	   var res=true;
         $.ajax({
  	          url : 'checkgoodsid.do',
  	          type : 'POST',
  	          dataType: 'text',
  	          data : {
  			    'merId' : merId,
  			    'goodsId' : goodsId
  	                  },
  	         async : false ,
  	         success : function(data){
  	                	 res=data;
  	               }
  	           });
         if(res =='1') {
         	   return true;
            }
            else return false;
     }
     //校验包月商品的商品号是否存在
     function checkBYGoods(goodsId){
  	 //  var merId=$('#merId2').omCombo('value') ;  正常获取不到值
  	   var merId=document.getElementById("merId2").value;
  	   var res;
         $.ajax({
  	          url : 'checkgoodsid.do',
  	          type : 'POST',
  	          dataType: 'text',
  	          data : {
  			    'merId' : merId,
  			    'goodsId' : goodsId
  	                  },
  	         async : false ,   
  	         success : function(data){
  	                	  res=data;
  	                  }
  	           });
         if(res =='1') {
       	   return true;
          }
          else return false;
     }
    //验证商品定价为非负整数
    function checkAmount(){
  	   var  priceMode=$('input:radio[name="priceMode"]:checked').val();
 	 	   var res= $('input[id="busiType1"]').is(':checked');  //全网是否被选中 true 选中 false 未选中
 	 	   if(res){
 	 		 if(priceMode != 0){
 	 			 alert( "全网商品必须为定价模式!"); 
 	    	      return false;
 	 		 }
 	 		 var amount=  $('input[id="amount"]').val();
 	 	
 	 		 var   type="^[0-9]*[1-9][0-9]*$"; 
 	    	  var   re   =   new   RegExp(type); 
 	    	  if(amount.match(re)==null) 
 	    	     { 
 	    	      alert( "价格模式中请输入大于零的整数!"); 
 	    	      return false;
 	    	     }
 	 	 }
  	 return true;
  	 }
    
    //验证包月商品定价为非负整数
    function checkBYAmount(){
  	   var  priceMode=$('input:radio[name="priceModeBY"]:checked').val();
 	 	   var res= $('input[id="busiTypeBY"]').is(':checked');  //全网是否被选中 true 选中 false 未选中
 	 	   if(res){
 	 		 if(priceMode != 0){
 	 			 alert( "全网商品必须为定价模式!"); 
 	    	      return false;
 	 		 }
 	 		 var amount=  $('input[id="amountBY"]').val();
 	 	
 	 		 var   type="^[0-9]*[1-9][0-9]*$"; 
 	    	  var   re   =   new   RegExp(type); 
 	    	  if(amount.match(re)==null) 
 	    	     { 
 	    	      alert( "价格模式中请输入大于零的整数!"); 
 	    	      return false;
 	    	     }
 	 	 }
  	 return true;
  	 }
//根据商品信息动态判断商品描述是否为必填项
function showStar(id){
	$("#"+id).show();
}
function hideStar(id){
	$("#img"+id).hide();
	$("#msg"+id).hide();
	$("#"+id).hide();
}
function getPushInfValue(){
	return $("input[name='pushInf']:checked").val();
}
