
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount }'></div>
<s:iterator value="psnInfoList" var="pi" status="st">
  <div class="main-list__item">
    <div class="main-list__item_content">
      <div class="psn-idx_medium">
        <div class="psn-idx__base-info">
          <div class="psn-idx__avatar_box">
            <div class="psn-idx__avatar_img">
              <a target="_blank" href="/psnweb/homepage/show?des3PsnId=${pi.des3PsnId }"> <img
                src="${pi.person.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
              </a>
            </div>
          </div>
          <div class="psn-idx__main_box">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name">
                <a target="_blank" href="/psnweb/homepage/show?des3PsnId=${pi.des3PsnId }">${pi.person.viewName}</a>
              </div>
              <div class="psn-idx__main_title" style="max-width: 300px !important">${pi.insInfo}</div>
              <div class="psn-idx__main_area">
                <div class="kw__box">
                  <s:iterator value="#pi.discList" var="keyword" status="st">
                    <div class="kw-chip_small">${keyword.keyWords }</div>
                  </s:iterator>
                </div>
              </div>
              <div class="psn-idx__main_stats">
                <span class="psn-idx__main_stats-item"><s:text name='myfriend.prjsum' /> ${pi.prjSum }</span><span
                  class="psn-idx__main_stats-item"><s:text name='myfriend.pubsum' /> ${pi.pubSum }</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <button class="button_main button_dense button_primary-reverse" style="${pi.isFriend ? 'display:none;' : ''}"
        id="addIdentificFriendBtn" onclick="addIdentificFriend('${pi.des3PsnId }','${pi.psnId}',this);">
        <s:text name='psnlist.btn.add.friend' />
      </button>
    </div>
  </div>
</s:iterator>
