var agencyThickbox = agencyThickbox ? agencyThickbox : {};
$(document).ready(function() {
  if (typeId != "") {
    $("#type").val(typeId);
    if (typeId == 130) {// 省市
      $("#region_div").css("display", "");
    }
    if (typeId == 140) {// 省市/地市
      $("#region_div").css("display", "");
      $("#city_div").css("display", "");
    }
  }

  if (addrCoun != '') {
    $("#addrCoun").val(addrCoun);
    if ($("#addrPrv option").length > 1) {
      $("#addr_prv_div").css("display", "");
    }
    if (addrPrv != '') {
      $("#addrPrv").val(addrPrv);
      if ($("#addrCity option").length > 1) {
        $("#addr_city_div").css("display", "");
      }
      if (addrCity != '') {
        $("#addrCity").val(addrCity);
      }
    }
  }

  $("#type").change(function() {
    $("#regionId").val("");
    $("#cityId").val("");
    if ($(this).val() == 130) {// 省市/地市
      $("#region_div").css("display", "");
      $("#city_div").css("display", "none");
    } else if ($(this).val() == 140) {
      $("#region_div").css("display", "");
    } else {
      $("#region_div").css("display", "none");
      $("#city_div").css("display", "none");
    }
  });
  $("#regionId").change(function() {// 地区
    if ($(this).val() != "" && $("#type").val() == 140) {
      $.ajax({
        url : ctxpath + "/fund/ajaxGetCity",
        type : "post",
        data : {
          "id" : $(this).val()
        },
        timeout : 10000,
        success : function(data) {
          if (data) {
            if (data.length == 0) {
              $("#cityId").empty();
              $("#city_div").css("display", "none");
              return;
            }
            $("#cityId").empty();
            $("#cityId").append("<option value=\"\"></option>");
            for (var i = 0; i < data.length; i++) {
              $("#cityId").append("<option value=\"" + data[i].id + "\">" + data[i].name + "</option>");
            }
            $("#city_div").css("display", "");
          }
        },
        error : function() {
          $("#city_div").css("display", "none");
        }
      });
    } else {
      $("#city_div").css("display", "none");
    }
  });
  $("#addrPrv").change(function() {// 地址
    $.ajax({
      url : ctxpath + "/fund/ajaxGetCity",
      type : "post",
      data : {
        "id" : $(this).val()
      },
      timeout : 10000,
      success : function(data) {
        if (data) {
          if (data.length == 0) {
            $("#addrCity").empty();
            $("#addr_city_div").css("display", "none");
            return;
          }
          $("#addrCity").empty();
          $("#addrCity").append("<option value=\"\"></option>");
          for (var i = 0; i < data.length; i++) {
            $("#addrCity").append("<option value=\"" + data[i].id + "\">" + data[i].name + "</option>");
          }
          $("#addr_city_div").css("display", "");
        }
      },
      error : function() {
        $("#addr_city_div").css("display", "none");
      }
    });
  });
  $("#addrCoun").change(function() {
    $.ajax({
      url : ctxpath + "/fund/ajaxGetCity",
      type : "post",
      data : {
        "id" : $(this).val()
      },
      timeout : 10000,
      success : function(data) {
        if (data) {
          if (data.length == 0) {
            $("#addrPrv").empty();
            $("#addrCity").empty();
            $("#addr_prv_div").css("display", "none");
            $("#addr_city_div").css("display", "none");
            return;
          }
          $("#addrPrv").empty();
          $("#addrPrv").append("<option value=\"\"></option>");
          for (var i = 0; i < data.length; i++) {
            $("#addrPrv").append("<option value=\"" + data[i].id + "\">" + data[i].name + "</option>");
          }
          $("#addr_prv_div").css("display", "");
          $("#addrCity").empty();
          $("#addr_city_div").css("display", "none");
        }
      },
      error : function() {
        $("#addrPrv").empty();
        $("#addr_prv_div").css("display", "none");
        $("#addrCity").empty();
        $("#addr_city_div").css("display", "none");
      }
    });
  });
  common.setTextareaMaxLength(200);
});

