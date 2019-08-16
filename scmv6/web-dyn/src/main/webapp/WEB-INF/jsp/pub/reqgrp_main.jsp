<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="dialogs__box" style="width: 720px;" dialog-id="dev_home_reqgrp_back">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="nav_horiz">
        <c:if test="${locale=='en_US' }">
          <div class="nav__list">
            <div class="dialogs__header_title nav__item dev_home_reqgrpList" style="width: 140px;"
              onclick="ReqGrp.reloadCurrentPage()">Group Requests</div>
            <div class="dialogs__header_title nav__item dev_home_reqfriend" onclick="Rm.iviteGrpAll()">Group
              Invitations</div>
          </div>
          <div class="nav__underline" style="min-width: 164px;"></div>
        </c:if>
        <c:if test="${locale=='zh_CN' }">
          <div class="nav__list">
            <div class="dialogs__header_title nav__item dev_home_reqgrpList" onclick="ReqGrp.reloadCurrentPage()">
              群组请求</div>
            <div class="dialogs__header_title nav__item dev_home_reqfriend" onclick="Rm.iviteGrpAll()">群组邀请</div>
          </div>
          <div class="nav__underline"></div>
        </c:if>
      </div>
      <button class="button_main button_icon" onclick="Rm.reqGrpAllClose();">
        <i class="material-icons">close</i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list item_no-border dev_home_reqgrpList" list-main="req_grp_list"></div>
  </div>
</div>