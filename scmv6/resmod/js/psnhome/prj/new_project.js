var NewProject = NewProject ? NewProject : {};

/**
 * 清除所有.New-proManage_containerBody-headerChecked
 */
NewProject.removeAllHeaderChecked = function() {
  $(".New-proManage_containerBody-headerItem").each(function(index, element) {
    if ($(element).hasClass("New-proManage_containerBody-headerChecked")) {
      $(element).removeClass("New-proManage_containerBody-headerChecked");
    }
  });
};

/**
 * url:ajax所需要加载的url params:所需要传输的参数，是一个对象{} func:需要回调的方法
 */
NewProject.ajaxLoadData = function(url, params, func) {
  $.ajax({
    url : url,
    type : 'post',
    data : params,
    dataType : 'html',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        $("#project_content_container").html(data);
        if (func != null) {
          func();
        }
      });
    }
  });
}

/**
 * 加载项目基本信息
 */
NewProject.loadProjectInfo = function(obj, des3PrjId) {
  NewProject.changeUrl("prjinfo");
  // 防重复点击
  BaseUtils.doHitMore(obj, 1000);
  // 先移除选中
  NewProject.removeAllHeaderChecked();
  // 再增加选中
  $(obj).addClass("New-proManage_containerBody-headerChecked");
  $.ajax({
    url : "/prjweb/detail/ajaxprjinfo",
    type : 'post',
    data : {
      "des3PrjId" : des3PrjId
    },
    dataType : 'html',
    success : function(data) {
      $("#project_content_container").html(data);
    }
  });
};

/**
 * 加载项目经费
 */
NewProject.loadProjectExpenditure = function(obj, des3PrjId) {
  NewProject.changeUrl("exp");
  // 防重复点击
  BaseUtils.doHitMore(obj, 1000);
  // 先移除选中
  NewProject.removeAllHeaderChecked();
  // 再增加选中
  $(obj).addClass("New-proManage_containerBody-headerChecked");
  // 加载项目经费信息，超时已经统一做了
  NewProject.ajaxLoadData("/prjweb/detail/ajaxprjexpenditure", {
    "des3PrjId" : des3PrjId
  }, null);
};

/**
 * 加载项目报告
 */
NewProject.loadProjectReport = function(obj, des3PrjId) {
  NewProject.changeUrl("report");
  // 防重复点击
  BaseUtils.doHitMore(obj, 1000);
  // 先移除选中
  NewProject.removeAllHeaderChecked();
  // 再增加选中
  $(obj).addClass("New-proManage_containerBody-headerChecked");
  // 加载项目报告信息，超时已经统一做了
  NewProject.ajaxLoadData("/prjweb/detail/ajaxprjreport", {
    "des3PrjId" : des3PrjId
  }, null);
};

/**
 * 加载项目变更(暂时不做)
 */
NewProject.loadProjectAlteration = function(obj, des3PrjId) {
  // 防重复点击
  BaseUtils.doHitMore(obj, 1000);
  // 先移除选中
  NewProject.removeAllHeaderChecked();
  // 再增加选中
  $(obj).addClass("New-proManage_containerBody-headerChecked");
  // 加载项目变更信息，超时已经统一做了
  NewProject.ajaxLoadData("", {
    "des3PrjId" : des3PrjId
  }, null);
};

/**
 * 加载项目成果
 */
NewProject.loadProjectPub = function(obj, des3PrjId) {
  NewProject.changeUrl("pub");
  // 防重复点击
  BaseUtils.doHitMore(obj, 1000);
  // 先移除选中
  NewProject.removeAllHeaderChecked();
  // 再增加选中
  $(obj).addClass("New-proManage_containerBody-headerChecked");
  // 加载项目成果信息，超时已经统一做了
  NewProject.ajaxLoadData("/prjweb/detail/ajaxprjpubinfo", {
    "des3PrjId" : des3PrjId
  }, null);
};

