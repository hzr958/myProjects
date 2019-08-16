//进入人员主页
function goToPersonalPage(des3PsnId) {
  window.location.href = "/psnweb/mobile/outhome?des3ViewPsnId=" + des3PsnId;
}
// 可能认识的人记录左移
function leftMoveItemCallBack(obj, event) {
  var selected = "#" + $(obj).attr("id");
  +":visible";
  if ($(selected).length > 0) {
    neglectMayKnowPsn($(obj).attr("des3PsnId"), $(obj).attr("id"), event);
  }
}
// 可能认识的人记录右移
function rightMoveItemCallBack(obj, event) {
  $(obj).css('transform', 'translateX(0px)');
  $(obj).css('transition', '0.3s');
  setTimeout(function() {
    $(obj).css('transition', '0s')
  }, 10);// 回复原位
}
// 点击可能认识的人记录
function clickItemCallBack(obj, event) {
  $(obj).css('transform', 'translateX(0px)');
  $(obj).css('transition', '0.3s');
  setTimeout(function() {
    $(obj).css('transition', '0s')
  }, 10);// 回复原位
}

// 加载可能认识的人员
function loadPsnMayKonw() {
  if ($("#hasNextPage").val() != "false" && $("#loadFinish").val() == "true") {
    $("#div_preloader").show();
    var url = "/psnweb/mobile/ajaxknowmew";
    var dataType = "html";
    var type = "post";
    var pageNo = isNaN($("#pageNo").val()) ? 1 : (parseInt($("#pageNo").val()) + 1);
    var dataJson = {
      "orderBy" : $("orderBy").val(),
      "pageNo" : pageNo
    };
    $("#loadFinish").val("false");
    $.ajax({
      url : url,
      dataType : dataType,
      type : type,
      data : dataJson,
      success : function(data) {
        $("#psnMayKnow").append(data);
        $("#pageNo").val(pageNo);
        $("#div_preloader").hide();
        $("#loadFinish").val("true");
      },
      error : function() {
        $("#div_preloader").hide();
        $("#loadFinish").val("true");
      }
    });
  }
}
// 加载联系人请求列表
function loadMorefriendPsn() {
  $.ajax({
    url : '/psnweb/mobile/friendreq',
    type : 'post',
    data : {
      'mobile' : true
    },
    dataType : 'html',
    timeout : 10000,
    success : function(data) {
      if (data.indexOf("list_item_container add_friend_item") == -1) {
        $("#personRequset").append(data);
        $("#friendRequset").hide();
      } else {
        $("#friendRequset").show();
        $("#personRequset").append(data);
      }
    }
  });
}

// 添加联系人请求
function addFriendReq(des3PsnId, psnId, event) {
  var option = {
    fun : function() {
      $.ajax({
        url : '/psnweb/friend/ajaxaddfriend',
        type : 'post',
        data : {
          'des3Id' : des3PsnId
        },
        dataType : 'json',
        timeout : 10000,
        success : function(data) {
          if (data.result == "true") {
            scmpublictoast(relation.sendFriendSuccess, 3000);
          } else {
            scmpublictoast(relation.addFriendPrivate, 5000);
          }
          reloadMayKnowPsn();
        },
        error : function() {
          scmpublictoast(relation.sendFriendError, 3000);
        }
      });
    }
  };
  $("#" + psnId).moveItem(option);
  event.stopPropagation();
}

// 忽略可能认识的人
function neglectMayKnowPsn(des3TempPsnId, psnId, event) {
  var option = {
    fun : function() {
      $.ajax({
        // url:'/scmwebsns/friend/mobile/ajaxneglect',
        url : '/psnweb/mobile/ajaxremove',
        type : 'post',
        // data:{'sysTempId':des3PsnId, 'currentDes3PsnId': $("#currentDes3PsnId").val()},
        data : {
          "des3TempPsnId" : des3TempPsnId
        },
        dataType : 'json',
        timeout : 10000,
        success : function(result) {
          if (data.result == "success") {
            scmpublictoast("操作成功", 1000);
            reloadMayKnowPsn();
          }
        },
        error : function() {
          scmpublictoast("操作失败", 1000);
        }
      });
    }
  };
  $("#" + psnId).moveItem(option);
  event.stopPropagation();
  reloadMayKnowPsn();
}

// 当列表中可能认识的人员都被移除时从新加载一次
function reloadMayKnowPsn() {
  if ($(".list_item_container").length == 0) {
    $("#pageNo").val("0");
    $("#lastPsnId").val("");
    loadPsnMayKonw();
  }
}