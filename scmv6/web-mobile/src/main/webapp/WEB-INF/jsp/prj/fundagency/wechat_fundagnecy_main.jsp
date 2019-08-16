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
<%-- <link href="${resmod }/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css"> --%>
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
<%-- <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">  --%>
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
<script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/js_v8/agency/scm_mobile_agency.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script src="${resmod }/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/new-mobile_Hintframe.js"></script>
<title>科研之友</title> <script type="text/javascript">
$(function(){
//初始化检索输入框
  var searchInputOptions = {
      "searchFunc": "fundagencyList();", //点击检索图标执行函数
      "oninput":"fundagencyList()",
      "placeHolder": "检索基金资助机构",
      "searchInputVal" : "${searchKey }", //检索的字符串
      "searchFilter": "select_filter();", //过滤条件图标点击事件
      "needFilter": true, //是否需要显示过滤条件图标
  };
  commonMobileSearch.initSearchInput(searchInputOptions);
	fundagencyList();
	selectRecommendFundMenu("agency");
})


function fundagencyList(){
    var searchString = $.trim($("#searchStringInput").val());
    mobile_fundagency_list = window.Mainlist({
        name:"mobile_fundagency_list",
        listurl: "/prj/mobile/ajaxfundagencylist",
        listdata: {"searchKey": $("#searchStringInput").val(),
              "regionAgency":$("#regionAgency").val(),
        },
        listcallback: function(){
          SmateCommon.UpdateUrlParam("searchKey", $("#searchStringInput").val());
            var curPage =$("#curPage").val();
            var totalPages=$("#totalPages").val();
            if(Number(curPage) >= Number(totalPages)){
            	$(".main-list__list").append("<div class='paper_content-container_list main-list__item' style='border:none; border-bottom: none!important;'><div style='padding:20px;color:#999;margin-bottom: 50px; font-size: 14px;'>没有更多记录</div></div>");
            	}
            $("#curPage").remove();
            $("#totalPages").remove();
            MobileAgency.ajaxInitOpt();
        },
        method: "scroll",
    })
}

//打开基金详情
function opendetail(des3FundAgencyId){
	window.location.href = "/prj/mobile/agencydetail?des3FundAgencyId="+des3FundAgencyId;
};

//条件选择
function select_filter(){
	$("#searchKey").val($.trim($("#searchStringInput").val()));
	$("#select_filter_action").submit();
}
function goMyhome(){
    location.href="/psnweb/mobile/myhome";
};
function commonResAward(obj){//赞
  MobileAgency.ajaxAward(obj);
}

function commonShare(obj){//分享
  var des3AgencyId = $(obj).attr("resid");
  MobileAgency.shareToScm(des3AgencyId);
}
function commonCollectnew(obj){//收藏
  MobileAgency.ajaxInterest(obj)
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
    $(obj).find("span").text("取消关注");
    $(obj).attr("collect",1);
  }else{
    $(obj).find("span").text("关注");
    $(obj).attr("collect",0);
  }
}
</script>
</head>
<body style="background-color: white;">
  <input type="hidden" id="flag" name="flag" value=${flag } />
  <form action="/prj/mobile/agencycondition" method="post" id="select_filter_action">
    <input type="hidden" id="searchKey" name="searchKey" value="${searchKey }" /> <input type="hidden" id="regionAgency" name="regionAgency"
      value="${regionAgency }" />
  </form>
  <div class="black_top" id="dynamicShare" style="display: none; margin-top: -1px;"
    onclick="javascript: $('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;">
      <div class="screening" style="max-width: 400px" id="shareScreen" onclick="MobileAgency.quickShareDyn(this);">
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
  <%@ include file="/common/mobile/common_mobile_search_input.jsp" %>
  <div class="paper_content-container main-list" id="content_prj_list" style="min-height: 100%; margin-top: 100px;">
    <div class="main-list__list item_no-padding" list-main="mobile_fundagency_list" ></div>
  </div>
  <!-- 底部按钮 -->
  <%@include file="/WEB-INF/jsp/mobile/bottom/mobile_fund_bottom.jsp"%>
</body>
</html>
