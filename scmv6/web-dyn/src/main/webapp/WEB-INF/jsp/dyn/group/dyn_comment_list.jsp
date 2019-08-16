<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:iterator value="groupDynCommentsShowInfoList" var="groupDynCommentsShowInfo">
  <div class="dynamic_comment_list_section">
    <div onclick='groupDynamic.openPsnDetail("${groupDynCommentsShowInfo.des3PersonId}",event)' style='cursor: pointer;'>
      <img src="${groupDynCommentsShowInfo.avatars }" class="avatar">
    </div>
    <div class="comment_content">
      <div class="name">
        <s:if test="#groupDynCommentsShowInfo.name!=null&&#groupDynCommentsShowInfo.name!=''">${groupDynCommentsShowInfo.name }</s:if>
        <s:else>${groupDynCommentsShowInfo.firstName }${groupDynCommentsShowInfo.lastName }</s:else>
      </div>
      <div>${groupDynCommentsShowInfo.commentContent }</div>
    </div>
    <div class="time">${groupDynCommentsShowInfo.commentDateForShow }</div>
  </div>
</s:iterator>
