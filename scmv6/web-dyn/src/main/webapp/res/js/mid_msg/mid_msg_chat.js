/**
 * 移动端站内信js
 */
var midmsg = midmsg ? midmsg : {};
/**
 * 获取当前窗口 active 0=消息通知；1=站内信会话； 2=全文请求
 */
midmsg.getcurrentActive = function() {
  var active;
  var $meun_items = $("#meun_items").find("div.message-page__fuctool-item");
  var index = $meun_items.index($("#meun_items").find(".active"));
  if (index == 0) {
    active = 0;
  } else if (index == 1) {
    if ($("#id_msg_box").is(":hidden")) {
      active = 1;
    } else {
      active = 11;
    }
  } else if (index == 2) {
    active = 2;
  }
  return active;
}
/**
 * 打开消息详情
 */
midmsg.openchatdetails = function(type, ev) {
  var $this = $(ev.currentTarget);
  var url = "";
  var des3relationid = $this.closest(".main-list__item").attr("des3relationid");
  if ("pub" == type) {
    var des3PubId = $this.closest(".dev_detail").attr("des3PubId");
    url = "/pub/details/snsnonext?des3PubId=" + encodeURIComponent(des3PubId) + "&des3relationid=" + des3relationid;
  } else if ("pdwhpub" == type) {
    var des3PubId = $this.closest(".dev_detail").attr("des3PubId");
    var dbid = $this.closest(".dev_detail").attr("dbid");
    url = "/pub/details/pdwh?useoldform=true&des3PubId=" + encodeURIComponent(des3PubId) + "&language=ZH&index=0";
  } else if ("fund" == type) {
    var des3FundId = $this.closest(".dev_detail").attr("des3FundId");
    url = "/prjweb/wechat/findfundsxml?des3FundId=" + encodeURIComponent(des3FundId);
  } else if ("prj" == type) {
    var des3PrjId = $this.closest(".dev_detail").attr("des3PrjId");
    url = "/prjweb/wechat/findprjxml?des3PrjId=" + encodeURIComponent(des3PrjId);
  } else if ("agency" == type) {
    var des3AgencyId = $this.closest(".dev_detail").attr("des3AgencyId");
    url = "/prj/mobile/agencydetail?des3FundAgencyId=" + des3AgencyId;
  }
  window.location.href = url;
}
// 标记已读
midmsg.setChatReadMsg = function(des3SenderId) {
  $.ajax({
    url : "/dynweb/showmsg/ajaxsetread",
    dataType : "json",
    type : "post",
    data : {
      des3SenderId : des3SenderId,
      readMsgType : 2
    },
    success : function(data) {
      // 消息统计数
      midmsg.loadMsgStatus();
    }
  });
}
/**
 * 下载
 */
midmsg.downloadMain = function(ev) {
  var $this = $(ev.currentTarget);
  var downloadUrl = $this.closest(".main-list__item").find("input[name='downloadUrl']").val();
  var des3PubId = $this.closest(".dev_detail").attr("des3pubid");
  var des3FileId = $this.closest(".dev_detail").attr("des3FileId");
  var type = $this.closest(".main-list__item").attr("type");
  if (type == "pub") {
    $.ajax({
      url : "/pub/fulltext/getpermission",
      type : "post",
      data : {
        "des3SearchPubId" : des3PubId,
        "des3FileId" : des3FileId
      },
      dataType : "json",
      success : function(data) {
        if (data.result == "success") {
          if (data.fullTextPermission != 0 && data.des3OwnerPsnId != des3CurrentPsnId) {
            // 全文隐私则发送全文请求
            Smate.confirm("提示", "全文未公开，是否请求查看？", function() {
              midmsg.requestFullText(des3PubId, 'sns', data.des3OwnerPsnId, function() {
                newMobileTip("全文请求已发送");
              })
            }, ["确定", "取消"]);
            return;
          }
          midmsg.downloadFullText(downloadUrl);
        }
      }
    })
  }
  if (type == "pdwhpub") {
    midmsg.downloadFullText(downloadUrl);
  }
  if (type == "file") {
    midmsg.downloadfile(downloadUrl);
  }
  if (type == "fulltext") {
    midmsg.downloadFullText(downloadUrl);
  }
}

/**
 * 请求全文
 */
