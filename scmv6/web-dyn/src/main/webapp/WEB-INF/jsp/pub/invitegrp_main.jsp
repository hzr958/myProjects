<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
   $(document).ready(function(){
       document.onkeydown = function(event){
           if(event.keyCode == 27){
               event.stopPropagation();
               event.preventDefault();
               GrpBase.hideGrpMemberApply();
           }
       }
   })
</script>
<div class="dialogs__box" style="width: 720px;" dialog-id="join_grp_invite_box" flyin-direction="top">
  <div class="dialogs__childbox_fixed"
    style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #ddd;">
    <nav class="nav_horiz">
      <ul class="nav__list">
        <li class="nav__item item_selected dev_grp_module_reg" onclick="GrpBase.changeGrpModule(this,'regGrp');"><s:text
            name='groups.list.reqGrpTitle' /></li>
        <li class="nav__item dev_grp_module_invite" onclick="GrpBase.changeGrpModule(this,'inviteGrp');"><s:text
            name='groups.list.inviteTitle' /></li>
      </ul>
      <div class="nav__underline" style="width: 112px; left: 0px;"></div>
    </nav>
    <i class="list-results_close google__icon-close" onclick="GrpBase.hideGrpMemberApply();"></i>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" id="has_ivite_grp_list">
      <!-- 被邀请的群组列表 -->
    </div>
  </div>
</div>