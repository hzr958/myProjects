var FundRecommend = FundRecommend ? FundRecommend : {}
var recommendReq;
var collectReq;

// 显示推荐页面
FundRecommend.showRecommendPage = function(){
  // 隐藏基金收藏
  $("#collectioned_list_div").html("");
  $("#collectioned_list_container").hide();
  // 隐藏基金发现
  $("#fund_find_list_div").html("");
  $("#fund_find_list_container").hide();
  // 隐藏资助机构
  $("#agency_list_div").html("");
  $("#agency_list_container").hide();
    document.getElementsByClassName("nav__underline")[0].style.width =  95 + "px";
    document.getElementsByClassName("nav__underline")[0].style.left = 0 + "px";
    document.getElementsByClassName("nav__underline")[0].style.top = 38 + "px";
  FundRecommend.showConditions();
}

// 显示基金发现页面
FundRecommend.showFundFindPage =function(){
  $(window).off("scroll");
  $.ajax({
    url: "/prjweb/fundfind/ajaxmain",
    type: "post",
    data: {
      "locale":locale
    },
    dataType: "html",
    success: function(data){
      FundRecommend.ajaxTimeOut(data, function(){
        // 先隐藏基金推荐页面
      // 隐藏基金推荐
        $("#recommend_list_div").html("");
        $("#recommend_list_container").hide();
        $("#conditions_div").hide();
        // 隐藏基金收藏
        $("#collectioned_list_div").html("");
        $("#collectioned_list_container").hide();
        // 隐藏资助机构
        $("#agency_list_div").html("");
        $("#agency_list_container").hide();
        // 其他
        $("#conditions_div-container").html("");
        
        $("#fund_find_list_container").show();
        $("#fund_find_list_div").show();
        $("#fund_find_list_div").html(data);
        FundRecommend.initFundFind();
      });
    },
    error: function(){}
  });
  FundRecommend.changeUrl('findFund');
}

// 显示资助机构页面
FundRecommend.showAgencyPage =function(){
  $(window).off("scroll");
  $.ajax({
    url: "/prjweb/fundAgency/ajaxmain",
    type: "post",
    data: {
      
    },
    dataType: "html",
    success: function(data){
      FundRecommend.ajaxTimeOut(data, function(){
        // 隐藏基金推荐
        $("#recommend_list_div").html("");
        $("#recommend_list_container").hide();
        $("#conditions_div").hide();
        // 隐藏基金收藏
        $("#collectioned_list_div").html("");
        $("#collectioned_list_container").hide();
        // 隐藏基金发现
        $("#fund_find_list_div").html("");
        $("#fund_find_list_container").hide();
        $("#agency_list_div").show();
                $("#agency_list_container").show();
                $("#agency_list_footer").show();
        $("#agency_list_div").html(data);
        FundRecommend.initAgencyPage();
      });
    },
    error: function(){}
  });
  FundRecommend.changeUrl('fundAgency');
}

// 显示收藏页面
FundRecommend.showCollectionPage = function(){
  FundRecommend.showCollectedFundList(false);
  
}

// 取消
FundRecommend.cancelPsnFundConditions = function(){
  hideDialog("scienceArea_Box");
}


// ajax加载右边检索栏和推荐条件弹出框
FundRecommend.showConditions = function(){
  $(window).off("scroll");
  $.ajax({
    url: "/prjweb/fundconditions/ajaxshow",
    type: "post",
    data: {
      
    },
    dataType: "html",
    success: function(data){
      FundRecommend.ajaxTimeOut(data, function(){
        $("#agency_list_div").html('');
        $("#agency_list_div").hide();
        $("#agency_list_container").hide();
        $("#agency_list_footer").hide();
        $("#conditions_div").html(data);
        $("#recommend_list_container").show();
        $("#conditions_div").show();
        FundRecommend.loadRecommendFundList(false);
      });
    },
    error: function(){}
  });
}
// 获取选择条件
FundRecommend.getSelectConditions = function(){
  var data={};
  var agencyCode=[];
  var scienceCode=[];
  var seniorityCode=[];
  var timeCode=[];
  $(".dev_agencyName").each(function(){
    if($(this).hasClass("dev_slected")){
      var val = $(this).attr("code");
      agencyCode.push(val);
      data["agencyIdSelect"] = agencyCode.join(",");    
    }
  });
  $(".dev_scienceArea").each(function(){
    if($(this).hasClass("dev_slected")){
      var val = $(this).attr("code");
      scienceCode.push(val);
      data["scienceCodeSelect"] = scienceCode.join(",");
    }
  });
  if( $(".dev_seniority").attr("select_sen") == "true"){// 第一次进来不需要这个条件
    var seniority = $(".dev_seniority").attr("code");
    data["seniorityCodeSelect"] = seniority;
  }
  $(".dev_timeFlag").each(function(){
    if($(this).hasClass("dev_slected")){
      var val = $(this).attr("code");
      timeCode.push(val);
      data["timeCodeSelect"] = timeCode.join(",");
    }
  });
  return data;
}

