<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${pubListVO.totalCount}"></div>
<c:forEach items="${pubListVO.resultList }" var="pubInfo">
  <div class="main-list__item">
    <div class="body_content-item main-list__item_content">
      <div class="body_content-item_selector" des3PubId="${pubInfo.des3PubId }">
        <i class="body_content-item_selector-tip"></i>
      </div>
      <div class="body_content-item_detail">
        <div class="body_content-item_title" title="${pubInfo.title }">
          <a href="${pubInfo.pubIndexUrl}" onclick="Pub.stopBubble();" target="_blank">${pubInfo.title }</a>
        </div>
        <div class="body_content-item_author" title="${pubInfo.authorNames}">${pubInfo.authorNames}</div>
        <div class="body_content-item_time" title="${pubInfo.briefDesc}">${pubInfo.briefDesc}</div>
      </div>
    </div>
  </div>
</c:forEach>