var PCAgency = PCAgency ? PCAgency : {};

/**
 * 赞、取消赞资助机构操作 des3AgencyId: 加密的资助机构ID, optType: 1(赞), 0(取消赞)
 */
PCAgency.ajaxAward = function(obj, des3AgencyId, optType) {

  $.ajax({
    url : "/prjweb/agency/ajaxaward",
    type : "post",
    data : {
      "Des3FundAgencyId" : des3AgencyId,
      "optType" : optType
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        var awardDom = $(obj);
        var awardCount = data.awardCount;
        PCAgency.initAwardOpt(awardDom, awardCount, optType);
      } else {
        // TODO 提示
      }
    },
    error : function() {
      // TODO 提示
    }
  });
}

/**
 * 关注、取消关注资助机构操作 des3AgencyId: 加密的资助机构ID, optType: 1(关注), 0(取消关注)
 */
PCAgency.ajaxInterest = function(obj, des3AgencyId, optType) {

  $.ajax({
    url : "/prjweb/agency/ajaxinterest",
    type : "post",
    data : {
      "Des3FundAgencyId" : des3AgencyId,
      "optType" : optType
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        var interestDom = $(obj);
        var interestCount = data.interestCount;
        PCAgency.initInterestOpt(interestDom, interestCount, optType);
      } else {
        if (data.errorMsg == "interest agency has reached the maximum") {
          scmpublictoast(fundRecommend.addFundSizeFail, 2000);
        } else if (data.errorMsg == "interested in at least one funding agency") {
          scmpublictoast(fundRecommend.addFundEmptyFail, 2000);
        }
      }
    },
    error : function() {
      // TODO 提示
    }
  });
}

/**
 * 动态列表中的关注、取消关注资助机构操作 des3AgencyId: 加密的资助机构ID, optType: 1(关注), 0(取消关注)
 */
PCAgency.ajaxDynamicInterest = function(obj, des3AgencyId, optType) {
  $.ajax({
    url : "/prjweb/agency/ajaxinterest",
    type : "post",
    data : {
      "Des3FundAgencyId" : des3AgencyId,
      "optType" : optType
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        var interestCount = data.interestCount;
        var cancelInterestOptDom = $("div.agency_cancel_interest_opt[agencyDes3Id='" + des3AgencyId + "']");
        var interestOptDom = $("div.agency_interest_opt[agencyDes3Id='" + des3AgencyId + "']");
        // 关注、取消关注显示
        if (optType == 1) {
          cancelInterestOptDom.show();
          interestOptDom.hide();
        } else {
          cancelInterestOptDom.hide();
          interestOptDom.show();
        }
      } else {
        if (data.errorMsg == "interest agency has reached the maximum") {
          scmpublictoast(fundRecommend.addFundSizeFail, 2000);
        } else if (data.errorMsg == "interested in at least one funding agency") {
          scmpublictoast(fundRecommend.addFundEmptyFail, 2000);
        }else if(data.errorMsg == "agencyId is null or not exists"){
          scmpublictoast(fundRecommend.fundAgencyIsDel, 2000);
        }
      }
    },
    error : function() {
      // TODO 提示
    }
  });
}

/**
 * 初始化赞操作
 */
PCAgency.initAwardOpt = function(awardDom, awardCount, optType) {
  var awardA = awardDom.hasClass("award_a") ? awardDom : awardDom.find("a.award_a");

  // 赞操作
  if (optType == 1) {
    if(awardCount>=1000){
      awardCount = "1k+";
    }
    awardA.find("span.award_word_span").html(fundRecommend.unlike + "(" + awardCount + ")");
    optType = 0;
    awardA.find("div.agency_award_div").addClass("new-Standard_Function-bar_selected");
  } else {
    if (awardCount > 0) {
      if(awardCount>=1000){
        awardCount = "1k+";
      }
      awardA.find("span.award_word_span").html(fundRecommend.like + "(" + awardCount + ")");
    } else {
      awardA.find("span.award_word_span").html(fundRecommend.like);
    }
    optType = 1;
    awardA.find("div.agency_award_div").removeClass("new-Standard_Function-bar_selected");
  }
  awardA.removeAttr("onclick");
  awardA.attr("onclick", "PCAgency.ajaxAward($(this), '" + awardA.attr("resId") + "', " + optType + ")");
}

/**
 * 初始化关注操作
 */
