<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>第三方登录</title>
<script src="${resmod }/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
    buildWindowHref();
})

function buildWindowHref(){
    //由于有时候协议不一样的话会有跨域问题，获取不到父页面地址栏url，故用window.name传递
    var parentWindowUrl = window.name;
    if(parentWindowUrl != null && parentWindowUrl != ""){
        //如果当前页面是oauth项目的页面，则qq登录后跳转动态首页，否则刷新当前页面
	    if(parentWindowUrl.indexOf("/oauth") > -1){
	        window.opener.location.href =('${domainscm }' + "/dynweb/main?AID="+'${AID }');
	    }else{
	        var parentWindowUrl = parentWindowUrl.replace(/AID=[^&]*(&)?/g, "");
	        if(parentWindowUrl.indexOf("?") > -1){
	            window.opener.location.href = parentWindowUrl + "&AID=${AID }";
	        }else{
	            window.opener.location.href = parentWindowUrl + "?AID=${AID }";
	        }
	    }
    }else{
        window.opener.location.href =('${domainscm }' + "/dynweb/main?AID="+'${AID }');
    }
    window.close();
}

</script>
</head>
<body>
</body>
</html>