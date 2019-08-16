/**
 * 移动端消息中心
 */
var midmsg = midmsg ? midmsg : {};
// 移动端-消息菜单-待确认成果列表展示
midmsg.confimPubList = function() {
  window.location.href = "/pub/confirmpublist?toBack=centerMsg";
};
// 移动端-消息菜单-待确认的全文
midmsg.confimpubftrcmd = function() {
  window.location.href = "/pub/wechat/pubfulltextlist?toBack=centerMsg";
};
/**
 * 显示会话窗口
 */
midmsg.showchatpsnUI = function() {
  $(".dev_meun_item").hide();

  $("#meun_items").show();
  $("#id_meun_main").show();
  $("#msg-chatpsn-list").show();
  $("#meun_items").find("div.message-page__fuctool-item").eq(1).click();
  $("#meun_items").find("div.message-page__fuctool-item").eq(1).mouseup();
}
midmsg.hidemeunandsearch = function() {
  $("#id_meun_main").hide();

}
midmsg.showmeunandsearch = function() {
  $("#id_meun_main").show();
  $("#id_midchatsearch").show();
  $("#meun_items").show();
}

midmsg.setTitle = function(text) {
  $("#id_meun_header").find(".message-page__psnheader-title").text(text);
}
midmsg.setChatCount = function(count) {
  $("#meun_items").find("span").eq(1).html(count);
}
midmsg.showTitleBack = function(myfunction) {
  // 新的聊天页面不要添加margin-left
  if ($("#id_meun_header").find(".message-page__psnheader-title").attr("chat") === "new") {
  }

  var $obj = $("#id_meun_header").find(".material-icons");
  midmsg.oldBack = $obj.attr("onclick");
  $obj.show().attr("onclick", myfunction)
}
midmsg.hideTitleBack = function() {
  $("#id_meun_header").find(".message-page__psnheader-title").css("margin-left", "0px");
  $("#id_meun_header").find(".material-icons").attr("onclick", "").hide();
}
midmsg.checkMsgInput = function(count) {
  $("#mid_msg_content").keyup(function(e) {
    var text = $.trim($("#mid_msg_content").val());
    if (text.length > count) {
      $("#mid_msg_content").val(text.substr(0, count));
      BaseUtils.set_focus($("#mid_msg_content")[0]);
    }
  });
}
/**
 * 更新全文认领和成果认领 midmsg.showMsgTip = function() { $.ajax({ url : '/pub/wechat/ajaxmsgtips', type :
 * 'post', dataType : 'json', data : { des3PsnId : $("#id_meun_header").attr("des3PsnId") }, success :
 * function(data) { BaseUtils.ajaxTimeOut(data, function() { if (Number(data.pubConfirmCount) > 0) {
 * $("#confirmPubNum").html(data.pubConfirmCount);
 * $("#confirmPubNum").closest(".main-list__item").show();
 * $("#confirmPubNum").closest(".main-list__item").attr("onclick", "midmsg.confimPubList()"); } if
 * (Number(data.pubFulltextCount) > 0) { $("#confirmfullTextNum").html(data.pubFulltextCount);
 * $("#confirmfullTextNum").closest(".main-list__item").show();
 * $("#confirmfullTextNum").closest(".main-list__item").attr("onclick", "midmsg.confimpubftrcmd()"); }
 * var $response_no = $("div[list-main='mobile_msg_center_list']").find(".response_no-result"); if
 * (Number(data.pubConfirmCount) <= 0 && Number(data.pubFulltextCount) <= 0) { setTimeout(function() {
 * if ($("#mobile_msg_claim_list").find(".main-list__item:hidden").length == 2) {
 * $response_no.show(); } $("div[scm_id='load_state_ico']").remove();
 * $("div[list-main='mobile_msg_center_list']").find(".preloader").remove(); }, 1); } else {
 * $response_no.hide(); } }); } }); }
 */
/**
 * 由于需求变更(SCM-24313),需要将成果认领和全文认领直接显示在消息通知下,默认先显示全文认领,再显示成果认领,邮件和个人成果列表根据参数(whoFirst)先显示全文认领还是成果认领
 */