NewProject.ajaxLoadExpenAccessory = function(expenRecordId) {
  $(".New-ChangeNormal_Bodyitem-Upload_Detail").html("");
  if (expenId != null && expenId != "") {
    $
        .ajax({
          url : "/prjweb/detail/ajaxexpenaccessory",
          type : 'post',
          data : {
            "expenRecordId" : expenRecordId
          },
          dataType : 'json',
          success : function(data) {
            if (data == null || data == "") {
              return;
            }
            for (var i = 0; i < data.length; i++) {
              var item = '<div class="New-ChangeNormal_Bodyitem-Upload_Detailitem">'
                  + '<div class="New-ChangeNormal_Bodyitem-Upload_Detailtext" des3FileId="'
                  + data[i].des3FileId
                  + '">'
                  + data[i].fileName
                  + '</div>'
                  + '<i class="material-icons New-ChangeNormal_Bodyitem-Upload_Detailclose" onclick="NewProject.closeAccessoy(this);" >close</i>'
                  + '</div>';
              $(".New-ChangeNormal_Bodyitem-Upload_Detail").append(item);
            }
          }
        });
  }
}
/**
 * 记一笔，操作按钮
 */
NewProject.addExpenditure = function() {
  var dataHtml = $("#project_content_container").find(".New-proManage_RecordBody").children().length;
  if (dataHtml == 0) {
    scmpublictoast("暂无项目经费科目，请导入后重试！", 1000);
    return;
  }
  $.ajax({
    url : "/prjweb/detail/ajaxexpenadd",
    type : 'post',
    data : {
      "des3PrjId" : $("#des3PrjId").val()
    },
    dataType : 'html',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        $("#project_content_container").parent().append(data);
        NewProject.initAddExpenBox();
      });
      var parentList = document.getElementsByClassName("New-ChangeNormal_Bodyitem-Box");
      for (var i = 0; i < parentList.length; i++) {
        parentList[i].addEventListener("click", function() {
          this.querySelector("input").focus();
        })
      }
    }
  });
}

/**
 * 初始化记一笔的窗口
 */
