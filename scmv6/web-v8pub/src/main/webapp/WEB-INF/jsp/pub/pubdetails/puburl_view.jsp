<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<script type="text/javascript">var ctxpath='${ctx}';</script>
<link href="${res}/css/public.css" type="text/css" rel="stylesheet" />
<link href="${res}/css/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<link href="${res}/css/page.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${res}/js/jquery.js"></script>
<script type='text/javascript' src='${res }/js/jquery.alerts_${locale}.js'></script>
<script type="text/javascript" src="${res}/js/jquery.alerts.js"></script>
</head>
<body>
  <form>
    <div id="divMessage"></div>
    <script type="text/javascript">	
	var urlJson=${forwardUrl};

	showViewStatus(true);		
	
	function showViewStatus(isWorking){
		var progressBar = "<img src='${resmod}/images/prog_bar.gif' />";	
		var msg = '<spring:message code"puburlview.msg.opening" />';
		if (isWorking){
			$("#divMessage").html("<table border=0 ><tr><td align=left valign=middle><B>" + msg + "</B></td><td align=left>" + progressBar + "</td></tr></table>");
			$("#divMessage").show();
		}
	}
	
	function openHref(url){
		try{
			try{
				url=decodeURIComponent(url);
			}catch(e){}
			
			if (url.indexOf("://")<0 && url.indexOf(":\\\\")<0)
			{
				url="http://" + url;
			}
			location.href=url;
		}catch(e){
			jAlert("<spring:message code'puburlview.msg.openfailed'/>",'<spring:message code"puburlview.msg.alert"/>',function(){
				window.close();
			});
		}
	}
	openHref(urlJson["urlInside"]);
	</script>
  </form>
</body>
</html>
