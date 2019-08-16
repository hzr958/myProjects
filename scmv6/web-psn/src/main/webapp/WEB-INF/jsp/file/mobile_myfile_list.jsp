<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="psnFileInfoList" var="sf">
  <div class="paper main-list__item" des3FileId="<iris:des3 code='${sf.fileId }'/>" onclick="midmsg.sendfilemsg(event)"
    style="display: flex;">
    <div class="paper_pic" style="width: 40px; height: 50px; background: url('');">
      <s:if test='#sf.fileType=="txt"  '>
        <img src="${resmod}/smate-pc/img/fileicon_txt.png" onerror="this.src='${resmod}/smate-pc/img/fileicon_txt.png'">
      </s:if>
      <s:elseif test='#sf.fileType=="ppt" || sf.fileType=="pptx" '>
        <img src="${resmod}/smate-pc/img/fileicon_ppt.png" onerror="this.src='${resmod}/smate-pc/img/fileicon_ppt.png'">
      </s:elseif>
      <s:elseif test='#sf.fileType=="doc" || sf.fileType=="docx" '>
        <img src="${resmod}/smate-pc/img/fileicon_doc.png" onerror="this.src='${resmod}/smate-pc/img/fileicon_doc.png'">
      </s:elseif>
      <s:elseif test='#sf.fileType=="rar" || sf.fileType=="zip" '>
        <img src="${resmod}/smate-pc/img/fileicon_zip.png" onerror="this.src='${resmod}/smate-pc/img/fileicon_zip.png'">
      </s:elseif>
      <s:elseif test='#sf.fileType=="xls" || sf.fileType=="xlsx" '>
        <img src="${resmod}/smate-pc/img/fileicon_xls.png" onerror="this.src='${resmod}/smate-pc/img/fileicon_xls.png'">
      </s:elseif>
      <s:elseif test='#sf.fileType=="pdf" '>
        <img src="${resmod}/smate-pc/img/fileicon_pdf.png" onerror="this.src='${resmod}/smate-pc/img/fileicon_pdf.png'">
      </s:elseif>
      <s:elseif test='#sf.fileType=="imgIc"  || sf.fileType=="jpg" || sf.fileType=="png" '>
        <img src="${sf.imgThumbUrl }" onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
      </s:elseif>
      <s:else>
        <img src="${resmod}/smate-pc/img/fileicon_default.png"
          onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
      </s:else>
    </div>
    <div class="paper_cont" style="margin-left: 12px; height: auto; width: 70vw;">
      <div class="pubTitle webkit-multipleline-ellipsis" href="javascript:void(0);">${sf.fileName}</div>
      <div class="p1" style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${sf.fileDesc}</div>
      <div class="p3 f999" style="color: #999;">
        <fmt:formatDate value="${sf.updateDate}" pattern="yyyy-MM-dd" />
      </div>
    </div>
  </div>
</s:iterator>