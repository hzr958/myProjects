<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
    
});

</script>
<c:if test="${result == null}">
  <div class="js_listinfo" smate_count='0'></div>
</c:if>
<c:if test="${result != null}">
  <div class="js_listinfo" smate_count='${totalPrjCount}'></div>
  <input type='hidden' id="totalPages" value='${totalPages}' />
  <input type='hidden' id="curPage" value='${pageNo}' />
  <input type='hidden' id="hasPrivatePrj" value='${hasPrivatePrj}' />
  <c:forEach items="${result}" var="agency" varStatus="status">
    <div class="new-financial_body-item main-list__item need_init_agency_item agency_show_item"
      style="margin-bottom: 0px; padding:12px 12px 12px 13px;">
      <input type="hidden" class="agency_opt_info" awardStatus="${agency.hasAward}"
        interestStatus="${agency.interested}" awardCount="${agency.awardCount }" shareCount="${agency.shareCount }"
        interestCount="${agency.interestCount }">
      <div class="new-financial_body-item_content">
        <div class="new-financial_body-item_avatar" onclick="opendetail('${agency.des3Id }')">
          <img src="${agency.logoUrl }" onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'"
            class="new-financial_body-item_avatar-detail" />
        </div>
        <div style="display: flex; flex-direction: column; flex-grow: 1;">
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <div class="new-financial_body-item_infor" onclick="opendetail('${agency.des3Id }')">
              <div class="new-financial_body-item_infor-work">${agency.nameView }</div>
              <div class="new-financial_body-item_infor-adddress">${agency.viewAddress }</div>
            </div>
            <div class="new-financial_body-item_Opportunity">
              <c:if test="${not empty agency.fundCount}">
                <span class="new-financial_body-item_Opportunity-detail">${agency.fundCount}</span>
                <span class="new-financial_body-item_Opportunity-detail">可用机会</span>
              </c:if>
            </div>
          </div>
  <%--社交操作start --%>
        <c:set var="isAward" value="${agency.hasAward}"></c:set>
        <c:set var="resDes3Id" >${agency.des3Id }</c:set>
        <c:set var="awardCount" value="${agency.awardCount}"></c:set>
        <c:set var="showComment" value="0"></c:set>
        <c:set var="shareCount" value="${agency.shareCount}"></c:set>
        <c:set var="isCollection" value="${agency.interested}"></c:set>
        <c:set var="collectName" value="关注"></c:set>
         <c:set var="unCollectName" value="取消关注"></c:set>
        
        <%@ include file="/common/standard_function_bar.jsp" %>
 <%--社交操作 end--%>            
<%--           <div class="new-financial_body-item_footer" style="width: 93%;justify-content: space-between;">
            <span class="paper_footer-tool__box agency_award_span"
              onclick="MobileAgency.ajaxAward($(this), '${agency.des3Id }', 1);event.stopPropagation();"
              resId="${agency.des3Id }"> <i class="paper_footer-tool paper_footer-fabulous"></i> <span
              class="paper_footer-tool_detail award_word_span">赞<c:if test="${agency.awardCount > 0}">(${agency.awardCount })</c:if></span>
            </span> <span style="display: none;" class="paper_footer-tool__box agency_cancel_award_span"
              onclick="MobileAgency.ajaxAward($(this), '${agency.des3Id }', 0);event.stopPropagation();"
              resId="${agency.des3Id }"> <i class="paper_footer-tool paper_footer-award_unlike"></i> <span
              class="paper_footer-tool_detail cancel_award_word_span">取消赞<c:if test="${agency.awardCount > 0}">(${agency.awardCount })</c:if></span>
            </span> <span class="paper_footer-tool__box paper_footer-tool__pos agency_share_span"
              onclick="MobileAgency.shareToScm('${agency.des3Id }');event.stopPropagation();" resId="${agency.des3Id }">
              <i class="paper_footer-tool paper_footer-share"></i> <span
              class="paper_footer-tool_detail share_word_span">分享<c:if test="${agency.shareCount > 0}">(${agency.shareCount })</c:if></span>
            </span> <span class="paper_footer-tool__box paper_footer-tool__pos agency_interest_span"
              onclick="MobileAgency.ajaxInterest($(this), '${agency.des3Id }', 1);event.stopPropagation();"
              resId="${agency.des3Id }"> <i class="paper_footer-tool paper_footer-comment"></i> <span
              class="paper_footer-tool_detail interest_word_span">关注<c:if test="${agency.interestCount > 0}">(${agency.interestCount })</c:if></span>
            </span> <span style="display: none;"
              class="paper_footer-tool__box paper_footer-tool__pos agency_cancel_interest_span"
              onclick="MobileAgency.ajaxInterest($(this), '${agency.des3Id }', 0);event.stopPropagation();"
              resId="${agency.des3Id }"> <i class="paper_footer-tool paper_footer-comment__flag"></i> <span
              class="paper_footer-tool_detail cancel_interest_word_span">取消关注<c:if test="${agency.interestCount > 0}">(${agency.interestCount })</c:if></span>
            </span>
          </div> --%>
        </div>
      </div>
    </div>
  </c:forEach>
</c:if>
