var MobileAgency = MobileAgency ? MobileAgency : {};

/**
 * 赞、取消赞资助机构操作 des3AgencyId: 加密的资助机构ID, optType: 1(赞), 0(取消赞)
 */
MobileAgency.ajaxAward = function(obj) {
  var optType = $(obj).attr("isaward");
  var des3AgencyId = $(obj).attr("resid");
  $.ajax({
    url : "/prjweb/agency/ajaxaward",
    type : "post",
    data : {
      "Des3FundAgencyId" : des3AgencyId,
      "optType" : optType==1 ? 0 : 1
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        var awardCount = data.awardCount;
        callBackAward(obj,awardCount);
      } else {
        scmpublictoast("网络繁忙，请稍后再试", 2000);
      }
    },
    error : function() {
      scmpublictoast("网络繁忙，请稍后再试", 2000);
    }
  });
}

/**
 * 关注、取消关注资助机构操作 des3AgencyId: 加密的资助机构ID, optType: 1(关注), 0(取消关注)
 */
MobileAgency.ajaxInterest = function(obj) {
  var optType = $(obj).attr("collect");
  var des3AgencyId = $(obj).attr("resid");
  $.ajax({
    url : "/prjweb/agency/ajaxinterest",
    type : "post",
    data : {
      "Des3FundAgencyId" : des3AgencyId,
      "optType" : optType==1 ? 0 : 1
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        callBackCollect(obj);
      } else {
        if (data.errorMsg == "interest agency has reached the maximum") {
          scmpublictoast("最多只能关注10个资助机构", 2000);
        } else if (data.errorMsg == "interested in at least one funding agency") {
          scmpublictoast("至少关注1个资助机构", 2000);
        } else {
          scmpublictoast("网络繁忙，请稍后再试", 2000);
        }
      }
    },
    error : function() {
      scmpublictoast("网络繁忙，请稍后再试", 2000);
    }
  });
}

/**
 * 初始化赞操作
 */
MobileAgency.initAwardOpt = function(awardDom, awardCount, optType) {
  awardDom.hide();
  var agencyItem = awardDom.closest("div.agency_show_item");
  if (awardCount > 0) {
    agencyItem.find("span.cancel_award_word_span").html("取消赞(" + awardCount + ")");
    agencyItem.find("span.award_word_span").html("赞(" + awardCount + ")");
  } else {
    agencyItem.find("span.cancel_award_word_span").html("取消赞");
    agencyItem.find("span.award_word_span").html("赞");
  }
  // 赞操作
  if (optType == 1) {
    agencyItem.find("span.agency_cancel_award_span").show();
  } else {
    agencyItem.find("span.agency_award_span").show();
  }
}

/**
 * 初始化关注操作
 */
MobileAgency.initInterestOpt = function(interestDom, interestCount, optType) {
  interestDom.hide();
  var agencyItem = interestDom.closest("div.agency_show_item");
  /*
   * if(interestCount > 0){ agencyItem.find("span.cancel_interest_word_span").html("取消关注(" +
   * interestCount + ")"); agencyItem.find("span.interest_word_span").html("关注(" + interestCount +
   * ")"); }else{
   */
  agencyItem.find("span.cancel_interest_word_span").html("取消关注");
  agencyItem.find("span.interest_word_span").html("关注");
  /* } */
  // 关注操作
  if (optType == 1) {
    agencyItem.find("span.agency_cancel_interest_span").show();
  } else {
    agencyItem.find("span.agency_interest_span").show();
  }
}

/**
 * 初始化分享数
 */
MobileAgency.initShareOpt = function(shareDom, shareCount) {
  if (shareCount > 0) {
    shareDom.find("span.share_word_span").html("分享(" + shareCount + ")");
  } else {
    shareDom.find("span.share_word_span").html("分享");
  }
}

/**
 * 初始化赞、分享、关注
 */
MobileAgency.ajaxInitOpt = function() {
  $(".need_init_agency_item").each(function() {
    var $this = $(this);
    var optInfo = $this.find("input.agency_opt_info");
    var awardStatus = optInfo.attr("awardStatus");
    var interestStatus = optInfo.attr("interestStatus");
    // 赞、取消赞
    if (awardStatus == "1") {
      $this.find(".agency_cancel_award_span").show();
      $this.find(".agency_award_span").hide();
    }
    // 关注、取消关注
    if (interestStatus == "1") {
      $this.find(".agency_cancel_interest_span").show();
      $this.find(".agency_interest_span").hide();
    }
    $this.removeClass("need_init_agency_item");
  });
}

// 弹出分享按钮
MobileAgency.shareToScm = function(des3ResId) {
  //需求变更,改为进入页面分享
//  $("#shareScreen").attr("des3ResId", des3ResId);
//  $('#dynamicShare').show();
  SmateCommon.mobileShareEntrance(des3ResId,"aidIns",$("#searchStringInput").val());
};

// 资助机构快速分享
MobileAgency.quickShareDyn = function(obj) {
  var $obj = $(obj);
  var des3ResId = $obj.attr("des3ResId");
  var data = {
    "dynType" : "B2TEMP",
    "dynText" : "",
    "resType" : "25",
    "des3PubId" : des3ResId,
    "operatorType" : 3,
    "des3ResId" : des3ResId
  };
  $.ajax({
    url : "/dynweb/dynamic/ajaxrealtime",
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      if (data.result == "success") {
        MobileAgency.updateShareCount(des3ResId, 1, "");
        newMobileTip(fundRecommend.shareSuccess);
      } else {
        newMobileTip(fundRecommend.shareFail);
      }

    },
    error : function(e) {
      newMobileTip(fundRecommend.shareFail);
    }
  });
};

/**
 * 分享时更新统计数
 */
MobileAgency.updateShareCount = function(des3ResId, shareToPlatform, comments) {
  $.ajax({
    url : "/prjweb/share/ajaxupdate",
    type : "post",
    data : {
      "Des3FundAgencyId" : des3ResId,
      "shareToPlatform" : shareToPlatform,
      "comments" : comments
    },
    dataType : "json",
    success : function(data) {
      if (data.result = "success") {
        var shareCount = data.shareCount;
        var domItem = $("span.agency_share_span[resId='" + des3ResId + "']:first");
        MobileAgency.initShareOpt(domItem, shareCount);
      }
    },
    error : function() {
    }
  });
}
