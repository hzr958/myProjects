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
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/pubrecommend/mobile_pub_recommend.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/pubrecommend/mobile_pub_recommend_${locale}.js"></script>
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
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/pub.details.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=1"></script>
<script type="text/javascript">
$(function(){
      //选中发现菜单栏
      selectRecommendPubMenu("find");
      //加载数据
      PubRecommend.ajaxFindPubList();
      //处理检索框
//      dealSearchInput();
      $("#searchStringInput").bind('search', function () {
            doSearch();
     });
      
      if($("#searchStringInput").val()=="" && !$("#searchStringInput").is(":focus")){
              $("#showIcon1").show();
              $("#showIcon2").hide();
      }else{
              $("#showIcon1").hide();
              $("#showIcon2").show();         
      }
      $("#searchStringInput").focus(function(){             
            if($("#searchStringInput").val()!=""){
              $("#showIcon1").hide();
              $("#showIcon2").show();
            }
      });
      $('#searchStringInput').bind('input propertychange', function() {  
        if($("#searchStringInput").val()!=""){
          $("#showIcon1").hide();
          $("#showIcon2").show();
        }else{
          $("#showIcon1").show();
          $("#showIcon2").hide();
        } 
      });  
      $("#searchStringInput").blur(function(){
          setTimeout(function(){
              if($("#searchStringInput").val()==""){
                  $("#showIcon1").show();
                  $("#showIcon2").hide();
              }else{
                  $("#showIcon1").hide();
                  $("#showIcon2").show();         
              }           
          },100);
      });     
});


function select_filter(){
    $("#select_filter_action").submit();
}
function loadPubDetail(des3Id, dbid){
    //移动端暂时没有成果详情的短地址，使用真实地址
    params="des3PubId="+encodeURIComponent(des3Id);
    window.location.href="/pubweb/mobile/findpdwhpubxml?useoldform=true&language=ZH&index=0&"+params+"&dbid="+dbid;
}
function sharePub(des3ResId,dbId){
    $('#shareScreen').attr("des3ResId",des3ResId);
    $('#shareScreen').attr("dbId",dbId);
    $('#dynamicShare').show();
    //wechat.pub.quickShareDyn(1,$("#des3PubId").val());
}
function showRecommend(){
    window.location.href ="/pubweb/mobile/pubrecommendmain";
}
function showMypub(){
    window.location.href ="/pub/collect/main";
}


function doSearch(){
    $("#searchString").val($.trim($("#searchStringInput").val()));
    $("#pageNo").val("1");
    PubRecommend.ajaxFindPubList();
}
//收藏和取消收藏成果回调
/* function collectedPubBack(obj,collected,pubId,pubDb){
    if(collected && collected=="0"){
        $(obj).find("i").removeClass("paper_footer-comment").addClass("paper_footer-comment__flag");
        $(obj).find("span").text(Pubsearch.unsave);   
    }else{
        $(obj).find("i").removeClass("paper_footer-comment__flag").addClass("paper_footer-comment");
        $(obj).find("span").text(Pubsearch.save);
    }
} */

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

</script>
</head>
<body>
  <form action="/pub/find/conditions" method="get" id="select_filter_action">
    <input type="hidden" id="scienceAreaIds" name="des3AreaId" value="${model.des3AreaId}" /> 
    <input type="hidden" id="searchPubYear" name="publishYear" value="${model.publishYear}" /> 
    <input type="hidden" id="searchPubType" name="searchPubType" value="${model.searchPubType}" /> 
    <input type="hidden" id="searchString" name="searchString" value="${model.searchString }" /> 
    <input type="hidden" id="pubDBIds" name="includeType" value="${model.includeType }" /> 
    <input type="hidden" id="areaName" name="areaName" value="${model.areaName }" />     
    <input type="hidden" id="searchPdwh" name="searchPdwh" value="0" />
    
<%--     <input type="hidden" id="searchLanguage" name="searchLanguage" value="${model.searchLanguage }" />
 --%>    <input type="hidden" id="orderBy" name="orderBy" value="${model.orderBy }" />
  </form>
  <!-- <div class="black_top" id="dynamicShare" style="display:none;margin-top: -1px;" onclick="javascript: $('#dynamicShare').hide();">
     <div class="screening_box" style="display:flex; justify-content:center;">
        <div class="screening" style="max-width: 400px" id="shareScreen" onclick="PubRecommend.quickShareDyn(this);">
            <h2 style="color: #333;"><a href="javascript:;">立即分享到科研之友</a></h2>
        </div>
     </div>
 </div> -->
  <div>
    <div class="paper__func-header">
      <a href="/psnweb/mobile/myhome"><i class="material-icons paper__func-header__tip">keyboard_arrow_left</i></a> <span>论文 - ${model.areaName }</span>
      <i class="material-icons paper__func-header__tip"></i>
    </div>
    <div class="paper__func-tool" style="justify-content: space-between;">
      <div class="paper__func-tool">
        <div class="paper__func-box" style="/* height: 30px; line-height: 30px; */">
          <a class="paper__func-search" id="showIcon1" style="height: 32px; display: none; width: 7%;" onclick="doSearch();"></a>        
          <form action="" onsubmit="return false;" style="width: 93%;">
            <input type="search" placeholder="检索论文" class="paper__func-box__inputarea" id="searchStringInput"
              value="${model.searchKey }" style="height: 30px; line-height: 24px; width: 100%;">
          </form>
          <a class="paper__func-search" id="showIcon2" style="height: 32px; width: 10%;" onclick="doSearch();"></a>
<!--           <i class="paper__func-search" onclick="doSearch();" style="margin-top: -10px;"></i>
 -->        </div>
        <div class="message-page__fuctool-right_tip" style="margin-right: 8px; margin-left: 16px;">
          <i class="material-icons" onclick="select_filter();">filter_list</i>
        </div>
      </div>
    </div>
  </div>
  <div style="width: 100%;">
    <div class="paper_content-container main-list" id="content_pub_list" style="min-height: 100%; margin-bottom: 50px;">
      <div class="main-list__list item_no-padding" list-main="mobile_find_pub_list"></div>
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
  <%@ include file="/WEB-INF/jsp/mobile/bottom/mobile_recommend_pub_bottom.jsp"%>
</body>
</html>