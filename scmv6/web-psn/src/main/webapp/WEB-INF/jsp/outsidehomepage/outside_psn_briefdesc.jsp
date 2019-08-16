<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="psnBriefDesc" id="psnBriefDesc" value="${psnBriefDesc }" />
<c:if test="${not empty psnBriefDesc }">
  <div class="module-card__box">
    <div class="module-card__header">
      <div class="module-card-header__title">
        <s:text name="briefDescModule.header_title" />
      </div>
    </div>
    <div class="global__padding_16">
      <div class="global__para_body" id="psnBriefVal" style="white-space: pre-wrap;">${psnBriefDesc }</div>
    </div>
  </div>
</c:if>
