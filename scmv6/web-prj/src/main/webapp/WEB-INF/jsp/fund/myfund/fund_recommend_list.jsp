<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	/* document.getElementById("cont_r-recommend").style.height = document.body.offsetHeight - 150 + "px"; */
	if($("#des3FundAgencyIds")){
		$("#des3FundAgencyIds").val('${des3FundAgencyIds}');
	}
});

</script>
<input type="hidden" name="pageNum" value="${pageNum }" id="recommendPageNo" />
<input type="hidden" name="pageSize" value="10" id="recommendPageSize" />
<input type="hidden" name="totalPages" value="${totalPages }" id="recommendTotalPages" />
<input type="hidden" name="totalCount" value="${totalCount }" id="recommendTotalCount" />
<input type="hidden" name="fundsUnlimit" value="${fundsUnlimit }" id="fundsUnlimit" />
<table border="0" cellpadding="0" cellspacing="0" class="tab-style">
  <s:if test="fundInfoList != null && fundInfoList.size() > 0">
    <s:iterator value="fundInfoList" id="fund" status="sta">
      <tr class="main-list__item">
        <input type="hidden" id="zhTitle_${fund.fundId }" value="<c:out value='${fund.zhTitle }'/>" />
        <input type="hidden" id="enTitle_${fund.fundId }" value="<c:out value='${fund.enTitle }'/>" />
        <input type="hidden" id="zhShowDesc_${fund.fundId }" value="<c:out value='${fund.zhShowDesc }'/>" />
        <input type="hidden" id="enShowDesc_${fund.fundId }" value="<c:out value='${fund.enShowDesc }'/>" />
        <input type="hidden" id="zhShowDescBr_${fund.fundId }" value="<c:out value='${fund.zhShowDescBr }'/>" />
        <input type="hidden" id="enShowDescBr_${fund.fundId }" value="<c:out value='${fund.enShowDescBr }'/>" />
        <td valign="top" width="65%">
          <div style="display: flex; justify-content: flex-start;">
              <a class="file-right_avator-container" href="/prjweb/outside/showfund?encryptedFundId=${fund.encryptedFundId }" target="_blank"> <img
                  class='logo_${fund.fundAgencyId }' src="${ressns }/images/default/default_fund_logo.jpg"
                  onerror="this.src='${ressns }/images/default/default_fund_logo.jpg'"
                  style="width: 60px; height: 60px;"></a>
              <div class="file-rigth file-rigth_contentbox">
                <div class="file-rigth-box">
                  <div class="file-rigth-title file-rigth-container fund_title pub-idx__main_title-multipleline" id="fund_title_${fund.fundId }" style="height: 40px; overflow: hidden;">
                    <a class="multipleline-ellipsis__content-box" href="/prjweb/funddetails/show?encryptedFundId=${fund.encryptedFundId }"
                      title="<c:out value='${fund.fundTitle }'/>" target="_blank"><c:out value='${fund.fundTitle }' /></a>
                  </div>
                  <div class="p1 file-rigth-container">
                    <a title="<c:out value='${fund.fundAgencyName }'/>"
                      href="/prjweb/fundAgency/detailMain?Des3FundAgencyId=<iris:des3 code='${fund.fundAgencyId }'/>"
                      target="_black"><c:out value='${fund.fundAgencyName }' /></a>
                  </div>
                  <div class="p1 file-rigth-container file-right_contion" style="color: #999 !important;">
                    <span style="cursor: default" title='<c:out value="${fund.scienceAreas }"/>'><c:out
                        value='${fund.scienceAreas }' /></span>
                  </div>
                </div>
                <div class="new-Standard_Function-bar">
                  <a style="${fund.hasAward ? 'display:none;' : ''}"
                    onclick="javascript:FundRecommend.awardOperation($(this), '${fund.encryptedFundId}', 0, '${fund.fundId }', 'recommend');"
                    class="manage-one mr20" id="recommendAward_${fund.fundId }">
                    <div class="new-Standard_Function-bar_item" style="margin-left: 0px;">
                      <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                        class="new-Standard_Function-bar_item-title"> <s:text
                          name='homepage.fundmain.recommend.like' /><span class="awardOperateCount"> <c:if
                            test="${!empty fund.awardCount && fund.awardCount > 0}">
    	               (${fund.awardCount})
    	               </c:if>
                      </span></span>
                    </div>
                  </a> <a style="${fund.hasAward ? '' : 'display:none;'}"
                    onclick="javascript:FundRecommend.awardOperation($(this), '${fund.encryptedFundId}', 1, '${fund.fundId }', 'recommend');"
                    class="manage-one mr20" id="recommendAwardCancel_${fund.fundId }">
                    <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected"
                      style="margin-left: 0px;">
                      <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                        class="new-Standard_Function-bar_item-title"> <s:text
                          name='homepage.fundmain.recommend.unlike' /><span class="awardOperateCount"> <c:if
                            test="${!empty fund.awardCount && fund.awardCount > 0}">
    	               (${fund.awardCount})
    	               </c:if>
                      </span></span>
                    </div>
                  </a> <a class="manage-one mr20" onclick="SmateShare.shareRecommendFund($(this));initSharePlugin();"
                    resid="${fund.encryptedFundId}" fundId="${fund.fundId }" logoUrl="${fund.logoUrl }"
                    fundDesc="<s:text name='homepage.fundmain.recommend.funddesc' />">
                    <div class="new-Standard_Function-bar_item">
                      <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
                        class="new-Standard_Function-bar_item-title"> <s:text
                          name='homepage.fundmain.recommend.share' /><span class="shareCount_${fund.fundId }"> <c:if
                            test="${!empty fund.shareCount && fund.shareCount > 0}">
    	                (${fund.shareCount })
    	            </c:if>
                      </span></span>
                    </div>
                  </a> <a style="${fund.hasCollected ? 'display:none;' : ''}"
                    onclick="javascript:FundRecommend.collectCoperation($(this), '${fund.encryptedFundId}', 0, '${fund.fundId }');"
                    class="manage-one mr20" id="collect_${fund.fundId }">
                    <div class="new-Standard_Function-bar_item">
                      <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                        class="new-Standard_Function-bar_item-title"> <s:text
                          name='homepage.fundmain.recommend.save' />
                      </span>
                    </div>
                  </a> <a style="${fund.hasCollected ? '' : 'display:none;'}"
                    onclick="javascript:FundRecommend.collectCoperation($(this), '${fund.encryptedFundId}', 1, '${fund.fundId }');"
                    class="manage-one mr20" id="collectCancel_${fund.fundId }">
                    <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected">
                      <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                        class="new-Standard_Function-bar_item-title"></i> <s:text
                          name='homepage.fundmain.recommend.unsave' /> </span>
                    </div>
                  </a>
                </div>
              </div>
            </div>

        </td>
        <td align="left" width="25%">${fund.showDate }</td>
        <td align="center"><i class="needDeal"></i></td>
      </tr>
    </s:iterator>
  </s:if>
  <s:elseif test="#pageNum==1">
    <div id="div_norecord"
      style="width: 100%; height: 36px; line-height: 36px; text-align: center; font-size: 16px; color: rgba(0, 0, 0, 0.54); margin-top: 24px">
      <s:text name='homepage.fundmain.recommend.norecord' />
    </div>
  </s:elseif>
  <s:else>
    <div id="div_noMoreRecord"
      style="width: 100%; height: 36px; line-height: 36px; text-align: center; font-size: 16px; color: rgba(0, 0, 0, 0.54); margin-top: 24px">
      <s:text name='homepage.fundmain.recommend.noMoreRecord' />
    </div>
  </s:else>
</table>