FundRecommend.loadRecommendFundList = function(needAppend){
/*
 * if(collectReq != null){ collectReq.abort(); } $(window).off("scroll"); var pageNum = 1;
 * if(needAppend){ pageNum = parseInt($("#recommendPageNo").val()); if(isNaN(pageNum) ||
 * !needAppend){ pageNum = 1; }else{ pageNum = pageNum + 1; } if($("#recommendTotalPages").val() != "" &&
 * pageNum > $("#recommendTotalPages").val()){ return false; } }
 */
  data = FundRecommend.getSelectConditions();
  // data["pageNum"] = pageNum;
  data["pageSize"] = 10;
  data["firstIn"] = $("#firstIn").val();
/*
 * recommendReq = $.ajax({ url: "/prjweb/fund/ajaxrecommend", type: "post", data: data, dataType:
 * "html", beforeSend:function(){ var content =
 * document.getElementById("div_preloader_re").innerHTML; if(content == null || content == ""){
 * $("#div_preloader_re").doLoadStateIco({ style:"height:28px; width:28px;
 * margin:auto;margin-top:24px;", status:1 }); } if(!needAppend){ $("#recommend_list_div").html(""); }
 * $("#div_preloader_re").show(); }, success: function(data){ $("#div_preloader_re").hide();
 * FundRecommend.ajaxTimeOut(data, function(){ if($("#div_norecord").length>0 ||
 * $("#div_noMoreRecord").length>0){ $("#div_norecord").remove(); } if(needAppend){
 * $("#recommend_list_div").append(data); $("#recommendPageNo").remove();
 * $("#recommendPageSize").remove(); $("#recommendTotalPages").remove();
 * $("#recommendTotalCount").remove(); }else{ $("#recommend_list_div").html(data); }
 * if($("#fundsUnlimit").val()=="true"){// 如果是按不限选出的额结果，选中不限
 * $(".item_list-align_item[code='0']").addClass("dev_slected");
 * $(".dev_seniority").text($(".dev_seniority_unlimit").text());
 * $(".dev_seniority").attr("code","0"); $(".dev_seniority").attr("select_sen","true"); }
 * $("#fundsUnlimit").remove(); // if(pageNum == 1){ FundRecommend.RecommendFundlistScroll(); // }
 * FundRecommend.dealRecommendations(); $("#recommend_list_div").show();
 * $("#recommend_list_container").show(); $("#recommend_list_footer").show(); // 加载logo
 * FundRecommend.ajaxFundLogos(); }); }, error: function(){} });
 */
  
  fundList = window.Mainlist({
    name:"recommendFundList",
    listurl: "/prjweb/fund/ajaxrecommend",
    listdata: data,
    listcallback: function(){
      // 显示推荐度
      FundRecommend.dealRecommendations();
      // 加载logo
      FundRecommend.ajaxFundLogos();
    },
    statsurl:"/prjweb/fundAgency/ajaxfundcount",
    method: "pagination",
  })

  FundRecommend.changeUrl('recommend');
}

