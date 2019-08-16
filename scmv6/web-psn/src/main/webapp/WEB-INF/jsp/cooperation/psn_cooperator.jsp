<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="page.result" id="result" status="itStat">
  <div class="main-list__item" style="padding: 0px 16px !important;">
    <div class="main-list__item_content" onclick="BaseUtils.goToPsnHomepage('${result.personDes3Id}', null);"
      style="cursor: pointer;">
      <div class="psn-idx_small">
        <div class="psn-idx__base-info">
          <div class="psn-idx__avatar_box">
            <div class="psn-idx__avatar_img" style="cursor: pointer;">
              <img src="${result.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" />
            </div>
          </div>
          <div class="psn-idx__main_box" style="justify-content: center !important;">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name" title="${result.personName}">${result.personName}</div>
              <div class="psn-idx__main_title1">${result.insName}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <c:if test="${result.isFriend !=true && hasLogin}">
      <div class="main-list__item_actions">
        <button class="button_main button_dense button_primary"
          onclick="Pub.addFriendReq('${result.personDes3Id}',this,false);">
          <s:text name="psnweb.add.friend"></s:text>
        </button>
      </div>
    </c:if>
  </div>
</s:iterator>
