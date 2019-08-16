/**
 * 动态实时生成
 */
var dynamic = dynamic ? dynamic : {};

dynamic.getTitle = function(type) {
  var imgTitle;
  if (type == 1) {
    imgTitle = "<div class='pub-idx__full-text_newbox-box_load dev_img_title' title='" + dynCommon.loadFullText
        + "'></div>";
  } else if (type == 2) {
    imgTitle = "<div class='pub-idx__full-text_newbox-box dev_img_title' title='" + dynCommon.upFullText + "'></div>";
  } else {
    imgTitle = "<div class='pub-idx__full-text_newbox-box dev_img_title' title='" + dynCommon.reqFullText + "'></div>";
  }
  return imgTitle;
}
// 动态中的收藏、取消收藏操作
dynamic.dynCollectCoperation = function(obj, des3ResId, type, myFunction) {
  $.ajax({
    url : "/prjweb/fund/ajaxcollect",
    type : "post",
    data : {
      "encryptedFundId" : des3ResId,
      "collectOperate" : type
    },
    dataType : "json",
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (typeof myFunction == "function") {
            myFunction();
          }
        }
      });
    }
  });
}
/**
 * 请求全文 requestPubType=sns/pdwh resOwnerDes3Id=资源拥有者id des3ResId=资源id
 */
dynamic.requestFullText = function(des3ResId, requestPubType, OwnerDes3Id, myFunction) {
  $.ajax({
    url : '/pub/fulltext/ajaxreqadd',
    type : 'post',
    data : {
      'des3RecvPsnId' : OwnerDes3Id,
      'des3PubId' : des3ResId,
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
 * sns下载全文 OwnerDes3Id=跳转请求用，下载可不要
 */
dynamic.downloadFullText = function(des3ResId) {
  $.ajax({
    url : "/fileweb/download/ajaxgeturl",
    type : "post",
    dataType : "json",
    data : {
      "des3Id" : des3ResId,
      "type" : 2
    },
    async : false,
    success : function(result) {
      if (result && result.status == "success") {
        window.location.href = result.data.url;
      } else {
        scmpublictoast(dynCommon.noDownloadPrivileges, 2000, 3);
      }
    }
  });
}
/**
 * pdwh下载全文
 */
dynamic.downloadPdwhFullText = function(des3ResId) {
  $.ajax({
    url : "/fileweb/download/ajaxgeturl",
    type : "post",
    data : {
      "des3Id" : des3ResId,
      "type" : 3
    },
    dataType : "json",
    success : function(result) {
      if (result && result.status == "success") {
        window.location.href = result.data.url;
      }
    }
  });
}
/**
 * 快速分享
 */
dynamic.quickShare = function(des3DynId, dynType, des3ResId, resType, content, myFunction) {
  var nextDnyType = dynamic.nextDynType(dynType);
  dataJson = {
    "dynType" : nextDnyType,
    "resType" : Number(resType),
    "des3ResId" : des3ResId,
    "des3DynId" : des3DynId,
    "operatorType" : 3,
    "dynText" : content
  }
  $.ajax({
    url : '/dynweb/dynamic/ajaxquickshare',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (typeof myFunction == "function") {
            myFunction();
          }
        }
      });
    }
  });
}
// 收藏成果 pubDb:"PDWH","SNS"
// collectedPubBack回调函数
dynamic.dealCollectedPub = function(pubId, pubDb, collected, collectedPubBack) {
  var url;
  // 判断是否为手机端
  if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
    switch (pubDb) {
      case "SNS" :// sns个人库成果
        $.ajax({
          url : "/pub/optsns/ajaxcollect",
          type : 'post',
          data : {
            "des3PubId" : pubId,
            "pubDb" : pubDb,
            "collectOperate" : collected
          },
          dateType : 'json',
          success : function(data) {
            BaseUtils.ajaxTimeOut(data, function() {
              if (data && data.result == "success") {
                if (typeof collectedPubBack == "function") {
                  collectedPubBack();
                } else {
                  scmpublictoast(dynCommon.optSuccess, 1000, 1);
                }
              } else if (data && data.result == "exist") {
                scmpublictoast(dynCommon.pubIsSaved, 1000, 3);
              } else if (data && data.result == "isDel") {
                scmpublictoast(dynCommon.pubIsDeleted, 1000, 3);
              } else {
                scmpublictoast(dynCommon.optError, 1000, 2);
              }
            });
          },
          error : function(data) {
            scmpublictoast(dynCommon.optError, 1000, 2);
          }
        });
      break;
      case "PDWH" :// 基准库成果
        $.ajax({
          url : "/pub/optpdwh/ajaxcollect",
          type : 'post',
          data : {
            "des3PubId" : pubId,
            "pubDb" : pubDb,
            "collectOperate" : collected
          },
          dateType : 'json',
          success : function(data) {
            BaseUtils.ajaxTimeOut(data, function() {
              if (data && data.result == "success") {
                if (typeof collectedPubBack == "function") {
                  collectedPubBack();
                } else {
                  scmpublictoast(dynCommon.optSuccess, 1000, 1);
                }
              } else if (data && data.result == "exist") {
                scmpublictoast(dynCommon.pubIsSaved, 1000, 3);
              } else if (data && data.result == "isDel") {
                scmpublictoast(dynCommon.pubIsDeleted, 1000, 3);
              } else {
                scmpublictoast(dynCommon.optError, 1000, 2);
              }
            });
          },
          error : function(data) {
            scmpublictoast(dynCommon.optError, 1000, 2);
          }
        });
      break;
      default :
      break;
    }
  } else {
    $.ajax({
      url : "/pub/opt/ajaxCollect",
      type : 'post',
      data : {
        "des3PubId" : pubId,
        "pubDb" : pubDb,
        "collectOperate" : collected
      },
      dateType : 'json',
      success : function(data) {
        BaseUtils.ajaxTimeOut(data, function() {
          if (data && data.result == "success") {
            if (typeof collectedPubBack == "function") {
              collectedPubBack();
            } else {
              scmpublictoast(dynCommon.optSuccess, 1000, 1);
            }
          } else if (data && data.result == "exist") {
            scmpublictoast(dynCommon.pubIsSaved, 1000, 3);
          } else if (data && data.result == "isDel") {
            scmpublictoast(dynCommon.pubIsDeleted, 1000, 3);
          } else if (data && data.result == "not_exists") {
            scmpublictoast(dynCommon.pubIsDeleted, 1000, 2);
          } else {
            scmpublictoast(dynCommon.optError, 1000, 2);
          }
        });
      },
      error : function(data) {
        scmpublictoast(dynCommon.optError, 1000, 2);
      }
    });
  }
}

