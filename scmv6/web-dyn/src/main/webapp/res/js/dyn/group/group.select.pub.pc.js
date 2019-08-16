/**
 * 群组选择成果
 */
var groupSelectPub = groupSelectPub ? groupSelectPub : {};

// 进入选择群组成果
groupSelectPub.comeSelectPub = function(e) {
  var pubData = $("#share_plugin_publist").html();

  var groupCategory = $("#current_groupCategory").val();
  if ("10" === groupCategory) {
    // 兴趣群组是， 不显示群组成果
    var $this = $("#share_plugin_addpub")
        .find(
            ".dialogs_whole >  .dialogs_title  >  .selector_dropdown_single > .selector_dropdown_value  > .selector_dropdown_option ");
    $this.each(function() {
      if ("1" == $(this).attr("val")) {
        $(this).remove();
      } else {
        $(this).addClass("selected");
      }
    })

    $this = $("#share_plugin_addpub")
        .find(
            ".dialogs_whole >  .dialogs_title  >  .selector_dropdown_single > .selector_dropdown_collections  > .selector_dropdown_option ");
    $this.each(function() {
      if ("1" == $(this).attr("val")) {
        $(this).remove();
      } else {
        $(this).addClass("hover");
      }
    })
    if ($.trim(pubData) == "") {// 选中群组成果
      groupSelectPub.comeSelectOwnerPub();
    } else {// 有数据回显
      $("#share_plugin_addpub").show();
      e.stopPropagation();
    }
  } else {
    if ($.trim(pubData) == "") {// 选中群组成果
      groupSelectPub.comeSelectGroupPub();
    } else {// 有数据回显
      $("#share_plugin_addpub").show();
      e.stopPropagation();
    }
  }

}
// 进入选择群组成果
groupSelectPub.comeSelectGroupPub = function() {
  groupSelectPub.selectGroupPub();
  setTimeout(groupSelectPub.scrollEvent('groupPub'), 2000);

}
// 进入选择我的成果
groupSelectPub.comeSelectOwnerPub = function() {
  groupSelectPub.selectOwnerPub();
  setTimeout(groupSelectPub.scrollEvent('owner'), 2000);

}
// 进入分享成果 ,默认加载群组成果
groupSelectPub.selectGroupPub = function() {
  // 加载转圈圈
  $("#share_plugin_publist").find(".preloader").remove();
  $("#share_plugin_publist").doLoadStateIco({
    status : 1,
    addWay : "append"
  });
  //
  setTimeout(function() {
    $("#share_plugin_addpub").show();
    var data = {
      "searchKey" : $("#group_dyn_select_pub_key").val(),
      "des3GroupId" : $("#des3GroupId").val(),
      "page.pageNo" : $("#select_page_no").val(),
      "page.pageSize" : 10,
      "page.ignoreMin" : true
    };
    ajaxparamsutil.doAjax("/groupweb/grouppub/ajaxgrouppubfordyn", data, groupSelectPub.selectGroupPubCall, "html");
  }, 500);
}

// 进入发布动态--回调
groupSelectPub.selectGroupPubCall = function(data) {
  // 取消圈圈
  $("#share_plugin_publist").find(".preloader").remove();
  if ("" == $.trim(data)) {
    // 没有获取到成果
    $("#share_plugin_publist").unbind("scroll");
    // 页面上没有成果列表
    if ("" == $.trim($("#share_plugin_publist").text())) {
      var obj = document.getElementById("share_plugin_publist");
      scmpublicprompt(obj, groupDynCommon.noRecord, "60px");
    }
    return;
  }

  $("#share_plugin_publist").append(data);
  // 页数加一
  var pageNo = numObj = new Number($("#select_page_no").val())
  $("#select_page_no").val(pageNo + 1);

}

// 选择自己的成果
groupSelectPub.selectOwnerPub = function() {
  // 加载转圈圈\
  $("#share_plugin_publist").find(".preloader").remove();
  $("#share_plugin_publist").doLoadStateIco({
    status : 1,
    addWay : "append"
  });
  setTimeout(function() {
    $("#share_plugin_addpub").show();
    var data = {
      "searchKey" : $("#group_dyn_select_pub_key").val(),
      "page.pageNo" : $("#select_page_no").val(),
      "page.pageSize" : 10,
      "page.ignoreMin" : true
    };
    ajaxparamsutil.doAjax("/pubweb/publication/ajaxmypubforgroupdyn", data, groupSelectPub.selectOwnerPubCall, "html");
  }, 500);
}

// 选择自己的成果-回调
groupSelectPub.selectOwnerPubCall = function(data) {
  // 取消圈圈
  $("#share_plugin_publist").find(".preloader").remove();
  if ("" == $.trim(data)) {
    $("#share_plugin_publist").unbind("scroll");
    // 页面上没有成果列表
    if ("" == $.trim($("#share_plugin_publist").text())) {
      var obj = document.getElementById("share_plugin_publist");
      scmpublicprompt(obj, groupDynCommon.noRecord, "60px");
    }
    return;
  }
  $("#share_plugin_publist").append(data);
  // 页数加一
  var pageNo = numObj = new Number($("#select_page_no").val())
  $("#select_page_no").val(pageNo + 1);
}

