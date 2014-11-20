<%@ page language="java"  pageEncoding="UTF-8" 	contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/jquery.min.js"></script>
<style>
.tooltip{
    position:absolute;
    border:1px solid #333;
    background:#f7f5d1;
    padding:1px;
    color:#333;
    display:none;
}
</style>

</head>
<body>
<p><a href="#" class="c_t" title="dsd" merid="9996" goodsid="100">提示1.</a></p>
<p><a href="#" class="c_t" title="www" merid="9996" goodsid="010">提示2.</a></p>
</body>
<script type="text/javascript">
$('.c_t').mouseover(function(e){ 
    //var alt = $(this).attr('title'); 
    //alt = alt+"yangwr";
    //alert(alt);
    var merid = $(this).attr('merid'); 
    var goodsid = $(this).attr('goodsid');
    var alt = merid + goodsid; 
    $(this).attr('title',''); 
    var dhtml = $("<div class='tooltip'>"+alt+"</div>").css({position:'absolute',top:(e.pageY+10)+'px',left:(e.pageX+5)+'px',display:'none'}); 
    dhtml.appendTo('body').fadeIn(); 
    $(this).unbind('mouseout').mouseout(function(){ 
       $(this).attr('title',alt); 
        dhtml.fadeOut(100,function(){$(this).remove()}); 
    }) 
}) 

</script>
</html>