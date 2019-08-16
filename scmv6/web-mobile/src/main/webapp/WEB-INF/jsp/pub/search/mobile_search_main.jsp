<!doctype html>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/search.history.js"></script>
<script type="text/javascript" src="${resmod }/js/search.history.controller.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type=text/javascript>
	$(function(){ 
	    var value=$("#searchStringInput").val(); //获取光标最后一个文字的后面获得焦点
		$("#searchStringInput").val("").focus().val(value); 
        history.showHistory();
	    $("#search_filter_icon").hide();
	    initSearchForUrl();
	    
	    //初始化检索框 
	    var searchString = $("#searchString").val();
	    var options = {
	        "searchFunc": "doSearch()",
	        "inputOnkeydown": "entersearch()",
	        "searchFilter": "",
	        "inputOnfocus": "history.showHistory()",
	        "placeHolder":"检索论文、专利、人员...",
	        "searchInputVal":searchString
	    };
	    commonMobileSearch.initSearchInput(options);

	});
   
	function clearSearchString(){
		$("#searchStringInput").val("");
		$("#searchString").val("");
	}
	
	function initSearchForUrl(){
	    var searchFor = $("#searchFor").val();
	    if("psn" == searchFor){
	        $("#pubsForm").attr("action", "/psnweb/mobile/search");
	    }else if("patent" == searchFor){
	        $("#pubsForm").attr("action", "/pub/patent/search");
	    }else{
	        $("#pubsForm").attr("action", "/pub/paper/search");
	    }
	}
	function doSearch(){
	    //保存到历史记录
	   var his = new History($("#desPsnId").val());
	   var keyword=$.trim($("#searchStringInput").val());
	   $("#searchString").val(keyword);
	   var url = $("#pubsForm").attr("action");
	   his.add(keyword, url+"?searchString="+keyword+"&fromPage=dyn", "");
	   $("#pubsForm").submit();
	}
	function entersearch(){
	   var event = window.event || arguments.callee.caller.arguments[0];  
	   if (event.keyCode == 13){  
	      doSearch();
	   }  
	}
	function hisKeyHref(key,href){
	   $("#searchStringInput").val(key);
	   $("#searchString").val(key);
	   doSearch();
	}
	function goBack(){
	  if(location.href.indexOf("/pub/search/main") != -1 && $("#fromPage").val() == "relationmain"){
	    location.href="/psnweb/mobile/relationmain";
	  }else{
	     SmateCommon.goBack('/dynweb/mobile/dynshow');
	  } 
	}
</script>
</head>
<body class="retrieve_bg">

  <input id="desPsnId" type="hidden" value="${desPsnId }" />
  <div class="m-top m-top_top-background_color">
    <a onclick="goBack();" class="fl" style="width: 46px;"><i
      class="material-icons navigate_before">&#xe408;</i></a> <span class="m-top_top-background_color-title"> 全站检索 </span>
  </div>
  <div class="paper__func-tool" style="background: #fff; height: 52px; top: 45px;">
     <%@ include file="/common/mobile/common_mobile_search_input.jsp"%>
  </div>
  <div class="top_clear"></div>

  <div class="top_clear"></div>
  <div class="content">
    <div id="showHistory"></div>
    <div class="may_like dev_history_comment">
      <h1>我可能感兴趣的</h1>
      <ul class="drop-shadow curved curved-hz-1">
        <li class="paper_box"><a><i class="paper_icon"></i><span>论文</span></a></li>
        <li class="patent_box"><a><i class="patent_icon"></i><span>专利</span></a></li>
        <li class="person_box"><a><i class="person_icon"></i><span>人员</span></a></li>
      </ul>
    </div>
    <form name="pubsForm" id="pubsForm" action="/pub/paper/search" method="post">
      <input name="searchString" id="searchString" type="hidden" value="<c:out value="${model.searchString}"/>" /> <input
        name="searchFor" id="searchFor" type="hidden" value="${model.searchFor}" /> <input
        name=fromPage id="fromPage" type="hidden" value="${model.fromPage}" />
        
    </form>
  </div>
</body>
</html>
