<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="main-list__header">
  <div class="main-list__header_title">
    <nav class="nav_horiz">
      <ul class="nav__list">
        <li class="nav__item item_selected" onclick="MsgBase.showFulltextRequestMsgList();"><s:text
            name='dyn.msg.center.requestFulltextTitle' /></li>
        <!--  <li class="nav__item">发送的全文请求</li> -->
      </ul>
      <div class="nav__underline"></div>
    </nav>
  </div>
</div>
<div class="main-list__list" id="fulltextRequestListId" list-main="fulltextRequestList"></div>
<div class="dialogs__box" dialog-id="msg_center_fulltext_upload" flyin-direction="bottom" style="width: 480px"
  id="msg_center_fulltext_upload_id">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='dyn.msg.center.uploadFulltext' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content global__padding_24" style="padding-bottom: 0px !important" id="fileupload">
      <form enctype="multipart/form-data" method="post">
        <div style="height: 160px; margin-bottom: 48px;">
          <div class="fileupload__box"></div>
        </div>
        <!-- <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title">文件描述</label>
            <div class="input__area">
              <textarea id="psn_file_upload_file_content"  class="dev_input-edit-area" maxlength="200"></textarea>
              <div class="textarea-autoresize-div"></div>
            </div>
            <div class="input__helper"></div>
          </div>
        </div> -->
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" id="uploadfilebuttonId"
        onclick="MsgBase.uploadFulltextButton();">
        <s:text name='dyn.msg.center.btn.upload' />
      </button>
      <button class="dev_input-footer_delete  button_main " onclick="MsgBase.cancleFulltextUpload(this);">
        <s:text name='dyn.msg.center.btn.cancel' />
      </button>
    </div>
  </div>
</div>