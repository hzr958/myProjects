<c:if test="${!empty pubDetailVO.countryName}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.country_name" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${pubDetailVO.countryName}&nbsp;</td>
  </tr>
</c:if>
<c:if test="${!empty pubDetailVO.cityName}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.city" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${pubDetailVO.cityName}&nbsp;</td>
  </tr>
</c:if>
