<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty visitInsList}">
  <div class="container__effect-title_box-title">
    <s:text name="psnInfluence.visit.institle" />
  </div>
  <s:iterator value="visitInsList" var="ins" status="status">
    <div class="container__effect-title_box-item">
      <span class="container__effect-title_box-item_title"
        <c:if test="${ins.name.length() >=15}">
                 title="${ins.name}">${ins.name.substring(0,15)}......</span>
            </c:if>
        <c:if test="${ins.name.length() < 15}">
                 >${ins.name}</span>
            </c:if>
        <span class="container__effect-title_box-item_cnt">${ins.num}</span>
    </div>
  </s:iterator>
</c:if>