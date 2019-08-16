<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>QQ登录</title>
<script src="${resmod }/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">

$(document).ready(function(){
	window.opener.location.href =('${domainscm }' + "/application/validate/maint?AID="+'${AID }');
	window.close();
})

</script>
</head>
<body>
</body>
</html>