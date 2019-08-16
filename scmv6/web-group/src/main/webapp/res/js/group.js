/**
 * 我的群组-群组菜单页面的JS事件.
 * 
 * @author zk
 * @since 2016-09-22
 */
var Group = Group ? Group : {};
var ctxpath = '/scmwebsns';
var respath = "/resscmwebsns";
// 微信分享
function getQrImg(url) {
  if (navigator.userAgent.indexOf("MSIE") > 0) {
    $("#share-qr-img").qrcode({
      render : "table",
      width : 175,
      height : 175,
      text : url
    });
  } else {
    $("#share-qr-img").qrcode({
      render : "canvas",
      width : 175,
      height : 175,
      text : url
    });
  }
}

Group.selectModule = function(des3GroupId, moduleName) {
  $(".group_nav").find(".hover").removeClass("hover");
  switch (moduleName) {
    case 'file' :
      Group.toModuleFile(des3GroupId);
      $("#filemenu").addClass("hover");
    break;
    case 'pub' :
      Group.toModulePubs(des3GroupId);
      $("#pubmenu").addClass("hover");
    break;
    case 'member' :
      Group.toModuleMember(des3GroupId);
      $("#membermenu").addClass("hover");
    break;
    case 'editGroup' :
      Group.toGroupEdit(des3GroupId);
    break;
    default :
      Group.toModuleBrief(des3GroupId);
      $("#brief").addClass("hover");
  }

}

Group.selectTargetModule = function(moduleName) {
  var url = location.href;
  url = url.replace("&backType=3", "");
  if (url.indexOf("&targetModule=") != -1) {
    var href = url;
    href = href.replace("file", moduleName);
    href = href.replace("pub", moduleName);
    href = href.replace("member", moduleName);
    href = href.replace("brief", moduleName);
    location.href = href;
  } else {
    location.href = url + "&targetModule=" + moduleName;
  }
}

// ---------------------群组成员模块 -----------------------------start
// 跳转至群组成员模块
Group.toModuleMember = function(des3GroupId) {
  var submodule = $("#submodule").val();
  Group.changeUrl("member", submodule);

  var url = "/groupweb/groupmodule/ajaxmember";
  var currentPsnGroupRoleStatus = $.trim($("#currentPsnGroupRoleStatus").val());
  dataJson = {
    currentPsnGroupRoleStatus : currentPsnGroupRoleStatus,
    des3GroupId : $("#des3GroupId").val()
  };
  ajaxparamsutil.doAjax(url, dataJson, Group.groupMemberHtml, 'html', 'post');
}

// 群组成员模块-html
Group.groupMemberHtml = function(data) {
  $(".group_box").remove();
  $("#groupContent").append(data);
  // 为群组创建人/管理员
  if ($("#currentPsnGroupRole").val() == 1 || $("#currentPsnGroupRole").val() == 2) {
    // if ($("#applyMemberCount").val() > 0) {
    $("#two2").show();// 显示"申请中字段"
    $(".num_tip").text($("#applyMemberCount").val());// 显示申请中人数
    // }
  }
}

// 群组成员模块-成员列表
Group.groupMemberList = function(dataJson) {
  var url = "/groupweb/groupmodule/ajaxmemberlist";
  if (dataJson == null) {
    dataJson = {
      'currentPsnGroupRoleStatus' : $.trim($("#currentPsnGroupRoleStatus").val()),
      'des3GroupId' : $("#des3GroupId").val()
    };
  }
  ajaxparamsutil.doAjax(url, dataJson, Group.groupMemberListHtml, 'html', 'post');
}

// 群组成员模块-成员列表回调
Group.groupMemberListHtml = function(data) {
  $("#memberList").remove();
  $("#pendingList").remove();
  $("#con_two_1").append(data);
}
// 群组成员设置为普通成员
Group.toGroupMember = function(des3MemberId) {
  if ($("#des3PsnId").val() == des3MemberId) {
    $.scmtips.show('warn', "不能给自己设置权限");
    return;
  }
  var groupIdList = [];
  groupIdList.push({
    des3InvitePsnId : des3MemberId
  });
  var des3InvitePsnIdStr = JSON.stringify(groupIdList);
  var url = snsctx + "/group/ajaxSetGroupMember";
  var data = {
    "groupPsn.groupNodeId" : 1,
    "groupPsn.des3GroupId" : $("#des3GroupId").val(),
    "des3InvitePsnIdStr" : des3InvitePsnIdStr
  };
  ajaxparamsutil.doAjax(url, data, Group.updateGroupMemberJurisdictionResult);
}
// 群组成员设置为管理员
Group.toGroupAdmin = function(des3MemberId) {
  if ($("#des3PsnId").val() == des3MemberId) {
    $.scmtips.show('warn', "不能给自己设置权限");
    return;
  }
  var groupIdList = [];
  groupIdList.push({
    des3InvitePsnId : des3MemberId
  });
  var des3InvitePsnIdStr = JSON.stringify(groupIdList);
  var url = snsctx + '/group/ajaxSetGroupAdmin';
  var data = {
    "groupPsn.groupNodeId" : 1,
    "groupPsn.des3GroupId" : $("#des3GroupId").val(),
    "des3InvitePsnIdStr" : des3InvitePsnIdStr
  };
  ajaxparamsutil.doAjax(url, data, Group.updateGroupMemberJurisdictionResult);
}
// 群组成员模块-修改权限回调
Group.updateGroupMemberJurisdictionResult = function(data, dataJson) {
  if (data.result == 'success') {
    Group.groupMemberList();
  }
}

// 群组成员模块-移除此人
Group.deleteGroupMember = function(des3MemberId, index, groupRole) {
  if ($("#des3PsnId").val() == des3MemberId) {
    $.scmtips.show('warn', groupMembers.notDelSelf);
    return;
  }
  if (Number($("#currentPsnGroupRole").val()) == 2 && Number(groupRole) == 1) {
    $.scmtips.show('warn', groupMembers.notDelCreator);
    return;
  }
  if (Number($("#currentPsnGroupRole").val()) == 2 && Number(groupRole) == 2) {
    $.scmtips.show('warn', groupMembers.notDelAdmin);
    return;
  }
  var url = "/groupweb/groupopt/ajaxdeletemember";
  var data = {
    'des3GroupId' : $("#des3GroupId").val(),
    'des3MemberId' : des3MemberId,
    'index' : index
  };
  ajaxparamsutil.doAjax(url, data, Group.deleteGroupMemberResult, 'json');
}

// 群组成员模块-移除此人回调
Group.deleteGroupMemberResult = function(data, dataJson) {
  if (data.result == 'success') {
    $("#memberList_" + dataJson["index"]).hide();
    $("#group_sumMember").text(
        (Number($("#group_sumMember").html().replace(/\D+/g, "")) - 1)
            + $("#group_sumMember").text().replace(/\d+/g, ""));// 移除成员成功,简介处的成员数也相应减少
    $("#memberList").remove();// 下面列表刷新之前先清一次
    Group.groupMemberList();// 这里刷新一次列表,是因为分页数据有可能变化
  } else if (data.result == 'notdelself') {
    $.scmtips.show('warn', groupMembers.notDelSelf);
  } else {
    $.scmtips.show('error', groupMembers.opFaild);
  }
}

// 群组成员模块-生成该成员简历(默认NSFC格式)
Group.addMemberResume = function(des3PsnId, index) {
  var ctx = "/scmwebsns";
  var styles = "blue_resume";
  var purposeConf = "10";
  var resumeName = $.trim($("#psn_" + index).text()) + "的简历";
  var language = "zh-CN";
  var bodyConf = "01,02,05,04,08,11";
  var des3NodeId = "Etn8ea4p6is%3D";
  var data = {
    "des3PsnId" : des3PsnId,
    "styles" : styles,
    "purposeConf" : purposeConf,
    "resumeName" : resumeName,
    "language" : language,
    "bodyConf" : bodyConf
  };
  $.ajax({
    url : ctx + '/ajaxpersonalResume/addresume',
    type : 'post',
    dataType : 'json',
    data : data,
    async : false,
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      } else {
        $("#resume_id").val(data.resumeId);
        window.location.href = ctx + "/ajaxpersonalResume/viewword?resumeId=" + data.resumeId + "&des3NodeId="
            + des3NodeId + "&des3PsnId=" + des3PsnId + "&language=" + language;
      }
    },
    error : function() {
      $.scmtips.show('error', groupMembers.opFaild);
    }
  });
}

// 群组成员模块-成员待审核列表
Group.groupPendingList = function(dataJson) {
  var url = "/groupweb/groupmodule/ajaxpendinglist";
  if (dataJson == null) {
    dataJson = {
      'des3GroupId' : $("#des3GroupId").val()
    };
  }
  ajaxparamsutil.doAjax(url, dataJson, Group.groupPendingListHtml, 'html', 'post');
}

// 群组成员模块-成员待审核列表回调
Group.groupPendingListHtml = function(data, dataJson) {
  $("#pendingList").remove();
  $("#memberList").remove();
  $("#con_two_2").append(data);
  // if ($("#PendingCount").val() > 0) {
  // $(".num_tip").text($("#PendingCount").val());//刷新显示申请中人数
  // }
  if ($.trim($("#PendingCount").val()) < 1) {
    if (!(dataJson.searchPending == "searchPending")) {
      $("#two2").hide();
      $("#two1").click();
      // Group.groupMemberListening();
      Group.groupMemberList();
    }
  }
}