NewProject.initAddExpenBox = function() {
  var mainEle = document.getElementsByClassName("New-ChangeNormal_Container")[0];
  mainEle.style.left = (window.innerWidth - mainEle.offsetWidth) / 2 + "px";
  mainEle.style.top = (window.innerHeight - mainEle.offsetHeight) / 2 + "px";
  window.onresize = function() {
    mainEle.style.left = (window.innerWidth - mainEle.offsetWidth) / 2 + "px";
    mainEle.style.top = (window.innerHeight - mainEle.offsetHeight) / 2 + "px";
  }

  var Valuelist = document.getElementsByClassName("New-ChangeNormal_Bodyitem-Input");
  for (var i = 0; i < Valuelist.length; i++) {
    if (Valuelist[i].value != "") {
      Valuelist[i].closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .add("New-ChangeNormal_Bodyitem-TitleInput");
    }
  }

  // 绑定input的鼠标移开和选中事件
  var inputlist = document.getElementsByClassName("New-ChangeNormal_Bodyitem-Input");
  for (var i = 0; i < inputlist.length; i++) {
    inputlist[i].onfocus = function() {
      this.closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .add("New-ChangeNormal_Bodyitem-TitleColor");
      this.closest(".New-ChangeNormal_Bodyitem-Box").classList.add("New-ChangeNormal_Bodyitem-Bottom");
      if (!this.closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .contains("New-ChangeNormal_Bodyitem-TitleInput")) {
        this.closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
            .add("New-ChangeNormal_Bodyitem-TitleInput");
      }
    }
    inputlist[i].onblur = function() {
      var setText = this.value;
      this.closest(".New-ChangeNormal_Bodyitem-Box").classList.remove("New-ChangeNormal_Bodyitem-Bottom");
      this.closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .remove("New-ChangeNormal_Bodyitem-TitleColor");
      if (setText == "") {
        if (this.closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
            .contains("New-ChangeNormal_Bodyitem-TitleInput")) {
          this.closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
              .remove("New-ChangeNormal_Bodyitem-TitleInput");
        }
      }
    }
    // 校验是否有填入值
    if (inputlist[i].value == "") {
      if (inputlist[i].closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .contains("New-ChangeNormal_Bodyitem-TitleInput")) {
        inputlist[i].closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
            .remove("New-ChangeNormal_Bodyitem-TitleInput");
      }
    } else {
      inputlist[i].closest(".New-ChangeNormal_Bodyitem-Box").classList.remove("New-ChangeNormal_Bodyitem-Bottom");
      inputlist[i].closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .remove("New-ChangeNormal_Bodyitem-TitleColor");
      inputlist[i].closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .add("New-ChangeNormal_Bodyitem-TitleInput");
    }
  }
  document.onclick = function() {
    // 如果日期选择框存在就隐藏

    // 如果科目下拉框存在就隐藏
    var secheElement = $(".New-ChangeNormal_Bodyitem-on_select").find(".New-ChangeNormal_Bodyitem-Select");
    if ($(secheElement).css("display") == "block") {
      $(secheElement).css("display", "none");
      $(".New-ChangeNormal_Bodyitem-on_select").find(".New-ChangeNormal_Bodyitem-onKey").html("arrow_drop_down");
    }
  };

  // 项目科目下拉框
  document.getElementsByClassName("New-ChangeNormal_Bodyitem-on_select")[0].onclick = function(e) {
    e.stopPropagation();
    e.preventDefault();
    if (this.querySelector(".New-ChangeNormal_Bodyitem-onKey").innerHTML === "arrow_drop_down") {
      this.querySelector(".New-ChangeNormal_Bodyitem-onKey").innerHTML = "arrow_drop_up";
      this.querySelector(".New-ChangeNormal_Bodyitem-Select").style.display = "block";
    } else {
      this.querySelector(".New-ChangeNormal_Bodyitem-onKey").innerHTML = "arrow_drop_down";
      this.querySelector(".New-ChangeNormal_Bodyitem-Select").style.display = "none";
    }
  }

  var selectItems = document.getElementsByClassName("New-ChangeNormal_Bodyitem-SelectItem");
  for (var i = 0; i < selectItems.length; i++) {
    selectItems[i].onclick = function(e) {
      e.stopPropagation();
      var expenItem = this.querySelector("span").innerHTML;
      var expenId = this.querySelector("span").getAttribute("expenid");
      var parentElement = this.closest(".New-ChangeNormal_Bodyitem-Box");
      parentElement.querySelector(".New-ChangeNormal_Bodyitem-Input").value = expenItem;
      parentElement.querySelector(".New-ChangeNormal_Bodyitem-onKey").innerHTML = "arrow_drop_down";
      parentElement.querySelector(".New-ChangeNormal_Bodyitem-Select").style.display = "none";

      // 将对应的expenid添加至input中
      parentElement.querySelector(".New-ChangeNormal_Bodyitem-Input").setAttribute("expenid", expenId);
      parentElement.querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .add("New-ChangeNormal_Bodyitem-TitleInput");
    }
  }

  // 初始化文件上传
  var $drawerBatchBox = $(".upfile");
  $drawerBatchBox.each(function() {
    NewProject.initialization(this);
  });
  // 日期选择框的初始化
  addFormElementsEvents();
  // 增加界面背影
  $("#login_toast_box").css({
    "display" : "block",
    "z-index" : 40
  });
  // 如果expenId有值，需要指定显示选中的项目科目
  NewProject.chooseExpenItem();
};
NewProject.chooseExpenItem = function() {
  var expenId = $("#expen_id").val();
  var expenItem = "";
  if (expenId != null && expenId != "") {
    $(".New-ChangeNormal_Bodyitem-SelectItem").each(function(index, element) {
      if ($(element).find("span").attr("expenid") == expenId) {
        expenItem = $(element).find("span").html();
      }
    });
    $(".dev_expen_expenitem").eq(0).val(expenItem);
    $(".dev_expen_expenitem").eq(0).attr("expenid", expenId);
  }
}

