var Improve = Improve ? Improve : {};

// 加载信息完善页面
Improve.preparePage = function() {
  $.ajax({
    url : "/psnweb/improve/ajaxinfo",
    type : "post",
    data : {

    },
    dataType : "html",
    success : function(data) {
      if (data != null && data != "") {
        $("#improveInfoDiv").html(data);
        if ($('#needWorkEdu').val() == "true") {
          showDialog("psnWorkEduBox");
        } else if ($("#needArea").val() == "true") {
          showDialog("scienceAreaBox");
        } else if ($("#needKeywords").val() == "true") {
          showDialog("psnKeyWordsBox");
        }
        Improve.dealNextBtn();
      }
    },
    error : function() {
    }
  });
}

Improve.showScienceArea = function() {
  $.ajax({
    url : "/psnweb/improve/ajaxarea",
    type : "post",
    data : {

    },
    dataType : "html",
    success : function(data) {
      if (data != null && data != "") {
        $("#sciencearea_box_div").html(data);
        showDialog("scienceAreaBox");
        hideDialog("psnKeyWordsBox");
      }
    },
    error : function() {
    }
  });
};

// 替换关键词
Improve.replaceKeywords = false; // 开关
Improve.replaceKeywordsDom = "";
/**
 * 返回到关键词
 */
Improve.returnScienceArea = function() {
  Improve.replaceKeywords = true;
  Improve.replaceKeywordsDom = $("#psnKeyWordsBox");
  Improve.showScienceArea();
}

Improve.showKeywords = function(scienceAreaIds,newScienceAreaIds) {
  // 是否加载关键词SCM-15658
  if (Improve.replaceKeywords === true) {
    //SCM-24041
    if(newScienceAreaIds != '' && scienceAreaIds == newScienceAreaIds){
      hideDialog("scienceAreaBox");
      Improve.replaceKeywords = false;
      $("#psnKeyWordsBox").remove();// 移除现在的
      // 添加以前的div
      $("#keywords_box_div").append(Improve.replaceKeywordsDom);
      showDialog("psnKeyWordsBox");
      return;
    }
  }

  $.ajax({
    url : "/psnweb/improve/ajaxkeywords",
    type : "post",
    data : {

    },
    dataType : "html",
    beforeSend : function() {
      $("#psnKeyWordsBox").doLoadStateIco({
        status : 1
      });// 加载圈
    },
    success : function(data) {
      $("#psnKeyWordsBox").find(".preloader").remove();// 隐藏加载圈
      if (data != null && data != "") {
        $("#keywords_box_div").html(data);
        hideDialog("scienceAreaBox");
        showDialog("psnKeyWordsBox");
      }
    },
    error : function() {
      hideDialog("psnKeyWordsBox");
    }
  });
}

// 保存科技领域
Improve.savePsnScienceArea = function(obj) {
  if (obj != undefined) {// 防止重复点击
    Improve.doHitMore(obj, 3000);
  }
  if (checkIsNull($("#scienceAreaIds").val())) {
    scmpublictoast(homepage.scienceAreaNotNull, 1000);
    return false;
  }
  var scienceAreaIds = $("#scienceAreaIds").val();
  var newScienceAreaIds = $("#newScienceAreaIds").val();
  $.ajax({
    url : "/psnweb/sciencearea/ajaxsave",
    type : "post",
    dataType : "json",
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
      "scienceAreaIds" : scienceAreaIds
    },
    beforeSend : function() {
      $("#scienceAreaBox").doLoadStateIco({
        status : 1
      });// 加载圈
    },
    success : function(data) {
      $("#scienceAreaBox").find(".preloader").remove();// 隐藏加载圈
      Improve.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          Improve.showKeywords(scienceAreaIds,newScienceAreaIds);
          // hideDialog("scienceAreaBox");
          // showDialog("psnKeyWordsBox");
        }
        // 如果
        if ($('.needScienceArea').val() == "true" && $('.needKeyWords').val() == "false") {
          MainBase.autoFollowPsn();
        }
      });

    },
    error : function() {
      hideDialog("scienceAreaBox");
    }
  });
}

