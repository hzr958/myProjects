/**
 * 群组js zzx
 */
var GrpBase = GrpBase ? GrpBase : {};

// 保存短地址--zzx-----
GrpBase.doSaveShortUrl = function(oldurl, newurl, myfunction1, myfunction2) {
  $.ajax({
    url : '/groupweb/mygrp/ajaxsavegrpshorturl',
    type : 'post',
    dataType : 'json',
    async : false,
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id"),
      'oldShortUrl' : oldurl,
      'newShortUrl' : newurl
    },
    success : function(data) {
      if (data.result == "success") {
        myfunction1();
        scmpublictoast(data.msg, 2000);
      } else {
        myfunction2();
        scmpublictoast(data.msg, 2000);
      }
    },
    error : function() {
      myfunction2();
      scmpublictoast(groupBase.systemBusy, 2000);
    }
  });
}
// 点击编辑短地址------------zzx---------------
GrpBase.modificationshortUrl = function(event) {
  if (1 != event.which) {
    return;
  }
  var span_shorturl = $("#span_shorturl");
  // 最开始的短地址
  var shorturl = $("#span_shorturl").attr("scm-oldurl");
  if ($("#input_shorturl").length > 0) {
    // 移除编辑框前要还原内容
    span_shorturl.text(span_shorturl.attr("scm-oldurl"));
    $("#input_shorturl").remove();
    return;
  }
  // 清理浏览器默认事件
  event.preventDefault();
  if (GrpBase.getSelectionText() == span_shorturl.text()) {
    // 取消选中状态
    if (window.getSelection()) {
      window.getSelection().removeAllRanges();
    } else {
      document.selection.empty();
    }
    // 操作短地址可编辑部分
    var strArr = shorturl.split("/G/");
    strArr[0] += "/G/";
    span_shorturl.text(strArr[0]);
    // 添加编辑框
    $("#span_shorturl").after(
        "<input id='input_shorturl'style='min-width:10px;'  type='text' value='" + strArr[1] + "'/>");
    $("#input_shorturl").focus();
    var num = $('#input_shorturl').val().length;
    $('#input_shorturl')[0].setSelectionRange(num, num);
    // 给编辑框绑定事件
    $("#input_shorturl").bind("keydown", function(ev) {
      if (ev.keyCode == 13) {
        // 正则过滤用户输入的短地址
        // var patrn=/^(?!_)(?!.*?_$)[a-zA-Z0-9_]{1,20}$/; //不能已下划线开头或结尾
        var patrn = /^[a-zA-Z0-9_]{1,20}$/;
        if (patrn.exec($('#input_shorturl').val()) == null) {
          scmpublictoast(groupBase.shortAddressNote, 3000);
          return;
        }
        // 发请求保存短地址
        GrpBase.doSaveShortUrl(strArr[1], $('#input_shorturl').val(), function() {
          // 保存成功-移除编辑框前要记录编辑内容
          $("#span_shorturl").attr("scm-oldurl", span_shorturl.text() + $('#input_shorturl').val());
          span_shorturl.text(span_shorturl.text() + $('#input_shorturl').val());
          $("#input_shorturl").remove();
        }, function() {
          // 保存失败-移除编辑框前要还原内容
          span_shorturl.text(span_shorturl.attr("scm-oldurl"));
          $("#input_shorturl").remove();
        });
      }
    }).bind("blur", function() {
      // 移除编辑框前要还原内容
      span_shorturl.text(span_shorturl.attr("scm-oldurl"));
      $("#input_shorturl").remove();
    });
  } else {
    // 第一次点击设置选中
    GrpBase.selectText("id_shorturl");
  }
}
// 检查短地址是否合法----------zzx----------
GrpBase.checkShortUrl = function(shortUrl) {
  if (Regex.IsMatch(shortUrl, "")) {

  } else {

  };
}
// 使元素的文本选中----------zzx----------
GrpBase.selectText = function(element) {
  var text = $("#span_shorturl")[0];
  if (document.body.createTextRange) {
    var range = document.body.createTextRange();
    range.moveToElementText(text);
    range.select();
  } else if (window.getSelection) {
    var selection = window.getSelection();
    var range = document.createRange();
    range.selectNodeContents(text);
    selection.removeAllRanges();
    selection.addRange(range);
  }
}
// 获取选中的文本- 用于判断是否已经点击一次-----------zzx----------------
GrpBase.getSelectionText = function() {
  if (window.getSelection) {
    return window.getSelection().toString();
  } else if (document.selection && document.selection.createRange) {
    return document.selection.createRange().text;
  }
  return '';
}
// 改变url
GrpBase.changeUrl = function(targetModule) {
  var json = {};
  var oldUrl = window.location.href;
  var index = oldUrl.lastIndexOf("model");
  var newUrl = window.location.href;
  if (targetModule != undefined && targetModule != "") {

    if (index < 0) {
      if (oldUrl.lastIndexOf("?") > 0) {
        newUrl = oldUrl + "&model=" + targetModule;
      } else {
        newUrl = oldUrl + "?model=" + targetModule;
      }
    } else {
      newUrl = oldUrl.substring(0, index) + "model=" + targetModule;
    }
  }
  window.history.replaceState(json, "", newUrl);
}

// 设置群组置顶 setTopOpt 1=设置置顶；0=取消置顶
GrpBase.setGrpTop = function(des3GrpId, setTopOpt) {
  $.ajax({
    url : '/groupweb/mygrp/ajaxmygrpsettop',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : des3GrpId,
      'setTopOpt' : setTopOpt
    // 1=设置置顶；0=取消置顶
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        // 加载群组列表
        GrpBase.showGrpList()
      });
    },
    error : function() {
    }
  });
}
// 加载群组列表
GrpBase.showGrpList = function() {
  window.Mainlist({
    name : "grplist",
    listurl : "/groupweb/mygrp/ajaxmygrplist",
    listdata : {},
    listcallback : function(xhr) {
      if ($("#grp_list").find(".main-list__item").length == 0) {
        $("#grp_list").html("<div class='response_no-result'>" + groupBase.noRecord + "</div>");
        $("#foot_jsp_div").empty();
      }
      // 加载群组列表关键词
      GrpBase.showDisciplinesAndKeywordsForList();
    },
    statsurl : "/groupweb/mygrp/ajaxmygrplistcallback"
  });
}

// 群组列表领域和关键词显示
;
GrpBase.showDisciplinesAndKeywordsForList = function() {
  var strS = "<div class='kw-chip_small'>";
  var strMoreS = "<div class='kw-chip_small  dev_kw_chip_more' style='display:none;'>";
  var strE = "</div>";
  var strDot = "<div class='kw-chip_small dev_dot' style='background-color: white;font-size: 20px;font-weight: bold;display:block;cursor:pointer;color: #1265cf;'>···</div>";

  $.each($("#grp_list").find(".kw__box"), function(i, o) {
    // 最多显示三个，多余的用...
    var count = 0;
    var totalCount = 0;
    var str = "";
    $.each($(o).attr("smate_disciplines").split(";"), function(i, discipline) {
      if (discipline != '') {
        str = str + strS + discipline + strE;
      }
    });
    $.each($(o).attr("smate_keywords").split(";"), function(i, keyword) {
      if (keyword != '') {
        keyword = keyword.replace(/77&/g, ";");
        if (count < 3) {
          str = str + strS + keyword + strE;
        } else {
          str = str + strMoreS + keyword + strE;
        }
        count++;
        totalCount++;
      }
    });
    $(o).html(str);
    if (totalCount > 3) {
      $(o).append(strDot);
      $(o).attr("onclick", "GrpBase.showMoreKeyWord(this)");
    }
  });
};
// 显示更多关键词
GrpBase.showMoreKeyWord = function(obj) {
  var displayVal = $(obj).find(".dev_dot").css("display");
  if (displayVal !== "none") {
    $(obj).find(".dev_dot").css("display", "none");
    $(obj).find(".dev_kw_chip_more").each(function() {
      $(this).css("display", "");
    });
  } else {
    $(obj).find(".dev_dot").css("display", "block");
    $(obj).find(".dev_kw_chip_more").each(function() {
      $(this).css("display", "none");
    });
  }
}

