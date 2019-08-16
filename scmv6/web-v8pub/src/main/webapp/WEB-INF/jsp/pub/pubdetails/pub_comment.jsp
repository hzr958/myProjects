<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${map.page.totalCount}'></div>
<c:if test="${not empty map.page.result}">
  <c:forEach items="${map.page.result}" var="result" varStatus="stat">
    <div class="main-list__item" style="margin-bottom: -10px">
      <div class="main-list__item_content">
        <div class="detail-comment__item" style="margin: 16px 0px 16px 0px;">
          <c:choose>
            <c:when test="${map.isLogin !=false}">
              <div class="detail-comment__avatar">
                <a href="/psnweb/homepage/show?des3PsnId='<iris:des3 code='${result.psnId}'/>'"><img
                  src="${result.psnAvatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
              </div>
            </c:when>
            <c:otherwise>
              <div class="detail-comment__avatar">
                <a href="/psnweb/outside/homepage?des3PsnId='<iris:des3 code='${result.psnId}'/>'"><img
                  src="${result.psnAvatars}" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
              </div>
            </c:otherwise>
          </c:choose>
          <div class="detail-comment__main">
            <div class="detail-comment__authorship">
              <span class="dynamic-post__author_name">
                  <a href="/psnweb/outside/homepage?des3PsnId='<iris:des3 code='${result.psnId}'/>'">${result.psnName}</a></span> <span class="detail-comment__post-time">${result.dateTimes}</span>
            </div>
            <div class="detail-comment__text" style="width: 100%;">
              <c:out value="${result.commentsContent}" escapeXml="false"></c:out>
            </div>
          </div>
        </div>
      </div>
    </div>
  </c:forEach>
  <div style="height: 10px;"></div>
</c:if>
