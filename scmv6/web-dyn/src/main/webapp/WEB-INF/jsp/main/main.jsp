<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<meta name="description" content="<s:text name='dyn.main.seo.descripe' />" />
<meta name="keywords" content="<s:text name='dyn.main.seo.keywords' />" />
<meta name="robots" content="index, follow" />
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/new-confirmbox/confirm.css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/main/main.base.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/main/main.base_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/dynamic.common.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/dynamic.pc.detail.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/dynamic.common_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<link href="${resmod}/css/resetpwd/resetpassword-temp.css" rel="stylesheet" type="text/css" />
<!--重置密码框css  -->
<script src="${resmod}/js/plugin/des/des.js" type="text/javascript"></script>
<!--重置密码框-密码加密js  -->
<script type="text/javascript" src="${resmod}/js/resetpwd/resetpassword_${locale}.js"></script>
<!--重置密码框js  -->
<script type="text/javascript" src="${resmod}/js/resetpwd/resetpassword.js"></script>
<!--重置密码框js  -->
<script type="text/javascript" src="${resmod }/js/homepage/homepage_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/homepage/psn.improveinfo.js"></script>
<script type="text/javascript" src="${resmod}/js/home/random.module.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_recommend.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/browser_redirect.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpBase/grp.base.js"></script>
<script type="text/javascript" src="${ressns}/js/group/grp/grpBase/grp.base_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
<script type="text/javascript">
var shareI18 = '<s:text name="dyn.pc.dyndetail.share"/>';
$(document).ready(function(){
	document.title="<s:text name='dyn.main.seo.title' />";
	//检查人员配置信息
	Improve.initPsnConfInfo();
	//加载信息完善页面
	Improve.preparePage();
	//随机显示某个模块
	Rm.toModule();
	//获取来访人员列表
	MainBase.getPsnList();
	//常用功能
	MainBase.getShortcuts();
	addFormElementsEvents();
	setTimeout("MainBase.autoFollowPsn()", 100);
});

