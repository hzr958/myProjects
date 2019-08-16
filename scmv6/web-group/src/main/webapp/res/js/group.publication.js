var Group = Group ? Group : {};

Group.publication = Group.publication ? Group.publication : {};

/**
 * 检索
 */
Group.publication.doSearch = function() {
  var searchKey = $.trim($("#searchKey").val());
  if (searchKey.length == 0 || searchKey == $("#searchKey").attr("title")) {
    searchKey = "";
  }

  $("#pageNo").val(1);
  $("#searchKey").val(searchKey);
  if ($("#isGroupPsnPubList").val() == "1") {
    $("#order").val("");
    $("#orderBy").val("");
    searchForPsnPubList();
  } else {
    $("#mainForm").submit();
  }
};

/**
 * 全选
 * 
 * @param cbAll
 */
Group.publication.selectAll = function(cbAll) {
  // alert($(cbAll).attr("checked"));
  var checkboxs = $(":checkbox[name=ckpub]");
  if ($(cbAll).attr("checked")) {
    checkboxs.attr("checked", true);
  } else {
    checkboxs.attr("checked", false);
  }
};

/**
 * 评价成果
 */
Group.publication.doPubCommentOp = function() {
  var artType = $("#navAction").val();
  var articleType;
  var articleName;
  var articleTypeName = "";
  if (artType == 'groupPubs') {
    articleTypeName = groupDetailPubs.pub;
    articleType = 1;
    articleName = "publication";
  } else if (artType == 'groupRefs') {
    articleType = 2;
    articleName = "reference";
    articleTypeName = groupDetailPubs.docuemnt;
  } else {
    return;
  }
  var gPubIds = selectedPubIds();
  if (gPubIds == '') {
    $.scmtips.show('warn', groupDetailPubs.select + articleTypeName + ".");
    $.Thickbox.closeWin();
    return false;
  }
  if (gPubIds.indexOf(",") > -1) {
    $.scmtips.show('error', groupDetailPubs.oneNotMary + articleTypeName + groupDetailPubs.evaluation);
    $.Thickbox.closeWin();
    return false;
  }
  var nodeId;
  var pubId;
  var groupId;
  $($("#page_list").children()[0]).find(">tr").each(function() {
    var isChecked = $(this).find("input[name='ckpub']").attr("checked");
    if (isChecked == true || isChecked == 'checked') {
      nodeId = $(this).find(".pub_nodeId_class").val();
      pubId = $(this).find(".pub_pubId_class").val();
      groupId = $(this).find(".pub_groupId_class").val();
    }
  });
  Group.publication.newAjaxDoCommentOp(groupId, nodeId, pubId, articleName);
};

Group.publication.newAjaxDoCommentOp = function(groupId, pubNodeId, pubId, articleName) {
  // 这里需要用$.ajax进行跳转,不然评价时会出现赞功能错误
  var actionType;
  if (articleName == 'publication')
    actionType = 1;
  else
    actionType = 2;
  $.ajax({
    url : ctxpath + "/comment/" + articleName + "/ajaxcommenturl",
    type : "post",
    dataType : "json",
    data : {
      'nodeId' : pubNodeId,
      'pubId' : pubId,
      'groupId' : groupId,
      'actionType' : actionType
    },
    success : function(data) {
      if (data) {
        if (data.result == 'success') {
          url = data.gotourl;
          $("#doComment_btn").attr("alt", url + "&actionType=" + actionType + "&TB_iframe=true&height=480&width=800");
          $("#doComment_btn").click();
        }
      }
    },
    error : function() {
    }
  });
};

/**
 * 修改成果.
 * 
 * @return
 */
