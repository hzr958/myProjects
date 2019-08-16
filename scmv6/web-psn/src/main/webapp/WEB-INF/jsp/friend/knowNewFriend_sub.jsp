<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty page.result}">
  <div class="js_listinfo" smate_count='${page.totalCount}'></div>
  <c:forEach items="${page.result}" var="result">
    <div class="main-list__item" id="know_${result.psnId}">
      <div class="main-list__item_content">
        <div class="psn-idx_medium">
          <div class="psn-idx__base-info">
            <div class="psn-idx__avatar_box">
              <div class="psn-idx__avatar_img">
                <a href="/psnweb/homepage/show?des3PsnId='${result.psnId}'" target="_blank"><img
                  src="${result.avatarUrl}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" /></a>
              </div>
            </div>
            <div class="psn-idx__main_box">
              <div class="psn-idx__main">
                <div class="psn-idx__main_name">
                  <a href="/psnweb/homepage/show?des3PsnId='${result.des3PsnId}'" target="_blank">${result.name}</a>
                </div>
                <div class="psn-idx__main_title">${result.insName}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="main-list__item_actions">
        <button class="button_main button_dense button_primary-changestyle"
          onclick="project.addFriendReq('${result.des3PsnId}','${result.psnId}',true,this)">
          <s:text name="psnweb.add.friend"></s:text>
        </button>
      </div>
    </div>
  </c:forEach>
</c:if>