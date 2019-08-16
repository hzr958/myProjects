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
      <s:text name="referencelist.res.label3" />
    </div>
    <div class="new-success_save-body_footer">
      <div class="new-success_save-body_footer-complete" onclick="Pub.closeAddPub()">
        <s:text name="referencelist.button.finish" />
      </div>
      <div class="new-success_save-body_footer-continue" onclick="Pub.returnAddPub()">
        <s:text name="referencelist.button.continue" />
      </div>
    </div>
  </div>
</div>