// 显示复制群组界面
GrpBase.showCopyGrpUI = function(des3GrpId, grpCategory, obj) {
  $("#copy_grp_name").parent().parent().addClass("input_focused");
  $("#copy_grp_name").val($.trim($(obj).closest(".main-list__item").find(".grp-idx__main_name > a").text()));
  $("#btn_copy_grp").attr("code", des3GrpId);
  if (grpCategory == 10) {
    $("#label_grpCategory").text(groupBase.grpCategory1);
  } else {
    $("#label_grpCategory").text(groupBase.grpCategory2);
  }
  // <!-- 10 课程群组 ，文献，课件，作业 -->
  if ("10" === grpCategory) {
    $(".dev_show_copy_grp_item").removeClass("dev_show_copy_grp_item");
    $("#copy_grp_ui_course").addClass("dev_show_copy_grp_item");
    $("#copy_grp_ui_course").css("display", "flex");
    $("#copy_grp_ui_project").css("display", "none");
    $("#copy_grp_ui_other").css("display", "none");
  } else if ("11" === grpCategory) {
    $(".dev_show_copy_grp_item").removeClass("dev_show_copy_grp_item");
    $("#copy_grp_ui_project").addClass("dev_show_copy_grp_item");
    $("#copy_grp_ui_course").css("display", "none");
    $("#copy_grp_ui_project").css("display", "flex");
    $("#copy_grp_ui_other").css("display", "none");
  } else {
    $(".dev_show_copy_grp_item").removeClass("dev_show_copy_grp_item");
    $("#copy_grp_ui_other").addClass("dev_show_copy_grp_item");
    $("#copy_grp_ui_course").css("display", "none");
    $("#copy_grp_ui_project").css("display", "none");
    $("#copy_grp_ui_other").css("display", "flex");
  }
  showDialog("copy_grp_ui");
}
// 隐藏复制群组界面
GrpBase.hideCopyGrpUI = function() {
  hideDialog("copy_grp_ui");
}
// 创建群组-修改群组类型
GrpBase.changeGrpType = function(obj, type) {
  $(obj).closest(".create-grp__select-type_container").find(".item_selected").removeClass("item_selected").removeClass(
      "class_create_grpCategory");
  $(obj).addClass("item_selected").addClass("class_create_grpCategory");
  if (type == 11) {// 项目群组
    $("#id_dev_projectno_val").show();
    $("#id_title_grp_name").html(groupBase.proTitle);
    $("#id_title_grp_grpdescription").html(groupBase.proDesc);
    $("span[name='corse_module']").css("display","none");
    $("span[name='project_module']").css("display","flex");

    $("#choose-authority-list").find("input[value='H']").closest(".input-radio__sxn").click();
  } else if (type == 10) {// 兴趣群组
      $("span[name='corse_module']").css("display","flex");
      $("span[name='project_module']").css("display","none");
    $("#id_dev_projectno_val").hide();
    $("#id_title_grp_name").html(groupBase.curTitle);
    $("#id_title_grp_grpdescription").html(groupBase.curDesc);
    $("#choose-authority-list").find("input[value='O']").closest(".input-radio__sxn").click();
  }
}
// 复制群组
GrpBase.doCopyGrp = function(obj) {
  GrpBase.doHitMore(obj, 2000);
  if ($.trim($("#copy_grp_name").val()) == "") {
    scmpublictoast(groupBase.grpNameCannotNone, 2000);
    return;
  }

  var isCopyBaseinfo = 0;
  var isCopyGrpPubs = 0;
  var isCopyGrpCourseware = 0;

  var isCopyCourseGrpCourseware = 0;
  var isCopyCourseGrpWork = 0;
  var isCopyProjectGrpPubs = 0;
  var isCopyProjectGrpRefs = 0;

  var name = "";
  $.each($("#copy_grp_ui").find(".dev_show_copy_grp_item  input[type='checkbox']:checked"), function(i, o) {
    name = $(o).attr("name");
    if ('isCopyBaseinfo' == name) {
      isCopyBaseinfo = 1;
    } else if ('isCopyGrpPubs' == name) {
      isCopyGrpPubs = 1;
    } else if ('isCopyGrpCourseware' == name) {
      isCopyGrpCourseware = 1;
    } else if ('isCopyCourseGrpCourseware' == name) {
      isCopyCourseGrpCourseware = 1;
    } else if ('isCopyCourseGrpWork' == name) {
      isCopyCourseGrpWork = 1;
    } else if ('isCopyProjectGrpPubs' == name) {
      isCopyProjectGrpPubs = 1;
    } else if ('isCopyProjectGrpRefs' == name) {
      isCopyProjectGrpRefs = 1;
    }
  });
  var des3GrpId = $(obj).attr("code");
  if (des3GrpId == null || des3GrpId == "") {
    scmpublictoast(groupBase.invalidGrp, 2000);
    return 0;
  }
  $.ajax({
    url : '/groupweb/mygrp/ajaxcopygrp',
    type : 'post',
    dataType : 'json',
    data : {
      'targetdes3GrpId' : des3GrpId,
      'isCopyBaseinfo' : isCopyBaseinfo,
      'isCopyGrpPubs' : isCopyGrpPubs,
      'isCopyGrpCourseware' : isCopyGrpCourseware,
      'isCopyCourseGrpCourseware' : isCopyCourseGrpCourseware,
      'isCopyCourseGrpWork' : isCopyCourseGrpWork,
      'isCopyProjectGrpPubs' : isCopyProjectGrpPubs,
      'isCopyProjectGrpRefs' : isCopyProjectGrpRefs,
      'grpName' : $.trim($("#copy_grp_name").val())
    },
    success : function(data) {
      GrpBase.hideCopyGrpUI();
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          scmpublictoast(groupBase.opreateSuccess, 2000);
          location.href = "/groupweb/grpinfo/main?des3GrpId=" + encodeURIComponent(data.des3GrpId);
        } else {
          scmpublictoast(data.msg, 2000);
        }
      });
    },
    error : function() {
      scmpublictoast(groupBase.opreateFail, 2000);
    }
  });
}
// 显示编辑群组页面
GrpBase.showGrpManageUI = function() {
  $("#grp_main_box").hide();
  $("#grp_manage_ui_show").html($("#grp_manage_ui").html());
  // addFormElementsEvents($("#grp_manage_ui_show")[0]);
  // window.ChipBox({name:"chipcodeedit",maxItem:10});
}
// 隐藏编辑群组页面
GrpBase.hideGrpManageUI = function() {
  $("#grp_params").find(".nav__list > .item_selected").click();
}
// 编辑群组基本信息
GrpBase.showGrpBaseUI = function() {
  $("#grp_opentype_ui").hide();
  $("#grp_base_ui").show();
}
// 编辑群组权限
GrpBase.showGrpOpentypeUI = function() {
  $("#grp_base_ui").hide();
  $("#grp_opentype_ui").show();
}
GrpBase.clickGrpManageLi = function(obj) {
  $("#grp_manage_li").find("li").removeClass("item_selected");
  $(obj).addClass("item_selected");
}
// 群组领域和关键词显示
;
GrpBase.showDisciplinesAndKeywords = function() {
  // 显示在头部
  var str = "";
  var strS = "<div class='kw-chip_medium'>";
  var strE = "</div>";
  // 显示在管理群组
  var manageStr = "";
  var manageStrS = "<div class='chip__box'><div class='chip__text'>";
  var manegeStrE = "</div><div class='chip__icon icon_delete' onclick='GrpBase.manageDelKeyword(this)'><i class='material-icons'>close</i></div></div>";
  // 拼接数据
  var obj = $("#smate_disciplines_keywords");
  $.each(obj.attr("smate_disciplines").split(";"), function(i, discipline) {
    if (discipline != '') {
      str = str + strS + discipline + strE;
    }
  });
  $.each(obj.attr("smate_keywords").split(";"), function(i, keyword) {
    keyword = $.trim(keyword);
    if (keyword != "") {
      keyword = keyword.replace(/77&/g, ";");
      manageStr += manageStrS + keyword + manegeStrE;
      str = str + strS + keyword + strE;
    }
  });
  obj.html(str);// 显示在头部
  $("#manage_keywords").html(manageStr); // 显示在管理群组
}
// 管理群组删除关键词
GrpBase.manageDelKeyword = function(obj) {
  $(obj).closest(".chip__box").remove();
}
// 删除群组
GrpBase.delMyGrp = function(obj) {
  if (obj != null) {
    GrpBase.doHitMore(obj, 2000);
  }
  $.ajax({
    url : '/groupweb/mygrp/ajaxdelmygrp',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          scmpublictoast(groupBase.opreateSuccess, 1000);
          location.href = "/groupweb/mygrp/main";
        } else {
          scmpublictoast(groupBase.opreateFail, 2000);
        }
      });
    },
    error : function() {
      scmpublictoast(groupBase.opreateFail, 2000);
    }
  });

}
// 修改群组模块公开权限
GrpBase.modifyGrpPermissions = function(obj) {
  GrpBase.doHitMore(obj, 2000);
  var isIndexDiscussOpen = 0;
  var isIndexMemberOpen = 0;
  var isIndexPubOpen = 0;
  var isIndexFileOpen = 0;

  var isCurwareFileShow = 0;
  var isWorkFileShow = 0;
  var isPrjPubShow = 0;
  var isPrjRefShow = 0;
  var name = "";
  $.each($("#grp_opentype_ui").find("input[type='checkbox']:checked"), function(i, o) {
    name = $(o).attr("name");
    if ('isIndexDiscussOpen' == name) {
      isIndexDiscussOpen = 1;
    } else if ('isIndexMemberOpen' == name) {
      isIndexMemberOpen = 1;
    } else if ('isIndexPubOpen' == name) {
      isIndexPubOpen = 1;
    } else if ('isIndexFileOpen' == name) {
      isIndexFileOpen = 1;
    } else if ('isCurwareFileShow' == name) {
      isCurwareFileShow = 1;
    } else if ('isWorkFileShow' == name) {
      isWorkFileShow = 1;
    } else if ('isPrjPubShow' == name) {
      isPrjPubShow = 1;
    } else if ('isPrjRefShow' == name) {
      isPrjRefShow = 1;
    }
  });
  var des3GrpId = $("#grp_params").attr("smate_des3_grp_id");
  $.ajax({
    url : '/groupweb/mygrp/ajaxmodifygrppermissions',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : des3GrpId,
      "isIndexDiscussOpen" : isIndexDiscussOpen,// 主页是否显示群组动态 [1=是 ， 0=否 ]
      "isIndexMemberOpen" : isIndexMemberOpen,// 主页是否显示群组成员 [1=是 ， 0=否 ]
      "isIndexPubOpen" : isIndexPubOpen,// 主页是否显示群组成果 [1=是 ， 0=否 ]
      "isIndexFileOpen" : isIndexFileOpen,// 主页是否显示群组文件 [1=是 ， 0=否 ]
      "isCurwareFileShow" : isCurwareFileShow,// 群组外成员是否显示项目成果 [1=是 ， 0=否 ] 默认1
      "isWorkFileShow" : isWorkFileShow,// 群组外成员是否显示项目文献 [1=是 ， 0=否 ] 默认1
      "isPrjPubShow" : isPrjPubShow,// 群组外成员是否显示课件 [1=是 ， 0=否 ] 默认1
      "isPrjRefShow" : isPrjRefShow,// 群组外成员是否显示作业 [1=是 ， 0=否 ] 默认1
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          scmpublictoast(groupBase.opreateSuccess, 1000);
          location.href = "/groupweb/grpinfo/main?des3GrpId=" + encodeURIComponent(des3GrpId);
        } else {
          scmpublictoast(groupBase.opreateFail, 2000);
        }
      });

    },
    error : function() {
      scmpublictoast(groupBase.opreateFail, 2000);
    }
  });
}

