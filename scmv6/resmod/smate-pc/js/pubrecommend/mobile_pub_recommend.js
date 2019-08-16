var PubRecommend = PubRecommend ? PubRecommend : {}
var recommendReq;
var collectReq;
// 推荐显示左边查询条件
PubRecommend.ajaxgetconditions = function() {
  $.ajax({
    url : "/pubweb/mobile/ajaxconditions",
    type : "post",
    dataType : "html",
    data : "",
    success : function(data) {
      $("#select").html(data);
      PubRecommend.ajaxLondPubList();// 显示数据
    }
  });
}
PubRecommend.ajaxLondPubList = function() {
  var defultArea = $("#defultArea").val();
  var defultKey = $("#defultKey").val();
  var defultPubYear = $("#defultPubYear").val();
  var defultPubType = $("#defultPubType").val();

  var searchArea = $("#searchArea").val();
  var searchPsnKey = $("#searchPsnKey").val();
  var searchPubYear = $("#searchPubYear").val();
  var searchPubType = $("#searchPubType").val();
  $("#searchPdwh").val("0");
  document.getElementsByTagName('body')[0].scrollTop = 0;
  $(".main-list__list").empty();
  window
      .Mainlist({
        name : "mobile_pub_list",
        listurl : "/pub/pubrecommend/ajaxpubList",
        listdata : {
          "defultArea" : defultArea,
          "defultKeyJson" : defultKey,
          "searchArea" : searchArea,
          "searchPsnKey" : searchPsnKey,
          "searchPubYear" : searchPubYear,
          "searchPubType" : searchPubType,
        },
        method : "scroll",
        listcallback : function(xhr) {
          var curPage = $("#curPage").val();
          var totalPages = $("#totalPages").val();
          if (Number(curPage) >= Number(totalPages)) {
            $(".main-list__list")
                .append(
                    "<div class='paper_content-container_list main-list__item main-list__item-nonebox' style='border:none; border-bottom:0px!important;'><div class='main-list__item-nonetip'>没有更多记录</div></div>");
          } else {
            $("#curPage").remove();
            $("#totalPages").remove();
          }
          const parentNode = $("div[list-main='mobile_pub_list']");
          var norecord = parentNode.find("div.response_no-result");
          if (norecord.length > 0) {
            var tips = norecord[0].innerText;
            parentNode.find("div.response_no-result").remove();
            parentNode
                .append("<div class='paper_content-container_list main-list__item' style='border:0'><div class='response_no-result'>"
                    + tips + "</div></div>");
          }
          PubRecommend.paperPullDown();
        }
      });
}
// 下拉刷新
var startPosY, endPosY;
PubRecommend.paperPullDown = function() {
  // 支持touch事件?
  isAndroid = (/android/gi).test(navigator.appVersion), isIDevice = (/iphone|ipad/gi).test(navigator.appVersion),
      isTouchPad = (/hp-tablet/gi).test(navigator.appVersion), hasTouch = 'ontouchstart' in window && !isTouchPad;
  var START_EV = hasTouch ? 'touchstart' : 'mousedown', MOVE_EV = hasTouch ? 'touchmove' : 'mousemove', END_EV = hasTouch
      ? 'touchend'
      : 'mouseup', CANCEL_EV = hasTouch ? 'touchcancel' : 'mouseup';
  document.addEventListener(START_EV, PubRecommend.getStartPos, false);
  document.addEventListener(MOVE_EV, PubRecommend.refreshList, false);
}
PubRecommend.getStartPos = function(e) {
  var touch = e.touches[0];
  startPosY = Number(touch.pageY);
}
PubRecommend.refreshList = function(e) {
  var START_EV = hasTouch ? 'touchstart' : 'mousedown', MOVE_EV = hasTouch ? 'touchmove' : 'mousemove';
  var touch = e.touches[0];
  var y = Number(touch.pageY);
  if (y - startPosY > 50) {
    // 下拉刷新后移除监听
    document.removeEventListener(START_EV, PubRecommend.getStartPos);
    document.removeEventListener(MOVE_EV, PubRecommend.refreshList);
    if ($("#searchPdwh").val() == "1") {
      var searchString = $("#searchString").val();
      PubRecommend.searchPdwhPub(searchString ? searchString : "")
    } else {
      PubRecommend.ajaxLondPubList();
    }
  }
}
// 推荐选择方法
PubRecommend.changCondition = function(obj) {
  if ($(obj).hasClass("selector__list-target")) {
    $(obj).removeClass("selector__list-target");
    return;
  }
  var typeclass = $(obj)
  if ($(obj).hasClass("type_time") || $(obj).hasClass("type_area")) {// 出版时间、科技领域是单选的
    $(obj).closest(".provision_container-body_item").children(".div_item").removeClass("selector__list-target");
  }
  if ($(obj).hasClass("type_key") && $(obj).attr("value") == "") {
    $(".type_key").removeClass("selector__list-target");
  } else if ($(obj).hasClass("type_key") && $(".type_key[value='']").hasClass("selector__list-target")) {
    $(".type_key[value='']").removeClass("selector__list-target");
  }
  $(obj).addClass("selector__list-target");

}
// 推荐取消选择方法
/*
 * PubRecommend.delCondition = function(obj,typeclass){ $(obj).removeClass("selector__list-target");
 * $(obj).attr('onclick','PubRecommend.addCondition(this,\''+typeclass+'\')'); }
 */
