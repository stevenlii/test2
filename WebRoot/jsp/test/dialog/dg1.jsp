<%@ page language="java"  pageEncoding="UTF-8" 	contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>dialog嵌套布局组件</title>
    <!--
    <script type="text/javascript" src="../../jquery.js"></script>
    <script type="text/javascript" src="../../ui/om-core.js"></script>
    <script type="text/javascript" src="../../ui/om-mouse.js"></script>
    <script type="text/javascript" src="../../ui/om-resizable.js"></script>
    <script type="text/javascript" src="../../ui/om-draggable.js"></script>
    <script type="text/javascript" src="../../ui/om-position.js"></script>
    <script type="text/javascript" src="../../ui/om-dialog.js"></script>
    <script type="text/javascript" src="../../ui/om-panel.js"></script>
    <script type="text/javascript" src="../../ui/om-borderlayout.js"></script>
    <script type="text/javascript" src="../../ui/om-tabs.js"></script>
    <script type="text/javascript" src="../../ui/om-tree.js"></script>
    <script type="text/javascript" src="../../ui/om-grid.js"></script>
    <script type="text/javascript" src="../../demos/common/js/themewriter.js"></script>
    <script type="text/javascript" src="data.js"></script>
      -->
      <link rel="stylesheet" href="${pageContext.request.contextPath}/js/ui/themes/default/om-all.css" type="text/css">
     <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-core.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-mouse.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-draggable.js"></script>
    
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-resizable.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-draggable.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-position.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-dialog.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-panel.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-borderlayout.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-tabs.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-tree.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-grid.js"></script>
    
    <script type="text/javascript" src="data.js"></script>
    

    <!-- view_source_begin -->
    <style>
    	html, body{ width: 100%; height: 100%; padding: 0; margin: 0;}
    	.om-dialog .om-dialog-content{
    	     padding: 0; margin: 0;
    	}
    	.om-grid {
		    overflow: visible;
		    position: relative;
		}
    </style>
    <script type="text/javascript">
        $(document).ready(function() {
        	
        	var element = $('#body').omBorderLayout({
            	   panels:[{
            	        id:"center-panel",
            	     	header:false,
            	        region:"center"
            	    },{
            	        id:"west-panel",
            	        resizable:true,
            	        collapsible:true,
            	        title:"west panel",
            	        region:"west",
            	        width:120
            	    }]
             });
             $("#mytree2").omTree({
                 dataSource : citydata,
                 onSelect: function(nodedata){
                 	if(!nodedata.children && nodedata.text){
                 		//避免在IE浏览器下出现中文乱码
                 		var url = encodeURI("griddata.do?method=filter&city="+nodedata.text);
                 		$("#grid").omGrid("setData", url);
                 	}else {
                 		$("#grid").omGrid("setData", "griddata.do?method=fast");
                 	}
                 }
             });
             
             $('#grid').omGrid({
                 dataSource : 'griddata.do?method=fast',
                 limit: 10,
                 height: 'fit',
                 width : 'fit',
                 title : '标题',
                 colModel : [ {header : 'ID', name : 'id', width : 82, align : 'center'}, 
                              {header : '地区', name : 'city', width : 120, align : 'left'}, 
                              {header : '地址', name : 'address', align : 'left', width : 'autoExpand'} ]
             });
             $( "#dialog").omDialog({
     			autoOpen: false,
     			width : 651,
     			height: 1000,
     			modal: true,
     			resizable:false
     		});
        });
        function showDialog(){
       	   $( "#dialog").omDialog('open');
       	}
        
    </script>
    <!-- view_source_end -->
</head>
<body>
    <!-- view_source_begin -->
    	<button onclick="showDialog();">显示弹出窗口</button>
    	<div id="dialog" title="布局组件和弹出窗口组合">
           <div id="body" style="width:649px; height:410px;">
                <div id="center-panel">
		    	    <table id="grid"></table>
		    	</div>
		    	<div id="west-panel">
		    	    <ul id="mytree2"></ul>
		    	</div>
           </div>
        </div>
    <!-- view_source_end -->
    <div id="view-desc">
    </div>
</body>
</html>