Group.publication.doModifyOp = function() {
  var gPubIds = selectedPubIds();
  var artType = $("#navAction").val();
  var articleTypeName = "";
  var articleType;
  if (artType == 'groupPubs') {
    articleType = 1;
    articleTypeName = groupDetailPubs.pub;
  } else if (artType == 'groupRefs') {
    articleType = 2;
    articleTypeName = groupDetailPubs.docuemnt;
  } else {
    return;
  }
  if (gPubIds == '') {
    $.scmtips.show('warn', groupDetailPubs.select + articleTypeName + ".");
    $.Thickbox.closeWin();
    return false;
  }
  if (gPubIds.indexOf(",") > -1) {
    $.scmtips.show('error', groupDetailPubs.oneNotMary + articleTypeName + groupDetailPubs.update);
    $.Thickbox.closeWin();
    return false;
  }
  if (!isSelectedPubsIsMe()) {
    $.scmtips.show('error', "<span style='color:red;'>" + groupDetailPubs.updateIsnotMy1 + articleTypeName
        + groupDetailPubs.updateIsnotMy2 + "<span>");
    $.Thickbox.closeWin();
    return false;
  }

  var des3GroupId = $("#des3GroupId").val();
  var folderId = $("#groupFolderId").val();
  var groupNodeId = $("#groupNodeId").val();
  $("<input  type='hidden' name='groupId' value='" + des3GroupId + "'/>").insertAfter("#doComment_btn");
  $("<input  type='hidden' name='folderId' value='" + folderId + "'/>").insertAfter("#doComment_btn");
  $("<input  type='hidden' name='nodeId' value='" + groupNodeId + "'/>").insertAfter("#doComment_btn");
  $("<input  type='hidden' id='forwardUrl' name='forwardUrl' value=''/>").insertAfter("#doComment_btn");
  var des3Id;
  var pubId;
  var ownerId;
  var nodeId;
  $($("#page_list").children()[0]).find(">tr").each(function() {
    var isChecked = $(this).find("input[name='ckpub']").attr("checked");
    if (isChecked == true || isChecked == 'checked') {
      des3Id = $(this).find(".pub_des3PubId_class").val();
      pubId = $(this).find(".pub_pubId_class").val();
      ownerId = $(this).find(".pub_ownerId_class").val();
      nodeId = $(this).find(".pub_nodeId_class").val();
    }
  });
  $("<input  type='hidden' id='des3Id' name='des3Id' value='" + des3Id + "'/>").insertAfter("#doComment_btn");
  if (pubId == null || nodeId == null || ownerId == null) {
    $.scmtips.show('error', groupDetailPubs.can_not_op);
    alert(groupDetailPubs.can_not_op);
    return;
  }
  postData = {
    'pubId' : pubId,
    'nodeId' : nodeId,
    'ownerId' : ownerId
  };
  $.ajax({
    url : snsctx + "/grouppub/ajaxpubediturl",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      if (data) {
        // 判断是否超时
        if (Group.publication.ajaxSessionTimeoutHandler(data)) {
          return;
        }
        if (data.result == 'success' && data.iscanedit == 'true') {
          var ownerUrl = window.location.href;
          Group.publication.groupForwardUrl("/" + data.articleUrl + "/edit?des3Id=" + encodeURIComponent(des3Id)
              + "&des3GroupId=" + encodeURIComponent(des3GroupId) + "&folderId=" + folderId, ownerUrl);
        } else {
          $.scmtips.show('error', groupDetailPubs.noUpdate);
          $.Thickbox.closeWin();
        }
      }
    },
    error : function() {
    }
  });
};
Group.publication.ajaxSessionTimeoutHandler = function(data) {
  var ajaxTimeOutFlag = data['ajaxSessionTimeOut'];
  if (ajaxTimeOutFlag != null && typeof ajaxTimeOutFlag != "undefined" && ajaxTimeOutFlag == 'yes') {

    alert(groupDetailPubs.sysTimeout);
    top.location.href = "/scmwebsns/";
    return true;

  }
  return false;
};
Group.publication.groupForwardUrl = function(gotoUrl, ownerUrl) {
  if ('undefined' != typeof ownerUrl && ownerUrl != '') {
    $("#ownerUrl").attr("value", ownerUrl);
  }
  $("#forwardUrl").attr("value", gotoUrl);
  $("#mainForm").attr("action", snsctx + "/forwardUrl");
  $("#mainForm").attr("method", "get");
  $("#mainForm").submit();
};

/**
 * 移出群组
 */
Group.publication.doRemoveFromGroupOp = function() {
  var gPubIds = selectedPubIds();
  var artType = $("#navAction").val();
  var groupRole = $("#groupRole").val();
  var articleTypeName = "";
  var articleType;
  var confirmTip;
  if (artType == 'groupPubs') {
    articleType = 1;
    articleTypeName = groupDetailPubs.pub;
    confirmTip = groupDetailPubs.delete_confirm_pub;
  } else if (artType == 'groupRefs') {
    articleType = 2;
    articleTypeName = groupDetailPubs.docuemnt;
    confirmTip = groupDetailPubs.delete_confirm_ref;
  } else {
    return;
  }
  if (gPubIds == '') {
    $.scmtips.show('warn', groupDetailPubs.select + articleTypeName + ".");
    $.Thickbox.closeWin();
    return false;
  }
  if (!isSelectedPubsIsMe() && groupRole != "1" && groupRole != "2") {
    $.scmtips.show('error', "<span style='color:red;'>" + groupDetailPubs.delIsnotMy1 + articleTypeName
        + groupDetailPubs.delIsnotMy2 + "<span>");
    $.Thickbox.closeWin();
    return false;
  }
  jConfirm(confirmTip, groupDetailPubs.prompt, function(sure) {
    if (sure) {
      var params = [];
      $($("#page_list").children()[0]).find(">tr").each(function() {
        var isChecked = $(this).find("input[name='ckpub']").attr("checked");
        if (isChecked == true || isChecked == 'checked') {
          var nodeId = $(this).find(".pub_nodeId_class").val();
          var pubId = $(this).find(".pub_pubId_class").val();
          var ownerId = $(this).find(".pub_ownerId_class").val();
          var groupPubsId = $(this).find("input[name='ckpub']").val();
          var groupId = $(this).find(".pub_groupId_class").val();
          params.push({
            'nodeId' : nodeId,
            'pubId' : pubId,
            'ownerId' : ownerId,
            'groupPubsId' : groupPubsId,
            'groupId' : groupId
          });
        }
      });

      var jsonParams = JSON.stringify(params);
      var postData = {
        "jsonParams" : jsonParams,
        "articleType" : articleType
      };
      var url;
      $.ajax({
        url : snsctx + "/grouppub/ajaxremovepubfromgroup",
        type : "post",
        dataType : "json",
        data : postData,
        success : function(data) {
          if (data) {
            if (data.result == 'success') {
              $.scmtips.show('success', data.msg);
              $("#searchKey").val("");
              $('#mainForm').submit();
            } else if (data.ajaxSessionTimeOut == 'yes') {
              // 超时
              Group.timeout.alert();
            }
          }
        },
        error : function() {
        }
      });
    }
  });
};

/**
 * 分享给指定联系人.
 */
