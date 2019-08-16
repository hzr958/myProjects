//请一起引入scm.pop.mobile.js

var mobile = mobile || {};
mobile.pub = mobile.pub || {};
mobile.snspub = mobile.snspub || {};
mobile.pub.ajaxrefreshpubs = function(myscroll) {
  var data = {
    "articleType" : $("#articleType").val(),
    "fromPage" : $("#fromPage").val(),
    "pubType" : $("#pubType").val(),
    "orderType" : $("#orderType").val(),
    "pubLocale" : $("#pubLocale").val(),
    "flag" : 1
  }
  mobile.pub.ajaxrequest(data, mobile.pub.refreshpubs);
}

mobile.pub.refreshpubs = function(data) {
  $("#listdiv").html("");
  $("#listdiv").html(data);
  $("#count").val(0);
}

mobile.pub.ajaxuploadpubs = function() {
  if ($("#load_preloader").length > 0) {
    return;
  }
  mobile.pub.showLoader();
  var nextId = null;
  if (nextId == null || nextId == "") {
    var nextIds = document.getElementsByName("nextIdEx");
    if (nextIds.length > 0) {
      nextId = nextIds.length;
    } else {
      nextId = 0;
    }
  }
  $("#nextId").val(nextId);
  var data = {
    "articleType" : $("#articleType").val(),
    "publishYear" : $("#publishYear").val(),
    "searchPubType" : $("#searchPubType").val(),
    "includeType" : $("#includeType").val(),
    "fromPage" : $("#fromPage").val(),
    "orderBy" : $("#orderBy").val(),
    "flag" : 1,
    "nextId" : nextId
  }
  mobile.pub.ajaxrequest(data, mobile.pub.uploadpubs);
  $("#des3NextId").val("");

}
mobile.pub.showLoader = function() {
  var loaderhtml = '<div class="preloader active" id="load_preloader" style="height:80px; padding: 20px 0px;">'
      + ' <div class="preloader-ind-cir__box" style="width: 24px; height: 24px; margin:0 auto;">'
      + '<div class="preloader-ind-cir__fill">' + '<div class="preloader-ind-cir__arc-box left-half">'
      + '<div class="preloader-ind-cir__arc">' + '</div>' + '</div>' + '<div class="preloader-ind-cir__gap">'
      + '<div class="preloader-ind-cir__arc">' + '</div>' + '</div>'
      + '<div class="preloader-ind-cir__arc-box right-half">' + '<div class="preloader-ind-cir__arc">' + '</div>'
      + '</div>' + '</div>' + '</div>' + '</div>';
  $("#addload").before(loaderhtml);
  $("#addload").remove();

}

mobile.pub.uploadpubs = function(data) {
  var hasPrivatePub = $("#psnHasPrivatePub").val();
  var isOthers = $("#other").val();
  var noMoreRecordTips = "没有更多记录";
  if (isOthers == "true" && hasPrivatePub == "true") {
    noMoreRecordTips = "由于权限设置, 可能部分数据未显示";
  }
  if ($("#listdiv").find(".noRecord").length == 0) {
    if ($("#listdiv").find(".paper").length > 0 && data.indexOf("response_no-result") > -1) {
      $("#load_preloader").before(data);
    } else {
      $("#load_preloader").before(data);
      $(".dev_nextIdEx:last").after('<div id="addload" style="width: 100%; height: 120px;"></div>');
    }
    if ($(".response_no-result").length > 0) {
      $(".response_no-result").text(noMoreRecordTips);
    }
  }
  // 如果只有一页内容，则要显示提示语
  var totalCount = $("#pubTotalCount").val();
  if (totalCount == 0) {
    noMoreRecordTips = "未找到符合条件的记录";
  }
  var pubSumFromPubList = $("#pubSumFromPubList").val();
  if (parseInt(totalCount) <= parseInt(pubSumFromPubList)) {
    if (parseInt(totalCount) != 0 && $(".response_no-result").length == 0) {
      $("#load_preloader").before(
          "<div class='response_no-result' style='padding-bottom: 19%;'>" + noMoreRecordTips + "</div>");
    } else {
      $(".response_no-result").text(noMoreRecordTips);
    }
    $("#addload").remove();
  }
  if (parseInt($("#count").val()) == 0 && data.indexOf("noRecord") != -1) {
    var count = parseInt($("#count").val()) + 1;
    $("#count").val(count);
  }
  $("#load_preloader").remove();
}

