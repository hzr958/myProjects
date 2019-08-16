<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount }'></div>
<c:if test="${not empty page.result}">
  <c:forEach items="${page.result}" var="prj">
    <div class="main-list__item" id="unadded_${prj.id }" onclick="PsnResume.addToRepresentPrj('${prj.id }');">
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
                <div class="pub-idx__main_title" id="title_${prj.id }">${prj.prjInfo.showTitle}</div>
                <div class="pub-idx__main_author" id="authorNames_${prj.id }">${prj.prjInfo.showAuthorNames }</div>
                <div class="pub-idx__main_src"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="main-list__item_actions " id="checkDiv_${prj.id }">
        <i checkPrjId="${prj.id }" class="material-icons prjCheckStatus">add</i>
      </div>
    </div>
  </c:forEach>
</c:if>