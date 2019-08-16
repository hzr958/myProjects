<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input value="<c:forEach items="${resultMap}" var="item">${item.key}:${item.value}次; </c:forEach>" type="hidden" class="hidden_data" count=${page.totalCount } page=${page.pageNo }
  totalpages=${page.totalPages } currentCount="${page.pageNo }">
<tr>
  <th width="15%">账号</th>
  <th width="4%">端口号</th>
  <th>优先发送的模板</th>
  <th width="25%">优先发送的邮箱</th>
  <th>优先发送的客户端</th>
  <th width="4%">每天最大发送数</th>
  <th width="4%">今日发送统计</th>
  <th>状态</th>
  <th>描述</th>
  <th>更新时间</th>
</tr>
<s:iterator value="mailSenderList" var="info" status="s">
  <tr>
    <td style="word-wrap: break-word">${info.account }</td>
    <td>${info.port }</td>
    <td style="word-wrap: break-word">${info.priorTemplateCode }</td>
    <td style="word-wrap: break-word">${info.priorEmail }</td>
    <td>${info.priorClient }</td>
    <td>${info.maxMailCount }</td>
    <td>${info.todayMailCount }</td>
    <td><s:if test="#info.status == 0 ">可用</s:if> <s:if test="#info.status == 1 ">不可用</s:if> <s:if
        test="#info.status == 9 ">超过发送限制</s:if> <s:if test="#info.status == 88 ">管理员通知账号</s:if></td>
    <td>${info.msg }</td>
    <td><fmt:formatDate value="${info.updateDate }" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
  </tr>
</s:iterator>