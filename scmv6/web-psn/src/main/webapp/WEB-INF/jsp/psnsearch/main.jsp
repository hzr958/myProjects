<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=10;IE=9; IE=8; IE=EDGE" />
<title>人员检索</title>
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/index.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/header.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/home.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/login.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/agency.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/plugin/jquery.validate.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/common.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css/plugin/jquery.thickbox.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/jquery.scmtips.css" />
<script src="${resmod }/js/jquery.js"></script>
<script src="${resmod }/js/smate.index.maint.js"></script>
<script type="text/javascript" src="${resmod }/js/googleAnalytics.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.baidu.tongji.js"></script>
<script type="text/javascript" src="${resmod}/js/userSearch/userSearch.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.scmtips.js"></script>
<script type="text/javascript">
var ctx = '${ctx}';
var ctxpath = '${ctx}';
var respath = '${res}';
var locale='${locale}';
var domCas='${domaincas}';
var domSns='${domainsns}';
var logoutindex='${logoutindex}';
var domainOauth='${domainoauth}'


var input_eptinf='<s:text name="main.page.label.inf2"/>';
var inviteInf='<s:text name="main.page.label.inf14"/>';
$(document).ready(function(){
	/* UserSearch.init("${snsctx}","${resmod}",${resultSize},${startIndex},${searchSize}); */
});




