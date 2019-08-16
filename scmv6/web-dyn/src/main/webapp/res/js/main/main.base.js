/**
 * 科研之友主页js--zzx
 */
var MainBase = MainBase ? MainBase : {};
// 发表动态
MainBase.realtime = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  var resId = $("#main_share__card").find(".aleady_select_pub").attr("pubId");
  var des3ResId = $("#main_share__card").find(".aleady_select_pub").attr("des3PubId");
  var textarea = $.trim($("#main_share__card").find("textarea").val());
  if (textarea.length > 500) {
    scmpublictoast("讨论内容不能超过500个字符", 1000);
    return;
  }
  textarea = textarea.replace(/&lt;(.*)&gt;.*&lt;\/\1&gt;|&lt;(.*) \/&gt;/, "&_lt;$1&_gt;").replace(/</g, "&lt;")
      .replace(/>/g, "&gt;").replace(/\n/g, '<br>');// SCM-9848
  var dynType = "ATEMP";
  var operatorType = -1;
  if ("" == textarea && resId != "") {
    dynType = "B2TEMP";
    operatorType = 3;
  }
  if (resId != "") {
    resType = 1;
  } else {
    resType = 0;
  }
  var dataJson = {
    "dynType" : dynType,
    "dynText" : textarea,
    "resType" : resType,
    "des3ResId" : des3ResId,
    "operatorType" : operatorType
  }
  $.ajax({
    url : '/dynweb/dynamic/ajaxrealtime',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      BaseUtils.ajaxTimeOut(data,
          function() {
            MainBase.hidePublish();
            MainBase.clearSelectedPub();
            dynamic.ajaxLondDynList();
            /**
             * 以下代码解决 SCM-13810 发布换行动态内容后，再点击发起新讨论，输入框大小变大的问题
             */
            var $textarea = $("#main_share__card").find("textarea")[0];
            $textarea.value = "";
            $("#id_dyn_content_length").html("0");
            var $div = $("#main_share__card").find(".textarea-autoresize-div")[0]; // 自动填词辅助框，用来计算高度
            $div.innerHTML = $textarea.value;
            $textarea.closest(".input__area").style.height = window.getComputedStyle($div, null).getPropertyValue(
                "height");
          });
    },
    error : function() {
      scmpublictoast(mainBase.shareFail, 1000);
    }
  });

}
MainBase.clearSelectedPub = function() {
  var pubinfo = $(".dev_share_pubinfo");
  pubinfo.hide();
  pubinfo.find(".aleady_select_pub").attr("pubId", "");
  pubinfo.find(".pub-idx__main_title").html("");
};
// 关闭评论选中的成果
MainBase.clearCommentSelectedPub = function() {
  var pubinfo = $(".dev_comment_share_pubinfo");
  pubinfo.hide();
  pubinfo.find(".aleady_select_pub").attr("pubId", "");
  pubinfo.find(".pub-idx__main_title").html("");
};
MainBase.sureSharePub = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  var item = $("#myPubListId").find("input:radio[name='pub-type']:checked").closest(".main-list__item");
  var pubinfo = $(".show_selected_pub_info");
  pubinfo.show();
  pubinfo.find(".aleady_select_pub").attr("pubId", item.attr("pubid"));
  pubinfo.find(".aleady_select_pub").attr("des3PubId", item.attr("des3PubId"));
  pubinfo.find(".pub-idx__main_title").html(item.find(".pub-idx__main_title").text());
  MainBase.hideMsgPubUI();
}
// 点击发表动态
MainBase.showPublish = function(obj) {
  $("#main_share__card").find(".dev_do_share").show();
  $("#main_share__card").find(".dynamic-cmt__new-post_deactive").hide();
  // $("#main_share__card").find(".dev_share_input").show();
  $("#main_share__card").find(".dev_share_input").removeClass("element__piblic-none");
  $("#main_share__card").find(".dev_share_input").addClass("element__piblic-block");
  $("#main_share__card").find("textarea").focus();
  MainBase.PublishInterval = setInterval(function() {// 确定按钮的监听
    MainBase.is_canshare();
  }, 1000);
};
// 关闭发表动态
MainBase.hidePublish = function(obj) {
  $("#main_share__card").find(".dev_do_share").hide();
  $("#main_share__card").find(".dynamic-cmt__new-post_deactive").show();
  // $("#main_share__card").find(".dev_share_input").hide();
  $("#main_share__card").find(".dev_share_input").removeClass("element__piblic-block");
  $("#main_share__card").find(".dev_share_input").addClass("element__piblic-none");
  $("#main_share__card").find("textarea").val("");
  clearInterval(MainBase.PublishInterval);// 取消确定按钮的监听
};
// 显示成果UI
MainBase.showMsgPubUI = function() {
  MainBase.msgpubBoxInterval = setInterval(function() {// 确定按钮的监听
    MainBase.is_pubsend();
  }, 1000);
  showDialog("select_my_pub_import_dialog");
  $(".dev_share_pubinfo").addClass("show_selected_pub_info");
  MainBase.showMyPubList();
}
// 评论中显示成果UI
MainBase.showCommentPubUI = function() {
  MainBase.msgpubBoxInterval = setInterval(function() {// 确定按钮的监听
    MainBase.is_pubsend();
  }, 1000);
  showDialog("select_my_pub_import_dialog");
  $(".dev_comment_share_pubinfo").addClass("show_selected_pub_info");
  MainBase.showMyPubList();
}

