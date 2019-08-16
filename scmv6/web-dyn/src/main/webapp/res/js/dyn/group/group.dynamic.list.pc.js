/**
 * 群组动态列表显示及部分逻辑的js---zzx
 */
var groupDynamic = groupDynamic ? groupDynamic : {};
// ---防止重复点击--zzx--
groupDynamic.doHitMore = function(obj, time) {
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
}
// ---超时处理---zzx----
groupDynamic.doTimeOut = function(data, dataType) {
  var toConfirm = false;
  if ("json" == dataType && data != null) {
    toConfirm = data.ajaxSessionTimeOut == 'yes' ? true : toConfirm;
  } else {
    toConfirm = data == "{\"ajaxSessionTimeOut\":\"yes\"}" ? true : toConfirm;
  }
  if (toConfirm) {
    jConfirm(groupDynCommon.timeout, groupDynCommon.tipTitle, function(r) {
      if (r) {
        document.location.href = window.location.href;
        return;
      }
    });
  }
}
// --------------群组动态、评论列表显示-js--zzx--------------
groupDynamic.loadMoreComments = function(dynId, obj) {
  $(obj).prev().children().eq(1).click();
};
// --------------群组动态、评论列表--点击评论-js--zzx--------------
groupDynamic.clickComments = function(dynId, obj, status) {
  // 加载转圈圈的图标
  $(obj).parent().parent().find(".dynamic_comment_sample_container").doLoadStateIco({
    status : 1
  });
  var dataJson = {
    dynId : dynId
  };
  $.ajax({
    url : "/dynweb/dynamic/groupdyn/ajaxcomments",
    dataType : "html",
    type : "post",
    data : dataJson,
    success : function(data) {
      groupDynamic.doTimeOut(data, "html");
      if (data != null && data != "{\"ajaxSessionTimeOut\":\"yes\"}") {
        $(obj).parent().parent().find(".dynamic_comment_list").html(data);
        if (status != 1) {
          $(obj).closest(".dynamic_container").find(".dynamic_comment_sample_container").remove();
          $(obj).closest(".dynamic_container").find(".dynamic_comment_list").slideToggle(200);
          $(obj).closest(".dynamic_container").find(".dynamic_comment_action").slideToggle(200);
        }
      }
    }
  });
}
// --------------群组动态-显示动态时间-js--zzx------------
groupDynamic.showDynsTime = function() {
  $(".dynTime").each(function(i, obg) {
    $(obg).parent().find(".time").html($(obg).val());
  });
}
// --------------群组动态-加载附带在动态上的评论-js--zzx-----------
groupDynamic.getCommentFordyn = function() {
  var objComments = $("#group_dyn_main_load").find(".dynamic_container");
  var objCount = objComments.length - 1;// 总的显示动态数----减1只是因为“发起讨论”也在其中，导致查询的动态数多1
  var count = objCount % 10 == 0 ? 10 : objCount % 10;// 当次翻页新增的动态数
  var status = objCount > 10 ? Number(objCount) - Number(count) : -1;// 需要加载评论的动态临界点
  objComments.each(function(i, obj) {
    if (status < i) {// 只更新翻页后出现的count条数据
      var dynId = $(obj).find("input[name='dynId']").eq(0).val();
      if (dynId != null && dynId != "") {
        $.ajax({
          url : "/dynweb/dynamic/groupdyn/ajaxgetcommentsfordyn",
          dataType : "html",
          type : "post",
          data : {
            des3DynId : dynId
          },
          success : function(data) {
            groupDynamic.doTimeOut(data, "html");
            if (data != null && data != "") {
              $(obj).find(".dynamic_comment_sample_container").remove();
              $(obj).find(".dynamic_operations_container").eq(0).after(data);
            }
          }
        });
      }
    }
  });
}
// -------------群组动态-点击分享，把成果信息加载到分享界面-js--zzx+ajb---------
groupDynamic.showPubInfoToShareUINew = function(obj) {
  var objGetInfo = $(obj).closest(".dynamic_container").find(".dynamic_content_container");
  var objShowInfo = $("#id_fluctuate").find(".attachment");
  $("#id_share_ui_param").val($(obj).closest(".dynamic_container").find("input[name='dynId']").attr("resid"));
  $("#id_share_ui_param").attr("des3DynId", $(obj).closest(".dynamic_container").find("input[name='dynId']").val());
  objShowInfo.find(".pub_avatar").attr("src", objGetInfo.find(".pub_avatar").attr("src"));// 成果图片
  objShowInfo.find(".title").html(objGetInfo.find(".title").html());// 成果标题
  objShowInfo.find(".author").html(objGetInfo.find(".author").html());// 成果作者
  objShowInfo.find(".source").html(objGetInfo.find(".source").html());// 成果简介
  $(obj).attr("resId", $("#id_share_ui_param").val());
  $(obj).parent().find("div").css({
    "text-align" : "start",
    "left" : "30px"
  });
};
// -------------群组动态-分享到XXX--分享成功 --分享数加1-js--zzx--
groupDynamic.addOneShareCount = function() {
  var inputObj = $("input[value='" + $("#id_share_ui_param").attr("des3dynid") + "']");
  if (inputObj != null) {
    var t = inputObj.closest(".dynamic_container").find(".share_sites_show").text();
    var shareCount = inputObj.closest(".dynamic_container").find(".share_sites_show").text().replace(/[^0-9]/ig, "");
    if (shareCount != null && $.trim(shareCount) != "") {
      inputObj.closest(".dynamic_container").find(".share_sites_show").text(
          groupDynCommon.share + " (" + (Number(shareCount) + 1) + ")");
    } else {
      inputObj.closest(".dynamic_container").find(".share_sites_show").text(groupDynCommon.share + " (1)");
    }
  }
}

