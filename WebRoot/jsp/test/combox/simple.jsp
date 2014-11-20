<%@ page language="java"  pageEncoding="UTF-8" 	contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/ui/themes/default/om-all.css" type="text/css">
<script src="${pageContext.request.contextPath}/js/ui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/ui/js/om-core.js"></script>
<script src="${pageContext.request.contextPath}/js/ui/js/om-combo.js"></script>
 <script type="text/javascript">
        $(document).ready(function() {
            $('#combo1').omCombo({
                dataSource : [ {text : 'PRC', value : 'PRC'}, 
                               {text : 'USA', value : 'USA'}, 
                               {text : 'UK', value : 'UK'}, 
                               {text : 'JPN', value : 'JPN'} ]
                ,listMaxHeight:40
                ,forceSelection : true
            });
            //alert($('#combo1').omCombo('value'));
            $('#combo1').omCombo('value','JPN');
            
        });
    </script>
</head>

<body>

  <!-- view_source_begin -->
    <input id="combo1"/>
    <input type="button" onclick="alert($('#combo1').omCombo('value'));" value="getValue"/>
    <!-- view_source_end -->
    <div id="view-desc">
        <p>设置dataSource数据源属性，在指定的input元素中渲染combo下拉框组件。
        </p>
    </div>    
</body>
</ml>