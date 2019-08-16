/**
 * 群组成果----左侧成员成果
 */
var Member = Member ? Member : {};
Member.publication = Member.publication ? Member.publication : {};

/**
 * 加载群组 成员成果 左边栏
 */
Member.publication.loadLeftMenu = function(des3GroupId, groupNodeId) {
  // alert(des3GroupId);
  $.ajax({
    url : '/groupweb/grouppub/ajaxgroupmembers',
    type : "post",
    dataType : "html",
    data : {
      "des3GroupId" : des3GroupId,
      "groupNodeId" : groupNodeId
    },
    success : function(data) {
      // alert("success!");
      $("#groupMemberPub").html(data);
    }
  });
};

/**
 * 点击 成员，加载成员的公开成果列表
 */
Member.publication.loadPsnOpenPubs = function(des3PsnId, des3GroupId, pageNo) {
  $.ajax({
    url : '/pubweb/publication/ajaxpsnopenpubs',
    type : "post",
    dataType : "html",
    data : {
      "psnId" : des3PsnId,
      "des3GroupId" : findDes3GroupId(),
      "groupId" : des3GroupId,
      "page.pageNo" : pageNo
    },
    success : function(data) {
      if (data == '{"ajaxSessionTimeOut":"yes"}') {
        jConfirm(i18n_timeout, i18n_tipTitle, function(r) {
          if (r) {
            top.location.reload(true);
            return;
          }
        });
      } else {
        $("#right-wrap").html(data);
        $("#des3PsnId").val(des3PsnId);
        $("#searchKey").val("");
        // 为了显示提示语
        $("#searchKey").focus();
        $("#searchKey").blur();
        setTimeout(function() {
          if ($(".right-wrap").height() > $(".right-wrap").prev(".left-wrap").height()) {
            $(".right-wrap").height("");
          }
        }, 1500);
        // 禁用除搜索框之外的左边菜单栏
        $("#menu-mine").nextAll().children("a").attr("onclick", "");
        $("#menu-mine").children("a").attr("onclick", "Group.menu.jump('" + $("#des3GroupId").val() + "','1','pub')");
        var length = $("#groupPubMemberName").innerWidth();
        if (length > 200) {
          $("#groupPubMemberName").attr("style",
              "float:left;width:200px;text-overflow:ellipsis;overflow:hidden; white-space:nowrap;");
        }
      }

    }
  });
};

/**
 * 成员成果---前5个，后5个
 */
Member.publication.groupPsnPubMove = function(go) {
  var members = $(".group_psn_pub");
  var count = Math.floor(members.length / 5);
  if (members.length % 5 > 0) {
    count++;
  }
  var currentFirst = Number($(".groupPsnPubIndex").val());
  if ("left" == go) {
    var leftCanClick = $("#preFive").attr("canclick");
    if (leftCanClick == "true") {// 前五个可用时
      currentFirst -= 1;
    }
    $(".groupPsnPubIndex").val(currentFirst);
  }
  if ("right" == go) {
    var rightCanClick = $("#nextFive").attr("canclick");
    if (rightCanClick == "true") {// 后五个可用时
      currentFirst += 1;
    }
    $(".groupPsnPubIndex").val(currentFirst);
  }
  if (currentFirst <= 1) {
    $("#preFive").attr("class", "");
    $("#preFive").attr("canClick", "false");
    $("#nextFive").attr("class", "canclick");
    $("#nextFive").attr("canClick", "true");
    currentFirst = 1;
  } else if (currentFirst >= count) {
    $("#preFive").attr("class", "canclick");
    $("#preFive").attr("canClick", "true");
    $("#nextFive").attr("class", "");
    $("#nextFive").attr("canClick", "false");
    currentFirst = count;
  } else {
    $("#preFive").attr("class", "canclick");
    $("#preFive").attr("canClick", "true");
    $("#nextFive").attr("class", "canclick");
    $("#nextFive").attr("canClick", "true");
  }
  for (var i = 0; i < members.length; i++) {
    $(members[i]).hide();
  }
  for (var i = (currentFirst - 1) * 5; i < ((currentFirst - 1) * 5 + 5) && i < members.length; i++) {
    if ($(members[i]).length > 0) {
      $(members[i]).show();
    }
  }
};

