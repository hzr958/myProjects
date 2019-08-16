var GrpDiscuss = GrpDiscuss ? GrpDiscuss : {};

GrpDiscuss.showPublish = function() {
  $("#publish_box").css("display", "flex");
  $("#publish_notic").hide();
  $("#publish_option").show();
  $("#publish_text").focus();
  GrpDiscuss.PublishInterval = setInterval(function() {// 确定按钮的监听
    try {
      GrpDiscuss.is_canshare();
    } catch (err) {
    }
  }, 1000);
};

// 文件发送按钮的disabled设置
GrpDiscuss.is_canshare = function() {
  var text = $.trim($("#grp_share_card").find("textarea").val()).length;
  var pubId = $("#grp_share_card").find(".aleady_select_pub").attr("pubId").length;
  if (text > 0 || pubId > 0) {
    $("#grp_share_card").find(".button_primary").removeAttr("disabled");
  } else {
    $("#grp_share_card").find(".button_primary").attr("disabled", true);
  }
}

GrpDiscuss.showSelectPubBox = function(obj) {
  $(obj).parent().parent().find(".select_res_box").addClass("show_selected_pub_info");
  $("#select_pub_search_box").find("input").val("");
  GrpSelectPub.showSelectPubBox();
};

/**
 * @author houchuanjie 群组发表的动态中含有网址的话展示时显示网页链接
 */
GrpDiscuss.transUrl = function() {

  var dynboxes = $('[list-main=grp_discuss_list]').find('.dynamic-main__box');

  dynboxes.each(function() {
    var $div = $(this).children('.dyn_content').eq(0);
    if (!$div.attr('transurl')) {
      var c = $div.html().trim();
      var matchArray = dynamic.matchUrl(c);
      // console.log(c);
      var newstr = "";
      for (var i = 0; i <= matchArray.length; i++) {
        var beginIndex = i == 0 ? 0 : matchArray[i - 1].lastIndex;
        var endIndex = i == matchArray.length ? c.length : matchArray[i].index;
        var stri = c.substring(beginIndex, endIndex);
        var urli = "";
        if (i < matchArray.length) {
          urli = " <a href=\"" + matchArray[i].str + "\" style=\"color: #005eac !important;\" target=\"_blank\">"
              + matchArray[i].str + "</a> ";
        }
        newstr += stri + urli;
      }
      $div.html(newstr);
      $div.attr('transurl', 'true');
    }
  });
};
GrpDiscuss.showGrpDiscussList = function(showGrpDiscussMainRole) {
  if ($("div[list-main='grp_discuss_list']").length == 0) {
    return;
  }
  window.Mainlist({
    name : "grp_discuss_list",
    listurl : "/dynweb/dynamic/grpdyn/ajaxshow",
    listdata : {
      'des3GroupId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    listcallback : function(xhr) {
      // 更新全文图片信息
      GrpDiscuss.updateFullTextImage();
      // 弹出框
      GrpDiscuss.initDynCollectStatus();
      GrpDiscussComment.loadSampleDiscussComment();
      GrpDiscussList.showDynsTime();
      GrpDiscussList.showDynsOperation();
      if (showGrpDiscussMainRole == 1 || showGrpDiscussMainRole == 2 || showGrpDiscussMainRole == 3) {
        $("#grpDiscussListId").find(".response_no-result").html(grpDiscuss.noDiscuss);
      } else {
        $("#grpDiscussListId").find(".response_no-result").html(grpDiscuss.isNotGrpMemberNoDiscuss);
      }
      var $_ul = $("*[list-main='grp_discuss_list']");
      if ($_ul.find(".main-list__item").length == $_ul.find(".js_listinfo").attr("smate_count")
          && $_ul.find(".response_no-result").length == 0) {
        $_ul.append("<div class='main-list__item global__padding_0 '><div class='response_no-result'>"
            + grpDiscuss.noMoreRecord + "</div></div>");
      }
      GrpDiscuss.transUrl();
      // 更新全文请求提示信息
      GrpDiscuss.updateFullTextTitle();
      GrpDiscuss.getfullTextUrl();
      PCAgency.ajaxInitGrpDynInterestStatus();

    },
    method : "scroll"
  });

};
GrpDiscuss.updateFullTextImage = function() {
  $(".dev_pub_fulltext").find("img").each(function(index, element) {
    var $this = $(element).parent();
    var $inputs = $this.closest(".main-list__item").find("input[name='dynId']");
    // 设置des3FileId的值,pdwhPub不要设置权限
    if ($inputs.attr("restype") != "pdwhpub") {
      $this.closest(".dev_pub_fulltext").attr("des3FileId", $($inputs).parent().attr("resFullTextFileId"));
      $this.closest(".dev_pub_fulltext").attr("permission", $($inputs).parent().attr("permission"));
    } else {
      $this.closest(".dev_pub_fulltext").attr("permission", "0");
    }
    // $(element).attr("src",$($inputs).parent().attr("resFullTextImage"));
  });
};

