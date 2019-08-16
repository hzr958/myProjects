<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${resmod }/js/friend/findpsn/friend.findpsn.js"></script>
<script>
    //合作者打开主页
    importPub.openPsnDetail = function(des3ProducerPsnId, event) {
      window.open("/psnweb/outside/homepage?des3PsnId=" + des3ProducerPsnId);
      importPub.stopNextEvent(event);
    }
    //停止进一步的事件触发
    importPub.stopNextEvent=function(evt){
        if(evt&&evt.currentTarget){
            if(evt.stopPropagation){
                evt.stopPropagation();
            }else{
                evt.cancelBubble=true;
            }
        }
    }
    
</script>
<c:if test="${empty importVo.recommendPsn || fn:length(importVo.recommendPsn) == 0}">
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
        <span class="new-success_save-body_tip-num">${importVo.importSuccessSize }</span><spring:message code="referencelist.label.count_1" />
      </div>
      <div class="new-success_save-body_footer">
        <div class="new-success_save-body_footer-complete" onclick="searImport.viewHistory()"><spring:message code="referencelist.button.finish" /></div>
        <div class="new-success_save-body_footer-continue" onclick="continueImport()"><spring:message code="referencelist.button.continue" /></div>
      </div>
    </div>
  </div>
</c:if>
<c:if test="${!empty importVo.recommendPsn && fn:length(importVo.recommendPsn) > 0}">
  <div class="new-import_addfriend search_import_result_tips">
    <div class="new-success_save-header">
      <span class="new-success_save-header_title"><spring:message code="referencesearch.label.addPublication" /></span> <i
        class="new-success_save-header_tip new-searchplugin_container-header_close" onclick="continueImport();"></i>
    </div>
    <div class="new-import_addfriend-body">
      <div class="new-import_addfriend-body_top">
        <img src="/resmod/smate-pc/img/pass.png" class="new-import_addfriend-body_top-avator"> <span
          class="new-import_addfriend-body_top-detail">${importVo.importSuccessSize }<spring:message code="referencelist.res.label3_plural" /></span>
      </div>
      <span class="new-import_addfriend-body_center"><spring:message code="add.req.friends.label" /></span>
      <div class="new-import_addfriend-body_content">
        <div class="new-import_addfriend-body_srollbox">
          <c:forEach items="${importVo.recommendPsn }" varStatus="status" var="psnInfo">
            <div class="new-import_addfriend-body_content-item" onclick="searImport.selectAddFriendPsn(this);"
              des3PsnId="${psnInfo.des3PsnId }">
              <img src="${psnInfo.avatarUrl }" onclick="importPub.openPsnDetail('${psnInfo.des3PsnId }',event)"
                onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
              <div class="new-import_addfriend-body_content-item_container">
                <span class="dynamic-post__author_name" style=" width: 120px;" title="${psnInfo.name }"
                  onclick="importPub.openPsnDetail('${psnInfo.des3PsnId }',event)">${psnInfo.name }</span> <span
                  class="new-import_addfriend-body_content-item_container-work" title="${psnInfo.insAndDep }">${psnInfo.insAndDep }</span>
              </div>
            </div>
          </c:forEach>
        </div>
      </div>
      <div class="new-import_addfriend-body_add">
        <div class="new-import_addfriend-body_adddetail" onclick="searImport.sendAddFriendReq();"><spring:message code="add.req.friends.button" /></div>
      </div>
    </div>
    <div class="new-searchplugin_container-footer">
      <div class="new-searchplugin_container-footer_cancel new-searchplugin_container-close"
        onclick="searImport.viewHistory()"><spring:message code="referencelist.button.finish" /></div>
      <div class="new-searchplugin_container-footer_comfire new-searchplugin_container-close" onclick="continueImport()"><spring:message code="referencelist.button.continue" /></div>
    </div>
  </div>
</c:if>
