<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
   $(document).ready(function(){
       document.onkeydown = function(event){
           if(event.keyCode == 27){
               event.stopPropagation();
               event.preventDefault();
               var target = document.getElementsByClassName("list-results_close")[0];
               for(var i = 0; i < target.length; i++){
                   Resume.hideVistPsnMoreUI(target[i]);
               }
           }
       }
   })
</script>
<div class="dialogs__box dev_home_pubconfirm_box" style="width: 720px;" dialog-id="dev_home_pubconfirm_back">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="nav_horiz">
        <div class="nav__list">
          <div class="dialogs__header_title nav__item item_selected dev_home_pubconfirm" onclick="Rm.toConfirmList();"
            style="padding: 0px; font-size: 16px!important;">
            <c:if test="${locale=='en_US' }">Confirm Publication</c:if>
            <c:if test="${locale=='zh_CN' }">成果认领</c:if>
          </div>
          <div class="dialogs__header_title nav__item dev_home_pubfulltext" onclick="Rm.toFulltextList();"
            style="padding: 0px; font-size: 16px!important; margin-left: 20px;">
            <c:if test="${locale=='en_US' }">Confirm Full-text</c:if>
            <c:if test="${locale=='zh_CN' }">全文认领</c:if>
          </div>
        </div>
        <div class="nav__underline setting-list_page-item_hidden"></div>
      </div>
      <div class="pub-idx__main_add dev_home_pubconfirm_back_add_tip" style="position: absolute; right: 86px;">
        <i class="material-icons pub-idx__main_add-tip">check_box</i> <span class="pub-idx__main_add-detail"> <c:if
            test="${locale=='en_US' }">Send contact invitation to my co-authors</c:if> <c:if test="${locale=='zh_CN' }">邀请我的合作者成为联系人</c:if>
        </span>
      </div>
      <i class="list-results_close" onclick="Rm.closePubConfirmBack();"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list item_no-border dev_home_confirmList" list-main="pubconfirm"></div>
    <div class="main-list__list item_no-border dev_home_fulltextList" list-main="pubftconfirm"></div>
  </div>
</div>