//pc
var dynamic = dynamic ? dynamic : {};
/**
 * 动态打开详情
 */
dynamic.openDetail = function(des3DynId, dynType, des3ResId, resType, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  var $this = $(ev.currentTarget);
  switch (resType) {
    case "1" :
      dynamic.openNotNextPubDetail(des3ResId, ev);
    break;
    case "11" :
      dynamic.openNotNextFundDetail(des3ResId, ev);
    break;
    case "22" :
    case "24" :
      var $inputs = $this.closest(".main-list__item").find("input[name=dynId]");
      var dbId = $inputs.eq($inputs.length - 1).attr("dbId");
      dynamic.openNotNextPdwhDetail(des3ResId, dbId, ev);
    break;
    case "4" :
      dynamic.openPrjDetail(des3ResId, ev);
    break;
    case "25" :
      dynamic.openAgencyDetail(des3ResId, ev);
    break;
    case "26" :
      dynamic.openNewsDetail(des3ResId, ev);
    break;
    default :
    break;
  }
}
/**
 * 全文下载或全文请求
 */
dynamic.fullTextEvent = function(des3DynId, dynType, des3ResId, resType, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  var $this = $(ev.currentTarget);
  var $inputs = $this.closest(".main-list__item").find("input[name='dynId']");
  var resOwnerDes3Id = $inputs.eq($inputs.length - 1).attr("resOwnerDes3Id");
  var des3FileId = $this.closest(".dev_pub_fulltext").attr("des3FileId");
  var permission = $this.closest(".dev_pub_fulltext").attr("permission");
  var userId = $("#userId").val();
  switch (resType) {
    case "1" :
    case "2" :// sns库的成果处理
      // SCM-24197 yhx 2019/3/28
      var exist = dynamic.snsPubIsExist(des3ResId);
      if (!exist) {
        return;
      }
      var des3OwnerId = dynamic.getSnsPubOwner(des3ResId);
      if (des3OwnerId != null) {
        resOwnerDes3Id = des3OwnerId;
      }
      if (des3FileId) {// 全文存在则下载
        if (permission == 0 || userId == resOwnerDes3Id) {
          dynamic.downloadFullText(des3ResId);
        } else {
          dynamic.requestFullText(des3ResId, 'sns', resOwnerDes3Id, function() {
            scmpublictoast(dynCommon.requestFullTextSuccess, 2000);
          });
        }
      } else {
        if (userId == resOwnerDes3Id) {// 自己的成果，则上传全文
          dynamic.uploadFullText(des3ResId);
        } else if (resOwnerDes3Id != "") {// 不是自己的，则全文请求
          dynamic.requestFullText(des3ResId, 'sns', resOwnerDes3Id, function() {
            scmpublictoast(dynCommon.requestFullTextSuccess, 2000);
          });
        }
      }
    break;
    case "11" :// 基金
      dynamic.openNotNextFundDetail(des3ResId, ev);
    break;
    case "22" :// 基准库成果处理
    case "24" :
      dynamic.pdwhIsExist(des3ResId, function() {
        if (des3FileId) {// 全文存在则下载
          dynamic.downloadPdwhFullText(des3ResId);
        } else {// 不存在则请求
          dynamic.requestFullText(des3ResId, 'pdwh', '', function() {
            scmpublictoast(dynCommon.requestFullTextSuccess, 2000);
          })
        }
      });
    break;
    case "4" :// 项目到详情，SCM-20663
      dynamic.openDetail(des3DynId, dynType, des3ResId, resType, ev)
    break;
    case "25" :// 资助机构详情
      dynamic.openDetail(des3DynId, dynType, des3ResId, resType, ev)
    break;
    case "26" :// 新闻详情
      dynamic.openDetail(des3DynId, dynType, des3ResId, resType, ev)
    break;
    default :
    break;
  }
}

dynamic.getSnsPubOwner = function(des3ResId) {
  var des3ownerId = null;
  $.ajax({
    url : '/dynweb/dynamic/ajaxgetpubowner',
    type : 'post',
    async : false,
    data : {
      "des3ResId" : des3ResId
    },
    success : function(data) {
      if (data.result == "success") {
        des3ownerId = data.des3ownerId;
      }
    }
  });
  return des3ownerId;
}

/**
 * 收藏
 */
dynamic.dynCollectPub = function(des3DynId, dynType, des3ResId, resType, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  var $this = $(ev.currentTarget);
  var type = $this.attr("collected")
  switch (resType) {
    case "1" :
    case "2" :// sns个人库成果
      dynamic.dealCollectedPub(des3ResId, 'SNS', type, function() {
        dynamic.dynCollectStyle(type, $this);
      });
    break;
    case "11" :// 基金
      if (dynamic.checkFundAgency(des3ResId, resType)) {
        dynamic.dynCollectCoperation($this, des3ResId, type, function() {
          dynamic.dynCollectStyle(type, $this);
        });
      }
    break;
    case "22" :
    case "24" :// 基准库成果
      dynamic.dealCollectedPub(des3ResId, 'PDWH', type, function() {
        dynamic.dynCollectStyle(type, $this);
      });
    break;
    case "25" : // 资助机构
      if (dynamic.checkFundAgency(des3ResId, resType)) {
        dynamic.dynInterestAgency($this, des3ResId, type, function() {
          $this.attr("resType", "25");
          dynamic.dynCollectStyle(type, $this);
        });
      }
    break;
    default :
    break;
  }
  dynamic.stopNextEvent(ev);
}
/**
 * 收藏后的样式改变
 */
dynamic.dynCollectStyle = function(type, obj) {
  var $this = $(obj);
  var collectWord = dynCommon.collected;
  var unCollectWord = dynCommon.unCollected;
  if ($this.attr("resType") == "25") {
    collectWord = dynCommon.interest;
    unCollectWord = dynCommon.cancelInterest;
  }
  if (type == "1") {
    $this.attr("collected", "0");
    $this.find("a").html(collectWord);
    scmpublictoast(dynCommon.optSuccess, 2000);
  } else {
    $this.attr("collected", "1");
    $this.find("a").html(unCollectWord);
    scmpublictoast(dynCommon.optSuccess, 2000);
  }
}
/**
 * 引用
 */
dynamic.dynCitePub = function(des3DynId, dynType, des3ResId, resType, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  var $this = $(ev.currentTarget);
  var url;
  switch (resType) {
    case "1" :// sns个人库成果
      Pub.showPubQuote('/pub/ajaxpubquote', des3ResId, this);
      // url="/pubweb/publication/ajaxpubquote?des3Id="+des3ResId+"&TB_iframe=true&height=350&width=750";
    break;
    case "22" :
    case "24" :// 基准库成果
      Pub.showPdwhQuote('/pub/ajaxpdwhpubquote', des3ResId, this);
      // url="/pubweb/publication/ajaxpdwhpubquote?des3Id="+des3ResId+"&TB_iframe=true&height=350&width=750";
    break;
    default :
    break;
  }
  // $this.closest(".main-list__list").find(".dyn_thickbox").eq(0).attr("href",url).click();
  dynamic.stopNextEvent(ev);
}
/**
 * 首页动态分享
 */
