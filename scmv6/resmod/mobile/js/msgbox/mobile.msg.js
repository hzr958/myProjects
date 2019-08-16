/**
 * 移动端-消息菜单页面的js事件
 */
var Msg = Msg ? Msg : {};
// 加载更多全文
Msg.ajaxmorefulltext = function() {
  var curPubItems = new Array();
  var checkCurPubItems = $('.rcmd_pub_fulltext_tr');
  checkCurPubItems.each(function() {
    curPubItems.push($(this).val());
  });
  var post_data = {
    curIds : curPubItems.join(','),
    formMobile : "true"
  };
  $.ajax({
    url : ctxpath + '/pubftrcmd/ajaxPubftList',
    type : 'post',
    dataType : 'html',
    data : post_data,
    success : function(data) {
      if (data.lastIndexOf('ajaxSessionTimeOut') != -1) {
        jConfirm(timeout, reminder, function(sure) {
          if (sure) {
            window.location.reload();
          }
        });
      } else {
        $("#moreFulltext").append(data);
      }
    }
  });
}
// 是我的全文成果
Msg.doConfirmPubft = function(pubId, next,obj) {
  // 防止重复点击
//  if ($(".dev_yes_botton")) {
//    BaseUtils.doHitMore($(".dev_yes_botton"), 1000);
//  }
  //防止重复点击
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  $.ajax({
        url : '/pub/opt/ajaxConfirmPubft',
        type : 'post',
        dataType : 'json',
        data : {
          'ids' : pubId,
          'currentDes3PsnId' : $("#currentDes3PsnId").val()
        },
        success : function(data) {
          if(data.status == "not_exist"){
            scmpublictoast("附件已删除", 1000);
            return;
          }
          if (next == "next") {
            scmpublictoast("认领成功", 1000);
            Msg.oneDetails(0, "psnpub");
          } else {
            $("#fulltext_confirmation_container_" + pubId).remove();
          }
//          if (list_item.length == 0) {
//            var noRecordTip = "<div id='con_one_2' class='noRecord' style='margin-top:-50px'><div class='content'><div class='no_effort'> <h2 class='tc'>未找到全文认领记录</h2></div></div></div>";
//            $(".body_content").html(noRecordTip);
//          }
          //重新加载未读数据条数
          Msg.reloadPubFullTextRcmdData();
        },
        error : function() {
          scmpublictoast("网络异常", 1000);
        }
      });
}
/**
 * 重新获取成果认领与全文认领的数据
 */
Msg.reloadPubFullTextRcmdData = function () {
  //由于在全文认领详情界面,不需要重新获取,所以添加异常处理
  try  {
    $("#meun_items").find("div.message-page__fuctool-item")[1].click();
  } catch (err) {
    return;
  }
}
// 不是我的全文成果
Msg.doRejectPubft = function(pubId, next,obj) {
  // 防止重复点击
//  if ($(".dev_not_botton")) {
//    BaseUtils.doHitMore($(".dev_not_botton"), 1000);
//  }
//防止重复点击
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  $.ajax({
        url : '/pub/opt/ajaxRejectPubft',
        type : 'post',
        dataType : 'json',
        data : {
          'ids' : pubId,
          'currentDes3PsnId' : $("#currentDes3PsnId").val()
        },
        success : function(data) {
          if (next == "next") {
            scmpublictoast("操作成功", 1000);
            Msg.oneDetails(0, "psnpub");
          } else {
            $("#fulltext_confirmation_container_" + pubId).remove();
          }
//          if (list_item.length == 0) {
//            var noRecordTip = "<div id='con_one_2' class='noRecord' style='margin-top:-50px'><div class='content'><div class='no_effort'> <h2 class='tc'>未找到全文认领记录</h2></div></div></div>";
//            $(".body_content").html(noRecordTip);
//          }
          //重新加载未读数据条数
          Msg.reloadPubFullTextRcmdData();
        },
        error : function() {
          scmpublictoast("网络异常", 1000);
        }
      });
}

// 移动端-消息菜单-消息提示(待确认成果数,待确认全文数)
Msg.showMsgTip = function() {
  var url = "/pub/wechat/ajaxmsgtips";
  var dataJson = {
    'des3PsnId' : $("#currentDes3PsnId").val()
  };
  ajaxparamsutil.doAjax(url, dataJson, Msg.showMsgTipResult, 'json', 'post');
};

