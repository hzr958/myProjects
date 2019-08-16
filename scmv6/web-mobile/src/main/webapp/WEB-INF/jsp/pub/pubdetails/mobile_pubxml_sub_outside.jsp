<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/plugin/autosize.min.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/smate.custom.valuechange.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript">
$(function(){
    $.post(
        "/psnweb/outside/ajaxsignature",
        {"currentUrl": encodeURIComponent(window.location.href)},
        function(data){
            if(data.result == "success"){
                smatewechat.customWeiXinShare(
                        data.appId,
                        data.timeStamp, 
                        data.nonceStr,
                        data.signature,
                        $("#pubTitleShare").text(),
                        data.domain + "/pub/outside/details/snsnonext?des3PubId=${pubDetailVO.des3PubId}",
                        data.domain + "/resmod/mobile/images/scm_icon_share.png",
                        $("#pubAbs").text());
            }
        },"json");
    mobile.pub.ajaxLoadComments(1,10,1);
    autosize(document.querySelectorAll('textarea'));
    if($("#hasAwarded").val()==1){
        $("#pubAwardCount").prev(".material-icons").css({"background":"#2196f3","color":"#ffffff"});
    }
    //findDes3FullTextId();
    if($("#isOwner").val() != "true"){
        //添加访问记录
        SmateCommon.addVisitRecord($("#des3OwnerId").val(),$("#des3PubId").val(),1);
    }
    document.getElementsByClassName("paper_detial")[0].style.minHeight = window.innerHeight - 46 - 55 - 130 + "px";
    document.getElementById("paperbox").style.minHeight = window.innerHeight - 46 - 95 - 130 + "px";
});
    

function showReplyBox(){
    var text = $("#pubReplyContent").val().trim();
    if(text.length>0){
        $("#scm_send_comment_btn").removeClass("not_point");
    }else{
        $("#scm_send_comment_btn").addClass("not_point");
    }
    $("#pubReplyBox").slideToggle(function(){
        if($("#pubReplyBox").css("display")!="none" && $("#hasLogin").val() != 0){
            $("#pubReplyContent").focus();
        }
    });

}
function loadMoreComments(){
    var PagNo = $("#PagNo").val();
    mobile.pub.ajaxLoadComments(parseInt(PagNo) + 1,10,1)  ;
    $("#PagNo").val(parseInt(PagNo) + 1);
}

function showMobileTip(){
    if($("#hasLogin").val() == 0){
        Pubdetails.showMobileLoginTips($("#loginTargetUrl").val());
    }
}

function needLogin(){
    window.location.href= "/oauth/mobile/index?sys=wechat&service=" + encodeURIComponent(window.location.href);  
}

function commonResAward(obj){//赞
  needLogin();
}
function commonShowReplyBox(obj){//评论
  needLogin();
}
function commonShare(obj){//分享
  needLogin();
}
function commonCollectnew(obj){//收藏
  needLogin();
}

</script>
<input id="isOwner" name="isOwner" type="hidden" value="${pubDetailVO.isOwner }" />
<input id="des3OwnerId" name="des3OwnerId" type="hidden" value="<iris:des3 code='${pubDetailVO.pubOwnerPsnId}'/>" />
<input id="des3PubId" name="des3PubId" type="hidden" value="${pubDetailVO.des3PubId}" />
<c:choose>
  <c:when test="${empty pubDetailVO }">
    <div class="response_no-result">未找到对应成果详情</div>
  </c:when>
  <c:otherwise>
    <div>
    <div class="paper_detial" style=" margin-bottom: 12px;display: flex; flex-direction: column;justify-content: space-between; padding: 0px 8px;">
       <input id="pubId" name="pubId" type="hidden" value="${pubDetailVO.pubId}" /> <input id="articleType"
        name="articleType" type="hidden" value="<%-- ${pubDetailVO.articleType} --%>"> <input id="ownerId"
        name="ownerId" type="hidden" value="${pubDetailVO.pubOwnerPsnId}" /> <input id="awardCount_${pubId}"
        name="pubAwardCount" type="hidden" value="${pubDetailVO.awardCount}" /> <input id="hasAwarded"
        name="hasAwarded" type="hidden" value="${isAward}" /> <input id="des3FullTextId" name="des3FullTextId"
        type="hidden" value="${pubDetailVO.fullText.fileid}" /> <input id="file_permission" type="hidden"
        value="${pubDetailVO.fullText.permission}" /> <input id="fullTextDownloadUrl" type="hidden"
        value="${pubDetailVO.srcFulltextUrl}" /> <input id="pubFullTextDownloadUrl" type="hidden"
        value="${pubDetailVO.fullTextDownloadUrl }" />
       <div id="paperbox" style="display: flex; flex-direction: column;justify-content: space-between;"> 
       <dl>
        <h2>
          <c:if test="${!empty pubDetailVO.title}">
            <span id="pubTitleShare">${pubDetailVO.title}</span>
          </c:if>
        </h2>
        <dt>
          <c:if test="${!empty pubDetailVO.authorNames}">
                    ${pubDetailVO.authorNames}
                </c:if>
        </dt>
        <dd>
          <c:if test="${!empty pubDetailVO.briefDesc}">
                            ${pubDetailVO.briefDesc }
                        </c:if>
        </dd>
      </dl>
      <c:if test="${not empty pubDetailVO.summary}">
        <h3>摘要:</h3>
        <p>
          <c:if test="${not empty pubDetailVO.summary}">
            <span id="pubAbs">${pubDetailVO.summary }</span>
          </c:if>
        </p>
      </c:if>
      <c:if test="${not empty pubDetailVO.keywords}">
        <h3>关键词:</h3>
        <p style="word-wrap: break-word;">
          <c:if test="${not empty pubDetailVO.keywords}">${pubDetailVO.keywords}&nbsp;</c:if>
        </p>
      </c:if>
      <!-- DOI的显示要放到关键词的下面 -->
      <c:if test="${!empty pubDetailVO.doi}">
        <h3>
          DOI：
          <c:if test="${locale eq 'en_US' }">&nbsp;</c:if>
          <a href="#">${pubDetailVO.doi}</a>
        </h3>
      </c:if>
      <div class="project_infor-footer">
        <div id="animatePubId"></div>
        <c:if test="${not empty pubDetailVO.fullText.fileId || not empty pubDetailVO.fullText.des3fileId}">
          <c:if test="${pubDetailVO.fullText.permission == 0}">
            <!-- 全文下载 -->
            <a onclick="needLogin();" resId="${pubDetailVO.pubId}" des3Id="<iris:des3 code='${pubDetailVO.pubId}'/>"
              class="project_infor-footer__request" style="width: 375px;">下载全文</a>
          </c:if>
          <c:if test="${pubDetailVO.fullText.permission != 0}">
            <a  id="requestPubFullTextTab" onclick="needLogin();" class="project_infor-footer__request">请求全文</a>
          </c:if>
        </c:if>
        <c:if test="${empty pubDetailVO.fullText.fileId && empty pubDetailVO.fullText.des3fileId}">
          <a  id="requestPubFullTextTab" onclick="needLogin();" class="project_infor-footer__request">请求全文</a>
        </c:if>
        <a onclick="needLogin();" class="project_infor-footer__mine">这是我的成果</a>
      </div>
      </div>

 <%--社交操作start --%>
        <c:set var="isAward" value="${pubDetailVO.isAward}"></c:set>
        <c:set var="resDes3Id" value="${pubDetailVO.des3PubId}"></c:set>
        <c:set var="awardCount" value="${pubDetailVO.awardCount}"></c:set>
        <c:set var="commentCount" value="${pubDetailVO.commentCount}"></c:set>
        <c:set var="shareCount" value="${pubDetailVO.shareCount}"></c:set>
        <c:set var="isCollection" value="${pubDetailVO.isCollection}"></c:set>
        <%@ include file="/common/standard_function_bar.jsp" %>
 <%--社交操作 end--%>      
     
