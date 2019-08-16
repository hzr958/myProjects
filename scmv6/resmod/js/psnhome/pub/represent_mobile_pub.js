var RepresentPub = RepresentPub || {};

RepresentPub.loadPubList = function() {
  window
      .Mainlist({
        name : "pub_list",
        listurl : "/pub/represent/ajaxpublist",
        listdata : {},
        method : "scroll",
        noresultHTML : "<div style='width: 100%; height: 36px; line-height: 36px; text-align: center; font-size: 16px; color: rgba(0, 0, 0, 0.54); margin-top: 24px'>还没有设置代表成果</div>",
        listcallback : function(xhr) {
        }
      });
};

RepresentPub.loadOpenPubList = function() {
  window
      .Mainlist({
        name : "pub_list",
        listurl : "/pub/represent/ajaxopenpublist",
        listdata : {
          "publishYear" : $("#publishYear").val(),
          "pubType" : $("#pubType").val(),
          "includeType" : $("#includeType").val(),
          "orderBy" : $("#orderBy").val(),
          "searchKey" : $("#searchKey").val(),
        },
        method : "scroll",
        noresultHTML : "<div style='width: 100%; height: 36px; line-height: 36px; text-align: center; font-size: 16px; color: rgba(0, 0, 0, 0.54); margin-top: 24px'>未找到符合条件的记录</div>",
        listcallback : function(xhr) {
          var presentPubIds = $("#representDes3PubIds").val();
          if (presentPubIds) {
            var pubIds = presentPubIds.split(",");
            $.each(pubIds, function(i, val) {
              var addPubDiv = $(".main-list__item[des3pubid='" + val + "']").find(".material-icons");
              /*
               * $(addPubDiv).attr("onclick",
               * "").html("已添加").addClass("new-Represent_achieve-item_func-addbtn");
               */
              $(addPubDiv).attr("onclick", "").html("check");
            });
          }
        }
      });
};

RepresentPub.removeRepresentPub = function(obj) {
  if (obj != undefined) {// 防止重复点击
    BaseUtils.doHitMore(obj, 3000);
  }
  var $item = $(obj.closest(".main-list__item"));
  $item.addClass("hasdelete");
  var des3PubIds = [];
  $(".main-list__item").each(function() {
    if (!$(this).hasClass("hasdelete")) {
      des3PubIds.push($(this).attr("des3pubid"));
    }
  });
  $.ajax({
    url : "/pub/represent/ajaxsaverepresentpub",
    data : {
      'representDes3PubIds' : des3PubIds.join(","),
    },
    type : "post",
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        $item.hide();
      } else {
        scmpublictoast("移除失败", 1000, 2);
        $item.removeClass("hasdelete");
      }
    },
    error : function(data) {
      scmpublictoast("移除失败", 1000, 2);
      $item.removeClass("hasdelete");
    }
  });
}
RepresentPub.addRepresentPub = function(obj) {
  if (obj != undefined) {// 防止重复点击
    BaseUtils.doHitMore(obj, 3000);
  }
  if ($(obj).hasClass("new-Represent_achieve-item_func-addbtn")) {
    return;
  }
  var presentPubIds = $("#representDes3PubIds").val();
  if (presentPubIds && presentPubIds.split(",").length >= 10) {
    scmpublictoast("最多添加10个", 2000, 3);
    return;
  }
  presentPubIds += "," + $(obj.closest(".main-list__item")).attr("des3pubid");
  $.ajax({
    url : "/pub/represent/ajaxsaverepresentpub",
    data : {
      'representDes3PubIds' : presentPubIds,
    },
    type : "post",
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        /*
         * $(obj).attr("onclick",
         * "").html("已添加").addClass("new-Represent_achieve-item_func-addbtn");
         */
        $(obj).attr("onclick", "").html("check");
        $("#representDes3PubIds").val(presentPubIds);
      } else {
        scmpublictoast("添加失败", 1000, 2);
      }
    },
    error : function(data) {
      scmpublictoast("添加失败", 1000, 2);
    }
  });
}