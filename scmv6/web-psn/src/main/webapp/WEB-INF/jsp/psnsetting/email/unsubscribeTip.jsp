<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>确认邮件是否退订</title>
</head>
<body>
  <div id="content">
    <div class="system-point">
      <div class="system-content">
        <p class="f666 mtop5" style="font-size: 18px; text-align: center">
          <s:text name='sub.mail.tip' />
          &nbsp;
        </p>
        <div class="request__actions" style="margin: 40px auto 0px;">
          <a href="${domainscm}">
            <div class="button_main button_dense button_primary-changestyle">
              <span><s:text name="sub.sys.cancel"></s:text></span>
            </div>
          </a> <a href="${unsubscribeMailUrl}" style="margin-left: 20px;">
            <div class="button_main button_dense button_grey">
              <span><s:text name="sub.sys.continue"></s:text></span>
            </div>
          </a>
        </div>
      </div>
    </div>
  </div>
</body>
</html>