var GrpMemberPub = GrpMemberPub ? GrpMemberPub : {};

;
GrpMemberPub.showGrpMemberPubFilterBox = function() {
  if ($("#grp_member_pub_filter_box").css("display") == "none") {
    $("#grp_member_pub_filter_box").show();
  } else {
    $("#grp_member_pub_filter_box").hide();
  }
};

;
GrpMemberPub.hideMemberGrpPubFilterBox = function() {
  $("#grp_member_pub_filter_box").hide();
};

;
GrpMemberPub.showMemberPub = function(obj) {
  if ($(".psn_chosen").parent()[0] != $(obj)[0]) {
    // $(".option_selected").removeClass("option_selected");
    // 上面那个把群组成果列表的条件都移除了 --ajb 2017-05-10
    $("#grp_member_pub_box").find(".option_selected").removeClass("option_selected");
    GrpMemberPub.hideMemberGrpPubFilterBox();
    $("#search_content").val("");
  }
  window.scroll(0, 284);
  if (document.getElementById("grp_member_pub_box").style.visibility !== "visible"
      || $("#grp_member_pub_box").attr("visibility") !== "visible") {
    showDialog("grp_member_pub_box");
  }
  if ($("#grp_member_pub").find(".psn_chosen").length > 0 || GrpMemberPub.showgrpMemberPubListStatus) {
    GrpMemberPub.memberpubMainlist.listdata.des3PsnId = $(obj).attr("des3PsnId");
    GrpMemberPub.memberpubMainlist.resetAllFilterOptions();
  } else {
    GrpMemberPub.showGrpMemberPubList(obj);
    GrpMemberPub.showgrpMemberPubListStatus = true;// 用来标记是不是第一次点击群组成员成果
  }
  $(".psn_chosen").removeClass("psn_chosen");
  $(obj).children().addClass("psn_chosen");
  document.getElementById("grp_member_pub").style.zIndex = "100";
  $("#grp_member_pub_filter_box").find(".option_selected").removeClass("option_selected");
  if ($("#id_order_type").find(".option_selected").length > 0) {

  } else {
    $("#id_order_type").find(".filter-value__item").eq(0).addClass("option_selected");
  }

};

;
GrpMemberPub.hideMemberPub = function(mark) {
  var ids = "";
  $("#grp_member_pub_list").find(".main-list__item").each(function() {
    if ($(this).find("input").is(":checked") && $(this).find("input").attr("disabled") != "disabled") {
      ids += "," + $(this).attr("pubId");
    }
  });
  if (ids != "") {
    ids = ids.substring(1);
  };
  if (ids != "" && mark != 1) {
    GrpMember.jconfirm(function() {
      hideDialog("grp_member_pub_box");
      $(".background-cover[dialog-target='grp_member_pub_box']").remove();
      document.getElementById("grp_member_pub").style.zIndex = "";
    }, grpPubCommon.backTips, "", "");
  } else {
    hideDialog("grp_member_pub_box");
    document.getElementById("grp_member_pub").style.zIndex = "";
    if (mark != 1) {
      GrpPub.reloadGrpPubCurrentPage();
    }
    $(".background-cover[dialog-target='grp_member_pub_box']").remove();
  }
  $(".psn_chosen").removeClass("psn_chosen");
  $(".drawer-batch__box").attr("list-drawer", "grppub");

};

;
GrpMemberPub.showGrpMemberPubList = function(obj) {
  $("#grp_member_pub_list").doLoadStateIco({
    style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
    status : 1
  });
  var des3PsnId = $(obj).attr("des3PsnId");
  GrpMemberPub.memberpubMainlist = window.Mainlist({
    name : "memberpub",
    listurl : "/groupweb/grppub/ajaxmemberpublist",
    listdata : {
      'des3PsnId' : des3PsnId,
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'isPsnPubs' : '1'
    },
    listcallback : function(xhr) {
      // $("#grp_member_pub_list").html(xhr.responseText);
    },
    statsurl : "/pub/query/ajaxgrppubcount"
  });
};

;
GrpMemberPub.importMemberPubToGrp = function(obj) {
  GrpBase.doHitMore(obj, 2000);
  var ids = "";
  $("#grp_member_pub_list").find(".main-list__item").each(function() {
    if ($(this).find("input").is(":checked") && $(this).find("input").attr("disabled") != "disabled") {
      ids += "," + $(this).attr("des3PubId");
    }
  });
  if (ids != "") {
    ids = ids.substring(1);
  };
  if (ids == "") {
    scmpublictoast(grpPubCommon.choosePub, 2000);
    return;
  }
  var savePubType = 0;
  var showmodule = $(".main-list").find(".filter-list").attr("showmodule");
  if (showmodule !== undefined && "projectPub" === showmodule) {
    savePubType = 1;
  }
  $.ajax({
    url : '/groupweb/grppub/ajaximportmemberpub',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'pubIds' : ids,
      'savePubType' : savePubType
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        scmpublictoast(grpPubCommon.importSuccess, 1000);
        $("#grp_pub_search_content").val("");
        // GrpPub.showGrpPubList();
        $grpPub.resetAllFilterOptions();
        GrpMemberPub.hideMemberPub(1);
      });

    },
    error : function() {
      scmpublictoast(grpPubCommon.importFail, 2000);
      GrpPub.showGrpPubList();
      GrpMemberPub.hideMemberPub(1);
    }
  });

};
