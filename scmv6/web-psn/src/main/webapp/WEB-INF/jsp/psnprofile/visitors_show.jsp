<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container__card">
  <div class="module-card__box">
    <div class="module-card__header">
      <div class="module-card-header__title">
        <s:text name="homepage.latest.viewed" />
      </div>
      <button class="button_main button_link" onclick="Resume.showVistPsnMoreUI(this)">
        <s:text name="psnweb.view.all" />
      </button>
    </div>
    <div class="main-list">
      <div class="main-list__list item_no-border" id="psn_list">
        <!-- 最近来访人员列表 -->
      </div>
    </div>
  </div>
</div>