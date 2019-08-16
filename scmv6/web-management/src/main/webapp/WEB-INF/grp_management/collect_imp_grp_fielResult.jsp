<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="${resmod }/css_v5/reset.css"/>
  <link rel="stylesheet" href="${resmod }/css_v5/project/newpagestyle.css"/>
  <link rel="stylesheet" href="${resmod }/css_v5/project/unit.css"/>
  <link rel="stylesheet" href="${resmod }/css_v5/project/achievement_lt.css"/>
  <link rel="stylesheet" href="${resmod }/css_v5/project/administrator.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>群组管理</title>
</head>
<body style="overflow-x: hidden;">
<div class="conter_finished" style="display: block; height: 723px;">
<!--部门--><div id="con_five_1" style="display:block">
             <ul class="finish version-tip" style="top:37%;">
                <li><img src="${resmod }/images_v5/pass.png" alt=""></li>
                <li><h1 class="finish_catalog">
                    <c:choose>
                        <c:when test="${ empty count }">0</c:when>
                        <c:otherwise>${count }</c:otherwise>
                    </c:choose>个群组创建成功</h1></li>
                <li> <a href="/scmmanagement/grpmanage/importgrp" class="martter-demo-step finish_catalog_step">继续</a>
                    <a href="/scmmanagement/grpmanage/main"  class="martter-demo-browse">返回列表</a>
                </li>
            </ul>
         </div>
        <div id="con_five_2" style="display:none"></div>
        <div id="con_five_3" style="display:none"></div>
        <div id="con_five_4" style="display:none"></div>
           </div>
</body>
</html>
