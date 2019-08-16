var GrpSelectPub = GrpSelectPub ? GrpSelectPub : {};

GrpSelectPub.showSelectPubBox = function() {
    GrpSelectPub.msgpubBoxInterval = setInterval(function() {// 确定按钮的监听
        GrpSelectPub.is_pubsend();
    }, 1000);
  showDialog("grp_select_pub");
  setTimeout(GrpSelectPub.queryPub(), 300);

};

GrpSelectPub.clearSelectedPub = function(obj) {
  $(obj).parent().find(".aleady_select_pub").attr("pubId", "");
  $(obj).parent().find(".aleady_select_pub").attr("des3PubId", "");
  $(obj).parent().find(".aleady_select_pub").find("a").text("");
  $(obj).parents(".select_res_box").hide();
  $(obj).parents(".comment_res_box").hide();
  if ($("#publish_text").val().length > 0) {
    $("#publish_button").removeAttr("disabled");
  } else {
    $("#publish_button").attr("disabled", "");
  }
}

GrpSelectPub.hideSelectPubBox = function() {
  $(".show_selected_pub_info").removeClass("show_selected_pub_info");
  hideDialog("grp_select_pub");
  $("#id_pubtype_list").find(".filter-value__item").eq(0).click();
};

GrpSelectPub.selectSearchType = function() {
  var searchType = $("#select_search_type").attr("searchType");
  if (searchType == 1) {
    $("#select_search_type").attr("searchType", "0");
  } else {
    $("#select_search_type").attr("searchType", "1");
  }
};
GrpSelectPub.queryPub = function() {
  GrpSelectPub.queryPubMaililst = window.Mainlist({
    name : "selectpub",
    listurl : "/groupweb/grppub/ajaxselectpublist",
    listdata : {},
    listcallback : function(xhr) {
      addFormElementsEvents($("#id_grp_select_pub")[0]);
    },
    method : "scroll"
  });

};

GrpSelectPub.selectOnePub = function(obj) {
  var pubId = $(obj).attr("pubId");
  var des3PubId = $(obj).attr("des3PubId");
  var title = $(obj).find(".pub-idx__main_title").find("a").text();
  $(".show_selected_pub_info").find(".aleady_select_pub").attr("pubId", pubId);
  $(".show_selected_pub_info").find(".aleady_select_pub").attr("des3PubId", des3PubId);
  $(".show_selected_pub_info").find(".aleady_select_pub").find("a").text(title);
  $(".show_selected_pub_info").show();
  // $(".show_selected_pub_info").removeClass("show_selected_pub_info");
  GrpSelectPub.hideSelectPubBox();
  $("#publish_option").show();
  $("#publish_button").removeAttr("disabled");
};
GrpSelectPub.sureSelectOnePub = function() {
    var obj = $("#grp_select_pub_list").find("input:checked").closest(".main-list__item");
    var pubId = obj.attr("pubId");
    var des3PubId = obj.attr("des3PubId");
    var title = obj.find(".pub-idx__main_title").find("a").text();
    $(".show_selected_pub_info").find(".aleady_select_pub").attr("pubId", pubId);
    $(".show_selected_pub_info").find(".aleady_select_pub").attr("des3PubId", des3PubId);
    $(".show_selected_pub_info").find(".aleady_select_pub").find("a").text(title);
    $(".show_selected_pub_info").show();
    // $(".show_selected_pub_info").removeClass("show_selected_pub_info");
    GrpSelectPub.hideSelectPubBox();
    $("#publish_option").show();
    $("#publish_button").removeAttr("disabled");
};

// 文件发送按钮的disabled设置
GrpSelectPub.is_pubsend = function() {
    if ($("#grp_select_pub_list").find("input:radio[name='pub-type']:checked").length > 0) {
        $("#id_grp_select_pub").find(".button_primary-reverse").removeAttr("disabled");
    } else {
        $("#id_grp_select_pub").find(".button_primary-reverse").attr("disabled", true);
    }
}
