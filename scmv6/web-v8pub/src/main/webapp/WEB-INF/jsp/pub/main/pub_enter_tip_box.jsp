<!-- 录入成果方式提示框 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- ================================添加成果弹出框start================================== -->
<div class="dialogs__box  dialogs__childbox_limited-big" style="width: auto;" dialog-id="dev_pub_add">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">pub.add.style</div>
      <button class="button_main button_icon" onclick="Pub.hideSelectImportType();">
        <i class="material-icons">close</i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="import-methods__box">
      <c:if test="${confirmCount>0}">
        <div class="import-methods__sxn" style="position: relative;">
          <div class="import-methods__sxn_logo file-import" onclick="Pub.hideSelectImportType();Pub.pubConfirmAll();"></div>
          <div class="import-methods__sxn_name">pub.file.results</div>
          <div class="import-methods__sxn_explain" style="margin-left: 30px">pub.file.results.description</div>
          <div class="imporet-methods__recong-num">
            <c:if test="${confirmCount<99}">
              <div class="imporet-result__num">${confirmCount}</div>
            </c:if>
            <c:if test="${confirmCount>=99}">
              <div class="imporet-result__num">99</div>
              <i class="material-icons">add</i>
            </c:if>
          </div>
        </div>
      </c:if>
      <div class="import-methods__sxn">
        <div class="import-methods__sxn_logo online-search" onclick="Pub.searchPubImport();"></div>
        <div class="import-methods__sxn_name">pub.search.import</div>
        <div class="import-methods__sxn_explain" style="margin-left: 30px">pub.seach.import.description</div>
      </div>
      <div class="import-methods__sxn">
        <div class="import-methods__sxn_logo manual-entry" onclick="Pub.manualImportPub();"></div>
        <div class="import-methods__sxn_name">pub.manual.import</div>
        <div class="import-methods__sxn_explain" style="margin-left: 30px">pub.manual.import.description</div>
      </div>
      <div class="import-methods__sxn">
        <div class="import-methods__sxn_logo file-import" onclick="Pub.fileImport();"></div>
        <div class="import-methods__sxn_name">pub.file.import</div>
        <div class="import-methods__sxn_explain" style="margin-left: 30px">pub.file.import.description</div>
      </div>
    </div>
  </div>
</div>
<!-- ================================添加成果弹出框end================================== -->
