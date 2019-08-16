<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<script type="text/javascript" src="${resmod}/js/plugin/autosize.min.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/smate.custom.valuechange.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/msgbox/mobile.msg.js"></script>
<script type="text/javascript">
    $(function(){ 
        document.getElementsByClassName("paper_detial")[0].style.minHeight = window.innerHeight - 46 - 55 - 130 + "px";
        document.getElementById("paperbox").style.minHeight = window.innerHeight - 46 - 95 - 130 + "px";
    });
    
    function commonResAward(obj){
      mobile.pub.pdwhAwardOpt(obj);
    }
    function commonShowReplyBox(obj){
      showReplyBox();
    }
    function commonShare(obj){
      sharePdwhPub();
    }
    function commonCollectnew(obj){
      mobile.pub.pdwhIsExist($("#des3PubId").val(), function() {
        mobile.snspub.collectnew(obj);
      })
    }

    function callBackAward(obj,awardCount){
      if(awardCount>999){
        awardCount = "1k+";
      }
      $(obj).find(".new-Standard_Function-bar_item").toggleClass("new-Standard_Function-bar_selected");
      var isAward = $(obj).attr("isAward");
      if (isAward == 1) {// 取消赞
        $(obj).attr("isAward", 0);           
        if (awardCount == 0) {
          $(obj).find('span').text("赞");
        } else {
          $(obj).find('span').text("赞" + "(" + awardCount + ")");
        }
      } else {// 赞
        $(obj).attr("isAward", 1);
        $(obj).find('span').text("取消赞" + "(" + awardCount + ")");
      }
    }

    function callBackCollect(obj){
      $(obj).toggleClass("new-Standard_Function-bar_selected");
      var isCollect = $(obj).attr("collect");
      if(isCollect==0){
        $(obj).find("span").text("取消收藏");
        $(obj).attr("collect",1);
      }else{
        $(obj).find("span").text("收藏");
        $(obj).attr("collect",0);    
      }
    }
</script>
<div class="paper_detial"  style="display: flex; flex-direction: column;justify-content: space-between;">
 <div id="paperbox" style="display: flex; flex-direction: column;justify-content: space-between;"> 
 <div>
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
        DOI：<a href="http://dx.doi.org/${pubDetailVO.doi}" target="_blank">${pubDetailVO.doi}</a>
      </h3>
    </c:if>
    <div id="animatePdwh"></div>
   </div>
    <div class="project_infor-footer">
      <c:if test='${!empty pubDetailVO.fullText}'>
        <a onclick="Msg.downloadFTFile('${pubDetailVO.fullTextDownloadUrl }')" id="fulltextDownload"
          class="project_infor-footer__request">下载全文</a>
      </c:if>
      <c:if test="${empty pubDetailVO.fullText}">
        <a id="fulltextRequest" onclick="mobile.pub.requestPubFullText('${pubDetailVO.des3PubId}', 'pdwh', '')"
          class="project_infor-footer__request">请求全文</a>
      </c:if>
      <a onclick="isMyMyPub('${pubDetailVO.pubId}')" class="project_infor-footer__mine">这是我的成果</a>
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
</h2>
<div id="replyDiv" style="background: #fff; padding: 1px 8px;"></div>
<div id="showMore"   style="width: 100%; height: 30px;padding:10px; background: #eee;display: none;text-align: center">
          <a href="javascript:;"  onclick="loadMoreComments()" style="text-align: center">查看更多评论</a>
    </div>