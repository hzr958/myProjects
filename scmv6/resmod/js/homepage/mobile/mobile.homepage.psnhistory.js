//判断机构是否为空
function checkInsNameNull() {
  if ($("#newInsName").val() != null && $("#newInsName").val() != "") {
    $("#insNameErrorMsg").css("visibility", "hidden");
    isNullFlag = false;
  } else {
    $("#insNameErrorMsg").css("visibility", "visible");
    isNullFlag = true;
  }
}

// 校验开始年份
function checkFromDate() {
  // 开始时间
  var startYear = $("#work_from_year").val();
  var startMonth = $("#work_from_month").val();
  // 结束时间
  var endYear = $("#work_to_year").val();
  var endMonth = $("#work_to_month").val();
  if (endYear == "nowDate") {
    endYear = $("#nowTime").val().split("-")[0];
    endMonth = $("#nowTime").val().split("-")[1];
    isActive = 1;
  }
  // 开始时间出错的情况：开始时间大于当前时间、开始时间大于结束时间（当结束时间有值时）
  if (parseInt(endYear) < parseInt(startYear)
      || (parseInt(endYear) == parseInt(startYear) && parseInt(endMonth) < parseInt(startMonth))) { // 开始时间校验
    $("#toDateErrorMsg").css("visibility", "hidden");
    $("#fromDateErrorMsg").css("visibility", "visible");
    fromDataFlag = true;
    return;
  }
  $("#fromDateErrorMsg").css("visibility", "hidden");
  $("#toDateErrorMsg").css("visibility", "hidden");
  fromDataFlag = false;
}

// 校验结束年份
function checkToDate() {
  // 开始时间
  var startYear = $("#work_from_year").val();
  var startMonth = $("#work_from_month").val();
  // 结束时间
  var endYear = $("#work_to_year").val();
  var endMonth = $("#work_to_month").val();
  if (endYear == "nowDate") {
    endYear = $("#nowTime").val().split("-")[0];
    endMonth = $("#nowTime").val().split("-")[1];
    isActive = 1;
  } else {
    isActive = 0;
  }
  // 结束时间校验
  if (parseInt(endYear) < parseInt(startYear)
      || (parseInt(endYear) == parseInt(startYear) && parseInt(endMonth) < parseInt(startMonth))) {
    $("#fromDateErrorMsg").css("visibility", "hidden");
    $("#toDateErrorMsg").css("visibility", "visible");
    toDataFlag = true;
    return;
  }
  $("#toDateErrorMsg").css("visibility", "hidden");
  $("#fromDateErrorMsg").css("visibility", "hidden");
  toDataFlag = false;
}

function selectYear() {
  if ($("#work_to_year").val() == "nowDate") {
    $("#work_to_month").css("visibility", "hidden");
  } else {
    $("#work_to_month").css("visibility", "visible");
  }
}

// 年份初始化
function setYear() {
  var start = 1960;
  var nowDate = new Date();
  var end = nowDate.getFullYear() + 10;
  var nowYear = end;
  var years = "";
  for (var i = start; i <= end; i++) {
    years += "<option id='from_" + i + "' value='" + i + "'>" + i + "年</option>";
  }
  $("#work_from_year").html(years);
  $("#work_from_year").val(nowYear);
  years = ""
  for (var i = start; i <= end; i++) {
    years += "<option id='to_" + i + "' value='" + i + "'>" + i + "年</option>";
  }
  years += "<option id='nowDate' value='nowDate' >至今</option>";
  $("#work_to_year").html(years);
  $("#work_to_year").val(nowYear);

  // 若为编辑页面 需初始化默认选中年份
  if ($("#type").val() != "add") {
    if ($("#fromYear").val() != null && $("#fromYear").val() != "") {
      var selectFromYear = "#from_" + $("#fromYear").val();
      $(selectFromYear).attr("selected", "selected");
    }

    if ($("#toYear").val() != null && $("#toYear").val() != "") {
      var selectFromYear = "#to_" + $("#toYear").val();
      $(selectFromYear).attr("selected", "selected");
    } else {
      $("#nowDate").attr("selected", "selected");
      $("#work_to_month").css("visibility", "hidden");
    }
  } else { // 若为新增页面，年份初始化为当前年月份
    var selectFromYear = "#from_" + nowDate.getFullYear();
    $(selectFromYear).attr("selected", "selected");
    var selectFromYear = "#to_" + nowDate.getFullYear();
    $(selectFromYear).attr("selected", "selected");
  }
}