FundRecommend.ajaxFundLogos = function(){
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
// 是否是空值
FundRecommend.isNullVal = function(val){
  if(val == null || val == "" || typeof(val) == "undefined"){
    return true;
  }else{
    return false;
  }
}



/**
 * ajax加载已收藏基金列表 needAppend: 是否需要追加内容， 区分重新加载页面还是分页获取
 */

FundRecommend.showCollectedFundList = function(needAppend){
  if(recommendReq != null){
    recommendReq.abort();
  }
  // 隐藏基金推荐
  $("#recommend_list_div").html("");
  $("#recommend_list_container").hide();
  $("#conditions_div").hide();
  // 隐藏基金发现
  $("#fund_find_list_div").html("");
  $("#fund_find_list_container").hide();
  // 隐藏资助机构
  $("#agency_list_div").html("");
  $("#agency_list_container").hide();
  $(window).off("scroll");
  var pageNum = parseInt($("#collectedPageNo").val());
  if(isNaN(pageNum) || !needAppend){
    pageNum = 1;
  }else{
    pageNum = pageNum + 1;
  }
  if(pageNum > $("#collectedTotalPages").val() && pageNum > 1){
    return false;
  }
  collectReq = $.ajax({
    url: "/prjweb/collectedfund/ajaxlist",
    type: "post",
    data: {
      "page.pageNo": pageNum,
      "page.pageSize": $("#collectedPageSize").val()
    },
    dataType: "html",
    beforeSend:function(){
      var content = document.getElementById("div_preloader").innerHTML;
      if(content == null || content == ""){
        $("#div_preloader").doLoadStateIco({
          style:"height:28px; width:28px; margin:auto;margin-top:24px;",
          status:1
        });
      }
      $("#div_preloader").show();
    },
    success: function(data){
      $("#div_preloader").hide();
      FundRecommend.ajaxTimeOut(data, function(){
       /*
         * document.getElementsByClassName("nav__underline")[0].style.width = 95 + "px";
         * document.getElementsByClassName("nav__underline")[0].style.left = 100 + "px";
         * document.getElementsByClassName("nav__underline")[0].style.top= 38 + "px";
         */
        if(!needAppend){
          $("#collectioned_list_div").html(data);
        }else{
          $("#collectedPageNo").remove();
          $("#collectedPageSize").remove();
          $("#collectedTotalPages").remove();
          $("#totalCount").remove();
          $("#collectioned_list_div").append(data);
        }
        $("#collectioned_list_div").show();
          $("#collectioned_list_container").show();
          $("#collectioned_list_footer").show();
        if(pageNum == 1 || pageNum <= $("#collectedTotalPages").val()){
          FundRecommend.CollectedFundlistScroll();
        }
      });
    },
    error: function(){}
  });
  FundRecommend.changeUrl('collected');
}


// 赞、取消赞操作
FundRecommend.awardOperation = function(obj, id, type, fundId, pageType){
  FundRecommend.doHitMore($(obj), 2000);
  $.ajax({
    url: "/prjweb/fund/ajaxaward",
    type: "post",
    data: {
      "encryptedFundId" : id,
      "awardOperation": type,
      "fundId":fundId
    },
    dataType: "json",
    success: function(data){
      FundRecommend.ajaxTimeOut(data, function(){
        if(data.result == "success"){
          var awardSel = "#"+pageType+"Award_"+fundId;
          var calcelSel = "#"+pageType+"AwardCancel_"+fundId;
          var count = data.awardCount;
          if(count>0 && count<1000){
             count = "("+count+")";  
          }else if(count>=1000){
            count = "(1k+)";  
          }
                    
          if(type == 0){
            if (data.awardCount >0) {
              $(calcelSel).find("span.awardOperateCount").html(count);
            }else{
              $(calcelSel).find("span.awardOperateCount").text("");
            }
            $(awardSel).hide();
            $(calcelSel).show();
          }else{
            if(data.awardCount>0){
              $(awardSel).find("span.awardOperateCount").html(count);
              }else{
              $(awardSel).find("span.awardOperateCount").text("");
              }
            $(calcelSel).hide();
            $(awardSel).show();
          }
        }else{
          // TODO 提示
        }
      });
    },
    error: function(){
      // TODO 提示
    }
  });
}



// 收藏、取消收藏操作
FundRecommend.collectCoperation = function(obj, id, type, fundId){
  FundRecommend.doHitMore($(obj), 2000);
  $.ajax({
    url: "/prjweb/fund/ajaxcollect",
    type: "post",
    data: {
      "encryptedFundId" : id,
      "collectOperate": type
    },
    dataType: "json",
    success: function(data){
      FundRecommend.ajaxTimeOut(data, function(){
        if(data.result == "success"){
          if(type == 0){
            $("#collect_"+fundId).hide();
            $("#collectCancel_"+fundId).show();
          }else{
            $("#collectCancel_"+fundId).hide(); // 我的基金页面
            $("#collect_"+fundId).show(); 
            $("#"+fundId).hide();
            if($(".collect_fund_info:visible").length == 0){
              $("#no_result_div").show();
            }else{
              $("#collectioned_list_div").html("");
              FundRecommend.showCollectedFundList(false);
            }
          }
        }else{
          // TODO 提示
        }
      });
    },
    error: function(){
      // TODO 提示
    }
  });
}

// 动态中的收藏、取消收藏操作
FundRecommend.dynCollectCoperation = function(obj, id, type, fundId){
  FundRecommend.doHitMore($(obj), 2000);
  $.ajax({
    url: "/prjweb/fund/ajaxcollect",
    type: "post",
    data: {
      "encryptedFundId" : id,
      "collectOperate": type
    },
    dataType: "json",
    success: function(data){
      FundRecommend.ajaxTimeOut(data, function(){
        if(data.result == "success"){
          if(type == 0){
            $(".collect_"+fundId).hide();
            $(".collectCancel_"+fundId).show();
          }else{
            $(".collect_"+fundId).show();
            $(".collectCancel_"+fundId).hide();
          }
        }else{
          // TODO 提示
        }
      });
    },
    error: function(){
      // TODO 提示
    }
  });
}


// 绑定滚动加载收藏的基金
FundRecommend.CollectedFundlistScroll = function(){
  $(window).scroll(function(){
    　　var scrollTop = $(this).scrollTop();
    　　var scrollHeight = $(document).height();
    　　var windowHeight = $(this).height();
    　　if(scrollTop + windowHeight == scrollHeight){
    　　　　FundRecommend.showCollectedFundList(true);
    　　}
  });
}


// 绑定滚动加载推荐的基金
FundRecommend.RecommendFundlistScroll = function(){
  $(window).scroll(function(){
    　　var scrollTop = $(this).scrollTop();
    　　var scrollHeight = $(document).height();
    　　var windowHeight = $(this).height();
    　　if(scrollTop + windowHeight == scrollHeight){
    　　　　FundRecommend.loadRecommendFundList(true);
    　　}
  });
}

// 显示推荐条件弹出框
FundRecommend.showConditionsBox = function(){
  showDialog("fundConditionsBox");
}

// 显示科技领域
FundRecommend.showScienceAreaBox = function(obj){
  FundRecommend.doHitMore($(obj), 3000);
  $.ajax({
    url: "/prjweb/fundscience/ajaxbox",
    type: "post",
    data: {
      
    },
    dataType: "html",
    success: function(data){
      $("#scienceArea_Box").html(data);
      hideDialog("fundConditionsBox");
      showDialog("scienceArea_Box");
    },
    error: function(){
      
    }
  });
    
}


// 初始化基金操作
FundRecommend.initFundOperation = function(desFundId, fundId){
  $.ajax({
    url: "/prjweb/fundop/ajaxinit",
    type: "post",
    data: {
      "des3FundId": desFundId
    },
    dataType: "json",
    success: function(data){
      FundRecommend.ajaxTimeOut(data, function(){
        if(data.result == "success"){
          if(data.awardCount>0){
            var awardCount = data.awardCount > 999 ? '1k+' : data.awardCount;
            $(".awardOperateCount").html("("+awardCount+")");                        
          }else{
            $(".awardOperateCount").text("");
          }
          if(data.shareCount>0){
            var shareCount = data.shareCount > 999 ? '1k+' : data.shareCount;
            $(".shareCount_"+fundId).html("("+shareCount+")");     
          }else{
            $(".shareCount_"+fundId).text("");
          }
          if(data.hasAward){
            $("#detailAwardCancel_"+fundId).show();
            $("#detailAward_"+fundId).hide();
          }
          if(data.hasCollect){
            $("#collectCancel_"+fundId).show();
            $("#collect_"+fundId).hide();
          }
          FundRecommend.addFundReadCount(desFundId,fundId);//记录基金阅读数
        }
      });
    },
    error: function(){
      FundRecommend.addFundReadCount(desFundId,fundId);
    }
  });
}
FundRecommend.addFundReadCount = function(desFundId,fundId){
  $.ajax({
    url: "/prjweb/fundop/ajaxinitReads",
    type: "post",
    data: {
      "des3FundId": desFundId
    },
    dataType: "json",
    success : function(data) {
      FundRecommend.ajaxTimeOut(data, function(){
        if(data.result == "success"){
      if(data.readCount>0){
        var readCount = data.readCount > 999 ? '1k+' : data.readCount;
        $(".readCount_"+fundId).html(readCount);     
      }else{
        $(".readCount_"+fundId).text("");
      }
      }
    });
    },
    error : function() {
    }
  });
}
// 处理推荐度
FundRecommend.dealRecommendations = function(){
  var totalCount = parseInt($(".js_listinfo").attr("smate_count"));
  var twentyPersent = 0;
  var thirtyPersent = 0;
  var other = 0;
  twentyPersent = totalCount * 0.2 < 1 ? 1 : (Math.floor(totalCount * 0.2 / 1));
  if(twentyPersent < totalCount){
    thirtyPersent = totalCount * 0.3 < 1 ? 1 : (Math.floor(totalCount * 0.3 / 1));
    if(twentyPersent + thirtyPersent < totalCount){
      other = totalCount - thirtyPersent - twentyPersent;
    }
  }
  var currentPageNum=parseInt($(".js_listinfo").attr("smate_page_num"))||0;
  var hasDealCount = (currentPageNum-1)*10;
  var i = 1;
  $(".needDeal").each(function(){
    $this = $(this);
    if(hasDealCount + i <= twentyPersent){
      $this.addClass("relevance_icon01");
    }else if(thirtyPersent > 0 && hasDealCount + i <= twentyPersent + thirtyPersent){
      $this.addClass("relevance_icon02");
    }else{
      $this.addClass("relevance_icon03");
    }
    $this.removeClass("needDeal");
    $this.addClass("hasDeal");
    i++;
  });
  
} 





// 超时处理
FundRecommend.ajaxTimeOut = function(data,myfunction){
   var toConfirm=false;
    
    if('{"ajaxSessionTimeOut":"yes"}'==data){
      toConfirm = true;
    }
    if(!toConfirm&&data!=null){
      toConfirm=data.ajaxSessionTimeOut;
    }
    if(toConfirm){
      jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
        if (r) {
          var url = window.location.href;
          if(url.indexOf("/prjweb/outside/showfund")>=0){
            // 站外的需要设置站内地址才会跳转到登录页面
            url="/oauth/index?service="+encodeURIComponent(url);
          }
          window.location.href = url;
          return 0;
        }
      });
      
    }else{
      if(typeof myfunction == "function"){
        myfunction();
      }
    }
};

