<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="page" value="${page.result}">
<c:if test="${not empty page.result}">
  <div class="container__card">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name="psnweb.cooperator"></s:text>
        </div>
        <%-- <c:if test="${fn:length(page.result)>5}"> --%>
        <button class="button_main button_link" onclick="project.ajaxprjallcooperation('${des3CurrentId}')">
          <s:text name="psnweb.view.all"></s:text>
        </button>
      </div>
      <c:forEach items="${page.result}" var="result">
        <div class="main-list__list" id="Cooper_${result.personId}">
          <div class="main-list__item" style="padding: 0px 16px !important;">
            <div class="main-list__item_content">
              <div class="psn-idx_small">
                <div class="psn-idx__base-info">
                  <div class="psn-idx__avatar_box">
                    <input type="hidden" id="des3Id_${result.personId}" value="${result.personDes3Id}">
                    <div class="psn-idx__avatar_img">
                      <a href="/psnweb/homepage/show?des3PsnId='${result.personDes3Id}'" target="_blank"><img
                        src="${result.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" /></a>
                    </div>
                  </div>
                  <div class="psn-idx__main_box" style="justify-content: center !important;">
                    <div class="psn-idx__main">
                      <div class="psn-idx__main_name">
                        <a href="/psnweb/homepage/show?des3PsnId='${result.personDes3Id}'" title="${result.personName}"
                          target="_blank">${result.personName}</a>
                      </div>
                      <div class="psn-idx__main_title" title="${result.insName}">${result.insName}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="main-list__item_actions">
              <c:if test="${result.isFriend !=true}">
                <button class="button_main button_dense button_primary"
                  onclick="project.addFriendCooper('${result.personDes3Id}','${result.personId}',false,this,true)">
                  <s:text name="psnweb.add.friend"></s:text>
                </button>
              </c:if>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
  <div class="dialogs__box" style="width: 720px;" dialog-id="prjCooperation" id="prjCooperationAll">
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__header">
        <div class="dialogs__header_title">
          <s:text name="psnweb.cooperator"></s:text>
        </div>
        <i class="list-results_close" onclick="closePrjAllCooperationt()"></i>
      </div>
    </div>
    <div class="dialogs__childbox_adapted" style="height: 560px;">
      <div class="main-list__list item_no-border" list-main="prjcooperationlist"></div>
    </div>
  </div>
</c:if>
