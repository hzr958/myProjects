/**
 * 移动端全文请求js
 */
var midmsg = midmsg ? midmsg : {};
/**
 * 加载全文请求列表
 */
midmsg.loadMsgFullTextList = function() {
  var $this = $(this);
  $("#id_meun_main").find(".main-list__searchbox").attr("list-search", "mobile_msg_fulltext_list");
  midmsg.msgfulltextlist = window.Mainlist({
    name : "mobile_msg_fulltext_list",
    listurl : "/dynweb/mobile/ajaxgetmidmsglist",
    listdata : {
      "msgType" : "11"
    },
    method : "scroll",
    listcallback : function(xhr) {
      slideDom("msg-req-item", 45, true, midmsg.fullTextReqMsgLeftMove, midmsg.fullTextReqMsgRightMove,
          midmsg.fullTextReqClickItemCallBack);

      var tiplist = document.getElementsByClassName("full-request__footer-agree_topc");
      for (var i = 0; i < tiplist.length; i++) {
        tiplist[i].onclick = function() {
          smatemobile();

        }
      }
      setTimeout(function() {
        $("div[scm_id='load_state_ico']").remove();
        $("div[list-main='mobile_msg_center_list']").find(".preloader").remove();
      }, 1);
      // 显示没有更多数据,有记录条数才显示
      var recordNum = parseInt($("div[list-main='mobile_msg_fulltext_list'] div[class='js_listinfo']").attr(
          "smate_count"));
      if (recordNum > 0) {
        SmateCommon.noMoreRecordTips();
      }

    }
  });
}

midmsg.fullTextReqMsgLeftMove = function(obj, event) {
  var msgId = $(obj).attr("msg-id");
  var pubId = $(obj).attr("pub-id");
  midmsg.optFullTextRequest(msgId, 2, pubId);
}
midmsg.fullTextReqMsgclick = function(ev) {
  var $this = $(ev.currentTarget);
  BaseUtils.doHitMore($this);
  var msgId = $this.closest(".main-list__item").attr("msg-id");
  var pubId = $this.closest(".main-list__item").attr("pub-id");
  midmsg.optFullTextRequest(msgId, 1, pubId);
}
midmsg.fullTextReqMsgIgnore = function(ev) {
  var $this = $(ev.currentTarget);
  BaseUtils.doHitMore($this);
  var msgId = $this.closest(".main-list__item").attr("msg-id");
  var pubId = $this.closest(".main-list__item").attr("pub-id");
  midmsg.optFullTextRequest(msgId, 2, pubId);
}

midmsg.fullTextReqMsgRightMove = function(obj, event) {
  $(obj).css('transform', 'translateX(0px)');
  $(obj).css('transition', '0.3s');
  setTimeout(function() {
    $(obj).css('transition', '0s')
  }, 10);// 回复原位
}
midmsg.fullTextReqClickItemCallBack = function(obj, event) {
  $(obj).css('transform', 'translateX(0px)');
  $(obj).css('transition', '0.3s');
  setTimeout(function() {
    $(obj).css('transition', '0s')
  }, 10);// 回复原位
}

/**
 * 全文请求操作 param: msgRelationId: 消息id dealStatus: 要做的操作，1--同意，2--忽略/拒绝，3--上传 pubId: 操作的成果id
 */
midmsg.optFullTextRequest = function(des3MsgId, dealStatus, des3PubId) {
  if (dealStatus != 1 && dealStatus != 2) {
    // 上传全文
    return;
  }
  // 处理全文请求
  $.ajax({
    url : '/pub/fulltext/ajaxagree',
    type : "post",
    dataType : "json",
    data : {
      'des3MsgId' : des3MsgId,
      'des3PubId' : des3PubId,
      'dealStatus' : dealStatus
    },
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.status == "success") {
          scmpublictoast(data.msg, 1000);
          $("div[msg-id='" + des3MsgId + "']").moveItemLeft();
          midmsg.loadMsgStatus(3);
          if ($("div[msg-id]").length < 2) {
            $(".message-page__fuctool-item_detail")[2].click();
          }
        } else {
          scmpublictoast(data.msg, 2000);
        }
      });
    },
    error : function(data) {
    }
  });
}

midmsg.openPubDetail = function(des3PubId) {
  location.href = "/pub/details/snsnonext?des3PubId=" + encodeURIComponent(des3PubId);
}

midmsg.downloadFulltext = function(des3PubId) {
  Smate.confirm("下载提示", "要下载全文附件吗？", function() {
    $.post('/fileweb/download/ajaxgeturl', {
      "des3Id" : des3PubId,
      "type" : 2,
      "shortUrl" : true
    }, function(result) {
      BaseUtils.ajaxTimeOut(result, function() {
        if (result.status == "success") {
          window.location.href = result.data.url;
        } else {
          scmpublictoast(result.msg, 2000);
        }
      });
    }, 'json');
  }, ["下载", "取消"]);
}