/**
 * 收藏个人库成果
 */
dynamic.CollectSnSPub = function(des3PubId, myFunction) {
  /*
   * var params=[]; params.push({"des3PubId":des3PubId}); var
   * postData={"jsonParams":JSON.stringify(params),"articleType":"2"};
   */
  $.ajax({
    url : '/pubweb/ajaxsavePaper',
    type : 'post',
    data : {
      "des3PubId" : des3PubId,
      "pubDb" : "SNS"
    },
    dataType : "json",
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (typeof myFunction == "function") {
            myFunction();
          }
        } else if (data.result == "exist") {
          scmpublictoast(dynCommon.pubIsSaved, 1000, 3);
        } else if (data.result == "isDel") {
          scmpublictoast(dynCommon.pubIsDeleted, 1000, 3);
        } else {
          scmpublictoast(dynCommon.CollectionFail, 1000, 2);
        }
      });
    }

  });
}
/**
 * 收藏基准库成果
 */
dynamic.CollectPdwhPub = function(des3PubId, myFunction) {
  /*
   * var params=[]; params.push({"des3PubId":des3PubId}); var
   * postData={"jsonParams":JSON.stringify(params),"publicationArticleType":"2"};
   */
  $.ajax({
    url : "/pubweb/ajaxsavePaper",
    type : "post",
    dataType : "json",
    data : {
      "des3PubId" : des3PubId,
      "pubDb" : "PDWH"
    },
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (typeof myFunction == "function") {
            myFunction();
          }
        } else if (data.result == "exist") {
          scmpublictoast(dynCommon.pubIsSaved, 1000, 3);
        } else if (data.result == "isDel") {
          scmpublictoast(dynCommon.pubIsDeleted, 1000, 3);
        } else {
          scmpublictoast(dynCommon.CollectionFail, 1000, 2);
        }
      });
    }
  });
}
// 删除成果资源
dynamic.delPub = function() {
  $("#des3ResId").val("");
  $("#dynResType").val(0);
  $("#paper").hide();
  if ($("#dyntextarea").val().length == 0) {
    dynamic.dontAllowPublish($("#dynrealtime"));
  }
}
// 允许发布动态
dynamic.allowPublish = function(objQuery) {
  objQuery.attr("onclick", "dynamic.realtime();");
  objQuery.removeClass("share_no");
  objQuery.addClass("share");
}
// 不允许发布动态
dynamic.dontAllowPublish = function(objQuery) {
  objQuery.removeAttr("onclick");
  objQuery.addClass("share_no");
  objQuery.removeClass("share");
}

