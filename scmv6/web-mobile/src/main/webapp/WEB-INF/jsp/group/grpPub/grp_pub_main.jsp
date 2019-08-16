<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<%response.setHeader("cache-control","public"); %>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<%-- <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css"> --%>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">

<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=1"></script>
<script type="text/javascript" src="${resmod}/js/group/mobile/mobile_grp_pub.js"></script>
<style>
.new-Standard_Function-bar{
   margin-bottom: 0px!important;
}
</style>

<script type="text/javascript">
$(function(){
 //初始化检索输入框
  var searchInputOptions = {
      "oninput":"groupPubSearch()",//检索框oninput事件
      "searchFunc": "groupPubSearch()", //点击检索图标执行函数
      "placeHolder": "检索论文",
      "searchInputVal" : "${pubVO.searchKey }", //检索的字符串
      "searchFilter": "select_filter();", //过滤条件图标点击事件
      "needFilter": true, //是否需要显示过滤条件图标
  };
  commonMobileSearch.initSearchInput(searchInputOptions); 
  var showPrjPub = $("#showPrjPub").val();
  var showRefPub = $("#showRefPub").val();
  var grpCategory = $("#grpCategory").val();
  if(grpCategory==10){
    $("#pageTitle").text("群组文献");
    changeSelectStyle($("#grpPub"));
  }else if(grpCategory==11){
    if(showPrjPub==1 && showRefPub!=1){
      changeSelectStyle($("#grpPubMenu"));
    }else if(showPrjPub!=1 && showRefPub==1){
      $("#pageTitle").text("群组文献");
      changeSelectStyle($("#grpRefMenu"));
    }
  }else{
    changeSelectStyle($("#grpPub"));
  }
  GrpPub.grpPubList();
});
function groupPubSearch(){
  var searchStr = $.trim($("#searchStringInput").val());
  $("#searchKey").val(searchStr);
  GrpPub.grpPubList();
}
function select_filter(){
  var url = $("#isLogin").val()=="false" ? "/grp/outside/mobile/grouppubconditions" : "/grp/mobile/grouppubconditions";
  $("#select_filter_action").attr("action",url);
  $("#select_filter_action").submit();
}
function openPubDetail(des3PubId){
  window.location.href = "/pub/details?des3PubId=" + encodeURIComponent(des3PubId) + "&des3GrpId=" + encodeURIComponent($("#des3GrpId").val());
}

function commonResAward(obj){
  mobile.snspub.awardoptnew(obj);
}

function commonShare(obj){
  var des3PubId = $(obj).attr("resId");
  var param = {};
  param.des3GrpId = $("#des3GrpId").val();
  SmateCommon.shareRes(des3PubId,"pub",param);
}

function callBackAward(obj,awardCount){
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

</script>
</head>
<body>
  <form action="/grp/mobile/grouppubconditions" method="get" id="select_filter_action">
    <input type="hidden" id="des3GrpId" name="des3GrpId" value="${pubVO.des3GrpId}"/> 
    <input type="hidden" id="searchKey" name="searchKey" value="${pubVO.searchKey}"/>
    <input type="hidden" id="publishYear" name="publishYear" value="${pubVO.publishYear}"/>
    <input type="hidden" id="pubType" name="pubType" value="${pubVO.pubType}"/>
    <input type="hidden" id="includeType" name="includeType" value="${pubVO.includeType}"/>
    <input type="hidden" id="orderBy" name="orderBy" value="${pubVO.orderBy}"/>    
    <input type="hidden" id="showPrjPub" name="showPrjPub" value="${pubVO.showPrjPub}"/>
    <input type="hidden" id="showRefPub" name="showRefPub" value="${pubVO.showRefPub}"/>
    <input type="hidden" id="grpCategory" name="grpCategory" value="${pubVO.grpCategory}"/>
  </form>
    <input type="hidden" id="isLogin" name="isLogin" value="${isLogin}"/>

    <div class="provision_container-title" style="z-index: 120;">
    <i class="material-icons" onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i>
    <span id="pageTitle">群组成果</span> <i></i>
    </div>
  <%@ include file="/common/mobile/common_mobile_search_input.jsp" %> 
  <div class="effort_list" style="width: 100%;z-index: 99;">
    <div class="paper_content-container main-list" style="min-height: 100%; margin-bottom: 50px;  margin-top: 100px;">
      <div class="main-list__list item_no-padding" list-main="grppublist"></div>
    </div>
  </div>
 <c:set var="des3GrpId" value="${pubVO.des3GrpId}"></c:set>
 <c:set var="grpCategory" value="${pubVO.grpCategory}"></c:set>   
 <c:set var="psnRole" value="${pubVO.psnRole}"></c:set>      
 <%@ include file="/WEB-INF/jsp/group/common/mobile_group_nav.jsp" %> 
</body>
</html>