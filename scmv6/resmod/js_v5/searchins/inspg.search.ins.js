var SearchIns = SearchIns ? SearchIns : {};
var $insList;

//检索机构列表新的
SearchIns.ajaxSearchNewIns = function(data) {
  $("#searchListComplete").val("0");
  $("#inscontent").doLoadStateIco({
    style : "height:28px; width:28px;margin-top:24px;",
    status : 1
  });
  $.ajax({
    url : "/prjweb/outside/agency/ajaxshownewins",
    type : "post",
    data : data,
    dataType : "html",
    success : function(data) {
      $("#searchListComplete").val("1");
      $("#inscontent").html("");
      $("#inscontent").html(data);
      //SearchIns.initMenuClick();
    },
    error : function() {
      $("#searchListComplete").val("1");
      //SearchIns.initMenuClick();
    }
  });
}

// 检索机构列表
SearchIns.ajaxSearchIns = function(data) {
  $("#searchListComplete").val("0");
  $("#right_list").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:150px;",
    status : 1
  });
  $.ajax({
    url : "/prjweb/outside/agency/ajaxshowins",
    type : "post",
    data : data,
    dataType : "html",
    success : function(data) {
      $("#right_list").html(data);
      $("#searchListComplete").val("1");
      SearchIns.initMenuClick();
    },
    error : function() {
      $("#searchListComplete").val("1");
      SearchIns.initMenuClick();
    }
  });
}

// 初始化样式
SearchIns.initPageStyle = function() {
  var cliclist = document.getElementsByClassName("mechanism-checked__tip");
  var overlist = document.getElementsByClassName("mechanism-left__content-list__title");
  var onlist = document.getElementsByClassName("mechanism-left__search")[0];
  for (var i = 0; i < cliclist.length; i++) {
    cliclist[i].onclick = function() {
      if (this.innerHTML == "keyboard_arrow_up") {
        this.innerHTML = "keyboard_arrow_down";
        this.closest(".mechanism-left__content-item").querySelector(".mechanism-left__content-box").style.display = "none";
      } else {
        this.innerHTML = "keyboard_arrow_up";
        this.closest(".mechanism-left__content-item").querySelector(".mechanism-left__content-box").style.display = "block";
      }
    }
  }
  for (var i = 0; i < overlist.length; i++) {
    overlist[i].onmouseover = function() {
      this.title = this.innerHTML;
    }
  }

  onlist.onfocus = function() {
    this.style.borderColor = "#288aed";
  };

  onlist.onblur = function() {
    this.style.borderColor = "#ddd";
  };
}

// 初始化检索插件
SearchIns.initInsSearch = function() {
  // 成果列表
  $insList = window.Mainlist({
    name : "insSearch",
    listurl : "/prjweb/outside/agency/ajaxshowins",
    listdata : {},
    listcallback : function(xhr) {
    },
    statsurl : "/prjweb/outside/agency/ajaxsearchcallback"
  });
};

// 刷新地区菜单
SearchIns.refreshRegionFilter = function() {
  $("#regionMenuComplete").val("0");
  $("#region_load").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;margin-bottom:24px;",
    status : 1
  });
  // 构造传的参数
  var postData = {
    "createMenu" : 1,
    "searchString" : $.trim($("#search_some_one").val()),
    "insRegion" : $("#selected_ins_region").val(),
    "insCharacter" : $("#selected_ins_Character").val()
  }
  $.ajax({
    url : "/prjweb/outside/agency/ajaxregionmenu",
    type : "post",
    data : postData,
    dataType : "html",
    success : function(data) {
      if (data != null && data != "") {
        $("#insRegions").html(data);
      }
      $("#regionMenuComplete").val("1");
      SearchIns.initMenuClick();
      $("#region_load").html("");
    },
    error : function() {
      $("#regionMenuComplete").val("1");
      SearchIns.initMenuClick();
      $("#region_load").html("");
    }
  });
}

// 刷新类别菜单
SearchIns.refreshCharacterFilter = function() {
  $("#characterMenuComplete").val("0");
  $("#character_load").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;margin-bottom:24px;",
    status : 1
  });
  // 构造传的参数
  var postData = {
    "createMenu" : 1,
    "searchString" : $.trim($("#search_some_one").val()),
    "insRegion" : $("#selected_ins_region").val(),
    "insCharacter" : $("#selected_ins_Character").val()
  }
  $.ajax({
    url : "/prjweb/outside/agency/ajaxcharactermenu",
    type : "post",
    data : postData,
    dataType : "html",
    success : function(data) {
      if (data != null && data != "") {
        $("#insCharacters").html(data);
      }
      $("#characterMenuComplete").val("1");
      SearchIns.initMenuClick();
      $("#character_load").html("");
    },
    error : function() {
      $("#characterMenuComplete").val("1");
      SearchIns.initMenuClick();
      $("#character_load").html("");
    }
  });
}

