<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<title>科研之友</title>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/scmjscollection.css" rel="stylesheet" type="text/css">
<link href="${resmod }/smate-pc/new-confirmbox/confirm.css" rel="stylesheet" type="text/css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_dialogs.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/scmpc_chipbox.js" type="text/javascript"></script>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="utf-8"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script src="${resmod}/js/loadStateIco.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/prj/prj.common.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/prj/prj.common_${locale}.js"></script>
<script type="text/javascript" src="${resmod }/js/homepage/homepage_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
<script type="text/javascript">
var locale = '${locale}';
var des3CurrentId = '${des3CurrentId}';
  $(document).ready(function(){
    project.ajaxShow(des3CurrentId);
	var tiplist = document.getElementsByClassName("input-custom-style");
 	for(var i = 0; i < tiplist.length; i++){
 		tiplist[i].onclick = function(){
 			if(	document.getElementsByClassName("drawer-batch__tip-container").length!=0){
 				document.getElementsByClassName("drawer-batch__tip-container")[0].style.display="block";
 			}
 		}
 	}
	
	if(document.getElementsByClassName("drawer-batch__box").length!=0){
        document.getElementsByClassName("drawer-batch__box")[0].onmousedown=function(){
        	$(".drawer-batch__tip-container").remove();
		}
	}
})
</script>
</head>
<body>
  <div class="container__horiz oldDiv">
  </div>
  <!-- 分享插件 -->
  <jsp:include page="/common/smate.share.jsp" />
</body>
</html>
