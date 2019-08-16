<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mobile.css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/news/news_zh_CN.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/news/news.base.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/news/mobile.news.js"></script>
<script type='text/javascript' src='${resmod}/js/common/smate.common.js'></script>
<script>
        function goHistory(origin){
            if(origin != undefined && origin == "outsideShare"){
                SmateCommon.goBack('/dynweb/mobile/dynshow');
            }else{
                history.back();
            }
        }
       window.onload =function(){
           document.getElementsByClassName("new-mobilenews_showbody")[0].style.height = window.innerHeight - 96 + "px"; 
       } 
     function doLogin(){
         window.location.href="/oauth/mobile/index?sys=wechat&service=" + encodeURIComponent(window.location.href);
     }
     function newsAward(obj){//赞
       doLogin();
     }
    </script>
</head>
<body>
  <div class="paper__func-header">
    <i class="material-icons" onclick="goHistory('${origin}');">keyboard_arrow_left</i> <span>新闻详情</span> <i
      class="material-icons"></i>
  </div>
  <div class="new-mobilenews_showbody">
    <div class="new-mobilenews_showbody-title">${newsShowInfo.title }</div>
    <div class="new-mobilenews_showbody-content">${newsShowInfo.brief }</div>
    <div class="new-mobilenews_showbody-content">${newsShowInfo.content }</div>
    <div class="new-Standard_Function-bar" style="margin-top: 12px;">
      <a class="manage-one"> 
      <c:if test="${isLogin }">
      <c:if test="${newsShowInfo.isAward == 1}">
          <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;"
            isAward="1" onclick="NewsBase.newsAward('${newsShowInfo.des3NewsId}',this);">
            <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
              class="new-Standard_Function-bar_item-title" style="color: #666 !important;">取消赞 <c:if
                test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount>=1000}">
                             (1K+)
                          </c:if> <c:if test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount<1000}">
                              (${newsShowInfo.awardCount})
                          </c:if>
            </span>
          </div>
        </c:if> <c:if test="${newsShowInfo.isAward == 0}">
          <div class="new-Standard_Function-bar_item" style="margin-left: 0px;" isAward="0"
            onclick="NewsBase.newsAward('${newsShowInfo.des3NewsId}',this);">
            <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
              class="new-Standard_Function-bar_item-title" style="color: #666 !important;">赞 <c:if
                test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount>=1000}">
                             (1K+)
                          </c:if> <c:if test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount<1000}">
                              (${newsShowInfo.awardCount})
                          </c:if>
            </span>
          </div>
        </c:if>
      </c:if>
      <c:if test="${!isLogin }">
         <div class="new-Standard_Function-bar_item" style="margin-left: 0px;" isAward="0" onclick="newsAward(this);">
            <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span class="new-Standard_Function-bar_item-title"  style="color: #666 !important;">赞 
            <c:if test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount>=1000}">
               (1K+)
            </c:if>
            <c:if test="${newsShowInfo.awardCount > 0&&newsShowInfo.awardCount<1000}">
                (${newsShowInfo.awardCount})
            </c:if>
          </span>
        </div>
    </c:if>
      </a> <a class="manage-one" style="cursor: default;">
        <div class="new-Standard_Function-bar_item none-selected">
          <i class="new-Standard_Share-checkinfor"></i> <span class="new-Standard_Function-bar_item-title">查看 <c:if
              test="${newsShowInfo.viewCount > 0&&newsShowInfo.viewCount>=1000}">
                               (1K+)
                            </c:if> <c:if test="${newsShowInfo.viewCount > 0&&newsShowInfo.viewCount<1000}">
                                (${newsShowInfo.viewCount})
                            </c:if>
          </span>
        </div>
      </a> <a class="manage-one dev_pub_share">
        <div class="new-Standard_Function-bar_item" style="width: 100%;" resid="${newsShowInfo.des3NewsId}"
          onclick="MobileNews.shareNews(this)">
          <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
            class="new-Standard_Function-bar_item-title span_share">分享 <c:if
              test="${newsShowInfo.shareCount > 0&&newsShowInfo.shareCount>=1000}">
                           (1K+)
                        </c:if> <c:if test="${newsShowInfo.shareCount > 0&&newsShowInfo.shareCount<1000}">
                            (${newsShowInfo.shareCount})
                        </c:if>
          </span>
        </div>
      </a>
    </div>
  </div>
</body>
</html>