// 选中科技领域
Improve.addScienceArea = function(id) {
  var key = $("#" + id + "_category_title").html();
  var sum = parseInt($("#scienceAreaSum").val());
  if (sum < 5) {
    var html = '<div class="main-list__item" id="choosed_' + id + '">' + '<div class="main-list__item_content">' + key
        + '</div>' + '<div class="main-list__item_actions" onclick="javascript:Improve.delScienceArea(\'' + id
        + '\');"><i class="material-icons">close</i></div></div>';
    $("#choosed_area_list").append(html);
    $("#unchecked_area_" + id).attr("onclick", "");
    $("#unchecked_area_" + id).attr("id", "checked_area_" + id);
    $("#" + id + "_status").html("check");
    $("#" + id + "_status").css("color", "forestgreen");
    $("#scienceAreaSum").val(sum + 1);
    $("#scienceAreaIds").val($("#scienceAreaIds").val() + "," + id);
  } else {
    scmpublictoast(homepage.scienceAreaLimit, 1500);
    // 出提示语
  }
  Improve.dealNextBtn();
}

// 删除已选科技领域
Improve.delScienceArea = function(id) {
  $("#choosed_" + id).remove();
  $("#checked_area_" + id).attr("onclick", "Improve.addScienceArea('" + id + "')");
  $("#checked_area_" + id).attr("id", "unchecked_area_" + id);
  $("#" + id + "_status").html("add");
  $("#" + id + "_status").css("color", "");
  $("#scienceAreaIds").val($("#scienceAreaIds").val().replace("," + id, ""));
  var sum = parseInt($("#scienceAreaSum").val());
  $("#scienceAreaSum").val(sum - 1);
  Improve.dealNextBtn();
}

// 判断当前选中的科技领域数量，禁用或启用下一步按钮
Improve.dealNextBtn = function() {
  if (checkIsNull($("#scienceAreaIds").val())) {
    $("#nextToKeywords").attr("disabled", "true");
  } else {
    $("#nextToKeywords").removeAttr("disabled");
  }
}

// 防止多次点击
Improve.doHitMore = function(obj, time) {
  $(obj).attr("disabled", true);
  var click = $(obj).attr("onclick");
  if (click != null && click != "") {
    $(obj).attr("onclick", "");
  }
  setTimeout(function() {
    $(obj).removeAttr("disabled");
    if (click != null && click != "") {
      $(obj).attr("onclick", click);
    }
  }, time);
};

// 超时处理
Improve.ajaxTimeOut = function(data, myfunction) {
  var toConfirm = false;

  if ('{"ajaxSessionTimeOut":"yes"}' == data) {
    toConfirm = true;
  }
  if (!toConfirm && data != null) {
    toConfirm = data.ajaxSessionTimeOut;
  }
  if (toConfirm) {
    jConfirm(homepage.timeOut, homepage.tips, function(r) {
      if (r) {
        document.location.href = window.location.href;
        return 0;
      }
    });

  } else {
    if (typeof myfunction == "function") {
      myfunction();
    }
  }
};

// 处理关键词输入框完成按钮状态
Improve.dealCompleteBtn = function() {
  var keyCount = $("#oneKeyWords").find(".chip__box").length;
  if (keyCount > 0) {
    $("#completeBtn").removeAttr("disabled");
  } else {
    $("#completeBtn").attr("disabled", "true");
  }
}

// 删除关键词
Improve.removeKeywords = function(id) {
  if (id != "" && id != null) {
    var boxkeywords = "#" + id;
    $(boxkeywords).hide();
  }
  Improve.dealCompleteBtn();
}

// 删除关键词
// Improve.delThisKeyWords = function(obj){
// $(obj).parent("div.psnkeyword_show").remove();
// Improve.dealCompleteBtn();
// }

