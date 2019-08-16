<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:forEach items="${pubVO.pubInfoList }" var="result" varStatus="status">
  <div class="new__resume-list__content-title new__resume-list__content-sub"
    style="display: flex; align-items: baseline;">
    <c:if test="${status.index < 9}">
      <div style="width: 26px; min-width: 28px; line-height: 22px;">
    </c:if>
    <c:if test="${status.index >= 9}">
      <div style="width: 26px; min-width: 36px; line-height: 22px;">
    </c:if>
    (${status.count})&nbsp;
  </div>
  <div>
    <span>${result.authorNames}</span>
    <c:if test="${not empty result.authorNames}">, </c:if>
    <a href="${result.pubIndexUrl }" target="_blank">${result.title}</a>
    <c:if test="${not empty result.briefDesc}">, ${result.briefDesc}</c:if>
    <c:if test="${result.pubType == 1 and not empty result.awardAuthorList}">, ${result.awardAuthorList }</c:if>
    &nbsp;&nbsp;&nbsp;&nbsp;<strong>(${result.pubTypeName })</strong>
  </div>
  </div>
</c:forEach>
