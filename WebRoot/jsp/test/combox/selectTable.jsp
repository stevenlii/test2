<%@ page language="java"  pageEncoding="UTF-8" 	contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/ui/themes/default/om-all.css" type="text/css">
<script src="${pageContext.request.contextPath}/js/ui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/ui/js/om-core.js"></script>
<script src="${pageContext.request.contextPath}/js/ui/js/om-mouse.js"></script>
<script src="${pageContext.request.contextPath}/js/ui/js/om-resizable.js"></script>
<script src="${pageContext.request.contextPath}/js/ui/js/om-grid.js"></script>
 <script type="text/javascript">
        $(document).ready(function() {
            var timer = '_select1_timer';
            var input = $('#select1').attr('readonly', 'readOnly')
					                 .focus(function() {
					                     dropList.show();
					                 })
					                 .blur(function() {
					                     window[timer] = setTimeout(function() {
					                        dropList.hide();
					                     }, 450);
					                 });
             var inputOffset=input.offset();
             var dropList = input.next().css({top:inputOffset.top+input.outerHeight(),left:inputOffset.left})
                                        .mousedown(function(e) {
                                            e.stopPropagation();
                                            setTimeout(function() {
                                                clearTimeout(window[timer]);
                                            }, 25);
                                        });
             $(document.body).mousedown(function() {
                dropList.hide();
             });  
             
    
            $('#target-table').omGrid({
                dataSource : '${pageContext.request.contextPath}/demo/testJson.do',
                method : 'POST',
                width : 470,
                height : 200,
                showIndex : true,
                autoFit : true,
                colModel : [ {header : 'ID', name : 'id', width : 100, align : 'center', sortable : true}, 
                             {header : '地区', name : 'city', width : 120, align : 'left', sortable : true}, 
                             {header : '地址', name : 'address', align : 'left', width : 200, autoExpandMin : 100, sortable : true} ],
                onRowClick : function(event, rowData) {

                    input.val(rowData.city).attr('_trueValue', rowData.id);
                    dropList.hide();
                }
            });
            input.next().hide(); 
        });
            function search(){
            	
            	 var searchID=jQuery('#searchID').val();
            	// alert(searchID);
            	 var url = '${pageContext.request.contextPath}/demo/testJson.do'+'?id='+searchID;
            	 //alert(url);
            	 $('#target-table').omGrid("setData", url);
            }  
    </script>

</head>

<body>
    <span>
        <input id="select1" /> <!-- 如果display设置为none，则grid无法得到正确的高度 TODO -->
        <div class="omcombo-ct" style="position: absolute; display: block; left: 0;background:#EEE;">
        	<span id="spanSearch"><input type="text" id="searchID" value="001"><input type="button" id="searchBtn" value="查询" onclick="search();"></span>
            <table id="target-table"></table>
        </div>
    </span>
    <input type="button" onclick="$('#result').html($('#select1').attr('_trueValue'));" value="getValue"/>
    <br /><span style="font-size:12px">你选择的记录的value为：<span style="color: red" id="result"></span>
    
    </body>
</html>