midmsg.requestFullText = function(des3PubId, requestPubType, OwnerDes3Id, myFunction) {
  $.ajax({
    url : '/pub/fulltext/ajaxreqadd',
    type : 'post',
    data : {
      'des3RecvPsnId' : OwnerDes3Id,
      'des3PubId' : des3PubId,
      'pubType' : requestPubType
    },
    dataType : "json",
    success : function(data) {
      if (data.status == "success") { // 全文请求保存成功
        if (typeof myFunction == 'function') {
          myFunction();
        } else {
          // 用scmpublictoast提示，这里面有判断是移动端还是pc端
          scmpublictoast(dynCommon.requestFullTextSuccess, 2000, 1);
        }
      } else if (data.status == "isDel") {
        scmpublictoast(dynCommon.pubIsDeleted, 2000, 3);
      } else {
        scmpublictoast(dynCommon.requestFullTextError, 2000, 2);
      }
    },
    error : function() {
      scmpublictoast(dynCommon.requestFullTextError, 2000, 2);
    }
  });
}

/**
 * 下载全文
 */
midmsg.downloadFullText = function(downloadUrl) {
  if (downloadUrl != "") {
    Smate.confirm("下载提示", "要下载全文附件吗？", function() {
      window.location.href = downloadUrl;
    }, ["下载", "取消"]);
  }
}
/**
 * 下载文件
 */
midmsg.downloadfile = function(downloadUrl) {
  if (downloadUrl != "") {
    Smate.confirm("下载提示", "要下载文件吗？", function() {
      window.location.href = downloadUrl;
    }, ["下载", "取消"]);
  }
}
/**
 * 下载成果全文
 */
midmsg.downloadPubFulltext = function(downloadUrl) {
  if (downloadUrl != "") {
    Smate.confirm("下载提示", "要下载全文附件吗？", function() {
      window.location.href = downloadUrl;
    }, ["下载", "取消"]);
  }
}
/**
 * 设置检索框内容
 */
midmsg.setSearchInput = function(text) {
  $("#id_midchatsearch").attr("placeholder", text);
}
/**
 * mid消息检索
 */
midmsg.ChatPsnListInputEvent = function() {
  var $input = $("#id_midchatsearch");
  var $meun_items = $("#meun_items").find("div.message-page__fuctool-item");

  var bind_name = 'input';
  if (navigator.userAgent.indexOf("MSIE") != -1) {
    bind_name = 'propertychange';
  }
  $input.bind(bind_name, function() {
    var index = $meun_items.index($("#meun_items").find(".active"));
    if (index == 1) {
      // 检索消息通知
      if ($.trim($input.val()) == "") {
        $meun_items.eq(index).click();
        return;
      }
    } else if (index == 0) {
      if ($("#id_msg_box").is(":hidden")) {
        // 检索人员会话
        if ($.trim($input.val()) == "") {
          $meun_items.eq(index).click();
          $("div[list-main='mobile_msg_chatpsn_list']").show();
          return;
        }
        midmsg.chatPsnListInputSearch();
      } else {
        // 检索消息内容
        if ($.trim($input.val()) == "") {
          midmsg.loadMsgChatList($("div[list-main='mobile_msg_chatbox__list']").attr("code"), 1, "html");
          return;
        }
        midmsg.chatMsgListInputSearch();
      }
    } else if (index == 2) {
      // 检索全文
      if ($.trim($input.val()) == "") {
        $meun_items.eq(index).click();
        return;
      }
    }
    setTimeout(function() {
      $("div[scm_id='load_state_ico']").remove();
      $("div[list-main='mobile_msg_center_list']").find(".preloader").remove();
    }, 3000);
  });

}
midmsg.chatMsgListInputSearch = function() {
  var $box = $("div[list-main='mobile_msg_chatbox__list']");
  $box.unbind("scroll", midmsg.loadMsgChatScroll);
  midmsg.loadIco($box, "prepend");
  $.ajax({
    url : "/dynweb/mobile/ajaxgetmidmsglist",
    dataType : "html",
    type : "post",
    data : {
      "searchKey" : $.trim($("#id_midchatsearch").val()),
      "msgType" : "7",
      "des3ChatPsnId" : $("div[list-main='mobile_msg_chatbox__list']").attr("code")
    },
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        $box.html(data);
        $box.scrollTop(2000);
        if ($box.find(".main-list__item").length == 0) {
          $box.html("<div class='response_no-result'>未找到相关消息记录,最多检索前100条消息</div>");
        }
        setTimeout(function() {
          $("div[scm_id='load_state_ico']").remove();
          $("div[list-main='mobile_msg_center_list']").find(".preloader").remove();
        }, 1);
      });
    }
  });
}
midmsg.chatPsnListInputSearch = function() {
  $("div[list-main='mobile_msg_chatpsn_list']").hide();
  $("#msg-chat_friend_all-list").show();
  var $chatFriendList = $("div[list-main='mobile_msg_friend_psn_list']");
  var $chatAllList = $("div[list-main='mobile_msg_all_psn_list']");
  var $chatRecordList = $("div[list-main='mobile_msg_chat_record_list']");
  $("#title_more").hide();
  $("#chat_record").hide();
  $("#title_friend").hide();
  // 结果全部隐藏，最后统一显示
  // $("#msg-chatfriend-list").hide();
  $("#msg-chatallpsn-list").hide();
  $("#msg-chatsearchmsg-list").hide();
  $chatFriendList.html("");
  $chatAllList.html("");
  $chatRecordList.html("");
  // 开始一个一个加载
  midmsg.loadSearchFriend($chatFriendList, $chatAllList, $chatRecordList);
}

