var Pubdetails = Pubdetails ? Pubdetails : {};

Pubdetails.initBuidName = function() {
  $(".detail-pub__cognize-item[status='1']").mouseenter(function() {
    var $this = $(this);
    $(".detail-pub__cognize-toast").slideUp();
    $this.find(".detail-pub__cognize-toast").stop(true).slideDown();
  }).mouseleave(function() {
    var $this = $(this);
    $this.find(".detail-pub__cognize-toast").stop(true).slideUp();
    $("#showPurchase").slideUp();
  });

}
Pubdetails.sendMsg = function(des3PsnId, obj) {
  window.open("/dynweb/showmsg/msgmain?model=chatMsg&des3ChatPsnId=" + des3PsnId);
}
// 单个添加好友
Pubdetails.addOneFriend = function(reqPsnId, obj) {
  // 防止重复点击
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  $.ajax({
    url : '/psnweb/friend/ajaxaddfriend',
    type : 'post',
    data : {
      'des3Id' : reqPsnId
    },
    dataType : 'json',
    timeout : 10000,
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data.result == "true") {
          scmpublictoast(pubdetails.addFriendSuccess, 1000);
        } else {
          scmpublictoast(data.msg, 1000);
        }
      });
    }
  });
};

// 发表评论
Pubdetails.comment = function(des3PubId) {
  var replyContent = $.trim($("#pubComment").find("textarea[name$='comments']").val()).replace(/\n/g, '<br>');
  $.ajax({
    url : '/pub/opt/ajaxcomment',
    type : 'post',
    data : {
      "des3PubId" : des3PubId,
      "content" : replyContent
    },
    dataType : "json",
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        /* Pubdetails.getComment(des3PubId); */
        Pubdetails.getCommentList(des3PubId);
        Pubdetails.getCommentNumber(des3PubId);
        $("#pubComment").find("textarea[name$='comments']").val("");
        $("#pubCommnetBtn").attr("disabled", "disabled");

      });
    }

  });
}
// 初始化评论列表
Pubdetails.getCommentList = function(des3PubId) {
  window.Mainlist({
    name : "pubCommentList",
    listurl : "/pub/opt/ajaxcommentlist",
    listdata : {
      'des3PubId' : des3PubId
    },
    listcallback : function() {
      addFormElementsEvents(document.getElementById("pubComment"));
    },
    method : "scroll",
  })

}
// 初始化基准库的评论列表
Pubdetails.getPdwhCommentList = function(des3PubId) {
  window.Mainlist({
    name : "pubPdwhCommentList",
    listurl : "/pub/opt/ajaxcommentlistpdwh",
    listdata : {
      'des3PubId' : des3PubId
    },
    listcallback : function() {
      addFormElementsEvents(document.getElementById("pubComment"));
    },
    method : "scroll",
  })

}
// 基准库发表评论
Pubdetails.pdwhComment = function(des3PdwhPubId) {
  var replyContent = $.trim($("#pubComment").find("textarea[name$='comments']").val()).replace(/\n/g, '<br>');
  $.ajax({
    url : '/pub/opt/ajaxpdwhcomment',
    type : 'post',
    data : {
      "des3PdwhPubId" : des3PdwhPubId,
      "content" : replyContent
    },
    dataType : "json",
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          Pubdetails.getPdwhCommentList(des3PdwhPubId);
          Pubdetails.getPdwhCommentNumber(des3PdwhPubId);
          $("#pubComment").find("textarea[name$='comments']").val("");
          $("#pubCommnetBtn").attr("disabled", "disabled");
        } else {
          scmpublictoast(pubdetails.pubIsDeleted, 1000);
        }
      });
    }

  });
}
// 添加评论数
Pubdetails.getCommentNumber = function(des3PubId) {
  $.ajax({
    url : '/pub/opt/ajaxcommentnumber',
    type : 'post',
    data : {
      "des3PubId" : des3PubId
    },
    dataType : "json",
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data == '0') {
          $("#pubTotalComent").text(pubdetails.commentTips);
          $("#pubTotalComentOp").text(pubdetails.comment);
        } else {
          if (data >= 1000) {
            $("#pubTotalComent").text(pubdetails.commentTips + '(1K+)');
            $("#pubTotalComentOp").text(pubdetails.comment + '(1K+)');
          } else {
            $("#pubTotalComent").text(pubdetails.commentTips + '(' + data + ')');
            $("#pubTotalComentOp").text(pubdetails.comment + '(' + data + ')');
          }
        }
      });
    }

  });
}
// 基准库加载评论数
Pubdetails.getPdwhCommentNumber = function(des3PubId) {
  $.ajax({
    url : '/pub/opt/ajaxcommentnumberpdwh',
    type : 'post',
    async : false,
    data : {
      "des3PubId" : des3PubId
    },
    dataType : "json",
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data == '0' || data == null) {
          $("#pubTotalComent").text(pubdetails.commentTips);
        } else {
          $("#pubTotalComent").text(pubdetails.commentTips + '(' + data + ')');
          $("#pubTotalComentOp").text(pubdetails.comment + '(' + data + ')');
        }

      });
    }

  });
}
// 取消评论
Pubdetails.cancelComment = function() {
  $("#pubCommnetBtn").hide();
  $("#pubCommnetCancle").hide();
  $("#pubComment").find("textarea[name$='comments']").val("");
}
// 点击相关评论
Pubdetails.relatedComment = function(des3PubId) {
  Pubdetails.getCommentList(des3PubId);
  Pubdetails.getCommentNumber(des3PubId);
  $("#pubComment").find("textarea[name$='comments']").val("");
  $("#pubCommnetBtn").attr("disabled", "disabled");
}
// 基准库点击相关评论
Pubdetails.relatedPdwhComment = function(des3PubId) {
  Pubdetails.getPdwhCommentList(des3PubId);
  Pubdetails.getPdwhCommentNumber(des3PubId);
  $("#pubComment").find("textarea[name$='comments']").val("");
  $("#pubCommnetBtn").attr("disabled", "disabled");
}
// 收藏功能
Pubdetails.collection = function(pubId, ownerId) {
  var params = [];
  params.push({
    "pubId" : pubId,
    "ownerId" : ownerId
  });
  var postData = {
    "jsonParams" : JSON.stringify(params),
    "articleType" : "2"
  };
  $.ajax({
    url : '/pubweb/pubdetails/ajaxtomyliteratrue',
    type : 'post',
    data : postData,
    dataType : "json",
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(pubdetails.CollectionSuccess, 2000);
        } else {
          scmpublictoast(pubdetails.CollectionFail, 2000);
        }

      });
    }

  });
}

