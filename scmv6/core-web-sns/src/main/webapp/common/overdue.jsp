<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>科研之友</title>
</head>
<body>
  <div style="height: 600px;">
    <div class="tips" style="width: 600px; margin: 0 auto; margin-top: 100px;">
      <div class="tips_close"></div>
      <div class="tips_info">
        <ul>
          <c:if test="${locale =='zh_CN' }">
            <li class="tips_wrong">你访问的链接已经失效!</li>
          </c:if>
          <c:if test="${locale =='en_US' }">
            <li class="tips_wrong">The hyperlink is expired.</li>
          </c:if>
        </ul>
      </div>
    </div>
  </div>
</body>
</html>