// 按机构类别检索
SearchIns.searchByCharacter = function(insCharacter, obj) {
  if (insCharacter != "") {
    $(obj).addClass("selectedCharacterItem");
  } else {
    $(obj).removeClass("selectedCharacterItem");
  }
  $(obj).find("i[name='insCharacter']").css("visibility", "visible").html("&#xe5cd;");
  $(obj).find("i[name='insCharacter']").css("display", "block").html("&#xe5cd;");
  // $(obj).attr("onclick","SearchIns.searchByCharacter('', this, true)");
  $(obj).find(".menuList_sc").hide();
  $(obj).find("em").html("");
  $(obj).siblings().hide();
  // TODO 多次点击
  $("#selected_ins_Character").val(insCharacter);
  var searchString = $.trim($("#search_some_one").val());
  var orderBy = $("#orderBy").val();
  var insRegion = $("#selected_ins_region").val();
  var pageSize = $("#pageSize").val();
  if (pageSize == null || typeof (pageSize) == "undefined") {
    pageSize = 10;
  }
  var data = {
    "searchString" : searchString,
    "insCharacter" : insCharacter,
    "insRegion" : insRegion,
    "orderBy" : orderBy,
    "page.pageSize" : pageSize
  };
  SearchIns.ajaxSearchIns(data);
  if ($("#selected_ins_region").val() == null || $("#selected_ins_region").val() == "") {
    SearchIns.refreshRegionFilter();
  }

  if ($("#selected_ins_Character").val() == null || $("#selected_ins_Character").val() == "") {
    SearchIns.refreshCharacterFilter();
  }
}

// 按机构地区检索
SearchIns.searchByRegion = function(insRegion, obj, refreshMenu) {
  if (insRegion != "") {
    $(obj).addClass("selectedRegionItem");
  } else {
    $(obj).removeClass("selectedRegionItem");
  }
  if (!refreshMenu) {
    $(obj).find("i[name='insRegion']").css("visibility", "visible").html("&#xe5cd;");
    $(obj).find("i[name='insRegion']").css("display", "block").html("&#xe5cd;");
    // $(obj).attr("onclick","SearchIns.searchByRegion('', this, true)");
    $(obj).find(".menuList_sc").hide();
    $(obj).find("em").html("");
    // 隐藏兄弟节点
    $(obj).siblings().hide();
  }
  // TODO 多次点击
  $("#selected_ins_region").val(insRegion);
  var searchString = $.trim($("#search_some_one").val());
  var insCharacter = $("#selected_ins_Character").val();
  var orderBy = $("#orderBy").val();
  var pageSize = $("#pageSize").val();
  if (pageSize == null || typeof (pageSize) == "undefined") {
    pageSize = 10;
  }
  var data = {
    "searchString" : searchString,
    "insCharacter" : insCharacter,
    "insRegion" : insRegion,
    "orderBy" : orderBy,
    "page.pageSize" : pageSize
  };
  SearchIns.ajaxSearchIns(data);
  if ($("#selected_ins_Character").val() == null || $("#selected_ins_Character").val() == "") {
    SearchIns.refreshCharacterFilter();
  }
  if (refreshMenu) {
    SearchIns.refreshRegionFilter();
  }
}

/**
 * 赞、取消赞操作
 */
SearchIns.awardInsOpt = function(desInsId, status, insId) {
  // TODO 多次点击
  $.ajax({
    url : "/prjweb/agency/award/ajaxopt",
    type : "post",
    data : {
      "desInsId" : desInsId,
      "status" : status
    },
    dataType : "json",
    success : function(data) {
      SearchIns.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (status == 1) {
            $("#cancleAward_" + insId).show();
            $("#award_" + insId).hide();
          } else {
            $("#cancleAward_" + insId).hide();
            $("#award_" + insId).show();
          }
          if (data.likeSum != null) {
            $(".likeSum_" + insId).html("(" + data.likeSum + ")");
            if (data.likeSum <= 0) {
              $(".likeSum_" + insId).hide();
            } else {
              $(".likeSum_" + insId).show();
            }
          }
        } else {
          scmpublictoast("网络繁忙", 1000);
        }
      });
    },
    error : function() {
      scmpublictoast("网络繁忙", 1000);
    }
  });
}