mobile.pub.ajaxrequest = function(data, callback) {
  var isLogin = $("#hasLogin").val();
  var url = "";
  if ($("#other").val() == "true") {
    url = "/pub/querylist/ajaxpsn";
    data['des3SearchPsnId'] = $("#des3SearchPsnId").val();
    if (isLogin == "1") {
      url = "/pub/querylist/ajaxpsn";
    } else {
      url = "/pub/outside/querylist/ajaxpsn";
    }
  } else {
    url = "/pub/querylist/ajaxpsn";
  }
  $.ajax({
    url : url,
    type : "post",
    dataType : "html",
    data : data,
    async : false,
    success : function(data) {
      setTimeout(function() {
        callback(data);
      }, 300)

    },
    error : function(data) {
      $("#load_preloader").hide();
      alert("请求数据出错!");
    }
  });
}

/**
 * 加载评论信息
 */
mobile.pub.ajaxLoadComments = function(pagNo, maxresult, type) {
  var des3PubId = $("#des3PubId").val();

  $.ajax({
    url : "/pub/outside/ajaxcommentlist",
    type : "post",
    dataType : "html",
    data : {
      "des3PubId" : des3PubId,
      "pageNo" : pagNo,
      "maxresult" : maxresult
    },
    success : function(data) {
      $("#replyDiv").append(data);
      var replySizeArray = document.getElementsByName("dynReplySize");
      var commentNum = $(".commentTotalCount").val();
      var comment = $("#comentCountSpan").html();
      if (type == 1) {
        /* var commentNum=comment.replace(/\D+/g, ""); */
        if (parseInt(commentNum) < 1) {
          $(".wdful_comments").hide();
        }
      }
      if (type == 2) {
        if ($("#operateType").val() == "comment") {
          $("html,body").animate({
            scrollTop : $("#animatePubId").offset().top
          }, 100);
        }
      }
      if (replySizeArray.length < parseInt(commentNum)) {
        $("#showMore").show();
      } else {
        $("#showMore").hide();
      }
    },
    error : function() {
      console.log("加载出错");
      return false;
    }
  });
}

/**
 * 成果分享
 */
mobile.pub.ajaxdopubshare = function(des3PubId) {
  $.ajax({
    url : '/pubweb/wechat/ajaxpubshare',
    type : 'post',
    dataType : 'json',
    data : {
      "des3PubId" : des3PubId
    },
    success : function(data) {
      if (data.result == "success") {
        var pcc = $("#pubShareCount").text();
        $("#pubShareCount").text(parseInt(pcc) + 1);
        alert("分享成功!");
      } else {
        alert("分享失败!");
      }
    },
    error : function() {
      alert("分享失败,请稍候再试!");
    }
  });
  // 产生动态 分享：操作类型为 3
  var data = {
    "resType" : 1,
    "des3ResId" : des3PubId,
    "des3PubId" : des3PubId,
    "dynType" : "B2TEMP",
    "replyContent" : "",
    "operatorType" : 3
  }

  mobile.pub.ajaxrealtime(data);
}
/**
 * 发布动态信息
 */
mobile.pub.ajaxrealtime = function(dataJson, callBack) {
  $.ajax({
    url : '/dynweb/dynamic/ajaxrealtime',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      if (data.result == "success") {
      } else {
        alert("发布动态信息失败,请稍候再试!");
      }
    },
    error : function() {
      alert("发布动态信息失败,请稍候再试!");
    }
  });
}

/**
 * 成果赞
 */
