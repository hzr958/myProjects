<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="page" value="${page.result}">
<input type="hidden" class="dev_may_count" value="0" />
<c:if test="${not empty page.result}">
  <div class="container__card">
    <div class="module-card__box  kownMore">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name="psnweb.may.know"></s:text>
        </div>
        <button class="button_main button_link" onclick="project.ajaxAllKnowFriend()">
          <s:text name="psnweb.more"></s:text>
        </button>
      </div>
      <c:forEach items="${page.result}" var="result">
        <div class="main-list__list" id="know_${result.psnId}">
          <div class="main-list__item">
            <div class="main-list__item_content">
              <div class="psn-idx_small">
                <div class="psn-idx__base-info">
                  <div class="psn-idx__avatar_box">
                    <%-- <input type="hidden"  name="page" value="${result.personDes3Id}"> --%>
                    <div class="psn-idx__avatar_img">
                      <a href="/psnweb/homepage/show?des3PsnId='${result.des3PsnId}'"><img src="${result.avatarUrl}"
                        onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" /></a>
                    </div>
                  </div>
                  <div class="psn-idx__main_box">
                    <div class="psn-idx__main">
                      <div class="psn-idx__main_name">
                        <a href="/psnweb/homepage/show?des3PsnId='${result.des3PsnId}'">${result.name}</a>
                      </div>
                      <div class="psn-idx__main_title">${result.insName}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="main-list__item_actions">
              <button class="button_main button_dense button_primary"
                onclick="project.addFriendReq('${result.des3PsnId}','${result.psnId}',false,this)">
                <s:text name="psnweb.add.friend"></s:text>
              </button>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
  <div class="dialogs__box" style="width: 720px;" dialog-id="kownAllPerson" id="kownAllPerson">
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__header">
        <div class="dialogs__header_title">
          <s:text name="psnweb.may.know"></s:text>
        </div>
        <button class="button_main button_icon" onclick="project.hideKownAllPsn()">
          <i class="material-icons">close</i>
        </button>
      </div>
    </div>
    <div class="dialogs__childbox_adapted" style="height: 560px;">
      <div class="main-list__list item_no-border" list-main="kownNewPsnlist"></div>
    </div>
  </div>
</c:if>
