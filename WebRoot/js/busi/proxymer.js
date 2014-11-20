//多条件查询方法
function query(){
     
  var merId = jQuery('#merId').val();
  var merName = jQuery('#merName').val();
  var operator = jQuery('#operator').val();

  var key = "";
  if(merId != ""){ //没有查询条件，要显示全部数据
	  key = key + '&merId=' + encodeURI(merId);
  }
   if(merName != ""){ 
        key = key + '&merName=' + encodeURI(merName);
   }
   if(operator != "" && operator != "全部"){ 
   		key = key + '&operator=' + encodeURI(operator);
   }
   $('#grid').omGrid("setData", url+key);
   return false;
}