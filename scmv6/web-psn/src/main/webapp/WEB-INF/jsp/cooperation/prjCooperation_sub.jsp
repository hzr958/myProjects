<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty page.result}">
  <div class="js_listinfo" smate_count='${page.totalCount}'></div>
  <c:forEach items="${page.result}" var="result">
    <%-- <input type="hidden" id="isFriend" value="${result.isFriend}"> --%>
    <div class="main-list__item">
      <div class="main-list__item_content">
        <div class="psn-idx_medium">
          <div class="psn-idx__base-info">
            <div class="psn-idx__avatar_box">
              <input type="hidden" id="des3Id_${result.personId}" value="${result.personDes3Id}">
              <div class="psn-idx__avatar_img">
                <a href="/psnweb/homepage/show?des3PsnId='${result.personDes3Id}'" target="_blank"><img
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
      <div class="main-list__item_actions">
        <c:if test="${result.isFriend !=true}">
          <button class="button_main button_dense button_primary"
            onclick="project.addFriendCooper('${result.personDes3Id}','${result.personId}',false,this,false)">
            <s:text name="psnweb.add.friend"></s:text>
          </button>
        </c:if>
      </div>
    </div>
  </c:forEach>
</c:if>
