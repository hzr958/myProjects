<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<div class="project-edit_item-footer_header">
  <div class="project-edit_item-footer_header-left"><s:text name="projectEdit.label.otherFile.filename"/></div>
  <div class="project-edit_item-footer_header-right">
    <div class="handin_import-content_container-right_author-addbtn" onclick="ProjectAttachment.addPrjAttachment();" style="display: none;"><i class="handin_import-content_container-right_author-add"></i>添加</div>
    <input id="filedata1" type="file" name="filedata" style="display:none;">
  </div>
</div>
<div class="project-edit_item-footer_body">
  <!--  模板-->
  <!-- template-->
  <div class="handin_import-content_container-right_upload-box fileupload__box dev_fileupload__box" style="display: none" maxClass="prj_attachment_dev" maxlength="10" onclick="ProjectAttachment.fileuploadBoxOpenInputClick(event);" title="<s:text name="project.edit.upfileMsg"/>">
    <div class="fileupload__core initial_shown">
      <div class="fileupload__initial">
        <div class="pubv8-enter_add_file1_avator"></div>
        <div class="pubv8-enter_add_file2_avator"></div>
        <div class="fileupload__hint-text" style="left: -27px; bottom: -40px; width: 120px;">
          <s:text name="project.edit.selectFile"/></div>
        <input type="file" class="fileupload__input"  style="display: none;">
      </div>
      <div class="fileupload__progress">
        <canvas width="56" height="56"></canvas>
        <div class="fileupload__progress_text"></div>
      </div>
      <div class="fileupload__saving">
        <div class="preloader"></div>
        <div class="fileupload__saving-text"></div>
      </div>
      <div class="fileupload__finish"></div>
    </div>
  </div>
  <!--  模板-->
  <div  class="project-edit_item-footer_body-left" style="width: 14%;"></div>
  <div  class="project-edit_item-footer_body-right" id="projectAttachmentList">
    <div class="handin_import-content_container-right"  style="justify-content: flex-start; align-items: flex-start; display: flex;">
      <div class="upfile" filetype="fulltext">
        <div class="handin_import-content_container-right_upload-box fileupload__box"   maxClass="prj_attachment_dev" maxlength="10" onclick="ProjectAttachment.fileuploadBoxOpenInputClick(event);" title="<s:text name="project.edit.upfileMsg"/>">
          <div class="fileupload__core initial_shown">
            <div class="fileupload__initial">
                <div class="pubv8-enter_add_file1_avator"></div>
                <div class="pubv8-enter_add_file2_avator"></div>
              <div class="fileupload__hint-text" style="left: -27px; bottom: -40px; width: 120px;">
                <s:text name="project.edit.selectFile"/></div>
              <input type="file" class="fileupload__input"  style="display: none;">
            </div>
            <div class="fileupload__progress">
              <canvas width="56" height="56"></canvas>
              <div class="fileupload__progress_text"></div>
            </div>
            <div class="fileupload__saving">
              <div class="preloader"></div>
              <div class="fileupload__saving-text"></div>
            </div>
            <div class="fileupload__finish"></div>
          </div>
        </div>
      </div>
      <!--  模板 end-->
      <div class="handin_import-content_container-right_upload-item  prj_attachment_template" style="width:45%;display: none">
        <input type="hidden" id="_prj_attachments_prj_attachment_0_file_id"
               name="/prj_attachments/prj_attachment[0]/@file_id" value="" />
        <input type="hidden" id="_prj_attachments_prj_attachment_0_seq_no"
               name="/prj_attachments/prj_attachment[0]/@seq_no"  value="" />
        <input type="hidden" id="_prj_attachments_prj_attachment_0_file_ext"
               name="/prj_attachments/prj_attachment[0]/@file_ext" value="" />
        <input type="hidden" style="width: 85% !important;" maxlength="61" autocomplete="off" class="dev-detailinput_container full_width json_member_name "
               name="/prj_attachments/prj_attachment[0]/@file_name"
               id="_prj_attachments_prj_attachment_0_file_name"
               value=""  />
        <input type="hidden" name="/prj_attachments/prj_attachment[0]/@permission"
               id="_prj_attachments_prj_attachment_0_permission" value="2">
        <span class="handin_import-content_container-right_upload-item_detaile dev_prj_name_show"><a title=""></a></span>
        <i class="material-icons handin_import-content_upload-item_icon" onclick="ProjectAttachment.delPrjAttachment(this);">close</i>
        <%--<i class="selected-func_close" title="仅自己可以查看" onclick="ProjectAttachment.setPermission(this)"></i>--%>
      </div>
      <!--  模板-->
      <div class="project-edit_item-uploadfile_list"  id="prj_attachment_list" style="width: 100%;">
        <c:set value="0" var="index" scope="page" />
          <x:forEach select="$myoutput/data/prj_attachments/prj_attachment" var="attach">
            <c:set value="${index+1}" var="index" scope="page" />
            <c:choose>
              <c:when test="${index<10}">
                <c:set value="0" var="flag" scope="page" />
              </c:when>
              <c:otherwise>
                <c:set value="" var="flag" scope="page" />
              </c:otherwise>
            </c:choose>


            <c:set var="fileCode">
              <x:out select="$attach/@file_id" />
            </c:set>
            <c:set var="file_name">
              <x:out select="$attach/@file_name" escapeXml="false" />
            </c:set>
            <c:set var="fileExt">
              <x:out select="$attach/@file_ext" escapeXml="false" />
            </c:set>

            <c:set var="permission">
              <x:out select="$attach/@permission" />
            </c:set>

            <div class="handin_import-content_container-right_upload-item prj_attachment_dev" style="width:45%;">
              <input type="hidden" id="_prj_attachments_prj_attachment_${flag}${index}_file_id"
                     name="/prj_attachments/prj_attachment[${flag}${index}]/@file_id" value="${fileCode}" />
              <input type="hidden" id="_prj_attachments_prj_attachment_${flag}${index}_seq_no"
                     name="/prj_attachments/prj_attachment[${flag}${index}]/@seq_no"  value="${index}" />
              <input type="hidden" id="_prj_attachments_prj_attachment_${flag}${index}_file_ext"
                     name="/prj_attachments/prj_attachment[${flag}${index}]/@file_ext" value="${fileExt}" />
              <input type="hidden" style="width: 85% !important;" maxlength="61" autocomplete="off" class="dev-detailinput_container full_width json_member_name "
                     name="/prj_attachments/prj_attachment[${flag}${index}]/@file_name"
                     id="_prj_attachments_prj_attachment_${flag}${index}_file_name"
                     value="${file_name}"  />
              <span class="handin_import-content_container-right_upload-item_detaile dev_prj_name_show" onclick="ProjectEnter.link('${prjAttachDownload[fileCode]}',this,event);"><a title="${file_name}">${file_name}</a></span>
              <i class="material-icons handin_import-content_upload-item_icon" onclick="ProjectAttachment.delPrjAttachment(this);">close</i>
              <%--<i class="<c:if test="${permission != 2}">selected-func_close-open</c:if> <c:if test="${permission == 2}">selected-func_close</c:if>" title="<c:if test="${permission == 2}">仅自己可以查看</c:if><c:if test="${permission != 2}">所有人都可以查看</c:if>" onclick="ProjectAttachment.setPermission(this)">
              </i>--%>
              <input type="hidden" name="/prj_attachments/prj_attachment[${flag}${index}]/@permission"
                     id="_prj_attachments_prj_attachment_${flag}${index}_permission" value="2">
            </div>

          </x:forEach>
     </div>
    </div>
  </div>
</div>
