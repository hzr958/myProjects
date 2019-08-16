var MainDynBase = MainDynBase || {};

// 动态列表
MainDynBase.ajaxLoadDyn = function(locale) {
  $("#load_preloader").show();
  var pageSize = parseInt($("#pageSize").val());
  var pageNumber = parseInt($("#pageNumber").val()) + 1;
  var post_data = {
    "pageNumber" : pageNumber,
    "pageSize" : pageSize,
    "locale" : locale
  };

  $.ajax({
    url : '/dynweb/dynamic/ajaxshow',
    type : 'post',
    dataType : 'html',
    data : post_data,
    success : function(data) {
      $("#load_preloader").hide();
      if (data.indexOf("关注更多专家获得更多动态") != -1) {
        data = data.replace("margin-top: 6px", "margin-top: 20px; font-size: 14px;");
        data = data.replace("class=\"bottom_tip mt20\"", "");
        data = data.replace("/pubweb/mobile/search/main", "/scmwebsns/friend/know?menuId=10007");
      }
      $(".container__horiz_left").append(data);
      $("#pageNumber").val(pageNumber);
      var number = parseInt($(".dynamic").length);
      if (data.indexOf("关注更多专家获得更多动态") == -1) {
        if ((number % pageSize == 0 && number != 0)) {
          $("#moreDynamic_div").show();
          $("#moreExperts").hide();
          $("#bottom_tip_c").hide();
        } else {
          $("#moreDynamic_div").hide();
          $("#bottom_tip_c").show();
        }
      } else {
        $("#moreDynamic_div").hide();
        $("#bottom_tip_c").show();
      }
      if (number == 0) {
        $("#moreDynamic_div").hide();
        $("#bottom_tip_c").hide();
      }
      $("#load_preloader").hide();
    },
    error : function() {
      $("#load_preloader").hide();
      return false;
    }
  })

};
