var Grp = Grp ? Grp : {};
var GrpFile = GrpFile ? GrpFile : {};

// 群组动态 阻止冒泡事件
;
GrpFile.stopPropagation = function(e) {
  if (e && e.stopPropagation) {// 非IE
    e.stopPropagation();
  } else {// IE
    window.event.cancelBubble = true;
  }
}

/**
 * n 群组文件列表
 */
;
var $grpFile;
// zzx-判断是否是IE浏览器
Grp.IEVersion = function() {
  var userAgent = navigator.userAgent; // 取得浏览器的userAgent字符串
  var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; // 判断是否IE<11浏览器
  var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; // 判断是否IE的Edge浏览器
  var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
  if (isIE) {
    var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
    reIE.test(userAgent);
    var fIEVersion = parseFloat(RegExp["$1"]);
    if (fIEVersion == 7) {
      return 7;
    } else if (fIEVersion == 8) {
      return 8;
    } else if (fIEVersion == 9) {
      return 9;
    } else if (fIEVersion == 10) {
      return 10;
    } else {
      return 6;// IE版本<=7
    }
  } else if (isEdge) {
    return 'edge';// edge
  } else if (isIE11) {
    return 11; // IE11
  } else {
    return -1;// 不是ie浏览器
  }
}
// zzx-兼容IE Input元素的默认提示样式
Grp.loadLabelInput = function($inputobj, myfunction) {
  if (Grp.IEVersion() == -1) {
    $inputobj.keydown(function(ev) {
      var $this = $(this);
      if (ev.keyCode == 13 && typeof myfunction == "function") {
        myfunction(ev, $this);
      }
    });
    return;
  }
  // 以下是为了兼容IE的提示样式
  var placeholder = $inputobj.attr("placeholder");
  $inputobj
      .blur(
          function() {
            $inputobj.closest(".setup-keyword__box-input")[0].querySelector(".setup-keyword__input-tip1").style.display = "block";
          }).focus(function() {// 获得光标
        var text = $.trim($inputobj.val());
        if (text == "") {
          $inputobj.css("color", "#999");
          $inputobj.val(placeholder);
          $inputobj[0].setSelectionRange(0, 0);
        }
      }).mouseup(function() {
        var color = $inputobj.css("color");
        if (color == "rgb(153, 153, 153)") {
          $inputobj.focus();
          $inputobj[0].setSelectionRange(0, 0);
        }
      }).keyup(function() {
        var text = $.trim($inputobj.val());
        if (text == "") {
          $inputobj.val(placeholder);
          $inputobj.css("color", "#999");
          $inputobj[0].setSelectionRange(0, 0);
        }
      }).off("keydown").keydown(function(ev) {
        var $this = $(this);
        var color = $inputobj.css("color");
        if (color == "rgb(153, 153, 153)") {
          $inputobj.val("");
          $inputobj.css("color", "#333");
        }
        if (ev.keyCode == 13 && typeof myfunction == "function" && color != "rgb(153, 153, 153)") {
          myfunction(ev, $this);
        }
      });

}
Grp.listenerAddGrpLabel = function(event, obj) {
  if (event.keyCode == 13) {
    $(obj).closest(".setup-keyword__box-input").find("i").click();
    $(obj).closest(".setup-keyword__box-input").querySelector(".setup-keyword__input-tip1").style.display = "block";
    $(obj).closest(".setup-keyword__box-input").querySelector(".setup-keyword__input-tip2").style.display = "none";
  }
};
Grp.showGrpFileList = function(url) {
  if (url == undefined) {
    url = "/groupweb/grpfile/ajaxgrpfilelist";
  }
  $("#grp_file_list").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });

  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'searchDes3GrpFileMemberId' : $("#memberSelectId").attr("des3MemberId")
  };

  $grpFile = window.Mainlist({
    name : "grpfilelist",
    listurl : url,
    listdata : dataJson,
    listcallback : function(xhr) {
      addFormElementsEvents();
      Grp.checkSelectLabel();// 置灰标签
    },
    drawermethodsmaskback : function() {
      $("div[list-drawer='grpfilelist']").find(".kw-chip_container").css("cursor", "default");
    },
    drawermethods : locale == "zh_CN" ? {
      "删除文件" : function(a) {
        if (a.length > 0) {
          Grp.delGrpFiles(a.join(","));
        }
      },
      "加至个人文件" : function(a) {
        if (a.length > 0) {
          Grp.collectionGrpFiles(a.join(","));
        }
      },
      "下载文件" : function(a) {
        if (a.length > 0) {
          Grp.batchDownloadGrpFiles(a.join(","));
        }
      }
    } : {
      "Delete" : function(a) {
        if (a.length > 0) {
          Grp.delGrpFiles(a.join(","));
        }
      },
      "Add to My files" : function(a) {
        if (a.length > 0) {
          Grp.collectionGrpFiles(a.join(","));
        }
      },
      "Download" : function(a) {
        if (a.length > 0) {
          Grp.batchDownloadGrpFiles(a.join(","));
        }
      }
    }
  });

};

// 批量下载群组文件
Grp.batchDownloadGrpFiles = function(fileIds) {
  $.ajax({
    url : '/fileweb/batchdownload/ajaxgeturl',
    type : 'post',
    dataType : 'json',
    data : {
      "type" : 1,
      'des3Ids' : fileIds
    },
    success : function(data) {
      window.location.href = data.data.url;
    },
    error : function() {
    }
  });
}