GrpDiscuss.getfullTextUrl = function() {
  $("div[list-main='grp_discuss_list']").find(".main-list__item").each(function(i, o) {
    var resfulltextimage = $(o).find(".container__card").find(".dynamic__box").attr("resfulltextimage");
    var $img = $(o).find(".dynamic-content__main").find("img");
    if (resfulltextimage != null && resfulltextimage != '') {
      $img.attr("src", resfulltextimage);
    }
  });
}

GrpDiscuss.updateFullTextTitle = function() {
  // 进行提示 全文请求，上传全文，下载全文的增加
  $(".dynamic-main__att").find("img").each(function(index, element) {
    var $this = $(element).parent();
    var $inputs = $this.closest(".main-list__item").find("input[name='dynId']");
    var resownerdes3id = $inputs.eq($inputs.length - 1).attr("resownerdes3id");
    var des3FileId = $this.closest(".dev_pub_fulltext").attr("des3FileId");
    var permission = $this.closest(".dev_pub_fulltext").attr("permission");
    var userId = $("#userId").val();
    var restype = $inputs.eq($inputs.length - 1).attr("restype");
    if (restype == "pub") {
      // SCM-24197 yhx 2019/3/28
      var des3ResId = $inputs.eq($inputs.length - 1).attr("resid");
      var des3OwnerId = GrpDiscuss.getSnsPubOwner(des3ResId);
      if (des3OwnerId != null) {
        resownerdes3id = des3OwnerId;
      }
    }
    if (des3FileId) {// 全文存在则下载
      if (permission == 0 || userId == resownerdes3id) {
        $(element).next().attr("title", grpDiscuss.downloadFullText);
        $(element).next().removeClass("pub-idx__full-text_newbox-box");
        $(element).next().addClass("pub-idx__full-text_newbox-box_load");
        // $(element).next().css("display", "none");
      } else {
        $(element).next().attr("title", grpDiscuss.requestFullText);
      }
    } else {
      if (userId == resownerdes3id) {// 自己的成果，则上传全文
        $(element).next().attr("title", grpDiscuss.uploadFullText);
      } else if (resownerdes3id != "") {// 不是自己的，则全文请求
        $(element).next().attr("title", grpDiscuss.requestFullText);
      }
    }

  });
}

GrpDiscuss.getSnsPubOwner = function(des3ResId) {
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

GrpDiscuss.initDynCollectStatus = function() {
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

// 站外
;
GrpDiscuss.showGrpOutsideDiscussList = function() {
  if ($("div[list-main='grp_discuss_list']").length == 0) {
    return;
  }
  window.Mainlist({
    name : "grp_discuss_list",
    listurl : "/dynweb/grpdyn/outside/ajaxshow",
    listdata : {
      'des3GroupId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    listcallback : function(xhr) {
      GrpDiscuss.updateFullTextImage();
      // 弹出框
      GrpDiscussComment.loadSampleDiscussComment("/dynweb/grpdyn/outside/ajaxgetsamplecomment");
      GrpDiscussList.showDynsTime("outside");
      // 更新全文请求提示信息
      GrpDiscuss.updateFullTextTitle();
      GrpDiscuss.getfullTextUrl();
    },
    method : "scroll"
  });

};

GrpDiscuss.imgEventFunction = function() {
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
  });
};

