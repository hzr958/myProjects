<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	
	 $("#grp_params").attr("smate_des3_grp_id" ,"${des3GrpId}") ;

});
</script>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="GrpFileShowInfos" var="grpFile">
  <div class="main-list__item" drawer-id="<iris:des3 code="${grpFile.grpFileId}"></iris:des3>"
    fileid="<iris:des3 code="${grpFile.grpFileId}"></iris:des3>" grpid="${grpId}"
    archivefileid="${ grpFile.archiveFileId}">
    <div class="main-list__item_content">
      <div class="file-idx_medium">
        <div class="file-idx__base-info">
          <div class="file-idx__snap_box">
            <div class="file-idx__snap_img">
              <s:if test='fileType=="txt"  '>
                <img src="${resmod}/smate-pc/img/fileicon_txt.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_txt.png'" onclick="GrpFile.loginFromOutside()">
              </s:if>
              <s:elseif test='fileType=="ppt" || fileType=="pptx" '>
                <img src="${resmod}/smate-pc/img/fileicon_ppt.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_ppt.png'" onclick="GrpFile.loginFromOutside()">
              </s:elseif>
              <s:elseif test='fileType=="doc" || fileType=="docx" '>
                <img src="${resmod}/smate-pc/img/fileicon_doc.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_doc.png'" onclick="GrpFile.loginFromOutside()">
              </s:elseif>
              <s:elseif test='fileType=="rar" || fileType=="zip" '>
                <img src="${resmod}/smate-pc/img/fileicon_zip.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_zip.png'" onclick="GrpFile.loginFromOutside()">
              </s:elseif>
              <s:elseif test='fileType=="xls" || fileType=="xlsx" '>
                <img src="${resmod}/smate-pc/img/fileicon_xls.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_xls.png'" onclick="GrpFile.loginFromOutside()">
              </s:elseif>
              <s:elseif test='fileType=="pdf" '>
                <img src="${resmod}/smate-pc/img/fileicon_pdf.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_pdf.png'" onclick="GrpFile.loginFromOutside()">
              </s:elseif>
              <s:else>
                <img
                  src="/upfile${fn:substringBefore(grpFile.filePath, '.')}_c.${fn:substringAfter(grpFile.filePath, '.')}"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'" onclick="GrpFile.loginFromOutside()">
              </s:else>
            </div>
          </div>
          <div class="file-idx__main_box">
            <div class="file-idx__main">
              <div class="file-idx__main_title">
                <a href="javascript:;" onclick="GrpFile.loginFromOutside()"> ${grpFile.fileName } </a>
              </div>
              <div class="kw__box" style="max-width: 400px;">
                <!-- 文件标签 -->
                <c:forEach items="${grpFile.grpFileLabelShowInfoList }" var="label">
                  <div class="kw-chip_container" style="display: flex; cursor: pointer;"
                    onclick="Grp.selectGrpLabelFileList(this);" des3FileLabelId="${label.des3FileLabelId }"
                    des3GrpLabelId="${label.des3GrpLabelId }" des3grpfileid="${label.des3GrpFileId }">
                    <div class="kw-chip_small">${label.labelName }</div>
                  </div>
                </c:forEach>
              </div>
              <div class="file-idx__main_intro multipleline-ellipsis" style="width: 630px; height: 40px;">
                <div style="cursor: default;" class="multipleline-ellipsis__content-box">${grpFile.fileDesc }</div>
              </div>
              <div class="file-idx__main_src"><a href="/psnweb/outside/homepage?des3PsnId=<iris:des3 code='${grpFile.uploadPsnId}'/>" target="_Blank">${psnIdNameMap[grpFile.uploadPsnId] }</a>
                <s:text name='groups.outside.file.uploadAt' />
                <fmt:formatDate value="${  grpFile.uploadDate}" pattern="yyyy-MM-dd" />
              </div>
              <ul class="idx-social__list">
                <li class="idx-social__item" onclick="loginFromOutside('file')"><s:text
                    name='groups.outside.file.share' /></li>
                <li class="idx-social__item" onclick="loginFromOutside('file')"><s:text
                    name='groups.outside.file.save' /></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>
