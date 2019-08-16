<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
    if($(".pub_total_count").length > 1){
        $(".pub_total_count:not(:last)").remove();
    }
})
</script>
<input type="hidden" id="hasLogin" value="${pubListVO.hasLogin }">
<input type="hidden" id="pubSumFromPubList" value="${pubListVO.resultList.size()}">
<input type="hidden" id="pubTotalCount" value="${pubListVO.totalCount}">
<c:if test="${pubListVO.resultList.size()>0}">
  <c:forEach items="${pubListVO.resultList}" var="pub" varStatus="stat">
    <input type="hidden" name="fulltextImagePermission" id="fulltextImagePermission" value="${pub.fullTextPermission}">
    <input type="hidden" name="canDownload" id="canDownload" value="${pub.canDownloadFulltext}">
    <div class="paper" style="padding:12px 16px 0px 16px;">
      <div class="paper_content-container_list-avator" style="float: left;">
        <c:if test="${pub.canDownloadFulltext == 1}">
          <img  class="paper_content-list_save-avator" onclick="mobile.pub.downloadFTFile('${pub.fullTextDownloadUrl}')" src="${pub.fullTextImgUrl}"
            onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'" />
        </c:if>
        <c:if test="${pub.canDownloadFulltext != 1 && pub.hasFulltext == 1}">
                    <img class="paper_content-list_save-avator"
                     <c:if test="${pubListVO.other }">
                         onclick="mobile.pub.requestFTFile('<iris:des3 code='${pub.pubId }'/>','sns','<iris:des3 code='${pub.ownerPsnId }'/>','true')" 
                     </c:if>
                         src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img1.jpg'"/>
                </c:if>
                <c:if test="${pub.canDownloadFulltext != 1 && pub.hasFulltext != 1}">
                    <img  class="paper_content-list_save-avator"
                      <c:if test="${pubListVO.other }">
                        onclick="mobile.pub.requestFTFile('<iris:des3 code='${pub.pubId }'/>','sns','<iris:des3 code='${pub.ownerPsnId }'/>')" 
                      </c:if> 
                      <c:if test="${!pubListVO.other }">
                         onclick="fullTextUpTips()" 
                     </c:if>
                        src="${pub.fullTextImgUrl}" onerror="this.src='${resmod }/images_v5/images2016/file_img.jpg'"/>
                </c:if>
      </div>
      <div class="paper_cont" style="margin-left: 52px !important" href="javascript:void(0);"
        onclick="opendetail('<iris:des3 code='${pub.pubId }'/>',${stat.index},'${pubQueryModel.nextId}')">
        <p style="max-width: 90%;">
          <a class="pubTitle" style="display:block;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${pub.title} 
          </a>
        </p>
        <p class="page-body_program-detail_item-author">${pub.authorNames}</p>
        <p class="f999  paper_content-container_list-source">
          <span class="paper_content-container_list-source_detail" style="width: auto;max-width: 70%;">${pub.briefDesc }</span><span class="fccc" style="padding-left: 3px;"><c:if test="${not empty pub.publishYear}"> | </c:if>${pub.publishYear}</span>
        </p>
      </div>
      
 <%--社交操作start --%>
        <c:set var="isAward" value="${pub.isAward}"></c:set>
        <c:set var="resDes3Id" ><iris:des3 code='${pub.pubId }'/></c:set>
        <c:set var="awardCount" value="${pub.awardCount}"></c:set>
        <c:set var="commentCount" value="${pub.commentCount}"></c:set>
        <c:set var="shareCount" value="${pub.shareCount}"></c:set>
        <c:set var="showCollection" value="0"></c:set>
        <c:set var="currentPage" value="${pubQueryModel.nextId}"></c:set>  
        <c:set var="currentIndex" value="${stat.index}"></c:set>        
        <%@ include file="/common/standard_function_bar.jsp" %>
 <%--社交操作 end--%>
 </div>      
      <%-- <c:if test="${pubListVO.hasLogin == 0 }">
        <div class="paper-footer_func-tool" style="margin-left: 44px;width: 83%;">
          <c:if test="${pub.isAward == 0}">
            <div class="paper-footer_func-tool_item" isAward="${pub.isAward}" onclick="loginToScmOutside();">
              <i class="paper_footer-tool paper_footer-fabulous"></i> <span style="padding-top: 5px;">赞<c:if
                  test="${pub.awardCount > 0}">(${pub.awardCount})</c:if></span>
            </div>
          </c:if>
          <c:if test="${pub.isAward == 1}">
            <div class="paper-footer_func-tool_item" isAward="${pub.isAward}" onclick="loginToScmOutside();">
              <i class="paper_footer-tool paper_footer-award_unlike"></i> <span style="padding-top: 5px;">取消赞<c:if
                  test="${pub.awardCount > 0}">(${pub.awardCount})</c:if></span>
            </div>
          </c:if>
          <div class="paper-footer_func-tool_item" onclick="loginToScmOutside();">
            <i class="paper_footer-tool paper_footer-comment2" style="background-position: -30px -291px;"></i> <a
              style="padding-top: 2px;">评论<c:if test="${pub.commentCount>0}">(${pub.commentCount})</c:if></a>
          </div>
          <div class="paper-footer_func-tool_item" onclick="loginToScmOutside();">
            <i class="paper_footer-tool paper_footer-share"></i> <a style="padding-top: 3px;">分享<span
              class="shareCountText" des3PubId="<iris:des3 code='${pub.pubId }'/>"><c:if
                  test="${pub.shareCount > 0}">(${pub.shareCount})</c:if></span></a>
          </div>
        </div>
    </div>