midmsg.loadSearchFriend = function($chatFriendList, $chatAllList, $chatRecordList) {
  midmsg.loadIco($chatFriendList, "html");
  // 先请求联系人列表
  $.ajax({
    url : '/psnweb/mobile/ajaxgetchatfriendlist',
    type : 'post',
    data : {
      searchType : 0,
      psnTypeForChat : "friend",
      "page.pageSize" : "5",
      searchKey : $.trim($("#id_midchatsearch").val()),
      "page.ignoreMin" : true
    },
    dataType : 'html',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        $chatFriendList.html(data);
        if ($chatFriendList.find(".main-list__item").length > 0) {
          $("#title_friend").show();
        }
        // 请求全站人员列表
        midmsg.loadSearchAllPsn($chatFriendList, $chatAllList, $chatRecordList);
      });
    },
    error : function() {
      // 请求全站人员列表
      midmsg.loadSearchAllPsn($chatFriendList, $chatAllList, $chatRecordList);
    }
  });
}

// 检索全站人员列表
midmsg.loadSearchAllPsn = function($chatFriendList, $chatAllList, $chatRecordList) {
  midmsg.loadIco($chatAllList, "html");
  $("#msg-chatallpsn-list").show();

  $.ajax({
    url : '/psnweb/mobile/ajaxmsgallpsnlist',
    type : 'post',
    data : {
      searchType : 0,
      searchString : $.trim($("#id_midchatsearch").val()),
      "page.pageSize" : "5",
      "page.ignoreMin" : true
    },
    dataType : 'html',
    success : function(data) {

      BaseUtils.ajaxTimeOut(data, function() {
        $chatAllList.html(data);
        if ($chatAllList.find(".main-list__item").length > 0) {
          $("#title_more").show();
        }
        // 请求聊天记录列表
        midmsg.loadSearchChatRecord($chatFriendList, $chatAllList, $chatRecordList);
      });
    },
    error : function() {
      // 请求聊天记录列表
      midmsg.loadSearchChatRecord($chatFriendList, $chatAllList, $chatRecordList);
    }
  });
}

// 检索聊天记录
midmsg.loadSearchChatRecord = function($chatFriendList, $chatAllList, $chatRecordList) {
  midmsg.loadIco($chatRecordList, "html");
  $("#msg-chatsearchmsg-list").show();

  $.ajax({
    url : '/dynweb/mobile/ajaxgetsearchchatpsnlist',
    type : 'post',
    data : {
      "searchKey" : $.trim($("#id_midchatsearch").val())
    },
    dataType : 'html',
    success : function(data) {
      $("#chat_record").show();
      BaseUtils.ajaxTimeOut(data, function() {
        $chatRecordList.html(data);
        if ($chatRecordList.find(".main-list__item").length > 0) {
          $("#chat_record").show();
        }
        // 设置显示区域的高度
        var clienheight = document.body.clientHeight;
        document.getElementById("msg-chat_friend_all-list").style.height = clienheight - 215 + "px";
        if ($("#msg-chat_friend_all-list").find(".main-list__item").length == 0) {
          $("#msg-chatallpsn-list").show();
          $("#title_more").hide();
          $("#chat_record").hide();
          $("#title_friend").hide();
          $chatAllList.html("<div class='response_no-result'>未找到符合条件的记录</div>");
        }
      });
    },
    error : function() {
      // 设置显示区域的高度
      var clienheight = document.body.clientHeight;
      document.getElementById("msg-chat_friend_all-list").style.height = clienheight - 215 + "px";
      if ($("#msg-chat_friend_all-list").find(".main-list__item").length == 0) {
        $("#msg-chatallpsn-list").show();
        $("#title_more").hide();
        $("#chat_record").hide();
        $("#title_friend").hide();
        $chatAllList.html("<div class='response_no-result'>未找到符合条件的记录</div>");
      }
    }
  });
}

