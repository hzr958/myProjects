<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${resmod }/js/friend/findpsn/friend.findpsn.js"></script>
<script>
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
        <span class="new-success_save-body_tip-num">${importSuccessNum }</span><s:text name="referencelist.label.count_1"/>
      </div>
      <div class="new-success_save-body_footer">
        <div class="new-success_save-body_footer-complete" onclick="searImport.viewHistory()"><s:text name="referencelist.button.finish"/></div>
        <div class="new-success_save-body_footer-continue" onclick="continueImport()"><s:text name="referencelist.button.continue"/></div>
      </div>
    </div>
  </div>
