<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input value="  " type="hidden" class="hidden_data" count=${page.totalCount } page=${page.pageNo }
  totalpages=${page.totalPages } currentCount="${page.pageNo }">
<tr>
  <th>序号</th>
  <th>邮箱</th>
  <th>状态</th>
</tr>
<s:iterator value="mailWhitelist" var="info" status="s">
  <tr>
    <td>${info.id }</td>
    <td>${info.email }</td>
    <td><s:if test="#info.status == 0 ">开启</s:if> <s:if test="#info.status == 1 ">关闭</s:if> </td>
  </tr>
</s:iterator>