// 附件框删除操作
NewProject.closeAccessoy = function(obj) {
  $(obj).parent().remove();
}
// 关闭"记一笔"弹框
NewProject.closeAddexpenBox = function() {
  $("#prj_detail_addexpen_box").remove();
  // 阴影移除
  $("#login_toast_box").css({
    "display" : "none",
    "z-index" : 1111111
  });
  // 如果日期弹窗框还在的话，需要进行删除
  if ($(".datepicker__box").eq(0).css("display") == "block") {
    $(".datepicker__box").eq(0).css("display", "none");
  }
};

NewProject.fileuploadBoxOpenInputClick = function(ev) {
  var $this = $(ev.currentTarget);
  $this.find('input.fileupload__input').click();
  ev.stopPropagation();
  // 阻止事件的向上传播
  return;
};

/**
 * 保存一笔经费情况
 */
NewProject.savePrjExpenditure = function(des3PrjId, obj) {
  // 防重复点击
  BaseUtils.doHitMore(obj, 1000);
  if (NewProject.validPrjExpen()) {
    var expenDate = $(".dev_expen_expendate").eq(0).val(); // 支出日期
    var expenId = $(".dev_expen_expenitem").eq(0).attr("expenid"); // 支出项目科目的id
    var expenAmount = $(".dev_expen_expenamount").eq(0).val(); // 支出项目科目的金额
    var remark = $(".dev_expen_remark").eq(0).val(); // 备注
    var des3fileids = "";
    $(".New-ChangeNormal_Bodyitem-Upload_Detailtext").each(function(index, element) {
      des3fileids += $(element).attr("des3fileid") + ";";
    });
    $.ajax({
      url : "/prjweb/detail/ajaxsaveexpen",
      type : 'post',
      data : {
        "expenRecordId" : $("#expen_record_id").val(),
        "des3PrjId" : des3PrjId,
        "expenDate" : expenDate,
        "expenId" : expenId,
        "expenAmount" : expenAmount,
        "remark" : remark,
        "des3fileids" : des3fileids
      },
      dataType : 'json',
      success : function(data) {
        if (data.msg == "success") {
          // 提示操作成功
          scmpublictoast("操作成功", 1000);
          // 关闭弹框
          NewProject.closeAddexpenBox();
          // 重新加载项目经费列表
          NewProject.loadProjectExpenditure($(".New-proManage_containerBody-headerItem").eq(1), des3PrjId);
        } else {
          // 提示操作失败
          scmpublictoast("操作失败", 1000);
        }
      },
      error : function() {
        // 提示系统正忙
        scmpublictoast("系统正忙!", 1000);
      }
    });
  }
};
// 校验
NewProject.validPrjExpen = function() {
  var expenDate = $(".dev_expen_expendate").eq(0).val(); // 支出日期
  if (expenDate == null || expenDate == "") {
    scmpublictoast("支出日期必须输入！", 1000);
    return false;
  }
  if (expenDate.split("-").length != 3) {
    scmpublictoast("支出日期必须精确至日！", 1000);
    return false;
  }
  // 整数位最高8位，小数点后两位
  var reg = /(^[0-9]{1,8}$)|(^[0-9]{1,8}[\.]{1}[0-9]{1,2}$)/;
  var expenAmount = $(".dev_expen_expenamount").eq(0).val(); // 支出项目科目的金额
  if (!reg.test(expenAmount)) {
    $(".dev_expen_expenamount").eq(0).val("");
    scmpublictoast("支出金额输入不合法！", 1000);
    return false;
  }
  return true;
}
// 报告列表
NewProject.reportList = function(des3PrjId) {
  $reportList = window.Mainlist({
    name : "prjreport",
    listurl : "/prjweb/detail/ajaxprjreportlist",
    listdata : {
      "des3PrjId" : des3PrjId
    },
    method : "scroll",
    listcallback : function(xhr) {
      BaseUtils.ajaxTimeOut(xhr.responseText);
      // 加载上传附件的展示框
      NewProject.loadReportfile();
    },
    more : true,
    statsurl : "/prjweb/detail/ajaxprjreportcount"
  });
};

