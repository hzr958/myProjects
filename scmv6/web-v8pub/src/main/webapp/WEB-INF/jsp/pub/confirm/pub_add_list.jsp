<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${pubListVO.totalCount}"></div>
<c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
  <div class="main-list__item">
    <div class="body_content-item main-list__item_content">
      <div class="body_content-item_selector" dtId='<c:out value="${pub.pubId}" escapeXml="false"/>'
        des3PubId="'<iris:des3 code='${pub.pubId }'/>'">
        <i class="body_content-item_selector-tip"></i>
      </div>
      <div class="body_content-item_detail">
        <div class="body_content-item_title">
          <a href="${pub.pubIndexUrl}" target="_blank" title='<c:out value="${pub.title}" escapeXml="false"/>'><c:out
              value="${pub.title}" escapeXml="false" /></a>
        </div>
        <div class="body_content-item_author" title='<c:out value="${pub.authorNames}" escapeXml="false"/>'>
          <c:out value="${pub.authorNames}" escapeXml="false" />
        </div>
        <div class="body_content-item_time" title='<c:out value="${pub.briefDesc}" escapeXml="false"/>'>
          <c:out value="${pub.briefDesc}" escapeXml="false" />
        </div>
      </div>
    </div>
  </div>
</c:forEach>
