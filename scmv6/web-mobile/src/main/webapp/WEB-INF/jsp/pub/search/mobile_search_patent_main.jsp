<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<%
  response.setHeader("cache-control", "public");
%>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
<%-- <link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/css/scmpcframe.css"> --%>
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/plugin/scm.pop.mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/search.history.js"></script>
<script type="text/javascript" src="${resmod }/js/search.history.controller.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/js/search/Mobile.PdwhPubSearch_${locale}.js"></script>
<script type="text/javascript" src="${resmod }/js/search/Mobile.PdwhPubSearch.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/iscroll.js?version=1"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/pdwh.search.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/pub.details.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=1"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/plugin/scm.pop.mobile.js"></script>
<%-- <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css"> --%>
<%-- <script type="text/javascript" src="${resmod}/mobile/js/plugin/new-mobile_Hintframe.js"></script>
 --%>
<script type="text/javascript">
  $(function() {
    //ios 微信回退不刷新页面问题
    var isPageHide = false;
    window.addEventListener('pageshow', function() {
      //选中发现菜单栏
      if (isPageHide) {
        setTabs("one", 2, 3, $("li[id='one2']").parent().get(0));
      }
    });
    window.addEventListener('pagehide', function() {
      isPageHide = true;
    });
    initTabStatus($("#one2"));
    $("#searchStringInput").attr("searchType", "patent");
    var searchString = $("#searchString").val();
    if (($.isEmptyObject(searchString) || $.isEmptyObject($("#searchStringInput").val()))&& !$.isEmptyObject(getUrlParameter("searchString"))) {
      $("#searchString").val(getUrlParameter("searchString"));
      $("#searchStringInput").val(getUrlParameter("searchString"));
    }
    //初始化检索框
    var options = {
        "searchFunc": "doSearch()",
        "inputOnkeydown": "entersearch()",
        "searchFilter": "select_filter()",
        "inputOnfocus": "history.showHistory()",
        "searchInputVal":searchString,
        "needFilter": true,
        "placeHolder":"检索专利"
      };
   commonMobileSearch.initSearchInput(options);
   //加载数据
   PdwhSearch.ajaxLoadPatent();
   //显示加载框
   var icon = '<div class="preloader active"><div class="preloader-ind-cir__box" style="width: 24px; height: 24px;"><div class="preloader-ind-cir__fill"><div class="preloader-ind-cir__arc-box left-half"><div class="preloader-ind-cir__arc"></div></div><div class="preloader-ind-cir__gap"><div class="preloader-ind-cir__arc"></div></div><div class="preloader-ind-cir__arc-box right-half"><div class="preloader-ind-cir__arc"></div></div></div></div></div>'
   $(".main-list__list").html(icon);
  });
  function getUrlParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);//search,查询？后面的参数，并匹配正则
    if (r != null)
      return decodeURIComponent(r[2]);
    return null;
  }

  function select_filter() {
    $("#select_filter_action").submit();
  }

  function sharePub(des3ResId, dbId) {
    mobile.pub.pdwhIsExist(des3ResId, function() {
      $('#shareScreen').attr("des3ResId", des3ResId);
      $('#shareScreen').attr("dbId", dbId);
      $('#dynamicShare').show();
    })
  }

  //收藏和取消收藏成果回调
  function collectedPubBack(obj, collected, pubId, pubDb) {
    if (collected && collected == "0") {
      $(obj).find("i").removeClass("paper_footer-comment").addClass("paper_footer-comment__flag");
      $(obj).find("span").text(Pubsearch.unsave);
    } else {
      $(obj).find("i").removeClass("paper_footer-comment__flag").addClass("paper_footer-comment");
      $(obj).find("span").text(Pubsearch.save);
    }
  }
  function clearSearchString() {
    $("#searchStringInput").val("");
  }
  function entersearch() {
    var event = window.event || arguments.callee.caller.arguments[0];
    if (event.keyCode == 13) {
      if ($.trim($("#searchStringInput").val()) == "") {
        return;
      }
      //点击检索按钮，弹出框中的过滤条件要清空为默认的
      $("#searchString").val($.trim($("#searchStringInput").val()));
      $("#orderBy").val("DEFAULT");
      $("#searchPubYear").val("");
      $("#searchPubType").val("");
      $("#pageNo").val("1");
      $("#pubDBIds").val("");
      $("#language").val("ALL");
      //保存到历史记录
      var his = new History($("#desPsnId").val());
      var keyword = $.trim($("#searchStringInput").val());
      var fromPage = $("#fromPage").val();
      var searchType = $("#searchStringInput").attr("searchType");
      var searchUrl = "/pub/paper/search";
      if (searchType == "patent") {
        searchType = "/pub/patent/search";
        PdwhSearch.ajaxLoadPatent();
      } else {
        PdwhSearch.ajaxLoadPaper();
      }
      his.add(keyword, searchUrl + "?searchString=" + keyword + "&fromPage=" + fromPage, "");
    }
  }

  function doSearch() {
    if ($.trim($("#searchStringInput").val()) == "") {
      return;
    }
    //点击检索按钮，弹出框中的过滤条件要清空为默认的
    //$("#searchString").val(encodeURIComponent($.trim($("#searchStringInput").val())));
    $("#orderBy").val("DEFAULT");
    $("#searchPubYear").val("");
    $("#searchPubType").val("");
    $("#pageNo").val("1");
    $("#pubDBIds").val("");
    $("#language").val("ALL");
    //保存到历史记录
    var his = new History($("#desPsnId").val());
    var keyword = $.trim($("#searchStringInput").val());
    var fromPage = $("#fromPage").val();
    var searchType = $("#searchStringInput").attr("searchType");
    var searchUrl = "/pub/paper/search";
    if (searchType == "patent") {
      searchUrl = "/pub/patent/search";
      PdwhSearch.ajaxLoadPatent();
    } else {
      PdwhSearch.ajaxLoadPaper();
    }
    his.add(keyword, searchUrl + "?searchString=" + keyword + "&fromPage=" + fromPage, "");
  }
  function goBack() {
    if(location.href.indexOf("/pub/search/main") != -1 && $("#fromPage").val() == "relationmain"){
      location.href="/psnweb/mobile/relationmain";
    }else{
       SmateCommon.goBack('/dynweb/mobile/dynshow');
    } 
  }
  
  
