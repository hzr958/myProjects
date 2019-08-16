<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<c:forEach items="${page.result}" var="ft" varStatus="stat">
  <div class="main-list__item" id="dev_${ft.id}">
    <div class="main-list__item_content">
      <div class="pub-idx_small">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img" style="position: relative; width: 72px; height: 92px;">
              <c:if test="${not empty ft.downloadUrl}">
                <a href="${ft.downloadUrl }"> <img src="${ft.fullTextImagePath}" style="cursor: pointer;"
                  onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'" title="">
                </a>
                <a href="${ft.downloadUrl }" class="new-tip_container-content"
                  title='<spring:message code="pub.download"/>'> <img src="/resmod/smate-pc/img/file_ upload1.png"
                  class="new-tip_container-content_tip1"> <img src="/resmod/smate-pc/img/file_ upload.png"
                  class="new-tip_container-content_tip2">
                </a>
              </c:if>
              <c:if test="${empty ft.downloadUrl}">
                <img src="${resmod}/images_v5/images2016/file_img.jpg"
                  onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'">
              </c:if>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main" style="height: 92px; justify-content: space-between;">
              <div class="pub-idx__main_title">
                <a onclick="Pub.newPubDetail('<iris:des3 code='${ft.pub.pubId}'/>');"> ${ft.pub.title} </a>
              </div>
              <div class="pub-idx__main_author">${ft.pub.authorNames}</div>
              <div class="pub-idx__main_src">${ft.pub.briefDesc}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_grey" onclick="Rm.doRejectPubft(this,'${ft.id}');">
        <spring:message code="pub.ignore.opt" />
      </button>
      <button class="button_main button_dense button_primary-changestyle" onclick="Rm.doConfirmPubft(this,'${ft.id}');">
        <spring:message code="pub.confirm.opt" />
      </button>
    </div>
  </div>
</c:forEach>