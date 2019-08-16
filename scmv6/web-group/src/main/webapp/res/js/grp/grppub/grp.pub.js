var GrpPub = GrpPub ? GrpPub : {};

;
GrpPub.showGrpPubFilterBox = function() {
  if ($("#grp_pub_filter_box").css("display") == "none") {
    $("#grp_pub_filter_box").show();
  } else {
    $("#grp_pub_filter_box").hide();
  }
};

;
GrpPub.hideGrpPubFilterBox = function() {
  $("#grp_pub_filter_box").hide();
};

;
GrpPub.hideGrpPubSelectImportType = function() {
  hideDialog("grp_pub_select_import_type");
};

;
GrpPub.showGrpPubSelectImportType = function() {
  showDialog("grp_pub_select_import_type");

};

;
GrpPub.hideGrpPubRcmdBox = function() {
  hideDialog("show_all_pub_rcmd");
};

;
GrpPub.showGrpPubRcmdBox = function() {
  showDialog("show_all_pub_rcmd");
  GrpPub.showGrpPubRcmdList();

};

var $grpPub;
;
GrpPub.showGrpPubList = function(myurl) {
  var url = "/groupweb/grppub/ajaxgrppublist";
  var callurl = "/pub/query/ajaxgrppubcount";
  if (myurl != null && typeof myurl != 'undefined' && "" != myurl) {
    url = myurl;
    callurl = "";
  }
  $("#grp_pubs_List").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  if ($("#frompage").val() == "edit") {
    $("div[list-main='grppub']").attr("memlist", "display");
    $("#frompage").val("");
  }
  $grpPub = window.Mainlist({
    name : "grppub",
    listurl : url,
    listdata : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    listcallback : function(xhr) {
      GrpBase.ajaxTimeOut(xhr.responseText);
      // 初始化上传文件的插件
      GrpPub.loadPubFulltext();
      // 遍历 给所有checkbox 给批量选择框绑定事件-同步上传文件问题
      GrpPub.bindASyncUpload();
    },
    grpmore : true,
    statsurl : callurl,
    drawermethods : locale == "zh_CN" ? {
      "导出成果" : function(a) {
        if (a.length > 0) {
          if (document.getElementsByClassName("drawer-batch__layer").length > 0) {
            document.getElementsByClassName("drawer-batch__layer")[0].style.display = "flex";
          }
        }
      }
    } : {
      "Export" : function(a) {
        if (a.length > 0) {
          if (document.getElementsByClassName("drawer-batch__layer").length > 0) {
            document.getElementsByClassName("drawer-batch__layer")[0].style.display = "flex";
          }
        }
      }
    },
    exportmethods : function(array, exportType) {
      if (array.length > 0) {
        var pubIds = array.join(",");
        GrpPub.pubExport(pubIds, exportType, "common", 1);
      }
    }
  });
};
// 成果删除
GrpPub.pubDel = function(des3PubId) {
  smate.showTips._showNewTips(grpPubCommon.i18n_delete_content, grpPubCommon.i18n_delete_title, "GrpPub.delGrpPubs('"
      + des3PubId + "')", "");

};
// 导出成果
GrpPub.pubExport = function(pubIds, exportType, exportScope, articleType) {
  BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function() {
    if (exportType && exportType != "") {
      window.location.href = "/pub/opt/ajaxpubexport?pubIds=" + pubIds + "&exportType=" + exportType + "&exportScope="
          + exportScope + "&articleType=" + articleType;
    } else {
      scmpublictoast("请选择成果文件导出格式", 1000);
    }
  }, 1);
};
// 加载群组成果上传全文的事件
GrpPub.loadPubFulltext = function() {
  $(".pub_uploadFulltext").each(function() {
    $this = $(this);
    /*
     * var dataJson = {
     * allowType:'*.txt;*.jpg;*.jpeg;*.gif;*.pdf;*.doc;*.docx;*.png;*.html;*.xhtml;*.htm;*.rar;*.zip;',
     * fileDealType: "generalfile" };
     */
    // var filetype =
    // ".txt;.jpg;.jpeg;.gif;.pdf;.doc;.docx;.png;.html;.xhtml;.htm;.rar;.zip;".split(";");
    var data = {
      "fileurl" : "/fileweb/fileupload",
      "filedata" : {
        fileDealType : "generalfile"
      },
      "method" : "nostatus",
      "filecallback" : GrpPub.reloadFulltext,
      "filecallbackparam" : {
        pubId : $this.attr("des3Id")
      },
    // "filetype":filetype
    };

    fileUploadModule.initialization(this, data);
  });
};
GrpPub.requestFullText = function(des3RecvPsnId, des3PubId) {
  if (des3RecvPsnId && des3PubId) {
    $.ajax({
      url : '/pub/fulltext/ajaxreqadd',
      type : 'post',
      data : {
        'des3RecvPsnId' : des3RecvPsnId,
        'des3PubId' : des3PubId,
        'pubType' : 'sns'
      },
      dataType : "json",
      success : function(data) {
        GrpBase.ajaxTimeOut(data, function() {
          if (data.status == "success") { // 全文请求保存成功
            scmpublictoast(grpPubCommon.req_success, 2000);
          } else {
            scmpublictoast(grpPubCommon.req_error, 2000);
          }
        });
      },
      error : function() {
        scmpublictoast(grpPubCommon.req_param_error, 2000);
      }
    });
  }
};
GrpPub.fileuploadBoxOpenInputClick = function(ev) {
  var $this = $(ev.currentTarget);
  $this.find('input.fileupload__input').click();
}
GrpPub.delGrpPubs = function(pubIds) {
  $.ajax({
    url : '/groupweb/grppub/ajaxDeleteGrpPub',
    type : 'post',
    dataType : 'json',
    data : {
      'pubIds' : pubIds,
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(grpPubCommon.delSuccessPre + data.count
              + (data.count > 1 ? grpPubCommon.delSuccessSuffs : grpPubCommon.delSuccessSuff), 1000);
          $grpPub.reloadCurrentPage();
          $grpPub.drawerRemoveSelected();
          GrpPub.showGrpMemberPubSum();
        } else if (data.result == "noPermit") {
          // 项目群组，删除成果提示
          if (model != undefined && model === "pub" && $("#grp_params").attr("smate_grpcategory") == 11) {
            scmpublictoast(grpPubCommon.noRightDelPub, 1000);
          } else {
            scmpublictoast(grpPubCommon.noRightDelRefer, 1000);
          }
        } else {
          scmpublictoast(grpPubCommon.optFail, 1000);
        }
        var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
        pluginclose.closest(".background-cover").style.display = "none";
      })
    },
    error : function() {
    }
  });
}
GrpPub.reloadFulltext = function(xhr, params) {
  if (typeof (xhr) == "undefined" || !params) {
    return false;
  }
  const data = eval('(' + xhr.responseText + ')');
  var pubId = params.pubId;
  var des3fid = data.successDes3FileIds.split(",")[0];
  $
      .ajax({
        url : '/pub/opt/ajaxupdatefulltext',
        type : 'post',
        data : {
          'des3PubId' : pubId,
          'des3FileId' : des3fid
        },
        dataType : 'json',
        timeout : 10000,
        success : function(data) {
          GrpBase
              .ajaxTimeOut(
                  data,
                  function() {
                    if (data.result == "true") {
                      var $drawerBatchBox = $(".drawer-batch__box div[des3id='" + pubId + "']").parent();
                      var img = "<image class='dev_fulltext_download dev_pub_img' style='cursor:pointer' onerror='this.onerror=null;this.src=\"/resmod/images_v5/images2016/file_img1.jpg\"'";
                      img += "src='";
                      if (data.fullTextImagePath != null && data.fullTextImagePath != "") {
                        img += data.fullTextImagePath;
                      } else {
                        img += "/resmod/images_v5/images2016/file_img1.jpg";
                      }
                      img += "'";
                      if (data.downUrl != null && data.downUrl != "") {
                        img += "onclick='window.location.href=\"" + data.downUrl + "\"';";
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
                      $drawerBatchBox.html(img);
                      $grpPub.reloadCurrentPage();
                    } else {
                      scmpublictoast('系统出现异常，请稍后再试', 2000);
                    }
                  });
        }
      });
};
// checkbox选中时，给批量选择框绑定事件-同步上传文件
GrpPub.bindASyncUpload = function() {
  var $drawerBatchBox = $(".drawer-batch__box");
  $drawerBatchBox.on("click", ".pub_uploadFulltext", function() {
    $this = $(this);
    /*
     * var dataJson = {
     * allowType:'*.txt;*.jpg;*.jpeg;*.gif;*.pdf;*.doc;*.docx;*.png;*.html;*.xhtml;*.htm;*.rar;*.zip;', };
     */
    var filetype = ".txt;.jpg;.jpeg;.gif;.pdf;.doc;.docx;.png;.html;.xhtml;.htm;.rar;.zip;".split(";");
    var data = {
      "fileurl" : "/fileweb/fileupload",
      "filedata" : {
        fileDealType : "generalfile"
      },
      "method" : "nostatus",
      "filecallback" : GrpPub.reloadFulltext,
      "filecallbackparam" : {
        pubId : $this.attr("des3id")
      },
      "filetype" : filetype
    };
    fileUploadModule.initialization(this, data);
  });
}

// 刷新列表。 ajb
GrpPub.reloadGrpPubCurrentPage = function() {
  $grpPub.initializeDrawer();
  $grpPub.reloadCurrentPage();
}

;
GrpPub.showGrpPubRcmdList = function() {
  $("#grp_pub_rcmd_list").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  $.ajax({
    url : '/groupweb/grppub/ajaxpubrcmd',
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'showType' : 2,
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_pub_rcmd_list").html(data);
      });

    },
    error : function() {
    }
  });

};

