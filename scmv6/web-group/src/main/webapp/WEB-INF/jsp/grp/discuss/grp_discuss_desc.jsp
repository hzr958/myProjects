<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="module-card__box">
  <div class="module-card__header">
    <div class="module-card-header__title">
      <!-- 判断群组类型 显示不一样的效果 -->
      <c:if test="${ grpCategory=='11'}">
        <s:text name='groups.base.curInfo2' />
      </c:if>
      <c:if test="${ grpCategory=='10'}">
        <s:text name='groups.base.curInfo' />
      </c:if>
      <c:if test="${ grpCategory=='12'}">
          群组简介
          </c:if>
    </div>
  </div>
  <div class="global__padding_16">
    <div class="global__para_body">${grpDesc }</div>
  </div>
</div>