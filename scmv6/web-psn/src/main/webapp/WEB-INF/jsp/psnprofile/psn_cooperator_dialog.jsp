<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- =================================合作者查看全部start=================================== -->
<div class="dialogs__box" style="width: 720px;" dialog-id="psncooperator_dialog">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name="homepage.psn.copartner" />
      </div>
      <i class="list-results_close" onclick="Resume.closePsnCooperatorBack();"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list item_no-border" list-main="psncooperator"></div>
  </div>
</div>
<%--  <div class="dialogs__box" style="width: 360px;"dialog-id="updateCite">
  <div class="dialogs__childbox_adapted">
    <div class="preloader_ind-linear">
      <div class="preloader-ind-linear__box">
        <div class="preloader-ind-linear__bar1"></div>
        <div class="preloader-ind-linear__bar2"></div>
      </div>
    </div>
    <div class="update-citation"> <div class="update-citation__hint"> <span><s:text name="pub.updateCitations1"/><span>0</span> <s:text name="pub.updateCitations2"/></span> </div>
      <div class="update-citation__counter"><s:text name="pub.updated"></s:text> <span>0</span>/<span>0</span></div>
    </div>
  </div>
</div> --%>
<!-- =================================合作者查看全部end=================================== -->