PubRecommend.canclbtnClick = function() {
  $(".div_item").removeClass("selector__list-target");
  $("#searchArea").val("");
  $("#searchPsnKey").val("");
  $("#searchPubYear").val("");
  $("#searchPubType").val("");
  $(".type_area").attr("onclick", "PubRecommend.addCondition(this,'type_area')");
  $(".type_key").attr("onclick", "PubRecommend.addCondition(this,'type_key')");
  $(".type_time").attr("onclick", "PubRecommend.addCondition(this,'type_time')");
  $(".type_pub").attr("onclick", "PubRecommend.addCondition(this,'type_pub')");

}

// 基准库成果详情赞操作
PubRecommend.pdwhAward = function(des3PubId, dbid, obj) {
  var isAward = $(obj).attr("isAward");
  var count = Number($(obj).find('.dev_pub_award:first').text().replace(/[\D]/ig, ""));
  $.ajax({
    url : "/pubweb/details/ajaxpdwhaward",
    type : "post",
    dataType : "json",
    data : {
      "des3PubId" : des3PubId,
      "isAward" : isAward,
      "dbid" : dbid
    },
    success : function(data) {
      PubRecommend.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (isAward == 1) {// 取消赞
            $(obj).attr("isAward", 0);
            $(obj).find('.paper_footer-tool:first').removeClass("paper_footer-award_unlike");
            $(obj).find('.paper_footer-tool:first').addClass("paper_footer-fabulous");
            count -= 1;
            if (count == 0) {
              $(obj).find('.dev_pub_award:first').text(pubRecommend.like);
            } else {
              $(obj).find('.dev_pub_award:first').text(pubRecommend.like + "(" + count + ")");
            }
            // $(obj).find('.dev_pub_award:first').removeClass("paper_footer-tool__box-click");
          } else {// 赞
            $(obj).attr("isAward", 1);
            $(obj).find('.paper_footer-tool:first').removeClass("paper_footer-fabulous");
            $(obj).find('.paper_footer-tool:first').addClass("paper_footer-award_unlike");
            count += 1;
            $(obj).find('.dev_pub_award:first').text(pubRecommend.unlike + "(" + count + ")");
            // $(obj).find('.dev_pub_award:first').removeClass("paper_footer-tool__box-click");
            // $(obj).find('.dev_pub_award:first').addClass("paper_footer-tool__box-click");
          }
        }
      });
    }
  });
};
// 基准库快速分享
PubRecommend.quickShareDyn = function(obj) {
  var des3ResId = $(obj).attr("des3ResId");
  var dbId = $(obj).attr("dbId");
  var data = {
    "dynType" : "B2TEMP",
    "resType" : 24,
    "des3PubId" : des3ResId,
    "operatorType" : 3,
    "dbId" : dbId,
    "databaseType" : 2,
    "des3ResId" : des3ResId
  };
  $.ajax({
    url : "/dynweb/dynamic/ajaxrealtime",
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      if (data.result == "success") {
        scmpublictoast(pubRecommend.shareSuccess, 2000);
        var shareContentObj = $(".span_share[resid='" + des3ResId + "']");
        var text = shareContentObj.text();
        var count = 0;
        if (text != "") {
          count = $.trim(text.replace(/[^0-9]+/g, ''));
        }
        count = parseInt(count) ? parseInt(count) + 1 : 1;
        if (count > 0 && count <= 999) {
          shareContentObj.text("分享 (" + count + ")");
        }
        if (count > 999) {
          shareContentObj.text("分享 (1k+)");
        }
      } else {
        scmpublictoast(pubRecommend.shareFail, 2000);
      }

    },
    error : function(e) {
      scmpublictoast(pubRecommend.shareFail, 2000);
    }
  });
}
// 超时处理
PubRecommend.ajaxTimeOut = function(data, myfunction) {
  var toConfirm = false;
  if ('{"ajaxSessionTimeOut":"yes"}' == data) {
    toConfirm = true;
  }
  if (!toConfirm && data != null) {
    toConfirm = data.ajaxSessionTimeOut;
  }
  if (toConfirm) {
    jConfirm(pubi18n.i18n_timeout, pubi18n.i18n_tipTitle, function(r) {
      if (r) {
        document.location.href = window.location.href;
        return 0;
      }
    });
  } else {
    if (typeof myfunction == "function") {
      myfunction();
    }
  }
};

