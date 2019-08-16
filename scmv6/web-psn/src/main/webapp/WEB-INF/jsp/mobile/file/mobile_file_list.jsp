<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="psnFileInfoList" var="sf">
  <div class="main-list__item" onclick="downloadFulltext('${sf.downloadUrl }')" style="padding: 0px; width: 100%; margin: 0px;">
    <div class="page-folder__list" style="width: 100%; padding: 16px 16px 8px 16px!important; margin: 0px;">
      <div style="display: flex;">
        <s:if test='#sf.fileType=="txt"  '>
          <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_txt.png"
            onerror="this.src='${resmod}/smate-pc/img/fileicon_txt.png'">
        </s:if>
        <s:elseif test='#sf.fileType=="ppt" || sf.fileType=="pptx" '>
          <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_ppt.png"
            onerror="this.src='${resmod}/smate-pc/img/fileicon_ppt.png'">
        </s:elseif>
        <s:elseif test='#sf.fileType=="doc" || sf.fileType=="docx" '>
          <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_doc.png"
            onerror="this.src='${resmod}/smate-pc/img/fileicon_doc.png'">
        </s:elseif>
        <s:elseif test='#sf.fileType=="rar" || sf.fileType=="zip" '>
          <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_zip.png"
            onerror="this.src='${resmod}/smate-pc/img/fileicon_zip.png'">
        </s:elseif>
        <s:elseif test='#sf.fileType=="xls" || sf.fileType=="xlsx" '>
          <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_xls.png"
            onerror="this.src='${resmod}/smate-pc/img/fileicon_xls.png'">
        </s:elseif>
        <s:elseif test='#sf.fileType=="pdf" '>
          <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_pdf.png"
            onerror="this.src='${resmod}/smate-pc/img/fileicon_pdf.png'">
        </s:elseif>
        <s:elseif test='#sf.fileType=="imgIc"  || sf.fileType=="jpg" || sf.fileType=="png" '>
          <img class="page-folder__list-avator" src="${sf.imgThumbUrl }"
            onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
        </s:elseif>
        <s:else>
          <img class="page-folder__list-avator" src="${resmod}/smate-pc/img/fileicon_default.png"
            onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
        </s:else>
        <div class="page-folder__left">
          <div class="page-header__work">${sf.fileName}</div>
          <div class="page-folder__footer">${sf.fileDesc}</div>
        </div>
      </div>
      <div class="page-folder__right">
        <div class="page-header__work">
          <fmt:formatDate value="${sf.uploadDate}" pattern="yyyy-MM-dd" />
        </div>
        <div class="page-folder__footer">${sf.fileSize}</div>
      </div>
    </div>
  </div>
</s:iterator>