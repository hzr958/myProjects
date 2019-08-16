<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	addFormElementsEvents();
})	
</script>
<c:if test="${not empty eduList || isMySelf== 'true'}">
  <div class="container__card" id="eduHistoryItems">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name='homepage.profile.title.edu' />
        </div>
        <button class="button_main button_icon button_light-grey operationBtn" onclick="addEduHistory();">
          <i class="material-icons">add</i>
        </button>
      </div>
      <div class="main-list__list">
        <c:if test="${!empty eduList }">
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
                        <div class="exp-idx__main_intro multipleline-ellipsis">
                          <div class="multipleline-ellipsis__content-box">${eduHistory.description }</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="main-list__item_actions">
                <button class="button_main button_icon button_light-grey operationBtn"
                  onclick="editEduHistory('<iris:des3 code="${eduHistory.eduId}"></iris:des3>');">
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
