<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<input type='hidden' id="totalPages" value='${page.totalPages}' />
<input type='hidden' id="curPage" value='${page.pageNo}' />
<c:if test="${page.totalCount>0 && page.pageNo==1}">
  <h2 class="wdful_comments wdful__comments-title" style="font-size: 16px; font-weight: 100; padding: 14px 0px 0px 6px;">精彩评论</h2>
</c:if>
<c:forEach items="${commentList}" var="replay">
  <div class="dynamic_one mt15 main-list__item" onclick="window.open('${replay.psnIndexUrl}','_self')">
    <a href="#" class="dynamic_head"><img src="${replay.avatars}"></a>
    <p>
      <span class="fr" style="color:black;">${replay.rebuildTime}</span><em>${replay.name}</em>
    </p>
    <p class="p2">${replay.commentsContent}</p>
  </div>
</c:forEach>