// 加载上传附件的事件
NewProject.loadReportfile = function() {
  $(".pub_uploadFulltext").each(function() {
    $this = $(this);
    var data = {
      "fileurl" : "/fileweb/fileupload",
      "filedata" : {
        fileDealType : "generalfile"
      },
      "method" : "nostatus",
      "filecallback" : NewProject.reloadfile,
      "filecallbackparam" : {
        reportId : $this.attr("des3Id")
      },
    // "filetype":filetype
    };
    try {
      fileUploadModule.initialization(this, data);
    } catch (e) {
    }
  });
};

// 上传全文，文件上传成功后的回调函数，用于更新成果全文附件信息
NewProject.reloadfile = function(xhr, params) {
  if (typeof (xhr) == "undefined" || !params) {
    return false;
  }
  const data = eval('(' + xhr.responseText + ')');
  var reportId = params.reportId;
  var des3fid = data.successDes3FileIds.split(",")[0];
  $
      .ajax({
        url : '/prjweb/detail/ajaxupdatereportfile',
        type : 'post',
        data : {
          'reportId' : reportId,
          'des3FileId' : des3fid
        },
        dataType : 'json',
        timeout : 10000,
        success : function(data) {
          BaseUtils
              .ajaxTimeOut(
                  data,
                  function() {
                    if (data.result == "true") {
                      // 替换全文图片和下载链接 列表和批量选择框都需要替换
                      var $listIconBox = $(".dev_pub_del_" + reportId + " .pub-idx__full-text_img");
                      var img = "<image class='dev_fulltext_download dev_pub_img' style='cursor:pointer' onerror='this.onerror=null;this.src=\"/resmod/images_v5/images2016/file_img1.jpg\"'";
                      img += "src='/resmod/images_v5/images2016/file_img1.jpg'";
                      if (data.downUrl != null && data.downUrl != "") {
                        img += " onclick='window.location.href=\"" + data.downUrl + "\"';";
                        img += " title='下载全文';";
                      }
                      img += " />";
                      img += "<a ";
                      if (data.downUrl != null && data.downUrl != "") {
                        img += "href=\""
                            + data.downUrl
                            + "\" class='new-tip_container-content' title='下载全文'> <img src='/resmod/smate-pc/img/file_ upload1.png' class='new-tip_container-content_tip1' > <img src='/resmod/smate-pc/img/file_ upload.png' class='new-tip_container-content_tip2'> </a>";
                      } else {
                        img += "></a>"
                      }
                      $listIconBox.html(img);
                      $listIconBox.css({
                        "border" : "",
                        "cursor" : "pointer",
                        "position" : "relative",
                        "display" : "flex",
                        "justify-content" : "center",
                        "align-items" : "center"
                      });
                    } else {
                      scmpublictoast('系统出现异常，请稍后再试', 2000);
                    }
                  });
        }
      });

};