FundRecommend.doHitMore = function(obj,time){
  $(obj).attr("disabled",true);
  var click = $(obj).attr("onclick");
  if(click!=null&&click!=""){
    $(obj).attr("onclick","");
  }
  setTimeout(function(){
    $(obj).removeAttr("disabled");
    if(click!=null&&click!=""){
      $(obj).attr("onclick",click);
    }
  },time);
};

FundRecommend.changeUrl = function(targetModule) {
  var json = {};
  var oldUrl = window.location.href;
  var reg=eval('/([&\?]module=)([^&]*)/gi');
  var newUrl = window.location.href;
  if (targetModule != undefined && targetModule != "") {
    var index = oldUrl.match(reg); 
    if (index == null) {
      if(oldUrl.lastIndexOf("?")>0){
        newUrl = oldUrl + "&module=" + targetModule;
      }else{
        newUrl = oldUrl + "?module=" + targetModule;
      }
    } else {
      if(oldUrl.lastIndexOf("?module")>0){
        newUrl = oldUrl.replace(reg,'?module='+targetModule); 
      }else{
        newUrl = oldUrl.replace(reg,'&module='+targetModule); 
      }
    }
  }
  window.history.replaceState(json, "", newUrl);
};
FundRecommend.deleteFundAgency = function(obj){
  var agencyCode = $(obj).prev(".dev_agencyName").attr("code");
  $.ajax({
    url:"/prjweb/fundconditions/ajaxdeleteagencyInterest",
    type:"post",
    data:{"agencyId":agencyCode},
    dataType:"json",
    success: function(data){
      if(data.result="success"){
        $(obj).parent("li").remove();
        scmpublictoast(fundRecommend.deleteSuccess,2000);
      }else{
        scmpublictoast(fundRecommend.deleteFail,2000);
      }
    },
    error: function(data){
      scmpublictoast(fundRecommend.deleteFail,2000);
    }
  });
};
FundRecommend.deleteFundArea = function(obj){
  var areaCode = $(obj).prev(".dev_scienceArea").attr("code");
  $.ajax({
    url:"/prjweb/fundconditions/ajaxdeletefundarea",
    type:"post",
    data:{"areaCode":areaCode},
    dataType:"json",
    success: function(data){
      if(data.result="success"){
        $(obj).parent("li").remove();
        scmpublictoast(fundRecommend.deleteSuccess,2000);
      }else{
        scmpublictoast(fundRecommend.deleteFail,2000);
      }
    },
    error: function(data){
      scmpublictoast(fundRecommend.deleteFail,2000);      
    }
  });
};
var nowPageNum = 1;
FundRecommend.funPagingConditions = function(){
  FundRecommend.initShowPage(0,5);
  $(".setting-list_page-up").click(function(){
    if(!$(this).hasClass("no_page-for_selected")){
      FundRecommend.showPrePage(nowPageNum,5);
      --nowPageNum;
      FundRecommend.showIsClickPage(nowPageNum,5);
    }
  });
  $(".setting-list_page-down").click(function(){
    if(!$(this).hasClass("no_page-for_selected")){
      FundRecommend.showNextPage(nowPageNum,5);
      ++nowPageNum;
      FundRecommend.showIsClickPage(nowPageNum,5);
    }
  });
};
FundRecommend.initShowPage = function(nowPageNum,pageSize){
  FundRecommend.showNextPage(nowPageNum,5);
  ++nowPageNum;
  FundRecommend.showIsClickPage(nowPageNum,5);
};
FundRecommend.showIsClickPage = function(nowPageNum,pageSize){
  var $allItem = $(".dev_agency_ul").find(".setting-list_page-item");
  if(nowPageNum-1<=0){// 没有上一页
    $(".setting-list_page-up").addClass("no_page-for_selected");
  }else{
    $(".setting-list_page-up").removeClass("no_page-for_selected");
  }
  var allPageNum = Math.ceil($allItem.length/pageSize);
  if(nowPageNum+1>allPageNum){// 没有下一页
    $(".setting-list_page-down").addClass("no_page-for_selected");
  }else{
    $(".setting-list_page-down").removeClass("no_page-for_selected");
  }
};
FundRecommend.showNextPage = function(nowPageNum,pageSize){
  var nextPageNum = nowPageNum+1
    var $allItem = $(".dev_agency_ul").find(".setting-list_page-item");
  var allPageNum = Math.ceil($allItem.length/pageSize);
  if(nextPageNum>=0 && pageSize>0 && nextPageNum<=allPageNum){
    $allItem.addClass("setting-list_page-item_hidden");// 先全部隐藏
    $allItem.each(function(i){
      if(Math.ceil((i+1)/pageSize)==nextPageNum){
        $(this).removeClass("setting-list_page-item_hidden");     
      }
    }); 
  }
};
FundRecommend.showPrePage = function(nowPageNum,pageSize){
  var upPageNum = nowPageNum-1
    var $allItem = $(".dev_agency_ul").find(".setting-list_page-item");
  if(upPageNum>0 && pageSize>0){
    $allItem.addClass("setting-list_page-item_hidden");// 先全部隐藏
    $allItem.each(function(i){
      if(Math.ceil((i+1)/pageSize)==upPageNum){
        $(this).removeClass("setting-list_page-item_hidden");     
      }
    }); 
  }
};