// 关闭成果UI
MainBase.hideMsgPubUI = function() {
  $("#search_my_pub_key").val("");
  clearInterval(MainBase.msgpubBoxInterval);// 取消确定按钮的监听
  hideDialog("select_my_pub_import_dialog");
  $(".show_selected_pub_info").removeClass("show_selected_pub_info");
}
// 文件发送按钮的disabled设置
MainBase.is_canshare = function() {
  var text = $.trim($("#main_share__card").find("textarea").val()).length;
  var pubId = $("#main_share__card").find(".aleady_select_pub").attr("pubId").length;
  if (text > 0 || pubId > 0) {
    $("#main_share__card").find(".button_primary").removeAttr("disabled");
  } else {
    $("#main_share__card").find(".button_primary").attr("disabled", true);
  }
}
// 文件发送按钮的disabled设置
MainBase.is_pubsend = function() {
  if ($("#select_my_pub_import").find("input:radio[name='pub-type']:checked").length > 0) {
    $("#select_my_pub_import").find(".button_primary-reverse").removeAttr("disabled");
  } else {
    $("#select_my_pub_import").find(".button_primary-reverse").attr("disabled", true);
  }
}
// 主页 我的成果列表
MainBase.showMyPubList = function() {
  $("#grpPubMyPubListId").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  MainBase.myfileMainlist = window.Mainlist({
    name : "mypublist",
    listurl : "/groupweb/grppub/ajaxmsgpublist",
    listdata : {
      "isRadio" : true
    },
    method : "scroll",
    listcallback : function(xhr) {
    },
  });
}
// //获取群组列表
MainBase.getGrpList = function() {
  var grpList = $("#grp_list");
  grpList.doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
    status : 1,
    addWay : "after"
  });
  $.ajax({
    url : '/groupweb/mygrp/ajaxgrplistformain',
    type : 'post',
    data : {
      "page.pageSize" : "3",
      "page.ignoreMin" : true
    },
    dataType : 'html',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        grpList.closest("ul").find("*[scm_id='load_state_ico']").remove();
        grpList[0].innerHTML = data;
      });
    }
  });
}
// //获取来访人员列表
MainBase.getPsnList = function() {
  $("#psn_list").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
    status : 1
  });
  $.ajax({
    url : '/psnweb/friendreq/ajaxvistpsnlist',
    type : 'post',
    data : {
      "page.pageSize" : "3",
      "page.ignoreMin" : true
    },
    dataType : 'html',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        $("#psn_list").html(data);
        if ($("#psn_list").find(".main-list__item").length == 0) {
          $("#psn_list").html("<div class='response_no-result'>" + mainBase.noViewRecord + "</div>");
        }
      });
    }
  });
}
// 常用功能
MainBase.getShortcuts = function() {
  $.ajax({
    url : '/dynweb/main/ajaxgetdynshortcuts',
    type : 'post',
    data : {},
    dataType : 'html',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        $("#shortcuts_list").html(data);
      });
    }
  });
}
// 添加联系人
MainBase.addOneFriend = function(des3PsnId, obj) {
  BaseUtils.doHitMore(obj, 1000);
  $.ajax({
    url : '/psnweb/friend/ajaxaddfriend',
    type : 'post',
    data : {
      'des3Id' : des3PsnId
    },
    dataType : 'json',
    timeout : 10000,
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.result == "true") {
          scmpublictoast(mainBase.sentSuccess, 1000);
        } else {
          scmpublictoast(data.msg, 1000);
        }
      });
    }
  });
};

MainBase.autoFollowPsn = function() {
  $.ajax({
    url : '/psnweb/friendreq/ajaxautofollowing',
    type : 'post',
    data : {},
    dataType : 'json',
    success : function(data) {
      setTimeout("dynamic.ajaxLondDynList()", 100);
    },
    error : function() {
      setTimeout("dynamic.ajaxLondDynList()", 100);
    }
  });
}

MainBase.showVistPsnMoreUI = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    showDialog("dev_vist_psn_more");
    MainBase.getPsnListMore();
  }, 1);
}
MainBase.hideVistPsnMoreUI = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  hideDialog("dev_vist_psn_more");

}
MainBase.getPsnListMore = function() {
  MainBase.PsnListMore = window.Mainlist({
    name : "vist_psn_more_list",
    listurl : "/psnweb/friendreq/ajaxvistpsnlist",
    listdata : {
      "isAll" : "1"
    },
    method : "scroll",
    listcallback : function(xhr) {
    },
  });
}