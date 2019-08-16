<c:if test="${!empty typeInfo.publisher}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.publisher" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.publisher}&nbsp;</td>
  </tr>
</c:if>
<%@ include file="pubbasic_country_city_view.jsp"%>
<c:if test="${!empty typeInfo.language}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.language" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.language}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty pubDetailVO.publishDate}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.publish_year_2" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${pubDetailVO.publishDate }&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.ISBN}">
  <tr>
    <td align="right" class="cuti">ISBN<spring:message code="colon.all" /></td>
    <td align="left">${typeInfo.ISBN}&nbsp;</td>
  </tr>
</c:if>
<c:set var="total_pages">
  <x:out select="$myoutput/data/pub_book/@total_pages" escapeXml="false" />
</c:set>
<c:if test="${!empty typeInfo.totalPages}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.total_pages" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.totalPages}&nbsp;</td>
  </tr>
</c:if>
<c:set var="total_words">
  <x:out select="$myoutput/data/pub_book/@total_words" escapeXml="false" />
</c:set>
<c:if test="${!empty typeInfo.totalWords}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.total_words" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.totalWords}&nbsp;</td>
  </tr>
</c:if>
