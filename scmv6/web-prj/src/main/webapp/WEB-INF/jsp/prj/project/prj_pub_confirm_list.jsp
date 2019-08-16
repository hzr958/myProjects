<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${prjPubList.size()}"></div>
<c:forEach items="${prjPubList}" var="pub" varStatus="stat">
  <div class="main-list__item" id="dev_${pub.pubId}">
    <div class="main-list__item_content">
      <div class="pub-idx_small">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img" style="position: relative; width: 72px; height: 92px;">
              <c:if test="${not empty pub.fullTextFieId }">
                <a href="${pub.fullTextDownloadUrl }"> <img src="${pub.fullTextImgUrl}"
                  onerror="this.src='/resmod/images_v5/images2016/file_img1.jpg'"></a>
                <a href="${pub.fullTextDownloadUrl }" class="new-tip_container-content"
                  title="下载全文"> <img src="/resmod/smate-pc/img/file_ upload1.png"
                  class="new-tip_container-content_tip1"> <img src="/resmod/smate-pc/img/file_ upload.png"
                  class="new-tip_container-content_tip2">
                </a>
              </c:if>
              <c:if test="${empty pub.fullTextFieId }">
                <img src="${pub.fullTextImgUrl}" onerror="this.src='/resmod/images_v5/images2016/file_img.jpg'">
              </c:if>
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main" style="height: 90px; justify-content: space-around;">
              <div class="pub-idx__main_title">
                <a href="${pub.pubIndexUrl}" target="_blank" title='<c:out value="${pub.title}" escapeXml="false" />'><c:out
                    value="${pub.title}" escapeXml="false" /></a>
              </div>
              <div class="pub-idx__main_author" title="${pub.authorNames}">
                <c:out value="${pub.authorNames}" escapeXml="false" />
              </div>
              <div class="pub-idx__main_src" title="${pub.briefDesc}">
                <c:out value="${pub.briefDesc}" escapeXml="false" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_main_button_dense_ignore"
        onclick="NewProject.pubConfirmOpt(this,'${des3PrjId}','${pub.pubId}','2');">
                   不是 
      </button>
      <button class="button_main button_dense button_primary-changestyle"
        onclick="NewProject.pubConfirmOpt(this,'${des3PrjId}','${pub.pubId}','1')">
                    是此项目成果
      </button>
    </div>
  </div>
</c:forEach>