// 移动端-消息菜单-消息提示回调
Msg.showMsgTipResult = function(data) {
  // 待确认成果数
  if (Number(data.pubConfirmCount) > 0) {
    $("#confirmPubNum").html(data.pubConfirmCount);
    $("#confirm_pub_div").show();
    $("#no_msg_style").val(Number($("#no_msg_style").val()) + 1);
    $("#no_fullfile_style").val(Number($("#no_fullfile_style").val()) + 1);
  } else {
    $("#no_fullfile_style").val(Number($("#no_fullfile_style").val()) + 0);
    $("#no_msg_style").val(Number($("#no_msg_style").val()) + 0);
  }
  // 待确认全文数
  if (Number(data.pubFulltextCount) > 0) {
    $("#confirmfullTextNum").html(data.pubFulltextCount);
    $("#confirm_fullText_div").show();
    $("#no_msg_style").val(Number($("#no_msg_style").val()) + 1);
    $("#no_fullfile_style").val(Number($("#no_fullfile_style").val()) + 1);
  } else {
    $("#no_msg_style").val(Number($("#no_msg_style").val()) + 0);
    $("#no_fullfile_style").val(Number($("#no_fullfile_style").val()) + 0);
  }
  if ($("#no_fullfile_style").val() == 0) {
    $(".pub_fullfile_num").hide();
  }
};
// 移动端-消息菜单-分享消息提示
Msg.showShareMsg = function() {
  if ($.trim($("#msg_next_page").val()) != "false") {
    $(".preloader").show();
    var url = "/dynweb/mobile/ajaxgetmsglist";
    /* var pageNo = isNaN($("#pageNo").val()) ? 1 : (parseInt($("#pageNo").val()) + 1); */
    var pageNo = parseInt($("#pageNo").val()) + 1;
    dataJson = {
      "status" : 0,// 消息状态 0:查未读、1:查已读、2:查全部
      "msgType" : "7",// 站内信
      "mobilePageNo" : pageNo,
      'des3PsnId' : $("#currentDes3PsnId").val()
    };
    $.ajax({
      url : url,
      dataType : "html",
      type : "post",
      data : dataJson,
      success : function(data) {
        $(".list_container").append(data);
        $("#pageNo").val(pageNo);
        $(".preloader").hide();
      },
      error : function() {
        $(".preloader").hide();
      }
    });
    // ajaxparamsutil.doAjax(url,dataJson,Msg.showShareMsgResult,'html','post');
  }
};
Msg.showReadyShareMsg = function() {
  var url = "/dynweb/mobile/ajaxgetmsglist";
  dataJson = {
    "status" : 0,// 消息状态 0:查未读、1:查已读、2:查全部
    // "mark" : "mobile",
    "msgType" : "7",// 站内信
    "mobilePageNo" : 1,
    'des3PsnId' : $("#currentDes3PsnId").val()
  };
  $.ajax({
    url : url,
    dataType : "html",
    type : "post",
    data : dataJson,
    success : function(data) {
      $(".list_container").append(data);
      $("#pageNo").val(1);
      $(".preloader").hide();
    },
    error : function() {
      $(".preloader").hide();
    }
  });
  // ajaxparamsutil.doAjax(url,dataJson,Msg.showShareMsgResult,'html','post');
};

// 移动端-消息菜单-待确认成果列表展示
Msg.confimPubList = function() {
  window.location.href = "/pub/confirmpublist";
};
// 移动端-消息菜单-待确认的全文
Msg.confimpubftrcmd = function(toBack,whoFirst) {
  //window.location.replace("/pub/wechat/pubfulltextlist?toBack=" + toBack);
  window.location.replace("/psnweb/mobile/msgbox?model=centerMsg&whoFirst="+whoFirst+"&toBack=" + toBack);
};

// 移动端成果认领页返回到消息中心
Msg.arrowBack = function(toBack) {
  if (toBack == "psnpub") {
    window.location.replace("/pub/querylist/psn");
  } else {
    switch (toBack) {
      case "centerMsg" :
        window.location.href = "/psnweb/mobile/msgbox?model=centerMsg";
      break;
      case "reqFullTextMsg" :
        window.location.href = "/psnweb/mobile/msgbox?model=reqFullTextMsg";
      break;
      default :
        window.location.href = "/psnweb/mobile/msgbox";
    }
  }

};

