<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no" />
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mobile.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/css/mobilefund/css/public.css" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${ressns}/js/fund/fund_mobile_recommend_${locale}.js"></script>
<script type="text/javascript" src="${ressns}/js/fund/fund_mobile_recommend.js"></script>
<script type="text/javascript">
$(function(){
	$(".selectArea").each(function(){
		var code = $(this).val();
		$("li[areaId='"+code+"']").find("i").addClass("icon__region--slt");
	});
});
</script>
</head>
<body>
  <form id="fundAreaForm" name="fundAreaForm" method="post" action="/prjweb/mobile/savefundarea">
    <input type="hidden" id="selectedAreaId" name="areaIds" value="" />
  </form>
  <s:if test="fundRegionList.size()>0">
    <s:iterator value="fundRegionList" var="region">
      <input type="hidden" class="selectArea" value='${region.regionId}'>
    </s:iterator>
  </s:if>
  <div class="page-header page-file__header" style="position: fixed; top: 0px;">
    <i class="material-icons page-file__tip" style="margin-left: 10px" onclick="FundRecommend.saveSelectedArea();">keyboard_arrow_left</i>
    <div class="page-file__title" style="margin-left: -20px">选择地区</div>
  </div>
  <div class="top__clear"></div>
  <div class="content">
    <div class="region__list">
      <ul>
        <s:iterator value="areaNextList">
          <li onclick="FundRecommend.chooseArea(this)" areaId="${id }"><a href="javascript:;">${zhName}</a><i
            class="dev_region_choose"></i></li>
        </s:iterator>
      </ul>
    </div>
  </div>
</body>
</html>