// 基金发现初始化页面
FundRecommend.initFundFind = function(){
  var heightlist = $(".new-mainpage_scroll-height");
  for(var i = 0; i < heightlist.length; i++){
      heightlist[i].style.height = window.innerHeight - 135 + "px";
      heightlist[i].style.minHeight = window.innerHeight - 135 + "px";
  }
  
  $(".new-mainpage_left-list_header-onkey").each(function(){
    $(this).bind("click",function(){
      if(this.innerHTML == "expand_less"){
        this.innerHTML = "expand_more";
        if(this.closest(".new-mainpage_left-list").querySelector(".new-mainpage_left-list_body")){
            this.closest(".new-mainpage_left-list").querySelector(".new-mainpage_left-list_body").style.display = "none";
        }
    }else{
        this.innerHTML = "expand_less";
        if(this.closest(".new-mainpage_left-list").querySelector(".new-mainpage_left-list_body")){
            this.closest(".new-mainpage_left-list").querySelector(".new-mainpage_left-list_body").style.display = "block";
        }
    }
    });
});
  
  document.getElementsByClassName("new-mainpage_container")[0].style.left = (window.innerWidth- 1200)/2 + "px";
  window.onresize = function(){
      if((window.innerWidth- 1200)>0){
          document.getElementsByClassName("new-mainpage_container")[0].style.left = (window.innerWidth- 1200)/2 + "px";
      }else{
          document.getElementsByClassName("new-mainpage_container")[0].style.left = 0 + "px";
      }
  }
  
  /*
   * $(".new-mainpage_left-sublist_header-onkey").each(function(){ $(this).bind("click",function(){
   * if($(this).hasClass("new-mainpage_left-sublist_header-open")){
   * $(this).removeClass("new-mainpage_left-sublist_header-open");
   * this.closest(".new-mainpage_left-sublist").querySelector(".new-mainpage_left-sublist_body").style.display =
   * "none"; }else{ $(this).addClass("new-mainpage_left-sublist_header-open");
   * this.closest(".new-mainpage_left-sublist").querySelector(".new-mainpage_left-sublist_body").style.display =
   * "block"; } }) })
   */
  
  /*
   * $(".new-mainpage_left-sublist_body-item").each(function(){ $(this).bind("click",function(){
   * $(this).toggleClass("new-mainpage_left-sublist_body-item_selected");
   * FundFind.loadFundFindList(false); }) })
   */
  // 搜索框绑定回车检索事件
  $("#searchKey").keypress(function (e) {
    if (e.which == 13) {
      FundFind.loadFundFindList(false);
    }
});
  // 加载地区分页
  FundFind.funPagingConditions();
  FundFind.loadFundFindList(false);
}

