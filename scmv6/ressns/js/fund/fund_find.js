var FundFind = FundFind ? FundFind : {}

var nowPageNum = 1;
FundFind.funPagingConditions = function() {
  FundFind.initShowPage(0, 5);
};

FundFind.prePage = function(obj) {
  if (!$(obj).hasClass("no_page-for_selected")) {
    FundFind.showPrePage(nowPageNum, 5);
    --nowPageNum;
    FundFind.showIsClickPage(nowPageNum, 5);
  }
}

FundFind.nextPage = function(obj) {
  if (!$(obj).hasClass("no_page-for_selected")) {
    FundFind.showNextPage(nowPageNum, 5);
    ++nowPageNum;
    FundFind.showIsClickPage(nowPageNum, 5);
  }
};

FundFind.initShowPage = function(nowPageNum, pageSize) {
  FundFind.showNextPage(nowPageNum, 5);
  ++nowPageNum;
  FundFind.showIsClickPage(nowPageNum, 5);
};
FundFind.showIsClickPage = function(nowPageNum, pageSize) {
  var $allItem = $(".dev_region_ul").find(".setting-list_page-item");
  if (nowPageNum - 1 <= 0) {// 没有上一页
    $(".setting-list_page-up").addClass("no_page-for_selected");
  } else {
    $(".setting-list_page-up").removeClass("no_page-for_selected");
  }
  var allPageNum = Math.ceil($allItem.length / pageSize);
  if (nowPageNum + 1 > allPageNum) {// 没有下一页
    $(".setting-list_page-down").addClass("no_page-for_selected");
  } else {
    $(".setting-list_page-down").removeClass("no_page-for_selected");
  }
};
FundFind.showNextPage = function(nowPageNum, pageSize) {
  var nextPageNum = nowPageNum + 1;
  var $allItem = $(".dev_region_ul").find(".setting-list_page-item");
  var allPageNum = Math.ceil($allItem.length / pageSize);
  if (nextPageNum >= 0 && pageSize > 0 && nextPageNum <= allPageNum) {
    $allItem.addClass("setting-list_page-item_hidden");// 先全部隐藏
    $allItem.each(function(i) {
      if (Math.ceil((i + 1) / pageSize) == nextPageNum) {
        $(this).removeClass("setting-list_page-item_hidden");
      }
    });
  }
  FundFind.showIsClickPage(nextPageNum,pageSize);
};
FundFind.showPrePage = function(nowPageNum, pageSize) {
  var upPageNum = nowPageNum - 1
  var $allItem = $(".dev_region_ul").find(".setting-list_page-item");
  if (upPageNum > 0 && pageSize > 0) {
    $allItem.addClass("setting-list_page-item_hidden");// 先全部隐藏
    $allItem.each(function(i) {
      if (Math.ceil((i + 1) / pageSize) == upPageNum) {
        $(this).removeClass("setting-list_page-item_hidden");
      }
    });
  }
  FundFind.showIsClickPage(upPageNum,pageSize);
};

FundFind.searchRegion = function() {
  var searchRegion = $("#searchRegion").val();
  if (searchRegion != "") {
    var flag = FundFind.showRegionList(searchRegion);
    if(flag){
      $.ajax({
        url : "/prjweb/fundfind/fundRegion",
        type : "post",
        data : {
          "searchRegion" : searchRegion,
        },
        dataType : "json",
        success : function(data) {
           if(data.result != ''){
             FundFind.showRegionList(data.result);
           }else{
             FundFind.showNextPage(0, 5);
           }
        },
        error : function() {
          
        }
      });
    }
  } else {
    FundFind.showNextPage(0, 5);
  }

}

FundFind.showRegionList = function(searchRegion){
  var flag = true;
  if(/^[a-zA-Z]+$/.test(searchRegion)){
    searchRegion = searchRegion.toLowerCase();
    $(".dev_agencyName").each(function() {
      regCode = $(this).attr("title").toLowerCase();
      if (regCode.indexOf(searchRegion) != -1) {
        var pageNum = $(this).parent().val();
        /* var prePageNum = Math.ceil(pageNum / 5) - 1; */
        var prePageNum = Math.ceil(pageNum / 5);
        FundFind.showNextPage(prePageNum, 5);
        flag = false;
        return false;
      }
    })
  }else{
    var searchRegionCode = FundFind.paseUniCode(searchRegion);
    $(".dev_agencyName").each(function() {
      var regCode = FundFind.paseUniCode($(this).attr("title"));
      if (regCode.indexOf(searchRegionCode) != -1) {
        var pageNum = $(this).parent().val();
        var prePageNum = Math.ceil(pageNum / 5) - 1;
        FundFind.showNextPage(prePageNum, 5);
        flag = false;
        return false;
      }
    })
  }
  return flag;
}