Member.publication.groupPubsImport = function() {
  var iszhCN = locale == "zh_CN" ? true : false;
  var pubList = [];
  var checkboxs = $("input:checked[name=ckpub]").not(":disabled");
  var articleType = "1";
  var pub = iszhCN ? "请先选择成果" : "Please select publications";
  var reference = iszhCN ? "请先选择文献" : "Please select publications";
  var opFaild = iszhCN ? "操作失败" : "operated failed";
  var opSuccess = iszhCN ? "操作成功" : "operated successfully";
  var sysouttimes = iszhCN ? "系统超时或未登录，你要登录吗？" : "You are not logged in or session time out, please log in again";
  var systips = iszhCN ? "提示" : "Reminder";

  if (checkboxs.length == 0) {
    if (articleType == "1") {
      $.scmtips.show('warn', required_publication);
    } else if (articleType == "2") {
      $.scmtips.show('warn', required_reference);
    }
    $("#flag").val("1");
    return;
  }
  checkboxs.each(function(i, cb) {
    var value = $(cb).val();
    pubList.push({
      des3Id : value,
      des3GroupId : findDes3GroupId()
    });
  });

  var des3PubIdsStr = JSON.stringify(pubList);
  var url = "";
  var artType = $(parent.document).find(":input[id=navAction]").val();
  var articleType;
  if (artType == 'groupPubs') {
    articleType = 1;
  } else if (artType == 'groupRefs') {
    articleType = 2;
  } else {
    $("#flag").val("1");
    return;
  }
  if (articleType == "1") {
    url = '/pubweb/publication/ajaxGroupPubsImport';
  } else if (articleType == "2") {
    url = '/pubweb/publication/ajaxGroupPubsImport';
  }
  $.ajax({
    url : url,
    type : 'post',
    dataType : 'json',
    data : {
      "articleType" : articleType,
      "des3PubIdsStr" : des3PubIdsStr,
      "des3GroupId" : findDes3GroupId(),
      "groupNodeId" : findNodeId()
    },
    success : function(data) {
      var toConfirm = false;
      if (data != null) {
        toConfirm = data.ajaxSessionTimeOut == 'yes' ? true : toConfirm;
      } else {
        toConfirm = data == "{\"ajaxSessionTimeOut\":\"yes\"}" ? true : toConfirm;
      }
      if (toConfirm) {
        jConfirm(sysouttimes, systips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      } else {
        if (data.result == 'success') {
          $.scmtips.show('success', addSuccess);
          setTimeout(function() {
            parent.Group.publication.clearBackPage();
            $("#orderBy").val("");
            $("#order").val("");
            parent.$.Thickbox.closeWin();
            // searchForPsnPubList();
            // $("#flag").val("1");
            // 跳转到所有成果界面
            $("#menu-mine").children("a").click();
          }, 1000);
        } else {
          if (data.ajaxSessionTimeOut == 'yes') {
            jConfirm(i18n_timeout, i18n_tipTitle, function(r) {
              if (r) {
                top.location.reload(true);
                return;
              }
            });
          } else {
            $.scmtips.show('error', addFail);
          }
        }
      }

    },
    error : function() {
      $.scmtips.show('error', addFail);
    }
  });

  // }

}

function groupPsnPubToPage() {
  // var des3PsnId = $("#des3PsnId").val();
  // var des3GroupId = $("#des3GroupId").val();
  // var pageNo = $("#pageNo").val();
  // Member.publication.loadPsnOpenPubs(des3PsnId, des3GroupId,pageNo);
  searchForPsnPubList();
}

function groupPsnPubReady() {
  // Group.publication.ready();
  $("input.thickbox").thickbox();
  // 分享下拉模式
  $(".share_pull").sharePullMode({
    showSharePage : function(_this) {
      Group.publication.sharePublication(_this, 1);
    }
  });
  // 全文下载
  $(".notPrintLinkSpan").fullTextRequest();
}

function searchForPsnPubList() {
  var order = $("#order").val();
  var orderBy = $("#orderBy").val();
  var searchKey = $.trim($("#searchKey").val());
  var pageSize = $("#pageSize").val();
  if (searchKey.length == 0 || searchKey == $("#searchKey").attr("title")
      || $("#searchKey").css("color") == "rgb(153, 153, 153)" || $("#searchKey").css("color") == "#999") {
    searchKey = "";
  }
  var des3PsnId = $("#des3PsnId").val();
  var des3GroupId = $("#des3GroupId").val();
  var pageNo = $("#pageNo").val();
  $.ajax({
    url : '/pubweb/publication/ajaxpsnopenpubs',
    type : "post",
    dataType : "html",
    data : {
      "des3GroupId" : des3GroupId,
      "des3PsnId" : des3PsnId,
      "page.orderBy" : orderBy,
      "page.order" : order,
      "page.pageNo" : pageNo,
      "searchKey" : searchKey,
      "orderBy" : orderBy,
      "page.pageSize" : pageSize
    },
    success : function(data) {
      if (data == '{"ajaxSessionTimeOut":"yes"}') {
        jConfirm(i18n_timeout, i18n_tipTitle, function(r) {
          if (r) {
            top.location.reload(true);
            return;
          }
        });
      } else {
        $("#right-wrap").html(data);
        $("#des3PsnId").val(des3PsnId);
        $("#des3GroupId").val(des3GroupId);
        ScholarCommonOrder.initOrderValue();
      }
    }
  });

}

function selected(obj) {
  $(".leftnav-hover").css("color", "#333333");
  $(".isSelected").attr("class", "f8080");
  $(".leftnav-hover").removeClass("leftnav-hover");
  $(obj).addClass("leftnav-hover");
  $(obj).css("color", "#FFF");
  $(obj).find(".f8080").attr("class", "isSelected");
  $(".menu-expansion").each(function() {
    $(this).attr("class", "menu-shrink");
    $(this).parent().find(".Shear-headbottom").attr("class", "Shear-head");
  });
}

function orderByChanged(obj) {
  if ($(obj).attr("order") == 'asc') {
    $(obj).attr("order", "desc");
  } else {
    $(obj).attr("order", "asc");
  }
  $("#order").val($(obj).attr("order"));
  $("#orderBy").val($(obj).attr("orderBy"));
  $("#pageNo").val("1");
  searchForPsnPubList();

}

function inviteFriendToGroup(des3Id) {
  $("#isInvite").val("1");
  Group.menu.jump(des3Id, '1', 'member');
}

/**
 * 成员成果的全选
 */
Member.publication.selectAll = function(cbAll) {
  var checkboxs = $(":checkbox[name=ckpub]").not(":disabled");
  if ($(cbAll).attr("checked")) {
    checkboxs.attr("checked", true);
  } else {
    checkboxs.attr("checked", false);
  }
};
