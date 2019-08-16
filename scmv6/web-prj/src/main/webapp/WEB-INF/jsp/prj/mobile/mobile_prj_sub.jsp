<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<input type='hidden' id="totalPages" value='${page.totalPages}' />
<input type='hidden' id="curPage" value='${page.pageNo}' />
<input type='hidden' id="hasPrivatePrj" value='${hasPrivatePrj}' />
<s:if test="page.result.size()>0">
  <c:forEach items="${page.result}" var="prj" varStatus="stat">
    <div class="paper main-list__item" style="padding: 12px 16px; border-bottom: 1px dashed #ddd;">
      <!-- 图片 -->
      <!-- 标题、作者、来源 -->
      <div class="paper_cont" style="margin-left: 5px !important;"
        onclick="opendetail('<iris:des3 code='${prj.prjId }'/>');">
        <p class="webkit-multipleline-ellipsis" style="text-align: left;">${prj.title}</p>
        <p class="p3" style="text-align: left;">${prj.authors}</p>
        <p class="p3 f999" style="text-align: left;">
          <em style="max-width: 100%;">${prj.briefDesc }</em>
        </p>
      </div>
 <%--社交操作start --%>
        <c:set var="isAward" value="${prj.award}"></c:set>
        <c:set var="resDes3Id" ><iris:des3 code='${prj.prjId }'/></c:set>
        <c:set var="awardCount" value="${prj.awardCount}"></c:set>
        <c:set var="commentCount" value="${prj.commentCount}"></c:set>
        <c:set var="shareCount" value="${prj.shareCount}"></c:set>
        <c:set var="showCollection" value="0"></c:set>
        <%@ include file="/common/standard_function_bar.jsp" %>
 <%--社交操作 end--%>      
      <!-- 社交操作 -->
<%--       <div class="paper_footer" style="justify-content: space-around; margin-left:20px;">
        <span class="paper_footer-tool__box" isAward="${prj.award}" des3PrjId='<iris:des3 code='${prj.prjId }'/>'
          onclick="Project.award(this)"> <c:if test="${prj.award == 1}">
            <i class="paper_footer-tool paper_footer-fabulous paper_footer-award_unlike"></i>
            <span class="dev_pub_award ">取消赞 <c:if test="${prj.awardCount > 0}">
	                     (${prj.awardCount})
	                    </c:if>
            </span>
          </c:if> <c:if test="${prj.award != 1}">
            <i class="paper_footer-tool paper_footer-fabulous"></i>
            <span class="dev_pub_award">赞 <c:if test="${prj.awardCount > 0}">
	                     (${prj.awardCount})
	                    </c:if>
            </span>
          </c:if>
        </span> <span class="paper_footer-tool__box paper_footer-tool__pos"
          onclick="opendetail('<iris:des3 code='${prj.prjId }'/>');"> <i
          class="paper_footer-tool paper_footer-comment2"></i> <span>评论 <c:if test="${prj.commentCount>0}">
	                        (${prj.commentCount})
	                    </c:if>
        </span>
        </span> <span class="paper_footer-tool__box paper_footer-tool__pos" des3ResId="<iris:des3 code='${prj.prjId }'/>"
          onclick="SmateCommon.mobileShareEntrance('${prj.des3PrjId }','prj');"> <i class="paper_footer-tool paper_footer-share"></i> <span
          shareprjid="<iris:des3 code='${prj.prjId }'/>">分享 <c:if test="${prj.shareCount>0 }"> 
	                   (${prj.shareCount })
	                </c:if>
        </span>
        </span>
      </div> --%>
      
    </div>
  </c:forEach>
</s:if>
