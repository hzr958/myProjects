<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type=text/javascript>
 $(document).ready(function(){
    var setele = document.getElementsByClassName("new-success_save")[0];
    setele.style.left = (window.innerWidth - setele.offsetWidth)/2 + "px";
    setele.style.bottom = (window.innerHeight - setele.offsetHeight)/2 + "px";
    window.onresize = function(){
        setele.style.left = (window.innerWidth - setele.offsetWidth)/2 + "px";
        setele.style.bottom = (window.innerHeight - setele.offsetHeight)/2 + "px";
    }
 }); 
</script>
<%-- <div class='resultContent'>
    <div class="dialogs__box dialogs__box-fixed" style="width: 261.989px; min-height: 111px; top: 380px; left: 820px; visibility: visible; opacity: 1;" dialog-id="dev_jconfirm_ui" flyin-direction="top">
        <div class="dialogs__modal_text" style="width:230px;"><c:out value="${totalCount}"></c:out><spring:message code="referencelist.res.label3" /></div>
        <div class="dialogs__modal_actions">
          <button class="button_main button_dense button_primary" style="width: auto;min-width: 82px;padding: 0px;"  onclick="Pub.returnAddPub()"  ><spring:message code="referencelist.button.continue" /></button>
          <button class="button_main button_dense"onclick="Pub.closeAddPub()"><spring:message code="referencelist.button.finish" /></button>
        </div>
    </div>
</div>  --%>
<div class="new-success_save search_import_result_tips">
  <div class="new-success_save-header">
    <span class="new-success_save-header_title"></span> <i
      class="new-success_save-header_tip new-searchplugin_container-header_close search_import_result_tips_close"></i>
  </div>
  <div class="new-success_save-body">
    <div class="new-success_save-body_avator">
      <img src="/resmod/smate-pc/img/pass.png">
    </div>
    <div class="new-success_save-body_tip">
      <span class="new-success_save-body_tip-num"><c:out value="${totalCount}"></c:out></span>
      <spring:message code="referencelist.res.label3" />
    </div>
    <div class="new-success_save-body_footer">
      <div class="new-success_save-body_footer-complete" onclick="Pub.closeAddPub()">
        <spring:message code="referencelist.button.finish" />
      </div>
      <div class="new-success_save-body_footer-continue" onclick="Pub.returnAddPub()">
        <spring:message code="referencelist.button.continue" />
      </div>
    </div>
  </div>
</div>