// 群组成员模块-成员审核操作-接受
Group.acceptMember = function(des3InvitePsnId, index) {
  $("#accept_a_" + index).removeAttr("onclick");
  var ctx = "/scmwebsns";
  var groupNodeId = "1";
  var des3GroupId = $("#des3GroupId").val();
  var groupIdList = [];
  groupIdList.push({
    'des3InvitePsnId' : des3InvitePsnId
  });
  var des3InvitePsnIdStr = JSON.stringify(groupIdList);
  var data = {
    "groupPsn.des3GroupId" : des3GroupId,
    "groupPsn.groupNodeId" : groupNodeId,
    "des3InvitePsnIdStr" : des3InvitePsnIdStr
  };
  $.ajax({
    url : ctx + '/group/ajaxGroupMemberApprove',
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      groupDynamic.doTimeOut(data, 'json');
      if (data.result == "success") {
        $("#pending_" + index).hide();
        $(".num_tip").text(parseInt($(".num_tip").text() - 1));
        $("#group_sumMember").text(
            (Number($("#group_sumMember").html().replace(/\D+/g, "")) + 1)
                + $("#group_sumMember").text().replace(/\d+/g, ""));// 移除成员成功,简介处的成员数也相应增加
        $("#pendingList").remove();// 下面列表刷新之前先清一次
        Group.groupPendingList();// 这里刷新一次列表,是因为分页数据有可能变化
      }
    },
    error : function() {
      $.scmtips.show('error', groupMembers.opFaild);
    }
  });
}

// 群组成员模块-成员审核操作-忽略
Group.ignoreMember = function(des3InvitePsnId, index) {
  var ctx = "/scmwebsns";
  var groupNodeId = "1";
  var des3GroupId = $("#des3GroupId").val();
  var groupIdList = [];
  groupIdList.push({
    'des3InvitePsnId' : des3InvitePsnId
  });
  var des3InvitePsnIdStr = JSON.stringify(groupIdList);
  var data = {
    "groupPsn.des3GroupId" : des3GroupId,
    "groupPsn.groupNodeId" : groupNodeId,
    "des3InvitePsnIdStr" : des3InvitePsnIdStr
  };
  $.ajax({
    url : ctx + '/group/ajaxGroupMemberRefuse',
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      groupDynamic.doTimeOut(data, 'json');
      if (data.result == "success") {
        $("#pending_" + index).hide();
        $(".num_tip").text(parseInt($(".num_tip").text() - 1));
        $("#pendingList").remove();// 下面列表刷新之前先清一次
        Group.groupPendingList();// 这里刷新一次列表,是因为分页数据有可能变化
      }
    },
    error : function() {
      $.scmtips.show('error', groupMembers.opFaild);
    }
  });
}

// 群组成员模块-邀请成员加入列表
Group.inviteMemberJion = function() {
  var url = "/psnweb/friend/ajaxinvitefriendjion";
  dataJson = {
    'des3GroupId' : $("#des3GroupId").val()
  };
  ajaxparamsutil.doAjax(url, dataJson, Group.inviteMemberJionListHtml, 'html', 'post');
}

// 群组成员模块-邀请成员加入列表回调
Group.inviteMemberJionListHtml = function(data) {
  $(".invite_jion_list").remove();
  $("#invite_member").append(data);
}

// 群组成员模块-邀请成员加入操作-邀请
Group.inviteMember = function(des3PsnId, index) {
  var ctx = "/scmwebsns";
  var inviteTitle = "";
  var inviteBody = "";
  var groupUrl = ctx + "/group/view?menuId=31&groupPsn.des3GroupNodeId=Etn8ea4p6is%3D&groupPsn.des3GroupId="
      + $("#des3GroupId").val();
  var groupNodeId = 1;
  var des3GroupId = $("#des3GroupId").val();
  var memberInfoList = [];
  memberInfoList.push({
    'val' : des3PsnId,
    'text' : $("#invite_name_" + index).text()
  });
  var psnIds_emails = JSON.stringify(memberInfoList);
  $.ajax({
    url : ctx + "/group/ajaxGroupMemberInvite",
    type : 'post',
    dataType : 'json',
    data : {
      "psnIds_emails" : psnIds_emails,
      "inviteTitle" : inviteTitle,
      "inviteBody" : inviteBody,
      "groupPsn.groupUrl" : groupUrl,
      "groupPsn.groupNodeId" : groupNodeId,
      "groupPsn.des3GroupId" : des3GroupId
    },
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }
    },
    error : function() {
      $.scmtips.show('error', groupMembers.opFaild);
    }
  });
  if ($(".psn_hide").length == 0) {
    setTimeout(function() {
      Group.inviteMemberJion();
    }, 1000);
  } else {
    $("#invite_join_" + index).remove();
    $(".psn_hide:first").show();
    $(".psn_hide:first").removeClass("psn_hide");
  }
}

// =================群组成员列表监听=============start
Group.groupMemberListening = function() {
  var searchFlag = false;
  // 检索框样式
  $("#memberSearch").keyup(function() {
    var searchkey = $(this).val();
    if (searchkey != "" && searchkey.length > 0) {
      $(this).css("color", "#333");
    }
  }).focus(function() {
    searchFlag = true;
  }).blur(function() {
    searchFlag = false;
  });
  document.onkeydown = function(event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];
    if (searchFlag && e && e.keyCode == 13) { // enter 键
      Group.searchMember();
    }
  };
  $("#member_search_btn").click(function(event) {
    Group.searchMember();
  });
}

// 群组成员模块-查找成员
Group.searchMember = function() {
  Group.groupMemberList(Group.getGroupMemberParam());
}

// 群组成员列表获取参数(排序、翻页调用)
Group.getGroupMemberParam = function() {
  var data = {
    "searchKey" : $("#memberSearch").val(),
    "des3GroupId" : $("#des3GroupId").val(),
    "page.pageNo" : $("#pageNo").val(),
    "page.pageSize" : $("#pageSize").val(),
    "groupOpenType" : $.trim($("#_groupOpenType").val()),
    "currentPsnGroupRoleStatus" : $.trim($("#currentPsnGroupRoleStatus").val())
  };
  return data;
}
// =================群组成员列表监听=============end

// ==================群组成员待审核列表监听=================start
Group.groupPendingListening = function() {
  var searchFlag = false;
  // 检索框样式
  $("#pendingSearch").keydown(function() {
    var searchkey = $(this).val();
    if (searchkey != "" && searchkey.length > 0) {
      $(this).css("color", "#333");
    }
  }).focus(function() {
    searchFlag = true;
  }).blur(function() {
    searchFlag = false;
  });
  document.onkeydown = function(event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];
    if (searchFlag && e && e.keyCode == 13) { // enter 键
      Group.searchPending();
    }
  };
  $("#pending_search_btn").click(function(event) {
    Group.searchPending();
  });
}

// 群组成员模块-查找成员
Group.searchPending = function() {
  Group.groupPendingList(Group.getGroupPendingParam());
}

// 群组成员待审核列表获取参数(排序、翻页调用)
Group.getGroupPendingParam = function() {
  var data = {
    "searchKey" : $("#pendingSearch").val(),
    "des3GroupId" : $("#des3GroupId").val(),
    "page.pageNo" : $("#pageNo").val(),
    "page.pageSize" : $("#pageSize").val(),
    "searchPending" : "searchPending"
  };
  return data;
}
// ==================群组成员待审核列表监听===================end

// ---------------------群组成员模块 -----------------------------end

// ---------------------群组文件模模块 -----------------------------

// 跳转至群组文件模块
Group.toModuleFile = function(des3GroupId) {

  Group.changeUrl("file");
  var url = "/groupweb/groupmodule/ajaxgroupfile";
  var data = {
    groupOpenType : $.trim($("#_groupOpenType").val()),
    'des3GroupId' : $("#des3GroupId").val()
  };
  ajaxparamsutil.doAjax(url, data, Group.groupFileHtml, 'html');
}

// 获取参数(排序、翻页公共调用)
Group.getGroupFileParam = function() {
  var data = {
    searchKey : $("#fileSearch").val(),
    des3GroupId : $("#des3GroupId").val(),
    "page.pageNo" : $("#pageNo").val(),
    "page.pageSize" : $("#pageSize").val()
  };
  return data;
}

// 群组文件模块html
Group.groupFileHtml = function(data) {
  $(".group_box").remove();
  $("#file_edit_div").remove();
  $("#groupContent").append(data);
}

// ---------------------群组文件模模块 删除文件 ------------------------------

// 调用删除群组文件ajax
Group.deleteGroupFile = function(des3GroupFileId, index) {
  var url = "/groupweb/groupopt/ajaxgroupdeletefile";
  var data = {
    'des3GroupId' : $("#des3GroupId").val(),
    'des3GroupFileId' : des3GroupFileId,
    'index' : index
  };
  ajaxparamsutil.doAjax(url, data, Group.deleteGroupFileResult, 'json');
}
// 删除群组文件ajax回调
Group.deleteGroupFileResult = function(data, dataJson) {
  if (data.result == 'success') {
    $.scmtips.show('success', groupDetailFiles.opSuccess);
    $("#fileIndex_" + dataJson["index"]).hide();
    // Group.myFileList();
    Group.toModuleFile($.trim($("#des3GroupId").val()));
  } else {
    if (data.result == "NoPermissions") {
      $.scmtips.show('warn', "对不起，你不能删除他人的文件");
    } else {
      $.scmtips.show('warn', groupDetailFiles.fileMaxSize);
    }
  }
}
// ---------------------群组文件模模块 编辑文件------------------------------
Group.editGroupFile = function(des3GroupFileId, index) {
  var _flag;
  $.ajax({
    type : "post",
    url : "/groupweb/groupopt/ajaxgroupmyfilelist",
    data : {
      "des3GroupId" : $("#des3GroupId").val(),
      "des3GroupFileId" : des3GroupFileId
    },
    dataType : "json",
    async : false,
    success : function(data) {
      if (data.result == "error") {
        _flag = "notEdit"
        $.scmtips.show("warn", "只能修改自己的文件");
        return false;
      }
    },
    error : function() {
      $.scmtips.show('warn', groupDetailFiles.opFaild);
    }
  });
  if (_flag != "notEdit") {
    // 修改文件信息
    $('#_groupFileId').val(des3GroupFileId);
    var description = $.trim($('#viewFileDesc_' + index).html().replace(/</g, "&lt;").replace(/>/g, "&gt;"));
    if ($.browser && !$.browser.msie) {// 不是IE浏览器
      var regS = new RegExp("<br>", "gi");
      description = description.replace(regS, "\r\n");
    }
    $('#file_description').html(description);
    common.divWordCount('file_description', 200);
    if (description.length != 0) {
      $('#file_description').removeClass('watermark');
      $('#file_description').css('color', '#000');
    } else {
      $('#file_description').html(fileDescMaxSize);
      $('#file_description').addClass('watermark');
    }

    $("#showFileEdit").attr("alt", "/#TB_inline?height=230&amp;width=570&amp;inlineId=edit_file_floatDiv");
    $("#showFileEdit").click();
  }
}

