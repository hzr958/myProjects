/**
 * 分享js zzx
 */
var SmateShare = SmateShare ? SmateShare : {};
var ctxpath = '/scmwebsns';
SmateShare.timeOut = false;
// 资源类型ID
SmateShare.resTypeId = {
  "fund" : "11",
  "agency" : "25",
  "snspub" : "1",
  "pdwhpub1" : "22",
  "pdwhpub2" : "24",
  "prj" : "4"
}

// 超时处理
SmateShare.ajaxTimeOut = function(data, myfunction) {
  var toConfirm = false;

  if ('{"ajaxSessionTimeOut":"yes"}' == data) {
    toConfirm = true;
    SmateShare.timeOut = true;
  } else {
    SmateShare.timeOut = false;
  }
  if (!toConfirm && data != null) {
    toConfirm = data.ajaxSessionTimeOut;
  }
  if (toConfirm) {
    // 如果报错请添加js：<script type="text/javascript"
    // src="${resmod}/js/smate.share_${locale}.js"></script>
    jConfirm(smateShare.i18n_timeout, smateShare.i18n_tipTitle, function(r) {
      if (r) {
        var url = window.location.href;
        document.location.href = "/oauth/index?service=" + encodeURIComponent(url);
        return 0;
      }
    });

  } else {
    if (typeof myfunction == "function") {
      myfunction();
    }
  }
}

SmateShare.clickcLeanSeachKey = function() {
  $("#select_grp_searchKey").val("");
  $("#select_grp_searchKey").keyup();
}

SmateShare.clickSeachGrp = function() {
  $("#grp_seach_input").closest(".dialogs__childbox_fixed").toggle("slow");
}

SmateShare.showShareToScmBox = function(event) {
  // SCM-16060 点击分享到科研之友才验证登录
  BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function() {
    // 初始化推荐联系人
    SmateShare.loadMaybeShareFriendList();
    SmateShare.showBoxInterval = setInterval(function() {
      SmateShare.changeForShare();
    }, 1000);
    // $(document).on("DOMSubtreeModified","#grp_friends,#share_to_scm_box,#grp_names",SmateShare.changeForShare);
    // SCM-15452
    $("#id_sharegrp_textarea").val("");
    $("#grp_addfriend").text("");
    showDialog("share_to_scm_box");
    // 检索框聚焦
    $("#grp_addfriend").focus();
  }, 1);
};
// 站外成果分享到科研之友
SmateShare.showOutsideShareToScmBox = function(event) {
  BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function() {
    SmateShare.showBoxInterval = setInterval(function() {
      SmateShare.changeForShare();
    }, 1000);
    // $(document).on("DOMSubtreeModified","#grp_friends,#share_to_scm_box,#grp_names",SmateShare.changeForShare);
    showDialog("share_to_scm_box");
  }, 1);
};

SmateShare.hideShareToScmBox = function() {
  clearInterval(SmateShare.showBoxInterval);
  // $(document).off("DOMSubtreeModified","#grp_friends,#share_to_scm_box,#grp_names",SmateShare.changeForShare);
  $("#grp_friends").find(".chip__box").remove();
  $("#shareFriendResults").find(".chip__box").remove();
  hideDialog("share_to_scm_box");
  // 显示批量处理框,如果要想显示在批量处理窗口，在drawer-batch__mask class添加 dev_show_drawer-batch
  $(".drawer-batch__box").find(".dev_show_drawer-batch").click();
  $(".drawer-batch__box").find(".dev_show_drawer-batch").removeClass("dev_show_drawer-batch");
};
// --分享确定按钮是否可以点击--SCM-13061
SmateShare.changeForShare = function() {
  var type = $("#share_to_scm_box").find("*[selector-id='list_sharetype']").attr("sel-value");
  var btn = $("#sharePrimary");
  setTimeout(function() {
    var paramId = "";
    var emails = "";
    if (type == 1) {
      paramId = $("#grp_names").find(".dev_grp_input").attr("code");
    } else if (type == 2) {
      paramId = $("#share_to_scm_box").attr("resid");
      if (paramId == null || paramId == "") {
        paramId = $("#share_to_scm_box").attr("des3ResId");
      }
    } else if (type == 3) {
      $("#grp_friends").find(".chip__text").each(function(i, n) {
        paramId += $(n).closest(".chip__box").attr("code");
        if (SmateShare.isEmail2($(this).html())) {
          emails += $(this).html();
        }
      });

    }
    if ((paramId != null && $.trim(paramId) != "") || ($.trim(emails) != "")) {
      btn.removeAttr("disabled");
    } else {
      btn.attr("disabled", true);
    }
  }, 300);
}

SmateShare.isEmail2 = function(email) {
  return /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/.test(email);
};
SmateShare.showShareToScmSelectGrpBox = function() {
  showDialog("share_to_scm_select_grp_box");
  SmateShare.loadGrpList();
};

SmateShare.hideShareToScmSelectGrpBox = function() {
  hideDialog("share_to_scm_select_grp_box");
}

SmateShare.showShareToScmSelectFriendBox = function() {
  $("#share_to_scm_select_friend_box").find(".dev_search_key").val("");
  showDialog("share_to_scm_select_friend_box");
  SmateShare.loadFriendList();
};

SmateShare.hideShareToScmSelectFriendBox = function() {
  // 在remove之前必须重置所有的人员
  $("#shareFriendResults").find(".chip__box").remove();
  // $("#grp_friends").find(".chip__box").remove();

  hideDialog("share_to_scm_select_friend_box");
};