// 初始化上传控件
NewProject.initialization = function(obj) {
  var multiple = "false";
  if ($(obj).attr("fileType") == "file") {
    multiple = "true";
  }
  var data = {
    "fileurl" : "/fileweb/fileupload",
    "filedata" : {
      fileDealType : "generalfile"
    },
    "method" : "nostatus",
    "nonsupportType" : ["js", "jsp", "php"],
    "filecallback" : NewProject.upfileBack,
    // 回调函数
    "filecallbackparam" : {
      "obj" : obj,
      "fileType" : $(obj).attr("fileType"),
    },
    "multiple" : multiple,
    "maxtip" : "0"
  };
  fileUploadModule.initialization(obj, data);
};
// 回调函数
NewProject.upfileBack = function(xhr, params) {
  const data = eval('(' + xhr.responseText + ')');
  if (data.successFiles >= 1) {
    var fileIdArray = data.successDes3FileIds.split(",");
    // 支持多文件上传
    for (var key = 0; key < fileIdArray.length; key++) {
      var des3fid = fileIdArray[key];
      var fileName = data.extendFileInfo[key].fileName;
      var downloadUrl = data.extendFileInfo[key].downloadUrl;
      var obj = params.obj;
      var fileType = params.fileType;
      var item = '<div class="New-ChangeNormal_Bodyitem-Upload_Detailitem">'
          + '<div class="New-ChangeNormal_Bodyitem-Upload_Detailtext"  title=" '+ fileName + ' "  onclick="NewProject.link(\''
          + downloadUrl
          + '\');" des3FileId="'
          + des3fid
          + '">'
          + fileName
          + '</div>'
          + '<i class="material-icons New-ChangeNormal_Bodyitem-Upload_Detailclose" onclick="NewProject.closeAccessoy(this);" >close</i>'
          + '</div>';
      $(obj).parent().find(".New-ChangeNormal_Bodyitem-Upload_Detail").append(item);
    }
  }
  NewProject.resetFileUploadBox(obj);
};

NewProject.link = function(url, e) {
  e = window.event || e;
  location.href = url;
  if (window.event)
    window.event.returnValue = false;
  else
    e.preventDefault();

};
// 重置上传框
NewProject.resetFileUploadBox = function(obj) {
  $(obj).find(".fileupload__core").removeClass("finish_shown").addClass("initial_shown");
  $(obj).find(".fileupload__box").removeClass("upload_finished");
  $(obj).find(".preloader").removeClass("green-style active").children().remove();
  $(obj).find(".fileupload__progress_text").text("");
  $(obj).find(".fileupload__input").val("");
  NewProject.initialization(obj);
};
// 阅读记录
NewProject.ajaxProjectView = function() {
  $.ajax({
    url : "/prjweb/detail/ajaxsaveview",
    type : 'post',
    data : {
      "des3PrjId" : $("#des3PrjId").val()
    },
    dataType : 'json',
    success : function(data) {

    }
  });
}