;
GrpPub.showGrpPubRcmd = function() {
  $("#grpFileMyFileListId").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  var url = "/groupweb/grppub/ajaxpubrcmd";
  if (ispending == 2) {// 群组列表未处理事项跳转
    url += "?ispending=2";
    ispending = 0;
  }
  $.ajax({
    url : url,
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'showType' : 1,
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_pub_rcmd").html(data);
      });

    },
    error : function() {
    }
  });

};

GrpPub.deleteGrpPubConfirm = function(pubIds, canDelete) {
  var callbackData = {
    'pubIds' : pubIds,
    'canDelete' : canDelete
  };
  // 删除成果
  smate.showTips._showNewTips(grpPubCommon.delPubConfirm, grpPubCommon.i18n_delete_title, "GrpPub.deleteGrpPub('"
      + pubIds + "'," + canDelete + ")", "");

};

GrpPub.deleteGrpPub = function(pubIds, canDelete) {
  if (canDelete != 1) {
    scmpublictoast(grpPubCommon.noRightDelPub, 1000);
  }
  $.ajax({
    url : '/groupweb/grppub/ajaxDeleteGrpPub',
    type : 'post',
    dataType : 'json',
    data : {
      'pubIds' : pubIds,
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(grpPubCommon.optSuccess, 1000);
          // 批量处理
          $grpPub.drawerRemoveSelected(data.delDes3Pubs);
          $grpPub.reloadCurrentPage();
          GrpPub.showGrpMemberPubSum();
          $("#alert_box_cancel_btn").click();
        } else {
          scmpublictoast(grpPubCommon.optFail, 1000);
        }
      })
    },
    error : function() {
    }
  });

};
GrpPub.editPub = function(des3PubId) {
  var showmodule = $(".main-list").find(".filter-list").attr("showmodule");
  var extarParams = "";
  // 群组成果： 0==文献， 1==成果
  if (showmodule !== undefined && "projectPub" === showmodule) {
    extarParams = "&isProjectPub=1";
  } else if (showmodule !== undefined && "projectRef" === showmodule) {
    extarParams = "&isProjectPub=0";
  } else {
    extarParams = "&isProjectPub=0";
  }
  // 保存过滤条件
  $grpPub.setCookieValues();
  var forwardUrl = "/pub/enter?des3PubId=" + encodeURIComponent(des3PubId) + extarParams;;
  BaseUtils.forwardUrlRefer(true, forwardUrl);
};