PCAgency.initInterestOpt = function(interestDom, interestCount, hasInterested) {
  // 是否已关注
  var interest = hasInterested == 1 ? true : false;
  var optType = 0;
  var interestA = interestDom.hasClass("interest_a") ? interestDom : interestDom.find("a.interest_a");
  interestA.find("span.interest_word_span").html(fundRecommend.unfocus);
  interestA.find("div.agency_interest_div").addClass("new-Standard_Function-bar_selected");
  if (!interest) {
    optType = 1;
    interestA.find("span.interest_word_span").html(fundRecommend.focus);
    interestA.find("div.agency_interest_div").removeClass("new-Standard_Function-bar_selected");
  }
  interestA.removeAttr("onclick");
  interestA.attr("onclick", "PCAgency.ajaxInterest($(this), '" + interestA.attr("resId") + "', " + optType + ")")
}

/**
 * 初始化分享数
 */
PCAgency.initShareOpt = function(shareDom, shareCount) {
  var shareA = shareDom.hasClass("share_a") ? shareDom : shareDom.find("a.share_a");
  if (shareCount > 0) {
    if(shareCount>=1000){
      shareCount = "1k+";
    }
    shareA.find("span.share_word_span").html(fundRecommend.share + "(" + shareCount + ")");
  } else {
    shareA.find("span.share_word_span").html(fundRecommend.share);
  }
}

/**
 * 初始化资助机构列表赞、分享、关注
 */
PCAgency.ajaxInitOptStatusAndCount = function() {
  var data = new Array();
  $(".need_init_item").each(function() {
    var des3Id = $(this).attr("des3Id");
    data.push(des3Id);
  });
  $.ajax({
    url : "/prjweb/agency/ajaxinit",
    type : "post",
    data : {
      "des3AgencyIds" : data.join(",")
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        if (data.initInfo != null && data.initInfo.length > 0) {
          var initData = data.initInfo;
          var dataLength = initData.length;
          for (var i = 0; i < dataLength; i++) {
            var infoItem = initData[i];
            var agencyId = infoItem.id;
            var hasAward = infoItem.hasAward;
            var hasInterested = infoItem.interested;
            var awardCount = infoItem.awardCount;
            var shareCount = infoItem.shareCount;
            var interestCount = infoItem.interestCount;
            var domItem = $("div.need_init_item[des3Id='" + agencyId + "']:first");
            // 初始化关注操作
            PCAgency.initInterestOpt(domItem, interestCount, hasInterested);
            // 初始化赞操作
            PCAgency.initAwardOpt(domItem, awardCount, hasAward);
            // 初始化分享数
            PCAgency.initShareOpt(domItem, shareCount);
            domItem.removeClass("need_init_item");
          }
        }
      } else {
        // TODO 显示提示信息
      }
    },
    error : function() {
      // TODO 显示提示信息
    }
  });
}

/**
 * 初始化群组动态中的资助机构的关注状态
 */
PCAgency.ajaxInitGrpDynInterestStatus = function() {
  $.ajax({
    url : "/prjweb/interest/ajaxall",
    type : "post",
    data : {},
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        var allIds = data.allInterestedId;
        if (allIds != null && allIds != "" && typeof (allIds) != "undefined") {
          var ids = allIds.split(",");
          var idLength = ids.length;
          $("input.need_init_agency").each(function() {
            var des3Id = $(this).val();
            for (var i = 0; i < idLength; i++) {
              if (ids[i] == des3Id) {
                var cancelInterestOptDom = $("div.agency_cancel_interest_opt[agencyDes3Id='" + des3Id + "']");
                var interestOptDom = $("div.agency_interest_opt[agencyDes3Id='" + des3Id + "']");
                cancelInterestOptDom.show();
                interestOptDom.hide();
              }
            }
          });
        }
      }
    },
    error : function(data) {
    }
  });
}

// 初始化群组动态中分享资助机构的赞和分享操作
PCAgency.ajaxInitGrpDynAwardAndShare = function() {
  $.ajax({
    url : "/prjweb/interest/ajaxall",
    type : "post",
    data : {},
    dataType : "json",
    success : function(data) {

    },
    error : function() {
    }
  });
}

/**
 * 分享到站外时更新统计数
 */
PCAgency.updateShareOutsideCount = function() {
  var resType = $("#share_to_scm_box").attr("resType");
  var des3ResId = $("#share_to_scm_box").attr("des3ResId");
  var shareToPlatform = $("#share_platform_val").val();
  // 资助机构
  if (resType == 25 || resType == "agency") {
    $.ajax({
      url : "/prjweb/share/ajaxupdate",
      type : "post",
      data : {
        "Des3FundAgencyId" : des3ResId,
        "shareToPlatform" : shareToPlatform
      },
      dataType : "json",
      success : function(data) {
        if (data.result = "success") {
          var shareCount = data.shareCount;
          var domItem = $("div[des3Id='" + des3ResId + "']:first");
          PCAgency.initShareOpt(domItem, shareCount);
        }
      },
      error : function() {
      }
    });
  }
  // 基金
  if (resType == 11 || resType == "fund") {

  }
}