Group.publication.shareFriend = function() {
  var outter = this;
  if (outter.sourceType == 11 || outter.sourceType == 12) {// 群组成果、文献
    if (selectedPubIds() == '') {
      $.scmtips.show('warn', pub_required);
      return false;
    }
    overGrayBg();
    $("#selected_friend_div").attr("title", "auto_name_inner_div");
    $("#auto_name_inner_div").find(".mail_div").remove();
    showShareDialog("floatBoxBgEmail", "floatBoxEmail", "auto_name_outer_div", "share_mail_div_input", "680px", "300px");
    return true;
  }
};

/**
 * 是否选中的成果都是我的
 */
function isSelectedPubsIsMe() {
  var ooPsnId = $("#ooDes3PsnId").val();
  if (ooPsnId == "") {
    return false;
  }
  var chk = $("input:checkbox:checked").filter(".group_checkbox_click");
  var isAllMe = true;
  chk.each(function() {
    var ownerId = $(this).parent().parent().find(".pub_ownerId_class").val();
    if (ooPsnId != ownerId) {
      isAllMe = false;
    }
  });
  return isAllMe;
}

function forwardUrl1(fwdUrl, ownerUrl) {
  $("#forwardUrl").attr("value", fwdUrl);
  $("#mainForm").attr("action", "/pubweb/forwardUrl");
  $("#mainForm").attr("method", "get");
  $("#mainForm").submit();
}

function selectGroupPubIds() {
  var checkboxs = $("input:checked[name='ckpub']");
  var pubList = [];
  checkboxs.each(function() {
    var value = $(this).val();
    if (isPub()) {
      pubList.push({
        des3GroupPubsId : value,
        des3GroupId : findDes3GroupId()
      });
    } else {
      pubList.push({
        des3GroupRefsId : value,
        des3GroupId : findDes3GroupId()
      });
    }
  });

  return pubList;
}

function findDes3GroupId() {
  return $("#des3GroupId").val();
}

// 成果/文献 从文件夹中移除
function deletePubsToFolder() {
  var pubList = selectGroupPubIds();
  var des3PubIdsStr = JSON.stringify(pubList);

  if (pubList.length == 0) {
    $.scmtips.show('warn', isPub() ? groupDetailPubs.selectPub : groupDetailPubs.selectDocument);
    return false;
  }

  // 判断是否属于自己的
  var groupRole = $("#groupRole").val();
  if (!isSelectedPubsIsMe() && groupRole != "1" && groupRole != "2") {
    var message = "";
    var artType = $("#navAction").val();
    if (artType == 'groupPubs') {
      message = groupDetailPubs.noRemovePubsToFolder;
    } else if (artType == 'groupRefs') {
      message = groupDetailPubs.noRemoveRefsToFolder;
    }
    $.scmtips.show('error', "<span style='color:red;'>" + message + "<span>");
    return false;
  }

  jConfirm(groupDetailPubs.confirm, groupDetailPubs.prompt, function(sure) {
    if (!sure)
      return;
    var url = isPub() ? '/group/ajaxGroupPubsDeleteToFolder' : '/group/ajaxGroupRefsDeleteToFolder';
    $.ajax({
      url : snsctx + url,
      type : 'post',
      dataType : 'json',
      data : {
        "des3PubIdsStr" : des3PubIdsStr,
        "groupFolder.groupFolderId" : $("#groupFolderIds").val(),
        "groupPsn.groupNodeId" : findNodeId(),
        "groupPsn.des3GroupId" : findDes3GroupId()
      },
      success : function(data) {

        if (data.result == 'success') {
          $.scmtips.show('success', data.msg);
          setTimeout(function() {
            $("#searchKey").val("");
            $('#mainForm').submit();
          }, 500);
        } else {
          $.scmtips.show('error', data.msg);
        }
      }
    });
  });

}
function isPub() {
  var artType = $("#navAction").val();
  return artType == 'groupPubs';
}

/**
 * 选中的成果
 * 
 * @return
 */
function selectedPubIds() {
  var pubIds = "";
  $("input:enabled[type='checkbox'][name='ckpub']").each(function() {

    if ($(this).attr("checked")) {
      if (pubIds == "") {
        pubIds = $(this).attr("value");
      } else {
        pubIds = pubIds + "," + $(this).attr("value");
      }
    }
  });
  $("#pubIds").attr("value", pubIds);
  return pubIds;
}

function selectedPublicationIds() {
  var pubIds = "";
  $("input:enabled[type='checkbox'][name='ckpub']").each(function() {

    if ($(this).attr("checked")) {
      if (pubIds == "") {
        pubIds = $(this).attr("selectPubId");
      } else {
        pubIds = pubIds + "," + $(this).attr("value");
      }
    }
  });
  $("#pubIds").attr("value", pubIds);
  return pubIds;
}

function changeButStatus(obj, butId, boxName) {
  if ($(obj).attr("checked")) {
    $('#' + butId).attr("disabled", false);
  } else {
    var count = 0;
    $(":checkbox[name='" + boxName + "']").each(function() {
      if ($(this).attr("checked")) {
        count++;
        $('#' + butId).attr("disabled", false);
        return;
      }
    });
    if (count == 0) {
      $('#' + butId).attr("disabled", true);
    }
  }
}

/**
 * 成果加入文件夹
 */
