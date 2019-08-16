<!-- 合作者 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="dev_pub_cooperator"></div>
<!-- =================================成果合作者查看全部start=================================== -->
<div class="dialogs__box" style="width: 720px;" dialog-id="dev_lookall_pubcooperator_back">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <spring:message code="pub.copartner" />
      </div>
      <i class="list-results_close" onclick="Pub.closePubCooperatorBack();"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list item_no-border" list-main="pubcooperator"></div>
  </div>
</div>
<div class="dialogs__box" style="width: 360px;" dialog-id="updateCite" id="updateCite">
  <div class="dialogs__childbox_adapted">
    <div class="preloader_ind-linear">
      <div class="preloader-ind-linear__box">
        <div class="preloader-ind-linear__bar1"></div>
        <div class="preloader-ind-linear__bar2"></div>
      </div>
    </div>
    <div class="update-citation">
      <div class="update-citation__hint">
        <span><spring:message code="pub.updateCitations1" /><span>0</span> <spring:message
            code="pub.updateCitations2" /></span>
      </div>
      <div style="display: flex; align-items: center;">
        <div class="update-citation__counter">
          <spring:message code="pub.updated" />
          <span>0</span>/<span>0</span><img title="<spring:message code='pub.update.title.tip'/>"
            src="${resmod }/smate-pc/img/question.png" style="width: 16px; height: 16px; margin-left: 6px;">
        </div>
        <div style="color: #999; font-size: 14px; cursor: pointer; margin: 24px 0px 0px 18px; line-height: 18px;"
          onclick="Pub.closeUpdateCite()">
          <spring:message code="pub.update.stop" />
        </div>
      </div>
    </div>
  </div>
</div>
<div class="dialogs__box closeUpdate"
  style="left: 771.5px; top: 386px; width: 360px; height: 160px; opacity: 1; display: flex; flex-direction: column; align-items: center; justify-content: space-around;"
  dialog-id="closeCite" id="closeCite">
  <div style="color: #333; font-size: 14px; height: 70px; line-height: 24px; text-align: center; padding-top: 8px;">
    <spring:message code="pub.update.nubmers" />
  </div>
  <div style="display: flex; justify-content: space-around; height: 64px; margin-top: 24px;">
    <div class="new-standard__style-btn_cancel" onclick="Pub.cancelStop()">
      <spring:message code="pub.update.cancel" />
    </div>
    <div class="new-standard__style-btn_confirm" style="margin-left: 16px;" onclick="Pub.stopUpdate()">
      <spring:message code="pub.update.sure" />
    </div>
  </div>
</div>
<div class="dialogs__box updateError"
  style="left: 771.5px; top: 386px; width: 360px; height: 64px !important; opacity: 1; display: flex; flex-direction: column; align-items: center; justify-content: space-around;"
  dialog-id="updateError" id="updateError">
  <div style="width: 100%; height: 22px; line-height: 28px; display: flex; justify-content: flex-end;">
    <i class="material-icons" style="margin-right: 8px; margin-top: 4px;" onclick="closeTip()">close</i>
  </div>
  <div style="color: #333; font-size: 18px; text-align: center; height: 95px; line-height: 36px;">
    <spring:message code="pub.update.fail" />
  </div>
</div>
<!-- =================================成果合作者查看全部end=================================== -->