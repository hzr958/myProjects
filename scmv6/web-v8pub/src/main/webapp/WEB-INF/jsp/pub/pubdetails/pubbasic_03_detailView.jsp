<c:set var="typeInfo" value="${pubDetailVO.pubTypeInfo}" />
<c:if test="${!empty typeInfo.paperType}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.conf_type" /> <spring:message
        code="colon.all" /></td>
    <td align="left"><iris:lable zhText="${typeInfo.paperType}"></iris:lable></td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.name}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.proceeding_title" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.name}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.organizer}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.conf_organizer" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.organizer}</td>
  </tr>
</c:if>
<%@ include file="pubbasic_country_city_view.jsp"%>
<c:if test="${!empty typeInfo.pageNumber}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.pages" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.pageNumber }&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty pubDetailVO.publishDate}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.publish_date" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${pubDetailVO.publishDate}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.startDate || !empty typeInfo.endDate}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.conf_date" /> <spring:message
        code="colon.all" /></td>
    <td align="left"><c:if test="${! empty typeInfo.startDate}">
		${typeInfo.startDate }
		</c:if> <c:if test="${! empty typeInfo.startDate && ! empty typeInfo.endDate}">
        <spring:message code="publicationEdit.label.period_seperator" />
      </c:if> <c:if test="${! empty typeInfo.endDate}">
		${typeInfo.endDate }
		</c:if>&nbsp;</td>
  </tr>
</c:if>