<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:if test="commentList.size > 0">
  <s:iterator value="commentList" var="comment">
    <div class="main-list__item" style="margin-bottom: -10px">
      <div class="main-list__item_content" style="margin: 0px!important; border-top: 1px dashed #ddd;">
        <div class="detail-comment__item">
          <div class="detail-comment__avatar">
            <a href="${psnIndexUrl }" target="_blank"> <img src="${comment.avatars}"
              class="new-program_body-comment_avator" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
            </a>
          </div>
          <div class="detail-comment__main">
            <div class="detail-comment__authorship" style="display: flex; justify-content: space-between;">
              <a href="${psnIndexUrl }" target="_blank" class="detail-comment__author-name" title="${comment.name}" >${comment.name}</a> 
              <span class="detail-comment__post-time">${comment.rebuildTime}</span>
            </div>
            <div class="detail-comment__text" style="width: 100%;">
              <c:out value="${comment.commentsContent}" escapeXml="false"></c:out>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div style="height: 10px;"></div>
  </s:iterator>
</s:if>