//初始化 分享 插件
function initSharePlugin(obj){
	var dyntype = $("#share_to_scm_box").attr("dyntype") ;
	$(obj).dynSharePullMode({
		'groupDynAddShareCount':"",
		'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)' ,
	});
	if("ATEMP" == dyntype ||"B1TEMP" == dyntype ||"B2TEMP" == dyntype ||"B3TEMP" == dyntype){
		$("#share_to_scm_box").find(".nav__list .nav__item").eq(0).click() ;
		document.getElementsByClassName("nav__item-selected")[0].classList.remove("nav__item-selected");
		document.getElementsByClassName("nav__item-container")[0].querySelector(".item_selected").classList.add("nav__item-selected");
		/* $("#share_to_scm_box").find(".nav__list .nav__item").eq(0).classList.add("nav__item-selected"); */
	}
}
//分享回调
function shareCallback (dynId,shareContent,resId,pubId,isB2T ,receiverGrpId){
		location.href="/dynweb/main";	
};
function sharePsnCallback (des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType,dyntype){
	if(resType == "1" || resType == "2"){
		addPubShare(des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType, dyntype);
	}else if(resType == "22" || resType == "24"){
		addPdwhPubShare(des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType, dyntype);
	}else if(resType == "11"){
	  addFundShare(des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType, dyntype);
	}
	shareGrpCallback (des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, dyntype, resType);
}
//分享回调
function shareGrpCallback (des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, dyntype, resType){
    if(resType=="fund"){
      addFundShare(des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType, dyntype);
    }
	$.ajax({
		url : '/dynweb/dynamic/ajaxsharecount',
		type : 'post',
		dataType : 'json',
		data : {'des3ResId':resId,
			'des3DynId':des3DynId,
			'resTypeStr':resType,
			 'dynType':dyntype    
		       },
		success : function(data) {
			if (data.result == "success") {
				var $input = $("input[des3dynid='"+des3DynId+"']");
				var functionBtn = $input.nextAll(".dynamic-social__list ").find(".dynamic-social__item");
				var $shareDiv = functionBtn.eq(2);
				if ($shareDiv.attr("onclick").indexOf("dynamic.shareDynMain") == -1) {
				  $shareDiv = functionBtn.eq(1);//无评论按钮
				}
				var count = $shareDiv.text().replace(/[^0-9]/ig,"");
			    count = count ? Number(count)+1 : 1;
			    if(count>=1000){
			      $shareDiv.find('a').text(shareI18+"(1K+)");
			    }else{
			      $shareDiv.find('a').text(shareI18+"("+count+")");
			    }
			}
		}
	});		
};
function showDynContentLength(){
	$("#id_dyn_content_length").html($("#publish_text").val().length);
}
function addPdwhPubShare(des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType, dyntype){
    $.ajax({
        url : '/pub/opt/ajaxpdwhshare',
        type : 'post',
        dataType : 'json',
        data : {'des3PdwhPubId':resId,
                  'comment':shareContent,
                  'sharePsnGroupIds':receiverGrpId,
                  'platform':"2"
               },
        success : function(data) {
        }
    }); 
}
function addPubShare(des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType, dyntype){
    $.ajax({
        url : '/pub/opt/ajaxshare',
        type : 'post',
        dataType : 'json',
        data : {'des3PubId':resId,
             'comment':shareContent,
             'sharePsnGroupIds':receiverGrpId,
             'platform':"2"
               },
        success : function(data) {
        }
    });
}
function addFundShare (des3DynId,shareContent,resId,pubId,isB2T ,receiverGrpId, resType, dyntype){
  $.ajax({
      url : '/prjweb/fund/ajaxsharecount',
      type : 'post',
      dataType : 'json',
      data : {
          'des3FundId':resId
      },
      success : function(data) {
      }
  });
}
</script>
<style type="text/css">
.dynamic-post__linelimit-icon {
  width: 64px;
  height: 95px;
  background-image: url(../img/add-Achievements2.png);
  background-repeat: no-repeat;
  background-position: center;
  line-height: 64px;
  background-color: #f4f4f4;
}
</style>
</head>
<body>
  <br />
  <div id="improveInfoDiv"></div>
  <div class="black_top" id="dynamicShare" style="display: none" onclick="javascirpt:	$('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;"
      onclick="javascirpt:$('#dynamicShare').show();">
      <div id="quickShareOpt" class="screening" style="max-width: 400px">
        <h2>
          <a class="screening" href="javascript:;">立即分享到科研之友</a>
        </h2>
      </div>
    </div>
  </div>
  <div class="container__horiz" style="padding-bottom: 206px;">
    <input id="pageNumber" name="pageNumber" type="hidden" value="0"> <input id="pageSize" name="pageSize"
      type="hidden" value="10"> <input id="userId" name="userId" type="hidden" value="${des3PsnId }">
    <div class="container__horiz_left width-9">
      <div class="container__card" onclick="MainBase.showPublish(this)" id="main_share__card">
        <div class="dynamic__box create-new-post" style="margin-bottom: 16px;">
          <div class="dynamic-cmt">
            <div class="dynamic-cmt__post">
              <div style="display: flex; height: 108px;">
                <div class="dynamic-content__post dynamic-content__post-container">
                  <div class="dynamic-post__avatar">
                    <img src="${avatars }" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                  </div>
                  <div class="dynamic-post__main">
                    <div class="dynamic-cmt__new-post_deactive" id="publish_notic">
                      <s:text name="dyn.main.base.publishText" />
                    </div>
                    <div class="dev_share_input dynamic-post__linelimit element__piblic-none" style="height: 60px;">
                      <div class="input__area">
                        <div class="input__box">
                          <textarea oninput="showDynContentLength()" maxlength="500" onfocus=" this.placeholder='' "
                            class="global_no-border" placeholder="<s:text name="dyn.main.base.publishText"/>"
                            id="publish_text"></textarea>
                          <div class="textarea-autoresize-div"></div>
                        </div>
                      </div>
                    </div>
                    <div class="dynamic-main__box select_res_box dev_share_pubinfo dynamic-main__file-box" style="display: none; height: 50px;">
                      <div class="dynamic-divider" style="margin-top: 1px!important;"></div>
                      <div class="dynamic-main__att" style="margin: 0px;">
                        <div class="pub-idx_small">
                          <div class="pub-idx__base-info">
                            <div class="pub-idx__main_box">
                              <div class="pub-idx__main aleady_select_pub" pubId="">
                                <div class="pub-idx__main_title"></div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="att-delete-icon clear_selected_pub" onclick="MainBase.clearSelectedPub(this);">
                          <i class="material-icons">close</i>
                        </div>
                      </div>
                    </div>
                    <div class="dynamic-main__box"></div>
                  </div>
                  <div class="dynamic-content__post-count">
                    <div class="dynamic-content__post-count_Curve" id="id_dyn_content_length">0</div>
                    /
                    <div class="dynamic-content__post-count_total">500</div>
                  </div>
                </div>
                <div class="dynamic-post__linelimit-icon" onclick="MainBase.showMsgPubUI()"
                  title="<s:text name='dyn.main.base.addPub'/>"></div>
              </div>
              <div class="dynamic-cmt__post_actions dev_do_share" style="display: none; justify-content: flex-end;">
                <button class="button_main button_dense button_primary button_clickable" disabled
                  onclick="MainBase.realtime(this)" style="color: #288aed;">
                  <s:text name='dyn.main.base.postDiscuss' />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 动态列表页面 -->
      <div class="main-list">
        <div class="main-list__list item_no-padding" list-main="psn_dyb_list"></div>
      </div>
    </div>
    <div class="container__horiz_right width-3">
      <div class="container__card" style="margin-bottom: 8px;">
        <!-- ===================================================成果认领与全文认领_start -->
        <div class="dev_displaymodule"></div>
        <!-- ===================================================成果认领与全文认领_end -->
        <div class="home-quickaccess__box" id="shortcuts_list">
            <!-- 常用功能 -->
        </div>
      </div>
      <div class="container__card">
        <div class="module-card__box">
          <div class="module-card__header" style="padding: 0 7px;">
            <div class="module-card-header__title">
              <s:text name="dyn.main.base.viewHis" />
            </div>
            <button class="button_main button_link" onclick="MainBase.showVistPsnMoreUI(this)">
              <s:text name="dyn.main.base.more" />
            </button>
          </div>
          <div class="main-list">
            <div class="main-list__list item_no-border" id="psn_list">
              <!-- 最近来访人员列表 -->
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- =================================查看更多最近来访--start=================================== -->
  <div class="dialogs__box" style="width: 720px;" dialog-id="dev_vist_psn_more">
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__header">
        <div class="dialogs__header_title">
          <s:text name="dyn.main.base.viewHis" />
        </div>
        <i class="list-results_close" onclick="MainBase.hideVistPsnMoreUI(this);"></i>
      </div>
    </div>
    <div class="dialogs__childbox_adapted" style="height: 560px;">
      <div class="main-list__list" list-main="vist_psn_more_list"></div>
    </div>
  </div>
  <!-- =================================查看更多--最近来访--end=================================== -->
  <!-- 分享插件 -->
  <jsp:include page="/common/smate.share.jsp" />
  <%@ include file="/WEB-INF/jsp/main/main_pub_main.jsp"%>
  <!-- =================================成果认领查看全部start=================================== -->
  <%@ include file="/WEB-INF/jsp/pub/confirm_main.jsp"%>
  <!-- =================================成果认领查看全部end=================================== -->
  <!-- =================================全文认领查看全部start=================================== -->
  <%@ include file="/WEB-INF/jsp/pub/fulltext_main.jsp"%>
  <!-- =================================全文认领查看全部end=================================== -->
  <%@ include file="/WEB-INF/jsp/pub/invitegrp_main.jsp"%>
  <%@ include file="/WEB-INF/jsp/pub/reqfriend_main.jsp"%>
  <%@ include file="/WEB-INF/jsp/pub/reqgrp_main.jsp"%>
</body>
</html>