/**
 * 事件存储
 */
midmsg.ajaxEventArr = [];
/**
 * 加载样式
 */
midmsg.loadIco = function($obj, addWay) {
  if (addWay == null) {
    addWay = "html";
  }
  $("*[scm_id='load_state_ico']").remove();
  $obj.doLoadStateIco({
    "style" : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    "status" : 1,
    "addWay" : addWay
  });
}
/**
 * 点击检索聊天记录会话人员事件
 */
midmsg.chatSearchPsnEvent = function(searchKey, ev) {
  var $this = $(ev.currentTarget);
  BaseUtils.doHitMore($this, 1000);
  midmsg.dochat($this.closest(".main-list__item").attr("code"));
  midmsg.setChatReadMsg($this.closest(".main-list__item").attr("code"));
}
midmsg.dochat = function(des3ChatPsnId) {
  window.location.href = "/dynweb/mobile/ajaxshowchatui?des3ChatPsnId=" + encodeURIComponent(des3ChatPsnId);
}
/**
 * 点击会话人员事件
 */
midmsg.chatPsnEvent = function(ev) {
  var $this = $(ev.currentTarget);
  BaseUtils.doHitMore($this, 1000);
  midmsg.setChatReadMsg($this.closest(".main-list__item").attr("code"));
  midmsg.dochat($this.closest(".main-list__item").attr("code"));
}
/**
 * 点击检索人员人员事件
 */
midmsg.chatSeachPsnEvent = function(ev) {
  var $this = $(ev.currentTarget);
  BaseUtils.doHitMore($this, 1000);
  midmsg.dochat($this.closest(".main-list__item").attr("code"));
  midmsg.setChatReadMsg($this.closest(".main-list__item").attr("code"));
}
midmsg.chatPsnEventBack = function() {
  midmsg.setTitle("消息");
  midmsg.setSearchInput("检索人员");
  $("#msg-chatpsn-list").show();
  $("#id_msg_box").hide();
  midmsg.hideTitleBack();
  midmsg.showmeunandsearch();
  midmsg.hideBottomMenu(1);
}
midmsg.chatSeachPsnEventBack = function() {
  midmsg.setTitle("消息");
  midmsg.setSearchInput("检索人员");
  $("#msg-chat_friend_all-list").show();
  $("#msg-chatfriend-list").show();
  $("#msg-chatall-list").show();
  $("#id_msg_box").hide();
  midmsg.hideTitleBack();
  midmsg.showmeunandsearch();
  midmsg.hideBottomMenu(1);
}
midmsg.openchatpsnlist = function() {
  window.location.href = "/psnweb/mobile/msgbox?model=chatMsg";
}
/**
 * 加载站内信消息列表
 */
