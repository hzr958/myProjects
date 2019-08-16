<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" id="locale" value="${locale}" />
<input type="hidden" id="totalCount" value="${fn:length(commentList)}" />
<c:if test="${ !empty commentList && fn:length(commentList) > 0}">
  <c:forEach items="${commentList }" var="comment" varStatus="status">
    <input type="hidden" name="dynReplySize" />
    <div class="dynamic_one">
      <div onclick="Group.openPsnDetail('${comment.des3PersonId }', event);">
        <a class="dynamic_head"><img src="${comment.avatars}"
          onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
        <p>
          <span class="fr">${comment.commentDateForShow}</span>
          <em>${!empty comment.name ? comment.name : (comment.firstName + comment.lastName)}</em>
        </p>
      </div>
      <p class="p2" resId="${comment.commentResId }">${comment.commentContent}
        <c:if test="${comment.commentResId != null && comment.commentResId != ''}">
          <br />
          <a style="color: #2882d8 !important;"
            href="" resId="${comment.des3CommentResId }"><iris:lable zhText="${comment.commentResZhTitle }" enText="${comment.commentResEnTitle }"/></a>
        </c:if>
      </p>
    </div>
  </c:forEach>
</c:if>