</script>
</head>
<body>
  <div id="scholarmate_hearder">
    <div class="scholarmate_top">
      <div class="sm_logo_${locale }">
        <a href="${snsDomain}/scmwebsns/main?rolInsId=0" title="<s:text name='conmon.logo.top' />"
          alt="<s:text name='conmon.logo.top' />"></a>
      </div>
      <div class="sm_top_nav">
        <%--增加返回首页的链接_MJG_2013-06-17_SCM-2719 --%>
        <span id="back_span" style="display: none;"><a href="${snsDomain}/scmwebsns/main?rolInsId=0"
          rel="nofollow"><s:text name="home.index.search.label.back" /></a> |</span>
        <c:if test="${locale=='zh_CN'}">
          <!-- scm-6659 -->
          <a href="${res}/html/helpCenter/index.html" target="_blank" rel="nofollow"> <s:text
              name="home.index.helpCenter" />
          </a> | 
				</c:if>
        <!-- scm-6659 -->
        <c:if test="${locale=='en_US'}">
          <a href="${res}/html/helpCenter/index_en_US.html" target="_blank">&nbsp;Learning Center&nbsp;</a>|
				</c:if>
        <%-- <a href="${res}/html/res_scm_zh_CN.html" target="_blank">&nbsp;<s:text
						name="home.index.resources" />&nbsp;</a> |  --%>
        <c:if test="${locale=='en_US'}">
          <a onclick="SCMIndex.change_locol_language('zh_CN');">&nbsp;<s:text name="home.index.chinese" />&nbsp;
          </a>
        </c:if>
        <c:if test="${locale=='zh_CN'}">
          <a onclick="SCMIndex.change_locol_language('en_US');">&nbsp;English&nbsp;</a>
        </c:if>
      </div>
    </div>
  </div>
  <div class="menu">
    <div class="menu-bjleft"></div>
    <div class="menu-bjcenter">
      <div class="m-text">
        <span class="fcu14"><s:text name='page.main.search' /></span>
      </div>
      <div class="mailbox">
        <span class="fcu14"><s:text name="page.label.login.dream" /></span>
      </div>
    </div>
    <div class="menu-bjright"></div>
  </div>
  <input type="hidden" id="pre_user_info" value="<c:out value='${userInfo }'/>" />
  <div id="zhuce">
    <div class="z-left">
      <table width="729" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="23" valign="top"><img src="${resmod}/images/login_nrbj2.gif" width="729" height="23" /></td>
        </tr>
        <tr>
          <td align="left" valign="top" class="login_centerbg">
            <!-- 修正切换语言时丢失请求参数的问题_MJG_2013-03-07_SCM-1935 -->
            <form action="/psnweb/personsearch/show" method="get" id="us_main_form"
              onsubmit="return UserSearch.searchPerson(1)">
              <div style="width: 645px; margin: 0px auto;">
                <div class="edit_title">
                  <span class="cu14"><i class="icon35 py-icon"></i> <s:text name="main.page.label.inf2" /></span>
                </div>
                <div class="">
                  <s:text name="main.page.label.inf5" />
                  ： <input id="us_main_input" name="searchName" type="text" class="l_input" maxlength="50"
                    value="<c:out value='${searchName }'/>" />&nbsp;&nbsp;&nbsp;&nbsp;
                  <s:text name="main.page.label.inf5_1" />
                  ： <input id="us_main_input_key" name="searchKey" type="text" class="l_input" maxlength="50"
                    value="<c:out value='${searchKey}'/>" /> <a onclick="UserSearch.searchPerson()"
                    class="uiButton uiButtonConfirm mleft5" title="<s:text name='user.search.btn.search' />"
                    style="padding: 3px 20px;"><s:text name="user.search.btn.search" /></a>
                </div>
                <label id="us_main_error" class="error" style="line-height: 30px; display: none;"></label>
              </div>
            </form> <input id="pageNo" name="pageNo" type="hidden" value="${pageNo }" />
            <div class="person_search">
              <h2>
                <s:text name="main.page.label.inf3" />
              </h2>
              <table id="person_search_tb" width="100%" border="0" cellspacing="0" cellpadding="0" class="per_box">
                <%@include file="./psnList.jsp"%>
              </table>
            </div> <s:if test="psnInfoList==null">
              <div id="us_more_psn_div" class="morenews4">
                <s:text name="main.page.label.inf17"></s:text>
              </div>
            </s:if> <s:if test="psnInfoList!=null">
              <s:if test="searchResultList.size()==0">
                <div id="us_more_psn_div" class="morenews4">
                  <s:text name="main.page.label.inf17"></s:text>
                </div>
              </s:if>
              <s:elseif test="psnInfoList.size()>=20">
                <div id="us_more_psn_div" class="morenews4">
                  <a id="us_more_psn_link" href="javascript:void(0)" onclick="UserSearch.searchMorePerson()"
                    class="Blue font14"><s:text name="main.page.label.inf4" /></a> <img id="us_more_psn_lodding"
                    src="${resmod }/images/loading.gif" style="display: none;" />
                </div>
              </s:elseif>
            </s:if>
          </td>
        </tr>
        <tr>
          <td height="6" valign="top"><img src="${resmod}/images/login_nrbj4.gif" width="729" height="6" /></td>
        </tr>
      </table>
    </div>
    <div class="z-right">
      <div class="z-joinbj"></div>
      <div class="join-prompt">
        <div class="info_img">
          <h2>
            <s:text name="main.page.label.inf6" />
          </h2>
          <ul class="re_list">
            <li class="search_icon"><s:text name="main.page.label.inf7" /></li>
            <li class="share_pic"><s:text name="main.page.label.inf8" /></li>
          </ul>
        </div>
        <div class="info_img mtop20">
          <h2>
            <s:text name="main.page.label.inf9" />
          </h2>
          <ul class="re_list">
            <li class="Database"><s:text name="main.page.label.inf10" /></li>
            <li class="pie_img"><s:text name="main.page.label.inf11" /></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
  <div id="footer">
    <div class="box_footer">
      <%--基金委成果在线，需要更换页脚 --%>
      <c:choose>
        <c:when test="${param.selRol eq 'no'}">
          <div class="footer-left">国家自然科学基金委员会 京ICP备05002826号</div>
          <div class="footer-right">
            软件制作:<a target="_blank" class="Blue" href="http://www.irissz.com">爱瑞思软件(深圳)有限公司</a>
          </div>
        </c:when>
        <c:otherwise>
          <div class="footer-left">
            &copy; 2016 <a href="http://www.irissz.com/${locale=='zh_CN'?'':'en/'}" class="Blue" target="_blank"><s:text
                name="page.footer.label.company" /></a>
            <s:text name="page.footer.label.company.info" />
          </div>
          <div class="footer-right">
            <%-- <a href="<s:text name="page.foot.about.link"/>" class="Blue" target="_blank">
						<s:text name="page.foot.aboutUs" />
					</a> |  --%>
            <a href="/resscmwebsns/html/policy_${locale }.html" class="Blue" target="_blank"> <s:text
                name="page.foot.privacv" />
            </a> | <a href="/resscmwebsns/html/condition_${locale }.html" class="Blue" target="_blank"> <s:text
                name="page.foot.terms" />
            </a> | <a href="/resscmwebsns/html/about_us_zh_CN.html" class="Blue" target="_blank"> <s:text
                name="page.foot.aboutUs" />
            </a> | <a href="/resscmwebsns/html/contact_${locale }.html" class="Blue" target="_blank"> <s:text
                name="page.foot.contactUs" />
            </a> | <a href="/resscmwebsns/html/res_download_zh_CN.html" class="Blue" target="_blank"> <s:text
                name="page.foot.download" />
            </a>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>
