//显示所有简介
function showAllBrief() {
  $("#psn_brief_div").html($("#psn_brif_content").val());
  $("#psn_brief_div").addClass("app_psn-main_page-body_psninfor-allbrief");
  $("#psn_brief_div").removeClass("app_psn-main_page-body_psninfor-detail");
  $("#more_psn_brief").html("隐藏部分");
  $("#more_psn_brief").attr("onclick", "hideSomeBrief();");
}

// 隐藏部分简介
function hideSomeBrief() {
  var psnBrief = $("#some_psn_brif_content").val();
  if (psnBrief == null || typeof (psnBrief) == "undefined") {
    psnBrief = $("#psn_brief_content").val();
  }
  if (psnBrief != null && typeof (psnBrief) != "undefined") {
    $("#psn_brief_div").html(psnBrief.substring(0, 110) + "...");
  }
  $("#more_psn_brief").html("查看全部");
  $("#more_psn_brief").attr("onclick", "showAllBrief();");
  $("#psn_brief_div").addClass("app_psn-main_page-body_psninfor-detail");
  $("#psn_brief_div").removeClass("app_psn-main_page-body_psninfor-allbrief");
}

// 显示代表性成果
function showRepresentPub() {
  $("#mobile_represent_pub_div").hide();
  if ($("#load_represent_pub").val() == 1) {
    var data = {
      "des3SearchPsnId" : $("#des3PsnId").val()
    };
    $.ajax({
      url : "/pub/outside/querylist/represent",
      type : "post",
      dataType : "html",
      data : data,
      success : function(data) {
        if (data != null) {
          $("#mobile_represent_pub_div").html(data);
        }
        if ($(".dev_psn_represent_pub").length > 0) {
          $("#mobile_represent_pub_div").show();
        }
      },
      error : function() {
      }
    });
  }
}
// 显示代表性项目
function showRepresentPrj() {
  $("#mobile_represent_prj_div").hide();
  if ($("#load_represent_prj").val() == 1) {
    $.ajax({
      url : "/psnweb/outside/mobile/ajaxrepresentprj",
      type : "post",
      dataType : "html",
      data : {
        "des3PsnId" : $("#des3PsnId").val()
      },
      success : function(data) {
        if (data != null) {
          $("#mobile_represent_prj_div").html(data);
        }
        if ($(".dev_psn_represent_prj").length > 0) {
          $("#mobile_represent_prj_div").show();
        }
      },
      error : function() {
      }
    });
  }
}

// 跳转到项目列表
function showPrjs(obj) {
  var prjCount = $(obj).find("div:first").text().trim();
  var targetUrl = "/prjweb/wechat/prjmain?des3PsnId=" + $("#des3PsnId").val() + "&prjCount=" + prjCount;
  if ($("#outHomePage").val() == "true") {
    targetUrl = "/prjweb/outside/mobileotherprjs?des3PsnId=" + $("#des3PsnId").val() + "&prjCount=" + prjCount;
  }
  document.location.href = targetUrl;
}

// 跳转到成果列表
function showPubs(showName, obj) {
  /*if (showName.length >= 10) {
    showName = showName.substring(0, 9) + "...";
  }
  var othername = encodeURI(showName);*/
  var targetUrl = "/pub/outside/querylist/psn?pubSum=" + $("#pubSum").val();
  if ($("#outHomePage").val() == "true") {
    targetUrl = "/pub/outside/querylist/psn?des3SearchPsnId=" + $("#des3PsnId").val()
        + "&pubSum=" + $("#pubSum").val();
  }
  document.location.href = targetUrl;
}

// 跳转联系人列表
function showFriends() {
  var targetUrl = "/psnweb/mobile/friendlist";
  if ($("#outHomePage").val() == "true") {
    targetUrl = "/psnweb/outside/mobilefriendlist?des3PsnId=" + $("#des3PsnId").val();
  }
  document.location.href = targetUrl;
}

// 添加联系人请求发送成功
function addFriendSuccessFun(data) {
  if (data.result == "true") {
    scmpublictoast("联系人请求发送成功", 1000);
  } else {
    scmpublictoast(data.msg, 1000);
  }
}

