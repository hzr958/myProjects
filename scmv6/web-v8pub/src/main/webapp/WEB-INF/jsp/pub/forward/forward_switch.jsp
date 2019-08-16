<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<head>
</head>
<body>
  <div id="box_main">
    <c:if test="${forwardUrl!=''}">
      <script>
		window.location.href = "${forwardUrl}";
	</script>
    </c:if>
  </div>
</body>
</html>
