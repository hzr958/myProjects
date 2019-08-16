<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<input type = "hidden" value = "${des3FundIds }" id = "aidInsDeatils_des3FundIds"/>
<s:iterator value="page.result" var="fund">
  <input type="hidden" id="zhTitle_${fund.fundId }" value="${fund.zhTitle }" />
  <input type="hidden" id="enTitle_${fund.fundId }" value="${fund.enTitle }" />
  <input type="hidden" id="zhShowDesc_${fund.fundId }" value="${fund.zhShowDesc }" />
  <input type="hidden" id="enShowDesc_${fund.fundId }" value="${fund.enShowDesc }" />
  <input type="hidden" id="zhShowDescBr_${fund.fundId }" value="${fund.zhShowDescBr }" />
  <input type="hidden" id="enShowDescBr_${fund.fundId }" value="${fund.enShowDescBr }" />
  <div class="funding-agencies_container-right_item main-list__item" style="padding: 16px;">
    <a href="/prjweb/outside/showfund?encryptedFundId=${fund.encryptedFundId }" target="_blank"
      style="padding: 0px; margin: 0px;"> <img src="${fund.logoUrl}" logo_id = "logo_${fund.fundAgencyId }"
      onerror="src='${ressns }/images/default/default_fund_logo.jpg'"
      class="funding-agencies_container-right_item-avator">
    </a>
    <div class="funding-agencies_container-right_item-infor" style="height: auto;">
      <div class="funding-agencies_container-right_item-detaile" id="fund_title_${fund.fundId }">
        <a href="/prjweb/funddetails/show?encryptedFundId=${fund.encryptedFundId }"
          title='${fund.fundTitle }' target="_blank">${fund.fundTitle }</a>
      </div>
      <div class="funding-agencies_container-right_item-mechanism">
        <a href="javascript:;" style="cursor: default;" title='${fund.fundAgencyName }"/>'>${fund.fundAgencyName }</a>
      </div>
      <div class="funding-agencies_container-right_item-subject">
        <a href="javascript:;" style="cursor: default" title='${fund.disAllName }'>${fund.disAllName}</a>
      </div>
      <div class="new-Standard_Function-bar" style="margin: 4px 24px 0px 0px; font-size: 14px;">
        <a style="${fund.hasAward ? 'display:none;' : ''}"
          href="javascript:FundRecommend.awardOperation($(this), '${fund.encryptedFundId}', 0, '${fund.fundId }', 'recommend');"
          class="manage-one mr20" id="recommendAward_${fund.fundId }"><i class="icon-praise"></i><s:text name='homepage.fundmain.recommend.like' /> <span class="awardOperateCount"> 
          <c:if test="${!empty fund.awardCount && fund.awardCount > 0 && fund.awardCount < 1000}">
           (${fund.awardCount})
          </c:if>
          <c:if test="${!empty fund.awardCount && fund.awardCount > 1000}">
           (1k+)
          </c:if>
        </span> </a> <a style="${fund.hasAward ? '' : 'display:none;'} color: #288aed;"
          href="javascript:FundRecommend.awardOperation($(this), '${fund.encryptedFundId}', 1, '${fund.fundId }', 'recommend');"
          class="mr20 manage-one_blue" id="recommendAwardCancel_${fund.fundId }"><i class="icon-praise-cur"></i><s:text name='homepage.fundmain.recommend.unlike' /> <span class="awardOperateCount">
          <c:if test="${!empty fund.awardCount && fund.awardCount > 0 && fund.awardCount < 1000}">
           (${fund.awardCount})
          </c:if>
          <c:if test="${!empty fund.awardCount && fund.awardCount > 1000}">
           (1k+)
          </c:if>
        </span> </a> <a class="manage-one mr20" onclick="SmateShare.shareRecommendFund($(this));initSharePlugin();"
          resid="${fund.encryptedFundId}" fundId="${fund.fundId }" logoUrl="${fund.logoUrl }" fundDesc="基金描述"><i
          class="icon-share"></i><s:text name='homepage.fundmain.recommend.share' /> <span
          class="shareCount_${fund.fundId }"> 
            <c:if test="${!empty fund.shareCount && fund.shareCount > 0 && fund.shareCount < 1000}">
             (${fund.shareCount})
            </c:if>
            <c:if test="${!empty fund.shareCount && fund.shareCount >= 1000}">
             (1k+)
            </c:if>
        </span> </a> <a style="${fund.hasCollected ? 'display:none;' : ''}"
          href="javascript:FundRecommend.collectCoperation($(this), '${fund.encryptedFundId}', 0, '${fund.fundId }');"
          class="manage-one mr20" id="collect_${fund.fundId }">
          <div class="new-Standard_Function-bar_item">
            <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
              class="new-Standard_Function-bar_item-title"><s:text name='homepage.fundmain.recommend.save' />
            </span>
          </div>
        </a> <a style="${fund.hasCollected ? '' : 'display:none;'}"
          href="javascript:FundRecommend.collectCoperation($(this), '${fund.encryptedFundId}', 1, '${fund.fundId }');"
          class="manage-one mr20" id="collectCancel_${fund.fundId }">
          <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected">
            <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
              class="new-Standard_Function-bar_item-title"><s:text name='homepage.fundmain.recommend.unsave' /></span>
          </div>
        </a>
      </div>
    </div>
    <div class="funding-agencies_container-right_item-time"
      <c:if test="${!fund.isStaleDated }">style="color:#000"</c:if>>${fund.showDate }</div>
  </div>
</s:iterator>
<%--  <s:if test="page.totalPages == page.pageNo && page.totalCount != null">
<jsp:include page="/skins_v6/footer_infor.jsp"/>
</s:if> --%>
<input type='hidden' id="curPage" value='${page.pageNo}' />
<input type="hidden" value="${page.totalPages}" />
