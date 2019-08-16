<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<table width="80%" align="center" class="t_css mtop15 " border="1">
  <input type="hidden" value="${mailInfo.id}" class="mailId" id="mailId" />
  </td>
  <tbody>
    <tr>
      <td width="12%" align="center"><s:text name="邮件模板" /></td>
      <td width="12%" align="center"><s:text name="发送时间" /></td>
      <td width="12%" align="center"><s:text name="状态" /></td>
      <td width="12%" align="center"><s:text name="操作" /></td>
    </tr>
    <s:if test="mailInfoList.size != 0">
      <c:forEach items="${mailInfoList }" var="mailInfo">
        <%-- <s:iterator value="mailInfoList" var="mailInfo"> --%>
        <tr>
          <td width="12%" align="center">${ mailInfo.template}</td>
          <td width="12%" align="center">${fn:substring(mailInfo.lastSend,0,19)}</td>
          <td width="12%" align="center"><c:choose>
              <c:when test="${mailInfo.status == -3}">待审核</c:when>
              <c:when test="${mailInfo.status == -2}">禁止发送</c:when>
              <c:when test="${mailInfo.status == -1}">黑名单</c:when>
              <c:when test="${mailInfo.status == 0}">待发送</c:when>
              <c:when test="${mailInfo.status == 1}">发送成功</c:when>
              <c:when test="${mailInfo.status == 2}">发送失败</c:when>
              <c:when test="${mailInfo.status == 3}">被定时器暂停</c:when>
              <c:otherwise>已经恢复</c:otherwise>
            </c:choose></td>
          <td width="12%" align="center"><a target="_blank" style="text-decoration: none;" class="blue"
            href="/scmwebsns/mail/view?des3Id=${mailInfo.des3Id}">查看</a></td>
        </tr>
      </c:forEach>
    </s:if>
  </tbody>
</table>