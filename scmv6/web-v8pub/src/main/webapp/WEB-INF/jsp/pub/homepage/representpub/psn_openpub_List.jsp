<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="searchKey" value="${pubListVO.searchKey }" />
<div class="js_listinfo" smate_count='${pubListVO.totalCount }'></div>
<c:if test="${!empty pubListVO.resultList}">
  <c:forEach items="${pubListVO.resultList }" var="openPub" varStatus="status">
    <div class="main-list__item" des3PubId="${openPub.des3PubId }">
      <div class="main-list__item_content">
        <div class="pub-idx_x-small">
          <div class="pub-idx__base-info">
            <div class="pub-idx__full-text_box">
              <div class="pub-idx__full-text_img">
                <img src="">
              </div>
            </div>
            <div class="pub-idx__main_box">
              <div class="pub-idx__main" onclick="addToRepresentPub(this);" style="cursor: pointer;">
                <div class="pub-idx__main_title pubTitle">${openPub.title}</div>
                <div class="pub-idx__main_author pubAuthorNames">${openPub.authorNames }</div>
                <div class="pub-idx__main_author pub_BriefDesc">${openPub.briefDesc }</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <c:if test='${openPub.isRepresentPub == 1 }'>
        <div class="main-list__item_actions checkPubDiv">
          <i style="color: forestgreen" class="material-icons pubCheckStatus">check</i>
        </div>
      </c:if>
      <c:if test='${openPub.isRepresentPub != 1 }'>
        <div class="main-list__item_actions checkPubDiv" onclick="addToRepresentPub(this);">
          <i class="material-icons pubCheckStatus">add</i>
        </div>
      </c:if>
    </div>
  </c:forEach>
</c:if>