// 资助机构初始化页面
FundRecommend.initAgencyPage = function(){
  fundAgencyList();
  var closelist = document.getElementsByClassName("filter-section__header");
  for(var i = 0; i < closelist.length; i++){
    closelist[i].onclick = function(){
      if(this.querySelector(".filter-section__toggle").innerHTML=="expand_less"){
        this.querySelector(".filter-section__toggle").innerHTML="expand_more";
        this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="none";
      }else{
      
        this.querySelector(".filter-section__toggle").innerHTML="expand_less";
              this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="block";
      }
    }
  }
  var totalheight =document.documentElement.clientHeight || document.body.clientHeight;
  var totalheight =document.documentElement.clientHeight;
  if(document.getElementsByClassName("main-list").length > 0){
      document.getElementsByClassName("main-list")[0].style.height = totalheight - 110 + "px";   
  }
  document.getElementById("filter-list__section").style.height = totalheight - 150 + "px";
  var setheight = window.innerHeight - 180 - 95;
  var contentlist = document.getElementsByClassName("content-details_container");
  for(var i = 0; i < contentlist.length; i++){
      contentlist[i].style.minHeight = setheight + 60 + "px";
  }
  document.getElementsByClassName("nav__underline")[0].style.width =  95 + "px";
  document.getElementsByClassName("nav__underline")[0].style.left =  100 + "px";
  document.getElementsByClassName("nav__underline")[0].style.top= 38 + "px";
}

