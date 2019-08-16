<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- doi -->
<c:if test="${!empty pubDetailVO.doi}">
  <tr>
    <td align="right" class="cuti">DOI<spring:message code="colon.all" /></td>
    <td align="left"><span id="doiabc">${pubDetailVO.doi}</span></td>
  </tr>
</c:if>
<!--引用次数-->
<c:if test="${!empty pubDetailVO.citations && pubDetailVO.citations>0}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.cite.times" /> <spring:message
        code="colon.all" /></td>
    <td align="left"><c:choose>
        <c:when test="${!empty pubDetailVO.citationUrl}">
          <a onclick="ScholarView.openRolCitesUrl('<iris:des3 code="${pubDetailVO.pubId}"></iris:des3>')"
            style="cursor: pointer;">${pubDetailVO.citations}</a>
        </c:when>
        <c:otherwise>${pubDetailVO.citations}</c:otherwise>
      </c:choose></td>
  </tr>
</c:if>
<!--基金标注-->
<c:if test="${!empty pubDetailVO.fundInfo}">
  <tr>
    <td align="right" class="cuti"><spring:message code="pubView.label.fund" /> <spring:message code="colon.all" /></td>
    <td align="left">${pubDetailVO.fundInfo}</td>
  </tr>
</c:if>
<!-- 相关链接 -->
<tr>
  <td align="right" class="cuti"><spring:message code="publicationEdit.label.source_url" /> <spring:message
      code="colon.all" /></td>
  <td align="left"><c:if test="${!empty pubDetailVO.sourceUrl}">
      <c:choose>
        <c:when test="${ pubDetailVO.recordFrom=='IMPORT_FROM_PDWH' }">
          <a target="_blank" style="cursor: pointer;" class="Blue" href="${pubDetailVO.sourceUrl }"><spring:message
              code="publicationEdit.label.urlView" /></a>&nbsp;
	</c:when>
        <c:otherwise>
          <a style="cursor: pointer;" class="Blue" href="${pubDetailVO.insSourceUrl }"><spring:message
              code="publicationEdit.label.urlView" /></a>&nbsp;
	</c:otherwise>
      </c:choose>
    </c:if> <a href="#" target="_blank" title="<spring:message code="maint.label.link.google"/>" onclick="googleSearch(this)"><img
      style="width: 16px; height: 16px" align="absmiddle" src="${resmod}/images_v5/home/google_icon.gif" /></a>&nbsp; <a
    href="#" target="_blank" title="<spring:message code="maint.label.link.baidu"/>" onclick="baiduSearch(this)"><img
      style="width: 16px; height: 16px" align="absmiddle" src="${resmod}/images_v5/home/baidu_icon.gif" /></a>&nbsp;</td>
</tr>
<!-- 全文链接 -->
<c:if test="${!empty pubDetailVO.srcFulltextUrl}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.fulltext_url" /> <spring:message
        code="colon.all" /></td>
    <td align="left"><c:choose>
        <c:when test="${ pubDetailVO.recordFrom=='IMPORT_FROM_PDWH' }">
          <a target="_blank" style="cursor: pointer;" class="Blue" href="${pubDetailVO.srcFulltextUrl }"><spring:message
              code="publicationEdit.label.viewFullText" /></a>&nbsp;
	</c:when>
        <c:otherwise>
          <a style="cursor: pointer;" class="Blue" href="${pubDetailVO.insFulltextUrl }"><spring:message
              code="publicationEdit.label.viewFullText" /></a>&nbsp;
	</c:otherwise>
      </c:choose></td>
  </tr>
</c:if>
<!-- remark -->
<c:if test="${!empty pubDetailVO.remarks}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.remark" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${pubDetailVO.remarks}&nbsp;</td>
  </tr>
</c:if>
<!--最新更新时间 -->
<c:if test="${!empty pubDetailVO.gmtModified}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.update.date" /> <spring:message
        code="colon.all" /></td>
    <td align="left"><fmt:formatDate value="${pubDetailVO.gmtModified }" pattern="yyyy/MM/dd HH:mm:ss" />&nbsp;</td>
  </tr>
</c:if>
<!-- 全文附件 -->
<c:set var="hasTable" value="0" />
<c:choose>
  <c:when test="${!empty pubDetailVO.fullText  }">
    <c:set var="hasTable" value="1" />
  </c:when>
  <c:when test="${pubDetailVO.accessorys.size() ge 1}">
    <c:set var="hasTable" value="1" />
  </c:when>
  <c:otherwise>
    <c:set var="hasTable" value="0" />
  </c:otherwise>
</c:choose>
<c:if test="${hasTable==1 }">
  <tr id="fulltext_tr">
    <td align="right" class="cuti" valign="top"><spring:message code="publicationEdit.label.tabAttach" /> <spring:message
        code="colon.all" /></td>
    <td align="left" valign="top"><%@ include file="pubViewTab2.jsp"%></td>
  </tr>
</c:if>