GrpPub.markGrpPub = function(pubIds, canMark, isProjectPub, isLabel) {
  if (canMark != 1) {
    scmpublictoast(grpPubCommon.noRightMarkPub, 1000);
    return;
  }
  if (isProjectPub == 1 && isLabel == 1) {
    GrpBase.jconfirm(function() {
      // 标记为文献
      GrpPub.markGroupPub(pubIds);
    }, grpPubCommon.markReferTips);
  } else {
    GrpPub.markGroupPub(pubIds);
  }

};
GrpPub.markGroupPub = function(pubIds) {
  $.ajax({
    url : '/groupweb/grppub/ajaxmarkgrppub',
    type : 'post',
    dataType : 'json',
    data : {
      'pubIds' : pubIds,
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(grpPubCommon.optSuccess, 1000);
          GrpPub.showGrpPubList();
        } else {
          scmpublictoast(grpPubCommon.optFail, 1000);
        }
      })

    },
    error : function() {
    }

  });
}

;
GrpPub.editGrpPub = function(des3pubId, canEdit) {
  var des3GrpId = $("#grp_params").attr("smate_des3_grp_id");
  if (canEdit != 1) {
    scmpublictoast(grpPubCommon.noRightEditPub, 1000);
    return;
  }
  var showmodule = $(".main-list").find(".filter-list").attr("showmodule");
  var extarParams = "";
  // 群组成果： 0==文献， 1==成果
  if (showmodule !== undefined && "projectPub" === showmodule) {
    extarParams = "&isProjectPub=1";
  } else if (showmodule !== undefined && "projectRef" === showmodule) {
    extarParams = "&isProjectPub=0";
  } else {
    extarParams = "&isProjectPub=0";
  }
  // 保存过滤条件
  $grpPub.setCookieValues();
  var forwardUrl = "/pub/enter?des3PubId=" + encodeURIComponent(des3pubId) + "&des3GrpId="
      + encodeURIComponent(des3GrpId) + extarParams;
  BaseUtils.forwardUrlRefer(true, forwardUrl);
};

