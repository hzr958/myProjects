<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta name="keywords"
  content="科研之友，科研社交网络，科研创新生态环境，学术推广，科研推广，学者推广，科研合作，成果推广，论文引用，基金机会，科研项目，科研成果，科研诚信，同行专家，科研系统，科研项目申请书，ISIS系统。" />
<meta name="description" content="科研之友：科研社交网络，成就科研梦想。学术推广，科研推广，学者推广，科研合作，成果推广，论文引用，基金机会，科研项目，科研成果，科研系统。" />
<title><s:text name="forgetpassword.label.resetpwd" /> | <s:text name="skin.main.title_scm" /></title>
<link href="${resmod}/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/scmjscollection.css" rel="stylesheet" type="text/css" />
</head>
<body>
  <%@ include file="/common/header.jsp"%>
  <div class="content-1200 content-special__style">
    <div class="fillin-left tip__box tip__container-content" style="margin-bottom: 81px;">
      <div class="tip__container-box">
        <div class="tip__container">
          <div class="tip__icon"></div>
          <div class="tip__icon-title">
            <s:text name="forgetPassword.label.resetPwdSuccess" />
          </div>
        </div>
        <div class="tip__container-size">
          <s:text name="forgetPassword.label.resetPwdSuccessTip" />
          <a href="/oauth/index"> <s:text name="forgetPassword.link.loginNow" />
          </a>
        </div>
      </div>
    </div>
    <div class="fillin-right">
      <h2>
        <s:text name="forgetPassword.label.joinus" />
      </h2>
      <ul>
        <li><i class="icon-join-related01"></i> &nbsp; <s:text name="forgetPassword.label.keepInTouch" /></li>
        <li><i class="icon-join-related02"></i> &nbsp; <s:text name="forgetPassword.label.generalizeResearchPub" />
        </li>
        <li><i class="icon-join-related03"></i> &nbsp; <s:text name="forgetPassword.label.getOpportunities" /></li>
      </ul>
    </div>
  </div>
  <%@include file="/common/footer.jsp"%>
</body>
</html>