// 初始化赞状态
SearchIns.initAwardStatus = function() {
  var insIds = "";
  $(".initInsIds").each(function() {
    insIds += $(this).val() + ",";
  });
  insIds = insIds.substring(0, insIds.length - 1);
  $.ajax({
    url : "/prjweb/outside/agency/ajaxinitaward",
    type : "post",
    data : {
      "initInsIds" : insIds
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "success") {
        var awardInsId = data.insId;
        if (awardInsId != null && awardInsId != "") {
          var needDealInsId = awardInsId.split(",");
          if (needDealInsId.length > 0) {
            for (var i = 0; i < needDealInsId.length; i++) {
              $("#cancleAward_" + needDealInsId[i]).show();
              $("#award_" + needDealInsId[i]).hide();
            }
          }
        }
      }
    },
    error : function() {
      scmpublictoast("网络繁忙", 1000);
    }
  });
}

// 自己输入地区
SearchIns.inputRegionNameBySelf = function() {
  var $regionId = $("#insRegionName").attr("code");
  var showName = $("#insRegionName").val();
  if ($regionId != "") {
    var createHtml = '<div onclick="SearchIns.searchByRegion(null, this, true)"><a class="menuList-item_selected" href="javascript:;"><span>'
        + showName
        + '</span> <em class="fr"></em><i name="insRegion" class="material-icons close" style="visibility: visible;">&#xe5cd;</i></a></div>';
    SearchIns.searchByRegion($regionId, this, false);
    $("#insRegions").html(createHtml);
    $("#insRegions").attr("title", showName);
    $("#insRegionName").val("");
    $("#insRegionName").attr("code", "");
    var $acBox = document.getElementsByClassName("ac__box")[0]
    selectorEnableScroll($acBox);// 关闭提示窗口
  }
}

function selectorEnableScroll(el) {
  el.style.display = "none";
  document.body.classList.remove("js_selectornoscroll");
  const $cover = document.getElementsByClassName("background-cover cover_transparent")[0];
  if ($cover) {
    $cover.parentNode.removeChild($cover);
  }
}

// 初始化机构url
SearchIns.initInsUrl = function() {
  var insIds = "";
  $(".initInsIds").each(function() {
    insIds += $(this).val() + ",";
  });
  insIds = insIds.substring(0, insIds.length - 1);
  $.ajax({
    url : "/prjweb/outside/agency/ajaxurl",
    type : "post",
    data : {
      "initInsIds" : insIds
    },
    dataType : "json",
    success : function(data) {
      var hasLogin = $("#hasLogin").val();
      if (data.result == "success") {
        var insUrlJson = data.insUrl;
        if (insUrlJson != null && insUrlJson != "") {
          var urlJson = $.parseJSON(insUrlJson);
          for (var i = 0; i <= urlJson.length - 1; i++) {
            if (urlJson[i]["domain"]) {
              var insDomain = "http://" + urlJson[i]["domain"] + "/insweb/index";
              // if(hasLogin == 1){
              // insDomain += "/main?locale="+locale+"&viewIns=1";
              // }
              $("#item_click_" + urlJson[i]["insId"]).attr("onclick", "window.open('" + insDomain + "');");
              $("#item_href_" + urlJson[i]["insId"]).attr("href", insDomain);
              $("#item_click_" + urlJson[i]["insId"]).removeClass("noUrl");
              $("#item_click_" + urlJson[i]["insId"]).addClass("hasUrl");
              $("#item_click_" + urlJson[i]["insId"]).addClass("file-left__download");
            }
          }
        }
        $(".noUrl").find(".mechanism-right__container-item_name").addClass("no__link-download");
      }
    },
    error : function() {

    }
  });
}

// 初始化菜单点击事件
SearchIns.initMenuClick = function() {
  var characterSta = $("#characterMenuComplete").val();
  var regionSta = $("#regionMenuComplete").val();
  var listSta = $("#searchListComplete").val();
  if (characterSta == "1" && regionSta == "1" && listSta == "1") {
    $(".selectedRegionItem").attr("onclick", "SearchIns.searchByRegion('', this, true)");
    $(".selectedCharacterItem").attr("onclick", "SearchIns.searchByCharacter('', this, true)");
  }
}
//创建机构主页
SearchIns.createInsMain = function(){
  BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", function(){
    document.location.href = "/prjweb/ins/create/main";
  }, 1);
}

