<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
</head>
<body>
  <div id="box_main">
    <s:if test="forwardUrl!=''">
      <script>
		window.open("${forwardUrl}");
		<s:if test="backUrl!=''">
			history.back(-1);
		</s:if>
	</script>
    </s:if>
  </div>
</body>
</html>
