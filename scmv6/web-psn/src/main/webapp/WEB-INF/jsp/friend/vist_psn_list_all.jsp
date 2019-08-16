<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="psnInfoList" var="psnInfo" status="st">
  <div class="main-list__item">
    <div class="main-list__item_content"
      onclick="BaseUtils.goToPsnHomepage('<iris:des3 code="${psnInfo.psnId}"/>', '${psnInfo.psnShortUrl}');"
      style="cursor: pointer;">
      <div class="psn-idx_small">
        <div class="psn-idx__base-info">
          <div class="psn-idx__avatar_box">
            <div class="psn-idx__avatar_img">
              <img src="${psnInfo.avatarUrl}" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'" />
            </div>
          </div>
          <div class="psn-idx__main_box">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name" title="${psnInfo.name}">${psnInfo.name}</div>
              <div class="psn-idx__main_title1">${psnInfo.insName }</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <c:if test="${psnInfo.isFriend ==false}">
        <button class="button_main button_dense button_primary"
          onclick="MainBase.addOneFriend('<iris:des3 code="${psnInfo.psnId}"/>',this)">
          <s:text name="friend.main.list.addfriend" />
        </button>
      </c:if>
    </div>
  </div>
</s:iterator>