// 站外-操作-统一提示登录
Pubdetails.outsideTip = function(des3PubId) {
  Pubdetails.pdwhIsExist2(des3PubId, function() {
    $.ajax({
      url : "/psnweb/timeout/ajaxtest",
      type : "post",
      dataType : "json",
      success : function(data) {
        Pubdetails.ajaxTimeOut(data, function() {
        });
      }
    });
  });
};
Pubdetails.outsideTip2 = function() {
  $.ajax({
    url : "/psnweb/timeout/ajaxtest",
    type : "post",
    dataType : "json",
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
      });
    }
  });
};

// 检查基准库成果是否存在
Pubdetails.pdwhIsExist2 = function(des3PubId, func) {
  $.ajax({
    url : "/pub/outside/ajaxpdwhisexists",
    type : 'post',
    dataType : "json",
    async : false,
    data : {
      "des3PubId" : des3PubId
    },
    timeout : 10000,
    success : function(data) {
      if (data.result == 'success') {
        func();
      } else {
        scmpublictoast(pubdetails.pubIsDeleted, 1000);
      }
    },
    error : function(data) {
      scmpublictoast("系统出错", 1000);
    }
  });
}

// 超时处理
Pubdetails.ajaxTimeOut = function(data, myfunction) {
  var toConfirm = false;
  if (data.ajaxSessionTimeOut == 'yes') {
    toConfirm = true;
  }
  if (!toConfirm && data != null) {
    toConfirm = data.ajaxSessionTimeOut;
  }
  if (toConfirm) {
    jConfirm(pubdetails.i18n_timeout, pubdetails.i18n_tipTitle, function(r) {
      if (r) {
        var url = window.location.href;
        if (url.indexOf("/pubweb/outside/pdwhdetails") > 0) {
          document.location.href = url.replace("/pubweb/outside/pdwhdetails", "/pubweb/details/showpdwh");
          return 0;
        }
        document.location.href = "/oauth/index?sys=SNS&service=" + encodeURIComponent(url);
      }
    });
  } else {
    if (typeof myfunction == "function") {
      myfunction();
    }
  }
};
/**
 * 添加阅读记录
 */
