var PubRecommend = PubRecommend ? PubRecommend : {}
var recommendReq;
var collectReq;
// 推荐显示左边查询条件
PubRecommend.ajaxLeftshow = function() {
  $.ajax({
    url : "/pub/ajaxrecommendLeftshow",
    type : "post",
    dataType : "html",
    data : "",
    success : function(data) {
      $("#left_condition").html(data);
      PubRecommend.ajaxLondPubList();
    },
  });
}
// 推荐左边选择方法
PubRecommend.addCondition = function(obj, typeclass) {
  if (typeclass == "type_area") {
    /*
     * if(!$("#area_ul").find("li").hasClass(".item_list-align_slected")){ $("#searchArea").val(""); }
     */
    if ($("#area_ul >.item_list-align_slected").size() == 0) {
      $("#searchArea").val("");
    }
    if ($("#searchArea").val() == "") {
      $("#searchArea").val($(obj).attr("value"));
    } else {
      var areaList = $("#searchArea").val().split(',');
      var index = $.inArray($(obj).attr("value"), areaList);
      if (index < 0) {
        $("#searchArea").val($("#searchArea").val() + ',' + $(obj).attr("value"));
      }
    }
  }
  if (typeclass == "type_key") {
    if ($("#key_ul >.item_list-align_slected").size() == 0) {
      $("#searchPsnKey").val("");
    }
    PubRecommend.searchPsnKey = PubRecommend.addListElement($("#searchPsnKey").val(), $(obj).text().trim());
    $("#searchPsnKey").val(PubRecommend.searchPsnKey);

  }
  if (typeclass == "type_time") {
    $("." + typeclass).closest(".item_list-align").each(function() {
      $(this).removeClass("item_list-align_slected");
    });
    $("." + typeclass).each(function() {
      $(this).attr('onclick', 'PubRecommend.addCondition(this,\'' + typeclass + '\')');
    });
    $("#searchPubYear").val($(obj).attr("value"));
  }
  if (typeclass == "type_pub") {
    if ($("#searchPubType").val() == "") {
      $("#searchPubType").val($(obj).attr("value"));
    } else {
      var pubTypeList = $("#searchPubType").val().split(',');
      var index = $.inArray($(obj).attr("value"), pubTypeList);
      if (index < 0) {
        $("#searchPubType").val($("#searchPubType").val() + ',' + $(obj).attr("value"));
      }
    }
  }
  $(obj).closest(".item_list-align").addClass("item_list-align_slected");
  $(obj).attr('onclick', 'PubRecommend.delCondition(this,\'' + typeclass + '\')');
  $(obj).next().remove();
  PubRecommend.ajaxLondPubList();
}
// 推荐左边取消选择方法
PubRecommend.delCondition = function(obj, typeclass) {
  if (typeclass == "type_area") {
    /*
     * var deleteHtml = '<i class="material-icons filter-value__cancel delete_area"
     * onclick="deleteScienArea(this)" >close</i>'
     */
    var areaStr = $("#searchArea").val();
    var areaList = areaStr.split(',');
    if ($.inArray($(obj).attr("value"), areaList) >= 0) {
      areaList.splice($.inArray($(obj).attr("value"), areaList), 1);
      $("#searchArea").val(areaList.join(','));
    }
    $(obj).attr('onclick', 'PubRecommend.addCondition(this,"type_area")');
    /* $(obj).closest(".item_list-align").append(deleteHtml); */
    /* PubRecommend.addHover($(obj).closest(".item_list-align"),deleteHtml); */
  }
  if (typeclass == "type_key") {
    /*
     * var deleteHtml = '<i class="material-icons filter-value__cancel delete_area"
     * onclick="deleteKeyWord(this)">close</i>'
     */
    $('input[name="searchPsnKey"]').each(function() {
      if ($(obj).attr("value") == $(this).val()) {
        $(this).remove();
      }
    });
    PubRecommend.searchPsnKey = PubRecommend.deleteListElement($("#searchPsnKey").val(), $(obj).text().trim());
    $("#searchPsnKey").val(PubRecommend.searchPsnKey);
    /*
     * var psnKeyStr = $("#searchPsnKey").val(); var psnKeyList = psnKeyStr.split(',');
     * if($.inArray($(obj).attr("value"), psnKeyList)>=0){
     * psnKeyList.splice($.inArray($(obj).attr("value"),psnKeyList),1);
     * $("#searchPsnKey").val(psnKeyList.join(',')); }
     */
    $(obj).attr('onclick', 'PubRecommend.addCondition(this,"type_key")');
    /*
     * $(obj).closest(".item_list-align").append(deleteHtml);
     * PubRecommend.addHover($(obj).closest(".item_list-align"),deleteHtml);
     */
  }
  if (typeclass == "type_time") {
    $("#searchPubYear").val("");
    $(obj).attr('onclick', 'PubRecommend.addCondition(this,"type_time")');
  }
  if (typeclass == "type_pub") {
    var pubTypeStr = $("#searchPubType").val();
    var pubTypeList = pubTypeStr.split(',');
    if ($.inArray($(obj).attr("value"), pubTypeList) >= 0) {
      pubTypeList.splice($.inArray($(obj).attr("value"), pubTypeList), 1);
      $("#searchPubType").val(pubTypeList.join(','));
    }
    $(obj).attr('onclick', 'PubRecommend.addCondition(this,"type_pub")');
  }
  $(obj).closest(".item_list-align").removeClass("item_list-align_slected");
  /* $(obj).closest(".item_list-align").remove(".filter-value__cancel"); */
  /* $(obj).querySelector(".filter-value__cancel").style.opacity="1"; */
  PubRecommend.ajaxLondPubList();
}

