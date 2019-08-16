<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- <script>
   $(document).ready(function(){
       document.onkeydown = function(event){
           if(event.keyCode == 27){
               event.stopPropagation();
               event.preventDefault();
               if(document.getElementsByClassName("cover_colored")){
                   ReqFriend.reloadCurrentPage();
               }
              
           }
       }
   })
   
   
</script> -->
<div class="dialogs__box" style="width: 720px;" dialog-id="dev_home_reqfriend_back">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="nav_horiz">
        <c:if test="${locale=='en_US' }">
          <div class="nav__list">
            <div class="dialogs__header_title nav__item dev_home_reqfriend" onclick="ReqFriend.reloadCurrentPage()"
              style="padding: 0px;">Contact Requests</div>
          </div>
          <div class="nav__underline" style="min-width: 144px;"></div>
        </c:if>
        <c:if test="${locale=='zh_CN' }">
          <div class="nav__list">
            <div class="dialogs__header_title nav__item dev_home_reqfriend" onclick="ReqFriend.reloadCurrentPage()"
              style="padding: 0px;">联系人请求</div>
          </div>
          <div class="nav__underline"></div>
        </c:if>
      </div>
      <i class="list-results_close" onclick="Rm.reqFriendAllClose();"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list item_no-border dev_home_reqfriend" list-main="reqfriend"></div>
  </div>
</div>