GrpPub.searchPubImport = function() {
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
    var showmodule = $(".main-list").find(".filter-list").attr("showmodule");
    var extarParams = "";
    // savePubType = 0; // 0==群组文献， 1==群组项目成果 ，2==群组项目文献
    if (showmodule !== undefined && "projectPub" === showmodule) {
      extarParams = "&savePubType=1";
    } else if (showmodule !== undefined && "projectRef" === showmodule) {
      extarParams = "&savePubType=2";
    }
    var grpId = $("#grp_params").attr("smate_des3_grp_id");
    location.href = "/pub/import/search?des3GroupId=" + encodeURIComponent(grpId) + "&nodeId=1" + "&artType=" + 1
        + extarParams;
  }, 1);

};

GrpPub.manualImportPub = function() {
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
    // 保存过滤条件
    $grpPub.setCookieValues();
    var showmodule = $(".main-list").find(".filter-list").attr("showmodule");
    var extarParams = "";
    // savePubType = 0; // 0==群组文献， 1==群组项目成果
    if (showmodule !== undefined && "projectPub" === showmodule) {
      extarParams = "&isProjectPub=1";
    } else if (showmodule !== undefined && "projectRef" === showmodule) {
      extarParams = "&isProjectPub=0";
    } else {
      extarParams = "&isProjectPub=0";
    }
    var des3GrpId = $("#grp_params").attr("smate_des3_grp_id");
    var forwardUrl = "/pub/enter?des3GrpId=" + encodeURIComponent(des3GrpId) + extarParams;
    BaseUtils.forwardUrlRefer(false, forwardUrl);
  }, 1);

};

GrpPub.manualImpMemberPub = function(ev) {
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
    // 隐藏弹框
    GrpPub.hideGrpPubSelectImportType();
    newgroupimportAchive();
    GrpPub.showGrpMemberPubSum2();
  }, 1);

};