Grp.collectionGrpFiles = function(fileIds) {
  $.ajax({
    url : '/groupweb/grpfile/ajaxcollectgrpfile',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpFileIdList' : fileIds
    },
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpFile.timeOut, grpFile.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      } else {
        if (data.status == "success") {
          scmpublictoast(grpFile.saveSuccessPre + data.count
              + (data.count > 1 ? grpFile.saveSuccessSuffs : grpFile.saveSuccessSuff), 1000);
        } else if (data.status == "error") {
          scmpublictoast(grpFile.saveFail, 1000);
        }
      }
    },
    error : function() {
    }
  });
}
Grp.delGrpFiles = function(fileIds) {
  $.ajax({
    url : '/groupweb/grpfile/ajaxdelgrpfile',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'des3GrpFileIdList' : fileIds
    },
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpFile.timeOut, grpFile.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      } else {
        if (data.status == "success") {
          scmpublictoast(grpFile.delSuccessPre + data.count
              + (data.count > 1 ? grpFile.delSuccessSuffs : grpFile.delSuccessSuff), 1000);
          $grpFile.reloadCurrentPage();
          $grpFile.drawerRemoveSelected();
        } else if (data.status == "noPermit") {
          scmpublictoast(grpFile.noEligibleDel, 2000);
        } else if (data.status == "error") {
          scmpublictoast(grpFile.delFail, 2000);
        }
      }
    },
    error : function() {
    }
  });
}
/**
 * 刷新群组文件列表
 */
Grp.refreshGrpFileList = function() {
  $("#memberSelectId").attr("des3MemberId", "");
  Grp.showGrpFileList();
};

/**
 * 群组动态文件下载 SCM-14409 hcj
 */
GrpFile.openDynFile = function(des3GrpFileId) {
  var dataJson = {
    "des3Id" : des3GrpFileId,
    "type" : 1
  };
  $.ajax({
    url : '/fileweb/download/ajaxgeturl',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(result) {
      GrpBase.ajaxTimeOut(result, function() {
        if (result.status == "success") {
          window.location.href = result.data.url;
        }
      });

    },
    error : function() {
    }
  });

};

/**
 * 打开群组文件 已废弃，改为新的下载链接。新的下载链接在a标签的href上。SCM-14409 hcj
 */
/*
 * GrpFile.openFile = function(fileId, nodeId, type) { window.location.href = snsctx +
 * "/file/download?des3Id=" + fileId + "&nodeId=" + nodeId + "&type=" + type +"&flag=5"; };
 */

GrpFile.loginFromOutside = function(fileId, nodeId, type) {
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
  }, 1);
}

/**
 * 课程群组，查询文件类型
 */
GrpFile.searchGrpFileType = function(fileType, obj) {
  var check = $(obj).attr("checked");
  if ("checked" == check) {
    if (fileType == 1) {
      $("#grp_file_list").attr("workFile", 1);
    } else {
      $("#grp_file_list").attr("courseFile", 2);
    }

  } else {
    if (fileType == 1) {
      $("#grp_file_list").attr("workFile", "");
    } else {
      $("#grp_file_list").attr("courseFile", "");
    }
  }
  Grp.showGrpFileList();

}
/**
 * 搜索文件
 */
;
GrpFile.searchGrpFileKey = function(obj) {

  Grp.showGrpFileList();

};
Grp.showGrpMemberFileInfoList = function() {
  $("#grp_member_file_info_list").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  var grpFileType = $("#main-list__header_id").find(".main-list-header__title .filter-list__section .option_selected")
      .attr("filter-value");
  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'searchGrpFileMemberName' : $("#searchGrpFileMemberNameId").val(),
    'grpFileType' : grpFileType
  };
  $.ajax({
    url : '/groupweb/grpfile/ajaxgrpfilememberlist',
    type : 'post',
    dataType : 'html',
    data : dataJson,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_member_file_info_list").html(data);
        if ($("#grp_member_file_info_list").find(".friend-selection__item-2").length == 0) {
          $("#grp_member_file_info_list").addClass("main-list__list")
          $("#grp_member_file_info_list").html("<div class='response_no-result'>" + grpFile.noRecord + "</div>");
        } else {
          $("#grp_member_file_info_list").removeClass("main-list__list")
        }
        // 检索成员时，如果选中成员 ，则刷新所有的文件列表--SCM-12760
        if (!$.trim($("#memberSelectId").attr("des3MemberId")) == "") {
          $("#memberSelectId").attr("des3MemberId", "");
          Grp.showGrpFileList();
        }

      });

    },
    error : function() {
    }
  });
};
Grp.refreshGrpMemberFileInfoList = function() {
  $("#searchGrpFileMemberNameId").val("");
  Grp.showGrpMemberFileInfoList();
}

// 删除群组文件
;
Grp.deleteGrpFileComfirm = function(des3GrpId, des3GrpFileId, obj) {
  var callbackData = {
    'des3GrpId' : des3GrpId,
    'des3GrpFileId' : des3GrpFileId
  }
  GrpBase.jconfirm(function() {
    // 删除群组文件
    Grp.deleteGrpFile(callbackData);
  }, grpFile.delConfirm);

};
Grp.deleteGrpFile = function(callbackData) {
  var des3GrpId = callbackData.des3GrpId;
  var des3GrpFileId = callbackData.des3GrpFileId
  $.ajax({
    url : '/groupweb/grpfile/ajaxdelgrpfile',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : des3GrpId,
      'des3GrpFileId' : des3GrpFileId
    },
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpFile.timeOut, grpFile.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }

      if (data.status == "success") {
        scmpublictoast(grpFile.delSuccess, 1000);
        // Grp.showGrpFileList();
        $grpFile.drawerRemoveSelected(data.delDes3FileIds);
        $grpFile.reloadCurrentPage();
        Grp.showGrpMemberFileInfoList();
      } else if (data.status == "noPermit") {
        scmpublictoast(grpFile.noEligibleDel, 2000);
      } else if (data.status == "error") {
        scmpublictoast(grpFile.opreateFail, 2000);
      }
    },
    error : function() {
    }
  });
};

