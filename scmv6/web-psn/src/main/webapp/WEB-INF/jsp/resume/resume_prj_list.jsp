<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" id="resumePrjIds" value="${addedPrjIds}" />
<c:if test="${not empty prjList}">
  <c:forEach items="${prjList}" var="prj" varStatus="status">
    <div class="main-list__item" id="addedPrj_${prj.id}" onclick="PsnResume.delRepresentPrj('${prj.id}');">
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
                <div class="pub-idx__main_title" id="selected_prj_title_${prj.id}">${prj.prjInfo.showTitle}</div>
                <div class="pub-idx__main_author" id="selected_prj_author_${prj.id}">${prj.prjInfo.showAuthorNames }</div>
                <div class="pub-idx__main_src"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="main-list__item_actions">
        <i class="list-results_close"></i>
      </div>
    </div>
  </c:forEach>
</c:if>
<div class="main-list__item" id="addedPrjItem" style="display: none;" onclickmethod>
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
            <div class="pub-idx__main_title" id="selected_prj_title_$prjId$">replacetitle</div>
            <div class="pub-idx__main_author" id="selected_prj_author_$prjId$">replaceAuthorName</div>
            <div class="pub-idx__main_src"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="main-list__item_actions">
    <i class="list-results_close"></i>
  </div>
</div>