// 添加联系人请求发送失败
function addFriendErrorFun(data) {
  scmpublictoast("系统繁忙", 1000);
}

// 关注此人
function addAttention(reqPsnId) {
  $.ajax({
    url : "/psnweb/psnsetting/ajaxSaveAttFrd",
    type : 'post',
    dataType : 'json',
    data : {
      'des3PsnIds' : reqPsnId
    },
    success : function(data) {
      if (data.action == "success" || data.action == "doNothing") {
        scmpublictoast("关注成功", 1000);
        $(".attention_btn").removeAttr("onclick");
        $(".attention_btn").attr("onclick", "cancelAttention('" + data.attPsnId + "','" + reqPsnId + "')");
        $(".attention_btn span").text("取消关注");
        $(".attention_btn i").attr("class", "app_psn-main_page-header_top-func_deletefollow");
      } else {
        scmpublictoast("关注失败", 1000);
      }
    },
    error : function() {
    }
  });
}
// 取消关注
function cancelAttention(attentionId, reqPsnId) {
  var param = {
    "userSettings.cancelId" : attentionId
  }
  $.ajax({
    url : "/psnweb/psnsetting/ajaxCancelAttPerson",
    type : 'post',
    dataType : 'json',
    data : param,
    success : function(data) {
      if (data.result == "success") {
        scmpublictoast("取消关注成功", 1000);
        $(".attention_btn").removeAttr("onclick");
        $(".attention_btn").attr("onclick", "addAttention('" + reqPsnId + "')");
        $(".attention_btn span").text("关注");
        $(".attention_btn i").attr("class", "app_psn-main_page-header_top-func_follow");
      } else {
        scmpublictoast("取消关注失败", 1000);
      }

    },
    error : function() {
    }
  });

}
// 取消联系人
function cancelFriend(des3FriendId) {
  $.ajax({
    url : "/psnweb/friend/ajaxdel",
    type : "post",
    dataType : 'json',
    data : {
      "friendPsnIds" : des3FriendId
    },
    success : function(data) {
      if (data.result == "success") {
        scmpublictoast("删除联系人成功", 1000);
        $("#cancelAttention").removeAttr("onclick");
        $("#cancelAttention").attr("onclick", "addAttention('" + des3FriendId + "')");
        $("#cancelAttention span").text("关注");
        $("#cancelAttention i").attr("class", "app_psn-main_page-header_top-func_follow");
        /*
         * $(".friend_req_btn").removeAttr("onclick");
         * $(".friend_req_btn").attr("onclick","SmateCommon.addIdentificFriend('" + des3FriendId +
         * "', this, addFriendSuccessFun, addFriendErrorFun)"); $(".friend_req_btn").text("加为联系人");
         */
        $("#more_operation_div").hide();
        $("#more_operation_back_div").hide();
        $("#cancelFrdBtn").hide();
        $("#addFrdBtn").show();
      } else {
        scmpublictoast("删除联系人失败", 1000);
      }
    },
    error : function() {
      scmpublictoast("网络繁忙", 1000);
    }

  });
}

// 显示二维码
function showQrcode() {
  $("#qrcode_div").show();
}
// 生成二维码
function getQrImg(url) {
  var img_height = $("#qrcode_img").height();
  var img_width = $("#qrcode_img").width();
  var options = {
    render : "canvas",
    ecLevel : 'H',// 识别度
    fill : '#000',// 二维码颜色
    background : '#ffffff',// 背景颜色
    quiet : 2,// 边距
    width : img_width,// 宽度
    height : img_height,
    text : url,// 二维码内容
    // 中间logo start
    mode : 4,
    mSize : 20 * 0.01,
    mPosX : 50 * 0.01,
    mPosY : 50 * 0.01,
    image : $('#qrCodeIco')[0],// logo图片
    // 中间logo end
    label : 'jQuery.qrcode',
    fontname : 'Ubuntu',
    fontcolor : '#ff9818',
  };
  $('#qrcode_img').empty().qrcode(options).hide();
  var canvas = $('#qrcode_img').find('canvas').get(0);
  $('#create_qrcode_img').attr('src', canvas.toDataURL('image/png'));
}