mobile.pub.ajaxdoawardpub = function(des3PubId) {
  // 点赞操作
  $.ajax({
    url : "/dynweb/dynamic/ajaxawarddyn",
    type : "post",
    data : {
      "resType" : 1,
      "des3ResId" : des3PubId,
      "des3PubId" : des3PubId,
      "dynType" : "B2TEMP",
      "replyContent" : "",
      "operatorType" : 2
    },
    dataType : "json",
    success : function(data) {
      if (data.action == 1) {
        $("#pubAwardCount").prev(".material-icons").css({
          "background" : "#2196f3",
          "color" : "#ffffff"
        });
        // 不能取消赞
        $("#hasAwarded").val("1");
        var pcc = $("#pubAwardCount").text();
        $("#pubAwardCount").text(parseInt(pcc) + 1);
      }
    },
    error : function(e) {
    }
  });
}

// 成果详情评论,一条一条添加的
mobile.pub.submitMobileComment = function(des3ResId, commnetContent) {
  /*
   * articleType = articleType > 0 ? articleType : 1;//默认为1
   */$.ajax({
    url : '/pub/optsns/ajaxcomment',
    type : 'post',
    data : {
      "des3PubId" : des3ResId,
      "content" : commnetContent
    },
    dataType : "json",
    success : function(data) {
      $(".wdful_comments").show();
      mobile.pub.ajaxLoadComments(1, 1, 2);
      var commentStr = $(".span_comment").html();
      var commentCount = commentStr.replace(/\D+/g, "");;
      commentCount = $.trim(commentCount) != "" ? parseInt(commentCount) : 0;
      if (commentCount > 0 && commentCount <= 999) {
        $(".span_comment").html("评论 (" + (commentCount + 1) + ")");
      }
      if (commentCount > 999) {
        $(".span_comment").html("评论 (1k+)");
      }

      $("#pubReplyBox").slideUp();
      // 隐藏"查看更早10条评论"
      var replySizeArray = document.getElementsByName("dynReplySize");
      if (replySizeArray.length > 10) {
        $("#showMore").show();
      }
    }

  });

}
// //成果快速分享
// mobile.snspub.quickshare=function(platform, des3PubId){
// if (des3PubId == "dev_publist_share") {
// des3PubId = $('.dev_publist_share').val();
// }
// var dataJson={
// "platform" : platform,
// "des3PubId": des3PubId
// }
// $.ajax( {
// url : '/pub/optsns/ajaxshare',
// type : 'post',
// dataType:'json',
// contentType: 'application/json',
// data : JSON.stringify(dataJson),
// success : function(data) {
// if(data.result=="success"){
// location.href="/dynweb/mobile/dynshow";
// }
// },
// error: function (){
// alert("分享失败,请稍候再试!");
// }
// });
// };

// 成果快速分享
mobile.pub.quickShareDyn = function(resType, des3ResId) {
  if (des3ResId == "dev_publist_share") {
    des3ResId = $('.dev_publist_share').val();
  }
  var dataJson = {
    "dynType" : "B2TEMP",
    "resType" : Number(resType),
    "des3ResId" : des3ResId,
    "operatorType" : 3,
    "databaseType" : 2
  }
  $.ajax({
    url : '/dynweb/dynamic/ajaxquickshare',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      if (data.result == "success") {
        scmpublictoast("分享成功", 1000);
        var pubDB = "SNS";
        if (resType == "22") {
          pubDB = "PDWH";
        }
        mobile.pub.initPubOptStatistics(des3ResId, pubDB);
      } else {
        scmpublictoast("网络繁忙，请稍后重试", 1000);
      }
    },
    error : function() {
      scmpublictoast("网络繁忙，请稍后重试", 1000);
    }
  });
};

mobile.pub.shareToDyn = function(obj) {
  var $obj = $(obj);
  mobile.pub.quickShareDyn($obj.attr("resType"), $obj.attr("des3ResId"));
}

