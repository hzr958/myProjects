var Institution = Institution || {};

/**
 * 打开机构性质
 */
Institution.openInsProperty = function() {
  var $this = $(".handin_import-content_container-right_select");
  var property = $this.find(".dev_ins_property_tip").html();
  if (property == "arrow_drop_down") {
    $this.find(".dev_ins_property_tip").html("arrow_drop_up");
    $this.find(".dev_select-detail").css("display", "block");
  } else {
    $this.find(".dev_ins_property_tip").html("arrow_drop_down");
    $this.find(".dev_select-detail").css("display", "none");
  }
}

Institution.bindInsPropertyEvent = function() {
  $(".dev_ins_property").bind("click", function() {
    var content = $(this).find("span").html();
    var value = $(this).find("span").attr("value");
    $("#showPropertyName").val(content);
    $("#showPropertyName").attr("item", value);
    $("#property_error").removeClass("dev_error_tip");
  });
  $("#insName").bind("keyup input",function() {
        Institution.checkInsName();
    });
  $("#insDomain").bind("keyup input",function() {
    Institution.checkInsDomain();
  });


  $(".create-Homepage_Option-item").bind("click", function() {
    $("#nature").val($(this).attr("item"));
    if ($(this).attr("item") == "0") {
      $("#other_nature").css("display", "flex");
    } else {
      $("#other_nature").css("display", "none");
    }
  });

}

// 添加经济行业
Institution.showEconomicBox = function(areaSelect) {
  var areaIds = "";
  areaIds = $("#eco_code_id1").val() + "," + $("#eco_code_id2").val();
  $.ajax({
    url : "/prjweb/ins/create/ajaxaddeconomic",
    type : "post",
    dataType : "html",
    data : {
      "economicIds" : areaIds
    },
    success : function(data) {
      $("#login_box_refresh_currentPage").val("false");
      // 登录不跳转
      BaseUtils.ajaxTimeOut(data, function() {
        $("#economicBox").html(data);
        showDialog("economicBox");
        addFormElementsEvents(document.getElementById("research-area-list"));
        $("#areaSelect").val(areaSelect);
      });
    },
    error : function() {
    }
  });
};
Institution.addEconomic = function(id) {
  var key = $("#" + id + "_category_title").html();
  var sum = $("#choosed_area_list").find(".main-list__item").length;
  if (sum < 2) {
    var html = '<div class="main-list__item" style="padding: 0px 16px!important;" areaid = "' + id + '" >'
        + '<div class="main-list__item_content">' + key + '</div>'
        + '<div class="main-list__item_actions"  onclick="javascript:Institution.delEconomic(\'' + id
        + '\');"><i class="material-icons">close</i></div></div>';
    $("#choosed_area_list").append(html);
    $("#unchecked_area_" + id).attr("onclick", "");
    $("#unchecked_area_" + id).attr("id", "checked_area_" + id);
    $("#" + id + "_status").html("check");
    $("#" + id + "_status").css("color", "forestgreen");
  } else {
    // 出提示语
    scmpublictoast("最多选择两个行业", 1500);
  }
};
Institution.delEconomic = function(id) {
  $("#choosed_area_list").find(".main-list__item[areaid='" + id + "']").remove();
  $("#checked_area_" + id).attr("onclick", "Institution.addEconomic('" + id + "')");
  $("#checked_area_" + id).attr("id", "unchecked_area_" + id);
  $("#" + id + "_status").html("add");
  $("#" + id + "_status").css("color", "");
};

Institution.saveEconomic = function() {

  setTimeout(function() {
    $("#eco_code_id1").val("");
    $("#eco_code_id2").val("");
    var keyName = "";
    $("#choosed_area_list").find(".main-list__item").each(function(index) {
      var id = $(this).attr("areaid");
      keyName += $("#" + id + "_category_title").html() + ", ";
      if (index == 0) {
        $("#eco_code_id1").val(id);
      } else {
        $("#eco_code_id2").val(id);
      }
    });
    $("#eco_code_id").val(keyName.replace(/,\s$/, ""));
    if ($.trim($("#eco_code_id").val()) != "") {
      $("#eco_code_error").removeClass("dev_error_tip");
    }
  }, 100);
  hideDialog("economicBox");
};
// 隐藏
Institution.hideEconomicBox = function(obj) {
  $("#economicBox").html("");
  hideDialog("economicBox");
}