// 确定编辑群组操作
GrpBase.doGrpBaseManage = function(obj) {
  GrpBase.doHitMore(obj, 2000);
  var smate_grpcategory = $("#grp_params").attr("smate_grpcategory");
  var obj_ui = $("#grp_base_ui");
  var grpName = obj_ui.find(".input_grpname_val").val();
  if ($.trim(grpName) == "") {
    if ("11" == smate_grpcategory) {
      scmpublictoast(groupBase.notPrjTitle, 2000);
    } else {
      scmpublictoast(groupBase.notCurName, 2000);
    }
    return 0;
  }
  var grpDescription = obj_ui.find(".input_grpdescription_val").val();
  if ($.trim(grpDescription) == "") {
    if ("11" == smate_grpcategory) {
      scmpublictoast(groupBase.notPrjAbstract, 2000);
    } else {
      scmpublictoast(groupBase.notCurDesc, 2000);
    }
    return 0;
  }
  var projectNo = obj_ui.find(".input_projectno_val").val();
  if (projectNo && projectNo != "" && projectNo.length > 100) {
    scmpublictoast(groupBase.proNoLimit, 2000);
    return;
  }

  var openType = obj_ui.find("input[name='choose-authority']:checked").val();
  var keywords = GrpBase.getGrpManageKeywords();
  if (keywords.length > 640) {
    scmpublictoast(groupBase.keywordsLimit, 2000);
    return;
  }
  var firstCategoryId = $(".sel__box[selector-id='1st_discipline']").attr("sel-value");
  var secondCategoryId = $(".sel__box[selector-id='2nd_discipline']").attr("sel-value");
  if ($.trim(firstCategoryId) == "" || $.trim(secondCategoryId) == "") {
    scmpublictoast(groupBase.notPrjResearchArea, 2000);
    return 0;
  }
  var des3GrpId = $("#grp_params").attr("smate_des3_grp_id");
  $.ajax({
    url : '/groupweb/mygrp/ajaxsaveeditgrp',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : des3GrpId,
      'grpName' : grpName,
      'grpDescription' : grpDescription,
      'projectNo' : projectNo,
      'openType' : openType,
      'keywords' : encodeURIComponent(keywords),
      'firstCategoryId' : firstCategoryId,
      'secondCategoryId' : secondCategoryId
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          scmpublictoast(groupBase.opreateSuccess, 1000);
          setTimeout(function() {
            location.href = "/groupweb/grpinfo/main?des3GrpId=" + encodeURIComponent(des3GrpId);
          }, 1000)
        } else {
          scmpublictoast(groupBase.opreateFail, 2000);
        }
      });

    },
    error : function() {
      scmpublictoast(groupBase.opreateFail, 2000);
    }
  });
}

var hitOpneTypeCommen = "";// 获取群组权限级别
// 公开
function grpChangePublic(str) {
  if (str === "myGrp") {
      hitOpneTypeCommen = "O";
      $("#grpEditPublicDescribe").css("display", "block");
      $("#grpEditSemiPublicDescribe").css("display", "none");
    $("#myGrpPublicDescribe").css("display", "block");
    $("#myGrpSemiPublicDescribe").css("display", "none");
    $("#myGrpPrivacyDescribe").css("display", "none");
  }

  if (str === "grpEdit") {
    hitOpneTypeCommen = "O";
    $("input[name='choose-authority']").eq(0).attr("checked", "checked");
    $("#grpEditPublicDescribe").css("display", "block");
    $("#grpEditSemiPublicDescribe").css("display", "none");
    $("#grpEditPrivacyDescribe").css("display", "none");
  }
}