// 添加关键词
Improve.addPsnKeywords = function(id, keyword, $createGrpChipBox) {
  var number = $(".chip-panel__stats").text();
  var numberArray = new Array();
  numberArray = number.split("/");
  if (parseInt(numberArray[0]) >= 10) {
    return;
  }
  if (id != null && id != "") {
    var rcdKeyword = "#" + id;
    $(rcdKeyword).remove();
    var strS = "<div class=\"chip__box\" id=\"disciplinesAndKeywords\"><div class=\"chip__avatar\"></div><div class=\"chip__text\">";
    var strE = "</div><div class=\"chip__icon icon_delete\" onclick=\"javascript:Improve.removeKeywords('boxkeywords');\"><i class=\"material-icons\">close</i></div>";
    var str = "";
    str = strS + keyword + strE;
    var boxkeywordsNumber = parseInt(numberArray[0]) + 1;
    str = str.replace("boxkeywords", "boxkeywords_" + boxkeywordsNumber);
    $("#autokeywords").before(str);
  }
  $createGrpChipBox.chipBoxInitialize();
  $("#completeBtn").removeAttr("disabled");

}

// 保存人员关键词
Improve.savePsnKeyWords = function(obj) {
  var keysArr = [];
  var keyStrArr = [];
  var keyStr = "";
  $.each($("#oneKeyWords").find(".chip__text"), function(i, o) {
    keysArr.push({
      'keys' : $(o).text()
    });
    keyStrArr.push($(o).text().toUpperCase());
  });
  if (arrayCheck(keyStrArr)) {
    scmpublictoast(homepage.keyWordRepeat, 1000);
    return;
  }
  if (obj != undefined) {// 防止重复点击
    Improve.doHitMore(obj, 3000);
  }
  var str_key = JSON.stringify(keysArr);
  if (str_key == "[]") {// 关键词为空
    str_key = '[{"keys":""}]';// 区分学科领域与关键词
    scmpublictoast(homepage.keywordsNotNull, 1000);
    return null;
  }
  var postData = {
    'keywordStr' : str_key,
    'des3PsnId' : $("#des3PsnId").val()
  };
  $.ajax({
    url : "/psnweb/keywords/ajaxsave",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      Improve.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          hideDialog("psnKeyWordsBox");
          // 显示账号邮箱验证
          checkAccountEmail();
        }
        // 最后自动加载关注人员
        if ($('.needKeyWords').val() == "true") {
          MainBase.autoFollowPsn();
        }
      });
    },
    error : function() {
    }
  });
}

function arrayCheck(arr)// 数组查重
{
  var hash = {};
  for ( var i in arr) {
    if (hash[arr[i]]) {
      return true;
    }
    hash[arr[i]] = true;
  }
  return false;
}

// 检查是否为空
function checkIsNull(param) {
  var check = false;
  if (param == null || param == "" || typeof (param) == "undefined") {
    check = true;
  } else {
    param = Improve.trimLeftAndRightSpace(param);
    if (param == "") {
      check = true;
    }
  }
  return check;
}

