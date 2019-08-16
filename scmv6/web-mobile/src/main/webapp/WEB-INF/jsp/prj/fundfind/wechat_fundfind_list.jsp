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
    var targetlist = document.getElementsByClassName("message-page__fuctool-item");
    if(targetlist[0] != null && targetlist[0] != "undefined"){
      document.getElementsByClassName("message-page__selector-line")[0].style.width = targetlist[0].offsetWidth + "px";
    }
    for(var i = 0; i < targetlist.length;i++){
		 targetlist[i].onclick = function(){
		     selectAgencyStyle(this);
		 }
    }
    $(".dev_select_agency").click(function(){
        $("#searchAgencyId").val($(this).attr("value"));
        fundList();         
    });
    selectRecommendFundMenu('recommend');
    var searchAgencyId = $("#searchAgencyId").val()=="" ? 0 : $("#searchAgencyId").val();
    var agencySelect = $(".dev_select_agency[value='"+searchAgencyId+"']");
    selectAgencyStyle(agencySelect);
    var length = $(".message-page__fuctool-selector_item")[0].offsetLeft; 
    $(".dev_scroll_agency_select").scrollLeft(length-5); 
});
function fundList(){
	mobilefundList = window.Mainlist({
        name:"mobileFundList",
        listurl: "/prjweb/wechat/ajaxfindfunds",
        listdata: getSearchCondition(),
        listcallback: function(){
			var curPage =$("#curPage").val();
			var totalPages=$("#totalPages").val();
			if(Number(curPage) >= Number(totalPages)){
				$(".main-list__list").append("<div class='paper_content-container_list main-list__item' style='border:none; border-bottom: none!important;'><div style='padding:20px;color:#999;margin-bottom: 50px; font-size: 14px;'>没有更多记录</div></div>");
			}
			$("#curPage").remove();
			$("#totalPages").remove();
			FundRecommend.ajaxFundLogos();
        },
        method: "scroll",
    })
};
function getSearchCondition(){
	var data={};
	if($("#searchAreaCodes").val()!=""&&$("#searchAreaCodes").val()!="0"){
		data.searchAreaCodes = $("#searchAreaCodes").val();	
	}
    if($("#searchAgencyId").val()!=""&&$("#searchAgencyId").val()!="0"){
        data.searchAgencyId = $("#searchAgencyId").val();
    }
    if($("#searchTimeCodes").val()!=""){
        data.searchTimeCodes = $("#searchTimeCodes").val();
    }
    if($("#searchseniority").val()!=""){
        data.searchseniority = $("#searchseniority").val();
    }
	return data;
};
function opendetail(obj){
	$("#select_filter_action").attr("action","/prjweb/wechat/findfundsxml?");
	$("#des3FundId").val(obj);
	 //location.href="/prjweb/wechat/findfundsxml?des3FundId="+obj;
    $("#select_filter_action").submit();
};
function goMyhome(){
	 location.href="/psnweb/mobile/myhome";
};
function shareFund(des3ResId,fundId){
    $("#shareScreen").attr("des3ResId",des3ResId);
    $("#shareScreen").attr("fundId",fundId);
    $('#dynamicShare').show();
};
function selectAgencyStyle(obj){
    if(document.getElementsByClassName("message-page__fuctool-selector_item").length>0){ 
        document.getElementsByClassName("message-page__fuctool-selector_item")[0].classList.remove("message-page__fuctool-selector_item");
    }
    $(obj).addClass("message-page__fuctool-selector_item");
    var disleft = "";
/*     if($(obj).offset()){
    	disleft = obj.getBoundingClientRect().x; 
    } */
    var diswidth = "";
    if($(obj).offset()){
    	diswidth = $(obj)[0].offsetWidth;
    }
    document.getElementsByClassName("message-page__selector-line")[0].style.width = diswidth + "px";
    document.getElementsByClassName("message-page__selector-line")[0].style.left = disleft + "px";

};
function select_filter(){
	$("#select_filter_action").attr("action","/prjweb/wechat/findfundcondition");
    $("#select_filter_action").submit();
};
</script>
</head>
<body style="background-color: white;">
  <form id="select_filter_action" action="/prjweb/wechat/findfundcondition" method="get">
    <input type="hidden" id="searchAreaCodes" name="searchAreaCodes"
      value="${empty searchAreaCodes ? 0 : searchAreaCodes}" /> <input type="hidden" id="searchAgencyId"
      name="searchAgencyId" value="${searchAgencyId}" /> <input type="hidden" id="searchTimeCodes"
      name="searchTimeCodes" value="${searchTimeCodes}" /> <input type="hidden" id="searchseniority"
      name="searchseniority" value="${searchseniority}" /> <input type="hidden" id="des3FundId" name="des3FundId"
      value="" />
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
  <div id="header" class="fund__page-header">
    <span class="fund__page-icon"><i class="material-icons" style="margin-left: 10px" onclick="goMyhome()">keyboard_arrow_left</i></span>
    <span class="fund__page-title"
      style="margin-right: 10% !important; font-family: Noto Sans SC, Hiragino Sans GB W3, Heiti SC, Microsoft Yahei, Segoe UI;">基金</span>
  </div>
  <div class="paper__func-tool" style="justify-content: space-between; overflow-y: hidden;">
    <div
      class="message-page__fuctool message-page__fuctool-scroll message-page__fuctool-container dev_scroll_agency_select">
      <div class="message-page__fuctool-item dev_select_agency" value="0"
        style="padding: 0px 16px; height: 100%; display: flex; align-items: center;">
        <span>全部推荐</span>
      </div>
      <s:iterator value="fundAgencyInterestList" var="item">
        <div class="message-page__fuctool-item dev_select_agency" value="${item.agencyId }"
          style="padding: 0px 4px; height: 100%; display: flex; align-items: center; flex-shrink: 0; margin: 0px 10px;">
          <span>${item.showName }</span>
        </div>
      </s:iterator>
      <div class="message-page__selector-line" style="left: 0px; max-width: 160px; display: none;"></div>
    </div>
    <div class="message-page__fuctool-right_tip" style="margin-right: 12px;">
      <i class="material-icons" onclick="select_filter();">filter_list</i>
    </div>
  </div>
  <div class="wrap_com" style="padding-top: 95px;">
    <div class="main-list">
      <div class="main-list__list" list-main="mobileFundList"></div>
    </div>
  </div>
  <s:include value="/WEB-INF/jsp/mobile/bottom/mobile_fund_bottom.jsp"></s:include>
</body>
</html>