var getregionselect = function(url) {
  if (!document.getElementsByClassName("region-select_container").length > 0) {
    var contentele = document.createElement("div");
    contentele.className = "region-select_container";
    var innerele = '<div class="region-select_container-header">'
        + '<span class="region-select_container-header_cancle region-select_container-header_close">取消</span>'
        + '<span class="region-select_container-header_confirm region-select_container-header_close">确定</span>'
        + '</div>' + '<div class="region-select_container-body">' + '<div class="region-select_container-body_show">'
        + '<div class="region-select_container-body_show-item"></div>'
        + '<div class="region-select_container-body_show-item"></div>'
        + '<div class="region-select_container-body_show-item"></div>' + '</div>' + '<div style="overflow: hidden;">'
        + '<div class="region-select_container-body_selecter" style="margin-top: 4px;">'
        + '<div class="region-select_item">'
        + '<div class="region-select_container-body_selecter-item region-select_container-body_first">' + '</div>'
        + '</div>' + '<div class="region-select_item">'
        + '<div class="region-select_container-body_selecter-item region-select_container-body_second">' + '</div>'
        + '</div>' + '<div class="region-select_item">'
        + '<div class="region-select_container-body_selecter-item region-select_container-body_third">' + '</div>'
        + '</div>' + '</div>' + '</div>' + '</div>';
    contentele.innerHTML = innerele;
  }
  if (!document.getElementsByClassName("new-normal_background").length > 0) {
    var layerbox = document.createElement("div");
    layerbox.className = "new-normal_background";
    document.body.appendChild(layerbox);
    document.getElementsByClassName("new-normal_background")[0].appendChild(contentele);
    getregiondata(url);
    if ((document.getElementById("newRegionId").value != "")
        || (document.getElementById("newRegionId").value.split(",") != "")) {
      var startval = document.getElementById("newRegionId").value.split(",");
      if (startval.length == 1) {
        document.getElementsByClassName("region-select_container-body_show-item")[0].innerHTML = startval[0]
      } else if (startval.length == 2) {
        document.getElementsByClassName("region-select_container-body_show-item")[0].innerHTML = startval[0]
        document.getElementsByClassName("region-select_container-body_show-item")[1].innerHTML = startval[1]
      } else {
        document.getElementsByClassName("region-select_container-body_show-item")[0].innerHTML = startval[0]
        document.getElementsByClassName("region-select_container-body_show-item")[1].innerHTML = startval[1]
        document.getElementsByClassName("region-select_container-body_show-item")[2].innerHTML = startval[2]
      }
    }
    var closelist = document.getElementsByClassName("region-select_container-header_close");
    var showtarget = document.getElementsByClassName("region-select_container")[0];
    for (var i = 0; i < closelist.length; i++) {
      closelist[i].onclick = function() {
        showtarget.style.bottom = -500 + "px";
        if (this.classList.contains("region-select_container-header_confirm")) {
          var regionArray = [];
          var firstname = document.getElementsByClassName("region-select_container-body_show-item")[0].innerHTML;
          if (firstname != "" && firstname != null) {
            regionArray.push(firstname);
          }
          var secondname = document.getElementsByClassName("region-select_container-body_show-item")[1].innerHTML;
          if (secondname != "" && secondname != null) {
            regionArray.push(secondname);
          }
          var thirdname = document.getElementsByClassName("region-select_container-body_show-item")[2].innerHTML;
          if (thirdname != "" && thirdname != null) {
            regionArray.push(thirdname);
          }
          var totalname = regionArray.join(",").toString();
          if (document.getElementsByClassName("new-edit_psntitle-body_item-input_areabox").length > 0) {
            if (regionArray.length != 0) {
              document.getElementsByClassName("new-edit_psntitle-body_item-input_areabox")[0].onfocus();
              document.getElementsByClassName("new-edit_psntitle-body_item-input_areabox")[0].value = totalname;
            }
          }
        }
        document.getElementsByClassName("new-normal_background")[0].removeChild(showtarget);
        document.body.removeChild(document.getElementsByClassName("new-normal_background")[0]);
        document.getElementById("psnRegionId").value = document.getElementById("selectRegionId").value;
      }
    }
  }
}