dynamic.shareDynMain = function(des3DynId, dynType, des3ResId, resType, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  var $this = $(ev.currentTarget);
  if (dynType == "ATEMP" || dynType == "B1TEMP") {
    // 需求变更-删除快速分享
    if (resType == "1" || resType == "2") {
      dynamic.checkPub($this, des3ResId);
    } else if (resType == "11" || resType == "25") {
      if (dynamic.checkFundAgency(des3ResId, resType)) {
        dynamic.quickShareInit($this);
      }
    } else {
      if (resType == "22") {
        dynamic.pdwhIsExist(des3ResId, function() {
          dynamic.quickShareInit($this);
        });
      } else if (resType == "4") {
        if (!dynamic.checkProjectExist(des3ResId)) {
          dynamic.quickShareInit($this);
        }
      } else if (resType == "26") {
        if (!dynamic.checkNewsExist(des3ResId)) {
          dynamic.quickShareInit($this);
        }
      } else {
        dynamic.quickShareInit($this);
      }
    }
    if (resType == "0") {
      $("#share_site_div_id").find(".share_plugin_section:first").click();
      var obj_lis = $("#share_to_scm_box").find("li");
      obj_lis.eq(1).hide();
      obj_lis.eq(2).hide();
    }
    /*
     * dynamic.quickShare(des3DynId,dynType,des3ResId,resType,function(){
     * scmpublictoast(dynCommon.shareSuccess,1000); setTimeout(dynamic.openDynList, 1000); });
     */
  } else if (dynType == "B2TEMP" || dynType == "B3TEMP") {
    if (resType == "1" || resType == "2") {
      dynamic.checkPub($this, des3ResId);
    } else if (resType == "11" || resType == "25") {
      if (dynamic.checkFundAgency(des3ResId, resType)) {
        dynamic.quickShareInit($this);
      }
    } else {
      if (resType == "22") {
        dynamic.pdwhIsExist(des3ResId, function() {
          dynamic.quickShareInit($this);
        });
      } else if (resType == "4") {
        if (!dynamic.checkProjectExist(des3ResId)) {
          dynamic.quickShareInit($this);
        }
      } else if (resType == "26") {
        if (!dynamic.checkNewsExist(des3ResId)) {
          dynamic.quickShareInit($this);
        }
      } else {
        dynamic.quickShareInit($this);
      }
    }
  }
  dynamic.stopNextEvent(ev);
}
dynamic.quickShareInit = function($this) {
  SmateShare.getParamForQuickShare($this[0]);
  initSharePlugin($this[0]);
}
// 检查基准库成果是否存在
dynamic.pdwhIsExist = function(des3PubId, func) {
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
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          func();
        } else {
          scmpublictoast(dynCommon.pubIsDeleted, 1000);
        }
      });
    },
    error : function(data) {
      scmpublictoast(dynCommon.optError, 1000);
    }
  });
}

// 检查成果是否存在
dynamic.snsPubIsExist = function(des3PubId) {
  var exist = true;
  $.ajax({
    url : "/pub/ajaxCheckPub",
    type : 'post',
    dataType : "json",
    async : false,
    data : {
      "des3PubId" : des3PubId
    },
    timeout : 10000,
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.status == 'isDel') {
          exist = false;
          scmpublictoast(dynCommon.pubIsDeleted, 1000);
        }
      });
    },
    error : function(data) {
      scmpublictoast(dynCommon.optError, 1000);
    }
  });
  return exist;
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
        scmpublictoast(dynCommon.fundIsDel, 2000);
      } else if (data.status == "fundAgencyIsDel") {
        scmpublictoast(dynCommon.fundAgencyIsDel, 2000);
      } else {
        a = true;
      }
    }
  });
  return a;
}

/**
 * 检查数据库项目是否被删除
 */
dynamic.checkProjectExist = function(des3ResId) {
  var a = false;
  $.ajax({
    url : '/prjweb/ajaxCheckPjr',
    type : 'post',
    async : false,
    data : {
      "des3PrjId" : des3ResId,
    },
    success : function(data) {
      if (data.status == "true") {
        scmpublictoast(dynCommon.projectIsDel, 2000);
        a = true;
      }
    }
  });
  return a;
}

/**
 * 检查新闻是否被删除
 */
dynamic.checkNewsExist = function(des3ResId) {
  var a = false;
  $.ajax({
    url : '/dynweb/news/ajaxCheckNews',
    type : 'post',
    async : false,
    data : {
      "des3NewsId" : des3ResId,
    },
    success : function(data) {
      if (data.status == 'isDel') {
        a = true;
        scmpublictoast(dynCommon.newsIsDeleted, 1000);
      }
    }
  });
  return a;
}

/**
 * 检查个人库成果是否被删除 以及 是否是隐私成果
 */
dynamic.checkPub = function($this, des3ResId) {
  $.ajax({
    url : '/pub/ajaxCheckPub',
    type : 'post',
    data : {
      "des3PubId" : des3ResId
    },
    dataType : "json",
    success : function(data) {
      if (data.status == "isDel") {
        scmpublictoast(dynCommon.pubIsDeleted, 2000);
      } else if (data.status == "isSelfPrivate") {
        smate.showTips._showNewTips(dynCommon.pubShareContent, dynCommon.tipTitle, "closeDynDiv()", "",
            dynCommon.chooseCloseBtn, "");
        $("#alert_box_cancel_btn").hide();
      } else if (data.status == "isNotSelfPrivate") {
        smate.showTips._showNewTips(dynCommon.pubNotOwenerShareContent, dynCommon.tipTitle, "closeDynDiv()", "",
            dynCommon.chooseCloseBtn, "");
        $("#alert_box_cancel_btn").hide();
      } else {
        dynamic.quickShareInit($this);
      }
    }
  });
}

function closeDynDiv() {
  var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
  if ($(".new-searchplugin_container")) {
    $(".background-cover").remove();
    $(".new-searchplugin_container").remove();
  }
}
/**
 * 获取初始化动态统计信息以及发布时间
 */
