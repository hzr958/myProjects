var GrpManage = GrpManage ? GrpManage : {};

/**
 * 显示推荐群组主页
 */
;
GrpManage.showRcmdGrp = function(obj) {
  GrpManage.changeUrl("rcmdGrp");
  var dataJson = {};
  $.ajax({
    url : '/groupweb/rcmdgrp/ajaxrcmdgrpmain',
    type : 'post',
    dataType : 'html',
    data : dataJson,
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_lists").html(data);
      });

    },
    error : function() {
    }
  });

}

/**
 * 刷新我的群组
 */
;
GrpManage.reloadMyGrpList = function(obj) {
  GrpBase.showGrpList();
}
/**
 * 推荐群组列表
 */
var $gcmdGrpList;
;
GrpManage.getRcmdGrpList = function() {
  /*
   * $("#grp_rcmd_list").doLoadStateIco({ style:"height: 38px; width:38px; margin:auto ;
   * margin-top:10px;", status:1});
   */
  var dataJson = {};
  $gcmdGrpList = window.Mainlist({
    name : "grprcmdlist",
    listurl : "/groupweb/rcmdgrp/ajaxrcmdgrplist",
    listdata : dataJson,
    method : "scroll",
    listcallback : function(xhr) {
      addFormElementsEvents();
      // 加载群组列表关键词
      GrpManage.showDisciplinesAndKeywordsForList();
    },
    statsurl : "/groupweb/rcmdgrp/ajaxrcmdgrpliststatistics"
  });

};

// 群组列表领域和关键词显示
;
GrpManage.showDisciplinesAndKeywordsForList = function() {
  var strS = "<div class='kw-chip_small'>";
  var strMoreS = "<div class='kw-chip_small  dev_kw_chip_more' style='display:none;'>";
  var strE = "</div>";
  var strDot = "<div class='kw-chip_small dev_dot' style='background-color: white;font-size: 20px;font-weight: bold;display:block;cursor:pointer;color: #1265cf;'>···</div>";

  $.each($("#grp_rcmd_list").find(".kw__box"), function(i, o) {
    // 最多显示三个，多余的用...
    var count = 0;
    var totalCount = 0;
    var str = "";
    $.each($(o).attr("smate_disciplines").split(";"), function(i, discipline) {
      if (discipline != '') {
        str = str + strS + discipline + strE;
      }
    });
    $.each($(o).attr("smate_keywords").split(";"), function(i, keyword) {
      if (keyword != '') {
        keyword = keyword.replace(/77&/g, ";");
        if (count < 5) {
          str = str + strS + keyword + strE;
        } else {
          str = str + strMoreS + keyword + strE;
        }
        count++;
        totalCount++;
      }
    });
    $(o).html(str);
    if (totalCount > 5) {
      $(o).append(strDot);
      $(o).attr("onclick", "GrpBase.showMoreKeyWord(this)");
    }
  });
};
// 显示更多关键词
GrpManage.showMoreKeyWord = function(obj) {
  var displayVal = $(obj).find(".dev_dot").css("display");
  if (displayVal !== "none") {
    $(obj).find(".dev_dot").css("display", "none");
    $(obj).find(".dev_kw_chip_more").each(function() {
      $(this).css("display", "");
    });
  } else {
    $(obj).find(".dev_dot").css("display", "block");
    $(obj).find(".dev_kw_chip_more").each(function() {
      $(this).css("display", "none");
    });
  }
}

/**
 * 推荐群组列表统计数
 */
;
GrpManage.getRcmdGrpListStatistics = function() {
  var dataJson = {

  };
  $.ajax({
    url : '/groupweb/rcmdgrp/ajaxrcmdgrpliststatistics',
    type : 'post',
    dataType : 'html',
    data : dataJson,
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpManageLan.timeout, grpManageLan.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }

      $(".container__horiz_left").eq(0).html(data);
    },
    error : function() {
    }
  });
};

/**
 * 推荐群组 忽略或者申请
 */
;
GrpManage.optRcmdGrp = function(des3GrpId, rcmdStatus, openType) {
  var dataJson = {
    'des3GrpId' : des3GrpId,
    'rcmdStatus' : rcmdStatus
  };
  $.ajax({
    url : '/groupweb/rcmdgrp/ajaxoptionrcmdgrp',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      var toConfirm = false;
      toConfirm = data.ajaxSessionTimeOut;
      if (toConfirm) {
        jConfirm(grpManageLan.timeout, grpManageLan.tips, function(r) {
          if (r) {
            document.location.href = window.location.href;
            return;
          }
        });
      }
      if (data.status == "success") {
        if (rcmdStatus == 1) {// 申请群组后的操作
          GrpMember.ajaxMemberApplyJoinGrp(null, des3GrpId);
          if (openType != undefined && openType == 'O') {
            // 公开群组
            setTimeout(function() {
              location.href = "/groupweb/grpinfo/main?des3GrpId=" + des3GrpId;
            }, 1000);
            return;
          }
          $gcmdGrpList.sendRequestForList();
          $gcmdGrpList.sendRequestForStats();
        } else {
          scmpublictoast(grpManageLan.optSuccess, 1000);
          $gcmdGrpList.sendRequestForList();
          $gcmdGrpList.sendRequestForStats();
        }
      } else if (data.status == "error") {
        scmpublictoast(grpManageLan.optFail, 2000);
      }
    },
    error : function() {
    }
  });
};

/**
 * 群组推荐专用
 */

// 改变url
GrpManage.changeUrl = function(targetModule) {
  var json = {};
  var oldUrl = window.location.href;
  var index = oldUrl.lastIndexOf("model");
  var newUrl = window.location.href;
  if (targetModule != undefined && targetModule != "") {
    if (oldUrl.lastIndexOf("?") < 0) {
      if (index < 0) {
        newUrl = oldUrl + "?model=" + targetModule;
      } else {
        newUrl = oldUrl.substring(0, index) + "model=" + targetModule;
      }
    } else {
      if (index < 0) {
        newUrl = oldUrl + "&model=" + targetModule;
      } else {
        newUrl = oldUrl.substring(0, index) + "model=" + targetModule;
      }
    }

  }
  window.history.replaceState(json, "", newUrl);
}