// 半公开
function grpChangeSemiPublic(str) {
  if (str === "myGrp") {
      hitOpneTypeCommen = "H";
      $("#grpEditPublicDescribe").css("display", "none");
      $("#grpEditSemiPublicDescribe").css("display", "block");
    $("#myGrpPublicDescribe").css("display", "none");
    $("#myGrpSemiPublicDescribe").css("display", "block");
    $("#myGrpPrivacyDescribe").css("display", "none");
  }

  if (str === "grpEdit") {
    hitOpneTypeCommen = "H";
    $("#grpEditPublicDescribe").css("display", "none");
    $("#grpEditSemiPublicDescribe").css("display", "block");
    $("#grpEditPrivacyDescribe").css("display", "none");
  }
}

// 隐私
function grpChangeSemiPrivacy(str) {
  if (str === "myGrp") {
    $("#myGrpPublicDescribe").css("display", "none");
    $("#myGrpSemiPublicDescribe").css("display", "none");
    $("#myGrpPrivacyDescribe").css("display", "block");
      hitOpneTypeCommen = "P";
      $("#grpEditPublicDescribe").css("display", "none");
      $("#grpEditSemiPublicDescribe").css("display", "none");
  }

  if (str === "grpEdit") {
    hitOpneTypeCommen = "P";
    $("#grpEditPublicDescribe").css("display", "none");
    $("#grpEditSemiPublicDescribe").css("display", "none");
    $("#grpEditPrivacyDescribe").css("display", "block");
  }
}

// 确定编辑群组操作
GrpBase.doGrpBaseManage = function(obj) {
  GrpBase.doHitMore(obj, 2000);
  var isIndexDiscussOpen = 1;
  var isIndexMemberOpen = 0;
  var isIndexPubOpen = 0;
  var isIndexFileOpen = 0;

  var isCurwareFileShow = 0;
  var isWorkFileShow = 0;
  var isPrjPubShow = 0;
  var isPrjRefShow = 0;
  var name = "";
  if (hitOpneTypeCommen === "") {
    hitOpneTypeCommen = $("[name = 'hitOpneType']").val();
  }
  if(hitOpneTypeCommen == "O" || hitOpneTypeCommen == "H"){
      var select = 0;
      var id = hitOpneTypeCommen == "O" ? "openType" : "halfType";
         $("#"+id).find(".input-radio__sxn").each(function () {
          var find = $(this).find("input[type='checkbox']").is(':checked');
          if(find){
              select ++ ;
          }
      });
      if(select<3){
          scmpublictoast(groupBase.selectOpenModuleTip, 2000);
          return 0;
      }
  }
  if (hitOpneTypeCommen == "O") {
    $.each($("#openType").find("input[type='checkbox']:checked"), function(i, o) {
      name = $(o).attr("name");
      if ('isIndexMemberOpen1' == name) {
        isIndexMemberOpen = 1;
      } else if ('isIndexPubOpen1' == name) {
        isIndexPubOpen = 1;
      } else if ('isIndexFileOpen1' == name) {
        isIndexFileOpen = 1;
      } else if ('isCurwareFileShow1' == name) {
        isCurwareFileShow = 1;
      } else if ('isWorkFileShow1' == name) {
        isWorkFileShow = 1;
      } else if ('isPrjPubShow1' == name) {
        isPrjPubShow = 1;
      } else if ('isPrjRefShow1' == name) {
        isPrjRefShow = 1;
      }
    });
  }

  if (hitOpneTypeCommen == "H") {
    $.each($("#halfType").find("input[type='checkbox']:checked"), function(i, o) {
      name = $(o).attr("name");
      if ('isIndexMemberOpen2' == name) {
        isIndexMemberOpen = 1;
      } else if ('isIndexPubOpen2' == name) {
        isIndexPubOpen = 1;
      } else if ('isIndexFileOpen2' == name) {
        isIndexFileOpen = 1;
      } else if ('isCurwareFileShow2' == name) {
        isCurwareFileShow = 1;
      } else if ('isWorkFileShow2' == name) {
        isWorkFileShow = 1;
      } else if ('isPrjPubShow2' == name) {
        isPrjPubShow = 1;
      } else if ('isPrjRefShow2' == name) {
        isPrjRefShow = 1;
      }
    });
  }

  var smate_grpcategory = $("#grp_params").attr("smate_grpcategory");
  var obj_ui = $("#grp_base_ui");
  var grpName = obj_ui.find(".input_grpname_val").val();
  if ($.trim(grpName) == "") {
    if ("11" == smate_grpcategory) {
      scmpublictoast(groupBase.notPrjTitle, 2000);
    } else {
      scmpublictoast(groupBase.notCurName, 2000);
    }
    return 0;
  }
  var grpDescription = obj_ui.find(".input_grpdescription_val").val();
  if ($.trim(grpDescription) == "") {
    if ("11" == smate_grpcategory) {
      scmpublictoast(groupBase.notPrjAbstract, 2000);
    } else {
      scmpublictoast(groupBase.notCurDesc, 2000);
    }
    return 0;
  }
  var projectNo = obj_ui.find(".input_projectno_val").val();
  if (projectNo && projectNo != "" && projectNo.length > 100) {
    scmpublictoast(groupBase.proNoLimit, 2000);
    return;
  }

  var openType = obj_ui.find("input[name='choose-authority']:checked").val();
  var keywords = GrpBase.getGrpManageKeywords();
  if (keywords.length > 640) {
    scmpublictoast(groupBase.keywordsLimit, 2000);
    return;
  }
  var firstCategoryId = $(".dev_sel__box[selector-id='1st_discipline']").attr("sel-value");
  var secondCategoryId = $(".dev_sel__box[selector-id='2nd_discipline']").attr("sel-value");
  if ($.trim(firstCategoryId) == "" || $.trim(secondCategoryId) == "") {
    scmpublictoast(groupBase.notPrjResearchArea, 2000);
    return 0;
  }
  var des3GrpId = $("#grp_params").attr("smate_des3_grp_id");
  $.ajax({
    url : '/groupweb/mygrp/ajaxsaveeditgrp',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : des3GrpId,
      "isIndexDiscussOpen" : isIndexDiscussOpen,// 主页是否显示群组动态 [1=是 ， 0=否 ]
      "isIndexMemberOpen" : isIndexMemberOpen,// 主页是否显示群组成员 [1=是 ， 0=否 ]
      "isIndexPubOpen" : isIndexPubOpen,// 主页是否显示群组成果 [1=是 ， 0=否 ]
      "isIndexFileOpen" : isIndexFileOpen,// 主页是否显示群组文件 [1=是 ， 0=否 ]
      "isCurwareFileShow" : isCurwareFileShow,// 群组外成员是否显示项目成果 [1=是 ， 0=否 ] 默认1
      "isWorkFileShow" : isWorkFileShow,// 群组外成员是否显示项目文献 [1=是 ， 0=否 ] 默认1
      "isPrjPubShow" : isPrjPubShow,// 群组外成员是否显示课件 [1=是 ， 0=否 ] 默认1
      "isPrjRefShow" : isPrjRefShow,// 群组外成员是否显示作业 [1=是 ， 0=否 ] 默认1
      'grpName' : grpName,
      'grpDescription' : grpDescription,
      'projectNo' : projectNo,
      'openType' : openType,
      'keywords' : encodeURIComponent(keywords),
      'firstCategoryId' : firstCategoryId,
      'secondCategoryId' : secondCategoryId
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          scmpublictoast(groupBase.opreateSuccess, 1000);
          setTimeout(function() {
            location.href = "/groupweb/grpinfo/main?des3GrpId=" + encodeURIComponent(des3GrpId);
          }, 1000)
        } else {
          scmpublictoast(groupBase.opreateFail, 2000);
        }
      });

    },
    error : function() {
      scmpublictoast(groupBase.opreateFail, 2000);
    }
  });
}

