var FundRecommend = FundRecommend ? FundRecommend : {}
// 赞、取消赞操作
FundRecommend.awardOperation = function(obj, id) {
  var id = $(obj).attr("resid");
  var type = $(obj).attr("isAward");
  $.ajax({
    url : "/prjweb/fund/ajaxaward",
    type : "post",
    data : {
      "encryptedFundId" : id,
      "awardOperation" : type
    },
    dataType : "json",
    success : function(data) {
      BaseUtils.ajaxMobileTimeOut(data, function() {
        if (data.result == "success") {
          var count = data.awardCount;
          callBackAward(obj,count);
        } else {
          // TODO 提示
        }
      });
    },
    error : function() {
      // TODO 提示
    }
  });
};

// 收藏、取消收藏操作
FundRecommend.collectCoperation = function(obj) {
  var id = $(obj).attr("resid");
  var type = $(obj).attr("collect");
  $.ajax({
    url : "/prjweb/fund/ajaxcollect",
    type : "post",
    data : {
      "encryptedFundId" : id,
      "collectOperate" : type
    },
    dataType : "json",
    success : function(data) {
      BaseUtils.ajaxMobileTimeOut(data, function() {
        if (data.result == "success") {
          callBackCollect(obj);
        } else {
          scmpublictoast(fundRecommend.saveFail, 2000);
        }
      });
    },
    error : function() {
      scmpublictoast(fundRecommend.saveFail, 2000);
    }
  });
};
// 基金收藏页面取消收藏操作
FundRecommend.unCollectCoperation = function(obj, id) {
  var $objSpan = $(obj).find("span");
  var type = $objSpan.attr("isAward");
  $.ajax({
    url : "/prjweb/fund/ajaxcollect",
    type : "post",
    data : {
      "encryptedFundId" : id,
      "collectOperate" : type
    },
    dataType : "json",
    success : function(data) {
      BaseUtils.ajaxMobileTimeOut(data, function() {
        if (data.result == "success") {
          fundList();
        } else {
          scmpublictoast(fundRecommend.saveFail, 2000);
        }
      });
    },
    error : function() {
      scmpublictoast(fundRecommend.saveFail, 2000);
    }
  });
};
// 基金快速分享
FundRecommend.quickShareDyn = function(obj) {
  var $obj = $(obj);
  var des3ResId = $obj.attr("des3ResId");
  var resInfoId = $obj.attr("fundId");
  var resInfoJson = "";
  resInfoJson = JSON.stringify({
    "fund_desc_zh" : $("#zhShowDesc_" + resInfoId).val(),
    "fund_desc_en" : $("#enShowDesc_" + resInfoId).val(),
    "fund_title_zh" : $("#zhTitle_" + resInfoId).val(),
    "fund_title_en" : $("#enTitle_" + resInfoId).val()
  });
  var data = {
    "dynType" : "B2TEMP",
    "dynText" : "",
    "resType" : "11",
    "des3PubId" : des3ResId,
    "operatorType" : 3,
    "des3ResId" : des3ResId,
    "resInfoJson" : resInfoJson
  };
  $.ajax({
    url : "/dynweb/dynamic/ajaxrealtime",
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      if (data.result == "success") {
        var shareContentObj = $(".span_share[resid='" + des3ResId + "']");
        var text = shareContentObj.text();
        var count = 0;
        if (text != "") {
          count = $.trim(text.replace(/[^0-9]+/g, ''));
        }
        count = parseInt(count) ? parseInt(count) + 1 : 1;
        if (count > 0 && count <= 999) {
          shareContentObj.text("分享 (" + count + ")");
        }
        if (count > 999) {
          shareContentObj.text("分享 (1k+)");
        }

        
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

FundRecommend.ajaxFundLogos = function() {
  $.ajax({
    url : "/prjweb/fund/ajaxrecommendlogo",
    type : "post",
    data : {
      "des3FundAgencyIds" : $("input[name='des3FundAgencyIds']:last").val()
    },
    dataType : "json",
    success : function(data) {
      if (data) {
        for (var i = 0; i < data.length; i++) {
          if (data[i].logoUrl != null && data[i].logoUrl.trim() != "") {
            $("img[class='logo_" + data[i].fundAgencyId + "']").attr("src", data[i].logoUrl);
          }
        }
      }
    }
  })
};
// 推荐选择方法
FundRecommend.addCondition = function(obj, typeclass) {
  if (typeclass == "type_sen" || typeclass == "type_agency" || typeclass == "type_time") {// 申请资格、资助机构、时间是单选的
    $("." + typeclass).removeClass("selector__list-target");
    $("." + typeclass).attr('onclick', 'FundRecommend.addCondition(this,\'' + typeclass + '\')');
  } else if ($(obj).attr('value') == "0") {// 申请资格、科技领域选中不限，清空选中
    $("." + typeclass).removeClass("selector__list-target");
    $("." + typeclass).attr('onclick', 'FundRecommend.addCondition(this,\'' + typeclass + '\')');
  } else if ($("." + typeclass + "[value='0']").hasClass("selector__list-target")) {// 申请资格、科技领域选中其他，不限如果选中要去掉
    $("." + typeclass + "[value='0']").removeClass("selector__list-target");
    $("." + typeclass).attr('onclick', 'FundRecommend.addCondition(this,\'' + typeclass + '\')');
  }
  $(obj).addClass("selector__list-target");
  $(obj).attr('onclick', 'FundRecommend.delCondition(this,\'' + typeclass + '\')');

}
// 推荐取消选择方法
FundRecommend.delCondition = function(obj, typeclass) {
  $(obj).removeClass("selector__list-target")
  $(obj).attr('onclick', 'FundRecommend.addCondition(this,\'' + typeclass + '\')');
}

// 选中地区
FundRecommend.chooseArea = function(obj, areaId) {
  var $this = $(obj);
  if ($this.find('.dev_region_choose').hasClass("icon__region--slt")) {
    $this.find('.dev_region_choose').removeClass("icon__region--slt");
    $this.removeClass("has_selected");
  } else {
    $this.find('.dev_region_choose').addClass("icon__region--slt");
    $this.addClass("has_selected");
  }
}

FundRecommend.saveSelectedArea = function() {
  var selectedArea = [];
  $(".has_selected").each(function() {
    selectedArea.push($(this).attr("areaId"));
  });
  $("#selectedAreaId").val(selectedArea.join(","));
  $("#fundAreaForm").submit();
};
FundRecommend.changeRgb = function(obj) {
  $(obj).css("background-color", "#F5F5F5");
};