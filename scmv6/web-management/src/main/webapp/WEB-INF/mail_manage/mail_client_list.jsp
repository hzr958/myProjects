<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input value="  " type="hidden" class="hidden_data" count=${page.totalCount } page=${page.pageNo }
  totalpages=${page.totalPages } currentCount="${page.pageNo }">
<tr>
  <th>序号</th>
  <th>客户端名字</th>
  <th>优先发送模板</th>
  <th>优先发送邮箱</th>
  <th>优先发送账号</th>
  <th>状态</th>
  <th>等待时间</th>
  <th>描述</th>
  <th>更新时间</th>
</tr>
<s:iterator value="mailClientList" var="info" status="s">
  <tr>
    <td>${info.id }</td>
    <td>${info.clientName }</td>
    <td>${info.priorTemplate }</td>
    <td>${info.priorEmail }</td>
    <td>${info.priorSenderAccount }</td>
    <td><s:if test="#info.status == 0 ">可用</s:if> <s:if test="#info.status == 1 ">不可用</s:if> </td>
    <td>${info.waitTime }</td>
    <td>${info.msg }</td>
    <td><fmt:formatDate value="${info.updateDate }" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
  </tr>
</s:iterator>