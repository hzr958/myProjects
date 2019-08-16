<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib
  uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ include file="/common/taglibs.jsp"%>
<s:set var="SESSION_MENU_ID" value="#session['##SESSION_MENU_ID']" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=10;IE=9; IE=8; IE=EDGE" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="keywords" content="科研" />
<meta http-equiv="content-style-type" content="text/css" />
<c:choose>
  <c:when test="${!empty userRolData.rolLogoUrl and userRolData.fromIsis eq 'nsfcscm'}">
    <c:set var="pageTitle">
      <s:text name="skin.main.title_nsfcscm" />
    </c:set>
    <c:set var="pageHeaderMargin">margin-top:0px;</c:set>
    <c:set var="pageTopLeftContent">
      <div class="unit_logo">
        <img src="${resmod}/images/logo_kyzx.gif" height="50" title="<s:text name='skin.main.title_nsfcscm' />"
          alt="<s:text name='skin.main.title_nsfcscm' />" />
      </div>
    </c:set>
    <c:set var="pageTopRightScmLogo">
      <div class="sm_logo2">
        <a href="${snsDomain}/scmwebsns/main?rolInsId=0"><img src="${resmod}/images/rol_home/s_logo.gif" width="261"
          height="27" alt="<s:text name="skin.main.title_scm" />" title="<s:text name="skin.main.title_scm" />" /></a>
      </div>
    </c:set>
    <c:set var="selRol" value="no" />
  </c:when>
  <c:when test="${!empty userRolData.rolLogoUrl and userRolData.fromIsis eq 'nsfcr'}">
    <c:set var="pageTitle">
      <s:text name="skin.main.title_nsfcr" />
    </c:set>
    <c:set var="pageHeaderMargin">margin-top:0px;</c:set>
    <c:set var="pageTopLeftContent">
      <div class="unit_logo">
        <img src="${resmod }/images/logo_cgzx.gif" height="50" title="<s:text name='skin.main.title_nsfcr' />"
          alt="<s:text name='skin.main.title_nsfcr' />" />
      </div>
    </c:set>
    <c:set var="pageTopRightScmLogo">
      <div class="sm_logo2">
        <a href="${snsDomain}/scmwebsns/main?rolInsId=0"> <img src="${resmod }/images/rol_home/s_logo.gif"
          width="261" height="27" alt="<s:text name="skin.main.title_scm" />"
          title="<s:text name="skin.main.title_scm" />" />
        </a>
      </div>
    </c:set>
    <c:set var="selRol" value="no" />
  </c:when>
  <c:when test="${!empty userRolData.rolLogoUrl}">
    <c:set var="pageTitle">${userRolData.rolTitle }</c:set>
    <c:set var="pageHeaderMargin">margin-top:0px;</c:set>
    <c:set var="pageTopLeftContent">
      <div class="unit_logo">
        <img src="${userRolData.rolLogoUrl }" height="50" title="${userRolData.rolTitle }"
          alt="${userRolData.rolTitle }" />
      </div>
    </c:set>
    <c:set var="pageTopRightScmLogo">
      <div class="sm_logo2">
        <a href="${snsDomain}/scmwebsns/main?rolInsId=0"><img src="${resmod}/images/rol_home/s_logo.gif" width="261"
          height="27" alt="<s:text name="skin.main.title_scm" />" title="<s:text name="skin.main.title_scm" />" /></a>
      </div>
    </c:set>
  </c:when>
  <c:when test="${!empty userRolData.rolTitle}">
    <c:set var="pageTitle">${userRolData.rolTitle }</c:set>
    <c:set var="pageHeaderMargin">margin-top:0px;</c:set>
    <c:set var="pageTopLeftContent">
      <div class="unit_logo">
        <!-- 修改了单位标题显示内容(当前单位标题值为空时显示其他语言的单位名_MaoJianGuo_2013-01-30_ROL-396 -->
        <c:choose>
          <c:when test="${locale eq 'en_US' }">
            <span
              style="font-family: arial, Tahoma, Verdana, simsun, sans-serif; font-size: 26px; line-height: 50px; color: #395d94;">
              <c:choose>
                <c:when test="${!empty userRolData.rolTitleEn}">
							${userRolData.rolTitleEn}
							</c:when>
                <c:otherwise>
							${userRolData.rolTitleCh}
							</c:otherwise>
              </c:choose>
            </span>
          </c:when>
          <c:otherwise>
            <span style="font-size: 26px; color: #395d94; line-height: 50px; font-family: '微软雅黑', '黑体', '楷体';"> <c:choose>
                <c:when test="${!empty userRolData.rolTitleCh}">
							${userRolData.rolTitleCh}
							</c:when>
                <c:otherwise>
							${userRolData.rolTitleEn}
							</c:otherwise>
              </c:choose>
            </span>
          </c:otherwise>
        </c:choose>
      </div>
    </c:set>
    <c:set var="pageTopRightScmLogo">
      <%-- <div class="sm_logo2"><a href="${snsDomain}/scmwebsns/main?rolInsId=0"><img src="${resmod}/images/rol_home/s_logo.gif" width="261" height="27" alt="<s:text name="skin.main.title_scm" />" title="<s:text name="skin.main.title_scm" />"/></a></div> --%>
      <a alt="<s:text name="skin.main.title_scm" />" title="<s:text name="skin.main.title_scm" />"></a>
    </c:set>
  </c:when>
  <c:otherwise>
    <c:set var="pageTitle">
      <s:text name="skin.main.title_scm" />
    </c:set>
    <c:set var="pageHeaderMargin" value=""></c:set>
    <c:set var="pageTopLeftContent">
      <a href="${snsctx}" title="<s:text name="skin.main.title_scm" />" alt="<s:text name="skin.main.title_scm" />">
        <c:choose>
          <c:when test="${locale eq 'en_US' }">
            <img src="${resmod }/images/logo_${locale }.gif" title="科研之友" />
          </c:when>
          <c:otherwise>
            <img src="${resmod }/images/logo_${locale }.gif" width="277" height="20" title="科研之友" />
          </c:otherwise>
        </c:choose>
      </a>
    </c:set>
    <c:set var="pageTopRightScmLogo" value="" />
  </c:otherwise>
</c:choose>
<title>${pageTitle }</title>
<c:choose>
  <c:when test="${!empty userRolData.rolLogoUrl and userRolData.fromIsis eq 'nsfcscm'}">
    <link href="${resmod}/css/header.css" rel="stylesheet" type="text/css" />
  </c:when>
  <c:when test="${!empty userRolData.rolLogoUrl and userRolData.fromIsis eq 'nsfcr'}">
    <link href="${resmod }/css/header.css" rel="stylesheet" type="text/css" />
  </c:when>
  <c:when test="${!empty userRolData.rolTitle}">
    <link href="${resmod }/css/header_sns.css" rel="stylesheet" type="text/css" />
  </c:when>
  <c:otherwise>
    <link href="${resmod }/css/header_sns.css" rel="stylesheet" type="text/css" />
  </c:otherwise>
</c:choose>
<link href="${resmod }/css/common.css" rel="stylesheet" type="text/css" />
="text/css">
<link href="${resmod }/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/home.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/plugin/jquery.browser.tips.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/smate.scmtips.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/organization.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/smate.alerts.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
//定义环境变量
var lang = "${lang}";
var isSame="${userRolData.des3PsnId}";
var handle;
var refreshCount=1;
//以上变量为v5版本之前用到的环境变量.以下是v5版本增加的环境变量.
var snsctx='${snsctx}';
var resmod ='${resmod}';
var ressns ='${ressns}';
var locale='${locale}';
var domCas='${domaincas}';
var logoutindex='${logoutindex}';
var domainOauth='${domainoauth}';
</script>
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/json2.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.watermark.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.baidu.tongji.js"></script>
<script type="text/javascript" src="${resmod}/js/common.ui.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod}/js/msgbox/msgbox.tip_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/msgbox/msgbox.common.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.browser.tips.js"></script>
<script type="text/javascript" src="${resmod }/js/googleAnalytics.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.menu.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.index.maint.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.maint.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.cookie.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.alerts_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.proceeding.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.proceedingwin.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.page.js"></script>
<script type="text/javascript" src="${ressns}/js/inspg/inspgstatistics.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.module.loaddiv.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js/link.status.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/judge-browser/judge-browser.js"></script>
<script type="text/javascript">
var msg_common_reminder='<s:text name="dialog.manageTag.tip.cfmDelete.title"/>';
var msgBoxIsClose = parseInt(MsgBoxUtil.getCookie("msgBoxIsClose") == null 
		? 0 : MsgBoxUtil.getCookie("msgBoxIsClose")); //消息弹出框是否点击了关闭.
var scm_url='${snsDomain}/scmwebsns/main?rolInsId=0';		
var online_help_url="http://wpa.qq.com/msgrd?v=3&uin=800018382&site=qq&menu=yes";
</script>
<script type="text/javascript">
$(document).ready(function(){
});

</script>
<%@ include file="refreshSession.jsp"%>
<%--以下业务块的head内容 --%>
<decorator:head />
</head>
<body>
  <!-- 节点id inspg outside-->
  <input id="nodeUrlId" type="hidden" value="<irisnode:node />" />
  <!-- HEADER START -->
  <s:if test="userRolData == null">
    <%@ include file="/common/main_inspg_outside_header.jsp"%>
  </s:if>
  <s:else>
    <%@ include file="/common/main_inspg_header.jsp"%>
  </s:else>
  <!-- HEADER END -->
  <%--加载的主体内容 --%>
  <decorator:body />
  <!-- FOOTER START -->
  <jsp:include page="/common/footer.jsp">
    <jsp:param name="selRol" value="${selRol}" />
  </jsp:include>
  <!-- FOOTER END -->
</body>
</html>