var getregiondata = function(dataurl, datacode) {
  if (dataurl == "") {
    alert("数据请求的地址无效，无法获取有效数据");
  } else {
    $.ajax({
      url : dataurl,
      type : 'post',
      dataType : 'json',
      async : false,
      success : function(data) {
        var taltalarray = data;
        document.getElementsByClassName("region-select_container-body_first")[0].innerHTML = "";
        Array.from(data.country).forEach(function(x) {
          var contentbox = document.createElement("div");
          contentbox.className = "region-select_item-list";
          contentbox.innerHTML = x.name;
          contentbox.setAttribute("data-code", x.code);
          document.getElementsByClassName("region-select_container-body_first")[0].appendChild(contentbox);
        });

        var firtlist = document.getElementsByClassName("region-select_container-body_first")[0]
            .getElementsByClassName("region-select_item-list");

        for (var i = 0; i < firtlist.length; i++) {
          firtlist[i].addEventListener("click",
              function() {
                var name = this.innerHTML;
                var code = this.getAttribute("data-code");
                document.getElementsByClassName("region-select_container-body_show-item")[0].innerHTML = name;
                document.getElementsByClassName("region-select_container-body_show-item")[1].innerHTML = "";
                document.getElementsByClassName("region-select_container-body_show-item")[0].setAttribute("set-code",
                    code);
                document.getElementsByClassName("region-select_container-body_show-item")[1].innerHTML = "";
                document.getElementsByClassName("region-select_container-body_show-item")[1].setAttribute("set-code",
                    "");
                document.getElementsByClassName("region-select_container-body_show-item")[2].innerHTML = "";
                document.getElementsByClassName("region-select_container-body_show-item")[2].setAttribute("set-code",
                    "");
                if (code == "156") {
                  document.getElementsByClassName("region-select_container-body_second")[0].innerHTML = "";
                  Array.from(data.province).forEach(function(x) {
                    var contentbox = document.createElement("div");
                    contentbox.className = "region-select_item-list";
                    contentbox.innerHTML = x.name;
                    contentbox.setAttribute("data-code", x.code);
                    document.getElementsByClassName("region-select_container-body_second")[0].appendChild(contentbox);
                  });

                  var secondlist = document.getElementsByClassName("region-select_container-body_second")[0]
                      .getElementsByClassName("region-select_item-list");
                  for (var i = 0; i < secondlist.length; i++) {
                    secondlist[i].addEventListener("click", function() {
                      var name = this.innerHTML;
                      var code = this.getAttribute("data-code");
                      document.getElementsByClassName("region-select_container-body_show-item")[1].innerHTML = name;
                      document.getElementsByClassName("region-select_container-body_show-item")[2].innerHTML = "";
                      document.getElementsByClassName("region-select_container-body_third")[0].innerHTML = "";
                      document.getElementsByClassName("region-select_container-body_show-item")[1].setAttribute(
                          "set-code", code);
                      Array.from(taltalarray.city[code]).forEach(
                          function(y) {
                            var contentbox = document.createElement("div");
                            contentbox.className = "region-select_item-list";
                            contentbox.innerHTML = y.name;
                            contentbox.setAttribute("data-code", y.code);
                            document.getElementsByClassName("region-select_container-body_third")[0]
                                .appendChild(contentbox);
                          });
                      document.getElementById("selectRegionId").value = code;
                      setthirdlistevent();
                    })
                  }
                }
              })
        };
        bindregiondata();
      }
    })
  }
};

var setthirdlistevent = function() {
  var thirdlist = document.getElementsByClassName("region-select_container-body_third")[0]
      .getElementsByClassName("region-select_item-list");
  for (var i = 0; i < thirdlist.length; i++) {
    thirdlist[i].addEventListener("click", function() {
      var name = this.innerHTML;
      var code = this.getAttribute("data-code");
      document.getElementsByClassName("region-select_container-body_show-item")[2].innerHTML = name;
      document.getElementsByClassName("region-select_container-body_show-item")[2].setAttribute("set-code", code);
    })
  }
  bindregiondata();
};

// 绑定地区选项点击事件，每次点击选项获取其code
var bindregiondata = function() {
  $(".region-select_item-list").bind('click', function() {
    $("#selectRegionId").val($(this).attr("data-code"));
  });
}
// 后退
function goback() {
  document.location.href = "/psnweb/mobile/homepage";
}

// 初始化所在地区
function initRegion() {
  $.ajax({
    url : "/psnweb/psninfo/ajaxRegion",
    type : "post",
    dataType : "json",
    data : {
      "superRegionId" : $("#psnRegionId").val()
    },
    success : function(data) {
      var regionArray = new Array();
      if (data.thirdRegionName != "" && data.thirdRegionName != null) {
        regionArray.push(data.thirdRegionName);
        regionArray.push(data.secondRegionName);
        regionArray.push(data.firstRegionName);
      } else if (data.secondRegionName != "" && data.secondRegionName != null) {
        regionArray.push(data.secondRegionName);
        regionArray.push(data.firstRegionName);
      } else {
        regionArray.push(data.firstRegionName);
      }
      var regionName = regionArray.join(",").toString();
      $("#newRegionId").val(regionName);
    }
  });
}

