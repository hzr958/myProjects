<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" id="hasPubfulltextConfirm" value="${pubListVO.hasPubfulltextConfirm}">
<div class="js_listinfo" smate_count="${pubListVO.totalCount}"></div>
<c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
  <div class="main-list__item" id="dev_${pub.pubId}">
    <div class="main-list__item_content">
      <div class="pub-idx_small">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img" style="position: relative;width: 72px; height: 92px;">
              <c:if test="${not empty pub.fullTextFieId }">
                <a href="${pub.fullTextDownloadUrl }"> <img src="${pub.fullTextImgUrl}"
                  onerror="this.src='/resmod/images_v5/images2016/file_img1.jpg'"></a>
                <a href="${pub.fullTextDownloadUrl }" class="new-tip_container-content"
                   title="<spring:message code="pub.download"/>"> <img src="/resmod/smate-pc/img/file_ upload1.png"
                    class="new-tip_container-content_tip1"> <img src="/resmod/smate-pc/img/file_ upload.png"
                    class="new-tip_container-content_tip2">
                </a>
              </c:if>
              <c:if test="${empty pub.fullTextFieId }">
                <img src="${pub.fullTextImgUrl}" onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
              </c:if>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main" style="height: 92px; justify-content: space-between;">
              <div class="pub-idx__main_title">
                <a href="${pub.pubIndexUrl}" target="_blank" title='<c:out value="${pub.title}" escapeXml="false"/>'><c:out
                    value="${pub.title}" escapeXml="false" /></a>
              </div>
              <div class="pub-idx__main_author">
                <c:out value="${pub.authorNames}" escapeXml="false" />
              </div>
              <div class="pub-idx__main_src">
                <c:out value="${pub.briefDesc}" escapeXml="false" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_grey" onclick="Rm.ignoreConfirmPub(this,'${pub.pubId}');">
        <spring:message code="pub.ignore.opt" />
      </button>
      <button class="button_main button_dense button_primary-changestyle"
        onclick="Rm.affirmConfirmPub(this,'${pub.pubId}')">
        <spring:message code="pub.confirm.opt" />
      </button>
    </div>
  </div>
</c:forEach>
