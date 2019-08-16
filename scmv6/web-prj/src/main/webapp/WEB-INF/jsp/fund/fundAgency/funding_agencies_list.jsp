
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${page.totalCount}"></div>
<s:iterator value="fundAgencyList" id="agency" status="sta">
  <div class="main-list__item" style="padding: 0px;">
    <div class="main-list__item_content" style="padding: 0px; margin: 0px !important;">
      <div class="funding-agencies_container-right_item need_init_item" style="width: 100%; padding: 20px 0px;"
        des3Id="${agency.des3Id }">
        <img src=" ${agency.logoUrl}" onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'"
          class="funding-agencies_container-right_item-avator" style="margin-bottom: 12px;">
        <div class="funding-agencies_container-right_item-infor">
          <div class="funding-agencies_container-right_item-detaile  pub-idx__main_title-multipleline" style="height: 40px; overflow: hidden;">
            <a class="multipleline-ellipsis__content-box" href="/prjweb/fundAgency/detailMain?Des3FundAgencyId=${agency.des3Id }"
              title='${agency.nameView}' id="agency_title_${agency.id}">${agency.nameView}</a>
          </div>
          <div class="funding-agencies_container-right_item-city">${agency.viewAddress}</div>
          <div class="new-Standard_Function-bar">
            <a style="" class="manage-one mr20 award_a"
              onclick="PCAgency.ajaxAward($(this), '${agency.des3Id }', 1);event.stopPropagation();"
              resId="${agency.des3Id }">
              <div class="new-Standard_Function-bar_item agency_award_div" style="margin-left: 0px;">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title award_word_span">
                    <s:text name="homepage.fundmain.recommend.like"></s:text>
                </span>
              </div>
            </a> <a class="manage-one mr20 share_a"
              onclick="SmateShare.shareFundAgency($(this));event.stopPropagation();initSharePlugin();"
              resId="${agency.des3Id }" agencyId="${agency.id }" logourl="" agencyDesc="资助机构描述">
              <div class="new-Standard_Function-bar_item">
                <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
                  class="new-Standard_Function-bar_item-title share_word_span">
                    <s:text name="homepage.fundmain.recommend.share"></s:text>
                 </span>
              </div>
            </a> <a style="" class="manage-one mr20 interest_a"
              onclick="PCAgency.ajaxInterest($(this), '${agency.des3Id }', 1);event.stopPropagation();"
              resId="${agency.des3Id }">
              <div class="new-Standard_Function-bar_item agency_interest_div">
                <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                  class="new-Standard_Function-bar_item-title interest_word_span">
                  <s:text name="homepage.fundmain.recommend.focus"></s:text>
                </span>
              </div>
            </a>
          </div>
        </div>
        <div class="funding-agencies_container-right_item-Opportunity">
          <c:if test="${not empty agency.fundCount}">
            <div class="funding-agencies_container-right_item-Opportunity_num">${agency.fundCount}</div>
            <div class="funding-agencies_container-right_item-Opportunity_title">
              <s:text name="homepage.fundmain.chance" />
            </div>
          </c:if>
        </div>
      </div>
    </div>
  </div>
</s:iterator>