midmsg.loadMsgChatList = function(des3ChatPsnId, pagePageNo, addWay) {
  var $box = $("div[list-main='mobile_msg_chatbox__list']");
  const $listHeight = $box[0].scrollHeight;
  BaseUtils.stopAJAX(midmsg.ajaxEventArr);
  $("#meun_items").hide();
  var ajaxgetmsglist = $
      .ajax({
        url : "/dynweb/mobile/ajaxgetmidmsglist",
        dataType : "html",
        type : "post",
        data : {
          "page.pageNo" : pagePageNo,
          "msgType" : "7",
          "des3ChatPsnId" : des3ChatPsnId
        },
        success : function(data) {
          BaseUtils
              .ajaxTimeOut(
                  data,
                  function() {
                    $("#mobile_msg_chatbox__list_ico").remove();
                    if (addWay == "html") {
                      $box.html(data);

                      // 设置滚动
                      var chat_pate = $("div[scm_id='mobile_msg_chatbox__list']").eq(0);
                      var scm_totalcount = chat_pate.attr("scm_totalcount");
                      var newItemCount = $box.find(".main-list__item").length;
                      if (parseInt(scm_totalcount) > parseInt(newItemCount)
                          && $box[0].scrollHeight <= $box[0].clientHeight) {
                        midmsg.loadMsgChatScroll();
                      }
                      $box.unbind("scroll", midmsg.loadMsgChatScroll);
                      $box.bind("scroll", midmsg.loadMsgChatScroll);
                      var height = 0;
                      $.each($box.find(".main-list__item"), function(i, o) {
                        height += $(o).height();
                      });
                      setTimeout(function() {
                        midmsg.scrollRunning = false;
                      }, 300);
                      var wHeight = $(window).height();
                      var footerHeight = $("#id_chatbox_footer")[0].offsetHeight;
                      $box.css("height", wHeight - footerHeight);
                      // 新建聊天消息窗窗口，界面调整-2018-07-16 -ajb
                      if ($("div[list-main='mobile_msg_chatbox__list']").attr("chat-ui") === "new") {
                        if ($("div[list-main='mobile_msg_chatbox__list']").attr("changepage") === "false") {
                          $("div[list-main='mobile_msg_chatbox__list']").attr("changepage", "true");
                        } else {
                          $("#id_meun_header").remove();
                          $(".new_mobile-send_message-neck").remove();
                          $("#id_meun_header_new").css("display", "flex");
                          $("div[list-main='mobile_msg_chatbox__list']").css("top", "62px")
                          $("div[list-main='mobile_msg_chatbox__list']").css("height", "722px");
                        }
                      }
                      if (midmsg.loadclienheight == 0) {
                        // 计算高度
                        var clienheight = document.body.clientHeight;
                        var heightlist = document.getElementsByClassName("main-list__list");
                        for (var i = 0; i < heightlist.length; i++) {
                          heightlist[i].style.height = clienheight - 164 + "px";
                          $("div[list-main='mobile_msg_chatbox__list']")[0].style.height = clienheight - 164 + "px";
                        }
                        midmsg.height = clienheight - 164;
                        midmsg.loadclienheight++;
                      } else {
                        $("div[list-main='mobile_msg_chatbox__list']")[0].style.height = midmsg.height + "px";
                      }
                      if ((height + 90) < $box.height()) {
                        $box.scrollTop(0);
                      } else {
                        $box.scrollTop(height + 90);
                      }
                      const scrollheight = $box.scrollTop();
                      BaseUtils
                          .mobileSlideEvent(
                              $box.find(".main-list__item"),
                              {
                                "uEvent" : function($obj) {
                                  var $ScrollBottom = $(document).height() - $(window).height() - $(window).scrollTop();
                                  if ($ScrollBottom <= 0) {
                                    $box
                                        .append("<div id='mobile_msg_chatbox__list_ico' class='state-content__right main-list__item'></div>");
                                    midmsg.loadIco($box.find(".main-list__item:last"), "html");
                                    $box.scrollTop(2000);
                                    midmsg.loadMsgChatList($("div[list-main='mobile_msg_chatbox__list']").attr("code"),
                                        1, "html");
                                  }
                                }
                              });
                    } else if (addWay == "prepend") {
                      $("div[scm_id='mobile_msg_chatbox__list']").remove();
                      $box.prepend(data);
                      $box[0].scrollTop = $box[0].scrollHeight - $listHeight;
                      setTimeout(function() {
                        midmsg.scrollRunning = false;
                      }, 300);
                    }
                    setTimeout(function() {
                      $("div[scm_id='load_state_ico']").remove();
                      $("div[list-main='mobile_msg_center_list']").find(".preloader").remove();
                    }, 1);
                    // 将文本中的数据改为链接
                    midmsg.transUrl();
                  });
        }
      });
  midmsg.ajaxEventArr.push(ajaxgetmsglist);
  return true;
}
/**
 * 加载站内信消息列表滚动事件
 */
midmsg.loadMsgChatScroll = function() {
  var $box = $("div[list-main='mobile_msg_chatbox__list']");
  var des3RelationId = $box.attr("code");
  var newItemCount = $box.find(".main-list__item").length;
  var chat_pate = $("div[scm_id='mobile_msg_chatbox__list']").eq(0);
  var pageNo = chat_pate.attr("scm_pageno");
  var scm_totalcount = chat_pate.attr("scm_totalcount");
  var pagePageNo = 1;
  if (pageNo != null) {
    pagePageNo = (Number(pageNo) + 1);
  }
  if (newItemCount < scm_totalcount) {
    if ($box.scrollTop() <= 50 && !midmsg.scrollRunning) {
      midmsg.scrollRunning = true;
      $box.prepend("<div id='mobile_msg_chatbox__list_ico' class='state-content__right main-list__item'></div>");
      midmsg.loadIco($box.find(".main-list__item:first"), "html");
      midmsg.loadMsgChatList(des3RelationId, pagePageNo, "prepend");
    }
  } else {
    $box.unbind("scroll", midmsg.loadMsgChatScroll);
  }
}
midmsg.delchatpsnmove = function(ev) {
  var $del = $(ev.currentTarget).closest(".main-list__item");
  $del.moveItem();
  midmsg.delMsgChatRelation($del.attr("des3MsgChatRelationId"));
  midmsg.setChatReadMsg($del.attr("code"));
}
/**
 * 加载站内信会话列表
 */
