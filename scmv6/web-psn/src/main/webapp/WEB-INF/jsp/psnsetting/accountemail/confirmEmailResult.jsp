<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%/**确认邮件**/ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="${res }/css_v5/error.css" rel="stylesheet" type="text/css" />
<title>科研之友</title>
</head>
<body>
  <div id="content">
    <div class="system-point">
      <c:choose>
        <c:when test="${confirmResult eq 'success'}">
          <div class="ico_note"></div>
          <div class="system-content">
            <p class="cu14">
              <s:text name="email.stutas.emailConfirmSuccess" />
            </p>
            <p class="f666">
              <s:text name="email.label.click" />
              <a href="/oauth/index" class="Blue"><s:text name="email.label.here" /></a>
              <s:text name="email.label.home" />
            </p>
          </div>
        </c:when>
        <c:when test="${confirmResult eq 'error'}">
          <div class="nofind_pic"></div>
          <div class="system-content" style="width: 800px;">
            <p class="cu14">
              <s:text name="email.stutas.emailConfirmFail" />
            </p>
            <p class="f666">
              <s:text name="email.label.click" />
              <a href="/oauth/index" class="Blue"><s:text name="email.label.here" /></a>
              <s:text name="email.label.home" />
            </p>
          </div>
        </c:when>
        <c:otherwise>
          <div class="nofind_pic"></div>
          <div class="system-content" style="width: 800px;">
            <p class="cu14">
              <s:text name="email.stutas.emailInvalid" />
            </p>
            <p class="f666">
              <s:text name="email.label.click" />
              <a href="/oauth/index" class="Blue"><s:text name="email.label.here" /></a>
              <s:text name="email.label.home" />
            </p>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>
