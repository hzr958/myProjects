var RepresentPrj = RepresentPrj || {};

RepresentPrj.loadPrjList = function() {
  window
      .Mainlist({
        name : "prj_list",
        listurl : "/prj/mobile/represent/ajaxprjlist",
        listdata : {},
        method : "scroll",
        noresultHTML : "<div style='width: 100%; height: 36px; line-height: 36px; text-align: center; font-size: 16px; color: rgba(0, 0, 0, 0.54); margin-top: 24px'>还没有设置代表项目</div>",
        listcallback : function(xhr) {
        }
      });
};

RepresentPrj.loadOpenPrjList = function() {
  window
      .Mainlist({
        name : "prj_list",
        listurl : "/prj/mobile/represent/ajaxopenprjlist",
        listdata : {
          "orderBy" : $("#orderBy").val(),
          "agencyNames" : $("#agencyNames").val(),
          "fundingYear" : $("#fundingYear").val(),
          "searchKey" : $("#searchKey").val(),
        },
        method : "scroll",
        noresultHTML : "<div style='width: 100%; height: 36px; line-height: 36px; text-align: center; font-size: 16px; color: rgba(0, 0, 0, 0.54); margin-top: 24px'>未找到符合条件的记录</div>",
        listcallback : function(xhr) {
          var addToRepresentPrjIds = $("#addToRepresentPrjIds").val();
          if (addToRepresentPrjIds) {
            var prjIds = addToRepresentPrjIds.split(",");
            $.each(prjIds, function(i, val) {
              var addPubDiv = $(".main-list__item[des3prjid='" + val + "']").find(".add_prj_div");
             /* $(addPubDiv).attr("onclick", "").html("已添加").addClass("new-Represent_achieve-item_func-addbtn");*/
              $(addPubDiv).attr("onclick", "").html("check");
            });
          }
        }
      });
};

RepresentPrj.removeRepresentPrj = function(obj) {
  if (obj != undefined) {// 防止重复点击
    BaseUtils.doHitMore(obj, 3000);
  }
  var $item = $(obj.closest(".main-list__item"));
  $item.addClass("hasdelete");
  var des3PrjIds = [];
  $(".main-list__item").each(function() {
    if (!$(this).hasClass("hasdelete")) {
      des3PrjIds.push($(this).attr("des3prjid"));
    }
  });
  $.ajax({
    url : "/prj/mobile/represent/ajaxsaverepresentprj",
    data : {
      'addToRepresentPrjIds' : des3PrjIds.join(","),
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
RepresentPrj.addRepresentPrj = function(obj) {
  if (obj != undefined) {// 防止重复点击
    BaseUtils.doHitMore(obj, 3000);
  }
  if ($(obj).hasClass("new-Represent_achieve-item_func-addbtn")) {
    return;
  }
  var addToRepresentPrjIds = $("#addToRepresentPrjIds").val();
  if (addToRepresentPrjIds && addToRepresentPrjIds.split(",").length >= 5) {
    scmpublictoast("最多添加5个", 2000, 3);
    return;
  }
  addToRepresentPrjIds += "," + $(obj.closest(".main-list__item")).attr("des3prjid");
  $.ajax({
    url : "/prj/mobile/represent/ajaxsaverepresentprj",
    data : {
      'addToRepresentPrjIds' : addToRepresentPrjIds,
    },
    type : "post",
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        /*$(obj).attr("onclick", "").html("已添加").addClass("new-Represent_achieve-item_func-addbtn");*/
        $(obj).attr("onclick", "").html("check");
        $("#addToRepresentPrjIds").val(addToRepresentPrjIds);
      } else {
        scmpublictoast("添加失败", 1000, 2);
      }
    },
    error : function(data) {
      scmpublictoast("添加失败", 1000, 2);
    }
  });
}