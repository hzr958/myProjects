<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="psnBriefDesc" id="psnBriefDesc" value="${psnBriefDesc}" />
<c:if test="${not empty psnBriefDesc || isMySelf== 'true'}">
  <div class="module-card__box">
    <div class="module-card__header">
      <div class="module-card-header__title">
        <s:text name='homepage.profile.title.about' />
      </div>
      <button class="button_main button_icon button_light-grey operationBtn" onclick="psnBriefDesc()">
        <i class="material-icons">edit</i>
      </button>
    </div>
    <c:if test="${!empty psnBriefDesc }">
      <div class="global__padding_16">
        <div class="global__para_body" id="psnBriefVal" style="white-space: pre-wrap;">${psnBriefDesc }</div>
      </div>
    </c:if>
  </div>
</c:if>
