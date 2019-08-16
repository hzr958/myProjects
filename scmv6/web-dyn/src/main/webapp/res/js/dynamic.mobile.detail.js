//移动端打开人员主页
var dynamic = dynamic ? dynamic : {};
/**
 * 动态打开详情
 */
dynamic.openDetail = function(des3DynId, dynType, des3ResId, resType, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  var $this = $(ev.currentTarget);
  switch (resType) {
    case "1" :
      location.href = "/pub/details/snsnonext?des3PubId=" + encodeURIComponent(des3ResId);
      dynamic.stopNextEvent(ev);
    break;
    case "11" :
      dynamic.openNotNextFundDetail(des3ResId, ev);
      // if (dynamic.checkFundAgency(des3ResId, resType)) {
      // }
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
      // if (dynamic.checkFundAgency(des3ResId, resType)) {
      // }
    break;
    case "26" :
      dynamic.openNewsDetail(des3ResId, ev);
    break;
    default :
      dynamic.openDynDetail(des3DynId);
    break;
  }
}
dynamic.openNotNextPdwhDetail = function(des3PubId, dbid, event) {
  location.href = "/pub/details/pdwh?des3PubId=" + encodeURIComponent(des3PubId);
  dynamic.stopNextEvent(event);
}

// 检查个人库成果是否存在
dynamic.snsPubIsExist = function(des3PubId) {
  var exist = true;
  $.ajax({
    url : "/pub/optsns/checkPub",
    type : 'post',
    dataType : "json",
    async : false,
    data : {
      "des3PubId" : encodeURIComponent(des3PubId)
    },
    timeout : 10000,
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.result == 'isDel') {
          exist = false;
          scmpublictoast(dynCommon.pubIsDeleted, 2000);
        }
      });
    },
    error : function(data) {
      scmpublictoast(dynCommon.optError, 2000);
    }
  });
  return exist;
}

/**
 * 全文下载或全文请求
 */
dynamic.fullTextEvent = function(des3DynId, dynType, des3ResId, resType, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  var $this = $(ev.currentTarget);
  var $inputs = $this.closest(".main-list__item").find("input[name='dynId']");
  var resOwnerDes3Id = $inputs.eq($inputs.length - 1).attr("resOwnerDes3Id");
  if (resOwnerDes3Id == undefined) {
    resOwnerDes3Id = $("#resOwnerDes3Id").val();
  }
  var des3FileId = $this.closest(".dev_pub_fulltext").attr("des3FileId");
  var userId = $("#userId").val();
  switch (resType) {
    case "1" :
    case "2" :// sns库的成果处理
      // 校验成果是否删除
      var exist = dynamic.snsPubIsExist(des3ResId);
      if (!exist) {
        return;
      }
      // SCM-24197 yhx 2019/3/28
      var des3OwnerId = dynamic.getSnsPubOwner(des3ResId);
      if (des3OwnerId != null) {
        resOwnerDes3Id = des3OwnerId;
      }
      if (des3FileId) {// 全文存在判断是否隐私
        $.ajax({
          url : "/pub/fulltext/getpermission",
          type : "post",
          data : {
            "des3SearchPubId" : des3ResId,
            "des3FileId" : des3FileId
          },
          dataType : "json",
          success : function(data) {
            if (data.result) {
              if (data.fullTextPermission != 0 && resOwnerDes3Id != userId) {
                // 全文隐私则发送全文请求
                dynamic.requestSeeFTFile(des3ResId, 'sns', resOwnerDes3Id);
                return;
              }
              dynamic.downloadFTFile(des3ResId, 'sns');
            }
          }
        })
      } else {
        if (userId == resOwnerDes3Id || encodeURIComponent(userId) == resOwnerDes3Id
            || userId == encodeURIComponent(resOwnerDes3Id)) {// 自己的成果，则上传全文
          // dynamic.uploadFullText(des3ResId);//移动端不用上传全文
          dynamic.smatemobile();
        } else if (resOwnerDes3Id != "") {// 不是自己的，则全文请求
          dynamic.requestAuthorFTFile(des3ResId, 'sns', resOwnerDes3Id);
        }
      }
    break;
    case "11" :// 基金
      if (dynamic.checkFundAgency(des3ResId, resType)) {
        dynamic.openNotNextFundDetail(des3ResId, ev);
      }
    break;
    case "22" :// 基准库成果处理
    case "24" :
      dynamic.pdwhIsExist(des3ResId, function() {
        if (des3FileId) {// 全文存在则下载
          dynamic.downloadFTFile(des3ResId, 'pdwh');
        } else {// 不存在则请求
          dynamic.requestAuthorFTFile(des3ResId, 'pdwh', '');
        }
      });
    break;
    case "25" :
      if (dynamic.checkFundAgency(des3ResId, resType)) {
        dynamic.openAgencyDetail(des3ResId, ev);
      }
    break;
    case "26" :
      dynamic.openNewsDetail(des3ResId, ev);
    break;
    default :
    break;
  }
};

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