SmateShare.showShareType = function() {
  $("#id_list_sharetype").show();
};
SmateShare.loadFriendList = function(order) {
  var orderBy = "";
  var loadUrl = "";
  var fromAttention = false;
  if (order != undefined) {
    orderBy = order;
  } else {
    orderBy = $("#share_to_scm_select_friend_box").find(".dialogs__header_sort-box_detaile").attr("val");
  }
  // 排除关注人员标识
  if ($("#share_to_scm_select_friend_box").attr("attention") == "true") {
    fromAttention = true;
  }
  $("#id_grp_add_friend_names_list").html("");
  $("#id_grp_add_friend_names_list").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
    status : 1
  });
  var searchKey = $("#share_to_scm_select_friend_box").find(".dev_search_key").val();
  var des3GroupId = $("#grp_params").attr("smate_des3_grp_id");
  // 判断是群组成员还是联系人
  if (des3GroupId != null && des3GroupId != "" && $("#memberTypeSelect").val() == "0") {
    // 群组成员
    loadUrl = "/groupweb/share/ajaxgetmyfriendnames";
  } else {
    // 联系人
    loadUrl = "/psnweb/friend/ajaxgetmyfriendnames";
  }

  $.ajax({
    url : loadUrl,
    type : 'post',
    dataType : 'html',
    data : {
      "type" : 1,
      "searchKey" : searchKey,
      "orderBy" : orderBy,
      "fromAttention" : fromAttention,
      "des3GroupId" : (des3GroupId != null) ? des3GroupId : ""
    },
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        $("#id_grp_add_friend_names_list").html(data);
        if ($("#id_grp_add_friend_names_list").find(".friend-selection__item-3").length == 0) {
          $("#id_grp_add_friend_names_list").addClass("main-list__list");
          // 没有查询到任何联系人
          $("#id_grp_add_friend_names_list").html(
              "<div class='response_no-result'>" + smateShare.i18n_noSearchFriend + "</div>");
        } else {
          $("#id_grp_add_friend_names_list").removeClass("main-list__list");
        }
        SmateShare.showFriendCallback();
        $("#share_to_scm_select_friend_box").show();
      });
    },
    error : function() {
    }
  });
};
// 加载可能要分享的联系人列表
SmateShare.loadMaybeShareFriendList = function() {
  var des3GroupId = $("#grp_params").attr("smate_des3_grp_id");
  var shareUrl = "";
  if (des3GroupId != null && des3GroupId != "") {
    shareUrl = "/groupweb/share/ajaxgetmyfriendnames";
  } else {
    shareUrl = "/psnweb/friend/ajaxgetmyfriendnames";
  }

  $.ajax({
    url : shareUrl,
    type : 'post',
    dataType : 'html',
    data : {
      "type" : 2,
      "des3GroupId" : (des3GroupId != null) ? des3GroupId : "",
    },
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        $("#share_to_scm_box").find(".share-friends__rcmd_chips-container").html(data);
        // 隐藏推荐的联系人
        $("#grp_friends").find(".chip__box").each(function() {
          var des3PsnId = $(this).attr("code");
          $("#id_bt_select_friend").find(".share-friends__rcmd_chips-container .chip__box ").each(function() {
            if ($(this).attr("code") === des3PsnId) {
              $(this).css("display", "none");
            }
          });
        });
      });
    },
    error : function() {
    }
  });
};
// 获取选择群组列表数据
SmateShare.loadGrpList = function() {
  $("#share_to_scm_select_grp_list").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
    status : 1
  });
  $.ajax({
    url : '/groupweb/share/ajaxselectgrplist',
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'searchKey' : $("#select_grp_searchKey").val()
    },
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        $("#share_to_scm_select_grp_list").html(data);
        if ($("#share_to_scm_select_grp_list").find(".main-list__item").length == 0) {
          $("#share_to_scm_select_grp_list").html(
              "<div class='response_no-result'>" + smateShare.i18n_norecord + "</div>");
        }
      });
    },
    error : function() {
    }
  });
};
// 分享到群组
SmateShare.shareToGrp = function() {
  var tempType = "";
  var $obj = $("#share_to_scm_box");
  var des3ResId = $obj.attr("des3ResId");
  var des3GrpId = $obj.attr("des3GrpId");
  var dyntype = $obj.attr("dyntype");
  var resType = $obj.attr("resType");
  var dynId = $obj.attr("des3dynid");
  dynId = dynId ? dynId : $obj.attr("dynid");
  var databaseType = $obj.attr("databaseType");
  var dbId = $obj.attr("dbId");
  var pubId = $obj.attr("pubid");
  if(resType==26){
    pubId = $obj.attr("newsId");
  }
  if ("GRP_ADDFILE" == dyntype || "GRP_SHAREFILE" == dyntype || "GRP_ADDCOURSE" == dyntype || "GRP_ADDWORK" == dyntype) {
    tempType = "GRP_SHAREFILE";
    resType = "grpfile";
  } else if ("GRP_ADDPUB" == dyntype || "GRP_PUBLISHDYN" == dyntype || "GRP_SHAREPUB" == dyntype) {
    tempType = "GRP_SHAREPUB";
    if (resType == null || resType == "") {
      resType = "pub";
    }
  } else if ("GRP_SHAREFUND" == dyntype || "PSN_SHAREFUND" == dyntype) {
    tempType = "GRP_SHAREFUND";
    resType = "fund";
  } else if ("GRP_SHAREAGENCY" == dyntype || "PSN_SHARE_AGENCY" == dyntype) {
    tempType = "GRP_SHAREAGENCY";
    resType = "agency";
  } else if ("GRP_SHARENEWS" == dyntype || "PSN_SHARENEWS" == dyntype) {
    tempType = "GRP_SHARENEWS";
    resType = "news";
  } else if ("PSN_SHAREFILE" == dyntype) {
    scmpublictoast(smateShare.i18n_filenotshare, 2000);
    return 0;
  } else if ("ATEMP" == dyntype || "B1TEMP" == dyntype || "B2TEMP" == dyntype || "B3TEMP" == dyntype) {

  }
  if (resType == "GRP_SHAREPDWHPUB" || dyntype == "GRP_SHAREPDWHPUB") {
    tempType = "GRP_SHAREPDWHPUB";
  }
  des3ResId = $obj.attr("des3ResId");
  switch (resType) {
    case "1" :
      tempType = "GRP_SHAREPUB";
      resType = "pub";
    break;
    case "11" :
      tempType = "GRP_SHAREFUND";
      resType = "fund";
    break;
    case "22" :
    case "24" :
      tempType = "GRP_SHAREPDWHPUB";
      resType = "pdwhpub";
    break;
    case "4" :
      tempType = "GRP_SHAREPRJ";
      resType = "prj";
    break;
    case SmateShare.resTypeId.agency :
      tempType = "GRP_SHAREAGENCY";
      resType = "agency";
    break;
    case "26" :
      tempType = "GRP_SHARENEWS";
      resType = "news";
    break;
    default :
    break;
  }
  var receiverGrpId = $("#grp_names").find(".dev_grp_input").attr("code");
  if (receiverGrpId == null || receiverGrpId == "") {
    return 0;
  }
  // 基金
  var resInfoId = $("#share_to_scm_box").attr("resInfoId");
  var resInfoJson = "";
  if (resInfoId != null && resInfoId != "" && typeof (resInfoId) != "undefined") {
    resInfoJson = JSON.stringify({
      "fund_desc_zh" : $("#zhShowDesc_" + resInfoId).val(),
      "fund_desc_en" : $("#enShowDesc_" + resInfoId).val(),
      "fund_title_zh" : $("#zhTitle_" + resInfoId).val(),
      "fund_title_en" : $("#enTitle_" + resInfoId).val(),
      "fund_logo_url" : $("#share_to_scm_box").attr("logoUrl")
    });
  }
  var dynComment = $.trim($("#id_sharegrp_textarea").val()).replace(/\n/g, '<br>');
  var data = {
    "des3GroupId" : des3GrpId,
    "dynContent" : dynComment,
    "des3ResId" : des3ResId,
    "tempType" : tempType,
    "resType" : resType,
    "des3ReceiverGrpId" : receiverGrpId,
    "databaseType" : databaseType,
    "dbId" : dbId,
    "resInfoJson" : resInfoJson
  };
  var shareContent = $("#id_sharegrp_textarea").val();
  $.ajax({
    url : "/dynweb/dynamic/ajaxgroupdyn/dopublish",
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        scmpublictoast(smateShare.i18n_shareSuccess, 1000);
        if (typeof shareGrpCallback == "function") {
          if (resType == "fund") {
            shareGrpCallback(dynId, $("#id_sharegrp_textarea").val(), des3ResId, resInfoId, undefined, receiverGrpId,
                dyntype, resType);
          } else if (resType == "agency") {
            var $input = $("input[des3dynid='" + dynId + "']");
            // 首页资金机构分享至群组回调
            if ($input.nextAll(".dynamic-social__list ").length > 0) {
              SmateShare.shareToGrpCallBack($obj.attr("des3dynid"), data, des3ResId, receiverGrpId, dynComment,dyntype, resType);
            } else {
              if (typeof shareAgencyGrpCallback === "function") {
                shareAgencyGrpCallback(data, des3ResId, receiverGrpId, dynComment,dynId);
              }
            }
          } else {
            shareGrpCallback(dynId, shareContent, des3ResId, pubId, undefined, receiverGrpId, dyntype,
                resType);
          }
        }
        // 个人主页项目列表分享更新
        var countStr = $("span[value='" + des3ResId + "']").text();
        if (countStr != "") {
          var newCount = parseInt(countStr.replace("(", "").replace(")", "")) + 1;
          $("span[value='" + des3ResId + "']").text("(" + newCount + ")");
        } else {
          $("span[value='" + des3ResId + "']").text("(1)");
        }
      });

    },
    error : function() {
    }
  });
}
// 资助机构分享至群组回调
SmateShare.shareToGrpCallBack = function(dynId, data, des3ResId, des3GroupId, comments,dyntype, resType) {
  $.ajax({
    url : "/prjweb/share/ajaxupdate",
    type : "post",
    data : {
      "Des3FundAgencyId" : des3ResId,
      "shareToPlatform" : 3,
      "des3GroupId" : des3GroupId,
      "comments" : comments
    },
    dataType : "json",
    success : function(data) {
      shareGrpCallback (dynId,comments,des3ResId,undefined,undefined ,des3GroupId, dyntype, resType);
    },
    error : function() {
    }
  });
}
/*
 * // 添加分享记录 SmateShare.addShareRecored = function() { var des3ResId = $obj.attr("des3ResId"); if
 * (des3ResId == null) { des3ResId = $obj.attr("resid"); } $.ajax({ url :
 * "/dynweb/dynamic/ajaxgroupdyn/dopublish", type : 'post', dataType : 'json', data : data, success :
 * function(data) { SmateShare.ajaxTimeOut(data, function() {
 * scmpublictoast(smateShare.i18n_optSuccess, 1000); if (typeof shareGrpCallback == "function")
 * shareGrpCallback($obj.attr("dynId"), $("#id_sharegrp_textarea").val(), $obj .attr("des3ResId"),
 * $obj.attr("pubId")); }); }, error : function() { } }); }
 */
// 快速分享
SmateShare.quickShareDyn = function(obj) {
  var $dynIds = $(obj).closest(".main-list__item").find("input[name='dynId']");
  $dynIds = $dynIds.eq($dynIds.length - 1);
  var resType = $dynIds.attr("restype");
  var des3ResId = $dynIds.attr("des3ResId");
  var dynType = $dynIds.attr("dyntype");
  var des3DynId = $dynIds.attr("des3DynId");
  var des3ParentDynId = $dynIds.attr("des3ParentDynId");
  var databaseType = $dynIds.attr("databasetype");
  dynamic.quickShare(resType, des3ResId, dynType, des3DynId, des3ParentDynId, databaseType);
}
// 分享到动态
SmateShare.shareToDyn = function(shareType) {
  var $obj = $("#share_to_scm_box");
  var formDynType = $obj.attr("dyntype");
  var resType = $obj.attr("resType");
  var dbId = $("#share_to_scm_box").attr("dbid");
  var des3ResId = $obj.attr("des3ResId");
  var pubId = $obj.attr("pubid");
  if (des3ResId == null || des3ResId == "") {
    des3ResId = $obj.attr("resid");
  }
  var pdwhpubShare = $obj.attr("pdwhpubShare");
  var dynText = $.trim($("#id_sharegrp_textarea").val()).replace(/\n/g, '<br>');
  // 动态快速分享改造--start
  if (formDynType === "B1TEMP" || formDynType === "ATEMP") {
    dynamic.quickShare($obj.attr("des3dynid"), formDynType, des3ResId, resType, dynText, function() {
      // 需求变更,跳入页面分享,需要重定向到动态首页
      // window.location.href = "/dynweb/mobile/dynshow";
      scmpublictoast(dynCommon.shareSuccess, 1000);
      setTimeout(dynamic.openDynList, 1000);
    });
    return;
  }
  // 动态快速分享改造--end
  var dynType = "B2TEMP";
  var operatorType = 3;
  var isB2T = true;
  if (resType == null) {
    alert("配置动态初始化方法SmateShare.getXXXSareParam出错，缺失resType");
  }
  if (resType != 0 && (des3ResId == null || des3ResId == "")) {
    alert("配置动态初始化方法SmateShare.getXXXSareParam出错,resType=" + resType + ",但是没有取到des3ResId");
  }
  if ("GRP_ADDFILE" == formDynType || "GRP_SHAREFILE" == formDynType || "GRP_ADDCOURSE" == formDynType
      || "GRP_ADDWORK" == formDynType || "PSN_SHAREFILE" == formDynType) {
    scmpublictoast(smateShare.i18n_filenotshareDyn, 2000);
    return 0;
  }
  if(resType==26){
    pubId = $obj.attr("newsId");
  }
  if (pdwhpubShare == "true") {
    var dynType = "B2TEMP";
    var operatorType = 3;
    var isB2T = true;
  }
  if (dynText != null && dynText != "") {
    dynType = "ATEMP";
    operatorType = -1;
    isB2T = false;
  }
  if (des3ResId == null) {
    resType = 0;
  }
  var resInfoId = $("#share_to_scm_box").attr("resInfoId");
  var resInfoJson = "";
  if (resInfoId != null && resInfoId != "" && typeof (resInfoId) != "undefined") {
    if (resType == "fund") {
      resInfoJson = JSON.stringify({
        "fund_desc_zh" : $("#zhShowDesc_" + resInfoId).val(),
        "fund_desc_en" : $("#enShowDesc_" + resInfoId).val(),
        "fund_title_zh" : $("#zhTitle_" + resInfoId).val(),
        "fund_title_en" : $("#enTitle_" + resInfoId).val(),
        "fund_logo_url" : $("#share_to_scm_box").attr("logoUrl")
      });
    }
  }

  switch (resType) {
    case "pub" :
      resType = "1";
    break;
    case "fund" :
      resType = "11";
    break;
    case "agency" :
      resType = SmateShare.resTypeId.agency;
    break;
    case "pdwhpub" :
      resType = "22";
    break;
    case "prj" :
      resType = "4";
    break;
    default :
    break;
  }

  var data = {
    "dynType" : dynType,
    "dynText" : dynText,
    "resType" : resType,
    "des3PubId" : des3ResId,
    "operatorType" : operatorType,
    "dbId" : dbId,
    "des3ResId" : des3ResId,
    "resInfoJson" : resInfoJson
  };
  $.ajax({
    url : "/dynweb/dynamic/ajaxrealtime",
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      if ("fail" == data.result) {// 超时是，回显的 fail
        scmpublictoast(smateShare.i18n_shareFail, 2000);
      } else {
        // 需求变更,移动端跳入分享页面进行分享,分享后应该重定向到动态首页,数据会自动刷新
        if (typeof shareType != typeof undefined && shareType == "mobile") {
          SmateShare.ajaxTimeOut(data, function() {
            scmpublictoast("分享成功", 2000);
            setTimeout(function() {
              // mobile.pub.initPubOptStatistics(des3ResId, shareType.toUpperCase());
              window.location.href = $("#historyPage").val();
            }, 2000);
          });
        } else {
          SmateShare.ajaxTimeOut(data, function() {
            scmpublictoast(smateShare.i18n_shareSuccess, 1000);
            if (typeof shareCallback == "function") {
              if (formDynType == "PSN_SHAREFUND") {
                shareCallback($obj.attr("dynId"), dynText, des3ResId, resInfoId, undefined, null);
              } else if (formDynType == "PSN_SHARE_AGENCY") {
                shareAgencyDynCallback(data, des3ResId, dynText);
              } else {
                shareCallback($obj.attr("dynId"), dynText, des3ResId, pubId, isB2T, null, resType, dbId);
              }
            }
          });
          // 个人主页项目列表分享更新
          var countStr = $("span[value='" + des3ResId + "']").text();
          if (countStr != undefined) {
            var newCount = parseInt(countStr.replace("(", "").replace(")", "")) + 1;
            $("span[value='" + des3ResId + "']").text("(" + newCount + ")");
          }
        }
      }
    }
  });
}