Group.editGroupFileWatermark = function(thiz) {
  // 修改文件信息
  $("#file_description").click(function() {
    if (fileDescMaxSize == $.trim($('#file_description').html()) || $.trim($('#file_description').html()).length == 0) {
      $('#file_description').html("");
      $('#file_description').removeClass('watermark');
      $('#file_description').css('color', '#000');
      $("#file_description_countLabel").text(0);
      return;
    }
    if ($.trim($('#file_description').html()).length != 0) {
      $('#file_description').removeClass('watermark');
      $('#file_description').css('color', '#000');
    } else {
      $('#file_description').html(fileDescMaxSize);
      $('#file_description').addClass('watermark');
    }
  }).blur(function() {
    if ($.trim($('#file_description').html()).length == 0) {
      $('#file_description').html(fileDescMaxSize);
      $('#file_description').addClass('watermark');
      return;
    }
  });
}

// 群组文件编辑修改
Group.editSaveGroupFile = function() {
  var description = $.trim($('#file_description').text());
  // 等于水印字符
  if (description == $.trim(fileDescMaxSize)) {
    description = "";
  }
  if (description.length > 200) {
    $.scmtips.show('warn', groupDetailFiles.fileMaxSize);
  } else {
    var url = "/groupweb/groupopt/ajaxgroupfiledescredit";
    var data = {
      'des3GroupFileId' : $("#_groupFileId").val(),
      'fileDesc' : description,
      'des3GroupId' : $("#des3GroupId").val()
    };
    ajaxparamsutil.doAjax(url, data, Group.editSaveGroupFileResult, 'json');
  }
}
// 群组文件编辑修改回调
Group.editSaveGroupFileResult = function(data) {
  if (data.result == 'success') {
    $.scmtips.show('success', groupDetailFiles.opSuccess);
    parent.$.Thickbox.closeWin();
    Group.searchFile();
  } else {
    $.scmtips.show('warn', groupDetailFiles.opFaild);
  }
}
// ---------------------群组文件模模块 检索 ------------------------------
// 群组文件监听
Group.groupFileListening = function() {
  var searchFlag = false;
  // 检索框样式
  $("#fileSearch").keyup(function() {
    var searchkey = $(this).val();
    if (searchkey != "" && searchkey.length > 0) {
      $(this).css("color", "#333");
    } else {
      $(this).css("color", "#d1d1d1")
    }
  }).blur(function() {
    searchFlag = false;
    var searchkey = $(this).val();
    if (searchkey != "" && searchkey.length > 0) {
      $(this).css("color", "#333");
    } else {
      $(this).css("color", "#d1d1d1")
    }
  }).focus(function() {
    searchFlag = true;
  });
  document.onkeydown = function(event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];
    if (searchFlag && e && e.keyCode == 13) { // enter 键
      Group.searchFile();
    }
  };
  $(".s_btn_small").click(function(event) {
    Group.searchFile();
  });
}

Group.searchFile = function() {
  Group.groupFileList(Group.getGroupFileParam());
}
// ---------------------群组文件模模块 上传文件 ------------------------------
Group.groupUploadFile = function() {
  $("#doUploadFile_btn").attr("alt", "");
  var groupPsnList = [{
    'des3GroupId' : encodeURIComponent($("#des3GroupId").val()),
    'groupNodeId' : 1,
    'navAction' : "groupFiles",
    "newflag" : 1
  }];
  var groupIdNodeIdStr = JSON.stringify(groupPsnList);
  var groupfolderId = $("#groupFolderId").val();
  groupIdNodeIdStr = escape(groupIdNodeIdStr);
  var alt = snsctx + '/group/fileaddmain?groupIdNodeIdStr=' + groupIdNodeIdStr + "&TB_iframe=true&height=245&width=570";

  $("#doUploadFile_btn").attr("title", groupDetailFiles.selectUploadFile);
  $("#doUploadFile_btn").attr("alt", alt);
  $("#doUploadFile_btn").click();
}

// ---------------------群组文件模模块 文件列表 ------------------------------

// 群组文件模块-文件列表
Group.groupFileList = function(dataJson) {
  var url = "/groupweb/groupmodule/ajaxgroupfilelist";
  if (dataJson == null) {
    dataJson = {
      'currentPsnGroupRoleStatus' : $("#currentPsnGroupRoleStatus").val(),
      'des3GroupId' : $("#des3GroupId").val()
    };
  }
  var groupFileName = $("#fileSearch").val();
  if (groupFileName != '' && groupFileName != null && typeof dataJson.searchKey == 'undefined') {
    dataJson = {
      'des3GroupId' : $("#des3GroupId").val(),
      'searchKey' : groupFileName
    };
  }

  ajaxparamsutil.doAjax(url, dataJson, Group.groupFileListHtml, 'html');
}
// 群组文件模块-文件列表回调
Group.groupFileListHtml = function(data) {
  $("#fileList").remove();
  $(".lt_box").append(data);
}

// ---------------------群组文件模模块 我的文件夹 ------------------------------
// 我的文件夹ajax
Group.myFileList = function(isFirst) {
  var url = "/psnweb/myfile/ajaxfileforgroup";
  var data;
  var pageNo2 = parseInt($("#myfilepageno").val());
  if (pageNo2 == 1) {
    pageNo2 += 1;
  }
  if (isFirst == null || isFirst == true) {
    data = {
      'des3GroupId' : $("#des3GroupId").val(),
      'des3PsnId' : $("#des3PsnId").val(),
      'groupOpenType' : $.trim($("#_groupOpenType").val())
    };
  } else {
    data = {
      'des3GroupId' : $("#des3GroupId").val(),
      'des3PsnId' : $("#des3PsnId").val(),
      'pageNo' : pageNo2,
      'pageSize' : 1,
      'groupOpenType' : $.trim($("#_groupOpenType").val())
    };
  }

  ajaxparamsutil.doAjax(url, data, isFirst == null || isFirst == true ? Group.myFileListHtml : Group.myFileSubListHtml,
      'html');
}

// 我的文件夹ajax回调
Group.myFileListHtml = function(data, dataJson) {
  var groupFileListTable = document.getElementById("groupMyFile");
  $(".group_r").replaceWith(data);
  // if(groupFileListTable!=null){
  // $(groupFileListTable).remove();
  // }
  // $(".group_l").after(data);
}

// 我的文件夹ajaxSub回调
Group.myFileSubListHtml = function(data, dataJson) {
  $("#myFileTable").append(data);
}

// 从我的文件夹添加文件至群组ajax
Group.addGroupFileFromMyFile = function(des3FileId, indexPageNo) {
  var url = "/groupweb/groupopt/ajaxgroupaddfile";
  var data = {
    'des3GroupId' : $("#des3GroupId").val(),
    'des3StataionFileId' : des3FileId,
    'index' : indexPageNo
  };
  ajaxparamsutil.doAjax(url, data, Group.addGroupFileFromMyFileResult, 'json');
}
// 从我的文件夹添加文件至群组回调
Group.addGroupFileFromMyFileResult = function(data, dataJson) {
  if (data.result == 'success') {
    $("#myfile_" + dataJson["index"]).hide();
    var trList = document.getElementById("myFileTable").getElementsByTagName("tr");
    var num = 0;
    for (var i = 0; i < trList.length; i++) {
      if ($(trList[i]).css("display") == "none") {
        num++;
      }
    }
    if (num == trList.length) {
      $("#groupMyFile").hide();
    }
    var filePageNoArray = document.getElementsByName("filePageNo");
    if (filePageNoArray.length > 0) {
      $("#myfilepageno").val(filePageNoArray[filePageNoArray.length - 1].value);
    }
    Group.groupFileList();
    Group.myFileList(false);
  } else {
    alert("操作失败!");
  }
}

// ---------------------群组文件模模块 -----------------------------

/**
 * 简介
 */
