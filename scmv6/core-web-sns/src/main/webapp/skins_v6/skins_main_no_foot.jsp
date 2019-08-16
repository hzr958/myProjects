<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta name="keywords" content="科研" />
<meta http-equiv="content-style-type" content="text/css" />
<title><c:if test="${not empty seoTitle}">
    <c:out value="${seoTitle }" escapeXml="true"/>
  </c:if> <c:if test="${empty seoTitle}">
    <s:text name="skin.main.title_scm" />
  </c:if></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css/scmjscollection.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/plugin/jquery.browser.tips.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/smate.scmtips.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/organization.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/smate.alerts.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/achievement.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/plugin/jquery.thickbox.css" rel="stylesheet" type="text/css" />
<!-- 老  -->
<link href="${resmod }/css/header_sns.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css/home.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/footer2016.css" rel="stylesheet" type="text/css" />
<!-- 老 -->
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/css/scm-newpagestyle.css">
<script type="text/javascript">
var snsctx='${snsctx}';
var resmod ='${resmod}';
var ressns ='${ressns}';
var resscmsns='${resscmsns}';
var locale='${locale}';
</script>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_dialogs.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/js/common.ui.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/judge-browser/judge-browser.js"></script>
<script type="text/javascript" src="${resmod}/js/msgbox/msgbox.common.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.cookie.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.alerts_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.proceeding.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.proceedingwin.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${ressns}/js/inspg/inspgstatistics.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.module.loaddiv.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js/newmsg/smate.msg.count.js"></script>
<script type="text/javascript" src="${resmod}/js/search/PdwhPubSearch.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<!-- 老 -->
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.maint.js"></script>
<!-- 老 -->
<script type="text/javascript" src="${resmod}/js/smate.baidu.tongji.js"></script>
<script type="text/javascript" src="${resmod }/js/googleAnalytics.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	//加载消息 
	smate.msgcount.header();
	//在联系人和群组菜单上增加提醒-no -foot
	smate.msgcount.prompt();
	$('#skin_psn_auatars').on('click',function(event){
	    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
	      event.stopPropagation();
          event.preventDefault();
          if($('#skin_psn_setting').is(':hidden')){
               $('#skin_psn_setting').show();
          }else{
              $('#skin_psn_setting').hide();
          }
	    }, 1);
	});
	$(document).on('click',function(e){
		var _target = $(e.target);
        if (_target.closest("#skin_psn_setting").length == 0) {
            $('#skin_psn_setting').hide();
        }
	});
	   document.getElementsByClassName("application_bomb-container")[0].addEventListener("click",function(event){
	        event=event||window.event;
	        event.stopPropagation();
	    });
	    document.getElementsByClassName("application_bomb-body_content-key_icon")[0].onclick = function(event){
	        event.stopPropagation();
	        event.preventDefault();
	        if(document.getElementsByClassName("application_bomb-container").length>0){
	            document.getElementsByClassName("application_bomb-container")[0].style.right = "-350px";
	        }
	        setTimeout(function(){
	            if(document.getElementsByClassName("application_bomb-background").length>0){
	               document.getElementsByClassName("application_bomb-background")[0].style.display = "none";
	            }
	        },100);
	    }
	    document.getElementsByClassName("application_bomb-background")[0].style.height = window.innerHeight + "px";
	    document.getElementsByClassName("application_bomb-container")[0].style.height = window.innerHeight + "px";
	    window.onresize = function(){
	        document.getElementsByClassName("application_bomb-background")[0].style.height = window.innerHeight + "px";
	        document.getElementsByClassName("application_bomb-container")[0].style.height = window.innerHeight + "px";
	    }
});





