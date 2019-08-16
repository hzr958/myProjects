/**
 * 群组人员js zzx
 * aa
 */
var GrpMember = GrpMember ? GrpMember : {};

GrpMember.enterCallBack = function() {
  var chipBox = $("#grp_selected_friends").find(".chip__box:last");
  if (chipBox && chipBox.attr("code") == "") {
    var email = chipBox.find(".chip__text").text();
    if (/^[a-z0-9A-Z_]+[a-z0-9_\-.]*@([a-z0-9A-Z_]+\.)+[a-zA-Z_]{2,10}$/i.test(email)) {

    } else {
      scmpublictoast(grpMember.emailformat, 1000);
      chipBox.remove();
    }
  }
}

GrpMember.fileCheck = function(inputFileId) {
  var status = true;
  var input = document.getElementById(inputFileId);
  if (/.(xlsx)$/.test(input.value)) {
    if (((input.files[0].size).toFixed(2)) >= (10 * 1024 * 1024)) {
      scmpublictoast(grpMember.sizeformat, 2000);
      status = false;
    }
  } else {
    scmpublictoast(grpMember.xlsxformat, 2000);
    status = false;
  }
  return status;
}

GrpMember.uploadFile = function(inputFileId) {
  if (GrpMember.fileCheck(inputFileId)) {
    $.ajaxFileUpload({
      url : "/groupweb/grpmember/ajaxuploademailexceltemp",
      secureuri : false,// 一般设置为false
      fileElementId : inputFileId,// 文件上传表单的id
      dataType : 'json',// 返回值类型 一般设置为json
      success : function(data, status) {
        if (data.result == 'error') {
          scmpublictoast(data.msg, 1000);
        } else {
          GrpMember.buildEmailBoxStr(data.emailList);
          var title = grpMember.impemailtitle1 + data.successCount + grpMember.impemailtitle2;
          if (data.fiterEmailCount > 0) {
            title = grpMember.impemailtitle0 + title;
          }
          scmpublictoast(title, 2000);
        }
      },
      error : function(data) {
        scmpublictoast(grpMember.timeout, 1000);
      }
    });
  }
}

GrpMember.buildEmailBoxStr = function(list) {
  var htmlStr = "";
  var firstStr = "<div class='chip__box' code=''><div class='chip__avatar'><img src=''></div><div class='chip__text'>";
  var nextStr = "</div><div class='chip__icon icon_delete' onclick='GrpMember.delSelectFriend(this)'><i class='material-icons'>close</i></div></div>";
  if (list != null && list.length > 0) {
    for (var i = 0; i < list.length; i++) {
      var email = list[i];
      if (email != null && email != "" && GrpMember.checkrepemail(email)) {
        htmlStr += firstStr + email + nextStr;
      }
    }
  }
  if (htmlStr != "") {
    $("#grp_selected_friends").find(".js_autocompletebox").before(htmlStr);
  }
}
GrpMember.checkrepemail = function(email) {
  var status = true;
  $("#grp_selected_friends").find(".chip__text").each(function(i, o) {
    if ($(o).text() == email) {
      status = false;
    }
  });
  return status;
}
GrpMember.downloadEmailExcel = function(event) {
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
    GrpBase.doHitMore($(event.currentTarget), 1000);
    var url = "/groupweb/grpmember/ajaxdownloademailexceltemp";
    location.href = url;
  }, 1);

}
GrpMember.uploadEmailExcel = function(event) {
  var canRun = false;
  setTimeout(function() {
    while (canRun) {
      $("#emailExcelFile").click();
      canRun = false;
    }
  }, 500);
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
    GrpBase.doHitMore($(event.currentTarget), 1000);
    canRun = true;
  }, 1);
}

// 人员列表操作
;
GrpMember.clickMemberOpt = function(obj, e) {
  $("#grp_member_List").find(".list_shown").removeClass("list_shown");
  $(obj).parent().find('.action-dropdown__list').addClass("list_shown");
  // 点击其他地方关闭
  GrpMember.clickOtherHide(e, function() {
    $(obj).parent().find('.action-dropdown__list').removeClass("list_shown");
  });
}