agencyThickbox.saveAgency = function() {
  var agencyId = $("#agencyId").val();
  var parentAgencyId = $("#parentAgencyId").val();
  var auditAgencyId = $("#auditAgencyId").val();
  var nameZh = common.htmlEncode($.trim($("#nameZh").val()));
  var nameEn = common.htmlEncode($.trim($("#nameEn").val()));
  var type = $.trim($("#type").val());
  var regionId = $.trim($("#regionId").val());
  var cityId = $.trim($("#cityId").val());
  var code = $.trim($("#code").val());
  var abbr = $.trim($("#abbr").val());
  var address = $.trim($("#address").val());
  var url = $.trim($("#url").val());
  var auditType = $.trim($("#auditType").val());
  if (url != "") {
    if (url == "http://") {
      url = "";
    }
    if (url != "" && url.indexOf("http://") < 0 && url.indexOf("https://") < 0) {// 网址字段以http://格式保存
      url = "http://" + url;
    }
  }
  var contact = $.trim($("#contact").val());
  regionId = cityId != "" ? cityId : regionId;
  var logoUrl = $("#agency_logo").attr("src");
  var auditType = $.trim($("#auditType").val());
  var parentAgencyId = $.trim($("#parentAgencyId").val());
  if (logoUrl.indexOf("/images_v5/fund_logo/default_logo.jpg") != -1) {
    $.scmtips.show('warn', '资助机构logo不能为空', null, 2000);
    return;
  }
  if (logoUrl.indexOf("http") == -1) {
    var domain = window.location.href;
    domain = domain.substr(0, domain.indexOf(".com/") + 4);
    logoUrl = domain + logoUrl;
  }
  if (nameZh == "") {
    $.scmtips.show('warn', "机构名称不能为空", null, 2000);
    return;
  }
  if (nameEn == "") {
    $.scmtips.show('warn', "机构名称(英文)不能为空", null, 2000);
    return;
  }
  if (type == "") {
    $.scmtips.show('warn', "机构类型不能为空", null, 2000);
    return;
  }
  if (("130" == type || "140" == type) && regionId == "") {
    $.scmtips.show('warn', "请选择省市", null, 2000);
    return;
  }
  var ecitys = "110000,120000,310000,500000";
  if ("140" == type && regionId != "" && ecitys.indexOf(regionId) == -1 && cityId == "") {
    $.scmtips.show('warn', "请选择地区", null, 2000);
    return;
  }

  var addrCoun = $("#addrCoun").val();
  var addrPrv = $("#addrPrv").val();
  var addrCity = $("#addrCity").val();
  if (!$("#addr_prv_div").is(":visible") && addrPrv == null) {
    addrPrv = "";
  }
  if (!$("#addr_city_div").is(':visible') && addrCity == null) {
    addrCity = "";
  }

  $("#btn_submit").disabled();
  var params = {
    "fundAgency.id" : agencyId,
    "fundAgency.nameZh" : nameZh,
    "fundAgency.nameEn" : nameEn,
    "fundAgency.type" : type,
    "fundAgency.regionId" : regionId,
    "fundAgency.code" : code,
    "fundAgency.abbr" : abbr,
    "fundAgency.address" : address,
    "fundAgency.url" : url,
    "fundAgency.contact" : contact,
    "fundAgency.logoUrl" : logoUrl,
    "auditType" : auditType,
    "fundAgency.parentAgencyId" : parentAgencyId,
    "fundAgency.auditAgencyId" : auditAgencyId,
    "fundAgency.addrPrv" : addrPrv,
    "fundAgency.addrCity" : addrCity,
    "fundAgency.addrCoun" : addrCoun,
    "auditType" : auditType
  };

  $.ajax({
    url : ctxpath + "/fund/ajaxCheckAgency",
    type : "post",
    data : params,
    timeout : 10000,
    success : function(result) {
      if (result.ajaxSessionTimeOut == "yes") {
        jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
          if (r) {
            document.location.href = ctxpath + "/fund/agency/maint";
          }
        });
      } else {
        if (result == "false") {
          $.ajax({
            url : ctxpath + "/fund/ajaxAgencySave",
            type : "post",
            data : params,
            timeout : 10000,
            success : function(data) {
              $.scmtips.show('success', "操作成功!", null, 2000);
              setTimeout(function() {
                parent.$.Thickbox.closeWin();
                parent.window.location.reload();
              }, 1000);
            },
            error : function() {
              $("#btn_submit").enabled();
              $.scmtips.show('error', "操作失败", null, 2000);
              setTimeout(function() {
                parent.$.Thickbox.closeWin();
              }, 1000);
            }
          });
        } else {
          $("#btn_submit").enabled();
          $.scmtips.show('warn', "该资助机构已经存在", null, 2000);
        }
      }
    },
    error : function() {
      $("#btn_submit").enabled();
      $.scmtips.show('error', "操作失败", null, 2000);
      setTimeout(function() {
        parent.$.Thickbox.closeWin();
      }, 1000);
    }
  });
}

agencyThickbox.approve = function() {
  var agencyId = $("#auditAgencyId").val();
  var nameZh = common.htmlEncode($.trim($("#nameZh").val()));
  var nameEn = common.htmlEncode($.trim($("#nameEn").val()));
  var type = $.trim($("#type").val());
  var regionId = $.trim($("#regionId").val());
  var cityId = $.trim($("#cityId").val());
  regionId = cityId != "" ? cityId : regionId;
  if (nameZh == "" && nameEn == "") {
    $.scmtips.show('warn', "机构名称不能都为空", null, 2000);
    return;
  }
  if (type == "") {
    $.scmtips.show('warn', "机构类型不能为空", null, 2000);
    return;
  }
  if (("130" == type || "140" == type) && regionId == "") {
    $.scmtips.show('warn', "请选择省市", null, 2000);
    return;
  }
  var ecitys = "110000,120000,310000,500000";
  if ("140" == type && regionId != "" && ecitys.indexOf(regionId) == -1 && cityId == "") {
    $.scmtips.show('warn', "请选择地区", null, 2000);
    return;
  }
  jConfirm("您确定要批准该资助机构吗？", "提示", function(sure) {
    if (sure) {
      $.ajax({
        url : ctxpath + "/fund/ajaxAuditInsFundAgency",
        type : "post",
        data : {
          "id" : agencyId,
          "status" : 1
        },
        timeout : 10000,
        success : function(data) {
          if (data.ajaxSessionTimeOut == "yes") {
            jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
              if (r) {
                document.location.href = ctxpath + "/fund/agency/maint";
              }
            });
          } else {
            agencyThickbox.saveAgency();
          }
        }
      });
    }
  });
}

