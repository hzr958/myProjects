<c:set var="pubList"></c:set>
<c:if test="${ei==1}">
  <c:set var="pubList">EI</c:set>
</c:if>
<c:if test="${sci==1}">
  <c:set var="pubList">${pubList},SCIE</c:set>
</c:if>
<c:if test="${istp==1}">
  <c:set var="pubList">${pubList},ISTP</c:set>
</c:if>
<c:if test="${ssci==1}">
  <c:set var="pubList">${pubList},SSCI</c:set>
</c:if>
<!--  个人成果补充 -->
<c:if test="${cssci==1}">
  <c:set var="pubList">${pubList},CSSCI</c:set>
</c:if>
<c:if test="${pku==1}">
  <c:set var="pubList">${pubList},PKU</c:set>
</c:if>
<c:if test="${other==1}">
  <c:set var="pubList">${pubList},OTHER</c:set>
</c:if>
<!--  基准库成果补充 -->
<c:if test="${cnki==1}">
  <c:set var="pubList">${pubList},CNKI</c:set>
</c:if>
<c:if test="${cnkipat==1}">
  <c:set var="pubList">${pubList},CNKIPAT</c:set>
</c:if>
<c:if test="${rainpat==1}">
  <c:set var="pubList">${pubList},RAINPAT</c:set>
</c:if>
<c:if test="${!empty pubDetailVO.listInfo}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.pub_list" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${pubDetailVO.listInfo}</td>
  </tr>
</c:if>