Group.toModuleBrief = function() {
  Group.changeUrl("brief");
  var url = "/groupweb/groupmodule/ajaxintro";
  var data = {
    'des3GroupId' : $("#des3GroupId").val()
  };
  ajaxparamsutil.doAjax(url, data, Group.groupBriefHtml, 'html', 'post');
}
Group.groupBriefHtml = function(data) {
  $(".group_nav").find(".hover").removeClass("hover");
  $("#brief").addClass("hover");
  $(".group_box").remove();
  $("#groupContent").append(data);
}
// 切换到群组成果
Group.toModulePubs = function(des3GroupId) {
  $(".group_box").remove();
  $(".border-top").remove();
  Group.changeUrl("pub");
  // 群组成果编辑，回到相应的页面
  var groupPubEdit = $("#gp_groupPubEdit").val();
  if ("true" == groupPubEdit) {
    var data = {
      backType : 1,
      menuId : 31,
      des3GroupId : des3GroupId,
      "screenRecords" : $("#gp_screenRecords").val(),
      "screenPubTypes" : $("#gp_screenPubTypes").val(),
      "screenYears" : $("#gp_screenYears").val(),
      "orderBy" : $("#gp_orderBy").val(),
      "page.pageNo" : $("#gp_pageNo").val(),
      "page.pageSize" : $("#gp_pageSize").val(),
      searchKey : $("#gp_searchKey").val(),
      groupNodeId : 1
    };

    var objDiv = $("#group_pub_list");
    objDiv.css("height", objDiv.height() > 300 ? objDiv.height() : 300 + "px");
    objDiv.doLoadStateIco({
      style : "height: 28px; width:28px; margin:auto;margin-top:50px;",
      status : 1
    });
    ajaxparamsutil.doAjax("/groupweb/grouppub/ajaxshowmain", data, function(data) {
      $("#groupbox").remove();
      $(".group_box").remove();
      $("#groupContent").append(data);
    }, "html");

    Group.loadGroupPubMembers();
    return;
  }

  if (typeof searchKey == undefined && searchKey == null) {// 为了群组新增成果的链接能够跳到成果列表中的当条成果
    var searchKey = '';
  }

  var data = {
    backType : 1,
    menuId : 31,
    des3GroupId : des3GroupId,
    "page.pageNo" : $("#pageNo").val(),
    "page.pageSize" : $("#pageSize").val(),
    groupNodeId : 1,
    searchKey : searchKey
  };
  Group.loadGroupPubs(data, 1, 1);
  setTimeout(function() {
    Group.loadGroupPubMembers();
  }, 1);
}
// 群组成果获取参数(排序、翻页公共调用)
;
Group.getGroupParam = function() {
  var dataJson = ajaxparamsutil.getParams("#screen_box");
  var data = {
    screenRecords : dataJson.screenRecords,
    screenPubTypes : dataJson.screenPubTypes,
    screenYears : dataJson.screenYears,
    "page.orderBy" : $("#_orderBy").val(),
    searchKey : $("#input_default01").val(),
    des3GroupId : $("#des3GroupId").val(),
    "page.pageNo" : $("#pageNo").val(),
    "page.pageSize" : $("#pageSize").val()
  };
  return data;
}
// 加载群组成果(排序、翻页、检索公共调用)
;
Group.loadGroupPubs = function(data, i, timeout) {
  var mytime = 500;// 纯粹是为了ie动画效果，没有什么特殊逻辑意义
  if (timeout != null && timeout != 'undefined') {
    mytime = 1;
  }
  var objDiv = $("#group_pub_list");
  objDiv.css("height", objDiv.height() > 300 ? objDiv.height() : 300 + "px");
  objDiv.doLoadStateIco({
    style : "height: 28px; width:28px; margin:auto;margin-top:50px;",
    status : 1
  });
  setTimeout(function() {
    ajaxparamsutil.doAjax("/groupweb/grouppub/ajaxshowmain", data, function(data) {
      if (i == 3) {//
        objDiv.css("height", "");
        objDiv.html(data);
        Group.addGroupPubCount("", false);
      } else if (i == 2) {
        $("#groupbox").remove();
        $(".group_box").remove();
        $("#groupContent").append(data);
        Group.addGroupPubCount("", false);
      } else if (i == 1) {
        $("#groupbox").remove();
        $(".group_box").remove();
        $("#groupContent").append(data);
      } else {
        objDiv.css("height", "");
        objDiv.html(data);
      }
    }, "html");
  }, mytime);
}
// 加载群组成果成员
;
Group.loadGroupPubMembers = function(i) {
  var data = {
    des3GroupId : $("#des3GroupId").val(),
    backType : i
  };
  ajaxparamsutil.doAjax("/groupweb/grouppub/ajaxgrouppubmembers", data, function(data) {
    if (i == 1) {
      $("#group_r_pubmember").html(data);
    } else {
      $("#grouppubmembers").html(data);
    }

  }, "html");
}

Group.memberPubsImport = function() {
  var artType = $(parent.document).find(":input[id=navAction]").val();
  var articleType = 1;
  if (artType == 'groupPubs') {
    articleType = 1;
  } else if (artType == 'groupRefs') {
    articleType = 2;
  } else {
    $("#flag").val("1");
    return;
  }
  var pubList = [];
  var checkboxs = $("input:checked[name=membpubcheck]").not(":disabled");
  if (checkboxs.length == 0) {
    if (articleType == "1") {
      $.scmtips.show('warn', groupDetailPubs.selectPub);
    } else if (articleType == "2") {
      $.scmtips.show('warn', groupDetailPubs.selectDocument);
    }
    $("#flag").val("1");
    return;
  }
  checkboxs.each(function(i, cb) {
    pubList.push({
      des3Id : $(cb).val(),
      des3GroupId : $("#des3GroupId").val()
    });
  });

  var des3PubIdsStr = JSON.stringify(pubList);
  var url = "";
  if (articleType == "1") {
    url = '/pubweb/publication/ajaxGroupPubsImport';
  } else if (articleType == "2") {
    url = '/pubweb/publication/ajaxGroupPubsImport';
  }
  var data = {
    "articleType" : articleType,
    "des3PubIdsStr" : des3PubIdsStr,
    "des3GroupId" : $("#des3GroupId").val(),
    "groupNodeId" : 1
  };
  ajaxparamsutil.doAjax(url, data, Group.memberPubsImportResult);
}

Group.memberPubsImportResult = function(data, dataJson) {
  if (data.result == 'success') {
    var checkboxs = $("input:checked[name=membpubcheck]").not(":disabled");
    if (checkboxs.length > 0) {
      checkboxs.each(function(i, cb) {
        $(cb).attr("disabled", true);
      });
    }
    $.scmtips.show('success', groupDetailPubs.optSuccess);
    // 标识成果导入成果为true
    $("#memberPubsImport").val(true);
  } else {
    // $.scmtips.show('error',groupDetailPubs.addFailed);
  }
}
//
;
Group.checkedListAllmemberPubs = function() {
  var checkboxs = $("input[name=membpubcheck]").not(":disabled");
  if (checkboxs.length > 0) {
    if ($("#filled-in-box001").attr("checked") == "checked") {
      checkboxs.each(function(i, cb) {
        $(cb).attr("checked", "checked");
      });
    } else {
      checkboxs.each(function(i, cb) {
        $(cb).removeAttr("checked");
      });
    }

  }
}

// 群组成果成员详情
;
Group.detailsGroupPubMember = function(des3PsnId) {
  window.open("/scmwebsns/resume/view?des3PsnId=" + encodeURIComponent(des3PsnId));
}
// 删除群组成果
;
Group.delGroupPub = function(obj) {
  var _obj = $("#" + obj);
  var groupRole = $("#groupRole").val();
  var des3PsnId = _obj.find(".pub_des3PsnId_class").val();
  var groupId = _obj.find(".pub_groupId_class").val();
  var ownerPsnId = _obj.find(".pub_ownerId_class").val();
  var des3PubId = _obj.find(".inputpubId").val();
  var des3groupPubsId = _obj.find(".group_des3PubsId_class").val();
  var pubId = _obj.find(".pub_pubId_class").val();
  if (des3PsnId != ownerPsnId && groupRole != "1" && groupRole != "2") {
    $.scmtips.show("warn", groupDetailPubs.noRemovePubsNotYou);
    return;
  }
  var params = [];
  params.push({
    'nodeId' : 1,
    'pubId' : pubId,
    'ownerId' : ownerPsnId,
    'groupPubsId' : des3groupPubsId,
    'groupId' : groupId
  });
  var jsonParams = JSON.stringify(params);
  var postData = {
    "jsonParams" : jsonParams,
    "articleType" : 1
  };
  ajaxparamsutil.doAjax(snsctx + "/grouppub/ajaxremovepubfromgroup", postData, function(data) {
    if (data) {
      if (data.result == 'success') {
        $.scmtips.show('success', data.msg);
        Group.addGroupPubCount(-1, true);
        Group.toModulePubs(groupId);
      }
    }
  }, "json");
}
// 修改群组头部成果数量
Group.addGroupPubCount = function(addcount, status) {
  var sumPubCount = $("#totalCount").val();
  if (sumPubCount == null || typeof sumPubCount == "undefined" || sumPubCount == "") {
    sumPubCount = 0;
  }
  if (status) {// 增加
    if (typeof (sumPubCount) != 'undefined' && $.trim($("#input_default01").val()) == ""
        && $("#screen_box").find(".hover").size() == 0) {
      $("#group_sumPub").html($("#group_sumPub").html().replace(/\d+/g, Number(sumPubCount) + Number(addcount)));
    }
  } else {// 替换
    if (addcount == "") {
      addcount = sumPubCount;
    }
    $("#group_sumPub").html($("#group_sumPub").html().replace(/\d+/g, addcount));
  }
}
// 检索群组成果
;
Group.searchGroupPub = function(obj) {
  var data = {
    backType : 1,
    searchKey : $("#input_default01").val(),
    "page.pageSize" : $("#pageSize").val(),
    des3GroupId : $("#des3GroupId").val()
  };
  Group.loadGroupPubs(data, 1);
  Group.loadGroupPubMembers();
}
// 筛选下拉框
;
Group.pubScreenBoxLoad = function(obj, e) {
  var _obj = $(obj);
  // 显示隐藏
  if (_obj.is(":hidden")) {
    _obj.show();

  } else {
    _obj.hide();
    return;
  }
  // 点击其他地方关闭
  /*
   * if (e && e.stopPropagation) {//非IE e.stopPropagation(); } else {//IE window.event.cancelBubble =
   * true; } $(document).click(function(){_obj.hide();});
   */
}
// 成果筛选下拉框取消按钮
;
Group.pubScreenBoxCancel = function(obj) {
  $("#screen_box").hide();
}
// 成果筛选下拉框确定按钮
;
Group.pubScreenBoxConfirm = function() {
  var dataJson = ajaxparamsutil.getParams("#screen_box");
  var data = {
    screenRecords : dataJson.screenRecords,
    screenPubTypes : dataJson.screenPubTypes,
    screenYears : dataJson.screenYears,
    orderBy : $("#_orderBy").val(),
    searchKey : $("#input_default01").val(),
    des3GroupId : $("#des3GroupId").val(),
    "page.pageNo" : $("#pageNo").val(),
    "page.pageSize" : $("#pageSize").val()
  };
  Group.loadGroupPubs(data);
}
// 成果筛选下拉框加载--年份
;
Group.pubScreenBoxLoadYear = function(obj, myfunction, newestpublishYear) {
  var currentYear = new Date().getFullYear();
  $(obj).empty();
  $(obj).append(
      "<div class='filter_list_value existed'><div class='text' onclick='Group.pubScreenBoxHit(this,0," + myfunction
          + ")'>" + currentYear + "年以后</div><div class='delete' onclick='#' paramValue='" + currentYear
          + "'><i class='material-icons'>&#xe5cd;</i></div></div>").append(
      "<div class='filter_list_value existed'><div class='text' onclick='Group.pubScreenBoxHit(this,0," + myfunction
          + ")'>" + (Number(currentYear) - 1) + "年以后</div><div class='delete' onclick='#' paramValue='"
          + (Number(currentYear) - 1) + "'><i class='material-icons'>&#xe5cd;</i></div></div>").append(
      "<div class='filter_list_value existed'><div class='text' onclick='Group.pubScreenBoxHit(this,0," + myfunction
          + ")'>近" + 5 + "年</div><div class='delete' onclick='#' paramValue='" + (Number(currentYear) - 5)
          + "'><i class='material-icons'>&#xe5cd;</i></div></div>").append(
      "<div class='filter_list_value existed'><div class='text' onclick='Group.pubScreenBoxHit(this,0," + myfunction
          + ")'>近" + 10 + "年</div><div class='delete' onclick='#' paramValue='" + (Number(currentYear) - 10)
          + "'><i class='material-icons'>&#xe5cd;</i></div></div>");
  var $_li = $(obj).children();
  var differ = currentYear - Number(newestpublishYear);
  if (differ == 1) {
    $_li.eq(0).removeClass("existed");
    $_li.eq(0).children('.text').attr("onclick", "");
  } else if (differ < 6 && differ > 1) {
    $_li.each(function(i, n) {
      if (i == 2) {
        return false;
      }
      $(n).removeClass("existed");
      $(n).attr("onclick", "");
    });
  } else if (differ < 11 && differ > 5) {
    $_li.each(function(i, n) {
      if (i == 3) {
        return false;
      }
      $(n).removeClass("existed");
      $(n).attr("onclick", "");
    });
  } else if (differ > 10 || newestpublishYear == "") {
    $_li.each(function(i, n) {
      $(n).removeClass("existed");
      $(n).attr("onclick", "");
    });
  }
}
// 成果筛选下拉框加载--类别
;
Group.pubScreenBoxLoadPubType = function(pubTypesStr, obj, myfunction) {
  var $_li = $(obj).find("div[class='filter_list_value']");
  var pubTypesArr = pubTypesStr.split(",");
  $.each(pubTypesArr, function(i, n) {
    if (n != null && n != "") {
      var pubTypeId = Number(n);
      switch (pubTypeId) {
        case 4 :
          $_li.eq(0).addClass("existed");
          $_li.eq(0).children(".text").attr("onclick", myfunction);
        break;
        case 3 :
          $_li.eq(1).addClass("existed");
          $_li.eq(1).children(".text").attr("onclick", myfunction);
        break;
        case 8 :
          $_li.eq(2).addClass("existed");
          $_li.eq(2).children(".text").attr("onclick", myfunction);
        break;
        case 5 :
          $_li.eq(3).addClass("existed");
          $_li.eq(3).children(".text").attr("onclick", myfunction);
        break;
        default :
          $_li.eq(4).addClass("existed");
          $_li.eq(4).children(".text").attr("onclick", myfunction);
      }
    }
  });
}

