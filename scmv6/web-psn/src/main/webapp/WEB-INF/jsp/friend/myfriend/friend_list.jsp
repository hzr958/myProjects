<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<c:forEach items="${psnInfoList}" var="psnInfo" varStatus='sta'>
  <div class="main-list__item" id="${psnInfo.psnId }" style="padding: 0px;">
    <div class="main-list__item_content">
      <div class="psn-idx_medium">
        <div class="psn-idx__base-info">
          <div class="psn-idx__avatar_box">
            <div class="psn-idx__avatar_img">
              <a href="${psnInfo.psnShortUrl}" target="_blank"><img src="${psnInfo.person.avatars}"
                onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
            </div>
          </div>
          <div class="psn-idx__main_box">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name">
                <a href="${psnInfo.psnShortUrl}" target="_blank" title="${psnInfo.name}">${psnInfo.name}</a>
                <c:if test="${not empty psnInfo.psnOpenId}"><a style="display: inline-flex;align-items: center; "  href="${psnInfo.psnShortUrl}" target="_blank" title='<s:text name="psn.label.ScholarId"/>'>&nbsp;&nbsp;<i class="SID-container_avator"></i>${psnInfo.psnOpenId}</a></c:if>
              </div>
              <div class="psn-idx__main_title1">
                <c:if test="${not empty psnInfo.posAndTitolo && not empty psnInfo.insAndDep}">${psnInfo.insAndDep},&nbsp;${psnInfo.posAndTitolo}</c:if>
                <c:if test="${empty psnInfo.posAndTitolo && not empty psnInfo.insAndDep}">${psnInfo.insAndDep}</c:if>
                <c:if test="${not empty psnInfo.posAndTitolo && empty psnInfo.insAndDep}">${psnInfo.posAndTitolo}</c:if>
              </div>
              <div class="psn-idx__main_area">
                <div class="kw__box">
                  <c:forEach items="${psnInfo.discList}" var="keyword">
                    <div class="kw-chip_small">${keyword.keyWords}</div>
                  </c:forEach>
                </div>
              </div>
              <div class="psn-idx__main_stats">
                <span class="psn-idx__main_stats-item"><s:text name="myfriend.prjsum" /> ${psnInfo.prjSum}</span><span
                  class="psn-idx__main_stats-item"><s:text name="myfriend.pubsum" /> ${psnInfo.pubSum}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-list__item_actions">
      <div class="action-dropdown">
        <button class="button_main button_icon" onclick="Friend.friendOpt(this,event);">
          <i class="material-icons">more_vert</i>
        </button>
        <ul class="action-dropdown__list">
          <li class="action-dropdown__item item_hovered" onclick="Friend.sendMsg('${psnInfo.des3PsnId}');"><s:text
              name="friend.sendMsg" /></li>
          <li class="action-dropdown__item" onclick="Friend.ajaxDelFriend('${psnInfo.psnId }', '${psnInfo.des3PsnId}');"><s:text
              name="friend.remove" /></li>
        </ul>
      </div>
    </div>
  </div>
</c:forEach>
