<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!--列表循环 start  -->
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="page.result" var="msil" status="st">
  <li class="main-list__item dev_no_chat" code="<iris:des3 code="${msil.psnId}"/>"
    onclick="MsgBase.ChatListClick(event,this)">
    <div class="main-list__item_content" style="margin: 0px;">
      <div class="inbox-messenger__item">
        <div class="inbox-messenger__avatar-box">
          <div class="inbox-messenger__avatar">
            <img src="${msil.avatars}">
          </div>
        </div>
        <div class="inbox-messenger__info">
          <div class="inbox-messenger__psn-info">
            <div class="inbox-messenger__psn-name dev_filter_class">${msil.name}</div>
            <div class="inbox-messenger__operations">
              <div class="inbox-messenger__time"></div>
            </div>
          </div>
          <div class="inbox-messenger__newest-record">${msil.insName}</div>
        </div>
      </div>
    </div>
  </li>
</s:iterator>
