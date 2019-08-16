<c:if test="${!empty typeInfo.issuingAuthority}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.issue_ins_name" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.issuingAuthority}</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.awardDate}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.publish_year_1" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.awardDate}</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.category}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.award_category" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.category}</td>
  </tr>
</c:if>
<c:if test="${!empty typeInfo.grade}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.award_grade" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${typeInfo.grade}</td>
  </tr>
</c:if>
<%@ include file="pubbasic_country_city_view.jsp"%>
