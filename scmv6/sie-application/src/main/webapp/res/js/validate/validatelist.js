function query() {
  var pageNo = $(":input[name='page.pageNo']").val();
  pageNo = typeof pageNo == 'undefined' ? '' : pageNo;
  var pageSize = $(":input[name='page.pageSize']").val();
  pageSize = typeof pageSize == 'undefined' ? '' : pageSize;
  var sendfile = {
    "page.pageNo" : pageNo,
    "page.pageSize" : pageSize
  };
  var sendata = getoptions();
  var sendtotal = Object.assign(sendata, sendfile);
  $.ajax({
    url : "/application/validate/ajaxvalidatelist",
    type : 'post',
    data : sendtotal,
    dataType : 'html',
    success : function(data) {
      if (data.ajaxSessionTimeOut == "yes") {
        ScmLoginBox.showLoginToast();
      } else {
        $("#validatelist").html(data);
        $("#top_totalcount").text($("#totalCount").val());
      }
    },
    error : function() {
      scmpublictoast('系统出现异常，请稍后再试！', 1000);
    }
  });
}

var ValidateList = ValidateList ? ValidateList : {};
// 查看
ValidateList.viewDetail = function(status, des3UuId, obj, flag) {
  if (status == "0") {
    if (flag == 'true') {
      scmpublictoast('该文档后台验证中，请稍后查看', 2000);
      return;
    }
    $
        .ajax({
          url : "/application/validate/ajaxrefreshstatus",
          type : "post",
          dataType : "json",
          data : {
            "des3Uuid" : des3UuId
          },
          success : function(data) {
            if (data.ajaxSessionTimeOut == "yes") {
              ScmLoginBox.showLoginToast();
            } else if (data.result == 'success') {
              if (data.status == '1') {
                var htmlContent = "<a href=\"###\" onclick=\"ValidateList.viewDetail('1','"
                    + des3UuId
                    + "',this);\" class=\"icon_examine1\" title=\"文档已验证完成\"><i style=\"display: inline-block; width: 18px; height: 18px; margin-right: 4px; background: url(/ressie/images/icon_via.png) no-repeat;\"></i></a>";
                $(obj).parent("p").html(htmlContent);

              } else if (data.status == '0') {
                scmpublictoast('该文档后台验证中，请稍后查看', 2000);
              }
            }
          },
          error : function(data) {
            scmpublictoast('系统出现异常，请稍后再试！', 1000);
          }
        });
  } else {
    $.ajax({
      url : "/application/validate/ajaxcheckupload",
      type : "post",
      dataType : "json",
      data : "",
      success : function(data) {
        if (data.ajaxSessionTimeOut == "yes") {
          ScmLoginBox.showLoginToast();
        } else {
          window.location.href = "/application/validate/viewdetail?des3UuId=" + des3UuId;
        }
      },
      error : function(data) {
        scmpublictoast('系统出现异常，请稍后再试！', 1000);
      }
    });
  }
}

ValidateList.toAdd = function(isPay) {
  $.ajax({
    url : "/application/validate/ajaxcheckupload",
    type : "post",
    dataType : "json",
    // async : false,
    data : "",
    success : function(data) {
      if (data.ajaxSessionTimeOut == "yes") {
        ScmLoginBox.showLoginToast();
      } else {
        var url = "/application/validate/toadd";
        // if (isPay == '0') {
        // ValidateList.openMyWin(url);
        // } else {
        window.location.href = url;
        // }
      }
    },
    error : function(data) {
      scmpublictoast('系统出现异常，请稍后再试！', 1000);
    }
  });
}

ValidateList.downFile = function(fdesId) {
  $.ajax({
    url : "/application/validate/ajaxcheckupload",
    type : "post",
    dataType : "json",
    data : "",
    success : function(data) {
      if (data.ajaxSessionTimeOut == "yes") {
        ScmLoginBox.showLoginToast();
      } else {
        window.location.href = "/application/validate/fileDownload?fdesId=" + fdesId;
      }
    },
    error : function(data) {
      scmpublictoast('系统出现异常，请稍后再试！', 1000);
    }
  });
}

ValidateList.refreshStatus = function(status, des3UuId, obj) {
  $
      .ajax({
        url : "/application/validate/ajaxrefreshstatus",
        type : "post",
        dataType : "json",
        data : {
          "des3Uuid" : des3UuId
        },
        success : function(data) {
          if (data.ajaxSessionTimeOut == "yes") {
            ScmLoginBox.showLoginToast();
          } else if (data.result == 'success') {
            if (data.status == '1') {
              var htmlContent = "<a href=\"###\" onclick=\"ValidateList.viewDetail('1','des3UuId',this);\" class=\"icon_examine1\" title=\"文档验证通过\"><i style=\"display: inline-block; width: 18px; height: 18px; margin-right: 4px; background: url(/ressie/images/icon_via.png) no-repeat;\"></i></a>";
              $(obj).parent("p").html(htmlContent);
            } else if (data.status == '2') {
              var htmlContent = "<a href=\"###\" onclick=\"ValidateList.viewDetail('2','des3UuId',this);\" class=\"icon_examine1\" title=\"文档验证存疑\"><i style=\"display: inline-block; width: 18px; height: 18px; margin-right: 4px; background: url(/ressie/images/icon_mistake.png) no-repeat;\"></i></a>";
              $(obj).parent("p").html(htmlContent);
            }
          }
        },
        error : function(data) {
          scmpublictoast('系统出现异常，请稍后再试！', 1000);
        }
      });
}

ValidateList.confirmDelMain = function(des3Id) {
  if (des3Id != '') {
    var tip = "您确定删除选定的验证记录吗？"
    var option = {
      'screentxt' : tip,
      'screencallback' : ValidateList.doDelMain,
      'screencallbackData' : des3Id,
      'screencancelcallback' : ValidateList.goBackCallbackNo
    };
    popconfirmbox(option);
  } else {
    scmpublictoast("请先选择验证记录", 1500);
  }
}

ValidateList.doDelMain = function(des3Id) {
  $.ajax({
    url : '/application/validate/ajaxdelete',
    type : 'POST',
    data : {
      "des3Id" : des3Id
    },
    dataType : 'json',
    success : function(data) {
      if (data.result == 'success') {
        scmpublictoast(data.msg, 1500);
        setTimeout(function() {
          window.location.reload();
        }, 1000)
      } else if (data.ajaxSessionTimeOut == "yes") {
        ScmLoginBox.showLoginToast();
      } else {
        scmpublictoast("删除失败", 1500);
      }
    },
    error : function() {
      scmpublictoast('系统出现异常，请稍后再试！', 1000);
    }
  });
}
ValidateList.goBackCallbackNo = function() {
  return;
}
ValidateList.openMyWin = function(url) {
  $('body').append($('<a href="' + url + '" target="_blank" id="openWin"></a>'))
  document.getElementById("openWin").click();// 点击事件
  $('#openWin').remove();
}
