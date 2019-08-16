<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/css/scm-newpagestyle.css">
<link href="${resmod }/smate-pc/new-fundRecommend/public2016.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript" src="${ressns }/js/fund/fund_agency_detail_${locale }.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_agency_detail.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_recommend.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type='text/javascript' src='${resmod}/js/common/smate.common.js'></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<title>资助机构详情页面</title>
<style type="text/css">
.nav__item {
  height: 46px !important;
  min-height: 40px !important;
}

.item_selected {
  border-bottom: 2px solid #288aed;
}
</style>
<script type="text/javascript">
$(function(){
    var locale = '${locale}';
	FundDetail.showFundList();
	FundDetail.showLeftCondition();
	//addFormElementsEvents();
    var headerlist = document.getElementsByClassName("nav_horiz-container");
    var total = document.getElementsByClassName("header__box")[0].offsetWidth;
    var parentleft = document.getElementsByClassName("header__nav")[0].offsetLeft;
    var subleft  = document.getElementsByClassName("header-nav__item-bottom")[0].offsetWidth;
    for(var i = 0 ; i < headerlist.length; i++){
        if(!!window.ActiveXObject || "ActiveXObject" in window){
            if(locale == "en_US"){
                headerlist[i].style.right = 167 + "px"; 
            }else{
                headerlist[i].style.right = 167 + "px"; 
            }
        }else{
            if(locale == "en_US"){
                headerlist[i].style.right = 167 + "px"; 
            }else{
                headerlist[i].style.right = 167 + "px"; 
            }
        }
    }
    if(document.getElementsByClassName("nav__underline")){
        document.getElementsByClassName("nav__underline")[0].style.width =  95 + "px";
        document.getElementsByClassName("nav__underline")[0].style.left =  100 + "px";
        document.getElementsByClassName("nav__underline")[0].style.top= 38 + "px";    
    }
    var setheight = window.innerHeight - 260 - 95;
    var contentlist = document.getElementsByClassName("content-details_container");
    for(var i = 0; i < contentlist.length; i++){
        contentlist[i].style.minHeight = setheight + "px";
    }
});