dynamic.dynstatistics = function() {
  var temp = "";
  var dynIdArray = [];
  $("div[list-main='psn_dyb_list']").find(".main-list__item").each(function(i, o) {
    var $inputs = $(o).find("input[name='dynId']");
    var $input = $inputs.eq($inputs.length - 1);
    var des3DynId = $input.attr("des3DynId");
    if (des3DynId != null) {
      dynIdArray.push(des3DynId);
    }
  });
  temp = dynIdArray.join(",");
  $.ajax({
    url : '/dynweb/dynamic/ajaxgetdynstatistics',
    type : 'post',
    dataType : 'json',
    data : {
      "dynStatisticsIds" : temp
    },
    success : function(data) {
      if (data.result === "success") {
        var dynStatisMap = data.dynStatisMap;
        for ( var key in dynStatisMap) {
          var $item = $("input[name='dynId'][value='" + key + "']").eq(0).closest(".main-list__item");
          // 遍历动态操作行为（赞、评论、分享）
          var $opts = $item.find(".dynamic-social__list > .dynamic-social__item");
          $.each($opts, function(i, o) {
            var isHasComment = false;
            if (i == 1) {
              var commentOnclick = $(o).attr("onclick");
              if (commentOnclick.indexOf("dynamic.replyDyn") != -1) {
                isHasComment = true;// 有评论按钮
              }
            }
            // 获取统计数
            var count = 0;
            if (i == 0) {
              count = dynStatisMap[key].awardCount;
            } else if (i == 1 && isHasComment == true) {
              count = dynStatisMap[key].commentCount;
            } else if (i == 1 && dynStatisMap[key].viewCount != undefined) {
              count = dynStatisMap[key].viewCount;
            } else if (i == 1 && isHasComment == false) {
              count = dynStatisMap[key].shareCount;
            } else if (i == 2) {
              count = dynStatisMap[key].shareCount;
            }
            // 获取操作并修改统计数
            var ahtml = $(o).find("a").html();
            if (ahtml.indexOf("(") > 0) {
              ahtml = ahtml.substring(0, ahtml.indexOf("("));
            }
            if (count > 0) {
              if (count >= 1000) {
                $(o).find("a").html(ahtml + "(1K+)");
              } else {
                $(o).find("a").html(ahtml + "(" + count + ")");
              }

            } else {
              $(o).find("a").html(ahtml);
            }
          });
          // 设置动态时间
          $("#time_" + key).text(dynStatisMap[key].publishDate);
          if (dynStatisMap[key].resType != "11" && dynStatisMap[key].resType != "4"
              && dynStatisMap[key].resType != "25" && dynStatisMap[key].resType != "26") {// 不是基金就更新全文图片
            // 更新成果全文图片
            var $inputs = $item.find("input[name='dynId']");
            var resOwnerDes3Id = $inputs.eq($inputs.length - 1).attr("resOwnerDes3Id");
            if (dynStatisMap[key].resType == "1" && dynStatisMap[key].resOwnerDes3Id != null) {
              resOwnerDes3Id = dynStatisMap[key].resOwnerDes3Id;
            }
            var userId = $("#userId").val();
            $item.find(".dev_pub_fulltext").find(".dev_img_title").remove();
            var $img = $item.find(".dynamic-content__main").find("img");
            if (!!dynStatisMap[key].des3FileId && dynStatisMap[key].des3FileId != "") {
              $item.find(".dev_pub_fulltext").attr("des3FileId", dynStatisMap[key].des3FileId).attr("permission",
                  dynStatisMap[key].permission);
              if (!!dynStatisMap[key].imgUrl && dynStatisMap[key].imgUrl != "") {
                $img.attr("src", dynStatisMap[key].imgUrl);
              }
              if (dynStatisMap[key].permission == 0 || resOwnerDes3Id == userId) {
                $item.find(".dev_pub_fulltext").find("a").append(dynamic.getTitle(1));
              } else {
                $item.find(".dev_pub_fulltext").find("a").append(dynamic.getTitle(3));
              }
            } else {
              if (userId == resOwnerDes3Id) {
                $item.find(".dev_pub_fulltext").find("a").append(dynamic.getTitle(2));
              } else {
                $item.find(".dev_pub_fulltext").find("a").append(dynamic.getTitle(3));
              }
              $item.find(".dev_pub_fulltext").attr("des3FileId", "");
              $img.attr("src", "/resmod/images_v5/images2016/file_img.jpg");
            }
          }
          if (dynStatisMap[key].resType == "11") {// 更新基金图片
            var $img = $item.find(".dynamic-content__main").find("img");
            if (dynStatisMap[key].imgUrl != "") {
              $img.attr("src", dynStatisMap[key].imgUrl);
            }
            $img.error(function() {// 没有或出错显示默认图片
              $(this).attr('src', "/ressns/images/default/default_fund_logo.jpg");
            })
          }
          if (dynStatisMap[key].resType == "26") {// 新闻图片
            var $img = $item.find(".dynamic-content__main").find("img");
            if (dynStatisMap[key].imgUrl != "") {
              $img.attr("src", dynStatisMap[key].imgUrl);
            }
            $img.error(function() {// 没有或出错显示默认图片
              $(this).attr('src', "/resmod/smate-pc/img/logo_newsdefault.png");
            })
          }
          // 更新基金收藏情况
          if (dynStatisMap[key].collected != null) {
            $item.find(".dev_save").attr("collected", dynStatisMap[key].collected);
            if (dynStatisMap[key].collected == "1") {
              $item.find(".dev_save").find("a").html(dynCommon.unCollected);
            } else {
              $item.find(".dev_save").find("a").html(dynCommon.collected);
            }
          }

          // 初始化资助机构相关信息
          if (dynStatisMap[key].resType == "25") {
            dynamic.initAgencyShareDynInfo(dynStatisMap, $item, key);
          }
        }
      }
    }
  });
};

/**
 * 初始化分享资助机构动态信息
 */
dynamic.initAgencyShareDynInfo = function(dynStatisMap, $item, key) {
  // 更新资助机构图片
  var $img = $item.find(".dynamic-content__main").find("img");
  if (dynStatisMap[key].imgUrl != "") {
    $img.attr("src", dynStatisMap[key].imgUrl);
  } else {
    $img.attr("src", "/resmod/smate-pc/img/logo_instdefault.png");
  }
  $img.error(function() {// 没有或出错显示默认图片
    $(this).attr('src', "/resmod/smate-pc/img/logo_instdefault.png");
  })
  // 关注操作
  if (dynStatisMap[key].collected != null) {
    $item.find(".dev_save").attr("collected", dynStatisMap[key].collected);
    if (dynStatisMap[key].collected == "1") {
      $item.find(".dev_save").find("a").html(dynCommon.cancelInterest);
    } else {
      $item.find(".dev_save").find("a").html(dynCommon.interest);
    }
  }
}

/**
 * 获取初始化动态时候点赞
 */
dynamic.initHasAward = function() {
  var temp = "";
  var dynIdArray = [];
  $("div[list-main='psn_dyb_list']").find(".main-list__item").each(function(i, o) {
    var $inputs = $(o).find("input[name='dynId']");
    var $input = $inputs.eq($inputs.length - 1);
    var des3DynId = $input.attr("des3DynId");
    if (des3DynId != null) {
      dynIdArray.push(des3DynId);
    }
  });
  temp = dynIdArray.join(",");
  $.ajax({
    url : '/dynweb/dynamic/ajaxinithasaward',
    type : 'post',
    async : false,
    dataType : 'json',
    data : {
      "dynResIdResTypes" : temp
    },
    success : function(data) {
      if (data.result = "success") {
        var dynHasAwardMap = data.hasAwardMap
        if (dynHasAwardMap != null) {
          for ( var key in dynHasAwardMap) {
            if (dynHasAwardMap[key] == true) {
              var id = ".dev_awardcount_" + key;
              $(id).find("a").html(dynCommon.unlike);
            }
          }
        }
        dynamic.dynstatistics();
      }
    },
    error : function() {
      return false;
    }
  });
  return;
};

