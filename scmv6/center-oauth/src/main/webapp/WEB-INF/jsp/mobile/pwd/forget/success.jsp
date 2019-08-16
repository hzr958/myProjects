<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
</head>
<body class="bdbg">
  <div class="reset_tip">
    <i class="reset_tip_icon"></i>
    <h2>重设密码邮件已发送至你的邮箱</h2>
    <p>如果你没有收到我们的邮件，请检查垃圾邮箱</p>
    <p>
      还没有收到我们的邮件？<a href="/oauth/mobile/pwd/forget/index?email=${request.resendEmail}">重新发送邮件</a>
    </p>
    <!--         <p>还是无法登陆账号？<a target="_blank" href="https://www.scholarmate.com/resscmwebsns/html/contact_zh_CN.html">联系我们客服</a></p>
 -->
  </div>
</body>
</html>