function movePubsToFolder() {
  var pubList = selectGroupPubIds();
  var des3PubIdsStr = JSON.stringify(pubList);
  var unchangingIds = "";
  var folderList = [];
  $("input:checked[name='moveToFolders']").each(function() {
    var value = $(this).val();
    folderList.push(value);
  });
  $(".addFolderUl li").each(function() {
    if ($(this).find("input[type='checkbox']").is(":hidden")) {
      var titleId = $(this).find("input[type='checkbox']").attr("id");
      titleId = titleId.substr(titleId.lastIndexOf("_") + 1, titleId.length);
      unchangingIds += "," + titleId;
    }
  });
  if (unchangingIds.length > 0) {
    unchangingIds = unchangingIds.substring(1);
  }
  /*
   * if (folderList.length == 0){ $.scmtips.show('warn',groupDetailPubs.selectTag); return false; }
   */
  var folderList = folderList.join(",");
  var url = isPub() ? '/group/ajaxGroupPubsAddToFolder' : '/group/ajaxGroupRefsAddToFolder';
  $.ajax({
    url : snsctx + url,
    type : 'post',
    dataType : 'json',
    data : {
      "des3PubIdsStr" : des3PubIdsStr,
      "folderList" : folderList,
      "groupPsn.groupNodeId" : findNodeId(),
      "groupPsn.des3GroupId" : findDes3GroupId(),
      'unchangingIds' : unchangingIds
    },
    success : function(data) {
      if (data.result == 'success') {
        $.scmtips.show("success", data.msg);
        setTimeout(function() {
          $("#searchKey").val("");
          $('#mainForm').submit();
        }, 500);
      } else if (data.ajaxSessionTimeOut == 'yes') {
        // 超时
        Group.timeout.alert();
      } else {
        $.scmtips.show('error', data.msg);
      }
    }
  });
}

function findIsSameFolderName(folderName, folderType, groupFolderId) {
  var isSameName = false;
  for ( var i in groupFolderListJson) {
    var folder = groupFolderListJson[i];
    if (folderName == folder.folderName
        && ((folderType == folder.folderType && groupFolderId == null) || (folderType == null && groupFolderId != folder.groupFolderId))) {
      isSameName = true;
      break;
    }
  }
  return isSameName;
}
/**
 * 获得群组节点ID
 * 
 * @returns
 */
function findNodeId() {
  return $("#groupNodeId").val();
}
/**
 * 获得加密的群组ID
 * 
 * @returns
 */
function findDes3GroupId() {
  return $("#des3GroupId").val();
}

/**
 * 导出成果到文件
 */
function exportPublication() {
  var artType = $("#navAction").val();
  var articleType;
  if (artType == 'groupPubs') {
    articleType = 1;
  } else if (artType == 'groupRefs') {
    articleType = 2;
  } else {
    return;
  }
  var exportType = $("input:radio[name='export_Type']:checked").val();
  var export_Fields = $("input:radio[name='export_Fields']:checked").val();
  // 导出群组中的全部成果.
  var groupPubIds = selectedPubIds();
  if (groupPubIds == '') {

  }
  // 导出群组中选择的成果.
  $("#exportForm").remove();
  $(
      "<form id='exportForm'  name='exportForm'  method='post' >"
          + "<input  type='hidden' id='export_exportType' name='exportType' value='" + exportType + "'/>"
          + "<input  type='hidden' id='export_Fields' name='export_Fields' value='" + export_Fields + "'/>"
          + "<input  type='hidden' id='export_groupPubIds' name='groupPubIds' value='" + groupPubIds + "'/>"
          + "<input  type='hidden' id='export_articleType' name='articleType' value='" + articleType + "'/>"
          + "<input  type='hidden' id='export_des3GroupId' name='des3GroupId' value='" + $("#des3GroupId").val()
          + "'/>" + "<input  type='hidden' id='export_nodeId' name='nodeId' value='" + $("#groupNodeId").val() + "'/>"
          + "</form>").insertAfter("body");
  $("#exportForm").attr("action", snsctx + "/grouppub/ajaxexportpub");
  $("#exportForm").submit();
}

/**
 * 分享.
 */
Group.publication.sharePublication = function(obj, isNoOutSites) {
  var resIds = "";
  var des3Ids = "";
  var nodeId = 1;
  var resDetails = [];
  var checkedPubObj;
  var artType = $("#navAction").val();
  if (obj == undefined) {
    checkedPubObj = $("input[name='ckpub']:checked");
    if (checkedPubObj.length == 0) {

      if (artType == 'groupPubs') {
        $.scmtips.show("warn", groupDetailPubs.selectPub);
        return false;
      } else if (artType == 'groupRefs') {
        $.scmtips.show("warn", groupDetailPubs.selectDocument);
        return false;
      }

    }
    $(checkedPubObj).each(function() {
      if (resIds == "") {
        des3Ids = $(this).parent().find("input.inputpubId").val();
        resIds = $(this).parent().find("input.inputpubId_nodes").val();
      } else {
        des3Ids = des3Ids + "," + $(this).parent().find("input.inputpubId").val();
        resIds = resIds + "," + $(this).parent().find("input.inputpubId_nodes").val();
      }
      nodeId = $(this).parent().find("input.inputnodeId").val();
      resDetails.push({
        "resId" : $(this).parent().find("input.inputpubId_nodes").val(),
        "resNode" : nodeId
      });
    });
  } else {
    var tdObj = $("#tr" + $(obj).attr("resId"));
    resIds = $(tdObj).find("input.pub_pubId_class").val();
    des3Ids = $(tdObj).find("input.inputpubId").val();
    nodeId = $(tdObj).find("input.pub_nodeId_class").val();
    resDetails.push({
      "resId" : $(tdObj).find("input.pub_pubId_class").val(),
      "resNode" : nodeId
    });
  }

  shareConfig = {
    "resType" : $("#articleType").val(),
    "resIds" : resIds,
    "des3Ids" : des3Ids,
    "nodeId" : nodeId,
    "actionName" : "ext",
    "jsonParam" : {
      "resType" : $("#articleType").val(),
      "resDetails" : resDetails
    }
  };
  if (resDetails.length > 1) {
    shareConfig["shareType"] = "1,2,4";
    shareConfig["shareTypeFirst"] = "2";
  } else {
    if (isNoOutSites != undefined && isNoOutSites == 1) {
      shareConfig["shareType"] = "1,2,4";
      shareConfig["shareTypeFirst"] = "1";
    } else {
      shareConfig["shareType"] = "1,2,4,5";
      shareConfig["shareTypeFirst"] = "5";
    }
  }

  var shareContent = $("#articleType").val() == 2
      ? groupDetailPubs.friendShareContentRef
      : groupDetailPubs.friendShareContentPub;
  shareContentConfig = {
    "mineDynContent" : "",
    "friendShareContent" : shareContent
  };

  $("#pubShareBtn").click();
};

