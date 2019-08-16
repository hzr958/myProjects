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
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${ressns}/js/fund/fund_mobile_recommend_${locale}.js"></script>
<script type="text/javascript" src="${ressns}/js/fund/fund_mobile_recommend.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript">
$(function(){
    $('.dev_region').show();
    $(".focus__region>a").each(function(){//勾选地区
    	var code = $(this).attr("value");
    	$("li[code='"+code+"']").find("i").addClass("icon__region--slt");
    });
});

function searchRegion() {
	var searchKey = $.trim($('#dev_region_search').val());
	if (searchKey != "") {
		$.ajax({
			url: '/prjweb/wechat/ajaxsearchregion',
			type: 'post',
			dataType: 'html',
			data: {"searchKey":searchKey},
			success: function(data) {
				$('.dev_region').html("");
				$('.dev_region').html(data);
			}
		});
	}
};

function deleteRegion(obj,code){
	$.ajax({
        url:"/prjweb/wechat/ajaxdeleteregion",
        type:"post",
        data:{"regionCode":code},
        dataType:"json",
        success: function(data){
            if(data.result="success"){
                $(obj).closest("a").remove();
            }else{
                scmpublictoast("删除失败!",2000,2);
            }
        },
        error: function(data){
            scmpublictoast("删除失败!",2000,2);
        }
    });
}
function goback(){
    $("#pub_search").submit();
};
</script>
</head>
<body>
  <form id="pub_search" method="post" action="/prjweb/wechat/findfundcondition">
    <input type="hidden" id="searchAreaCodes" name="searchAreaCodes" value="${searchAreaCodes}" /> <input type="hidden"
      id="searchRegionCodes" name="searchRegionCodes" value="${searchRegionCodes}" /> <input type="hidden"
      id="searchTimeCodes" name="searchTimeCodes" value="${searchTimeCodes}" /> <input type="hidden"
      id="searchseniority" name="searchseniority" value="${searchseniority}" /> <input type="hidden" id="defultArea"
      name="defultArea" value="${defultArea}" />
  </form>
  <div class="top">
    <div class="top__mn dj_s">
      <input required placeholder="选择地区" type="search" id="dev_region_search" class="top__input fl"
        oninput="searchRegion();" style="line-height: 24px;" /> <input value="完成" type="button"
        class="top__ensure--btn" onclick="goback();">
    </div>
  </div>
  <div class="top__clear"></div>
  <div class="content dev_region" style="display: none;">
    <!--  style="display: none;" -->
    <h2 class="slt__region">关注的地区</h2>
    <div class="focus__region">
      <s:if test="fundRegionList.size()>0">
        <s:iterator value="fundRegionList" var="region">
          <a href="javascript:;" value='${region.regionId}'><c:out value="${region.showName}" /><i
            class="focus__region--delete" onclick="deleteRegion(this,'${region.regionId}')"></i></a>
        </s:iterator>
      </s:if>
      <a href="javascript:;">不限</a>
    </div>
    <h2 class="slt__region">选择地区</h2>
    <div class="region__list--slt">
      <ul>
        <s:iterator value="allProvinceList">
          <li onclick="location.href='/prjweb/wechat/queryareanext?superRegionId=${id}'" code="${regionCode}"><a
            href="javascript:;">${zhName}</a><i class="dev_region_choose" style="margin-top: 0.75rem;"></i></li>
        </s:iterator>
      </ul>
    </div>
  </div>
  <div id="div_preloader"></div>
</body>
</html>