// 编辑文件
;
Grp.editGrpFile = function(des3GrpId, des3GrpFileId, obj) {
  $.ajax({
    url : '/groupweb/grpfile/ajaxcheckmygrpfile',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : des3GrpId,
      'des3GrpFileId' : des3GrpFileId
    },
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpFile.timeOut, grpFile.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }
      if (data.status == "success") {
        var desc = $(obj).closest(".file-idx__main_box").find(".multipleline-ellipsis__content-box").html();
        $("#grp_file_edit_file_content").val(desc);
        Grp.showEditGrpFile(des3GrpId, des3GrpFileId);

      } else if (data.status == "noPermit") {
        scmpublictoast(grpFile.noEligibleEdit, 2000);
      } else if (data.status == "error") {
        scmpublictoast(grpFile.opreateFail, 2000);
      }
    },
    error : function() {
    }
  });
};

/**
 * 显示编辑框
 */
;
Grp.showEditGrpFile = function(des3GrpId, des3GrpFileId) {
  addFormElementsEvents();
  showDialog("grp_file_eidt_file_dialog");
  // 中英文翻译
  $("#grp_file_edit_file_id").find(".dialogs__header_title").html(grpFile.fileDesc);
  $("#grp_file_edit_file_id").find(".input__title").html(grpFile.briefDesc);
  $("#grp_file_edit_file_id").find(".button_main:first").html(grpFile.confirm);
  $("#grp_file_edit_file_id").find(".button_main:last").html(grpFile.cancel);
  $("#grp_file_edit_file_id").attr("des3_grp_id", des3GrpId);
  $("#grp_file_edit_file_id").attr("des3_grp_file_id", des3GrpFileId);
  $("body").css("overflow", "hidden");

}
/**
 * 隐藏编辑框
 */
;
Grp.hiddenEditGrpFile = function() {
  hideDialog("grp_file_eidt_file_dialog");
  $("body").css("overflow", "");
  $("#grp_file_edit_file_content").val("");
}
/**
 * 保存编辑
 */
;
Grp.saveEditGrpFile = function() {

  $.ajax({
    url : '/groupweb/grpfile/ajaxeditmygrpfile',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : $("#grp_file_edit_file_id").attr("des3_grp_id"),
      'des3GrpFileId' : $("#grp_file_edit_file_id").attr("des3_grp_file_id"),
      'grpFileDesc' : $("#grp_file_edit_file_content").val()

    },
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpFile.timeOut, grpFile.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }

      if (data.status == "success") {
        scmpublictoast(grpFile.opreateSuccess, 1000);
        Grp.hiddenEditGrpFile();
        $grpFile.reloadCurrentPage();
        // Grp.showGrpFileList();
      } else if (data.status == "noPermit") {
        scmpublictoast(grpFile.noEligibleOpt, 2000);
      } else if (data.status == "error") {
        scmpublictoast(grpFile.opreateFail, 2000);
      }
    },
    error : function() {
    }
  });
}
// 组员文件列表
;
Grp.selectMemberFile = function(des3MemberId, obj) {

  var check = $(obj).attr("check")
  if (check != undefined && check == "check") {// 第二次点击
    $(obj).attr("check", "");
    $(obj).css("background-color", "");
    $("#memberSelectId").attr("des3MemberId", "");
    $grpFile.listdata.searchDes3GrpFileMemberId = "";
  } else { // 第一次点击
    // 重置其他已经点击的
    $(".friend-selection__item-2").each(function() {
      $(this).attr("check", "");
      $(this).css("background-color", "");
    });
    $(obj).attr("check", "check");
    $(obj).css("background-color", "#eee");
    $("#memberSelectId").attr("des3MemberId", des3MemberId);
    $grpFile.listdata.searchDes3GrpFileMemberId = des3MemberId;
  }
  $grpFile.resetAllFilterOptions();
};

/**
 * 群组动态收藏文件
 */
Grp.grpDyncollectionGrpFile = function(obj) {
  var fileData = $(obj).closest(".dynamic__box").find("input[name='dynId']");
  if (fileData != null) {
    var resId = fileData.attr("resid");
    Grp.checkGrpFile(obj, resId);
  }

}

/**
 * 检查群组文件是否被删除
 */
Grp.checkGrpFile = function(obj, des3ResId) {
  $.ajax({
    url : '/groupweb/grpfile/ajaxCheckGrpFile',
    type : 'post',
    data : {
      "des3GrpFileId" : des3ResId
    },
    dataType : "json",
    success : function(data) {
      if (data.status == "isDel") {
        scmpublictoast(grpFile.grpFileIsDeleted, 2000);
      } else {
        Grp.collectionGrpFile(des3ResId, obj);
      }
    }
  });
}

// gai
;
Grp.collectionGrpFile = function(des3GrpFileId, obj) {
  GrpBase.doHitMore(obj, 2000);
  var dataJson = {
    'des3GrpFileId' : des3GrpFileId
  };
  $.ajax({
    url : '/groupweb/grpfile/ajaxcollectgrpfile',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpFile.timeOut, grpFile.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }

      if (data.status == "success") {
        scmpublictoast(grpFile.addSuccess, 1000);
      } else if (data.status == "error") {
        scmpublictoast(grpFile.addFail, 1000);
      }
    },
    error : function() {
    }
  });

}

// 显示添加文件框
;
Grp.addGrpFile = function(fileType) {

  $("#grpFileMyFileListId").attr("fileType", fileType);
  showDialog("select_import_grp_file_method_dialog");
  $("body").css("overflow", "hidden");
  if (fileType === 1) {
    // 添加作业
    $("#select_import_grp_file_method").find(".dialogs__childbox_footer > .dialogs__childbox_footer-tip").html(
        "check_box_outline_blank");
  } else {
    $("#select_import_grp_file_method").find(".dialogs__childbox_footer > .dialogs__childbox_footer-tip").html(
        "check_box");
  }

}
// 关闭弹出框
;
Grp.closeSelectGrpFileMethod = function() {
  $("#main-list__header_id").attr("fileType", "");
  hideDialog("select_import_grp_file_method_dialog");
  $("body").css("overflow", "");
}
// 本地上传文件弹出框
;