/*
 * PubRecommend.addListElement = function(listJson,addStr){ if(!addStr || addStr == ""){ return; }
 * var list = new Array();; if(listJson){ list=eval(listJson); var index = $.inArray(addStr, list);
 * if(index < 0){ list.push(addStr); } }else{ list.push(addStr); } return(JSON.stringify(list)); }
 */
PubRecommend.deleteListElement = function(listJson, addStr) {
  if (!addStr || addStr == "" || !listJson) {
    return;
  }
  var list = new Array();;
  list = eval(listJson);
  var index = $.inArray(addStr, list);
  if (index >= 0) {
    list.splice(index, 1);
  }
  return (JSON.stringify(list));
}
PubRecommend.searchPdwhPub = function(searchString) {
  $("#searchString").val(searchString);
  $("#searchPdwh").val("1");
  $("#content_pub_list").find(".main-list__list").empty();
  window
      .Mainlist({
        name : "mobile_pub_list",
        listurl : "/pubweb/pdwhpaper/search/ajaxmobilepublist",
        listdata : {
          "searchString" : searchString,
        },
        method : "scroll",
        beforeSend : function() {
          $(".main-list__list").doLoadStateIco({
            status : 1,
            addWay : "append"
          });
        },
        listcallback : function(xhr) {
          var $_ul = $("*[list-main='mobile_pub_list']");
          if ($(".main-list__list").attr("total-count") == 0
              || $(".main-list__list").attr("total-count") == 'undifined') {
            $_ul.find(".response_no-result").remove();
            $_ul
                .append("<div class='paper_content-container_list main-list__item' style='border:0'><div class='response_no-result' style='text-align:center'>"
                    + pubRecommend.noRecord + "</div></div>");
          } else if ($_ul.find(".main-list__item").length == $(".main-list__list").attr("total-count")
              && $_ul.find(".response_no-result").length == 0) {
            $_ul
                .append("<div class='paper_content-container_list main-list__item' style='border:0'><div class='response_no-result' style='text-align:center'>"
                    + pubRecommend.noMoreRecord + "</div></div>");
          }
          PubRecommend.paperPullDown();
        }
      });
};
/**
 * 监听移动端软键盘的回车搜索按键
 */
PubRecommend.listenInput = function(e) {
  if ($.trim($("#searchStringInput").val()) != "") {
    var event = e.which || e.keyCode;
    if (event == 13) {
      var searchKey = $(".paper__func-box input").val();
      PubRecommend.searchPdwhPub($("#searchStringInput").val());
    }
  }
}

// 进入发现页面
PubRecommend.findPub = function() {
  window.location.href = "/pubweb/mobile/findpub/area?searchArea" + $("#searchArea").val();
}