// 点击其他地方事件
;
GrpMember.clickOtherHide = function(e, myFunction) {
  if (e && e.stopPropagation) {// 非IE
    e.stopPropagation();
  } else {// IE
    window.event.cancelBubble = true;
  }
  $(document).click(function() {
    myFunction();
  });
}
// 默认查询两条， 隐藏最后一条
;
GrpMember.hidePropose = function() {
  var objs = $("#grp_propose_list").find(".main-list__item");
  if (objs.length > 1) {
    objs.eq(1).hide();
  }
}
// 点击显示全部申请人界面
;
GrpMember.showPropsesList = function() {
  showDialog("grp_proposes_ui");
  // 转圈圈
  $("#grp_proposes_list").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
    status : 1
  });
  GrpMember.ajaxPropsesList();
}
// 显示全部申请人列表
;
GrpMember.ajaxPropsesList = function() {
  GrpMember.showGrpProposers(3, 1, 10);
}
// 点击关闭全部申请人窗口
;
GrpMember.hidePropsesList = function() {
  hideDialog("grp_proposes_ui");
  // 下一个群组申请
  GrpMember.showGrpProposers(1, 1, 1);
  $("#member_main").click();
}
// 群组申请人
;
GrpMember.showGrpProposers = function(showType, pageNo, pageSize) {
  var url = "/groupweb/grpmember/ajaxshowgrpproposes";
  if (ispending == 1) {
    url += "?ispending=1";
    ispending = 0;
  }
  $.ajax({
    url : url,
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'showType' : showType,// showType 1=显示单条申请人记录(忽略、确认)，3=显示所有申请人列表
      'page.pageNo' : pageNo,
      'page.pageSize' : pageSize
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (showType == 1) {
          $("#grp_proposer").html(data);
        } else if (showType == 3) {
          $("#grp_proposes_list").html(data);
        }
      });

    },
    error : function() {
    }
  });
};
// 处理群组申请（接受/忽略） disposeType 1=同意 ； 0=忽略
;
GrpMember.disposegrpApplication = function(disposeType, targetPsnIds, myCallBack, obj) {
  var des3GrpId = $("#grp_params").attr("smate_des3_grp_id");
  $.ajax({
    url : '/groupweb/grpmember/ajaxdisposegrpapplication',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : des3GrpId,
      'targetPsnIds' : targetPsnIds,
      'disposeType' : disposeType
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
          myCallBack(obj,disposeType);
      });
    },
    error : function() {
      scmpublictoast(grpMember.optFail, 2000);
    }
  });
}
// 处理群组申请回调1 群组成员-一条申请的处理使用
GrpMember.disposegrpApplicationCallBack1 = function() {
  // 下一个群组申请
  GrpMember.showGrpProposers(1, 1, 1);
  // 刷新成员列表
  GrpMember.refreshMember($("#grp_params").attr("smate_grp_id"));
  scmpublictoast(grpMember.optSuccess, 1000);
}
// 处理群组申请回调2 群组成员-申请列表的处理使用
GrpMember.disposegrpApplicationCallBack2 = function(obj) {
  /*// 群组成员列表
  var $grpID = parseInt(document.getElementById("grp_params").getAttribute("smate_grp_id"));
  GrpMember.refreshMember($grpID, GrpMember.loadAddFriend());
  if ($("#grp_proposes_list").find(".main-list__item").length == 1) {
    hideDialog("grp_proposes_ui");
    GrpMember.showGrpProposers(1, 1, 1);// 单条申请人
  } else {
    GrpMember.ajaxPropsesList();// 全部申请人列表
  }*/
  $(obj).closest(".main-list__item").remove();
    if ($("#grp_proposes_list").find(".main-list__item").length == 0) {
        $("#grp_proposes_list").append("<div class='response_no-result'>未找到符合条件的记录</div>");
    }
    scmpublictoast(grpMember.optSuccess, 1000);
}
// 处理群组申请回调3 群组成员-申请列表的全部同意使用
GrpMember.disposegrpApplicationCallBack3 = function(obj) {
    $("#grp_proposes_list").find(".main-list__item").remove();
    $("#grp_proposes_list").append("<div class='response_no-result'>未找到符合条件的记录</div>");
    scmpublictoast(grpMember.optSuccess, 1000);
  /*GrpMember.showGrpProposers(1, 1, 1);
  // 群组成员列表
  var $grpID = parseInt(document.getElementById("grp_params").getAttribute("smate_grp_id"));
  GrpMember.refreshMember($grpID, GrpMember.loadAddFriend());
  scmpublictoast(grpMember.optSuccess, 1000);*/
};
GrpMember.disposegrpAllApplications = function(disposeType, myCallBack, obj) {
  var targetPsnIds = "";
  $.each($("#grp_proposes_list").find(".main-list__item"), function(i, o) {
    targetPsnIds += $(o).attr("smate_psn_id") + ",";
  });
  if (targetPsnIds != "") {
    GrpMember.disposegrpApplication(disposeType, targetPsnIds, myCallBack, obj);
  } else {
    scmpublictoast(grpMember.noReqOpt, 2000);
  }
}
// 刷新成员列表
;
GrpMember.refreshMember = function(grpId, myfunction, myurl) {
  var url = "/groupweb/grpmember/ajaxshowgrpmembers";
  if (myurl != null && typeof myurl != 'undefined' && "" != myurl) {
    url = myurl;
  }
  var des3GrpId = $("#grp_params").attr("smate_des3_grp_id");
  window.Mainlist({
    name : "grpmember",
    listurl : url,
    listdata : {
      des3GrpId : des3GrpId,
      pageSize : 10
    },
    listcallback : function(xhr) {
      var $memberList = document.getElementById("grp_member_List");
      console.log = xhr.getResponseHeader("Content-Type");
      // GrpMember.loadAddFriend();
      // 成员列表没有可操作项，隐藏操作按钮
      $.each($("#grp_member_List").find(".dev_member_opt"), function(i, o) {
        if ($(o).find(".action-dropdown__item").length <= 0) {
          $(o).html("");
        }
      });
    }
  });
}

