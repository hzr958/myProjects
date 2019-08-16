<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta content="<spring:message code="keywords"/>" name="Keywords" />
<meta content="<spring:message code="description"/>" name="description" />
<title><spring:message code="title" /></title>
<%-- <link href="${res}/css_v5/header.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/common.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/public.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/home/home.css" rel="stylesheet" type="text/css" />
<link href="${res }/css_v5/page.css" rel="stylesheet" type="text/css" />
<link href="${res}/css_v5/index.css" rel="stylesheet"	type="text/css" />
<link href="${res}/css_v5/login.css" rel="stylesheet"	type="text/css" />
<link href="${res}/css_v5/agency.css" rel="stylesheet" type="text/css" />

 --%>
<link href="${resmod}/css/newsvm3/login.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/newsvm3/agency.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/newsvm3/public.css" rel="stylesheet" type="text/css" />
<script src="${res}/js_v5/jquery.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#mainForm").attr("action",window.location.href);
		
	});
	
</script>
</head>
<body>
  <div id="scholarmate_hearder">
    <div class="scholarmate_top">
      <div class="sm_logo_${locale}">
        <a alt="<spring:message code="homePage"/>" title="<spring:message code="homePage"/>"
          href="http://www.scholarmate.com"></a>
      </div>
      <div class="sm_top_nav">
        <span id="back_span"> <a rel="nofollow" href="http://www.scholarmate.com"><spring:message
              code="backToHome" /></a> |
        </span> <a rel="nofollow" target="_blank" href="/help">&nbsp;<spring:message code="help" />&nbsp;
        </a> | <a href="pub_A_${locale=='en_US'?'zh_CN':locale}.html">&nbsp;<spring:message code="lanVersion" />&nbsp;
        </a>
      </div>
    </div>
  </div>
  <div class="agency_box">
    <div class="menu">
      <div class="menu-bjleft"></div>
      <div class="menu-bjcenter">
        <div class="m-text">
          <span class="fcu14"><spring:message code="viewPublication" /></span>
        </div>
      </div>
      <div class="menu-bjright"></div>
    </div>
    <div class="agency_bg01"></div>
    <div class="agency_bg02">
      <!-- 新增字母和搜索 -->
      <div class="agency_search" style="margin: 0px 20px; display: none;">
        <div class="ag_search_box">
          <span class="cuti Fleft">站内成果搜索：</span> <input id="keyword" name="keyword" type="text" class="inp_text"
            style="width: 270px; height: 22px; line-height: 22px;" /> <a href="javascript:searchPub(1);"
            class="uiButton uiButtonConfirm mleft5" title="搜索" style="padding: 3px 25px;">搜索</a>
        </div>
        <div class="clear" style="height: 0px; overflow: hidden;"></div>
      </div>
      <div class="achievement_list">
        <c:forEach items="${pdwhPubSeoVo.page.result}" var="pub">
          <ul>
            <li><a href="${pub.indexUrl}" title="${pub.title}" class="Blue" target="_blank">${pub.title}</a></li>
          </ul>
        </c:forEach>
      </div>
      <div style="height: 1px; overflow: hidden;" class="clear"></div>
    </div>
    <div class="agency_bg03"></div>
  </div>
  <div id="footer">
    <div class="box_footer">
      <div class="footer-left" style="width: 60%;">
        &copy;2019 <a target="_blank" class="Blue" href="http://www.irissz.com"><spring:message code="company" /></a>
        <spring:message code="backUpNoNew" />
        <img src="${res}/images_v5/beian/beian.png" style="width: 12px;" />
        <spring:message code="backUpNo" />
      </div>
      <div class="footer-right">
        <a target="_blank" class="Blue" href="${resmod}/html/policy_zh_CN.html"> <spring:message code="privacy" />
        </a> | <a target="_blank" class="Blue" href="${resmod}/html/condition_zh_CN.html"> <spring:message code="terms" />
        </a> | <a target="_blank" class="Blue" href="${resmod}/html/contact_zh_CN.html"> <spring:message code="contact" />
        </a>
        <%-- |
				<a target="_blank" class="Blue"
					href="${res}/html/res_download_zh_CN.html"> <spring:message code="download"/> </a> --%>
      </div>
    </div>
  </div>
</body>
</html>