// 显示全文图片和下载统计数
PubRecommend.ajaxrgetfulltext = function(fullTextPubs) {
  $.ajax({
    url : "/pubweb/mobile/ajaxrgetfulltext",
    type : "post",
    dataType : "html",
    data : {
      "fullTextPubs" : fullTextPubs
    },
    success : function(data) {
      if (data != null && typeof data !== 'undefined' && data !== '') {
        var objdata = eval(data);
        $(".dev_pub-list").find("img[name='fullImg']").each(function(i) {
          var _dowCount = $(this);
          $.each(objdata, function(n, value) {
            var pubId = value['des3PubId'];
            var fullTextImg = value['fullTextImg'];
            if (_dowCount.attr("des3PubId") == pubId && typeof (fullTextImg) != 'undefined') {
              _dowCount.attr("src", fullTextImg);
            }
          });
        });
        $(".dev_down-count").each(function(i) {
          var _dowCount = $(this);
          $.each(objdata, function(n, value) {
            var downloadCount = value['downloadCount'];
            var pubId = value['des3PubId'];
            if (_dowCount.attr("des3PubId") == pubId && typeof (downloadCount) != 'undefined' && downloadCount != 0) {
              var html = '<div>' + downloadCount + '</div><div class="">' + pubRecommend.download + '</div>'
              _dowCount.html(html);
            }
          });

        });
      }
    }
  });
}

// selectStr：查询的的字符串如searchPsnKey的值，selectName：选择的条件的class如type_area
PubRecommend.ajaxSelectConditions = function(selectStr, selectName) {
  var selectList = selectStr.split(',');
  $("." + selectName).each(function() {
    for ( var item in selectList) {
      if ($(this).attr("value") == selectList[item]) {
        $(this).addClass("slectcur");
        $(this).attr('onclick', 'PubRecommend.delCondition(this,"' + selectName + '")');
      }
    }
  });
}