GrpPub.showGrpMemberPubSum2 = function() {
  $("#grp_member_pub2").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });

  $.ajax({
    url : '/groupweb/grpmember/ajaxshowmemberpubsum2',
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_member_pub2").html(data);
        // 默认选中第一个
        var item = $("#grp_member_pub2").find(".group-achive_left-item:first");
        if (item.length > 0) {
          GrpPub.showGrpMemberPubList(item.attr("des3psnid"))
        } else {
          var promptWords = locale == "zh_CN" ? "未找到符合条件的记录" : "No matched record has been found.";
          $("#responseNoResultMemberPub2").html("<div class = 'response_no-result'>" + promptWords + "</div>");
        }
      });

    },
    error : function() {
    }
  });

};
;
GrpPub.showGrpMemberPubSum = function() {
  $("#grp_member_pub").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });

  var isProjectPub = null;// 项目成果1 项目文献0
  if ($("#grp_params").attr("smate_grpcategory") == "11") {
    if ("projectRef" == model) {
      isProjectPub = 0;
    }
    if ("pub" == model) {
      isProjectPub = 1;
    }
  }

  $.ajax({
    // url : '/groupweb/grpmember/ajaxshowmemberpubsum',
    url : '/groupweb/grpmember/ajaxshowmemberpublist',
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'isProjectPub' : isProjectPub
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_member_pub").html(data);
      });

    },
    error : function() {
    }
  });

};

var GrpPub_memberpubMainlist;
;
GrpPub.showGrpMemberPubList = function(des3PsnId) {
  if (des3PsnId == null || $.trim(des3PsnId) == "") {
    return;
  }
  GrpPub_memberpubMainlist = window.Mainlist({
    name : "memberpub2",
    listurl : "/groupweb/grppub/ajaxmemberpublist2",
    listdata : {
      'des3PsnId' : des3PsnId,
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'isPsnPubs' : '1'
    },
    listcallback : function(xhr) {
      $("div[list-main='memberpub2']").show();
      GrpPub.memberPubListCallBack();
      // 初始化上传文件的插件
      GrpPub.loadMemberPubFulltext();
      // 遍历 给所有checkbox 给批量选择框绑定事件-同步上传文件问题
      GrpPub.bindASyncMemberUpload();
    },
    statsurl : "/pub/query/ajaxgrppubcount"
  });
};
GrpPub.memberPubListCallBack = function() {
  $("div[list-main='memberpub2']")
      .off("click", ".dev_update_text_event")
      .on(
          "click",
          ".dev_update_text_event",
          function() {
            var $obj = $(this);
            var item = $obj.closest(".main-list__item");
            // 不是自己的-请求全文
            if ((item.attr("hasFulltext") == "0" || (item.attr("hasFulltext") == "1" && item.attr("fullTextPermission") == "2"))
                && item.attr("isOwn") == "0") {
              GrpPub.requestFullText(item.attr("des3RecvPsnId"), item.attr("des3PubId"));
            }
            // // 是自己的-去上传全文
            // if (item.attr("hasFulltext") == "0" && item.attr("isOwn") ==
            // "1") {
            // GrpPub.editPub(item.attr("des3PubId"));
            // }
          });
};
GrpPub.reShowGrpMemberPubList = function(des3PsnId) {
  if (des3PsnId != null && $.trim(des3PsnId) != "") {
    GrpPub_memberpubMainlist.listdata.des3PsnId = des3PsnId;
    GrpPub_memberpubMainlist.resetAllFilterOptions();
  }
};

//
GrpPub.selectShowGrpImpPubMember = function(obj) {
  var check = $(obj).attr("check");
  var des3MemberId = $(obj).attr("des3PsnId");
  if (check != undefined && check == "check") {// 第二次点击
    $(obj).attr("check", "");
    $(obj).css("background-color", "");
    $("#memberSelectId").attr("des3MemberId", "");
    $grpPub.listdata.des3MemberId = "";
  } else { // 第一次点击
    // 重置其他已经点击的
    $(".friend-selection__item-2").each(function() {
      $(this).attr("check", "");
      $(this).css("background-color", "");
    });
    $(obj).attr("check", "check");
    $(obj).css("background-color", "#eee");
    $("#memberSelectId").attr("des3MemberId", des3MemberId);
    $grpPub.listdata.des3MemberId = des3MemberId;
  }
  $grpPub.resetAllFilterOptions();
};