// 动态分享基金回调
SmateShare.shareAgencyDynCallback = function(data, des3ResId, comments) {
  $.ajax({
    url : "/prjweb/share/ajaxupdate",
    type : "post",
    data : {
      "Des3FundAgencyId" : des3ResId,
      "shareToPlatform" : 1,
      "comments" : comments
    },
    dataType : "json",
    success : function(data) {
      if (data.result = "success") {
        var shareCount = data.shareCount;
        var domItem = $("div[des3Id='" + des3ResId + "']:first");
        PCAgency.initShareOpt(domItem, shareCount);
      }
    },
    error : function() {
    }
  });
}

SmateShare.shareFileToFriend = function(receivers, textConent, dataStr, zh_title, en_title, des3PubId, grpFile) {
  var $obj = $("#share_to_scm_box");
  var des3ResId = $obj.attr("resid");
  if (des3ResId == null) {
    des3ResId = $obj.attr("des3ResId");
  }
  var resId = $("#share_to_scm_box").attr("archivefileId");
  var resDetails = [];
  resDetails.push({
    'resId' : resId,
    'resNode' : 1
  });
  var jsonParam = {
    "resType" : 3,
    "resDetails" : resDetails
  };
  jsonParam["shareTitle"] = zh_title;
  jsonParam["shareEnTitle"] = en_title;
  jsonParam["isAddShareTimes"] = true;
  var post_data = {
    "receivers" : receivers,
    "des3fundIdArray" : des3PubId,
    "nodeId" : 1,
    "shareDeadline" : dataStr,
    "resType" : 3,
    "content" : textConent,
    "grpFile" : grpFile,
    "jsonParam" : JSON.stringify(jsonParam)
  };
  $.ajax({
    url : ctxpath + "/commend/file",
    type : "post",
    dataType : "json",
    data : post_data,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.success = "success") {
          scmpublictoast(smateShare.i18n_shareSuccess, 1000);
          if (typeof sharePsnCallback == "function") {
            sharePsnCallback($obj.attr("dynId"), $("#id_sharegrp_textarea").val(), des3ResId);
          }

        } else {
          scmpublictoast(smateShare.i18n_optFail, 1000);
        }
      });
    }
  });
}
// 分享到联系人
SmateShare.shareToFriend = function() {
  var $obj = $("#share_to_scm_box");
  var dyntype = $obj.attr("dyntype");
  var des3ResId = $obj.attr("des3ResId");
  var textConent = $.trim($("#id_sharegrp_textarea").val());
  var dynId = $obj.attr("des3dynid");
  dynId = dynId ? dynId : $obj.attr("dynid");
  var des3PubId = $obj.attr("resid");
  if (des3PubId == null) {
    des3PubId = $obj.attr("des3ResId");
  }
  var smateInsideLetterType = "text";
  var resType = $obj.attr("resType");
  // 个人文件分享到联系人
  if ("PSN_SHAREFILE" == dyntype) {
    SmateShare.sharePsnFileToFriend($obj);
    return;
  }
  // 分享基金
  if ("PSN_SHAREFUND" == dyntype || "GRP_SHAREFUND" == dyntype || resType == "fund") {
    SmateShare.shareFundToFriend();
    return;
  }
  // 分享资助机构
  if ("PSN_SHARE_AGENCY" == dyntype || "GRP_SHARE_AGENCY" == dyntype || resType == "agency") {
    SmateShare.shareAgencyToFriend();
    return;
  }

  // 分享群组文件
  if ("GRP_ADDFILE" == dyntype || "GRP_SHAREFILE" == dyntype || "GRP_ADDCOURSE" == dyntype || "GRP_ADDWORK" == dyntype) {
    Grp.shareGrpFiles();
    return 0;
  }
  des3PubId = $obj.attr("des3ResId");
  switch (resType) {
    case "1" :
    case "2" :
    case "pub" :
      smateInsideLetterType = "pub";
    break;
    case "11" :
      smateInsideLetterType = "fund";
      SmateShare.shareFundToFriend();
      return;
    break;
    case SmateShare.resTypeId.agency :
      smateInsideLetterType = "agency";
      SmateShare.shareAgencyToFriend();
      return;
    break;
    case "22" :
    case "24" :
    case "pdwhpub" :
      smateInsideLetterType = "pdwhpub";
    break;
    case "4" :
    case "prj" :
      smateInsideLetterType = "prj";
      SmateShare.sharePrjToPsn(des3ResId);
      shareCallback(dynId, textConent);
      return;
    break;
    case "26" :
        SmateShare.shareNewsToPsn(des3ResId);
        return;
        break;
    case "6":
      SmateShare.shareProfileUrlToPsn(des3ResId);
      return;
      break;
    case "29":
      SmateShare.shareInsToPsn(des3ResId);
      return;
      break;
    default :
    break;
  }

  if (des3PubId != null && des3PubId != "") {
    var des3psnIds = "";
    var receiverEmails = "";
    var zh_title = smateShare.i18n_shareto;
    var en_title = "share";
    var code = "";
    $("#grp_friends").find(".chip__text").each(function(i, n) {
      code = $(n).closest(".chip__box").attr("code");
      if (code != null && $.trim(code) != "") {
        des3psnIds += code + ",";
        if (i < 5) {// 截断处理
          zh_title += "<label title='" + $(n).html() + "'>'" + $(n).html() + "'</label>";
          en_title += "<label title='" + $(n).html() + "'>'" + $(n).html() + "'</label>";
          if (i == 4) {
            zh_title += "...";
            en_title += "...";
          }
        }
      }
      // 邮件
      if (SmateShare.isEmail2($(this).html())) {
        receiverEmails += $(this).html() + ",";
      }
    });

    var mydate = new Date();
    var dataStr = (Number(mydate.getFullYear()) + 2) + "/" + (Number(mydate.getMonth()) + 1) + "/" + mydate.getDate();
    if ((des3psnIds != null && $.trim(des3psnIds) != "") || $.trim(receiverEmails) != "") {
      if ("GRP_ADDFILE" == dyntype || "GRP_SHAREFILE" == dyntype || "GRP_ADDCOURSE" == dyntype
          || "GRP_ADDWORK" == dyntype) {
        var grpFile = "grpFile";
        if ($.trim(des3psnIds) != "") {
          SmateShare.shareFileToFriend(des3psnIds, textConent, dataStr, zh_title, en_title, des3PubId, grpFile);
        }
        return 0;
      } else {
        SmateShare.updateSelectedDate(des3psnIds);
        if ($.trim(des3psnIds) != "" || $.trim(receiverEmails) != "") {
          SmateShare.sendPubShareToFriendMsg(des3psnIds, textConent, des3PubId, smateInsideLetterType, receiverEmails,
              $obj.attr("pubId"), dynId, resType, dyntype);
        }
        var databaseType = 1;
        if (smateInsideLetterType == "pdwhpub") {
          databaseType = 2;
        }
        // 邮件分享成果
        des3psnIds = des3psnIds + receiverEmails;
        // 分享成果邮件迁到新系统
        SmateShare.sendShareMail(des3psnIds, textConent, des3PubId, databaseType);
        // SmateShare.sendShareFriend(des3psnIds,textConent,dataStr,zh_title,en_title,true,des3PubId,databaseType);
      }
    }
  }
}

/**
 * 发送分享给联系人的消息
 */
SmateShare.sendPubShareToFriendMsg = function(des3psnIds, content, des3PubId, smateInsideLetterType, receiverEmails,
    pubId, dynId, resType, dyntype) {
  $.ajax({
    url : "/dynweb/showmsg/ajaxsendpubsharetofriend",
    type : 'post',
    dataType : "json",
    data : {
      "des3ReceiverIds" : des3psnIds,
      "content" : content,
      "des3PubIds" : des3PubId,
      "smateInsideLetterType" : smateInsideLetterType,
      "receiverEmails" : receiverEmails
    },
    success : function(data) {
      if (typeof sharePsnCallback == "function") {
        scmpublictoast(smateShare.i18n_shareSuccess, 1000);
        sharePsnCallback(dynId, content, des3PubId, pubId, null, des3psnIds, resType, dyntype);
      }
    },
    error : function() {
    }
  });
}

