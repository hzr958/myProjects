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
<link href="${resmod}/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/common_HideShowDiv.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css/smate.alerts.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/plugin/scm.pop.mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/common.HideShowDiv.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="/ressns/js/dyn/dynamic.common.js"></script>
<script type="text/javascript" src="/ressns/js/dyn/dynamic.common_${locale}.js"></script>
<script type="text/javascript" src="/ressns/js/dyn/dynamic.mobile.detail.js?version=20181013"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="UTF-8"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js" charset="UTF-8"></script>
<script type="text/javascript" src="/resmod/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/main/main.base.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/main/main.base_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_dialogs.js"></script>
<script type='text/javascript' src='${resmod }/js/smate.alerts_${locale}.js'></script>
<script type='text/javascript' src='${resmod }/js/smate.alerts.js'></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/dynamic.common.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/resmod/js/link.status.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_recommend.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/browser_redirect.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/new-mobile_Hintframe.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod }/js/search.history.js"></script>
<script type="text/javascript" src="${resmod }/js/search.history.controller.js"></script>
<script type="text/javascript">
/* window.BrowserRedirect({
	  		"pc_uri":"/dynweb/main"
	  }); */
var shareI18 = '<s:text name="dyn.pc.dyndetail.share"/>';
$(function(){
	if("${wxOpenId}"){
		smatewechat.initWeiXinShare("${appId}","${timestamp}", "${nonceStr}","${signature}");
	}
// 	$(".cur").removeClass("cur");
// 	$("#dynamic_nav").addClass("cur");
	mobile_bottom_setTag("dyn");
	common.scrolldirect("toolbar" ,"hide_bottom" ,"show_bottom");
	common.scrolldirect("add_dmic","hide_add" ,"show_add");
	$("#pageNumber").val(1);
	//加载动态列表
	var icon = '<div class="preloader active"><div class="preloader-ind-cir__box" style="width: 24px; height: 24px;"><div class="preloader-ind-cir__fill"><div class="preloader-ind-cir__arc-box left-half"><div class="preloader-ind-cir__arc"></div></div><div class="preloader-ind-cir__gap"><div class="preloader-ind-cir__arc"></div></div><div class="preloader-ind-cir__arc-box right-half"><div class="preloader-ind-cir__arc"></div></div></div></div></div>'
    $(".main-list__list").html(icon);
    setTimeout("MainBase.autoFollowPsn()", 100);
    //判断是否为iso系统 
    var ua = navigator.userAgent.toLowerCase();
    if (ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/i)) {
      //调整app打开按钮的居中
      var oWidth = window.innerWidth;
      document.getElementsByClassName("openpage-inapp_btn")[0].style.left = (oWidth - 96) / 2 + "px";
      window.onresize = function() {
        var oWidth = window.innerWidth;
        document.getElementsByClassName("openpage-inapp_btn")[0].style.left = (oWidth - 96) / 2 + "px";
      }
      $("#openAppBtn").show();
    }
    
    //初始化检索框 
    var options = {
        "searchFunc": "doSearch()",
        "inputOnkeydown": "entersearch()",
        "searchFilter": "",
        "inputOnfocus": "history.showHistory()",
        "placeHolder":"检索论文、专利、人员..."
    };
    commonMobileSearch.initSearchInput(options);
  });
    
  function doSearch(){
    //保存到历史记录
    var his = new History($("#desPsnId").val());
    var keyword=$.trim($("#searchStringInput").val());
    his.add(keyword, "/pub/paper/search?searchString="+keyword+"&fromPage=dyn", "");
    window.location.href = "/pub/paper/search?searchString="+keyword+"&fromPage=dyn"
  }
  function entersearch(){
    var event = window.event || arguments.callee.caller.arguments[0];  
    if (event.keyCode == 13){  
      doSearch();
    }  
 }
  function hisKeyHref(key,href){
    $("#searchStringInput").val(key);
    doSearch();
  }
  
  function openApp() {
    window.location.href = "/oauth/openApp?service=" + encodeURIComponent(window.location.href);
  }

  //初始化 分享 插件
  function initSharePlugin(obj) {
    var dyntype = $("#share_to_scm_box").attr("dyntype");
    $(obj).dynSharePullMode({
      'groupDynAddShareCount' : "",
      'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
    });
    if ("ATEMP" == dyntype || "B1TEMP" == dyntype || "B2TEMP" == dyntype || "B3TEMP" == dyntype) {
      $("#share_to_scm_box").find(".nav__list .nav__item").eq(0).click();
      //框必须最后弹出来
      $("#share_site_div_id").find(".inside").click();
      var obj_lis = $("#share_to_scm_box").find("li");
      obj_lis.eq(1).hide();
      obj_lis.eq(2).hide();
    }
  }
  //分享回调
  function shareGrpCallback(dynId, shareContent, resId, pubId, isB2T, receiverGrpId, dyntype, resType) {
    var count = Number($('.dev_sharecount_' + pubId).find("a").text().replace(/[\D]/ig, "")) + 1;
    $.ajax({
      url : '/dynweb/dynamic/ajaxsharecount',
      type : 'post',
      dataType : 'json',
      data : {
        'des3ResId' : resId,
        "resTypeStr" : resType,
        'dyntype' : dyntype
      },
      success : function(data) {
        if (data.result == "success") {
          var count = $('.dev_sharecount_' + des3DynId).text().replace(/[^0-9]/ig, "");
          count = count ? Number(count) + 1 : 1;
          $('.dev_sharecount_' + des3DynId).find('a').text(shareI18 + "(" + count + ")");
        }
      }
    });
  };
  //分享回调
  function shareCallback(dynId, shareContent, resId, pubId, isB2T, receiverGrpId) {
    location.href = "/dynweb/mobile/dynshow";
  }
  function msg_menu_find() {
    window.location.href = "/pub/search/main";
  };
  //向上滑动时候
  function mobileUpSlide(options) {
    $(".dev_search_div").slideUp("fast");
  }
  //向下滑动时候
  function mobileDownSlide(options) {
    $(".dev_search_div").slideDown("fast");
  }