// 重置查询条件
Group.resetFindParam = function() {
  $("#screen_box").find(".filter_list_value").each(function(i, obj) {
    if ($(obj).hasClass("active")) {
      $(obj).removeClass("active");
      $(obj).children(".delete").removeClass("paramSelected");
      return;
    }
  })
}

// 成果筛选下拉框加载--收录
;
Group.pubScreenBoxLoadRecords = function(recordStr, obj, myfunction) {
  var $_li = $(obj).find("div[class='filter_list_value']");
  var recordArr = recordStr.split(",");
  $.each(recordArr, function(i, n) {
    if (n != null && n != "" && n > 0) {
      $_li.eq(i).addClass("existed");
      $_li.eq(i).children(".text").attr("onclick", myfunction);
    }
  });
}
// 成果筛选下拉框点击事件 status=1为多选，status=0为单选
;
Group.pubScreenBoxHit = function(obj, status, myfunction) {
  var obj_p = $(obj).parent();
  if (!obj_p.find(".delete").hasClass("paramSelected")) {
    if (status == 1) {
      obj_p.find(".delete").addClass("paramSelected");
      obj_p.addClass("active")
    } else {
      obj_p.parent().find(".delete").removeClass("paramSelected");
      obj_p.find(".delete").addClass("paramSelected");

      obj_p.parent().find("div").removeClass("active");
      obj_p.addClass("active")
    }
  } else {
    obj_p.find(".delete").removeClass("paramSelected");
    obj_p.removeClass("active");
  }
  setTimeout(function() {
    myfunction();
  }, 1);
}