// 初始化成果操作数，pubDB: 个人库（SNS）,基准库（PDWH）
mobile.pub.initPubOptStatistics = function(des3PubId, pubDB) {
  var shareContentObj = $(".span_share[resid='" + des3PubId + "']");
  var url = "/pub/optsns/ajaxstatistics";
  var postData = {
    "des3PubId" : des3PubId
  };
  if (pubDB == "PDWH") {
    url = "/pub/optpdwh/ajaxstatistics";
    postData = {
      "des3PdwhPubId" : des3PubId
    };

  }
  $.ajax({
    url : url,
    type : 'post',
    dataType : 'json',
    data : postData,
    success : function(data) {
      if (data.result == "success") {
        if (data.shareCount > 0 && data.shareCount <= 999) {
          shareContentObj.text("分享 (" + data.shareCount + ")");
        }
        if (data.shareCount > 999) {
          shareContentObj.text("分享 (1k+)");
        }

        // TODO 可以把其他操作数也更新下
      }
    },
    error : function() {
    }
  });
}

// 个人主页 成果分享功能
mobile.pub.quickPsnHomeShareDyn = function(resType, obj) {
  var des3ResId = $(obj).attr("des3PubId");
  var pubId = $(obj).attr("pubId");
  var dataJson = {
    "dynType" : "B2TEMP",
    "resType" : Number(resType),
    "des3ResId" : des3ResId,
    "operatorType" : 3,
    "databaseType" : 2
  }
  $.ajax({
    url : '/dynweb/dynamic/ajaxquickshare',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      if (data.result == "success") {
        var count = Number($("#shareCount_" + pubId).text().replace(/[\D]/ig, ""));
        count = count + 1;
        $("#shareCount_" + pubId).text("分享" + "(" + count + ")");
      }
    },
    error : function() {
      alert("分享失败,请稍候再试!");
    }
  });
};
/**
 * 更新全文认领和成果认领
 */
mobile.pub.msgtips = function(other) {
  var isLogin = $("#hasLogin").val();
  if (other != "true" && isLogin == true) {
    $.ajax({
      url : '/pub/wechat/ajaxmsgtips',
      type : 'post',
      dataType : 'json',
      data : {},
      success : function(data) {
        BaseUtils.ajaxTimeOutMobile(data, function() {
          if (Number(data.pubConfirmCount) > 0) {
            $("#confirmPubNum").html(data.pubConfirmCount);
            $("#confirmPubNum").closest(".dev_confirmpub_tip").show();
            $("#confirmPubNum").closest(".dev_confirmpub_tip").attr("onclick", "mobile.pub.confimPubList()");
          }
          if (Number(data.pubFulltextCount) > 0) {
            $("#confirmfullTextNum").html(data.pubFulltextCount);
            $("#confirmfullTextNum").closest(".dev_confirmfulltext_tip").show();
            $("#confirmfullTextNum").closest(".dev_confirmfulltext_tip")
                .attr("onclick", "mobile.pub.confimpubftrcmd()");
          }
        });
      }
    });
  }
};

// 移动端-我的成果-待确认成果列表展示
mobile.pub.confimPubList = function() {
  // window.location.replace("/pub/confirmpublist?fromPage=mobileConfirmPub&toBack=psnpub");
  window.location.replace("/psnweb/mobile/msgbox?model=centerMsg&whoFirst=pubRcmd");
};

// 移动端-我的成果-待确认的全文
mobile.pub.confimpubftrcmd = function() {
  // window.location.replace("/pub/wechat/pubfulltextlist?toBack=psnpub");
  window.location.replace("/psnweb/mobile/msgbox?model=centerMsg&whoFirst=fullTextRcmd");
};

