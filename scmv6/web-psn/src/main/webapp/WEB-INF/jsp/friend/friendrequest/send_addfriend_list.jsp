<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="main-list__header">
  <div class="main-list-header__title">
    <s:text name="friend.sendReq" />
  </div>
  <button class="button_main button_link" onclick="Friend.reqList();">
    <s:text name="friend.queryReq" />
  </button>
</div>
<div class="main-list__list" id="sendaddfriend">
  <c:forEach items="${psnInfoList}" var="psnInfo">
    <div class="main-list__item" id='${psnInfo.psnId}'>
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
                  <a href="${psnInfo.psnShortUrl}" title="${psnInfo.name}" target="_blank">${psnInfo.name}</a>
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
        <button class="button_main button_primary-changestyle"
          onclick="Friend.cancelSendFriend('${psnInfo.des3PsnId}','${psnInfo.psnId}')">
          <s:text name="friend.cancelSend" />
        </button>
      </div>
    </div>
  </c:forEach>
  <c:if test="${sendCount > 3}">
    <div class="check-more_info dev_sendlist_more">
      <span onclick="Friend.sendaddfriend(1);"><s:text name="friend.viewMore" /></span>
    </div>
  </c:if>
</div>