// 获取编辑群组的关键词
GrpBase.getGrpManageKeywords = function() {
  var arrKeywords = new Array();
  $.each($("#manage_keywords").find(".chip__text"), function(i, o) {
    arrKeywords.push($(o).text().replace(/;/g, "77&"));
  });
  return arrKeywords.join(";");
}
// 获取创建群组的关键词
GrpBase.getGrpCreateKeywords = function() {
  var arrKeywords = new Array();
  $.each($("#create_keywords").find(".chip__text"), function(i, o) {
    arrKeywords.push($(o).text().replace(/;/g, "77&"));
  });
  return arrKeywords.join(";");
}
// 创建群组字符限制
GrpBase.inputLimits = function() {
  $(".input_grpname_val").on("keydown", function(e) {
    if ($(this).val().length > 200) {
      $(this).val($(this).val().substring(0, 200));
    }
  });
  $(".input_grpdescription_val").on("keydown", function(e) {
    if ($(this).val().length > 500) {
      $(this).val($(this).val().substring(0, 500));
    }
  });
}
// 确定创建群组操作
GrpBase.doGrpBaseCreate = function(obj) {
  GrpBase.doHitMore(obj, 2000);
  var obj_ui = $("#create_grp_ui");
  var grpCategory = obj_ui.find(".class_create_grpCategory").attr("smate_grpCategory");
  var grpName = obj_ui.find(".input_grpname_val").val();
  if ($.trim(grpName) == "") {
    if ("11" == grpCategory) {
      scmpublictoast(groupBase.notPrjTitle, 2000);
    } else {
      scmpublictoast(groupBase.notCurName, 2000);
    }
    return 0;
  }

  var grpDescription = obj_ui.find(".input_grpdescription_val").val();
  if ($.trim(grpDescription) == "") {
    if ("11" == grpCategory) {
      scmpublictoast(groupBase.notPrjAbstract, 2000);
    } else {
      scmpublictoast(groupBase.notCurDesc, 2000);
    }
    return 0;
  }
  var projectNo = obj_ui.find(".input_projectno_val").val();
  // 计算长度不需要编码encodeURIComponent，去除encodeURIComponent(projectNo)
  if (projectNo != "" && projectNo.length > 100) {
    scmpublictoast(groupBase.proNoLimit, 2000);
    return;
  }
  var openType = obj_ui.find("input[name='choose-authority']:checked").val();
  var keywords = GrpBase.getGrpCreateKeywords();
  if (keywords.length > 640) {
    scmpublictoast(groupBase.keywordsLimit, 2000);
    return 0;
  }
  var firstCategoryId = $(".dev_sel__box[selector-id='1st_discipline']").attr("sel-value");
  var secondCategoryId = $(".dev_sel__box[selector-id='2nd_discipline']").attr("sel-value");
  if ($.trim(firstCategoryId) == "" || $.trim(secondCategoryId) == "") {
    scmpublictoast(groupBase.notPrjResearchArea, 2000);
    return 0;
  }
    var isIndexDiscussOpen = 1;
    var isIndexMemberOpen = 0;
    var isIndexPubOpen = 0;
    var isIndexFileOpen = 0;

    var isCurwareFileShow = 0;
    var isWorkFileShow = 0;
    var isPrjPubShow = 0;
    var isPrjRefShow = 0;
    var select = 0;
    //grpCategory == 11 项目群组
    if(hitOpneTypeCommen == ""){
        hitOpneTypeCommen = "H" ; //默认值
    }
    if (hitOpneTypeCommen == "O") {
        $.each($("#openType").find("input[type='checkbox']:checked"), function(i, o) {
            name = $(o).attr("name");
            if ('isIndexMemberOpen1' == name) {
                isIndexMemberOpen = 1;
                select ++ ;
            } else if ('isIndexPubOpen1' == name) {
                isIndexPubOpen = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isIndexFileOpen1' == name) {
                isIndexFileOpen = 1;
                if(grpCategory == "11")select ++ ;
            } else if ('isCurwareFileShow1' == name) {
                isCurwareFileShow = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isWorkFileShow1' == name) {
                isWorkFileShow = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isPrjPubShow1' == name) {
                if(grpCategory == "11")select ++ ;
                isPrjPubShow = 1;
            } else if ('isPrjRefShow1' == name) {
                if(grpCategory == "11")select ++ ;
                isPrjRefShow = 1;
            }
        });
        if(select<2){
            scmpublictoast(groupBase.selectOpenModuleTip, 2000);
            return 0;
        }
    }

    if (hitOpneTypeCommen == "H") {
        $.each($("#halfType").find("input[type='checkbox']:checked"), function(i, o) {
            name = $(o).attr("name");
            if ('isIndexMemberOpen2' == name) {
                isIndexMemberOpen = 1;
                select ++ ;
            } else if ('isIndexPubOpen2' == name) {
                isIndexPubOpen = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isIndexFileOpen2' == name) {
                isIndexFileOpen = 1;
                if(grpCategory == "11")select ++ ;
            } else if ('isCurwareFileShow2' == name) {
                isCurwareFileShow = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isWorkFileShow2' == name) {
                isWorkFileShow = 1;
                if(grpCategory == "10")select ++ ;
            } else if ('isPrjPubShow2' == name) {
                isPrjPubShow = 1;
                if(grpCategory == "11")select ++ ;
            } else if ('isPrjRefShow2' == name) {
                if(grpCategory == "11")select ++ ;
                isPrjRefShow = 1;
            }
        });
        if(select<2){
            scmpublictoast(groupBase.selectOpenModuleTip, 2000);
            return 0;
        }
    }
  // var disciplines = "";
  $.ajax({
    url : '/groupweb/mygrp/ajaxsavecreategrp',
    type : 'post',
    dataType : 'json',
    data : {
        "isIndexDiscussOpen" : isIndexDiscussOpen,// 主页是否显示群组动态 [1=是 ， 0=否 ]
        "isIndexMemberOpen" : isIndexMemberOpen,// 主页是否显示群组成员 [1=是 ， 0=否 ]
        "isIndexPubOpen" : isIndexPubOpen,// 主页是否显示群组成果 [1=是 ， 0=否 ]
        "isIndexFileOpen" : isIndexFileOpen,// 主页是否显示群组文件 [1=是 ， 0=否 ]
        "isCurwareFileShow" : isCurwareFileShow,// 群组外成员是否显示项目成果 [1=是 ， 0=否 ] 默认1
        "isWorkFileShow" : isWorkFileShow,// 群组外成员是否显示项目文献 [1=是 ， 0=否 ] 默认1
        "isPrjPubShow" : isPrjPubShow,// 群组外成员是否显示课件 [1=是 ， 0=否 ] 默认1
        "isPrjRefShow" : isPrjRefShow,// 群组外成员是否显示作业 [1=是 ， 0=否 ] 默认1
      'grpName' : grpName,
      'grpDescription' : grpDescription,
      'projectNo' : projectNo,
      'openType' : openType,
      'keywords' : encodeURIComponent(keywords),
      'grpCategory' : grpCategory,
      'firstCategoryId' : firstCategoryId,
      'secondCategoryId' : secondCategoryId
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          scmpublictoast(groupBase.opreateSuccess, 1000);
          location.href = "/groupweb/grpinfo/main?des3GrpId=" + data.des3GrpId;
        } else {
          scmpublictoast(data.msg, 2000);
        }
      });

    },
    error : function() {
      scmpublictoast(groupBase.opreateFail, 2000);
    }
  });
}
// 群组列表点击未处理事项 type 1=跳转到申请人列表 2=跳转到成员认领列表
GrpBase.toPendIngUI = function(type, des3GrpId) {

  if (type == 1) {
    location.href = "/groupweb/grpinfo/main?ispending=1&des3GrpId=" + encodeURIComponent(des3GrpId) + "&model=member";
  } else if (type == 2) {
    location.href = "/groupweb/grpinfo/main?ispending=2&des3GrpId=" + encodeURIComponent(des3GrpId) + "&model=pub";
  }
}

