<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="page.result" id="result" status="itStat">
  <div class="main-list__item" id="cooperator_${result.personId}">
    <div class="main-list__item_content">
      <div class="psn-idx_medium">
        <div class="psn-idx__base-info">
          <div class="psn-idx__avatar_box">
            <div class="psn-idx__avatar_img">
              <a href="/psnweb/homepage/show?des3PsnId='${result.personDes3Id}'" target="_blank"> <img
                src="${result.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" /></a>
            </div>
          </div>
          <div class="psn-idx__main_box">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name">
                <a href="/psnweb/homepage/show?des3PsnId='${result.personDes3Id}'" target="_blank"
                  title="${result.personName}">${result.personName}</a>
              </div>
              <div class="psn-idx__main_title1" title="${result.insName}">${result.insName}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <c:if test="${result.isFriend !=true}">
      <div class="main-list__item_actions">
        <button class="button_main button_dense button_primary"
          onclick="Pub.addFriendReq('${result.personDes3Id}',this,false);">
          <s:text name="psnweb.add.friend"></s:text>
        </button>
      </div>
    </c:if>
  </div>
</s:iterator>
