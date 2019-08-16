<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="module-card__box">
  <div class="module-card__header">
    <div class="module-card-header__title">
      <s:text name='groups.outside.discuss.member' />
    </div>
    <button class="button_main button_link" onclick="GrpBase.toOutsideGrpMember()">
      <s:text name='groups.outside.more.members' />
    </button>
  </div>
  <div class="main-list__list">
    <s:iterator value="memberInfoList" var="psn" status="st">
      <div class="main-list__item">
        <div class="main-list__item_content">
          <div class="psn-idx_small">
            <div class="psn-idx__base-info">
              <div class="psn-idx__avatar_box">
                <div class="psn-idx__avatar_img">
                  <a href="/psnweb/outside/homepage?des3PsnId=${psn.des3MemberId}" target="_Blank"> <img
                    src="${psn.memberAvatur }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
                </div>
              </div>
              <div class="psn-idx__main_box">
                <div class="psn-idx__main">
                  <div class="psn-idx__main_name">
                    <a href="/psnweb/outside/homepage?des3PsnId=${psn.des3MemberId}" target="_Blank"
                      title="${psn.memberName }">${psn.memberName }</a>
                  </div>
                  <div class="psn-idx__main_title" title="${psn.insName }">${psn.insName }</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </s:iterator>
  </div>
</div>