// //////////////////--------------------------------------------////////////
// 添加新兴产业代码
Institution.showCseiBox = function(areaSelect) {
  var areaIds = "";
  areaIds = $("#csei_code_id1").val() + "," + $("#csei_code_id2").val();
  $.ajax({
    url : "/prjweb/ins/create/ajaxaddcsei",
    type : "post",
    dataType : "html",
    data : {
      "economicIds" : areaIds
    },
    success : function(data) {
      $("#login_box_refresh_currentPage").val("false");
      // 登录不跳转
      BaseUtils.ajaxTimeOut(data, function() {
        $("#economicBox").html(data);
        showDialog("economicBox");
        addFormElementsEvents(document.getElementById("research-area-list"));
        $("#areaSelect").val(areaSelect);
      });
    },
    error : function() {
    }
  });
};
Institution.addCsei = function(id) {
  var key = $("#" + id + "_category_title").html();
  var sum = $("#choosed_area_list").find(".main-list__item").length;
  if (sum < 2) {
    var html = '<div class="main-list__item" style="padding: 0px 16px!important;" areaid = "' + id + '" >'
        + '<div class="main-list__item_content">' + key + '</div>'
        + '<div class="main-list__item_actions"  onclick="javascript:Institution.delCsei(\'' + id
        + '\');"><i class="material-icons">close</i></div></div>';
    $("#choosed_area_list").append(html);
    $("#unchecked_area_" + id).attr("onclick", "");
    $("#unchecked_area_" + id).attr("id", "checked_area_" + id);
    $("#" + id + "_status").html("check");
    $("#" + id + "_status").css("color", "forestgreen");
  } else {
    // 出提示语
    scmpublictoast("最多选择两个产业", 1500);
  }
};
Institution.delCsei = function(id) {
  $("#choosed_area_list").find(".main-list__item[areaid='" + id + "']").remove();
  $("#checked_area_" + id).attr("onclick", "Institution.addCsei('" + id + "')");
  $("#checked_area_" + id).attr("id", "unchecked_area_" + id);
  $("#" + id + "_status").html("add");
  $("#" + id + "_status").css("color", "");
};

Institution.saveCsei = function() {

  setTimeout(function() {
    $("#csei_code_id1").val("");
    $("#csei_code_id2").val("");
    var keyName = "";
    $("#choosed_area_list").find(".main-list__item").each(function(index) {
      var id = $(this).attr("areaid");
      keyName += $("#" + id + "_category_title").html() + ", ";
      if (index == 0) {
        $("#csei_code_id1").val(id);
      } else {
        $("#csei_code_id2").val(id);
      }
    });
    $("#csei_code_id").val(keyName.replace(/,\s$/, ""));
  }, 100);
  hideDialog("economicBox");
};
// 隐藏
Institution.hideCseiBox = function(obj) {
  $("#economicBox").html("");
  hideDialog("economicBox");
};

/**
 * 检查单位
 */
Institution.checkInsName = function() {
  var insName = $("#insName").val();
  if ($.trim(insName) == "") {
    $("#insNameError").css("display", "none");
    return;
  } else {
    $("#insName").closest("div").removeClass("dev_error_tip");
  }
  $.ajax({
    url : "/prjweb/ins/checkins",
    type : "post",
    dataType : "json",
    data : {
      "insName" : insName,
      "checkInsNameType" : 1
    },
    success : function(data) {
      if (data.result == "success") {
        $("#insNameError").css("display", "none");
        $("#insName").closest("div").removeClass("dev_error_tip");
      } else {
        $("#insNameError").css("display", "block");
        $("#insName").closest("div").addClass("dev_error_tip");
        $("#guide_complain").attr("href", data.domain + "/insweb/index?guide_complain=1");
      }
    },
    error : function() {
    }
  });
};

