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
    <input type="hidden" value="${userRolData.rolInsId }" id="orolInsId" />
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
<link href="${resmod }/css/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/home.css" rel="stylesheet" type="text/css" />
<link href="${resscmsns }/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resscmsns }/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resscmsns }/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/header_sns.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/page.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/plugin/jquery.browser.tips.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/smate.scmtips.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/organization.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/smate.alerts.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/achievement.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/css/scmjscollection.css" rel="stylesheet" type="text/css" />
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
var resscmsns='${resscmsns}';
var locale='${locale}';
var domCas='${domaincas}';
var domscm='${domainscm}';
var logoutindex='${logoutindex}';
var searchType="输入姓名、所属机构查找人员";
var searchType2="输入关键词、作者、DOI查找论文";
var searchType3="输入关键词、作者、专利号查找专利";
var domainOauth='${domainoauth}';
</script>
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery.browser.tips.js"></script>
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
var msg_common_timeout='<s:text name="sns.tip.timeout"/>';
var msg_common_reminder='<s:text name="dialog.manageTag.tip.cfmDelete.title"/>';
var msgBoxIsClose = parseInt(MsgBoxUtil.getCookie("msgBoxIsClose") == null 
		? 0 : MsgBoxUtil.getCookie("msgBoxIsClose")); //消息弹出框是否点击了关闭.
var scm_url='${snsDomain}/scmwebsns/main?rolInsId=0';		
var online_help_url="http://wpa.qq.com/msgrd?v=3&uin=800018382&site=qq&menu=yes";
</script>
<script type="text/javascript">
$(document).ready(function(){	  	
	var searchCode;
	var href=window.location.href;
	$("#to_login").attr("href","${domainscm}"+snsctx+"/index?service="+encodeURIComponent(href));

	//绑定检索人员标签响应事件.
	ScmMaint.searchSomeOneBind(); 	
	ScmMaint.bindMainLabels();
	//默认绑定
	$('#search_some_one').css("color","#999");
	$("#search_some_one").val("输入关键词检索论文、专利、专家、机构...");
	var searchString = $("#searchString").val();
 	if(searchString!="" && searchString !=searchCode){
	/* 	$("#search_some_one").css("color","#000000"); */
		$("#search_some_one").val($("#searchString").val());
	}  
 	
 	$("#show_select").click(function(e){
		var _obj = $("#home_search_items");
		// 显示隐藏
		if (_obj.is(":hidden")) {
			_obj.show();
		} else {
			_obj.hide();
			return;
		}
		//点击其他地方关闭
	    if (e && e.stopPropagation) {//非IE  
	        e.stopPropagation();  
	    }  
	    else {//IE  
	        window.event.cancelBubble = true;  
	    } 
	    $(document).click(function(){_obj.hide();});
	});
});


function select_search_menu(url){
	var userInfo=$("#search_some_one").val();
	if(userInfo==searchType || userInfo==searchType2 || userInfo==searchType3){
		$("#search_some_one").val("");
	}
	$("#search_some_one").val($("#searchString").val());  
	$("#search_some_one_form").attr("action",url);
	$("#search_some_one_form").submit();
}

</script>
<%@ include file="refreshSession.jsp"%>
<%--以下业务块的head内容 --%>
<decorator:head />
</head>
<body>
  <!-- 节点id  skin_main.jsp-->
  <input id="nodeUrlId" type="hidden" value="<irisnode:node />" />
  <input id="des3PsnId" type="hidden" value="${userRolData.des3PsnId }" />
  <input id="rolDomain" type="hidden" value="${userRolData.rolDomain}" />
  <div class="">
    <div class="header-t" style="height: 44px; position: relative;">
      <div class="header_wrap-t" style="height: 44px;">
        <a href="${snsctx }/main" class="logo fl"></a>
        <div class="search fl">
          <div id="divselect1" class="fl">
            <span class="tp_box" id="show_select"><i id="search_type_img" class="lt_icon"></i></span>
            <ul id="home_search_items" style="display: none">
              <li onclick="ScmMaint.select_search_paper()"><a href="javascript:void(0);" selectid="1"><i
                  class="lw_icon"></i>论文</a></li>
              <li onclick="ScmMaint.select_search_patent()"><a href="javascript:void(0)" selectid="2"><i
                  class="zl_icon"></i>专利</a></li>
              <li onclick="ScmMaint.select_search_psn()"><a href="javascript:void(0)" selectid="3"><i
                  class="ry_icon"></i>人员</a></li>
            </ul>
          </div>
          <form id="search_some_one_form" action="/pub/search/pdwhmain" method="get">
            <input id="search_some_one" value="" name="searchString" title="输入关键词检索论文、专利、专家、机构..." type="text" />
          </form>
          <a href="javascript:void(0)" class="s_btn" onclick="ScmMaint.searchSomeOne()"></a>
        </div>
        <div class="login fr">
          <a href="#" class="ky_top"></a>
          <p>
            <a id="to_login" href="">请登录</a> <a href="${snsctx }/register/register">免费注册</a>
          </p>
        </div>
      </div>
    </div>
  </div>
  <div class="clear_h10"></div>
  <%--加载的主体内容 --%>
  <decorator:body />
  <div class="clear_h20"></div>
  <!-- FOOTER START -->
  <jsp:include page="/common/footer.jsp"></jsp:include>
  <!-- FOOTER END -->
</body>
</html>