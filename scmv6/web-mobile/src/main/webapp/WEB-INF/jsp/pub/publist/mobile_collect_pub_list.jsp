<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${pubListVO.page.totalCount}'></div>
<input type='hidden' id="totalPages" value='${pubListVO.page.totalPages}' />
<input type='hidden' id="curPage" value='${pubListVO.page.pageNo}' />
<c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
  <div class="paper main-list__item" style="padding: 12px 8px; border-bottom: 1px dashed #ddd;">
    <!-- 成果图片 -->
    <div class="paper_content-container_list-avator" style="float: left;">
      <c:if test="${not empty pub.hasFulltext && pub.hasFulltext == 1 }">
        <c:if test="${pub.pubDb == 'SNS' && pub.canDownloadFulltext == 1}">
          <div onclick="mobile.pub.downloadFTFile('${pub.fullTextDownloadUrl}');">
        </c:if>
        <c:if test="${pub.pubDb == 'SNS' && pub.canDownloadFulltext == 0}">
          <div onclick="mobile.pub.requestFTFile('${pub.des3PubId}', '${pub.pubDb }', '${pub.ownerPsnId }','true');">
        </c:if>
        <c:if test="${pub.pubDb == 'PDWH' }">
          <div  onclick="mobile.pub.downloadFTFile('${pub.fullTextDownloadUrl}')">
        </c:if>
        <img class="paper_content-list_save-avator" src="${pub.fullTextImgUrl}" onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
        </div>
      </c:if>
    <c:if test="${empty pub.hasFulltext || pub.hasFulltext == 0}">
      <img class="paper_content-list_save-avator" src="${pub.fullTextImgUrl}" onerror="this.src='${resmod}/images_v5/images2016/file_img.jpg'"
        onclick="mobile.pub.requestFTFile('${pub.des3PubId}', '${pub.pubDb }', '${pub.ownerPsnId }');">
    </c:if>
  </div>
  <!-- 成果标题、作者、来源 -->
  <c:if test="${pub.pubDb == 'SNS' }">
    <div class="paper_cont" style="margin-left: 52px !important"
      onclick="mobile.snspub.details('${pub.des3PubId}', 'single');">
  </c:if>
  <c:if test="${pub.pubDb == 'PDWH' }">
    <div class="paper_cont" style="margin-left: 52px !important" onclick="mobile.pub.pdwhDetails('${pub.des3PubId}');">
  </c:if>
  <p class="webkit-multipleline-ellipsis" style="text-align: left;">${pub.title}</p>
  <c:if test="${pub.authorNames != null && pub.authorNames != 'null'}">
    <p class="p3" style="text-align: left;">${pub.authorNames}</p>
  </c:if>
  <p class="p3 f999" style="text-align: left;">
    <span style="width: auto;max-width: 100%;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;">${pub.briefDesc}</span>
  </p>
  </div>
  
     <%--社交操作start --%>
        <c:set var="isAward" value="${pub.isAward}"></c:set>
        <c:set var="resDes3Id" value="${pub.des3PubId}"></c:set>
        <c:set var="awardCount" value="${pub.awardCount}"></c:set>
        <c:set var="commentCount" value="${pub.commentCount}"></c:set>
        <c:set var="shareCount" value="${pub.shareCount}"></c:set>
        <c:set var="isCollection" value="1"></c:set>
        <c:set var="pubDb" value="${pub.pubDb}"></c:set>
        <%@ include file="/common/standard_function_bar.jsp" %>
   <%--社交操作 end--%>
  
  <!-- 社交操作 -->
 <%--  <div class="paper_footer" style="margin-left: 0px !important;margin-top: 8px;width: 93%;">
    <c:if test="${pub.pubDb == 'PDWH' }">
      <span class="paper_footer-tool__box" isAward="${pub.isAward}"
        onclick="mobile.pub.pdwhAwardOpt('${pub.des3PubId}',this)">
    </c:if>
    <c:if test="${pub.pubDb == 'SNS' }">
      <span class="paper_footer-tool__box" isAward="${pub.isAward}"
        onclick="mobile.snspub.awardopt('${pub.des3PubId}',this)">
    </c:if>
    <c:if test="${pub.isAward == 1 }">
      <i class="paper_footer-tool paper_footer-award_unlike"></i>
      <span class="dev_pub_award dev_cancel_award">取消赞 <c:if test="${pub.awardCount > 0 }">
                     (${pub.awardCount})
                 </c:if>
      </span>
    </c:if>
    <c:if test="${pub.isAward == 0}">
      <i class="paper_footer-tool paper_footer-fabulous"></i>
      <span class="dev_pub_award ">赞 <c:if test="${pub.awardCount > 0}">
                     (${pub.awardCount})
                 </c:if>
      </span>
    </c:if>
    </span>
    <c:if test="${pub.pubDb == 'SNS' }">
      <span class="paper_footer-tool__box paper_footer-tool__pos"
        onclick="mobile.snspub.details('${pub.des3PubId}', 'single');">
    </c:if>
    <c:if test="${pub.pubDb == 'PDWH' }">
      <span class="paper_footer-tool__box paper_footer-tool__pos" onclick="mobile.pub.pdwhDetails('${pub.des3PubId}')">
    </c:if>
    <i class="paper_footer-tool paper_footer-comment2"></i> <span>评论 <c:if test="${pub.commentCount  > 0 }">
                        (${pub.commentCount})
                </c:if>
    </span> </span> <span class="paper_footer-tool__box paper_footer-tool__pos"> <i
      class="paper_footer-tool paper_footer-share"></i> <span onclick="sharePub('${pub.des3PubId}','${pub.pubDb }')">分享
        <span class="shareCountText" des3PubId="${pub.des3PubId }"> <c:if test="${pub.shareCount > 0 }">
                     (${pub.shareCount})
             </c:if>
      </span>
    </span>
    </span> <span class="paper_footer-tool__box paper_footer-tool__pos" des3PubId="${pub.des3PubId}" pubDb="${pub.pubDb }"
      needRefresh="true" onclick="mobile.snspub.collect('${pub.des3PubId}', 1, this);"> <i
      class="paper_footer-tool paper_footer-comment__flag"></i> <span>取消收藏</span>
    </span>
  </div>
   --%>
  
  </div>
</c:forEach>