<%--       <div class="detail-pub__action" style="display: flex; flex-wrap: nowrap; justify-content: space-around; margin-bottom: 12px;">
        <div class="button__box button__model_rect" style="display: flex; align-items: center; justify-content: center;"
          isAward="${pubDetailVO.isAward}" onclick="needLogin();">
          <a>
            <div class="button__main button__style_flat button__size_dense button__color-plain  ripple-effect">
              <div class="paper_footer-tool paper_footer-fabulous">
                <div class="css-spirit__icon spirit-icon__thumbup-outline-flag dev_pubdetails_award_style"></div>
              </div>
              <span style="margin-top: 5px;" class="dev_pub_award"><spring:message code="pub.details.opt.award" />
                <c:if test="${pubDetailVO.awardCount > 0}">(${pubDetailVO.awardCount})</c:if></span>
            </div>
          </a>
        </div>
        <div class="button__box button__model_rect" style="display: flex; align-items: center; justify-content: center;"
          onclick="needLogin();">
          <a>
            <div
              class="button__main button__style_flat button__size_dense button__color-plain color-display_blue ripple-effect">
              <div class="button__inline-icon css-spirit__main">
                <div class="css-spirit__icon spirit-icon__comment-flag"></div>
              </div>
              <span id="comentCountSpan" class="func_btn"><spring:message code="pub.details.opt.coment" /> <c:if
                  test="${pubDetailVO.commentCount>0}">(${pubDetailVO.commentCount})</c:if></span>
            </div>
          </a>
        </div>
        <div class="button__box button__model_rect" style="display: flex; align-items: center; justify-content: center;"
          onclick="needLogin();" resId="<iris:des3 code='${pubDetailVO.pubId}'/>" resType="${articleType}"
          pubId="${pubDetailVO.pubId}">
          <a>
            <div
              class="button__main button__style_flat button__size_dense button__color-plain color-display_blue ripple-effect">
              <div class="button__inline-icon css-spirit__main">
                <div class="css-spirit__icon spirit-icon__share-flag"></div>
              </div>
              <span class="dev_pub_share_${pubDetailVO.pubId} func_btn"><spring:message code="pub.details.opt.share" /> 
                 <span class="shareCountText" des3PubId="<iris:des3 code='${pubDetailVO.pubId}'/>">              
                  <c:if test="${pubDetailVO.shareCount > 0}">(${pubDetailVO.shareCount})</c:if>
                  </span>
             </span>
            </div>
          </a>
        </div>
      </div> --%>
      
      </div>
    </div>
    <div class="background__line" style="width: 100%; height: 12px; background: #eee;"></div>
    <h2 class="wdful_comments wdful__comments-title">
      精彩评论
    </h2>
    <div id="replyDiv" style="background: #fff; padding: 1px 8px;">
      <jsp:include page="/WEB-INF/jsp/pub/pubdetails/mobile_pub_reply.jsp"></jsp:include>
    </div>
   <div id="showMore"   style="width: 100%; height: 30px;padding:10px; background: #eee;display: none;text-align: center">
          <a href="javascript:;"  onclick="loadMoreComments()" style="text-align: center">查看更多评论</a>
    </div>
  </c:otherwise>
</c:choose>
<div style="margin: 20px; position: flex">
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