SmateShare.updateSelectedDate = function(des3psnIds) {
  $.ajax({
    url : "/groupweb/grpmember/ajaxupdateselecteddate",
    type : 'post',
    dataType : "json",
    data : {
      "targetPsnIds" : des3psnIds
    },
    success : function(data) {
    },
    error : function() {
    }
  });
}
SmateShare.sendShareFriend = function(receivers, shareContent, commendDendLine, shareTitleCN, shareTitleEN,
    isAddShareTimes, des3pubId, databaseType) {
  var $obj = $("#share_to_scm_box");
  var post_data = {
    "receivers" : receivers,
    "des3PubIds" : des3pubId,
    "shareDeadline" : commendDendLine,
    "articleName" : "publication",
    "resType" : 1,
    "dbid" : "",
    "databaseType" : databaseType,
    "content" : shareContent,
    "jsonParam" : JSON.stringify({
      resType : 1,
      shareTitle : shareTitleCN,
      shareEnTitle : shareTitleEN,
      resDetails : [{
        des3ResId : des3pubId,
        resId : 0,
        resNode : 1
      }],
      isAddShareTimes : isAddShareTimes
    })
  };
  $.ajax({
    url : snsctx + "/commend/publication/ajaxCommendPub",
    type : 'post',
    dataType : "json",
    data : post_data,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        scmpublictoast(smateShare.i18n_shareSuccess, 2000);
        // if (typeof sharePsnCallback == "function")
        // sharePsnCallback($obj.attr("dynId"),
        // $("#id_sharegrp_textarea").val(), $obj
        // .attr("resid"), $obj.attr("pubId"),
        // isAddShareTimes);
      });
    },
    error : function() {
    }
  });
}
SmateShare.sendShareMail = function(receivers, shareContent, des3pubId, databaseType) {
  var $obj = $("#share_to_scm_box");
  var post_data = {
    "receivers" : receivers,
    "des3PubIds" : des3pubId,
    "articleName" : "publication",
    "resType" : 1,
    "dbid" : "",
    "databaseType" : databaseType,
    "content" : shareContent,
  };
  $.ajax({
    url : "/pub/ajaxSendSharePubMail",
    type : 'post',
    dataType : "json",
    data : post_data,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        scmpublictoast(smateShare.i18n_shareSuccess, 2000);
        // if (typeof sharePsnCallback == "function")
        // sharePsnCallback($obj.attr("dynId"),
        // $("#id_sharegrp_textarea").val(), $obj
        // .attr("resid"), $obj.attr("pubId"),
        // isAddShareTimes);
      });
    },
    error : function() {
    }
  });
}
SmateShare.shareToGrpUI = function() {
  $("#share_to_scm_box").find("*[selector-id='list_sharetype']").attr("sel-value", "1");
  $("#id_bt_select_friend").hide();
  $("#id_bt_select_grp").show();
}
SmateShare.shareToFriendUI = function() {
  $("#share_to_scm_box").find("*[selector-id='list_sharetype']").attr("sel-value", "3");
  $("#id_bt_select_grp").hide();
  var dyntype = $("#share_to_scm_box").attr("dyntype");

  $("#id_bt_select_grp").hide();
  $("#id_bt_select_friend").show();
  // 检索框聚焦
  $("#grp_addfriend").focus();

}
SmateShare.shareToDynUI = function() {
  $("#share_to_scm_box").find("*[selector-id='list_sharetype']").attr("sel-value", "2");
  $("#id_bt_select_friend").hide();
  $("#id_bt_select_grp").hide();

}
SmateShare.clickGrpName = function(obj) {
  var $div = $("#grp_names").find(".dev_grp_input");
  $div.val($(obj).text());
  $div.attr("code", $(obj).attr("des3GrpId"));
  SmateShare.hideShareToScmSelectGrpBox();
}
SmateShare.resetSelectInput = function() {
  $("#grp_friends").find(".chip__box").remove();
  // 重置存储分享人员的容器
  $("#shareFriendResults").find(".chip__box ").remove();
  $("#grp_names").find(".dev_grp_input").attr("code", "").val("");
  $("#id_sharegrp_textarea").val("");
}
SmateShare.checkParamForFriendName = function(code) {
  var status = true;
  $("#grp_friends").find(".chip__text").each(function(i, o) {
    if (code == $(o).attr("code")) {
      status = false;
      return false;
    }
  });
  return status;
}

SmateShare.clickFriendName = function() {

  // 在remove之前必须重置所有的人员
  SmateShare.resetMaybeShareFriendName();
  // $("#grp_friends").find(".chip__box").remove();
  // 将容器中的元素添加到grp_addfriend
  $("#grp_addfriend").before($("#shareFriendResults").html());
  hideDialog("share_to_scm_select_friend_box");
  // var htmlStr ="";
  // var name="";
  // var des3PsnId="";
  // var avatar_img="";
  // var totalcount =$("#id_grp_add_friend_names_list").attr("totalcount") ;
  // var currentCount =$("#id_grp_add_friend_names_list").attr("currentCount") ;
  // //获取输入邮件的内容
  // var emailHtmlStr="";
  // $("#grp_friends").find(".chip__box ").each(function(){
  // if ( $(this).attr("code")==="" || $(this).attr("code") === undefined ){
  // emailHtmlStr += " <div class='chip__box' code=''>"+
  // "<div class='chip__avatar'></div>"+
  // "<div class='chip__text' >"+ $(this).find(".chip__text").html()+"</div>"+
  // "<div class='chip__icon icon_delete' onclick='SmateShare.cancelSelectEmailFriend(this)'><i
  // class='material-icons'>close</i></div>"+
  // "</div>";
  // }
  // });
  // //获取收入邮件的 end
  //	
  // // totalcount 总查询结果的数量 currentCount 检索之后当前查询结果的数量
  // // 设置这两个变量的意义在于：区别检索与不检索
  // if(totalcount == currentCount){
  // $("#id_grp_add_friend_names_list").find(".psn_chosen").each(function(i,obj){
  // name = $(obj).find(".psn-idx__main_name").html();
  // des3PsnId = $(obj).find(".psn-idx__main_name").attr("code");
  // avatar_img = $(obj).find(".psn-idx__avatar_img").html();
  // htmlStr += " <div class='chip__box' code='"+des3PsnId+"'>"+
  // "<div class='chip__avatar'>"+avatar_img+"</div>"+
  // "<div class='chip__text' >"+name+"</div>"+
  // "<div class='chip__icon icon_delete' onclick='SmateShare.cancelSelectFriend(this)'><i
  // class='material-icons'>close</i></div>"+
  // "</div>";
  // //隐藏推荐的联系人
  // $("#id_bt_select_friend").find(".share-friends__rcmd_chips-container .chip__box
  // ").each(function(){
  // if ( $(this).attr("code")===des3PsnId ){
  // $(this).css("display","none");
  // }
  // });
  // });
  // if ($("#share_to_scm_box").attr("windowType") == "group") {
  // // 在remove之前必须重置所有的人员
  // SmateShare.resetMaybeShareFriendName();
  // }
  // $("#grp_friends").find(".chip__box").remove();
  // $("#grp_addfriend").before(emailHtmlStr+htmlStr);
  // hideDialog("share_to_scm_select_friend_box");
  // } else {
  // // 检索后要进行的操作，选中的操作
  // $("#id_grp_add_friend_names_list")
  // .find(".psn_chosen")
  // .each(
  // function(i, obj) {
  // name = $(obj).find(".psn-idx__main_name").html();
  // des3PsnId = $(obj).find(".psn-idx__main_name")
  // .attr("code");
  // avatar_img = $(obj).find(".psn-idx__avatar_img")
  // .html();
  // var exists = false;
  // $("#grp_friends")
  // .find(".chip__box")
  // .each(
  // function(j) {
  // if ($(this).attr("code") === des3PsnId) {
  // exists = true;
  // } else {
  // }
  // })
  // // 不存在就添加
  // if (!exists) {
  // htmlStr += " <div class='chip__box' code='"
  // + des3PsnId
  // + "'>"
  // + "<div class='chip__avatar'>"
  // + avatar_img
  // + "</div>"
  // + "<div class='chip__text' >"
  // + name
  // + "</div>"
  // + "<div class='chip__icon icon_delete' onclick='SmateShare.cancelSelectFriend(this)'><i
  // class='material-icons'>close</i></div>"
  // + "</div>";
  // // 隐藏推荐的联系人
  // $("#id_bt_select_friend")
  // .find(
  // ".share-friends__rcmd_chips-container .chip__box ")
  // .each(
  // function() {
  // if ($(this).attr("code") === des3PsnId) {
  // $(this).css("display",
  // "none");
  // }
  // });
  // }
  //
  // });
  //
  // // 检索后要进行的操作，没选中的操作 , 把存在的移除
  // $("#id_grp_add_friend_names_list").find(".psn-idx_small").each(function(i,obj){
  //			
  // if( !$(this).hasClass("psn_chosen")){
  // des3PsnId = $(obj).find(".psn-idx__main_name").attr("code");
  // var delChip = $("#grp_friends").find(".chip__box[code='"+des3PsnId+"']") ;
  // if(delChip !=undefined){
  // var delObj = delChip.find(".icon_delete")[0];
  // SmateShare.cancelSelectFriend(delObj) ;
  // }
  //				
  //				
  // }
  // });
  // $("#grp_addfriend").before(emailHtmlStr+htmlStr);
  // hideDialog("share_to_scm_select_friend_box");
  // }

};

// 重置你可能分享的人员
SmateShare.resetMaybeShareFriendName = function() {
  // 将选中的人员复原会推荐的人员
  $("#grp_friends").find(".chip__box ").each(function(index, obj) {
    // 进行逐个重置,是邮箱的话保留
    var shareTo = $.trim($(obj).find(".chip__text").text());
    if (!SmateShare.isEmail2(shareTo)) {
      $(obj).closest(".chip__box").remove();
    }
    // 显示推荐的联系人
    var targetcode = $(obj).closest(".chip__box").attr("code");
    $("#id_bt_select_friend").find(".share-friends__rcmd_chips-container .chip__box ").each(function() {
      if ($(this).attr("code") === targetcode) {
        $(this).css("display", "");
      }
    });
  });
  // 将选中的推荐人员拉下来
  $("#shareFriendResults").find(".chip__box ").each(function(index, obj1) {
    // 存在相同的人员的情况下
    $("#id_bt_select_friend").find(".share-friends__rcmd_chips-container .chip__box ").each(function(index, obj2) {
      if ($(obj1).attr("code") === $(obj2).attr("code")) {
        $(obj2).css("display", "none");
      }
    });
  });

}

// 点击可能要分享的联系人
SmateShare.clickMaybeShareFriendName = function(obj) {
  var htmlStr = "";
  var name = $(obj).find(".chip__text").html();
  var des3PsnId = $(obj).attr("code");
  var avatar_img = $(obj).find(".chip__avatar").html();
  var length = $("#grp_addfriend").parent().find(".chip__box[code='" + des3PsnId + "']").length;
  if (length == 0) {
    htmlStr += " <div class='chip__box' code='"
        + des3PsnId
        + "'>"
        + "<div class='chip__avatar'>"
        + avatar_img
        + "</div>"
        + "<div class='chip__text' >"
        + name
        + "</div>"
        + "<div class='chip__icon icon_delete' onclick='SmateShare.cancelSelectFriend(this)'><i class='material-icons'>close</i></div>"
        + "</div>";
    $("#grp_addfriend").before(htmlStr);
  }
  SmateShare.isExistsSameName(name);
  // 需要判断所选人员是否存在容器之中
  if (SmateShare.isExistsInFriends(des3PsnId)) {
    $("#shareFriendResults").append(htmlStr);
  }
  $(obj).css("display", "none");
};

// 取消选择的联系人
SmateShare.cancelSelectFriend = function(obj) {
  $(obj).closest(".chip__box").remove();
  // 显示推荐的联系人
  var targetcode = $(obj).closest(".chip__box").attr("code");
  $("#id_bt_select_friend").find(".share-friends__rcmd_chips-container .chip__box ").each(function() {
    if ($(this).attr("code") === targetcode) {
      $(this).css("display", "");
    }
  });
  // 在容器中删除
  if (!SmateShare.isExistsInFriends(targetcode)) {
    SmateShare.removeSelectFriend(targetcode);
  }

};

// 取消选择邮件输入的联系人
SmateShare.cancelSelectEmailFriend = function(obj) {
  $(obj).closest(".chip__box").remove();
};
// 通过code取消联系人

SmateShare.cancelSelectFriendByCode = function(targetcode) {
  $("#id_bt_select_friend").find(".share-friends__rcmd_chips-container .chip__box ").each(function() {
    if ($(this).attr("code") === targetcode) {
      $(this).css("display", "");
    }
  });
};
// 通过code联系人
SmateShare.sureSelectFriendByCode = function(targetcode) {
  $("#id_bt_select_friend").find(".share-friends__rcmd_chips-container .chip__box ").each(function() {
    if ($(this).attr("code") === targetcode) {
      $(this).css("display", "none");
    }
  });
};