;
GrpPub.importMemberPubToGrp = function(obj) {
  GrpBase.doHitMore(obj, 1000);
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
    var ids = "";
    $("div[list-main='memberpub2']").find(".item-selected_tip").each(function() {
      if ($(this).attr("class").indexOf("item-selected_forbid-tip") == -1) {
        var des3pubid = $(this).closest(".main-list__item").attr("des3pubid");
        if (des3pubid != "") {
          ids += "," + des3pubid;
        }
      }
    });
    if (ids != "") {
      ids = ids.substring(1);
    };
    if (ids == "") {
      scmpublictoast(grpPubCommon.choosePub, 2000);
      return;
    }

    if (model == null) {
      console.log("读取model失败，请检查GrpPub.importMemberPubToGrp", 2000);
      scmpublictoast("系统繁忙，请稍后重试", 2000);
      return;
    }
    var savePubType = 0;
    var grpcategory = $("#grp_params").attr("smate_grpcategory");
    if ("projectRef" == model) {
      savePubType = 0;
    }
    if ("pub" == model) {
      savePubType = 1;
    }
    // 课程群组
    if ("10" == grpcategory) {
      savePubType = 0;
    }
    $.ajax({
      url : '/groupweb/grppub/ajaximportmemberpub',
      type : 'post',
      dataType : 'json',
      data : {
        'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
        'pubIds' : ids,
        'savePubType' : savePubType
      },
      success : function(data) {
        GrpBase.ajaxTimeOut(data, function() {
          GrpPub_memberpubMainlist.reloadCurrentPage();
          scmpublictoast(grpPubCommon.importSuccess, 1000);
        });

      }
    });
  }, 1);

};

GrpPub.optionRcmdGrpPub = function(pubIds, optionType) {
  var grpId = $("#grp_params").attr("smate_grp_id");
  if (pubIds == 'undifine' || pubIds == "") {
    return;
  }
  $.ajax({
    url : '/groupweb/grppub/ajaxoptionpubrcmd',
    type : 'post',
    dataType : 'json',
    data : {
      'pubIds' : pubIds,
      'grpId' : grpId,
      'optionType' : optionType
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(grpPubCommon.optSuccess, 1000);
          GrpPub.showGrpPubRcmd();
          GrpPub.showGrpPubRcmdList();
          if (optionType == 1) {
            GrpPub.showGrpPubList();
          }
        } else {
          scmpublictoast(grpPubCommon.optFail, 1000);
        }
      })

    },
    error : function() {
    }
  });
};

GrpPub.confirmAllRcmdGrpPub = function() {

  var pubIds = "";
  $(".grp_pub_rcmd_info").each(function() {
    pubIds += "," + $(this).attr("pubId");
  });
  if (pubIds != "") {
    pubIds = pubIds.substring(1);
    GrpPub.optionRcmdGrpPub(pubIds, 1)
  };
};

// 成果赞操作
GrpPub.pubAward = function(obj, des3ResId) {
  var f = $(obj).attr("onclick");
  $(obj).attr("onclick", "");
  var awardCount = 0;
  var post_data = {
    "resType" : 1,
    "resNode" : 1,
    "des3PubId" : des3ResId
  };
  if ($(obj).hasClass("is_award")) {
    post_data.operate = '1';// 赞
  } else {
    post_data.operate = '0';// 取消赞
  }
  $.ajax({
    url : '/pub/opt/ajaxlike',
    type : 'post',
    dataType : 'json',
    data : post_data,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        awardCount = data.awardTimes;
        if (data.result == "success") {
          if ($(obj).hasClass("is_award")) {//
            $(obj).removeClass("is_award");
            $(obj).parent().find('.dev_pub_award_item:first').text(grpPubCommon.unlike + "(" + awardCount + ")");
            if ($(obj).parent().find("i:first").hasClass("icon-praise")) {
              $(obj).parent().find("i:first").removeClass("icon-praise").addClass("icon-praise-award");
            }
          } else {
            if (awardCount == 0) {
              $(obj).parent().find('.dev_pub_award_item:first').text(grpPubCommon.like);
            } else {
              $(obj).parent().find('.dev_pub_award_item:first').text(grpPubCommon.like + "(" + awardCount + ")");
            }
            $(obj).addClass("is_award");
            if ($(obj).parent().find("i:first").hasClass("icon-praise-award")) {
              $(obj).parent().find("i:first").removeClass("icon-praise-award").addClass("icon-praise");
            }
          }
        }
        $(obj).attr("onclick", f);
      })

    },
    error : function(data) {
      $(obj).attr("onclick", f);
    }
  });
};

/**
 * 把当前页码添加到url上
 */
