<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    $(document).ready(function() {
        $("#grp_params").attr("smate_des3_grp_id", "${des3GrpId}");
    });
</script>
<div class="js_listinfo" smate_count='${page.totalCount}'></div>
<s:iterator value="GrpFileShowInfos" var="grpFile">
  <div class="main-list__item" drawer-id="<iris:des3 code="${grpFile.grpFileId}"></iris:des3>"
    fileid="<iris:des3 code="${grpFile.grpFileId}"></iris:des3>" grpid="${grpId}"
    archivefileid="${ grpFile.archiveFileId}">
    <div class="main-list__item_checkbox">
      <div class="input-custom-style">
        <input type="checkbox" name=""> <i class="material-icons custom-style"></i>
      </div>
    </div>
    <div class="main-list__item_content">
      <div class="file-idx_medium">
        <div class="file-idx__base-info">
          <div class="file-idx__snap_box">
            <div class="file-idx__snap_img" onclick="window.location.href='${grpFile.downloadUrl }';">
              <s:if test='fileType=="txt"  '>
                <img src="${resmod}/smate-pc/img/fileicon_txt.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_txt.png'">
              </s:if>
              <s:elseif test='fileType=="ppt" || fileType=="pptx" '>
                <img src="${resmod}/smate-pc/img/fileicon_ppt.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_ppt.png'">
              </s:elseif>
              <s:elseif test='fileType=="doc" || fileType=="docx" '>
                <img src="${resmod}/smate-pc/img/fileicon_doc.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_doc.png'">
              </s:elseif>
              <s:elseif test='fileType=="rar" || fileType=="zip" '>
                <img src="${resmod}/smate-pc/img/fileicon_zip.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_zip.png'">
              </s:elseif>
              <s:elseif test='fileType=="xls" || fileType=="xlsx" '>
                <img src="${resmod}/smate-pc/img/fileicon_xls.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_xls.png'">
              </s:elseif>
              <s:elseif test='fileType=="pdf" '>
                <img src="${resmod}/smate-pc/img/fileicon_pdf.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_pdf.png'">
              </s:elseif>
              <s:else>
                <img src="${grpFile.imgThumbUrl}" onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
              </s:else>
            </div>
          </div>
          <!--  <div class="file-idx__main_box" style="position: relative;"> -->
          <div class="file-idx__main_box">
            <div class="file-idx__main">
              <div class="file-idx__main_title pub-idx__main_title-multipleline" >
                <a  href="${grpFile.downloadUrl }"> ${grpFile.fileName } </a>
              </div>
              <div class="kw__box" style="max-width: 400px;">
                <!-- 文件标签 -->
                <c:forEach items="${grpFile.grpFileLabelShowInfoList }" var="label">
                  <div class="kw-chip_container" style="display: flex; cursor: pointer;"
                    onclick="Grp.selectGrpLabelFileList(this);" des3FileLabelId="${label.des3FileLabelId }"
                    des3GrpLabelId="${label.des3GrpLabelId }" des3grpfileid="${label.des3GrpFileId }">
                    <div class="kw-chip_small">${label.labelName }</div>
                    <c:if test="${label.showDel == true }">
                      <div class="kw-chip_container-func">
                        <i class="normal-global_icon normal-global_del-icon"
                          title="<s:text name="groups.file.del.file.label"/>"
                          onclick="Grp.delGrpFileLabelConfirm(event ,this);"></i>
                      </div>
                    </c:if>
                  </div>
                </c:forEach>
              </div>
              <div style="height: auto; overflow: hidden; width: 420px;">
                <div class="file-idx__main_intro multipleline-ellipsis" style="margin-left: 2px !important">
                  <div style="cursor: default;" class="multipleline-ellipsis__content-box">${grpFile.fileDesc }</div>
                </div>
              </div>
              <ul class="idx-social__list" style="width:120%;">
                <li class="idx-social__item" onclick="SmateShare.getFileListSareParam(this); fileInitSharePlugin(this);"
                  style="display: flex; align-items: center;"><i
                  class="normal-global_icon normal-global_share-icon"></i> <span><s:text
                      name='groups.file.dynamic.share' /></span></li>
                <li class="idx-social__item"
                  onclick="Grp.collectionGrpFile('<iris:des3 code="${grpFile.grpFileId}"></iris:des3>',this)"
                  style="display: flex; align-items: center;"><i class="normal-global_icon normal-global_save-icon"></i>
                  <span><s:text name='groups.file.dynamic.save' /></span></li>
                <s:if test=" grpRole==1 || grpRole==2 || grpRole ==3">
                  <s:if test="showEdit == true">
                    <li class="idx-social__item"
                      onclick="Grp.editGrpFile('${des3GrpId}'  ,'${ grpFile.des3GrpFileId}' ,this)"
                      style="display: flex; align-items: center;"><i
                      class="normal-global_icon normal-global_edit-icon"></i> <span><s:text
                          name='groups.file.dynamic.edit' /></span></li>
                  </s:if>
                  <s:if test=" showDel == true ">
                    <li class="idx-social__item"
                      onclick="Grp.deleteGrpFileComfirm('${des3GrpId}'  ,'${ grpFile.des3GrpFileId}'  ,this)"
                      style="display: flex; align-items: center;"><i
                      class="normal-global_icon normal-global_del-icon"></i> <span><s:text
                          name='groups.file.dynamic.del' /></span></li>
                  </s:if>
                </s:if>
                <s:else>
                  <s:if test=" showDel == true ">
                      <li class="idx-social__item"
                        onclick="Grp.deleteGrpFileComfirm('${des3GrpId}'  ,'${ grpFile.des3GrpFileId}'  ,this)"
                        style="display: flex; align-items: center;"><i
                        class="normal-global_icon normal-global_del-icon"></i> <span><s:text
                            name='groups.file.dynamic.del' /></span></li>
                    </s:if>
                </s:else>
                <li class="idx-social__item" style="display: flex; align-items: center;"><i
                  class="normal-global_icon normal-global_Setup-icon" onclick="Grp.showFileLabelListExcludeOwner(this);"></i>
                  <span onclick="Grp.showFileLabelListExcludeOwner(this);"><s:text name="groups.file.label.set" /></span>
                  <div class="idx-social__item-show">
                    <span class="idx-social__item-show_item"></span>
                  </div></li>
              </ul>
            </div>
            <div class="file-idx__main_src">
              <div class="file-idx__src_time">
                <fmt:formatDate value="${  grpFile.uploadDate}" pattern="yyyy-MM-dd" />
              </div>
              <div class="file-idx__src_uploader"></div>
            </div>
            <!-- <div class="idx-social__item-show">
                              <span class="idx-social__item-show_item"></span>
                        </div> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</s:iterator>