dynamic.ajaxrealtime = function(dataJson, callBack) {
  $.ajax({
    url : '/dynweb/dynamic/ajaxrealtime',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      if (data.result == "success") {
        if (typeof (callBack) == "function") {
          callBack();
        }
      } else {
        alert(dynCommon.shareFail);
      }
    }
  });
}
// 快速分享
dynamic.quickShareDyn = function(resType, resId, dynType, dynId, parentDynId, databaseType, event) {
  var nextDnyType = dynamic.nextDynType(dynType);
  dataJson = {
    "dynType" : nextDnyType,
    "resType" : Number(resType),
    "resId" : Number(resId),
    "dynId" : dynId,
    "parentDynId" : parentDynId,
    "operatorType" : 3,
    "databaseType" : databaseType
  }
  $.ajax({
    url : '/dynweb/dynamic/ajaxquickshare',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(dynCommon.shareSuccess, 1000, 1);
          setTimeout(dynamic.openDynList, 1000);
        } else {
          scmpublictoast(dynCommon.shareFail, 2000, 2);
        }
      });

    },
    error : function() {
      scmpublictoast(dynCommon.shareFail, 2000, 2);
    }
  });
}

/**
 * 动态评论，当输入的内容为空是，提交按钮变灰，不继续进行
 */
dynamic.submitComment = function(resType, des3ResId, dynType, des3DynId, commnetContent, platform, dynId) {
  var commnetContent = $.trim($("#comment_content").val());
  var dynamicType = dynamic.nextDynType(dynType);
  datas = {
    "des3DynId" : des3DynId,
    "des3ResId" : des3ResId,
    "resType" : resType,
    "dynType" : dynType,
    "nextDynType" : dynamicType,
    "replyContent" : commnetContent,
    "operatorType" : 1,
    "platform" : platform
  }
  $.ajax({
    url : '/dynweb/dynamic/ajaxreplydyn',
    type : 'post',
    data : datas,
    success : function(data) {
      if (data.result == "success") {
        $(".wdful_comments").show();// 显示"精彩评论"
        var des3ReplyerId = data.dynReplayInfo["des3ReplyerId"];
        var psnImage = data.dynReplayInfo["replyerAvatar"];
        var buildDate = data.dynReplayInfo["rebuildDate"];
        var replyerName = data.dynReplayInfo["replyerName"];
        if (typeof $("#locale") != 'undefined' && "en_US" == $("#locale").val()) {
          replyerName = data.dynReplayInfo["replyerEnName"];
        }
        var psnInsInfo = data.dynReplayInfo["psnInsInfo"];
        var replyContent = data.dynReplayInfo["replyContent"];
        var replyHtml = "<div onclick=\"dynamic.openPsnDetail('" + des3ReplyerId
            + "',event)\" class=\"dynamic_one\"><a  class=\"dynamic_head\"><img src=\"" + psnImage
            + "\"></a><p><span class=\"fr\">" + buildDate + "</span><em>" + replyerName + "</em></p><p class=\"p2\">"
            + replyContent + "</p></div>";
        var replySizeArray = $(".dynamic_one");
        if (replySizeArray.length < 1) {
          $("#pubview_discuss_list").html(replyHtml);
        } else {
          // $(".dynamic_one:last").after(replyHtml);//多于1条评论加载在最后一条评论后面
          $("#pubview_discuss_list").append(replyHtml);
        }
        // 评论数加1
        var id = ".commentcount_" + dynId;
        var html = $(id).find("span").text();
        if (html == "") {
          $(id).find("span").text(1);
        } else {
          var newAwardCount = Number(html) + 1;
          $(id).find("span").text(newAwardCount);
        }
        // 隐藏"查看更早10条评论"
        if (replySizeArray.length > 10) {
          $("#moreComment").show();
        }
      }
      $("#comment_content").val("");
      $("#scm_send_comment_btn").addClass("not_point");
      window.scrollTo(0, document.body.scrollHeight);// 把滚动条置底

    },
    error : function() {
      return false;
    }
  });

}

/**
 * 加载评论信息
 */
