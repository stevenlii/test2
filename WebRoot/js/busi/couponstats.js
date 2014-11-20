 function getURL(url){
	 var params = $("#form").serialize();
	 url += "&"+ params;
	 return url;
 }
 
 function doQuery(url){
	 url = getURL(url);
	 $('#grid').omGrid("setData", url);
 }
 
 function doExport(url){
	 var start = $("#startDate").val();
	 var end = $("#endDate").val();
	 if(start == ''){
		 $("#result").html('请选择开始时间');
		 $( "#dialog-modal").omDialog('open');
		 return;
	 }
	 if(end == ''){
		 $("#result").html('请选择结束时间');
		 $( "#dialog-modal").omDialog('open');
		 return;
	 }
	 $('#form')[0].action= getURL(url);
	 $('#form')[0].submit();
 }
