var Project = Project ? Project : {};
Project.showPrjList = function() {
  var data = {
    "agencyNames" : $("#agencyNames").val(),
    "fundingYear" : $("#fundingYear").val(),
    // "searchKey" : $("#searchKey").val(),
    "orderBy" : $("#orderBy").val(),
    "searchDes3PsnId" : $("#des3PsnId").val()
  }
  document.getElementsByTagName('body')[0].scrollTop = 0;
  $(".main-list__list").empty();
  window
      .Mainlist({
        name : "mobile_prj_list",
        listurl : "/prjweb/wechat/findprjs",
        listdata : data,
        method : "scroll",
        beforeSend : function() {
          $(".main-list__list").doLoadStateIco({
            status : 1,
            addWay : "append"
          });
        },
        listcallback : function(xhr) {
          var curPage = $("#curPage").val();
          var totalPages = $("#totalPages").val();
          var prjCount = $("#prjCount").val();
          if (curPage == undefined && totalPages == undefined) {
            if (prjCount != 0) {
              $(".main-list__list").empty();
              msg = "由于权限设置, 可能部分数据未显示";
              $(".main-list__list").append(
                  "<div class='paper_content-container_list main-list__item' style='border:none'><div style='padding:20px;color:#999'>"
                      + msg + "</div></div>");
            }
          } else {
            if (Number(curPage) >= Number(totalPages) && totalPages > 0) {
              var msg = "没有更多记录";
//              if ($("#hasPrivatePrj").val() == "true") {
              if ($("#hasPrivatePrj_new").val() == "true") {
                msg = "由于权限设置, 可能部分数据未显示";
              }
              $(".main-list__list")
                  .append(
                      "<div class='paper_content-container_list main-list__item' style='border:none;padding-bottom: 17%;border-bottom: 0px dashed #ccc!important;'><div style='padding:20px;color:#999'>"
                          + msg + "</div></div>");
            } else {
              $("#curPage").remove();
              $("#totalPages").remove();
            }
          }
          const parentNode = $("div[list-main='mobile_pub_list']");
          var norecord = parentNode.find("div.response_no-result");
          if (norecord.length > 0) {
            var tips = norecord[0].innerText;
            parentNode.find("div.response_no-result").remove();
            parentNode
                .append("<div class='paper_content-container_list main-list__item' style='border:0'><div class='response_no-result'>"
                    + tips + "</div></div>");
          }

        }
      });
};

// 项目详情界面 - 点赞
Project.award = function(obj) {
  var awardUrl = "";
  BaseUtils.doHitMore(obj, 100);
  var isAward = $(obj).attr("isAward");
  var des3PrjId = $(obj).attr("resid");
  if (isAward == "1") {
    awardUrl = "/prjweb/project/ajaxprjcancelaward";
  } else {
    awardUrl = "/prjweb/project/ajaxprjaddaward";
  }
  $.ajax({
    url : awardUrl,
    type : "post",
    data : {
      "resType" : 4,
      "resNode" : 1,
      "des3Id" : des3PrjId,
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        var awardCount = data.awardCount;
        callBackAward(obj, awardCount);
      }
    },
    error : function(date) {

    }
  });
}

// 快速分享
Project.quickShareDyn = function(obj) {
  var des3ResId = $(obj).attr("des3ResId");
  var data = {
    "dynType" : "B2TEMP",
    "resType" : 4,
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
        scmpublictoast("分享成功", 2000);
        var $spanObj = $("span[shareprjid='" + des3ResId + "']");
        var commentStr = $spanObj.html();
        var commentCount = commentStr.replace(/\D+/g, "");;
        commentCount = $.trim(commentCount) != "" ? parseInt(commentCount) : 0;
        $spanObj.html("分享 (" + (commentCount + 1) + ")");
      } else {
        scmpublictoast("分享失败", 2000);
      }

    },
    error : function(e) {
      scmpublictoast("分享失败", 2000);
    }
  });
}