agencyThickbox.refuse = function() {
  jConfirm("您确定要拒绝该资助机构吗？", "提示", function(sure) {
    if (sure) {
      $.ajax({
        url : ctxpath + "/fund/ajaxAuditInsFundAgency",
        type : "post",
        data : {
          "id" : $("#auditAgencyId").val(),
          "status" : 2
        },
        timeout : 10000,
        success : function(data) {
          if (data.ajaxSessionTimeOut == "yes") {
            jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
              if (r) {
                document.location.href = ctxpath + "/fund/agency/maint";
              }
            });
          } else {
            $.scmtips.show('success', "操作成功!", null, 2000);
            setTimeout(function() {
              parent.$.Thickbox.closeWin();
              parent.window.location.reload();
            }, 1000);
          }
        },
        error : function() {
          $.scmtips.show('error', "操作失败", null, 2000);
          setTimeout(function() {
            parent.$.Thickbox.closeWin();
          }, 1000);
        }
      });
    }
  });
}
//文件改变事件
agencyThickbox.change=function(){
  var file= $("#filedata").val();
  //如果点击了取消按钮 
  if(file==""){
    //将显示框也显示为空
    $(".filedata").val("");
  }
}
agencyThickbox.ajaxUploadFile = function() {
  $("#upload_btn").disabled();
  if ($(".filedata").val() == "") {
    $.scmtips.show("warn", "请选择要上传的图片", null, 2000);
    $("#upload_btn").enabled();
    return;
  }
  jConfirm("在上传文件之前，请确保您上传的文件不违反相关法律法规并且不侵犯版权", "提示", function(sure) {
    if (sure) {
      Loadding_div.show_over("pro_fund_main", "pro_fund_main");
      $.ajaxFileUpload({
        url : ctxpath + "/archiveFiles/ajaxUploadFund",
        secureuri : false,
        // allowType : '*.jpg,*.jpeg,*.gif,*.png,*.bmp',
        fileElementId : 'filedata',
        data : {
          allowType : '*.jpg,*.jpeg,*.gif,*.png,*.bmp',
          'jsessionId' : jsessionId,
        },
        dataType : 'json',
        timeout : 10000,
        success : function(data, status) {
          if (data.ajaxSessionTimeOut == "yes") {
            jConfirm("系统已超时，请重新登录", "提示", function(r) {
              if (r) {
                parent.window.location.reload();
              }
            });
          } else {
            Loadding_div.close("pro_fund_main");
            if (data.result == 'error') {
              $.scmtips.show("warn", data.msg, null, 2000);
            } else {
              $.scmtips.show("success", "上传成功", null, 2000);
              $("#agency_logo").attr("src", "/bpoupfile" + data.path);
              $(".uploadLogo2").css("display", "none");
              $(".uploadLogo1").css("display", "");
            }
            $('.file').attr('value', '');
            $("#filedata,.filedata").change(function() {
              $('.filedata').css("color", "#000");
              $('.filedata').val($(this).val().substring($(this).val().replace(/\\/g, '/').lastIndexOf('/') + 1));
            });
            $("#upload_btn").enabled();
          }
        },
        error : function(data, status, e) {
          Loadding_div.close("pro_fund_main");
          $('.file').attr('value', '');
          $.scmtips.show("error", "上传超时", null, 2000);
          $('.file').attr('value', '');
          $("#filedata,.filedata").change(function() {
            $('.filedata').css("color", "#000");
            $('.filedata').val($(this).val().substring($(this).val().replace(/\\/g, '/').lastIndexOf('/') + 1));
          });
          $("#upload_btn").enabled();
        }
      });
    } else {
      $("#upload_btn").enabled();
    }
  }, 'yesOrNo');
};

agencyThickbox.editLogo = function() {
  if ($(".uploadLogo1").is(":hidden")) {
    $(".uploadLogo2").css("display", "none");
    $(".uploadLogo1").css("display", "");
  } else {
    $("#agency_logo").attr("src", "/resmod/images_v5/fund_logo/default_logo.jpg");
    $(".uploadLogo1").css("display", "none");
    $(".uploadLogo2").css("display", "");
  }
}