function commonResAward(obj){//赞
  var des3PubId = $(obj).attr("resId");
  mobile.pub.pdwhAwardOpt(obj);
}
function commonShowReplyBox(obj){//评论
  var des3PubId = $(obj).attr("resId");
  mobile.pub.pdwhDetails(des3PubId);
}
function commonShare(obj){//分享
  //需求变更,进入页面分享
  /* var des3PubId = $(obj).attr("resId");
  sharePub(des3PubId); */
  //先校验基准库成果是否存在再分享
  var des3PubId = $(obj).attr("resId");
  mobile.pub.pdwhIsExist(des3PubId, function() {
    SmateCommon.mobileShareEntrance(des3PubId,$(obj).attr("pubdb").toLowerCase());
  });
}
function commonCollectnew(obj){//收藏
  var des3PubId = $(obj).attr("resId");
  mobile.snspub.collect(obj);
}

function callBackAward(obj,awardCount){//赞回调
  if(awardCount>999){
    awardCount = "1k+";
  }
  $(obj).find(".new-Standard_Function-bar_item").toggleClass("new-Standard_Function-bar_selected");
  var isAward = $(obj).attr("isAward");
  if (isAward == 1) {// 取消赞
    $(obj).attr("isAward", 0);           
    if (awardCount == 0) {
      $(obj).find('span').text("赞");
    } else {
      $(obj).find('span').text("赞" + "(" + awardCount + ")");
    }
  } else {// 赞
    $(obj).attr("isAward", 1);
    $(obj).find('span').text("取消赞" + "(" + awardCount + ")");
  }
}

function callBackShare(obj){

}
function callBackCollect(obj){
  $(obj).toggleClass("new-Standard_Function-bar_selected");
  var isCollect = $(obj).attr("collect");
  if(isCollect==0){
    $(obj).find("span").text("取消收藏");
    $(obj).attr("collect",1);
  }else{
    $(obj).find("span").text("收藏");
    $(obj).attr("collect",0);    
  }
}
function hisKeyHref(key,href){
  $("#searchString").val(key);
  $("#searchStringInput").val(key);
  var searchType = $("#searchStringInput").attr("searchType");
  if(searchType == "patent"){
      PdwhSearch.ajaxLoadPatent();
  }else{
      PdwhSearch.ajaxLoadPaper();
  }
}
</script>
</head>
<body>
  <form action="/pub/patent/conditions" method="get" id="select_filter_action">
    <input type="hidden" id="scienceAreaIds" name="des3AreaId" value="${model.des3AreaId}" /> 
    <input type="hidden" id="searchPubYear" name="publishYear" value="${model.publishYear}" /> 
    <input type="hidden" id="searchPubType" name="searchPubType" value="${model.searchPubType}" />
    <input type="hidden" id="searchString" name="searchString" value="<c:out value="${model.searchString }"/>" /> 
    <input type="hidden" id="pubDBIds" name="includeType" value="${model.includeType }" /> 
    <input type="hidden" id="orderBy" name="orderBy" value="${model.orderBy }" /> 
    <input type="hidden" id="searchPdwh" name="searchPdwh" value="0" /> 
    <input type="hidden" id="fromPage" name="fromPage" value="${model.fromPage }" />
    <input type="hidden" id="desPsnId" name="desPsnId" value="${model.des3SearchPsnId }" /> 
    
  </form>
  <div class="m-top m-top_top-background_color">
    <a onclick="goBack();" class="fl"> <i class="material-icons navigate_before"></i>
    </a> <span class="m-top_top-background_color-title">全站检索</span>
  </div>
  <%@ include file="/common/mobile/common_mobile_search_input.jsp"%>
  <%@ include file="mobile_search_head_tab.jsp"%>
  <div style="width: 100%;">
    <div id="showHistory" style="padding-top: 92px; display:none;"></div>
    <div class="paper_content-container main-list dev_history_comment" id="content_pub_list"
      style="height:auto; margin-top: 0px;">
      <div class="main-list__list item_no-padding" list-main="mobile_find_patent_list" id="mobile_find_patent_list_div" style = "margin-top: 145px;"></div>
      <div id="no_record_tips" class=" noRecord" style='display: none;'>
        <div class="content">
          <div class="no_effort" style="margin: 0px;">
            <h2 class="tc">很抱歉，未找到与检索条件相关结果</h2>
            <div class="no_effort_tip pl27">
              <span>温馨提示： </span>
              <p>检查输入条件是否正确.</p>
              <p>尝试同义词或更通用关键词.</p>
              <p>更换检索类别或过滤条件.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="black_top" style="display: none" id="select"></div>
  </div>
  <div class="black_top" id="dynamicShare" style="margin-top: -1px; display: none;"
    onclick="javascript: $('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;">
      <div class="screening" style="max-width: 400px" id="shareScreen" onclick="PdwhSearch.quickShareDyn(this);"
        pubId="" des3resid="" pubdb="PDWH">
        <h2>
          <a class="ui-link" href="javascript:;" style="color: #333;">立即分享到科研之友</a>
        </h2>
      </div>
    </div>
  </div>
</body>
</html>