// V8成果赞操作
mobile.snspub.awardopt = function(obj) {
  BaseUtils.doHitMore(obj, 100);
  var awardCount = 0;
  var operate = 1;
  var isAward = $(obj).attr("isAward");
  var des3PubId = $(obj).attr("resid");
  if (isAward == '1') {
    operate = '0';// 取消赞
  } else {
    operate = '1';// 点赞
  }
  var post_data = {
    "des3PubId" : des3PubId,
    "operate" : operate
  };
  $.ajax({
    url : '/pub/optsns/ajaxlike',
    type : 'post',
    dataType : 'json',
    data : post_data,
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
        if (data.result == "success") {
          awardCount = data.awardTimes;
          callBackAward(obj, awardCount);
        }
      });
    },
  });
};
// V8成果赞操作
mobile.snspub.awardoptnew = function(obj) {
  BaseUtils.doHitMore(obj, 100);
  var awardCount = 0;
  var operate = 1;
  var isAward = $(obj).attr("isAward");
  var des3PubId = $(obj).attr("resId");
  if (isAward == '1') {
    operate = '0';// 取消赞
  } else {
    operate = '1';// 点赞
  }
  var post_data = {
    "des3PubId" : des3PubId,
    "operate" : operate
  };
  $.ajax({
    url : '/pub/optsns/ajaxlike',
    type : 'post',
    dataType : 'json',
    data : post_data,
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
        if (data.result == "success") {
          awardCount = data.awardTimes;
          callBackAward(obj, awardCount);
        }
      });
    },
  });
};

// 成果列表-评论 (打开成果详情)
mobile.snspub.details = function(des3PubId, type) {
  params = "des3PubId=" + encodeURIComponent(des3PubId);
  var url = "/pub/details/snsnonext?";
  if (type == "list") {
    url = "/pub/details/sns?";
  }
  location.href = url + params;
};

// 成果列表-评论 (打开成果详情)
mobile.pub.pdwhDetails = function(des3PubId) {
  mobile.pub.pdwhIsExist(des3PubId, function() {
    params = "des3PubId=" + encodeURIComponent(des3PubId);
    location.href = "/pub/details/pdwh?" + params + "&whoFirst=" + $("#whoFirst").val();
  })
};

// 弹出分享成果框
mobile.pub.sharePubBox = function(des3PubId) {
  BaseUtils.mobileCheckTimeoutByUrl("/pub/ajaxtimeout", function() {
    SmateCommon.checkPubAnyUser(des3PubId, "pubList");
  });
};
// 个人主页弹出分享成果框
mobile.pub.sharePsnHomePagePubBox = function(des3PubId, pubId) {
  $("#shareScreen").attr("pubId", pubId);
  $("#shareScreen").attr("des3PubId", des3PubId);
  $('#dynamicShare').show();
};
/**
 * 收藏或取消收藏成果
 */
mobile.snspub.collect = function(obj) {
  var $this = $(obj);
  pubDb = $.trim($this.attr("pubDb"));
  var des3PubId = $this.attr("resId");
  var collectOperate = $this.attr("collect");
  if (pubDb.toUpperCase() == "PDWH") {
    mobile.pub.pdwhIsExist(des3PubId, function() {
      mobile.pub.collect(des3PubId, collectOperate, obj);
    })
  } else {
    mobile.pub.collect(des3PubId, collectOperate, obj);
  }
};

mobile.pub.collect = function(des3PubId, collectOperate, obj) {
  // **collectOperate 收藏操作： 0：收藏， 1：取消收藏；pubDb ：基准库：PDWH，个人：SNS
  var $this = $(obj);
  var needRefresh = $this.attr("needRefresh");
  var pubDb = "SNS";
  if (typeof ($this.attr("pubDb")) != "undefined" && $this.attr("pubDb") != "") {
    pubDb = $.trim($this.attr("pubDb"));
  }
  var datastr = {
    "des3PubId" : des3PubId,
    "pubDb" : pubDb,
    "collectOperate" : collectOperate
  };
  $.ajax({
    url : "/pub/optsns/ajaxcollect",
    type : "post",
    dataType : "json",
    data : datastr,
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
        if (collectOperate == 0) {
          if (data && data.result) {
            if (data.result == "success") {
              callBackCollect(obj);

            } else if (data.result == "exist") {
              scmpublictoast("已收藏该成果", 1000);
            } else if (data.result == "isDel") {
              scmpublictoast("成果不存在", 1000);
            } else {
              scmpublictoast("收藏失败", 1000);
            }
          } else {
            scmpublictoast("收藏失败", 1000);
          }
        } else if (collectOperate == 1) {
          if (data && data.result == "success") {
            callBackCollect(obj);
          } else {
            scmpublictoast("取消收藏失败!", 1000);
          }
          if ("true" == needRefresh) {
            Pub.loadMobilePaperList();
          }
        }

      });
    },
    error : function() {
    }
  });

}

