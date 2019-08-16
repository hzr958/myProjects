<c:if test="${!empty typeInfo.name}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.pub_book_book_title" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.name}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.seriesName}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.pub_book_series_name" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.seriesName}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.editors}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.pub_book_editors" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.editors }&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.chapterNo}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.pub_book_chapter_no" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.chapterNo}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.publisher}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.publisher" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.publisher}&nbsp;</td>
  </tr>
</c:if>
<%@ include file="pubbasic_country_city_view.jsp"%>
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
    <td align="left">${typeInfo.ISBN}</td>
  </tr>
</c:if>
<%-- <c:if test="${!empty typeInfo.articleNo}">	
<tr>
	<td align="right" class="cuti"><spring:message code="publicationEdit.label.articleno"/></td>
	<td align="left">${typeInfo.articleNo}</td>
</tr>
</c:if> --%>
<c:if test="${!empty typeInfo.pageNumber}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.pages" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.pageNumber }&nbsp;</td>
  </tr>
</c:if>
