<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input value=" " type="hidden" class="hidden_data" count=${page.totalCount } page=${page.pageNo }
  totalpages=${page.totalPages } currentCount="${page.pageNo }">
<tr>
  <th>统计日期(前一天的数据)</th>
  <th>产生的邮件总数</th>
  <th>发送成功数</th>
  <th>发送出错数</th>
  <th>收件箱不存在数</th>
  <th>黑名单数</th>
  <th>待构造数</th>
  <th>构造出错数</th>
  <th>待分配数</th>
  <th>待发送数</th>
  <th>调度出错数</th>
  <th>拒绝接收数</th>
  <th>模版发送频率限制数</th>
  <th>收发件人首要邮箱一致数</th>
</tr>
<s:iterator value="mailStatisticList" var="info" status="s">
  <tr>
    <td><fmt:formatDate value="${info.createDate }" pattern="yyyy/MM/dd" /></td>
    <td style="word-wrap: break-word">${info.totalCount }</td>
    <td>${info.sendSuccess }</td>
    <td style="word-wrap: break-word">${info.sendError }</td>
    <td style="word-wrap: break-word">${info.receiverInexistence }</td>
    <td>${info.blacklist }</td>
    <td>${info.toBeConstruct }</td>
    <td>${info.constructError }</td>
    <td>${info.toBeDistributed }</td>
    <td>${info.toBeSend }</td>
    <td>${info.mailDispatchError }</td>
    <td>${info.refuseReceive }</td>
    <td>${info.templateTimeLimit }</td>
    <td>${info.firstEmailSame }</td>
  </tr>
</s:iterator>