<c:if test="${!empty typeInfo.degree}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.programme" /> <spring:message
        code="colon.all" /></td>
    <td align="left"><iris:lable zhText="${typeInfo.degree }"></iris:lable></td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.defenseDate}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.publish_date_8" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.defenseDate}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.issuingAuthority}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.thesis_issue_org" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.issuingAuthority}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.department}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.department" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.department}</td>
  </tr>
</c:if>
<%@ include file="pubbasic_country_city_view.jsp"%>
