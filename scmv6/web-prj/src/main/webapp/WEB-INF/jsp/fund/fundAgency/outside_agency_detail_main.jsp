<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta property="og:title" content="${fund.nameView}">
<meta property="og:image" content="${fund.logoUrl}">
<meta property="og:description" content="${fund.viewAddress}">
<meta property="og:site_name" content="ScholarMate">
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/css/scm-newpagestyle.css">
<link href="${resmod }/smate-pc/new-fundRecommend/public2016.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript" src="${ressns }/js/fund/fund_agency_detail.js"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_recommend.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<title>资助机构详情页面</title>
<style type="text/css">
.nav__item {
  height: 40px !important;
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
	addFormElementsEvents();
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
	$("#share_to_scm_box").find(".nav__list .nav__item").eq(0).click();
};
</script>
</head>
<body>
  <input id = "aidInsDetails_hasLogin" type = "hidden" value = "${hasLogin }"/>
  <input id="fundAgencyId" type="hidden" value="${fund.id}" />
  <input id="fundAgencyName" type="hidden" value="${fund.nameView}" />
  <input id="logoUrl" type="hidden" value="${fund.logoUrl}" />
  <div class="funding-agencies_neck">
    <div class="funding-agencies_neck-container">
      <img src="${fund.logoUrl}" onerror="src='${ressns }/images/default/default_fund_logo.jpg'"
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