// 修改群组角色权限
;
GrpMember.updateGrpPsnRole = function(updateRole, des3TargetPsnId) {
  var grpId = $("#grp_params").attr("smate_grp_id");
  $.ajax({
    url : '/groupweb/grpmember/ajaxsetgrprole',
    type : 'post',
    dataType : 'json',
    data : {
      'grpId' : grpId,
      'des3TargetPsnId' : des3TargetPsnId,
      'updateRole' : updateRole
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (updateRole == 1) {
          $("#btn_grp_manage").hide();
        }
        // 刷新列表
        GrpMember.refreshMember(grpId);
        scmpublictoast(grpMember.optSuccess, 1000);
      });

    },
    error : function() {
      scmpublictoast(grpMember.optFail, 2000);
    }
  });

}
// 添加联系人 //老系统逻辑-可以选择标签
;
GrpMember.loadAddFriend = function(des3TargetPsnId) {
  $(".addFriend").each(function() {
    $(this).thickbox({
      ctxpath : "/scmwebsns",
      respath : "/resscmwebsns",
      parameters : {
        "d3d" : $(this).attr("des3Id")
      },
      type : "addRequests"
    });
  });
}
// 添加联系人 -直接添加
;
GrpMember.addFriend = function(des3TargetPsnId) {
  $.ajax({
    url : '/psnweb/friend/ajaxaddfriend',
    type : 'post',
    dataType : 'json',
    data : {
      'des3Id' : des3TargetPsnId
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data['result'] == 'true') {
          scmpublictoast(grpMember.sentSuccess, 1000);
        } else {
          if (data != null) {
            scmpublictoast(data.msg, 2000);
          } else {
            scmpublictoast(grpMember.addFail, 1000);
          }

        }
      });

    },
    error : function() {
      jConfirm(grpMember.sessionTimeout, grpMember.tips, function(r) {
        if (r) {
          document.location.href = window.location.href;
          return 0;
        }
      });
    }
  });
}

GrpMember.jconfirm = function(myfunction, title, arguments1, arguments2) {
  $("#dev_jconfirm").find(".button_primary").unbind();
  $("#dev_jconfirm").find(".dialogs__modal_text").html(title);
  showDialog("dev_jconfirm_ui");
  $("#dev_jconfirm").find(".button_primary").bind("click", function() {
    myfunction(arguments1, arguments2);
  });
}