/**
 * 收藏或取消收藏成果
 */
mobile.snspub.collectnew = function(obj) {
  // **collectOperate 收藏操作： 0：收藏， 1：取消收藏；pubDb ：基准库：PDWH，个人：SNS
  var collectOperate = $(obj).attr("collect");
  var des3PubId = $(obj).attr("resId");
  var $this = $(obj);
  var needRefresh = $this.attr("needRefresh");
  var pubDb = "SNS";
  if (typeof ($this.attr("pubDb")) != "undefined" && $this.attr("pubDb") != "") {
    pubDb = $.trim($this.attr("pubDb"));
  }
  var datastr = {
    "des3PubId" : des3PubId,
    "pubDb" : pubDb,
    "collectOperate" : collectOperate
  };
  $.ajax({
    url : "/pub/optsns/ajaxcollect",
    type : "post",
    dataType : "json",
    data : datastr,
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
        if (collectOperate == 0) {
          if (data && data.result) {
            if (data.result == "success") {
              callBackCollect(obj);
            } else if (data.result == "exist") {
              scmpublictoast("已收藏该成果", 1000);
            } else if (data.result == "isDel") {
              scmpublictoast("成果不存在", 1000);
            } else {
              scmpublictoast("收藏失败", 1000);
            }
          } else {
            scmpublictoast("收藏失败", 1000);
          }
        } else if (collectOperate == 1) {
          if (data && data.result == "success") {
            callBackCollect(obj);
          } else {
            scmpublictoast("取消收藏失败!", 1000);
          }
          if ("true" == needRefresh) {
            Pub.loadMobilePaperList();
          }
        }

      });
    },
    error : function() {
    }
  });

};

// 登录超时 跳转url
mobile.pub.PsnHometimeOut = function() {
  var url = "/psnweb/mobile/otherhome?des3ViewPsnId=" + $("#des3PsnId").val();
  document.location.href = "/oauth/mobile/index?sys=wechat&service=" + encodeURIComponent(url);
}

// 检查基准库成果是否存在
mobile.pub.pdwhIsExist = function(des3PubId, func) {
  $.ajax({
    url : "/pub/optpdwh/ajaxpdwhisexists",
    type : 'post',
    dataType : "json",
    async : false,
    data : {
      "des3PdwhPubId" : des3PubId
    },
    timeout : 10000,
    success : function(data) {
      if (data.result == 'success') {
        func();
      } else {
        if (data.errmsg == "is deleted") {
          scmpublictoast("成果已删除", 1000);
        } else {
          scmpublictoast("成果不存在", 1000);
        }
      }
    },
    error : function(data) {
      scmpublictoast("系统出错", 1000);
    }
  });
}

// 基准库成果赞、取消赞
mobile.pub.pdwhAwardOpt = function(obj) {
  var des3PubId = $(obj).attr("resid");
  mobile.pub.pdwhIsExist(des3PubId, function() {
    BaseUtils.doHitMore(obj, 100);
    var awardCount = 0;
    var operate = 1;
    var isAward = $(obj).attr("isAward");
    var des3PubId = $(obj).attr("resId");
    if (isAward == '1') {
      operate = '0';// 取消赞
    } else {
      operate = '1';// 点赞
    }
    var post_data = {
      "des3PdwhPubId" : des3PubId,
      "operate" : operate
    };
    $.ajax({
      url : '/pub/optpdwh/ajaxlike',
      type : 'post',
      dataType : 'json',
      data : post_data,
      success : function(data) {
        BaseUtils.ajaxTimeOutMobile(data, function() {
          if (data.result == "success") {
            awardCount = data.awardTimes;
            callBackAward(obj, awardCount);
          }
        });
      },
    });
  })
};

