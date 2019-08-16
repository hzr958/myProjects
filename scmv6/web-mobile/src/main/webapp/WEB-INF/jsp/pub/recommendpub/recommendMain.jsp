<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/pubrecommend/mobile_pub_recommend.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/pubrecommend/mobile_pub_recommend_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/search/Mobile.PdwhPubSearch_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/search/Mobile.PdwhPubSearch.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=1"></script>
<script src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/iscroll.js?version=1"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript">
$(function(){
	pageIsBack();
   	PubRecommend.ajaxLondPubList();//显示数据
    var targetlist = document.getElementsByClassName("message-page__fuctool-item");
    if(targetlist[0] != null && targetlist[0] != "undefined"){
      document.getElementsByClassName("message-page__selector-line")[0].style.width = targetlist[0].offsetWidth + "px";
    }
    for(var i = 0; i < targetlist.length;i++){
     targetlist[i].onclick = function(){
    	 selectAreaStyle(this);
     }
    }
	    
    $(".dev_select_area").click(function(){
    	$("#searchArea").val($(this).attr("value"));
        PubRecommend.ajaxLondPubList();    		
    });
    selectRecommendPubMenu('recommend');
    var areacode = $("#searchArea").val();
    var areaSelect = $(".dev_select_area[value='"+areacode+"']");
    selectAreaStyle(areaSelect);
});
function selectAreaStyle(obj){
    if(document.getElementsByClassName("message-page__fuctool-selector").length>0){ 
        document.getElementsByClassName("message-page__fuctool-selector")[0].classList.remove("message-page__fuctool-selector");
    }
    if(obj.length==0){
      return;
    }
    $(obj).addClass("message-page__fuctool-selector");
    var disleft = $(obj).offset().left;
    var diswidth = $(obj)[0].offsetWidth 
    document.getElementsByClassName("message-page__selector-line")[0].style.width = diswidth + "px";
    document.getElementsByClassName("message-page__selector-line")[0].style.left = disleft + "px";

};
function select_filter(){
	$("#select_filter_action").submit();
};

function sharePub(des3ResId,dbId){
	$('#shareScreen').attr("des3ResId",des3ResId);
	//$('#shareScreen').attr("dbId",dbId);
	$('#dynamicShare').show();
	//wechat.pub.quickShareDyn(1,$("#des3PubId").val());
};
function showRecommend(){
	window.location.href ="/pub/mobile/pubrecommendmain";
};
function showMypub(){
	window.location.href ="/pub/collect/main";
};
function doSearch(){
   if($.trim($("#searchStringInput").val()) != ""){
   		PubRecommend.searchPdwhPub($("#searchStringInput").val());
   }else{
	   $("#searchStringInput").val("");
   }
};
//收藏和取消收藏成果回调
function collectedPubBack(obj,collected,pubId,pubDb){
    if(collected && collected=="0"){
        $(obj).find("i").removeClass("paper_footer-comment").addClass("paper_footer-comment__flag");
        $(obj).find("span").text(Pubsearch.unsave);   
    }else{
        $(obj).find("i").removeClass("paper_footer-comment__flag").addClass("paper_footer-comment");
        $(obj).find("span").text(Pubsearch.save);
    }
};
function pageIsBack(){
	try {
	    var searchData = JSON.parse($.cookie('searchData'));
	    if(searchData){
	        if (searchData) {
	            searchArea = searchData.searchArea;
	            searchPsnKey = searchData.searchPsnKey;
	            searchPubYear = searchData.searchPubYear;
	            searchPubType = searchData.searchPubType;
	            $("#searchArea").val(searchArea);
	            $("#searchPsnKey").val(searchPsnKey);
	            $("#searchPubYear").val(searchPubYear);
	            $("#searchPubType").val(searchPubType);     
	            $.cookie('searchData',null);
	        }
	    }
	} catch (e) {
		return;
	}
};
function setCookieData(){
    var searchArea = $("#searchArea").val();
    var searchPsnKey = $("#searchPsnKey").val();
    var searchPubYear = $("#searchPubYear").val();
    var searchPubType = $("#searchPubType").val();
    var searchData={};
    searchData.searchArea = searchArea;
    searchData.searchPsnKey = searchPsnKey;
    searchData.searchPubYear = searchPubYear;
    searchData.searchPubType = searchPubType;
    $.cookie('searchData',JSON.stringify(searchData),{expires:1});
};
function goback(){
  if(document.referrer.indexOf("/dynweb/mobile/dynshow") != -1){
    location.href=document.referrer;
  }else{
    location.href="/psnweb/mobile/myhome";
  }
};

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
  <form action="/pub/mobile/ajaxconditions" method="get" id="select_filter_action">
    <input type="hidden" id="defultArea" name="defultArea" value="${pubVO.defultArea}" /> <input type="hidden"
      id="defultKey" name="defultKey" value='<c:out value="${pubVO.defultKeyJson}"/>' /> <input type="hidden"
      id="searchArea" name="searchArea" value="${pubVO.searchArea}" /> <input type="hidden" id="searchPsnKey"
      name="searchPsnKey" value='<c:out value="${pubVO.searchPsnKey}"/>' /> <input type="hidden" id="searchPubYear"
      name="searchPubYear" value="${pubVO.searchPubYear}" /> <input type="hidden" id="searchPubType"
      name="searchPubType" value="${pubVO.searchPubType}" />
  </form>
  <div class="black_top" id="dynamicShare" style="display: none; margin-top: -1px;"
    onclick="javascript: $('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;">
      <div class="screening" style="max-width: 400px" id="shareScreen" onclick="PubRecommend.quickShareDyn(this);">
        <h2 style="color: #333;">
          <a href="javascript:;">立即分享到科研之友</a>
        </h2>
      </div>
    </div>
  </div>
  <div>
    <div class="paper__func-header">
      <div onclick="goback();"><i class="material-icons paper__func-header__tip">keyboard_arrow_left</i></div> <span>论文</span>
      <i class="material-icons paper__func-header__tip"></i>
    </div>
    <div class="paper__func-tool" style="justify-content: space-between;">
      <div class="message-page__fuctool message-page__fuctool-container">
<!--         <div class="message-page__fuctool-item dev_select_area" value="" style="max-width: 160px;">
          <span>不限</span>
        </div> -->
        <c:forEach items="${pubVO.areaList }" var="item">
          <div class="message-page__fuctool-item dev_select_area" value="${item.scienceAreaId }"
            style="max-width: 160px;">
            <span>${item.scienceArea }</span>
          </div>
        </c:forEach>
        <div class="message-page__selector-line  setting-list_page-item_hidden" style="left: 0px; max-width: 160px;"></div>
      </div>
      <div class="message-page__fuctool-right_tip" style="margin-right: 12px;">
        <i class="material-icons" onclick="select_filter();">filter_list</i>
      </div>
    </div>
  </div>
  <div style="width: 100%;">
    <div class="paper_content-container main-list" id="content_pub_list" style="min-height: 100%; margin-bottom: 50px;">
      <div class="main-list__list item_no-padding" list-main="mobile_pub_list"></div>
    </div>
    <div class="black_top" style="display: none" id="select"></div>
  </div>
  <%@ include file="/WEB-INF/jsp/mobile/bottom/mobile_recommend_pub_bottom.jsp"%>
</body>
</html>