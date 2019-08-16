
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="dialogs__box dialogs__childbox_limited-biggest" style="width: 600px;" id="select_my_pub_import" dialog-id="select_my_pub_import_dialog"
  flyin-direction="bottom">
  <div class="dialogs__childbox_fixed" id="id_mypub_header">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name="dyn.msg.center.myPub" />
      </div>
      <div class="dialogs__header_searchbox" style="margin-right: 64px;">
        <div class="searchbox__container main-list__searchbox" list-search="mypublist">
          <div class="searchbox__main">
            <input placeholder=" <s:text name='dyn.msg.center.searchPub' />" id="search_my_pub_key">
            <div class="searchbox__icon material-icons"></div>
          </div>
        </div>
      </div>
      <i class="list-results_close" onclick="MsgBase.hideMsgPubUI();"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" id="myPubListId" list-main="mypublist">
      <!-- 我的成果列表 -->
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" disabled onclick="MsgBase.sendChatMsg(3,this)">
        <s:text name="dyn.msg.center.sure" />
      </button>
    </div>
  </div>
</div>