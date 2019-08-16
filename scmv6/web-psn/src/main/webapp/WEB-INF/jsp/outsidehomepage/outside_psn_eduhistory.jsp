<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container__card" id="eduHistoryItems">
  <c:if test="${!empty eduList }">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name="eduHistoryItems.header_title" />
        </div>
      </div>
      <div class="main-list__list">
        <c:forEach items="${eduList }" var="eduHistory" varStatus="status">
          <div class="main-list__item">
            <div class="main-list__item_content">
              <div class="exp-idx_medium">
                <div class="exp-idx__base-info">
                  <div class="exp-idx__logo_box">
                    <div class="exp-idx__logo_img">
                      <c:if test="${!empty eduHistory.insImgPath }">
                        <img src="${eduHistory.insImgPath }"
                          onerror="this.src='${resmod }/smate-pc/img/logo_instdefault.png'">
                      </c:if>
                      <c:if test="${empty workHistory.insImgPath }">
                        <img src="${resmod }/smate-pc/img/logo_instdefault.png">
                      </c:if>
                    </div>
                  </div>
                  <div class="exp-idx__main_box">
                    <div class="exp-idx__main">
                      <div class="exp-idx__main_inst">${eduHistory.insName }</div>
                      <div class="exp-idx__main_dept">${eduHistory.eduDesc }</div>
                      <div class="exp-idx__main_intro">${eduHistory.description }</div>
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