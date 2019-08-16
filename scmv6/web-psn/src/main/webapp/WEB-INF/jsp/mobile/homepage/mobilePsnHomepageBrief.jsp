<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<%@ include file="/common/taglibs.jsp"%>
<head>
<script type="text/javascript">
  function moreBrief(){
	  var brief = $("#briefId").val();
	  $("#moreBriefId").html(brief);
	  
  }
</script>
</head>
<body>
  <input type="hidden" id="briefId" value="${ person.brief}">
  <c:if test="${fn:length(person.brief) >110}">
    <li>
      <h3>个人简介</h3> <span id="moreBriefId"> ${ fn:substring(person.brief, 0, 110)}...&nbsp;&nbsp;<a
        href="javascript:void();" onclick="moreBrief()">更多</a></span>
    </li>
  </c:if>
  <c:if test="${!empty person.brief  }">
    <c:if test="${fn:length(person.brief) <111}">
      <li>
        <h3>个人简介</h3> <span class="friend-personal__profile">${person.brief }</span>
      </li>
    </c:if>
  </c:if>
</body>
</html>