// 2017-09 二次确认框start
GrpMember.jconfirmNew = function(myfunction, title, arguments1, arguments2) {
  var data = {
    "arguments1" : arguments1,
    "arguments2" : arguments2
  }
  GrpMember.jconfirm(function() {
    if (typeof myfunction == "function") {
      myfunction(data);
    }
  }, title);

};
GrpMember.delGrpPsnNew = function(data) {
  GrpMember.delGrpPsn(data.arguments1, data.arguments2);
};
GrpMember.updateGrpPsnRoleNew = function(data) {
  GrpMember.updateGrpPsnRole(data.arguments1, data.arguments2);
};

// 2017-09 二次确认框end

// 群组成员移除此人
;
GrpMember.delGrpPsn = function(des3TargetPsnId, obj) {
  $.ajax({
    url : '/groupweb/grpmember/ajaxdelgrpmember',
    type : 'post',
    dataType : 'json',
    data : {
      'grpId' : $("#grp_params").attr("smate_grp_id"),
      'delType' : 99,// 99=删除（被移除出群组）,98=删除（自动退出群组）
      'des3TargetPsnId' : des3TargetPsnId
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $(obj).closest(".main-list__item").remove();
        scmpublictoast(grpMember.optSuccess, 2000);
      });

    },
    error : function() {
      scmpublictoast(grpMember.optFail, 2000);
    }
  });
}
// 自动退出群组
;
GrpMember.autoDelGrpPsn = function(des3GrpId, obj) {
  GrpBase.doHitMore(obj, 2000);
  $.ajax({
    url : '/groupweb/grpmember/ajaxautoexitgrp',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : des3GrpId,
      'delType' : 98
    // 99=删除（被移除出群组）,98=删除（自动退出群组）
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data['result'] == 'success') {
          GrpBase.showGrpList();
          scmpublictoast(grpMember.optSuccess, 1000);
        } else {
          scmpublictoast(data['result'], 2000);
        }
      });

    },
    error : function() {
      scmpublictoast(grpMember.optFail, 2000);
    }
  });
}

