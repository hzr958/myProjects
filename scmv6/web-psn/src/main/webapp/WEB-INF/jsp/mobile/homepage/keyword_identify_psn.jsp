<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>科研之友</title>
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod }/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/wechat.custom.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mui/mui.min.css">
<script type="text/javascript" src="${resmod}/mobile/js/plugin/mui/mui.min.js"></script>
</head>
<body>
  <input type="hidden" id="des3PsnId" name="des3PsnId" value="${des3PsnId }">
  <input type="hidden" id="discId" name="discId" value="${discId }">
  <input type="hidden" id="isOthers" name="isOthers" value="${isOthers }">
  <header class="mui-bar mui-bar-nav m-top m-top_top-background_color">
    <a id="back" class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left color_white"></a>
    <h1 id="title" class="mui-title">认同者</h1>
  </header>
  <div class="mui-content"></div>
  <!--下拉刷新容器-->
  <div id="pullrefresh" class="mui-content mui-scroll-wrapper">
    <div class="mui-scroll">
      <!--数据列表-->
      <div id="identify_psn_list"></div>
    </div>
  </div>
  <script>
        
        /**
         * 下拉刷新具体业务实现
         */
        function pulldownRefresh() {
            setTimeout(function() {
                $.ajax({
                    url: "/psnweb/outside/mobile/ajaxidentifypsn",
                    type: "post",
                    dataType: "html",
                    data: {
                        "page.pageNo": 1,
                        "des3PsnId": $("#des3PsnId").val(),
                        "discId": $("#discId").val(),
                        "serviceType": "kwIdentific",
                        "isOthers": $("#isOthers").val()
                    },
                    success: function(data){
                        if(data != null){
                        	$("#identify_psn_list").html(data);
                        }
                        mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
                        mui('#pullrefresh').pullRefresh().refresh(true);
                    },
                    error: function(){
                    	mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
                    	mui('#pullrefresh').pullRefresh().refresh(true);
                    }
               });
            }, 1500);
        }
        /**
         * 上拉加载具体业务实现
         */
        function pullupRefresh() {
            var currentPageNo = parseInt($("#pageNo").val());
            var totalPages = parseInt($("#totalPage").val());
            var noMorePage = currentPageNo == totalPages;
            setTimeout(function() {
                mui('#pullrefresh').pullRefresh().endPullupToRefresh((currentPageNo == totalPages)); //参数为true代表没有更多数据了。
                if(!noMorePage){
	                $.ajax({
	                    url: "/psnweb/outside/mobile/ajaxidentifypsn",
	                    type: "post",
	                    dataType: "html",
	                    data: {
	                        "page.pageNo": parseInt($("#pageNo").val()) + 1,
	                        "des3PsnId": $("#des3PsnId").val(),
	                        "discId": $("#discId").val(),
	                        "serviceType": "kwIdentific",
	                        "isOthers": $("#isOthers").val()
	                    },
	                    success: function(data){
	                        if(data != null){
	                        	$("#pageNo").remove();
	                        	$("#totalPage").remove();
	                            $("#identify_psn_list").append(data);
	                        }
	                        mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
	                    },
	                    error: function(){
	                        mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
	                    }
	               });
                }
            }, 1500);
        }
        
        
        function ajaxKeywordsIdentifyPsnList(){
        	$.ajax({
        		 url: "/psnweb/outside/mobile/ajaxidentifypsn",
                 type: "post",
                 dataType: "html",
                 data: {
                     "page.pageNo": 1,
                     "des3PsnId": $("#des3PsnId").val(),
                     "discId": $("#discId").val(),
                     "serviceType": "kwIdentific",
                     "isOthers": $("#isOthers").val()
                 },
                 success: function(data){
                     if(data != null){
                    	 $("#identify_psn_list").html(data);
                     }
                 },
                 error: function(){}
        	});
        }
        
        mui.init({
            pullRefresh: {
                container: '#pullrefresh',
                down: {
                    callback: pulldownRefresh
                },
                up: {
                    contentrefresh: '正在加载...',
                    callback: pullupRefresh
                }
            }
        });
        
        $(function(){
        	ajaxKeywordsIdentifyPsnList();
        })
        
    </script>
</body>
</html>