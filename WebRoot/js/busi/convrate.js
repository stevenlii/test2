//查询方法
function query(){
	var from=$('#from').val();
	var to=$('#to').val();
	var key="";
	if(from != "" && to == ""){
		alert("请填写结束时间");
		return false;
	}else if(from == "" && to != ""){
		alert("请填写开始时间");
		return false;
	}else if(from != "" && to != ""){
		key = key + '&from=' + encodeURI(from);
		key = key + '&to=' + encodeURI(to);
	}
	$('#grid').omGrid("setData", url+key);
}

//下载方法，flag为0表示下载汇总文件，为1表示下载明细文件
function downloadConvRate(flag,date){
	window.open('downloadConvRate.do?flag='+flag+'&date='+date, 'go', 'scrollbars=yes,resizable=yes,width=508,height=350');
}
