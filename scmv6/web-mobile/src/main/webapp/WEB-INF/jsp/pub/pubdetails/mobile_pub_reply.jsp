<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script src="${resdyn}/dynamic.mobile.detail.js?version=20181013" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
    $(".commentTotalPages:not(:last)").remove();
    $(".commentTotalCount:not(:last)").remove();
});
</script>
<input type="hidden" class="commentTotalPages" value="${replyInfoList.page.totalPages }" />
<input type="hidden" class="commentTotalCount" value="${replyInfoList.page.totalCount }" />
<c:forEach items="${replyInfoList.page.result}" var="replay">
  <input name="dynReplySize" type="hidden" />
  <div class="dynamic_one mt15" onclick="dynamic.openPsnDetail('${replay.des3PsnId}',event)">
    <a href="#" class="dynamic_head"><img src="${replay.psnAvatars}"></a>
    <p>
      <span class="fr">${replay.dateTimes}</span><em>${replay.psnName}</em>
    </p>
    <p class="p2">${replay.commentsContent}</p>
  </div>
</c:forEach>