// 基准库成果详情赞操作
PubRecommend.pdwhAward = function(des3PubId, dbid, obj) {
  var isAward = $(obj).attr("isAward");
  var count = Number($(obj).find('.dev_pub_award:first').text().replace(/[\D]/ig, ""));
  $.ajax({
    url : "/pubweb/details/ajaxpdwhaward",
    type : "post",
    dataType : "json",
    data : {
      "des3PubId" : des3PubId,
      "isAward" : isAward,
      "dbid" : dbid
    },
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (isAward == 1) {// 取消赞
            $(obj).attr("isAward", 0);
            count -= 1;
            if (count == 0) {
              $(obj).find('.dev_pub_award:first').text(pubRecommend.like);
            } else {
              $(obj).find('.dev_pub_award:first').text(pubRecommend.like + "(" + count + ")");
            }
            $(obj).find("i").removeClass("icon-praise-award").addClass("icon-praise");
          } else {// 赞
            $(obj).attr("isAward", 1);
            count += 1;
            $(obj).find('.dev_pub_award:first').text(pubRecommend.unlike + "(" + count + ")");
            $(obj).find("i").removeClass("icon-praise").addClass("icon-praise-award");

          }
        }
      });
    }
  });
};

/**
 * 基准库收藏成果 publicationArticleType
 */
PubRecommend.importPdwh = function(des3PubId, obj) {
  /*
   * var params=[]; params.push({"des3PubId":des3PubId}); var
   * postData={"jsonParams":JSON.stringify(params),"publicationArticleType":"2"};
   */
  $.ajax({
    url : "/pubweb/ajaxsavePaper",
    type : "post",
    dataType : "json",
    data : {
      "des3PubId" : des3PubId,
      "pubDb" : "PDWH"
    },
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data && data.result) {
          if (data.result == "success") {
            scmpublictoast(pubRecommend.collectionSuccess, 1000);
          } else if (data.result == "exist") {
            scmpublictoast(pubRecommend.pubIsSaved, 1000);
          } else if (data.result == "isDel") {
            scmpublictoast(pubRecommend.pubIsDeleted, 1000);
          } else {
            scmpublictoast(pubRecommend.collectionFail, 1000);
          }
        } else {
          scmpublictoast(pubRecommend.collectionFail, 1000);
        }

      });

    },
    error : function() {
    }
  });

}
// 引用
PubRecommend.dynCitePub = function(pubId, obj, event) {
  var urlStr = "/pubweb/publication/ajaxpdwhpubquote?pubId=" + pubId + "&TB_iframe=true&height=350&width=750";
  $(obj).attr("href", urlStr).click();
  PubRecommend.stopNextEvent(event)
}

PubRecommend.addDefultArea = function(areaCode) {
  if ($("#defultArea").val() == "") {
    $("#defultArea").val(areaCode);
  } else {
    var areaList = $("#defultArea").val().split(',');
    var index = $.inArray(areaCode, areaList);
    if (index < 0) {
      $("#defultArea").val($("#defultArea").val() + ',' + areaCode);
    }
  }
}
PubRecommend.deleteDefultArea = function(areaCode) {
  var areaStr = $("#defultArea").val();
  var areaList = areaStr.split(',');
  if ($.inArray(areaCode, areaList) >= 0) {
    areaList.splice($.inArray(areaCode, areaList), 1);
    $("#defultArea").val(areaList.join(','));
  }
  var searchAreaStr = $("#searchArea").val();
  var searchAreaList = searchAreaStr.split(',');
  if ($.inArray(areaCode, searchAreaList) >= 0) {
    searchAreaList.splice($.inArray(areaCode, searchAreaList), 1);
    $("#searchArea").val(searchAreaList.join(','));
  }
}

PubRecommend.addListElement = function(listJson, addStr) {
  if (!addStr || addStr == "") {
    return;
  }
  var list = new Array();;
  if (listJson) {
    list = eval(listJson);
    var index = $.inArray(addStr, list);
    if (index < 0) {
      list.push(addStr);
    }
  } else {
    list.push(addStr);
  }
  return (JSON.stringify(list));
}
PubRecommend.deleteListElement = function(listJson, addStr) {
  if (!addStr || addStr == "" || !listJson) {
    return;
  }
  var list = new Array();;
  list = eval(listJson);
  var index = $.inArray(addStr, list);
  if (index >= 0) {
    list.splice(index, 1);
  }
  return (JSON.stringify(list));
}