// 分享到科研之友main方法----------------zzx----------------
groupDynamic.shareMain = function(e, obj) {
  groupDynamic.doHitMore(obj, 2000);
  var shareType = $("#id_share_ui_param").attr("shareType");
  switch (Number(shareType)) {
    case 1 :
      groupDynamic.shareToMyDynamic(obj);// 分享到我的动态
    break;
    case 2 :
      groupDynamic.shareToMyFriend(obj)// 分享到我的联系人
    break;
    case 3 :
      groupDynamic.shareToMyGroup(obj);// 分享到我的群组
    break;
    default :
  }
};
// 分享到我的联系人 -------------------------zzx---------------
groupDynamic.shareToMyFriend = function(obj) {
  var des3PubId = $("#id_share_ui_param").val();
  if (des3PubId != null && des3PubId != "") {
    var des3psnIds = "";
    var zh_title = "分享给";
    var en_title = "share";
    $("#share_plugin_select_friends").find(".content").each(function(i, n) {
      des3psnIds += $(n).attr("code") + ",";
      if (i < 5) {// 截断处理
        zh_title += "<label title='" + $(n).attr("name") + "'>'" + $(n).attr("name") + "'</label>";
        en_title += "<label title='" + $(n).attr("name") + "'>'" + $(n).attr("name") + "'</label>";
        if (i == 4) {
          zh_title += "...";
          en_title += "...";
        }
      }
    });
    var textConent = $("#id_share_textarea").val();
    var mydate = new Date();
    var dataStr = (Number(mydate.getFullYear()) + 2) + "/" + (Number(mydate.getMonth()) + 1) + "/" + mydate.getDate();
    if (des3psnIds != null && $.trim(des3psnIds) != "") {
      groupDynamic.sendShareForFriendNew(des3psnIds, textConent, dataStr, zh_title, en_title, true, des3PubId);
    } else {
      scmpublictoast(groupDynCommon.nofriend, 1000);
    }

  } else {
    scmpublictoast(groupDynCommon.noPub, 1000);
    groupDynamic.cancleShare();
  }
}
// 分享到我的群组 -------------------------zzx---------------
groupDynamic.shareToMyGroup = function(obj) {
  var code = $.trim($("#id_share_ui_param").attr("code"));
  if (code == "") {
    scmpublictoast(groupDynCommon.noGroup, 1000);
    return;
  }
  var data = {
    "des3GroupId" : code,
    "dynContent" : $("#id_share_textarea").val(),
    "des3ResId" : $("#id_share_ui_param").val(),
    "tempType" : "SHAREPUB",
    "resType" : "pub"
  };
  ajaxparamsutil.doAjax("/dynweb/dynamic/ajaxgroupdyn/dopublish", data, function() {
    groupDynamic.addOneShareCount();
    groupDynamic.updateDynShareCount();
    scmpublictoast(groupDynCommon.shareSuccess, 1000);
    if ($("#des3GroupId").val() == code) {
      $("#brief").find("a").click();
    }
  }, "json");
  $(obj).prev().click();// 点击取消
}
// 分享到我的动态-----------------------zzx------------------------
groupDynamic.shareToMyDynamic = function(obj) {
  var des3ResId = $.trim($("#id_share_ui_param").val());
  if (des3ResId != null && des3ResId != "") {
    var dynText = $.trim($("#id_share_textarea").val());
    var dynType = "B2TEMP";
    var operatorType = 3;
    if (dynText != null && dynText != "") {// 如果分享内容不为空则发“ATEMP”动态，否则发“B2TEMP”动态
      dynType = "ATEMP";
      operatorType = -1;
    }
    var dataJson = {
      "dynType" : dynType,
      "dynText" : dynText,
      "resType" : "1",
      "des3PubId" : des3ResId,
      "operatorType" : operatorType
    };
    ajaxparamsutil.doAjax("/dynweb/dynamic/ajaxrealtime", dataJson, function() {
      groupDynamic.addOneShareCount();
      groupDynamic.updateDynShareCount();
      scmpublictoast(groupDynCommon.shareSuccess, 1000);
    }, "json");
  }
  $(obj).prev().click();// 点击取消
}
// 更新动态分享数量---------------zzx-----------
groupDynamic.updateDynShareCount = function() {
  var des3DynId = $("#id_share_ui_param").attr("des3DynId");
  var shareContent = $.trim($("#id_share_textarea").val());
  if (des3DynId != null && des3DynId != "") {
    var dataJson = {
      des3DynId : des3DynId,
      shareContent : shareContent
    };
    ajaxparamsutil.doAjax("/dynweb/dynamic/groupdyn/ajaxshare", dataJson, function() {
    });
  } else {
  }

}
// 分享到我的群组---点击选择群组------zzx--------------------------
groupDynamic.checkGroupForShare = function() {
  var htmlStr = "<ul class='item_list_container'>";
  $("#id_group_add_group_name_list").show();
  $("#id_group_add_group_name_list").find(".dialogs_container").doLoadStateIco({
    status : 1
  });
  $.ajax({
    url : "/groupweb/groupopt/ajaxautogroupnames",
    dataType : "json",
    type : "post",
    data : {
      status : "getmygroupnames"
    },
    success : function(data) {
      groupDynamic.doTimeOut(data, "json");
      if (data != null && data != 'undefined') {
        $.each(data, function(i, obj) {
          if (typeof obj.code != "undefined") {
            htmlStr += "<li code='" + obj.code + "' onclick='groupDynamic.clickMyGroupName(this);'>" + obj.name
                + "</li>";
          }
        });
        htmlStr += "</div>";
        $("#id_group_add_group_name_list").find(".dialogs_container").html(htmlStr);
        $("body").css("overflow", "hidden");
      } else {
        // 隐藏 转圈圈的
        $("#id_group_add_group_name_list").find(".dialogs_container  > .preloader").remove();
        scmpublicprompt($("#id_group_add_group_name_list").find(".dialogs_container"), groupDynCommon.notfoundGrp,
            "60px");

      }
    }
  });
}
// 分享到我的群组---点击选择群组---点击列出的群组列表--------zzx--------------------------
groupDynamic.clickMyGroupName = function(obj) {
  $("#id_share_ui_param").attr("code", $(obj).attr("code"));
  $("#id_chioce_group_name").val($(obj).html());
  $("#id_group_ac_container").empty();
  $("#id_group_add_group_name_list").hide();
  $("body").css("overflow", "auto");
}
// 分享到我的群组----群组名字自动填充回调----zzx-----
groupDynamic.listShow = function(data) {
  var htmlStr = "<ul class='ac_item_list'>";
  if (data != null && data != "") {
    $.each(data, function(i, obj) {
      htmlStr += "<li class='text' code='" + obj.code + "' onclick='groupDynamic.clickMyGroupName(this);'>" + obj.name
          + "</li>";
    });
    htmlStr += "</ul>";
    $("#id_group_ac_container").empty();
    $("#id_group_ac_container").html(htmlStr);
  } else {
    $("#id_group_ac_container").html("");
  }
}
// 分享到我的群组----群组名字自动填充回车事件----zzx-----
groupDynamic.listEnter = function() {
  if ($("#id_group_ac_container").find("li").length > 0 && autocompleteword.liIndex != -1) {
    groupDynamic.clickMyGroupName($("#id_group_ac_container").find("li").eq(autocompleteword.liIndex));
  } else {
    $("#id_chioce_group_name").val("");
    $("#id_group_ac_container").html("");
  }
}
// 分享到我的联系人----点击选择联系人----zzx-----
groupDynamic.showMyfriends = function() {
  if ($("#id_group_add_friend_names_list").find(".select_person_container").length > 0) {
    groupDynamic.showMyfriendsResult();
    $("#id_group_add_friend_names_list").show();
  } else {
    $("#id_group_add_friend_names_list").find(".select_friends_list").doLoadStateIco({
      status : 1
    });
    $("#id_group_add_friend_names_list").show();
    ajaxparamsutil.doAjax("/psnweb/friend/ajaxgetmyfriendnames", {}, function(data) {
      $("#id_group_add_friend_names_list").find(".select_friends_list").html(data);
      groupDynamic.showMyfriendsResult();
    }, "html");
  }
}
// 分享到我的联系人----点击选择联系人--回显--zzx-----
groupDynamic.showMyfriendsResult = function() {
  if ($("#id_group_add_friend_names_list").find(".person_namecard_whole_tiny").length > 0) {
    $("#id_group_add_friend_names_list").find(".person_namecard_whole_tiny").removeClass("chosen");
    $("#share_plugin_select_friends").find(".content").each(
        function(i, o) {
          $("#id_group_add_friend_names_list").find("div[code='" + $(o).attr("code") + "']").closest(
              ".person_namecard_whole_tiny").addClass("chosen");
        });
  } else {
    scmpublicprompt($("#id_group_add_friend_names_list").find(".select_friends_list"), groupDynCommon.notfoundFriend,
        "60px");
  }

}
// 分享到我的联系人---点击选择联系人---点击确定----zzx-----
groupDynamic.getFriendsInfo = function() {
  var htmlStr = "";
  $("#id_group_add_friend_names_list").find(".chosen").each(
      function(i, obj) {
        htmlStr += "<div class='chips_normal'>" + "<div class='content' code='" + $(obj).find(".name").attr("code")
            + "' name='" + $(obj).find(".name").html() + "'>" + $(obj).find(".name").html() + "</div>"
            + "<div class='delete' onclick='groupDynamic.delFriendName(this)'>" + "<i class='material-icons'>close</i>"
            + "</div></div>";
      });
  $("#id_textarea_friend_name").closest(".share_plugin_add_friends").find(".chips_normal").remove();
  $("#id_textarea_friend_name").before(htmlStr);
  $("#id_group_add_friend_names_list").hide();
}
// 分享到我的联系人---点击选择联系人---点击取消----zzx-----
groupDynamic.cancleFriendsInfo = function() {
  $("#id_group_add_friend_names_list").hide();
}
// 分享到我的联系人----直接输入-名字自动填充回调----zzx-----
groupDynamic.FriendListShow = function(data) {
  if (data != null && typeof data != "undefined" && data.length > 0) {
    var htmlStr = "<ul class='ac_item_list'>";
    $.each(data, function(i, obj) {
      htmlStr += "<li class='text' code='" + obj.code + "' onclick='groupDynamic.clickMyFriendName(this);'>" + obj.name
          + "</li>";
    });
    htmlStr += "</ul>";
    $("#id_friend_ac_container").html(htmlStr);
  } else {
    $("#id_friend_ac_container").html("");
  }
}
// 分享到我的联系人----直接输入-名字自动填充回调----zzx-----
groupDynamic.FriendlistEnter = function() {
  if ($("#id_friend_ac_container").find("li").length > 0 && autocompleteword.liIndex != -1) {
    groupDynamic.clickMyFriendName($("#id_friend_ac_container").find("li").eq(autocompleteword.liIndex));
  } else {
    $("#id_textarea_friend_name").html("");
    $("#id_friend_ac_container").html("");
  }
}
// 分享到我的联系人---点击名字列表---zzx-----
groupDynamic.clickMyFriendName = function(obj) {
  var status = 0;// 开关，防止重复添加相同的人员
  var htmlStr = "<div class='chips_normal'><div class='content' code='"
      + $(obj).attr("code")
      + "' name='"
      + $(obj).html()
      + "'>"
      + $(obj).html()
      + "</div><div class='delete' onclick='groupDynamic.delFriendName(this)'><i class='material-icons'>close</i></div>";
  $("#share_plugin_select_friends").find(".content").each(function(i, o) {
    if ($(o).attr("code") == $(obj).attr("code")) {
      status = 1;
    }
  });
  if (status == 0 && obj != null) {
    $("#id_textarea_friend_name").before(htmlStr);
  }
  $("#id_textarea_friend_name").html("");
  $("#id_friend_ac_container").empty();
}
// 分享到我的联系人--删除联系人
groupDynamic.delFriendName = function(obj) {
  $(obj).closest(".chips_normal").remove();
}
// 分享到我的联系人----在联系人列表点击取消----zzx-----
groupDynamic.canclefromfriends = function() {
  $("#id_fluctuate").find(".fluctuate_public").show();
  $("#id_fluctuate").find(".fluctuate_public").next().remove();
}
// 分享到我的联系人----在联系人列表点击确认----zzx-----
groupDynamic.surefromfriends = function() {
  var htmlStr = "";
  $("#invitePsn_psnList_check").find(".chips_normal").each(
      function(i, obj) {
        htmlStr += "<div class='chips_normal' onclick='groupDynamic.remove(this)' des3PsnId='"
            + $(obj).find(".content").attr("val") + "' name='" + $(obj).find(".content").text() + "'>"
            + $(obj).find(".content").text() + "<i class='material-icons icon-clear' >clear</i></div>";
      });
  $("#id_fluctuate").find(".addfriend_name").html(htmlStr);
  $("#id_fluctuate").find(".fluctuate_public").show();
  $("#id_fluctuate").find(".fluctuate_public").next().remove();
}
// 分享到我的联系人--拷贝老系统的逻辑----zzx-----
groupDynamic.sendShareForFriendNew = function(receivers, shareContent, commendDendLine, shareTitleCN, shareTitleEN,
    isAddShareTimes, des3pubId) {
  var post_data = {
    "receivers" : receivers,
    "des3PubIds" : des3pubId,
    "shareDeadline" : commendDendLine,
    "articleName" : "publication",
    "resType" : 1,
    "dbid" : "",
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
      groupDynamic.doTimeOut(data, "json");
      if (data.result == "success") {
        // 更新分享数
        groupDynamic.updateDynShareCount();
        groupDynamic.addOneShareCount();
        scmpublictoast(groupDynCommon.shareSuccess, 1000);
      }
    },
    error : function() {
      scmpublictoast("error", 1000);
    }
  });
  groupDynamic.cancleShare();
}
groupDynamic.cleanParam = function() {
  $("#id_chioce_group_name").keydown(function(event) {
    var code = event.keyCode;
    if (code != 13 && code != 38 && code != 40) {
      $("#id_share_ui_param").attr("code", "");
    }
  });
}
// 点击删除当前元素----zzx-----
groupDynamic.remove = function(obj) {
  $(obj).remove();
}
// 打开人员主页----zzx-----
groupDynamic.openPsnDetail = function(des3ProducerPsnId, event) {
  if (des3ProducerPsnId != null && des3ProducerPsnId != "") {
    location.href = "/scmwebsns/resume/psnView?des3PsnId=" + encodeURIComponent(des3ProducerPsnId);
    groupDynamic.stopPropagation(event);
  }
}
// 打开成果详情----zzx-----
groupDynamic.openPubDetail = function(des3PubId, event) {
  if (des3PubId != null && des3PubId != "") {
    window.open("/pubweb/publication/wait?des3Id=" + encodeURIComponent(des3PubId) + "&des3GroupId="
        + encodeURIComponent($("#des3GroupId").val()) + "&currentDomain=/pubweb&pubFlag=1");
    // window.open("/scmwebsns/publication/view?des3Id="+encodeURIComponent(des3PubId)+"&currentDomain=/pubweb&pubFlag=1");
    groupDynamic.stopPropagation(event);
  }
}
// 下载动态分享的文件
groupDynamic.openFile = function(fileId, nodeId, type) {
  location.href = snsctx + "/file/download?des3Id=" + fileId + "&nodeId=" + nodeId + "&type=" + type;
}
// 下载成果全文
// 全文下载
groupDynamic.fullTextDownload = function(fileId) {
  if (fileId != undefined && fileId != "") {
    window.location.href = "/pubweb/fulltext/ajaxgetinfobyfileId?fulltextFileId=" + fileId;
    return;
  }
}