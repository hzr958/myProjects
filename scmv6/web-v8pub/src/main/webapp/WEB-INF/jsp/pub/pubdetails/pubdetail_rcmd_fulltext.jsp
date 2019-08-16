<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
    var goheight = document.getElementsByClassName("bckground-cover")[0].offsetHeight;
    var gowidth = document.getElementsByClassName("bckground-cover")[0].offsetWidth;
    var setheight = document.getElementsByClassName("confirm-fulltext")[0].offsetHeight;
    var setwidth = document.getElementsByClassName("confirm-fulltext")[0].offsetWidth;
    window.onresize = function(){
        document.getElementsByClassName("confirm-fulltext")[0].style.bottom = (goheight - setheight)/2 + "px"; 
        document.getElementsByClassName("confirm-fulltext")[0].style.left = (gowidth - setwidth)/2 + "px";
    }
})
</script>
<div class="bckground-cover" id="rcmdFulltext">
  <div class="confirm-fulltext">
    <div class="confirm-fulltext_header">
      <span><spring:message code="pub.fulltext.confirm" /></span> <i
        class="material-icons confirm-fulltext_header-close" onclick="closeRcmdFulltext()">close</i>
    </div>
    <div class="confirm-fulltext_body">
      <div class="confirm-fulltext_body-title">
        <spring:message code="pub.fulltext.confirm.msg" />
        <a href="${pubRcmdft.downloadUrl }" class="confirm-fulltext_body-title_check" target="_blank"><spring:message
            code="pub.fulltext.confirm.look" /></a>
        <spring:message code="pub.fulltext.confirm.sure" />
      </div>&nbsp;
      <div style="color:red;float:right">(<span id="current">1</span>/<span id="total">${totalCount} </span>)</div>
      <div class="confirm-fulltext_body-content">
        <div class="confirm-fulltext_body-content_avator" style="position: relative;">
          <c:if test="${not empty pubRcmdft.downloadUrl}">
            <a href="${pubRcmdft.downloadUrl }"> <img src="${pubRcmdft.fullTextImagePath}"
              style="cursor: pointer; border: 1px solid #eee;"
              onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
            </a>
            <a href="${pubRcmdft.downloadUrl }" class="new-tip_container-content" title="下载全文"
              style="position: absolute; opacity: 0.3; width: 100%; height: 100%; justify-content: center; align-items: center; margin-top: -4px; top: 0px; left: 0px;">
              <img src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip2"
              style="transform: rotate(180deg); width: 32px; height: 32px;">
            </a>
          </c:if>
          <c:if test="${empty pubRcmdft.downloadUrl}">
            <img src="${resmod}/images_v5/images2016/file_img.jpg"
              onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
          </c:if>
        </div>
        <div class="confirm-fulltext_body-content_infor">
          <div class="confirm-fulltext_body-content_infor-box">
            <div class="confirm-fulltext_body-content_infor-title" title="${pubRcmdft.pub.title}">
              ${pubRcmdft.pub.title}</div>
          </div>
          <div class="confirm-fulltext_body-content_infor-btn">
            <div class="confirm-fulltext_body-content_infor-btn_confirm"
              onclick="Pubdetails.doConfirmPubft(${pubRcmdft.id});">
              <spring:message code="pub.fulltext.confirm.yes" />
            </div>
            <div class="confirm-fulltext_body-content_infor-btn_cancle"
              onclick="Pubdetails.doRejectPubft(${pubRcmdft.id});">
              <spring:message code="pub.fulltext.confirm.no" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