midmsg.loadMsgChatPsnList = function() {
  midmsg.msgchatpsnlistcount = 0;
  midmsg.msgchatpsnlist = window.Mainlist({
    name : "mobile_msg_chatpsn_list",
    listurl : "/dynweb/mobile/ajaxgetchatpsnlist",
    listdata : {},
    method : "scroll",
    listcallback : function(xhr) {
      var $items = $("div[list-main='mobile_msg_chatpsn_list']").find(".main-list__item");
      BaseUtils.mobileSlideEvent($items, {
        "lEvent" : function($obj) {
          if ($obj.movex < -90) {
            $obj.$this.css("left", "-120px");
          } else {
            $obj.$this.css("left", "0px");
          }
        },
        "rEvent" : function($obj) {
          if ($obj.movex > 10) {
            $obj.$this.css("left", "0px");
          }
        },
        "mlEvent" : function($obj) {
          if ($obj.movex >= -120 && parseFloat($obj.$this.css('left')) != -120) {
            $items.css("left", "0px");
            $obj.$this.css("left", $obj.movex);
          }
        },
        "mrEvent" : function($obj) {
          if ($obj.movex < 90 && parseFloat($obj.$this.css('left')) < 0) {
            $obj.$this.css("left", -120 + $obj.movex);
          }
        }
      });
      if (midmsg.msgchatpsnlistcount == 0) {
        scrollTo(0, 0);
      }
      midmsg.msgchatpsnlistcount++;
    }
  });
}
// 标记已读
midmsg.setChatReadMsg = function(des3SenderId) {
  $.ajax({
    url : "/dynweb/showmsg/ajaxsetread",
    dataType : "json",
    type : "post",
    data : {
      des3SenderId : des3SenderId,
      readMsgType : 2
    },
    success : function(data) {
      midmsg.loadMsgStatus();
    }
  });
}
/*
 * 站内信 会话删除
 */
midmsg.delMsgChatRelation = function(des3MsgChatRelationId) {
  if (des3MsgChatRelationId == null || des3MsgChatRelationId == "") {
    return;
  }
  $.ajax({
    url : '/dynweb/showmsg/ajaxdelmsgchatrelation',
    type : 'post',
    dataType : 'json',
    data : {
      des3MsgChatRelationId : des3MsgChatRelationId
    },
    success : function(data) {
    }
  });
}
/**
 * 发送文本消息
 */
midmsg.sendtextmsg = function(ev) {
  BaseUtils.doHitMore(ev.currentTarget);
  var param = {};
  param.content = $.trim($("#mid_msg_content").val());
  param.smateInsideLetterType = "text";
  param.receiverIds = $("div[list-main='mobile_msg_chatbox__list']").attr("code");
  if ($.trim(param.receiverIds) === "") {
    scmpublictoast("请添加联系人", 1000, 3);
    return;
  }
  midmsg.comsendmsg(param, function() {
    midmsg.loadMsgChatList(param.receiverIds, 1, "html");
  });
  $("#mid_msg_content").val("");
}
/**
 * 发送成果消息
 */
midmsg.sendpubmsg = function(ev) {
  BaseUtils.doHitMore(ev.currentTarget);
  if (midmsg.oldBack != "") {
    midmsg.showTitleBack(midmsg.oldBack);
  }
  BaseUtils.doHitMore(ev.currentTarget);
  BaseUtils.stopNextEvent(ev);
  var param = {};
  param.smateInsideLetterType = "pub";
  param.receiverIds = $("div[list-main='mobile_msg_chatbox__list']").attr("code");
  if ($.trim(param.receiverIds) === "") {
    scmpublictoast("请添加联系人", 1000, 3);
    return;
  }
  param.des3PubId = $(ev.currentTarget).attr("des3PubId");
  param.belongPerson = true;
  SmateCommon.checkPubAnyUser(param.des3PubId, "insideMsg", param);
}
/**
 * 发送文件消息
 */