dynamic.realtime = function() {
  $("#moodDynSubmitBtn").disabled();
  var textarea = $.trim($("#share_news").text());
  if (DynMessageUtil.textLengthCount(textarea) > 250) {
    $("#moodDynSubmitBtn").enabled();
    $.scmtips.show("warn", dynamicMsg.lengthWarn);
    $("#share_news").focus();
    return;
  }
  if ((textarea.length == 0 || $("#share_news").hasClass("watermark")) && $("#des3ResId").val() == "") {
    $("#moodDynSubmitBtn").enabled();
    $.scmtips.show("warn", dynamicMsg.typecontent);
    $("#share_news").focus();
    return;
  }

  textarea = textarea.replace(/&lt;(.*)&gt;.*&lt;\/\1&gt;|&lt;(.*) \/&gt;/, "&_lt;$1&_gt;").replace(/</g, "&lt;")
      .replace(/>/g, "&gt;");

  if ($("#share_news").hasClass("watermark")) {
    textarea = "";
  }

  var resType = $("#dynResType").val();
  var des3ResId = $("#des3ResId").val();
  var dynType = "ATEMP";
  var operatorType = -1;
  if ("" == textarea && resType == 1 && des3ResId != "") {
    dynType = "B2TEMP";
    operatorType = 3;
  } else {
    resType = 0;
  }
  var dataJson = {
    "dynType" : dynType,
    "dynText" : textarea,
    "resType" : resType,
    /*
     * "des3ResId":des3ResId, "des3PubId":des3ResId,
     */
    "pubId" : des3ResId,
    "operatorType" : operatorType
  }
  dynamic.ajaxrealtime(dataJson, setTimeout(function() {
    location.href = ctxpath + "/main";
  }, 500));
}
/**
 * 上传全文 pc=跳转成果编辑页面
 */
dynamic.uploadFullText = function(des3ResId) {
  var forwardUrl = "/pub/enter?des3PubId=" + encodeURIComponent(des3ResId);
  if (des3ResId) {
    $.ajax({
      url : '/pub/ajaxCheckPub',
      type : 'post',
      data : {
        "des3PubId" : des3ResId
      },
      dataType : "json",
      success : function(data) {
        if (data.status == "success") {
          BaseUtils.forwardUrlRefer(true, forwardUrl);
        } else if (data.status == "isDel") {
          scmpublictoast(dynCommon.pubIsDeleted, 2000);
        } else {
          BaseUtils.forwardUrlRefer(true, forwardUrl);
        }
      },
      error : function() {
        BaseUtils.forwardUrlRefer(true, forwardUrl);
      }
    });
  }
}
// pc端打开人员主页
dynamic.openPsnDetail = function(des3ProducerPsnId, event) {
  window.open("/psnweb/outside/homepage?des3PsnId=" + des3ProducerPsnId);
  dynamic.stopNextEvent(event);
}
// pc端打开基金详情
dynamic.openNotNextFundDetail = function(des3PubId, event) {
  window.open("/prjweb/funddetails/show?encryptedFundId=" + encodeURIComponent(des3PubId));
  dynamic.stopNextEvent(event);
}
// pc端打开成果详情
dynamic.openNotNextPubDetail = function(des3PubId, event) {
  Pub.newPubDetail(des3PubId);
  dynamic.stopNextEvent(event);
}
// pc端打开项目详情
dynamic.openPrjDetail = function(des3ResId, event) {
  window.open("/prjweb/project/detailsshow?des3PrjId=" + encodeURIComponent(des3ResId));
  dynamic.stopNextEvent(event);
}
// pc端打开资助机构详情
dynamic.openAgencyDetail = function(des3ResId, event) {
  window.open("/prjweb/fundAgency/detailMain?des3FundAgencyId=" + encodeURIComponent(des3ResId));
  dynamic.stopNextEvent(event);
}
// pc端打开新闻详情
dynamic.openNewsDetail = function(des3ResId, event) {
  window.open("/dynweb/news/details?des3NewsId=" + encodeURIComponent(des3ResId));
  dynamic.stopNextEvent(event);
}
dynamic.openNotNextPdwhDetail = function(des3PubId, dbid, event) {
  Pub.newPubPdwhDetail(des3PubId);
  dynamic.stopNextEvent(event);
}
// pc端短地址打开成果详情
dynamic.shortUrlPubDetail = function(url, event) {
  if (url != null) {
    window.open(url)
  }
  dynamic.stopNextEvent(event);
}

// pc端打开别人的成果列表
dynamic.openOtherPubList = function(des3ProducerPsnId, event) {
  location.href = "/psnweb/homepage/show?des3PsnId=" + des3ProducerPsnId + "#publication_box";

  dynamic.stopNextEvent(event);
}

// pc端打开动态详情
dynamic.openDynDetail = function() {

}

// pc端打开动态列表
dynamic.openDynList = function() {
  location.href = "/dynweb/main";
}
// 快速分享
dynamic.shareDyn = function(resType, resId, dynType, dynId, parentDynId, event) {
  $("#dynamicShare").css("display", "flex");
  $("#quickShareOpt").attr(
      "onclick",
      "dynamic.quickShareDyn(" + resType + "," + resId + " ,'" + dynType + "' ," + dynId + "," + parentDynId
          + ",event);");
  dynamic.stopNextEvent(event);
}

dynamic.initDynCollectStatus = function() {
  var fundIds = "";
  $("input.needInit").each(function() {
    var fundId = $(this).val();
    if (fundId != "" && fundIds.indexOf(fundId) == -1) {
      if (fundIds == "") {
        fundIds += fundId;
      } else {
        fundIds += "," + fundId;
      }
    }
  });
  if (fundIds != "") {
    $.ajax({
      url : "/prjweb/fund/initCollectStatus",
      type : "post",
      data : {
        "des3FundIds" : fundIds
      },
      dataType : "json",
      success : function(data) {
        if (data.result == "success") {
          if (data.collectedFundId) {
            var fundIdArr = data.collectedFundId.split(",");
            if (fundIdArr != null && fundIdArr.length > 0) {
              for (var i = 0; i < fundIdArr.length; i++) {
                $(".collect_" + fundIdArr[i]).hide();
                $(".collectCancel_" + fundIdArr[i]).show();
              }
            }
          }
          $("input.needInit").removeClass("needInit");
        } else {
        }
      },
      error : function() {
      }
    });
  }
}

