<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${resscmsns}/css_v5/pop.css" />
<link rel="stylesheet" type="text/css" href="${resscmsns}/css_v5/public.css" />
<script type="text/javascript" src="/resmod/js/common.ui.js"></script>
<title>文件编辑</title>
<script type="text/javascript">

</script>
</head>
<body>
  <%-- 修改文件描述信息 --%>
  <div id="file_edit_div">
    <input id="showFileEdit" class="thickbox" type="hidden" title="<s:text name="group.res.pubs.group.modify.title" />">
    <div id="edit_file_floatDiv" style="display: none;">
      <div class="dialog_content" style="overflow-x: hidden; overflow-y: auto; padding-bottom: 20px; height: 150px;">
        <span class="cu12 discuss_tit Fleft"><s:text name="group.res.file.description" /></span>
        <div class="filedesc_input inp_bg1 f999 Fleft" style="width: 410px;">
          <div class="input-line3"
            style="width: 405px; height: auto !important; height: 105px; min-height: 105px; outline: none; line-height: 17px;"
            id="file_description" contentEditable="true" onKeyDown="common.divWordCount('file_description',200)"
            onKeyUp="common.divWordCount('file_description',200)" onPaste="common.divWordCount('file_description',200)"
            onFocus="common.divWordCount('file_description',200)"></div>
          <p style="color: #999; text-align: right; padding-right: 5px; margin-top: 0px; line-height: 17px;">
            <label id="file_description_countLabel" class="count_label">0</label>/200
          </p>
        </div>
        <input type="hidden" value="" id="_groupFileId" /> <input type="hidden" value="" id="ownerId" />
      </div>
      <div class="pop_buttom">
        <a class="uiButton uiButtonConfirm text14" href="javascript:;" onclick="Group.editSaveGroupFile();"> <s:text
            name="group.res.pubs.save" />
        </a> <a class="uiButton text14 mright10" href="javascript:;" onclick="parent.$.Thickbox.closeWin();"><s:text
            name="group.res.pubs.cancel" /> </a>
      </div>
    </div>
  </div>
</body>
</html>