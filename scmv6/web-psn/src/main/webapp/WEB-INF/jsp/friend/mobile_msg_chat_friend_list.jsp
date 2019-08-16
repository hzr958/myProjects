<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!--列表循环 start  -->
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="psnInfoList" var="msil" status="st">
  <div class="message-page__body-container main-list__item" style="padding: 8px !important;"
    code="<iris:des3 code="${msil.psnId}"/>" onclick="midmsg.chatSeachPsnEvent(event)">
    <c:if test='${locale == "en_US" }'>
      <input type="hidden" value="${msil.enName}" name="chatName" />
    </c:if>
    <c:if test='${locale == "zh_CN" }'>
      <input type="hidden" value="${msil.zhName}" name="chatName" />
    </c:if>
    <div class="message-page__body-container__infor">
      <img src="${msil.avatarUrl}" class="message-page__body-container__avator">
      <div class="message-page__body-container__psn" style="width: 73%;">
        <div class="message-page__body-name__box">
          <span class="message-page__body-container__name"> <c:if test='${locale == "en_US" }'>
                      					${msil.enName}
                      				</c:if> <c:if test='${locale == "zh_CN" }'>
                      				${msil.zhName}
                      				</c:if>
          </span>
        </div>
        <span class="message-page__body-container__work">${msil.insName}</span> <span
          class="message-page__body-container__time"></span>
      </div>
      <i class="material-icons">keyboard_arrow_right</i>
    </div>
  </div>
</s:iterator>