// 初始化基准库成果操作统计数
mobile.pub.initPdwhStatistics = function(des3PubId) {
  var URI = "/pub/optpdwh/ajaxstatistics";
  var isLogin = $("#pdwh_details_isLogin");
  if (typeof isLogin != typeof undefined && isLogin && isLogin.val() == "false") {
    URI = "/pub/outside/optpdwh/ajaxstatistics";
  }
  $.ajax({
    url : URI,
    type : 'post',
    dataType : 'json',
    data : {"des3PdwhPubId" : encodeURIComponent(des3PubId)},
    success : function(data) {
      if (data.result == "success") {
        if (data.awardCount > 0 && data.awardCount <= 999) {
          $(".span_award").text($(".span_award").text() + " (" + data.awardCount + ")");
        }
        if (data.awardCount > 999) {
          $(".span_award").text($(".span_award").text() + " (1k+)");
        }
        if (data.commentCount > 0 && data.commentCount <= 999) {
          $(".span_comment").text("评论 (" + data.commentCount + ")");
          $("#pdwhCommentCount").val(data.commentCount);
        }
        if (data.commentCount > 999) {
          $(".span_comment").text("评论 (1k+)");
          $("#pdwhCommentCount").val(data.commentCount);
        }
        if (data.shareCount > 0 && data.shareCount <= 999) {
          $(".span_share").text("分享 (" + data.shareCount + ")");
        }
        if (data.shareCount > 999) {
          $(".span_share").text("分享 (1k+)");
        }
      }
    },
    error : function(data) {

    }
  });
}

// 基准库成果-导入到我的成果库
mobile.pub.importPdwhPub = function(callbackData) {
  // if ($('#dev_imp_pdwh').val() == "dup") {
  // scmpublictoast(pubdetails.confirm_dup,2000);
  // return;
  // }
  var params = [];
  params.push({
    "pubId" : callbackData.pubId
  });
  var postData = {
    "pubJsonParams" : JSON.stringify(params),
    "articleType" : "1"
  };
  $.ajax({
    url : "/pub/optpdwh/ajaximport",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
        if (data != null && data.result) {
          if (data.result == "success") {
            // $('#dev_imp_pdwh').val("dup");
            scmpublictoast("导入成功", 2000);
          } else if (data.result == "dup") {
            scmpublictoast("该成果已加入你的成果库中", 2000);
          } else {
            scmpublictoast("导入失败", 2000);
          }
        } else {
          scmpublictoast("导入失败", 2000);
        }
      });
    },
    error : function() {
      scmpublictoast("导入失败", 2000);
    }
  });
};

/**
 * 成果请求全文
 * 
 * @author houchuanjie
 */
mobile.pub.requestPubFullText = function(des3PubId, pubType, des3ReceivePsnId) {
  var flag = false;
  if (pubType.toUpperCase() == "PDWH") {
    mobile.pub.pdwhIsExist(des3PubId, function() {
      flag = true;
    })
  } else {
    flag = true;
  }
  // 发出请求
  if (des3PubId && pubType && flag) {
    $.ajax({
      url : "/pub/fulltext/ajaxreqadd",
      type : "post",
      dataType : "json",
      data : {
        'des3PubId' : des3PubId,
        'pubType' : pubType,
        "des3RecvPsnId" : des3ReceivePsnId
      },
      success : function(data) {
        BaseUtils.ajaxTimeOutMobile(data, function() {
          if (data.status == "success") {
            newMobileTip("发送全文请求成功");
          } else {
            newMobileTip("请求发送失败，请稍后再试");
          }
        });
      },
      error : function(data) {
        newMobileTip("请求发送失败，请稍后再试");
      }
    });
  } else {
    newMobileTip("请求参数不正确");
  }
}

// 基准库成果-导入到我的成果库
mobile.pub.importSNSPub = function(callbackData) {
  var params = [];
  params.push({
    "pubId" : callbackData.pubId,
    "ownerId" : callbackData.ownerId
  });
  var postData = {
    "pubJsonParams" : JSON.stringify(params),
    "articleType" : "1"
  };
  $.ajax({
    url : "/pub/optsns/ajaximport",
    type : "post",
    dataType : "json",
    data : postData,
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
        if (data != null && data.result) {
          if (data.result == "success") {
            scmpublictoast("导入成功", 2000);
          } else if (data.result == "dup") {
            scmpublictoast("该成果已加入你的成果库中", 2000);
          } else {
            scmpublictoast("导入失败", 2000);
          }
        } else {
          scmpublictoast("导入失败", 2000);
        }
      });
    },
    error : function() {
      scmpublictoast("导入失败", 2000);
    }
  });
};