dynamic.smatemobile = function() {
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
/**
 * 收藏
 */
dynamic.dynCollectPub = function(des3DynId, dynType, des3ResId, resType, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 500);
  var $this = $(ev.currentTarget);
  var type = $this.attr("collected")
  switch (resType) {
    case "1" :// sns个人库成果
      dynamic.dealCollectedPub(des3ResId, 'SNS', type, function() {
        dynamic.dynCollectStyle(type, $this);
      });
    break;
    case "11" :// 基金
      if (dynamic.checkFundAgency(des3ResId, resType)) {
        var type = $this.attr("collected")
        dynamic.dynCollectCoperation($this, des3ResId, type, function() {
          dynamic.dynCollectStyle(type, $this);
        });
      }
    break;
    case "22" :
    case "24" :// 基准库成果
      var deletedStatus = dynamic.checkResDeleteStatus(resType, des3ResId);
      if (deletedStatus) {
        newMobileTip("资源已删除");
        return;
      }
      dynamic.dealCollectedPub(des3ResId, 'PDWH', type, function() {
        dynamic.dynCollectStyle(type, $this);
      });
    break;
    case "25" : // 资助机构
      if (dynamic.checkFundAgency(des3ResId, resType)) {
        var type = $this.find(".paper_footer-tool").hasClass("paper_footer-comment__flag") == true ? 1 : 0;
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
    $this.find("i").removeClass("paper_footer-comment__flag").addClass("paper_footer-comment");
    // scmpublictoast(dynCommon.optSuccess, 2000);
  } else {
    $this.attr("collected", "1");
    $this.find("a").html(unCollectWord);
    $this.find("i").removeClass("paper_footer-comment").addClass("paper_footer-comment__flag");
    // scmpublictoast(dynCommon.optSuccess, 2000);
  }
}

/**
 * 引用
 */
dynamic.dynCitePub = function(des3DynId, dynType, des3ResId, resType, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  var $this = $(ev.currentTarget);
  switch (resType) {
    case 1 :// sns个人库成果
      windows.location.href = "/pubweb/publication/ajaxpubquote?desId=" + des3ResId
          + "&TB_iframe=true&height=350&width=750";
    break;
    case 22 :
    case 24 :// 基准库成果
      windows.location.href = "/pubweb/publication/ajaxpdwhpubquote?desId=" + des3ResId
          + "&TB_iframe=true&height=350&width=750";
    break;
    default :
    break;
  }
  $this.closest(".main-list__list").find(".dyn_thickbox").eq(0).attr("href", url).click();
  dynamic.stopNextEvent(ev);
}
/**
 * 首页动态分享
 */
dynamic.shareDynMain = function(des3DynId, dynType, des3ResId, resType, ev) {
  var deletedStatus = dynamic.checkResDeleteStatus(resType, des3ResId);
  if (deletedStatus) {
    newMobileTip("资源已删除");
    return;
  }
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  var $this = $(ev.currentTarget);
  dynamic.stopNextEvent(ev);
  // 成果分享添加隐私判断
  if (resType == "1" || resType == "2") {
    var dynamicParam = {};
    dynamicParam.des3DynId = des3DynId;
    dynamicParam.dynType = dynType;
    dynamicParam.des3ResId = des3ResId;
    dynamicParam.resType = resType;
    dynamicParam.ev = $this[0];
    SmateCommon.checkPubAnyUser(des3ResId, "dynamic", dynamicParam);
  } else {
    dynamic.shareDynCommon(des3DynId, dynType, des3ResId, resType, $this[0]);
  }

}
/**
 * 动态分享功能公共部分
 */
dynamic.shareDynCommon = function(des3DynId, dynType, des3ResId, resType, ev) {
  // if (dynType == "ATEMP" || dynType == "B1TEMP") {
  // // 需求变更-删除快速分享
  // SmateShare.getParamForQuickShare(ev);
  // initSharePlugin(ev);
  // /*
  // * dynamic.quickShare(des3DynId,dynType,des3ResId,resType,function(){
  // * scmpublictoast(dynCommon.shareSuccess,1000); setTimeout(dynamic.openDynList, 1000); });
  // */
  // } else if (dynType == "B2TEMP" || dynType == "B3TEMP") {
  // SmateShare.getParam(ev);
  // initSharePlugin(ev);
  // }
  /**
   * 需求变更,将首页动态分享由弹框改成跳入分享页面(SCM-23746)
   * 修改逻辑:点击分享获取动态id,由后台构造分享所需要的参数,不管是哪种类型,利用DynamicForm/DynamicMsg进行接收,包含所有参数,哪种类型获取所需要的参数即可
   */
  // 传递数据库没有保存的数据,这些数据(除dynId)是保存在mangodb的模板内,无法获取,所以传递
  var $box_obj = $(ev).closest(".dynamic__box");
  var dynId = $box_obj.find("input[name=dynId]").attr("value");
  var databaseType = $(ev).attr("databaseType");
  if (typeof databaseType == typeof undefined) {
    databaseType = 0;
  }
  var dbId = $(ev).attr("dbId");
  if (typeof dbId == typeof undefined) {
    dbId = 0;
  }
  var text_content = $.trim(encodeURIComponent($box_obj.find(".dyn_content").text()));
  var resgrpid = $box_obj.find("input[name=dynId]").attr("resgrpid");
  if (typeof resgrpid == typeof undefined) {
    resgrpid = 0;
  }
  var title = $.trim(encodeURIComponent($box_obj.find(".dynamic-content .pub-idx__main_title").text()));
  var resInfoId = $(ev).attr("resInfoId") != "undefined" ? $(ev).attr("resInfoId") : 0;
  if (typeof resInfoId == typeof undefined) {
    resInfoId = 0;
  }
  var fundId = $(ev).attr("notEncodeId") != "undefined" ? $(ev).attr("notEncodeId") : 0;
  if (typeof fundId == typeof undefined) {
    fundId = 0;
  }
  var des3PubId = $(ev).attr("des3PubId") != "undefined" ? $(ev).attr("des3PubId") : 0;
  if (typeof des3PubId == typeof undefined) {
    des3PubId = "";
  }
  var parentDynId = $(ev).attr("parentdynid");
  if (typeof parentDynId == typeof undefined) {
    parentDynId = 0;
  }
  var params = "?dynId=" + dynId + "&databaseType=" + databaseType + "&dbId=" + dbId + "&dynText=" + text_content
      + "&resGrpId=" + resgrpid + "&title=" + title + "&resInfoId=" + resInfoId + "&fundId=" + fundId + "&des3PubId="
      + des3PubId + "&parentDynId=" + parentDynId;
  window.location.href = "/dyn/mobile/middynshare" + params;
}

/**
 * 首页动态分享
 */
dynamic.shareDyn = function(ev) {
  SmateShare.getParam(this);
  initSharePlugin(this);
}

/**
 * 获取初始化动态统计信息以及发布时间
 */
dynamic.dynstatistics = function() {
  var temp = "";
  var dynIdArray = [];
  $("div[list-main='mobile_dyn_list']").find(".main-list__item").each(function(i, o) {
    var des3DynId = $(o).find("input[name='dynId']").eq(0).attr("des3DynId");
    if (des3DynId != null) {
      dynIdArray[dynIdArray.length] = des3DynId;
    }
  });
  if (dynIdArray.length == 0) {
    return;
  }
  temp = BaseUtils.unique3(dynIdArray).join(",");
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
          var $item = $("input[name='dynId'][value='" + key + "']").closest(".main-list__item");
          var $opts = $item.find(".dynamic-social__list > .dynamic-social__item");
          // 遍历动态操作行为（赞、评论、分享）
          $.each($opts, function(i, o) {
            // 获取统计数
            var count = 0;
            /*
             * if (i == 0) { count = dynStatisMap[key].awardCount; } else if (i == 1) { count =
             * dynStatisMap[key].commentCount; } else if (i == 2) { count =
             * dynStatisMap[key].shareCount }
             */
            // 获取操作并修改统计数
            var ahtml = $(o).find("a").html();
            if (ahtml.indexOf("(") > 0) {
              ahtml = ahtml.substring(0, ahtml.indexOf("("));
            }
            if (ahtml.indexOf("赞") >= 0) {
              count = dynStatisMap[key].awardCount;
              if (count > 0) {
                count = count > 999 ? '1k+' : count;
                $(o).find("a").html(ahtml + "(" + count + ")");
              } else {
                $(o).find("a").html(ahtml);
              }
            } else if (ahtml.indexOf("评论") >= 0) {
              count = dynStatisMap[key].commentCount;
              if (count > 0) {
                count = count > 999 ? '1k+' : count;
                $(o).find("a").html(ahtml + "(" + count + ")");
              } else {
                $(o).find("a").html(ahtml);
              }
            } else if (ahtml.indexOf("分享") >= 0) {
              count = dynStatisMap[key].shareCount
              if (count > 0) {
                count = count > 999 ? '1k+' : count;
                $(o).find("a").html(ahtml + "(" + count + ")");
              } else {
                $(o).find("a").html(ahtml);
              }
            } else if (ahtml.indexOf("查看") >= 0) {
              count = dynStatisMap[key].viewCount
              if (count > 0) {
                count = count > 999 ? '1k+' : count;
                $(o).find("a").html(ahtml + "(" + count + ")");
              } else {
                $(o).find("a").html(ahtml);
              }
            }

          });
          // 设置动态时间
          if (dynStatisMap[key].dynType == "B1TEMP" && $("#b1time_" + key).length > 0) {
            $("#b1time_" + key).html("<div>" + dynStatisMap[key].publishDate + "</div>");
          } else {
            $("#time_" + key).html("<div>" + dynStatisMap[key].publishDate + "</div>");
          }
          if (dynStatisMap[key].resType != "11") {// 不是基金就更新全文图片
            var $img = $item.find(".dynamic-content__main").find("img");
            // 更新成果全文图片
            if (dynStatisMap[key].des3FileId != null && dynStatisMap[key].des3FileId != "") {
              $item.find(".dev_pub_fulltext").attr("des3FileId", dynStatisMap[key].des3FileId);
              if (dynStatisMap[key].imgUrl !== null && dynStatisMap[key].imgUrl != "") {
                $img.attr("src", dynStatisMap[key].imgUrl);
              }
            } else {
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
          // 更新基金收藏情况
          if (dynStatisMap[key].collected != null) {
            $item.find(".dev_save").attr("collected", dynStatisMap[key].collected);
            if (dynStatisMap[key].collected == "1") {
              $item.find(".dev_save").attr("collected", "1");
              $item.find(".dev_save").find("a").html(dynCommon.unCollected);
              $item.find(".dev_save>i").removeClass("paper_footer-comment").addClass("paper_footer-comment__flag");
            } else {
              $item.find(".dev_save").attr("collected", "0");
              $item.find(".dev_save").find("a").html(dynCommon.collected);
            }
          }

          // 初始化资助机构相关信息
          if (dynStatisMap[key].resType == "25") {
            dynamic.initAgencyShareDynInfo(dynStatisMap, $item, key);
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
        }
        dynamic.initDelBut();// 添加删除按钮
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
  }
  $img.error(function() {// 没有或出错显示默认图片
    $(this).attr('src', "/resmod/smate-pc/img/logo_instdefault.png");
  })
  // 关注操作
  var interestStatus = dynStatisMap[key].collected;
  if (interestStatus != null) {
    $item.find(".dev_save").attr("collected", interestStatus);
    if (interestStatus == "1") {
      $item.find(".dev_save").find("a").html(dynCommon.cancelInterest);
    } else {
      $item.find(".dev_save").find("a").html(dynCommon.interest);
    }
  }
}

dynamic.dyndetailstatistics = function(des3DynId) {
  $.ajax({
    url : '/dynweb/dynamic/ajaxgetdynstatistics',
    type : 'post',
    dataType : 'json',
    data : {
      "dynStatisticsIds" : des3DynId
    },
    success : function(data) {
      if (data.result === "success") {
        var dynStatisMap = data.dynStatisMap;
        for ( var key in dynStatisMap) {
          var $item = $(".dynamic-content__main.mobile");
          if (dynStatisMap[key].resType != "11") {// 不是基金就更新全文图片
            var $img = $item.find("img");
            // 更新成果全文图片
            if (dynStatisMap[key].des3FileId != null && dynStatisMap[key].des3FileId != "") {
              $item.find(".dev_pub_fulltext").attr("des3FileId", dynStatisMap[key].des3FileId);
              if (dynStatisMap[key].imgUrl !== null && dynStatisMap[key].imgUrl != "") {
                $img.attr("src", dynStatisMap[key].imgUrl);
              }
            }
          } else {
            $item.find(".dev_pub_fulltext").attr("des3FileId", "");
            $img.attr("src", "/resmod/images_v5/images2016/file_img.jpg");
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
        }
        dynamic.initDelBut();// 添加删除按钮
      }
    }
  })
};

dynamic.hasCollect = function(des3DynId) {
  $.ajax({
    url : '/dynweb/dynamic/ajaxgetdynCollect',
    type : 'post',
    async : false,
    dataType : 'json',
    data : {
      "des3DynId" : des3DynId
    },
    success : function(data) {
      if (data.result = "success") {
        if (data.collected == "1") {
          $(".dev_save").attr("collected", 1);
          $(".dev_save").find("i").removeClass("paper_footer-comment").addClass("paper_footer-comment__flag");
          $(".dev_save").find("a").html(dynCommon.unCollected);
        } else {
          $(".dev_save").attr("collected", 0);
          $(".dev_save").find("i").removeClass("paper_footer-comment__flag").addClass("paper_footer-comment");
          $(".dev_save").find("a").html(dynCommon.collected);
        }
      }
    },
    error : function() {
      return false;
    }
  });
}
dynamic.hasAward = function(des3DynId) {
  var dynId;
  $.ajax({
    url : '/dynweb/dynamic/ajaxinithasaward',
    type : 'post',
    async : false,
    dataType : 'json',
    data : {
      "dynResIdResTypes" : des3DynId
    },
    success : function(data) {
      if (data.result = "success") {
        var dynHasAwardMap = data.hasAwardMap
        if (dynHasAwardMap != null) {
          for ( var key in dynHasAwardMap) {
            dynId = key;
            if (dynHasAwardMap[key] == true) {
              $(".dev_awardcount_" + dynId + " a").html(dynCommon.unlike);
              if ($(".dev_awardcount_" + dynId).find("i").hasClass("paper_footer-fabulous")) {
                $(".dev_awardcount_" + dynId).find("i").removeClass("paper_footer-fabulous");
                $(".dev_awardcount_" + dynId).find("i").addClass("paper_footer-award_unlike");
              }
            };
          }
        } else {
          return false;
        }
      } else {
        return false;
      }
      if (dynId != null && dynId > 0) {
        var awardCount = $("#dyn-statis-award").val();
        var replyCount = $("#dyn-statis-reply").val();
        var shareCount = $("#dyn-statis-share").val();
        if (awardCount > 0) {
          awardCount = awardCount > 999 ? '1k+' : awardCount;
          $(".dev_awardcount_" + dynId + " a").append("(" + awardCount + ")");
        }
        if (replyCount > 0) {
          replyCount = replyCount > 999 ? '1k+' : replyCount;
          $(".dev_commentcount_" + dynId + " a").append("(" + replyCount + ")");
        }
        if (shareCount > 0) {
          shareCount = shareCount > 999 ? '1k+' : shareCount;
          $(".dev_sharecount_" + dynId + " a").append("(" + shareCount + ")");
        }
      }
    },
    error : function() {
      return false;
    }
  });
}
/**
 * 获取初始化动态时候点赞
 */
dynamic.initHasAward = function() {
  var temp = "";
  var dynIdArray = [];
  $("div[list-main='mobile_dyn_list']").find(".main-list__item").each(function(i, o) {
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
              if ($(id).find("i").hasClass("paper_footer-fabulous")) {
                $(id).find("i").removeClass("paper_footer-fabulous");
                $(id).find("i").addClass("paper_footer-award_unlike");
              }
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
  dynamic.dontAllowPublish($("#dynrealtime"));
  var resType = $("#dynResType").val();
  var des3ResId = $("#des3ResId").val();
  var textarea = $("#dyntextarea").val();
  var des3psnId = $("#des3psnId").val();
  var dynType = "ATEMP";
  var operatorType = -1;
  if ($.trim(textarea) == "" && des3ResId == "") {
    smate.materialize.toast(locale == 'en_US' ? 'Content not Empty!' : '内容不为空!', 1000);
    $("#dyntextarea").val("")
    dynamic.allowPublish($("#dynrealtime"));
    return;
  }
  if ("" == textarea && des3ResId != "") {
    dynType = "B2TEMP";
    operatorType = 3;
  }
  textarea = textarea.replace(/&lt;(.*)&gt;.*&lt;\/\1&gt;|&lt;(.*) \/&gt;/, "&_lt;$1&_gt;").replace(/</g, "&lt;")
      .replace(/>/g, "&gt;").replace(/\n/g, '<br>');
  var dataJson = {
    "dynType" : dynType,
    "dynText" : textarea,
    "resType" : resType,
    "des3ResId" : des3ResId,
    "des3PubId" : des3ResId,
    "operatorType" : operatorType,
    "des3psnId" : des3psnId,
    "platform" : "mobile"
  }
  dynamic.ajaxrealtime(dataJson, function() {
    location.href = "/dynweb/mobile/dynshow";
  });
}

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
        if (dynamic.checkFundAgency(des3ResId, resType)) {
          dynamic.openNotNextFundDetail(des3ResId, event);
        }
      break;
      case 22 :
      case 24 :
        dynamic.openNotNextPdwhPubDetail(des3ResId, event);
      break;
      default :
        dynamic.openDynDetail(des3DynId);
      break;
    }
  } else {
    if ((resType == "25" || resType == "11") && !dynamic.checkFundAgency(des3ResId, resType)) {
      return;
    }
    dynamic.openDynDetail(des3DynId, event);
  }
  dynamic.stopNextEvent(event);
}

dynamic.shareDyn = function(resType, resId, dynType, dynId, parentDynId, event) {
  $("#dynamicShare").show();
  // $(".sharecount_"+dynId).attr("onclick" ,"dynamic.emptyclick(event)");
  $("#quickShareOpt").attr(
      "onclick",
      "dynamic.quickShareDyn(" + resType + "," + resId + " ,'" + dynType + "' ," + dynId + "," + parentDynId
          + ",event);");
  dynamic.stopNextEvent(event);
}

dynamic.openPsnDetail = function(des3ProducerPsnId, event) {
  location.href = "/psnweb/mobile/outhome?des3ViewPsnId=" + des3ProducerPsnId;
  dynamic.stopNextEvent(event);
}

// 移动端打开有滑动的成果详情

dynamic.openPubDetail = function(des3PubId, event) {
  location.href = "/pubweb/wechat/findpubxml?useoldform=true&des3PubId=" + des3PubId;
  dynamic.stopNextEvent(event);
}
// 端打开基金详情
dynamic.openNotNextFundDetail = function(des3PubId, event) {
  window.open("/prjweb/wechat/findfundsxml?des3FundId=" + encodeURIComponent(des3PubId));
  dynamic.stopNextEvent(event);
}
// 移动端打开成果详情
dynamic.openNotNextPubDetail = function(des3PubId, event) {
  location.href = "/pub/details/snsnonext?des3PubId=" + encodeURIComponent(des3PubId) + "&operateType=comment";
  dynamic.stopNextEvent(event);
}
// 移动端基准库成果详情
dynamic.openNotNextPdwhPubDetail = function(des3ResId, event) {
  location.href = "/pub/details/pdwh?des3PubId=" + encodeURIComponent(des3ResId) + "&operateType=comment";
  dynamic.stopNextEvent(event);
}
// 移动端打开项目详情
dynamic.openPrjDetail = function(des3ResId, event) {
  window.open("/prjweb/wechat/findprjxml?des3PrjId=" + encodeURIComponent(des3ResId));
  dynamic.stopNextEvent(event);
}
// 移动端打开别人的成果列表
dynamic.openOtherPubList = function(des3PsnId, event) {
  location.href = "/pub/querylist/psn?des3PsnId=" + encodeURIComponent(des3PsnId);
  dynamic.stopNextEvent(event);
}
// 移动端打开资助机构详情
dynamic.openAgencyDetail = function(des3ResId, event) {
  window.open("/prj/outside/agency?des3FundAgencyId=" + encodeURIComponent(des3ResId));
  dynamic.stopNextEvent(event);
}
// 移动端打开新闻详情
dynamic.openNewsDetail = function(des3ResId, event) {
  window.open("/dynweb/mobile/news/details?des3NewsId=" + encodeURIComponent(des3ResId));
  dynamic.stopNextEvent(event);
}
// 移动端打开动态详情
dynamic.openDynDetail = function(des3DynId, event) {
  var json = {
    time : new Date().getTime()
  };
  var $this = $(event.currentTarget);
  var $inputs = $this.closest(".main-list__item").find("input[name='dynId']");
  var resOwnerDes3Id = $inputs.eq($inputs.length - 1).attr("resOwnerDes3Id");
  var userId = $("#userId").val();
  window.history.pushState(json, "", location.href);
  location.href = "/dynweb/mobile/dyndetail?des3DynId=" + des3DynId + "&resOwnerDes3Id=" + resOwnerDes3Id + "&userId="
      + encodeURIComponent(userId) + "&platform=mobile";
  dynamic.stopNextEvent(event);
}

dynamic.dynDetailBack = function() {
  window.onpopstate();

}
// 移动端打开动态列表
dynamic.openDynList = function() {
  // 分享完毕后返回原分享页
  if ($("#historyPage") && typeof $("#historyPage") != typeof undefined && $("#historyPage").val().length > 0) {
    window.location.href = $("#historyPage").val();
    return;
  }
  window.location.href = "/dynweb/mobile/dynshow";
}
/**
 * 在文本框直接输入评论后点击确定
 */
dynamic.doComment = function() {
  var dynId = $("input[name='dynId']").val();
  var resId = $("input[name='dynId']").attr("resid");
  var des3ResId = $("input[name='dynId']").attr("des3resid");
  var des3DynId = $("input[name='dynId']").attr("des3dynid");
  var resType = $("input[name='dynId']").attr("restype");
  var dynType = $("input[name='dynId']").attr("dyntype");
  var commnetContent = $.trim($("#comment_content").val());
  if (commnetContent != "") {
    dynamic.submitComment(resType, des3ResId, dynType, des3DynId, commnetContent, "mobile");
    // 评论数加1
    var id = ".dev_commentcount_" + dynId;
    var html = $(id).find("a").html();
    if (html.indexOf("1k+") < 0) {
      var commentCount = html.replace(/\D+/g, "");
      commentCount = $.trim(commentCount) != "" ? parseInt(commentCount) + 1 : 1;
      commentCount = commentCount > 999 ? '1k+' : commentCount;
      $(id).find("a").html(dynCommon.comment + "(" + commentCount + ")");
    }
  }
  $("#comment_content").val("");
  $("#comment_content").height("20px");
}

/**
 * 在模板中的点击，评论按钮进行评论 获取评论内容
 */
dynamic.makeComment = function(resType, resId, dynType, dynId) {
  $(".m-bottom").slideToggle(200);
  var makeCommentPageUrl = document.location.href;
  if (fromUrl.indexOf("/dynweb/mobile/dynshow") > 1 || fromUrl.indexOf("/dynweb/dynamic/show") > 1
      || fromUrl.indexOf("/dynweb/mobile/dyndetail") > 1) {
    dynamic.openDynDetail(dynId);
  }
  $("#comment_content").focus();
  var commnetContent = $.trim($("#comment_content").val());
  if (commnetContent != "") {
    dynamic.submitComment(resType, resId, dynType, dynId, commnetContent);
  }
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
  window
      .Mainlist({
        name : "mobile_dyn_list",
        listurl : "/dynweb/dynamic/ajaxshow",
        listdata : {
          "platform" : "mobile"
        },
        method : "scroll",
        listcallback : function(xhr) {
          // dynamic.initDynCollectStatus();
          dynamic.initHasAward();
          dynamic.addDelBynBut();
          dynamic.transUrl();
          var $_ul = $("*[list-main='mobile_dyn_list']");
          if ($(".main-list__list").attr("total-count") == 0
              || $(".main-list__list").attr("total-count") == 'undifined') {
            $_ul.find(".response_no-result").remove();
            $_ul
                .append("<div class='main-list__item global__padding_0' style='margin-bottom:0px;'><div class='response_no-result' style='margin-top: 40px;'>"
                    + dynCommon.noMoreRecord + "</div></div>");
          } else if ($_ul.find(".main-list__item").length == $(".main-list__list").attr("total-count")
              && $_ul.find(".response_no-result").length == 0) {
            $_ul
                .append("<div class='main-list__item global__padding_0' style='margin-bottom: 0;'><div class='response_no-result' style='margin-top: 40px;'>"
                    + dynCommon.noMoreRecord + "</div></div>");
          }

          var item = $("div[list-main='mobile_dyn_list']").find(".main-list__item");
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
            var unread = $("div[list-main='mobile_dyn_list']").find(".dyn_pageready_unread");
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
          BaseUtils.mobileSlideEvent($(".main-list__item"), {
            "muEvent" : mobileUpSlide,
            "mdEvent" : mobileDownSlide
          });// 添加滑动事件
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
  var item = $("div[list-main='mobile_dyn_list']").find(".main-list__read-item");
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
      "platform" : "mobile",
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
  var item = $("div[list-main='mobile_dyn_list']").find(".main-list__read-item");
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
      "platform" : "mobile",
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
  var item = $("div[list-main='mobile_dyn_list']").find(".main-list__read-item");
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
      "form" : "mobile",
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
    document.location.href = "/pub/mobile/pubrecommendmain";
  } else if (type == 2) {
    document.location.href = "/prjweb/wechat/findfunds?module=recommend";
  } else {
    document.location.href = "/dynweb/news/mobile/main";
  }
}
// 查看
dynamic.viewRemdRes = function(des3ResId, type) {
  if (type == 1) {
    document.location.href = "/pub/details/pdwh?des3PubId=" + encodeURIComponent(des3ResId);
  } else if (type == 2) {
    document.location.href = "/prjweb/wechat/findfundsxml?des3FundId=" + encodeURIComponent(des3ResId);
  } else {
    document.location.href = "/dynweb/mobile/news/details?des3NewsId=" + encodeURIComponent(des3ResId);
  }
}
// 不感兴趣
dynamic.notViewRemdRes = function(obj, des3ResId, type) {
  if (type == 1) {
    $.ajax({
      url : "/pub/mobile/ajaxuninterested",
      type : "post",
      dataType : "json",
      data : {
        "des3PubId" : des3ResId
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
  dynamic.requestFTFile(des3ResId, 'pdwh', '');

  /*
   * dynamic.requestFullText(des3ResId, 'pdwh', '', function() {
   * scmpublictoast(dynCommon.requestFullTextSuccess, 2000); })
   */
}
/**
 * @author houchuanjie 发表的动态中含有网址的话展示时显示网页链接
 */
dynamic.transUrl = function() {

  var dynboxes = $('[list-main=mobile_dyn_list]').find('.dynamic-main__box');

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
                + "\" style=\"color: #005eac !important; word-break:break-all;\" target=\"_blank\">"
                + matchArray[i].str + "</a> ";
          }
          newstr += stri + urli;
        }
        $div.html(newstr);
        $div.attr('transurl', 'true');
      }
    }
  });
}
/**
 * 加载更多动态列表内容
 */
dynamic.ajaxLoadDyn = function() {
  var length = $(".bottom_tip_c").length;
  // 没有动态是，就直接返回
  if (length > 0 || $("#load_preloader").css("display") == "block") {
    return;
  }
  $("#load_preloader").show()
  var pageNumber = parseInt($("#pageNumber").val()) + 1;
  var post_data = {
    "pageNumber" : pageNumber,
  };
  $.ajax({
    url : '/dynweb/dynamic/ajaxshow',
    type : 'post',
    dataType : 'html',
    data : post_data,
    success : function(data) {
      setTimeout(function() {
        if (data.indexOf("关注更多专家获得更多动态") == -1) {
          $("#moreDynamic_div").before(data);
          $("#pageNumber").val(pageNumber);
        } else {
          $("#moreDynamic_div").before(data);
          $("#moreDynamic_div").hide();
        }
        $("#load_preloader").hide();
      }, 1000);

    },
    error : function() {
      return false;
    }
  })
};

dynamic.addDelBynBut = function() {
  var $itemList = $(".main-list").find(".main-list__list[list-main='mobile_dyn_list']  .main-list__item");
  var itemReply = []
  if ($itemList.length > 0) {
    var $itemListReverse = Array.from($itemList).reverse();
    $itemListReverse.forEach(function(obj, index) {
      if (index > 9) {
        return;
      }

      $(obj).find(".dynamic-post__time:not(:first)").remove();
      $(obj).find(".dynamic-post__time").click(function(event) {
        BaseUtils.doHitMore(event.currentTarget, 1000);
        dynamic.deleteDynTip(this);
      });
    });
  }
}

// 删除动态提示框
dynamic.deleteDynTip = function(obj) {
  if (obj) {
    Smate.confirm("提示", "要删除该条动态吗？", function() {
      dynamic.deleteDynamic(obj);
    }, ["确定", "取消"]);
  }
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
          scmpublictoast(dynCommon.delSuccess, 1000, 1);
        } else {
          scmpublictoast(dynCommon.delFail, 1000, 2);
        }
      });
    },
  });
}

