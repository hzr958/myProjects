function fileUploadBindChangeEvent() {
  $("input[type='file']").val("");
  $('.file').attr('value', '');
  /* $("#export_link").disabled(); */
  $("input[type='file']").change(function() {
    if ($(this).val().length > 0) {
      $(".filedata").val($(this).val().substring($(this).val().replace(/\\/g, '/').lastIndexOf('/') + 1));
      /* $("#export_link").enabled(); */
    }
  });
}

// 文件上传后回调函数。
function fileAddHandler(data) {
  // 如果错误 提示 并提示修改
  if (data.result == 'error') {
    $.scmtips.show("error", "文件上传失败");
    fileUploadBindChangeEvent();
  } else if (data.result == 'fileTypeNotMatch') {
    $.scmtips.show("error", "<s:text name='referencelist.fileFormateError'/>");
    fileUploadBindChangeEvent();
  } else if (data.result == "tooLongPosition") {
    // 导入人员的文件中职位信息过长 ROL-2464 WSN
    if (data.row != null && data.row != "") {
      if (locale != null && typeof locale != "undefined" && locale == 'zh_CN') {
        $.scmtips.show("error", "第" + data.row + "行记录的职位信息过长");
      } else {
        $.scmtips.show("error", "Position information is too long on Row " + data.row);
      }

    }
    fileUploadBindChangeEvent();
  } else {
    setTimeout(function() {
      $("#versionId").val(data.versionId);
      $("#progress_bar").click();
    }, 1000);
  }

}