// 转义
PubRecommend.encodeHtml = function(s) {
  REGX_HTML_ENCODE = /"|&|'|<|>|[\x00-\x20]|[\x7F-\xFF]|[\u0100-\u2700]/g;
  return (typeof s != "string") ? s : s.replace(REGX_HTML_ENCODE, function($0) {
    var c = $0.charCodeAt(0), r = ["&#"];
    c = (c == 0x20) ? 0xA0 : c;
    r.push(c);
    r.push(";");
    return r.join("");
  });
};

PubRecommend.addHover = function(hoverlist, text) {
  for (var j = 0; j < hoverlist.length; j++) {
    hoverlist[j].onmouseover = function() {
      if (!$(this).hasClass('item_list-align_slected') && $(this).find(".filter-value__cancel").length <= 0) {
        $(this).append(text);
      }
    }
    hoverlist[j].onmouseleave = function() {
      $(this).find(".filter-value__cancel").remove();
    }
  }
}

// 成果详情,点击分享，把成果信息加载到分享界面
PubRecommend.getPubDetailsSareParam = function(obj) {
  // 初始化推荐联系人
  var des3ResId = $(obj).attr("des3ResId");
  if (des3ResId == null) {
    des3ResId = $(obj).attr("resId");
  }
  var pubId = $(obj).attr("pubId");
  var databaseType = $(obj).attr("databaseType");
  var dbId = $(obj).attr("dbId");
  var resType = $(obj).attr("resType");
  $("#share_to_scm_box").attr("dyntype", "GRP_SHAREPUB").attr("des3ResId", des3ResId).attr("pubId", pubId).attr(
      "resType", resType).attr("dbId", dbId);
  var content = $(obj).prev().val();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);
};

// 编辑个人领域
function showScienceAreaBox() {
  var areaArray = [];
  var scienceArea = $(".type_area");
  scienceArea.each(function() {
    areaArray.push($(this).attr("value"));
  })
  var scienceAreaIds = areaArray.join(",");
  $.ajax({
    url : "/psnweb/sciencearea/ajaxeditScienceArea",
    type : "post",
    dataType : "html",
    data : {
      "scienceAreaIds" : scienceAreaIds,
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        $("#scienceAreaBox").html(data);
        showDialog("scienceAreaBox");
      });

    },
    error : function() {
    }
  });
}
// 编辑关键词
function showKeyWordsBox() {
  var keyworStr = [];
  $(".type_key").each(function(){
    keyworStr.push($(this).attr("value"));
  })
  $.ajax({
    url : "/psnweb/keywords/ajaxeditrecommend",
    type : "post",
    dataType : "html",
    data : {
      "keywordStr" : JSON.stringify(keyworStr),
    },
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        $("#keywordsModule").html(data);
        $(".background-cover.cover_colored").show();
        showDialog("keyWordsBox");
      });
    },
    error : function() {
    }
  });
};
// 保存论文推荐科研领域
function savePsnScienceArea(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  $.ajax({
    url : "/pub/pubconditions/ajaxAddScienArea",
    type : "post",
    dataType : "json",
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
      "scienceAreaIds" : $("#choooseScienceAreaIds").val()
    },
    success : function(data) {
      Resume.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          hideScienceAreaBox();
          showPsnScienceArea();
        }
      });

    },
    error : function() {
    }
  });
}

