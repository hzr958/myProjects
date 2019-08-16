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

</script>
</head>
<body class="white_bg select__content-text">
  <input id="pubTitle" name="pubTitle" type="hidden" value="${pubDetailVO.title}" />
  <div class="top_clear"></div>
  <div class="content">
    <c:choose>
      <c:when test="${isExist == '0'}">
        <div class="no_effort" style="margin-top: 15px;">
          <div class="response_no-result" style="margin-top: 0px !important;">
 没有权限访问或者群组不存在!
          </div>
        </div>
      </c:when>
      <c:when test="${isPrivate == '1'}">
        <div class="no_effort" style="margin-top: 15px;">
          <div class="response_no-result" style="margin-top: 0px !important;">
          该群组是隐私群组，无法查看
          </div>
        </div>
      </c:when>

    </c:choose>
  </div>
</body>
</html>