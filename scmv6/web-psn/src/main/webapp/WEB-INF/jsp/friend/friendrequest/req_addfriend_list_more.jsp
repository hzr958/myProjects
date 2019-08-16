<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:forEach items="${psnInfoList}" var="psnInfo">
  <div class="main-list__item dev_reqlist_${psnInfo.psnId}">
    <div class="main-list__item_content">
      <div class="psn-idx_medium">
        <div class="psn-idx__base-info">
          <div class="psn-idx__avatar_box">
            <div class="psn-idx__avatar_img">
              <a href="/psnweb/homepage/show?des3PsnId=${psnInfo.des3PsnId}" target="_blank"><img
                src="${psnInfo.person.avatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
            </div>
          </div>
          <div class="psn-idx__main_box">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name">
                <a href="/psnweb/homepage/show?des3PsnId=${psnInfo.des3PsnId}" title="${psnInfo.name}" target="_blank">${psnInfo.name}</a>
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
      <button class="button_main button_grey" onclick="Friend.neglet('${psnInfo.psnId}','${psnInfo.des3PsnId}');">
        <s:text name="friend.ignore" />
      </button>
      <button class="button_main button_primary-changestyle"
        onclick="Friend.agree('${psnInfo.psnId}','${psnInfo.des3PsnId}');">
        <s:text name="friend.accept" />
      </button>
    </div>
  </div>
</c:forEach>