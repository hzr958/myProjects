<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<script type="text/javascript">
    $(function(){
        dealAreaClickEvent(); //科技领域点击事件
    });
    
    //返回
    function goback(){
        window.history.back();
    };
    
    //科技领域点击事件
    function dealAreaClickEvent(){
        $(".dev_area").click(function(){//绑定点击事件
            window.location.href = "/pub/find/main?des3AreaId=" + $(this).attr("value")+"&areaName="+$.trim($(this).text());
        });
    }
    
    </script>
</head>
<body>
  <form id="pub_search" method="post" action="/pubweb/mobile/ajaxconditions">
    <input type="hidden" id="scienceAreaIds" name="scienceAreaIds" value="${scienceAreaIds}" />
  </form>
  <div class="new_subject-field">
    <div class="provision_container-title">
      <i class="material-icons" onclick="goback();">keyboard_arrow_left</i> <span>论文发现</span> <i></i>
    </div>
    <div class="new_subject-field_checked-container_body">
      <div class="new_subject-field_checked-container_detail" id="scroller">
        <c:forEach items="${allAreas }" var="itemMap" varStatus="status">
          <div class="new_subject-field_item-title" value="${itemMap.first.des3AreaId}">
            <span>${itemMap.first.categoryZh}</span>
          </div>
          <div class="new_subject-field_item-container">
            <c:forEach items="${itemMap.second }" var="secondArea" varStatus="secondStatus">
              <div class="new_subject-field_item-detail dev_area" value="${secondArea.des3AreaId}" parentid="${itemMap.first.des3AreaId}">${secondArea.categoryZh}</div>
            </c:forEach>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</body>
</html>