// 加载发现页面成果列表
var $mobileLoadFindPub;
PubRecommend.ajaxFindPubList = function() {
  document.getElementsByTagName('body')[0].scrollTop = 0;
  $(".main-list__list").empty();
  // 保存过滤条件，请求列表页
  var includeType = $("#includeType").val(),
  // TODO 收录类别、科技领域
  pubDBIds = $("#pubDBIds").val(), scienceAreaIds = $("#scienceAreaIds").val(), publishYear = $
      .trim($("#searchPubYear").val()), pubType = $.trim($("#searchPubType").val()), searchString = $.trim($(
      "#searchStringInput").val()), orderBy = $.trim($("#orderBy").val()), $mobilepaperList = window.Mainlist({
    name : "mobile_find_pub_list",
    listurl : "/pub/querylist/ajaxpdwhpub",
    listdata : {
      "publishYear" : publishYear,
      "searchPubType" : pubType,
      "des3AreaId" : scienceAreaIds,
      "searchString" : searchString,
      "includeType" : pubDBIds,
      "orderBy" : orderBy
    },
    method : "scroll",
    listcallback : function(xhr) {
      var currentNumItem = $(".main-list__item").length;
      var totalCount = $(".main-list__list").attr("total-count");
      if (totalCount>0 && Number(currentNumItem) >= Number(totalCount)) {
        $(".main-list__list")
            .append(
                "<div class='paper_content-container_list main-list__item main-list__item-nonebox'  style='border:none; border-bottom:0px!important;'><div class='main-list__item-nonetip'>没有更多记录</div></div>");
      }
      if (totalCount <= 0) {
        $("div.response_no-result").remove();
        $(".main-list__list").append("<div class='paper_content-container_list main-list__item' style='border:0'><div class='response_no-result'>未找到符合条件的记录</div></div>");
      }
    }
  });
}

// 初始化操作
PubRecommend.initPubOperation = function() {
  var pubIdsArr = new Array();
  // 取到要初始化的成果加密ID
  $(".pub_item:not(.not_need_init)").each(function() {
    pubIdsArr.push($(this).attr("des3PubId"));
  });
  var data = {
    "des3PubIds" : pubIdsArr.join(",")
  };
  $.ajax({
    url : "/pubweb/mobile/findpub/ajaxinitopt",
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      if (data.result == "success" && "optData" in data) {
        // 标记不用再初始化了
        $(".pub_item:not(.not_need_init)").each(function() {
          $(this).addClass("not_need_init");
        });
        var operateData = data.optData;
        if (operateData != null && operateData.length > 0) {
          for (var i = 0; i < operateData.length; i++) {
            var optItem = operateData[i];
            var des3PubId = optItem.des3PubId;
            var pubItem = $(".pub_item[des3PubId='" + des3PubId + "']");
            // 处理赞
            var pubAwardSpan = pubItem.find(".pub_award_span");
            if (optItem.isAward == 1) {
              pubAwardSpan.attr("isAward", 1);
              pubAwardSpan.find('.paper_footer-tool:first').removeClass("paper_footer-fabulous");
              pubAwardSpan.find('.paper_footer-tool:first').addClass("paper_footer-award_unlike");
              pubAwardSpan.find('.dev_pub_award:first').text(pubRecommend.unlike + "(" + optItem.awardCount + ")");
            } else {
              pubAwardSpan.find('.dev_pub_award:first').text(pubRecommend.like + "(" + optItem.awardCount + ")");
            }
            // 处理评论
            pubItem.find('.pub_comment_span .dev_pub_comment:first').text("评论(" + optItem.commentCount + ")");
            // 处理分享
            pubItem.find(".dev_pub_share:first").text("分享(" + optItem.shareCount + ")");
            // 处理收藏
            if (optItem.isCollection == 1) {
              var collectSpan = pubItem.find(".dev_pub_collected");
              collectSpan.attr("collected", "1");
              collectSpan.find("i").removeClass("paper_footer-comment ").addClass("paper_footer-comment__flag");
              collectSpan.find("span").text(Pubsearch.unsave);
            }
            // 处理全文
            var pubFullTextSpan = pubItem.find(".pub_fulltext_span");
            if ("des3FileId" in optItem) {
              pubFullTextSpan.find("img").attr("src", optItem.fulltextImagePath);
              pubFullTextSpan.find("a").attr("href", optItem.downloadUrl);
            }
          }
        }
      }
    },
    error : function(e) {

    }
  });
}
