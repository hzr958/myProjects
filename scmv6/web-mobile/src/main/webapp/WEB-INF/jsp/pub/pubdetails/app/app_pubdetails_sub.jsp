<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<c:choose>
  <c:when test="${pubDetailVO.pubId==0}">
    <dl class="tc">
      <dd class="response_no-result">已浏览完毕</dd>
    </dl>
  </c:when>
  <c:when test="${empty pubDetailVO }">
    <div class="response_no-result">未找到对应成果详情</div>
  </c:when>
  <c:otherwise>
    <div class="paper_detial" style=" margin-bottom: 12px;">
      <dl>
        <h2>
          <c:if test="${!empty pubDetailVO.title}">
            <span id="pubTitleShare">${pubDetailVO.title}</span>
          </c:if>
        </h2>
        <dt>
          <c:if test="${!empty pubDetailVO.authorNames}">
		            ${pubDetailVO.authorNames}
		            </c:if>
        </dt>
        <dd>
          <c:if test="${!empty pubDetailVO.briefDesc}">
		            ${pubDetailVO.briefDesc }
		            </c:if>
        </dd>
      </dl>
      <c:if test="${not empty pubDetailVO.summary}">
        <h3>摘要:</h3>
        <p>
          <c:if test="${not empty pubDetailVO.summary}">
            <span id="pubAbs">${pubDetailVO.summary }</span>
          </c:if>
        </p>
      </c:if>
      <c:if test="${not empty pubDetailVO.keywords}">
        <h3>关键词:</h3>
        <p style="word-wrap: break-word;">
          <c:if test="${not empty pubDetailVO.keywords}">${pubDetailVO.keywords}&nbsp;</c:if>
        </p>
      </c:if>
      <!-- DOI的显示要放到关键词的下面 -->
      <c:if test="${!empty pubDetailVO.doi}">
        <h3>
          DOI：
          <c:if test="${locale eq 'en_US' }">&nbsp;</c:if>
          <a href="http://dx.doi.org/${pubDetailVO.doi}" target="_Blank">${pubDetailVO.doi}</a>
        </h3>
      </c:if>
    </div>
  </c:otherwise>
</c:choose>
