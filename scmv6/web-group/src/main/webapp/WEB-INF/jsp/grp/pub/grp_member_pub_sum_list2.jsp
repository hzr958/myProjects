<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${psnInfoList != null && fn:length(psnInfoList) > 0}">
  <s:iterator value="psnInfoList" var="pi" status="st">
    <div class="group-achive_left-item   <s:if test="#st.index==0"> group-achive_left-item_shadow</s:if>"
      des3PsnId='<iris:des3 code="${pi.person.personId}"/>'>
      <img class="group-achive_left-item_avator" src="${pi.person.avatars}"
        onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
      <div class="group-achive_left-item_infor">
        <div class="group-achive_left-item_order" title="${pi.name}">${pi.name}</div>
        <div class="group-achive_left-item_detail">
          <span><s:text name='groups.pub.pubNum' />：</span> <span>${pi.psnStatistics.openPubSum}</span>
        </div>
      </div>
    </div>
  </s:iterator>
</c:if>