/**
 * 检查域名
 */
Institution.checkInsDomain = function() {
  var insDomain = $.trim($("#insDomain").val());
  if (insDomain == "") {
    $("#insDomainError").css("display", "none");
      $("#insDomainRule").css("display", "none");
    return;
  } else {
    $("#insDomain").closest("div").removeClass("dev_error_tip");
  }
  if (!BaseUtils.checkInsDomain(insDomain)) {
    $("#insDomain").closest("div").addClass("dev_error_tip");
    $("#insDomainError").css("display", "none");
    $("#insDomainRule").css("display", "block");
    return;
  }else{
      $("#insDomainRule").css("display", "none");
  }
  $.ajax({
    url : "/prjweb/ins/checkins",
    type : "post",
    dataType : "json",
    data : {
      "insDomain" : insDomain,
      "checkInsNameType" : 2
    },
    success : function(data) {
      if (data.result == "success") {
        $("#insDomainError").css("display", "none");
        $("#insDomain").closest("div").removeClass("dev_error_tip");
      } else {
        $("#insDomainError").css("display", "block");
        $("#insDomain").closest("div").addClass("dev_error_tip");
      }
    },
    error : function() {
    }
  });
};

Institution.showScmDomainTip = function() {
  $("#insDomainTip").css("display", "block");
};
Institution.hideScmDomainTip = function() {
  $("#insDomainTip").css("display", "none");
};

/**
 * 保存机构主页
 */
Institution.saveInsPage = function(obj) {
  if (Institution.checkData()) {
    Institution.save(obj)
  } else {
    console.log("save error");
  }
};

/**
 * 保存数据
 */
Institution.save = function(obj) {
  var ecoCode = "";
  if ($("#eco_code_id2").val() != "" && $("#eco_code_id1").val() != "") {
    ecoCode = $("#eco_code_id1").val() + "," + $("#eco_code_id2").val()
  } else {
    ecoCode = $("#eco_code_id1").val() + $("#eco_code_id2").val()
  }
  var cseiCode = "";
  if ($("#eco_code_id2").val() != "" && $("#eco_code_id1").val() != "") {
    cseiCode = $("#csei_code_id1").val() + "," + $("#csei_code_id2").val()
  } else {
    cseiCode = $("#csei_code_id1").val() + $("#csei_code_id2").val()
  }
  var countryId = $("div[selector-id='1st_area']").attr("sel-value");
  var prvId = $("div[selector-id='2nd_area']").attr("sel-value");
  var cityId = $("div[selector-id='3nd_area']").attr("sel-value");
  var click = BaseUtils.unDisable(obj);
  $.ajax({
    url : "/prjweb/ins/ajaxsaveins",
    type : "post",
    dataType : "json",
    data : {
      "nature" : $("#nature").val(),
      "insName" : $("#insName").val(),
      "insDomain" : $("#insDomain").val(),
      "url" : $("#insUrl").val(),
      "ecoCode" : ecoCode,
      "cseiCode" : cseiCode,
      "description" : $("#description").val(),
      "countryId" : countryId,
      "prvId" : prvId,
      "cyId" : cityId,
    },
    success : function(data) {
      if (data.status == "success") {
        scmpublictoast("创建成功", 1500);
        setTimeout(function() {
            if(window.opener){
                try {
                    window.opener.location.reload(); // 刷新父页面
                }catch (err){
                    console.log("不能刷新父页面");
                }

            }
          location.href = data.forwardUrl;
        }, 1500);
      } else {
        BaseUtils.disable(obj, click);
        scmpublictoast("创建失败", 1500);
      }
    },
    error : function() {
    }
  });
};