dynamic.ajaxLondDynList = function() {
  window.Mainlist({
    name : "psn_dyb_list",
    listurl : "/dynweb/dynamic/ajaxshow",
    listdata : {},
    method : "scroll",
    listcallback : function(xhr) {
      // dynamic.initDynCollectStatus();
      dynamic.initHasAward();
      dynamic.ajaxLoadSampleDyn();
      dynamic.addDelBynBut();
      dynamic.transUrl();
      var $_ul = $("div[list-main='psn_dyb_list']");
      if ($_ul.find(".main-list__item").length == $_ul.find(".js_listinfo").attr("smate_count")
          && $_ul.find(".response_no-result").length == 0) {
        $_ul.append("<div class='main-list__item global__padding_0'><div class='response_no-result'>"
            + dynCommon.noMoreRecord + "</div></div>");
      }
      var item = $("div[list-main='psn_dyb_list']").find(".main-list__item");
      // var page = 0;
      var ranarr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
      if ($_ul.find(".dyn_pageready_read").length < 10) {
        for (var i = 0; i < item.length; i++) {
          item.eq(i).removeClass("dyn_pageready_unread");
          item.eq(i).addClass("dyn_pageready_read");
          var ranitem = Math.floor(Math.random() * ranarr.length);
          var page = ranarr[ranitem];
          var index = ranarr.indexOf(page);
          if (i == 0) {
            // page++;
            ranarr.splice(index, 1);
            dynamic.randomModule(item.eq(i), page, "before");
          } else if (i != 0 && i % 3 == 0) {
            // page++;
            ranarr.splice(index, 1);
            dynamic.randomModule(item.eq(i - 1), page, "after");
          }
        }
      } else {
        var unread = $("div[list-main='psn_dyb_list']").find(".dyn_pageready_unread");
        for (var i = 0; i < unread.length; i++) {
          unread.eq(i).removeClass("dyn_pageready_unread");
          unread.eq(i).addClass("dyn_pageready_read");
          var ranitem = Math.floor(Math.random() * ranarr.length);
          var page = ranarr[ranitem];
          var index = ranarr.indexOf(page);
          if (i != 0 && i % 3 == 0) {
            // page++;
            ranarr.splice(index, 1);
            dynamic.randomModule(unread.eq(i - 1), page, "after");
          }
        }
      }

      // B1模版添加分享内容后，影响删除动态的样式，在这里做兼容
      $_ul.find(".main-list__item").each(function(i, o) {
        var length = $(o).find("input[name='dynId']").length;
        if (length != 2) {
          $(o).find(".dev_delBtn").removeClass("dyn-publish_content-func");
        } else {
          $(o).find(".dynamic-creator__box").css("position", "relative");
        }
      });
    }
  });
}

// 首页动态-随机显示成果或者基金推荐
dynamic.randomModule = function(position, page, prior) {
  var arr = [1, 2, 3];
  var item = Math.floor(Math.random() * arr.length);
  var r = arr[item];
  if (r == 1) {
    dynamic.pubRecommend(position, page, prior);
  }
  if (r == 2) {
    dynamic.FundRecommend(position, page, prior);
  }
  if (r == 3) {
    dynamic.NewsRecommend(position, page, prior);
  }
}

dynamic.pubRecommend = function(position, page, prior) {
  var ids = "";
  var item = $("div[list-main='psn_dyb_list']").find(".main-list__read-item");
  item.each(function() {
    var des3PubId = $(this).closest(".main-list__read-item").find(".remdPubId").val();
    if (des3PubId != "" && des3PubId != undefined) {
      ids += "," + des3PubId;
    }
  });
  if (ids != "") {
    ids = ids.substring(1);
  };
  $.ajax({
    url : "/dynweb/dynamic/ajaxgetdynpubrecommend",
    type : "post",
    dataType : "html",
    data : {
      "des3PubId" : ids,
      "pageNo" : page
    },
    success : function(data) {
      if (prior == "before") {
        position.before(data);
      } else {
        position.after(data);
      }
    }
  });
};

dynamic.FundRecommend = function(position, page, prior) {
  var ids = "";
  var item = $("div[list-main='psn_dyb_list']").find(".main-list__read-item");
  item.each(function() {
    var des3ResId = $(this).closest(".main-list__read-item").find(".remdFundId").val();
    if (des3ResId != "" && des3ResId != undefined) {
      ids += "," + des3ResId;
    }
  });
  if (ids != "") {
    ids = ids.substring(1);
  };
  $.ajax({
    url : "/prjweb/fund/ajaxfundrecommendshowindyn",
    type : "post",
    dataType : "html",
    data : {
      "des3FundIds" : ids,
      "pageNum" : page
    },
    success : function(data) {
      if (prior == "before") {
        position.before(data);
      } else {
        position.after(data);
      }
    }
  });
};

dynamic.NewsRecommend = function(position, page, prior) {
  var ids = "";
  var item = $("div[list-main='psn_dyb_list']").find(".main-list__read-item");
  item.each(function() {
    var des3ResId = $(this).closest(".main-list__read-item").find(".remdNewsId").val();
    if (des3ResId != "" && des3ResId != undefined) {
      ids += "," + des3ResId;
    }
  });
  if (ids != "") {
    ids = ids.substring(1);
  };
  $.ajax({
    url : "/dynweb/news/ajaxnewsrecommendshowindyn",
    type : "post",
    dataType : "html",
    data : {
      "des3NewsId" : ids,
      "pageNum" : page
    },
    success : function(data) {
      if (prior == "before") {
        position.before(data);
      } else {
        position.after(data);
      }
    }
  });
};
// 查看更多
dynamic.viewMoreRemd = function(type) {
  if (type == 1) {
    BaseUtils.checkCurrentSysTimeout("/pubweb/ajaxtimeout", function() {
      document.location.href = "/pub/applypapermain";
    }, 1);
  } else if (type == 2) {
    BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function() {
      document.location.href = "/prjweb/fund/main";
    }, 1);
  } else {
    BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function() {
      document.location.href = "/dynweb/news/main";
    }, 1);
  }
}
// 查看
dynamic.viewRemdRes = function(des3ResId, type) {
  if (type == 1) {
    Pub.newPubPdwhDetail(des3ResId);
  } else if (type == 2) {
    var newwindow = window.open("about:blank");
    newwindow.location.href = "/prjweb/funddetails/show?encryptedFundId=" + encodeURIComponent(des3ResId);
  } else {
    var newwindow = window.open("about:blank");
    newwindow.location.href = "/dynweb/news/details?des3NewsId=" + encodeURIComponent(des3ResId);
  }
}

dynamic.notViewRemdRes = function(obj, des3ResId, type) {
  if (type == 1) {
    $.ajax({
      url : "/pub/recm/ajaxuninterested",
      type : "post",
      dataType : "json",
      data : {
        "des3PubId" : des3ResId
      },
      success : function(data) {
        BaseUtils.ajaxTimeOut(data, function() {
          if (data.result == "success") {
            $(obj).closest(".main-list__read-item").addClass("setting-list_page-item_hidden");
            // scmpublictoast(dynCommon.optSuccess, 1000);
          } else {
            // scmpublictoast(dynCommon.optError, 1000);
          }
        });
      },
      error : function(data) {
        // scmpublictoast(dynCommon.optError, 1000);
      }
    });
  } else if (type == 2) {
    $.ajax({
      url : "/prjweb/fund/ajaxuninterestedcmd",
      type : "post",
      dataType : "json",
      data : {
        "des3FundId" : des3ResId
      },
      success : function(data) {
        BaseUtils.ajaxTimeOut(data, function() {
          if (data.result == "success") {
            $(obj).closest(".main-list__read-item").addClass("setting-list_page-item_hidden");
            // scmpublictoast(dynCommon.optSuccess,1000);
          } else {
            // scmpublictoast(dynCommon.optError,1000);
          }
        });
      },
      error : function(data) {
        // scmpublictoast(dynCommon.optError,1000);
      }
    });
  } else {
    $.ajax({
      url : "/dynweb/news/ajaxuninterestedcmd",
      type : "post",
      dataType : "json",
      data : {
        "des3NewsId" : des3ResId
      },
      success : function(data) {
        BaseUtils.ajaxTimeOut(data, function() {
          if (data.result == "success") {
            $(obj).closest(".main-list__read-item").addClass("setting-list_page-item_hidden");
          } else {
          }
        });
      },
      error : function(data) {
      }
    });
  }
}