// 项目成果列表
NewProject.pubList = function(des3PrjId) {
  $reportList = window.Mainlist({
    name : "prjpub",
    listurl : "/prjweb/detail/ajaxprjpublist",
    listdata : {
      "des3PrjId" : des3PrjId
    },
    method : "pagination",
    listcallback : function(xhr) {
    },
    more : true,
    statsurl : "/prjweb/detail/ajaxprjpubcount"
  });
};
// 加载支出记录
NewProject.loadExpenRecord = function(expenId, obj) {
  // 防重复点击
  BaseUtils.doHitMore(obj, 1000);
  // 每次加载重新清空
  $(".New-changeStyle_container").eq(0).find(".New-changeStyle_container-body").html("");
  if (expenId == null || expenId == "") {
    return;
  }
  $.ajax({
    url : "/prjweb/detail/ajaxloadexpenrecord",
    type : 'post',
    data : {
      "des3PrjId" : $("#des3PrjId").val(),
      "expenId" : expenId
    },
    dataType : 'json',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data != null && data.length > 0) {
          var inerBody = $(".New-changeStyle_container").eq(0).find(".New-changeStyle_container-body");
          for (var i = 0; i < data.length; i++) {
            var remark = typeof (data[i].remark) == undefined || data[i].remark == null ? "" : data[i].remark;
            var recordItem = '<div class="New-changeStyle_container-bodyItem">'
                + '<span class="New-changeStyle_content" style="width: 250px;" title=" '+  remark  + ' ">' + remark + '</span>'
                + '<div class="New-changeStyle_container-bodyItem_Time">' + data[i].formatDate + '</div>'
                + '<div class="New-changeStyle_container-bodyItem_Amount">' + data[i].formatAmount + '¥ </div>'
                + '<div class="New-changeStyle_container-bodyItem_func">'
                + '<div class="New-changeStyle_container-bodyItem_edit" onclick="NewProject.editExpenRecord(\''
                + data[i].id + '\',this)"></div>'
                + '<div class="selected-func_delete" onclick="NewProject.deleteExpenRecord(\'' + data[i].id
                + '\',this)" ></div></div></div>';
            $(inerBody).append(recordItem);
          }

          $(".New-changeStyle_container").eq(0).show();
          document.getElementsByClassName("New-changeStyle_container")[0].style.top = (window.innerHeight - document
              .getElementsByClassName("New-changeStyle_container")[0].offsetHeight)
              / 2 + "px";
          document.getElementsByClassName("New-changeStyle_container")[0].style.left = (window.innerWidth - document
              .getElementsByClassName("New-changeStyle_container")[0].offsetWidth)
              / 2 + "px";
          window.onresize = function() {
            document.getElementsByClassName("New-changeStyle_container")[0].style.top = (window.innerHeight - document
                .getElementsByClassName("New-changeStyle_container")[0].offsetHeight)
                / 2 + "px";
            document.getElementsByClassName("New-changeStyle_container")[0].style.left = (window.innerWidth - document
                .getElementsByClassName("New-changeStyle_container")[0].offsetWidth)
                / 2 + "px";
          }
          // 增加界面背影
          $("#login_toast_box").css({
            "display" : "block",
            "z-index" : 40
          });
          // 更改一下项目支出明细的标题，置为对应的项目科目
          var expenItem = $(obj).parent().find(".New-proManage_RecordBody-ItemTitle span").html();
          $(".New-changeStyle_container").eq(0).find(".New-changeStyle_container-header span").html(expenItem);
        } else {
          scmpublictoast('暂无支出记录，可以通过记一笔进行记录！', 1000);
        }
      });
    },
    error : function() {
      scmpublictoast('系统出现异常，请稍后再试', 2000);
      $(".New-changeStyle_container").eq(0).hide();
    }
  });
};
// 删除支出记录
NewProject.deleteExpenRecord = function(expenRecordId, obj) {
  // 防重复点击
  BaseUtils.doHitMore(obj, 1000);
  if (expenRecordId == null || expenRecordId == "") {
    return;
  }
  $.ajax({
    url : "/prjweb/detail/ajaxdeleteexpenrecord",
    type : 'post',
    data : {
      "expenRecordId" : expenRecordId
    },
    dataType : 'json',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.msg == "success") {
          scmpublictoast('删除成功！', 1000);
          // 关闭支出记录框
          NewProject.closeExpenRecordDetail();
          // 重新加载项目经费列表
          NewProject.loadProjectExpenditure($(".New-proManage_containerBody-headerItem").eq(1), $("#des3PrjId").val());
        } else if (data.msg == "no_record") {
          scmpublictoast('记录已删除！', 1000);
        } else {
          scmpublictoast('删除失败！', 1000);
        }
      });
    },
    error : function() {
      scmpublictoast('系统出现异常，请稍后再试', 2000);
    }
  });
};
// 关闭支出记录框
NewProject.closeExpenRecordDetail = function(obj) {
  // 防重复点击
  BaseUtils.doHitMore(obj, 1000);
  // 隐藏框
  $(".New-changeStyle_container").eq(0).hide();
  // 阴影移除
  $("#login_toast_box").css({
    "display" : "none",
    "z-index" : 1111111
  });
};