function addScienceArea(id) {
  var key = $("#" + id + "_category_title").html();
  var sum = parseInt($("#scienceAreaSum").val());
  if (sum < 3) {
    var html = '<div class="main-list__item" style="padding: 0px 16px!important;" id="choosed_' + id + '">'
        + '<div class="main-list__item_content id=\"' + id + '\" title="' + key + '">' + key + '</div>'
        + '<div class="main-list__item_actions"  onclick="javascript:delScienceArea(\'' + id
        + '\');"><i class="material-icons">close</i></div></div>';
    $("#choosed_area_list").append(html);
    $("#unchecked_area_" + id).attr("onclick", "");
    $("#unchecked_area_" + id).attr("id", "checked_area_" + id);
    $("#" + id + "_status").html("check");
    $("#" + id + "_status").css("color", "forestgreen");
    $("#scienceAreaSum").val(sum + 1);
    $("#choooseScienceAreaIds").val($("#choooseScienceAreaIds").val() + "," + id);
    $("#areaStr").val($("#areaStr").val() + "," + key);
  } else {
    // 出提示语
    scmpublictoast(pubRecommend.addAreaSizeFail, 1500);
  }
}

function addkeyWordsBox(id, keyword, $createGrpChipBox) {
  var chipbox = $("div[chipbox-id='oneKeyWords']");
  var chipNum = $("div[chipbox-id='oneKeyWords']").children("div.chip__box").length;
  if (!(chipNum < 10)) {
    // SCM-16133 关键词超过10个
    chipbox.addClass("count_exceed");
    return;
  }
  if (id != null && id != "") {
    var rcdKeyword = "#" + id;
    $(rcdKeyword).remove();
    var strS = "<div class=\"chip__box\" id=\"disciplinesAndKeywords\"><div class=\"chip__avatar\"></div><div class=\"chip__text\">";
    var strE = "</div><div class=\"chip__icon icon_delete\" onclick=\"javascript:removeKeywords('boxkeywords');\"><i class=\"material-icons\">close</i></div>";
    var str = "";
    str = strS + keyword + strE;
    var boxkeywordsNumber = chipNum + 1;
    str = str.replace("boxkeywords", "boxkeywords_" + boxkeywordsNumber);
    $("#autokeywords").before(str);
    if ($(".recommendKeywordItem").size() <= 0) {
      $('.psn_rcmd_keywords').html("");
    }
  }
  $createGrpChipBox.chipBoxInitialize();
}

// 隐藏关键词编辑框
function hideKeyWordsBox(obj) {
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  $(".background-cover.cover_colored").hide();
  $("#keywordsModule").html("");
}

function delScienceArea(id) {
  $("#choosed_" + id).remove();
  $("#checked_area_" + id).attr("onclick", "addScienceArea('" + id + "')");
  $("#checked_area_" + id).attr("id", "unchecked_area_" + id);
  $("#" + id + "_status").html("add");
  $("#" + id + "_status").css("color", "");
  var choooseScienceAreaIds = $("#choooseScienceAreaIds").val();
  $("#choooseScienceAreaIds").val(choooseScienceAreaIds.replace("," + id, ""));
  var sum = parseInt($("#scienceAreaSum").val());
  $("#scienceAreaSum").val(sum - 1);
}

function removeKeywords(id) {
  if (id != "" && id != null) {
    var boxkeywords = "#" + id;
    $(boxkeywords).hide();
  }
}

PubRecommend.ajaxLondPubList = function() {
  var dataList = PubRecommend.getListData();
  window.Mainlist({
      name: "pub_list",
      listurl: "/pub/ajaxrecommend",
      listdata: dataList,
      method: "pagination",
      noresultHTML : "<div style='width: 100%; height: 36px; line-height: 36px; text-align: center; font-size: 16px; color: rgba(0, 0, 0, 0.54); margin-top: 24px'>"
     +pubRecommend.noRecommend+"</div>",
      listcallback: function(xhr) {  
          //$(".thickbox").thickbox();
      }
  });
};

