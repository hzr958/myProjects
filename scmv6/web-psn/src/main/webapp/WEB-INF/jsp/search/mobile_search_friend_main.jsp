<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/search/Mobile.PsnSearch.js"></script>
<script type="text/javascript" src="${resmod}/js/search/mobile.search.scroll.js"></script>
<script type="text/javascript" src="${resmod}/js/search/mobile.addFriend.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript">
var snsctx = "${snsctx}";
$(function(){
	//ios 微信回退不刷新页面问题
	var isPageHide = false;
      window.addEventListener('pageshow', function () {
        if (isPageHide) {
          setTabs("one",3,3,$("li[id='one3']").parent().get(0));
        }
      });
      window.addEventListener('pagehide', function () {
        isPageHide = true;
      });
	mobile_bottom_setTag("link");
	searchListening();
	PsnSearch.firstload();//刚进页面时加载
	$(".header_search_input").bind("search", function() {
		var searchString = $.trim($(".header_search_input").val());
		if(searchString == ''){
			return;
		}
		$("#searchString").val(searchString);
		$("#pageNo").val(1);
		var data = {
				"searchString": searchString, 
				"orderBy":$("orderBy").val(), 
				"page.pageNo": "1",
				"fromPage": "mobileSearchFriend"
				}
		PsnSearch.ajaxFriendList(data,1);
	});
	common.scrolldirect("toolbar" ,"hide_bottom" ,"show_bottom", function(){
		var searchString = $.trim($(".header_search_input").val());
		if(searchString == ''){
			return;
		}
		var data = {
				"searchString": searchString, 
				"orderBy":$("orderBy").val(), 
				"page.pageNo":parseInt($("#pageNo").val()) + 1,
				"fromPage": "mobileSearchFriend"
				}
		PsnSearch.ajaxFriendList(data,2);
	});
	//加载圈圈
	$("#load_preloader").doLoadStateIco({
		status:1
	});
	$("#load_preloader").hide();
	
});
function setTabs(name,cursel,n, obj){
    for(i=1;i<=n;i++){
        var menu=document.getElementById(name+i);
        menu.className=i==cursel?"hover":"";
    }
    var hrefVal = $(obj).attr("href") + "?searchString=" + $.trim($("#searchString").val());
    $(obj).attr("href", hrefVal);
}
//清空搜索框
function clean_search() {
	$(".header_search_input").val("");
	$(".header_search_cancel").hide();
};

//返回联系菜单页面
function search_friend_back() {
	window.location.href = "/psnweb/mobile/relationmain";
};

//检索框监听事件
function searchListening(){
	$(".header_search_input").keyup(function(){
		var searchkey = $.trim($(this).val());
		if(searchkey!=""&&searchkey.length>0){
			$(".header_search_cancel").show();
		} else {
			$(".header_search_cancel").hide();
		}
	});
};

//打开个人主页
function open_outhome(des3PsnId) {
	window.location.href = "/psnweb/mobile/outhome?des3ViewPsnId="+des3PsnId;
};
</script>
</head>
<body>
  <form action="#" onsubmit="return false;">
    <div class="header">
      <div class="header_toolbar">
        <div class="header_toolbar_tools">
          <div class="header_toolbar_icon" style="width: 16px;" onclick="search_friend_back();">
            <i class="material-icons">keyboard_arrow_left</i>
          </div>
        </div>
        <div class="header_toolbar_title" style="padding: 12px 10px;">
          <div class="header_search_container">
            <input type="search" placeholder="输入姓名发现人员" class="header_search_input">
            <div style="display: none;" onclick="clean_search();" class="header_search_cancel">
              <i class="material-icons fz_14">close</i>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
  <div style="height: 56px;"></div>
  <div class="body_content">
    <div class="list_container">
      <div class="list_item_container psn_item_notice">
        <div class="list_item_section"></div>
        <div class="list_item_section">
          <div class="person_namecard_whole hasBg">请输入信息检索人员</div>
        </div>
        <div class="list_item_section"></div>
      </div>
    </div>
    <div id="load_preloader"></div>
  </div>
  <input type="hidden" id="currentDes3PsnId" name="currentDes3PsnId" value="${des3PsnId}" />
  <input id="pageNo" name="page.pageNo" type="hidden" value="${page.pageNo}" />
  <input id="searchString" name="searchString" type="hidden" value="<c:out value='${searchString}'/>" />
  <s:include value="/WEB-INF/jsp/mobile/bottom/mobile_bottom.jsp"></s:include>
</body>
</html>
