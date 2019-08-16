<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="handin_import-content_container-start">
  <div class="handin_import-content_container-left">
    <span class="handin_import-content_container-tip">*</span> <span> <c:if test="${pubVo.pubType != 1 }">
        <spring:message code="pub.enter.fulltext" />
      </c:if> <c:if test="${pubVo.pubType == 1 }">
        <spring:message code="pub.enter.awardfile" />
      </c:if>
    </span>
  </div>
  <!-- 上传全文 start -->
  <div class="handin_import-content_container-right"
    style="display: flex; justify-content: flex-start; align-items: flex-start;">
    <c:if test='${not empty pubVo.fullText.des3fileId }'>
      <div class="upfile" fileType="fulltext" style="display: none">
    </c:if>
    <c:if test='${empty pubVo.fullText.des3fileId }'>
      <div class="upfile" fileType="fulltext">
    </c:if>
    <div class="handin_import-content_container-right_upload-box fileupload__box"
      onclick="PubEdit.fileuploadBoxOpenInputClick(event);" title='<spring:message code="pub.enter.upfild"/>'>
      <div class="fileupload__core initial_shown  fileupload__position">
        <div class="fileupload__initial">
          <div class="pubv8-enter_add_file1_avator"></div> 
          <div class="pubv8-enter_add_file2_avator"></div>
          
          <input type="file" class="fileupload__input" id="fulltextFileInput" style='display: none;'>
        </div>
        <div class="fileupload__hint-text">
            <spring:message code="pub.enter.selectFile" />
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
    <c:if test='${not empty pubVo.fullText.des3fileId }'>
      <div class="handin_import-content_container-right_upload-item">
        <span class="handin_import-content_container-right_upload-item_detaile"> <a
          title="${pubVo.fullText.fileName }" onclick="PubEdit.link('${pubVo.simpleDownLoadUrl}',event);">${pubVo.fullText.fileName }</a></span>
        <i class="material-icons handin_import-content_upload-item_icon"
          onclick="PubEdit.deleteFileItem(this,'fulltext')">close</i> <input type="hidden"
          class="json_fulltext_des3fileId" name="des3fileId" value="${pubVo.fullText.des3fileId }" /> <i
          id="fullTextPic"
          class='selected-func_close <c:if test="${pubVo.fullText.permission==0 && pubVo.permission!=4}">selected-func_close-open</c:if> <c:if test="${pubVo.permission==4 }">selected-func_close-none</c:if>'
          onclick="PubEdit.changFulltextPermission(this)"
          title='<c:if test="${pubVo.fullText.permission==0 && pubVo.permission!=4}"><spring:message code="pub.permission.public" /></c:if><c:if test="${pubVo.fullText.permission!=0 && pubVo.permission!=4}"><spring:message code="pub.permission.onlyMe" /></c:if><c:if test="${pubVo.permission==4}"><spring:message code="pub.fulltext.private" /></c:if>'></i>
        <input type="hidden" class="json_fulltext_permission" name="permission" value="${pubVo.fullText.permission }" />
        <div class="handin_import-content_container-upload_again" onclick="PubEdit.resetUpFulltext()">
          <spring:message code="pub.enter.resetUpload" />
        </div>
      </div>
    </c:if>
    <input type="hidden" class="json_fulltext_fileName" name="fileName" value="${pubVo.fullText.fileName }">
  </div>
</div>
<!-- 上传全文 start -->
</div>