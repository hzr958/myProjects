<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="float_div_table" id="float_div_table" style="width: 776px;">
  <div class="contant-maintitle">
    <div class="bargain">
      <span class="bargain-icon"><i class="icon01 py-icon"></i> <s:text name="group.table.title.publication" /></span>
      <c:if test="${page.totalCount > 0 }">
        <span class="f8080">(<s:text name="group.table.title.publication.tip" /><span class="cuti">${page.totalCount}</span>)
        </span>
      </c:if>
    </div>
    <%-- 非群组成员不能提供操作 --%>
    <s:if test="groupInvitePsn != null">
      <div class="btn-wrapright">
        <input type="hidden" alt="${snsctx}/dynamic/ajaxShareMaint?TB_iframe=true&height=570&width=720"
          title="<s:text name="dyn.common.label.share"/>" class="thickbox" id="pubShareBtn" /> <input type="button"
          style="display: none" id="export_publication_op" title="<s:text name="group.title.exportPub"/>"
          class="thickbox" alt="#TB_inline?height=259&amp;width=600&amp;inlineId=output_pub_thckboxDiv" />
        <!-- 检索成果 -->
        <s:if test="groupPsn.pubViewType==1 || groupInvitePsn.groupRole==1 || groupInvitePsn.groupRole==2">
          <a class="uiButton thickbox" id="search_publication_togroup_op"
            title="<s:text name='group.btn.publication.search' />"><s:text name="group.btn.publication.search" /></a>
        </s:if>
        <!-- 导入成果 -->
        <s:if test="groupPsn.pubViewType==1 || groupInvitePsn.groupRole==1 || groupInvitePsn.groupRole==2">
          <a
            href="/pubweb/group/ajaxMyPubList?articleType=1&groupId=${groupPsn.groupId}&TB_iframe=true&height=435&width=800"
            class="uiButton thickbox" title="<s:text name='group.btn.publication.import' />" id="importLink"><s:text
              name="group.btn.publication.import" /></a>
        </s:if>
        <!-- 删除 -->
        <a class="uiButton" id="group_remove_fromgroup_op"><s:text name='group.btn.more.remove' /></a>
        <!-- 分享 -->
        <a class="uiButton" onclick="Group.publication.sharePublication()" title="<s:text name='group.btn.share' />"><s:text
            name="group.btn.share" /></a>
        <div class="tc_box" style="display: none" id="sharebtnlist">
          <ul>
            <%-- 指定联系人 --%>
            <li><a id="dynsharefrd" onclick="Group.publication.shareFriend()"
              title="<s:text name="sns.share.btnlist.btn1" />"><s:text name="sns.share.btnlist.btn1" /></a></li>
          </ul>
        </div>
        &nbsp; <a class="uiButton uiSelectorButton" title="<s:text name='group.btn.more' />" id="more_operation_btn"><s:text
            name="group.btn.more" /></a>
      </div>
      <!-- 更多操作 -->
      <div class="tc_box" style="display: none" id="more_operation">
        <ul>
          <s:if test="groupPsn.pubViewType==1 || groupInvitePsn.groupRole==1 || groupInvitePsn.groupRole==2">
            <li><a id="group_enter_newpub_op"><s:text name="group.btn.more.manually" /></a></li>
          </s:if>
          <!-- fzq意见 该功能重复此处不再显示 <li><a  id="group_pub_comment_op">评价成果</a>
				</li> -->
          <li><a id="group_import_to_mypub_op"><s:text name="group.btn.more.publication.importTo" /></a></li>
          <li><a id="group_import_to_myref_op"><s:text name="group.btn.more.reference.importTo" /></a></li>
          <li><a id="group_modify_op"><s:text name="group.btn.more.update" /></a></li>
          <li><a id="export_file_btn" title="<s:text name='group.btn.exportPub' />"><s:text
                name="group.btn.exportPub" /></a></li>
          <li><a id="add_to_folder_btn"><s:text name="group.btn.more.addToTag" /></a> <input type="hidden"
            id="showAddToFolderDiv" class="thickbox" title="<s:text name='group.btn.more.addToTag' />" /></li>
          <c:if
            test="${leftMenuId != null && leftMenuId != '' && leftMenuId != 'menu-mine' && leftMenuId != 'menu-dd-folder-else' && fn:contains(leftMenuId, 'menu-dd-f')}">
            <li><a id="delete_to_folder_btn"><s:text name="group.btn.more.removeToTag" /></a></li>
          </c:if>
        </ul>
      </div>
    </s:if>
  </div>
  <s:if test="page.totalCount > 0">
    <div class="table_header">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="t_css">
        <tr class="t_tr">
          <td width="5%" align="center"><input type="checkbox" name="checkbox" id="checkbox"
            onclick="Group.publication.selectAll(this)" /></td>
          <td align="left"><s:text name="group.table.head.author" />&nbsp;/&nbsp;<a style="cursor: pointer;"
            id="maint_grid_head_title" class="Blue Drop-down headOrder" order="asc" orderBy="title"><s:text
                name="group.table.head.title" /></a>&nbsp;/&nbsp;<s:text name="group.table.head.source" />&nbsp;/&nbsp;<a
            style="cursor: pointer;" class="Blue Drop-down headOrder" order="asc" orderBy="publishYear"><s:text
                name="group.table.head.publicatedDate" /></a></td>
          <td width="15%" align="center"><a style="cursor: pointer;" class="Blue Drop-down headOrder" order="asc"
            orderBy="citedList"><s:text name="group.table.head.timesCited" /></a></td>
        </tr>
      </table>
    </div>
  </s:if>