dynamic.initDelBut = function() {
  var $itemList = $(".main-list").find(".main-list__list[list-main='mobile_dyn_list']  .main-list__item");
  var itemReply = []
  if ($itemList.length > 0) {
    var $itemListReverse = Array.from($itemList).reverse();
    $itemListReverse.forEach(function(obj, index) {
      var delDiv = $(obj).find(".dynamic-post__time");
      delDiv.addClass("pointer-events", "auto").addClass("display", "auto").addClass("pointer-events", "flex")
          .addClass("justify-content", "flex-end").addClass("align-items", "center");
      if (delDiv.text() != "" || delDiv.text() != null) {
        var time = delDiv.html();
        if (time != undefined) {
          delDiv.html(time.indexOf("more_horiz") != -1 ? time.substring(0, time.indexOf("more_horiz")) : time
              + "<i class='material-icons' style='font-size: 20px;margin-left: 4px;color: #333;'>more_horiz</i>")
        }
      }
    });
  }
}

// 检查资源删除状态， 1：已删除，0：未删除
dynamic.checkResDeleteStatus = function(resType, des3ResId) {
  var deleted = false;
  $.ajax({
    url : "/dyn/mobile/ajaxstatus",
    type : "post",
    data : {
      "resType" : resType,
      "des3ResId" : des3ResId
    },
    dataType : "json",
    async : false,
    success : function(data) {
      if (data.status == "success") {
        deleted = data.hasDeleted == 1 ? true : false;
      }
    },
    error : function(data) {
    }
  });
  return deleted;
}