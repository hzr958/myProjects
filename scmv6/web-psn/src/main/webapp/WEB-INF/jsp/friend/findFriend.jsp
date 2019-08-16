<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${resmod }/js/search/mobile.search.scroll.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript">
$(function(){
	findPsn.bindSearch();
	var objDiv = $("#div_preloader");
	/* objDiv.doLoadStateIco({
		style:"height:28px; width:28px; margin:auto;margin-top:24px;",
		status:1
	}); */
	findPsn.listScroll();
}) 
function keypress(){
	var replyContent=$.trim($("#search_psn_input").val());
	if(replyContent ==''){
		$("#addFriendBtn").attr("disabled", "disabled");
	}
}
</script>
<input type="hidden" name="hasNextPage" id="hasNextPage" value="true" />
<input type="hidden" name="selectedPsnIds" id="selectedPsnIds" value="" />
<div class="dialogs__box" style="width: 800px;" dialog-id="find_friend_dialog" id="find_friend_dialog">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="display: flex; align-items: center;">
      <div class="dialogs__header_title">
        <s:text name='friend.findfriend.invite' />
      </div>
      <div style="margin-right: 16px; display: flex;">
        <i class="list-results_close add__friend-close" onclick="findPsn.hideFindBox('find_friend_dialog');"></i>
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="input__area  input_no-border dialogs__header_title">
        <input id="search_psn_input" type="search" placeholder="<s:text name='friend.findfriend.note.search'/>"
          onKeyUp="keypress()">
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 2560px;" id="find_psn_div">
    <div class="connections__invite-friends_box" style="display: none;" id="inviteDiv">
      <div class="connections__invite-friends_text">
        <s:text name='friend.findfriend.invitenote1' />
        &nbsp;<span id="emailNoteDiv"></span>&nbsp;
        <s:text name='friend.findfriend.invitenote2' />
      </div>
      <button class="button_main button_primary-reverse" onclick="Friend.sendMail();">
        <s:text name="friend.findfriend.btn.invite" />
      </button>
    </div>
    <div class="connections__invite-friends_box" style="display: none;" id="inviteFrdDiv">
      <div class="connections__invite-friends_text">
        <s:text name='friend.findfriend.invitenote3' />
      </div>
    </div>
    <!-- <div class="friend-selection__box" id="find_psn_items"> -->
    <div class="main-list__list item_no-border" id="find_psn_items"></div>
    <div id="div_preloader"></div>
  </div>
  <%-- <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" id="addFriendBtn" disabled="disabled" onclick="findPsn.addFriend()"><s:text name='friend.findfriend.btn.addfriend'/></button>
      <button class="button_main" onclick="findPsn.hideFindBox('find_friend_dialog');"><s:text name='friend.findfriend.btn.cancel'/></button>
    </div>
  </div> --%>
  <input id="pageNo" name="page.pageNo" type="hidden" value="${page.pageNo}" />
</div>