;
GrpBase.resetSecondDiscipline = function() {
  var $selectBox = document.querySelector('.sel__box[selector-id="2nd_discipline"]');
  if ($selectBox.style.visibility === "hidden") {
    $selectBox.style.visibility = "visible";
  } else {
    $selectValue = $selectBox.querySelector("span");
    $selectValue.textContent = groupBase.secCategory;
    $selectValue.classList.add("sel__value_placeholder");
    $selectBox.setAttribute("sel-value", "");
  }
}

;
GrpBase.sndDisciplieLoad = function() {
  var $selectBox = document.querySelector('.sel__box[selector-id="2nd_discipline"]');
  if ($selectBox != null && $selectBox.getAttribute("sel-value") == "") {
    $selectBox.style.visibility = "hidden";
    // $selectBox.setAttribute("visibility","hidden");
  }
}

GrpBase.getJSON = function() {
  var $valueBox = document.querySelector('.sel-dropdown__box[selector-data="1st_discipline"]');
  if (!$valueBox.querySelector('*[selected="selected"]')) {
    return {};
  } else {
    return {
      "fCategoryId" : $valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue")
    }
  }
}
GrpBase.jconfirm = function(myfunction, title) {
  $("#dev_jconfirm").find(".button_primary").unbind();
  $("#dev_jconfirm").find(".dialogs__modal_text").html(title);
  showDialog("dev_jconfirm_ui");
  $("#dev_jconfirm").find(".button_primary").bind("click", function() {
    myfunction();
  });
}

GrpBase.jconfirmDelGrpPsn = function(des3GrpId, obj) {

  GrpBase.jconfirm(function() {
    // 退出群组
    GrpMember.autoDelGrpPsn(des3GrpId, obj);
  }, groupBase.quitGroup);

}

GrpBase.jconfirmdelMyGrp = function(obj) {
  GrpBase.jconfirm(function() {
    // 删除群组
    GrpBase.delMyGrp(obj);
  }, groupBase.delGroup);

}

GrpBase.loadMyGrpSub = function(jumpto) {
  GrpManage.changeUrl("myGrp");
  $.ajax({
    url : '/groupweb/mygrp/ajaxmygrpsublist',
    type : 'post',
    dataType : 'html',
    data : {
      'jumpto' : jumpto
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_lists").html(data);
      });

    },
    error : function() {
    }
  });
}
// ---防止重复点击--zzx--
GrpBase.doHitMore = function(obj, time) {
  $(obj).attr("disabled", true);
  var click = $(obj).attr("onclick");
  if (click != null && click != "") {
    $(obj).attr("onclick", "");
  }
  setTimeout(function() {
    $(obj).removeAttr("disabled");
    if (click != null && click != "") {
      $(obj).attr("onclick", click);
    }
  }, time);
}
// 超时处理
GrpBase.ajaxTimeOut = function(data, myfunction) {
  var toConfirm = false;

  if ('{"ajaxSessionTimeOut":"yes"}' == data) {
    toConfirm = true;
  }
  if (!toConfirm && data != null) {
    toConfirm = data.ajaxSessionTimeOut;
  }
  if (toConfirm) {
    jConfirm(groupBase.timeOut, groupBase.tips, function(r) {
      if (r) {
        document.location.href = window.location.href;
        return 0;
      }
    });

  } else {
    if (typeof myfunction == "function") {
      myfunction();
    }
  }
}
GrpBase.grpEdit = function() {
  $.ajax({
    url : '/groupweb/mygrp/ajaxmanagegrp',
    type : 'post',
    dataType : 'html',
    data : {
      'des3GrpId' : $("#grp_params").attr("smate_des3_grp_id")
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#grp_main_box").html(data);
        addFormElementsEvents($("#grp_main_box")[0]);
        // Chipinputbox(0,10);
        // 加载推荐关键词
        GrpBase.loadRcmdKeyWorks();
      });

    },
    error : function() {
    }
  });
}

;
GrpBase.hideGrpMemberApply = function() {
  hideDialog("join_grp_invite_box");
  if ($(".nav_horiz > .nav__list > .nav__item").eq(0).hasClass("item_selected")) {
    GrpBase.showGrpList();
  }
  if ($(".dev_displaymodule").length > 0) {
    Rm.toModule();
  }
};