// 邀请推荐人加入群组
;
GrpMember.InvitedReferrer = function(des3TargetPsnIds, obj) {
  $(obj).closest(".main-list__item").remove();
  var grpId = $("#grp_params").attr("smate_grp_id");
  GrpMember.invitedJoinGrp(des3TargetPsnIds, grpId, 1, GrpMember.showGrpReferrers);
}
// 邀请加入群组isReferrer=0 普通邀请 isReferrer=1表示是邀请推荐库的人员
;
GrpMember.invitedJoinGrp = function(des3TargetPsnIds, grpId, isReferrer, myFunction) {
  $.ajax({
    url : '/groupweb/grpmember/ajaxinvitedjoingrp',
    type : 'post',
    dataType : 'json',
    data : {
      'grpId' : grpId,
      'targetPsnIds' : des3TargetPsnIds,
      'isReferrer' : isReferrer
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        myFunction();
        scmpublictoast(grpMember.optSuccess, 1000);
      });

    },
    error : function() {
      scmpublictoast(grpMember.optFail, 2000);

    }
  });
}
// 群组推荐成员列表
;
GrpMember.showGrpReferrers = function() {
  $("#grp_referrers_List").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
    status : 1
  });
  $.ajax({
    url : '/groupweb/grpmember/ajaxshowgrpreferrers',
    type : 'post',
    dataType : 'html',
    data : {
      'grpId' : $("#grp_params").attr("smate_grp_id"),
      'page.pageNo' : 1,
      'page.pageSize' : 10
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_referrers_List").html(data);
      });

    },
    error : function() {

    }
  });
};
// 打开人员主页----zzx-----
;
GrpMember.openPsnDetail = function(des3ProducerPsnId, event) {
  if (des3ProducerPsnId != null && des3ProducerPsnId != "") {
    location.href = "/psnweb/homepage/show?des3PsnId=" + encodeURIComponent(des3ProducerPsnId);
    // window.open("/scmwebsns/resume/psnView?des3PsnId="+encodeURIComponent(des3ProducerPsnId));
    // window.open("/psnweb/homepage/show?des3PsnId="+encodeURIComponent(des3ProducerPsnId));
    // location.href="/scmwebsns/resume/psnView?des3PsnId="+encodeURIComponent(des3ProducerPsnId);
    GrpMember.stopPropagation(event);
  }
}
// 打开人员主页----zzx-----
;
GrpMember.openPsnDetailNewWind = function(des3ProducerPsnId, event) {
  if (des3ProducerPsnId != null && des3ProducerPsnId != "") {
    // location.href="/psnweb/homepage/show?des3PsnId="+encodeURIComponent(des3ProducerPsnId);
    // window.open("/scmwebsns/resume/psnView?des3PsnId="+encodeURIComponent(des3ProducerPsnId));
    window.open("/psnweb/homepage/show?des3PsnId=" + encodeURIComponent(des3ProducerPsnId));
    // location.href="/scmwebsns/resume/psnView?des3PsnId="+encodeURIComponent(des3ProducerPsnId);
    GrpMember.stopPropagation(event);
  }
}
// 群组动态 阻止冒泡事件
;
GrpMember.stopPropagation = function(e) {
  if (e && e.stopPropagation) {// 非IE
    e.stopPropagation();
  } else {// IE
    window.event.cancelBubble = true;
  }
}
// 显示邀请联系人界面
;
GrpMember.showInvItedFriendUI = function() {
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
    GrpMember.ajaxInvItedFriends();
    showDialog("grp_invited_friend_ui");
    window.requestAnimationFrame(function() {
      inviteBtnInit();
    });
    GrpMember.invitecheckload();
  }, 1);

}
// 隐藏邀请联系人界面
;
GrpMember.hideInvItedFriendUI = function() {
  hideDialog("grp_invited_friend_ui");
  $("#grp_selected_friends").find(".chip__box").remove();
  window.cancelAnimationFrame($aID);
}
// 邀请联系人列表
;
GrpMember.ajaxInvItedFriends = function() {
  $("#grp_invited_friend_list").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
    status : 1
  });
  $.ajax({
    url : '/groupweb/grpmember/ajaxshowfriends',
    type : 'post',
    dataType : 'html',
    data : {
      'grpId' : $("#grp_params").attr("smate_grp_id")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_invited_friend_list").html(data);
        if ($("#grp_invited_friend_list").find(".friend-selection__item-3").length == 0) {
          $("#grp_invited_friend_list").addClass("main-list__list");
          $("#grp_invited_friend_list").html("<div class='response_no-result'>" + grpMember.notAddFriend + "</div>");
        } else {
          $("#grp_invited_friend_list").removeClass("main-list__list");
        }
      });

    },
    error : function() {
    }
  });
}
// 隐藏邀请联系人界面
;
GrpMember.clickFriend = function(obj) {
  if (!$(obj).find(".psn-idx_small").hasClass("none-selected")) {
    var code = $(obj).attr("smate_psn_id");
    if ($(obj).find(".psn-idx_small").hasClass("psn_chosen")) {
      $(obj).find(".psn-idx_small").removeClass("psn_chosen");
      $("#grp_selected_friends").find(".chip__box[code='" + code + "']").remove();
    } else {
      $(obj).find(".psn-idx_small").addClass("psn_chosen ");
      var name = $(obj).find(".psn-idx__main_name").text();
      var avatar = $(obj).find(".psn-idx__avatar_img").html();
      if (GrpMember.checkParam(code)) {
        var html = "<div class='chip__box' code='" + code + "'> " + "<div class='chip__avatar'>" + avatar + "</div> "
            + "<div class='chip__text'>" + name + "</div>"
            + "<div class='chip__icon icon_delete' onclick='GrpMember.delSelectFriend(this)'>"
            + "<i class='material-icons'>close</i>" + "</div>" + "</div>";
        $("#grp_selected_friends").find(".chip-panel__manual-input").before(html);
      }
    }
  }
}
// 删除已选择的联系人
;
GrpMember.delSelectFriend = function(obj) {
  $(obj).closest(".chip__box").remove();
}

;
GrpMember.isEmail = function(str) {
  if (str == null || str == "") {
    return false;
  }
  var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
  return reg.test(str);
}

