//当前页面弹出操作提示框
function showSimpleTip(result) {
    // result 结果为1表示成功，结果为0表示失败
    if (result == '1') {
        $.omMessageBox.alert({
            type: 'success',
            title: '操作提示',
            content: '操作成功！'
        });
    }
    if (result != '1') {
        $.omMessageBox.alert({
            type: 'error',
            title: '操作提示',
            content: '操作失败！'
        });
    }
}

// 刷新页面
function shuaxin() {
    jQuery('#grid').omGrid('reload');
}

// 增加方法
function show() {
    $("#dialog").omDialog('open');
    var frameLoc = window.frames[0].location;
    frameLoc.href = 'add.do';
    $("#dialog").omDialog({
        onClose: function(event) {
            window.frames[0].location.href = '../wait.htm';
        }
    });
    return false;
}

// 查询方法
function querys() {
    var paraType = jQuery('#paraType').val();
    var paraCode = jQuery('#paraCode').val();
    var key = "querys.do?queryKey=para.all";
    if (paraType != "") {
        key = key + '&paraType=' + encodeURI(paraType);
    }
    if (paraCode != "") {
        key = key + '&paraCode=' + encodeURI(paraCode);
    }
    if (getByteLen(paraCode) > 16) {
        alert('参数编号最大长度为中文8位，英文16位');
        return;
    }
    $('#grid').omGrid("setData", key);
}
function queryb() {
    var paraType = jQuery('#paraType').val();
    var paraCode = jQuery('#paraCode').val();
    var key = "queryb.do?queryKey=para.all";
    if (paraType != "") {
        key = key + '&paraType=' + encodeURI(paraType);
    }
    if (paraCode != "") {
        key = key + '&paraCode=' + encodeURI(paraCode);
    }
    if (getByteLen(paraCode) > 16) {
        alert('参数编号最大长度为中文8位，英文16位');
        return;
    }
    $('#grid').omGrid("setData", key);
}

// 显示修改方法
function showModifyDialog(paraType,paraCode) {
    $("#dialog").omDialog("option", "width", '650');
    $("#dialog").omDialog("option", "title", '修改参数信息');
    $("#dialog").omDialog("option", "height", '300');
    $("#dialog").omDialog("option", "modal", true);
    $("#dialog").omDialog('open');
    var frameLoc = window.frames[0].location;
    paraType = encodeURI(paraType);
    frameLoc.href = 'mod.do?paraType=' + paraType + '&paraCode=' + paraCode;
    $("#dialog").omDialog({
        onClose: function(event) {
            window.frames[0].location.href = '../wait.htm';
        }
    });
    return false;
}

// iframe窗口调用的关闭操作，返回值是dialog出口的id 例如详情dialog 则为#dialog-detail
function fillBackAndCloseDialog(msg) {
    // jQuery('#grid').omGrid('reload',1);
    $(msg).omDialog('close');
}
// 删除空格
function trim(str) {
    if (str == null) {
        return "";
    }
    return str.replace(/^\s*(.*?)[\s\n]*$/g, '$1');
}

// 隐藏框
function display(servMonth, interval, conMode) {
    var traget = document.getElementById(servMonth);
    traget.style.display = "none";
    var traget = document.getElementById(interval);
    traget.style.display = "none";
    var traget = document.getElementById(conMode);
    traget.style.display = "none";
}
// 显示框
function showTr(servMonth, interval, conMode) {
    var traget = document.getElementById(servMonth);
    traget.style.display = "";
    var traget = document.getElementById(interval);
    traget.style.display = "";
    var traget = document.getElementById(conMode);
    traget.style.display = "";
}

function isNotEmpty(v) {
    if (!v) {
        return false;
    }
    if (v == '') {
        return false;
    }
    return true;
}

// 新增方法js

// 功能：隐藏、显示表格中的一行，行由参数给出
// 参数：num 行；display 1 显示 0 隐藏
// *********************************
function HiddenTr(num, display) {
    var tempTable = document.getElementsByTagName("table")[0] // 表示页面中第几个表格，在页面中从上往下数
    var tempTd = tempTable.getElementsByTagName("tr")[num]
    if (display == "1") tempTd.style.display = ""
    else tempTd.style.display = "none"
    var tempTd = tempTable.getElementsByTagName("tr")[num + 1]
    if (display == "1") tempTd.style.display = ""
    else tempTd.style.display = "none"
    var tempTd = tempTable.getElementsByTagName("tr")[num + 2]
    if (display == "1") tempTd.style.display = ""
    else tempTd.style.display = "none"
}

function HiddenTr1(num, display) {
    var tempTable = document.getElementsByTagName("table")[0] // 表示页面中第几个表格，在页面中从上往下数
    var tempTd = tempTable.getElementsByTagName("tr")[num]
    if (display == "1") tempTd.style.display = ""
    else tempTd.style.display = "none"
}

function getByteLen(strTemp) { 
    var i,sum;
    sum=0;
    for(i=0;i<strTemp.length;i++) { 
      if ((strTemp.charCodeAt(i)>=0) && (strTemp.charCodeAt(i)<=255)) {
        sum=sum+1;
      }else {
        sum=sum+2;
      }
    }
    return sum; 
  } 