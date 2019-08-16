<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var domainrol = "${domainrol}";
var randomModule = "${randomModule}";
</script>
<style type="text/css"> 
.line_overflow_hide{
    line-height:1rem;
    height:2.1rem;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
</style> 
<c:if test='${page.result.size() ge 1 }'>
  <input id="dev_homefulltext_isall" type="hidden" value="one" />
  <input id="dev_home_confirmCount" type="hidden" value="${confirmCount}" />
  <input type="hidden" class="dev_home_opt_fulltext_count" value="0" />
  <div class="dev_Achieve-Claim">
    <div class="dev_Achieve-Claim_title">
      <h1>
        <spring:message code='pub.fulltext.confirm' />
      </h1>
      <a href="javascript:;" onclick="Rm.pubftConfirmAll();" class="Claim_title-check">
        <button class="button_main button_link">
          <spring:message code='pub.home.more' />
        </button>
      </a>
    </div>
    <c:forEach items="${page.result}" var="ft" varStatus="stat">
      <div class="dev_Achieve-Claim_content">
        <div class="dev_Achieve-Claim_content-infor">
          <div class="dev_Achieve-Claim_content-infor_avatar pub-idx__full-text_img"
            title='<spring:message code="pub.download"/>' style="position: relative;">
            <c:if test="${!empty ft.downloadUrl }">
              <a href="${ft.downloadUrl }"> <img src="${ft.fullTextImagePath}" style="border: 1px solid #eeeeee;"
                onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
              </a>
              <a href="${ft.downloadUrl }" class="new-tip_container-content" style="margin-top: 9px;"> <img
                src="/resmod/smate-pc/img/file_ upload1.png" class="new-tip_container-content_tip1"> <img
                src="/resmod/smate-pc/img/file_ upload.png" class="new-tip_container-content_tip2">
              </a>
            </c:if>
            <%-- <c:if test="${!empty ft.downloadUrl }"> <a href="${ft.downloadUrl }"></c:if>
        <img src="${ft.fullTextImagePath}" style="border:1px solid #eeeeee;" onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
        <c:if test="${!empty ft.downloadUrl }"> </a></c:if> --%>
          </div>
          <div class="multipleline-ellipsis full-request__content-title"
            style="overflow: hidden; height: 40px; max-height: 40px;">
            <a onclick="Pub.newPubDetail('<iris:des3 code='${ft.pub.pubId}'/>');">
              <div class="multipleline-ellipsis__content-box" title="${ft.fulltextTitle}">${ft.fulltextTitle}</div>
            </a>
          </div>
        </div>
        <div class="dev_Achieve-Claim_content-select">
          <a onclick="Rm.doRejectPubft(this,'${ft.id}');" class="content-select-title content-select-style"><spring:message
              code='pub.home.ignore' /></a> <a onclick="Rm.doConfirmPubft(this,'${ft.id}');"
            class="content-select-title content-selected"><spring:message code='pub.home.confirm' /></a>
        </div>
      </div>
    </c:forEach>
  </div>
</c:if>
