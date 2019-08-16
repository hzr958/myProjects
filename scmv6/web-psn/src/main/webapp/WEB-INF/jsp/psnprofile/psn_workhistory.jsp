<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	addFormElementsEvents();
})	
</script>
<c:if test="${not empty workList || isMySelf== 'true'}">
  <div class="container__card" id="workHistoryItems">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name='homepage.profile.title.work' />
        </div>
        <button class="button_main button_icon button_light-grey operationBtn" onclick="addWorkHistory();">
          <i class="material-icons">add</i>
        </button>
      </div>
      <div class="main-list__list">
        <c:if test="${!empty workList }">
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
                            onerror="this.onerror=null;this.src='${resmod }/smate-pc/img/logo_instdefault.png'">
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
                        <div class="exp-idx__main_intro multipleline-ellipsis">
                          <div class="multipleline-ellipsis__content-box">${workHistory.description }</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="main-list__item_actions">
                <button class="button_main button_icon button_light-grey operationBtn"
                  onclick="editWorkHistory('<iris:des3 code="${workHistory.workId}"></iris:des3>');">
                  <i class="material-icons">edit</i>
                </button>
              </div>
            </div>
          </c:forEach>
        </c:if>
      </div>
    </div>
  </div>
</c:if>
