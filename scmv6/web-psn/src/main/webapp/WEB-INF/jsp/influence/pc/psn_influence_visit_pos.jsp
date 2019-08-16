<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty visitPosList}">
  <div class="container__effect-title_box-title">
    <s:text name="psnInfluence.visit.postitle" />
  </div>
  <s:iterator value="visitPosList" var="pos" status="status">
    <div class="container__effect-title_box-item">
      <span class="container__effect-title_box-item_title">${pos.name }</span> <span
        class="container__effect-title_box-item_cnt">${pos.num }</span>
    </div>
  </s:iterator>
</c:if>