dynamic.remdPdwhFullText = function(des3ResId, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  dynamic.requestFullText(des3ResId, 'pdwh', '', function() {
    scmpublictoast(dynCommon.requestFullTextSuccess, 2000);
  })
}

/**
 * 下载成果全文
 */
dynamic.getfulltextUrlDownload = function(des3PubId) {
  var params = {};
  if (des3PubId != undefined) {
    params.des3PubId = encodeURIComponent(des3PubId);
  }
  $.ajax({
    url : "/pubweb/ajaxgetFulltextUrl",
    type : "post",
    dataType : "json",
    data : params,
    async : false,
    success : function(data) {
      if (data.fulltextUrl != "") {
        window.location.href = data.fulltextUrl;
      }
    }
  });
}

/**
 * @author houchuanjie 发表的动态中含有网址的话展示时显示网页链接
 */
dynamic.transUrl = function() {

  var dynboxes = $('[list-main=psn_dyb_list]').find('.dynamic-main__box');

  dynboxes.each(function() {
    var $div = $(this).children('div:first-child');
    if (!$div.attr('class') || $div.hasClass('dyn_content')) {
      if (!$div.attr('transurl')) {
        var c = $div.text().trim();
        var matchArray = dynamic.matchUrl(c);
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
        $div.html(newstr);
        $div.attr('transurl', 'true');
      }
    }
  });
}
// 加载第一评论。
dynamic.ajaxLoadSampleDyn = function(locale) {
  var $itemList = $("*[list-main='psn_dyb_list']").find(".main-list__item");
  var itemReply = []
  if ($itemList.length > 0) {
    var $itemListReverse = Array.from($itemList).reverse();
    $itemListReverse.forEach(function(obj, index) {
      if (index > 9) {
        return;
      }
      var $input = $(obj).find("input[name='dynId']:last");
      var des3DynId = $input.attr("des3dynId");
      if (des3DynId != undefined && des3DynId != "") {
        var post_data = {
          "des3DynId" : des3DynId
        };
        $.ajax({
          url : '/dynweb/dynamic/ajaxsampledynreply',
          type : 'post',
          dataType : 'html',
          data : post_data,
          success : function(data) {
            $(obj).find(".dynamic__box .dynamic-cmt").remove();
            $(obj).find(".dynamic__box .dynamic-cmt__sample").remove();
            // 在重新加载评论
            $(obj).find(".dynamic__box").append(data);
            $(obj).find(".thickbox").thickbox();
            // 没有评论隐藏评论显示区域
            if ($(obj).find("input[name$='dyn_size']").val() == 0) {
              $(obj).find(".dynamic-cmt__list-box").hide();
            }
            dynamic.IEinitCommentFocus();
          },
          error : function() {
            // console.log("获取评论列表异常")
            return false;
          }
        })
      }

    })
  }

};

dynamic.ajaxLoadDyn = function(locale) {
  var pageSize = parseInt($("#pageSize").val());
  var pageNumber = parseInt($("#pageNumber").val()) + 1;
  var post_data = {
    "pageNumber" : pageNumber,
    "pageSize" : pageSize,
    "locale" : locale
  };

  $.ajax({
    url : '/dynweb/dynamic/ajaxshow',
    type : 'post',
    dataType : 'html',
    data : post_data,
    success : function(data) {
      $("#load_preloader").hide();
      if (data.indexOf("关注更多专家获得更多动态") != -1) {
        data = data.replace("margin-top: 6px", "margin-top: 20px; font-size: 14px;");
        data = data.replace("class=\"bottom_tip mt20\"", "");
        data = data.replace("/pubweb/mobile/search/main", "/scmwebsns/friend/know?menuId=10007");
      }
      $(".container__horiz_left").append(data);
      $("#pageNumber").val(pageNumber);
      var number = parseInt($(".dynamic").length);
      if (data.indexOf("关注更多专家获得更多动态") == -1) {
        if ((number % pageSize == 0 && number != 0)) {
          $("#moreDynamic_div").show();
          $("#moreExperts").hide();
          $("#bottom_tip_c").hide();
        } else {
          $("#moreDynamic_div").hide();
          $("#bottom_tip_c").show();
        }
      } else {
        $("#moreDynamic_div").hide();
        $("#bottom_tip_c").show();
      }
      if (number == 0) {
        $("#moreDynamic_div").hide();
        $("#bottom_tip_c").hide();
      }
      $("#load_preloader").hide();
    },
    error : function() {
      $("#load_preloader").hide();
      return false;
    }
  })

};

/**
 * 在模板中的点击，评论按钮进行评论
 */
dynamic.replyDyn = function(resType, des3ResId, dynType, des3DynId, event) {
  if (dynType == "B2TEMP" || dynType == "B3TEMP") {
    switch (resType) {
      case 1 :
        dynamic.openNotNextPubDetail(des3ResId, event);
      break;
      case 4 :
        dynamic.openPrjDetail(des3ResId, event);
      break;
      case 11 :
        dynamic.openNotNextFundDetail(des3ResId, event);
      break;
      case 22 :
      case 24 :
        dynamic.openNotNextPdwhPubDetail(des3ResId, event);
      break;
      default :
        dynamic.pcDynReply(des3ResId, resType, des3DynId, $(event.target));
      break;
    }
  } else {
    if (resType == '22') {
      dynamic.pdwhIsExist(des3ResId, function() {
        dynamic.pcDynReply(des3ResId, resType, des3DynId, $(event.target));
      });
    } else {
      dynamic.pcDynReply(des3ResId, resType, des3DynId, $(event.target));
    }
  }
  // 其他读取动态评论
  $(".m-bottom").slideToggle(200);
  dynamic.stopNextEvent(event);
}
dynamic.openNotNextPdwhPubDetail = function(des3ResId, event) {
  Pub.newPubPdwhDetail(des3ResId);
  // window.open("/pubweb/details/showpdwh?des3Id="+des3ResId);
  dynamic.stopNextEvent(event);
}
/**
 * PC端动态评论--加载动态详情页面---首页动态评论用
 */