Pubdetails.addReadRecord = function() {
  $.ajax({
    url : '/pub/opt/ajaxview',
    type : 'post',
    data : {
      "des3PubId" : $("#des3PubId").val(),
      "des3ReadPsnId" : $("#currentPsnId").val()
    },
    dataType : "json",
    success : function(data) {

    },
    error : function() {

    }

  });
}

/**
 * 添加访问记录
 */
Pubdetails.addVistRecord = function(vistPsnDes3Id, actionDes3Key, actionType) {
  if (vistPsnDes3Id != null && vistPsnDes3Id != "" && actionDes3Key != null && actionDes3Key != ""
      && actionType != null && actionType != "") {
    $.ajax({
      url : "/psnweb/outside/addVistRecord",
      type : "post",
      dataType : "json",
      data : {
        "vistPsnDes3Id" : vistPsnDes3Id,
        "actionDes3Key" : actionDes3Key,
        "actionType" : actionType
      },
      success : function(data) {

      },
      error : function() {
      }
    });
  }
}

/**
 * 阅读记录添加后，更新人员成果影响力
 */
Pubdetails.updatePsnEffect = function() {
  $.ajax({
    url : "/pubweb/statistics/updatepsneffect",
    type : "post",
    dataType : "json",
    data : {
      "readPsnDes3Id" : $("#ownerPsnId").val()
    },
    success : function(data) {

    },
    error : function() {
    }
  });
}
Pubdetails.rcmFulltext = function() {
  $.ajax({
    url : "/pub/opt/ajaxgetpubdetailrcmdpubft",
    type : "post",
    data : {
      "des3PubId" : $("#des3PubId").val(),
      "ownerDes3PsnId" : $("#ownerPsnId").val()
    },
    dataType : "html",
    success : function(data) {
      $("#rcmdPubft").html(data);
      if (document.getElementsByClassName("confirm-fulltext")[0]) {
        var goheight = document.getElementsByClassName("bckground-cover")[0].offsetHeight;
        var gowidth = document.getElementsByClassName("bckground-cover")[0].offsetWidth;
        var setheight = document.getElementsByClassName("confirm-fulltext")[0].offsetHeight;
        var setwidth = document.getElementsByClassName("confirm-fulltext")[0].offsetWidth;
        document.getElementsByClassName("confirm-fulltext")[0].style.bottom = (goheight - setheight) / 2 + "px";
        document.getElementsByClassName("confirm-fulltext")[0].style.left = (gowidth - setwidth) / 2 + "px";
        document.getElementsByClassName("confirm-fulltext_header-close")[0].onclick = function() {
          document.getElementsByClassName("confirm-fulltext")[0].style.bottom = -setheight + "px";
          setTimeout(function() {
            document.getElementsByClassName("bckground-cover")[0].removeChild(document
                .getElementsByClassName("confirm-fulltext")[0]);
            document.getElementById("rcmdPubft").removeChild(document.getElementsByClassName("bckground-cover")[0]);
          }, 500);
        }
      }
    },
    error : function() {
    }
  });
}
// 是我的全文成果
Pubdetails.doConfirmPubft = function(Id) {
  closeRcmdFulltext();
  $.ajax({
    url : '/pub/opt/ajaxConfirmPubft',
    type : 'post',
    dataType : 'json',
    data : {
      'ids' : Id
    },
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data.status == "success") {
          scmpublictoast(pubdetails.rcmdft_success, 2000);
        } else if (data.status == "not_exist") {
          scmpublictoast("附件已删除", 2000);
          return;
        } else {
          scmpublictoast(pubdetails.rcmdft_error, 2000);
        }
      });
    },
    error : function() {
      scmpublictoast(pubdetails.optFailed, 2000);
    }
  });
};