midmsg.showMsgTip = function() {
  var whoFirst = "";
  var whoFirstObj = $("#whoFirst");
  if (whoFirstObj.length > 0) {
    whoFirst = whoFirstObj.val();
  }
  whoFirst = whoFirst == "" ? "default" : whoFirst;
  midmsg.loadIco($("#mobile_msg_claim_list"), "html");
  $.ajax({
    url : '/pub/wechat/ajaxpubfulltextshow',
    type : 'post',
    dataType : 'html',
    data : {
      "whoFirst" : whoFirst
    },
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
        $("#mobile_msg_claim_list").html(data);
      });
    },
    error : function(data) {

    }
  });
}
/**
 * 显示隐藏底部菜单 type 1=显示 0=隐藏
 */
midmsg.hideBottomMenu = function(type) {
  if (type && type == 1) {
    $("div[class='btn_navigation']").show();
  } else {
    $("div[class='btn_navigation']").hide();
  }
}
/**
 * 加载消息未读\已读状态 i：忽略的项 1=消息通知 2=站内信 3=全文请求
 */
midmsg.loadMsgStatus = function(myfunction) {
  $.ajax({
    url : '/dynweb/showmsg/ajaxcountunreadmsg',
    type : 'post',
    dataType : 'json',
    data : {},
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data["result"] == "success") {
          $(".menu_msg > .btn_navigation_point").hide();
          var items = $("#meun_items").find("p");
          items.css("color", "#134FA6");

          if (Number(data["7"]) > 0) {
            items.eq(0).css("color", "red");
            $(".menu_msg > .btn_navigation_point").show();
            items.eq(0).html("(" + data["7"] + ")");
          } else {
            items.eq(0).html("");
          }
          if (Number(data["11"]) > 0) {
            items.eq(2).css("color", "red");
            $(".menu_msg > .btn_navigation_point").show();
            items.eq(2).html("(" + data["11"] + ")");
          } else {
            items.eq(2).html("");
          }
          // 消息通知start
          var msgNotifyCount = 0;
          if (Number(data["2"]) > 0) {
            msgNotifyCount += Number(data["2"]);
          }
          if (Number(data["3"]) > 0) {
            msgNotifyCount += Number(data["3"]);
          }
          if (msgNotifyCount > 0) {
            items.eq(1).css("color", "red");
            $(".menu_msg > .btn_navigation_point").show();
            items.eq(1).html("(" + msgNotifyCount + ")");
          } else {
            items.eq(1).html("");
          }
          // 消息通知end
          if (typeof myfunction == "function") {
            myfunction();
          }
          setTimeout(function() {
            $("div[scm_id='load_state_ico']").remove();
          }, 1);
        }
      });
    }
  });
}
/**
 * 载入页面切换事件
 */
midmsg.loadMain = function() {
  var targetlist = document.getElementsByClassName("message-page__fuctool-item");
  var targetline = document.getElementsByClassName("message-page__selector-line")[0];
  var targetsearch = document.getElementsByClassName("message-page__neck-search")[0];
  var localizelist = document.getElementsByClassName("message-page__fuctool-selector")[0];
  var standwidth = document.getElementsByClassName("message-page__fuctool")[0].offsetWidth / 3 - 33;
  targetline.style.width = standwidth + "px";
  targetline.style.left = localizelist.offsetLeft + 15 + "px";
  for (var i = 0; i < targetlist.length; i++) {
    targetlist[i].onclick = function() {
      document.getElementsByClassName("message-page__fuctool-selector")[0].classList
          .remove("message-page__fuctool-selector");
      this.classList.add("message-page__fuctool-selector");
      var truetarget = document.getElementsByClassName("message-page__fuctool-selector")[0];
      var disleft = document.getElementsByClassName("message-page__fuctool-selector")[0].offsetLeft;
      var diswidth = document.getElementsByClassName("message-page__fuctool-selector")[0].offsetWidth;
      this.closest(".message-page__fuctool").querySelector(".message-page__selector-line").style.left = disleft + 15
          + "px";
      this.closest(".message-page__fuctool").querySelector(".message-page__selector-line").style.width = diswidth - 30
          + "px";
    }
  }
  targetsearch.onfocus = function() {
    this.style.borderColor = "#ddd";
    this.closest(".message-page__search-box").style.borderColor = "#288aed";
  }
  targetsearch.onblur = function() {
    this.closest(".message-page__search-box").style.borderColor = "#ddd";
  }
}
midmsg.menumodel = function(model) {
  if (model == "reqFullTextMsg") {
    $("#meun_items").find("div.message-page__fuctool-item").eq(2).click();
  } else if (model == "centerMsg") {
    $("#meun_items").find("div.message-page__fuctool-item").eq(1).click();
  } else if (model == null || model == "" || model == "chatMsg") {
    $("#meun_items").find("div.message-page__fuctool-item").eq(0).click();
  }
}
/**
 * 切换事件
 */