</script>
<decorator:head />
<%@ include file="/skins/refreshSession.jsp"%>
</head>
<body>
  <header>
    <!--   <div class="header__box depth4"> -->
    <div class="header__box">
      <div class="header__main">
        <div class="header-main__box">
          <jsp:include page="header_info.jsp"></jsp:include>
          <jsp:include page="new_menu.jsp"></jsp:include>
          <jsp:include page="inside_info.jsp"></jsp:include>
        </div>
      </div>
    </div>
    <div class="header__placeholder_inside1"></div>
    <div class="application_bomb-background">
      <div class="application_bomb-container">
        <div class="application_bomb">
          <div class="application_bomb-body">
            <div class="application_bomb-body_content">
              <div class="application_bomb-body_content-key">
                <i class="list-results_close application_bomb-body_content-key_icon"></i>
              </div>
              <div class="application_bomb-body_content-title">
                <s:text name="dyn.main.menu.personal.services" />
              </div>
              <div class="application_bomb-body_content-container">
                <div class="application_bomb-body_content-list">
                  <a onclick="toFunds();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-fund"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <s:text name="dyn.main.menu.fund" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-list">
                  <a onclick="toPrj();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-project"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <s:text name="dyn.main.menu.pro" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-list">
                  <a onclick="toReferences();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-paper"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <s:text name="dyn.main.menu.paper" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-list">
                  <a target="_black" href="/psnweb/login/tocxc">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-patent"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <s:text name="dyn.main.menu.patent" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-list">
                  <a onclick="toFiles();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-file"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <s:text name="dyn.main.menu.file" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-list">
                  <a onclick="toInspage();">
                    <div class="application_bomb-body_content-list_img application_bomb-body_content-file"></div>
                    <div class="application_bomb-body_content-list_detail">
                      <s:text name="dyn.main.menu.inspage" />
                    </div>
                  </a>
                </div>
              </div>
            </div>
            <div class="application_bomb-body_content">
              <div class="application_bomb-body_content-title">
                <s:text name="dyn.main.menu.sie.services" />
              </div>
              <div class="application_bomb-body_content-serverse">
                <div class="application_bomb-body_content-serverse_list border-bottom_style-dashed">
                  <a href="http://irisaas.smate.com/egrantweb/" target="_black">
                    <div class="application_bomb-body_content-serverse_list-title" style="margin-top: 12px;">
                      <s:text name="dyn.main.menu.unit.irisaas" />
                    </div>
                    <div class="application_bomb-body_content-serverse_list-detail" style="margin: 12px 0px;">
                      <s:text name="dyn.main.menu.unit.intelligent" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-serverse_list border-bottom_style-dashed">
                  <a href="https://sie.scholarmate.com/common/index?tabType=2" target="_black">
                    <div class="application_bomb-body_content-serverse_list-title" style="margin: 12px 0px;">
                      <s:text name="dyn.main.menu.unit.sie" />
                    </div>
                    <div class="application_bomb-body_content-serverse_list-detail" style="margin-bottom: 20px;">
                      <s:text name="dyn.main.menu.unit.platform" />
                    </div>
                  </a>
                </div>
                <div class="application_bomb-body_content-serverse_list">
                  <a href="/application/validate/maint" target="_black">
                    <div class="application_bomb-body_content-serverse_list-title" style="margin: 12px 0px;">
                      <s:text name="dyn.main.menu.unit.quickaccess.srv" />
                    </div>
                    <div class="application_bomb-body_content-serverse_list-detail" style="margin-bottom: 20px;">
                      <s:text name="dyn.main.menu.unit.quickaccess.info" />
                    </div>
                  </a>
              </div>
              </div>
            </div>
          </div>
          <div class="application_bomb-footer">
            <div class="application_bomb-footer_content">
              <i class="application_bomb-footer_tip"></i>
              <div class="application_bomb-footer_content-detail">
                <a href="/prjweb/ins/create/main" target="_blank"><s:text name="dyn.main.menu.create.sie" /></a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
  <%--加载的主体内容 --%>
  <decorator:body />
  <%@ include file="/skins_v6/login_box.jsp"%>
</body>
</html>