GrpPub.addPageValueToUrl = function() {
  var pageValue = $(".pagination__pages_list .item_selected").attr("page-value");
  console.log(page_value);

  var json = {};
  var oldUrl = window.location.href;
  var index = oldUrl.indexOf("pageValue");
  var newUrl = window.location.href;

  if (oldUrl.indexOf("?") < 0) {
    newUrl = oldUrl + "?pageValue=" + pageValue;
  } else {
    if (index < 0) {
      newUrl = oldUrl + "&pageValue=" + pageValue;
    } else {
      var flag = false;
      var params = "";
      var paraString = oldUrl.substring(oldUrl.indexOf("?") + 1, oldUrl.length);
      var paraArr = paraString.split("&");
      for (var i = 0; j = paraArr[i]; i++) {
        // 参数名.
        var name = j.substring(0, j.indexOf("="));
        // 参数值.
        var value = j.substring(j.indexOf("=") + 1, j.length);
        if (name == 'pageValue') {// 存在页码.
          flag = true;
          value = pageValue;
        }
        params = params + name + "=" + value + "&";
      }
      newUrl = oldUrl.substring(0, oldUrl.indexOf("?")) + params;
    }
  }
  window.history.replaceState(json, "", newUrl);
}

// 加载群组成员成果上传全文的事件
GrpPub.loadMemberPubFulltext = function() {
  $(".pub_uploadFulltext").each(function() {
    $this = $(this);
    var data = {
      "fileurl" : "/fileweb/fileupload",
      "filedata" : {
        fileDealType : "generalfile"
      },
      "method" : "nostatus",
      "filecallback" : GrpPub.reloadMemberPubFulltext,
      "filecallbackparam" : {
        pubId : $this.attr("des3Id")
      },
    };

    fileUploadModule.initialization(this, data);
  });
};

GrpPub.reloadMemberPubFulltext = function(xhr, params) {
  if (typeof (xhr) == "undefined" || !params) {
    return false;
  }
  const data = eval('(' + xhr.responseText + ')');
  var pubId = params.pubId;
  var des3fid = data.successDes3FileIds.split(",")[0];
  $
      .ajax({
        url : '/pub/opt/ajaxupdatefulltext',
        type : 'post',
        data : {
          'pubId' : pubId,
          'des3FileId' : des3fid
        },
        dataType : 'json',
        timeout : 10000,
        success : function(data) {
          GrpBase
              .ajaxTimeOut(
                  data,
                  function() {
                    if (data.result == "true") {
                      var $listIconBox = $(".dev_pub_grp_" + pubId + " .group-achive_body-content_item-avator");
                      var img = "<image class='dev_fulltext_download dev_pub_img' style='cursor:pointer' onerror='this.onerror=null;this.src=\"/resmod/images_v5/images2016/file_img1.jpg\"'";
                      img += "src='";
                      if (data.fullTextImagePath != null && data.fullTextImagePath != "") {
                        img += data.fullTextImagePath;
                      } else {
                        img += "/resmod/images_v5/images2016/file_img1.jpg";
                      }
                      img += "'";
                      if (data.downUrl != null && data.downUrl != "") {
                        img += "onclick='window.location.href=\"" + data.downUrl + "\"';";
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
                    } else {
                      scmpublictoast('系统出现异常，请稍后再试', 2000);
                    }
                  });
        }
      });
};
// checkbox选中时，给批量选择框绑定事件-同步上传文件
GrpPub.bindASyncMemberUpload = function() {
  var $drawerBatchBox = $(".drawer-batch__box");
  $drawerBatchBox.on("click", ".pub_uploadFulltext", function() {
    $this = $(this);
    var filetype = ".txt;.jpg;.jpeg;.gif;.pdf;.doc;.docx;.png;.html;.xhtml;.htm;.rar;.zip;".split(";");
    var data = {
      "fileurl" : "/fileweb/fileupload",
      "filedata" : {
        fileDealType : "generalfile"
      },
      "method" : "nostatus",
      "filecallback" : GrpPub.reloadMemberPubFulltext,
      "filecallbackparam" : {
        pubId : $this.attr("des3id")
      },
      "filetype" : filetype
    };
    fileUploadModule.initialization(this, data);
  });
}
