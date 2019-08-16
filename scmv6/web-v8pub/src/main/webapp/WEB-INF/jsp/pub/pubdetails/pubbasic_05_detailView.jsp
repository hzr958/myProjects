<c:if test="${!empty typeInfo.applicationNo}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.patent_no" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.applicationNo}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.publicationOpenNo}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.patent_open_no" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.publicationOpenNo}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.IPC}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.category_no" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.IPC}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.applicationDate}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.apply_year" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.applicationDate}</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.type}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.patent_type" /> <spring:message
        code="colon.all" /></td>
    <td align="left"><iris:lable zhText="${typeInfo.type }"></iris:lable></td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.startDate || !empty typeInfo.endDate}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.effective_date" /> <spring:message
        code="colon.all" /></td>
    <td align="left"><c:if test="${!empty typeInfo.startDate}">
	${typeInfo.startDate }
	</c:if> <c:if test="${!empty typeInfo.startDate && !empty typeInfo.endDate}">
        <spring:message code="publicationEdit.label.period_seperator" />
      </c:if> <c:if test="${!empty typeInfo.endDate}">
	${typeInfo.endDate }
	</c:if></td>
  </tr>
</c:if>
<c:set var="patent_org">
  <x:out select="$myoutput/data/pub_patent/@patent_org" />
</c:set>
<c:if test="${!empty typeInfo.issuingAuthority}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.patent_org" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.issuingAuthority}&nbsp;</td>
  </tr>
</c:if>
<%@ include file="pubbasic_country_city_view.jsp"%>