SmateShare.showFriendCallback = function() {
  $.each($("#id_grp_add_friend_names_list").find(".psn-idx__main_name"), function(i, o) {
    $.each($("#grp_friends").find(".chip__box"), function(j, k) {
      if ($(k).attr("code") == $(o).attr("code")) {
        // 增加已经选中的人员状态
        $(o).closest(".psn-idx_small").addClass("psn_chosen");
        // 将选中的人员增加到选择人员存储容器中
        SmateShare.clickChooiseFriend($(o).closest(".psn-idx_small"),true);
      }
    });

  });
};
SmateShare.shareMain = function() {
  var type = $("#share_to_scm_box").find("*[selector-id='list_sharetype']").attr("sel-value");
  if (1 == type) {// 分享到群组
    SmateShare.shareToGrp();
  } else if (2 == type) {// 分享到动态
    SmateShare.shareToDyn();
  } else if (3 == type) {// 分享到联系人
    SmateShare.shareToFriend();
  } else {

  }
  SmateShare.hideShareToScmBox();
  SmateShare.resetSelectInput();
}
// 需求变更删除快速分享-快速分享的专用
SmateShare.QuickShareContent = "";
SmateShare.getParamForQuickShare = function(obj) {
  var $box_obj = $(obj).closest(".dynamic__box");
  var $paramInput = $box_obj.find("input[name=dynId]:last");
  var des3DynId = $paramInput.attr("des3DynId");
  var dynType = $paramInput.attr("dyntype");
  var des3ResId = $paramInput.attr("des3ResId");
  var resType = $paramInput.attr("resType");

  $("#share_to_scm_box").attr("dyntype", dynType).attr("restype", resType).attr("des3dynid", des3DynId).attr(
      "des3parentdynid", des3DynId).attr("des3resid", des3ResId);
  SmateShare.QuickShareContent = $box_obj.find(".dyn_content").text();

  var title = $box_obj.find(".dynamic-content .pub-idx__main_title").text();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  if (resType != "0") {
    SmateShare.createFileDiv($parent, title, des3ResId);
  }
}
// 分享对话框初始化参数
SmateShare.getParam = function(obj) {
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  var $box_obj = $(obj).closest(".dynamic__box");
  var dyntype = $box_obj.find("input[name=dynId]").attr("dyntype");
  if (dyntype == undefined) {
    // 个人动态，可能为空。
    dyntype = $box_obj.find("input[name=dynId]:last").attr("dyntype");
    console.log(dyntype);
  }
  var resType = $(obj).attr("resType");
  var des3ResId = $box_obj.find("input[name=dynId]").attr("des3ResId");
  var resid = $box_obj.find("input[name=dynId]").attr("resid");
  var databaseType = $(obj).attr("databaseType");
  var dbId = $(obj).attr("dbId");
  var text_content = $box_obj.find(".dyn_content").text();
  var resgrpid = $box_obj.find("input[name=dynId]").attr("resgrpid");
  var dynId = $box_obj.find("input[name=dynId]").attr("value");
  $("#share_to_scm_box").attr("dyntype", dyntype).attr("resType", resType).attr("des3ResId", des3ResId).attr("resid",
      resid).attr("text_content", text_content).attr("resgrpid", resgrpid).attr("dynId", dynId).attr("dbId", dbId);
  if ("GRP_ADDFILE" == dyntype || "GRP_SHAREFILE" == dyntype || "GRP_ADDCOURSE" == dyntype || "GRP_ADDWORK" == dyntype) {
    var content = $box_obj.find(".pub-idx__main_title").html();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);

  } else if ("GRP_ADDPUB" == dyntype || "GRP_PUBLISHDYN" == dyntype || "GRP_SHAREPUB" == dyntype
      || "GRP_SHAREPDWHPUB" == dyntype) {
    if (restype == "1") {
      restype = "pub";
    } else if (restype == "22") {
      restype = "pdwhpub";
    }
    $("#share_to_scm_box").attr("resType", resType);
    var content = $box_obj.find(".pub-idx__main_title").text();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);

  } else if ("GRP_SHAREFUND" == dyntype) {
    if (restype == "11") {
      restype = "fund";
    }
    var resInfoId = $(obj).attr("resInfoId");
    var fundId = $(obj).attr("notEncodeId");
    $("#share_to_scm_box").attr("resInfoId", resInfoId).attr("fundId", fundId).attr("resType", resType);
    var content = $box_obj.find(".pub-idx__main_title").text();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);
  } else if ("ATEMP" == dyntype || "B1TEMP" == dyntype || "B2TEMP" == dyntype || "B3TEMP" == dyntype) {
    // 个人动态
    var restype = $(obj).attr("restype");
    if (restype == "pub") {
      restype = "1";
    } else if (restype == "pdwhpub") {
      restype = "22";
    } else if (restype == "fund") {
      restype = "11";
    }
    var resid = $(obj).attr("resid");
    var des3Resid = $(obj).attr("des3Resid");
    var des3PubId = $(obj).attr("des3PubId");
    var dyntype = $(obj).attr("dyntype");
    var dynid = $(obj).attr("dynid");
    var parentdynid = $(obj).attr("parentdynid");
    $("#share_to_scm_box").attr("dyntype", dyntype).attr("restype", restype).attr("resid", resid).attr("dyntype",
        dyntype).attr("dynid", dynid).attr("parentdynid", parentdynid).attr("des3Resid", des3Resid).attr("des3PubId",
        des3PubId);

    var content = $box_obj.find(".pub-idx__main_title").text();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);
  }
  $box_obj.find(".dynamic_content_container").html();
};

SmateShare.getPubListSareParam = function(obj) {
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  var $box_obj = $(obj).closest(".pub_info_box");
  var des3ResId = $box_obj.attr("des3ResId");
  if (des3ResId == null) {
    des3ResId = $(obj).attr("des3ResId");
  }
  var pubId = $(obj).attr("pubId");
  var resType = $(obj).attr("resType");
  $("#share_to_scm_box").attr("dyntype", "GRP_SHAREPUB").attr("des3ResId", des3ResId).attr("pubId", pubId).attr(
      "resType", resType).attr("windowType", "group");
  // 初始化群组分享框
  SmateShare.groupWindowSetting();
  var content = $box_obj.find(".pub_info_title").text();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);

}
// 群组文件
;
SmateShare.getFileListSareParam = function(obj) {
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  var $box_obj = $(obj).closest(".main-list__item");
  var resid = $box_obj.attr("fileid");
  var des3ResId = $box_obj.attr("fileid");
  var resgrpid = $box_obj.attr("grpId");
  var des3GrpId = $("#grp_params").attr("smate_des3_grp_id");
  var archivefileid = $box_obj.attr("archivefileid");
  $("#share_to_scm_box").attr("dyntype", "GRP_SHAREFILE").attr("resid", resid).attr("resgrpid", resgrpid).attr(
      "archivefileid", archivefileid).attr("des3ResId", des3ResId).attr("des3GrpId", des3GrpId).attr("windowType",
      "group");

  // 初始化群组分享框
  SmateShare.groupWindowSetting();
  var content = $box_obj.find(".file-idx__main_title").html();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);

}

// 个人成果列表,点击分享，把成果信息加载到分享界面
SmateShare.getPsnPubListSareParam = function(obj) {
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  var des3ResId = $(obj).attr("des3ResId");

  var windowType = $(obj).attr("windowType");
  if (des3ResId == null || des3ResId == "") {
    des3ResId = $(obj).attr("resId");
  }
  var pubId = $(obj).attr("pubId");
  if (windowType == "pub") {
    $("#share_to_scm_box").attr("dyntype", "GRP_SHAREPUB").attr("des3ResId", des3ResId).attr("pubId", pubId).attr(
        "resType", "1").attr("windowType", "pub");
  } else {
    $("#share_to_scm_box").attr("dyntype", "GRP_SHAREPUB").attr("des3ResId", des3ResId).attr("pubId", pubId).attr(
        "resType", "1").attr("windowType", "group");
  }
  var objGetInfo = $(obj).closest(".dev_pub_list_div");
  var content = objGetInfo.find(".dev_pub_title").text();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  SmateShare.groupWindowSetting();
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);

};
// 成果详情,点击分享，把成果信息加载到分享界面
SmateShare.getPdwhPubSareParam = function(obj) {
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  var des3ResId = $(obj).attr("des3ResId");
  var dbId = $(obj).attr("dbId");
  $("#share_to_scm_box").attr("dyntype", "ATEMP").attr("des3ResId", des3ResId).attr("pubId", pubId).attr("resType",
      "22").attr("dbId", dbId);
  var content = $(".dev_pubdetails_title").html();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);
};
// 成果详情,点击分享，把成果信息加载到分享界面
SmateShare.getPubDetailsSareParam = function(obj) {
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  var des3ResId = $(obj).attr("des3ResId");
  if (des3ResId == null) {
    des3ResId = $(obj).attr("resId");
  }
  var pubId = $(obj).attr("pubId");
  var databaseType = $(obj).attr("databaseType");
  var dbId = $(obj).attr("dbId");
  var resType = $(obj).attr("resType");
  var pdwhpubShare = $(obj).attr("pdwhpubShare");
  $("#share_to_scm_box").attr("dyntype", "GRP_SHAREPUB").attr("des3ResId", des3ResId).attr("pubId", pubId).attr(
      "resType", resType).attr("dbId", dbId).attr("pdwhpubShare", pdwhpubShare);
  var content = $(".dev_pubdetails_title").html();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);
};

