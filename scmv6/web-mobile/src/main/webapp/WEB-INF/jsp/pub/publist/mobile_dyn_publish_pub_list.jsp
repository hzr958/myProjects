<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
    if($(".pub_total_count").length > 1){
        $(".pub_total_count:not(:last)").remove();
    }
})
</script>
<input type="hidden" id="pubTotalCount" value="${pubListVO.totalCount}" class="pub_total_count">
<c:if test="${pubListVO.resultList.size()>0}">
  <c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
    <input type="hidden" name="fulltextImagePermission" id="fulltextImagePermission" value="${pub.fullTextPermission}">
    <div class="paper" onclick="dyn_select('<iris:des3 code='${pub.pubId }'/>',this)">
      <div class="paper_content-container_list-avator" style="float: left;">
        <c:if test="${pub.hasFulltext == 1}">
          <img src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'" style="border-radius: 0px; width: 40px;"> 
        </c:if>
        <c:if test='${pub.hasFulltext != 1}'>
          <img src="${resmod }/images_v5/images2016/file_img.jpg" style="border-radius: 0px; width: 40px;">
        </c:if>
      </div>
      <div class="paper_cont" style="margin-left: 52px !important;" href="javascript:void(0);">
        <p>
          <a class="pubTitle"> 
               ${pub.title}
               <c:if test="${pub.isAnyUser eq 4 }">
                  <i class = "selected-func_closed"></i>
               </c:if>
          </a>
        </p>
        <p class="p1">${pub.authorNames}</p>
        <p class="p3 f999">
          <span>${pub.briefDesc }</span><span class="fccc"><c:if test="${not empty pub.publishYear && pubQueryModel.fromPage != 'dyn'}"> | ${pub.publishYear}</c:if></span>
        </p>
      </div>
    </div>
    <c:if test="${stat.last}">
      <input class="dev_nextIdEx" name="nextIdEx" type="hidden" value="<iris:des3 code='${pub.pubId }'/>">
    </c:if>
  </c:forEach>
</c:if>
<c:if test="${pubListVO.resultList.size()<1}">
  <div class="response_no-result">未找到符合条件的记录</div>
</c:if>
