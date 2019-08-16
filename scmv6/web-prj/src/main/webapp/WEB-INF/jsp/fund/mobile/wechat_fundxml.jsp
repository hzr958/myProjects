<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/wechat.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<link href="/resmod/mobile/css/scmmobileframe.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/wechat.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/wechat.iscroll.css" media="all" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/resmod/js/weixin/iscroll.js"></script>
<script type="text/javascript" src="/resmod/js/weixin/wechat.fund.js?version=1"></script>
<script type="text/javascript" src="/resmod/mobile/css/mobile.css"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_mobile_recommend_${locale}.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_mobile_recommend.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/new-mobile_Hintframe.js"></script>
<script type="text/javascript">
$(function(){
//判断是否为iso系统 
  var ua = navigator.userAgent.toLowerCase();
  if(ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/i)){
  //调整app打开按钮的居中
    var oWidth = window.innerWidth;
       document.getElementsByClassName("openpage-inapp_btn")[0].style.left = (oWidth - 96 )/2 + "px";
       window.onresize = function(){
           var oWidth = window.innerWidth;
           document.getElementsByClassName("openpage-inapp_btn")[0].style.left = (oWidth - 96 )/2 + "px";
       }
    $("#openAppBtn").show();
  }
  initWeixinShare();
});
function shareFund(des3ResId,fundId){
    $("#shareScreen").attr("des3ResId",des3ResId);
    $("#shareScreen").attr("fundId",fundId);
    $('#dynamicShare').show();
}
function login(){
  BaseUtils.buildLoginUrl(window.location.href);
}

function openApp(){
  window.location.href = "/oauth/openApp?service=" + encodeURIComponent(window.location.href);
}
function commonResAward(obj){
  FundRecommend.awardOperation(obj);
}

function commonShare(obj){
  //需求变更,进入页面分享
  /* var encryptedFundId = $(obj).attr("resId");
  var fundId = $(obj).attr("fundId");
  shareFund(encryptedFundId,fundId); */
  SmateCommon.mobileShareEntrance($(obj).attr("resId"),"fundDetail");
}

