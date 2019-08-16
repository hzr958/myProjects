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
<title>科研之友</title>
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script>
$(document).ready(function(){
	

});
</script>
</head>
<body class="bg_write">
  <c:if test="${session.openId!=null}">
    <div class="top">
      <div class="top_mn">
        <a href="/psnweb/mobile/friendlist?psnId=${psnId}" class="rtn_icon"></a>
        <h1>äººååè¡¨</h1>
      </div>
  </c:if>
  </div>
  <div class="b_set"></div>
  <div class="frd_lt frd_lt1">
    <dl>
      <c:forEach items="psnInfoList" var="psnInfo">
        <dd>
          <a href="/psnweb/mobile/hompage?psnId=${psnInfo.psnId}" class="fr_infro"><img
            src="${psnInfoList.avatarUrl}">
            <h3>${psnInfoList.name}</h3>
            <p>${psnInfo.insInfo}</p></a>
        </dd>
      </c:forEach>
    </dl>
  </div>
  <div class="b_set"></div>
  <div class="bottom">
    <ul>
      <li><a href="#"><i class="dyc_icon"></i><span>å¨æ</span></a></li>
      <li><a href="#"><i class="fd_icon"></i><span>åç°</span></a></li>
      <li class="cur"><a href="/psnweb/mobile/friendlist?psnId=${psnId}"><i class="frd_icon"></i><span>å¥½å</span></a></li>
      <li><a href="/psnweb/mobile/hompage?psnId=${psnId}"><i class="user_icon"></i><span>æç</span></a></li>
    </ul>
  </div>
</body>
</html>