/**
 * 赞、取消赞操作
 */
SearchIns.awardNewInsOpt = function(insId, obj) {
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  var isAward = $(obj).attr("isAward");// 是否赞过 1 是 0否
  if (isAward == 'true') {
    isAward = 1;
  } else if (isAward == 'false') {
    isAward = 0;
  }
  var post_data = {
      "insId" : insId
    };
  if (isAward == '0') {//type 1赞2分享3关注4取消赞5取消关注
    $(obj).attr("isAward", 1);
    post_data.type = '1';// 点赞
  } else {
    $(obj).attr("isAward", 0);
    post_data.type = '4';// 取消赞
  }
  $.ajax({
    url : "/prjweb/agency/ins/ajaxoptins",
    type : "post",
    data : post_data,
    dataType : "json",
    success : function(data) {
      SearchIns.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (isAward == 0) {
            $(obj).parent().parent().find(".new-Standard_Function-bar_item:first").addClass(
            "new-Standard_Function-bar_selected");
            $(obj).find(".new-Standard_Function-bar_item-title").text(insSearch.insUnlike);
            var count = $(".likeSum_" + insId).text().replace(/[^0-9]/ig,"");
            count = count ? Number(count)+1 : 1;
            $(".likeSum_" + insId).html("(" + count + ")");
            if (count <= 0) {
              $(".likeSum_" + insId).hide();
            } else {
              $(".likeSum_" + insId).show();
            }
          } else {
            $(obj).parent().parent().find(".new-Standard_Function-bar_item:first").removeClass(
            "new-Standard_Function-bar_selected");
            $(obj).find(".new-Standard_Function-bar_item-title").text(insSearch.inslike);
            var count = $(".likeSum_" + insId).text().replace(/[^0-9]/ig,"");
            count = count ? Number(count)-1 : 0;
            $(".likeSum_" + insId).html("(" + count + ")");
            if (count <= 0) {
              $(".likeSum_" + insId).hide();
            } else {
              $(".likeSum_" + insId).show();
            }
          }
        } else {
          scmpublictoast("网络繁忙", 1000);
        }
      });
    },
    error : function() {
      scmpublictoast("网络繁忙", 1000);
    }
  });
}

SearchIns.followNewInsOpt = function(insId, obj)  {
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  var followed = $(obj).attr("followed");
  var post_data = {
      "insId" : insId
    };
  if (followed == '0') {//type 1赞2分享3关注4取消赞5取消关注
    $(obj).attr("followed", 1);
    post_data.type = '3';// 关注
  } else {
    $(obj).attr("followed", 0);
    post_data.type = '5';// 取消关注
  }
  $.ajax({
    url : "/prjweb/agency/ins/ajaxoptins",
    type : 'post',
    data : post_data,
    dateType : 'json',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data && data.result == "success") {
          if(followed && followed=="0"){
            $(obj).find('.new-Standard_Function-bar_item').addClass('new-Standard_Function-bar_selected');
            $(obj).find('.new-Standard_Function-bar_item-title:first').text(insSearch.insUnfollow);
          }else{
            $(obj).find('.new-Standard_Function-bar_item').removeClass('new-Standard_Function-bar_selected');
            $(obj).find('.new-Standard_Function-bar_item-title:first').text(insSearch.insfollow);
          }
        } else {
          scmpublictoast("网络繁忙", 1000);
        }
      });
    },
    error : function(data) {
      scmpublictoast("网络繁忙", 1000);
    }
  });
}

// scm-超时处理--
SearchIns.ajaxTimeOut = function(data, myfunction) {
  var tips = locale == "zh_CN" ? "提示" : "Reminder";
  var notLogin = locale == "zh_CN"
      ? "系统超时或未登录，你要登录吗？"
      : "You are not logged in or session has time out, please login again.";
  var toConfirm = false;
  if ('{"ajaxSessionTimeOut":"yes"}' == data) {
    toConfirm = true;
  }
  if (!toConfirm && data != null) {
    toConfirm = data.ajaxSessionTimeOut;
  }
  if (toConfirm) {
    jConfirm(notLogin, tips, function(r) {
      if (r) {
        document.location.href = "/oauth/index?service=" + encodeURIComponent(location.href);
        return;
      }
    });

  } else {
    if (typeof myfunction == "function") {
      myfunction();
    }
  }
}
