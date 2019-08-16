<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input value="<c:forEach items="${resultMap}" var="item">${item.key}:${item.value}条; </c:forEach>" type="hidden"
  class="hidden_data" count=${page.totalCount } page=${page.pageNo } totalpages=${page.totalPages }
  currentCount="${page.pageNo }">
<tr>
  <th>序号</th>
  <th>邮件模版/邮件标题</th>
  <th>收件人ID</th>
  <th>收件人邮箱</th>
  <th>发件人ID</th>
  <th>发件邮箱</th>
  <th>发送时间</th>
  <th>状态</th>
  <!-- <th>触发操作类型</th> -->
  <th>操作</th>
</tr>
<s:iterator value="mailForShowInfoList" var="info" status="s">
  <tr>
    <td>${info.mailId }</td>
    <td style="max-width: 350px">${info.mailTemplateName }</br> <a class="open_details" class="text_overflow"
      title="${info.subject }" des3MailId="${info.des3MailId }">${info.subject }</a></td>
    <td>${info.receiverPsnId }</td>
    <td>${info.receiver }</td>
    <td>${info.senderPsnId }</td>
    <td>${info.sender }</td>
    <td><fmt:formatDate value="${info.updateDate }" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
    <td>${info.statusMsg }</td>
    <!-- <td></td> -->
    <td><a class="open_link" des3MailId="${info.des3MailId }">邮件操作记录</a>&nbsp;&nbsp;<a class="open_template"
      templateCode="${info.templateCode }">模板操作记录</a></td>
  </tr>
</s:iterator>