;
Group.getMousePos = function(event) {
  var e = event || window.event;
  return {
    'x' : e.screenX,
    'y' : screenY
  }
}
// 导入成员成果界面
;
Group.addMemberPubUI = function(des3PsnId, obj) {
  // 移除scoll事件SCM-10963
  $('body').css('overflow', 'hidden');

  $("#groupPubImport_Btn").removeClass("thickbox");
  $("#add_member_pub_flipper").addClass("active");
  var objLi = $("#grouppubmembers").find("div[des3psnid='" + $("#current_add_groupPub_psn").val() + "']");
  // 为了设置默认选中阴影
  if (objLi.length > 0) {
    objLi.addClass("raised");
  } else {
    $("#grouppubmembers").find(".person_dialog_namecard_whole:first").addClass("raised");
  }
  // 为了获取动画位置
  if (typeof objLi[0] == "undefined") {
    objLi = $("#grouppubmembers").find("img:first");
  }
  $('html,body').animate(
      {
        scrollTop : '180px'
      },
      280,
      "",
      function() {
        var li_top = objLi[0].getBoundingClientRect().top - 10;
        var li_left = objLi[0].getBoundingClientRect().left - 10;
        $("#content_sub").attr(
            "style",
            "height:70px; width:180px; top:" + li_top + "px; left:calc(14% + " + li_left
                + "px); position:absolute; opacity:0; transition:all 0.2s ease-out; z-index:100006;");
      });
  var timeOut = setTimeout(
      function() {
        var lastPsnId = $.trim($("#current_add_groupPub_psn").val());
        if (lastPsnId != "") {
          des3PsnId = lastPsnId;
        }
        $("#currentdes3PsnId").val(des3PsnId);
        $("#shelter2").show();
        $("#cont").show();
        $("#content_sub").show();
        $("#grouppubmembers").attr("style", "z-index:100005;");
        $("#content_sub")
            .attr(
                "style",
                "height:calc(100% - 16px); width:790px; top:8px; left:calc(50% - 592px); position:absolute; opacity:1; transition:all 0.2s ease-out; z-index:100006;");
        var des3GroupId = $("#des3GroupId").val();

        $("#content_sub").doLoadStateIco({
          style : "height: 28px; width:28px; margin:auto;margin-top:50px;",
          status : 1
        });
        $("#memberPub").addClass("active");
        $("#preloader").addClass("active");

        setTimeout(function() {
          Group.loadPsnOpenPubs(des3PsnId, des3GroupId, 1, 1);
          $("#cont").css("z-index", "100000");
          Group.convertAddPubMembers();
        }, 210);
        // 添加scoll事件SCM-10963
        $('body').css('overflow', 'scoll');
      }, 300);
}
// 群组成果排序
;
Group.clickPubSort = function(style, obj) {
  $(obj).closest(".filter_list_content").find(".filter_list_value").removeClass("active");
  $(obj).closest(".filter_list_value").addClass("active");
  $("#_orderBy").val(style);
  Group.loadGroupPubs(Group.getGroupParam());
}
// 点击 成员，加载成员的公开成果列表
;
Group.loadPsnOpenPubs = function(des3PsnId, des3GroupId, pageNo, i, obj) {
  $(obj).parent().find(".raised").removeClass("raised");
  setTimeout(function() {
    $(obj).addClass("raised");
  }, 8);
  $("#current_add_groupPub_psn").val(des3PsnId);
  var postData = {
    des3PsnId : des3PsnId,
    des3GroupId : des3GroupId,
    "page.pageNo" : pageNo
  };
  Group.selectPsnOpenPubs(postData, i);
}
// 群组成员成果排序
;
Group.clickPsnPubSort = function(style, obj) {
  $(obj).closest(".filter_list_content").find(".filter_list_value").removeClass("active");
  $(obj).closest(".filter_list_value").addClass("active");
  $("#_orderBy2").val(style);
  Group.doSelectPsnOpenPubs();
}
// 获取查询群组成员成果的参数
;
Group.getParamForPsnOpenPubs = function() {
  var dataJson = ajaxparamsutil.getParams("#screen_box2");
  var data = {
    "page.orderBy" : $("#_orderBy2").val(),
    "page.order" : "desc",
    screenPubTypes : dataJson.screenPubTypes,
    screenYears : dataJson.screenYears,
    screenRecords : dataJson.screenRecords,
    des3PsnId : $("#currentdes3PsnId").val(),
    des3GroupId : $("#des3GroupId").val(),
    "page.pageNo" : $("#pageNo").val(),
    "page.pageSize" : $("#pageSize").val(),
    searchKey : $("#input_default02").val()
  };
  return data;
}
// 查询成员的公开成果列表(排序，翻页，筛选共用)
;
Group.selectPsnOpenPubs = function(postData, i, timeout) {
  var mytime = 500;// 纯粹是为了ie动画效果，没有什么特殊逻辑意义
  if (timeout != null && timeout != 'undefined') {
    mytime = 1;
  }
  var url;
  var objDiv = $("#div2");
  if (objDiv != null && typeof objDiv != "undefined") {
    objDiv.css("height", objDiv.height() > 300 ? objDiv.height() : 300 + "px");
    objDiv.doLoadStateIco({
      style : "height: 28px; width:28px; margin:auto;margin-top:50px;",
      status : 1
    });
  }
  if (i == 1) {
    url = "/pubweb/publication/ajaxpsnopenpubsmain";
  } else {
    url = "/pubweb/publication/ajaxpsnopenpubslist";
  }
  setTimeout(function() {
    ajaxparamsutil.doAjax(url, postData, function(data) {
      if (i == 1) {
        $("#content_sub").html(data);
      } else {
        objDiv.css("height", "");
        objDiv.html(data);
      }
    }, "html");
  }, mytime);
}
// 执行查询
;
Group.doSelectPsnOpenPubs = function() {
  Group.selectPsnOpenPubs(Group.getParamForPsnOpenPubs());
}
// 检索成员的公开成果（按关键词）
Group.searchPsnOpenPubs = function() {
  var data = {
    des3PsnId : $("#currentdes3PsnId").val(),
    des3GroupId : $("#des3GroupId").val(),
    "page.pageSize" : $("#pageSize").val(),
    searchKey : $("#input_default02").val()
  };
  Group.selectPsnOpenPubs(data, 1);
}
// 返回群组成果
;
Group.backFormPsnOpenPubs = function(obj) {
  $("#add_member_pub_flipper").removeClass("active");
  $("#cont").css("z-index", "100006")
  var objLi = $("#grouppubmembers").find("div[des3psnid='" + $("#current_add_groupPub_psn").val() + "']");
  if (typeof objLi[0] == "undefined") {
    objLi = $("#grouppubmembers").find("img:first");
  }
  var li_top = objLi[0].getBoundingClientRect().top - 10;
  var li_left = objLi[0].getBoundingClientRect().left - 10;
  $("#content_sub").empty();
  $("#content_sub").attr(
      "style",
      "height:70px; width:180px; top:" + li_top + "px; left:calc(14% + " + li_left
          + "px); position:absolute; opacity:0; transition:all 0.2s ease-out; z-index:100006;");
  $("#grouppubmembers").find(".person_dialog_namecard_whole").each(
      function(i, n) {
        $(n).attr("onclick",
            "Group.detailsGroupPubMember('" + $(n).attr("des3psnid") + "','" + $("#des3GroupId").val() + "',1,1);");
        $(n).removeClass("elevation");
        $(n).removeClass("raised");
      });
  setTimeout(function() {
    $('body').css('overflow', 'scroll');
    $("#cont").hide();
    $("#shelter2").hide();
    setTimeout(function() {
      $("#groupPub_table").after(my_temp);
      // 因为返回要到第一页所以取参就不调用公共取参方法Group.getGroupParam()了
      var dataJson = ajaxparamsutil.getParams("#screen_box");
      var data = {};
      if ("true" == $("#memberPubsImport").val()) { // 说明已经导入了成果 ，重置条件
        $("#memberPubsImport").val(false)
        data = {
          des3GroupId : $("#des3GroupId").val(),
        }
        // 重置条件信息
        // Group.pubScreenBoxLoadYear("#screen_year",Group.pubScreenBoxConfirm,'${newestpublishYear}');
        Group.resetFindParam();
      } else {
        data = {
          screenRecords : dataJson.screenRecords,
          screenPubTypes : dataJson.screenPubTypes,
          screenYears : dataJson.screenYears,
          "page.orderBy" : $("#_orderBy").val(),
          searchKey : $("#input_default01").val(),
          des3GroupId : $("#des3GroupId").val(),
          "page.pageNo" : $("#pageNo").val(),
          "page.pageSize" : $("#pageSize").val()
        }
      }
      Group.loadGroupPubs(data);//
      $("#grouppubmembers").css("z-index", "");
      $("#groupPubImport_Btn").addClass("thickbox");
    }, 250);
  }, 200);
}
// 点击导入成员成果转换成果成员界面
var str = "<s:text name='group.pubs.backMemberPub' />";
var my_temp;// 暂存的分页doc SCM-10712
;
Group.convertAddPubMembers = function() {
  $('body').css('overflow', 'hidden');
  $('#groupbox2').css('overflow', 'auto');
  $("#grouppubmembers").find(".person_dialog_namecard_whole").each(
      function(i, n) {
        $(n).attr("onclick",
            "Group.loadPsnOpenPubs('" + $(n).attr("des3psnid") + "','" + $("#des3GroupId").val() + "',1,1,this);");
        $(n).addClass("elevation");
      });
  // 页面数据清理，防止冲突
  my_temp = $("#group_pub_list").find(".page").detach();
}
// 成果列表滚动条
;
Group.scroll = function(event, scroller) {
  var k = event.wheelDelta ? event.wheelDelta : -event.detail * 10;
  scroller.scrollTop = scroller.scrollTop - k;
  return false;
};
// 群组成果详情
;
Group.groupPubDetails = function(des3Id, des3GroupId, ownerPsnId) {
  var pram = {
    des3Id : des3Id
  };
  var params = "des3Id=" + encodeURIComponent(des3Id) + "&nodeId=1&des3GroupId=" + encodeURIComponent(des3GroupId)
      + "&des3OwnerId=" + encodeURIComponent(ownerPsnId) + "&currentDomain=/pubweb&pubFlag=1&newGroupPub=yes";
  var newwindow = window.open("about:blank");
  $.ajax({
    url : "/pubweb/publication/ajaxview",
    type : "post",
    data : pram,
    dataType : "json",
    success : function(data) {
      if (data.result == 2) {
        newwindow.location.href = data.shortUrl;
      }
      if (data.result == 1 || data.ajaxSessionTimeOut == 'yes') {
        newwindow.location.href = "/pubweb/outside/details?" + params;
      } else if (data.result == 0) {
        newwindow.location.href = "/pubweb/publication/wait?" + params;
        newwindow.focus();
      }
    },
    error : function() {
    }
  });
}
// 导入成果菜单
;
Group.groupPubImportUI = function() {
  $("#_showPubImportDiv").find(".ach_main_search_zh_CN").each(function(i, obj) {
    if (i > 0) {
      $(obj).next().remove();
      $(obj).remove();
    }
  });
  $("#_showPubImport").attr("alt", "/#TB_inline?height=350&amp;width=570&amp;inlineId=_showPubImportDiv");
  $("#_showPubImport").click();
}
// 导入成果
;
Group.groupPubImport = function(url) {
  $("#TB_title").remove();
  $("#TB_ajaxContent").hide();

  $("#_showPubImport").attr("alt", url);
  $("#_showPubImport").click();
}
// -----------------------群组创建-------------------------------------------------------------------
Group.nextCreateStep = function(groupType) {
  $(".group_step div:eq(1)").removeClass("step_line_grey");
  $(".group_step div:eq(1)").addClass("step_line_blue");
  $(".group_step div:eq(2)").removeClass("step_grey loca02");
  $(".group_step div:eq(2)").addClass("step_blue loca02");
  $(".group_step div:eq(2)").find("i").removeClass("step_grey_icon")
  $(".group_step div:eq(2)").find("i").addClass("step_blue_icon")
  $(".group_type").css("display", "none");
  $(".group_infro").css("display", "block");

  // 如果返回上一步钱前选中的是和现在选中的群组类型一样则不需要清空表单中已经填写的值，否则需要清空
  if ($("input[name='groupCategory']").val() != '' && $("input[name='groupCategory']").val() != groupType) {
    $("input[name='groupCategory']").val("");
    $("input[name='groupName']").val("");
    $("input[name='groupName']").removeClass("active");
    $("input[name='groupName']").removeClass("valid");
    $("#introduce").val("");
    $("#introduce").removeClass("active");
    $("#introduce").removeClass("valid");
    $("input[name='keyWords1']").val("");
    $("#firstCategoryShown").text("选择一个研究领域");
    $("#secondCategoryShown").text("选择二级研究领域");
    $("input[name='disciplines']").val("");
    $(".input_character_counter persistent").html("");
    $(".auto_word_div").remove();
  }
  if ($("input[name='groupName']").val() != '') {
    $("input[name='groupName']").addClass("active");// 从我的项目创建群组时
  }
  $("input[name='groupCategory']").val(groupType);
  if (groupType == '10') {
    $("input:radio:first").attr("checked", "checked");
  }
  if (groupType == '11') {
    $("input:radio:eq(1)").attr("checked", "checked");
  }
};

Group.saveCreateGroup = function() {
  if ($.trim($("input[name='groupName']").val()) == '') {
    $("input[name='groupName']").removeClass("normal");
    $("input[name='groupName']").removeClass("valid");
    $("input[name='groupName']").addClass("invalid");
    $("#groupNameCounter").css("display", "block");
    $("#groupNameCounter").attr("invalid_msg", "群组名不能为空");
    $(".button01 w86").removeAttr("onclick");
  } else {
    if ($("input[name='groupName']").val() != null && $("input[name='groupName']").val().length > 200) {
      var subStringVal = $("input[name='groupName']").val().substring(0, 200);
      $("input[name='groupName']").val(subStringVal);
    }
    if ($("#introduce").val() != null && $("#introduce").val().length > 4000) {
      var subStringVal = $("#introduce").val().substring(0, 4000);
      $("#introduce").val(subStringVal);
    }

    Group.replaceSpecialChar($("input[name='groupName']"));
    Group.replaceSpecialChar($("#introduce"));
    var result_chKey = "";
    $(".auto_word").each(function() {
      var textVal = Group.replaceSpecialChars($(this).text());
      result_chKey = result_chKey + textVal + ";";
    });
    var result_key = result_chKey.substring(0, result_chKey.lastIndexOf(";"));
    $("input[name='keyWords1']").val(result_key);
    $(".waves-effect:first").removeAttr("onclick");
    $(".waves-effect:first").removeAttr("href");
    $.ajax({
      type : "post",
      url : "/groupweb/creategroup/ajaxsave",
      data : {
        "keyWords1" : $("input[name='keyWords1']").val(),
        "groupDescription" : $("#introduce").val(),
        "groupName" : $.trim($("input[name='groupName']").val()),
        "groupCategory" : $("input[name='groupCategory']").val(),
        "disciplines" : $("input[name='disciplines']").val() == '0' || $("input[name='disciplines']").val() == ''
            ? ''
            : $("input[name='disciplines']").val(),
        "openType" : $("input:radio:checked").val(),
        "des3PrjId" : $("input[name='des3PrjId']").val()
      },
      dataType : "json",
      success : function(data) {
        if (data.result == 'success') {
          window.location.href = data.forwardUrl;
        } else {
          $.scmtips.show('error', "保存群组失败");
          $(".waves-effect:first").attr("onclick", "Group.saveCreateGroup()");
          $(".waves-effect:first").attr("href", "javascript:;");
        }
      },

      error : function(data) {
        $.scmtips.show('error', "保存群组失败");
        $(".waves-effect:first").attr("onclick", "Group.saveCreateGroup()");
        $(".waves-effect:first").attr("href", "javascript:;");
      }
    });

  }
};