// 不是我的全文成果
Pubdetails.doRejectPubft = function(Id) {
  closeRcmdFulltext();
  $.ajax({
    url : '/pub/opt/ajaxRejectPubft',
    type : 'post',
    dataType : 'json',
    data : {
      'ids' : Id
    },
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(pubdetails.optSuccess, 2000);
          // window.location.reload();
          setTimeout(function() {
            Pubdetails.rcmFulltext();// 再次获取全文
          }, 1000);

        } else {
          scmpublictoast(pubdetails.optFailed, 2000);
        }
      });
    },
    error : function() {
      scmpublictoast(pubdetails.optFailed, 2000);
    }
  });
};
/**
 * 个人成果请求全文
 * 
 * @author houchuanjie
 */
Pubdetails.requestFullText = function(obj, des3RecvPsnId, des3PubId) {
  // 防止重复点击
  BaseUtils.doHitMore(obj, 1000);
  if (des3RecvPsnId && des3PubId) {
    $.ajax({
      url : '/pub/fulltext/ajaxreqadd',
      type : 'post',
      data : {
        "des3RecvPsnId" : des3RecvPsnId,
        "des3PubId" : des3PubId,
        'pubType' : 'sns'
      },
      dataType : "json",
      success : function(data) {
        Pubdetails.ajaxTimeOut(data, function() {
          if (data.status == "success") { // 全文请求保存成功
            scmpublictoast(pubdetails.req_success, 2000);
          } else if (data.status == "isDel") {
            scmpublictoast(pubdetails.pubIsDeleted, 2000, 2);
          } else {
            scmpublictoast(pubdetails.req_error, 2000);
          }
        });
      },
      error : function() {
        scmpublictoast(pubdetails.req_error, 2000);
      }
    });
  }
};
/**
 * 基准库成果请求全文
 * 
 * @author houchuanjie
 */
Pubdetails.requestPdwhFullText = function(des3PubId) {
  Pubdetails.pdwhIsExist2(des3PubId, function() {
    if (des3PubId) {
      $.ajax({
        url : '/pub/fulltext/ajaxreqadd',
        type : 'post',
        data : {
          "des3PubId" : des3PubId,
          'pubType' : 'pdwhpub'
        },
        dataType : "json",
        success : function(data) {
          Pubdetails.ajaxTimeOut(data, function() {
            if (data.status == "success") { // 全文请求保存成功
              scmpublictoast(pubdetails.req_success, 2000);
            } else if (data.status == "isDel") {
              scmpublictoast(pubdetails.pubIsDeleted, 2000, 2);
            } else {
              scmpublictoast(pubdetails.req_error, 2000);
            }
          });
        },
        error : function() {
          scmpublictoast(pubdetails.req_error, 2000);
        }
      });
    }
  });
};
/**
 * 基准库收藏成果 publicationArticleType
 */
