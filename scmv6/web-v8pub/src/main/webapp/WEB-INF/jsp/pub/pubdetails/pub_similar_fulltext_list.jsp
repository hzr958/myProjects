<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty similarInfoList}">
  <c:forEach items="${similarInfoList}" var="info">
    <div class="paper_container-content_item">
      <img class="paper_container-avator" src="${info.fulltextImg}"
        onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
      <div class="paper_container-infor">
        <div class="paper_container-name" title="${info.fulltextName}">${info.fulltextName}</div>
        <div class="paper_container-infor_author" title="${info.psnName}">${info.psnName}</div>
        <div class="paper_container-infor_school" title="${info.psnTitle}">${info.psnTitle}</div>
      </div>
      <div class="paper_container-size">
        <c:if test="${not empty info.fulltextSize && info.fulltextSize != 0}">
          <fmt:formatNumber value="${info.fulltextSize/1024}" minFractionDigits="2"></fmt:formatNumber> KB
	</c:if>
      </div>
      <div class="paper_container-download" onclick="location.href='${info.downloadUrl}'">
        <spring:message code="pub.details.fulltext.download" />
      </div>
    </div>
  </c:forEach>
</c:if>