// //选择成果后回到 编辑页面。
groupSelectPub.sureSelect = function(pubId, des3pubId) {
  var fullImage = $("#full_image_" + pubId).val();
  var title = $("#title_" + pubId).html();
  var author = $("#author_" + pubId).html();
  var brief = $("#brief_" + pubId).html();

  var pubHtml =

  ' <div class="dynamic_content_divider"></div> ' + ' <div class="attachment"> ' + ' <div class="pub_whole"> '
      + '    <div><img src="' + fullImage + '" class="pub_avatar"></div> ' + '    <div class="pub_information">  '
      + '       <div class="title">' + title + '</div>  ' + '       <div class="author">' + author + '</div>  '
      + '       <div class="source">' + brief + '</div>  ' + '      </div>  ' + ' </div>   '
      + ' <div class="attachment_delete"   onclick="groupDynamic.deleteSelectPub()" > '
      + '      <div class="delete"><i class="material-icons" >close</i></div> ' + ' </div> ' + '  </div>  ';

  $("#select_pub").html(pubHtml);
  $("#select_pub_id").val(des3pubId);
  $("#share_plugin_addpub").hide();
  // groupSelectPub.toShareDyn();
}

// 删除选中的成果
groupDynamic.deleteSelectPub = function() {
  $("#select_pub").empty();
  $("#select_pub_id").val("");
}

/*
 * groupSelectPub.toShareDyn = function(){ $("#share_plugin_publist").scrollTop(0);
 * $("#share_plugin_publist").empty(); $(".fukuang").css("display","none");
 * $(".first").css("display","block"); //初始化值 $("#select_page_no").val(1) }
 */

groupSelectPub.scrollEvent = function(module) {

  $("#share_plugin_publist").unbind("scroll");
  $("#share_plugin_publist").scroll(function() {
    var h = $(this).height();// div可视区域的高度
    var sh = $(this)[0].scrollHeight;// 滚动的高度，$(this)指代jQuery对象，而$(this)[0]指代的是dom节点
    var st = $(this)[0].scrollTop;// 滚动条的高度，即滚动条的当前位置到div顶部的距离
    if (h + st >= sh && st != 0) {
      // 上面的代码是判断滚动条滑到底部的代码
      if ("groupPub" == module) {
        groupSelectPub.selectGroupPub();
      } else {
        groupSelectPub.selectOwnerPub();
      }

    }
  })

};

// 切换选择成果
groupSelectPub.changework = function(obj) {
  // 初始化值
  $("#share_plugin_publist").empty();
  $("#select_page_no").val(1)
  $("#share_plugin_publist").scrollTop(0);
  if ("owner_pub" == $(obj).val()) {
    groupSelectPub.comeSelectOwnerPub();
  } else {
    groupSelectPub.comeSelectGroupPub();
  }

}
// 显示查询成果
groupSelectPub.showSearchPub = function(obj) {
  $(obj).closest(".dialogs_whole").find(".dialogs_title").eq(0).css("display", "none");
  $(obj).closest(".dialogs_whole").find(".dialogs_title").eq(1).css("display", "flex");

  $(obj).closest(".dialogs_whole").find(".dialogs_title > .input_field_titled > .input_field").keyup(
      function() {
        var value = $(this).val();
        var selectVal = $(this).closest(".dialogs_whole").find(
            ".dialogs_title > .selector_dropdown_single > .selector_dropdown_value > .selected").attr("val");
        // 初始化值
        $("#share_plugin_publist").empty();
        $("#select_page_no").val(1)
        $("#share_plugin_publist").scrollTop(0);
        if ("1" == selectVal) {// 群组成果
          groupSelectPub.comeSelectGroupPub();

        } else if ("2" == selectVal) {// 我的成果

          groupSelectPub.comeSelectOwnerPub();
        }

      });

}
// 隐藏查询成果
groupSelectPub.hiddenSearchPub = function(obj) {
  // 清空
  $(obj).closest(".dialogs_whole").find(".dialogs_title").eq(0).css("display", "flex");
  $(obj).closest(".dialogs_whole").find(".dialogs_title").eq(1).css("display", "none");
  groupSelectPub.emptySerachPubKey(obj);
}
// 清空查询的关键词
groupSelectPub.emptySerachPubKey = function(obj) {
  // 清空
  $(obj).parent().find(".input_field_titled > .input_field").val("");
  var selectVal = $(obj).closest(".dialogs_whole").find(
      ".dialogs_title > .selector_dropdown_single > .selector_dropdown_value > .selected").attr("val");
  // 初始化值
  $("#share_plugin_publist").empty();
  $("#select_page_no").val(1)
  $("#share_plugin_publist").scrollTop(0);
  if ("1" == selectVal) {// 群组成果
    groupSelectPub.comeSelectGroupPub();

  } else if ("2" == selectVal) {// 我的成果

    groupSelectPub.comeSelectOwnerPub();
  }
}

// Hide Dialogs Layer
hideDialogs = function(obj, event) {

  var allDialogs = $('body').find("div[class='background_cover_layer']")
  var currentDialog = $(obj).find("div[class='dialogs_whole']");
  var moveDistance = $(window).height() - currentDialog.offset().top + $(window).scrollTop();

  if (event.target != null && event.target !== obj) {
    return false;
  } else {
    currentDialog.css('transform', 'translateY(' + moveDistance + 'px)');
    allDialogs.css('pointer-events', 'none');
    setTimeout(function() {
      $(obj).hide();
      currentDialog.css('transform', 'translateY(0)');
      allDialogs.css('pointer-events', 'auto');
      $('.background_cover_layer').each(function() {
        var $dialog = $(this);
        if ($dialog.is(":visible")) {
          $('body').css('overflow', 'hidden');
          return false;
        } else {
          $('body').css('overflow', 'auto');
        }
      });
    }, 300);
  }
};