PubRecommend.getListData = function(){
  var dataList = {};
  var defultKey = [];
  var defultArea = [];
  
  var searchArea = [];
  var searchPsnKey = [];
  var searchPubYear = [];
  var searchPubType = [];
  $("#key_ul>.item_list-align").each(function(){
    var value =  $(this).find(".type_key").attr("value");
    defultKey.push(value);
    if($(this).hasClass("item_list-align_slected")){
      searchPsnKey.push(value);
    }
  });
  if(searchPsnKey.length<=0){
    searchPsnKey = defultKey;
  }
  $("#area_ul>.item_list-align").each(function(){
    var value =  $(this).find(".type_area").attr("value");
    defultArea.push(value);
    if($(this).hasClass("item_list-align_slected")){
      searchArea.push(value);
    }
  });
  if(searchArea.length<=0){
    searchArea = defultArea;
  }
  $("#type_ul>.item_list-align_slected").each(function(){
    var value =  $(this).find(".type_pub").attr("value");
    searchPubType.push(value);
  });
  
  searchPubYear = $("#year_ul>.item_list-align_slected>.type_time").attr("value") ;
  
  dataList.defultKeyJson = JSON.stringify( defultKey );
  dataList.searchArea = searchArea.join(",");
  dataList.searchPsnKey = JSON.stringify( searchPsnKey );
  dataList.searchPubYear = searchPubYear ? searchPubYear : "";
  dataList.searchPubType = searchPubType.join(",");
  return dataList;
};
// 保存关键词
function saveKeyWordsBox(obj) {
  var keysArr = [];
  var keyStr = "";
  $.each($("#oneKeyWords").find(".chip__text"), function(i, o) {
    keysArr.push($(o).text());
  });
  var jsonKeyword = JSON.stringify(keysArr);
  //keyStr = keysArr.join(";");//以分号分割,因为;会退出编辑并且逗号拆分会导致多个的问题
  if (obj != undefined) {// 防止重复点击
    Resume.doHitMore(obj, 3000);
  }
  var postData = {
    'addKeyWord' : jsonKeyword,
  };
  $
      .ajax({
        url : "/pub/pubconditions/ajaxAddKeyWord",
        type : "post",
        dataType : "json",
        data : postData,
        success : function(data) {
          Resume
              .ajaxTimeOut(
                  data,
                  function() {
                    if (data.result == "success") {
                      $("#key_ul").html("");
                      if (data.keywordsList != "[]") {
                        var keywordsHtml = "";
                        var newAddkeywords = "";
                        //不需要截取
                        //var keywords = data.keywordsList.substring(1, data.keywordsList.length - 1).split(";");
                        var keywords = data.keywordsList.split(";");
                        if (typeof data.newAdddKeyWords != typeof undefined && data.newAdddKeyWords.length != 0) {
                          newAddkeywords = data.newAdddKeyWords;
                        } else {
                          $("#searchPsnKey").val("");
                        }
                        //不再需要toString()
                        //$("#defultKeyJson").val(data.keywordsList.toString());
                        $("#defultKeyJson").val(data.keywordsList);
                        for (var i = 0; i < keywords.length; i++) {
                          keywordsHtml = "<li class='item_list-align'><div class='item_list-align_item type_key' onclick=\"PubRecommend.addCondition(this,'type_key')\" value=\""
                              + keywords[i] + "\" title=\"" + keywords[i] + "\">" + keywords[i] + "</div></li>";
                          $("#key_ul").append(keywordsHtml);
                          if (newAddkeywords != '' && $.inArray($.trim(keywords[i]), newAddkeywords) != -1) {
                            PubRecommend.addCondition($(".type_key")[i], 'type_key');
                          }
                        }
                      } else {
                        $("#defultKeyJson").val("");
                        $("#searchPsnKey").val("");
                      }
                      hideKeyWordsBox();
                      PubRecommend.ajaxLondPubList();
                    } else if (data.result == "rep") {
                      scmpublictoast(pubRecommend.addKeyRepFail, 1500);
                    } else if (data.result == "blank") {
                      scmpublictoast(pubRecommend.addEmptyKey, 1500);
                    } else if (data.result == "exc") {
                      scmpublictoast(pubRecommend.addKeySizeFail, 1500);
                    }
                  });

        },
        error : function() {

        }
      });
}
