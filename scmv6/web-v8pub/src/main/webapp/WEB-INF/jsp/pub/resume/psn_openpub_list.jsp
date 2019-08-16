<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="searchKey" value="${searchKey }" />
<div class="js_listinfo" smate_count='${page.totalCount }'></div>
<c:if test="${!empty pubInfoList}">
  <c:forEach items="${pubInfoList }" var="openPub" varStatus="status">
    <c:if test="${openPub.hasPublished == 0 || openPub.isXmlExists != 1}">
      <div class="main-list__item" id="unadded_${openPub.pubId }"
        title="${openPub.hasPublished == 0 ? '不能添加投稿阶段的论文' : '' }">
    </c:if>
    <c:if test="${openPub.hasPublished == 1 && openPub.isXmlExists == 1}">
      <div class="main-list__item" id="unadded_${openPub.pubId }"
        onclick="PsnResume.addToRepresentPub('${openPub.pubId }', ${moduleId });">
    </c:if>
    <div class="main-list__item_content">
      <div class="pub-idx_x-small">
        <div class="pub-idx__base-info">
          <div class="pub-idx__full-text_box">
            <div class="pub-idx__full-text_img">
              <img src="">
            </div>
          </div>
          <div class="pub-idx__main_box">
            <div class="pub-idx__main">
              <div class="${openPub.hasPublished == 1 ? 'pub-idx__main_title' : 'pub-idx__main_author' }"
                id="pubTitle_${openPub.pubId }">${openPub.title}</div>
              <div class="pub-idx__main_author" id="pubAuthorNames_${openPub.pubId }">${openPub.authorNames }</div>
              <div class="pub-idx__main_src"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <c:if test='${openPub.hasPublished == 1 && openPub.isXmlExists == 1}'>
      <div class="main-list__item_actions publishStatus" id="checkPubDiv_${openPub.pubId }">
        <i checkPubId="${openPub.pubId }" class="material-icons pubCheckStatus">add</i>
      </div>
    </c:if>
    </div>
  </c:forEach>
</c:if>