// 编辑支出记录
NewProject.editExpenRecord = function(expenRecordId, obj) {
  // 防重复点击
  BaseUtils.doHitMore(obj, 1000);
  if (expenRecordId == null || expenRecordId == "") {
    return;
  }
  $.ajax({
    url : "/prjweb/detail/ajaxexpenadd",
    type : 'post',
    data : {
      "des3PrjId" : $("#des3PrjId").val(),
      "expenRecordId" : expenRecordId
    },
    dataType : 'html',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        $("#project_content_container").parent().append(data);
        NewProject.initAddExpenBox();
        // 关闭经费支出明细框
        NewProject.closeExpenRecordDetail();
        // 增加界面背影
        $("#login_toast_box").css({
          "display" : "block",
          "z-index" : 40
        });
      });
    }
  });
};

// 项目成果认领
NewProject.pubConfirm = function(des3PrjId) {
  $.ajax({
    url : "/prjweb/detail/ajaxprjpubconfirm",
    type : "post",
    dataType : "html",
    data : {
      "des3PrjId" : des3PrjId
    },
    success : function(data) {
      $(".dev_pub_confirm").html("");
      $(".dev_pub_confirm").append(data);
    }
  });
};

// 项目成果认领 查看全部
NewProject.toConfirmList = function(des3PrjId) {
  $('#dev_pubconfirm_isall').val("all");
  $('.dev_confirmList').show();
  showDialog("dev_lookall_pubconfirm_back");
  $reportList = window.Mainlist({
    name : "prjpubconfirm",
    listurl : "/prjweb/detail/ajaxprjpubconfirm",
    listdata : {
      "isAll" : "1",
      "des3PrjId" : des3PrjId
    },
    method : "scroll",
    listcallback : function(xhr) {
    }
  });
};

NewProject.pubConfirmOpt = function(obj, des3PrjId, pubId, confirmResult) {
  BaseUtils.doHitMore(obj, 2000);

  if ($('#dev_pubconfirm_isall').val() == "all") {
    $("#dev_" + pubId).hide();
    var $parentNode = $("#dev_" + pubId).parent();
    var totalcount = $parentNode.attr("total-count");
    $parentNode.attr("total-count", totalcount - 1);
    if (totalcount - 1 == 0) {
      $parentNode.append("<div class='response_no-result'>未找到符合条件的记录</div>")
    }
  }
  $.ajax({
    url : "/prjweb/detail/ajaxprjpubconfirmopt",
    type : "post",
    dataType : "json",
    data : {
      "pubId" : pubId,
      "des3PrjId" : des3PrjId,
      "confirmResult" : confirmResult
    },
    success : function(data) {
      if (data.result == 'success') {
        if ($('#dev_pubconfirm_isall').val() == "one") {
          var des3PrjId = $("#des3PrjId").val();
          NewProject.pubConfirm(des3PrjId);
        }
      }
    }
  });
}

// 成果认领-关闭查看全部弹出层
NewProject.closePubConfirmBack = function() {
  $('#dev_pubconfirm_isall').val("one");
  hideDialog("dev_lookall_pubconfirm_back");
  $.ajax({
    url : "/psnweb/timeout/ajaxtest",
    type : "post",
    dataType : "json",
    data : {},
    success : function(data) {
      if (data.ajaxSessionTimeOut != "yes") {
        NewProject.pubConfirm($("#des3PrjId").val());
        NewProject.pubList($("#des3PrjId").val());
      }
    }
  });
};

//改变url
NewProject.changeUrl = function(targetModule) {
  var json = {};
  var oldUrl = window.location.href;
  var index = oldUrl.lastIndexOf("module");
  var newUrl = window.location.href;
  if (targetModule != undefined && targetModule != "") {
    if (index < 0) {
      if(oldUrl.lastIndexOf("?")>0){
        newUrl = oldUrl + "&module=" + targetModule;
      }else{
        newUrl = oldUrl + "?module=" + targetModule;
      }
    } else {
      newUrl = oldUrl.substring(0, index) + "module=" + targetModule;
    }
  }
  window.history.replaceState(json, "", newUrl);
}