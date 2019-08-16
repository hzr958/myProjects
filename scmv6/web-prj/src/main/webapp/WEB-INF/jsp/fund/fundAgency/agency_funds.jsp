<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<!-- <meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" /> -->
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>科研之友</title>
<link href="/resmod/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="/resmod/mobile/css/scmmobileframe.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/wechat.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/wechat.iscroll.css" media="all" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="/resmod/js/weixin/iscroll.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
<script type="text/javascript" src="/resmod/js/weixin/wechat.fund.js?version=1"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="/resmod/mobile/css/mobile.css"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_mobile_recommend_${locale}.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_mobile_recommend.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript">
$(function(){
	smatewechat.initWeiXinShare("${appId}","${timestamp}", "${nonceStr}","${signature}");
	fundList();
    selectRecommendPubMenu('collect');
});
function fundList(){
	mobilefundList = window.Mainlist({
        name:"mobileFundList",
        listurl: "/prj/mobile/agencydetail",
        listdata: {
            "searchKey":$.trim($("#searchStringInput").val())
        },
        listcallback: function(){
			var curPage =$("#curPage").val();
			var totalPages=$("#totalPages").val();
			if(Number(curPage) >= Number(totalPages)){
				$(".main-list__list").append("<div class='paper_content-container_list main-list__item' style='border:none; border-bottom: none!important;'><div style='padding:20px;color:#999;margin-bottom: 50px; font-size: 14px;'>没有更多记录</div></div>");
			}
			$("#curPage").remove();
			$("#totalPages").remove();
        },
        method: "scroll",
    })
};
function opendetail(obj){
	 location.href="/prjweb/wechat/findfundsxml?des3FundId="+obj;
};
function goMyhome(){
	 location.href="/psnweb/mobile/myhome";
};
function shareFund(des3ResId,fundId){
    $("#shareScreen").attr("des3ResId",des3ResId);
    $("#shareScreen").attr("fundId",fundId);
    $('#dynamicShare').show();
};
function msg_menu_find(){
    window.location.href = "/pub/search/main";
}; 
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
  <div id="header" class="fund__page-header">
    <span class="fund__page-icon"><i class="material-icons" style="margin-left: 10px" onclick="goMyhome()">keyboard_arrow_left</i></span>
    <span class="fund__page-title"
      style="margin-right: 10% !important; font-family: Noto Sans SC, Hiragino Sans GB W3, Heiti SC, Microsoft Yahei, Segoe UI;">基金</span>
  </div>
  <form action="javascript:return true">
    <div class="paper__func-tool dev_search_div" style="background: #f8f8f8; height: 52px; top: 46px;">
      <div class="paper__func-box" style="align-items: center; width: 95%;">
        <a class="paper__func-search" style="margin-top: -10px;" onclick="fundList();"></a> <input
          class="paper__func-search__flag" name="searchStringInput" id="searchStringInput" oninput="fundList();"
          type="search" placeholder="检索已收藏基金..."
          style="line-height: 24px; height: 93%; font-size: 12px; padding-top: 3px;">
      </div>
    </div>
  </form>
  <div class="wrap_com" style="padding-top: 95px;">
    <div class="main-list">
      <div class="main-list__list" list-main="mobileFundList"></div>
    </div>
  </div>
  <s:include value="/WEB-INF/jsp/mobile/bottom/mobile_fund_bottom.jsp"></s:include>
</body>
</html>