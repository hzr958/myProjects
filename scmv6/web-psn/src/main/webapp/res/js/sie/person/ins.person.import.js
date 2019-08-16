var manage = manage ? manage : {};
manage.profileImport = manage.profileImport ? manage.profileImport : {};

manage.profileImport.showMsg = function() {
  if (msg == "") {
    return;
  }
  if (msg.indexOf("no_psn") != -1) {
    $.scmtips.show('success', tip7);
  } else if (msg.indexOf("import_error") != -1) {
    $.scmtips.show('error', tip4);
  } else if (msg.indexOf("importSuccess") != -1) {
    $.scmtips.show('success', tip7);
  } else if (msg.indexOf("importError") != -1) {
    $.scmtips.show('error', tip6);
  }
};

manage.profileImport.stepTo = function(step) {
  if (step != $("#step_flag").val()) {
    $(".rol_upload").each(function() {
      $(this).hide();
    });

    $("#step_" + step).show();
    $("#step_flag").val(step);
  }
};

manage.profileImport.importFile = function() {
  // 部门为空
  if (insUnitCount <= 0) {
    manage.profileImport.showAddUnit();
    return;
  }
  var file = $('#filedata').val();
  if (file != '' && /^.*\.xls$/ig.test(file)) {
    // parent.$.Thickbox.closeWin();
    // prcWin.openWin();
    // 为了方便进度条的显示 改称ajax
    var filedata = $("#filedata").val();
    // $("#mainForm").submit();
    // $("#uploadprogressbar").progressBar();
  } else {
    $.scmtips.show('warn', tip1);
  }
};
// 响应 导入人员页面-下载模版按钮 的触发请求.
manage.profileImport.downFile = function() {

  /* 需要判断系统是否超时 避免出现超时后，点击下载模版，没反应.然后再打开网页登录,就直接在新的网页下载模版了 tsz_2015-3-3_ROL-1582 */
  $.ajax({
    url : '/psnweb/person/ajaxtimeout',
    type : 'post',
    dataType : 'json',
    data : {},
    success : function(data) {
      if (data.result != "success" && data.ajaxSessionTimeOut == "yes") {
        /* 超时 点击下载模板 刷新页面 */
        window.location.reload();
      } else {
        $("#down_file_frame").attr("src", "/psnweb/person/downpsntmp?xxtemp=" + (new Date().getTime().toString(36)));
      }
    }
  });

};

manage.profileImport.uploadFile = function() {
  var file = $(".filedata").val();
  $.ajax({
    url : '/psnweb/person/ajaxtimeout',
    type : 'post',
    dataType : 'json',
    data : {},
    success : function(data) {
      if (data.result != "success" && data.ajaxSessionTimeOut == "yes") {
        /* 超时 点击导入模板 刷新页面 */
        window.location.reload();
      } else {
        if (file == '' || !/^.*\.xls$/ig.test(file)) {
          $.scmtips.show("error", fileTypeNotMatch);
          fileUploadBindChangeEvent();
          return;
        } else {
          var con = document.getElementById('importConfirmBox').innerHTML
          scmpcnewtip({
            targettitle : '导入人员',
            targetcllback : '',
            targettxt : con,
            targetcllback : fileUploadHandler.ajaxFileUpload
          });
        }
      }
    }
  });

}

manage.profileImport.isUnitEmpty = function() {
  if (insUnitJson.length == 0) {
    return true;
  }

  if (insUnitJson[0].data.attributes.id > 0) {
    return false;
  }
  if (insUnitJson[0].data.attributes.id <= 0 && insUnitJson[0].children && insUnitJson[0].children.length > 0) {
    return false;
  }
  return true;
};