dynamic.pcDynReply = function(des3ResId, resType, des3DynId, obj) {
  $.ajax({
    url : "/dynweb/dynamic/ajaxdetail",
    type : "post",
    data : {
      "des3DynId" : des3DynId,
      "platform" : "pc"
    },
    dataType : "html",
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        $(obj).closest(".container__card").find(".global_no-border").focus();
        var dynDetailDiv = $(obj).parents(".container__card");
        dynamic.pcLoadDynComment(des3DynId, resType, 1, dynDetailDiv);
      });

    }

  })
}
/**
 * pc端加载动态评论内容---首页动态评论用
 */
dynamic.pcLoadDynComment = function(des3DynId, resType, pageNumber, dynDetailDiv) {
  $.ajax({
    url : "/dynweb/dynamic/ajaxdynreply",
    type : "post",
    dataType : "html",
    data : {
      "des3DynId" : des3DynId,
      "resType" : resType,
      "pageNumber" : pageNumber,
      "pageSize" : 100,
      "platform" : "pc"
    },
    success : function(data) {
      // 先移除之前的评论
      $(dynDetailDiv).find(".dynamic__box .dynamic-cmt").remove();
      $(dynDetailDiv).find(".dynamic__box .dynamic-cmt__sample").remove();
      // 在重新加载评论
      $(dynDetailDiv).find(".dynamic__box").append(data);
      $(dynDetailDiv).find(".thickbox").thickbox();
      /*
       * if($(dynDetailDiv).find(".dynamic_scroll").outerHeight() >= 390){ skyScroll({ target:
       * "scroll_" + dynDetailDiv.find("input[name$='nowDate']").val() }); }
       */
      // 没有评论隐藏评论显示区域
      if (dynDetailDiv.find("input[name$='dyn_size']").val() == 0) {
        dynDetailDiv.find(".dynamic-cmt__list-box").hide();
      }
      dynamic.IEinitCommentFocus();
      // 打开代码-2018-06-26 ie浏览器有问题 --SCM-18187
      dynDetailDiv.find(".dynamic-content__post").find(".dynamic-post__main").find("[name='comments']").focus()
    }
  })
}
/**
 * pc端发表评论---首页动态评论用
 */
dynamic.pcDoComment = function(obj) {
  var dynDetailDiv = $(obj).closest(".main-list__item");
  var des3DynId = dynDetailDiv.find("input[dyntype$='TEMP']").attr("des3DynId");
  var des3ResId = dynDetailDiv.find("input[dyntype$='TEMP']").attr("des3ResId");
  var resType = dynDetailDiv.find("input[dyntype$='TEMP']").attr("restype");
  var dynType = dynDetailDiv.find("input[dyntype$='TEMP']").attr("dyntype")
  var commnetContent = $.trim(dynDetailDiv.find("textarea[name$='comments']").val());
  if (commnetContent == "添加评论" || commnetContent == "Add a comment...") {
    commnetContent = "";
  }
  var pubId = $(obj).closest(".dynamic-cmt__post").find(".dev_comment_share_pubinfo   .aleady_select_pub")
      .attr("pubId");
  var des3PubId = "";
  var pubTitle = $(obj).closest(".dynamic-cmt__post").find(".dev_comment_share_pubinfo   .pub-idx__main_title").html();
  if (commnetContent != "" || pubId != "") {
    dynamic.pcSubmitComment(resType, des3ResId, dynType, des3DynId, commnetContent, dynDetailDiv, pubId, des3PubId,
        pubTitle, obj);
  } else {
    scmpublictoast(dynCommon.noComment, 1000);
  }
}

/**
 * pc端发表评论-------首页动态评论用
 */
dynamic.pcSubmitComment = function(resType, des3ResId, dynType, des3DynId, commnetContent, dynDetailDiv, pubId,
    des3PubId, pubTitle, obj) {
  var nextDynType = dynamic.nextDynType(dynType);
  datas = {
    "des3DynId" : des3DynId,
    "des3ResId" : des3ResId,
    "resType" : resType,
    "nextDynType" : nextDynType,
    "dynType" : dynType,
    "replyContent" : commnetContent,
    "operatorType" : 1,
    "pubId" : pubId,
    "replyPubTitle" : pubTitle,
    "des3PubId" : des3PubId
  };
  $.ajax({
    url : '/dynweb/dynamic/ajaxreplydyn',
    type : 'post',
    data : datas,
    success : function(data) {
      SmateShare.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          var $a = dynDetailDiv.find("div[class*='dev_commentcount'] a");
          var commentCount = $a.html().replace(/\D+/g, "");
          commentCount = $.trim(commentCount) != "" ? parseInt(commentCount) + 1 : 1;
          $a.html(dynCommon.comment + "(" + commentCount + ")");
          dynamic.pcLoadDynComment(des3DynId, resType, 1, dynDetailDiv);
        }

      });

    },
    error : function() {
    }
  });
}

dynamic.pcDynHasAward = function(resId, resType, dynId, obj) {
  var dynResIdResTypes = resId + "_" + resType + "_" + dynId + "_" + dyntype;
  $.ajax({
    url : '/dynweb/dynamic/ajaxinithasaward',
    type : 'post',
    dataType : 'json',
    data : {
      "dynResIdResTypes" : dynResIdResTypes
    },
    success : function(data) {
      if (data.result = "success") {
        var dynHasAwardMap = data.hasAwardMap;
        if (dynHasAwardMap != null) {
          if (dynHasAwardMap[dynResIdResTypes]) {
            // 已经点赞
            $(obj).find(".dev_awardcount_" + dynId + " a").prepend("取消");
          }
        }
      }
    },
  });
}

dynamic.showOldDynContent = function(obj) {
  var dynDetailDiv = $(obj).parents(".dyntempDiv").children(".dynDetailDiv")
  dynDetailDiv.hide();
  dynDetailDiv.siblings(".dynamic").show();
};
/**
 * IE初始化评论光标问题
 */
dynamic.IEinitCommentFocus = function() {
  if (!!window.ActiveXObject || "ActiveXObject" in window) { // 判断是否为IE浏览器
    var ua = navigator.userAgent.toLowerCase();
    var trident = /(trident)\/([\w.]+)/;
    var matchBS = trident.exec(ua);
    if (matchBS[2] == "7.0") { // 判断是否为IE11
      /* $("textarea[name='comments']:-ms-input-placeholder").css("color","#ccc"); */
      $("textarea[name='comments']").each(function() {
        text = $(this).attr("placeholder");
        $(this).focus(function() {
          if ($(this).val() == text) {
            $(this).val("");
          }
        });
        $(this).focusout(function() {
          if ($(this).val() == "") {
            $(this).val(text);
          }
        });
      });
    }
  }
}
dynamic.cancelAttentionPsn = function(ev) {
  var $this = $(ev.currentTarget);
  var $input = $this.closest(".main-list__item").find("input[name='dynId']:first");
  var refPsnId = $input.attr("dynownerdes3id");
  if (refPsnId == null || refPsnId == "") {
    return;
  }
  $.ajax({
    url : "/psnweb/psnsetting/ajaxCancelDynAttPerson",
    type : 'post',
    dataType : 'json',
    data : {
      "des3RefPsnId" : refPsnId
    },
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(dynCommon.cancelAttentionSuccess, 2000);
          var dynamicPcDetailItem = $(".global__padding_0");
          for (var i = 0; i < dynamicPcDetailItem.length; i++) {
            if ($(dynamicPcDetailItem[i]).find("input[name='dynId']:first").attr("dynownerdes3id") == refPsnId) {
              if ($(dynamicPcDetailItem[i]).find("li").hasClass("dev_cancelAttention")) {
                $(dynamicPcDetailItem[i]).find("li[class='menu__item dev_cancelAttention']").remove();
              }
            }
          }
        }
      });
    }
  });
}
// 删除动态
dynamic.deleteDynamic = function(obj) {
  var dynId = $(obj).closest(".dynamic__box").find("input").attr("value");
  var dataJson = {
    'dynId' : dynId
  };
  $.ajax({
    url : '/dynweb/dynamic/ajaxdeletedyn',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.result === "success") {
          $(obj).closest(".main-list__item").remove();
          scmpublictoast(dynCommon.delSuccess, 1000);
        } else {
          scmpublictoast(dynCommon.delFail, 1000);
        }
      });
    },
  });
}

