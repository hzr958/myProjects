<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/influence/mobile_influence.js"></script>
<script src="${resmod}/js_v5/echarts/echarts-3.8.5.js"></script>
<script src="${resmod}/js_v5/echarts/theme/walden.js"></script>
<script type="text/javascript">
		    $(function(){
		        document.getElementById("big_visit_trend_img").style.height = window.innerHeight - 48 + "px";
		        document.getElementById("dev_cite_trend_big").style.height = window.innerHeight - 48 + "px";
		        setTimeout(function(){
            	   document.getElementById("big_visit_trend_img").style.display="none";
                   document.getElementById("dev_cite_trend_big").style.display="none";
		        },200);
		        MobileInfluence.getPsnStatistics();//获取人员各项统计数
		        MobileInfluence.showVisitTrend();//显示阅读数趋势图
		        MobileInfluence.showBarMap();//显示引用趋势图
		        document.getElementsByClassName("new-Influencefactor_body")[0].style.height = window.innerHeight + "px";
		        var showbox = document.getElementsByClassName("new-Influencefactor_body-item_infor");
		        window.onresize = function(){
		            if(window.innerHeight < window.innerWidth){
		                //此时横屏
		                for(key in showbox){
		                  
		                }
		            }else{
		                for(key in showbox){
                         
                        }
		            }
		        }
		    })
    </script>
</head>
<body>
  <div class="message-page__header"
    style="position: fixed; top: 0px; z-index: 55; display: flex; justify-content: space-between; flex-grow: 1;">
    <i class="material-icons new-Influencefactor_header-left" onclick="MobileInfluence.goBack()">keyboard_arrow_left</i> <span
      class="new-Influencefactor_header-center">科研影响力</span> <i class="new-Influencefactor_header-right"></i>
  </div>
  <div class="new-Influencefactor_body influence_module" style="overflow: scroll;">
    <div class="new-Influencefactor_neck" style="padding-top:38px;">
      <div class="new-Influencefactor_neck-item">
        <span class="new-Influencefactor_neck-item_content">赞/分享</span> <span class="new-Influencefactor_neck-item_cnt"
          id="influence_award_share_sum">0</span>
      </div>
      <div class="new-Influencefactor_neck-item">
        <span class="new-Influencefactor_neck-item_content">阅读</span> <span class="new-Influencefactor_neck-item_cnt"
          id="influence_visit_sum">0</span>
      </div>
      <div class="new-Influencefactor_neck-item">
        <span class="new-Influencefactor_neck-item_content">下载</span> <span class="new-Influencefactor_neck-item_cnt"
          id="influence_download_sum">0</span>
      </div>
      <div class="new-Influencefactor_neck-item">
        <span class="new-Influencefactor_neck-item_content">引用</span> <span class="new-Influencefactor_neck-item_cnt"
          id="influence_cited_sum">0</span>
      </div>
      <div class="new-Influencefactor_neck-item">
        <span class="new-Influencefactor_neck-item_content">H-index</span> <span
          class="new-Influencefactor_neck-item_cnt" id="influence_hindex">0</span>
      </div>
    </div>
    <div class="new-Influencefactor_body-item" style="padding-top:20px;">
      <div class="new-Influencefactor_body-item_title">
        <span class="new-Influencefactor_body-item_title-detaile">阅读数趋势</span> <i
          class="new-Influencefactor_body-item_title-icon" onclick="MobileInfluence.showBigVisitTrend();"></i>
      </div>
      <div class="new-Influencefactor_body-item_infor" id="linemap"></div>
    </div>
    <div class="new-Influencefactor_body-item dev_cite_thread">
      <div class="new-Influencefactor_body-item_title">
        <span class="new-Influencefactor_body-item_title-detaile">引用数趋势</span> <i
          onclick="MobileInfluence.showBigCiteTrend();" class="new-Influencefactor_body-item_title-icon"></i>
      </div>
      <div id="barmap" class="new-Influencefactor_body-item_infor"></div>
    </div>
  </div>
  <!-- 放大的访问趋势图    begin-->
  <div class="new-Influencefactor_body" id="big_visit_trend_img"></div>
  <i class="material-icons" style="position: absolute; left: 88%; bottom: 8%; font-size: 32px; display: none;"
    id="reduce_icon" onclick="MobileInfluence.reduceBigVisitTrend();">zoom_out</i>
  <!-- 放大的访问趋势图    end-->
  <!-- 放大的引用趋势图    start-->
  <div id="dev_cite_trend_big" class="new-Influencefactor_body"></div>
  <i onclick="MobileInfluence.hideBigCiteTrend();" class="material-icons dev_cite_small_icons"
    style="position: absolute; left: 88%; bottom: 8%; font-size: 32px; display: none;">zoom_out</i>
  <!-- 放大的引用趋势图    end-->
</body>
</html>