/**
 * 群组上传文件弹出框
 */
GrpFile.SelectFileUpload = function() {
  addFormElementsEvents();
  showDialog("grp_file_upload_file");
  $("body").css("overflow", "hidden");
  GrpFile.initSelectFileUpload();
};
GrpFile.hideSelectFileUpload = function() {
  hideDialog("grp_file_upload_file");
  $("body").css("overflow", "");
  $("#grp_file_upload_file_content").val("");
};

/**
 * 点击按钮上传文件
 */
GrpFile.uploadFileButton = function(obj) {

  // 重复点击
  BaseUtils.doHitMore(obj, 1000);
  // 超时拦截，超时之后不允许文件继续的上传
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
    // 点击上传文件
    var fileSelect = $("#fileupload").find(".file_selected");
    if (fileSelect == undefined || fileSelect.length == 0) {
      scmpublictoast(grpFile.chooseFileUpld, 2000);
      return;
    }

    var fileDoxDom = $("#fileupload").find(".fileupload__box").get(0);
    var data = {
      "fileDesc" : $("#grp_file_upload_file_content").val()
    }
    fileUploadModule.uploadFile(fileDoxDom, data);

    /*
     * if (document .getElementsByClassName("form__sxn_row-list").length > 0) { document
     * .getElementsByClassName("form__sxn_row-list")[0].innerHTML = ""; }
     */
  }, 1);
}

GrpFile.initSelectFileUpload = function() {
  // 上传文件重置
  var fileDoxDom = $("#fileupload").find(".fileupload__box").get(0);
  fileUploadModule.resetFileUploadBox(fileDoxDom);

  Grp.closeSelectGrpFileMethod();
  var fileType = $("#grpFileMyFileListId").attr("fileType");
  var navAction = "groupFiles";
  if (fileType == "1") {
    navAction = "groupWorks";
  } else if (fileType == "2") {
    navAction = "groupCourses";
  }

  var isNeedSyncToMyFile = '';
  var dataJson = {
    "fileDealType" : "generalfile"
  };
  // var filetype =
  // ".txt,.pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx,.rar,.zip,.jpg,.png,.gif,.jpeg,.htm,.html,.xhtml".split(",");
  var data = {
    "fileurl" : "/fileweb/fileupload",
    "filedata" : dataJson,
    "filecallback" : GrpFile.uploadFileCallBack,
    "method" : "dropadd",
    "multiple" : "true",
    "maxlength" : "10",
    "maxtip" : "1"
  }
  fileUploadModule.initialization(document.getElementById("fileupload"), data);
};
/**
 * 上传文件回调函数
 */
GrpFile.uploadFileCallBack = function(xhr) {
  const data = eval('(' + xhr.responseText + ')');
  GrpBase.ajaxTimeOut(data, function() {
    if (data.successFiles >= 1) {
      var fileIdArray = data.successDes3FileIds.split(",");
      // 支持多文件上传
      for (var key = 0; key < fileIdArray.length; key++) {
        var isNeedSendGroupEmail = 0;// 是否需要发送群组邮件通知，默认不需要，如果用户选址了需要发送的复选框，则该值为1
        if ($("#select_import_grp_file_method").find(".dialogs__childbox_footer > .dialogs__childbox_footer-tip")
            .html() === "check_box") {
          isNeedSendGroupEmail = 1;
        }
        var returnData = {
          'grpFileDesc' : $("#grp_file_upload_file_content").val(),
          'des3ArchiveFileid' : fileIdArray[key],
          'grpFileType' : $("#grpFileMyFileListId").attr("fileType"),
          'isNeedSendGroupEmail' : isNeedSendGroupEmail
        }
        var isLast = "false";
        if (key >= fileIdArray.length - 1) {
          isLast = "true";
        }
        GrpFile.saveUploadGrpFile(returnData, isLast);
      }
      GrpFile.hideSelectFileUpload();
      Grp.refreshGrpFileList();
      Grp.refreshGrpMemberFileInfoList();
      $("#grp_file_upload_file_content").val("");
    } else {
      scmpublictoast(data.failFileNames, 2000);
    }
  })

};

// 本地上传文件弹出框 取消
;
GrpFile.cancleUploadFile = function() {
  GrpFile.hideSelectFileUpload();
  document.getElementsByClassName("form__sxn_row-list")[0].innerHTML = "";
}

/**
 * 保存上传的群组文件
 */
;
GrpFile.saveUploadGrpFile = function(returnData, isLast) {

  var des3GrpLabelIds = "";
  $("#fileupload").find(".kw__box  .kw-chip_container").each(function() {
    des3GrpLabelIds = des3GrpLabelIds + $(this).attr("des3grplabelid") + ",";
  });
  var dataJson = {
    'des3GrpId' : $("#grp_file_main_id").attr("des3GrpId"),
    'des3GrpLabelIds' : des3GrpLabelIds,
  };
  $.extend(dataJson, returnData);
  $.ajax({
    url : '/groupweb/grpfile/ajaxSaveUploadGrpFile',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    async : false,// 多文件上传同步方式
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpFile.timeOut, grpFile.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }
      /*
       * if (data.status == "success" ) { scmpublictoast(grpFile.uploadSuccess, 1000);
       * GrpFile.hideSelectFileUpload(); Grp.refreshGrpFileList();
       * Grp.refreshGrpMemberFileInfoList(); } else if (data.status == "error") {
       * scmpublictoast(grpFile.uploadFail, 2000); }
       */
    },
    error : function() {
    }
  });

}

