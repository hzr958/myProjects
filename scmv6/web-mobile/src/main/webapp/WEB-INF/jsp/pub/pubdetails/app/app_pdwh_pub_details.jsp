<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>科研之友</title>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
</head>
<body class="white_bg" style="-moz-user-select: text; user-select: text; -webkit-user-select: text;">
  <div class="top_clear"></div>
  <div class="content">
    <div id="touchDiv">
      <div class="paper_detial">
        <c:if test="${pubDetailVO.pubId==0 }">
          <dl>
            <dd class="response_no-result">未找到对应的记录</dd>
          </dl>
        </c:if>
        <c:if test="${pubDetailVO.pubId > 0 }">
          <dl>
            <h2>
              <span id="pubTitleShare">${pubDetailVO.title}</span>
            </h2>
            <dt>${pubDetailVO.authorNames}</dt>
            <dd>${!empty pubDetailVO.briefDesc ? pubDetailVO.briefDesc : "--"}</dd>
          </dl>
          <c:if test="${!empty pubDetailVO.summary}">
            <h3>摘要:</h3>
            <p>
              <span id="pubAbs">${pubDetailVO.summary }</span>
            </p>
          </c:if>
          <c:if test="${!empty pubDetailVO.keywords}">
            <h3>关键词:</h3>
            <p>${pubDetailVO.keywords }&nbsp;</p>
          </c:if>
          <!-- DOI的显示要放到关键词的下面 -->
          <c:if test="${!empty pubDetailVO.doi}">
            <h3>
              DOI：<a href="http://dx.doi.org/${pubDetailVO.doi}" target="_Blank">${pubDetailVO.doi}</a>
            </h3>
          </c:if>
        </c:if>
      </div>
    </div>
  </div>
</body>
</html>