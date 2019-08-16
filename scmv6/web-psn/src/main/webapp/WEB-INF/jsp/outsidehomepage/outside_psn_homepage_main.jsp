<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta http-equiv="content-style-type" content="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="format-detection" content="telephone=no">
<title>科研之友</title>
<meta property="og:url" content="${psnProfileUrl }"/>
<meta property="og:title" content="${name }"/>
<meta property="og:image" content="${psnInfo.avatars}?random=<%=Math.random()%>"/>
<meta property="og:description" content="${insAndDepInfo };${positionAndTitolo}(${psnRegionInfo })" />
<meta property="og:site_name" content="ScholarMate"/>
<c:if test="${!empty psnBriefDesc }">
  <meta name="description" content="<c:out value='${psnBriefDesc }' escapeXml='true'/>" />
</c:if>
<c:if test="${empty psnBriefDesc }">
  <meta name="description" content="<s:text name='homepage.profile.description'/>" />
</c:if>
<c:if test="${!empty psnKeywordsStr}"> 
  <meta name="keywords" content="<c:out value='${psnKeywordsStr }' escapeXml='true'/>" />
</c:if>
<c:if test="${!empty psnProfileUrl }">
  <link rel="canonical" href="${psnProfileUrl }" />
</c:if>
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/css/scmpcframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<link href="${resmod }/css/scmjscollection.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod }/js/homepage/homepage_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/prj/prj.outside.js"></script>
<script type="text/javascript" src="${resmod }/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/common.ui.js"></script>
<script type="text/javascript" src="${resmod}/js/loadding_div.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/no.result.prompt.language.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/cursorPositionPlugins.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/outside.pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scm-pc_filedragbox.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_dialogs.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="utf-8"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<link type="text/css" rel="stylesheet" href="${resmod}/css/smate.autoword.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css/plugin/jquery.complete.css" />
<script type="text/javascript" src="${resmod}/js/plugin/jquery.appraisal.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.imgcutupload.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.filestyle.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.imgcut.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.complete.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.autoword.js"></script>
<script type="text/javascript" src="${resmod }/js/homepage/outside.psn.homepage.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod}/js/shorturledit_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/shorturledit.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/js/influence/influence_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/influence/influence.outside.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript">
var module = "${module}";
var showDialogPub = "${showDialogPub}";
$(document).ready(function(){
	if(module == "" || module == "homepage"){
		outside.homepage.loadHomepageModule();
	}else{
		selectTab(module);
	}
	window.LoadShortUrlEdit({
		"scm_myfunction":function(){},
		"split_text":"/P/",
		"scm_role":2
		});
	SmateCommon.addVisitRecord($("#outsideDes3PsnId").val(),$("#outsideDes3PsnId").val(),6);
});
function selectTab(tabName){
	$(".tabClass").removeClass("item_selected");
	switch(tabName){
		case 'pub': 
			$("#pubTab").addClass("item_selected");
			Pub.main();
			break;
		case 'homepage':
			$("#homeTab").addClass("item_selected");
			toHomePage();
			break;
		case 'prj':
			$("#prjTab").addClass("item_selected");
			/* var des3PsnId = $("#des3PsnId").val(); */
			project.ajaxOutSideShow();
			break;
		case 'influence':
            $("#infTab").addClass("item_selected");
            Influence.main();
            break;  
		case 'pubConsequence':
			$("#pubConsequence").addClass("item_selected");
			toPubConsequence();
		default: 
			$("#homeTab").addClass("item_selected");
			break;
	}
}
//初始化 分享 插件
function psnInitSharePlugin(obj){
    if(SmateShare.timeOut && SmateShare.timeOut == true)
        return;
    if (locale == "en_US") {
        $(obj).dynSharePullMode({
            'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
            'language' : 'en_US'
        });
    } else {
        $(obj).dynSharePullMode({
            'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
        });
    }
    var obj_lis = $("#share_to_scm_box").find("li");
    obj_lis.eq(0).hide();
    obj_lis.eq(1).click();
    obj_lis.eq(2).hide();
};
</script>
</head>
<body>
  <%@ include file="./psn_cnf_const.jsp"%>
  <input type="hidden" name="briefConf" class="psn_outside_conf" value="<s:property value='#CNF_BRIEF'/>"
    id="briefSecurity" />
  <input type="hidden" name="keywordsConf" class="psn_outside_conf" value="<s:property value='#CNF_EXPERTISE'/>"
    id="keywordsSecurity" />
  <input type="hidden" name="workHistoryConf" class="psn_outside_conf" value="<s:property value='#CNF_WORK'/>"
    id="workSecurity" />
  <input type="hidden" name="eduHistoryConf" class="psn_outside_conf" value="<s:property value='#CNF_EDU'/>"
    id="eduSecurity" />
  <input type="hidden" name="representPubConf" class="psn_outside_conf" value="<s:property value='#CNF_PUB'/>"
    id="pubSecurity" />
  <input type="hidden" name="representPrjConf" class="psn_outside_conf" value="<s:property value='#CNF_PRJ'/>"
    id="prjSecurity" />
  <input type="hidden" name="contactInfoConf" class="psn_outside_conf" value="<s:property value='#CNF_CONTACT'/>"
    id="contactSecurity" />
  <input type="hidden" name="cnfAnyMode" id="cnfAnyMode" value="${cnfAnyMode }" />
  <input type="hidden" name="outsideDes3PsnId" value='<iris:des3 code="${psnId }"></iris:des3>' id="outsideDes3PsnId" />
  <input type="hidden" name="isMySelf" id="isMySelf" value="${isMySelf }" />
  <input type="hidden" name="cnfId" id="cnfId" value="${cnfBuild.cnf.cnfId }" />
  <input type="hidden" name="isFriend" id="isFriend" value="${isFriend }" />
  <input type="hidden" name="searchKey" id="searchKey" value="${searchKey }" />
  <input type="hidden" name="sortBy" id="sortBy" value="${sortBy }" />
  <input type="hidden" name="isLogin" id="isLogin" value="${isLogin }" />
  <div class="pro-header headDiv" id="head_div">
    <div class="pro-header__container"  style="position:relative;">
      <div class="pro-header__left-panel" id="psn_base_info">
        <div class="pro-header__base-info">
          <div class="pro-header__avatar" style="position: relative;">
            <img id="psn_avatar" src="${psnInfo.avatars}?random=<%=Math.random()%> "
              onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
            <c:if test="${isMySelf }">
              <div class="pro-header__avator-tip">
                <s:text name='homepage.outside.profile.header' />
              </div>
            </c:if>
          </div>
          <div class="pro-header__main" id="psn_info_head">
            <div class="pro-header__main_psn-name" id="psn_name">
              <span title="${name}">${name}</span>
            </div>
            <div class="pro-header__main_psn-inst" id="psn_insAndDept">
              <span title="${insAndDepInfo }">${insAndDepInfo }</span>
            </div>
            <div class="pro-header__main_psn-inst" id="psn_posAndTitolo">
              <span title="${positionAndTitolo}">${positionAndTitolo}</span>
            </div>
            <div class="pro-header__main_psn-loc" id="psn_regionName">${psnRegionInfo }</div>
          </div>
        </div>
      </div>
      <div class="new-scholarmate_ID-container">
        <c:if test="${locale == 'zh_CN'}">
          <span class="new-scholarmate_ID-container_title">科研号:</span>
        </c:if>
        <c:if test="${locale != 'zh_CN'}">
          <span class="new-scholarmate_ID-container_title">Scholar ID:</span>
        </c:if>
        <div class="new-scholarmate_ID-container_content">
            <span class="new-scholarmate_ID-container_content-detaile">${psnOpenId }</span>
            <i class="find-nothing_tip" title='<c:if test="${locale == 'zh_CN'}">科研之友人员唯一编号</c:if><c:if test="${locale != 'zh_CN'}">The only identifier of researchers in ScholarMate</c:if>'></i>
        </div>
    </div>
    </div>
    <div class="pro-header__nav">
      <div class="pro-header__nav-box">
        <nav class="nav_horiz">
          <ul class="nav__list">
            <li class="nav__item dev_module_homepage item_selected tabClass" id="homeTab"
              onclick="selectTab('homepage')"><s:text name="head_div.homeTab" /></li>
            <c:if test="${openPrjSum!=0}">
              <li class="nav__item tabClass" onclick="selectTab('prj');" id="prjTab"><s:text name="head_div.prjTab" /></li>
            </c:if>
            <li class="nav__item dev_module_pub tabClass" onclick="selectTab('pub');" id="pubTab"><s:text
                name="head_div.pubTab" /></li>
            <%-- <li class="nav__item dev_module_inf tabClass" onclick="selectTab('influence');" id="infTab"><s:text
                name='homepage.head.tab.influence' /></li> --%>
          </ul>
          <div class="nav__underline" style="width: 112px; left: 0px;"></div>
        </nav>
        <div class="pro-header__url" id="clip_container" scm-paren='span_paren'>
          <c:if test="${!empty psnProfileUrl }">
            <a><span id="span_shorturl" scm-oldurl="${psnProfileUrl }" scm-id='span_id' title='${psnProfileUrl }'>${psnProfileUrl }</span></a>
            <i class="material-icons" style="display: none;" scm-id='span_c_s' title='<s:text name="homepage.profile.copyUrl" />'>content_copy</i>
            <a class="manage-one dev_pdwhpub_share" onclick="SmateShare.shareprofile($(this));psnInitSharePlugin();"
                    resid="${des3PsnId}" psnId="${psnId }">
                <div class="new-Standard_Function-bar_item">
                    <!-- <i class="new-Standard_function-icon new-Standard_Share-icon"></i>  -->
                    <span class="new-Standard_Function-bar_item-title dev_psn_share_${psnId}">
                       <i class="material-icons" style="font-size:22px; color: #666;margin-left: 4px;" title='<s:text name="homepage.profile.share" />'>share</i>
                    </span>
                </div>
              </a>
          </c:if>
        </div>
      </div>
    </div>
  </div>
  <div class="container__horiz oldDiv">
    <div class="container__horiz_left width-8">
      <c:if test="${cnfAnyMode != 0}">
        <div class="container__card" style="${(empty module || module eq 'homepage') ? '' : 'display:none;'}">
          <div class="pro-stats__panel">
            <div class="pro-stats__list">
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty prjSum || prjSum < 0 ? '0' : prjSum }</div>
                <div class="pro-stats__item_title">
                  <s:text name='homepage.profile.statistics.project' />
                </div>
              </div>
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty pubSum || pubSum < 0 ? '0' :  pubSum}</div>
                <div class="pro-stats__item_title">
                  <s:text name='homepage.profile.statistics.pub' />
                </div>
              </div>
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty readSum || readSum < 0 ? '0' : readSum}</div>
                <div class="pro-stats__item_title">
                  <s:text name='homepage.profile.title.reads' />
                </div>
              </div>
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty downloadCount || downloadCount < 0 ? '0' : downloadCount }</div>
                <div class="pro-stats__item_title">
                  <s:text name='homepage.profile.title.downloadCount' />
                </div>
              </div>
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty citedSum || citedSum < 0 ? '0' : citedSum }</div>
                <div class="pro-stats__item_title">
                  <s:text name='homepage.profile.title.citations' />
                </div>
              </div>
              <div class="pro-stats__item">
                <div class="pro-stats__item_number">${empty hIndex || hindex < 0 ? '0' :  hIndex}</div>
                <div class="pro-stats__item_title">
                  <s:text name="homepage.profile.statistics.hindex" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </c:if>
      <!-- 简介 -->
      <div class="container__card" style="display: none;" id="briefDescModule"></div>
      <!-- 科技领域 -->
      <div id="scienceAreaModuleDiv"></div>
      <!-- 关键词 -->
      <div id="keywordsModuleDiv"></div>
      <!-- 工作经历 -->
      <div id="workHistoryModuleDiv"></div>
      <!-- 教育经历 -->
      <div id="eduHistoryModuleDiv"></div>
      <!-- 代表性成果、项目  隐藏了 -->
      <div id="representPubModuleDiv"></div>
      <div id="representPrjModuleDiv"></div>
      <!-- 联系信息 -->
      <div id="contactInfoModuleDiv"></div>
      <div class="Privacy-tip_container dev_private_msg" style="display:none;margin: 0 auto;height: 450px;">
          <div class="Privacy-tip_container-avator" style=" width: 120px;"></div>
          <div class="Privacy-tip_container-tip">
                                   <%--   由于<span class="Privacy-tip_container-tip_author">${name}</span>的隐私设置,内容未完全显示 --%>
                                    由于隐私设置,内容未完全显示 
          </div>
      </div>  
      <div class="container__card" style="display: none;" id="contactInfoModule"></div>
    </div>
    <div class="container__horiz_right width-4">
      <c:if test="${cnfAnyMode != 0}">
        <!-- 合作者 -->
        <div id="psn_cooperator" class="psn_cooperator">
          <%@ include file="psn_cooperator_model.jsp"%>
        </div>
      </c:if>
    </div>
  </div>
  <div class="dialogs__box oldDiv setnormalZindex" style="width: 480px;" dialog-id="editPsnWorkBox" cover-event="hide"
    id="editPsnWorkBox"></div>
  <div class="dialogs__box oldDiv setnormalZindex" style="width: 480px;" dialog-id="editPsnEduBox" cover-event="hide" id="editPsnEduBox"></div>
  <div class="dialogs__box oldDiv setnormalZindex" style="width: 480px;" dialog-id="psnInfoBox" cover-event="hide" id="psnInfoBox"></div>
  <div class="sel-dropdown__box oldDiv setnormalZindex" selector-data="work_history_select" data-src="request"
    request-url="/psnweb/workhistory/ajaxlist" request-data="buildWorkHistorySelector();"></div>
  <div class="dialogs__box oldDiv setnormalZindex" dialog-id="briefDescBox" cover-event="hide" style="width: 480px; height: 400px;"
    id="briefDescBox"></div>
  <div class="background-cover dev_psnhome_back oldDiv" style="display: none;">
    <div class="dialogs__box" style="width: 720px;">
      <div class="dialogs__header">
        <div class="dialogs__header_title">合作者</div>
        <button class="button_main button_icon">
          <i class="material-icons">close</i>
        </button>
      </div>
      <div class="dialogs__content">
        <div class="dialogs__content-list">
          <div class="main-list__list">
            <div class="main-list__item">
              <div class="main-list__item_content">
                <div class="psn-idx_medium">
                  <div class="psn-idx__base-info">
                    <div class="psn-idx__avatar_box">
                      <div class="psn-idx__avatar_img">
                        <img src="/img/avatar.jpg">
                      </div>
                    </div>
                    <div class="psn-idx__main_box">
                      <div class="psn-idx__main">
                        <div class="psn-idx__main_name">马建</div>
                        <div class="psn-idx__main_title">香港城市大学</div>
                        <div class="psn-idx__main_area">
                          <div class="kw__box">
                            <div class="kw-chip_small">recommendation system</div>
                            <div class="kw-chip_small">社交网络</div>
                          </div>
                        </div>
                        <div class="psn-idx__main_stats">
                          <span class="psn-idx__main_stats-item">项目数: 34</span><span class="psn-idx__main_stats-item">成果数:
                            202</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="main-list__item_actions">
                <button class="button_main button_dense button_primary-reverse">添加联系人</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- =================================成果合作者查看全部end=================================== -->
  <jsp:include page="/common/smate.share.jsp" />
  <!-- 分享操作 -->
</body>
</html>
