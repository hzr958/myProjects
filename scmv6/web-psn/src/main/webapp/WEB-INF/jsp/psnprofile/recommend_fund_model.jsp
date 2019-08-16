<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container__card">
  <div class="module-card__box">
    <div class="module-card__header">
      <div class="module-card-header__title">
        <s:text name="homepage.suggested.grants" />
      </div>
      <button class="button_main button_link" onclick="Resume.toFundRecommend()">
        <s:text name="psnweb.view.all" />
      </button>
    </div>
    <div class="main-list">
      <div class="main-list__list item_no-border" id="recommend_fund_list">
        <%-- <%@ include file="recommend_fund_list.jsp" %> --%>
        <div class="preloader active" id="recommend_fund_loading"
          style="height: 28px; width: 28px; margin: auto; margin-top: 24px;">
          <div class="preloader-ind-cir__box">
            <div class="preloader-ind-cir__fill">
              <div class="preloader-ind-cir__arc-box left-half">
                <div class="preloader-ind-cir__arc"></div>
              </div>
              <div class="preloader-ind-cir__gap">
                <div class="preloader-ind-cir__arc"></div>
              </div>
              <div class="preloader-ind-cir__arc-box right-half">
                <div class="preloader-ind-cir__arc"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>