// 移动端-消息菜单-是我的成果(yes)
Msg.importMyPubPdwh = function(self, pdwhPubId) {
  var params = [];
  params.push({
    "pubId" : pdwhPubId
  });
  var postData = {
    "jsonParams" : JSON.stringify(params),
    "publicationArticleType" : "1"
  };
  $.ajax({
    url : "/pubweb/publication/ajaximportpdwh",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      if (data.result == "success") {
        $("#pub_" + pdwhPubId).remove();
        var allText = $(self).closest(".list_container").find(".pub-idx__main_add-tip").html();
        if (allText == "check_box") {
          Msg.sendInviteFriend(pdwhPubId);
        }
        scmpublictoast("操作成功", 1000);
        // Msg.pubLoad();
        // Msg.findPubs();
      }
    },
    error : function(data) {
      scmpublictoast("网络异常", 1000);
    }
  });
};
//检查是否还有成果认领,没有就隐藏邀请成为合作者
Msg.checkIsHasPubRcmd = function () {
  var isHasPubRcmd = $("div[id^='pub_']");
  if (isHasPubRcmd.length == 0) {
      $(".pub-idx__main_add").hide();
  }
}

// 是我的成果
Msg.AffirmConfirmPub = function(obj, pdwhPubId) {
  // 防止重复点击
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  $.ajax({
    url : "/pub/opt/ajaxAffirmPubComfirm",
    type : "post",
    dataType : "json",
    data : {
      "pubId" : pdwhPubId
    },
    success : function(data) {
      if (data.result == 'error') {
      } else {
        $("#pub_" + pdwhPubId).remove();
        var allText = $("#isCheck").html();
        if (allText == "check_box") {
          Msg.sendInviteFriend(pdwhPubId);
        }
//        var ele = $(".pub_whole");
//        if (ele.length == 0) {
//          $("#listdiv").html("<div class='response_no-result'>没有成果待认领, 可完善更详细的人员信息获取更多的成果推荐</div>");
//        }
       //重新加载未读数据条数
        Msg.reloadPubFullTextRcmdData();
        Msg.checkIsHasPubRcmd();
        //scmpublictoast("操作成功", 1000);
      }
    }
  });
};
/*
 * Msg.yesConfirmPub = function(pdwhPubId) { $.ajax({ url: "/pubweb/psn/ajaxpubconfirm", type :
 * "post", dataType : "json", data :{"pubId":pdwhPubId}, success:function(data){} }); };
 */

// 邀请合作者为联系人
Msg.sendInviteFriend = function(pdwhPubId) {
  $.ajax({
    url : "/psnweb/friend/ajaxinvitetofriend",
    type : "post",
    dataType : "json",
    data : {
      "pdwhPubId" : pdwhPubId
    },
    success : function(data) {
    }
  });
};

// 移动端-消息菜单-不是我的成果(no)
Msg.IgnoreConfirmPub = function(obj, pdwhPubId) {
  //防止重复点击
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  $.ajax({
    url : "/pub/opt/ajaxIgnorePubComfirm",
    type : "post",
    dataType : "json",
    data : {
      "pubId" : pdwhPubId
    },
    success : function(data) {
      if (data.result == 'error') {
      } else {
        $("#pub_" + pdwhPubId).remove();
        // Msg.pubLoad();
        // Msg.findPubs();
        //不需要提示,在全文认领和成果认领都没有的情况下才显示
//        if (ele.length == 0) {
//          $("#listdiv").html("<div class='response_no-result'>没有成果待认领, 可完善更详细的人员信息获取更多的成果推荐</div>");
//        }
        //重新加载未读数据条数
        Msg.reloadPubFullTextRcmdData();
        Msg.checkIsHasPubRcmd();
      }
    },
    error : function(data) {
      scmpublictoast("网络异常", 1000);
    }
  });
};
/*
 * Msg.noConfirmPub = function(self,pdwhPubId) { $.ajax({ url: "/pubweb/psn/ajaxNoConfirmPub", type :
 * "post", dataType : "json", data :{"pubId":pdwhPubId}, success:function(data){ if (data.result ==
 * 'error') { }else{ $("#pub_"+pdwhPubId).remove(); // Msg.pubLoad(); // Msg.findPubs(); } },
 * error:function(data){ scmpublictoast("网络异常",1000); } }); };
 */

// 加载待确认成果
/*
 * Msg.pubLoad = function() { var nextId=null; if(nextId==null||nextId==""){ var nextIds
 * =document.getElementsByName("dataCount"); if(nextIds.length>0){ nextId=nextIds.length; }else{
 * return; } } var dataParam = { "flag":1, "size":1, "count":nextId, "fromPage":"mobileConfirmPub" };
 * $.ajax({ url :'/pubweb/wechat/confirmpublist', type :"post", dataType :"html", data : dataParam,
 * success :function(data){ $("#listdiv").append(data); }, error:function(data){ } }); };
 */