</c:if>
<c:if test="${pubListVO.hasLogin == 1 }">
  <div class="paper-footer_func-tool" style="margin-left: 55px;">
    <c:if test="${pub.isAward == 0}">
      <div class="paper-footer_func-tool_item" isAward="${pub.isAward}"
        onclick="mobile.snspub.awardopt('<iris:des3 code='${pub.pubId }'/>',this);">
        <i class="paper_footer-tool paper_footer-fabulous"></i> <span style="padding-top: 5px;">赞<c:if
            test="${pub.awardCount > 0}">(${pub.awardCount})</c:if></span>
      </div>
    </c:if>
    <c:if test="${pub.isAward == 1}">
      <div class="paper-footer_func-tool_item" isAward="${pub.isAward}"
        onclick="mobile.snspub.awardopt('<iris:des3 code='${pub.pubId }'/>',this);">
        <i class="paper_footer-tool paper_footer-award_unlike"></i> <span style="padding-top: 5px;">取消赞<c:if
            test="${pub.awardCount > 0}">(${pub.awardCount})</c:if></span>
      </div>
    </c:if>
    <div class="paper-footer_func-tool_item" 
       onclick="opendetail('<iris:des3 code='${pub.pubId }'/>',${stat.index},'${pubQueryModel.nextId}');">
      <i class="paper_footer-tool paper_footer-comment2" style="background-position: -30px -291px;"></i> <a
        style="padding-top: 2px;">评论<c:if test="${pub.commentCount>0}">(${pub.commentCount})</c:if></a>
    </div>
    <div class="paper-footer_func-tool_item" onclick="mobile.pub.sharePubBox('<iris:des3 code='${pub.pubId }'/>');">
      <i class="paper_footer-tool paper_footer-share"></i> <a style="padding-top: 3px;">分享<span
        class="shareCountText" des3PubId="<iris:des3 code='${pub.pubId }'/>"><c:if test="${pub.shareCount > 0}">(${pub.shareCount})</c:if></span></a>
    </div>
  </div>
  </div>
</c:if> --%>

<c:if test="${stat.last}">
  <input class="dev_nextIdEx" name="nextIdEx" type="hidden" value="<iris:des3 code='${pub.pubId }'/>">
</c:if>
</c:forEach>
</c:if>
<c:if test="${pubListVO.resultList.size()<1}">
  <div class="response_no-result" style="padding-bottom: 19%;">未找到符合条件的记录</div>
</c:if>
