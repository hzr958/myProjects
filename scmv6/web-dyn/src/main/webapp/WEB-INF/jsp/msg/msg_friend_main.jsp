<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(document).ready(function(){
    var seclist =  document.getElementsByClassName("dialogs__header_sort-box_list");
    for(var i = 0; i < seclist.length; i++){
    	$(seclist[i]).on('click',function(e){
            this.closest(".dialogs__header_sort-box").querySelector(".dialogs__header_sort-box_detaile").innerHTML = this.innerHTML;
            $(this.closest(".dialogs__header_sort-box").querySelector(".dialogs__header_sort-box_detaile")).attr("val",$(this).attr("val"));
            MsgBase.loadFriendListByOrder($(this).attr("val"));
        });
    }
});
</script>
<div class="dialogs__box" dialog-id="share_to_scm_select_friend_box" flyin-direction="bottom" style="width: 560px;"
  id="share_to_scm_select_friend_box">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title" style="flex-grow: 0; padding: 0px;">
        <s:text name="dyn.msg.center.selectFriend" />
      </div>
      <div class="dialogs__header_serch-box"
        style="display: flex; width: 160px; height: 32px; line-height: 32px; border: 1px solid #ddd; margin-right: 80px; border-radius: 3px;">
        <input type="text" class="dialogs__header_serch-tool dev_search_key" onkeyup="MsgBase.loadFriendList();"
          id="msgFriendMainInputId" placeholder="<s:text name="common.share.searchByName"/>"
          style="outline: none; padding-left: 2px; margin-left: 2px; border-style: none; width: 90%;">
      </div>
      <div class="dialogs__header_sort-box">
        <i class="material-icons dialogs__header_sort-box-flag">swap_vert</i> <span
          class="dialogs__header_sort-box_detaile" style="width: 110px;"><s:text name="common.share.dateOrder" /></span>
        <div class="dialogs__header_sort-box_item" style="width: 110px;">
          <span class="dialogs__header_sort-box_list" val="date" style="width: 100%;"><s:text
              name="common.share.dateOrder" /></span> <span class="dialogs__header_sort-box_list" val="name"
            style="width: 100%;"><s:text name="common.share.nameOrder" /></span>
        </div>
      </div>
      <i class="list-results_close" onclick="MsgBase.hideShareToScmSelectFriendBox()"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="flex-grow: 1;">
    <div class="dialogs__content" style="height: 480px;">
      <div class="friend-selection__box" id="id_grp_add_friend_names_list"></div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="MsgBase.clickFriendName()">
        <s:text name="dyn.msg.center.sure" />
      </button>
      <button class="button_main" onclick="MsgBase.hideShareToScmSelectFriendBox()">
        <s:text name="dyn.msg.center.cancel" />
      </button>
    </div>
  </div>
</div>