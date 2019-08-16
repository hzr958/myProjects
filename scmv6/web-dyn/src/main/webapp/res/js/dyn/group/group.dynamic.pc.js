/**
 * 群组动态js
 */
var groupDynamic = groupDynamic ? groupDynamic : {};
/**
 * -----------------------------------------------------------
 */

groupDynamic.shareToIRIS = function(e) {
  $("body").css("overflow", "hidden");
  // $("#firstCategoryShown").find(".selector_dropdown_option").removeClass("selected").eq(0).addClass("selected");
  $("#id_fluctuate").show();
  groupDynamic.stopPropagation(e);
}
groupDynamic.cancleShare = function(obj) {
  $("body").css("overflow", "auto");
  // start 重置数据
  groupDynamic.shareDataReset(obj);
  // end
  // $("#id_fluctuate").hide();
  $("#id_fluctuate").click();
}

/*
 * 分享数据重置
 */
groupDynamic.shareDataReset = function(obj) {
  $("#id_share_textarea").val("");
  $("#id_chioce_group_name").val("");
  $("#id_textarea_friend_name").html("");
  $("#share_plugin_select_friends").find(".share_plugin_add_friends > .chips_normal").remove();
  /*
   * $("#id_share_ui_param").attr("shareType","1");//分享类型标识 1是到我的动态 2是到联系人 3是到群组 ---zzx
   * $("#share_plugin_select_group").hide(); $("#share_plugin_select_friends").hide();
   * $(obj).closest(".dialogs_whole").find(".selector_dropdown_value").children().removeClass("selected");
   * var topCategoryId = 1 ; var
   * categoryValue=$(obj).closest(".dialogs_whole").find(".selector_dropdown_value >
   * .selector_dropdown_option[val='"+topCategoryId+"']"); categoryValue.addClass("selected");
   */
}

groupDynamic.selectShareWay = function(obj, e) {

  var objOption = $(obj).parent().find(".selector_dropdown_collections");
  objOption.addClass("shown");
  groupDynamic.stopPropagation(e);
  $(document).click(function() {
    objOption.removeClass("shown");
  });
  $(".selector_dropdown_collections .selector_dropdown_option").mouseover(function() {
    $(this).addClass("hover");
  });
  $(".selector_dropdown_collections .selector_dropdown_option").mouseout(function() {
    $(this).removeClass("hover");
  });
  $(".selector_dropdown_collections .selector_dropdown_option").on(
      'click',
      function(e) {
        $(".selector_dropdown_collections .selector_dropdown_option").unbind('click');
        groupDynamic.stopPropagation(e);
        var topCategoryId = $(this).attr("val");
        var categoryValue = $(this).closest(".dialogs_whole").find(
            ".selector_dropdown_value > .selector_dropdown_option[val='" + topCategoryId + "']");
        if (topCategoryId == 1) {
          $("#id_share_ui_param").attr("shareType", "1");// 分享类型标识 1是到我的动态 2是到联系人 3是到群组 ---zzx
          $("#share_plugin_select_group").hide();
          $("#share_plugin_select_friends").hide();

        } else if (topCategoryId == 2) {
          $("#id_share_ui_param").attr("shareType", "2");// 分享类型标识 1是到我的动态 2是到联系人 3是到群组 ---zzx
          $("#share_plugin_select_group").hide();
          $("#share_plugin_select_friends").show();

        } else {
          $("#id_share_ui_param").attr("shareType", "3");// 分享类型标识 1是到我的动态 2是到联系人 3是到群组 ---zzx
          $("#id_share_ui_param").attr("code", "")// 置空缓存缓存数据------zzx
          // $("#id_chioce_group_name").val("");//置空缓存缓存数据------zzx---ajb不清空
          $("#share_plugin_select_group").show();
          $("#share_plugin_select_friends").hide();
        }

        categoryValue.addClass("selected");
        $(this).closest(".dialogs_whole").find(".selector_dropdown_value").children().not(categoryValue).removeClass(
            "selected");
        objOption.removeClass("shown");
      });
}

// 我的成果 ，群组成果
groupDynamic.selectPubSource = function(obj) {

  var objOption = $(obj).parent().find(".selector_dropdown_collections");
  objOption.addClass("shown");
  $(obj).find(".selector_dropdown_collections .selector_dropdown_option").mouseover(function() {
    $(this).addClass("hover");
  });
  $(obj).find(".selector_dropdown_collections .selector_dropdown_option").mouseout(function() {
    $(this).removeClass("hover");
  });
  $(obj).find(".selector_dropdown_collections > .selector_dropdown_option").click(
      function(e) {
        groupDynamic.stopPropagation(e);
        $(".selector_dropdown_collections > .selector_dropdown_option").unbind('click');
        // 初始化值
        $("#share_plugin_publist").empty();
        $("#select_page_no").val(1)
        $("#share_plugin_publist").scrollTop(0);

        var topCategoryId = $(this).attr("val");
        var categoryValue = $(this).closest(".dialogs_whole").find(
            ".selector_dropdown_value > .selector_dropdown_option[val='" + topCategoryId + "']");
        if (topCategoryId == 1) {
          // 1：是群组成果 ， 2是我的成果
          groupSelectPub.comeSelectGroupPub();
        } else if (topCategoryId == 2) {
          // 1：是群组成果 ， 2是我的成果
          groupSelectPub.comeSelectOwnerPub();
        }
        categoryValue.addClass("selected");
        $(this).closest(".dialogs_whole").find(".selector_dropdown_value").children().not(categoryValue).removeClass(
            "selected");
        objOption.removeClass("shown");
      });
}
// ----------------------------------------------------------------------------

