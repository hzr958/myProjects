<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!--列表循环 start  -->
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="page.result" var="msil" status="st">
  <div class="message-page__body-container main-list__item" style="padding: 8px !important;"
    code="<iris:des3 code="${msil.psnId}"/>" onclick="midmsg.chatSeachPsnEvent(event)">
    <input type="hidden" value="${msil.name}" name="chatName" />
    <div class="message-page__body-container__infor">
      <img src="${msil.avatars}" class="message-page__body-container__avator">
      <div class="message-page__body-container__psn" style="width: 73%;">
        <div class="message-page__body-name__box">
          <span class="message-page__body-container__name">${msil.name}</span>
        </div>
        <span class="message-page__body-container__work">${msil.insName}</span> <span
          class="message-page__body-container__time"></span>
      </div>
      <i class="material-icons">keyboard_arrow_right</i>
    </div>
  </div>
</s:iterator>
