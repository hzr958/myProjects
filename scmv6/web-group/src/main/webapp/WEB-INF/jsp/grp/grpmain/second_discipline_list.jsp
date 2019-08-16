<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:iterator value="disciplineList" var="dpl" status="st">
  <div class="sel-dropdown__item" sel-itemvalue="${dpl.secondCategoryId}">${dpl.secondCategoryZhName}</div>
</s:iterator>