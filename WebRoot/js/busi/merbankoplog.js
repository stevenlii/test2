//多条件查询方法
function query(){
  var merId = jQuery('#merId').val();
  var bankId = jQuery('#bankId').val();
  var creator = jQuery('#creator').val();
  var submitstart = jQuery('#submitstart').val();
  var submitend = jQuery('#submitend').val();
  var key = "?queryKey=merbankoplog.allmerbankoplogs";
   if(bankId == "全部"){
	   bankId = "";
   }
   if(merId != "" || bankId != ""){
   		ixData = merId + "-" + bankId;
        key = key + '&ixData=' + encodeURI(ixData);
   }
   if(creator != "" && creator != "全部"){ 
   		key = key + '&creator=' + encodeURI(creator);
   }
   if(submitstart != ""){ 
   		key = key + '&submitstart=' + submitstart;
   }
   if(submitend != ""){ 
  		key = key + '&submitend=' + submitend;
  }
   var requesturl = url+key;
   jQuery('#grid').omGrid("setData", requesturl);
} 

function doexport(){
  var merId = jQuery('#merId').val();
  var bankId = jQuery('#bankId').val();
  var creator = jQuery('#creator').val();
  var submitstart = jQuery('#submitstart').val();
  var submitend = jQuery('#submitend').val();
  var key = "?queryKey=merbankoplog.allmerbankoplogs";
   if(bankId == "全部"){
	   bankId = "";
   }
   if(merId != "" || bankId != ""){
   		ixData = merId + "-" + bankId;
        key = key + '&ixData=' + encodeURI(ixData);
   }
   if(creator != "" && creator != "全部"){ 
   		key = key + '&creator=' + encodeURI(creator);
   }
   if(submitstart != ""){ 
   		key = key + '&submitstart='+ submitstart;
   }
   if(submitend != ""){ 
  		key = key + '&submitend=' + submitend;
  }
   var requesturl = exporturl+key;
   jQuery('#form')[0].action= exporturl + key;
   jQuery('#form')[0].submit();
} 