dynamic.openOptMenu = function(obj, e) {
  if (e.stopPropagation) {
    e.stopPropagation();
  } else {
    e.cancelBubble = true;
  }
  $(".dev_del_dyn").hide();
  $(".show_time").show();
  dynamic.showDynsTimeStatus = true;
  $(obj).closest(".dev_delBtn").find(".dev_del_dyn").show();
  $(obj).closest(".dev_delBtn").find(".dev_dyn_menu").hide();
  setTimeout(function() {
    dynamic.showDynsTimeStatus = false;
  }, 300);
}

dynamic.showDynsTimeStatus = false;
dynamic.addDelBynBut = function() {
  $(document).click(function() {
    $(".dev_delBtn").hide();
    $(".dynamic-post__time").show();
  });
  var $itemList = $(".main-list").find(".main-list__list[list-main='psn_dyb_list']  .main-list__item");
  var itemReply = []
  if ($itemList.length > 0) {
    var $itemListReverse = Array.from($itemList).reverse();
    $itemListReverse
        .forEach(function(obj, index) {
          if (index > 9) {
            return;
          }
          var dynId = $(obj).find("input").attr("value");
          var delDynBut = '<div class="dynamic-post__operation-box dev_delBtn dyn-publish_content-func">'
              + '<div class="dynamic-operation__icon dev_dyn_menu"  onclick="dynamic.openOptMenu(this,event)"> <span class=" material-icons">more_horiz</span></div>'
              + '<div class="dynamic-operation__actions-box dev_del_dyn">'
              + '  <ul class="menu__list style_dense-item">' + '    <li class="menu__item"><a>'
              + '      <div class="menu-item__content">' + '        <div class="menu-item__content_icon"></div>'
              + '        <div class="menu-item__content_main" onclick="dynamic.deleteDynamic(this,event)">'
              + dynCommon.delDynamic + '</div>' + '      </div>' + '      </a></li>'
              + '    <li class="menu__item dev_cancelAttention"><a>' + '      <div class="menu-item__content">'
              + '        <div class="menu-item__content_icon"></div>'
              + '        <div class="menu-item__content_main" onclick="dynamic.cancelAttentionPsn(event)">'
              + dynCommon.cancelAttention + '</div>' + '      </div>' + '      </a></li>' + '  </ul>' + ' </div>'
              + '  </div>';
          var length = $(obj).find(".dynamic-creator__box").length;
          if (length > 0) {
            $(obj).find(".dev_delBtn").remove();
            $(obj).find(".dynamic-creator__box").append(delDynBut);
            $(obj).find(".dynamic-creator__box").on({
              "mouseenter" : function() {
                if ($(obj).find(".dev_del_dyn").is(":hidden") && !dynamic.showDynsTimeStatus) {
                  dynamic.showDynsTimeStatus = true;
                  $(obj).find(".dev_delBtn").show();
                  $(obj).find(".dev_dyn_menu").show();
                  $(obj).find(".dev_del_dyn").hide();
                  setTimeout(function() {
                    dynamic.showDynsTimeStatus = false;
                  }, 200);
                }
              }
            });
            $(obj).find(".dynamic-creator__box").on({
              "mouseleave" : function(evt) {
                if ($(obj).find(".dev_del_dyn").is(":hidden") && !dynamic.showDynsTimeStatus) {
                  dynamic.showDynsTimeStatus = true;
                  $(obj).find(".dev_delBtn").hide();
                  setTimeout(function() {
                    dynamic.showDynsTimeStatus = false;
                  }, 200);
                }
              }
            });
            /*
             * $(obj).find(".dev_del_dyn").on({ "mouseleave":function(e){
             * if(!dynamic.showDynsTimeStatus){ dynamic.showDynsTimeStatus=true;
             * $(obj).find(".dev_delBtn").hide();
             * setTimeout(function(){dynamic.showDynsTimeStatus=false;},200); } } });
             */
          } else {
            $(obj).find(".dev_delBtn").remove();
            $(obj).find(".dynamic-content  .dynamic-post__main").after(delDynBut);
            $(obj).find(".dynamic-post__time").on({
              "mouseenter" : function() {
                if ($(obj).find(".dev_del_dyn").is(":hidden") && !dynamic.showDynsTimeStatus) {
                  dynamic.showDynsTimeStatus = true;
                  $(obj).find(".dynamic-content  .dynamic-post__time").hide();
                  $(obj).find(".dev_delBtn").show();
                  $(obj).find(".dev_dyn_menu").show();
                  $(obj).find(".dev_del_dyn").hide();
                  setTimeout(function() {
                    dynamic.showDynsTimeStatus = false;
                  }, 200);
                }
              }
            });
            $(obj).find(".dev_dyn_menu").on({
              "mouseleave" : function(evt) {
                if ($(obj).find(".dev_del_dyn").is(":hidden") && !dynamic.showDynsTimeStatus) {
                  dynamic.showDynsTimeStatus = true;
                  $(obj).find(".dynamic-content .dynamic-post__time").show();
                  $(obj).find(".dev_delBtn").hide();
                  setTimeout(function() {
                    dynamic.showDynsTimeStatus = false;
                  }, 200);
                }
              }
            });
            /*
             * $(obj).find(".dev_del_dyn").on({ "mouseleave":function(e){
             * if(!dynamic.showDynsTimeStatus){ dynamic.showDynsTimeStatus=true;
             * $(obj).find(".dev_delBtn").hide(); $(obj).find(".dynamic-content
             * .dynamic-post__time").show();
             * setTimeout(function(){dynamic.showDynsTimeStatus=false;},200); } } });
             */
          }
          var dynownerdes3id = $(obj).closest(".main-list__item").find("input[name='dynId']:first").attr(
              "dynownerdes3id");
          var userId = $("#userId").val();
          if (dynownerdes3id == null || dynownerdes3id == "" || dynownerdes3id == userId
              || "W8Fa7kfXJMI%3D" == dynownerdes3id) {
            $(obj).closest(".main-list__item").find(".dev_cancelAttention").hide();
          }
        });
  }
}
