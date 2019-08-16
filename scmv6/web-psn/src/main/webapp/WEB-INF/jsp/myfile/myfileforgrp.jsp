<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="psnFileInfoList" var="sf">
  <div class="main-list__item" stationFileId="${sf.fileId }" des3FileId="<iris:des3 code='${sf.fileId }'/>">
    <div class="main-list__item_checkbox">
      <div class="input-custom-style">
        <s:if test="#sf.isGrpFile == true ">
          <input type="checkbox" name="pub-type" checked="checked" disabled="true" style="outline: 1px solid red;">
        </s:if>
        <s:else>
          <input type="checkbox" name="pub-type" style="background-color: red">
        </s:else>
        <i class="material-icons custom-style"></i>
      </div>
    </div>
    <div class="main-list__item_content">
      <div class="file-idx_medium">
        <div class="file-idx__base-info">
          <div class="file-idx__snap_box">
            <a onclick="BaseUtils.fileDown('${sf.downloadUrl }',this,event)">
              <div class="file-idx__snap_img">
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
            <div class="file-idx__main">
              <div class="file-idx__main_title"
                style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 370px; display: block;">
                <a onclick="BaseUtils.fileDown('${sf.downloadUrl }',this,event)">${sf.fileName}</a>
              </div>
              <div class="file-idx__main_intro"
                style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 370px; display: block;">${sf.fileDesc}</div>
              <div class="file-idx__main_src">
                <fmt:formatDate value="${  sf.updateDate}" pattern="yyyy-MM-dd" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>
