<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<div class="project-edit_item-start">
  <!-- template-->
  <div class="handin_import-content_container-right_upload-box fileupload__box dev_pubfulltext_upload__box" style="display: none" onclick="ProjectFulltext.fileuploadBoxOpenInputClick(event);" title="<s:text name="project.edit.uploadFulltext"/>">
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

  <c:set var="fileId">
    <x:out select="$myoutput/data/prj_fulltext/@file_id" />
  </c:set>
  <c:set var="fileNodeId">
    <x:out select="$myoutput/data/prj_fulltext/@node_id" />
  </c:set>
  <c:if test="${empty fileNodeId }">
    <c:set var="fileNodeId" value="${currentNodeId }"></c:set>
  </c:if>
  <c:set var="fileName">
    <x:out escapeXml="false" select="$myoutput/data/prj_fulltext/@file_name" />
  </c:set>
  <c:set var="fileDesc">
    <x:out escapeXml="false" select="$myoutput/data/prj_fulltext/@file_desc" />
  </c:set>
  <c:set var="upload_date">
    <x:out select="$myoutput/data/prj_fulltext/@upload_date" />
  </c:set>
  <c:set var="file_ext">
    <x:out select="$myoutput/data/prj_fulltext/@file_ext" />
  </c:set>
  <c:set var="fileInsId">
    <x:out select="$myoutput/data/prj_fulltext/@ins_id" />
  </c:set>
  <c:set var="permission">
    <x:out select="$myoutput/data/prj_fulltext/@permission" />
  </c:set>
  <c:if test="${empty fileInsId}">
    <c:set var="fileInsId" value="${currentInsId }"></c:set>
  </c:if>
  <c:set var="style" value="display:none;"></c:set>
  <c:set var="uploadStyle" value=""></c:set>
  <c:if test="${!empty fileId}">
    <c:set var="style" value=""></c:set>
    <c:set var="uploadStyle" value="display:none;"></c:set>
  </c:if>


  <input type="hidden" id="_prj_fulltext_current_node_id" value="${currentNodeId }" />
  <input type="hidden" id="_prj_fulltext_current_ins_id" value="${currentInsId }" />
  <input type="hidden" id="_prj_fulltext_node_id" name="/prj_fulltext/@node_id" value="${fileNodeId }" />
  <input type="hidden"id="_prj_fulltext_file_id" name="/prj_fulltext/@file_id" value="${fileId }" />
  <input type="hidden"id="_prj_fulltext_upload_date" name="/prj_fulltext/@upload_date" value="${upload_date}" />
  <input type="hidden"id="_prj_fulltext_file_ext" name="/prj_fulltext/@file_ext" value="${file_ext}" />
  <input type="hidden"id="_prj_fulltext_ins_id" name="/prj_fulltext/@ins_id" value="${fileInsId}" />
  <input type="hidden"class="inp_text" maxlength="200" style="width: 244px" id="_prj_fulltext_file_name"name="/prj_fulltext/@file_name"
         value="<c:out value="${fileName}"/>" />
  <div class="project-edit_item-title">
    <span><s:text name="project.edit.fulltext"/></span>
  </div>
  <div class="handin_import-content_container-right" id="upload_fulltext_view" style="display: none; justify-content: flex-start; align-items: flex-start;">
    <div class="upfile" filetype="fulltext">
      <div class="handin_import-content_container-right_upload-box fileupload__box" onclick="ProjectFulltext.fileuploadBoxOpenInputClick(event);" title="<s:text name="project.edit.uploadFulltext"/>" style="margin-left: 3px;">
        <div class="fileupload__core initial_shown">
          <div class="fileupload__initial">
              <div class="pubv8-enter_add_file1_avator"></div>
              <div class="pubv8-enter_add_file2_avator"></div>
              <div class="fileupload__hint-text" style="left: -27px; bottom: -40px; width: 120px;"><s:text name="project.edit.selectFile"/></div>
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
    <div class="handin_import-content_container-right_upload-container dev_upfiled_list" style="margin-top: -7px;">
      <input type="hidden" class="json_fulltext_fileName" name="fileName" value="">
    </div>
  </div>

  <div class="handin_import-content_container-right_upload-container dev_upfiled_list"
       id="upload_fulltext_content" style="margin-top: -7px;">
    <div class="handin_import-content_container-right_upload-item">
		<span class="handin_import-content_container-right_upload-item_detaile">
			<a title="<c:out value="${fileName}" escapeXml="false"/>" id="fulltext_link" href="" onclick="ProjectEnter.link('${downloadUrl }',this,event)">
				<c:out value="${fileName}" escapeXml="false"/>
			</a>
		</span>
      <i class="material-icons handin_import-content_upload-item_icon" onclick="ProjectFulltext.deleteFulltext(this)">
        close
      </i>
      <%--<i id="jsFullTextPic"  class="selected-func_close selected-func_close-open"
         onclick="ProjectFulltext.changFulltextPermission(this)" title="所有人都可以查看">
      </i>--%>
      <input type="hidden" class="json_fulltext_permission" id="fulltext_permission"
             name="/prj_fulltext/@permission" value="2">
      <div class="handin_import-content_container-upload_again" onclick="ProjectFulltext.resetUpFulltext(this)" can="true">
        <s:text name="project.edit.resetUpload"/>
      </div>
    </div>
  </div>
</div>
