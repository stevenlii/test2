//多条件查询方法
function query(){
  var orderId = $('#orderId').val();
  var merId = $('#merId').val();
  var subMerId = $('#subMerId').val();
  var goodsId = $('#goodsId').val();
  var mobileId = $('#mobileId').val();
  var category = $('#category').val();
  var businessType = $('#businessType').val();
  var from = $('#from').val();
  var to = $('#to').val();
  var orderState = $('#orderState').val();

  var key = "";
  if(orderId != ""){ //没有查询条件，要显示全部数据
	  key = key + '&orderId=' + encodeURI(orderId);
  }
  if(merId != ""){ //没有查询条件，要显示全部数据
	  key = key + '&merId=' + encodeURI(merId);
  }
  if(subMerId != ""){ //没有查询条件，要显示全部数据
	  key = key + '&subMerId=' + encodeURI(subMerId);
  }
  if(goodsId != ""){ 
      key = key + '&goodsId=' + encodeURI(goodsId);
  }
  if(mobileId != ""){ 
      key = key + '&mobileId=' + encodeURI(mobileId);
  }
  if(category != "" && category != "全部"){ 
	  key = key + '&category=' + encodeURI(category);
  }
  if(businessType != "" && businessType != "全部"){ 
	  key = key + '&businessType=' + encodeURI(businessType);
  }
  if(from != "" && to == ""){
	  alert("请填写交易时间的结束时间");
	  return false;
  }else if(from == "" && to != ""){
	  alert("请填写交易时间的开始时间");
	  return false;
  }else if(from != "" && to != ""){ 
	  key = key + '&from=' + encodeURI(from);
	  key = key + '&to=' + encodeURI(to);
  }
  if(orderState != ""){
      key = key + '&orderState=' + encodeURI(orderState);
  }
  $('#grid').omGrid("setData", url+key);
  return false;
}
//导出
function expProxyOrder(){
  var orderId = $('#orderId').val();
  var merId = $('#merId').val();
  var subMerId = $('#subMerId').val();
  var goodsId = $('#goodsId').val();
  var mobileId = $('#mobileId').val();
  var category = $('#category').val();
  var businessType = $('#businessType').val();
  var from = $('#from').val();
  var to = $('#to').val();
  var orderState = $('#orderState').val();

  var key = "export.do?queryKey=proxyorder.all";
  if(orderId != ""){ //没有查询条件，要显示全部数据
	  key = key + '&orderId=' + encodeURI(orderId);
  }
  if(merId != ""){ //没有查询条件，要显示全部数据
	  key = key + '&merId=' + encodeURI(merId);
  }
  if(subMerId != ""){ //没有查询条件，要显示全部数据
	  key = key + '&subMerId=' + encodeURI(subMerId);
  }
  if(goodsId != ""){ 
      key = key + '&goodsId=' + encodeURI(goodsId);
  }
  if(mobileId != ""){ 
      key = key + '&mobileId=' + encodeURI(mobileId);
  }
  if(category != "" && category != "全部"){ 
	  key = key + '&category=' + encodeURI(category);
  }
  if(businessType != "" && businessType != "全部"){ 
	  key = key + '&businessType=' + encodeURI(businessType);
  }
  if(from != "" && to == ""){
	  alert("请填写交易时间的结束时间");
	  return false;
  }else if(from == "" && to != ""){
	  alert("请填写交易时间的开始时间");
	  return false;
  }else if(from != "" && to != ""){ 
	  key = key + '&from=' + encodeURI(from);
	  key = key + '&to=' + encodeURI(to);
  }
  if(orderState != ""){
      key = key + '&orderState=' + encodeURI(orderState);
  }
  window.location.href=key;
}