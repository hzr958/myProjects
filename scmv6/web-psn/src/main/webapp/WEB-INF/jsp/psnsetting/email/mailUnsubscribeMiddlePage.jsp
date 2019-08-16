<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common_v5/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<script type="text/javascript" src="${res}/js_v5/jquery.js"></script>
<script type="text/javascript">
	var respath="${res}";
	var ctxpath="${ctx}";
	
 var host = window.location.host;
 
/*  邮件退订action 中间页面   tsz*/
 var url="http://"+host+"/psnweb/cancle/success/login?cancleMark=${ cancleMark }&mail=${ mail}"


$(document).ready(function() { 
	$.ajax({
	    url: ctxpath+'/main',
	    type: 'POST',	
	    dataType: 'json',
	    success: function(){ 	
	    	window.location.href=url
	    },
	 error: function(){ 	
	    	window.location.href=url
	    }
	});
	
})
</script>
</head>
<body>
</body>
</html>