// 进入动态发布页面
groupDynamic.postnewshare = function(obj) {
  var data = {
    "des3GroupId" : $("#des3GroupId").val()
  };
  ajaxparamsutil.doAjax("/dynweb/dynamic/groupdyn/ajaxpublishpage", data, groupDynamic.publishpagecall, "json");
}
// 进入发布动态--回调
groupDynamic.publishpagecall = function(data) {
  if ("success" == data.result) {
    $("#publishpage").show();
    $("body").css("overflow", "hidden");
    $("#first_name").html($("#currentPersonZhName").val());
    $("#first_institution").html($("#currentPersonInsname").val());
    // 触发防止点击事件，外穿的方法。
    // $("#publishpage").find(".share_div").disabled();
  } else {
    scmpublictoast("发布动态异常", 1000);
  }
}

// 发布分享动态
groupDynamic.publishshare = function(obj) {
  groupDynamic.doHitMore(obj, 2000);
  var dynamicContent = $("#first_dynamicContent").val();
  var des3PubId = $("#select_pub_id").val();
  if (dynamicContent == undefined || $.trim(dynamicContent) == "" && $.trim(des3PubId) == "") {
    scmpublictoast("分享内容不能为空", 1000);
    return;
  }
  var tempType = "PUBLISHDYN";
  var resType = "";
  if (des3PubId != null && $.trim(des3PubId) != "") {
    resType = "pub";
  }
  var data = {
    "des3GroupId" : $("#des3GroupId").val(),
    "dynContent" : dynamicContent,
    "des3ResId" : des3PubId,
    "tempType" : tempType,
    "resType" : resType
  };
  ajaxparamsutil.doAjax("/dynweb/dynamic/ajaxgroupdyn/dopublish", data, groupDynamic.publishsharecall, "json");
}
// 发布分享动态 回调
groupDynamic.publishsharecall = function(data) {
  if ("success" == data.result) {
    scmpublictoast(groupDynCommon.postSuccess, 1000);
    groupDynamic.deletework();
    Group.selectModule($("#des3GroupId").val(), 'brief');
  } else {
    scmpublictoast(groupDynCommon.postFail, 1000);
  }
}

// 取消分享动态
groupDynamic.deletework = function() {
  $("#publishpage").hide();
  $("body").css("overflow", "visible");
}

// 群组动态 阻止冒泡事件
groupDynamic.stopPropagation = function(e) {
  if (e && e.stopPropagation) {// 非IE
    e.stopPropagation();
  } else {// IE
    window.event.cancelBubble = true;
  }
}

// 群组动态赞
groupDynamic.award = function(dynId) {
  var data = {
    "dynId" : dynId
  };
  ajaxparamsutil.doAjax("/dynweb/dynamic/groupdyn/ajaxaward", data, groupDynamic.awardCall, "json");
}
// 群组动态赞 回调
groupDynamic.awardCall = function(data, dataJson) {
  if ("success" == data.result) {
    var $like = $("#group_dyn_id_" + dataJson.dynId), lang = $("#locale_language").val(), like = "赞", unlike = "取消赞";
    if (lang && lang.indexOf("en") > 0) {
      like = "LIKE", unlike = "UNLIKE";
    }
    if (!!data.hasAward) {
      $like.text(unlike + "(" + data.awardCount + ")");
    } else {
      if (data.awardCount > 0) {
        $like.text(like);
      } else {
        $like.text(like + "(" + data.awardCount + ")");
      }
    }
  }
}

// 发布评论

groupDynamic.publishCommnet = function(obj, dynId) {
  groupDynamic.doHitMore(obj, 2000);
  var commentContent = $("#comment_content_" + dynId).val();
  if (commentContent == undefined || $.trim(commentContent) == "") {
    return;
  }
  var data = {
    "dynId" : dynId,
    "commentContent" : commentContent
  };

  $.ajax({
    url : "/dynweb/dynamic/groupdyn/ajaxcomment",
    dataType : "json",
    type : "post",
    data : data,
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(groupDynCommon.timeout, groupDynCommon.tipTitle, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }
      if ("success" == data.result) {
        var objCount = $(obj).parent().parent().parent().find(".dynamic_operations_container").children().eq(1);
        var count = objCount.text().replace(/[^0-9]/ig, "");
        var language = $("#locale_language").val();
        if ("en_US" == language) {
          objCount.text("COMMENT(" + (Number(count) + 1) + ")");
        } else {
          objCount.text("评论(" + (Number(count) + 1) + ")");
        }
        $(obj).parent().parent().find("textarea").val("");
        groupDynamic.clickComments(dynId, $(obj).parent()[0], 1);
      }
      $(obj).closest(".dynamic_comment_action").find(".textarea_autoresize").css("height", "22px");
    }
  });
}
