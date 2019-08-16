<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:iterator value="pubInfos" id="result" status="index">
  <c:set var="authors" value="${result.authors}"></c:set>
  <div class="new__resume-list__content-title new__resume-list__content-sub"
    style="display: flex; align-items: baseline;">
    <s:if test="#index.index < 9 ">
      <div style="width: 26px; min-width: 28px; line-height: 22px;">
    </s:if>
    <s:else>
      <div style="width: 26px; min-width: 36px; line-height: 22px;">
    </s:else>
    (
    <s:property value='#index.index + 1 ' />
    )&nbsp;
  </div>
  <div>
    <span>${authors }</span>
    <s:if test="authors != null && authors != '' ">, </s:if>
    <a href="${result.pubUrl }" target="_blank">${result.title}</a>${result.source}<s:if
      test="#result.pubType == 1 && #result.awardAuthorList != null">, ${result.awardAuthorList }</s:if>
    &nbsp;&nbsp;&nbsp;&nbsp;<strong>(${pubTypeName })</strong>
  </div>
  </div>
</s:iterator>