midmsg.meunItemsEvent = function() {
  $("#meun_items").find("div.message-page__fuctool-item").click(function() {
    var $this = $(this);
    var itemId = $this.attr("item-id");
    // $this.find("span").css("color", "#134FA6");
    var index = $("#meun_items").find("div.message-page__fuctool-item").index($this);
    $(".dev_meun_item").hide();
    $("#id_midchatsearch").val("");
    $("#meun_items").find("div.message-page__fuctool-item").removeClass("active");
    midmsg.setTitle("消息");
    // 隐藏新建消息按钮
    $("#id_menu_header_new_msg").css("visibility", "hidden");
    if (index == 1) {
      BaseUtils.changeUrl('centerMsg');
      $this.addClass("active");
      $("#msg-center-list").show();
      // $this.find("span").css("color", "#134FA6");
      // 获取谁先显示
      midmsg.loadMsgCenterList();
      midmsg.loadMsgStatus(1);
      midmsg.setSearchInput("检索消息通知");
      $("#id_meun_main").find(".main-list__searchbox").attr("list-search", "");
    } else if (index == 0) {
      BaseUtils.changeUrl('chatMsg');
      $this.addClass("active");
      $("#msg-chatpsn-list").show();
      // $this.find("span").css("color", "#134FA6");
      midmsg.loadMsgChatPsnList();
      midmsg.loadMsgStatus(2);
      midmsg.setSearchInput("检索站内信");
      $("#id_meun_main").find(".main-list__searchbox").attr("list-search", "");
      $("div[list-main='mobile_msg_chatpsn_list']").show();
      // 显示新建消息按钮
      $("#id_menu_header_new_msg").css("visibility", "");
    } else if (index == 2) {
      BaseUtils.changeUrl('reqFullTextMsg');
      $("#msg-chat_friend_all-list").hide();
      $this.addClass("active");
      $("#msg-fulltext-list").show();
      // $this.find("span").css("color", "#134FA6");
      midmsg.loadMsgFullTextList();
      midmsg.loadMsgStatus(3);
      midmsg.setSearchInput("检索全文请求");
    }
    midmsg.hideBottomMenu(1);
  });
}

/**
 * 加载消息中心列表 midmsg.loadMsgCenterList = function() { var $this = $(this); midmsg.msgcenterlist =
 * window.Mainlist({ name : "mobile_msg_center_list", listurl : "/dynweb/mobile/ajaxgetmidmsglist",
 * listdata : { "pageSource" : "mobile" }, method : "scroll", listcallback : function(xhr) { var
 * $response_no = $("div[list-main='mobile_msg_center_list']").find(".response_no-result"); if
 * ($response_no.is(':hidden')) { setTimeout(function() { if
 * ($("#mobile_msg_claim_list").find(".main-list__item:hidden").length == 2) { $response_no.show(); }
 * $("div[scm_id='load_state_ico']").remove();
 * $("div[list-main='mobile_msg_center_list']").find(".preloader").remove(); }, 1); } else { if
 * ($("#mobile_msg_claim_list").find(".main-list__item:hidden").length == 2) { $response_no.show(); }
 * else { $response_no.hide(); } } } }); }
 */
/**
 * 由于需求变更(SCM-24313),需要将成果认领和全文认领直接显示在消息通知下,默认先显示全文认领,再显示成果认领,邮件和个人成果列表根据参数(whoFirst)先显示全文认领还是成果认领
 */
midmsg.loadMsgCenterList = function() {
  midmsg.showMsgTip();
}
// 站内信 内容删除
midmsg.delMsgContent = function(msgRelationId, ev) {
  var $this = $(ev.currentTarget);
  BaseUtils.doHitMore($this, 1000);
  $.ajax({
    url : "/dynweb/showmsg/ajaxdelchatmsg",
    dataType : "json",
    type : "post",
    data : {
      des3MsgRelationId : des3MsgRelationId,
      des3SenderId : des3SenderId
    },
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        midmsg.loadMsgChatList(des3SenderId, 1, "html");
      });
    }
  });
}