;
GrpBase.showGrpMemberApply = function() {
  // showDialog("join_grp_invite_box");

  GrpBase.getIviteGrpList();
};
GrpBase.changeGrpModule = function(obj, module) {
  if (module === "regGrp") {
    GrpBase.getIviteGrpList(module);
  } else {
    GrpBase.getIviteGrpList();
  }
}
GrpBase.getIviteGrpList = function(module) {
  // 默认群组邀请
  var url = "/groupweb/mygrp/ajaxhasivitegrplist";
  if (module != undefined && "regGrp" === module) {
    // 群组请求
    url = "/groupweb/mainmodule/ajaxgetgrpreq";
  }
  $("#has_ivite_grp_list").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
    status : 1
  });
  $.ajax({
    url : url,
    type : 'post',
    dataType : 'html',
    data : {
      'isAll' : 1
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        $("#has_ivite_grp_list").html(data);
        if ($("#has_ivite_grp_list").find(".main-list__item").length == 0) {
          var $parentNode = $("#has_ivite_grp_list");
          $parentNode.append("<div class='response_no-result'>未找到符合条件的记录</div>");
        }
      });
    },
    error : function() {
    }
  });
}
// 群组列表领域和关键词显示 ---zzx-----
;
GrpBase.showDisciplinesAndKeywordsForIviteGrpList = function() {
  var strS = "<div class='kw-chip_small'>";
  var strE = "</div>";
  $.each($("#has_ivite_grp_list").find(".kw__box"), function(i, o) {
    var str = "";
    $.each($(o).attr("smate_disciplines").split(";"), function(i, discipline) {
      if (discipline != '') {
        str = str + strS + discipline + strE;
      }
    });
    $.each($(o).attr("smate_keywords").split(";"), function(i, keyword) {
      if (keyword != '' && i < 5) {
        str = str + strS + keyword + strE;
      }
    });
    $(o).html(str);
  });
}
// 处理群组申请（全部同意） ---zzx-----
;
GrpBase.allIvitegrpApplication = function() {
  var targetdes3GrpId = "";
  $.each($("#has_ivite_grp_list").find(".main-list__item"), function(i, o) {
    targetdes3GrpId += $(o).attr("des3GrpId") + ",";
  });
  if (targetdes3GrpId != "") {
    GrpBase.ivitegrpApplication(1, targetdes3GrpId, GrpBase.hideGrpMemberApply);
    // 弹窗标识
    hasIvite = 0;
  }
}
// 处理群组申请（接受/忽略） disposeType 1=同意 ； 2=0 ---zzx-----
;
GrpBase.ivitegrpApplication = function(disposeType, targetdes3GrpId, myCallBack, obj) {
  // 超时处理

  if (obj != null) {
    $(obj).closest(".main-list__item").remove();
  }
  $.ajax({
    url : '/groupweb/grpmember/ajaxivitegrpapplication',
    type : 'post',
    dataType : 'json',
    data : {
      'targetdes3GrpId' : targetdes3GrpId,
      'disposeType' : disposeType
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if ($("#has_ivite_grp_list").find(".main-list__item").length == 0) {
          // 弹窗标识
          hasIvite = 0;
          $("#has_ivite_grp_list").append("<div class='response_no-result'>未找到符合条件的记录</div>");
          // GrpBase.hideGrpMemberApply();
        } else {

          myCallBack(disposeType, obj);
        }
        scmpublictoast(groupBase.opreateSuccess, 2000);
      });

    },
    error : function() {
      scmpublictoast(groupBase.opreateFail, 2000);
    }
  });
}
// 新开窗口打开群组详情----zzx-----
;
GrpBase.openGrpDetail = function(des3GrpId, event) {
  if (des3GrpId != null && des3GrpId != "") {
    window.open("/groupweb/grpinfo/main?des3GrpId=" + encodeURIComponent(des3GrpId));
    GrpMember.stopPropagation(event);
  }
};
GrpBase.saveGrpAvartars = function(grpAvartars) {
  var des3GrpId = $("#grp_params").attr("smate_des3_grp_id");
  $.ajax({
    url : '/groupweb/mygrp/ajaxsavegrpavartars',
    type : 'post',
    dataType : 'json',
    data : {
      'des3GrpId' : des3GrpId,
      'grpAvartars' : grpAvartars
    },
    success : function(data) {
      GrpBase.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          scmpublictoast(groupBase.opreateSuccess, 1000);
        } else {
          scmpublictoast(groupBase.opreateFail, 2000);
        }
      });
    },
    error : function() {
      scmpublictoast(groupBase.opreateFail, 2000);
    }
  });

}
// 群组的关键词推荐功能 begin
// 给群组名和群组简介绑定blur事件
GrpBase.bindKeyWordsRcmd = function() {
  $grpName = $(".input_grpname_val");
  $grpDesc = $(".input_grpdescription_val");
  $grpName.bind("blur", function() {
    GrpBase.loadRcmdKeyWorks();
  });
  $grpDesc.bind("blur", function() {
    GrpBase.loadRcmdKeyWorks();
  })
}
// 加载推荐关键词
GrpBase.loadRcmdKeyWorks = function() {
  var grpName = $(".input_grpname_val").val().trim();
  var grpDesc = $(".input_grpdescription_val").val().trim();
  var des3GrpId = $("#grp_params").attr("smate_des3_grp_id");
  $(".grp_rcmd_keywords").css("display", "none");// 默认隐藏
  if (grpName != '' || grpDesc != '') {
    $.ajax({
      url : '/groupweb/mygrp/ajaxLoadKeywords',
      type : 'post',
      dataType : 'json',
      data : {
        "groupName" : grpName,
        "groupDescription" : grpDesc,
        "des3GrpId" : des3GrpId
      },
      success : function(data) {
        if (data.mapKeyword.length > 0) {
          var listContent = GrpBase.displayKeywords(eval(data.mapKeywordZh), eval(data.mapKeywordEn));
          if (listContent) {
            var content = "<label class='input__title'>" + groupBase.suggestKeywords
                + "</label><div class='chip-panel__box inline-style'>";
            content += listContent;
            content += "</div><div class='global-hint-text'></div>";
            $('.grp_rcmd_keywords').html(content);
            $('.grp_rcmd_keywords').removeAttr("style").css("display", "block");
          }
        } else {
          $(".grp_rcmd_keywords").css("display", "none");
        }
      }
    });
  }
}
// 初始化系统推荐关键词 默认显示5个
GrpBase.displayKeywords = function(zhResult, enResult) {
  var containerZh = "", containerEn = "";
  var usedKeywords = [];
  $("#manage_keywords .chip__box").each(function() {
    var k_id = this.getAttribute("k_id");
    if (k_id != null && k_id != "") {
      usedKeywords[usedKeywords.length] = k_id;
    }
  });
  if (typeof zhResult !== 'undefined' && zhResult.length > 0) {
    // 过滤掉已选择的关键词
    if (usedKeywords.length > 0) {
      zhResult = GrpBase.delResult(usedKeywords, zhResult);
    }
    $.each(zhResult, function(i) {
      containerZh += GrpBase.createHtml(i, this, zhResult.length);
    });
    return containerZh;
  } else if (typeof enResult !== 'undefined' && enResult.length > 0) {
    // 过滤掉已选择的关键词
    if (usedKeywords.length > 0) {
      enResult = GrpBase.delResult(usedKeywords, enResult);
    }
    $.each(enResult, function(i) {
      containerEn += GrpBase.createHtml(i, this, enResult.length);
    });
    return containerEn;
  } else {
    return false;
  }
}
// 创建一个块
GrpBase.createHtml = function(id, obj, totalCount) {
  var array = new Array();
  array.push('<div class="chip__box white-style recommendKeywordItem" id="recommendKeywords_' + id + '" style="'
      + (id > 4 ? "display:none;" : "") + ' cursor: pointer;" onclick="javascript:GrpBase.addGrpKeywords(' + id + ','
      + obj.id + ',\'' + obj.keywords + '\',' + totalCount + ',$createGrpChipBox);">');
  array.push('<div class="chip__avatar"><img src="/img/avatar.jpg"></div>');
  array.push('<div class="chip__text">' + obj.keywords + '</div>');
  array.push('<div class="chip__icon icon_add"><i class="material-icons">add</i></div>');
  array.push('</div>');
  var result = array.join('');
  return result;
}
GrpBase.addGrpKeywords = function(index, id, text, totalCount, $createGrpChipBox) {
  var number = $(".chip-panel__stats").text();
  var numberArray = new Array();
  numberArray = number.split("/");
  if (parseInt(numberArray[0]) >= 10) {
    // 并且加上 超过后提示样式 SCM-16214
    $createGrpChipBox.mainbox.classList.add("count_exceed");
    setTimeout(function() {
      $(".chip-panel__box.count_exceed").removeClass("count_exceed");
    }, 1500);
    return;
  }
  var addIndex = "recommendKeywords_" + index;
  var lastIndex = "recommendKeywords_" + (totalCount - 1);
  $("#" + addIndex).remove();
  var strS = '<div class="chip__box" k_id="' + id
      + '" code><div class="chip__avatar"><img src="/img/avatar.jpg"></div><div class="chip__text">';
  var strE = '</div><div class="chip__icon icon_delete"><i class="material-icons">close</i></div>';
  var str = "";
  str = strS + text.trim() + strE;
  var boxkeywordsNumber = parseInt(numberArray[0]) + 1;
  $("#autokeywords").before(str);

  if ($("#" + lastIndex).css("display") == "none") {
    $keywordsList = $(".recommendKeywordItem");
    for (var i = 4; i < $keywordsList.length; i++) {
      if ($keywordsList.get(i).style.display == "none") {
        $keywordsList.get(i).style.display = "";
        break;
      }
    }
  }
  if ($(".recommendKeywordItem").size() <= 0) {
    $('.grp_rcmd_keywords').html("");
  }
  $createGrpChipBox.chipBoxInitialize();
}
GrpBase.delResult = function(usedKeywords, result) {
  for (var i = 0; i < usedKeywords.length; i++) {
    for (var j = 0; j < result.length; j++) {
      if (usedKeywords[i] == result[j].id) {
        result.splice(j--, 1);
      }
    }
  }
  return result;
}
// 群组的关键词推荐功能 end

// 站內跳转人员
GrpBase.toGrpMember = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  $("#member_main").click();
}
// 站內跳转成果
GrpBase.toGrpPub = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  // 项目群组跳成果
  $("#project_pub_main").click();
  // 课程群组跳文献
  $("#pub_main").click();
}

// 站外跳转人员
GrpBase.toOutsideGrpMember = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  $("#member_main").click();
}
// 站外跳转成果
GrpBase.toOutsideGrpPub = function(obj) {
  BaseUtils.doHitMore(obj, 1000);
  // 项目群组跳成果
  $("#project_pub_main").click();
  // 课程群组跳文献
  $("#pub_main").click();
}

