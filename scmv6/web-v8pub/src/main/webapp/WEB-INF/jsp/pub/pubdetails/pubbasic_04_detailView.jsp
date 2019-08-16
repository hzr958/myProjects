<c:set var="typeInfo" value="${pubDetailVO.pubTypeInfo}" />
<c:if test="${!empty typeInfo.name}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.jname" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.name}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.publishStatus}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.publish_state" /> <spring:message
        code="colon.all" /></td>
    <td align="left"><c:if test="${typeInfo.publishStatus=='P'}">
        <spring:message code="pub.enter.yifabiao" />
      </c:if> <c:if test="${typeInfo.publishStatus=='A'}">
        <spring:message code="pub.enter.weifabiao" />
      </c:if></td>
  </tr>
</c:if>
<%-- <c:if test="${typeInfo.publishStatus=='A' && !empty typeInfo.acceptDate}">			
<tr>
	<td  align="right" class="cuti"><spring:message code="publicationEdit.label.accept_date" /><spring:message code="colon.all" /></td>
	<td align="left">${typeInfo.acceptDate }&nbsp;</td>
</tr>
</c:if> --%>
<c:if test="${typeInfo.publishStatus=='P' }">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.publish_year" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${pubDetailVO.publishDate }&nbsp;</td>
  </tr>
</c:if>
<%-- <c:if test="${!empty typeInfo.articleNo}">
<tr>
	<td  align="right" class="cuti"><spring:message code="publicationEdit.label.articleno" /><spring:message code="colon.all" /></td>
	<td align="left">${typeInfo.articleNo}&nbsp;</td>
</tr>
</c:if> --%>
<c:if test="${!empty typeInfo.issue}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.issue" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.issue}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.volumeNo}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.volume" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.volumeNo}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.pageNumber}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.pages" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.pageNumber }&nbsp;</td>
  </tr>
</c:if>
<%@ include file="pubbasic_list_detailView.jsp"%>
