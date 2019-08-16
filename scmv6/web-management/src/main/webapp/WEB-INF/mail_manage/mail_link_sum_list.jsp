<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input id="divCode" type="hidden" value=${templateCode }>
<tr>
  <th>模板标识</th>
  <th>模板名</th>
  <th>链接key</th>
  <th>链接描述</th>
  <th>访问次数</th>
</tr>
<s:iterator value="mailLinkList" var="info" status="s">
  <tr>
    <td>${info.templateCode }</td>
    <td>${info.templateName }</td>
    <td>${info.linkKey }</td>
    <td>${info.urlDesc }</td>
    <td>${info.count }</td>
  </tr>
</s:iterator>