// 我的文件库导入
;
Grp.SelectMyFileImport = function() {

  // document.getElementsByClassName("dev_background-cover")[0].style.display="flex";

  Grp.closeSelectGrpFileMethod();
  showDialog("select_my_file_import_dialog");
  GrpFile.showMyFileList();
  $("body").css("overflow", "hidden");
  // GrpFile.hiddenSearchMyFile();
}

// 关闭我的文件库导入
;
GrpFile.closeMyFileImport = function() {
  hideDialog("select_my_file_import_dialog");
  // $("#grpFileMyFileListId").empty();//SCM-12762
  $("#grpFileMyFileListId").find(".main-list__item").remove();
  document.getElementById("grp_file_search_my_file_key").value = "";
  $("body").css("overflow", "");
  $(".drawer-batch__box").attr("list-drawer", "grpfilelist");
  $grpFile.resetAllFilterOptions();
}
// 重新刷新批量处理框
GrpFile.refreshDrawerSelected = function() {
  $grpFile.reloadCurrentPage();
  $grpFile.initializeDrawer();
}

// 我的文件库
;
GrpFile.showMyFileList = function() {
  $("#grpFileMyFileListId").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });

  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    "source" : "isPFBox"
  };

  GrpFile.myfileMainlist = window.Mainlist({
    name : "myfilelist",
    listurl : "/psnweb/myfile/ajaxmyfilelist",
    listdata : dataJson,
    method : "scroll",
    listcallback : function(xhr) {
    },
  });

}

// 导入我的文件到群组里
;
GrpFile.importMyFileListToGrp = function(obj) {
  Grp.test();
  if (obj != null) {
    GrpBase.doHitMore(obj, 2000);
  }
  var stationFileIdStr = "";
  $("#grpFileMyFileListId").find(".main-list__item").each(function(i) {
    var obj = $(this).find(".main-list__item_checkbox > .input-custom-style > input");
    if (obj[0].checked == true && obj[0].disabled != true) {
      stationFileIdStr += ($(this).attr("stationFileId") + ",");
    }

  })
  if ($.trim(stationFileIdStr) == "") {
    scmpublictoast(grpFile.selFiles, 2000);
    return;
  }
  var isNeedSendGroupEmail = 0;// 是否需要发送群组邮件通知，默认不需要，如果用户选址了需要发送的复选框，则该值为1
  if ($("#select_import_grp_file_method").find(".dialogs__childbox_footer > .dialogs__childbox_footer-tip").html() === "check_box") {
    isNeedSendGroupEmail = 1;
  }

  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'grpCategory' : $("#main-list__header_id").attr("grpCategory"),
    'grpFileType' : $("#grpFileMyFileListId").attr("fileType"),
    'stationFileIdStr' : stationFileIdStr,
    'isNeedSendGroupEmail' : isNeedSendGroupEmail
  };
  $.ajax({
    url : '/groupweb/grpfile/ajaxgrpaddmyfile',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpFile.timeOut, grpFile.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }

      if (data.status == "success") {
        scmpublictoast(grpFile.addSuccess, 1000);
        GrpFile.closeMyFileImport();
        Grp.refreshGrpFileList();
        Grp.refreshGrpMemberFileInfoList();

      } else if (data.status == "noPermit") {
        scmpublictoast(grpFile.noEligibleaddFile, 2000);
      } else {
        scmpublictoast(grpFile.addFail, 2000);
      }

    },
    error : function() {
    }
  });

}

// 标记文件类型
;
GrpFile.flagGrpFileType = function(fileType, des3GrpFileId, obj) {

  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'des3GrpFileId' : des3GrpFileId,
    'grpFileType' : fileType,
  };

  $.ajax({
    url : '/groupweb/grpfile/ajaxflaggrpfiletype',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpFile.timeOut, grpFile.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }

      if (data.status == "success") {
        $grpFile.drawerRemoveSelected(des3GrpFileId);
        $grpFile.reloadCurrentPage();
        Grp.showGrpMemberFileInfoList();
        /*
         * if ("1" == fileType) { $(obj).html(grpFile.markCourseware);
         * $(obj).closest(".file-idx__main_box").find(".file-idx__icon_courseware").remove();
         * $(obj).attr( "onclick", "nclick=GrpFile.flagGrpFileType(2 ,' " + des3GrpFileId + "'
         * ,this)"); $(obj).closest(".file-idx__main_box").find(".file-idx__main >
         * .file-idx__main_title > .file-idx__main_icon").first().html(""); } else {
         * $(obj).html(grpFile.markAssign);
         * $(obj).closest(".file-idx__main_box").find(".file-idx__icon_courseware").remove();
         * $(obj).closest(".file-idx__main_box").find(".file-idx__main_title").append("<span
         * class='file-idx__icon_courseware'></span>"); $(obj).attr( "onclick",
         * "nclick=GrpFile.flagGrpFileType(1 ,' " + des3GrpFileId + "' ,this)");
         * $(obj).closest(".file-idx__main_box").find(".file-idx__main > .file-idx__main_title >
         * .file-idx__main_icon").first().html("local_library"); }
         */
        scmpublictoast(grpFile.markSuccess, 1000);

      } else if (data.status == "noPermit") {
        scmpublictoast(grpFile.noEligiblemarkFile, 2000);
      } else if (data.status == "error") {
        scmpublictoast(grpFile.markFail, 2000);
      }

    },
    error : function() {
    }
  });

}

/**
 * 显示
 */
;
GrpFile.showSearchGrpMemberFile = function() {

  $("#grp_member_upload_file_search_id").css("display", "");
  $("#grp_member_upload_file_id").hide();
}
/**
 * 隐藏
 */
