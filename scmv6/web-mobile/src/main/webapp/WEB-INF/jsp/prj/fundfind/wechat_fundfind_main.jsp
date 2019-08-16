<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta charset="utf-8">
  <meta name="viewport"
    content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
      <link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/scmmobileframe.css">
          <link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
          <link href="${resmod }/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
            <script type="text/javascript" src="${resmod }/js/jquery.js"></script>
            <script type="text/javascript" src="${resmod }/js/weixin/iscroll.js"></script>
            <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
            <script type="text/javascript" src="${resmod }/js/weixin/wechat.fund.js?version=1"></script>
            <script type="text/javascript" src="${resmod }/mobile/js/wechat.custom.js"></script>
            <script type="text/javascript" src="${resmod }/smate-pc/js/scmpc_mainlist.js"></script>
            <script type="text/javascript" src="${resmod }/smate-pc/js/browsercompatible.js"></script>
            <script src="${resmod }/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
            <script type="text/javascript" src="${ressns }/js/fund/fund_mobile_recommend_zh_CN.js"></script>
            <script type="text/javascript" src="${ressns }/js/fund/fund_mobile_recommend.js"></script>
            <script type="text/javascript" src="${ressns }/js/fund/fund_find.js"></script>
            <script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
            <script type="text/javascript" src="${resmod }/js_v8/agency/scm_mobile_agency.js"></script>
            <script src="${resmod }/js/baseutils/baseutils.js"></script>
            <script type="text/javascript" src="${resmod}/mobile/js/plugin/new-mobile_Hintframe.js"></script>
            <script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
            
            <title>科研之友</title> <script type="text/javascript">
$(function(){
//初始化检索输入框
  var searchInputOptions = {
      "searchFunc": "fundFindList();", //点击检索图标执行函数
      "inputOnchange": "fundFindList();", //检索框onchange事件
      "placeHolder": "检索基金",
      "searchInputVal" : "${searchKey }", //检索的字符串
      "searchFilter": "select_filter();", //过滤条件图标点击事件
      "needFilter": true, //是否需要显示过滤条件图标
  };
  commonMobileSearch.initSearchInput(searchInputOptions);
  selectRecommendFundMenu("fundFind");
  fundFindList();
})

function fundFindList(){
    var searchString = $.trim($("#searchStringInput").val());
    SmateCommon.UpdateUrlParam("searchKey",searchString);    
    mobile_fundfind_list = window.Mainlist({
        name:"mobile_fundfind_list",
        listurl: "/prj/mobile/fundfindlist",
        listdata: {"searchKey": $("#searchStringInput").val().trim(),
              "searchRegionCodes":$("#regionCodesSelect").val(),
              "searchseniority":$("#seniorityCodeSelect").val(),
              "scienceCodesSelect":$("#scienceCodesSelect").val(),
        },
        listcallback: function(){
          var curPage =$("#curPage").val();
            var totalPages=$("#totalPages").val();
            if(Number(curPage) >= Number(totalPages)){
            	$(".main-list__list").append("<div class='paper_content-container_list main-list__item' style='border:none; border-bottom: none!important;'><div style='padding:20px;color:#999;margin-bottom: 50px; font-size: 14px;'>没有更多记录</div></div>");
            	}
            FundFind.ajaxFundLogos();
            $("#curPage").remove();
            $("#des3FundAgencyIds").remove();
            $("#totalPages").remove();
            $("#totalCount").remove();
        },
        method: "scroll",
    })
}

function shareFund(des3ResId,fundId){
  //需求变更,进入页面分享
  /* $("#shareScreen").attr("des3ResId",des3ResId);
  $("#shareScreen").attr("fundId",fundId);
  $('#dynamicShare').show(); */
  SmateCommon.mobileShareEntrance(des3ResId,"fund",$("#searchStringInput").val());
};
//打开基金详情
function opendetail(des3FundId){
  location.href="/prjweb/wechat/findfundsxml?des3FundId="+des3FundId;
};

//条件选择
function select_filter(){
	$("#searchKey").val($.trim($("#searchStringInput").val()));
	$("#select_filter_action").submit();
}
function goMyhome(){
    location.href="/psnweb/mobile/myhome";
};

function commonResAward(obj){
  FundRecommend.awardOperation(obj);
}

function commonShare(obj){
  var encryptedFundId = $(obj).attr("resId");
  var fundId = $(obj).attr("fundId");
  shareFund(encryptedFundId,fundId);
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
<body style="background-color: white;">
  <input type="hidden" id="flag" name="flag" value=${flag } />
  <form action="/prj/mobile/findfundcondition" method="post" id="select_filter_action">
    <input type="hidden" id="searchAreaCodes" name="searchAreaCodes" value="${searchAreaCodes }" />
    <input type="hidden" id="searchKey" name="searchKey" value="${searchKey }" /> 
    <input type="hidden" id="regionCodesSelect" name="searchRegionCodes" value="${regionCodesSelect }" />
    <input type="hidden" id="seniorityCodeSelect" name="searchseniority" value="${seniorityCodeSelect }" />
    <input type="hidden" id="scienceCodesSelect" name="scienceCodesSelect" value="${scienceCodesSelect }" />
  </form>
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
  <div class="message-page__header"
    style="position: fixed; top: 0px; z-index: 55; display: flex; justify-content: space-between;">
    <span class="class="fund__page-icon""> <i class="material-icons" style="margin-left: 15px; width: 10vw;"
      onclick="goMyhome()">keyboard_arrow_left</i>
    </span> <span style="width: 80vw; display: flex; justify-content: center; align-items: center;">基金</span> <i
      style="width: 10vw; margin-right: 15px" onclick="select_filter();"></i>
  </div>
  <%-- <form action="javascript:return true">
    <div class="new-financial_neck">
      <div class="new-financial_neck-search">
        <a class="paper__func-search"></a> <input placeholder="检索基金" class="new-financial_neck-text"
          style="font-size: 16px;padding-top: 3px;height: 28px; line-height: 28px;" name="searchStringInput" id="searchStringInput" oninput="fundFindList()"
          type="search" value="${searchKey }" />
      </div>
      <i class="material-icons" style="width: 10%;color:#666;" onclick="select_filter();">filter_list</i>
    </div>
  </form> --%>
  <%@ include file="/common/mobile/common_mobile_search_input.jsp" %>
  <div class="paper_content-container main-list" id="content_prj_list" style="min-height: 100%; margin-top: 65px;">
    <div class="main-list__list item_no-padding" list-main="mobile_fundfind_list" style="padding-top: 35px;"></div>
  </div>
  <!-- 底部按钮 -->
  <%@include file="/WEB-INF/jsp/mobile/bottom/mobile_fund_bottom.jsp"%>
</body>
</html>