// 将中文转换为unicode编码
FundFind.paseUniCode = function(str) {
  var strCode = "";
  var len = str.length;
  i = 0;
  while (i < len) {
    strCode += "\\u" + str.charCodeAt(i).toString(16);
    i++;
  }
  return strCode;
}
FundFind.clickRegion = function(obj) {
  var RegCode = $(obj).find(".dev_agencyName").attr("code");
  if ($(obj).hasClass("item_list-align_slected")) {
    $(obj).removeClass("item_list-align_slected");
    regionArray.splice($.inArray(RegCode, regionArray), 1);
  } else {
    $(obj).addClass("item_list-align_slected");
    regionArray.push(RegCode);
  }
  FundFind.loadFundFindList(false);
}

FundFind.clickType = function(obj, num) {
  if ($(obj).hasClass("item_list-align_slected")) {
    $(obj).removeClass("item_list-align_slected");
    $("#seniorityCodeSelect").val("0");
  } else {
    $(".new-mainpage_left-list_body-item").removeClass("item_list-align_slected");
    $(obj).addClass("item_list-align_slected");
    $("#seniorityCodeSelect").val(num);
  }
  FundFind.loadFundFindList(false);
}


FundFind.loadFundFindList = function(needAppend) {
  var searchKey = $.trim($("#searchKey").val());
  var regionStr = regionArray.join(",");
  // 获取选中的科技领域
  var scienceArray=new Array();
  $(".new-mainpage_left-sublist_body-item_selected").each(function() {
    scienceArray.push($(this).attr("id"));
  })
  var scinenceStr = scienceArray.join(",");
  var seniorityStr = $("#seniorityCodeSelect").val();

  window.Mainlist({
    name:"collectFundList",
    listurl: "/prjweb/fundfind/ajaxfundlist",
    listdata: {
      "regionCodesSelect" : regionStr,
      "seniorityCodeSelect" : seniorityStr,
      "scienceCodesSelect" : scinenceStr,
      "searchKey" : searchKey,
      "pageSize" : 10,
      "locale" : locale
    },
    listcallback: function(){
      // 加载logo
      FundFind.ajaxFundLogos();
    },
    method: "pagination",
   })
}

FundFind.ajaxFundLogos = function(){
    var des3FundAgencyIds = [];
    $(".main-list__item").each(function(){
      des3FundAgencyIds.push($(this).attr("des3id"));
    });
    $.ajax({
      url:"/prjweb/fund/ajaxrecommendlogo",
      type:"post",
      data:{"des3FundAgencyIds":des3FundAgencyIds.join()},
      dataType:"json",
      success:function(data){
        if(data){
          for(var i=0;i<data.length;i++){
            if(data[i].logoUrl!=null&&data[i].logoUrl.trim()!=""){
              $("img[class='logo_"+data[i].fundAgencyId+"']").attr("src",data[i].logoUrl);
            }
          }
        }
      }
    })
  }
// 绑定滚动加载推荐的基金
FundFind.FundlistScroll = function(){
  if($("#totalCount").val()<=10){
    return;
  }else if($("#pageNum").val() == $("#totalPages").val()){
    $(".main-list").off("scroll");
  }else{
    $(".main-list").scroll(function(){
    　　var scrollTop = $(this).scrollTop();
    　　var scrollHeight = $(document).height();
    　　var windowHeight = $(this).height();
    　　if(scrollTop + windowHeight <= scrollHeight){
    　　　　FundFind.loadFundFindList(true);
    　　}
  });
  }
}
// 推荐选择方法
FundFind.addCondition = function(obj, typeclass) {
  if (typeclass == "type_sen") {
    $("." + typeclass).removeClass("selector__list-target");
    $("." + typeclass).attr('onclick', 'FundFind.addCondition(this,\'' + typeclass + '\')');
    $("#searchseniority").val($(obj).attr("value"));
  } 
  $(obj).addClass("selector__list-target");
  $(obj).attr('onclick', 'FundFind.delCondition(this,\'' + typeclass + '\')');
}
// 推荐取消选择方法
FundFind.delCondition = function(obj, typeclass) {
  $(obj).removeClass("selector__list-target")
  $(obj).attr('onclick', 'FundFind.addCondition(this,\'' + typeclass + '\')');
  $("#searchseniority").val("");
}