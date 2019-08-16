<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<!--站内信会话列表  -->
<s:iterator value="msgShowInfoList" var="msil" status="st">
  <div class="message-page__body-container main-list__item" style="padding: 12px !important;"
    readcount="${msil.msgChatReadCount}" count="${msil.msgChatCount}" code="<iris:des3 code='${msil.senderId}'/>"
    des3MsgChatRelationId="<iris:des3 code='${msil.msgChatRelationId}'/>"
    onclick="midmsg.chatSearchPsnEvent('${searchKey}',event)">
    <c:if test="${locale == 'en_US' }">
      <input type="hidden" value="${msil.senderEnName}" name="chatName" />
    </c:if>
    <c:if test="${locale == 'zh_CN' }">
      <input type="hidden" value="${msil.senderZhName}" name="chatName" />
    </c:if>
    <div class="message-page__body-container__infor">
      <img src="${msil.senderAvatars}" class="message-page__body-container__avator">
      <div class="message-page__body-container__psn" style="width: 73%;">
        <div class="message-page__body-name__box">
          <c:if test="${locale == 'en_US' }">
            <span class="message-page__body-container__name">${msil.senderEnName}</span>
          </c:if>
          <c:if test="${locale == 'zh_CN' }">
            <span class="message-page__body-container__name">${msil.senderZhName}</span>
          </c:if>
        </div>
        <span class="message-page__body-container__work">${msil.contentNewest}</span> <span
          class="message-page__body-container__time"></span>
      </div>
      <i class="material-icons">keyboard_arrow_right</i>
    </div>
  </div>
</s:iterator>