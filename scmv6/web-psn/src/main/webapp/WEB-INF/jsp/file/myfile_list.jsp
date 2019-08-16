<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
 $(document).ready(function(){
		addFormElementsEvents();
	})	
  </script>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="psnFileInfoList" var="sf">
  <div class="main-list__item" stationFileId="${sf.fileId }" drawer-id="<iris:des3 code='${sf.fileId }'/>">
    <div class="main-list__item_checkbox">
      <div class="input-custom-style">
        <input type="checkbox" name="pub-type"> <i class="material-icons custom-style"></i>
      </div>
    </div>
    <div class="main-list__item_content">
      <div class="file-idx_medium">
        <div class="file-idx__base-info">
          <div class="file-idx__snap_box">
            <a onclick="BaseUtils.fileDown('${sf.downloadUrl }',this,event)">
              <div class="file-idx__snap_img" style="cursor: pointer;">
                <s:if test='#sf.fileType=="txt"  '>
                  <img src="${resmod}/smate-pc/img/fileicon_txt.png"
                    onerror="this.src='${resmod}/smate-pc/img/fileicon_txt.png'">
                </s:if>
                <s:elseif test='#sf.fileType=="ppt" || sf.fileType=="pptx" '>
                  <img src="${resmod}/smate-pc/img/fileicon_ppt.png"
                    onerror="this.src='${resmod}/smate-pc/img/fileicon_ppt.png'">
                </s:elseif>
                <s:elseif test='#sf.fileType=="doc" || sf.fileType=="docx" '>
                  <img src="${resmod}/smate-pc/img/fileicon_doc.png"
                    onerror="this.src='${resmod}/smate-pc/img/fileicon_doc.png'">
                </s:elseif>
                <s:elseif test='#sf.fileType=="rar" || sf.fileType=="zip" '>
                  <img src="${resmod}/smate-pc/img/fileicon_zip.png"
                    onerror="this.src='${resmod}/smate-pc/img/fileicon_zip.png'">
                </s:elseif>
                <s:elseif test='#sf.fileType=="xls" || sf.fileType=="xlsx" '>
                  <img src="${resmod}/smate-pc/img/fileicon_xls.png"
                    onerror="this.src='${resmod}/smate-pc/img/fileicon_xls.png'">
                </s:elseif>
                <s:elseif test='#sf.fileType=="pdf" '>
                  <img src="${resmod}/smate-pc/img/fileicon_pdf.png"
                    onerror="this.src='${resmod}/smate-pc/img/fileicon_pdf.png'">
                </s:elseif>
                <s:elseif test='#sf.fileType=="imgIc"  || sf.fileType=="jpg" || sf.fileType=="png" '>
                  <img src="${sf.imgThumbUrl }" onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                </s:elseif>
                <s:else>
                  <img src="${resmod}/smate-pc/img/fileicon_default.png"
                    onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                </s:else>
              </div>
            </a>
          </div>
          <div class="file-idx__main_box">
            <div class="file-idx__main" style="width: 530px;">
              <div class="file-idx__main_title pub-idx__main_title-multipleline" style="height: 40px; overflow: hidden;">
                <a class="multipleline-ellipsis__content-box" onclick="BaseUtils.fileDown('${sf.downloadUrl }',this,event)" title="${sf.fileName}" style="max-width: 520px; margin-left: 1px!important;">${sf.fileName}</a>
              </div>
              <div class="file-idx__main_intro multipleline-ellipsis">
                <div class="multipleline-ellipsis__content-box" style="margin-left: -2px !important;">${sf.fileDesc}</div>
              </div>
              <ul class="idx-social__list">
                <li class="idx-social__item" onclick="SmateShare.getPsnFileListSareParam(this);initSharePlugin();"><s:text
                    name='apps.files.list.share' /></li>
                <li class="idx-social__item" onclick="VFileMain.showFileEdit('<iris:des3 code="${sf.fileId }"/>',this)"><s:text
                    name='apps.files.list.edit' /></li>
                <li class="idx-social__item"
                  onclick="VFileMain.delMyFileConfirm('<iris:des3 code="${sf.fileId }"/>',this)"><s:text
                    name='apps.files.list.delete' /></li>
              </ul>
            </div>
            <div class="file-idx__main_src">
              <div class="file-idx__src_time">
                <fmt:formatDate value="${  sf.updateDate}" pattern="yyyy-MM-dd" />
              </div>
              <div class="file-idx__src_uploader"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>