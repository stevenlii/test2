//当前页面弹出操作提示框
function showSimpleTip(result){
  if(result !='1') {
    var msg = '操作失败';
    if(result != '0'){
      msg = result;
    }
    $.omMessageBox.alert({
      type:'error',
      title : '操作提示',
      content : msg
    });
  }
}

//查询方法
function query(){
  var merId=$('#merId').val();
  var key="";
  if(merId != ""){ //没要有查询条件，要显示全部数据
    key=key+'&merId='+encodeURI(merId);
  }
  $('#grid').omGrid("setData", url+key);
}
	
//增加方法
function add(){
  $( "#dialog").omDialog("option","width",'600');
  $( "#dialog").omDialog("option","title",'添加商户配置');
  $( "#dialog").omDialog("option","height",'550');
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
  $( "#dialog").omDialog("option","width",'520');
  $( "#dialog").omDialog("option","title",'修改商户配置');
  $( "#dialog").omDialog("option","height",'550');
  $( "#dialog").omDialog("option", "modal", true);
  $( "#dialog").omDialog('open');
  var frameLoc=window.frames[0].location;
  frameLoc.href='modify.do?id='+id;
  $("#dialog").omDialog({onClose : function(event) {
    window.frames[0].location.href='../wait.htm';
  }});
}

//检查当前商户号是否已经存在
function checkMerCnf(merId){
  var res;
  $.ajax({
    url : 'checkMerCnf.do',
    type : 'POST',
    dataType: 'text',
    async:false, //同步提交
    data : {
      'merId' : merId
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

//配置渠道的通知地址、下单地址和证书
function modCert(id){
  $( "#dialog").omDialog("option","width",'500');
  $( "#dialog").omDialog("option","title",'更新商户证书');
  $( "#dialog").omDialog("option","height",'180');
  $( "#dialog").omDialog("option", "modal", true);
  $( "#dialog").omDialog('open');
  var frameLoc=window.frames[0].location;
  frameLoc.href='conf.do?id='+id;
  $("#dialog").omDialog({onClose : function(event) {
    window.frames[0].location.href='../wait.htm';
  }});
}

//iframe窗口调用的关闭操作，返回值是dialog出口的id
function fillBackAndCloseDialog(msg){
  $(msg).omDialog('close');
}
function closeDialog(msg){
  $("#"+msg).omDialog('close');
}

function modify(state){
  showModifyDialog(state);//显示dialog
}
	
//显示详情方法
function show(id){
  $( "#dialog").omDialog("option","width",'370');
  $( "#dialog").omDialog("option","title",'查看商户信息');
  $( "#dialog").omDialog("option","height",'480');
  $( "#dialog").omDialog("option", "modal", true);
  $( "#dialog").omDialog('open');
  var frameLoc=window.frames[0].location;
  frameLoc.href='detail.do?id='+id ;
  $("#dialog").omDialog({onClose : function(event) {
	window.frames[0].location.href='../wait.htm';
  }});
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