// 查找待确认成果
/*
 * Msg.findPubs = function(){ var pubCounts = $("div[id^='pub_']").size(); var no_rcmd_count =
 * $("#no_rcmd_count").size(); if(pubCounts==0 && no_rcmd_count==0){ $("#listdiv").html("");
 * $("#listdiv").append("<div id='no_rcmd_count'>未找到相关记录</div>"); } };
 */

// 打开成果详情
Msg.opendetail = function(obj) {
  location.href = "/pub/details/snsnonext?des3PubId=" + encodeURIComponent(obj);
}
// 打开全文认领详情
Msg.openPubfulltextDetail = function(detailPageNo, toBack,whoFirst) {
  // location.href="/pubweb/wechat/findpubxmlnonext?des3PubId="+obj+"&fulltextFileId="+fulltextFileId+"&hasFulltext="+hasFulltext;
  location.replace("/pub/details/pubfulltext?detailPageNo=" + detailPageNo + "&toBack=" + toBack + "&whoFirst=" + whoFirst);
}

Msg.openFundDetail = function(obj) {
  location.href = "/prjweb/wechat/findfundsxml?des3FundId=" + encodeURIComponent(obj);
}

// 消息-联系人列表添加联系人
Msg.requestHandler = function(des3TempPsnId, psnId, event) {
  var option = {
    fun : function() {
      $.ajax({
        url : "/psnweb/friendreq/ajaxaccept",
        type : "post",
        dataType : "json",
        data : {
          "des3ReqPsnIds" : des3TempPsnId
        },
        success : function(data) {
          if (data.result == "success") {
            scmpublictoast("操作成功", 1000);
          } else if (data.result == "msg") {
            scmpublictoast("添加联系人失败，对方已取消联系人邀请。", 1000);
          }
          location.reload();
        },
        error : function(data) {
          scmpublictoast("网络异常", 1000);
        }
      });
    }
  };
  var friendId = "div#list_item_container_" + psnId;
  $(friendId).moveItem(option);
  event.stopPropagation();
};
// 消息-联系人列表拒绝联系人
Msg.refusetHandler = function(des3TempPsnId, psnId) {
  var option = {
    fun : function() {
      $.ajax({
        url : "/psnweb/friendreq/ajaxneglet",
        type : "post",
        dataType : "json",
        data : {
          "des3TempPsnId" : des3TempPsnId
        },
        success : function(data) {
          if (data.result == "success") {
            scmpublictoast("操作成功", 1000);
            location.reload();
          } else {
            scmpublictoast("网络异常", 1000);
          }
        },
        error : function(data) {
          scmpublictoast("网络异常", 1000);
        }
      });
    }
  };
  $("div#list_item_container_" + psnId).moveItem(option);
  event.stopPropagation();
}

// 移动端-消息菜单-上拉加载更多分享消息
Msg.loadMoreShareMsg = function() {
  common.scrolldirect("toolbar", "hide_bottom", "show_bottom", Msg.showShareMsg);
};

// 把当前人消息全部置为已读
// Msg.setReadMsg = function() {
// $.ajax({
// url : "/dynweb/mobile/ajaxsetread",
// type : "post",
// dataType : "json",
// data : {'des3PsnId': $("#currentDes3PsnId").val()},
// success : function(data) {
// if (data.result == "success") {
// alert("置为已读成功");
// }
// }
// });
// };

// 全文下载
Msg.downloadFTFile = function(url) {
  if (!!url && url != "") {
    Smate.confirm("下载提示", "要下载全文附件吗？", function() {
      window.location.href = url;
    }, ["下载", "取消"]);
  }
};

//
Msg.enlarge = function(img, srcImg, detailPageNo, toBack) {
  img = $("#fulltextImage").attr("src");
  window.location.href = "/pub/mobile/pubfulltextimg?fullTextImagePath=" + encodeURIComponent(img)
      + "&srcFullTextImagePath=" + encodeURIComponent(srcImg) + "&detailPageNo=" + detailPageNo + "&toBack=" + toBack;
};

//
Msg.oneDetails = function(detailPageNo, toBack) {
  $('.dev_details_sub').html("");
  $.ajax({
    url : "/pub/details/ajaxpubfulltext",
    type : "post",
    dataType : "html",
    data : {
      "detailPageNo" : detailPageNo,
      "toBack" : toBack,
      "findSubPage" : 1
    },
    success : function(data) {
      $('.dev_details_sub').html(data);
      if ($("#con_one_2").length > 0) {
        $(".material-icons.file_download").hide();
      } else {
        $("#fullTextFileId").attr("onclick", "downloadFulltext('" + $("#downloadUrl").val() + "')");
      }
    }
  });
};
