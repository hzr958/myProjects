<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(document).ready(function(){
	$("#msg_addfriend").keyup(function(){
	    var $prompt = $("#msg_friends").find(".chip-panel__Prompt");
	    var $chipbox = $("#msg_friends").find(".chip__box");
	    if($.trim($("#msg_addfriend").text()) =="" && $chipbox.length == 0){
	        $prompt.show();
	    }else{
	        $prompt.hide();
	    }
	});
	
	// 根据浏览器设置不同的属性 IE padding-top:2px;   火狐 padding-top:11px  谷歌 padding-top:8px;
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串 
	//判断是否Firefox浏览器  
	  if (userAgent.indexOf("Firefox") > -1) {  
	      $("#msg_addfriend").css("padding-top","11px");
	  }   
	  //判断是否chorme浏览器  
	  if (userAgent.indexOf("Chrome") > -1){  
		  $("#msg_addfriend").css("padding-top","8px");  
	  }  
	  //判断是否IE浏览器  
	  if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {  
		  $("#msg_addfriend").css("padding-top","2px");  
	  }  
	  //判断是否Edge浏览器  
	  if (userAgent.indexOf("Trident") > -1) {  
		  $("#msg_addfriend").css("padding-top","2px"); 
	  }; 
	  
	  $("#sendContent").keydown(function(event){
        if( event.keyCode == 13 && event.ctrlKey ){
            event.stopPropagation();
            event.preventDefault();
            $("#id_text_send_btn").click();
        }
        /*if(event.keyCode == 13){
          //var e = $(this).val();
          //$(this).val(e + '\n');
            console.log("keyCode")
          return;
        }*/
   });
	
});

function updateTips(){
    var $prompt = $("#msg_friends").find(".chip-panel__Prompt");
    var $chipbox = $("#msg_friends").find(".chip__box");
    if($.trim($("#msg_addfriend").text()) =="" && $chipbox.length == 0){
        $prompt.show();
    }else{
        $prompt.hide();
    }
}
</script>
<div class="inbox-messenger__box">
  <%--  <div class="inbox-messenger__banner">
      <button title="<s:text name='dyn.msg.center.searchFriend'/>" class="button_main button_icon inbox-messenger__create-new material-icons" onclick="MsgBase.createNewChat()">create</button>
    </div> --%>
  <div class="inbox-messenger__list-box">
    <div class="inbox-messenger__list-main" style="width: 304px;">
      <div class="inbox-messenger__search">
        <div class="searchbox__container main-list__searchbox"
          style="display: flex; align-items: center; justify-content: space-between;">
          <div class="searchbox__main">
            <input placeholder=" <s:text  name='dyn.msg.center.searchMsgPsn'/>" id="chatPsnSearchName">
            <!--  <div class="searchbox__icon material-icons"></div> -->
          </div>
          <i class="search-friend_container-tip" onclick="MsgBase.createNewChat()"
            title="<s:text name='dyn.msg.center.selectFriend'/>"></i>
        </div>
      </div>
      <div class="inbox-messenger__list">
        <div class="hidden-scrollbar__box">
          <ul class="main-list__list item_zero-space item_no-border" list-main="msg_chat_psn_list">
            <!-- 聊天-会话人员列表 -->
          </ul>
          <div id="msg_friend_all_list" style="display: none;">
            <div class="inbox-messenger__label" id="lab_friend">
              <s:text name='dyn.msg.center.searchMsgPsn.friend' />
            </div>
            <ul class="main-list__list item_zero-space item_no-border" list-main="msg_friend_psn_list">
              <!-- 聊天-联系人人员列表 -->
            </ul>
            <div class="inbox-messenger__label" id="lab_all">
              <s:text name='dyn.msg.center.searchMsgPsn.more' />
            </div>
            <ul class="main-list__list item_zero-space item_no-border" list-main="msg_all_psn_list">
              <!-- 聊天-全站人员列表 -->
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="inbox-messages__box">
  <div class="inbox-messages__banner">
    <div class="chip-panel__box inline-style" id="msg_friends" chipbox-id="msg_select_friends">
      <!-- chip-panel__manual-input -->
      <!-- <div id="msg_addfriend" class="chip-panel__manual-input"  contenteditable="true" manual-input="no" style="display: flex; align-items: center; padding-top: 12px;"></div>
          <div class="chip-panel__Prompt" style="position: absolute; color: rgb(204, 204, 204); left: 12px; line-height: 40px; z-index: 6; display: block;">
          <s:text name='dyn.msg.center.search3'/>
          </div> -->
      <input type="text" class="chip-panel__manual-input" id="msg_addfriend" manual-input="no"
        placeholder="<s:text name='dyn.msg.center.search3'/>"  style="width: 420px;">
      <div class="dev_msg_addfriend" style="margin-left: auto; margin-right: 2px; display: flex; align-items: center;">
        <button class="button_main button_primary" onclick="MsgBase.showShareToScmSelectFriendBox()">
          <s:text name="dyn.msg.center.selectFriend" />
        </button>
      </div>
    </div>
    <div class="inbox-messages__messenger-name" scm_chatPsnNo="" scm_id="chatPsnInfo"></div>
  </div>
  <div class="inbox-messages__chat-box">
    <div class="hidden-scrollbar__box" list-main="msg_chat_content_list">
      <!-- 聊天会话内容列表 -->
    </div>
  </div>
  <div class="inbox-messages__post" style="display: inline-block; border-top: none;">
    <div class="inbox-messages__post_actions">
      <div style="display: flex; margin: 0; padding: 0; width: 100%; background: #f4f4f4;">
        <!-- <span class="pub-idx_icon_pub-mark"></span> -->
        <button class="button_main button_icon button_light-grey button_send-specil_style"
          onclick="MsgBase.showMsgPubUI()" title="<s:text name='dyn.msg.center.sharePubs'/>">
          <span class="button_main-sendmessage_tip"></span>
        </button>
        <button class="button_main button_icon button_light-grey button_send-specil_style"
          onclick="MsgBase.showMsgFileSelectDialog()" title="<s:text name='dyn.msg.center.sendFiles'/>">
          <div class="button_main-addfile_tip"></div>
        </button>
      </div>
    </div>
    <div class="inbox-messages__post_content" style="margin-right: 0px;">
      <div class="form__sxn_row">
        <div class="input__box" style="max-height: 240px; overflow-y: auto; height: 81px;">
          <div class="input__area">
            <textarea style="border: none; min-height: 81px;" maxlength="500"
              placeholder="<s:text name='dyn.msg.center.shareWords' />" id="sendContent"></textarea>
            <div class="textarea-autoresize-div"></div>
          </div>
        </div>
      </div>
    </div>
    <div style="width: 100%; display: flex; justify-content: flex-end;">
      <div id="id_text_send_btn" class="inbox-messages_send-messagebtn" onclick="MsgBase.sendChatMsg(1,this)">
        <s:text name="dyn.msg.center.send" />
      </div>
    </div>
  </div>
</div>
<div class="ac__box" id="id_search_psn_list_ac__box"
  style="max-height: 358px; display: block; width: 200px; left: 644px; top: 124px; bottom: auto;">
  <div class="preloader_ind-linear" style="width: 100%;"></div>
  <div class="ac__list">
    <!-- 人员列表 -->
  </div>
</div>