</div>
<s:if test="page.totalCount > 0">
  <div style="height: 72px;">&nbsp;</div>
</s:if>
<s:else>
  <div style="height: 45px;">&nbsp;</div>
</s:else>
<%-- 加入标签 --%>
<div id="pubAddFolderDiv" style="display: none;">
  <div class="dialog_content">
    <div class="new-label">
      <input maxlength="20" type="text" class="inp_text Fleft inp_bg1" id="tag-input-new" style="width: 250px;" /> <a
        onclick="scmLeftMenu.addMenu(this,'tag-dl','${snsctx}/group/ajaxCreateGroupFolder')" class="uiButton f_normal"
        style="margin-bottom: 3px; margin-left: 5px;"><s:text name="action.label.new.tagname" /> </a>
    </div>
    <ul class="label-list addFolderUl" style="height: 120px; overflow-x: no; overflow-y: auto">
      <s:iterator value="groupFolderList" id="groupFolder">
        <li id="edittag_title_${groupFolder.groupFolderId}" title="<s:property value="#groupFolder.folderName" />">
          <span class="Fleft"> <input id="box_${groupFolder.groupFolderId}" type="checkbox" name="moveToFolders"
            value="<s:property value="#groupFolder.groupFolderId"/>"> <img id="img_${groupFolder.groupFolderId}"
            style="display: none" class="checkbox_status" src="${ressns}/images/checkbox-3status.gif">
        </span> <span id="edittag_lable_${groupFolder.groupFolderId}" class="lable-nr"><s:property
              value="#groupFolder.folderName" /></span>
        </li>
      </s:iterator>
    </ul>
  </div>
  <div class="pop_buttom">
    <a onclick="movePubsToFolder()" class="uiButton text14 uiButtonConfirm"><s:text name="person.label.submit" /></a> <a
      onclick="$.Thickbox.closeWin()" class="uiButton text14 mright10"><s:text name="group.res.pubs.cancel" /></a>
  </div>
</div>
<%-- 导出文件 --%>
<div style="display: none;" id="output_pub_thckboxDiv">
  <div class="dialog_content">
    <ul class="gain_nr">
      <li>
        <p class="cuf333">
          <s:text name="group.res.pubs.export.fields" />
        </p>
        <p>
          <input type="radio" name="export_Fields" value="table_fields" checked="checked" onclick="txtDisabled(1)"
            class="fieldClass" />&nbsp;
          <s:text name="group.res.pubs.export.tab_fields" />
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="radio" name="export_Fields" value="all_fields"
            onclick="txtDisabled(2)" class="fieldClass" />&nbsp;
          <s:text name="group.res.pubs.export.all_fields" />
        </p>
      </li>
      <li>
        <p class="cuf333">
          <s:text name="group.res.pubs.export.type" />
        </p>
        <p>
          <input type="radio" name="export_Type" id="word_check" value="word" checked="checked" />&nbsp;MS
          Word&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="radio" name="export_Type" value="excel" />&nbsp;MS
          Excel&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="radio" name="export_Type" value="pdf" />&nbsp;PDF&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <span id="export_endnote" title="<s:text name='action.label.export.at7' />"><input name="export_Type"
            value="endnotetxt" type="radio" class="txtClass"> &nbsp;EndNote(txt)(<s:text
              name="action.label.export.at7" />)</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span id="export_refwork"
            title="<s:text name='action.label.export.at8' />"><input name="export_Type" value="refworktxt"
            type="radio" class="txtClass"> &nbsp;Refwork(txt)</span> <br />
      </li>
      <li style="border-bottom: 0; margin-bottom: 0px; padding-bottom: 0px; font-size: 14px;" class="f999"><s:text
          name="group.res.pubs.export.tip" /></li>
    </ul>
  </div>
  <div class="pop_buttom">
    <a onclick="exportPublication();" class="uiButton uiButtonConfirm text14" id="uiButtonConfirm"><s:text
        name="group.res.pubs.button.export" /></a> <a onclick="$.Thickbox.closeWin();" class="uiButton text14 mright10"
      id="uiButtonConfirm"><s:text name="group.res.pubs.cancel" /></a>
  </div>
</div>
<%-- 导入到我的成果库弹出DIV--%>
<input type="hidden" id="detail_import_title" class="thickbox" name="detail_import_title"
  title="<s:text name="publication.import.label.title"/>" />
<div id="detail_import_div" style="padding: 0px; margin: 0px; display: none"></div>
<script type="text/javascript">
$(document).ready(function(){
	//浮动表头
	$("#float_div_table").capacityFixed();
	
	$(".fieldClass").each(function(){
		if($(this).val()=='table_fields'){
			txtDisabled(1);
		}
	})
});

function txtDisabled(flag){
	$(".txtClass").each(function(){
		if(flag==1){
			$(this).attr("checked",false);
		    $(this).attr("disabled","disabled");
		}else{
			$(this).attr("disabled",false);
		}
	});

	if(!$('input:radio[name="export_Type"]:checked').val()) {
		$("#word_check").attr("checked","checked");
	}
}
</script>