</script>
</head>
<body class="body_bg">
  <div class="m-top" style="display: flex; align-items: center; justify-content: space-between;">
    <div style="width: 20vw;"></div>
    <div class="m-top_left-title"
      style="width: 60vw; display: flex; justify-content: center; align-items: center; margin: 0px; font-size: 20px; font-weight: bold;">动态</div>
    <div class="m-top_right-title" style="font-size: 16px; width: 20vw; margin-top: 4px; margin-right: 0px;"
      onclick="javascript:location.href='/dynweb/dynamic/publish';">发布动态</div>
  </div>
  <div class="paper__func-tool dev_search_div" style="background: #f8f8f8; height: 52px; top: 46px;">
  
    <input type="hidden" id="desPsnId" name="desPsnId" value="${des3psnId}"/>
    <s:include value="/common/mobile/common_mobile_search_input.jsp"></s:include>
<!--     <div class="paper__func-box" style="align-items: center; width: 96%;">
      <a class="paper__func-search" style="margin-top: -10px;" onclick="msg_menu_find()"></a> <input type="search"
        class="paper__func-search__flag" name="searchStringInput" id="searchStringInput" onfocus="msg_menu_find()"
        placeholder="检索论文、专利、人员..." style="line-height: 24px; height: 93%; font-size: 12px; padding-top: 3px;">
    </div> -->
  </div>
  <div id="showHistory" style="padding-top: 92px;display:none;"></div>
  <div class="top_clear"></div>
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
  <div class="content dev_history_comment">
    <div class="content dev_history_comment" style="margin-top: 52px;">
      <input id="pageNumber" name="pageNumber" type="hidden" value="1" /> <input id="userId" name="userId"
        value="${des3psnId}" type="hidden">
        <!-- 动态列表页面 -->
          <div class="main-list">
      <div class="main-list__list item_no-padding main-list__list-back" list-main="mobile_dyn_list"></div>
        </div>
      </div>
      <div class="openpage-inapp_btn" onclick="openApp()" id="openAppBtn" style="display: none">在App中打开</div>
      <!-- <div class="add_dmic"  onclick="javascript:location.href='/dynweb/dynamic/publish';"><i class="material-icons">add_black</i></div> -->
      <jsp:include page="../../mobile/bottom/mobile_bottom.jsp"></jsp:include>
      <!-- 分享插件 -->
      <jsp:include page="/common/mobile_smate_share.jsp" />
</body>
</html>