function commonCollectnew(obj){
  FundRecommend.collectCoperation(obj)
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


function initWeixinShare(){
  var shareTitle = $("#shareFundTitle").html();
  if(shareTitle == null || shareTitle == "" || typeof(shareTitle) == 'undefined'){
      shareTitle = '基金推荐';
  }else{
      shareTitle = shareTitle.trim();
  }
  $.post(
      "/psnweb/outside/ajaxsignature",
      {"currentUrl": encodeURIComponent(window.location.href)},
      function(data){
          if(data.result == "success"){
              smatewechat.customWeiXinShare(
                      data.appId,
                      data.timeStamp, 
                      data.nonceStr,
                      data.signature,
                      shareTitle,
                      data.domain + "/prjweb/wechat/findfundsxml?des3FundId="+encodeURIComponent("${des3FundId}"),
                      "${domainScm}"+$("#defaultLogo").val(),
                      "基金推荐");
          }
      },"json");
}
</script>
</head>
<body style="background-color: white;">
  <div class="black_top" id="dynamicShare" style="display: none; margin-top: -1px;"
    onclick="javascript: $('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;">
      <div class="screening" style="max-width: 400px" id="shareScreen" onclick="FundRecommend.quickShareDyn(this);">
        <h2>
          <a class="ui-link" href="javascript:;" style="color: #333;">立即分享到科研之友</a>
        </h2>
      </div>
    </div>
  </div>
  <form id="fund_listPage" method="get" action="/prjweb/wechat/findfunds">
    <input type="hidden" id="searchAreaCodes" name="searchAreaCodes" value="${searchAreaCodes}" /> <input type="hidden"
      id="searchRegionCodes" name="searchAgencyId" value="${searchAgencyId}" /> <input type="hidden"
      id="searchTimeCodes" name="searchTimeCodes" value="${searchTimeCodes}" /> <input type="hidden"
      id="searchseniority" name="searchseniority" value="${searchseniority}" />
  </form>
  <div class="m-top m-top_top-background_color">
    <a href="javascript:void();" onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');" class="fl"><i class="material-icons ">keyboard_arrow_left</i></a>
    <span class="m-top_top-background_color-title"> 基金详情 </span>
  </div>
  <div class="detail mt60">
    <!-- <div class="top_clear"></div> -->
    <s:if test="resultList == null || resultList.size()<1">
      <div class="fund-details_infortitle">
        <div>未找到对应基金详情</div>
      </div>
    </s:if>
    <s:else>
      <c:forEach items="${resultList}" var="fund">
        <input type="hidden" id="zhTitle_${fund.fundId }" value="<c:out value='${fund.zhTitle }'/>" />
        <input type="hidden" id="enTitle_${fund.fundId }" value="<c:out value='${fund.enTitle }'/>" />
        <input type="hidden" id="zhShowDesc_${fund.fundId }" value="<c:out value='${fund.zhShowDesc }'/>" />
        <input type="hidden" id="enShowDesc_${fund.fundId }" value="<c:out value='${fund.enShowDesc }'/>" />
        <input type="hidden" id="zhShowDescBr_${fund.fundId }" value="<c:out value='${fund.zhShowDescBr }'/>" />
        <input type="hidden" id="enShowDescBr_${fund.fundId }" value="<c:out value='${fund.enShowDescBr }'/>" />
        <input type="hidden" name="logoUrl" id="logoUrl" value="${fund.logoUrl}" />
        <input type="hidden" name="defaultLogo" id="defaultLogo" value="${resmod }/images/insimg/scm_wx.jpg" />
        <div class="fund-details_infor-container">
          <div class="fund-details_Main-title" id="shareFundTitle">${fund.fundName}</div>
          <div class="fund-details_Sub-title">${fund.fundAgency}</div>
          <div class="fund-details_infor-item">
            <c:if test="${not empty fund.description}">
              <div class="fund-details_infor-item_title">类别描述：</div>
              <div class="fund-details_infor-item_content">${fund.description}</div>
            </c:if>
          </div>
          <div class="fund-details_infor-item">
            <c:if test="${not empty fund.time}">
              <div class="fund-details_infor-item_title">申请日期：</div>
              <div class="fund-details_infor-item_content">${fund.time}</div>
            </c:if>
          </div>
          <div class="fund-details_infor-item">
            <c:if test="${not empty fund.viewName}">
              <div class="fund-details_infor-item_title">适合地区：</div>
              <div class="fund-details_infor-item_content">${fund.viewName}</div>
            </c:if>
          </div>
          <div class="fund-details_infor-item">
            <c:if test="${not empty fund.discipline}">
              <div class="fund-details_infor-item_title">学科：</div>
              <div class="fund-details_infor-item_content">${fund.discipline}</div>
            </c:if>
          </div>
        </div>
        <div class="fund-details_infor-item_title">
          <a href="${fund.guideUrl}" target="_Blank" style="text-decoration: none; color: #288aed !important;">申报指南</a>
        </div>
        <div class="new-Standard_Function-bar">
        
 <%--社交操作start --%>
        <c:set var="isAward" value="${fund.hasAward == true ? 1 : 0}"></c:set>
        <c:set var="resDes3Id" >${fund.encryptedFundId }</c:set>
        <c:set var="awardCount" value="${fund.awardCount}"></c:set>
        <c:set var="showComment" value="0"></c:set>
        <c:set var="shareCount" value="${fund.shareCount}"></c:set>
        <c:set var="isCollection" value="${fund.hasCollected == true ? 1 : 0}"></c:set>
        <c:set var="fundId" value="${fund.fundId }"></c:set>
        
        <%@ include file="/common/standard_function_bar.jsp" %>
 <%--社交操作 end--%>         
        </div>
      </c:forEach>
    </s:else>
  </div>
   <div class="openpage-inapp_btn" onclick="openApp()" id="openAppBtn" style="display:none">在App中打开</div>
  <s:if test="hasLogin == 0">
    <div style="height: 56px;"></div>
    <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
  </s:if>
</body>
</html>
