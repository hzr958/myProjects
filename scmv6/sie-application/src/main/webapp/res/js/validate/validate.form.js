var Validate = {};

// 提交
Validate.doSubmit = function() {
  if (!$(".fileupload__box").find(" .fileupload__hint-text.file_selected")[0]) {
    $("#validateTip").find("label").html("待验证文档不能为空");
    $("#validateTip").css("display", "flex");
    return;
  }
  $("#validateTip").css("display", "none");
  $.ajax({
    url : "/application/validate/ajaxcheckupload",
    type : "post",
    dataType : "json",
    data : "",
    success : function(data) {
      if (data.ajaxSessionTimeOut == "yes") {
        ScmLoginBox.showLoginToast();
      } else {
        $.ajax({
          url : "/application/validate/ajaxsubmitbefore",
          type : "post",
          dataType : "json",
          data : "",
          success : function(data) {
            if (data.msg == "toPay") {
              var url = "/application/validate/toadd";
              window.location.href = url;
            }
            // 检查是否超过最大限制
            else if (data.flag == "false") {
              // scmpublictoast('您提交的验证文档数已超过管理员设置的最大值，不可提交，请联系单位管理员！',
              // 2000);
              $("#validateTip").find("label").html("您提交的验证文档数已超过管理员设置的最大值，不可提交，请联系单位管理员");
              $("#validateTip").css("display", "flex");
            } else {
              var data = {
                "fileurl" : "/application/validate/ajaxsubmitfile",
                "filedata" : {
                  fileDealType : "generalfile"
                },
                "method" : "click",
                "type" : "NotList",
                "filecallback" : Validate.reloadFulltext,
                "filecallbackparam" : "",
                "checktimeouturl" : '/application/validate/ajaxcheckupload'
              };
              sieFileUploadModule.uploadFile($(".demo_file")[0], data);
            }
          },
          error : function(data) {
            scmpublictoast('系统出现异常，请稍后再试！', 1000);
          }
        });
      }
    },
    error : function(data) {
      scmpublictoast('系统出现异常，请稍后再试！', 1000);
    }
  });
}

Validate.reloadFulltext = function(xhr, params) {
  const data = eval('(' + xhr.responseText + ')');
  // 检查是否购买
  if (data.msg == "toPay") {
    var url = "/application/validate/toadd";
    window.location.href = url;
  }
  // 检查是否超过最大上传次数
  else if (data.msg == "false" || data.flag == "false") {
    $("#validateTip").find("label").html("您提交的验证文档数已超过管理员设置的最大值，不可提交，请联系单位管理员");
    $("#validateTip").css("display", "flex");
  }
  // 是否上传成功
  else if (data.msg == 'success') {
    scmpublictoast("已经提交后台检测!", 1000);
    setTimeout(function() {
      window.location.href = "/application/validate/maint"
    }, 1000);
  } else {
    $(".fileupload__core").remove();
    initFileUpload();
    $(".fileupload__box").removeClass("upload_ready");
    $(".demo_file").removeClass("upload_finished");
    $("#validateTip").find("label").html("该申请书不支持，请联系在线客服");
    $("#validateTip").css("display", "flex");
  }
};

Validate.backList = function() {
  $.ajax({
    url : "/application/validate/ajaxcheckupload",
    type : "post",
    dataType : "json",
    data : "",
    success : function(data) {
      if (data.ajaxSessionTimeOut == "yes") {
        ScmLoginBox.showLoginToast();
      } else {
        window.location.href = "/application/validate/maint";
      }
    },
    error : function(data) {
      scmpublictoast('系统出现异常，请稍后再试！', 1000);
    }
  });
}

Validate.isPay = function(status) {
  if (status == '0' || status == '') {
    var a = document.getElementById('popup').innerHTML
    scmpcnewtip({
      targettitle : '选择新增方式',
      targetcllback : '',
      targettxt : a,
      targetfooter : 0
    });
  }
}