Institution.checkData = function() {
  /*
   * $(".dev_error_tip").each(function() { $(this).removeClass("dev_error_tip"); })
   */
  // 检查机构名称
  var insName = $("#insName").val();
  if ($.trim(insName) == "") {
    $("#insName").closest("div").addClass("dev_error_tip");
  }
  // 检查 domain
  var insDomain = $("#insDomain").val();
  if (!BaseUtils.checkInsDomain(insDomain)) {
    $("#insDomain").closest("div").addClass("dev_error_tip");
  }
  // 检查 所属行业
  var eco_code_id = $("#eco_code_id").val();
  if ($.trim(eco_code_id) == "") {
    $("#eco_code_error").addClass("dev_error_tip");
  }
  // 性质
  var nature = $("#nature").val();
  var showPropertyName = $("#showPropertyName").val();
  if (nature != 1 && nature != 2 && $.trim(showPropertyName) == "") {
    $("#property_error").addClass("dev_error_tip");
  } else if (nature != 1 && nature != 2) {
    $("#nature").val($("#showPropertyName").attr("item"));
  }
  // 检查国家
  var area = $("div[selector-id='1st_area']").attr("sel-value");
  if ($.trim(area) == "") {
    $("#area_erroe_tip").addClass("dev_error_tip");
  }

  var length = $(".dev_error_tip").length;
  // 检查服务条款
  if ($("#service_terms").html() != "check_box") {
    scmpublictoast("请同意遵守 [ 服务条款 ]", 1500);
    return false;
  }
  if (length > 0) {
    return false;
  } else {
    return true;
  }

}

// =====================================================================机构主页列表

/**
 * 创建机构主页
 */
Institution.createInsPage = function() {
  window.open("/prjweb/ins/create/main", "_blank");
};

Institution.ajaxInsPageList = function(insPageType) {
  BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function() {
    $.ajax({
      url : "/prjweb/ins/ajaxinspagelist",
      type : "post",
      dataType : "html",
      data : {
        "insPageType" : insPageType,
      },
      success : function(data) {
        $("#ins_list_id").html(data);
      },
      error : function() {
      }
    });
  }, 0);
}

Institution.ajaxCancelFollow = function(des3InsId, obj) {
  BaseUtils
      .checkCurrentSysTimeout(
          "/prjweb/ajaxtimeout",
          function() {
            $
                .ajax({
                  url : "/prjweb/ins/ajaxcancelfollow",
                  type : "post",
                  dataType : "json",
                  data : {
                    "des3InsId" : des3InsId,
                  },
                  success : function(data) {
                    if (data.result == "success") {
                      scmpublictoast("取消关注成功", 1500);
                      $(obj).closest(".create-InstitutionalGroups_body-item").remove();
                      var msg = "<div class='response_no-result' style='margin: 0 auto;'><div class='no_effort' style='padding: 88px 0 50px 130px !important;'>"
                          + "<h2 style='height: 40px!important; line-height: 40px!important; font-size: 20px;'>你还未关注任何机构主页</h2><div class='no_effort_tip'>"
                          + "<span>温馨提示：</span><p>通过全站检索框，可检索已经在科研之友平台开通主页的机构 .</p><p style='width:110%;'>关注机构主页，可及时了解机构的最新科研动态和成果，促进潜在合作机会.</p></div></div></div>";
                      if ($("#ins_list_id").find(".create-InstitutionalGroups_body-item").length == 0) {
                        document.getElementById("ins_list_id").innerHTML = msg;
                      }
                    } else {
                      scmpublictoast("取消关注失败", 1500);
                    }
                  },
                  error : function() {
                  }
                });
          })
}

/**
 * @param url
 */
Institution.openUrl = function(url) {
  window.open(url, "_blank");
}
/**
 * @param url
 */
Institution.changeServiceTerms = function() {
  if ($("#service_terms").html() == "check_box") {
    $("#service_terms").html("check_box_outline_blank");
  } else {
    $("#service_terms").html("check_box");
  }
}
