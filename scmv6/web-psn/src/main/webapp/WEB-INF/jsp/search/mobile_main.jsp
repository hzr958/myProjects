<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8">
  <meta name="viewport"
    content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no" />
    <meta name="format-detection" content="email=no" />
    <title>科研之友</title>
    <link href="${resmod }/css/wechat.iscroll.css?version=2" media="all" rel="stylesheet" type="text/css">
      <link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
        <link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
          <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
            <script type="text/javascript" src="${resmod}/js/jquery.js"></script>
            <script type="text/javascript" src="${resmod }/js/weixin/iscroll.js?version=1"></script>
            <script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
            <script type="text/javascript" src="${resmod}/js/search/Mobile.PsnSearch.js"></script>
            <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=2"></script>
            <script type="text/javascript" src="${resmod}/mobile/js/wechat.custom.js"></script>
            <script>
$(function(){
	firstload();
	$("#psn_list").html("输入关键词检索");
	$(".pullUpLabel").hide();
	scrollDirect2();
});
//刚进页面是的加载
function firstload(){
	var searchString = $.trim($("#searchStringInput").val());
	if(searchString == ''){
		return;
	}
	var data = {
			"searchString": searchString, 
			"page.pageNo":1
			};
	$("#pageNo").val(1);
	PsnSearch.ajaxlist(data,1);
}
function search(){
	var searchString = $.trim($("#searchStringInput").val());
	if(searchString == ''){
		return;
	}
	$("#searchString").val(searchString);
	$("#topsn").click();
	window.location.href=$("#topsn").attr("href");
	return;
	$("#pageNo").val(1);
	var data = {
			"searchString": searchString, 
			"orderBy":$("orderBy").val(), 
			"page.pageNo": "1"
			};
	PsnSearch.ajaxlist(data,1);
}

var myScroll,
pullDownEl, pullDownOffset,
pullUpEl, pullUpOffset,
generatedCount = 0,goalOffset=10;

function pullDownAction () {
	var searchString = $.trim($("#searchStringInput").val());
	if(searchString == ''){
		return;
	}
	var data = {
			"searchString": searchString, 
			"orderBy":$("orderBy").val(), 
			"page.pageNo": "1"
			};
	PsnSearch.ajaxlist(data,1);
	myScroll.refresh();		// Remember to refresh when contents are loaded (ie: on ajax completion)
}

function pullUpAction () {
	var searchString = $.trim($("#searchStringInput").val());
	if(searchString == ''){
		return;
	}
	var data = {
			"searchString": searchString, 
			"orderBy":$("orderBy").val(), 
			"page.pageNo":parseInt($("#pageNo").val()) + 1
			};
	PsnSearch.ajaxlist(data,2);
	$("#pageNo").val(parseInt($("#pageNo").val()) + 1);
	
	// Remember to refresh when contents are loaded (ie: on ajax completion)
}
function loadnextpage(){
	var searchString = $.trim($("#searchStringInput").val());
	if(searchString == ''){
		return;
	}
	var data = {
			"searchString": searchString, 
			"page.pageNo":parseInt($("#pageNo").val()) + 1
			};
	PsnSearch.ajaxlist(data,2);
	
}
scrollDirect2  = function (fn) {
	var beforeScrollTop = window.pageYOffset
	|| document.documentElement.scrollTop
	|| document.body.scrollTop
	|| 0;
    fn = fn || function () {
    };
    window.addEventListener("scroll", function (event) {
        event = event || window.event;
        var afterScrollTop = window.pageYOffset
    	|| document.documentElement.scrollTop
    	|| document.body.scrollTop
    	|| 0;
        delta = afterScrollTop - beforeScrollTop;
        beforeScrollTop = afterScrollTop;
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight == scrollHeight) { 
        	pullUpAction();
            return;
        }
    }, false);
}

function loaded() {
	pullDownEl = document.getElementById('pullDown');
	pullDownOffset = pullDownEl.offsetHeight;
	pullUpEl = document.getElementById('pullUp');	
	pullUpOffset = pullUpEl.offsetHeight;
	myScroll =  new  iScroll('wrapper', {
		useTransition: true,
		topOffset: pullDownOffset,
		onRefresh: function () {
			if (pullDownEl.className.match('loading')) {
				pullDownEl.className = '';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
			} else if (pullUpEl.className.match('loading')) {
				pullUpEl.className = '';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
			}
		},
		onScrollMove: function () {
			if (this.y > goalOffset && !pullDownEl.className.match('flip')) {
				pullDownEl.className = 'flip';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '释放刷新...';
				this.minScrollY = 0;
			} else if (this.y < goalOffset && pullDownEl.className.match('flip')) {
				pullDownEl.className = '';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
				this.minScrollY = -pullDownOffset;
			} else if (this.y < (this.maxScrollY - goalOffset) && !pullUpEl.className.match('flip')) {
				pullUpEl.className = 'flip';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '释放加载...';
				this.maxScrollY = this.maxScrollY;
			} else if (this.y > (this.maxScrollY + goalOffset) && pullUpEl.className.match('flip')) {
				pullUpEl.className = '';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
				this.maxScrollY = pullUpOffset;
			}
		},
		onScrollEnd: function () {
			if (pullDownEl.className.match('flip')) {
				pullDownEl.className = 'loading';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '正在刷新...';				
				pullDownAction();	// Execute custom function (ajax call?)
			} else if (pullUpEl.className.match('flip')) {
				pullUpEl.className = 'loading';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';				
				
				pullUpAction();	// Execute custom function (ajax call?)
			}
		}
	});
     setTimeout(function () { document.getElementById('wrapper').style.left = '0'; }, 800);
}
document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 400); }, false);
</script>
</head>
<body>
  <div class="m-top">
    <a href="javascript:void();" onclick="window.history.back();" class="rtn_icon"></a>
    <s:include value="mobile_search_head_searchinput.jsp"></s:include>
  </div>
  <div class="top_clear"></div>
  <div id="Tab_1">
    <s:include value="mobile_search_head_tab.jsp"></s:include>
  </div>
  <form id="psnForm" action="/psnweb/mobile/search" method="post">
    <input id="pubYear" name="pubYear" type="hidden" value="${pubYear}" /> <input id="orderBy" name="orderBy"
      type="hidden" value="${orderBy }" /> <input id="pageNo" name="page.pageNo" type="hidden" value="${page.pageNo }" />
    <input id="searchString" name="searchString" type="hidden" value="<c:out value='${searchString}'/>" />
    <div id="wrapper" style="background-color: white; margin-top: -20px;">
      <div id="scroller">
        <div id="pullDown">
          <span class="pullDownIcon"></span><span class="pullDownLabel">下拉刷新...</span>
        </div>
        <div id="bgdiv"
          <s:if test="page.result==null || 10>page.result.size() ">style="display: none;width: 100%;height: 1000%;z-index:1500;"</s:if>
          class="bg" style="display: none; z-index: 1200;" onclick="bgclick();"></div>
        <div class="wrap_com1 retrieve_member" id="psn_list">
          <s:include value="mobile_list.jsp"></s:include>
        </div>
        <div id="pullUp">
          <span class="pullUpIcon"></span><span class="pullUpLabel">上拉加载更多...</span>
        </div>
      </div>
    </div>
  </form>
  </div>
</body>
</html>