// 个人文件,初始化信息
SmateShare.getPsnFileListSareParam = function(obj) {
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  var $box_obj = $(obj).closest(".main-list__item");
  var des3ResId = $box_obj.attr("stationFileId");
  var resid = $box_obj.attr("stationFileId");
  $("#share_to_scm_box").attr("dyntype", "PSN_SHAREFILE").attr("resid", resid).attr("des3ResId", des3ResId);

  var content = $box_obj.find(".file-idx__main_title").html();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);

};
// 构建一个文件div，append到父类中
SmateShare.createFileDiv = function($parent, content, des3ResId) {
  var html = '<div class="share-attachments__item" des3ResId='
      + des3ResId
      + ' >'
      + '<div class="share-attachments__file-name">'
      + content
      + '</div> '
      + '<div class="share-attachments__cancel"  onclick="SmateShare.deleteSelectPsnFile(this);"> <i class="material-icons">close</i></div> '
      + '</div> ';
  $parent.append(html);
};
// 删除选中的文件
SmateShare.deleteSelectPsnFile = function(obj) {
  var len = $(obj).closest(".share-attachmemts__list").find(".share-attachments__item").length;
  if (len === 1) {
    // 没有文件
    SmateShare.hideShareToScmBox();
  }
  $(obj).closest(".share-attachments__item").remove();
};
// 个人文件批量分享， 初始化信息
SmateShare.getPsnFileListBatchSareParam = function() {
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  $("#share_to_scm_box").attr("dyntype", "PSN_SHAREFILE");
  var fileIds = "";
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  $(".main-list__list[list-main='myfile_list']").find(".main-list__item").each(function() {
    if ($(this).find(":checkbox")[0].checked == true) {
      $(this).attr("drawer-id");
      fileIds += $(this).attr("drawer-id") + ",";
      var content = $(this).find(".file-idx__main_title").html();
      var resid = $(this).attr("drawer-id");
      SmateShare.createFileDiv($parent, content, resid);
    }

  });
  if (fileIds === "") {
    scmpublictoast(smateShare.i18n_fileEmpty, 2000);
    return;
  }
  initSharePlugin();
  return;
};
// 个人文件批量框----批量分享分享， 初始化信息
SmateShare.getPsnFileListShareBoxBatchSareParam = function() {
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  $("#share_to_scm_box").attr("dyntype", "PSN_SHAREFILE");
  var fileIds = "";
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  $(".drawer-batch__box[list-drawer='myfile_list']").find(".drawer-batch__content .main-list__item").each(function() {
    fileIds += $(this).attr("drawer-id") + ",";
    var content = $(this).find(".file-idx__main_title").html();
    var resid = $(this).attr("drawer-id");
    SmateShare.createFileDiv($parent, content, resid);
  });
  if (fileIds === "") {
    scmpublictoast(smateShare.i18n_fileEmpty, 2000);
    return;
  }
  initSharePlugin();
  return;
};

// 分享个人文件到联系人
SmateShare.sharePsnFileToFriend = function($obj) {

  var fileIds = "";
  $("#share_to_scm_box").find(".dev_dialogs_share_file_module .share-attachmemts__list .share-attachments__item").each(
      function() {
        if ($(this).attr("resid") != null) {
          fileIds += $(this).attr("resid") + ",";
        } else {
          fileIds += $(this).attr("des3ResId") + ",";
        }
      });
  var fileIdArray = fileIds.split(",");
  var des3psnIds = "";
  var receiverEmails = "";
  var code = "";
  var textConent = $.trim($("#id_sharegrp_textarea").val());
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
  $(".share-attachmemts__list").find(".share-attachments__item .share-attachments__file-name").each(function(i, n) {
    fileNames += $(n).text() + ",";
  });
  fileNames = fileNames.substring(0, fileNames.length - 1);
  var des3PsnIdArray = des3psnIds.split(",");
  // 获取分享主表的主键id
  /*
   * $.ajax( { url :"/psnweb/myfile/ajaxgetsharebaseid" , type : "post", dataType : "json", data :
   * {}, success : function(data) { SmateShare.ajaxTimeOut(data,function(){ if(data.result ===
   * "success"){ var shareBaseId = data.shareBaseId ; for(var i = 0 ; i<des3PsnIdArray.length ;
   * i++){ for(var j=0 ; j<fileIdArray.length ; j++){ if($.trim(des3PsnIdArray[i]) !="" &&
   * $.trim(fileIdArray[j]) != "" ){ //console.log(des3PsnIdArray[i] +"=="+fileIdArray[j] +"=="+
   * textConent +"=="+shareBaseId) SmateShare.sendSharePsnFileMsg(des3PsnIdArray[i] ,fileIdArray[j] ,
   * textConent ,shareBaseId) ; } } }
   * 
   * }else{ scmpublictoast("获取分享主表的主键id 异常！！！",1000); } }); } });
   */

  // 改成批量分享
  // 获取参数
  var post_data = {
    des3ReceiverIds : des3psnIds,
    receiverEmails : receiverEmails,
    des3FileIds : fileIds,
    textContent : textConent,
    fileNames : fileNames
  };

  $.ajax({
    url : "/psnweb/myfile/ajaxsharemyfiles",
    type : "post",
    dataType : "json",
    data : post_data,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result === "nss") {
          scmpublictoast(smateShare.i18n_fileNotShareSelf, 1000);
        }
        // if(data.result === "sbnss"){
        // scmpublictoast("其他联系人分享成功，文件不能分享至自己！", 1000);
        // }
        if (data.result === "success") {
          scmpublictoast(smateShare.i18n_shareSuccess, 1000);
        }
        if (data.result === "error") {
          scmpublictoast(smateShare.i18n_shareFail, 1000);
        }
      });
    }
  });

};

// 分享个人文件的站内信
SmateShare.sendSharePsnFileMsg = function(des3psnId, fileId, textConent, shareBaseId) {
  var post_data = {
    msgType : 7,
    receiverIds : des3psnId,
    fileId : fileId,
    smateInsideLetterType : "file",
    content : textConent,
    belongPerson : true
  };

  $.ajax({
    url : "/dynweb/showmsg/ajaxsendmsg",
    type : "post",
    dataType : "json",
    data : post_data,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result === "success") {
          SmateShare.saveSharePsnFileInfo(des3psnId, fileId, data.msgRelationId, shareBaseId);
        } else {
          scmpublictoast(smateShare.i18n_shareFail, 1000);
        }
      });
    }
  });

}
/**
 * 保存分享个人文件的信息 des3psnIds=接收人ids fileIds=文件主键ids
 */
SmateShare.saveSharePsnFileInfo = function(des3psnId, fileId, msgRelationId, shareBaseId) {
  var post_data = {
    des3ReceiverId : des3psnId,
    fileId : fileId,
    msgRelationId : msgRelationId,
    shareBaseId : shareBaseId
  };
  $.ajax({
    url : "/psnweb/myfile/ajaxsharemyfiles",
    type : "post",
    dataType : "json",
    data : post_data,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result === "success") {
          scmpublictoast(smateShare.i18n_shareSuccess, 1000);
        } else {
          scmpublictoast(smateShare.i18n_shareFail, 1000);
        }
      });
    }
  });
}
//分享机构
SmateShare.shareInsParams = function(obj) {
  var des3ResId = $(obj).attr("des3ResId");
  if (des3ResId == null) {
    des3ResId = $(obj).attr("resId");
  }
  var insId = $(obj).attr("insId");
  var insPic = $(obj).attr("insPic");
  var insName = $(obj).attr("insName");
  var insUrl = $(obj).attr("insUrl");
  $("#share_to_scm_box").attr("dyntype", "PSN_SHAREINS").attr("resType", "29").attr("des3ResId", des3ResId).attr(
      "insPic", insPic).attr("insName", insName).attr("insUrl", insUrl).attr("insId", insId);
  var content  = $(obj).prev().val();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);
}
//分享个人主页
SmateShare.shareprofile = function(obj) {
  var des3ResId = $(obj).attr("des3ResId");
  if (des3ResId == null) {
    des3ResId = $(obj).attr("resId");
  }
  var psnId = $(obj).attr("psnId");
  $("#share_to_scm_box").attr("dyntype", "PSN_SHAREPSN").attr("resType", "6").attr("des3ResId", des3ResId).attr(
      "resInfoId", psnId).attr("psnId", psnId);

  var content = $("#span_shorturl").html();
  if(content==null || content == undefined){
    content = $("#psn_shorturl").val();
  }
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);
}
//分享新闻
SmateShare.shareRecommendNews = function(obj) {

    if($(obj).attr("publish") == "false"){
        smate.showTips._showNewTips(smateShare.i18n_pub_share_content, smateShare.i18n_tipTitle, "SmateShare.closeNewsTip()", "",
            smateShare.i18n_choose_close_btn, smateShare.i18n_choose_cancel_btn);
        return false;
    }
    //初始化插件
    initSharePlugin();
  var des3ResId = $(obj).attr("des3ResId");
  if (des3ResId == null) {
    des3ResId = $(obj).attr("resId");
  }
  var newsId = $(obj).attr("newsId");
  $("#share_to_scm_box").attr("dyntype", "PSN_SHARENEWS").attr("resType", "26").attr("des3ResId", des3ResId).attr(
      "resInfoId", newsId).attr("newsId", newsId);

  var content = $("#news_title_" + newsId).html();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);
}
SmateShare.closeNewsTip = function() {
    var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
    if($(".new-searchplugin_container")){
        $(".background-cover").remove();
        $(".new-searchplugin_container").remove();
     }
}

// 分享基金
SmateShare.shareRecommendFund = function(obj) {
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  var des3ResId = $(obj).attr("des3ResId");
  if (des3ResId == null) {
    des3ResId = $(obj).attr("resId");
  }
  var fundId = $(obj).attr("fundId");
  var logoUrl = $(obj).attr("logoUrl");
  var fundDesc = $(obj).attr("fundDesc");
  $("#share_to_scm_box").attr("dyntype", "PSN_SHAREFUND").attr("resType", "11").attr("des3ResId", des3ResId).attr(
      "resInfoId", fundId).attr("fundId", fundId).attr("logoUrl", logoUrl).attr("fundDesc", fundDesc);

  var content = $("#fund_title_" + fundId).html();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);
}

// 分享资助机构
SmateShare.shareFundAgency = function(obj) {
  // fundId--->agencyId, funcDesc--->agencyDesc, fund_title_---->agency_title_
  var des3ResId = $(obj).attr("des3ResId");
  if (des3ResId == null) {
    des3ResId = $(obj).attr("resId");
  }
  var agencyId = $(obj).attr("agencyId");
  var logoUrl = $(obj).attr("logoUrl");
  var agencyDesc = $(obj).attr("agencyDesc");
  $("#share_to_scm_box").attr("dyntype", "PSN_SHARE_AGENCY").attr("resType", SmateShare.resTypeId.agency).attr(
      "des3ResId", des3ResId).attr("agencyId", agencyId).attr("logoUrl", logoUrl).attr("agencyDesc", agencyDesc);

  var content = $("#agency_title_" + agencyId).html();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);
}

