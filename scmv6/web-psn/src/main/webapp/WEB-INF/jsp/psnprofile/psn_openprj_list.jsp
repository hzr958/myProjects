<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount }'></div>
<c:if test="${!empty openPrjList}">
  <c:forEach items="${openPrjList }" var="openPrj" varStatus="status">
    <div class="main-list__item" id="unadded_${openPrj.id }">
      <div class="main-list__item_content">
        <div class="pub-idx_x-small">
          <div class="pub-idx__base-info">
            <div class="pub-idx__full-text_box">
              <div class="pub-idx__full-text_img">
                <img src="">
              </div>
            </div>
            <div class="pub-idx__main_box">
              <div class="pub-idx__main" onclick="addToRepresentPrj('${openPrj.id }');" style="cursor: pointer;">
                <div class="pub-idx__main_title" id="title_${openPrj.id }">${openPrj.prjInfo.showTitle}</div>
                <div class="pub-idx__main_author" id="authorNames_${openPrj.id }">${openPrj.prjInfo.showAuthorNames }</div>
                <div class="pub-idx__main_src"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <c:if test='${openPrj.isRepresentPrj == 1 }'>
        <div class="main-list__item_actions " id="checkDiv_${openPrj.id }">
          <i checkPrjId="${openPrj.id }" style="color: forestgreen" class="material-icons prjCheckStatus">check</i>
        </div>
      </c:if>
      <c:if test='${openPrj.isRepresentPrj != 1 }'>
        <div class="main-list__item_actions " id="checkDiv_${openPrj.id }"
          onclick="addToRepresentPrj('${openPrj.id }');">
          <i checkPrjId="${openPrj.id }" class="material-icons prjCheckStatus">add</i>
        </div>
      </c:if>
    </div>
  </c:forEach>
</c:if>