// 邀请选择的联系人加入群组
;
GrpMember.sendFriendInvition = function(obj) {
  GrpBase.doHitMore(obj, 2000);
  var des3TargetPsnIds = "";
  var emails = "";
  var grpId = $("#grp_params").attr("smate_grp_id");
  var code = "";
  var email = "";
  // 获取参数
  $("#grp_selected_friends").find(".chip__box").each(function(i, o) {
    code = $(o).attr("code");
    email = $.trim($(o).find(".chip__text").text());
    if (code != null && code != "") {// 如果code为空，则是通过邮件邀请
      des3TargetPsnIds += code + ",";
    } else if (GrpMember.isEmail(email)) {
      emails += email + ",";
    }

  });
  if (des3TargetPsnIds != "") {// 由psnId邀请
    GrpMember.invitedJoinGrp(des3TargetPsnIds, grpId, 0, function() {
    });
  }
  if (emails != "") {
    // 由邮件邀请
    GrpMember.invitedByEmails(grpId, emails);
    if (des3TargetPsnIds == "") {
      scmpublictoast(grpMember.optSuccess, 1000);
    }
  }
  if (des3TargetPsnIds == "" && emails == "") {
    scmpublictoast(grpMember.noChooseGet, 2000);
  } else {
    $("#grp_selected_friends").find(".chip__box").remove();
  }
  // 放在最后面
  GrpMember.hideInvItedFriendUI();
}
GrpMember.invitedByEmails = function(grpId, emails) {
  $.ajax({
    url : '/groupweb/grpmember/ajaxinvitedjoingrpbyemail',
    type : 'post',
    dataType : 'json',
    data : {
      'grpId' : grpId,
      'emails' : emails
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
      });
    },
    error : function() {
    }
  });
}
// 数据查重-防止重复点击
;
GrpMember.checkParam = function(code) {
  var status = 0;
  $("#grp_selected_friends").find(".chip__box").each(function(i, o) {
    if ($(o).attr("code") == code) {
      status = 1;
      return;
    }
  });
  if (status == 1) {
    return false
  } else {
    return true;
  }
}
// 申请/取消加入加入群组 isApplyJoinGrp 是否是申请加入群组 1=申请加入群组，0=取消加群组
;
GrpMember.applyJoinGrp = function(isApplyJoinGrp, des3GrpId, myFunction) {
  $.ajax({
    url : '/groupweb/grpmember/ajaxapplyjoingrp',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : des3GrpId,
      'isApplyJoinGrp' : isApplyJoinGrp
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        myFunction();
      });

    },
    error : function() {
    }
  });
};
GrpMember.cancelJoinGrpCallBack = function() {

}

// 成员申请 群组
;
GrpMember.ajaxMemberApplyJoinGrp = function(grpId, des3GrpId, isGrpBase) {
  if (grpId == undefined || grpId == null) {
    grpId = $("#grp_params").attr("smate_grp_id");
  }
  $.ajax({
    url : '/groupweb/grpmember/ajaxapplyjoingrp',
    type : 'post',
    dataType : 'json',
    data : {
      'isApplyJoinGrp' : 1,
      'des3GrpId' : des3GrpId,
      'grpId' : grpId
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (data.addSuccess == "true") {
            // 加入成功的提示
            scmpublictoast(grpMember.addSuccess, 1000);
          } else {
            scmpublictoast(grpMember.sentSuccess, 1000);
          }
          if (isGrpBase == 1) {// 群组详情中点击的申请；
            if (data.role == "3") {
              $("#btn_apply").remove();
              $("#grp_params").find(".nav__list > .nav__item").each(function(i, o) {
                if ($(o).hasClass("item_selected")) {
                  $(o).click();
                }
              });
            } else {
              $("#btn_apply").html(grpMember.pending);
              $('#btn_apply').attr("disabled", true);
              $("#grp_params").find(".nav__list > .nav__item").each(function(i, o) {
                if ($(o).hasClass("item_selected")) {
                  $(o).click();
                }
              });
            }
          }
        } else {
          scmpublictoast(grpMember.pendFail, 1000);
        }
      });

    },
    error : function() {
    }
  });
}
// 群组邀请成员选择状态的监听
;
GrpMember.invitecheckload = function() {
  $("#grp_selected_friends").unbind('DOMNodeRemoved');
  $("#grp_selected_friends").bind('DOMNodeRemoved', function() {
    setTimeout(function() {
      $("#grp_invited_friend_list").find(".psn_chosen").each(function(i, o) {
        if (GrpMember.checkParam($(o).closest(".friend-selection__item-3").attr("smate_psn_id"))) {
          $(o).removeClass("psn_chosen");
        }
      });
    }, 20);
  });
}