Group.replaceSpecialChars = function(str) {
  var regex = /[<>]+/gi;// 替换尖括号
  var regx0 = /[“|”]/g;// 替换全角双引号
  var regex1 = /\'/g;// 替换半角单引号
  var regex2 = /\"/g;// 替换半角双引号
  var regex3 = /[‘|’]/g;// 替换全角单引号
  var strValue = str;
  if (regx0.test(str)) {
    strValue = str.replace(/[“|”]/g, "");
  };

  if (regex.test(str)) {
    strValue = str.replace(/[<>]+/gi, "");
  };

  if (regex1.test(str)) {
    strValue = str.replace(/\'/g, "");
  };

  if (regex2.test(str)) {
    strValue = str.replace(/\"/g, "");
  };

  if (regex3.test(str)) {
    strValue = str.replace(/[‘|’]/g, "");
  };
  return strValue;
};

Group.replaceSpecialChar = function(obj) {
  var regex = /[<>]+/gi;// 替换尖括号
  var regx0 = /[“|”]/g;// 替换全角双引号
  var regex1 = /\'/g;// 替换半角单引号
  var regex2 = /\"/g;// 替换半角双引号
  var regex3 = /[‘|’]/g;// 替换全角单引号
  var regx4 = /[;]/g;// 替换;号,群组名称和群组的关键字根据封号分隔开，如果在输入的时候有；号就会被分隔开成为多个关键词

  if (regx0.test($(obj).val())) {
    var values = $(obj).val().replace(/[“|”]/g, "");
    $(obj).val(values);
  };

  if (regex.test($(obj).val())) {
    var values = $(obj).val().replace(/[<>]+/gi, "");
    $(obj).val(values);
  };

  if (regex1.test($(obj).val())) {
    var values = $(obj).val().replace(/\'/g, "");
    $(obj).val(values);
  };

  if (regex2.test($(obj).val())) {
    var values = $(obj).val().replace(/\"/g, "");
    $(obj).val(values);
  };

  if (regex3.test($(obj).val())) {
    var values = $(obj).val().replace(/[‘|’]/g, "");
    $(obj).val(values);
  };
  if (regx4.test($(obj).val())) {
    var values = $(obj).val().replace(/[;]/g, "");
    $(obj).val(values);
  };
};

Group.saveEditGroup = function() {
  $("a.waves-effect.waves-light.button01 ").removeAttr("onclick");
  if ($.trim($("#GroupPsnForm").find("input[name='groupName']").val()) == '') {
    $("input[name='groupName']").removeClass("normal");
    $("input[name='groupName']").removeClass("valid");
    $("input[name='groupName']").addClass("invalid");
    $("#groupNameCounter").css("display", "block");
    $("#groupNameCounter").attr("invalid_msg", "群组名不能为空");
    $(".button01 w86").removeAttr("onclick");
    $("a.waves-effect.waves-light.button01 ").attr("onclick", "Group.saveEditGroup()");
  } else {
    if ($("#GroupPsnForm").find("input[name='groupName']").val() != null
        && $("#GroupPsnForm").find("input[name='groupName']").val().length > 200) {
      var subStringVal = $("input[name='groupName']").val().substring(0, 200);
      $("input[name='groupName']").val(subStringVal);
    }

    if ($("#introduce").val() != null && $("#introduce").val().length > 4000) {
      var subStringVal = $("#introduce").val().substring(0, 4000);
      $("input[name='groupDescription']").val(subStringVal);
    } else {
      $("input[name='groupDescription']").val($("#introduce").val());
    }
    Group.replaceSpecialChar($("input[name='groupName']"));
    Group.replaceSpecialChar($("input[name='groupDescription']"));
    var result_chKey = "";
    $(".auto_word").each(function() {
      var textVal = Group.replaceSpecialChars($(this).text());
      result_chKey = result_chKey + textVal + ";";
    });
    var result_key = result_chKey.substring(0, result_chKey.lastIndexOf(";"));
    $("input[name='keyWords1']").val(result_key);
    $("input[name='openType']").val($('input[name="type"]:checked ').val());
    $('#GroupPsnForm').attr("action", "/groupweb/groupedit/save");
    ajaxparamsutil.doAjax("/dynweb/ajaxtimeout", {}, function() {// 表单提交超时处理
      $('#GroupPsnForm').submit();
    });
  }
};

Group.toGroupEdit = function(des3GroupId) {
  var url = "/groupweb/groupedit/ajaxedit";
  dataJson = {
    'des3GroupId' : $("#des3GroupId").val()
  };
  ajaxparamsutil.doAjax(url, dataJson, Group.groupEditHtml, 'html', 'post');

};

Group.groupEditHtml = function(data) {
  $(".group_box").remove();
  $("#groupContent").append(data);
};

Group.goBack = function() {
  $(".group_step div:eq(1)").removeClass("step_line_blue");
  $(".group_step div:eq(1)").addClass("step_line_grey");
  $(".group_step div:eq(2)").removeClass("step_blue loca02");
  $(".group_step div:eq(2)").addClass("step_grey loca02");
  $(".group_step div:eq(2)").find("i").removeClass("step_blue_icon");
  $(".group_step div:eq(2)").find("i").addClass("step_grey_icon");
  $(".selector_dropdown_collections").hide();
  $(".group_type").css("display", "block");
  $(".group_infro").css("display", "none");

}
// Placeholder的字体颜色 兼容IE11处理
Group.compatibilityPlaceholder = function(obj) {
  checkPlaceholder(obj);
  obj.blur(function() {
    checkPlaceholder(obj);
  });
  obj.keyup(function() {
    checkPlaceholder(obj);
  });
}
Group.checkPlaceholder = function(obj) {
  if (obj != null && obj.val() != null && obj.val().length > 0) {
    obj.css("color", "#333");
  } else {
    obj.css("color", "#d1d1d1");
  }
}

// 申请群组
Group.applyForGroup = function(groupId) {
  var groupPsnList = [];
  groupPsnList.push({
    groupId : groupId
  });
  var groupIdNodeIdStr = JSON.stringify(groupPsnList);

  $.ajax({
    url : snsctx + '/group/ajaxGroupNode',
    type : 'post',
    dataType : 'json',
    data : {
      "groupIdNodeIdStr" : groupIdNodeIdStr
    },
    success : function(data) {
      var url = "/groupweb/groupmain/show?des3GroupId=" + $("#des3GroupId").val()
          + "&groupPsn.groupNodeId=1&currentPsnGroupRoleStatus=1";
      window.location.href = url;
    },
    error : function() {

    }
  });
}
// 群组成员邀请联系人
;
Group.selectFriendsForGroup = function() {
  $('body').css('overflow', 'hidden');
  $("#shelter2").show();
  $("#invite_GroupPsn_UI").doLoadStateIco();
  var totalHeight = $("#invite_GroupPsn_UI").height() > $(window).height() - 8 ? $(window).height() - 8 : $(
      "#invite_GroupPsn_UI").height();
  $("#invite_GroupPsn_UI").css("height", totalHeight + "px");
  $("#invite_GroupPsn_UI").css("top", ($(window).height() - totalHeight) / 2 + "px");
  var url = snsctx + "/friend/ajaxSelectFriendsForGroup";
  var data = {
    psnIds : $("#des3PsnId").val()
  };
  var invitePage = $("#invite_group_menber_page");
  if (invitePage != null && typeof invitePage != "undefined" && invitePage.length > 0) {
    return;
  }
  setTimeout(function() {
    ajaxparamsutil.doAjax(url, data, Group.selectFriendsForGroupResult, 'html', 'post');
  }, 250);
}
// 群组成员邀请联系人回调函数
;
Group.selectFriendsForGroupResult = function(data) {
  if (typeof data != 'undefined' && data) {
    $('body').css('overflow', 'hidden');
    // $("#invite_GroupPsn_UI").prepend(data);
    $("#invite_GroupPsn_UI").html(data);
    Group.adjustInviteInterface();
  }
}
// 调整邀请界面的弹出窗
;
Group.adjustInviteInterface = function() {
  var $_div = $("#invite_GroupPsn_UI");
  var windowHeight = $(window).height();
  var contentHeight = $_div.find(".dialogs_content").outerHeight();
  var contentWidth = $_div.find(".dialogs_content").outerWidth();
  var containerHeight = $_div.find(".dialogs_container").outerHeight();
  var containerWidth = $_div.find(".dialogs_container").outerWidth();
  var titleHeight = $_div.find(".dialogs_title").outerHeight();
  var operationsHeight = $_div.find(".dialogs_operations").outerHeight();
  var totalHeight = contentHeight + titleHeight + operationsHeight;
  if (typeof totalHeight == "undefined" || totalHeight == 0) {
    totalHeight = $_div.height();
  }
  totalHeight = totalHeight > windowHeight - 8 ? windowHeight - 8 : totalHeight;
  $_div.css("height", totalHeight + "px");
  $_div.css("width", containerWidth + "px");
  $_div.css("top", (windowHeight - totalHeight) / 2 + "px");
  $_div.css("left", "calc(50% - " + contentWidth / 2 + "px)");

  if ($("#invitePsn_psnList").find(".person_dialog_namecard_whole").length <= 0) {
    scmpublicprompt($("#invitePsn_psnList"), "未找到任何联系人", "60px");
  }
}
// 获取已选择群组成员邀请联系人节点
;
Group.getStrselectFriends = function(val, text, img, time) {
  var imgStr = "";
  if (img != null && img != "") {
    imgStr = "<div class='avatar'>" + "<img class='avatar' src='" + img + "' alt=''> " + "</div>";
  }
  var div_psn = "" + "<div class='chips_normal'>" + imgStr + "<div class='content' val='" + val + "'>" + text
      + "</div>" + "<div class='delete'>" + "<i class='material-icons' onclick='Group.removeDoc(this,event)' myTime='"
      + time + "'>&#xe5cd;</i>" + "</div>" + "</div>";
  return div_psn;
}
// 群组成员邀请联系人
;
Group.selectFriendsForGroupHit = function(obj) {
  if ($(obj).attr("had_checked")) {
    return;
  }
  var data = $(obj).find("input[name='friendDes3Id']").val();
  var img = $(obj).find("img").attr("src");

  var timestamp = new Date().getTime();

  var div_psn = Group.getStrselectFriends(data, $(obj).find("input[name='friendName']").val(), img, timestamp);

  var chips_normal_last = $("#invitePsn_psnList_check").find(".chips_normal:last");
  if (typeof chips_normal_last != "undefined" && chips_normal_last.length > 0) {
    chips_normal_last.after(div_psn);
  } else {
    $("#invitePsn_psnList_check").prepend(div_psn);
  }
  $(obj).attr("had_checked", true);
  $(obj).attr("myTime", timestamp);
}
// 群组成员邀请联系人
;
Group.selectFriendsForGroupEdit = function() {
  $("#invitePsn_psnList_check").click(function() {
    if ($("#myEdit") != null && $("#myEdit").size() > 0) {
      $("#myEdit").focus();
      return;
    }
    // var div_psn = "<input id='myEdit' type='text' class='dialogs_invitePsn_chips_input'
    // contenteditable='true'/>";
    var div_psn = "<div id='myEdit' type='text' class='dialogs_invitePsn_chips_input' contenteditable='true'/>";
    $("#invitePsn_psnList_check").append(div_psn);
    $("#myEdit").focus();

    /*
     * $("#myEdit").blur(function(){ $("#myEdit").remove(); });
     */
  });
  $("#invitePsn_psnList_check").keydown(function(event) {
    if (event.keyCode == 13) {
      /*
       * var $_myEdit = $("#myEdit"); var arr = $_myEdit.getCursorPositionSubStr(); if(arr[0]!=""){
       * $_myEdit.before(Group.getStrselectFriends("",$.trim(arr[0]))); } var div_psn = "<div
       * id='myEdit' type='text' class='dialogs_invitePsn_chips_input'
       * contenteditable='true'>"+arr[1]+"</div>"; $_myEdit.remove();
       * 
       * setTimeout(function(){ $("#invitePsn_psnList_check").append(div_psn); $("#myEdit").focus();
       * },1);
       */
    }
  });
}
// 删除节点
;
Group.removeDoc = function(obj, e) {
  var myTime = $(obj).attr("myTime")
  $("#invitePsn_psnList").find("div[myTime='" + myTime + "']").attr("had_checked", "");
  $(obj).parent().parent().remove();
  if (e && e.stopPropagation) {// 非IE
    e.stopPropagation();
  } else {// IE
    window.event.cancelBubble = true;
  }
}
// 关闭邀请联系人窗口
;
Group.closeFriends = function() {
  var $_div = $("#invite_GroupPsn_UI");
  var contentHeight = $_div.find(".dialogs_content").outerHeight();
  var titleHeight = $_div.find(".dialogs_title").outerHeight();
  var operationsHeight = $_div.find(".dialogs_operations").outerHeight();
  var totalHeight = contentHeight + titleHeight + operationsHeight;
  $_div.css("top", "100%");
  $("#shelter2").hide();
  setTimeout(function() {
    $('body').css('overflow', 'auto');
    $_div.css("height", totalHeight + "px");
  }, 200);
}
// 邀请联系人窗口
;
Group.sendInviteToFriends = function() {
  var divs = $("#invitePsn_psnList_check").find(".content");
  if (divs.size() <= 0) {
    // $.scmtips.show('warn',"没有选择任何联系人！");
    Group.closeFriends();
    return;
  }
  var url = snsctx + '/group/ajaxGroupMemberInvite';
  var arr = [];
  $.each(divs, function(i, n) {
    arr.push({
      'val' : $(n).attr("val"),
      'text' : $(n).text(),// 邮件地址
      'name' : "" // 姓名
    });
  });
  /*
   * if(arr.length>50){//SCM-10340 arr=arr.slice(0,50); }
   */
  for (var i = 0; i < arr.length; i++) {
    var item = arr[i];
    item.text = item.text == "undefined" ? "" : item.text;// 名称.
    item.val = item.val == "undefined" ? "" : item.val;// ID.
    item.name = item.name == "undefined" ? "" : item.name;// 姓名
  }
  var des3GroupId = $("#des3GroupId").val();
  var groupNodeId = 1;
  var des3NodeId = 1;
  // 设置邀请邮件中链接的响应地址.
  var groupUrl = snsctx + "/group/view?menuId=31&groupPsn.des3GroupNodeId=1&groupPsn.des3GroupId=" + des3GroupId;
  var psnIds_emails = JSON.stringify(arr);
  var inviteTitle = "";
  if ("en_US" == locale) {
    inviteTitle = "you may be interested in " + $("#_groupName").val() + "group";
  } else {
    inviteTitle = "您好，您可能对群组" + $("#_groupName").val() + "感兴趣";
  }
  var data = {
    "psnIds_emails" : psnIds_emails,
    "inviteTitle" : inviteTitle,
    "groupPsn.groupUrl" : groupUrl,
    "groupPsn.groupNodeId" : 1,
    "groupPsn.des3GroupId" : des3GroupId
  };

  Group.closeFriends();
  setTimeout(function() {
    ajaxparamsutil.doAjax(url, data, Group.sendInviteToFriendsResult);
  }, 300);

}
// 邀请联系人回调
;
Group.sendInviteToFriendsResult = function(data) {
  if (data.result == 'success') {
    $(".person_dialog_namecard_whole").removeAttr("had_checked");
    $.scmtips.show('success', "操作成功");
    $("#invitePsn_psnList_check").empty();
  } else if (data.result == 'error') {
    $.scmtips.show('error', data.msg);
  }
}

// 改变url
Group.changeUrl = function(toModule, submodule) {
  var json = {};
  var oldUrl = window.location.href;
  var index = oldUrl.lastIndexOf("&toModule");
  var newUrl = window.location.href;

  if (toModule != undefined && toModule != "") {
    $("#toModule").val(toModule);
    if (index < 0) {
      newUrl = oldUrl + "&toModule=" + toModule;
    } else {
      newUrl = oldUrl.substring(0, index) + "&toModule=" + toModule;
    }
  }

  if (submodule != undefined && submodule != "") {
    var subIndex = newUrl.lastIndexOf("&submodule");
    if (subIndex < 0) {
      newUrl = newUrl + "&submodule=" + submodule;
    } else {
      newUrl = newUrl.substring(0, subIndex) + "&submodule=" + submodule;
    }
  }

  // 去除backtype
  newUrl = newUrl.replace("backType", "undefined")
  window.history.replaceState(json, "", newUrl);

}
// --------------------群组动态-----------
// 加载群组动态页面main
Group.groupDynLoad = function() {
  var firstResult = 0;
  var maxResults = 10;
  var flag = "more";
  var data = {
    des3GroupId : $("#des3GroupId").val(),
    firstResult : firstResult,
    maxResults : maxResults
  };
  ajaxparamsutil.doAjax("/dynweb/dynamic/groupdyn/ajaxshow", data, Group.groupDynLoadResult, "html");
}

// 群组动态， 分享时 分享数据加一
Group.groupDynAddShareCount = function() {
  groupDynamic.addOneShareCount();
  groupDynamic.updateDynShareCount();
}

// 加载群组动态页面main--回调
Group.groupDynLoadResult = function(data) {
  $("#group_dyn_main_preloader").before(data);
  Group.scrollDirect(Group.groupDynLoadMore);
}

// 加载群组动态页面更多
Group.groupDynLoadMore = function() {
  // 加载样式
  $("#group_dyn_main_preloader").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });

  setTimeout(function() {
    var firstResult = new Number($("#dyn_first_result").val()) + 10;
    var maxResults = 10;
    $("#dyn_first_result").val(firstResult);
    var flag = "more";
    var data = {
      des3GroupId : $("#des3GroupId").val(),
      firstResult : firstResult,
      maxResults : maxResults,
      flag : flag
    };
    ajaxparamsutil.doAjax("/dynweb/dynamic/groupdyn/ajaxshow", data, Group.groupDynLoadMoreResult, "html");
  }, 500);
}
// 加载更多 群组动态页面main--回调
Group.groupDynLoadMoreResult = function(data) {
  if ($.trim(data) == "") {
    window.removeEventListener("scroll", {}, false)
  }
  // $("#group_dyn_main_load").append(data);
  $("#group_dyn_main_preloader").html("");
  $("#group_dyn_main_preloader").before(data);
}

// 滚动条事件
Group.scrollDirect = function(fn) {
  var beforeScrollTop = document.body.scrollTop;
  fn = fn || function() {
  };
  window.addEventListener("scroll", function(event) {
    event = event || window.event;

    var afterScrollTop = document.body.scrollTop;
    delta = afterScrollTop - beforeScrollTop;
    beforeScrollTop = afterScrollTop;

    var scrollTop = $(this).scrollTop();
    var scrollHeight = $(document).height();
    var windowHeight = $(this).height();
    if (scrollTop + windowHeight == scrollHeight) { // 滚动到底部执行事件
      fn();
    }

  }, false);
}