// 月份初始化
function setMonth() {
  var months = "";
  for (var i = 1; i < 13; i++) {
    months += "<option id='from_" + i + "' value='" + i + "'>" + i + "月</option>";
  }
  $("#work_from_month").html(months);
  var months = "";
  for (var i = 1; i < 13; i++) {
    months += "<option id='to_" + i + "' value='" + i + "'>" + i + "月</option>";
  }

  $("#work_to_month").html(months);

  // 若为编辑页面 需初始化默认选中年份
  if ($("#type").val() != "add") {
    if ($("#fromMonth").val() != null && $("#fromMonth").val() != "") {
      var selectFromMonth = "#from_" + $("#fromMonth").val();
      $("#work_from_month").find(selectFromMonth).attr("selected", "selected");
    }

    if ($("#toMonth").val() != null && $("#toMonth").val() != "") {
      var selectToMonth = "#to_" + $("#toMonth").val();
      $("#work_to_month").find(selectToMonth).attr("selected", "selected");
    }
  }
}

// 保存工作经历
function saveWorkHistory() {
  checkInsNameNull();
  //校验起止时间
  checkFromDate();
  checkToDate();
  if(document.getElementsByClassName("datepicker__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("datepicker__box")[0])
  }
  if(document.getElementsByClassName("ac__box").length > 0){
      document.body.removeChild(document.getElementsByClassName("ac__box")[0]);
  }
  if (isNullFlag) {
    scmpublictoast("信息填写不完整", 1000);
    return;
  }
  if (fromDataFlag || toDataFlag) {
    scmpublictoast("信息填写不正确", 1000);
    return;
  }
  
  var toYear = "";
  var toMonth = "";
  if (isActive != 1) {
    toYear = $("#work_to_year").val();
    toMonth = $("#work_to_month").val();
  }

  var data = {
    "workId" : $("#workId").val(),
    "insName" : $("#newInsName").val(),
    "department" : $("#newDepartment").val(),
    "position" : $("#newPosition").val(),
    "fromYear" : $("#work_from_year").val(),
    "fromMonth" : $("#work_from_month").val(),
    "toYear" : toYear,
    "toMonth" : toMonth,
    "description" : $("#newDescription").val(),
    "isActive" : isActive
  };

  $.ajax({
    url : "/psn/mobile/save/workhistory",
    type : "post",
    dataType : "json",
    data : data,
    success : function(data) {
      if (data.result == "success") {
        document.location.href = "/psnweb/mobile/homepage";
      } else {
        scmpublictoast("操作失败, 请稍后再试", 1000);
      }
    }
  });
}

// 保存教育经历
function saveEducateHistory() {
  checkInsNameNull();
  //校验起止时间
  checkFromDate();
  checkToDate();
  if (isNullFlag) {
    scmpublictoast("信息填写不完整", 1000);
    return;
  }
  if (fromDataFlag || toDataFlag) {
    scmpublictoast("信息填写不正确", 1000);
    return;
  }

  var toYear = "";
  var toMonth = "";
  if (isActive != 1) {
    toYear = $("#work_to_year").val();
    toMonth = $("#work_to_month").val();
  }

  var data = {
    "eduId" : $("#eduId").val(),
    "insName" : $("#newInsName").val(),
    "study" : $("#newStudy").val(),
    "degreeName" : $("#newDegreeName").val(),
    "fromYear" : $("#work_from_year").val(),
    "fromMonth" : $("#work_from_month").val(),
    "toYear" : toYear,
    "toMonth" : toMonth,
    "description" : $("#newDescription").val(),
    "isActive" : isActive
  };

  $.ajax({
    url : "/psn/mobile/save/educatehistory",
    type : "post",
    dataType : "json",
    data : data,
    success : function(data) {
      if (data.result == "success") {
        document.location.href = "/psnweb/mobile/homepage";
      } else {
        scmpublictoast("操作失败, 请稍后再试", 1000);
      }
    }
  });
}

// 删除工作经历
function delWorkHistory() {
  Smate.confirm("提示", "要删除该工作经历吗？", function() {
    $.ajax({
      url : "/psn/mobile/del/workhistory",
      type : "post",
      dataType : "json",
      data : {
        "workId" : $("#workId").val()
      },
      success : function(data) {
        if (data.result == "success") {
          document.location.href = "/psnweb/mobile/homepage";
        } else {
          scmpublictoast("操作失败, 请稍后再试", 1000);
        }
      }
    });
  }, ["确定", "取消"]);
}

// 删除教育经历
function delEducateHistory() {
  Smate.confirm("提示", "要删除该教育经历吗？", function() {
    $.ajax({
      url : "/psn/mobile/del/educatehistory",
      type : "post",
      dataType : "json",
      data : {
        "workId" : $("#workId").val()
      },
      success : function(data) {
        if (data.result == "success") {
          document.location.href = "/psnweb/mobile/homepage";
        } else {
          scmpublictoast("操作失败, 请稍后再试", 1000);
        }
      }
    });
  }, ["确定", "取消"]);
}

// 自动填充的机构名称
function showAutoInstitution() {
  var insName = $.trim($("#newInsName").val());
  $("#autoinstitution").html("");
  $("#autoinstitution").css("display", "none");
  if (insName == null || insName == "") {
    return;
  }
  var institutionList = "";
  $.ajax({
    url : "/psn/mobile/edit/ajaxautoinstitution",
    type : "post",
    dataType : "json",
    data : {
      "insName" : insName
    },
    success : function(data) {
      if (data.result != undefined && data.result.length > 0) {
        $.each(data.result, function(i, item) {
          if (i < 5) {
            institutionList += "<div class='new-edit_psntitle-body_item-input_autoshow-item' "
                + "onclick='selectAutoItem(this,event);' name='institution' >" + item.name + "</div>";
          }
        });
        $("#autoinstitution").html(institutionList);
        if (institutionList != "") {
          $("#autoinstitution").css("display", "block");
        }
        // 隐藏下拉框事件
        document.onclick = function() {
          $("#autoinstitution").css("display", "none");
        }
      }
    }
  });
}

// 获取自动填充的部门
function showAutoDepartment() {
  var searchKey = $.trim($("#newDepartment").val());
  if (searchKey == null || searchKey == "") {
    $("#autoinsunit").css("display", "none");
    return;
  }
  var insUnitList = "";
  $.ajax({
    url : "/psn/mobile/edit/ajaxautoinsunit",
    type : "post",
    dataType : "json",
    data : {
      "insName" : $.trim($("#newInsName").val()),
      "searchKey" : searchKey
    },
    success : function(data) {
      if (data.result != undefined && data.result.length > 0) {
        $.each(data.result, function(i, item) {
          if (i < 5) {
            insUnitList += "<div class='new-edit_psntitle-body_item-input_autoshow-item' "
                + "onclick='selectAutoItem(this,event);' name='insunit_department' >" + item.name + "</div>";
          }
        });
        $("#autoinsunit").html(insUnitList);
        if (insUnitList != "") {
          $("#autoinsunit").css("display", "block");
        }
        // 隐藏下拉框事件
        document.onclick = function() {
          $("#autoinsunit").css("display", "none");
        }
      }
    }
  });
}

// 获取自动填充的主修学院
function showAutoStudy() {
  var searchKey = $.trim($("#newStudy").val());
  if (searchKey == null || searchKey == "") {
    $("#autoinsunit").css("display", "none");
    return;
  }
  var insUnitList = "";
  $.ajax({
    url : "/psn/mobile/edit/ajaxautoinsunit",
    type : "post",
    dataType : "json",
    data : {
      "insName" : $.trim($("#newInsName").val()),
      "searchKey" : searchKey
    },
    success : function(data) {
      if (data.result != undefined && data.result.length > 0) {
        $.each(data.result, function(i, item) {
          if (i < 5) {
            insUnitList += "<div class='new-edit_psntitle-body_item-input_autoshow-item' "
                + "onclick='selectAutoItem(this,event);' name='insunit_study' >" + item.name + "</div>";
          }
        });
        $("#autoinsunit").html(insUnitList);
        if (insUnitList != "") {
          $("#autoinsunit").css("display", "block");
        }
      }
    }
  });
}

// 获取自动填充的职称
function showAutoPostion() {
  var position = $.trim($("#newPosition").val());
  $("#autoposition").html("");
  $("#autoposition").css("display", "none");
  if (position == null || position == "") {
    return;
  }
  var positionList = "";
  $.ajax({
    url : "/psn/mobile/edit/ajaxautoposition",
    type : "post",
    dataType : "json",
    data : {
      "position" : position
    },
    success : function(data) {
      if (data.result != undefined && data.result.length > 0) {
        $.each(data.result, function(i, item) {
          if (i < 5) {
            positionList += "<div class='new-edit_psntitle-body_item-input_autoshow-item' "
                + "onclick='selectAutoItem(this,event);' name='autoposition' >" + item.name + "</div>";
          }
        });
        $("#autoposition").html(positionList);
        if (positionList != "") {
          $("#autoposition").css("display", "block");
        }
      }
    }
  });
}

// 获取自动填充的学历
function showAutoDegree() {
  var degreeName = $.trim($("#newDegreeName").val());
  $("#autodegree").html("");
  $("#autodegree").css("display", "none");
  if (degreeName == null || degreeName == "") {
    return;
  }
  var degreeList = "";
  $.ajax({
    url : "/psn/mobile/edit/ajaxautodegree",
    type : "post",
    dataType : "json",
    data : {
      "degreeName" : degreeName
    },
    success : function(data) {
      if (data.result != undefined && data.result.length > 0) {
        $.each(data.result, function(i, item) {
          if (i < 5) {
            degreeList += "<div class='new-edit_psntitle-body_item-input_autoshow-item' "
                + "onclick='selectAutoItem(this,event);' name='autodegree' >" + item.name + "</div>";
          }
        });
        $("#autodegree").html(degreeList);
        if (degreeList != "") {
          $("#autodegree").css("display", "block");
        }
      }
    }
  });
}

// 获取下拉框选中的元素名
function selectAutoItem(obj, event) {
  event.stopPropagation(event);
  var newVal = $.trim($(obj).text());
  var model = $(obj).attr("name");
  switch (model) {
    case "institution" :
      $("#newInsName").val(newVal);
      $("#autoinstitution").css("display", "none");
    break;
    case "insunit_department" :
      $("#newDepartment").val(newVal);
      $("#autoinsunit").css("display", "none");
    break;
    case "insunit_study" :
      $("#newStudy").val(newVal);
      $("#autoinsunit").css("display", "none");
    break;
    case "autoposition" :
      $("#newPosition").val(newVal);
      $("#autoposition").css("display", "none");
    break;
    case "autodegree" :
      $("#newDegreeName").val(newVal);
      $("#autodegree").css("display", "none");
    break;
  }
  document.onclick = function() {
  };
}

// 兼容安卓APP，聚焦描述时自动下拉
function inputDescription() {
  $("html,body").animate({
    scrollTop : $("#newDescription").offset().top
  }, 100);
}