Pubdetails.importPdwh = function(pubId) {
  var params = [];
  params.push({
    "pubId" : pubId
  });
  var postData = {
    "jsonParams" : JSON.stringify(params),
    "publicationArticleType" : "2"
  };
  $.ajax({
    url : "/pubweb/ajaxpublication/import/pdwh",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data.result == true) {
          scmpublictoast(pubdetails.CollectionSuccess, 2000);
        } else {
          scmpublictoast(pubdetails.CollectionFail, 2000);
        }

      });

    },
    error : function() {
    }
  });

}
// 替代原有的基准库收藏
Pubdetails.ajaxSavePdwh = function(des3PubId) {
  $.ajax({
    url : "/pubweb/ajaxsavePaper",
    type : "post",
    dataType : "json",
    data : {
      "des3PubId" : des3PubId,
      "pubDb" : "PDWH"
    },
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data && data.result) {
          if (data.result == "success") {
            scmpublictoast(pubdetails.CollectionSuccess, 1000);
          } else if (data.result == "exist") {
            scmpublictoast(pubdetails.pubIsSaved, 1000);
          } else if (data.result == "isDel") {
            scmpublictoast(pubdetails.pubIsDeleted, 1000);
          } else {
            scmpublictoast(pubdetails.CollectionFail, 1000);
          }
        } else {
          scmpublictoast(pubdetails.CollectionFail, 1000);
        }
      });
    }
  });
}
// 替代原有的个人库收藏
Pubdetails.ajaxSaveSns = function(des3PubId) {
  $.ajax({
    url : "/pubweb/ajaxsavePaper",
    type : "post",
    dataType : "json",
    data : {
      "des3PubId" : des3PubId,
      "pubDb" : "SNS"
    },
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data && data.result) {
          if (data.result == "success") {
            scmpublictoast(pubdetails.CollectionSuccess, 1000);
          } else if (data.result == "exist") {
            scmpublictoast(pubdetails.pubIsSaved, 1000);
          } else if (data.result == "isDel") {
            scmpublictoast(pubdetails.pubIsDeleted, 1000);
          } else {
            scmpublictoast(pubdetails.CollectionFail, 1000);
          }
        } else {
          scmpublictoast(pubdetails.CollectionFail, 1000);
        }
      });
    }
  });
}

/**
 * 添加基准库成果阅读记录
 */
Pubdetails.addPdwhPubReadRecord = function() {
  $.ajax({
    url : '/pub/opt/ajaxviewpdwh',
    type : 'post',
    data : {
      "des3PdwhPubId" : $("#des3PubId").val(),
      "des3ReadPsnId" : $("#currentPsnId").val()
    },
    dataType : "json",
    success : function(data) {

    },
    error : function() {

    }

  });
}

// 获取基准库成果操作统计数
Pubdetails.findPdwhPubStatistics = function(des3PubId) {
  $.ajax({
    url : '/pub/opt/ajaxstatistics',
    type : 'post',
    data : {
      "des3PubId" : des3PubId,
    },
    dataType : "json",
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          $("#cited_count_span").html(data.citedCount);
          $("#read_count_span").html(data.readCount);
        } else {
          $("#cited_count_span").html(0);
          $("#read_count_span").html(0);
        }
        Pubdetails.addPdwhPubReadRecord();
      });
    },
    error : function() {
      $("#cited_count_span").html(0);
      $("#read_count_span").html(0);
      Pubdetails.addPdwhPubReadRecord();
    }
  });
}

// 个人库-这是我的成果
Pubdetails.impMyPubConfirm = function(nodeId, pubId, ownerId) {
  var screencallbackData = {
    'nodeId' : nodeId,
    'pubId' : pubId,
    'ownerId' : ownerId
  };
  var option = {
    'screentxt' : pubdetails.confirm_pub,
    'screencallback' : Pubdetails.impMyPub,
    'screencallbackData' : screencallbackData
  };
  popconfirmbox(option);
};

// 个人库成果-导入到我的成果库
Pubdetails.impMyPub = function(callbackData) {
  var params = [];
  params.push({
    "nodeId" : callbackData.nodeId,
    "pubId" : callbackData.pubId,
    "ownerId" : callbackData.ownerId,
    "ispubview" : "true"
  });
  var postData = {
    "pubJsonParams" : JSON.stringify(params),
    "articleType" : "1"
  };
  $.ajax({
    url : "/pub/snspub/importotherpubtomypub",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data != null && data.result) {
          if (data.result == "success") {
            scmpublictoast(pubdetails.importSuccess, 1000);
          } else if (data.result == "dup") {
            scmpublictoast(pubdetails.confirm_dup, 2000);
          } else {
            scmpublictoast(pubdetails.importFail, 2000);
          }
        } else {
          scmpublictoast(pubdetails.importFail, 2000);
        }
      });
    }
  });
};

Pubdetails.showMobileLoginTips = function(targetUrl) {
  Smate.confirm("提示", "系统超时或未登录，你要登录吗？", function() {
    window.location.href = "/oauth/mobile/index?sys=wechat&service=" + encodeURIComponent(targetUrl);
  }, ["确定", "取消"]);
}

