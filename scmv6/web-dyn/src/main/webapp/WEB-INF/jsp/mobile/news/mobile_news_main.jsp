<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title>科研之友</title>
  <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
  <meta charset="utf-8">
  <link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mobile.css">
  <script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<%--   <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css"> --%>
  <script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
  <script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
  <script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
  <link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
  <script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
  <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
  <script type="text/javascript" src="${resmod}/js_v8/news/news_zh_CN.js"></script>
  <script type="text/javascript" src="${resmod}/js_v8/news/news.base.js"></script>
  <script type="text/javascript" src="${resmod}/mobile/js/news/mobile.news.js"></script>
  <script type='text/javascript' src='${resmod}/js/common/smate.common.js'></script>
  <script type="text/javascript">
      function goHistory(){
          history.back();
      }
      var data = {
          orderBy:"update"
      };
      var newsList ;
          $(function(){
              var listdata = {
                  orderBy:"update"
              };
               newsList =  window.Mainlist({
                      name : "mobileNewsList",
                      listurl : "/dynweb/news/mobile/ajaxnewslist",
                      listdata : data,
                      method : "scroll",
                      listcallback : function(xhr) {

                      }
                  });
              //document.getElementsByClassName("new-mobilenews_body")[0].style.height = window.innerHeight - 96 + "px";
              //sendRequestForList();
          });
          function findNewsList(order,obj) {
              if(order == "update"){
                  $("#updateDiv").addClass("new-mobilegroup_neck-list_selected");
                  $("#heatDiv").removeClass("new-mobilegroup_neck-list_selected");
              }else{
                  $("#heatDiv").addClass("new-mobilegroup_neck-list_selected");
                  $("#updateDiv").removeClass("new-mobilegroup_neck-list_selected");
              }
              data.orderBy = order;
              newsList.listdata = data ;
              newsList.sendRequestForList()
          }
  </script>
</head>
<body>
<div style="width: 100%;min-height: 100%; overflow-x: hidden;">
<div  class="paper__func-header">
  <i class="material-icons" onclick="goHistory();">keyboard_arrow_left</i>
  <span>新闻</span>
  <i class="material-icons"></i>
</div>
<div class="new-mobilegroup_neck" style="top: 48px;">
  <div class="new-mobilegroup_neck-list new-mobilegroup_neck-list_selected" id="updateDiv"><span onclick="findNewsList('update');">时间</span></div>
  <div class="new-mobilegroup_neck-list" onclick="findNewsList('heat');" id="heatDiv"><span>热度</span></div>
</div>
  <div class=" new-mobilenews_body-item main-list" style="min-height: 100%;">
    <div class="main-list__list" list-main="mobileNewsList" style="overflow: hidden; margin: 85px 0px 40px 0px;"></div>
  </div>

</div>
</body>
</html>
