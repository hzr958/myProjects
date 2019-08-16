
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="page.totalCount > 0">
  <s:iterator value="page.result" id="obj" status="itStat" var="data">
    <div class="message_ask">
      <input type="hidden" value="${data.id}" name="unitId" /> <a class="message_ask_portrait" href="#"><img
        src="${ressie}/images/achievement/icon_record_lt.png" alt=""></a>
      <p class=" fg">
        <a href="#" class="data">${data.zhName}&nbsp;${position}</a>
      </p>
      <%--                <p class="monicker">${data.disciName}<span>${data.ptSum}</span><span>${data.pubSum}</span><span>${data.prjSum}</span><span>${data.psnSum}</span></p> --%>
      <p class="f999">${data.mobile}</p>
    </div>
  </s:iterator>
  <%@include file="/common/page-tags.jsp"%>
</s:if>