//初始化 分享 插件
function initSharePlugin(obj){
	if (locale == "en_US") {
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
			'language' : 'en_US'
		});
	} else {
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
		});
	}
	//$("#share_to_scm_box").find(".nav__list .nav__item").eq(0).click();
};
//==============================
function sharePsnCallback (dynId,shareContent,resId,fundId,isB2T ,receiverGrpId){
	$.ajax({
		url : '/prjweb/fund/ajaxsharecount',
		type : 'post',
		dataType : 'json',
		data : {
			'des3FundId':resId
		},
		success : function(data) {
			if (data.result == "success" || data.shareCount > 0) {
                var count = data.shareCount;
                if(count>0 && count<1000){
                   count = "("+count+")";  
                }else if(count>=1000){
                  count = "(1k+)";  
                }
                $('.shareCount_'+fundId).html(count);   
				
			}
		}
	});
}
function shareGrpCallback (dynId,shareContent,resId,fundId,isB2T ,receiverGrpId){
	$.ajax({
		url : '/prjweb/fund/ajaxsharecount',
		type : 'post',
		dataType : 'json',
		data : {
			'des3FundId':resId
		},
		success : function(data) {
			if (data.result == "success" || data.shareCount > 0) {
                var count = data.shareCount;
                if(count>0 && count<1000){
                   count = "("+count+")";  
                }else if(count>=1000){
                  count = "(1k+)";  
                }
                $('.shareCount_'+fundId).html(count);   
			}
		}
	});
}
//==============================
//分享回调
function shareCallback (dynId,shareContent,resId,fundId,isB2T ,receiverGrpId){
    if(shareContent){
        $.ajax({
            url : '/prjweb/fund/ajaxsharecount',
            type : 'post',
            dataType : 'json',
            data : {
                'des3FundId':resId
            },
            success : function(data) {
                if (data.result == "success") {
                  var count = data.shareCount;
                  if(count>0 && count<1000){
                     count = "("+count+")";  
                  }else if(count>=1000){
                    count = "(1k+)";  
                  }
                  $('.shareCount_'+fundId).html(count);   
                }
            }
        });
    }else{
      var count = $.trim($('.shareCount_'+fundId).text());
      if(count!='(1k+)'){
         count = Number($('.shareCount_'+fundId).text().replace(/[\D]/ig,""))+1;
         if(count>=1000){
           count = "1k+";  
         }
         $('.shareCount_'+fundId).html("("+count+")");            
      }   
    }
};
</script>
</head>
<body>
  <input id="fundAgencyId" type="hidden" value="${fund.id}" />
  <input id="fundAgencyName" type="hidden" value="${fund.nameView}" />
  <input id="logoUrl" type="hidden" value="${logoUrl}" />
  <div class="result-class01" style="position: fixed; background: #fff; opacity: 1; z-index: 15; top: 47px;">
    <div class="result-class__wrap" style="display: flex; justify-content: flex-end; position: relative;">
      <nav class="nav_horiz nav_horiz-container" style="position: absolute; top: 0px; right: 524px;">
        <c:if test="${locale=='zh_CN'}">
          <ul class="nav__list">
            <li class="nav__item" style="width: 60px;"
              onclick="javascript:window.location.href='/prjweb/fund/main?module=recommend';" id="recommendFundLi"><s:text
                name='homepage.fundmain.recommend' /></li>
            <li class="nav__item" style="width: 60px;"
              onclick="javascript:window.location.href='/prjweb/fund/main?module=collected';" id="collectedFundLi"><s:text
                name='homepage.fundmain.myfund' /></li>
                <li class="nav__item" style="width: 60px;"
              onclick="javascript:window.location.href='/prjweb/fund/main?module=findFund';" id="findFundLi"><s:text
                name='homepage.fundmain.findfund' /></li>
            <li class="nav__item item_selected" style="width: 60px;"
              onclick="javascript:window.location.href='/prjweb/fund/main?module=fundAgency';" id="agencyFundLi"><s:text
                name='homepage.fundmain.agencyName' /></li>
          </ul>
        </c:if>
        <c:if test="${locale=='en_US'}">
          <ul class="nav__list">
            <li class="nav__item" onclick="javascript:window.location.href='/prjweb/fund/main?module=recommend';"
              id="recommendFundLi"><s:text name='homepage.fundmain.recommend' /></li>
            <li class="nav__item" onclick="javascript:window.location.href='/prjweb/fund/main?module=collected';"
              id="collectedFundLi"><s:text name='homepage.fundmain.myfund' /></li>
              <li class="nav__item" onclick="javascript:window.location.href='/prjweb/fund/main?module=findFund';"
              id="findFundLi"><s:text name='homepage.fundmain.findfund' /></li>
            <li class="nav__item item_selected"
              onclick="javascript:window.location.href='/prjweb/fund/main?module=fundAgency';" id="agencyFundLi"><s:text
                name='homepage.fundmain.agencyName' /></li>
          </ul>
        </c:if>
      </nav>
    </div>
  </div>
  <div class="clear_h20"></div>
  <div class="funding-agencies_neck">
    <div class="funding-agencies_neck-container">
      <img src="${logoUrl}" onerror="src='${resmod }/smate-pc/img/logo_instdefault.png'"
        class="funding-agencies_neck-container_avator">
      <div class="funding-agencies_neck-container_infor">
        <div class="funding-agencies_neck-container_infor-title">
          <a href="javascript:;" style="cursor: default;" title='${fund.nameView }'>${fund.nameView}</a>
        </div>
        <div class="funding-agencies_neck-container_infor-subhead">${fund.viewAddress}</div>
      </div>
      <div class="funding-agencies_container-right_item-Opportunity">
        <div class="funding-agencies_container-right_item-Opportunity_num">${fund.fundCount}</div>
        <div class="funding-agencies_container-right_item-Opportunity_title">
          <s:text name="homepage.fundmain.fundNum" />
        </div>
      </div>
    </div>
  </div>
  <div class="funding-agencies_container">
    <div id="leftCondition"></div>
    <div class="main-list">
      <div list-main="fund_list" class="main-list__list funding-agencies_container-right content-details_container"
        style="padding-left: 20px;"></div>
    </div>
    <jsp:include page="/common/smate.share.jsp" />
  </div>
</body>
</html>