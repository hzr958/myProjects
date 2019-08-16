<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="res" value="/resscmwebsns" />
<c:set var="locale"><%=org.springframework.context.i18n.LocaleContextHolder.getLocale().toString() %></c:set>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Exception) request.getAttribute("javax.servlet.error.exception");

	//记录日志
	if (ex != null) {
		Logger logger = LoggerFactory.getLogger("500.jsp");
		logger.error(ex.getMessage(), ex);
	}
	//是否是ajax请求
	String uri = (String)request.getAttribute("action_request_uri");
	if(uri!= null && uri.toLowerCase().indexOf("ajax") > -1){
		request.setAttribute("is_ajax_req", true);
	}else{
		request.setAttribute("is_ajax_req", false);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<title>Scholar 500-error</title>
<link href="${res}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/all.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/header.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/footer.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/page.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/error.css" rel="stylesheet" type="text/css" />
</head>
<body>
  <fmt:bundle basename="resource.jsp500Resource">
    <c:if test="${is_ajax_req }">
      <div id="content">
        <div style="width: 976px">
          <div class="wrongtext_box">
            <span class="font_24b"><fmt:message key="error.server500ErrorTitle" /></span><br /> <br />
            <fmt:message key="error.server500Error" />
            <br /> <a href="${ctx }"><fmt:message key="error.backhome" /></a>
          </div>
        </div>
      </div>
    </c:if>
    <c:if test="${!is_ajax_req }">
      <div class="eror_main">
        <div style="margin-left: 90px;">
          <div class="error_pic2">
            <img src="${res }/images_v5/error/role_fgx2.gif" width="8" height="220" />
          </div>
          <div class="error_contant">
            <p style="font-size: 24px; font-family: '微软雅黑', '黑体', Arial; color: #ff6600; font-weight: bold;">
              <fmt:message key="error.server500Error"></fmt:message>
            </p>
            <p>
              <fmt:message key="error.server500Error" />
            </p>
            <div class="error_btn">
              <a href=""><span class="back_icon"><fmt:message key="error.backhome" /></span></a> <a
                onclick="javascript:window.history.back();"><span class="back_last"><fmt:message
                    key="error.backprepage" /></span></a>
            </div>
          </div>
          <div class="clear" style="height: 1px; overflow: hidden;"></div>
        </div>
      </div>
    </c:if>
  </fmt:bundle>
</body>
</html>
