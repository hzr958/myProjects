<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
    if($(".pub_total_count").length > 1){
        $(".pub_total_count:not(:last)").remove();
    }
})
</script>
<input type='hidden' id="totalPages" value='${pubListVO.page.totalPages}' />
<input type='hidden' id="curPage" value='${pubListVO.page.pageNo}' />
<div class="js_listinfo pub_total_count" smate_count="${pubListVO.page.totalCount}"></div>
<c:if test="${fn:length(pubListVO.resultList) > 0}">
  <c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
    <div class="paper main-list__item " style="display: flex;" des3PubId="${pub.des3PubId}"
      onclick="midmsg.sendpubmsg(event)">
      <c:if test="${not empty pub.fullTextFieId }">
        <div class="paper_content-container_list-avator"
          style="float: left; width: 40px; height: 50px; padding: 2px 0px;">
          <img src="${pub.fullTextImgUrl }" onerror="this.src='${ resmod}/images_v5/images2016/file_img1.jpg'"
            style="width: 100%; height: 100%;  border-radius: 0px;">
        </div>
      </c:if>
      <c:if test="${empty pub.fullTextFieId }">
        <div class="paper_content-container_list-avator"
          style="float: left; width: 40px; height: 50px; padding: 2px 0px;">
          <img src="/resmod/images_v5/images2016/file_img.jpg" style="width: 100%; height: 100%; border-radius: 0px;">
        </div>
      </c:if>
      <div class="paper_cont" style="margin-left: 12px; height: auto; width: 75vw;">
        <div class="pubTitle webkit-multipleline-ellipsis" href="javascript:void(0);" style="margin-bottom: 0px;">${pub.title}</div>
        <div class="p1" style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${pub.authorNames}</div>
        <div class="p3 f999" style="color: #999;">${pub.briefDesc}</div>
      </div>
    </div>
  </c:forEach>
</c:if>
