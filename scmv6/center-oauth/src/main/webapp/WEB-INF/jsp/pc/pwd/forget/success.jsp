<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta name="keywords"
  content="科研之友，科研社交网络，科研创新生态环境，学术推广，科研推广，学者推广，科研合作，成果推广，论文引用，基金机会，科研项目，科研成果，科研诚信，同行专家，科研系统，科研项目申请书，ISIS系统。" />
<meta name="description" content="科研之友：科研社交网络，成就科研梦想。学术推广，科研推广，学者推广，科研合作，成果推广，论文引用，基金机会，科研项目，科研成果，科研系统。" />
<title><s:text name="forgetPassword.label.forgetpassword" /> | <s:text name="skin.main.title_scm" /></title>
<link href="${resmod}/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/scmjscollection.css" rel="stylesheet" type="text/css" />
<style type="text/css">
ol.list {
  margin-left: 20px;
}

ol.list>li {
  list-style-type: decimal;
  list-style-position: inside;
}

.actions_link {
  color: #288AED;
  margin-left: 3px;
  cursor: pointer;
  text-decoration: none;
}

.actions_link:hover {
  
}
</style>
</head>
<body>
  <%@ include file="/common/header.jsp"%>
  <div class="content-1200 content-special__style">
    <div class="fillin-left tip__box" style="margin-bottom: 81px;">
      <div class="tip__container">
        <div class="tip__icon"></div>
        <div class="tip__icon-title">
          <s:text name="forgetPassword.label.pwdResetEmailHadSend" />
          &nbsp;
        </div>
        <div>${email}</div>
      </div>
      <div class="tip__container-size">
        <s:text name="forgetPassword.label.clickResetPwdUrl" />
      </div>
      <div class="tip__container-size">
        <div class="tip__container-size">
          <s:text name="forgetPassword.label.receiveWithoutTime" />
        </div>
        <ol class="list">
          <li><s:text name="forgetPassword.label.checkTrash" /></li>
          <li><span> <s:text name="forgetPassword.label.contact" />
          </span> <a class="actions_link"
            onclick="javascript:window.open('http://crm2.qq.com/page/portalpage/wpa.php?uin=800018382&amp;cref=http://node-web6/node-web6/dynweb/main&amp;ref=&amp;pt=scholarmate kefu&amp;f=1&amp;ty=1&amp;ap=&amp;as=&amp;aty=&amp;a=', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');">
              <s:text name="forgetPassword.label.customerService" />
          </a> <span class="tip__container-worktime"> <s:text name="forgetPassword.label.note" />
          </span></li>
        </ol>
      </div>
    </div>
    <div class="fillin-right" style="margin-bottom: 81px;">
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