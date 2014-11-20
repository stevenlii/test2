<%@ page language="java"  pageEncoding="UTF-8" 	contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html> 
<head><meta http-equiv="Content-Type" content="text/html;charset=UTF-8"></head>
<script> 
function getValue() {     //document.getElementById('txt').value=window.opener.txtName.value;        
 document.getElementById('txt').value=window.opener.document.getElementById('txtID').value; }
  </script>
  <body onload='getValue();'> 传过来的值是 <input type='text' id='txt' /> 
  <input type='button' value='调用父窗口的方法' onclick='window.opener.method();' />      
   设置父窗口的文本<input type='text' id='t' /> 
   <input type='button' value='执行' onclick='window.opener.document.getElementById("txtID").value=document.getElementById("t").value;' />     
   <input type='button' value='刷新父窗口' onclick='window.opener.location=window.opener.location;' />    
    <input type='button' value='关闭父窗口' onclick='window.opener.close();opener=null;' />
     </body>
      </html>