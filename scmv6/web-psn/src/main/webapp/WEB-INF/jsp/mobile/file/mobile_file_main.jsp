<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="utf-8">
<title>科研之友</title>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	mobile_bottom_setTag("psn");
	fileList();
});
function fileList(){
	mobileFileList = window.Mainlist({
		name:"mobileFileList",
		listurl: "/psnweb/mobile/ajaxmyfilelist",
		listdata: {},
		listcallback: function(){
		},
		method: "scroll",
	})
}
function goHistory(){
	location.href=window.history.go(-1);
}

function downloadFulltext (url){
    Smate.confirm("下载提示", "要下载当前文件吗？", function(){
    	window.location.href= url;  
    },["下载", "取消"]);
}

</script>
</head>
<body style="background: #fff; overflow-x: hidden;">
  <div class="page-header page-file__header" style="position: fixed; top: 0px;">
    <i class="material-icons page-file__tip" style="margin-left: 10px" onclick="window.history.back();">keyboard_arrow_left</i>
    <div class="page-file__title" style="margin-left: -20px">文件</div>
  </div>
  <div class="main-list">
    <div class="main-list__list" list-main="mobileFileList" style="overflow: hidden; padding: 48px 0px;"></div>
  </div>
  <jsp:include page="../../mobile/bottom/mobile_bottom.jsp"></jsp:include>
</body>
</html>