SmateShare.sharePrjToPsn = function(des3ResId) {
  // 接收者的数量，用于后面对分享数量进行增加（分享给几个人就增加几次）
  var receiverNum = 0;
  var receiverIds = "";
  var receiverEmails = "";
  $("#grp_friends").find(".chip__text").each(function(i, n) {
    code = $(n).closest(".chip__box").attr("code");
    if (code != null && $.trim(code) != "") {
      receiverIds += code + ",";
      receiverNum = receiverNum + 1;
    }// 邮件
    else if (SmateShare.isEmail2($(this).html())) {
      receiverEmails += $(this).html() + ",";
    }
  });
  var paramData = {
    "msgType" : 7,
    "smateInsideLetterType" : "prj",
    "receiverIds" : receiverIds,
    "receiverEmails" : receiverEmails,
    "des3PrjId" : des3ResId
  };
  var textConent = $.trim($("#id_sharegrp_textarea").val());
  $.ajax({
    url : "/dynweb/showmsg/ajaxsendmsg",
    type : "post",
    dataType : "json",
    data : paramData,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.status === "success") {
          // 分享成功后修改页面上的分享数量（项目详情有回调方法了）
          /*
           * var $div = $('.dev_prj_share'); var count =
           * Number($div.text().replace(/[\D]/ig,""))+receiverNum;
           * $div.text($div.attr("sharetitle")+"("+count+")");
           */
          // 个人主页项目列表分享更新
          var countStr = $("span[value='" + des3ResId + "']").text();
          if (countStr != undefined) {
            var newCount = parseInt(countStr.replace("(", "").replace(")", "")) + 1;
            $("span[value='" + des3ResId + "']").text("(" + newCount + ")");
          }
          // 分享的内容
          if (textConent != "") {
            paramData = {
              "msgType" : 7,
              "smateInsideLetterType" : "text",
              "content" : textConent,
              "receiverIds" : receiverIds,
              "receiverEmails" : receiverEmails
            };
            $.ajax({
              url : "/dynweb/showmsg/ajaxsendmsg",
              type : "post",
              dataType : "json",
              data : paramData,
              success : function(data) {
                scmpublictoast(smateShare.i18n_shareSuccess, 1000);
              }
            });
          } else {
            scmpublictoast(smateShare.i18n_shareSuccess, 1000);
          }
        } else if(data.status === "noPermit"){
          var countStr = $("span[value='" + des3ResId + "']").text();
          if (countStr != undefined) {
            var newCount = parseInt(countStr.replace("(", "").replace(")", "")) + 1;
            $("span[value='" + des3ResId + "']").text("(" + newCount + ")");
          }
          scmpublictoast(smateShare.i18n_shareSuccess, 1000);
        }else {
          scmpublictoast(smateShare.i18n_shareFail, 1000);
        }
      });
    }
  });
}

/**
 * 分享个人主页
 * @param des3ResId
 */
SmateShare.shareProfileUrlToPsn = function(des3ResId) {
    // 接收者的数量，用于后面对分享数量进行增加（分享给几个人就增加几次）
    var receiverIds = "";
    var receiverEmails = "";
    $("#grp_friends").find(".chip__text").each(function(i, n) {
        code = $(n).closest(".chip__box").attr("code");
        if (code != null && $.trim(code) != "") {
            receiverIds += code + ",";
        }// 邮件
        else if (SmateShare.isEmail2($(this).html())) {
            receiverEmails += $(this).html() + ",";
        }
    });
    var textConent = $.trim($("#id_sharegrp_textarea").val());
    var paramData = {
        "msgType" : 7,
        "smateInsideLetterType" : "psnUrl",
        "receiverIds" : receiverIds,
        "receiverEmails" : receiverEmails,
        "des3SharePsnId" : des3ResId,
        "msg" : textConent,
    };
    $.ajax({
        url : "/dynweb/showmsg/ajaxsendmsg",
        type : "post",
        dataType : "json",
        data : paramData,
        success : function(data) {
            SmateShare.ajaxTimeOut(data, function() {
                if (data.status === "success") {
                    // 分享的内容
                    if (textConent != "") {
                        paramData = {
                            "msgType" : 7,
                            "smateInsideLetterType" : "text",
                            "content" : textConent,
                            "receiverIds" : receiverIds,
                            "receiverEmails" : receiverEmails
                        };
                        $.ajax({
                            url : "/dynweb/showmsg/ajaxsendmsg",
                            type : "post",
                            dataType : "json",
                            data : paramData,
                            success : function(data) {
                                scmpublictoast(smateShare.i18n_shareSuccess, 1000);
                            }
                        });
                    } else {
                        scmpublictoast(smateShare.i18n_shareSuccess, 1000);
                    }
                }else if(data.status === "noPermit"){
                  scmpublictoast(smateShare.i18n_shareSuccess, 1000);
                } else {
                    scmpublictoast(smateShare.i18n_shareFail, 1000);
                }
            });
        }
    });
}

/**
 * 分享新闻
 * @param des3ResId
 */
SmateShare.shareNewsToPsn = function(des3ResId) {
    // 接收者的数量，用于后面对分享数量进行增加（分享给几个人就增加几次）
    var receiverNum = 0;
    var receiverIds = "";
    var receiverEmails = "";
    $("#grp_friends").find(".chip__text").each(function(i, n) {
        code = $(n).closest(".chip__box").attr("code");
        if (code != null && $.trim(code) != "") {
            receiverIds += code + ",";
            receiverNum = receiverNum + 1;
        }// 邮件
        else if (SmateShare.isEmail2($(this).html())) {
            receiverEmails += $(this).html() + ",";
        }
    });
    var textConent = $.trim($("#id_sharegrp_textarea").val());
    var paramData = {
        "msgType" : 7,
        "smateInsideLetterType" : "news",
        "receiverIds" : receiverIds,
        "receiverEmails" : receiverEmails,
        "des3NewsId" : des3ResId,
        "msg" : textConent,
    };
    $.ajax({
        url : "/dynweb/showmsg/ajaxsendmsg",
        type : "post",
        dataType : "json",
        data : paramData,
        success : function(data) {
            SmateShare.ajaxTimeOut(data, function() {
                if (data.status === "success") {
                    dealShareCount(receiverNum); //处理分享统计数
                    // 分享的内容
                    if (textConent != "") {
                        paramData = {
                            "msgType" : 7,
                            "smateInsideLetterType" : "text",
                            "content" : textConent,
                            "receiverIds" : receiverIds,
                            "receiverEmails" : receiverEmails
                        };
                        $.ajax({
                            url : "/dynweb/showmsg/ajaxsendmsg",
                            type : "post",
                            dataType : "json",
                            data : paramData,
                            success : function(data) {
                                scmpublictoast(smateShare.i18n_shareSuccess, 1000);
                            }
                        });
                    } else {
                        scmpublictoast(smateShare.i18n_shareSuccess, 1000);
                    }
                }else if(data.status === "noPermit"){
                  dealShareCount(1); //处理分享统计数
                  scmpublictoast(smateShare.i18n_shareSuccess, 1000);
                } else {
                    scmpublictoast(smateShare.i18n_shareFail, 1000);
                }
            });
        }
    });
}

/**
 * 分享机构
 * @param des3ResId
 */
SmateShare.shareInsToPsn = function(des3ResId) {
    // 接收者的数量，用于后面对分享数量进行增加（分享给几个人就增加几次）
    var receiverNum = 0;
    var receiverIds = "";
    var receiverEmails = "";
    $("#grp_friends").find(".chip__text").each(function(i, n) {
        code = $(n).closest(".chip__box").attr("code");
        if (code != null && $.trim(code) != "") {
            receiverIds += code + ",";
            receiverNum = receiverNum + 1;
        }// 邮件
        else if (SmateShare.isEmail2($(this).html())) {
            receiverEmails += $(this).html() + ",";
        }
    });
    var textConent = $.trim($("#id_sharegrp_textarea").val());
    var insUrl = $.trim($(".share-attachments__file-name").html());
    
    var paramData = {
        "msgType" : 7,
        "smateInsideLetterType" : "ins",
        "receiverIds" : receiverIds,
        "receiverEmails" : receiverEmails,
        "des3InsId" : des3ResId,
        "content" : insUrl,
    };
    $.ajax({
        url : "/dynweb/showmsg/ajaxsendmsg",
        type : "post",
        dataType : "json",
        data : paramData,
        success : function(data) {
            SmateShare.ajaxTimeOut(data, function() {
                if (data.status === "success") {
                    dealShareCount(receiverNum); //处理分享统计数
                    // 分享的内容
                    if (textConent != "") {
                        paramData = {
                            "msgType" : 7,
                            "smateInsideLetterType" : "text",
                            "content" : textConent,
                            "receiverIds" : receiverIds,
                            "receiverEmails" : receiverEmails
                        };
                        $.ajax({
                            url : "/dynweb/showmsg/ajaxsendmsg",
                            type : "post",
                            dataType : "json",
                            data : paramData,
                            success : function(data) {
                                scmpublictoast(smateShare.i18n_shareSuccess, 1000);
                            }
                        });
                    } else {
                        scmpublictoast(smateShare.i18n_shareSuccess, 1000);
                    }
                }else if(data.status === "noPermit"){
                  dealShareCount(1); //处理分享统计数
                  scmpublictoast(smateShare.i18n_shareSuccess, 1000);
                } else {
                    scmpublictoast(smateShare.i18n_shareFail, 1000);
                }
            });
        }
    });
}
// 分享基金到联系人
SmateShare.shareFundToFriend = function() {
  var fundIds = "";
  $("#share_to_scm_box").find(".dev_dialogs_share_file_module .share-attachmemts__list .share-attachments__item").each(
      function() {
        if ($(this).attr("resid") == null || $(this).attr("resid") == "") {
          fundIds += $(this).attr("des3ResId") + ",";
        } else {
          fundIds += $(this).attr("resid") + ",";
        }

      });
  var dynId = $("#share_to_scm_box").attr("des3dynid");
  dynId = dynId ? dynId : $("#share_to_scm_box").attr("dynid");
  var dyntype = $("#share_to_scm_box").attr("dyntype");
  var resType = "fund";
  var receiverEmails = "";
  var fundIdArray = fundIds.split(",");
  var des3psnIds = "";
  var code = "";
  var textConent = $.trim($("#id_sharegrp_textarea").val());
  $("#grp_friends").find(".chip__text").each(function(i, n) {
    code = $(n).closest(".chip__box").attr("code");
    if (code != null && $.trim(code) != "") {
      des3psnIds += code + ",";
    }// 邮件
    else if (SmateShare.isEmail2($(this).html())) {
      receiverEmails += $(this).html() + ",";
    }
  });
  des3psnIds = des3psnIds + receiverEmails;// 将psnId和邮件结合起来，一起发送到服务器
  var fundNames = "";
  $(".share-attachmemts__list").find("a").each(function(i, n) {
    fundNames += $(n).text() + ",";
  });
  fundNames = fundNames.substring(0, fundNames.length - 1);
  var des3PsnIdArray = des3psnIds.split(",");
  var resInfoId = $("#share_to_scm_box").attr("resInfoId");
  var resInfoJson = "";
  if (resInfoId != null && resInfoId != "" && typeof (resInfoId) != "undefined") {
    resInfoJson = JSON.stringify({
      "fund_desc_zh" : $("#zhShowDesc_" + resInfoId).val(),
      "fund_desc_en" : $("#enShowDesc_" + resInfoId).val(),
      "fund_desc_zh_br" : $("#zhShowDescBr_" + resInfoId).val(),
      "fund_desc_en_br" : $("#enShowDescBr_" + resInfoId).val(),
      "fund_title_zh" : $("#zhTitle_" + resInfoId).val(),
      "fund_title_en" : $("#enTitle_" + resInfoId).val(),
      "fund_logo_url" : $("#share_to_scm_box").attr("logoUrl")
    });
  }
  // 获取参数
  var post_data = {
    "des3ReceiverIds" : des3psnIds,
    "des3FundIds" : fundIds,
    "textContent" : textConent,
    "fundNames" : fundNames,
    "resInfoJson" : resInfoJson
  };

  $.ajax({
    url : "/psnweb/fund/ajaxshare",
    type : "post",
    dataType : "json",
    data : post_data,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result === "success") {
          scmpublictoast(smateShare.i18n_shareSuccess, 1000);
          if (typeof sharePsnCallback == "function") {
            sharePsnCallback(dynId, null, fundIds, resInfoId, undefined, null, resType, dyntype);
          }
        } else {
          scmpublictoast(smateShare.i18n_shareFail, 1000);
        }
      });
    }
  });

};

