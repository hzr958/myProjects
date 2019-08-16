
<c:if test="${!empty pub_date}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.publish_year" /> <spring:message
        code="colon.all" /></td>
    <td align="left"><x:out escapeXml="false" select="$myoutput/data/publication/@publish_date" /></td>
  </tr>
</c:if>
<c:if test="${di_mode=='year'}">
  <c:if test="${lang=='zh' }">
    <c:set var="year">
      <x:out escapeXml="false" select="$myoutput/data/publication/@publish_year" />
    </c:set>
    <c:if test="${!empty year }">
      <tr>
        <td align="right" class="cuti"><spring:message code="publicationEdit.label.publish_year" /> <spring:message
            code="colon.all" /></td>
        <td align="left">${year }<spring:message code="publicationEdit.label.year" /> <x:out escapeXml="false"
            select="$myoutput/data/publication/@pub_date_desc" />
        </td>
      </tr>
    </c:if>
  </c:if>
  <c:if test="${lang=='en' }">
    <c:set var="year">
      <x:out escapeXml="false" select="$myoutput/data/publication/@publish_year" />
    </c:set>
    <c:if test="${!empty year }">
      <tr>
        <td align="right" class="cuti"><spring:message code="publicationEdit.label.publish_year" /> <spring:message
            code="colon.all" /></td>
        <td align="left"><x:out escapeXml="false" select="$myoutput/data/publication/@pub_date_desc" />&nbsp;<x:out
            escapeXml="false" select="$myoutput/data/publication/@publish_year" /></td>
      </tr>
    </c:if>
  </c:if>
</c:if>
<c:if test="${di_mode=='period'}">
  <c:set var="start_date">
    <x:out escapeXml="false" select="$myoutput/data/pub_other/@start_date" />
  </c:set>
  <c:set var="end_date">
    <x:out escapeXml="false" select="$myoutput/data/pub_other/@end_date" />
  </c:set>
  <c:if test="${start_date!='' || end_date!=''}">
    <tr>
      <td align="right" class="cuti"><spring:message code="publicationEdit.label.publish_year" /> <spring:message
          code="colon.all" /></td>
      <td align="left"><c:if test="${! empty start_date}">${start_date }</c:if> <c:if
          test="${! empty start_date && ! empty end_date}">
          <spring:message code="publicationEdit.label.period_seperator" />
        </c:if> <c:if test="${! empty end_date}">${end_date }</c:if></td>
    </tr>
  </c:if>
</c:if>
&nbsp;
<%@ include file="pubbasic_country_city_view.jsp"%>
<%@ include file="pubbasic_list_detailView.jsp"%>