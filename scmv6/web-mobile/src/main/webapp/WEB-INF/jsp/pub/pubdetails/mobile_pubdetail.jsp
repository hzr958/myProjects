<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
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
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
<link rel="stylesheet" href="${resmod}/mobile/css/plugin/swiper/swiper.min.css">
  <!-- 详情页面自定义样式 -->
  <%@ include file="model/mobile_pub_detail_style.jsp" %>
</head>
<body class="white_bg" style="user-select: text; -webkit-user-select: text;">
  <!-- 头部导航栏和快速分享按钮 -->
  <%@ include file="model/mobile_pub_detail_navigation_bar.jsp" %>
  
  <input type="hidden" id="searchOrderBy" name="orderBy" value="${pubOperateVO.orderBy}" />
  <input type="hidden" id="publishYear" name="publishYear" value="${pubOperateVO.publishYear}" />
  <input type="hidden" id="searchPubType" name="searchPubType" value="${pubOperateVO.searchPubType}" />
  <input type="hidden" id="searchPubDBIds" name="includeType" value="${pubOperateVO.includeType }" />
  <input id="orderType" name="orderType" type="hidden" value="${pubOperateVO.orderType }" />
  <input id="pubType" name="pubType" type="hidden" value="${pubOperateVO.pubType }" />
  <input id="pubLocale" name="pubLocale" type="hidden" value="${pubOperateVO.pubLocale } " />
  <input id="articleType" name="articleType" type="hidden" value="${pubOperateVO.articleType } " />
  <input id="detailPageSum" name="detailPageSum" type="hidden" value=" ${pubOperateVO.detailPageSum }" />
  <input id="detailPageNo" name="detailPageNo" type="hidden" value="${pubOperateVO.detailPageNo } " />
  <input id="detailPageSize" name="detailPageSize" type="hidden" value=" ${pubOperateVO.detailPageSize } " />
  <input id="detailCurrSize" name="detailCurrSize" type="hidden" value="${pubOperateVO.detailCurrSize}" />
  <input id="des3SearchPsnId" name="des3SearchPsnId" type="hidden" value="${pubOperateVO.des3SearchPsnId}" />
  <input id="psnId" name="psnId" type="hidden" value="${pubDetailVO.psnId}" />
  <input id="userId" name="psnId" type="hidden" value="<%-- ${userId} --%>" />
  <input id="useoldform" name="useoldform" type="hidden" value="${pubOperateVO.useoldform} ">
  <input id="fromPage" name="fromPage" type="hidden" value="${pubOperateVO.fromPage}" />
  <input id="PagNo" name="PagNo" type="hidden" value=1 />
  <input id="hasLogin" name="hasLogin" type="hidden" value="${pubOperateVO.hasLogin ? 1 : 0 }" />
  <input id="isMyPub" name="isMyPub" type="hidden" value="${pubDetailVO.isOwner }" />
  <input id="des3PsnId" name="des3PsnId" type="hidden" value='<iris:des3 code="${pubDetailVO.psnId }"/>' />
  <input id="des3PubId" name="des3PubId" type="hidden" value="${pubDetailVO.des3PubId }" />
  <input id="pubSum" name="pubSum" type="hidden" value="${pubOperateVO.pubSum}" />
  <input id="domain" name="domain" type="hidden" value="${pubOperateVO.domain}" />
  
  <!-- 成果详情内容 -->
  <div class="content swiper-container" id="pub_detail_content_container">
    <div class="swiper-wrapper" id="touchDiv">
        <%-- <c:forEach var="i" begin="1" end="${pubOperateVO.pubSum}" step="1">
          <div class="swiper-slide mobile_pub_details_item">

          </div>
        </c:forEach> --%>
    </div>
    <div class="swiper-pagination"></div>
     <div class="openpage-inapp_btn" onclick="openApp()" id="openAppBtn" style="display:none">在App中打开</div>
  </div>
  <!--  -->
  <div class="bottom_clear"></div>
  <!-- 底部评论输入框和站外底部浮层 -->
  <%@ include file="model/mobile_pub_detail_bottom.jsp" %>
  
  <!-- js脚本 -->
  <%@ include file="model/mobile_pub_detail_script.jsp" %>
</body>
</html>