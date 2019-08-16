<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="dialogs__box" style="width: 720px;" dialog-id="keyWordIdentificPsnBox" cover-event="hide"
  id="keyWordIdentificPsnBox">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='psnlist.title.rentongzhe' />
      </div>
      <button class="button_main button_icon" onclick="hideDialog('keyWordIdentificPsnBox')">
        <i class="material-icons">close</i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" list-main="psnInfoList" id="aaaa"></div>
  </div>
</div>