GrpBase.closeGroupRcmdPub = function() {
  hideDialog("grp_pub_comfirm_box");
  if (model == "file" && $("#file_main").length > 0) {
    $("#file_main").click();
  } else if (model == "curware" && $("#curware_file_main").length > 0) {
    $("#curware_file_main").click();
  } else if (model == "work" && $("#work_file_main").length > 0) {
    $("#work_file_main").click();
  } else if (model == "member" && $("#member_main").length > 0) {
    $("#member_main").click();
  } else if (model == "pub" && $("#pub_main").length > 0) {
    $("#pub_main").click();
  } else if ((model == "projectPub" || model == "pub") && $("#project_pub_main").length > 0) {
    $("#project_pub_main").click();

  } else if (model == "projectRef" && $("#project_ref_main").length > 0) {
    $("#project_ref_main").click();

  } else if ($("#discuss_main").length > 0) {
    $("#discuss_main").click();
  }
};

// 不是群组的成果
GrpBase.rejectGrpPubRcmd = function(id) {
  // 先隐藏，再处理
  $("#dev_" + id).hide();
  $.ajax({
    url : '/groupweb/grppub/ajaxrejectpubrcmd',
    type : 'post',
    dataType : 'json',
    data : {
      'pubId' : id,
      'grpId' : $("#grp_params").attr("smate_grp_id")
    },
    success : function(data) {
      isTheLastPub(0);// 参数为0表示是忽略操作
    },
    error : function() {
      // scmpublictoast("网络异常",1000);
    }
  });
};

// 是群组的成果
GrpBase.acceptGrpPubRcmd = function(id) {
  // 先隐藏，再处理
  $("#dev_" + id).hide();
  $.ajax({
    url : '/groupweb/grppub/ajaxacceptpubrcmd',
    type : 'post',
    dataType : 'json',
    data : {
      'pubId' : id,
      'grpId' : $("#grp_params").attr("smate_grp_id")
    },
    success : function(data) {
      scmpublictoast(data.msg, 2000);
      isTheLastPub(1);// 参数为1表示为确认操作
      var pubId = data.newPubId;
      var grpPubCounts = data.grpPubCounts + "";
      var msgPub = data.msgPub + "";
      if (pubId != -1 && pubId != 0 && "undefined" != pubId && data.result != "not exist") {
        $('#grpinfo_sum_pub').html("");
        $('#grpinfo_sum_pub').html(grpPubCounts + '&nbsp;' + msgPub);
      }
    },
    error : function() {
      // scmpublictoast("网络异常",1000);
    }
  });
};

// 判断是否为最后一个待认领的成果，如果是，则关闭当前窗口,isConfirm表示当前是否是点击确认按钮操作（1），点击忽略按钮为0
function isTheLastPub(isConfirm) {
  var needConfirmPubNum = $("[dialog-id='grp_pub_comfirm_box']").find("div[id^='dev_']:visible").length;
  if (needConfirmPubNum == 0) {
    $("div[list-main='grouppubconfirm']").append("<div class='response_no-result'>未找到符合条件的记录</div>");
    // var $parentNode =
    // $("[dialog-id='grp_pub_comfirm_box']").find("div[id^='dev_']:visible").parent();
    // $parentNode.append("<div class='response_no-result'>未找到符合条件的记录</div>");
  } else if (isConfirm == 1) {// 认领成果才刷新，忽略则不刷新
    $("#project_pub_main").click();// 刷新当前群组已认领成果列表
  }
}

// 添加科技领域
GrpBase.showScienceAreaBox = function(areaSelect) {
  var areaIds = "";
  areaIds = $(".json_scienceArea_scienceAreaId:eq(0)").val() + "," + $(".json_scienceArea_scienceAreaId:eq(1)").val();
  $.ajax({
    url : "/groupweb/mygrp/ajaxaddsciencearea",
    type : "post",
    dataType : "html",
    data : {
      "scienceAreaIds" : areaIds
    },
    success : function(data) {
      $("#login_box_refresh_currentPage").val("false");// 登录不跳转
      BaseUtils.ajaxTimeOut(data, function() {
        $("#scienceAreaBox").html(data);
        showDialog("scienceAreaBox");
        addFormElementsEvents(document.getElementById("research-area-list"));
        $("#areaSelect").val(areaSelect);
        setTimeout(function() {
          var selValue = $("div[selector-id='2nd_discipline']").attr("sel-value");
          if (selValue != undefined) {
            selValue += "_status";
            $("#" + selValue).click();
            var level = selValue.substring(0,1);
              $("#" + level+"_level").click();
          }

        }, 500);
      });
    },
    error : function() {
    }
  });
};
GrpBase.dealAreaSaveBtnStatus = function() {
  var sum = $("#choosed_area_list").find(".main-list__item").length;
  if (sum == 0) {
    $("#homepage_area_save_btn").attr("disabled", "true");
  } else {
    $("#homepage_area_save_btn").removeAttr("disabled");
  }
};
GrpBase.addScienceArea = function(id, obj) {
  var key = $("#" + id + "_category_title").html();
  var firstAreaName = $(obj).closest(".nav-cascade__section").find(".nav-cascade__title").html();
  var sum = $("#choosed_area_list").find(".main-list__item").length;
  if (sum < 1) {
    var html = '<div class="main-list__item" style="padding: 0px 16px!important;" ' + 'firstAreaName=' + firstAreaName
        + '  areaid = "' + id + '" >' + '<div class="main-list__item_content">' + key + '</div>'
        + '<div class="main-list__item_actions"  onclick="javascript:GrpBase.delScienceArea(\'' + id
        + '\');"><i class="material-icons">close</i></div></div>';
    $("#choosed_area_list").append(html);
    $("#unchecked_area_" + id).attr("onclick", "");
    $("#unchecked_area_" + id).attr("id", "checked_area_" + id);
    $("#" + id + "_status").html("check");
    $("#" + id + "_status").css("color", "forestgreen");
  } else {
    // 出提示语
    scmpublictoast("最多可以选择1个科技领域", 1500);
  }
  GrpBase.dealAreaSaveBtnStatus();
};
GrpBase.delScienceArea = function(id) {
  $("#choosed_area_list").find(".main-list__item[areaid='" + id + "']").remove();
  $("#checked_area_" + id).attr("onclick", "GrpBase.addScienceArea('" + id + "')");
  $("#checked_area_" + id).attr("id", "unchecked_area_" + id);
  $("#" + id + "_status").html("add");
  $("#" + id + "_status").css("color", "");
  GrpBase.dealAreaSaveBtnStatus();
};
// 保存科技领域
GrpBase.saveScienceArea = function() {

  setTimeout(function() {
    var $item = $("#choosed_area_list").find(".main-list__item");
    if ($item.length == 1) {
      var firstareaname = $item.attr("firstareaname");
      var secareaname = $item.find(".main-list__item_content").html();
      var sectareaid = $item.attr("areaid");
      var firstareaid = sectareaid.substring(0, 1);
      $(".dev_sel__box[selector-id='1st_discipline']").attr("sel-value", firstareaid);
      $(".dev_sel__box[selector-id='1st_discipline']").find(".sel__value_selected").html(firstareaname);

      $(".dev_sel__box[selector-id='2nd_discipline']").attr("sel-value", sectareaid);
      $(".dev_sel__box[selector-id='2nd_discipline']").css("visibility", "");
      $(".dev_sel__box[selector-id='2nd_discipline']").find(".sel__value_selected").html(secareaname);
    }
    // $("#scienceAreaId").val(keyName.replace(/,\s$/, ""));
  }, 100);
  hideDialog("scienceAreaBox");
};
// 隐藏
GrpBase.hideScienceAreaBox = function(obj) {
  hideDialog("scienceAreaBox");
};
