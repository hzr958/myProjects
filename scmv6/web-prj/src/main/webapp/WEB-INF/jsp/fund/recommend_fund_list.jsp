<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count="${totalCount}"></div>
<s:iterator value="fundInfoList" id="fund" status="sta">
  <div class="main-list__item" onclick="Resume.showFundDetails('${fund.encryptedFundId }');" style="cursor: pointer;">
    <div class="main-list__item_content">
      <div class="psn-idx_small">
        <div class="psn-idx__base-info">
          <div class="psn-idx__avatar_box">
            <div class="psn-idx__avatar_img">
              <img src="${fund.logoUrl}" onerror="this.src='${ressns }/images/default/default_fund_logo.jpg'">
            </div>
          </div>
          <div class="psn-idx__main_box">
            <div class="psn-idx__main">
              <div class="psn-idx__main_name" style="max-width: calc(100% - 80px);"
                title="<c:out value='${fund.fundTitle }'/>">
                <c:out value='${fund.fundTitle }' />
              </div>
              <div class="psn-idx__main_title" style="max-width: calc(100% - 80px);"
                title="<c:out value='${fund.fundAgencyName }'/>">
                <c:out value='${fund.fundAgencyName }' />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>