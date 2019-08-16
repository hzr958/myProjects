<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="main-list__header" list-filter="msglist">
  <button class="button_main button_grey" onclick="MsgBase.setReadMsg(1)">
    <s:text name="dyn.msg.center.flagReaded" />
  </button>
  <div class="main-list__header_title" filter-section="status" filter-method="single">
    <!--  <button class="button_main button_primary" filter-value="" onclick="MsgBase.showMsgList(1)"><s:text name="dyn.msg.center.all" /></button>
      <button class="button_main button_grey" filter-value="0" onclick="MsgBase.showMsgList(0)"><s:text name="dyn.msg.center.unread" /></button> -->
  </div>
</div>
<div class="main-list__list item_no-padding" list-main="msglist">
  <!-- 消息列表 -->
</div>
