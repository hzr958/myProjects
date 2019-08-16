<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="pageNum" value="${page.pageNo }" id="collectedPageNo" />
<input type="hidden" name="pageSize" value="10" id="collectedPageSize" />
<input type="hidden" name="totalPages" value="${page.totalPages }" id="collectedTotalPages" />
<input type="hidden" name="totalCount" value="${page.totalCount }" id="totalCount" />
<table border="0" cellpadding="0" cellspacing="0" class="tab-style">
  <s:if test="fundInfoList != null && fundInfoList.size() > 0">
    <s:iterator value="fundInfoList" id="fund" status="sta">
      <input type="hidden" id="zhTitle_${fund.fundId }" value="${fund.zhTitle }" />
      <input type="hidden" id="enTitle_${fund.fundId }" value="${fund.enTitle }" />
      <input type="hidden" id="zhShowDesc_${fund.fundId }" value="${fund.zhShowDesc }" />
      <input type="hidden" id="enShowDesc_${fund.fundId }" value="${fund.enShowDesc }" />
      <input type="hidden" id="zhShowDescBr_${fund.fundId }" value="${fund.zhShowDescBr }" />
      <input type="hidden" id="enShowDescBr_${fund.fundId }" value="${fund.enShowDescBr }" />
      <div id="${fund.fundId }" class="collect_fund_info collect_fund_info-maincontainer">
        <div style="display: flex;">
          <div class="file-left">
            <a href="/prjweb/funddetails/show?encryptedFundId=${fund.encryptedFundId }" target='_blank'> <img
              src="${fund.logoUrl}" onerror="this.src='${ressns }/images/default/default_fund_logo.jpg'"></a>
          </div>
        </div>
        <div class="file-rigth-box" style="margin-left: 20px;">
          <div class="file-rigth-title file-rigth-container pub-idx__main_title-multipleline" id="fund_title_${fund.fundId }" style="width: 765px; height: 40px; overflow: hidden;">
            <a class="multipleline-ellipsis__content-box" href="/prjweb/outside/showfund?encryptedFundId=${fund.encryptedFundId }"
              title='${fund.fundTitle }' target='_blank'>${fund.fundTitle }</a>
          </div>
          <div class="p1 file-rigth-container" style="margin: 4px 0px;">
            <a title="${fund.fundAgencyName }"
              href="/prjweb/fundAgency/detailMain?Des3FundAgencyId=<iris:des3 code='${fund.fundAgencyId }'/>"
              target='_blank'>${fund.fundAgencyName }</a>
          </div>
          <div class="p1 file-rigth-container file-right_contion" style="color: #999 !important;">
            <span style="cursor: default" title="${fund.scienceAreas }">${fund.scienceAreas }</span>
          </div>
          <div class="new-Standard_Function-bar">
            <a style="${fund.hasAward ? 'display:none;' : ''}"
              href="javascript:FundRecommend.awardOperation($(this), '${fund.encryptedFundId}', 0, '${fund.fundId }', 'my');"
              class="manage-one mr20" id="myAward_${fund.fundId }">
              <div class="new-Standard_Function-bar_item" style="margin-left: 0px;">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> </i> <s:text name='homepage.fundmain.recommend.like' /><span
                  class="awardOperateCount"> 
                      <c:if test="${!empty fund.awardCount && fund.awardCount > 0 && fund.awardCount < 1000}">
                       (${fund.awardCount})
                      </c:if>
                      <c:if test="${!empty fund.awardCount && fund.awardCount > 1000}">
                       (1k+)
                      </c:if>   
                </span></span>
              </div>
            </a> <a style="${fund.hasAward ? '' : 'display:none;'}"
              href="javascript:FundRecommend.awardOperation($(this), '${fund.encryptedFundId}', 1, '${fund.fundId }', 'my');"
              class="manage-one mr20" id="myAwardCancel_${fund.fundId }">
              <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <s:text name='homepage.fundmain.recommend.unlike' /><span
                  class="awardOperateCount"> 
                      <c:if test="${!empty fund.awardCount && fund.awardCount > 0 && fund.awardCount < 1000}">
                       (${fund.awardCount})
                      </c:if>
                      <c:if test="${!empty fund.awardCount && fund.awardCount > 1000}">
                       (1k+)
                      </c:if>                  
                </span></span>
              </div>
            </a> <a class="manage-one mr20" onclick="SmateShare.shareRecommendFund($(this));initSharePlugin();"
              resid="${fund.encryptedFundId}" fundId="${fund.fundId }" logoUrl="${fund.logoUrl }" fundDesc="">
              <div class="new-Standard_Function-bar_item">
                <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <s:text name='homepage.fundmain.recommend.share' /><span
                  class="shareCount_${fund.fundId }"> 
                          <c:if test="${!empty fund.shareCount && fund.shareCount > 0 && fund.shareCount < 1000}">
                           (${fund.shareCount})
                          </c:if>
                          <c:if test="${!empty fund.shareCount && fund.shareCount >= 1000}">
                           (1k+)
                          </c:if>              
                </span></span>
              </div>
            </a>
          </div>
        </div>
        
        <div 
         <c:if test="${!fund.isStaleDated }">style="width: 216px;font-size: 14px; color: #000;"</c:if>
         <c:if test="${fund.isStaleDated }">style="width: 216px;font-size: 14px; color: #999;"</c:if>
         >${fund.showDate }</div>
        <div align="center">
          <a
            href="javascript:FundRecommend.collectCoperation($(this), '${fund.encryptedFundId}', 1, '${fund.fundId }');"
            class="manage-one manage-one-cur">
            <div class="new-Standard_Function-bar">
              <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected">
                <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <s:text name='homepage.fundmain.recommend.unsave' /></span>
              </div>
          </a>
        </div>
      </div>
      </div>
    </s:iterator>
  </s:if>
  <s:else>
    <div class="main-list__list">
      <div class="response_no-result">
        <s:text name="homepage.fundmain.recommend.norecord" />
      </div>
    </div>
  </s:else>
</table>