// 移动端个人库成果-导入到我的成果库
Pubdetails.impMyPubMobile = function(callbackData, targetUrl) {
  var params = [];
  params.push({
    "nodeId" : callbackData.nodeId,
    "pubId" : callbackData.pubId,
    "ownerId" : callbackData.ownerId,
    "ispubview" : "true"
  });
  var postData = {
    "jsonParams" : JSON.stringify(params),
    "articleType" : "1"
  };
  $.ajax({
    url : "/pubweb/publication/ajaximporttomypub",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      var toConfirm = false;
      if (data.ajaxSessionTimeOut == 'yes') {
        toConfirm = true;
      }
      if (!toConfirm && data != null) {
        toConfirm = data.ajaxSessionTimeOut;
      }
      if (toConfirm) {
        Pubdetails.showMobileLoginTips(targetUrl);
      } else {
        if (data != null && data.result) {
          if (data.result == "success") {
            scmpublictoast(data.msg, 1000);
          } else if (data.result == "dup") {
            scmpublictoast(pubdetails.confirm_dup, 2000);
          } else {
            scmpublictoast(data.msg, 2000);
          }
        } else {
          scmpublictoast("网络错误", 2000);
        }
      }
    }
  });
};

// 基准库-这是我的成果
Pubdetails.importMyPubPdwhConfirm = function(pubId) {
  Pubdetails.pdwhIsExist(pubId, function() {
    var screencallbackData = {
      'pubId' : pubId
    };
    var option = {
      'screentxt' : pubdetails.confirm_pub,
      'screencallback' : Pubdetails.importMyPubPdwh,
      'screencallbackData' : screencallbackData
    };
    popconfirmbox(option);
  });
};

// 检查基准库成果是否存在
Pubdetails.pdwhIsExist = function(des3PubId, func) {
  $.ajax({
    url : "/pub/opt/ajaxpdwhisexists",
    type : 'post',
    dataType : "json",
    async : false,
    data : {
      "des3PubId" : des3PubId
    },
    timeout : 10000,
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          func();
        } else {
          scmpublictoast(pubdetails.pubIsDeleted, 1000);
        }
      });
    },
    error : function(data) {
      scmpublictoast("系统出错", 1000);
    }
  });
}

// 基准库成果-导入到我的成果库
Pubdetails.importMyPubPdwh = function(callbackData) {
  $.ajax({
    url : "/pub/snspub/importpdwhpubtomypub",
    type : "post",
    dataType : "json",
    data : {
      "des3PubId" : callbackData.pubId
    },
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (data != null && data.result) {
          if (data.result == "success") {
            scmpublictoast(pubdetails.importSuccess, 2000);
          } else if (data.result == "dup") {
            scmpublictoast(pubdetails.confirm_dup, 2000);
          } else {
            scmpublictoast(pubdetails.importFail, 2000);
          }
        } else {
          scmpublictoast(pubdetails.importFail, 2000);
        }
      });
    },
    error : function() {
      scmpublictoast(pubdetails.importFail, 2000);
    }
  });
};
// 成果编辑
Pubdetails.pubEdit = function(des3PubId) {
  var forwardUrl = "/pub/enter?des3PubId=" + encodeURIComponent(des3PubId);
  BaseUtils.forwardUrlRefer(true, forwardUrl);
  /*
   * var data = {"forwardUrl":forwardUrl}; $.ajax({ url : '/pubweb/ajaxforwardUrlRefer', //url :
   * '/pubweb/forwardUrl', type : 'post', dataType:'json', data : data, success : function(data) {
   * Pubdetails.ajaxTimeOut(data , function(){ window.location.href=data.forwardUrl; }); }, });
   */

};
Pubdetails.changeLocale = function(locale, title) {
  if (locale == "en_US") {
    $("#detail_pub_main_en").css("display", "block");
    if (title != "") {
      $("#check_language_zh").css("display", "block");
    }
    $("#detail_pub_main_zh").css("display", "none");
    $("#check_language_en").css("display", "none");
  } else {
    $("#detail_pub_main_zh").css("display", "block");
    $("#check_language_zh").css("display", "none");
    if (title != "") {
      $("#check_language_en").css("display", "block");
    }
    $("#detail_pub_main_en").css("display", "none");
  }
}

