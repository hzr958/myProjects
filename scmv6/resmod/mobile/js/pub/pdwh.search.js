var PdwhSearch = PdwhSearch ? PdwhSearch : {};

// 加载发现页面成果列表
var $mobilepaperList;
PdwhSearch.ajaxLoadPaper = function() {
  document.getElementsByTagName('body')[0].scrollTop = 0;
  $(".main-list__list").empty();
  // 保存过滤条件，请求列表页
  var includeType = $("#includeType").val(),
  // 收录类别、科技领域
  pubDBIds = $("#pubDBIds").val(), scienceAreaIds = $("#scienceAreaIds").val(), publishYear = $
      .trim($("#searchPubYear").val()), pubType = $.trim($("#searchPubType").val()), searchString = $.trim($(
      "#searchStringInput").val()), orderBy = $.trim($("#orderBy").val());
  $mobilepaperList = window.Mainlist({
    name : "mobile_find_pub_list",
    listurl : "/pub/querylist/ajaxpdwhpub",
    listdata : {
      "publishYear" : publishYear,
      "searchPubType" : pubType,
      "des3AreaId" : scienceAreaIds,
      "searchString" : searchString,
      "includeType" : pubDBIds,
      "orderBy" : orderBy,
      "serviceType" : "paperListInSolr"
    },
    method : "scroll",
    beforeSend : function() {
    },
    listcallback : function(xhr) {
      if ($("#mobile_find_pub_list_div").attr("total-count") == "0") {
        $("#no_record_tips").show();
        $("#mobile_find_pub_list_div").html("");
      } else {
        $("#no_record_tips").hide();
      }
      history.hideHistory();
    }
  });
}

// 加载发现页面成果列表
var $mobilePatentList;
PdwhSearch.ajaxLoadPatent = function() {
  document.getElementsByTagName('body')[0].scrollTop = 0;
  $(".main-list__list").empty();
  // 保存过滤条件，请求列表页
  var includeType = $("#includeType").val(),
  // 收录类别、科技领域
  pubDBIds = $("#pubDBIds").val(), scienceAreaIds = $("#scienceAreaIds").val(), publishYear = $
      .trim($("#searchPubYear").val()), pubType = $.trim($("#searchPubType").val()), searchString = $.trim($(
      "#searchStringInput").val()), orderBy = $.trim($("#orderBy").val());
  $mobilePatentList = window.Mainlist({
    name : "mobile_find_patent_list",
    listurl : "/pub/querylist/ajaxpdwhpub",
    listdata : {
      "publishYear" : publishYear,
      "searchPubType" : pubType,
      "des3AreaId" : scienceAreaIds,
      "searchString" : searchString,
      "includeType" : pubDBIds,
      "orderBy" : orderBy,
      "serviceType" : "patentListInSolr"
    },
    method : "scroll",
    beforeSend : function() {

    },
    listcallback : function(xhr) {
      if ($("#mobile_find_patent_list_div").attr("total-count") == "0") {
        $("#no_record_tips").show();
        $("#mobile_find_patent_list_div").html("");
      } else {
        $("#no_record_tips").hide();
      }
      history.hideHistory();
    }
  });
}

// 基准库快速分享
PdwhSearch.quickShareDyn = function(obj) {
  var des3ResId = $(obj).attr("des3ResId");
  var pubDb = $(obj).attr("pubDb");
  var pubId = $(obj).attr("pubId");
  var dataJson, url;
  dataJson = {
    "dynType" : "B2TEMP",
    "resType" : 22,
    "des3PubId" : des3ResId,
    "operatorType" : 3,
    "dbId" : "",
    "databaseType" : 2,
    "des3ResId" : des3ResId
  }
  url = "/dynweb/dynamic/ajaxrealtime";
  $.ajax({
    url : url,
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      if (data.result == "success") {
        scmpublictoast("分享成功", 1000);
        mobile.pub.initPubOptStatistics(des3ResId, "PDWH");
      } else {
        scmpublictoast("分享失败", 1000);
      }
    },
    error : function() {
      scmpublictoast("分享失败", 1000);
    }
  });
}
