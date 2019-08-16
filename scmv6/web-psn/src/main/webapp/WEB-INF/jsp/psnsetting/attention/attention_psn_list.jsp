<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:forEach items="${attPersonInfoList }" var="psn">
  <div class="set-attention__per-box" attPersonId="${psn.attPersonId }">
    <img src="${psn.refHeadUrl }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"
      class="set-attention__avator">
    <div class="set-attention__infor" style="margin-left: 8px;">
      <div class="set-attention__name set-attention__psninfor">
        <a href="/psnweb/homepage/show?des3PsnId=${psn.refDes3PsnId }" target="_blank">${psn.refName }</a>
      </div>
      <div class="set-attention__work set-attention__psninfor">${psn.refTitolo }</div>
    </div>
    <i class="attention-clear" onclick="PsnsettingAttention.canelAttPersonConfirm(this);"></i>
  </div>
</c:forEach>