midmsg.sendfilemsg = function(ev) {
  BaseUtils.doHitMore(ev.currentTarget);
  if (midmsg.oldBack != "") {
    midmsg.showTitleBack(midmsg.oldBack);
  }
  BaseUtils.doHitMore(ev.currentTarget);
  var param = {};
  param.smateInsideLetterType = "file";
  param.receiverIds = $("div[list-main='mobile_msg_chatbox__list']").attr("code");
  if ($.trim(param.receiverIds) === "") {
    scmpublictoast("请添加联系人", 1000, 3);
    return;
  }
  param.des3FileId = $(ev.currentTarget).attr("des3FileId");
  param.belongPerson = true;
  midmsg.comsendmsg(param, function() {
    midmsg.loadMsgChatList(param.receiverIds, 1, "html");
    // midmsg.oldBack="midmsg.showchatpsnUI()";
    midmsg.showfilesUIBack();
  });
}
/**
 * 发送消息
 */
midmsg.comsendmsg = function(param, myfunction) {
  var status = true;
  if ($.trim(param.receiverIds) === "") {
    scmpublictoast("请添加联系人", 1000, 3);
    status = false;
  }
  if (param.smateInsideLetterType == "text" && param.content.length > 500) {
    scmpublictoast("文本消息最多输入500个字符", 1000, 3);
    status = false;
  }
  if (param.smateInsideLetterType == "text" && param.content.length == 0) {
    scmpublictoast("发送的文本不能为空", 1000, 3);
    status = false;
  }
  if (status) {
    $.ajax({
      url : "/dynweb/showmsg/ajaxsendmsg",
      dataType : "json",
      type : "post",
      data : {
        msgType : "7",
        receiverIds : param.receiverIds,
        content : param.content,
        smateInsideLetterType : param.smateInsideLetterType,
        des3FileId : param.des3FileId,
        des3PubId : param.des3PubId,
        belongPerson : param.belongPerson
      },
      success : function(result) {
        BaseUtils.ajaxTimeOut(result, function() {
          if (result.status == "success") {
            if (typeof myfunction == "function") {
              myfunction(result);
            }
          } else if (result.status == "noPermit") {
            scmpublictoast(result.msg, 2000, 3);
          }
        });
      }
    });
  }
}
/**
 * 显示成果列表
 */
midmsg.showpubsUI = function() {

  var receiverIds = $("div[list-main='mobile_msg_chatbox__list']").attr("code");
  if ($.trim(receiverIds) === "") {
    scmpublictoast("请添加联系人", 1000, 3);
    return;
  }

  var $chatpubbox = $("div[list-main='mobile_msg_chatpub_list']");
  $(".dev_meun_item").hide();
  $chatpubbox.closest(".dev_meun_item").show();
  $("#id_meun_main").hide();
  midmsg.chatpsnname = $("#id_meun_header").find(".message-page__psnheader-title").text();
  midmsg.setTitle("选择要发送的成果");
  midmsg.showTitleBack("midmsg.showpubUIBack()");
  midmsg.showpublist = window.Mainlist({
    name : "mobile_msg_chatpub_list",
    listurl : "/pub/querylist/ajaxsimple",
    listdata : {
      "des3PsnId" : $("#id_meun_header").attr("des3PsnId")
    },
    method : "scroll",
    listcallback : function(xhr) {
    }
  });
}
/**
 * 显示文件列表
 */
midmsg.showfilesUI = function() {

  var receiverIds = $("div[list-main='mobile_msg_chatbox__list']").attr("code");
  if ($.trim(receiverIds) === "") {
    scmpublictoast("请添加联系人", 1000, 3);
    return;
  }
  var $chatfilebox = $("div[list-main='mobile_msg_chatfile_list']");
  $(".dev_meun_item").hide();
  $chatfilebox.closest(".dev_meun_item").show();
  $("#id_meun_main").hide();
  midmsg.chatpsnname = $("#id_meun_header").find(".message-page__psnheader-title").text();
  midmsg.setTitle("选择要发送的文件");
  midmsg.showTitleBack("midmsg.showfilesUIBack()");
  midmsg.showpublist = window.Mainlist({
    name : "mobile_msg_chatfile_list",
    listurl : "/psnweb/mobile/ajaxmsgfilelist",
    listdata : {},
    method : "scroll",
    listcallback : function(xhr) {

    }
  });
}
midmsg.showpubUIBack = function() {
  $(".dev_meun_item").hide();
  $("#id_meun_main").hide();
  midmsg.setTitle($("#id_meun_header").find(".message-page__psnheader-title").attr("name"));
  $("#id_msg_box").show();
  $("#pub-list").find(".searchbox__main").find("input").val("");
  midmsg.setTitle(midmsg.chatpsnname);
  midmsg.showTitleBack("midmsg.openchatpsnlist()");
}
midmsg.showfilesUIBack = function() {
  $(".dev_meun_item").hide();
  $("#id_meun_main").hide();
  midmsg.setTitle($("#id_meun_header").find(".message-page__psnheader-title").attr("name"));
  $("#id_msg_box").show();
  $("#file-list").find(".searchbox__main").find("input").val("");
  midmsg.setTitle(midmsg.chatpsnname);
  midmsg.showTitleBack("midmsg.openchatpsnlist()");
}

