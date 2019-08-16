<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${pubVO.page.totalCount}'></div>
<input type='hidden' id="totalPages" value='${pubVO.page.totalPages}' />
<input type='hidden' id="curPage" value='${pubVO.page.pageNo}' />
<c:forEach items="${pubVO.page.result }" var="pubInfo">
  <div class="paper main-list__item" style="padding: 12px 8px; border-bottom: 1px dashed #ddd;">
    <!-- 成果图片 -->
    <div class="paper_content-container_list-avator" style="float: left;">
      <c:if test="${pubInfo.hasFulltext == 1}">
        <img class="paper_content-list_save-avator"  onclick="mobile.pub.downloadFTFile('${pubInfo.fullTextDownloadUrl}')" name='fullImg'
          des3PubId='${pubInfo.des3PubId}' src="${empty pubInfo.fullTextImgUrl ? false : pubInfo.fullTextImgUrl}"
          onerror="this.src='${resmod}/images_v5/images2016/file_img1.jpg'">
      </c:if>
      <c:if test="${pubInfo.hasFulltext == 0}">
        <img  class="paper_content-list_save-avator" name='fullImg' des3PubId='${pubInfo.des3PubId}' src="${resmod}/images_v5/images2016/file_img.jpg"
          onclick="mobile.pub.requestFTFile('${pubInfo.des3PubId}', 'pdwh', '');">
      </c:if>
    </div>
    <!-- 成果标题、作者、来源 -->
    <div class="paper_cont" style="margin-left: 52px !important"
      onclick="mobile.pub.pdwhDetails('${pubInfo.des3PubId}');">
      <p class="webkit-multipleline-ellipsis" style="text-align: left;">${pubInfo.title}</p>
      <p class="p3" style="text-align: left;">
        ${pubInfo.authorNames}
      </p>
      <p class="p3 f999" style="text-align: left;">
        <span style="width: auto;max-width: 100%;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;"><c:out value="${pubInfo.briefDesc}" /></span>
      </p>
    </div>
     <%--社交操作start --%>
        <c:set var="isAward" value="${pubInfo.isAward}"></c:set>
        <c:set var="resDes3Id" value="${pubInfo.des3PubId}"></c:set>
        <c:set var="awardCount" value="${pubInfo.awardCount}"></c:set>
        <c:set var="commentCount" value="${pubInfo.commentCount}"></c:set>
        <c:set var="shareCount" value="${pubInfo.shareCount}"></c:set>
        <c:set var="isCollection" value="${pubInfo.isCollected }"></c:set>
        <c:set var="resType" value="22"></c:set>
        <c:set var="pubDb" value="PDWH"></c:set>
        <%@ include file="/common/standard_function_bar.jsp" %>
   <%--社交操作 end--%>
   
    <!-- 社交操作 -->
<%--     <div class="paper_footer" style="margin-left: 0px !important;margin-top: 8px;width: 93%;">
      <span class="paper_footer-tool__box" isAward="${pubInfo.isAward}"
        onclick="mobile.pub.pdwhAwardOpt('${pubInfo.des3PubId}',this)"> <c:if test="${pubInfo.isAward == 1}">
          <i class="paper_footer-tool paper_footer-award_unlike"></i>
          <span class="dev_pub_award ">取消赞 <c:if test="${pubInfo.awardCount > 0}">
                     (${pubInfo.awardCount})
                    </c:if>
          </span>
        </c:if> <c:if test="${pubInfo.isAward == 0}">
          <i class="paper_footer-tool paper_footer-fabulous"></i>
          <span class="dev_pub_award">赞 <c:if test="${pubInfo.awardCount > 0}">
                     (${pubInfo.awardCount})
                    </c:if>
          </span>
        </c:if>
      </span>
      <span class="paper_footer-tool__box paper_footer-tool__pos" onclick="mobile.pub.pdwhDetails('${pubInfo.des3PubId}');">
                 <i class="paper_footer-tool paper_footer-comment2"></i>
                 <span>评论
                    <c:if test="${pubInfo.commentCount>0}">
                        (${pubInfo.commentCount})
                    </c:if>           
                 </span>
       </span> 
      <span class="paper_footer-tool__box paper_footer-tool__pos" onclick="sharePub('${pubInfo.des3PubId}')"> <i
        class="paper_footer-tool paper_footer-share"></i> <span>分享 
        <span class="pdwh_pub_share_count shareCountText" des3PubId="${pubInfo.des3PubId }">
          <c:if test="${pubInfo.shareCount>0 }"> 
                     (${pubInfo.shareCount })
          </c:if>
        </span>
      </span>
      </span>
      <c:if test="${pubInfo.isCollected == 0}">
        <span class="paper_footer-tool__box paper_footer-tool__pos" des3PubId="${pubInfo.des3PubId}" pubDb="PDWH"
          onclick="mobile.snspub.collect('${pubInfo.des3PubId}', 0, this);"> <i
          class="paper_footer-tool paper_footer-comment"></i> <span class="collect_pub_content">收藏</span>
        </span>
      </c:if>
      <c:if test="${pubInfo.isCollected == 1}">
        <span class="paper_footer-tool__box paper_footer-tool__pos" des3PubId="${pubInfo.des3PubId}" pubDb="PDWH"
          onclick="mobile.snspub.collect('${pubInfo.des3PubId}', 1, this);"> <i
          class="paper_footer-tool paper_footer-comment__flag"></i> <span class="collect_pub_content">取消收藏</span>
        </span>
      </c:if>
    </div> --%>
    
  </div>
</c:forEach>