/**
 * 分享给联系人.
 * 
 * @param receivers
 * @param shareContent
 * @param commendDendLine
 * @param shareTitleCN
 * @param shareTitleEN
 * @return
 */
function sendShareForFriend(receivers, shareContent, commendDendLine, shareTitleCN, shareTitleEN) {
  var jsonParam = shareConfig.jsonParam;
  jsonParam["shareTitle"] = shareTitleCN;
  jsonParam["shareEnTitle"] = shareTitleEN;
  var articleType = $("#articleType").val();
  var postData = {
    "receivers" : receivers,
    "des3PubIds" : shareConfig.des3Ids,
    "shareDeadline" : commendDendLine,
    "resType" : articleType,
    "content" : shareContent,
    "jsonParam" : JSON.stringify(jsonParam)
  };
  $.ajax({
    url : snsctx + "/commend/publication/ajaxCommendPub",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      if (data.result == 'success') {
        var resIdArray = shareConfig.resIds.split(",");
        for (var i = 0; i < resIdArray.length; i++) {
          var shareCountObj = $("#shareCountLabel_" + articleType + "_" + resIdArray[i]);
          var count = parseInt($(shareCountObj).attr("count")) + 1;
          $(shareCountObj).text("count", count);
          $(shareCountObj).html("(" + count + ")");
        }
        $.scmtips.show("success", groupDetailPubs.optSuccess);
        $.Thickbox.closeWin();
      }
    },
    error : function() {
    }
  });
}

function sendShareForFriendNew(receivers, shareContent, commendDendLine, shareTitleCN, shareTitleEN, isAddShareTimes) {
  var jsonParam = shareConfig.jsonParam;
  jsonParam["shareTitle"] = shareTitleCN;
  jsonParam["shareEnTitle"] = shareTitleEN;
  jsonParam["isAddShareTimes"] = isAddShareTimes;
  var articleType = $("#articleType").val();
  var postData = {
    "receivers" : receivers,
    "des3PubIds" : shareConfig.des3Ids,
    "shareDeadline" : commendDendLine,
    "resType" : articleType,
    "content" : shareContent,
    "jsonParam" : JSON.stringify(jsonParam)
  };
  $.ajax({
    url : snsctx + "/commend/publication/ajaxCommendPub",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      if (isAddShareTimes) {
        if (data.result == 'success') {
          var resIdArray = shareConfig.resIds.split(",");
          for (var i = 0; i < resIdArray.length; i++) {
            var shareCountObj = $("#shareCountLabel_" + articleType + "_" + resIdArray[i]);
            var count = parseInt($(shareCountObj).attr("count")) + 1;
            $(shareCountObj).text("count", count);
            $(shareCountObj).html("(" + count + ")");
          }
          $.scmtips.show("success", groupDetailPubs.optSuccess);
          $.Thickbox.closeWin();
        }
      }
    },
    error : function() {
    }
  });
}

function initAjaxComment(Obj) {
  var nodeId = $(Obj).attr("nodeId");
  var pubId = $(Obj).attr("pubId");
  $.ajax({
    url : snsctx + "/comment/publication/ajaxPubComment",
    type : "post",
    dataType : "json",
    data : {
      'nodeId' : nodeId,
      'pubId' : pubId
    },
    success : function(data) {
      if (data) {
        if (data.result == 'success') {
          if (data.psnnum > 0) {
            $("#comment_count_" + pubId).html("(" + data.psnnum + ")");
            $("#comment_count_span_" + pubId).show();
          }
        }
      }
    }
  });
}
// 跳转到添加成果，回来，直接回到列表
Group.publication.clearBackPage = function() {
  $("#pageNo").val(1);
  $("#searchKey").val("");
  $("#groupFolderId").val("");
  $("#searchName").val("");
  $("#leftMenuId").val("");
  var artType = $("#navAction").val();
  var articleType = 1;
  if (artType == 'groupPubs') {
    articleType = 1;
  } else if (artType == 'groupRefs') {
    articleType = 2;
  }
  if (articleType == 1) {
    $("#orderBy").val("groupPubsId");
    $("#order").val("desc");
  } else {
    $("#orderBy").val("groupRefsId");
    $("#order").val("desc");
  }

};

/**
 * 群组成果单选
 */
Group.publication.checkPub = function(obj) {
  var flag = true;
  if (obj.checked) {
    var cks = $(":checkbox[name='ckpub']");
    for (var i = 0; i < cks.length; i++) {
      // 没有全部选择
      if (!cks[i].checked) {
        flag = false;
        break;
      }
    }
  } else {
    flag = false;
  }
  $("#checkbox").attr("checked", flag);
};

