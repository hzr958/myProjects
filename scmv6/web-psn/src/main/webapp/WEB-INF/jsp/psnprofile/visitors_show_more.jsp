<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
   $(document).ready(function(){
       document.onkeydown = function(event){
           if(event.keyCode == 27){
               event.stopPropagation();
               event.preventDefault();
               var target = document.getElementsByClassName("list-results_close");
               for(var i = 0; i < target.length; i++){
                   Resume.hideVistPsnMoreUI(target[i]);
               }
           }
       }
   })
</script>
<!-- =================================查看更多--最近来访--start=================================== -->
<div class="dialogs__box" style="width: 720px;" dialog-id="dev_vist_psn_more">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name="homepage.latest.viewed" />
      </div>
      <i class="list-results_close" onclick="Resume.hideVistPsnMoreUI(this);"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" list-main="vist_psn_more_list"></div>
  </div>
</div>
<!-- =================================查看更多--最近来访--end=================================== -->