// 下载全文
mobile.pub.downloadFTFile = function(url) {
  if (!!url && url != "") {
    Smate.confirm("下载提示", "要下载全文附件吗？", function() {
      window.location.href = url;
    }, ["下载", "取消"]);
  }
};

// 发送全文请求
mobile.pub.requestFTFile = function(des3PubId, pubType, des3ReceivePsnId, flag) {
  // 校验基准库是否存在
  if (pubType && pubType.toLowerCase() == 'pdwh') {
    var falg = false;
    mobile.pub.pdwhIsExist(des3PubId, function() {
      falg = true;
    });
    if (!falg) {
      return;
    }
  }
  var searchPsnId = $("#searchPsnId").val();
  var requestFTFileConfirmTips = "是否向作者请求全文？";
  if (flag == "true") {
    requestFTFileConfirmTips = "全文未公开，是否请求查看？";
  }
  if (des3ReceivePsnId != searchPsnId) {
    if (des3PubId && pubType) {
      Smate.confirm("提示", requestFTFileConfirmTips, function() {
        mobile.pub.requestPubFullText(des3PubId, pubType, des3ReceivePsnId);
      }, ["确定", "取消"]);
    }
  } else {
    mobile.pub.smatemobile();
  }
};

mobile.pub.smatemobile = function() {
  if (document.getElementsByClassName("bckground-cover").length == 0) {
    var innerele = '<div class="background-cover__content">请使用电脑端上传全文</div>';
    var newele = document.createElement("div");
    newele.className = "bckground-cover";
    newele.innerHTML = innerele;
    document.getElementsByTagName("body")[0].appendChild(newele);
    var windheight = newele.offsetHeight;
    var windwidth = newele.offsetWidth;
    var windbottom = (windheight - 48) / 2 + "px";
    var windleft = (windwidth - 240) / 2 + "px";
    document.getElementsByClassName("background-cover__content")[0].style.left = windleft;
    document.getElementsByClassName("background-cover__content")[0].style.bottom = windbottom;
    setTimeout(function() {
      document.getElementsByClassName("background-cover__content")[0].style.bottom = "-64px";
      setTimeout(function() {
        document.getElementsByTagName("body")[0].removeChild(newele);
      }, 500);
    }, 1500);
  }
};

mobile.pub.addSNSPubVisitRecord = function(des3ReadPsnId, des3PubId) {
  $.ajax({
    url : "/pub/outside/ajaxsnsview",
    dataType : "json",
    type : "post",
    data : {
      'des3ReadPsnId' : des3ReadPsnId,
      'des3PubId' : des3PubId,
    },
    success : function(data) {
    },
    error : function(data) {
    },
  });
};
mobile.pub.addPDWHPubVisitRecord = function(des3PubId) {
  $.ajax({
    url : "/pub/outside/ajaxpdwhview",
    dataType : "json",
    type : "post",
    data : {
      'des3PdwhPubId' : des3PubId,
    },
    success : function(data) {
    },
    error : function(data) {
    },
  });
};
mobile.pub.changePubPermission = function(obj) {
  $.ajax({
    url : '/pub/optsns/updatepermission',
    type : 'post',
    dataType : 'json',
    data : {
      "des3PubId" : $(obj).attr("value")
    },
    success : function(data) {
      if (data.result == "success") {
        if ($(obj).hasClass("selected-func_close-open")) {
          $(obj).removeClass("selected-func_close-open");
        } else {
          $(obj).addClass("selected-func_close-open");
        }
      } else {
        scmpublictoast("操作失败", 1000);
      }
    },
    error : function() {
      scmpublictoast("操作失败", 1000);
    }
  });
}
