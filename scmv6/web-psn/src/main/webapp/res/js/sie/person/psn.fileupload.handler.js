var fileUploadHandler = fileUploadHandler ? fileUploadHandler : {};
fileUploadHandler.ajaxFileUpload = function(url) {
  url = "/psnweb/person/ajaximportpsn";
  /* if ($("#rightCKB").is(":checked")) { */
  var outter = this;
  $("#loading").ajaxStart(function() {
    $(this).show();
  }).ajaxComplete(function() {
    $(this).hide();
  });

  var filedata = $(".filedata").val();
  // 有问题
  if (filedata == '' || uploadMaxSize == filedata) {
    $.scmtips.show("warn", choosefile);
    return;
  }
  var needMail = "";
  if (url != null) {
    var newurl = url;
    // 供人员导入用
    needMail = parent.$("#needMail").val();
  } else {
    var newurl = "/archiveFiles/ajaxSimpleUpload";
  }

  $.ajaxFileUpload({
    url : newurl,
    secureuri : false,
    fileElementId : 'filedata',
    data : {// 你需要的参数
      "needMail" : needMail
    },
    dataType : 'json',
    beforeSend : function() {
      $("#loading").show();
    },
    complete : function() {
      $("#loading").hide();
    },
    success : function(data, status) {
      if (data.result == "error") {
        $(".filedata").attr("value", "");
        $("#filedata,.filedata").change(function() {
          $('.filedata').css("color", "#000");
          $('.filedata').val($(this).val().substring($(this).val().replace(/\\/g, '/').lastIndexOf('/') + 1));
        });
        if (data.msg == "no_psn_imported") {
          location.href = "/psnweb/person/importpage?message=no_psn";
        } else if (data.msg == "failed") {
          location.href = "/psnweb/person/importpage?message=import_error";
        } else {
          $.scmtips.show("warn", data.msg);
        }
      } else {
        parent.fileAddHandler(data);
      }
    },
    error : function(data, status, e) {
      $(".filedata").attr("value", "");
      $.scmtips.show("error", "上传失败");

    }
  });
  /*
   * } else { $.scmtips.show("warn", verifyCopyright); }
   */
};
fileUploadHandler.rightClick = function(obj) {
  if ($(obj).attr("checked")) {
    $("#uploadSubmit").enabled();
  } else {
    $("#uploadSubmit").disabled();
  }
};