// 去掉左右两端空格
Improve.trimLeftAndRightSpace = function(str) { // 删除左右两端的空格
  return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 工作经历至今的点击事件
 */
Improve.clickUpToNow = function(obj) {
  var upToNow = $(obj).prop("checked");
  var type = $('#dev_select_workedu').val() == "edu" ? "edu" : "work";
  if (upToNow) {
    var $input = $(obj).closest(".form__sxn_row").find(".dev_end_year  input");
    $input.val("")
    $input.attr("date-year", "");
    $input.attr("date-month", "");
    $input.attr("date-date", "");
    $(obj).closest(".form__sxn_row").find(".dev_end_year").addClass("");
    if (type == "edu") {
      $("#addEduToYearDiv").removeClass("input_not-null input_invalid");
      $("#addEduToYearHelp").attr("invalid-message", "");
    } else {
      $("#addWorkToYearDiv").removeClass("input_not-null input_invalid");
      $("#addWorkToYearHelp").attr("invalid-message", "");
    }
    // SCM-16168 修改endTime 为可点击
    $input.parent().bind("click", function() {
      obj.checked = false;
    });
  } else {
    var newEndYear = $("#newEndTime").attr("date-year");// work
    var endYear = $("#eduToTime").attr("date-year");// edu
    if (type == "edu") {
      if (isNaN(endYear) || endYear == "") {
        $("#addEduToYearDiv").removeClass("input_not-null").addClass("input_invalid");
        $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
      }
    } else {
      if (isNaN(newEndYear) || newEndYear == "") {
        $("#addWorkToYearDiv").removeClass("input_not-null").addClass("input_invalid");
        $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
      }
    }
    $(obj).closest(".form__sxn_row").find(".dev_end_year").removeClass("input_disabled");
  }
};

// 首页-保存工作经历/教育经历
Improve.saveWorkEdu = function(type, obj) {
  if (obj != undefined) {// 防止重复点击
    BaseUtils.doHitMore(obj, 3000);
  }
  if(document.getElementsByClassName("datepicker__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("datepicker__box")[0])
  }
  var result = true;
  // 工作
  var url = "/psnweb/workhistory/ajaximprovesave";
  var insId = "";
  var addIsActive = 0; // 至今
  if ($('#dev_select_workedu').val() == "edu") {
    type = "edu";
    // 经历
    url = "/psnweb/eduhistory/ajaximprovesave";
  } else {
    type = "work";
  }
  var currentDate = new Date();
  // 工作经历
  if (type == "work") {
    if ($("#newInsName").val().trim() == "") {
      $("#newInsNameDiv").addClass("input_invalid");
      $("#newInsNameHelper").attr("invalid-message", homepage.inputInsName);
      result = false;
    }
    var startYear = parseInt($("#newStartTime").attr("date-year"));
    var startMonth = parseInt($("#newStartTime").attr("date-month"));
    var toYear = parseInt($("#newEndTime").attr("date-year"));
    var toMonth = parseInt($("#newEndTime").attr("date-month"));
    var addWorkupToNow = $("#addWorkUpToNow").prop("checked")
    if (addWorkupToNow) {
      addIsActive = 1;
    }
    if (isNaN(toYear) && addIsActive == 0) {
      $("#addWorkToYearDiv").addClass("input_invalid");
      $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
      result = false;
    }
    if (isNaN(startYear)) {
      $("#addWorkFromYearDiv").addClass("input_invalid");
      $("#addWorkFromYearHelp").attr("invalid-message", homepage.trueStartTime);
      result = false;
    } else {
      if (!((!isNaN(toYear) && Improve.compareTime(startYear, startMonth, toYear, toMonth)) || addIsActive == 1)) {
        $("#addWorkToYearDiv").addClass("input_invalid");
        $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      } else {
        $("#addWorkToYearDiv").removeClass("input_invalid");
        $("#addWorkToYearHelp").attr("invalid-message", "");
        $("#addWorkFromYearDiv").removeClass("input_invalid");
        $("#addWorkFromYearHelp").attr("invalid-message", "");
      }
      if (addIsActive == 1
          && !Improve.compareTime(startYear, startMonth, currentDate.getFullYear(), currentDate.getMonth() + 1)) {
        $("#addWorkFromYearDiv").addClass("input_invalid");
        $("#addWorkFromYearHelp").attr("invalid-message", homepage.trueStartTime);
        result = false;
      }
    }
    var addData = {
      'insId' : $("#newInsName").attr("code"),
      'insName' : $("#newInsName").val().trim(),
      'department' : $("#newDepartment").val().trim(),
      'position' : $("#newPosition").val().trim(),
      'anyUser' : 7,
      'fromYear' : $("#newStartTime").attr("date-year"),
      'fromMonth' : $("#newStartTime").attr("date-month") == "null" ? "" : $("#newStartTime").attr("date-month"),
      'toYear' : $("#newEndTime").attr("date-year") == "null" ? "" : $("#newEndTime").attr("date-year"),
      'toMonth' : $("#newEndTime").attr("date-month") == "null" ? "" : $("#newEndTime").attr("date-month"),
      'isActive' : addIsActive
    };
    var code = $('#insName').attr("code");
    if (code == null || code == undefined) {
      insId = "";
    }
  }
  // 教育经历
  if (type == "edu") {
    if ($("#eduInsName").val().trim() == "") {
      $("#eduInsNameDiv").addClass("input_invalid");
      $("#eduInsNameHelper").attr("invalid-message", homepage.inputInsName);
      result = false;
    }
    var startYear = parseInt($("#eduFromTime").attr("date-year"));
    var startMonth = parseInt($("#eduFromTime").attr("date-month"));
    var toYear = parseInt($("#eduToTime").attr("date-year"));
    var toMonth = parseInt($("#eduToTime").attr("date-month"));
    var addEduUpToNow = $("#addEduUpToNow").prop("checked");
    if (addEduUpToNow) {
      addIsActive = 1;
    }
    if (isNaN(toYear) && addIsActive == 0) {
      $("#addEduToYearDiv").addClass("input_invalid");
      $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
      result = false;
    }
    if (isNaN(startYear)) {
      $("#addEduYearDiv").addClass("input_invalid");
      $("#addEduYearHelp").attr("invalid-message", homepage.trueStartTime);
      result = false;
    } else {
      if (!((!isNaN(toYear) && Improve.compareTime(startYear, startMonth, toYear, toMonth)) || addIsActive == 1)) {
        $("#addEduToYearDiv").addClass("input_invalid");
        $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
        result = false;
      } else {
        $("#addEduToYearDiv").removeClass("input_invalid");
        $("#addEduToYearHelp").attr("invalid-message", "");
        $("#addEduYearDiv").removeClass("input_invalid");
        $("#addEduYearHelp").attr("invalid-message", "");
      }
      if (addIsActive == 1
          && !Improve.compareTime(startYear, startMonth, currentDate.getFullYear(), currentDate.getMonth() + 1)) {
        $("#addEduYearDiv").addClass("input_invalid");
        $("#addEduYearHelp").attr("invalid-message", homepage.trueStartTime);
        result = false;
      }
    }
    var addData = {
      'insId' : $("#eduInsName").attr("code"),
      'insName' : $("#eduInsName").val().trim(),
      'study' : $("#eduDepartment").val().trim(),
      'degreeName' : $("#eduPosition").val().trim(),
      'anyUser' : 7,
      'fromYear' : $("#eduFromTime").attr("date-year"),
      'fromMonth' : $("#eduFromTime").attr("date-month") == "null" ? "" : $("#eduFromTime").attr("date-month"),
      'toYear' : $("#eduToTime").attr("date-year") == "null" ? "" : $("#eduToTime").attr("date-year"),
      'toMonth' : $("#eduToTime").attr("date-month") == "null" ? "" : $("#eduToTime").attr("date-month"),
      'description' : $("#eduDescription").val(),
      'isActive' : addIsActive
    };
    var code = $('#editEduInsName').attr("code");
    if (code != null && code != "" && code != undefined) {
      insId = code;
    } else {
      insId = $("#editEduInsId").val();
    }
  }
  if (!result) {
    return false;
  }
  $.ajax({
    url : url,
    type : "post",
    dataType : "json",
    data : addData,
    beforeSend : function() {
      /* $("#psnWorkEduBox").doLoadStateIco({status:1}); */// 加载圈
    },
    success : function(data) {
      /* $("#psnWorkEduBox").find(".preloader").remove(); */// 隐藏加载圈
      BaseUtils.ajaxTimeOut(data, function() {
        if (data != null && data.result) {
          if (data.result == "success") {
            hideDialog("psnWorkEduBox");
            if ($('.needScienceArea').val() == "true") {
              showDialog("scienceAreaBox");
            } else if ($('.needKeyWords').val() == "true") {
              showDialog("psnKeyWordsBox");
            }
          } else {
            scmpublictoast(homepage.systemBusy, 1000);
            hideDialog("psnWorkEduBox");
          }
        } else {
          scmpublictoast(homepage.systemBusy, 1000);
          hideDialog("psnWorkEduBox");
        }
        // 如果是个人关键词与科技领域都不需要继续填写，在此判断是否需要推荐关注人员并继续操作
        if ($('#needWorkEdu').val() == "true" && $('.needScienceArea').val() == "false"
            && $('.needKeyWords').val() == "false") {
          MainBase.autoFollowPsn();
        }
      });
    },
    error : function() {
      scmpublictoast(homepage.systemBusy, 1000);
      hideDialog("psnWorkEduBox");
    }
  });
};

// 校验时间
Improve.compareTime = function(startYear, startMonth, toYear, toMonth) {
  if (startYear > toYear) {
    return false;
  } else if (startYear == toYear && startMonth > toMonth) {
    return false;
  } else {
    return true;
  }
};

// 校验-教育经历-机构名称
Improve.checkEduName = function() {
  if ($("#eduInsName").val().trim() == "") {
    $("#eduInsNameDiv").addClass("input_invalid");
    $("#eduInsNameHelper").attr("invalid-message", homepage.inputInsName);
  } else {
    $("#eduInsNameDiv").removeClass("input_invalid");
    $("#eduInsNameHelper").attr("invalid-message", "");
  }
};

// 校验-教育经历-开始时间
Improve.checkEduDate = function() {
  if ($("div.datepicker__box").css("display") != "none") {
    // 如果正在选择，则不触发校验
    return true;
  }
  // 当前时间
  var nowYear = new Date().getFullYear();
  var nowMonth = new Date().getMonth() + 1;
  // 开始时间
  var startYear = $("#eduFromTime").attr("date-year");
  var startMonth = $("#eduFromTime").attr("date-month");
  // 结束时间
  var endYear = $("#eduToTime").attr("date-year");
  var endMonth = $("#eduToTime").attr("date-month");

  if (isNaN(startYear)) {
    $("#addEduYearDiv").addClass("input_invalid");
    $("#addEduYearHelp").attr("invalid-message", homepage.trueStartTime);
  } else {
    $("#addEduYearDiv").removeClass("input_invalid");
    $("#addEduYearHelp").attr("invalid-message", "");
    // 开始时间出错的情况：开始时间大于当前时间、开始时间大于结束时间（当结束时间有值时）
    if (nowYear < startYear
        || (nowYear == startYear && nowMonth < startMonth)
        || (!isNaN(endYear) && endYear != "" && (endYear < startYear || (endYear == startYear && endMonth < startMonth)))) { // 开始时间校验
      $("#addEduYearDiv").addClass("input_invalid");
      $("#addEduYearHelp").attr("invalid-message", homepage.trueStartTime);
    }
    if (!isNaN(endYear) && (endYear > startYear || (endYear == startYear && endMonth >= startMonth))) {
      $("#addEduToYearDiv").removeClass("input_invalid");
      $("#addEduToYearHelp").attr("invalid-message", "");
    }
  }
};
// 校验-教育经历-结束时间
Improve.checkEduEndDate = function() {
  if ($("div.datepicker__box").css("display") != "none") {
    // 如果正在选择，则不触发校验
    return true;
  }
  // 当前时间
  var nowYear = new Date().getFullYear();
  var nowMonth = new Date().getMonth() + 1;
  // 结束时间
  var endYear = $("#eduToTime").attr("date-year");
  var endMonth = $("#eduToTime").attr("date-month");
  // 开始时间
  var startYear = $("#eduFromTime").attr("date-year");
  var startMonth = $("#eduFromTime").attr("date-month");
  var checked = $("#addEduUpToNow").prop("checked");

  if (isNaN(endYear) && !checked) {
    $("#addEduToYearDiv").addClass("input_invalid");
    $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
  } else {
    $("#addEduYearDiv").removeClass("input_invalid");
    $("#addEduYearHelp").attr("invalid-message", "");
    $("#addEduToYearDiv").removeClass("input_invalid");
    $("#addEduToYearHelp").attr("invalid-message", "");
    if (nowYear < endYear || nowMonth < endMonth) { // 结束时间校验
      $("#addEduToYearDiv").addClass("input_invalid");
      $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
    }
    if (!isNaN(startYear) && (endYear < startYear || (endYear == startYear && endMonth < startMonth))) { // 开始时间校验
      $("#addEduToYearDiv").addClass("input_invalid");
      $("#addEduToYearHelp").attr("invalid-message", homepage.trueEndTime);
    }
  }
};
// 校验-工作经历-机构名称
Improve.checkWorkName = function() {
  if ($("#newInsName").val().trim() == "") {
    $("#newInsNameDiv").addClass("input_invalid");
    $("#newInsNameHelper").attr("invalid-message", homepage.inputInsName);
  } else {
    $("#newInsNameDiv").removeClass("input_invalid");
    $("#newInsNameHelper").attr("invalid-message", "");
  }
};

// 校验-工作经历-开始时间
Improve.checkWorkDate = function() {
  if ($("div.datepicker__box").css("display") != "none") {
    // 如果正在选择，则不触发校验
    return true;
  }
  // 当前时间
  var nowYear = new Date().getFullYear();
  var nowMonth = new Date().getMonth() + 1;
  // 开始时间
  var startYear = $("#newStartTime").attr("date-year");
  var startMonth = $("#newStartTime").attr("date-month");
  // 结束时间
  var endYear = $("#newEndTime").attr("date-year");
  var endMonth = $("#newEndTime").attr("date-month");
  if (isNaN(startYear)) {
    $("#addWorkFromYearDiv").addClass("input_invalid");
    $("#addWorkFromYearHelp").attr("invalid-message", homepage.trueStartTime);
  } else {
    $("#addWorkFromYearDiv").removeClass("input_invalid");
    $("#addWorkFromYearHelp").attr("invalid-message", "");
    // 开始时间出错的情况：开始时间大于当前时间、开始时间大于结束时间（当结束时间有值时）
    if (nowYear < startYear
        || (nowYear == startYear && nowMonth < startMonth)
        || (!isNaN(endYear) && endYear != "" && (endYear < startYear || (endYear == startYear && endMonth < startMonth)))) { // 开始时间校验
      $("#addWorkFromYearDiv").addClass("input_invalid");
      $("#addWorkFromYearHelp").attr("invalid-message", homepage.trueStartTime);
    }
    if (!isNaN(endYear) &&(endYear<=nowYear+10) &&(endYear > startYear || (endYear == startYear && endMonth >= startMonth))) {
      $("#addWorkToYearDiv").removeClass("input_invalid");
      $("#addWorkToYearHelp").attr("invalid-message", "");
    }
  }
};
// 校验-工作经历-结束时间
Improve.checkWorkEndDate = function() {
  if ($("div.datepicker__box").css("display") != "none") {
    // 如果正在选择，则不触发校验
    return true;
  }
  // 当前时间
  var nowYear = new Date().getFullYear();
  var nowMonth = new Date().getMonth() + 1;
  // 结束时间
  var endYear = $("#newEndTime").attr("date-year");
  var endMonth = $("#newEndTime").attr("date-month");
  // 开始时间
  var startYear = $("#newStartTime").attr("date-year");
  var startMonth = $("#newStartTime").attr("date-month");
  var checked = $("#addWorkUpToNow").prop("checked");
  if (isNaN(endYear) && !checked) {
    $("#addWorkToYearDiv").addClass("input_invalid");
    $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
  } else {
    $("#addWorkFromYearDiv").removeClass("input_invalid");
    $("#addWorkFromYearHelp").attr("invalid-message", "");
    $("#addWorkToYearDiv").removeClass("input_invalid");
    $("#addWorkToYearHelp").attr("invalid-message", "");
    if (nowYear+10 < endYear ) { // 结束时间校验   不超过10年
      $("#addWorkToYearDiv").addClass("input_invalid");
      $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
    }
    if (!isNaN(startYear) && (endYear < startYear || (endYear == startYear && endMonth < startMonth))) { // 开始时间校验
      $("#addWorkToYearDiv").addClass("input_invalid");
      $("#addWorkToYearHelp").attr("invalid-message", homepage.trueEndTime);
    }
      if (nowYear < startYear || (nowYear == startYear && nowMonth < startMonth)) { // 开始时间校验
          $("#addWorkFromYearDiv").addClass("input_invalid");
          $("#addWorkFromYearHelp").attr("invalid-message", homepage.trueStartTime);
      }
  }
};

// 检查人员配置信息
Improve.initPsnConfInfo = function() {
  $.ajax({
    url : "/psnweb/psnconf/ajaxinit",
    type : "post",
    datatype : "json",
    data : {},
    success : function(data) {
    },
    error : function() {
    }
  });
}

Improve.compareTime = function(startYear, startMonth, toYear, toMonth) {
  if (startYear > toYear) {
    return false;
  } else if (startYear == toYear && startMonth > toMonth) {
    return false;
  } else {
    return true;
  }

};
