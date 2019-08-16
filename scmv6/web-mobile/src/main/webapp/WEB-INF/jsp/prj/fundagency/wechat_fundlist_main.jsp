<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta charset="utf-8">
  <meta name="viewport"
    content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <title>科研之友</title>
    <link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
      <link href="${resmod}/mobile/css/scmmobileframe.css" rel="stylesheet" type="text/css">
        <link href="${resmod}/css/wechat.css" rel="stylesheet" type="text/css">
          <link href="${resmod}/css/wechat.iscroll.css" media="all" rel="stylesheet" type="text/css">
            <link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
              <script type="text/javascript" src="${resmod }/js/jquery.js"></script>
              <script type="text/javascript" src="${resmod}/js/weixin/iscroll.js"></script>
              <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
              <script type="text/javascript" src="${resmod}/js/weixin/wechat.fund.js?version=1"></script>
              <script type="text/javascript" src="${resmod}/mobile/js/wechat.custom.js"></script>
              <script type="text/javascript" src="${resmod}/mobile/css/mobile.css"></script>
              <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
              <script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
              <script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
              <%-- <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css"> --%>
                <script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
                <script type="text/javascript" src="${ressns }/js/fund/fund_mobile_recommend_${locale}.js"></script>
                <script type="text/javascript" src="${ressns }/js/fund/fund_mobile_recommend.js"></script>
                <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
                <script src="${resmod}/js/baseutils/baseutils.js"></script>
                <script type="text/javascript" src="${resmod}/mobile/js/plugin/new-mobile_Hintframe.js"></script>
                <title>科研之友</title> <script type="text/javascript">
$(function(){
//初始化检索输入框
  var searchInputOptions = {
      "searchFunc": "fundList();", //点击检索图标执行函数
      "inputOnchange": "fundList();", //检索框onchange事件
      "placeHolder": "检索机构基金...",
      "searchInputVal": "${searchKey}" //检索的字符串
  };
  commonMobileSearch.initSearchInput(searchInputOptions);
  fundList();
  selectRecommendFundMenu("agency");
})


function fundList(){
       var searchString = $.trim($("#searchStringInput").val());
       SmateCommon.UpdateUrlParam("searchKey",searchString);    
	   mobilefundList = window.Mainlist({
	        name:"mobileFundList",
	        listurl: "/prj/mobile/ajaxagencydetail",
	        listdata: {
	        	"searchKey":$.trim($("#searchStringInput").val()),
	             "des3FundAgencyId":$("#des3FundAgencyId").val(),
	             "logoUrl":$("#logoUrl").val()
	        },
	        listcallback: function(){
	            var curPage =$("#curPage").val();
	            var totalPages=$("#totalPages").val();
	            if(Number(curPage) >= Number(totalPages)){
	                $(".main-list__list").append("<div class='paper_content-container_list main-list__item' style='border:none; border-bottom: none!important;'><div style='padding:20px;color:#999;margin-bottom: 50px; font-size: 14px;'>没有更多记录</div></div>");
	            }
	            $("#curPage").remove();
	            $("#totalPages").remove();
	            //加载基金图片
	            SmateCommon.loadFundLogos($("#aidInsDeatils_des3FundIds").val());
	        },
	        method: "scroll",
	    })
}

//打开基金详情
function opendetail(obj){
	location.href="/prjweb/wechat/findfundsxml?des3FundId="+obj;
};

//论文推荐
function linkRecommed(){
    window.location.href = "/prjweb/wechat/findfunds";  
};
//发现
function linkPubFind(){
    window.location.href = "/pubweb/mobile/findpub/area";  
}
//资助机构
function linkfundagnecy(){
    window.location.href = "/prj/mobile/fundagency";  
}
//基金分享
function shareFund(des3ResId,fundId){
    $("#shareScreen").attr("des3ResId",des3ResId);
    $("#shareScreen").attr("fundId",fundId);
    $('#dynamicShare').show();
};


function commonResAward(obj){
  FundRecommend.awardOperation(obj);
}

function commonShare(obj){
  //需求变更,进入页面分享
  /* var encryptedFundId = $(obj).attr("resId");
  var fundId = $(obj).attr("fundId");
  shareFund(encryptedFundId,fundId); */
  SmateCommon.mobileShareEntrance($(obj).attr("resId"),"aidInsDetail",$("#des3FundAgencyId").val());
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
</script>
</head>
<input type="hidden" name="des3FundAgencyId" id="des3FundAgencyId" value="${des3FundAgencyId }"></input>
<input type="hidden" name="logoUrl" id="logoUrl" value="${logoUrl }"></input>
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
  <div id="header" class="fund__page-header">
    <span class="fund__page-icon"><i class="material-icons" style="margin-left: 10px; margin-top: 10px;"
      onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i></span> <span class="fund__page-title"
      style="margin-right: 10% !important; font-family: Noto Sans SC, Hiragino Sans GB W3, Heiti SC, Microsoft Yahei, Segoe UI;">基金</span>
  </div>
  <!-- <form action="javascript:return true">
    <div class="paper__func-tool dev_search_div" style="background: #f8f8f8; height: 52px; top: 46px;">
      <div class="paper__func-box" style="align-items: center; width: 95%;">
        <a class="paper__func-search" style="margin-top: -10px;" onclick="fundList();"></a> <input
          class="paper__func-search__flag" name="searchStringInput" id="searchStringInput" oninput="fundList();"
          type="search" placeholder="检索机构基金..."
          style="line-height: 24px; height: 93%; font-size: 12px; padding-top: 3px;">
      </div>
    </div>
  </form> -->
  <%@ include file="/common/mobile/common_mobile_search_input.jsp" %>
  <div class="wrap_com" style="padding-top: 95px;">
    <div class="main-list">
      <div class="main-list__list" list-main="mobileFundList" ></div>
    </div>
  </div>
  <!-- 底部按钮 -->
  <%@include file="/WEB-INF/jsp/mobile/bottom/mobile_fund_bottom.jsp"%>
</body>
</html>