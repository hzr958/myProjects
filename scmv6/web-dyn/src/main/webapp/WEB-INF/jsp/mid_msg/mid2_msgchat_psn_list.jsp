<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<!--站内信会话列表  -->
<s:iterator value="msgShowInfoList" var="msil" status="st">
  <div class="message-page__body-container main-list__item" style=" padding: 16px 16px 16px 20px; width: 120%; position: relative; border-bottom: 1px solid #ddd;"
    readcount="${msil.msgChatReadCount}" count="${msil.msgChatCount}" code="<iris:des3 code='${msil.senderId}'/>"
    des3MsgChatRelationId="<iris:des3 code='${msil.msgChatRelationId}'/>">
    <c:if test="${locale == 'en_US' }">
      <input type="hidden" value="${msil.senderEnName}" name="chatName" />
    </c:if>
    <c:if test="${locale == 'zh_CN' }">
      <input type="hidden" value="${msil.senderZhName}" name="chatName" />
    </c:if>
    <div class="message-page__body-container__infor"
      style="justify-content: flex-start; width: 85%; position: relative;" onclick="midmsg.chatPsnEvent(event)">
      <img src="${msil.senderAvatars}" class="message-page__body-container__avator">
      <div class="message-page__body-container__psn">
        <div class="message-page__body-name__box">
          <c:if test="${locale == 'en_US' }">
            <span class="message-page__body-container__name">${msil.senderEnName}</span>
          </c:if>
          <c:if test="${locale == 'zh_CN' }">
            <span class="message-page__body-container__name">${msil.senderZhName}</span>
          </c:if>
        </div>
        <span class="message-page__body-container__work">${msil.contentNewest}</span>
      </div>
      <span class="message-page__body-container__time" style="position: absolute; top: 7px; right: 12vw;">${msil.showDate }</span>
      <c:if test="${msil.msgChatCount>0}">
        <span class="message-page__tip-message__num" style="position: absolute; top: 0px; left: 30px;">${msil.msgChatCount}</span>
      </c:if>
    </div>
    <div class="dev_item-delete_tip" onclick="midmsg.delchatpsnmove(event)">
      <!-- <i class="material-icons icon setting-list_page-item_hidden" style="color: #fff;">chevron_left</i> -->
      <div style="font-size: 16px; color: #fff;margin:0 auto">删除</div>
    </div>
  </div>
</s:iterator>