// 初始化所在单位
function initWorkHistory() {
  var workHistoryArray = new Array();
  workHistoryArray.push($("#psnInsName").val());
  if ($("#psnDepartment").val() != "" && $("#psnDepartment").val() != null) {
    workHistoryArray.push($("#psnDepartment").val());
  }
  if ($("#psnPosition").val() != "" && $("#psnPosition").val() != null) {
    workHistoryArray.push($("#psnPosition").val());
  }
  var workHistoryName = workHistoryArray.join(",").toString();
  if (workHistoryName != "" && workHistoryName != null) {
    $("#workHistory").val(workHistoryName);
    $("#workHistory").addClass("new-edit_psntitle-body_item-input_down");
    $("#workHistory").addClass("new-edit_psntitle-body_item-input-show");
    $("#workHistory").prev("span").addClass("new-edit_psntitle-body_item-detail");
    $("#workHistory").prev("span").addClass("new-edit_psntitle-body_item-detail_inputed");
  }
}

// 打开选择所在地区的选项
function openWorkHistory() {
  event.stopPropagation(event);
  $
      .ajax({
        url : "/psn/mobile/psnInfo/ajaxworkhistorylist",
        type : "post",
        dataType : "json",
        success : function(data) {
          if (data.status == "success") {
            var workhistoryList = "";
            if (data.result.length > 0) {
              $.each(data.result, function(i, item) {
                workhistoryList += "<div class='new-edit_psntitle-body_item-box_selector-item' "
                    + "onclick='selectWorkHistory(this);' id='" + item.workId + "'>" + item.workInsInfoStr + "</div>";
              });
            } else {
              workhistoryList += "<div class='new-edit_psntitle-body_item-box_selector-item' style='text-align: center;margin-top: 13%;'>暂无可选择的单位，请新增工作经历后再选择</div>";
            }
            $("#workHistorySelect").html(workhistoryList);
            document.onclick = function() {
              $("#workHistorySelect").html("");
              $("#workHistorySelect").hide();
            }
          } else {
            scmpublictoast("操作失败, 请稍后再试", 1000);
          }
        }
      });
}

function selectWorkHistory(obj) {
  $("#newWorkId").val($(obj).attr("id"));
  $("#workHistory").val($.trim($(obj).text()));
  $("#workHistorySelect").hide();
  document.onclick = function() {
  }
}

// 构建name参数
function buildName() {
  var name = "";
  var firstName = $.trim($("#newFirstName").val());
  var lastName = $.trim($("#newLastName").val());
  var zhFirstName = $.trim($("#newZhFirstName").val());
  var zhLastName = $.trim($("#newZhLastName").val());
  if ((zhFirstName != null && zhFirstName != "") || (zhLastName != null && zhLastName != "")) {
    name = (zhLastName == null ? null : zhLastName) + (zhFirstName == null ? null : zhFirstName);
  } else {
    name = (firstName == null ? null : firstName) + "" + (lastName == null ? null : lastName);
  }
  return name;
}

function checkPar() {
  var flag = true;
  var firstName = $.trim($("#newFirstName").val());
  if (firstName == "" || firstName == null) {
    $("#firstNameErrorMsg").show();
    flag = false;
  } else {
    $("#firstNameErrorMsg").hide();
  }

  var lastName = $.trim($("#newLastName").val());
  if (lastName == "" || lastName == null) {
    $("#lastNameErrorMsg").show();
    flag = false;
  } else {
    $("#lastNameErrorMsg").hide();
  }
  return flag;
}

function savePsnInfo() {
  if (!checkPar()) {
    return;
  }

  $.ajax({
    url : "/psn/mobile/save/psnInfo",
    type : "post",
    dataType : "json",
    data : {
      "titolo" : $.trim($("#newTitolo").val()),
      "regionId" : $("#psnRegionId").val(),
      "workId" : $("#newWorkId").val(),
      "firstName" : $.trim($("#newFirstName").val()),
      "lastName" : $.trim($("#newLastName").val()),
      "zhFirstName" : $.trim($("#newZhFirstName").val()),
      "zhLastName" : $.trim($("#newZhLastName").val()),
      "name" : buildName(),
      "otherName" : $.trim($("#newMiddleName").val())
    },
    success : function(data) {
      if (data.status == "success") {
        document.location.href = "/psnweb/mobile/homepage";
      } else {
        scmpublictoast("操作失败, 请稍后再试", 1000);
      }
    }
  });
}