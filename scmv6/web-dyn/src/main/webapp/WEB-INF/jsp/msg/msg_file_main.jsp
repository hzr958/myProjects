<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="dialogs__box" style="width: 600px;" id="select_my_file_import" dialog-id="select_my_file_import_dialog"
  flyin-direction="bottom">
  <div class="dialogs__childbox_fixed" id="id_myfile_header">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name="dyn.msg.center.myFile" />
      </div>
      <div class="dialogs__header_searchbox" style="margin-right: 64px;">
        <div class="searchbox__container main-list__searchbox" list-search="myfilelist">
          <div class="searchbox__main">
            <input placeholder=" <s:text  name='dyn.msg.center.searchFile'/>" id="grp_file_search_my_file_key">
            <div class="searchbox__icon material-icons"></div>
          </div>
        </div>
      </div>
      <button class="button_main button_icon" onclick="MsgBase.hideMyFileSelectDialog()">
        <i class="list-results_close"></i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" id="psnFileMyFileListId" list-main="myfilelist">
      <!-- 我的文件列表 -->
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" disabled onclick="MsgBase.sendChatMsg(2,this)">
        <s:text name="dyn.msg.center.sure" />
      </button>
    </div>
  </div>
</div>
<div class="dialogs__box" dialog-id="msg_local_file_upload" flyin-direction="bottom" style="width: 480px; "
  id="msg_local_file_upload">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name="dyn.msg.center.chooseUploadFile" />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="overflow-y: inherit;">
    <div class="dialogs__content global__padding_24" style="padding-bottom: 0px !important"
      id="msg_local_fileupload_box">
      <form enctype="multipart/form-data" method="post">
        <div style="height: 160px; margin-bottom: 12px;">
          <div class="fileupload__box" maxlength="10"
        maxclass="share_msg_file_upload"></div>
        </div>
        <div class="form__sxn_row-list" style=" min-height: 48px; width: 100%; margin: 8px 0px"></div>
        <div class="form__sxn_row">
          <div class="input__box">
            <label class="input__title"> <s:text name="dyn.msg.center.filedesc" />
            </label>
            <div style="padding-bottom: 6px;"></div>
            <div class="input__area">
              <textarea id="msg_local_file_upload_content" class="dev_input-edit-area" maxlength="200" style="padding-top: 0px;"></textarea>
              <div class="textarea-autoresize-div"></div>
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" id="uploadfilebuttonId" onclick="MsgBase.uploadLocalFile()">
        <s:text name="dyn.msg.center.btn.uploadAndSend" />
      </button>
      <button class="dev_input-footer_delete  button_main " onclick="MsgBase.hideLocalFileSelectDialog(this)">
        <s:text name="dyn.msg.center.btn.cancel" />
      </button>
    </div>
  </div>
</div>
<div class="dialogs__box  dialogs__childbox_limited-normal" dialog-id="select_msg_file_dialog" flyin-direction="bottom" style="width: auto;"
  id="select_msg_file_dialog">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name="dyn.msg.center.selectFileFrom" />
      </div>
      <button class="button_main button_icon" onclick="MsgBase.hideMsgFileSelectDialog()">
        <i class="list-results_close"></i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted ">
    <div class="import-methods__box">
      <div class="import-methods__sxn" onclick="MsgBase.showLocalFileSelectDialog()">
        <div class="import-methods__sxn_logo file-import"></div>
        <div class="import-methods__sxn_name">
          <s:text name="dyn.msg.center.localFile" />
        </div>
        <div class="import-methods__sxn_explain">
          <s:text name="dyn.msg.center.localFile.desc" />
        </div>
      </div>
      <div class="import-methods__sxn" onclick="MsgBase.showMyFileSelectDialog()">
        <div class="import-methods__sxn_logo library-import"></div>
        <div class="import-methods__sxn_name">
          <s:text name="dyn.msg.center.myFile" />
        </div>
        <div class="import-methods__sxn_explain">
          <s:text name="dyn.msg.center.myFile.desc" />
        </div>
      </div>
    </div>
  </div>
</div>
