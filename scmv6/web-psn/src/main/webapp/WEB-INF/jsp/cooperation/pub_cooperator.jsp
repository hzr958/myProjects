<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.result.size()>0">
  <div class="container__card dev_pub_cooperator_div">
    <div class="module-card__box">
      <div class="module-card__header">
        <div class="module-card-header__title">
          <s:text name="psnweb.cooperator"></s:text>
        </div>
        <button class="button_main button_link" onclick="Pub.pubCooperatorAll();">
          <s:text name="psnweb.view.all"></s:text>
        </button>
      </div>
      <div class="main-list__list">
        <s:iterator value="page.result" id="result" status="itStat">
          <div class="main-list__item" style="padding: 0px 16px !important;">
            <div class="main-list__item_content">
              <div class="psn-idx_small">
                <div class="psn-idx__base-info">
                  <div class="psn-idx__avatar_box">
                    <div class="psn-idx__avatar_img" onclick="Pub.lookOtherPsnHome('${result.personDes3Id}');"
                      style="cursor: pointer;">
                      <img src="${result.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" />
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
            <c:if test="${result.isFriend !=true}">
              <div class="main-list__item_actions">
                <button class="button_main button_dense button_primary"
                  onclick="Pub.addFriendReq('${result.personDes3Id}',this,true);">
                  <s:text name="psnweb.add.friend"></s:text>
                </button>
              </div>
            </c:if>
          </div>
        </s:iterator>
      </div>
    </div>
  </div>
</s:if>
