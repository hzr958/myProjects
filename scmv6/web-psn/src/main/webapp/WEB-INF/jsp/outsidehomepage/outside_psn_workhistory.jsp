<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container__card" id="workHistoryItems">
  <c:if test="${!empty workList }">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name="workHistoryItems.header_title" />
        </div>
      </div>
      <div class="main-list__list">
        <c:forEach items="${workList }" var="workHistory" varStatus="status">
          <input type="hidden" name="${workHistory.workId }_isPrimary" class="work_history_isprimary"
            value="${workHistory.isPrimary }" id="${workHistory.workId }_isPrimary" />
          <div class="main-list__item">
            <div class="main-list__item_content">
              <div class="exp-idx_medium">
                <div class="exp-idx__base-info">
                  <div class="exp-idx__logo_box">
                    <div class="exp-idx__logo_img">
                      <c:if test="${!empty workHistory.insImgPath }">
                        <img src="${workHistory.insImgPath }"
                          onerror="this.src='${resmod }/smate-pc/img/logo_instdefault.png'">
                      </c:if>
                      <c:if test="${empty workHistory.insImgPath }">
                        <img src="${resmod }/smate-pc/img/logo_instdefault.png">
                      </c:if>
                    </div>
                  </div>
                  <div class="exp-idx__main_box">
                    <div class="exp-idx__main">
                      <div class="exp-idx__main_inst">${workHistory.insName }</div>
                      <div class="exp-idx__main_dept">${workHistory.workDesc }</div>
                      <div class="exp-idx__main_intro">${workHistory.description }</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>
  </c:if>
</div>
</div>
</div>