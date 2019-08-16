<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no" />
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod }/css/wechat.iscroll.css?version=2" media="all" rel="stylesheet" type="text/css" />
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css" />
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet" />
<link href="${resmod}/mobile/css/common_HideShowDiv.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
<%-- <link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/css/scmpcframe.css"> --%>
<%-- <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css"> 2333--%>
  <script type="text/javascript" src="${resmod}/js/jquery.js"></script>
  <script type="text/javascript" src="${resmod }/js/weixin/iscroll.js?version=1"></script>
  <script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
  <script type="text/javascript" src="${resmod}/js/search/Mobile.PsnSearch.js"></script>
  <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=2"></script>
  <script type="text/javascript" src="${resmod}/mobile/js/wechat.custom.js"></script>
  <script type="text/javascript" src="${resmod }/js/search/mobile.search.scroll.js"></script>
  <script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
  <script type="text/javascript" src="${resmod }/js/search.history.js"></script>
  <script type="text/javascript" src="${resmod }/js/search.history.controller.js"></script>
  <script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script>

$(function(){
    var searchString = $("#searchString").val();
    var option = {
      "searchFunc": "onclickSearch()",
      "inputOnkeydown": "entersearch()",
      "searchFilter": "select_filter()",
      "inputOnfocus": "history.showHistory()",
      "placeHolder":"检索人员",
      "searchInputVal":searchString,
    };
	commonMobileSearch.initSearchInput(option);
	if(( $.isEmptyObject($("#searchString").val()) || $.isEmptyObject($("#searchStringInput").val())) && !$.isEmptyObject(getUrlParameter("searchString"))){
    $("#searchString").val(getUrlParameter("searchString"));
    $("#searchStringInput").val(getUrlParameter("searchString"));
  }
	firstload();
	common.scrolldirect("toolbar" ,"hide_bottom" ,"show_bottom", function(){
		var searchString = $.trim($("#searchStringInput").val());
		if(searchString == ''){
			return;
		}
		if($("#psn_list").find(".noRecord").length==1){
			return;
		}
		var data = {
				"searchString": searchString, 
				"orderBy":$("orderBy").val(), 
				"page.pageNo":parseInt($("#pageNo").val()) + 1,
				}
		PsnSearch.ajaxlistnew(data,2);
		
	});
});

function getUrlParameter(name){
  var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
  var r = window.location.search.substr(1).match(reg);//search,查询？后面的参数，并匹配正则
  if(r!=null)return  decodeURIComponent(r[2]); return null;
}

//刚进页面是的加载
function firstload(){
	var searchString = $.trim($("#searchStringInput").val());
	if(searchString == ''){
		return;
	}
	var data = {
			"searchString": searchString, 
			"page.pageNo":1
			}
	$("#pageNo").val(1);
	PsnSearch.ajaxlistnew(data,1);
}
function search(){
	var searchString = $.trim($("#searchStringInput").val());
	if(searchString == ''){
		return;
	}
	//保存到历史记录
    var his = new History($("#desPsnId").val());
	var keyword=searchString;       
    his.add(keyword, "/pub/paper/search?searchString="+keyword+"&fromPage="+$("#fromPage").val(), "");
	$("#searchString").val(searchString);
	$("#topsn").click();
//	window.location.href=$("#topsn").attr("href");
	return;
	$("#pageNo").val(1);
	var data = {
			"searchString": searchString, 
			"orderBy":$("orderBy").val(), 
			"page.pageNo": "1"
			}
	PsnSearch.ajaxlistnew(data,1);
}
function clearSearchString(){
  $("#searchStringInput").val("");
}
function entersearch(){
   var event = window.event || arguments.callee.caller.arguments[0];  
   if (event.keyCode == 13){  
       search();  
   }  
}
function onclickSearch(){
  search();  
}
function goBack(){
  if(location.href.indexOf("/pub/search/main") != -1 && $("#fromPage").val() == "relationmain"){
    location.href="/psnweb/mobile/relationmain";
  }else{
     SmateCommon.goBack('/dynweb/mobile/dynshow');
  } 
}
function hisKeyHref(key,href){
  $("#searchString").val(key);
  $("#topsn").click();
}
</script>
</head>
<body>
  <div class="m-top">
    <a href="javascript:void();" onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');" class="rtn_icon"></a>
    <div class="m-top m-top_top-background_color">
      <a onclick="goBack();" class="fl"><i class="material-icons navigate_before">&#xe408;</i></a> <span
        class='m-top_top-background_color-title'>全站检索</span>
    </div>
    <s:include value="/common/mobile/common_mobile_search_input.jsp"></s:include>
  </div>
  <div class="top_clear"></div>
  <div id="showHistory" style="padding-top: 92px; display:none;"></div>
  <div class="dev_history_comment" id="Tab_1">
    <s:include value="mobile_search_head_tab.jsp"></s:include>
  </div>
  <form id="psnForm" action="/psnweb/mobile/search" method="post">
  <input id="fromPage" name="fromPage" type="hidden" value="${fromPage}" />
    <input id="pubYear" name="pubYear" type="hidden" value="${pubYear}" /> <input id="orderBy" name="orderBy"
      type="hidden" value="${orderBy }" /> <input id="pageNo" name="page.pageNo" type="hidden" value="${page.pageNo }" />
    <input id="searchString" name="searchString" type="hidden" value="<c:out value='${searchString}'/>" /> <input
      id="desPsnId" type="hidden" value="${des3PsnId }" />
    <div class="wrap_com1 retrieve_member dev_history_comment" id="psn_list"
      style="margin-top: 100px; flex; flex-direction: column; justify-content: center; align-items: center;">
      <div id="mobile_psn_list" style="margin-top: 20px; margin: 0 auto;">
        <div class='load_preloader' style='height: 0px; border: 0px'></div>
      </div>
    </div>
  </form>
  <s:if test="#request.hasLogin == 'no'">
    <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
  </s:if>
</body>
</html>