;
GrpFile.hiddenSearchGrpMemberFile = function() {
  $("#grp_member_upload_file_id").show();
  $("#grp_member_upload_file_id").css("display", "");
  $("#grp_member_upload_file_search_id").css("display", "none");
  $("#searchGrpFileMemberNameId").val("");
  Grp.showGrpMemberFileInfoList();
}
/**
 * 清空
 */
;
GrpFile.emptySearchGrpMemberFile = function() {
  $("#searchGrpFileMemberNameId").val("");
  Grp.showGrpMemberFileInfoList();
  // $("#memberSelectId").attr("des3MemberId","") ;
  // Grp.showGrpFileList();
}

;
Grp.test = function() {
  return 0;
};

// 分享群组文件
Grp.shareGrpFiles = function(obj) {

  var fileIds = "";
  $("#share_to_scm_box").find(".dev_dialogs_share_file_module .share-attachmemts__list .share-attachments__item").each(
      function() {
        fileIds += $(this).attr("des3ResId") + ",";
      });
  var textConent = $.trim($("#id_sharegrp_textarea").val());
  var des3psnIds = "";
  var receiverEmails = "";
  $("#grp_friends").find(".chip__text").each(function(i, n) {
    code = $(n).closest(".chip__box").attr("code");
    if (code != null && $.trim(code) != "") {
      des3psnIds += code + ",";
    }
    // 邮件
    if (SmateShare.isEmail2($(this).html())) {
      receiverEmails += $(this).html() + ",";
    }
  });
  var fileNames = "";
  $(".share-attachmemts__list").find("a").each(function(i, n) {
    fileNames += $(n).text() + ",";
  });
  fileNames = fileNames.substring(0, fileNames.length - 1);

  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'des3ReceiverIds' : des3psnIds,
    'receiverEmails' : receiverEmails,
    'des3GrpFileIdList' : fileIds,
    'textContent' : textConent,
    'fileNames' : fileNames
  };
  $.ajax({
    url : '/groupweb/grpfile/ajaxshareGrpfiles',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(grpFile.shareSuccess, 1000);
        } else if (data.result == "error") {
          scmpublictoast(grpFile.shareFail, 1000);
        }
      });

    },
    error : function() {
    }
  });

};
;

/**
 * 群组标签，开始处start**********************************************************************
 */
Grp.showFileLabelList = function() {
  var fileModuleType = 0; // 文件所属模块[0: 群组文件;1: 作业;2: 教学课件]
  var module = $("#grp_params").attr("module");
  if (module === "curware") {
    fileModuleType = 2;
  } else if (module === "work") {
    fileModuleType = 1;
  }
  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'fileModuleType' : fileModuleType,
  }
  $.ajax({
    url : '/groupweb/grplabel/ajaxgetallfilelabel',
    type : 'post',
    dataType : 'html',
    data : dataJson,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_file_label_box").find(".setup-keyword__box > .kw__box").html(data);
      });
    },
    error : function() {
    }
  });
};

/**
 * 检查字符串长度
 */
Grp.checkStrLen = function(str) {
  var strLen = 0;
  if (str === "") {
    return strLen;
  }
  for (var i = 0; i < str.length; i++) {
    if (str.charCodeAt(i) > 255) {// 如果是汉字，则字符串长度加2
      strLen += 2;
    } else {
      strLen++;
    }
  }
  return strLen;
}

/**
 * 添加群组标签 1=成功 ，2=群组标签存在20个 , 6=已经存在标签， 7=缺少必要参数 8=没权限 ， 9=异常，
 */
Grp.addGrpLabel = function(obj) {
  GrpBase.doHitMore(obj, 1000);
  var labelName = $("#add_grp_label_name").val().trim();
  if (labelName === "") {
    scmpublictoast(grpFile.labelNameNotEmpty, 2000);
    return;
  }
  length = Grp.checkStrLen(labelName);
  if (length > 30) {
    scmpublictoast(grpFile.labelNameToLong, 2000);
    return;
  }
  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'labelName' : labelName,
  }
  $.ajax({
    url : '/groupweb/grplabel/ajaxcreatelabel',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == 1) {
          scmpublictoast(grpFile.addSuccess, 1000);
          var content = Grp.buildLabel(data.des3BaseId, labelName);
          $("#grp_file_label_box").find(".setup-keyword__box > .kw__box").append(content);
          $("#add_grp_label_name").val("");
        } else if (data.result == 2) {
          scmpublictoast(grpFile.addLabelTip1, 2000);
        } else if (data.result == 6) {
          scmpublictoast(grpFile.addLabelTip2, 2000);
        } else if (data.result == 8) {
          scmpublictoast(grpFile.addLabelTip3, 2000);
        } else {
          scmpublictoast(grpFile.addFail, 2000);
        }
        // 定位标签
        $("#add_grp_label_name").focus();

      });
    },
    error : function() {
    }
  });
};

/**
 * 构建群组标签
 */
Grp.buildLabel = function(des3BaseId, labelName) {
  var content = '<div class="kw-chip_container">'
      + '<div class="kw-chip_small"  onclick="Grp.selectGrpLabelFileList(this);" style="display: flex;"  des3GrpLabelid="'
      + des3BaseId + '" >' + ' <span class="kw-chip_small-detail"  style=" cursor: pointer;">' + labelName + '</span>'
      + ' <span class="kw-chip_small-num"></span>' + '</div>'
      + ' <i class="normal-global_icon normal-global_del-icon normal-global_del-icon_show"  title="' + grpFile.delLabel
      + '" onclick="Grp.delGrpLabelConfirm(event,this)"></i>' + '</div>';

  return content;
}
/**
 * 构建文件标签
 */
