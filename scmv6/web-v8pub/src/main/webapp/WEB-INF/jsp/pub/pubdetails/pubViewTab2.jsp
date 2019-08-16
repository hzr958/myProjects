<%@ page language="java" pageEncoding="UTF-8"%>
<c:set var="index" value="0" scope="page"></c:set>
<table id="tblAttachs" width="100%" border="0" cellspacing="0" cellpadding="0" class="tr_box">
  <thead>
    <tr>
      <td width="8%"><spring:message code="publicationEdit.label.seq_no" /></td>
      <td width="30%"><spring:message code="publicationEdit.label.file_name" /></td>
      <td width="10%"><spring:message code="publicationEdit.label.action" /></td>
    </tr>
  </thead>
  <tbody>
    <c:if test="${!empty pubDetailVO.fullText }">
      <c:set var="index" value="${index + 1 }" scope="page"></c:set>
      <tr>
        <td align="center">${index }</td>
        <td align="left">${pubDetailVO.fullText.fileName}<font color="#598d26"> [<spring:message
              code="publicationEdit.label.fulltext" />]
        </font></td>
        <td align="center"><a class="Blue" style="cursor: pointer" href="${pubDetailVO.simpleDownLoadUrl}"><spring:message
              code="publicationEdit.label.download" /></a></td>
      </tr>
    </c:if>
    <c:forEach items="${pubDetailVO.accessorys}" var="result" varStatus="stat">
      <c:set value="${index + 1 }" var="index" scope="page" />
      <tr>
        <td align="center">${index }</td>
        <td align="left">${result.fileName }</td>
        <td align="center"><a class="Blue" style="cursor: pointer" href="${ result.simpleFileUrl}"><spring:message
              code="publicationEdit.label.download" /></a></td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<%-- 这个img不要删除，当CAS验证完成，往这边跳的时候，用来初始化权限的 --%>
<%-- 加这个判断是因为这个问题SCM-5308，如果从sns过来，这里初始化权限的话，而且改用户只有一个个人角色，系统会自动设换到rol个人版，然后带switchInsId，把原来的科研之友图标和菜单更改 --%>
<%-- <c:if test="${param.from ne 'sns'}">
	<img id="test" src="${ctx}/main" style="display: none" />
</c:if> --%>