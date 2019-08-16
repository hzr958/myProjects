<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!--列表循环 start  -->
<s:iterator value="psnInfoList" var="msil" status="st">
  <li class="ac__item" onclick="MsgBase.ChatPsnListEvent(this,event)" code="<iris:des3 code="${msil.psnId}"/>">${msil.zhName}</li>
</s:iterator>