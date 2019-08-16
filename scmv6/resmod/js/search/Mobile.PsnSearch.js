/**
 * Mobile-人员检索.
 * 
 * @author xys
 */
var PsnSearch = PsnSearch ? PsnSearch : {};

PsnSearch.init = function() {

};

/**
 * 加载人员列表. opType:1-刷新列表；2-加载更多
 */
PsnSearch.ajaxlist = function(data, opType) {
  $.ajax({
    url : '/psnweb/mobile/ajaxlist',
    type : 'post',
    dataType : 'html',
    cache : false,
    data : data,
    success : function(data) {
      $(".pullUpLabel").show();
      if (opType == 2) {// 加载更多
        if ($("#psn_list").find(".noRecord").length == 0) {
          $("#psn_list").append(data);
        }
        if ($("#psn_list").find(".noRecord").length == 1) {
          $("#pullUp").hide();
        }
        if ($("#psn_list").find("dd").length != 0) {
          $("#psn_list").find(".noRecord").hide();
          // $("#pullUp").hide();
        }
        myScroll.refresh();
      } else {
        $("#psn_list").html(data);
        if (parseInt($("#dl_data_size").attr("datasize")) < 10) {
          $(".pullUpLabel").hide();
        }
      }
      // 批量设置人员头像
      var des3PsnIdsStr = '';
      $(".psn_info").each(function() {
        des3PsnIdsStr += ',' + $(this).attr("des3psnId");
      });
      if (des3PsnIdsStr.length > 0) {
        des3PsnIdsStr = des3PsnIdsStr.substring(1);
      }
      PsnSearch.ajaxavatarurls(des3PsnIdsStr);
    },
    error : function(e) {

    }
  });
};

PsnSearch.ajaxlistnew = function(data, opType) {
  /*
   * if($("#load_preloader").css("display")=="block" ){ return ; }
   */
  // $("#load_preloader").show()
  $.ajax({
    url : '/psnweb/mobile/ajaxlist',
    type : 'post',
    dataType : 'html',
    cache : false,
    data : data,
    beforeSend : function() {
      $(".load_preloader").doLoadStateIco({
        status : 1,
        style : "height: 38px; width:38px; margin:auto ; margin-top:48px; padding-top: 50px;"
      });
    },
    success : function(data) {
      $(".load_preloader").find(".preloader").remove();
      if (opType == 2) {// 加载更多
        if ($("#psn_list").find(".noRecord").length == 0) {
          $(".load_preloader").before(data);
          // $("#mobile_psn_list").append(data);
          $("#pageNo").val(parseInt($("#pageNo").val()) + 1);
        }
        if ($("#psn_list").find(".noRecord").length == 1) {
          $("#pullUp").hide();
        }
        if ($("#psn_list").find("dd").length != 0) {
          $("#psn_list").find(".noRecord").hide();
          // $("#pullUp").hide();
        }
      } else {
        $("#mobile_psn_list").find("dl").remove();
        $(".load_preloader").before(data);
        // $("#mobile_psn_list").html(data);
        var psnlist = $("#dl_data_size").find("dd");
        $(psnlist[0]).css("border-top", "0");
        if (parseInt($("#dl_data_size").attr("datasize")) < 10) {
          $(".pullUpLabel").hide();
        }
      }

      // $("#load_preloader").hide()

      // 批量设置人员头像
      var des3PsnIdsStr = '';
      $(".psn_info").each(function() {
        des3PsnIdsStr += ',' + $(this).attr("des3psnId");
      });
      if (des3PsnIdsStr.length > 0) {
        des3PsnIdsStr = des3PsnIdsStr.substring(1);
      }
      PsnSearch.ajaxavatarurls(des3PsnIdsStr);
    },
    error : function(e) {

    }
  });
};

/**
 * 批量设置人员头像.
 */
PsnSearch.ajaxavatarurls = function(des3PsnIdsStr) {
  $.ajax({
    url : '/psnweb/mobile/ajaxavatarurls',
    type : 'post',
    dataType : 'json',
    cache : false,
    data : {
      "des3PsnIdsStr" : des3PsnIdsStr
    },
    success : function(data) {
      if (data.result == 'success') {
        var avatarUrls = data.avatarUrls;
        for (var i = 0; i < avatarUrls.length; i++) {
          $("#avatar_" + avatarUrls[i].psnId).attr("src", avatarUrls[i].avatarUrl);
        }
      }
    },
    error : function(e) {

    }
  });
};

// 移动端-联系菜单-发现联系人
PsnSearch.ajaxFriendList = function(data, opType) {
  // if ($.trim($("#msg_next_page").val()) != "false") {
  $("#load_preloader").show();
  $.ajax({
    url : '/psnweb/mobile/ajaxlist',
    type : 'post',
    dataType : 'html',
    cache : false,
    data : data,
    success : function(data) {
      if (opType == 2) {// 加载更多
        $("#pageNo").val(parseInt($("#pageNo").val()) + 1);
        $(".list_container").append(data);
      } else {
        $(".list_container").html(data);
        $("#pageNo").val(1);
      }
      if (($(".psn_info").length == 0 && $(".psn_item_notice").length > 1)
          || ($(".psn_info").length > 0 && $(".psn_item_notice").length > 0)) {
        $(".psn_item_notice:first").remove();
      }
      // 批量设置人员头像
      var des3PsnIdsStr = '';
      $(".psn_info").each(function() {
        des3PsnIdsStr += ',' + $(this).attr("des3psnId");
      });
      if (des3PsnIdsStr.length > 0) {
        des3PsnIdsStr = des3PsnIdsStr.substring(1);
      }
      PsnSearch.ajaxavatarurls(des3PsnIdsStr);
      $("#load_preloader").hide();
    },
    error : function(e) {
      $("#load_preloader").hide();
    }
  });
  // }
};

// 刚进页面时加载
PsnSearch.firstload = function() {
  var searchString = $.trim($(".header_search_input").val());
  if (searchString == '') {
    return;
  } else {
    $(".header_search_cancel").show();
  }
  var data = {
    "searchString" : searchString,
    "page.pageNo" : 1,
    "fromPage" : "mobileSearchFriend"
  }
  PsnSearch.ajaxFriendList(data, 1);
}
