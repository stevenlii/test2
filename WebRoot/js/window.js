
var outHtml="<div id=\"pop\"  style=\"filter:RevealTrans ( duration = 0.4,transition=19 );overflow:hidden;position: absolute;z-index:1000;width:97%;height:100%;visibility:hidden\" ><iframe name=\"wframe\" id=\"wframe\" src=\"\" width=\"100%\" height=\"100%\" frameborder=\"0\" scrolling=\"no\"></iframe></div>";
//var icoUrl = "<div style=\"background:#fff;font-size:12px;filter:progid:DXImageTransform.Microsoft.Shadow(Color=#ff666666, Strength=100, Direction=135);border-bottom:1px solid #999999;border-right:1px solid #999999;border-left:1px solid #999999; background-color:#eeeeee;width:97%;height:100%;\"><table width=\"100%\" border=\"0\" height=\"100%\"><tr><td height=\"25\" style='background-image:url(../profile/default/images/pop/title_bg_left.gif);width:4px'>&nbsp;</td><td   style='background-image:url(../profile/default/images/pop/title_bg_mid.gif)'>&nbsp;</td><td  style='background-image:url(../profile/default/images/pop/title_bg_right.gif);width:4px'>&nbsp;</td></tr><tr><td colspan=\"3\" align=\"center\" valign=\"middle\" >&nbsp;</td></tr></table></div>";
var icoUrl = "<div style=\"background:#fff;font-size:12px;border-bottom:4px solid #999999;border-right:4px solid #999999;border-left:1px solid #999999; background-color:#eeeeee;width:97%;height:100%;\"><table width=\"100%\" border=\"0\" height=\"100%\"><tr><td height=\"25\" style='background-image:url(../profile/default/images/pop/title_bg_left.gif);width:4px'>&nbsp;</td><td   style='background-image:url(../profile/default/images/pop/title_bg_mid.gif)'>&nbsp;</td><td  style='background-image:url(../profile/default/images/pop/title_bg_right.gif);width:4px'>&nbsp;</td></tr><tr><td colspan=\"3\" align=\"center\" valign=\"middle\" >&nbsp;</td></tr></table></div>";
document.write(outHtml);
var eventObj;
function showWin(url,width,height,event){
	    var obj = document.getElementById("pop");
	    var obj1 = document.getElementById("wframe");
	    var srcElement = event.target||window.event.srcElement;
		if(eventObj!=srcElement){
			obj.style.display = "none"; 
			obj.style.visibility = "hidden";
		}
		//alert('a1');
		eventObj = srcElement;
		var wframe = document.getElementById('wframe');
		//wframe.document.body.innerHTML=icoUrl;//FF NOT OK
		wframe.contentWindow.document.body.innerHTML=icoUrl;//for FF,IE,GOOGLE
		wframe.contentWindow.location=url;//for FF,IE,GOOGLE
		
		obj.style.display = "block"; 
		obj.style.width = width + "px"; 
		obj.style.height = height + "px"; 
		var evpos = mouseCoords(event);
		//obj.style.pixelLeft=getPos(srcElement,"Left");//FF NOT OK
		//obj.style.pixelTop=getPos(srcElement,"Top")+25;//FF NOT OK
		//obj.style.pixelLeft=evpos.x+10;//FF NOT OK
		//obj.style.pixelTop=evpos.y+5;//FF NOT OK
		obj.style.top=evpos.y+5+"px";//for FF,IE,GOOGLE
		obj.style.left=evpos.x+10+"px";//for FF,IE,GOOGLE
		//obj.style.top=0+5+"px";//for FF,IE,GOOGLE
		//obj.style.left=0+10+"px";//for FF,IE,GOOGLE
		obj.style.visibility = "visible"; 
		obj1.style.visibility = "visible"; 
		
}

function showDiv(url,width,height){
	
	showPop(width,height)
}
function showPop(width,height){ 
	
} 
function hidePop(){ 
var obj =document.getElementById("pop");
obj.style.display = "none"; 
obj.style.visibility = "hidden";
} 
function getPos(el,ePro){
		var ePos=0;
		while(el!=null)
		{  
		      ePos+=el["offset"+ePro];
		      el=el.offsetParent;
		}
		return ePos;
}
function bodyClose(event){
	//alert(1);
//alert("bodyClose start");
	var obj = document.getElementById("pop");
	var srcElement = event.target||window.event.srcElement;
	//alert(srcElement);
	if(eventObj!=srcElement && obj.style.visibility=="visible"){
	//if(obj.style.visibility=="visible"){
		//alert(obj.style.visibility);
		obj.style.display = "none"; 
		obj.style.visibility = "hidden";
	}
	//alert("bodyClose end");
}
//window.document.body.attachEvent("onclick", bodyClose);
function mouseCoords(ev)
{ 

    if(ev.pageX || ev.pageY){ 
    	return {x:ev.pageX, y:ev.pageY}; 
	} 
	return{ 
    	x:ev.clientX + document.body.scrollLeft - document.body.clientLeft, 
    	y:ev.clientY + document.body.scrollTop - document.body.clientTop 
	}; 
}
