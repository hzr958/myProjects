<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<link href="${resmod}/mobile/css/common_HideShowDiv.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css" />
<%-- <link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css"/> --%>
<%-- <link href="${resmod}/css/smate.scmtips.css?version=1"   media="all" rel="stylesheet" type="text/css"> --%>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil.js"></script>
<script type="text/javascript" src="${resmod }/js/search/mobile.search.scroll.js"></script>
<%-- <script type="text/javascript" src="${resmod }/js/smate.scmtips.js?version=1"></script> --%>
<script type="text/javascript" src="${resmod }/mobile/js/msgbox/mobile.msg.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/plugin/moveorclick.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/plugin/mobile.moveitem.activity.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/relation/mobile.psn.relation_${locale}.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/relation/mobile.psn.relation.js"></script>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/search.history.js"></script>
<script type="text/javascript" src="${resmod }/js/search.history.controller.js"></script>

<script type="text/javascript">

$(document).ready(function(){
	mobile_bottom_setTag("link");
	searchListening();
	reloadMayKnowPsn();
	loadMoreMayKnowPsn();
	$("#header_search_input").bind("search", function() {
		window.location.href="/psnweb/mobile/search?searchString=" + $("#header_search_input").val();
	});
	loadMorefriendPsn();
	var objDiv = $("#div_preloader");
	objDiv.doLoadStateIco({
		style:"height:28px; width:28px; margin:auto;margin-top:24px;",
		status:1
	});
	
  //初始化检索框 
  var options = {
      "searchFunc": "doSearch()",
      "inputOnkeydown": "entersearch()",
      "searchFilter": "",
      "inputOnfocus": "history.showHistory()",
      "placeHolder":"检索论文、专利、人员...",
      "searchInputVal": "" //检索的字符串
  };
  $("#searchStringInput").css("padding-left","5px");
  commonMobileSearch.initSearchInput(options);
});

function doSearch(){
  //保存到历史记录
  var his = new History($("#desPsnId").val());
  var keyword=$.trim($("#searchStringInput").val());
  his.add(keyword, "/pub/paper/search?searchString="+keyword+"&fromPage=dyn", "");
  history.UpdateUrlParam("searchString",keyword);
  window.location.href = "/pub/paper/search?searchString="+keyword+"&fromPage=dyn"
}
function entersearch(){
  var event = window.event || arguments.callee.caller.arguments[0];  
  if (event.keyCode == 13){  
    doSearch();
  }  
}

//联系人列表
function toFriendList(){
	window.location.href="/psnweb/mobile/friendlist";
}

//上拉加载更多可能认识的人
function loadMoreMayKnowPsn(){
	common.scrolldirect("toolbar" ,"hide_bottom" ,"show_bottom", loadPsnMayKonw);
}

//发现联系人
function toSearchFriend() {
	window.location.href = "/pub/search/main?searchFor=psn"; 
};

//检索联系人
function searchPsn(){
	window.location.href = "/psnweb/mobile/search?searchString=" + $("#header_search_input").val();
}

//清空检索框
function clearSearchString(){
	$("#header_search_input").val("");
	$(".header_search_cancel").hide();
}

//检索框监听事件
function searchListening(){
	$("#header_search_input").keyup(function(){
		var searchkey = $.trim($(this).val());
		if(searchkey!=""&&searchkey.length>0){
			$(".header_search_cancel").show();
		} else {
			$(".header_search_cancel").hide();
		} 
	});
};

/* function msg_menu_find(){
	window.location.href = "/pub/search/main?searchFor=psn";
};  */
</script>
<script type="text/javascript">
  var ctxpath="/scmwebsns";
</script>
</head>
<body onpageshow="clearSearchString()">
  <input type="hidden" id="currentDes3PsnId" name="currentDes3PsnId" value="${des3PsnId}" />
  <input type="hidden" id="lastPsnId" name="lastDes3PsnId" value="" />
  <input type="hidden" id="loadFinish" name="loadFinish" value="true" />
  <form action="" id="searchFrom" onsubmit="return false;">
    <div class="m-top m-top_top-background_color">
      
      <span style="width: 100vw; display: block; text-align: center;">联系人</span>
    </div>
    <div class="paper__func-tool" style="background: #f4f4f4; height: 52px; top: 45px;">
       <input type="hidden" id="desPsnId" name="desPsnId" value="${des3PsnId}"/>
       <%@ include file="/common/mobile/common_mobile_search_input.jsp" %>

    </div>
    <div id="showHistory" style="padding-top: 92px;display:none;"></div>
    <div class="top_clear"></div>
  </form>
  <div style="height: 56px;"></div>
  <div class="body_content dev_history_comment">
    <div class="tabs_whole" style="background-color: #ffffff; position: relative;">
      <div class="tabs_single active" onclick="toFriendList();" style="display: inline-block; text-align: center;">
        <s:if test="friendCount > 0">
          <span class="fc_blue500">${friendCount }</span>位联系人</s:if>
        <s:else>我的联系人</s:else>
      </div>
      <div class="vertical_divider"></div>
      <div class="tabs_single active" onclick="toSearchFriend();">新增联系人</div>
      <!-- <div class="new-find_friend-tool" >
            <div class="new-find_friend-tool_up">
                <i class="new-find_friend-tool_up-tip"></i>
                <span class="new-find_friend-tool_detail">通讯录</span>
            </div>
            <div class="new-find_friend-tool_down">
                <i class="new-find_friend-tool_down-tip"></i>
                <span  class="new-find_friend-tool_detail">扫一扫</span>
            </div>
        </div> -->
    </div>
    <div class="body_content_container" id="friendRequset">
      <div style="height: 4px; background: #e9e9e9;"></div>
      <div class="list_container" id=personRequset>
        <div class="title">联系人请求</div>
      </div>
    </div>
    <div class="body_content_container">
      <input type="hidden" name="currPage" id="currPage" value="0" />
      <div style="height: 4px; background: #e9e9e9;"></div>
      <div class="list_container" id="psnMayKnow">
        <div class="title" style="padding: 0px; border-bottom: 1px solid #ddd;padding-left: 20px;">可能认识的人</div>
      </div>
    </div>
    <div id="div_preloader"></div>
  </div>
  <div style="height: 56px;"></div>
  <jsp:include page="../bottom/mobile_bottom.jsp"></jsp:include>
  
</body>
</html>
