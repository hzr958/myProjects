<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:iterator value="groupDynCommentsShowInfoList" var="groupDynShowInfo">
  <div class="dynamic_comment_sample_container"
    onclick="groupDynamic.loadMoreComments('${groupDynShowInfo.dynId }',this)">
    <div onclick='groupDynamic.openPsnDetail("${groupDynCommentsShowInfo.des3PersonId}",event)' style='cursor: pointer;'>
      <img src="${groupDynShowInfo.avatars }" class="avatar">
    </div>
    <div class="comment_content">
      <span class="fw_500"> <s:if test="#groupDynShowInfo.name!=null&&#groupDynShowInfo.name!=''">${groupDynShowInfo.name }</s:if>
        <s:else>${groupDynShowInfo.firstName }${groupDynShowInfo.lastName }</s:else> ：
      </span> ${groupDynShowInfo.commentContent }
    </div>
    <%--  <div class="time">${groupDynShowInfo.commentDateForShow }</div> --%>
  </div>
</s:iterator>