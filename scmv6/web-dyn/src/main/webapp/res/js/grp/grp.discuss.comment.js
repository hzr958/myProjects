/**
 * 群组动态评论js
 */
var GrpDiscussComment = GrpDiscussComment ? GrpDiscussComment : {};
// 超时处理
GrpDiscussComment.ajaxTimeOut = function(data, myfunction) {
  var toConfirm = false;
  toConfirm = data.ajaxSessionTimeOut;
  if ('{"ajaxSessionTimeOut":"yes"}' == data) {
    toConfirm = true;
  }
  if (toConfirm) {
    jConfirm(discussCommon.sessionTimeout, discussCommon.tips, function(r) {
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
}

GrpDiscussComment.showSelectPubBox = function(obj) {
  $(obj).parent().parent().find(".comment_res_box").addClass("show_selected_pub_info");
  $("#select_pub_search_box").find("input").val("");
  GrpSelectPub.showSelectPubBox();
}

GrpDiscussComment.loadSampleDiscussComment = function(url) {
  var tarurl = url;
  if (!url) {
    tarurl = "/dynweb/dynamic/groupdyn/ajaxgetsamplecomment";
  }
  $(".load_sample_comment").each(function(i, obj) {
    $.ajax({
      url : tarurl,
      dataType : "html",
      type : "post",
      data : {
        des3DynId : $(obj).attr("des3DynId")
      },
      success : function(data) {
        GrpDiscussComment.ajaxTimeOut(data, function() {
          $(obj).find(".grp_discuss_sample_comment").html(data);
          $(obj).removeClass("load_sample_comment");
        })
      }
    });
  });

};

GrpDiscussComment.showDiscussCommentList = function(obj) {
  var resType = $(obj).closest(".main-list__item").find("input[name='dynId']").attr("restype");
  var des3resid = $(obj).closest(".main-list__item").find("input[name='dynId']").attr("des3resid");
  if (resType == "pdwhpub") {
    dynamic.pdwhIsExist(des3resid, function() {
      GrpDiscussComment.commentCommonPart(obj);
    });
  } else {
    GrpDiscussComment.commentCommonPart(obj);
  }

};
/**
 * 评论公共部分
 */
GrpDiscussComment.commentCommonPart = function(obj) {
  var boxObj = $(obj).parent().parent();
  var showObj = $(boxObj).find(".grp_discuss_comment_box");
  if ($(showObj).css("display") == "none") {
    $(showObj).show();
  } else {
    $(showObj).hide();
    return;
  }
  $(boxObj).find(".grp_discuss_sample_comment").hide();

  GrpDiscussComment.loadDiscussCommentList(obj);
}

// 站外
GrpDiscussComment.showOutsideDiscussCommentList = function(obj) {
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function() {
    var boxObj = $(obj).parent().parent();
    var showObj = $(boxObj).find(".grp_discuss_comment_box");
    if ($(showObj).css("display") == "none") {
      $(showObj).show();
    } else {
      $(showObj).hide();
      return;
    }
    $(boxObj).find(".grp_discuss_sample_comment").hide();
    GrpDiscussComment.loadDiscussCommentList(obj, "/dynweb/grpdyn/outside/ajaxdiscusscommentlist");
  }, 1);
};

GrpDiscussComment.loadDiscussCommentList = function(obj, url) {
  var tarurl = url;
  if (!url) {
    tarurl = "/dynweb/dynamic/groupdyn/ajaxdiscusscommentlist";
  }
  var des3DynId = $(obj).closest(".main-list__item").find("input[name='dynId']").val();
  var boxObj = $(obj).parent().parent();
  var showObj = $(boxObj).find(".grp_discuss_comment_box");
  $(showObj).doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  $.ajax({
    url : tarurl,
    dataType : "html",
    type : "post",
    data : {
      des3DynId : des3DynId
    },
    success : function(data) {
      GrpDiscussComment.ajaxTimeOut(data, function() {
        $(showObj).html(data);
        addFormElementsEvents($(showObj)[0]);
        $(showObj).find(".publish_comment_text").focus();
      });

    }
  });
};

GrpDiscussComment.commentDiscuss = function(obj) {
  $('#publish_button').attr("disabled", true);
  var commentContent = $(obj).parent().parent().find(".publish_comment_text").val();
  $(obj).parent().parent().find(".publish_comment_text").val(""); // 避免重复点击
  var pubId = $(obj).parent().parent().find(".aleady_select_pub").attr("pubId");
  if ((commentContent == undefined || $.trim(commentContent) == "") && pubId == "") {
    $('#publish_button').attr("disabled", false);
    scmpublictoast("请输入评论内容", 2000);
    return;
  }
  var pubId = $(obj).parent().parent().find(".aleady_select_pub").attr("pubId");
  var data = {
    "dynId" : $(obj).parents(".discuss_box").attr("dynId"),
    "commentContent" : commentContent,
    "commentResId" : pubId
  };
  // 同步成果评论start SCM-15238
  var des3ResId = $(obj).closest(".discuss_box").find("input[name='dynId']").attr("des3resid");
  var dyntype = $(obj).closest(".discuss_box").find("input[name='dynId']").attr("dyntype");
  var resType = $(obj).closest(".discuss_box").find("input[name='dynId']").attr("resType");
  if (des3ResId !== undefined && des3ResId !== "") {
    // ||"GRP_SHAREPDWHPUB"==dyntype
    if ("GRP_ADDPUB" == dyntype || "GRP_PUBLISHDYN" == dyntype || "GRP_SHAREPUB" == dyntype) {
      $.ajax({
        url : '/pub/opt/ajaxcomment',
        type : 'post',
        data : {
          "des3PubId" : des3ResId,
          "content" : commentContent
        },
        dataType : "json",
        success : function(data) {
        }
      });
    }
    if ("GRP_SHAREPDWHPUB" == dyntype) {
      GrpDiscussComment.pdwhComment(commentContent, des3ResId);
    }
  }
  if ("GRP_SHAREPRJ" == dyntype) {
    $.ajax({
      url : "/prjweb/project/ajaxaddcomment",
      type : 'post',
      data : {
        "des3PrjId" : des3ResId,
        "comment" : commentContent,
      },
      dataType : "json",
      success : function(data) {
      }
    });
  }

  // 同步评论end
  $.ajax({
    url : "/dynweb/dynamic/groupdyn/ajaxcomment",
    dataType : "json",
    type : "post",
    data : data,
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(discussCommon.sessionTimeout, discussCommon.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }
      if ("success" == data.result) {
        var objCount = $(obj).parents(".discuss_box").find(".comment_account").find("a");
        var count = objCount.text().replace(/[^0-9]/ig, "");
        objCount.text(discussCommon.comment + "(" + (Number(count) + 1) + ")");
        $(obj).parent().parent().find(".publish_comment_text").val("");
        GrpDiscussComment.loadDiscussCommentList($(obj).parents(".discuss_box").find(".comment_account"));
      }
    }
  });
  $('#publish_button').attr("disabled", false);
};

// 群组动态评论同步到基准库成果
GrpDiscussComment.pdwhComment = function(replyContent, des3PubId) {
  $.ajax({
    url : '/pub/opt/ajaxpdwhcomment',
    type : 'post',
    data : {
      "des3PdwhPubId" : des3PubId,
      "content" : replyContent
    },
    dataType : "json",
    success : function(data) {

    }

  });
}
