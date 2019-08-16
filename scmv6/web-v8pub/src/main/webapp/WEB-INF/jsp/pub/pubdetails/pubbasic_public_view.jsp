<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" name="viewDetailOp" id="viewDetailOp" value="${viewDetailOp}" />
<input type="hidden" name="pubId" id="pubId" value="${pubDetailVO.des3PubId}" />
<input type="hidden" name="briefDesc" id="briefDescabc" value="${pubDetailVO.briefDesc}" />
<tr>
  <td width="150" align="right" class="cuti"><spring:message code="publicationEdit.label.serialno" /> <spring:message
      code="colon.all" /></td>
  <td align="left">${pubDetailVO.pubId}<input type="hidden" value="${pubDetailVO.pubId}" id="hidden_pubId" /> <%--1:在线导入--%>
    <img style="width: 12px; height: 13px" align="absmiddle" id="record_from" src="${res}/images_v5/pub_online.gif" />
  </td>
</tr>
<tr>
  <td align="right" class="cuti"><spring:message code="publicationEdit.label.category" /> <spring:message
      code="colon.all" /></td>
  <td align="left"><c:choose>
      <c:when test="${empty pubDetailVO.typeName}">
        <spring:message code="publicationEdit.label.pub_type.unknown" />
      </c:when>
      <c:otherwise>${pubDetailVO.typeName }</c:otherwise>
    </c:choose></td>
</tr>
<!-- 标题 -->
<c:if test="${!empty pubDetailVO.title}">
  <tr>
    <td align="right" valign="top" class="cuti"><spring:message
        code="publicationEdit.label.title_${pubDetailVO.pubType}" /> <spring:message code="colon.all" /></td>
    <td align="left" id="bdText_zh_CN">${pubDetailVO.title}</td>
  </tr>
</c:if>
<!-- 摘要 -->
<c:if test="${!empty pubDetailVO.summary}">
  <tr>
    <td align="right" valign="top" class="cuti"><spring:message
        code="publicationEdit.label.abstract_${pubDetailVO.pubType}" /> <spring:message code="colon.all" /></td>
    <td align="left"><iris:subAbstract value="${pubDetailVO.summary }" lan="zh" /></td>
  </tr>
</c:if>
<!-- 关键词 -->
<c:if test="${!empty pubDetailVO.keywords}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.keywords" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${pubDetailVO.keywords}&nbsp;</td>
  </tr>
</c:if>
<!-- 作者 -->
<c:if test="${!empty pubDetailVO.authorNames}">
  <tr>
    <td align="right" class="cuti"><spring:message code="publicationEdit.label.author" /> <spring:message
        code="colon.all" /></td>
    <td align="left">${pubDetailVO.authorNames}</td>
  </tr>
</c:if>