// 其他类似全文
Pubdetails.fulltextList = function(des3PubId) {
  Pubdetails.checkTimeout(function() {
    $.ajax({
      url : '/pub/opt/ajaxsimilarlist',
      type : 'post',
      dataType : 'html',
      data : {
        "des3PubId" : des3PubId
      },
      success : function(data) {
        Pubdetails.ajaxTimeOut(data, function() {
          var option = {
            'data' : data
          };
          scmconfirmbox(option);
        });
      },
    });
  });
};

// pdwh其他类似全文
Pubdetails.pdwhFulltextList = function(des3PubId) {
  Pubdetails.checkTimeout(function() {
    $.ajax({
      url : '/pub/opt/ajaxpdwhsimilarlist',
      type : 'post',
      dataType : 'html',
      data : {
        "des3PdwhPubId" : des3PubId
      },
      success : function(data) {
        Pubdetails.ajaxTimeOut(data, function() {
          var option = {
            'data' : data
          };
          scmconfirmbox(option);
        });
      },
    });
  });
};

// 检查是否超时
Pubdetails.checkTimeout = function(myfunction) {
  $.ajax({
    url : "/dynweb/ajaxtimeout",
    dataType : "json",
    type : "post",
    data : {},
    success : function(data) {
      Pubdetails.ajaxTimeOut(data, function() {
        if (typeof myfunction == "function") {
          myfunction();
        }
      });
    }
  });
};

/**
 * 评论初始化
 */
Pubdetails.commentInit = function() {
  // var des3PubId = "uicwOqvCOzS16e4xj4WxDA%3D%3D";
  var des3PubId = $("#des3PubId").val();
  var ownerPsnId = $("#ownerPsnId").val();
  var psnId = $("#psnId").val();
  if (ownerPsnId != psnId) {
    Pubdetails.commentsOnfocus();
  }
  $('#input').atwho({
    at : "@",
    startWithSpace : false,
    callbacks : {
      remoteFilter : function(query, callback) {
        $.getJSON("/pub/ajaxgetrelationpsn?", {
          keywords : query,
          des3PubId : des3PubId,
          des3PsnId : ownerPsnId
        }, function(data) {
          callback(data)
        });
      }
    }
  })
}

Pubdetails.commentsOnfocus = function() {
  $("#input").focus(function() {
    var val = $(this).val();
    if ($.trim(val) == "") {
      var username = $("#pub_username").val();
      if (username != undefined && username != "") {
        $(this).val("@" + username + " ");
        $(this).keyup();
      }
    }
  });

}

/**
 * 显示关联的人员
 */
Pubdetails.showRelationPsn = function() {
  var position = Pubdetails.getCursortPosition($("#input")[0]);
  $("#input").attr("position", position);
  var val = $("#input").val().substring(0, position);
  if ($.trim(val) == "" || val.lastIndexOf("@") == -1) {
    $(".ac__box").css("display", "none");
    return;
  }
  var key = val.substr(val.lastIndexOf("@") + 1);
  if ($.trim(key).length != key.length) {
    $(".ac__box").css("display", "none");
    return;
  }
  var param = {
    des3PubId : "uicwOqvCOzS16e4xj4WxDA%3D%3D",
    keywords : key,
  }
  $.ajax({
    url : "/pub/ajaxgetrelationpsn",
    dataType : "json",
    type : "post",
    data : param,
    success : function(data) {
      $(".ac__box .ac__list").empty();
      if (data != undefined && data.length > 0) {
        for ( var i in data) {
          var html = '<li class="ac__item" code="34447">' + data[i] + '</li><input type="hidden"/>';
          if (i < 5) {
            $(".ac__box .ac__list").append(html);
          }
        }
        $(".ac__box .ac__list .ac__item").first().addClass("item_hovered");
        $(".ac__box").css("display", "block");
        Pubdetails.commentKeyUpEvnet();
      } else {
        $(".ac__box").css("display", "none");
      }
    }
  });
}