// 分享资助机构到联系人
SmateShare.shareAgencyToFriend = function() {
  var agencyIds = "";
  var idArr = new Array();
  $("#share_to_scm_box").find(".dev_dialogs_share_file_module .share-attachmemts__list .share-attachments__item").each(
      function() {
        if ($(this).attr("resid") == null || $(this).attr("resid") == "") {
          idArr.push($(this).attr("des3ResId"));
        } else {
          idArr.push($(this).attr("resid"));
        }
      });
  agencyIds = idArr.join(",");
  var dynId = $("#share_to_scm_box").attr("des3dynid");
  dynId = dynId ? dynId : $("#share_to_scm_box").attr("dynid");
  var dyntype = $("#share_to_scm_box").attr("dyntype");
  var resType = "agency";
  var des3psnIds = "";
  var receiverEmails = "";
  var code = "";
  var textConent = $.trim($("#id_sharegrp_textarea").val());
  $("#grp_friends").find(".chip__text").each(function(i, n) {
    code = $(n).closest(".chip__box").attr("code");
    if (code != null && $.trim(code) != "") {
      des3psnIds += code + ",";
    }// 邮件
    else if (SmateShare.isEmail2($(this).html())) {
      receiverEmails += $(this).html() + ",";
    }
  });
  var agencyNames = "";
  $(".share-attachmemts__list").find("a").each(function(i, n) {
    agencyNames += $(n).text() + ",";
  });
  agencyNames = agencyNames.substring(0, agencyNames.length - 1);
  // 获取参数
  var post_data = {
    "receiverMails" : receiverEmails,
    "des3ReceiverIds" : des3psnIds,
    "des3AgencyIds" : agencyIds,
    "comments" : textConent,
    "agencyNames" : agencyNames
  };

  $.ajax({
    url : "/prjweb/agency/ajaxshare",
    type : "post",
    dataType : "json",
    data : post_data,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result === "success") {
          scmpublictoast(smateShare.i18n_shareSuccess, 1000);
          if (typeof shareAgencyFrdCallback == "function") {
            shareAgencyFrdCallback(data, agencyIds,dynId);
          } else {// 在首页动态上的分享后的动态刷新分享数逻辑
            sharePsnCallback (dynId,textConent,agencyIds,undefined,undefined ,null, resType,dyntype);
          }
        } else {
          scmpublictoast(smateShare.i18n_shareFail, 1000);
        }
      });
    }
  });
};

SmateShare.loadFriendListByOrder = function(order) {
  /*
   * if("date" === order){
   * $(obj).closest(".dialogs__header_sort").find(".dialogs__header_sort-box").html(smateShare.i18n_dateOrder);
   * }else{
   * $(obj).closest(".dialogs__header_sort").find(".dialogs__header_sort-box").html(smateShare.i18n_nameOrder); }
   */
  /* SmateShare.selectFriendModuleToHiddenOrder(obj); */
  SmateShare.loadFriendList(order)

};

// 群组分享对话框初始化参数
SmateShare.getGroupParam = function(obj) {
  // 标识为群组界面
  var windowType = "group";
  // 初始化推荐联系人
  // SmateShare.loadMaybeShareFriendList();
  var $box_obj = $(obj).closest(".dynamic__box");
  var dyntype = $box_obj.find("input[name=dynId]").attr("dyntype");
  if (dyntype == undefined) {
    // 个人动态，可能为空。
    dyntype = $box_obj.find("input[name=dynId]:last").attr("dyntype");
    console.log(dyntype);
  }
  var resType = $(obj).attr("resType");
  var des3ResId = $box_obj.find("input[name=dynId]").attr("des3ResId");
  var resid = $box_obj.find("input[name=dynId]").attr("resid");
  if (des3ResId == undefined) {
    des3ResId = resid;
  }
  var databaseType = $(obj).attr("databaseType");
  var dbId = $(obj).attr("dbId");
  var text_content = $box_obj.find(".dyn_content").text();
  var resgrpid = $box_obj.find("input[name=dynId]").attr("resgrpid");
  var dynId = $box_obj.find("input[name=dynId]").attr("value");
  $("#share_to_scm_box").attr("dyntype", dyntype).attr("resType", resType).attr("des3ResId", des3ResId).attr("resid",
      resid).attr("text_content", text_content).attr("resgrpid", resgrpid).attr("dynId", dynId).attr("databaseType",
      databaseType).attr("dbId", dbId).attr("windowType", windowType);
  SmateShare.groupWindowSetting();
  if ("GRP_ADDFILE" == dyntype || "GRP_SHAREFILE" == dyntype || "GRP_ADDCOURSE" == dyntype || "GRP_ADDWORK" == dyntype) {
    var content = $box_obj.find(".pub-idx__main_title").html();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);

  } else if ("GRP_ADDPUB" == dyntype || "GRP_PUBLISHDYN" == dyntype || "GRP_SHAREPUB" == dyntype
      || "GRP_SHAREPDWHPUB" == dyntype) {
    if (restype == "1") {
      restype = "pub";
    } else if (restype == "22") {
      restype = "pdwhpub";
    }
    $("#share_to_scm_box").attr("resType", resType);
    var content = $box_obj.find(".dynamic-content .pub-idx__main_title").text();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);

  } else if ("GRP_SHAREFUND" == dyntype) {
    if (restype == "11") {
      restype = "fund";
    }
    var resInfoId = $(obj).attr("resInfoId");
    var fundId = $(obj).attr("notEncodeId");
    $("#share_to_scm_box").attr("resInfoId", resInfoId).attr("fundId", fundId).attr("resType", resType);
    var content = $box_obj.find(".pub-idx__main_title").text();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);
  } else if ("GRP_SHAREAGENCY" == dyntype) {
    if (restype == "25") {
      restype = "agency";
    }
    var agencyId = $(obj).attr("notEncodeId");
    $("#share_to_scm_box").attr("agencyId", agencyId).attr("resType", resType);
    var content = $box_obj.find(".pub-idx__main_title").text();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);
  } else if ("GRP_SHARENEWS" == dyntype) {
    var newsId = $(obj).attr("notEncodeId");
    $("#share_to_scm_box").attr("newsId", newsId).attr("resType", resType);
    var content = $box_obj.find(".pub-idx__main_title").text();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);
  } else if("GRP_SHAREPRJ"==dyntype){
    var content = $box_obj.find(".pub-idx__main_title").text();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);
  }else if ("ATEMP" == dyntype || "B1TEMP" == dyntype || "B2TEMP" == dyntype || "B3TEMP" == dyntype) {
    // 个人动态
    var restype = $(obj).attr("restype");
    if (restype == "pub") {
      restype = "1";
    } else if (restype == "pdwhpub") {
      restype = "22";
    } else if (restype == "fund") {
      restype = "11";
    } else if (restype == "news") {
      restype = "26";
    }
    var resid = $(obj).attr("resid");
    var des3Resid = $(obj).attr("des3Resid");
    var des3PubId = $(obj).attr("des3PubId");
    var dyntype = $(obj).attr("dyntype");
    var dynid = $(obj).attr("dynid");
    var parentdynid = $(obj).attr("parentdynid");
    $("#share_to_scm_box").attr("dyntype", dyntype).attr("restype", restype).attr("resid", resid).attr("dyntype",
        dyntype).attr("dynid", dynid).attr("parentdynid", parentdynid).attr("des3Resid", des3Resid).attr("des3PubId",
        des3PubId);

    var content = $box_obj.find(".pub-idx__main_title").text();
    var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
    $parent.html("");
    SmateShare.createFileDiv($parent, content, des3ResId);
  }
  $box_obj.find(".dynamic_content_container").html();
};

// 设置群组分享的特殊界面设置
SmateShare.groupWindowSetting = function() {
  if ($("#share_to_scm_box").attr("windowType") == "group") {
    // 显示下拉选择框
    $("#memberChooise").css("display", "none");
    $("#memberTypeSelect").css("display", "block");
    // 改变按钮文字为选择人员
    $("#id_bt_select_friend").find(".dev_select_friend").html(smateShare.i18n_selectMember);
  }

};

// 选中每一个人员触发的事件
// obj 是选中对象 selected是否选中的标识 true选中 false取消选中
SmateShare.clickChooiseFriend = function(obj, selected) {
  var des3PsnId = $(obj).find(".psn-idx__main_name").attr("code");
  if (selected) {
    // 选中元素
    var name = $(obj).find(".psn-idx__main_name").html();
    var avatar_img = $(obj).find(".psn-idx__avatar_img").html();
    // 判断htmlstr是否存在在存储的容器中，存在则不追加
    if (SmateShare.isExistsInFriends(des3PsnId)) {
      htmlStr = " <div class='chip__box' code='"
          + des3PsnId
          + "'>"
          + "<div class='chip__avatar'>"
          + avatar_img
          + "</div>"
          + "<div class='chip__text' >"
          + name
          + "</div>"
          + "<div class='chip__icon icon_delete' onclick='SmateShare.cancelSelectFriend(this)'><i class='material-icons'>close</i></div>"
          + "</div>";
      $("#shareFriendResults").append(htmlStr);
    }
  } else {
    // 不选中元素，判断元素在不在容器中，存在就remove
    if (!SmateShare.isExistsInFriends(des3PsnId)) {
      SmateShare.removeSelectFriend(des3PsnId);
    }
  }
};

// 判断是否存在在指定存储选中人员的容器之中
// 返回ture 说明可以添加 返回false 说明不可以添加
SmateShare.isExistsInFriends = function(des3PsnId) {
  var status = true;
  $("#shareFriendResults").find(".chip__box ").each(function() {
    if ($(this).attr("code") === des3PsnId) {
      // 存在重复的
      status = false;
    }
  });
  // 不存在重复的
  return status;
};
// SCM-18425
SmateShare.isExistsSameName = function(name) {
  $("#grp_friends").find(".chip__box ").each(function() {
    if ($(this).find(".chip__text").text().trim() === name.trim() && $(this).attr("code") === "") {
      // 存在重复的
      this.remove();
    }
  });
};

// 移除选中人员
SmateShare.removeSelectFriend = function(des3PsnId) {
  $("#shareFriendResults").find(".chip__box ").each(function() {
    if ($(this).attr("code") == des3PsnId) {
      // 存在重复的
      $(this).remove();
    }
  });
};