;
GrpDiscuss.showGrpDesc = function(url) {
  var tarurl = url;
  if (!url) {
    tarurl = "/groupweb/grpdiscuss/ajaxgrpbrief";
  }
  $("#grp_discuss_desc").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  $.ajax({
    url : tarurl,
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'grpCategory' : $("#grp_params").attr("smate_grpcategory")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if ($("#grp_discuss_desc")) {
          $("#grp_discuss_desc").html(data);
        }
      });

    },
    error : function() {
    }
  });

};

;
GrpDiscuss.showGrpFiveMember = function(url) {
  var tarurl = url;
  if (!url) {
    tarurl = "/groupweb/grpdiscuss/ajaxgrpmembers";
  }
  $("#grp_discuss_member").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  $.ajax({
    url : tarurl,
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if ($("#grp_discuss_member")) {
          $("#grp_discuss_member").html(data);
        }
      });

    },
    error : function() {
    }
  });

};

;
GrpDiscuss.showGrpFivePub = function(url) {
  var tarurl = url;
  if (!url) {
    tarurl = "/groupweb/grpdiscuss/ajaxgrppubs";
  }
  $("#grp_discuss_pub").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  $.ajax({
    url : tarurl,
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_discuss_pub").html(data);
      });

    },
    error : function() {
    }
  });

};

GrpDiscuss.showOtherInfo = function(url) {
  var tarurl = url;
  if (!url) {
    tarurl = "/groupweb/grpdiscuss/ajaxgrpotherinfo";
  }
  $("#grp_discuss_other").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  $.ajax({
    url : tarurl,
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_discuss_other").html(data);
        $("#grp_discuss_other").css("display", "block");
      });

    },
    error : function() {
    }
  });
}

GrpDiscuss.publishshare = function(obj) {
  $(obj).attr("disabled", true);
  var dynamicContent = $("#publish_text").val();
  if ($.trim(dynamicContent).length > 500) {
    scmpublictoast("讨论内容不能超过500个字符", 1000);
    return;
  }
  $("#publish_text").val(""); // 避免重复点击

  var des3PubId = $(obj).parent().parent().find(".aleady_select_pub").attr("des3PubId");
  if ((dynamicContent == undefined || $.trim(dynamicContent) == "") && des3PubId == "") {
    $(obj).attr("disabled", false);
    scmpublictoast(grpDiscuss.notEmpty, 2000);
    return;
  }
  dynamicContent = dynamicContent.replace(/&lt;(.*)&gt;.*&lt;\/\1&gt;|&lt;(.*) \/&gt;/, "&_lt;$1&_gt;").replace(/</g,
      "&lt;").replace(/>/g, "&gt;").replace(/\n/g, '<br>');// SCM-17958
  var tempType = "GRP_PUBLISHDYN";
  var resType = "";
  if (des3PubId != null && $.trim(des3PubId) != "") {
    resType = "pub";
  }
  var data = {
    'des3GroupId' : $("#grp_params").attr("smate_des3_grp_id"),
    "dynContent" : dynamicContent,
    "des3ResId" : des3PubId,
    "tempType" : tempType,
    "resType" : resType
  };

  $.ajax({
    url : '/dynweb/dynamic/ajaxgroupdyn/dopublish',
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        scmpublictoast(grpDiscuss.shareSuccess, 1000);
        $("#publish_text").val("");
        $("#id_grpdyn_content_length").html("0");
        GrpSelectPub.clearSelectedPub($(obj).parent().parent().find(".clear_selected_pub"));
        GrpDiscuss.showGrpDiscussList();

        GrpDiscuss.resetPublishshare();
      });

    },
    error : function() {
      scmpublictoast(grpDiscuss.shareFail, 2000);
    }
  });
  $(obj).attr("disabled", false);
};

// 重置发布动态框
GrpDiscuss.resetPublishshare = function() {
  $("#publish_box").find(".input__box  >  .input__area").css("height", "26px");
  $("#publish_notic").css("display", "");
  $("#publish_box").css("display", "none");
  $("#publish_option").hide();
}