// 基金翻页
/*
 * FundRecommend.funPagingConditions = function(){
 * if((document.getElementsByClassName("setting-list_page-item").length>0)&&(document.getElementsByClassName("setting-list_page-btn").length>0)){
 * var arrylist = document.getElementsByClassName("setting-list_page-item"); // 参与翻页的总项目个数 var upbtn =
 * document.getElementsByClassName("setting-list_page-up")[0]; // 向上翻 var downbtn =
 * document.getElementsByClassName("setting-list_page-down")[0]; // 向下翻 if(arrylist.length>5){ var
 * cont = Math.round((arrylist.length)/5); // 按照每页显示5个项目 可以分的总页数 var pagecont = 0; // 记录点击翻页的次数
 * for(var i = 0; i < arrylist.length;i++){ // 第一步 a.将全部的要显示的项目隐藏起来
 * arrylist[i].classList.add("setting-list_page-item_hidden"); }; for(var i = 0; i < 5;i++){ // 第一步
 * b.初始化显示五个项目 if(arrylist[i].classList.contains("setting-list_page-item_hidden")){
 * arrylist[i].classList.remove("setting-list_page-item_hidden") } } downbtn.onclick = function(){
 * pagecont++; // 点击向下翻一页 该数量加1 if(pagecont == (cont - 1)){
 * 
 * for(var i = 0; i < arrylist.length;i++){
 * arrylist[i].classList.add("setting-list_page-item_hidden"); }; for(var i = pagecont*5; i <
 * arrylist.length; i++){ arrylist[i].classList.remove("setting-list_page-item_hidden"); }
 * 
 * if(!downbtn.classList.contains("setting-list_page-none")){
 * downbtn.classList.add("setting-list_page-none"); }
 * if(upbtn.classList.contains("setting-list_page-none")){
 * upbtn.classList.remove("setting-list_page-none") }
 * 
 * }else{
 * 
 * for(var i = 0; i < arrylist.length;i++){
 * arrylist[i].classList.add("setting-list_page-item_hidden"); }; for(var i = (pagecont)*5; i <
 * ((pagecont+1)*5); i++){ arrylist[i].classList.remove("setting-list_page-item_hidden"); }
 * 
 * if(upbtn.classList.contains("setting-list_page-none")){
 * upbtn.classList.remove("setting-list_page-none"); }
 * if(downbtn.classList.contains("setting-list_page-none")){
 * downbtn.classList.remove("setting-list_page-none") } } } upbtn.onclick = function(event){
 * if(pagecont>0){ pagecont--; // 向上翻一页 该数量减一 } if(pagecont == 0){ for(var i = 0; i <
 * arrylist.length;i++){ arrylist[i].classList.add("setting-list_page-item_hidden"); }; for(var i =
 * 0; i < 5;i++){ if(arrylist[i].classList.contains("setting-list_page-item_hidden")){
 * arrylist[i].classList.remove("setting-list_page-item_hidden"); } }
 * if(!upbtn.classList.contains("setting-list_page-none")){
 * upbtn.classList.add("setting-list_page-none"); }
 * if(downbtn.classList.contains("setting-list_page-none")){
 * downbtn.classList.remove("setting-list_page-none") } }else{ for(var i = 0; i <
 * arrylist.length;i++){ arrylist[i].classList.add("setting-list_page-item_hidden"); }; for(var i =
 * (pagecont)*5; i < ((pagecont+1)*5); i++){
 * arrylist[i].classList.remove("setting-list_page-item_hidden"); }
 * if(upbtn.classList.contains("setting-list_page-none")){
 * upbtn.classList.remove("setting-list_page-none"); }
 * if(downbtn.classList.contains("setting-list_page-none")){
 * downbtn.classList.remove("setting-list_page-none") } } } }else{ var btnlist =
 * document.getElementsByClassName("setting-list_page-btn");
 * Array.from(btnlist.length).forEach(function(x){
 * if(!x.classList.contains("setting-list_page-none")){ x.classList.add("setting-list_page-none") } }) } } }
 */
