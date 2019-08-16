<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<tr>
  <th>url</th>
  <th>描述</th>
  <th>已访问数量</th>
</tr>
<s:iterator value="mailLinkList" var="info" status="s">
  <tr>
    <td class="text_overflow" style="max-width: 300px" title="${info.url }">${info.url }</td>
    <td class="text_overflow" style="max-width: 300px" title="${info.urlDesc }">${info.urlDesc }</td>
    <td>${info.count }</td>
  </tr>
</s:iterator>