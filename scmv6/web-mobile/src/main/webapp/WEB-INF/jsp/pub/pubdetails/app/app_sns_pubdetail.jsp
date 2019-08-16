<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no" />
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript">
function viewOtherPubs(){
    //window.location.href="/pub/outside/paper/search?searchString=" + encodeURIComponent($("#pubTitle").val());
	var target="scmiosapp://globalsearchtitle?searchtitle=" + encodeURIComponent($("#pubTitle").val());
    document.getElementById("openApp").href=target;
    document.getElementById("openApp").click();
}
</script>
</head>
<body class="white_bg select__content-text">
  <input id="pubTitle" name="pubTitle" type="hidden" value="${pubDetailVO.title}" />
  <div class="top_clear"></div>
  <div class="content">
    <c:choose>
      <c:when test="${pubOperateVO.viewStatus == 'hasDeleted'}">
        <div class="no_effort" style="margin-top: 15px;">
          <div class="response_no-result" style="margin-top: 0px !important;">
            对不起，该记录已被删除。
          </div>
        </div>
      </c:when>
      <c:when test="${pubOperateVO.viewStatus == 'no permission'}">
        <div class="no_effort" style="margin-top: 15px;">
          <div class="response_no-result" style="margin-top: 0px !important;">
            该论文由于个人隐私设置, 无法查看,<br />请<a style="cursor: pointer;color: #39639f;text-decoration: none;" onclick="viewOtherPubs();"><span>查看其它论文</span></a>
          </div>
        </div>
      </c:when>
      <c:otherwise>
        <div  id="touchDiv" style="background: #fff; padding: 16px 0px; width: 100%;">
          <jsp:include page="/WEB-INF/jsp/pub/pubdetails/app/app_pubdetails_sub.jsp"></jsp:include>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
  <a id='openApp' href="javascript:void(0);" style="display: none;" target="_blank"></a>
</body>
</html>