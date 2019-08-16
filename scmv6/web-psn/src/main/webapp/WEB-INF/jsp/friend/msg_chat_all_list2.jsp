<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!--列表循环 start  -->
<s:iterator value="page.result" var="msil" status="st">
  <li class="ac__item dev_all_psn" onclick="MsgBase.ChatPsnListEvent(this,event)"
    code="<iris:des3 code="${msil.psnId}"/>">${msil.name}</li>
</s:iterator>