dynamic.ajaxLoadComments = function(pageNumber) {
  var dynId = $("input[name='dynId']").val();
  var resType = $("input[name='dynId']").attr("resType");
  $.ajax({
    url : "/dynweb/dynamic/ajaxdynreply",
    type : "post",
    dataType : "html",
    data : {
      "dynId" : dynId,
      "resType" : resType,
      "pageNumber" : pageNumber,
      "pageSize" : 10
    },
    success : function(data) {
      $("#pubview_discuss_list").prepend(data);
      /* var commentNum = $(".commentTotal").html(); */
      var commentNum = $("#totalCount").val();
      var replySizeArray = document.getElementsByName("dynReplySize");
      if (replySizeArray.length > 0) {
        $(".wdful_comments").show();
        if (replySizeArray.length < parseInt(commentNum)) {
          $("#moreComment").show();
        } else {
          $("#moreComment").hide();
        }
      } else {
        /* $(".m-bottom").hide();SCM-9371 */
        $(".wdful_comments").hide();
      }
    },
    error : function() {
      return false;
    }
  });
};

/**
 * 获取下一个模板
 */
dynamic.nextDynType = function(dynType) {
  var nextDnyType;
  switch (dynType) {
    case "ATEMP" :
      nextDnyType = "B1TEMP";
    break;
    case "B1TEMP" :
      nextDnyType = "B1TEMP";
    break;
    case "B2TEMP" :
      nextDnyType = "B2TEMP";
    break;
    case "B3TEMP" :
      nextDnyType = "B2TEMP";
    break;
    case "CTEMP" :
      nextDnyType = "DTEMP";
    break;
    case "DTEMP" :
      nextDnyType = "DTEMP";
    break;
  }
  return nextDnyType;
}

/**
 * 抽出公共的部分
 */
dynamic.quickAwardDyn = function(resType, des3ResId, dynType, des3DynId, des3ParentDynId, event) {
  var $this = $(event.currentTarget);
  var nextDynType = dynamic.nextDynType(dynType);
  // 点赞操作
  $.ajax({
    url : "/dynweb/dynamic/ajaxawarddyn",
    type : "post",
    data : {
      "resType" : Number(resType),
      "des3ResId" : des3ResId,
      "des3DynId" : des3DynId,
      "des3ParentDynId" : des3ParentDynId,
      "nextDynType" : nextDynType,
      "dynType" : dynType,
      "operatorType" : 2
    },
    dataType : "json",
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data != null && data.result == "success") {
          var html = $this.find("a").html();
          var newAwardCount = data.awardTimes;
          if (data.action == 1) {// 已赞
            var count = newAwardCount > 999 ? '1k+' : newAwardCount;
            $this.find("a").html(dynCommon.unlike + "(" + count + ")");
            if ($this.find("i").hasClass("paper_footer-fabulous")) {
              $this.find("i").removeClass("paper_footer-fabulous");
              $this.find("i").addClass("paper_footer-award_unlike");
            }
          } else {// 已取消赞
            if (newAwardCount == 0) {
              $this.find("a").html(dynCommon.like);
            } else {
              var count = newAwardCount > 999 ? '1k+' : newAwardCount;
              $this.find("a").html(dynCommon.like + "(" + count + ")");
            }
            if ($this.find("i").hasClass("paper_footer-award_unlike")) {
              $this.find("i").removeClass("paper_footer-award_unlike");
              $this.find("i").addClass("paper_footer-fabulous");
            }
          }
        } else if (data != null && data.result == "resNotExist") {
          scmpublictoast(dynCommon.pubIsDeleted, 2000, 3);
        }
      });
    },
    error : function(e) {
    }
  });
}
/**
 * 动态，点赞 ,取消赞 resType:资源类型 resId：资源id dynType： 当前动态模板类型 A 类型B1 \B2\ C \ D dynId：动态Id
 */
dynamic.awardDyn = function(resType, des3ResId, dynType, des3DynId, des3ParentDynId, event) {
  if (resType == "11" || resType == "25") {
    if (dynamic.checkFundAgency(des3ResId, resType)) {
      dynamic.quickAwardDyn(resType, des3ResId, dynType, des3DynId, des3ParentDynId, event);
    }
  } else {
    if (resType == "22") {
      dynamic.pdwhIsExist(des3ResId, function() {
        dynamic.quickAwardDyn(resType, des3ResId, dynType, des3DynId, des3ParentDynId, event);
      });
    } else {
      dynamic.quickAwardDyn(resType, des3ResId, dynType, des3DynId, des3ParentDynId, event);
    }
  }
  dynamic.stopNextEvent(event);
}