Group.publication.ready = function() {

  // 左菜单
  scmLeftMenu = new ScmLeftMenu(leftMenuLocateId);
  scmLeftMenu.init();
  // 群组成果标签分页初始化
  scmLeftMenu.initTag("groupTag", 10);
  scmLeftMenu.doFundingYear();
  scmLeftMenu.fundingYearMove('left');

  /* 各种触发弹窗的元素需绑定下面的参数 */
  // 弹出管理div
  $("#management").thickbox({
    ctxpath : snsctx,
    respath : resscmsns,
    type : "groupFolderManageDiv"
  });
  // 导入操作链接,导入图片链接
  $("#importLink,#importPicLink").thickbox({
    ctxpath : snsctx,
    respath : resscmsns
  });
  // 弹出成果评价窗口
  $("#doComment_btn").thickbox({
    ctxpath : snsctx,
    respath : resscmsns
  });
  // 加入文件夹窗口
  $("#showAddToFolderDiv").thickbox({
    ctxpath : snsctx,
    respath : resscmsns
  });

  // 相关推荐
  $("#relatedRecommend").loaddiv({
    ctxpath : snsctx,
    respath : resscmsns,
    type : "relatedRecommend",
    parameters : {
      "count" : "5"
    }
  });

  // 检索成果
  $("#search_publication_togroup_op,#search_publication_togroup_op22,#search_publication_togroup_op33").click(
      function() {
        var artType = $("#navAction").val();
        var articleType;
        if (artType == 'groupPubs') {
          articleType = 1;
        } else if (artType == 'groupRefs') {
          articleType = 2;
        } else {
          return;
        }
        var des3GroupId = $("#des3GroupId").val();
        var groupFolderId = $("#groupFolderId").val();
        var groupNodeId = $("#groupNodeId").val();
        Group.publication.clearBackPage();
        if (articleType == 1) {
          forwardUrl1("/pubweb/publication/collect?groupId=" + encodeURIComponent(des3GroupId) + "&nodeId="
              + groupNodeId + "&artType=" + artType);
        } else if (articleType == 2) {
          forwardUrl1("/reference/group/collect?groupId=" + encodeURIComponent(des3GroupId) + "&nodeId=" + groupNodeId
              + "&artType=" + artType);
        }
      });

  // 手工录入成果
  $("#group_enter_newpub_op").click(
      function() {
        var artType = $("#navAction").val();
        var articleType;
        if (artType == 'groupPubs') {
          articleType = 1;
        } else if (artType == 'groupRefs') {
          articleType = 2;
        } else {
          return;
        }
        var des3GroupId = $("#des3GroupId").val();
        var folderId = $("#groupFolderId").val();
        var groupNodeId = $("#groupNodeId").val();
        Group.publication.clearBackPage();
        if (articleType == 1) {
          // forwardUrl1("/publication/enter?backType=1&menuId=31&groupId="+encodeURIComponent(des3GroupId)
          // + "&folderId="+folderId+"&nodeId="+groupNodeId);
          // forwardUrl1("/publication/batch");
          // forwardUrl1("/pubweb/publication/batchenter?backType=1&menuId=31&groupId="+encodeURIComponent(des3GroupId)
          // + "&folderId="+folderId+"&nodeId="+groupNodeId);
          forwardUrl1("/pubweb/publication/enter?backType=1&menuId=31&groupId=" + encodeURIComponent(des3GroupId)
              + "&folderId=" + folderId + "&nodeId=" + groupNodeId);

        } else if (articleType == 2) {
          forwardUrl1("/reference/enter?backType=3&menuId=31&groupId=" + encodeURIComponent(des3GroupId) + "&folderId="
              + folderId + "&nodeId=" + groupNodeId);
        }
      });

  // 评价成果
  $("#group_pub_comment_op").click(function() {
    Group.publication.doPubCommentOp();
  });

  // 导入到我的成果库
  $("#group_import_to_mypub_op").click(
      function() {
        var artType = $("#navAction").val();
        var articleTypeName = "";
        if (artType == 'groupPubs') {
          articleTypeName = groupDetailPubs.pub;
        } else if (artType == 'groupRefs') {
          articleTypeName = groupDetailPubs.docuemnt;
        } else {
          return;
        }
        if (selectedPubIds() == '') {
          $.scmtips.show('warn', groupDetailPubs.select + articleTypeName + ".");
          $.Thickbox.closeWin();
          return false;
        }
        var params = [];
        var currPsnId = $("#ooPsnId").val();
        var flag = true;
        $("#page_list").find("tr").each(function() {
          var isChecked = $(this).find("input[name='ckpub']").attr("checked");
          if (isChecked == true || isChecked == 'checked') {
            var nodeId = $(this).find(".pub_nodeId_class").val();
            var pubId = $(this).find(".pub_pubId_class").val();
            var ownerId = $(this).find(".pub_ownerId_class").val();
            params.push({
              nodeId : nodeId,
              pubId : pubId,
              ownerId : ownerId
            });
          }
        });
        if (!flag)
          return false;
        jConfirm(groupDetailPubs.input_pub_db, groupDetailPubs.prompt, function(sure) {
          if (sure) {
            var postData = {
              "jsonParams" : JSON.stringify(params),
              "articleType" : "1"
            };
            var url;
            // 检查成果状态
            ScholarView.checkPubsStatus(postData, function() {
              $.ajax({
                url : "/pubweb/publication/ajaximporttomypub",
                type : "post",
                // dataType:"json",
                data : postData,
                success : function(data) {
                  if (typeof data != 'undefined' && data && data.result == 'success') {
                    $.scmtips.show('success', data.msg);
                    $.Thickbox.closeWin();
                  } else {
                    if (data.articleType != 'undefined' && data.jsonParams != 'undefined') {
                      var dataJsonParams = data.jsonParams.replace(/\"/g, "&quot;");
                      $("#detail_import_title").attr(
                          "alt",
                          "/pubweb/publication/ajaximportmypub/show?articleType=1&myJsonParams=" + dataJsonParams
                              + "&TB_iframe=true&height=160&width=700");
                    }
                    $("#detail_import_title").click();
                  }
                },
                error : function() {
                  $.Thickbox.closeWin();
                }
              });
            });

          } else {
            $.Thickbox.closeWin();
            return false;
          }
        });
      });
  // 导入到我的文献库
  $("#group_import_to_myref_op").click(function() {

    var artType = $("#navAction").val();
    var articleTypeName = "";
    if (artType == 'groupPubs') {
      articleTypeName = groupDetailPubs.pub;
    } else if (artType == 'groupRefs') {
      articleTypeName = groupDetailPubs.docuemnt;
    } else {
      return;
    }
    if (selectedPubIds() == '') {
      $.scmtips.show('warn', groupDetailPubs.select + articleTypeName + ".");
      $.Thickbox.closeWin();
      return false;
    }
    var params = [];
    var flag = true;
    var currPsnId = $("#ooPsnId").val();
    $($("#page_list").children()[0]).find(">tr").each(function() {
      var isChecked = $(this).find("input[name='ckpub']").attr("checked");
      if (isChecked == true || isChecked == 'checked') {
        var nodeId = $(this).find(".pub_nodeId_class").val();
        var pubId = $(this).find(".pub_pubId_class").val();
        var ownerId = $(this).find(".pub_ownerId_class").val();
        params.push({
          nodeId : nodeId,
          pubId : pubId,
          ownerId : ownerId
        });
      }
    });
    if (!flag)
      return false;
    ScholarView.checkPubsStatus({
      "jsonParams" : JSON.stringify(params),
      "articleType" : 2
    }, function() {
    });
    jConfirm(groupDetailPubs.input_document_db, groupDetailPubs.prompt, function(sure) {
      if (sure) {
        var postData = {
          "jsonParams" : JSON.stringify(params),
          "articleType" : 2
        };
        var url;
        try {
          // 检查成果状态
          // ScholarView.checkPubsStatus(postData,function(){
          $.ajax({
            url : ctxpath + "/grouppub/ajaximportmypub",
            type : "post",
            dataType : "json",
            data : postData,
            success : function(data) {
              if (typeof data != 'undefined' && data) {
                if (data.result == 'success') {
                  $.scmtips.show('success', data.msg);
                  $.Thickbox.closeWin();
                } else if (data.ajaxSessionTimeOut == 'yes') {
                  // 超时
                  Group.timeout.alert();
                } else {
                  // 修改了出错时的提示内容_MJG_SCM-3536.
                  $.scmtips.show('warn', groupDetailPubs.addFailed);
                  $.Thickbox.closeWin();
                }
              }
            },
            error : function() {
              $.Thickbox.closeWin();
            }
          });
          // });
        } catch (e) {
          $.Thickbox.closeWin();
        }
      } else {
        $.Thickbox.closeWin();
        return false;
      }
    });
  });

  // 修改成果
  $("#group_modify_op").click(
      function() {
        // Group.publication.doModifyOp();
        var pubIds = selectedPublicationIds();
        var groupId = $("#des3GroupId").val();
        if (pubIds == "") {
          $.scmtips.show("warn", required_publication);
          return false;
        }
        if (pubIds.indexOf(",") > -1) {
          $.scmtips.show("warn", maint_global_update.content);
          return false;
        }
        if (!isSelectedPubsIsMe()) {
          $.scmtips.show('error', "<span style='color:red;'>" + groupDetailPubs.updateIsnotMy1
              + groupDetailPubs.updateIsnotMy2 + "<span>");
          $.Thickbox.closeWin();
          return false;
        }
        $("#forwardUrl").attr(
            "value",
            '/pub/enter?des3PubId=' + encodeURIComponent(pubIds) + "&backType=1" + "&groupId=" + groupId
                + "&isProjectPub=1");
        $("#mainForm").attr("action", "/pubweb/forwardUrl");
        $("#mainForm").submit();
      });

  // 移出群组
  $("#group_remove_fromgroup_op").click(function() {
    Group.publication.doRemoveFromGroupOp();
  });

  // 添加到文件夹
  $("#add_to_folder_btn").click(
      function() {
        if (selectGroupPubIds().length == 0) {
          $.scmtips.show('warn', $("#navAction").val() == 'groupPubs'
              ? groupDetailPubs.selectPub
              : groupDetailPubs.selectDocument);
          return false;
        }

        // 判断是否属于自己的
        var groupRole = $("#groupRole").val();
        if (!isSelectedPubsIsMe() && groupRole != "1" && groupRole != "2") {
          var message = "";
          var artType = $("#navAction").val();
          if (artType == 'groupPubs') {
            message = groupDetailPubs.noRemovePubsToFolder;
          } else if (artType == 'groupRefs') {
            message = groupDetailPubs.noRemoveRefsToFolder;
          }
          $.scmtips.show('error', "<span style='color:red;'>" + message + "<span>");
          return false;
        }
        // 检查
        $(".addFolderUl li").each(function() {
          var id = $(this).attr("id");
          id = id.substr(id.lastIndexOf("_") + 1, id.length);
          $(this).find("#box_" + id).attr("checked", false);
          $(this).find("#box_" + id).css("display", "block");
          $(this).find("#img_" + id).css("display", "none");
        });
        Loadding_div.show_over("right-wrap", "content");
        $.ajax({
          url : snsctx + '/group/checkEditFolder',
          type : 'post',
          dataType : 'json',
          data : {
            'ids' : selectedPubIds(),
            'des3GroupId' : $("#des3GroupId").val(),
            'groupNodeId' : $("#groupNodeId").val(),
            'type' : isPub() ? 'pub' : 'ref'
          },
          success : function(data) {
            $(".addFolderUl li").each(function() {
              var id = $(this).attr("id");
              id = id.substr(id.lastIndexOf("_") + 1, id.length);
              var flag = false;
              var unchange = false;
              $.each(data, function(n, map) {
                if (map.id == id) {
                  flag = true;
                  unchange = map.flag;
                  return;
                }
              });
              if (flag) {
                if (unchange) {
                  $(this).find("#box_" + id).attr("checked", true);
                } else {
                  $(this).find("#box_" + id).css("display", "none");
                  $(this).find("#img_" + id).css("display", "block");
                }
              }
            });
            Loadding_div.close("content");
            $("#showAddToFolderDiv").attr("alt", "/#TB_inline?height=265&amp;width=430&amp;inlineId=pubAddFolderDiv");
            $("#showAddToFolderDiv").click();
          }
        });
      });

  // 成果、文献评论
  $(".cls_comment").each(function() {
    var artType = $("#navAction").val();
    var actionType;
    var type;
    if (artType == 'groupPubs') {
      actionType = 1;
      type = "publicationComment";
    } else if (artType == 'groupRefs') {
      actionType = 2;
      type = "referenceComment";
    }
    initAjaxComment(this);
    $("#" + $(this).attr("id")).thickbox({
      ctxpath : snsctx,
      respath : resscmsns,
      parameters : {
        "pubId" : $(this).attr("pubId"),
        "actionType" : actionType
      },
      type : type
    });
  });

  // 赞次数初始化
  smate.award.iniAward($("#articleType").val());

  // 显示更多操作
  $('#more_operation_btn').click(function(event) {
    common.showMoreOperation(this, "more_operation", event);
  });

  // 将成果 从文件夹中移除
  $("#delete_to_folder_btn").click(deletePubsToFolder);

  // 导出文件按钮
  $("#export_file_btn").click(function() {
    var supportEndNote = false;
    var supportRefWorks = false;
    var pubIds = "";
    $("input:enabled[type='checkbox'][name='ckpub']").each(function() {
      if ($(this).attr("checked")) {
        var des3PubId = $(this).attr("value");
        if (pubIds == "") {
          pubIds = des3PubId;
        } else {
          pubIds = pubIds + "," + des3PubId;
        }
        var pubId = $(this).parent().find('.inputpubId_nodes').val();
        var pubType = $('#tr' + pubId).find('.pubType').val();
        if (pubType != 1) {
          if (pubType == 2 || pubType == 3 || pubType == 4 || pubType == 7) {
            supportRefWorks = true;
          }
          supportEndNote = true;
        }
      }
    });
    $("#pubIds").attr("value", pubIds);

    if (pubIds == '') {
      var artType = $("#navAction").val();
      if (artType == 'groupPubs') {
        $.scmtips.show('warn', groupDetailPubs.selectPub);
      } else if (artType == 'groupRefs') {
        $.scmtips.show('warn', groupDetailPubs.selectDocument);
      }
      return;
    }
    if (supportEndNote) {
      $('#export_endnote').show();
    } else {
      $('#export_endnote').hide();
    }
    if (supportRefWorks) {
      $('#export_refwork').show();
    } else {
      $('#export_refwork').hide();
    }
    $('#export_publication_op').click();
  });

  $('#searchKey').watermark({
    tipCon : searchInputTip
  });

  // 分享
  $("#pubShareBtn,#hidden-shareLink").thickbox({
    respath : resscmsns
  });

  resShareUtil = new ResShareUtil();
  resShareUtil.ajaxBatchLoadShareCount($("#articleType").val(), 0);

  // 保存中间线美观
  setTimeout(function() {
    if ($(".right-wrap").height() < $(".right-wrap").prev(".left-wrap").height()) {
      $(".right-wrap").height($(".right-wrap").prev(".left-wrap").height());
    }
  }, 1500);
};

// 左侧栏跳转
/*
 * ScmLeftMenu.switchContents = function(menuId, url,searchName, searchId) {
 * $("#searchId").val(searchId); $('#leftMenuId').val(menuId); $("#searchName").val(searchName);
 * $("#searchKey").val(""); $("#pageNo").val(1);
 * 
 * var folderUrl; if($("#navAction").val()=='groupPubs'){ folderUrl = "/groupweb/grouppub/show";
 * }else if($("#navAction").val()=='groupRefs'){ folderUrl = snsctx+"/group/ref"; }else
 * if($("#navAction").val()=='groupPrjs'){ folderUrl = snsctx+"/group/prj"; }else
 * if($("#navAction").val()=='groupFiles'){ folderUrl = snsctx+"/group/file"; }else
 * if($("#navAction").val()=='groupWorks'){ folderUrl = snsctx+"/group/work"; }else
 * if($("#navAction").val()=='groupCourses'){ folderUrl = snsctx+"/group/course"; }
 * 
 * $("#mainForm").attr("action", folderUrl); $("#mainForm").submit(); };
 */