Grp.buildFileLabel = function(des3filelabelid, des3grpfileid, labelName, des3GrpLabelId) {
  var content = '<div class="kw-chip_container" style="display: flex;cursor: pointer;" onclick="Grp.selectGrpLabelFileList(this);" des3filelabelid="'
      + des3filelabelid
      + '"  '
      + 'des3GrpLabelId="'
      + des3GrpLabelId
      + '"  des3grpfileid="'
      + des3grpfileid
      + '">'
      + '<div class="kw-chip_small">'
      + labelName
      + '</div>'
      + '<div class="kw-chip_container-func"> '
      + '<i class="normal-global_icon normal-global_del-icon"  title="'
      + grpFile.delFileLabel
      + '" onclick="Grp.delGrpFileLabelConfirm(event ,this);"></i>' + '</div>' + '</div>';
  return content;
}

Grp.delGrpLabelConfirm = function(e, obj) {
  GrpFile.stopPropagation(e);
  GrpBase.jconfirm(function() {
    // 删除群组标签
    Grp.delGrpLabel(obj);
  }, grpFile.confirmDelGrpLabel);

}

/**
 * 删除群组标签 1=成功 ， 5 == 标签不存在 ， 7=缺少必要参数 8=没权限 ， 9=异常
 */
Grp.delGrpLabel = function(obj) {
  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'des3LabelId' : $(obj).closest(".kw-chip_container").find(".kw-chip_small").attr("des3GrpLabelId"),
  }
  $.ajax({
    url : '/groupweb/grplabel/ajaxdellabel',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == 1) {
          $(obj).closest(".kw-chip_container").remove();
          scmpublictoast(grpFile.delSuccess, 1000);
        } else if (data.result == 8) {
          scmpublictoast(grpFile.addLabelTip3, 2000);
        } else if (data.result == 5) {
          scmpublictoast(grpFile.addLabelTip4, 2000);
        } else {
          scmpublictoast(grpFile.delFail, 2000);
        }
      });
    },
    error : function() {
    }
  });
};

Grp.delGrpFileLabelConfirm = function(e, obj) {
  GrpFile.stopPropagation(e);
  GrpBase.jconfirm(function() {
    // 删除文件标签
    Grp.delGrpFileLabel(obj);
  }, grpFile.confirmDelFileLabel);

}

/**
 * 删除文件标签 1=成功 , 5 == 标签不存在 , 7=缺少必要参数 8=没权限 ， 9=异常，
 */
Grp.delGrpFileLabel = function(obj) {
  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'des3FileLabelId' : $(obj).closest(".kw-chip_container").attr("des3FileLabelId"),
    'des3GrpFileId' : $(obj).closest(".kw-chip_container").attr("des3GrpFileId"),
  }
  $.ajax({
    url : '/groupweb/grpfilelabel/ajaxdelfilelabel',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == 1) {
          $(obj).closest(".kw-chip_container").remove();
          scmpublictoast(grpFile.delSuccess, 1000);
        } else if (data.result == 8) {
          scmpublictoast(grpFile.addLabelTip3, 2000);
        } else {
          scmpublictoast(grpFile.delFail, 2000);
        }
      });
    },
    error : function() {
    }
  });
};

/**
 * 显示群组标签列表，排除自己的
 */
Grp.showFileLabelListExcludeOwner = function(obj) {
  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'des3GrpFileId' : $(obj).closest(".main-list__item").attr("fileId"),
  }
  $.ajax({
    url : '/groupweb/grplabel/ajaxgetalllabelexcludeowner',
    type : 'post',
    dataType : 'html',
    data : dataJson,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if ($.trim(data) == "") {
          $("#add_grp_label_name").focus();
          scmpublictoast(grpFile.noMoreLabel, 2000);
        } else {
          $(obj).closest(".file-idx__main_box").find(".idx-social__item-show").css("display", "block");
          $(obj).closest(".file-idx__main_box").find(".idx-social__item-show").html(data);
        }
      });
    },
    error : function() {
    }
  });
};

/**
 * 添加文件标签 1=成功 ,3=文件最多关联10个标签 , 5 == 标签不存在 , 7=缺少必要参数 8=没权限 ， 9=异常，
 */
Grp.addFileLabel = function(e, obj) {
  GrpFile.stopPropagation(e);
  GrpBase.doHitMore(obj, 2000);
  var labelName = $(obj).html();
  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
    'des3LabelId' : $(obj).attr("des3LabelId"),
    'des3GrpFileId' : $(obj).closest(".main-list__item").attr("fileId"),
  }
  $.ajax({
    url : '/groupweb/grpfilelabel/ajaxaddfilelabel',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == 1) {
          var content = Grp.buildFileLabel(data.des3filelabelid, $(obj).closest(".main-list__item").attr("fileId"),
              labelName, $(obj).attr("des3LabelId"))
          $(obj).closest(".file-idx__main").find(".kw__box").append(content);
          if ($(obj).closest(".file-idx__main_box").find(".idx-social__item-show_item").length == 1) {
            $(obj).closest(".idx-social__item-show").css("display", "none");
          }
          $(obj).remove();
          scmpublictoast(grpFile.addSuccess, 1000);
        } else if (data.result == 8) {
          scmpublictoast(grpFile.addLabelTip3, 2000);
        } else if (data.result == 3) {
          scmpublictoast(grpFile.addLabelTip5, 2000);
        } else if (data.result == 4) {
          scmpublictoast(grpFile.addLabelTip2, 2000);
        } else {
          scmpublictoast(grpFile.addFail, 2000);
        }
      });
    },
    error : function() {
    }
  });
};

/**
 * 点击群组标签，显示标签对应的文件，支持多选
 */
Grp.selectGrpLabelFileList = function(obj) {

  // 批量处理框，不需要这点击事件
  if ($(obj).closest(".drawer-batch__box").length === 1) {
    return;
  }

  if ($(obj).hasClass("kw-chip_container")) {
    Grp.clickFileLabel(obj);
  } else {
    Grp.clickGrpLabel(obj);
  }
};

