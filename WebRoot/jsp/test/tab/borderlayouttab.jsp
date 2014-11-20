<%@ page language="java"  pageEncoding="UTF-8" 	contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
    <title>tab和布局组件组合</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/ui/themes/default/om-all.css" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-core.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-mouse.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-resizable.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-panel.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-borderlayout.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-tabs.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/js/om-tree.js"></script>

    <!-- view_source_begin -->
    <style>
    	html, body{ width: 100%; height: 100%; padding: 0; margin: 0;}
    	#center-tab .om-panel-body{
    		padding: 0;
    	}
    </style>
    <script type="text/javascript">
         $(document).ready(function() {
            $('body').omBorderLayout({
           	   panels:[{
           	        id:"center-panel",
           	     	header:false,
           	        region:"center"
           	    },{
           	        id:"west-panel",
           	        resizable:true,
           	        collapsible:true,
           	        title:"导航",
           	        region:"west",width:120
           	    }]
            });
            var tabElement = $('#center-tab').omTabs({
                height : "fit"
            });
            var navData = [{id:"n1",text:"搜索引擎",expanded:true},
                         {id:"n2",text:"中间件",expanded:true},
                         {id:"n10",pid:"n0",text:"test",url:"http://10.10.36.254:8888/hfMngBusi/jsp/test/dialog/dg1.jsp"},
                         {id:"n100",pid:"n00",text:"testDemo",url:"http://10.10.36.254:8888/hfMngBusi/demo/index.do"},
                         {id:"n11",pid:"n1",text:"百度",url:"http://www.baidu.com"},
    			         {id:"n12",pid:"n2",text:"金蝶中间件",url:"http://www.apusic.com/homepage/index.faces"}];
            $("#navTree").omTree({
                dataSource : navData,
                simpleDataModel: true,
                onClick : function(nodeData , event){
                	if(nodeData.url){
                		var tabId = tabElement.omTabs('getAlter', 'tab_'+nodeData.id);
                		if(tabId){
                			tabElement.omTabs('activate', tabId);
                		}else{
		                	tabElement.omTabs('add',{
		                        title : nodeData.text, 
		                        tabId : 'tab_'+nodeData.id,
		                        content : "<iframe id='"+nodeData.id+"' border=0 frameBorder='no' name='inner-frame' src='"+nodeData.url+"' height='"+ifh+"' width='100%'></iframe>",
		                        closable : true
		                    });
                		}
                	}
                }
            });
            var ifh = tabElement.height() - tabElement.find(".om-tabs-headers").outerHeight() - 4; //为了照顾apusic皮肤，apusic没有2px的padding，只有边框，所以多减去2px
            $('#3Dbox').height(ifh);
        });
    </script>
    <!-- view_source_end -->
</head>
<body>
    <!-- view_source_begin -->
    	<div id="center-panel">
    	    <div id="center-tab" >
		        <ul>
		           <li><a href="#tab1">3D盒子</a></li>
		        </ul>
		        <div id="tab1">
		           <iframe id='3Dbox' border=0 frameBorder='no' src='banner.html' width='100%'></iframe>
		        </div>
    	    </div>
    	</div>
    	<div id="west-panel">
    	    <ul id="navTree"></ul>
    	</div>
    <!-- view_source_end -->
    <div id="view-desc">
    </div>
</body>
</html>