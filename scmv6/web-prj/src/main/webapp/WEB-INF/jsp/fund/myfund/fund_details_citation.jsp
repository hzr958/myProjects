<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 中文标题的citation标签 --%>
<c:if test="${! empty constFundCategoryInfo.zhTitle }">
  <meta name="citation_zh_title" content="${constFundCategoryInfo.zhTitle }" />
</c:if>
<%-- 英文标题的citation标签 --%>
<c:if test="${! empty constFundCategoryInfo.enTitle }">
  <meta name="citation_zh_title" content="${constFundCategoryInfo.enTitle }" />
</c:if>
<%-- 描述的citation标签 --%>
<c:if test="${! empty constFundCategoryInfo.zhShowDesc }">
  <meta name="citation_zhShowDesc" content="${constFundCategoryInfo.zhShowDesc }" />
</c:if>
<c:if test="${! empty constFundCategoryInfo.enShowDesc }">
  <meta name="citation_enShowDesc" content="${constFundCategoryInfo.enShowDesc }" />
</c:if>
<%-- DC标签： --%>
<%-- 中文标题的citation标签 --%>
<c:if test="${! empty constFundCategoryInfo.zhTitle }">
  <meta name="DC.title" content="${constFundCategoryInfo.zhTitle }" xml:lang="zh_CN" />
</c:if>
<%-- 英文标题的citation标签 --%>
<c:if test="${! empty constFundCategoryInfo.enTitle }">
  <meta name="DC.title" content="${constFundCategoryInfo.enTitle }" xml:lang="en_US" />
</c:if>
<%-- 描述的citation标签 --%>
<c:if test="${! empty constFundCategoryInfo.zhShowDesc }">
  <meta name="DCTERMS.isPartOf" content="${constFundCategoryInfo.zhShowDesc }" xml:lang="zh_CN" />
</c:if>
<c:if test="${! empty constFundCategoryInfo.enShowDesc }">
  <meta name="DCTERMS.isPartOf" content="${constFundCategoryInfo.enShowDesc }" xml:lang="en_US" />
</c:if>
<c:choose>
  <c:when test="${locale eq 'zh_CN' }">
    <c:if test="${!empty constFundCategoryInfo.zhTitle}">
      <c:set var="showFundTitle" value="${constFundCategoryInfo.zhTitle }" />
    </c:if>
    <c:if test="${!empty constFundCategoryInfo.enTitle && empty constFundCategoryInfo.zhTitle}">
      <c:set var="showFundTitle" value="${constFundCategoryInfo.enTitle }" />
    </c:if>
  </c:when>
  <c:otherwise>
    <c:if test="${!empty constFundCategoryInfo.enTitle}">
      <c:set var="showFundTitle" value="${constFundCategoryInfo.enTitle }" />
    </c:if>
    <c:if test="${!empty constFundCategoryInfo.zhTitle && empty constFundCategoryInfo.enTitle}">
      <c:set var="showFundTitle" value="${constFundCategoryInfo.zhTitle }" />
    </c:if>
  </c:otherwise>
</c:choose>
<c:choose>
  <c:when test="${locale eq 'zh_CN' }">
    <c:if test="${!empty constFundCategoryInfo.zhShowDesc}">
      <c:set var="briefDesc" value="${constFundCategoryInfo.zhShowDesc }" />
    </c:if>
    <c:if test="${!empty constFundCategoryInfo.enShowDesc && empty constFundCategoryInfo.zhShowDesc}">
      <c:set var="briefDesc" value="${constFundCategoryInfo.enShowDesc }" />
    </c:if>
  </c:when>
  <c:otherwise>
    <c:if test="${!empty constFundCategoryInfo.enShowDesc}">
      <c:set var="briefDesc" value="${constFundCategoryInfo.enShowDesc }" />
    </c:if>
    <c:if test="${!empty constFundCategoryInfo.zhShowDesc && empty constFundCategoryInfo.enShowDesc}">
      <c:set var="briefDesc" value="${constFundCategoryInfo.zhShowDesc }" />
    </c:if>
  </c:otherwise>
</c:choose>
<c:if test="${! empty constFundCategoryInfo.detailsUrl }">
  <meta property="og:url" content="${constFundCategoryInfo.detailsUrl }" />
</c:if>
<c:if test="${! empty showFundTitle }">
  <meta property="og:title" content="${showFundTitle }" />
</c:if>
<c:if test="${! empty briefDesc }">
  <meta property="og:description" content="${briefDesc }" />
</c:if>
<c:if test="${! empty logoUrl }">
  <meta property="og:image" content="${logoUrl }" />
</c:if>
<meta property="og:site_name" content="ScholarMate" />