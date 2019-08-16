<!-- 成果认领 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
    var boxlist = document.getElementsByClassName("dev_lookall_pubconfirm");
    window.onresize = function(){
        for(var i = 0; i < boxlist.length; i++){
            var allheight = document.body.clientheight;
            var allwidth = document.body.offsetWidth;
            var selfheight = boxlist[i].offsetHeight;
            var selfwidth = boxlist[i].offsetWidth;
            boxlist[i].style.top = (allheight - selfheight)/2 + "px";
            boxlist[i].style.left = (allwidth - selfwidth)/2 + "px";
        }
    }
    document.onkeydown = function(event){
        if(event.keyCode == 27){
            event.stopPropagation();
            event.preventDefault();
            if(document.getElementsByClassName("cover_colored")){
              NewProject.closePubConfirmBack();
            }
        }
    }
})
</script>
<div class="dev_pub_confirm"></div>
<!-- 成果认领================ -->
<!-- =================================成果认领查看全部start=================================== -->
<div class="dialogs__box dev_lookall_pubconfirm" style="width: 720px;" dialog-id="dev_lookall_pubconfirm_back">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="max-height: 56px; height: 56px;">
      <div class="nav_horiz">
        <div class="nav__list">
          <div class="dialogs__header_title nav__item item_selected dev_item_pubconfirm" "
            style="font-size: 16px!important;">
                                 成果匹配
          </div>
        </div>
        <div class="nav__underline setting-list_page-item_hidden"></div>
      </div>
      <i class="list-results_close" onclick="NewProject.closePubConfirmBack();"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list item_no-border dev_confirmList" list-main="prjpubconfirm"></div>
  </div>
</div>
<!-- =================================成果认领查看全部end=================================== -->
