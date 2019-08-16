<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="dialogs__box dialogs__childbox_limited-huge" dialog-id="file_share_records">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='apps.files.showShare.his' />
      </div>
      <button class="button_main button_icon" onClick="VFileMain.hideShareRecordsUI(this)">
        <i class="list-results_close"></i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" list-main="sharerecords_list">
      <!-- 分享记录列表 -->
    </div>
  </div>
</div>