// 判断是否为移动端
function is_mobile() {
  var regex_match = /(nokia|iphone|android|motorola|^mot-|softbank|foma|docomo|kddi|up.browser|up.link|htc|dopod|blazer|netfront|helio|hosin|huawei|novarra|CoolPad|webos|techfaith|palmsource|blackberry|alcatel|amoi|ktouch|nexian|samsung|^sam-|s[cg]h|^lge|ericsson|philips|sagem|wellcom|bunjalloo|maui|symbian|smartphone|midp|wap|phone|windows ce|iemobile|^spice|^bird|^zte-|longcos|pantech|gionee|^sie-|portalmmm|jigs browser|hiptop|^benq|haier|^lct|operas*mobi|opera*mini|320x320|240x320|176x220)/i;
  var u = navigator.userAgent;
  if (null == u) {
    return true;
  }
  var result = regex_match.exec(u);
  if (null == result) {
    return false
  } else {
    return true
  }
}

// 检查基准库成果是否存在
dynamic.pdwhIsExist = function(des3PubId, func) {
  var reqUrl = "/pub/opt/ajaxpdwhisexists";
  var data = {
    "des3PubId" : des3PubId
  };
  if (is_mobile()) {
    reqUrl = "/pub/optpdwh/ajaxpdwhisexists";
    data = {
      "des3PdwhPubId" : des3PubId
    };
  }
  $.ajax({
    url : reqUrl,
    type : 'post',
    dataType : "json",
    async : false,
    data : data,
    timeout : 10000,
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          func();
        } else {
          scmpublictoast(dynCommon.pubIsDeleted, 1000);
        }
      });
    },
    error : function(data) {
      scmpublictoast("系统出错", 1000);
    }
  });
}

/**
 * 检查数据库中基金资助机构是否被删除
 */
dynamic.checkFundAgency = function(des3ResId, resType) {
  var a = false;
  $.ajax({
    url : '/prjweb/ajaxCheckPub',
    type : 'post',
    async : false,
    data : {
      "Des3FundAgencyId" : des3ResId,
      "resType" : resType
    },
    success : function(data) {
      if (data.status == "fundIsDel") {
        newMobileTip("资源已删除");
      } else if (data.status == "fundAgencyIsDel") {
        newMobileTip("资源已删除");
      } else {
        a = true;
      }
    }
  });
  return a;
}

dynamic.awardPdwhDyn = function(resType, resId, dynType, dynId, parentDynId, event) {
  // $(".dev_awardcount_"+dynId).find(".material-icons").attr("style","background:#2196f3;
  // color:#ffffff; box-shadow:0 2px 5px 0 rgba(0,0,0,0.26)");
  var nextDnyType = dynamic.nextDynType(dynType);
  var id = ".dev_awardcount_" + dynId;
  var html = $(id).find("a").html();
  var isAward;
  if (html.indexOf(dynCommon.unlike) != -1) {
    isAward = 1;
  } else {
    isAward = 0;
  }
  // 点赞操作
  $.ajax({
    url : "/pubweb/details/ajaxpdwhaward",
    type : "post",
    data : {
      "pubId" : resId,
      "isAward" : isAward
    },
    dataType : "json",
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result == "success" && data.action == 1) {
          var id = ".dev_awardcount_" + dynId;
          var html = $(id).find("a").html();
          if (dynCommon.like == html) {
            $(id).find("a").html(dynCommon.unlike + "(1)");
          } else {
            var count = html.replace(/\D+/g, "");
            var newAwardCount = Number(count) + 1;
            $(id).find("a").html(dynCommon.unlike + "(" + newAwardCount + ")");
          }
        } else if (data.action == 0) {
          var id = ".dev_awardcount_" + dynId;
          var html = $(id).find("a").html();
          var count = html.replace(/\D+/g, "");
          var newAwardCount = Number(count) - 1;
          if (newAwardCount <= 0) {
            $(id).find("a").html(dynCommon.like);
          } else {
            $(id).find("a").html(dynCommon.like + "(" + newAwardCount + ")");
          }
        }

      });
    },
    error : function(e) {
    }
  });
  dynamic.stopNextEvent(event);
}

dynamic.emptyclick = function(event) {
  dynamic.stopNextEvent(event);
}
/**
 * 加载成果信息
 */
