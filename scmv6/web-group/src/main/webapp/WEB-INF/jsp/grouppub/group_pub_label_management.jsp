<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="groupFolderManageDiv" style="display: none;">
  <div class="dialog_content1">
    <div class="new-label">
      <input maxlength="50" type="text" class="inp_text Fleft inp_bg1" id="tag-input" style="width: 250px;" /> <a
        onclick="scmLeftMenu.addMenu(this,'tag-dl','${snsctx}/group/ajaxCreateGroupFolder')" class="uiButton f_normal"
        style="margin-bottom: 3px; margin-left: 5px;"><s:text name="dialog.manageTag.btn.addATag" /></a>
    </div>
    <ul class="label-list manageFolderUl" style="height: 130px; overflow-x: no; overflow-y: auto">
      <s:iterator value="groupFolderList" var="item" status="idx">
        <li><span class="lable-nr" id="menu-label${groupFolderId}"
          title="<s:if test="psnId != createPsnId && groupInvitePsn.groupRole==3"><s:text name="group.tip.noPermission"/></s:if>">${fn:escapeXml(folderName)}</span>
          <input maxlength="50" type="text" class="inp_text Fleft menu-label" value="${fn:escapeXml(folderName)}"
          id="menu-input${groupFolderId}" itemId="${groupFolderId}"
          onblur="scmLeftMenu.handlerEditMenu('${groupFolderId}','${snsctx}/group/ajaxUpdateGroupFolder')"
          style="width: 127px; margin-left: 3px; display: none" /> <s:if
            test="psnId == createPsnId || groupInvitePsn.groupRole!=3">
            <i class="icon_edit" style="float: left; cursor: pointer;"
              onclick="scmLeftMenu.editMenu('${groupFolderId}')"></i>
            <a href="#"
              onclick='scmLeftMenu.deleteMenu(this,"${groupFolderId}","${snsctx}/group/ajaxDeleteGroupFolder")'
              class="pop-delete"></a>
          </s:if></li>
      </s:iterator>
    </ul>
    <div style="border-top: 1px dashed #cdcdcd; color: #999; padding-top: 6px; margin-top: 6px;">
      <span class="red">*</span>
      <s:text name="dialog.manageTag.tip.modify" />
    </div>
  </div>
  <div class="pop_buttom">
    <a onclick="$.Thickbox.closeWin()" class="uiButton text14 mright10"><s:text name="dialog.manageTag.btn.close" /></a>
  </div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$(".menu-label").live('keydown',function(e){
		var itemId = $(this).attr("itemId");
		var curKey = e.which;
		if(curKey == 13){
			scmLeftMenu.handlerEditMenu(itemId,'${snsctx}/group/ajaxUpdateGroupFolder');
			return false;
		}
	});
});
</script>