/**
 * @author匹配消息列表中的网址
 */
midmsg.msgBaseMatchUrl = function(str) {
  var Match = function(str, index, lastIndex) {
    this.str = str; // 匹配到的url
    this.index = index; // 匹配到的字符串在原字符串中的起始位置
    this.lastIndex = lastIndex; // 匹配到的字符串在原字符串中的终止位置
  };
  /**
   * 匹配http://|https://
   */
  var regexp1 = new RegExp('(http:\/\/|https:\/\/)', 'g');
  var matchArray = new Array();
  var matchStr;
  while ((matchStr = regexp1.exec(str)) != null) {
    matchArray.push(new Match(matchStr[0], matchStr.index, regexp1.lastIndex));
    /*
     * console.log(match) console.log(regexp1.lastIndex - match.index)
     */
  }
  /**
   * 匹配网址分隔符，空白字符或非单字节字符及特殊标点符号,;`·"|'做分隔
   */
  var regexp2 = /<br>|\s|[^\u0000-\u00FF]|[,;`·"\|']/g;
  for (var i = 0; i < matchArray.length; i++) {
    // 考虑内容结束但没有分隔符的情况
    var endIndex = matchArray[i + 1] ? matchArray[i + 1].index : str.length;
    var substr = str.substring(matchArray[i].lastIndex, endIndex);
    if ((matchStr = regexp2.exec(substr)) != null) { // 匹配到分隔符
      if (regexp2.lastIndex > 1) { // 提取网址
        if (matchStr[0] == '<br>') {
          matchArray[i].lastIndex += regexp2.lastIndex - 4;
        } else {
          matchArray[i].lastIndex += regexp2.lastIndex - 1;
        }
        matchArray[i].str = str.substring(matchArray[i].index, matchArray[i].lastIndex);
      } else { // 考虑"http:// "这样的情况
        matchArray.splice(i, 1);
      }
    } else if (endIndex == str.length) { // 考虑内容末尾无空白符的情况
      if (matchArray[i].lastIndex < endIndex) { // 提取网址
        matchArray[i].lastIndex = endIndex;
        matchArray[i].str = str.substring(matchArray[i].index, endIndex);
      } else { // 考虑内容末尾无空白符，但也没有实际网址的情况，如："http://"
        matchArray.splice(i, 1);
      }
    }// 考虑内容并未到结尾，无空白符的情况，如:http://baidu.comhttp://sina.com，这种情况识别为一个网址
    else if (matchArray[i + 1] && (endIndex == matchArray[i + 1].index)) {
      matchArray.splice(i + 1, 1);
      i = i - 1;
    } else { // 其他情况
      matchArray.splice(i, 1);
    }
    // console.log(matchStr);
    regexp2.lastIndex = 0;
  }
  return matchArray;
}
// 发表的动态中含有网址的话展示时显示网页链接
midmsg.transUrl = function() {
  var msgList = $(".main-list__item").find('.dev_detail');
  if (typeof msgList != "undefined" && typeof msgList != "") {
    msgList.each(function() {
      if ($(this).children().length == 0) {
        var $div = $(this).text();
        var c = $div.trim();
        var matchArray = midmsg.msgBaseMatchUrl(c);
        var newstr = "";
        for (var i = 0; i <= matchArray.length; i++) {
          var beginIndex = i == 0 ? 0 : matchArray[i - 1].lastIndex;
          var endIndex = i == matchArray.length ? c.length : matchArray[i].index;
          var stri = c.substring(beginIndex, endIndex);
          var urli = "";
          if (i < matchArray.length) {
            urli = " <a href=\"" + matchArray[i].str
                + "\" style=\"color: #005eac !important;word-break:break-all;\" target=\"_blank\">" + matchArray[i].str
                + "</a> ";
          }
          newstr += stri + urli;
        }
        $(this).html(newstr);
      }
    });
  }
}