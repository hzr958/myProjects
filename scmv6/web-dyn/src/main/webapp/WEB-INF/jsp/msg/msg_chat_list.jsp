<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!--列表循环 start  -->
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="msgShowInfoList" var="msil" status="st">
  <li class="main-list__item " code="<iris:des3 code="${msil.senderId}"/>"
    onclick="MsgBase.getChatPsnListEvent('',this);MsgBase.setChatReadMsg('<iris:des3 code="${msil.senderId}"/>');MsgBase.updateChatPsnLastRecode(this);">
    <div class="main-list__item_content" style="margin: 0px !important;">
      <div class="inbox-messenger__item <c:if test='${msil.msgChatCount>0}'> has-unread-messages</c:if> ">
        <div class="inbox-messenger__avatar-box">
          <div class="inbox-messenger__avatar">
            <img src="${msil.senderAvatars}">
          </div>
          <div class="inbox-messenger__unread-count"></div>
        </div>
        <div class="inbox-messenger__info">
          <div class="inbox-messenger__psn-info">
            <c:if test="${locale == 'en_US' }">
              <div class="inbox-messenger__psn-name dev_filter_class" filter_name="${msil.senderEnName}">${msil.senderEnName}</div>
            </c:if>
            <c:if test="${locale == 'zh_CN' }">
              <div class="inbox-messenger__psn-name dev_filter_class" filter_name="${msil.senderZhName}">${msil.senderZhName}</div>
            </c:if>
            <div class="inbox-messenger__operations">
              <div class="inbox-messenger__time">${msil.showDate }</div>
              <div class="inbox-messenger__settings material-icons"
                onclick="MsgBase.delMsgChatRelation(${msil.msgChatRelationId},event)">delete</div>
            </div>
          </div>
          <div class="inbox-messenger__newest-record">${msil.contentNewest}</div>
        </div>
      </div>
    </div>
  </li>
</s:iterator>
