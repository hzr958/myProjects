<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<script type="text/javascript" src="${resmod}/js/plugin/autosize.min.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript">
$(function(){
	//未登录，隐藏评论框
    $("#pubReplyBox").hide();  
	document.getElementsByClassName("paper_detial")[0].style.minHeight = window.innerHeight - 46 - 55 - 130 + "px";
	document.getElementById("paperbox").style.minHeight = window.innerHeight - 46 - 95 - 130  + "px";
});
function doLogin(){
	window.location.href="/oauth/mobile/index?sys=wechat&service=" + encodeURIComponent(window.location.href);
}
function commonResAward(obj){//赞
  doLogin();
}
function commonShowReplyBox(obj){//评论
  doLogin();
}
function commonShare(obj){//分享
  doLogin();
}
function commonCollectnew(obj){//收藏
  doLogin();
}
</script>
<div class="paper_detial" style="display: flex; flex-direction: column;justify-content: space-between;">
  <div id="paperbox" style="display: flex; flex-direction: column;justify-content: space-between;">
  <c:if test="${pubDetailVO.pubId==0 }">
    <dl>
      <dd>未找到对应的记录</dd>
    </dl>
  </c:if>
  <c:if test="${pubDetailVO.pubId > 0 }">
    <input id="des3PubId" name="des3PubId" type="hidden" value="${pubDetailVO.des3PubId}" />
    <input id="dbid" name="dbid" type="hidden" value="" />
    <input id="awardCount_${pubDetailVO.pubId}" name="pubAwardCount" type="hidden" value="${pubDetailVO.awardCount}" />
    <input id="hasAwarded" name="hasAwarded" type="hidden" value="${pubDetailVO.isAward }" />
    <input id="fullTextFileId" name="fullTextFileId" type="hidden" value="" />
    <dl>
      <h2>
        <span id="pubTitleShare">${pubDetailVO.title}</span>
      </h2>
      <dt>${pubDetailVO.authorNames}</dt>
      <dd>${!empty pubDetailVO.briefDesc ? pubDetailVO.briefDesc : "--"}</dd>
    </dl>
    <c:if test="${!empty pubDetailVO.summary}">
      <h3>摘要:</h3>
      <p>
        <span id="pubAbs">${pubDetailVO.summary }</span>
      </p>
    </c:if>
    <c:if test="${!empty pubDetailVO.keywords}">
      <h3>关键词:</h3>
      <p>${pubDetailVO.keywords }&nbsp;</p>
    </c:if>
    <!-- DOI的显示要放到关键词的下面 -->
    <c:if test="${!empty pubDetailVO.doi}">
      <h3>
        DOI：<a href="#">${pubDetailVO.doi}</a>
      </h3>
    </c:if>
    <div id="animatePdwh"></div>
    <div class="project_infor-footer">
      <c:if test='${!empty pubDetailVO.fullText}'>
        <a href="${pubDetailVO.fullTextDownloadUrl }" id="fulltextDownload" class="project_infor-footer__request">全文下载</a>
      </c:if>
      <c:if test="${empty pubDetailVO.fullText}">
        <a id="fulltextRequest" onclick="doLogin()" class="project_infor-footer__request">请求全文</a>
      </c:if>
      <a onclick="doLogin();" class="project_infor-footer__mine">这是我的成果</a>
    </div>
    </div>
     <%--社交操作start --%>
        <c:set var="isAward" value="${pubDetailVO.isAward}"></c:set>
        <c:set var="resDes3Id" value="${pubDetailVO.des3PubId}"></c:set>
        <c:set var="awardCount" value="${pubDetailVO.awardCount}"></c:set>
        <c:set var="commentCount" value="${pubDetailVO.commentCount}"></c:set>
        <c:set var="shareCount" value="${pubDetailVO.shareCount}"></c:set>
        <c:set var="isCollection" value="${pubDetailVO.isCollection}"></c:set>
        <c:set var="resType" value="22"></c:set>
        <c:set var="pubDb" value="PDWH"></c:set>
                 
        <%@ include file="/common/standard_function_bar.jsp" %>
    <%--社交操作 end--%>
  </c:if>
</div>
<div class="background__line" style="width: 100%; height: 12px; background: #eee;"></div>
<h2 class="wdful_comments wdful__comments-title">
  精彩评论
</h2
<div id="replyDiv" style="background: #fff; padding: 1px 8px;"></div>
<div id="showMore"   style="width: 100%; height: 30px;padding:10px; background: #eee;display: block;text-align: center">
          <a href="javascript:;"  onclick="loadMoreComments()" style="text-align: center">查看更多评论</a>
    </div>
<div style="margin-top: 5px; position: flex">
  <section
    style="text-align: center; padding: 24px; max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important; background-color: rgb(245, 244, 244);">
    <section
      style="max-width: 100%; display: inline-block; box-sizing: border-box !important; word-wrap: break-word !important;">
      <span style="max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;"> <strong
        style="max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;"> <span
          style="max-width: 100%; color: rgb(3, 3, 3); font-size: 20px; box-sizing: border-box !important; word-wrap: break-word !important;">科研之友</span>
      </strong> <strong style="max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;"> <span
          style="max-width: 100%; color: rgb(3, 3, 3); font-size: 20px; box-sizing: border-box !important; word-wrap: break-word !important;">&nbsp;</span>
      </strong><strong style="max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;"><span
          style="max-width: 100%; color: rgb(3, 3, 3); font-size: 20px; box-sizing: border-box !important; word-wrap: break-word !important;"></span></strong><span
        style="max-width: 100%; font-size: 18px; box-sizing: border-box !important; word-wrap: break-word !important;"><strong
          style="max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;"><span
            style="max-width: 100%; color: rgb(3, 3, 3); font-size: 20px; box-sizing: border-box !important; word-wrap: break-word !important;"></span></strong><span
          style="max-width: 100%; color: rgb(63, 63, 63); font-size: 20px; box-sizing: border-box !important; word-wrap: break-word !important;">|&nbsp;</span></span><span
        style="max-width: 100%; color: rgb(3, 3, 3); font-size: 14px; box-sizing: border-box !important; word-wrap: break-word !important;">分享与发现知识</span>
      </span><br style="max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;">
      <div style="text-align: center; margin-top: 8px;">
        <img src="/resmod/images/images2016/code.jpg" width="85" height="85">
      </div>
    </section>
    <section style="max-width: 100%; box-sizing: border-box !important; word-wrap: break-word !important;">
      <span
        style="max-width: 100%; color: rgb(12, 12, 12); font-size: 12px; box-sizing: border-box !important; word-wrap: break-word !important;">长按，识别二维码，加关注</span>
    </section>
  </section>
</div>
