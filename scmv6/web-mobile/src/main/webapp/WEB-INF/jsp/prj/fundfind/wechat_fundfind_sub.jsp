<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" id="totalCount" smate_count='${prjOperateVO.totalCount}'></div>
<c:if test="${resultList.size()>0 }">
  <input type='hidden' id="totalPages" value='${prjOperateVO.totalPages}' />
  <input type='hidden' id="curPage" value='${prjOperateVO.pageNum}' />
  <input type="hidden" id="des3FundAgencyIds" name="des3FundAgencyIds" value="${prjOperateVO.des3FundAgencyIds }" />
  <c:forEach items="${resultList}" var="fund" varStatus="status">
    <input type="hidden" id="zhTitle_${fund.fundId }" value="<c:out value='${fund.zhTitle }'/>" />
    <input type="hidden" id="enTitle_${fund.fundId }" value="<c:out value='${fund.enTitle }'/>" />
    <input type="hidden" id="zhShowDesc_${fund.fundId }" value="<c:out value='${fund.zhShowDesc }'/>" />
    <input type="hidden" id="enShowDesc_${fund.fundId }" value="<c:out value='${fund.enShowDesc }'/>" />
    <input type="hidden" id="zhShowDescBr_${fund.fundId }" value="<c:out value='${fund.zhShowDescBr }'/>" />
    <input type="hidden" id="enShowDescBr_${fund.fundId }" value="<c:out value='${fund.enShowDescBr }'/>" />
    <div class="paper_content-container_list main-list__item"  des3id = "${fund.des3FundId }" style="/* padding: 8px 16px */;display: flex;">
      <div class="paper_content-container_list-avator">
        <div onclick="opendetail('<iris:des3 code='${fund.fundId }'/>')">
          <img class='logo_${fund.fundAgencyId }' src="${ressns }/images/default/default_fund_logo.jpg"
            onerror="this.src='${ressns }/images/default/default_fund_logo.jpg'"></a>
        </div>
      </div>
      <div class="paper_content-container_list-box">
        <a style="padding-left: 6px;" onclick="opendetail('<iris:des3 code='${fund.fundId }'/>')">
        <div class="paper_list-box_title webkit-multipleline-ellipsis">
          <c:out value="${fund.fundName}" />
        </div>
        <div class="paper_list-box_author">
          <c:out value="${fund.fundAgency}" />
        </div>
        <div class="paper_list-box_introdu">
          <c:out value="${fund.showDate}" />
        </div>
        </a>
        <div class="new-Standard_Function-bar">
<%--社交操作start --%>
        <c:set var="isAward" value="${fund.hasAward == true ? 1 : 0}"></c:set>
        <c:set var="resDes3Id" >${fund.encryptedFundId }</c:set>
        <c:set var="awardCount" value="${fund.awardCount}"></c:set>
        <c:set var="showComment" value="0"></c:set>
        <c:set var="shareCount" value="${fund.shareCount}"></c:set>
        <c:set var="isCollection" value="${fund.hasCollected == true ? 1 : 0}"></c:set>
        <c:set var="fundId" value="${fund.fundId }"></c:set>
        <%@ include file="/common/standard_function_bar.jsp" %>
 <%--社交操作 end--%> 
        </div>
      </div>
    </div>
  </c:forEach>
</c:if>