/**
 * 群组标签，选择文件
 */
Grp.clickGrpLabel = function(obj) {
  var check = $(obj).attr("check")
  var des3GrpLabelId = $(obj).attr("des3grplabelid")
  if (check != undefined && check == "check") {// 第二次点击
    $(obj).attr("check", "");
    $(obj).css("background-color", "");
  } else { // 第一次点击
    $(obj).attr("check", "check");
    $(obj).css("background-color", "#eee");
  }
  var grpIds = "";
  $(obj).closest(".kw__box").find(".kw-chip_small[check='check']").each(function() {
    grpIds = grpIds + $(this).attr("des3grplabelid") + ","
  });
  if ($(obj).hasClass("kw-chip_container")) {
    // 单击文件里面的标签
    grpIds = des3GrpLabelId;
    if ($(obj).find(".kw-chip_small").attr("check") === "check") {
      // 第二次，(取消点击)
    }
  }
  $grpFile.listdata.des3GrpLabelIds = grpIds;
  $grpFile.resetAllFilterOptions();

};
/**
 * 点击单个文件标签
 */
Grp.clickFileLabel = function(obj) {
  var check = $(obj).find(".kw-chip_small").attr("check");
  var des3GrpLabelId = $(obj).attr("des3grplabelid")
  if (check === "check") {
    // 第二次，(取消点击)
    des3GrpLabelId = "";
  }
  $grpFile.listdata.des3GrpLabelIds = des3GrpLabelId;
  $grpFile.resetAllFilterOptions();

};

/**
 * 显示群组标签，上传文件的
 */
Grp.showFileLabelListForUploadFile = function(obj) {
  var dataJson = {
    'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
  }
  $.ajax({
    url : '/groupweb/grplabel/ajaxgetalllabelforuploadfile',
    type : 'post',
    dataType : 'html',
    data : dataJson,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $(obj).closest(".set__result-label").find(".set__result-label_item").html(data);
        // 排除已经选中的
        $("#fileupload").find(".kw__box .kw-chip_container ").each(function() {
          var des3grplabelid = $(this).attr("des3grplabelid");
          $("#fileupload").find(".set__result-label_item  .set__result-label_item-list").each(function() {
            if ($(this).attr("des3labelid") === des3grplabelid) {
              $(this).remove();
            }
          });
        });
        if ($("#fileupload").find(".set__result-label_item .set__result-label_item-list").length == 0) {
          scmpublictoast(grpFile.noMoreLabel, 2000);
        } else {
          // 存在标签，就显示
          $(obj).closest(".set__result-label").find(".set__result-label_item").css("display", "block");
        }
      });
    },
    error : function() {
    }
  });
};

/**
 * 构建上传文件的标签
 */
Grp.buildUploadFileLabel = function(des3GrpLabelId, labelName) {
  var content = '<div class="kw-chip_container"  des3GrpLabelId="' + des3GrpLabelId + '" >'
      + '<div class="kw-chip_small" style="display: flex;align-items: center;" > ' + '<span>' + labelName + '</span>'
      + '<i class="normal-global_delect-icon" onclick="Grp.removeUploadFileLabel(this);" style="cursor:pointer"></i>'
      + '</div>';
  +'</div>';
  return content;
};

/**
 * 添加标签
 */
Grp.addUploadFileLabel = function(e, obj) {
  // 最多添加10个
  var length = $("#fileupload").find(".kw__box .kw-chip_container").length;
  if (length != undefined && length === 10) {
    scmpublictoast(grpFile.addLabelTip5, 2000);
    return;
  }
  GrpFile.stopPropagation(e);
  var des3GrpLabelId = $(obj).attr("des3labelid");
  var labelName = $(obj).html();
  var content = Grp.buildUploadFileLabel(des3GrpLabelId, labelName);
  $("#fileupload").find(".kw__box ").append(content);
  $(obj).remove();
  $("#fileupload").find(".set__result-label_item").css("display", "none");
};

/**
 * 移除标签
 */
Grp.removeUploadFileLabel = function(obj) {
  $(obj).closest(".kw-chip_container").remove();
};

/**
 * 检查选中标签，并且置灰
 */
Grp.checkSelectLabel = function() {

  // 首先清空，群组标签的选中样式
  $("#grp_file_label_box").find(".setup-keyword__box .kw-chip_container").each(function() {
    $(this).find(".kw-chip_small").css("background-color", "#ebf5ff");
    $(this).find(".kw-chip_small").attr("check", "");
  });

  var des3GrpLabelIds = $grpFile.listdata.des3GrpLabelIds;
  if ($.trim(des3GrpLabelIds) !== "") {
    var idArray = des3GrpLabelIds.split(",");
    for (var i = 0; i < idArray.length; i++) {
      if ($.trim(idArray[i]) !== "") {
        var id = idArray[i];
        // 第一，置灰单个文件标签
        $("#grp_file_list").find(".main-list__item").each(function() {
          $(this).find(".kw__box  .kw-chip_container ").each(function() {
            if ($(this).attr("des3grplabelid") === id) {
              $(this).find(".kw-chip_small").css("background-color", "#eee");
              $(this).find(".kw-chip_small").attr("check", "check");
            }
          });
        });
        // 第二，置灰群组标签
        // grp_file_label_box setup-keyword__box kw-chip_container
        // kw-chip_small
        $("#grp_file_label_box").find(".setup-keyword__box .kw-chip_container").each(function() {
          if ($(this).find(".kw-chip_small").attr("des3grplabelid") === id) {
            $(this).find(".kw-chip_small").css("background-color", "#eee");
            $(this).find(".kw-chip_small").attr("check", "check");
          };

        });

      }
    }
  }

}