// 加载关键词信息
function showPsnKeywords() {
  $("#psn_keywords_div").hide();
  if ($("#load_psn_keywords").val() == 1) {
    $.ajax({
      url : "/psnweb/outside/mobile/ajaxkeywords",
      type : "post",
      dataType : "html",
      data : {
        "des3PsnId" : $("#des3PsnId").val()
      },
      success : function(data) {
        var isMyself = $("#isMyself").val();
        if (data != null) {
          $("#psn_keywords_div").html(data);
        }
        if ($(".mobile_keywords_item").length > 0 || isMyself == "true") {
          $("#psn_keywords_div").show();
          if (isMyself == "true") {
            $(".edit_icon_i").show();
          }
        }
      },
      error : function() {
      }
    });
  }
}

function showMoreOperation() {
  $("#more_operation_div").show();
  $("#more_operation_back_div").show();
}

function showMobileTip() {
  if ($("#hasLogin").val() == 0) {
    Smate.confirm("提示", "系统超时或未登录，你要登录吗？",
        function() {
          window.location.href = "/oauth/mobile/index?sys=wechat&service="
              + encodeURIComponent($("#loginTargetUrl").val());
        }, ["确定", "取消"]);
  }
}

// 认同关键词
function identifyKeyword(keyworId) {
  $
      .ajax({
        url : "/psnweb/keyword/ajaxIdentificKeyword",
        type : "post",
        dataType : "json",
        data : {
          "des3PsnId" : $("#des3PsnId").val(),
          "oneKeyWordId" : keyworId
        },
        success : function(data) {
          BaseUtils
              .ajaxTimeOutMobile(
                  data,
                  function() {
                    if (data.result == "success") {
                      $("#idenSum_" + keyworId).html(data.sum);
                      $("#area_" + keyworId).hide();
                      var _divKeyId = $(".dev_avatar_div_" + keyworId);
                      var lenthAvatar = _divKeyId.children(".dev_avatar_sum").length + 1;
                      if (lenthAvatar >= 3) {// 实现排序
                        _divKeyId.children(".body_keyword-detail_item-right_avator3").remove();
                      }
                      var _avator1 = _divKeyId.children(".body_keyword-detail_item-right_avator1");
                      _avator1.removeClass("body_keyword-detail_item-right_avator1");
                      var _avator2 = _divKeyId.children(".body_keyword-detail_item-right_avator2");
                      _avator2.removeClass("body_keyword-detail_item-right_avator2");
                      _avator1.addClass("body_keyword-detail_item-right_avator2");
                      _avator2.addClass("body_keyword-detail_item-right_avator3");

                      var psnAvatar = '<img src="'
                          + data.avatar
                          + '" onerror="this.onerror=null;this.src=\'/resmod/smate-pc/img/logo_psndefault.png\'" class="body_keyword-detail_item-right_avator1">';
                      _divKeyId.prepend(psnAvatar);
                      _divKeyId.attr('onclick', '').unbind('click');
                      _divKeyId.bind("click", function() {
                        showIdentifyPsnList(keyworId);
                      });
                    } else {
                      scmpublictoast(homepage.systemBusy, 1000);
                    }
                  });

        },
        error : function() {
          scmpublictoast(homepage.systemBusy, 1000);
        }
      });
}

function showShareBox(shareParams) {
  $("#more_operation_div").hide();
  $("#more_operation_back_div").hide();
  if(navigator.userAgent.indexOf("smate-android") != -1){
    appFun.showShareMenu(shareParams);
  }else{
    $("#homepage_share_box").show();
    if ($("#isWechatBrowser").val() != "true") {
      $(".footer_share-function_item-list:visible").attr("style", "width:33%;");
    }
  }
}

//初始化分享菜单
function initShareMenu(){
  if($("#isWechatBrowser").val() != "true"){
    $(".share_in_wechat").hide();
    $(".show_in_not_wechat").show();
    $(".footer_share-function").attr("style", "height:108px;");
  }
}
