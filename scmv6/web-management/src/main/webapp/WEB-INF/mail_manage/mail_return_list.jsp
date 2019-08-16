<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" value="${showMailConnError }" class="hidden_data" count=${page.totalCount } page=${page.pageNo }
  totalpages=${page.totalPages } currentCount="${page.pageNo }">
<tr>
  <th>收件箱</th>
  <th>发件箱</th>
  <th>发送时间</th>
  <th>主题</th>
  <th>邮件内容</th>
  <th>退信类别</th>
  <th>创建时间</th>
</tr>
<s:iterator value="mailReturnedRecordList" var="info" status="s">
  <tr>
    <td>${info.address }</td>
    <td>${info.account }</td>
    <td><fmt:formatDate value="${info.sendDate }" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
    <td title="${info.subject }" style="max-width: 250px;" class="text_overflow">${info.subject }</td>
    <td class="show_content"><a>查看回复</a>
      <div class="mail_data" style="display: none;">${info.content }</div></td>
    <td><s:if test="#info.type == 0 ">异常</s:if></td>
    <td><fmt:formatDate value="${info.createDate }" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
  </tr>
</s:iterator>
