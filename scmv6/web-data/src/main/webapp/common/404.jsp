<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="res" value="/resscmwebsns" />
<c:set var="locale"><%=org.springframework.context.i18n.LocaleContextHolder.getLocale().toString() %></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<title>Scholar 404-error</title>
<link href="${res}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/all.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/public.css" rel="stylesheet" type="text/css" />
<%-- 	<link href="${res}/css/header.css" rel="stylesheet" type="text/css" /> --%>
<link href="${res}/css/footer.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/error.css" rel="stylesheet" type="text/css" />
</head>
<body>
  <fmt:bundle basename="resource.jsp500Resource">
    <div class="error_box">
      <div class="error_pic">
        <img src="${res}/images_v5/error/error_fgx.gif" width="10" height="221" />
      </div>
      <div class="error_contant">
        <p style="font-size: 24px; font-family: '微软雅黑', '黑体', Arial; color: #ff6600; font-weight: bold;">您所访问的页面不存在</p>
        <p>
          请您再次核对您要访问的网址，或者发送EMAIL到<a href="mailto:support@scholarmate.com">support@scholarmate.com</a>联系我们的客服人员。
        </p>
        <div class="error_btn" style="margin-top: 40px;">
          <a href="/scmwebsns"><span class="back_icon">返回首页</span></a> <a onclick="javascript:window.history.back();"><span
            class="back_last">返回上一页</span></a>
        </div>
      </div>
      <div class="clear" style="height: 1px; overflow: hidden;"></div>
    </div>
  </fmt:bundle>
</body>
</html>