dynamic.getpubinfo = function(des3PubId) {
  $("#paper").show();
  $.ajax({
    url : "/pub/query/single",
    type : "post",
    data : {
      "des3SearchPubId" : des3PubId,
      "pubDB" : "SNS"
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "success" && data.pubListVO != null) {
        var resultList = data.pubListVO.resultList;
        if (resultList != null && resultList.length > 0) {
          var pubInfo = resultList[0];
          // 得到成果参数
          $("#pubtitle").html(pubInfo.title == null ? "" : pubInfo.title);
          $("#author").html(pubInfo.authorNames == null ? "" : pubInfo.authorNames);
          $("#journal").text(pubInfo.briefDesc == null ? "" : pubInfo.briefDesc);
          $("#dynResType").val(1);
          if ("" != pubInfo.publishYear && pubInfo.publishYear != null) {
            $("#journal").after("<span class=\"fccc\">| " + pubInfo.publishYear + "</span>")
          }
          $("#paper").show();
          if (pubInfo.hasFulltext == 1) {
            if (pubInfo.fullTextImgUrl != null && pubInfo.fullTextImgUrl != "") {
              $("#paper").find("img").attr("src", pubInfo.fullTextImgUrl);
            } else {
              $("#paper").find("img").attr("src", "/resmod/images_v5/images2016/file_img1.jpg");
            }
          } else {
            $("#paper").find("img").attr("src", "/resmod/images_v5/images2016/file_img.jpg");
          }
        }
      }
    },
    error : function() {
      // 选择成果出错
    }
  });
};

// 停止进一步的事件触发
dynamic.stopNextEvent = function(evt) {
  if (evt && evt.currentTarget) {
    if (evt.stopPropagation) {
      evt.stopPropagation();
    } else {
      evt.cancelBubble = true;
    }
  }
}
/**
 * @author houchuanjie 匹配动态中的网址，返回Match数组
 */
dynamic.matchUrl = function(str) {
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

// 动态中的收藏、取消收藏操作
dynamic.dynInterestAgency = function(obj, des3ResId, type, myFunction) {
  $.ajax({
    url : "/prjweb/agency/ajaxinterest",
    type : "post",
    data : {
      "Des3FundAgencyId" : des3ResId,
      "optType" : type == 1 ? 0 : 1
    },
    dataType : "json",
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (typeof myFunction == "function") {
            myFunction();
          }
        } else {
          if (data.errorMsg == "interest agency has reached the maximum") {
            scmpublictoast(dynCommon.addFundSizeFail, 2000, 3);
          } else if (data.errorMsg == "interested in at least one funding agency") {
            scmpublictoast(dynCommon.addFundEmptyFail, 2000, 3);
          }
        }
      });
    }
  });
}
// 全文请求提示框
dynamic.requestFTFile = function(des3PubId, pubType, resOwnerDes3Id) {
  if (des3PubId && pubType) {
    Smate.confirm("提示", "要发送全文请求吗？", function() {
      dynamic.requestFullText(des3PubId, pubType, resOwnerDes3Id, function() {
        newMobileTip(dynCommon.requestFullTextSuccess);
      })
    }, ["确定", "取消"]);
  }
}

// 未上传全文请求提示框
dynamic.requestAuthorFTFile = function(des3PubId, pubType, resOwnerDes3Id) {
  if (des3PubId && pubType) {
    Smate.confirm("提示", "是否向作者请求全文？", function() {
      dynamic.requestFullText(des3PubId, pubType, resOwnerDes3Id, function() {
        newMobileTip(dynCommon.requestFullTextSuccess);
      })
    }, ["确定", "取消"]);
  }
}

// 隐私全文请求提示框
dynamic.requestSeeFTFile = function(des3PubId, pubType, resOwnerDes3Id) {
  if (des3PubId && pubType) {
    Smate.confirm("提示", "全文未公开，是否请求查看？", function() {
      dynamic.requestFullText(des3PubId, pubType, resOwnerDes3Id, function() {
        newMobileTip(dynCommon.requestFullTextSuccess);
      })
    }, ["确定", "取消"]);
  }
}

// 全文下载提示框
dynamic.downloadFTFile = function(des3ResId, pubType) {
  if (des3ResId != "") {
    Smate.confirm("下载提示", "要下载全文附件吗？", function() {
      if (pubType == 'sns') {
        dynamic.downloadFullText(des3ResId);
      } else {
        dynamic.downloadPdwhFullText(des3ResId);
      }
    }, ["下载", "取消"]);
  }
};