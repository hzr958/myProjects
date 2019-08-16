<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input value="  " type="hidden" class="hidden_data" count=${page.totalCount } page=${page.pageNo }
  totalpages=${page.totalPages } currentCount="${page.pageNo }">
<tr>
  <th>模板标识</th>
  <th>模板名字</th>
  <th>中文主题</th>
  <th>英文主题</th>
  <th>可用状态</th>
  <th>模板类型ID</th>
  <th>限制状态</th>
  <th>优先级</th>
  <th>创建时间</th>
  <th>操作</th>
</tr>
<s:iterator value="mailTemplateList" var="info" status="s">
  <tr>
    <td>${info.templateCode }</td>
    <td>${info.templateName }</td>
    <td>${info.subject_zh }</td>
    <td>${info.subject_en }</td>
    <td><s:if test="#info.status == 0 ">可用</s:if> <s:if test="#info.status == 1 ">不可用</s:if></td>
    <td>${info.mailTypeId }</td>
    <td><s:if test="#info.limitStatus == 0 ">无限制</s:if> <s:if test="#info.limitStatus == 1 ">每天对一个邮箱发一封</s:if> <s:if
        test="#info.limitStatus == 2 ">每周对一个邮箱发一封</s:if> <s:if test="#info.limitStatus == 3 ">每月对一个邮箱发一封</s:if></td>
    <td>${info.priorLevel }</td>
    <td><fmt:formatDate value="${info.createDate }" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
    <td><a class="open_link" templateCode="${info.templateCode }">操作记录</a></td>
  </tr>
</s:iterator>