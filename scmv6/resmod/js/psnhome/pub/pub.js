/**
 * 个人主页-成果模块 相关js
 */
var Pub = Pub ? Pub : {};

// 微信分享
function getQrImg(url) {
  if (navigator.userAgent.indexOf("MSIE") > 0) {
    $("#share-qr-img").qrcode({
      render : "table",
      width : 175,
      height : 175,
      text : url
    });
  } else {
    $("#share-qr-img").qrcode({
      render : "canvas",
      width : 175,
      height : 175,
      text : url
    });
  }
};

// 改变url
Pub.changeUrl = function(targetModule) {
  var json = {};
  var oldUrl = window.location.href;
  var index = oldUrl.lastIndexOf("module");
  var newUrl = window.location.href;
  if (targetModule != undefined && targetModule != "") {
    if (index < 0) {
      if (oldUrl.lastIndexOf("?") > 0) {
        newUrl = oldUrl + "&module=" + targetModule;
      } else {
        newUrl = oldUrl + "?module=" + targetModule;
      }
    } else {
      if (oldUrl.indexOf("needresetpwd=true") < 1) {
        newUrl = oldUrl.substring(0, index) + "module=" + targetModule;
      }
    }
  }
  window.history.replaceState(json, "", newUrl);
};

// 成果模块
Pub.main = function() {
  Pub.changeUrl("pub");
  $('.dev_psnhome_back').remove();
  $.ajax({
    url : '/pub/psn/ajaxmain',
    type : 'post',
    dataType : 'html',
    data : {
      "des3PsnId" : $("#des3PsnId").val(),
      "run_env" : "test"
    },
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if ($(".container__horiz").length > 0) {
          $(".container__horiz").replaceWith(data);
        } else {
          $(".dev_select_tab").replaceWith(data); // container__horiz带有样式，影响力用dev_select_tab这个
        }
      });
      if (typeof (showDialogPub) != "undefined" && showDialogPub == "add") {// 打开导入成果框
        Pub.addPub();
      }
      if (typeof (showDialogPub) != "undefined" && showDialogPub == "showPubConfirmMore") {// 打开成果确认框
        // 延迟1秒等待成果列表加载完
        showDialogPub = undefined;
        setTimeout(Pub.pubConfirmAll, 1000);
      }
      if (RepeatPub != null) {
        RepeatPub.ajaxshowrepeatpubmain();
      }
      if (typeof opttype != "undefined" && opttype == "addpub") {
        Pub.addPub();
      }
    },
    error : function(status) {
    }
  });
};

// 未上传全文数目提示
Pub.nofulltextTips = function() {
  $.ajax({
    url : '/pubweb/publication/ajaxnofulltextcount',
    type : 'post',
    dataType : 'html',
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      $(".dev_nofulltext_count").html("");
      $(".dev_nofulltext_count").append(data);
    }
  });
};

// 成果列表
Pub.pubList = function() {
  // 编辑成果 2018-11-27
  var isEditPubFlag = false;
  if (typeof isEditPub != "undefined" && isEditPub == "true") {
    isEditPub = false;
    isEditPubFlag = true;
    // $("#sortBy").val("updateDate");
    // console.log("isEditPub");
  }
  // 邮件跳转过来的成果列表
  if ($("input[name='searchKey']").val() != null && $("input[name='searchKey']").val() != "") {
    window.Mainlist({
      name : "psnpub",
      listurl : "/pubweb/publication/ajaxpublist",
      listdata : {
        "des3PsnId" : $("#des3PsnId").val(),
        "searchKey" : $("input[name='searchKey']").val(),
        'isPsnPubs' : '1'
      },
      listcallback : function(xhr) {
      },
      statsurl : "/pubweb/outside/ajaxpublistcallback"
    });
    $("input[name='searchKey']").val("");
  }
  // 成果列表
  if ($("#sortBy").val() == "updateDate") {
    $("div[filter-section='orderBy']").attr("init", "ignore");
    $("div[filter-section='orderBy'] li").each(function() {
      if ($(this).attr("filter-value") == "updateDate") {
        $("div.sort-container_header-title").text($(this).find(".sort-container_item_name").text());
        this.classList.add("option_selected");
      } else {
        this.classList.remove("option_selected");
      }
    });
  }
  if ($("#sortBy").val() == "citedTimes") {
    $("div[filter-section='orderBy']").attr("init", "ignore");
    $("div[filter-section='orderBy'] li").each(function() {
      if ($(this).attr("filter-value") == "citedTimes") {
        $("div.sort-container_header-title").text($(this).find(".sort-container_item_name").text());
        this.classList.add("option_selected");
      } else {
        this.classList.remove("option_selected");
      }
    });
  }
  if ($("#frompage").val() == "edit") {
    $("div[list-main='psnpub']").attr("memlist", "display");
  }
  $pubList = window.Mainlist({
    name : "psnpub",
    listurl : "/pub/psn/ajaxlist",
    listdata : {
      "des3SearchPsnId" : $("#des3PsnId").val(),
      "editPubFlag" : isEditPubFlag,
      'isPsnPubs' : '1'
    },
    drawermethodsmaskback : function() {
      $("div[list-drawer='psnpub']").find(".main-list__item").find(".main-list__item_actions").remove();
    },
    listcallback : function(xhr) {
      // 从编辑页面过来需要初始化
      Pub.initFilterValue();

      Pub.pubListInitMethod();// 返回成果列表的初始化方法
      // 加载上传全文的展示框
      Pub.loadPubFulltext();
      // 遍历 给所有checkbox 给批量选择框绑定事件-同步上传文件问题
      Pub.bindASyncUpload();
      // 过滤成果类型
      setTimeout(Pub.filterPubType, 1000);

    },
    more : true,
    statsurl : "/pub/query/ajaxpsnpubcount",
    drawermethods : locale == "zh_CN" ? {
      "导出成果" : function(array) {
          if(document.getElementsByClassName("drawer-batch__layer").length > 0){
              document.getElementsByClassName("drawer-batch__layer")[0].style.display = "flex";
          }
      }
    } : {
      "Export" : function(array) {
          if(document.getElementsByClassName("drawer-batch__layer").length > 0){
              document.getElementsByClassName("drawer-batch__layer")[0].style.display = "flex";
          }
      }
    },
    exportmethods : function(array,exportType){
      if (array.length > 0) {
        var pubIds = array.join(",");
        Pub.pubExport(pubIds, exportType, "common", 1);
      }  
    }
  });
};
Pub.initFilterValue = function() {
  // 排序条件的header显示，是gzl另外实现的，所以在回调这实现即可。 获取选中的排序，更新到显示
  $("li.sort-container_item-list").each(function(x) {
    if (this.classList.contains("option_selected")) {
      var sortName = $(this).find(".sort-container_item_name").text();
      $("div.sort-container_header-title").text(sortName);
    }
  });
  // 初始化过滤条件是否展开
  var targetelem = document.getElementsByClassName("filter-section__toggle");
  for (var i = 0; i < targetelem.length; i++) {
    // 查看是否有选中的过滤条件，有则展开
    var selected_option = targetelem[i].closest(".filter-list__section").querySelector(".option_selected");
    if (selected_option != null) {
      if (targetelem[i].innerHTML === "expand_more") {
        targetelem[i].innerHTML = "expand_less";
        targetelem[i].closest(".filter-list__section").querySelector(".filter-value__list").style.display = "block";
      }
    }
    // 初始化展开项
    targetelem[i].closest(".filter-list__section").querySelector(".filter-value__list").style.display = targetelem[i].innerHTML == "expand_more"
        ? "none"
        : "block";
  }
}
// 过滤成果类型
Pub.filterPubType = function() {
  if ($.trim(filterPubType) != "" && filterPubType > 0) {
    var filter = $(".filter-value__item[filter-value='5']");
    $(".filter-value__item[filter-value='" + filterPubType + "']").find(".filter-value__option").click();
    // 重置数据
    filterPubType = "";
  }
}

// checkbox选中时，给批量选择框绑定事件-同步上传文件
Pub.bindASyncUpload = function() {
  var $drawerBatchBox = $(".drawer-batch__box");
  $drawerBatchBox.on("click", ".pub_uploadFulltext", function() {
    $this = $(this);
    /*
     * var dataJson = {
     * allowType:'*.txt;*.jpg;*.jpeg;*.gif;*.pdf;*.doc;*.docx;*.png;*.html;*.xhtml;*.htm;*.rar;*.zip;', };
     */
    // 20181213 SCM-21660 增加批量操作成果界面中上传全文的文件类型：.xls和.xlsx
    var filetype = ".txt;.jpg;.jpeg;.gif;.pdf;.doc;.docx;.png;.html;.xhtml;.htm;.rar;.zip;.xls;.xlsx;".split(";");
    var data = {
      "fileurl" : "/fileweb/fileupload",
      "filedata" : {
        fileDealType : "generalfile"
      },
      "method" : "nostatus",
      "filecallback" : Pub.reloadFulltext,
      "filecallbackparam" : {
        pubId : $this.attr("des3id")
      },
      "filetype" : filetype
    };
    fileUploadModule.initialization(this, data);
  });
}
// 导出成果
Pub.pubExport = function(pubIds, exportType, exportScope, articleType) {
  BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function() {
    if (exportType && exportType != "") {
          window.location.href = "/pub/opt/ajaxpubexport?pubIds=" + pubIds + "&exportType=" + exportType + "&exportScope="
        + exportScope + "&articleType=" + articleType;
       document.getElementsByClassName("drawer-batch__layer")[0].style.display = "none";//隐藏导出框
    }else{
      scmpublictoast("请选择成果文件导出格式", 1000);
    }
  }, 1);
};

// 返回成果列表的初始化方法
Pub.pubListInitMethod = function() {
  setTimeout(function() {
    if (jumpto == "pubfulltextall") {
      Pub.pubftConfirmAll();
      jumpto = "";// 清空
    }
    if (jumpto == "puball") {
      Pub.pubConfirmAll();
      jumpto = "";// 清空
    }
    if (jumpto == "pubCooperatorAll") {
      Pub.pubCooperatorAll();
      jumpto = "";// 清空
    }
  }, 500);
};

// 成果转化
Pub.pubConvert = function() {
  $.ajax({
    url : '/pubweb/publication/ajaxgetpatent',
    type : 'post',
    dataType : 'html',
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      $(".dev_pub_patent").replaceWith(data);
    }
  });
};

// 成果合作者
Pub.cooperator = function() {
  $("#pub_cooper_loading").show();
  $.ajax({
    url : '/psnweb/cooperation/ajaxpubcooperator',
    type : 'post',
    dataType : 'html',
    data : {
      "des3CurrentId" : $("#des3PsnId").val()
    },
    success : function(data) {
      $('.dev_pub_cooperator').html("");
      $(".dev_pub_cooperator").append(data);
    }
  });
};

// 查看他人个人主页
Pub.lookOtherPsnHome = function(des3PsnId) {
  window.open("/psnweb/homepage/show?des3PsnId=" + des3PsnId, 'target', '');
  // window.location.href = "/psnweb/homepage/show?des3PsnId="+des3PsnId;
};
// v8成果详情
Pub.newPubDetail = function(des3PubId) {
  var pram = {
    "des3PubId" : des3PubId
  };
  var newwindow = window.open("about:blank");
  $.ajax({
    url : "/pub/details/ajaxview",
    type : "post",
    data : pram,
    dataType : "json",
    success : function(data) {
      if (data.result == 2) {
        newwindow.location.href = data.shortUrl;
      } else {
        newwindow.location.href = "/pub/details?des3PubId=" + encodeURIComponent(des3PubId);
      }
    },
    error : function(data) {
      console.log(data);
    }
  });
};
// v8基准库成果详情
Pub.newPubPdwhDetail = function(des3PubId) {
  var pram = {
    "des3PubId" : des3PubId
  };
  var newwindow = window.open("about:blank");
  $.ajax({
    url : "/pub/details/ajaxpdwhview",
    type : "post",
    data : pram,
    dataType : "json",
    success : function(data) {
      if (data.result == 2) {
        newwindow.location.href = data.shortUrl;
      } else {
        newwindow.location.href = "/pub/details/pdwh?des3PubId=" + encodeURIComponent(des3PubId);
      }
    },
    error : function() {
    }
  });
};
// 成果详情
Pub.pubDetail = function(des3Id) {
  var pram = {
    des3Id : des3Id
  };
  var params = "des3Id=" + encodeURIComponent(des3Id);
  var newwindow = window.open("about:blank");
  $.ajax({
    url : "/pubweb/publication/ajaxview",
    type : "post",
    data : pram,
    dataType : "json",
    success : function(data) {
      if (data.result == 2) {
        newwindow.location.href = data.shortUrl;
      }
      if (data.result == 1 || data.ajaxSessionTimeOut == 'yes') {
        newwindow.location.href = "/pubweb/details/show?" + params + "&currentDomain=/pubweb&pubFlag=1";
      } else if (data.result == 0) {
        newwindow.location.href = "/pubweb/publication/wait?" + params;
      }
    },
    error : function() {
    }
  });
};
// 基准库成果详情短地址
Pub.pubPdwhDetail = function(des3Id, dbid) {
  var pram = {
    des3Id : des3Id
  };
  var params = "des3Id=" + encodeURIComponent(des3Id);
  var newwindow = window.open("about:blank");
  $.ajax({
    url : "/pubweb/publication/ajaxpdwhview",
    type : "post",
    data : pram,
    dataType : "json",
    success : function(data) {
      if (data.result == 2) {
        newwindow.location.href = data.shortUrl;
      } else {
        window.open("/pubweb/details/showpdwh?des3Id=" + encodeURIComponent(des3Id) + "&dbid=" + dbid)
      }
    },
    error : function() {
    }
  });
};
// 认领成果详情
Pub.rcmdDetail = function(des3RolPubId, insId) {
  var viewPubDetailUrl = "";
  $.ajax({
    url : snsctx + "/pubrolurl/ajaxPubViewUrl",
    type : "post",
    data : {
      "des3Id" : des3RolPubId,
      "insId" : insId
    },
    dataType : "json",
    async : false,
    success : function(data) {
      if (data.result == "success") {
        viewPubDetailUrl = data.viewUrl;
      }
    },
    error : function() {
    }
  });
  if (viewPubDetailUrl != "") {
    Pub.forceOpen(viewPubDetailUrl);
  }
  Pub.stopBubble();
};

Pub.forceOpen = function(url) {
  var a = document.getElementById("open_linkxx");
  if (a == null || typeof (a) == "undefined") {
    a = document.createElement("a");
    a.setAttribute("target", "_blank");
    a.setAttribute("href", url);
    a.setAttribute("id", "open_linkxx");
    a.setAttribute("name", "open_linkxx");
    document.body.insertBefore(a, document.body.childNodes[0]);
  } else {
    a.setAttribute("href", url);
  }
  if (document.all) {
    a.click();
  } else {
    var evt = document.createEvent("MouseEvents");
    evt.initEvent("click", true, true);
    a.dispatchEvent(evt);
  }
};

// 成果认领的全文下载
Pub.rcmdFulltextDownload = function(fdesId) {
  window.location.href = domainrol + "/scmwebrol/archiveFiles/fileDownload?fdesId=" + fdesId;
};

// 成果编辑
Pub.pubEdit = function(des3PubId) {
  BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function() {
    // 保存过滤条件
    $pubList.setCookieValues();
    var forwardUrl = "/pub/enter?des3PubId=" + encodeURIComponent(des3PubId);
    BaseUtils.forwardUrlRefer(true, forwardUrl);
  }, 1);
};
// 进入成果编辑页面
/*
 * Pub.enterEdit = function(forwardUrl){ if(forwardUrl==""){ return; } var data =
 * {"forwardUrl":forwardUrl}; $.ajax({ url : '/pub/ajaxforwardUrlRefer', type : 'post',
 * dataType:'json', data : data, success : function(data) { Pub.ajaxTimeOut(data,function(){
 * window.location.href=data.forwardUrl; }); }, }); };
 */
// 成果删除
Pub.pubDel = function(des3PubId, pubIndex) {
  BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function() {
    smate.showTips._showNewTips(pubi18n.i18n_delete_content, pubi18n.i18n_delete_title, "doDel('" + des3PubId + "','"
        + pubIndex + "')", "", pubi18n.i18n_choose_confirm_btn, pubi18n.i18n_choose_cancel_btn);
  }, 1);
};

function closeDiv() {
  var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
  if($(".new-searchplugin_container")){
      $(".background-cover").remove();
      $(".new-searchplugin_container").remove();
   }
}
// 成果引用
Pub.pubCite = function(obj) {
  BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function() {
    var pubMainList_isAnyUser = $(obj).attr("pubMainListIsAnyUser");
    var des3PubId = $(obj).attr("resid");
    if (pubMainList_isAnyUser != undefined) {
      var type = $(obj).attr("type").trim();
      var owner = $(obj).attr("owner").trim();
      if (pubMainList_isAnyUser == 4) {
        if (owner == "true" || owner == "yes" || owner == "1") {
          smate.showTips._showNewTips(pubi18n.i18n_pub_share_content, pubi18n.i18n_delete_title, "closeDiv()", "",
              pubi18n.i18n_choose_close_btn, pubi18n.i18n_choose_cancel_btn);
        } else {
          smate.showTips._showNewTips(pubi18n.i18n_pub_not_owener_share_content, pubi18n.i18n_delete_title,
              "closeDiv()", "", pubi18n.i18n_choose_close_btn, pubi18n.i18n_choose_cancel_btn);
        }
        $("#alert_box_cancel_btn").hide();
        $(".alerts_confirm_btn").attr("des3PubId", des3PubId);
      } else {
        if (type == "detail") {
          SmateShare.getPubDetailsSareParam(obj);
        } else {
          SmateShare.getPsnPubListSareParam(obj);
        }
        initSharePlugin(obj);
      }
    } else {// 论文收藏出调用
      $.ajax({
        url : "/pub/ajaxCheckSharePubPermissions",
        type : "post",
        dataType : "json",
        data : {
          "des3PubId" : des3PubId,
        },
        success : function(data) {
          if (data.result) {
            SmateShare.getPubDetailsSareParam(obj);
            initSharePlugin(obj);
          } else {
            if (data.isOwner) {
              smate.showTips._showNewTips(pubi18n.i18n_pub_share_content, pubi18n.i18n_delete_title, "closeDiv()", "",
                  pubi18n.i18n_choose_close_btn, pubi18n.i18n_choose_cancel_btn);
            } else {
              smate.showTips._showNewTips(pubi18n.i18n_pub_not_owener_share_content, pubi18n.i18n_delete_title,
                  "closeDiv()", "", pubi18n.i18n_choose_close_btn, pubi18n.i18n_choose_cancel_btn);
            }
            $("#alert_box_cancel_btn").hide();
            $(".alerts_confirm_btn").attr("des3PubId", des3PubId);
          }
        },
        error : function() {
        }
      });
    }
  }, 1);
}
function smateToShare(obj) {
  var des3PubId = $(obj).attr("des3PubId");
  var obj = $("li[resid='" + des3PubId + "']");
  SmateShare.getPsnPubListSareParam(obj);
  initSharePlugin(obj);
  doClosePubCiteWindow();
}
// 关闭弹窗
function doClosePubCiteWindow() {
  $("#smate_alert_tips_div").hide();
}

function doDel(des3PubId, pubIndex) {
  var post_data = {
    "des3PubIds" : des3PubId
  };
  $.ajax({
    url : '/pub/opt/delete',
    type : 'post',
    dataType : 'json',
    data : post_data,
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          if (pubIndex == "batch") {
            scmpublictoast(pubi18n.i18n_delete_pub1 + data.count + pubi18n.i18n_delete_pub2, 1000);
            $pubList.reloadCurrentPage();
            $pubList.drawerRemoveSelected(data.delDes3PubIds);
            var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
            $(".background-cover").remove();
            if($(".new-searchplugin_container")){
               $(".new-searchplugin_container").remove();
            }
            
            // Pub.nofulltextTips();
          } else {
            $(".dev_pub_del_" + pubIndex).remove();
            scmpublictoast(data.msg, 1000);
            $pubList.reloadCurrentPage();
            $pubList.drawerRemoveSelected(data.delDes3PubIds);
            var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
            $(".background-cover").remove();
            if($(".new-searchplugin_container")){
                $(".new-searchplugin_container").remove();
             }
             
            // Pub.nofulltextTips();
          }
        } else if (data.result == "warn") {
          scmpublictoast(data.msg, 1000);
        } else if (data.result == "exist") {
          scmpublictoast(data.msg, 1000);
        } else {
          scmpublictoast(data.msg, 1000);
        }
      });
    },
  });
}
// 成果赞操作
Pub.pubAward = function(des3PubId, obj) {
  var influenceObj = obj;
  var post_data = {
    "des3PubId" : des3PubId
  };
  var isAward = $(obj).attr("isAward");
  if (isAward == '1') {
    $(obj).attr("isAward", 0);
    post_data.operate = '0';// 取消赞
  } else {
    $(obj).attr("isAward", 1);
    post_data.operate = '1';// 点赞
  }
  if (!$(obj).hasClass('new-Standard_Function-bar_item')) {
    obj = $(obj).find('.new-Standard_Function-bar_item:first');
  }
  $(obj).attr("disabled", "true");
  var click = $(obj).attr("onclick");
  $(obj).attr("onclick", "");
  var awardText = $(obj).text();
  var awardCount = 0;
  $.ajax({
    url : '/pub/opt/ajaxlike',
    type : 'post',
    dataType : 'json',
    data : post_data,
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          awardCount = data.awardTimes;
          var style = $(obj).parent().find('.dev_pubdetails_award_style:first');
          if (typeof awardCallback == "function") {
            awardCallback(influenceObj, isAward, awardCount);
          }
          if (isAward == 1) {// 由取消赞变成赞

            // 移动端 字体和图标样式
            $(obj).removeClass("paper_footer-tool__box-click");
            $(obj).parent().find("i").removeClass("container_fuc-fabulous__flag").addClass("paper_footer-fabulous");
            // =================================个人成果详情里的赞样式改变_start
            if ($(style).hasClass("spirit-icon__thumbup")) {
              $(style).removeClass("spirit-icon__thumbup");
              $(style).addClass("spirit-icon__thumbup-outline");
            }
            // =================================个人成果详情里的赞样式改变_end
            // ===个人影响力-Hindex推荐成果列表的赞样式_start
            $(influenceObj).removeClass("fabulous_icon-checked");
            // ===个人影响力-Hindex推荐成果列表的赞样式_end
            if (awardCount == 0) {
              $(influenceObj).next(".dev_pub_award_item").text(pubi18n.i18n_like);
              $(obj).find(".new-Standard_Function-bar_item-title").text(pubi18n.i18n_like);
            } else {
              if (awardCount >= 1000) {
                $(influenceObj).next(".dev_pub_award_item").text(pubi18n.i18n_like + "(1K+)");
                $(obj).find(".new-Standard_Function-bar_item-title").text(pubi18n.i18n_like + "(1K+)");
              } else {
                $(influenceObj).next(".dev_pub_award_item").text(pubi18n.i18n_like + "(" + awardCount + ")");
                $(obj).find(".new-Standard_Function-bar_item-title").text(pubi18n.i18n_like + "(" + awardCount + ")");
              }
            }
            // 我的成果
            if ($(obj).hasClass("new-Standard_Function-bar_item")) {
              $(obj).removeClass("new-Standard_Function-bar_selected");
            }
          } else {// 由赞变成取消赞
            $(obj).addClass("dev_cancel_award paper_footer-tool__box-click");
            // 移动端 字体和图标样式
            $(obj).addClass("paper_footer-tool__box-click");
            $(obj).parent().find("i").addClass("container_fuc-fabulous__flag");
            // =================================个人成果详情里的赞样式改变_start
            if ($(style).hasClass("spirit-icon__thumbup-outline")) {
              $(style).removeClass("spirit-icon__thumbup-outline");
              $(style).addClass("spirit-icon__thumbup");
            }
            // =================================个人成果详情里的赞样式改变_end
            // ===个人影响力-Hindex推荐成果列表的赞样式_start
            $(influenceObj).addClass("fabulous_icon-checked");
            // ===个人影响力-Hindex推荐成果列表的赞样式_end
            if (awardCount >= 1000) {
              $(influenceObj).next(".dev_pub_award_item").text(pubi18n.i18n_unlike + "(1K+)");
              $(obj).find(".new-Standard_Function-bar_item-title").text(pubi18n.i18n_unlike + "(1K+)");
            } else {
              $(influenceObj).next(".dev_pub_award_item").text(pubi18n.i18n_unlike + "(" + awardCount + ")");
              $(obj).find(".new-Standard_Function-bar_item-title").text(pubi18n.i18n_unlike + "(" + awardCount + ")");
            }
            // 我的成果
            if ($(obj).hasClass("new-Standard_Function-bar_item")) {
              $(obj).addClass("new-Standard_Function-bar_selected");
            }
          }
          $(obj).removeAttr("disabled");
          $(obj).attr("onclick", click);
        }
      });
    },
  });
};
// 个人库成果赞
Pub.psnPubAward = function(des3PubId, obj) {
  BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function() {
    var post_data = {
      "des3PubId" : des3PubId
    };
    var isAward = $(obj).attr("isAward");
    if (isAward == '1') {
      $(obj).attr("isAward", 0);
      post_data.operate = '0';// 取消赞
    } else {
      $(obj).attr("isAward", 1);
      post_data.operate = '1';// 点赞
    }
    // 点赞数
    var awardCount = 0;
    $.ajax({
      url : '/pub/opt/ajaxlike',
      type : 'post',
      dataType : 'json',
      data : post_data,
      success : function(data) {
        if (data.result == "success") {
          awardCount = data.awardTimes;
          if (isAward == 1) {// 由取消赞变成赞
            if ($(obj).find("i").hasClass("icon-praise-award")) {
              $(obj).find("i").attr("class", "icon-praise");
              $(obj).find("span").text(pubi18n.i18n_like);
              if (awardCount > 0) {
                if (awardCount > 999) {
                  $(obj).find("span").text(pubi18n.i18n_like + "(1K+)");
                } else {
                  $(obj).find("span").text(pubi18n.i18n_like + "(" + awardCount + ")");
                }
              } else {
                $(obj).find("span").text(pubi18n.i18n_like);
              }
            }
          } else {
            if ($(obj).find("i").hasClass("icon-praise")) {
              $(obj).find("i").attr("class", "icon-praise-award");
              if (awardCount > 999) {
                $(obj).find("span").text(pubi18n.i18n_unlike + "(1K+)");
              } else {
                $(obj).find("span").text(pubi18n.i18n_unlike + "(" + awardCount + ")");
              }
            }
          }
        }
      }
    });
  }, 1);
}

// 移动端成果赞操作
Pub.mobilePubAward = function(resType, resNode, des3ResId, obj) {
  var awardCount = 0;
  var post_data = {
    "resType" : resType,
    "resNode" : resNode,
    "des3ResId" : des3ResId
  };
  var isAward = $(obj).attr("isAward");
  if (isAward == '1') {
    post_data.status = '0';// 取消赞
  } else {
    post_data.status = '1';// 点赞
  }
  $.ajax({
    url : '/pubweb/pubopt/ajaxaward',
    type : 'post',
    dataType : 'json',
    data : post_data,
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          awardCount = data.awardTimes;
          if (isAward == 1) {// 取消赞
            $(obj).attr("isAward", 0);
            $(obj).find('.paper_footer-tool:first').removeClass("paper_footer-award_unlike");
            $(obj).find('.paper_footer-tool:first').addClass("paper_footer-fabulous");
            // $(obj).find('.dev_pub_award:first').removeClass("paper_footer-tool__box-click");
            if (awardCount == 0) {
              $(obj).find('.dev_pub_award:first').text(pubi18n.i18n_like);
            } else {
              $(obj).find('.dev_pub_award:first').text(pubi18n.i18n_like + "(" + awardCount + ")");
            }
          } else {// 赞
            $(obj).attr("isAward", 1);
            $(obj).find('.paper_footer-tool:first').removeClass("paper_footer-fabulous");
            $(obj).find('.paper_footer-tool:first').addClass("paper_footer-award_unlike");
            $(obj).find('.dev_pub_award:first').text(pubi18n.i18n_unlike + "(" + awardCount + ")");
            // $(obj).find('.dev_pub_award:first').removeClass("paper_footer-tool__box-click");
            // $(obj).find('.dev_pub_award:first').addClass("paper_footer-tool__box-click");
          }
        }
      });
    },
  });
};
// 检查基准库成果是否存在
Pub.pdwhIsExist = function(des3PubId, func) {
  $.ajax({
    url : "/pub/opt/ajaxpdwhisexists",
    type : 'post',
    dataType : "json",
    async : false,
    data : {
      "des3PubId" : des3PubId
    },
    timeout : 10000,
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          func();
        } else {
          scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
        }
      });
    },
    error : function(data) {
      scmpublictoast("系统出错", 1000);
    }
  });
}
// 检查基准库成果是否存在
Pub.pdwhIsExist2 = function(des3PubId, func) {
  $.ajax({
    url : "/pub/outside/ajaxpdwhisexists",
    type : 'post',
    dataType : "json",
    async : false,
    data : {
      "des3PubId" : des3PubId
    },
    timeout : 10000,
    success : function(data) {
      if (data.result == 'success') {
        func();
      } else {
        scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
      }
    },
    error : function(data) {
      scmpublictoast("系统出错", 1000);
    }
  });
}

Pub.showPdwhQuote = function(url, des3PubId, obj) {
  $.ajax({
    url : "/pub/outside/ajaxpdwhisexists",
    type : 'post',
    dataType : "json",
    async : false,
    data : {
      "des3PubId" : des3PubId
    },
    timeout : 10000,
    success : function(data) {
      if (data.result == 'success') {
        Pub.showPubQuote(url, des3PubId, obj);
      } else {
        scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
      }
    },
    error : function(data) {
      scmpublictoast("系统出错", 1000);
    }
  });
}

var isSendRepeat = false;
Pub.showSnsQuote = function(url, des3PubId, obj) {
  if (!isSendRepeat) {
    isSendRepeat = true;
    $.ajax({
      url : "/pub/outside/ajaxsnsisexists",
      type : 'post',
      dataType : "json",
      async : false,
      data : {
        "des3PubId" : des3PubId
      },
      timeout : 10000,
      success : function(data) {
        if (data.result == 'success') {
          Pub.showPubQuote(url, des3PubId, obj);
        } else {
          scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
        }
      },
      error : function(data) {
        scmpublictoast("系统出错", 1000);
      }
    });
  }
}

// 成果引用展示
Pub.showPubQuote = function(url, des3PubId, obj) {
  var cite = "";
  var close = "";
  if (window.locale == "en_US") {
    cite = "Cite";
    close = "Close";
  } else {
    cite = "引用";
    close = "关闭";
  }
  var post_data = {
    "des3PubId" : des3PubId
  };
  $.ajax({
    url : url,
    type : 'post',
    dataType : "html",
    data : post_data,
    success : function(data) {
      if ((document.getElementsByClassName("background-cover").length == 0)
          && (document.getElementsByClassName("new-quote_container").length == 0)) {
        var content = '<div class="new-quote_container-header">' + '<span class="new-quote_container-header_title">'
            + cite + '</span>' + '<i class=" new-quote_container-closetip list-results_close"></i>' + '</div>'
            + '<div class="new-quote_container-body">' + '</div>' + '<div class="new-quote_container-footer">'
            + '<div class="new-quote_container-footer_close">' + close + '</div>' + '</div>';
        var container = document.createElement("div");
        container.className = "new-quote_container";
        container.innerHTML = content;
        var box = document.createElement("div");
        box.className = "background-cover";
        box.appendChild(container);
        document.body.appendChild(box);
        var closeele = document.getElementsByClassName("new-quote_container-closetip")[0];
        var boxele = document.getElementsByClassName("new-quote_container")[0];
        boxele.style.left = (window.innerWidth - boxele.offsetWidth) / 2 + "px";
        boxele.style.bottom = (window.innerHeight - boxele.offsetHeight) / 2 + "px";
        window.onresize = function() {
          boxele.style.left = (window.innerWidth - boxele.offsetWidth) / 2 + "px";
          boxele.style.bottom = (window.innerHeight - boxele.offsetHeight) / 2 + "px";
        }

        var closebtn = document.getElementsByClassName("new-quote_container-footer_close")[0];
        closebtn.onclick = function() {
          container.style.bottom = -600 + "px";
          setTimeout(function() {
            document.body.removeChild(box);
          }, 200);
        }
        closeele.onclick = function() {
          this.closest(".new-quote_container").style.bottom = -600 + "px";
          setTimeout(function() {
            document.body.removeChild(box);
          }, 200);
        }
      }

      Pub.ajaxTimeOut(data, function() {
        $(".new-quote_container-body").append(data);
        /* document.getElementsByClassName("new-quote_container-body")[0].appendChild(data); */
      });
      isSendRepeat = false;
    },
  });
}
// 个人-成果页随机显示成果或者全文认领模块
Pub.randomModule = function() {
  var arr = [1, 2];
  var item = Math.floor(Math.random() * arr.length);
  var r = arr[item];
  if (r == 1) {
    Pub.pubConfirm(true);
  }
  if (r == 2) {
    Pub.fulltextRcmd(true);
  }
}
// 首先判断成果认领是否有数据
Pub.checkPubHasList = function() {
  $.ajax({
    url : "/pub/ajaxgetpubconfirmcount",
    type : "post",
    dataType : "JSON",
    data : {
      "des3SearchPsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      if (data.count && data.count > 0) {
        Pub.pubConfirm();
      } else {
        Pub.fulltextRcmd();
      }
    }
  });
}
// 全文认领
Pub.fulltextRcmd = function() {
  // flag = typeof (flag) == undefined ? false : flag;
  $.ajax({
    url : "/pub/opt/ajaxgetrcmdpubfulltext",
    type : "post",
    dataType : "html",
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      $('.dev_fulltext_rcmd').html("");
      $(".dev_fulltext_rcmd").append(data);
      // if ($(".dev_no_pupfulltext").val() == "no") {// flag为true表示需要继续查询成果认领
      // Pub.pubConfirm();
      // }
    }
  });
};

// 成果认领
Pub.pubConfirm = function() {
  // flag = typeof (flag) == undefined ? false : flag;
  $.ajax({
    url : "/pub/ajaxgetpubconfirmlist",
    type : "post",
    dataType : "html",
    data : {
      "des3SearchPsnId" : $("#des3PsnId").val()
    },
    success : function(data) {
      $(".dev_pub_confirm").html("");
      $(".dev_pub_confirm").append(data);
      // if ($(".dev_no_pupconfirm").val() == "no") {
      // Pub.fulltextRcmd();
      // }
    }
  });
};
/*
 * 改造原成果认领逻辑 start
 */
// Pub.importMyPubPdwh=function(obj,pdwhPubId){
// //先隐藏，再处理
// if ($('#dev_pubconfirm_isall').val() == "all"){
// $("#dev_"+pdwhPubId).hide();
// var $parentNode = $("#dev_"+pdwhPubId).parent();
// var totalcount = $parentNode.attr("total-count");
// $parentNode.attr("total-count",totalcount-1);
// if(totalcount - 1 == 0){
// $parentNode.append("<div class='response_no-result'>未找到符合条件的记录</div>")
// }
// }
// var params=[];
// params.push({"pubId":pdwhPubId});
// var
// postData={"jsonParams":JSON.stringify(params),"publicationArticleType":"1"};
// $.ajax({
// url: "/pubweb/publication/ajaxclaimimportpdwh",
// type : "post",
// dataType : "json",
// data :postData,
// success:function(data){
// Pub.ajaxTimeOut(data,function(){
// if (data.result == "success") {
// if ($('#dev_pubconfirm_isall').val() == "one") {
// var oneText =
// $(obj).closest(".promo__box-container").find(".pub-idx__main_add-tip").html();
// if (oneText == "check_box") {
// Pub.sendInviteFriend(pdwhPubId);
// }
// Pub.pubConfirm();
// } else {
// $('.dev_confirm_pub_count').val(Number($('.dev_confirm_pub_count').val())+1);
// pubconfirm.listdata.confirmCount = $('.dev_confirm_pub_count').val();
// var allText =
// $(obj).closest(".dev_lookall_pubconfirm").find(".pub-idx__main_add-tip").html();
// if (allText == "check_box") {
// Pub.sendInviteFriend(pdwhPubId);
// }
// }
// }
// });
// },
// error:function(data){
// //scmpublictoast("网络异常",1000);
// }
// });
// };
// //是我的成果
// Pub.yesConfirmPub = function(pdwhPubId) {
// $.ajax({
// url: "/pubweb/psn/ajaxpubconfirm",
// type : "post",
// dataType : "json",
// data :{"pubId":pdwhPubId},
// success:function(data){}
// });
// };
/*
 * 改造原成果认领逻辑 end
 */
// 确认成果认领
Pub.affirmConfirmPub = function(obj, pdwhPubId) {
  BaseUtils.doHitMore(obj, 2000);
  Pub.pdwhIsExist(pdwhPubId, function() {
    // 先隐藏，再处理
    if ($('#dev_pubconfirm_isall').val() == "all") {
      $("#dev_" + pdwhPubId).hide();
      var $parentNode = $("#dev_" + pdwhPubId).parent();
      var totalcount = $parentNode.attr("total-count");
      $parentNode.attr("total-count", totalcount - 1);
      if (totalcount - 1 == 0) {
        $parentNode.append("<div class='response_no-result'>未找到符合条件的记录</div>")
      }
    }
    $.ajax({
      url : "/pub/opt/ajaxAffirmPubComfirm",
      type : "post",
      dataType : "json",
      data : {
        "pubId" : pdwhPubId
      },
      success : function(data) {
        if (data != null && data.result == "success") {
          if ($('#dev_pubconfirm_isall').val() == "one") {
            var oneText = $(obj).closest(".promo__box-container").find(".pub-idx__main_add-tip").html();
            if (oneText == "check_box") {
              Pub.sendInviteFriend(pdwhPubId);
            }
            Pub.pubConfirm();
          } else {
            $('.dev_confirm_pub_count').val(Number($('.dev_confirm_pub_count').val()) + 1);
            pubconfirm.listdata.confirmCount = $('.dev_confirm_pub_count').val();
            var allText = $(obj).closest(".dev_lookall_pubconfirm").find(".pub-idx__main_add-tip").html();
            if (allText == "check_box") {
              Pub.sendInviteFriend(pdwhPubId);
            }
          }
        } else {
          scmpublictoast("网络错误", 2000);
        }
      }
    });

  });
}

// 邀请合作者为联系人
Pub.sendInviteFriend = function(pdwhPubId) {
  $.ajax({
    url : "/psnweb/friend/ajaxinvitetofriend",
    type : "post",
    dataType : "json",
    data : {
      "pdwhPubId" : pdwhPubId
    },
    success : function(data) {
    }
  });
};
// 忽略成果认领
Pub.ignoreConfirmPub = function(obj, pdwhPubId) {
  BaseUtils.doHitMore(obj, 2000);
  Pub.pdwhIsExist(pdwhPubId, function() {
    if ($('#dev_homeconfirm_isall').val() == "one") {
      $(".dev_displaymodule").hide();
    } else {
      $("#dev_" + pdwhPubId).hide();
    }

    if ($('#dev_pubconfirm_isall').val() == "all") {
      $("#dev_" + pdwhPubId).hide();
      var $parentNode = $("#dev_" + pdwhPubId).parent();
      var totalcount = $parentNode.attr("total-count");
      $parentNode.attr("total-count", totalcount - 1);
      if (totalcount - 1 == 0) {
        $parentNode.append("<div class='response_no-result'>未找到符合条件的记录</div>")
      }
    }
    $.ajax({
      url : "/pub/opt/ajaxIgnorePubComfirm",
      type : "post",
      dataType : "json",
      data : {
        "pubId" : pdwhPubId
      },
      success : function(data) {
        if (data.result == 'success') {
          if ($('#dev_pubconfirm_isall').val() == "one") {
            Pub.pubConfirm();
          } else {
            $('.dev_confirm_pub_count').val(Number($('.dev_confirm_pub_count').val()) + 1);
            pubconfirm.listdata.confirmCount = $('.dev_confirm_pub_count').val();
          }
        }
      }
    });
  });
}
// 不是我的成果
Pub.noConfirmPub = function(pdwhPubId) {
  if ($('#dev_pubconfirm_isall').val() == "all") {
    $("#dev_" + pdwhPubId).hide();
    var $parentNode = $("#dev_" + pdwhPubId).parent();
    var totalcount = $parentNode.attr("total-count");
    $parentNode.attr("total-count", totalcount - 1);
    if (totalcount - 1 == 0) {
      $parentNode.append("<div class='response_no-result'>未找到符合条件的记录</div>")
    }
  }
  $.ajax({
    url : "/pubweb/psn/ajaxNoConfirmPub",
    type : "post",
    dataType : "json",
    data : {
      "pubId" : pdwhPubId
    },
    success : function(data) {
      if (data.result == 'success') {
        if ($('#dev_pubconfirm_isall').val() == "one") {
          Pub.pubConfirm();
        } else {
          $('.dev_confirm_pub_count').val(Number($('.dev_confirm_pub_count').val()) + 1);
          pubconfirm.listdata.confirmCount = $('.dev_confirm_pub_count').val();
        }
      }
    }
  });
};

// 是我的全文成果
Pub.doConfirmPubft = function(Id) {
  // 先隐藏，再处理
  if ($('#dev_pubftconfirm_isall').val() == "all") {
    $("#dev_" + Id).hide();
    var $parentNode = $("#dev_" + Id).parent();
    var totalcount = $parentNode.attr("total-count");
    $parentNode.attr("total-count", totalcount - 1);
    if (totalcount - 1 == 0) {
      $parentNode.append("<div class='response_no-result'>未找到符合条件的记录</div>")
    }
  }
  $.ajax({
    url : '/pub/opt/ajaxConfirmPubft',
    type : 'post',
    dataType : 'json',
    data : {
      'ids' : Id
    },
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data.status == "not_exist") {
          scmpublictoast("附件已删除", 2000);
        }
        if ($('#dev_pubftconfirm_isall').val() == "one") {
          $('.dev_rcmd_fulltext_div').remove();
          // scmpublictoast(pubi18n.i18n_optSuccess,1000);
          Pub.fulltextRcmd();
          // Pub.nofulltextTips();
        } else {
          // $("#dev_"+Id).remove();
          // $("#dev_"+Id).hide();
          $('.dev_rcmd_pub_fulltext_count').val(Number($('.dev_rcmd_pub_fulltext_count').val()) + 1);
          pubftconfirm.listdata.fulltextCount = $('.dev_rcmd_pub_fulltext_count').val();
        }
      });
    },
    error : function() {
      // scmpublictoast("网络异常",1000);
    }
  });
};

// 不是我的全文成果
Pub.doRejectPubft = function(Id) {
  // 先隐藏，再处理
  if ($('#dev_pubftconfirm_isall').val() == "all") {
    $("#dev_" + Id).hide();
    var $parentNode = $("#dev_" + Id).parent();
    var totalcount = $parentNode.attr("total-count");
    $parentNode.attr("total-count", totalcount - 1);
    if (totalcount - 1 == 0) {
      $parentNode.append("<div class='response_no-result'>未找到符合条件的记录</div>")
    }
  }
  $.ajax({
    url : '/pub/opt/ajaxRejectPubft',
    type : 'post',
    dataType : 'json',
    data : {
      'ids' : Id
    },
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if ($('#dev_pubftconfirm_isall').val() == "one") {
          $('.dev_rcmd_fulltext_div').remove();
          Pub.fulltextRcmd();
        } else {
          // $("#dev_"+Id).remove();
          // $("#dev_"+Id).hide();
          $('.dev_rcmd_pub_fulltext_count').val(Number($('.dev_rcmd_pub_fulltext_count').val()) + 1);
          pubftconfirm.listdata.fulltextCount = $('.dev_rcmd_pub_fulltext_count').val();
        }
      });
    },
    error : function() {
      // scmpublictoast("网络异常",1000);
    }
  });
};

// 成果转化-查看全部
Pub.pubConvertAll = function() {
  showDialog("dev_lookall_pubconvert_back");
  window.Mainlist({
    name : "pubconvert",
    listurl : "/pubweb/publication/ajaxgetpatent",
    listdata : {
      "isAll" : "1",
      "des3PsnId" : $("#des3PsnId").val()
    },
    method : "scroll",
    listcallback : function(xhr) {
    },
    statsurl : ""
  });
};

// 成果认领-查看全部
var pubconfirm;
Pub.pubConfirmAll = function() {
  addFormElementsEvents();
  $('#dev_pubconfirm_isall').val("all");
  $('#dev_pubftconfirm_isall').val("all");
  showDialog("dev_lookall_pubconfirm_back");
  if ($.trim($('.dev_no_pupfulltext').val()) == "no") {
    $('.dev_item_pubfulltext').hide();
  }
  $('.dev_item_pubconfirm').click();
  /*
   * pubconfirm = window.Mainlist({ name: "pubconfirm", listurl: "/pubweb/psn/ajaxgetpubconfirm",
   * listdata: {"isAll":"1","des3PsnId":$("#des3PsnId").val()}, method: "scroll", listcallback:
   * function(xhr){} });
   */
};

// 全文认领-查看全部
var pubftconfirm;
Pub.pubftConfirmAll = function() {
  addFormElementsEvents();
  $('#dev_pubconfirm_isall').val("all");
  $('#dev_pubftconfirm_isall').val("all");
  showDialog("dev_lookall_pubftconfirm_back");
  // if ($.trim($('.dev_no_pupconfirm').val()) == "no") {
  // $('.dev_item_pubconfirm1').hide();
  // }
  if ($.trim($('#dev_pub_confirmCount').val()) < 1) {
    $('.dev_item_pubconfirm1').hide();
  }
  $('.dev_item_pubfulltext1').click();
  /*
   * pubftconfirm = window.Mainlist({ name: "pubftconfirm1", listurl:
   * "/pubweb/psn/ajaxgetrcmdpubfulltext", listdata:
   * {"isAll":"1","des3PsnId":$("#des3PsnId").val()}, method: "scroll", listcallback:
   * function(xhr){} });
   */
};

// 合作者-查看全部
Pub.pubCooperatorAll = function() {
  $('#dev_pubcooperator_isall').val("all");
  showDialog("dev_lookall_pubcooperator_back");
  window.Mainlist({
    name : "pubcooperator",
    listurl : "/psnweb/cooperation/ajaxpubcooperator",
    listdata : {
      "des3CurrentId" : $("#des3PsnId").val(),
      "isAll" : "1"
    },
    method : "scroll",
    listcallback : function(xhr) {
    }
  });
};

// 成果转化-关闭查看全部弹出层
Pub.closePubConvertBack = function() {
  hideDialog("dev_lookall_pubconvert_back");
  // 统一刷新 刷新列表。 ajb
  $pubList.initializeDrawer();
  $pubList.reloadCurrentPage();
};
// 成果认领-关闭查看全部弹出层
Pub.closePubConfirmBack = function() {
  $('.dev_confirm_pub_count').val(0);
  $('#dev_pubconfirm_isall').val("one");
  $('#dev_pubftconfirm_isall').val("one");
  hideDialog("dev_lookall_pubconfirm_back");
  // ====/psnweb/timeout/ajaxtest 判断超时用
  $.ajax({
    url : "/psnweb/timeout/ajaxtest",
    type : "post",
    dataType : "json",
    data : {},
    success : function(data) {
      if (data.ajaxSessionTimeOut != "yes") {
        Pub.pubConfirm();
        Pub.reloadCurrentPubList();
        // $('.dev_item_pubconfirm').click();
      }
    }
  });
};

// 全文认领-关闭查看全部弹出层
Pub.closePubftconfirmBack = function() {
  $('.dev_rcmd_pub_fulltext_count').val(0);
  $('#dev_pubconfirm_isall').val("one");
  $('#dev_pubftconfirm_isall').val("one");
  hideDialog("dev_lookall_pubftconfirm_back");
  // ====/psnweb/timeout/ajaxtest 判断超时用
  $.ajax({
    url : "/psnweb/timeout/ajaxtest",
    type : "post",
    dataType : "json",
    data : {},
    success : function(data) {
      if (data.ajaxSessionTimeOut != "yes") {
        Pub.checkPubHasList();// 有成果认领显示成果认领，没有则显示全文认领
        // Pub.nofulltextTips();
        // 统一刷新 刷新列表。 ajb
        $pubList.initializeDrawer();
        $pubList.reloadCurrentPage();
        // $('.dev_item_pubfulltext1').click();
      }
    }
  });
};

// 成果合作者-关闭查看全部弹出层
Pub.closePubCooperatorBack = function() {
  $('#dev_pubcooperator_isall').val("one");
  hideDialog("dev_lookall_pubcooperator_back");
  Pub.reloadCurrentPubList();
};

// 关闭更新引用
Pub.closeUpdateCite = function() {
  var updateCite = $('div[dialog-id ="updateCite"] .update-citation__counter span:first').text();
  var totalPub = $('div[dialog-id ="updateCite"] .update-citation__counter span:last').text();
  var notUpdateTimes = totalPub - updateCite;
  /* document.querySelectorAll('[dialog-id="closeCite"]')[0].display="block"; */
  /* hiDialog("updateCite"); */
  hideDialog("updateCite");
  $(".closeUpdate span:first").text(notUpdateTimes);
  showDialog("closeCite");
  /* $("#closeCite").show(); */

  /* showDialog("closeCite"); */
};
// 取消更新
Pub.cancelStop = function() {
  /* $("#closeCite").hide(); */
  hideDialog("closeCite");
  showDialog("updateCite");
}
// 确定取消更新
Pub.stopUpdate = function() {
  /* $("#closeCite").hide(); */
  hideDialog("closeCite");
  document.location.href = window.location.href;
}

// 弹出成果列表过滤框
Pub.showPubFilterBox = function() {
  if ($("#dev_pub_filter_box").css("display") == "none") {
    $("#dev_pub_filter_box").show();
  } else {
    $("#dev_pub_filter_box").hide();
  }
};

// 添加联系人
Pub.addFriendReq = function(reqPsnId, obj, first) {
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  $.ajax({
    url : "/psnweb/friend/ajaxaddfriend",
    type : 'post',
    data : {
      'des3Id' : reqPsnId
    },
    dataType : 'json',
    timeout : 10000,
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data.result == "true") {
          if ($('#dev_pubcooperator_isall').val() == "one") {
            scmpublictoast(pubi18n.i18n_send_success, 1000);
          } else {
            scmpublictoast(pubi18n.i18n_send_success, 1000);
          }
          if (first == true) {
            Pub.cooperator();
          }
        } else if (data.result == "notaddself") {
          scmpublictoast(pubi18n.i18n_addself, 1000);
        } else {
          scmpublictoast(data.msg, 1000);
        }
      });
    },
    error : function() {
    }
  });
};

// 添加成果
var newpublist;
Pub.addPub = function() {
  // showDialog("dev_pub_add");
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    // 加载弹窗
    scmpcimport();
    // 先查找待认领成果
    newpublist = window.Mainlist({
      name : "addPubList",
      listurl : "/pub/ajaxpubconfirmlist",
      listdata : {
        "isAll" : "1",
        "des3SearchPsnId" : $("#des3PsnId").val()
      },
      method : "scroll",
      listcallback : function(xhr) {
        // Pub.impSelectAll();//初始化全选
        var confirmPubNum = $("div[list-main='addPubList']").children(".main-list__item").length;
        if (confirmPubNum != 0) {// SCM-19021成果》添加成果，窗口改为只显示待认领成果记录，认领完后提示无记录，不需要再按名字检索基准库记录显示出来
          Pub.changeliststate();
        }
      }
    });
  }, 1);
};
// 使用当前人名中英文+科技领域推荐，按发表年份倒排
Pub.recmPub = function() {
  // 获取人员科技领域json数据
  $.ajax({
    url : '/psnweb/sciencearea/ajaxjson',
    type : 'post',
    data : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    dataType : 'json',
    success : function(data) {
      // {"des3psnId":"des3psnId","zhName":"wcw","scienceArea":["化学","水产学","材料科学","生物学"],"name":"wcw"}
      var queryString = "";
      // 由于人名+科技领域匹配度较低，暂时只使用人名检索 solr改好后可以放开下面这段代码
      if (locale == "en_US") {
        queryString += data.name;
      } else {
        queryString += data.zhName;
      }
      /*
       * queryString += data.zhName; queryString += " or "+data.name; if(data.scienceArea!=null){
       * for(var i=0; i < data.scienceArea.length; i++){ queryString += " or "+data.scienceArea[i]; } }
       */
      // 构建搜索条件
      Pub.searchPub(queryString);
    }
  });
}
// 成果-添加成果-搜索
Pub.searchPub = function(searchString) {
  if (searchString == null || searchString.trim() == "") {
    return false;
  }
  $("div[list-main='addPubList']").empty();
  if ($("i.check_paper").get(0).classList.contains("check_language-selected")) {// 检索论文
    newpublist = window.Mainlist({
      name : "addPubList",
      listurl : "/pub/search/ajaxrecmpaperlist",
      listdata : {
        "searchString" : searchString
      },
      method : "scroll",
      listcallback : function(xhr) {
        // Pub.impSelectAll();//初始化全选
        Pub.changeliststate();
      }
    });
  } else {// 检索专利
    newpublist = window.Mainlist({
      name : "addPubList",
      listurl : "/pub/search/ajaxrecmpatentlist",
      listdata : {
        "searchString" : searchString
      },
      method : "scroll",
      listcallback : function(xhr) {
        // Pub.impSelectAll();//初始化全选
        Pub.changeliststate();
      }
    });
  }
}
// 成果-添加成果-导入
Pub.importPubs = function() {
  BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function() {
    if ($(".body_content-item_selectored-tip").length == 0) {
      scmpublictoast(pubi18n.i18n_pubnoselect, 1000);
      return false;
    }
    // 收集需要导入的成果信息
    var des3PubId = new Array();
    var dtIds = new Array();
    $(".body_content-item_selectored-tip").each(function() {
      var pubId = this.parentNode.getAttribute("des3PubId");
      Pub.pdwhIsExist(pubId, function() {
        des3PubId.push(pubId);
      });
      if (this.parentNode.getAttribute("dtId") != null) {
        dtIds.push(this.parentNode.getAttribute("dtId"));
      }
      $(this).closest(".main-list__item").remove();// 先隐藏，再处理
    });
    var params = [];
    var postData = {
      "des3PubIds" : des3PubId.join(","),
    };
    // 成果认领 -已经迁移到新系统，由于迁到新系统没有dtId,这里的dtId就是des3PubId
    if (dtIds.length > 0) {
      var $parentNode = $('div[list-main="addPubList"]');
      if (des3PubId != undefined && des3PubId.length > 0) {
        $.ajax({
          url : "/pub/opt/ajaxAffirmPubListComfirm",// 同时认领多条
          type : "post",
          dataType : "json",
          data : postData,
          beforeSend : function() {
            if ($parentNode.find(".main-list__item").length == 0) {
              $parentNode.doLoadStateIco({
                status : 1
              });
            }
          },
          success : function(data) {
            $parentNode.find(".preloader").remove();
            Pub.ajaxTimeOut(data, function() {
              if (data.result == "success") {
                scmpublictoast(pubi18n.i18n_optSuccess, 1000);
              } else if (data.result == "error") {
                scmpublictoast(data.message, 1000);
                newpublist.reloadCurrentPage();
              } else if (data.result == "dup") {
                scmpublictoast(data.message, 1000);
                newpublist.reloadCurrentPage();
              }
            });
          },
          error : function(data) {
          }
        });
      }
    } else {
      if (des3PubId.length <= 0) {
        return;
      };
      closeNewImportBox();
      setTimeout(function() {
        importAchieve();
        $.ajax({
          url : "/pub/snspub/ajaximportlist",
          type : "post",
          dataType : "html",
          data : {
            "des3PubIds" : des3PubId.join(","),
          },
          beforeSend : function() {
            $("div.import_Achieve-item-container").doLoadStateIco({
              addWay : "append",
              status : 1
            });
            $("div.import_Achieve-footer_save").attr("disabled", "disabled");
          },
          success : function(data) {
            $("div.import_Achieve-item-container").find(".preloader").remove();
            $("div.import_Achieve-item-container").append(data);
            Pub.changeliststate();
            authorMatch();//
            showDupInfo();
            if ($("#errorCount").val() > 0) {
              var errorCount = '<font color="red" style="font-weight: bold;">' + $("#errorCount").val() + '</font>';
              $(".import_Achieve-footer_error")
                  .html(pubi18n.i18n_pubnotExist1 + errorCount + pubi18n.i18n_pubnotExist2);
            }
            // 数据初始化完成后再放开
            $("div.import_Achieve-footer_save").attr("disabled", false);
          }
        });
      }, 500);
    }
  }, 0);
}
function importPub(obj) {
  chooseSave();
  $("#showLoading").css("display", "");
  $.ajax({
    type : 'post',
    url : '/pub/snspub/saveimportlist',
    dataType : 'html',
    data : $("#listForm").serialize(),
    success : function(data) {
      $("div." + obj).remove();
      scmpcnewtip({
        targettitle : '新增成果',
        targetcllback : '',
        targetfooter : "0",
        targettxt : data
      });
    },
    error : function() {
    }
  });
}
Pub.closeAddPub = function() {
  var numObj = new Number($(".new-success_save-body_tip-num").html());
  if (numObj > 0) {
    // 成果排序方式改为， 最新修改，2018-12-07 ajb
    $(".filter-list__section[filter-section='orderBy']").find(".filter-value__item").removeClass("option_selected");
    $(".filter-list__section[filter-section='orderBy']").find(".filter-value__item[filter-value='updateDate']")
        .addClass("option_selected");
  }
  // 关闭并刷新成果列表
  var coverbox = document.getElementsByClassName("background-cover");
  $(coverbox).remove();
  $pubList.initializeDrawer();
  $pubList.reloadCurrentPage();

}
Pub.returnAddPub = function() {
  // 返回检索
  var coverbox = document.getElementsByClassName("background-cover");
  $(coverbox).remove();
  Pub.addPub();
}
// 隐藏添加成果弹出框
Pub.hideSelectImportType = function() {
  hideDialog("dev_pub_add");
};

// 成果检索
Pub.searchPubImport = function() {
  window.location.href = "/pub/import/search";
};

// 手工录入
Pub.manualImportPub = function() {
  $pubList.setCookieValues();
  var forwardUrl = "/pub/enter";
  BaseUtils.forwardUrlRefer(true, forwardUrl);

};

// 文件导入
Pub.fileImport = function() {
  // window.location.href =
  // "/pubweb/publication/import/importFile?leftMenuFolId=";
  window.location.href = "/pub/file/importenter";
};

// 未上传全文
var pubnofulltext;
Pub.uploadFulltext = function() {
  showDialog("dev_lookall_pubnofulltext_back");
  pubnofulltext = window.Mainlist({
    name : "pubnofulltext",
    listurl : "/pubweb/publication/ajaxnofulltextpublist",
    listdata : {
      "des3PsnId" : $("#des3PsnId").val()
    },
    method : "scroll",
    listcallback : function(xhr) {
      var $pubnofull = $("div[class='dialogs__box'][dialog-id='dev_lookall_pubnofulltext_back']").first();
      $pubnofull.find(".dialogs__childbox_adapted   .main-list__item").each(function() {
        var $this = $(this);
        /*
         * var dataJson = {
         * allowType:'*.txt;*.jpg;*.jpeg;*.gif;*.pdf;*.doc;*.docx;*.png;*.html;*.xhtml;*.htm;*.rar;*.zip;',
         * pubId:$this.attr("id") };
         */
        // 20181213 SCM-21660 增加批量操作成果界面中上传全文的文件类型：.xls和.xlsx
        var filetype = ".txt;.jpg;.jpeg;.gif;.pdf;.doc;.docx;.png;.html;.xhtml;.htm;.rar;.zip;.xls;.xlsx;".split(";");
        var data = {
          "fileurl" : "/fileweb/fileupload",
          "filedata" : {
            fileDealType : "generalfile"
          },
          "filecallback" : Pub.uploadFileCallback,
          "filecallbackparam" : {
            pubId : $this.attr("id")
          },
          "filetype" : filetype
        };
        fileUploadModule.initialization(this, data);
      });
    },
    statsurl : ""
  });

};

// 上传文件后回调
Pub.uploadFileCallback = function(xhr, params) {
  if (!xhr || !params) {
    return false;
  }
  const data = eval('(' + xhr.responseText + ')');
  var pudId = params.pubId;
  var des3fid = data.successDes3FileIds.split(",")[0];
  if (!des3fid) {
    return;
  }

  $.ajax({
    url : '/pubweb/publication/ajaxupdatefulltext',
    type : 'post',
    data : {
      'pubId' : pubId,
      'des3fid' : des3fid
    },
    dataType : 'json',
    timeout : 10000,
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data.result == "true") {
          Pub.nofulltextTips();
          pubnofulltext.listdata.fulltextCount = document.querySelector('*[list-main="pubnofulltext"]')
              .getElementsByClassName("fileupload__box upload_finished").length;
          // 页面不刷新--ajb 2017-04-27
          // $("#"+pubId).remove();
          // $pubList.initializeDrawer();
          // $pubList.reloadCurrentPage();
          // $pubList.drawerRemoveAll();
          scmpublictoast(pubi18n.i18n_uploadSuccess, 1000);
        } else {
          scmpublictoast('系统出现异常，请稍后再试', 2000);
        }
      });
    }
  });
};

// 上传文件回调
/*
 * Pub.filecallback =function(xhr){ const data = eval('('+xhr.responseText+')'); var pubId =
 * data.pubId ; var fileName = data.fileName; if (fileName == null) { return; } var fileExt =
 * fileName.substring(fileName.lastIndexOf(".")); var fileDate = data.createTime; var fileDesc =
 * data.fileDesc; if (fileDesc == null) { fileDesc = ""; } var file_id = data.fileId; if(file_id ==
 * ""){ return; } ///查询成果权限,因为全文附件权限跟成果权限走_SCM-12105 var permission = 2;//0:所有人可下载 1:联系人 2:自己
 * $.ajax({ url: '/pubweb/publication/ajaxpubauthority', type:'post',
 * data:{"pubId":pubId,"des3PsnId":$("#des3PsnId").val()}, dataType:'json', async: false, success:
 * function(data){ Pub.ajaxTimeOut(data , function(){ if (data) { if (data.authority != undefined) {
 * if (data.authority == 4 || data.authority == 6) { permission = 2; } if (data.authority == 7) {
 * permission = 0; } } } }); } }); $.ajax({ url: '/pubweb/publication/ajaxupdatefulltext',
 * type:'post',
 * data:{'pubId':pubId,'node_id':1,'actionType':"1",'file_id':file_id,'file_name':fileName,'file_desc':fileDesc,'file_ext':fileExt,'upload_date':fileDate,'permission':permission},
 * dataType:'json', timeout: 10000, success: function(data){ Pub.ajaxTimeOut(data , function(){
 * if(data.result == "true"){ Pub.nofulltextTips(); pubnofulltext.listdata.fulltextCount =
 * document.querySelector('*[list-main="pubnofulltext"]').getElementsByClassName("fileupload__box
 * upload_finished").length; //页面不刷新--ajb 2017-04-27 //$("#"+pubId).remove();
 * //$pubList.initializeDrawer(); //$pubList.reloadCurrentPage(); //$pubList.drawerRemoveAll();
 * scmpublictoast(pubi18n.i18n_uploadSuccess,1000); } }); }, error:function(e){ } }); };
 */

// 关闭未上传全文
Pub.closePubNofulltextBack = function() {
  hideDialog("dev_lookall_pubnofulltext_back");
  $pubList.initializeDrawer();
  // 不管有没有上传全文都要 刷新列表。 ajb
  $pubList.reloadCurrentPage();
};

// 出售专利
Pub.patentPublish = function(pubId) {
  window.open("/pubweb/patent/publish?pubId=" + pubId);
};

// 超时处理
Pub.ajaxTimeOut = function(data, myfunction) {
  var toConfirm = false;
  if ('{"ajaxSessionTimeOut":"yes"}' == data) {
    toConfirm = true;
  }
  if (!toConfirm && data != null) {
    toConfirm = data.ajaxSessionTimeOut;
  }
  if (toConfirm) {
    jConfirm(pubi18n.i18n_timeout, pubi18n.i18n_tipTitle, function(r) {
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
};

// 刷新批量处理框列表
Pub.reloadCurrentPubList = function() {
  $pubList.initializeDrawer();
  $pubList.reloadCurrentPage();
}

// ============================================================
// 基准库成果详情赞操作
Pub.pdwhAward = function(des3PubId, obj) {
  Pub.pdwhIsExist2(des3PubId, function() {
    var isAward = $(obj).attr("isAward");// 是否赞过 1 是 0否
    if (isAward == 'true') {
      isAward = 1;
    } else if (isAward == 'false') {
      isAward = 0;
    }
    var post_data = {
      "des3PdwhPubId" : des3PubId
    };
    if (isAward == '1') {
      $(obj).attr("isAward", 0);
      post_data.operate = '0';// 取消赞
    } else {
      $(obj).attr("isAward", 1);
      post_data.operate = '1';// 点赞
    }
    if (!$(obj).hasClass('new-Standard_Function-bar_item-title')) {
      obj = $(obj).parent().find('.new-Standard_Function-bar_item-title:first');
    }
    var awardCount = 0;
    $.ajax({
      url : "/pub/opt/ajaxpdwhlike",
      type : "post",
      dataType : "json",
      data : post_data,
      success : function(data) {
        Pub.ajaxTimeOut(data, function() {
          if (data.result == "success") {
            awardCount = data.awardTimes;
            if (awardCount > 999) {
              awardCount = "1k+";
            }
            if (isAward == 1) {// 取消赞
              if (awardCount == 0) {
                $(obj).text(pubi18n.i18n_like);
              } else {
                $(obj).text(pubi18n.i18n_like + "(" + awardCount + ")");
              }
              // 我的成果
              $(obj).parent().parent().find(".new-Standard_Function-bar_item:first").removeClass(
                  "new-Standard_Function-bar_selected");
            } else {// 赞
              $(obj).text(pubi18n.i18n_unlike + "(" + awardCount + ")");
              // 我的成果
              $(obj).parent().parent().find(".new-Standard_Function-bar_item:first").addClass(
                  "new-Standard_Function-bar_selected");
            }
          } else {
            scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
          }
        });
      }
    });
  });
};

// ===============================================
// 成果认领与全文认领查看全部上的成果认领模块
Pub.toConfirmList = function() {
  $('.dev_fulltextList').html("");
  $('.dev_fulltextList').hide();
  $('.dev_confirmList').show();
  $('.dev_lookall_pubconfirm_back_add_tip').find(".pub-idx__main_add-tip").html("check_box");
  $('.dev_lookall_pubconfirm_back_add_tip').show();
  pubconfirm = window.Mainlist({
    name : "pubconfirm",
    listurl : "/pub/ajaxgetpubconfirmlist",
    listdata : {
      "isAll" : "1",
      "des3SearchPsnId" : $("#des3PsnId").val()
    },
    method : "scroll",
    listcallback : function(xhr) {
      Pub.initCheckBox();
      // 没有全文认领，则隐藏
      if ($("div[list-main='pubconfirm']").find(".js_listinfo").attr("fulltext_confirm") == "false") {
        $(".dev_item_pubfulltext").css("display", "none");
      } else {
        $(".dev_item_pubfulltext").css("display", "");
      }
    }
  });
};

// 成果认领与全文认领查看全部上的全文认领模块
Pub.toFulltextList = function() {
  $('.dev_confirmList').html("");
  $('.dev_confirmList').hide();
  $('.dev_fulltextList').show();
  $('.dev_lookall_pubconfirm_back_add_tip').hide();
  pubftconfirm = window.Mainlist({
    name : "pubftconfirm",
    listurl : "/pub/opt/ajaxgetrcmdpubfulltext",
    listdata : {
      "isAll" : "1",
      "des3PsnId" : $("#des3PsnId").val()
    },
    method : "scroll",
    listcallback : function(xhr) {
    }
  });
};
// =============================================

// 成果认领与全文认领查看全部上的成果认领模块
Pub.toConfirmList1 = function() {
  $('.dev_fulltextList1').html("");
  $('.dev_fulltextList1').hide();
  $('.dev_confirmList1').show();
  $('.dev_lookall_pubftconfirm_back_add_tip').find(".pub-idx__main_add-tip").html("check_box");
  $('.dev_lookall_pubftconfirm_back_add_tip').show();
  pubconfirm = window.Mainlist({
    name : "pubconfirm1",
    listurl : "/pub/ajaxgetpubconfirmlist",
    listdata : {
      "isAll" : "1",
      "des3SearchPsnId" : $("#des3PsnId").val()
    },
    method : "scroll",
    listcallback : function(xhr) {
      Pub.initCheckBox();
    }
  });
};

// 成果认领与全文认领查看全部上的全文认领模块
Pub.toFulltextList1 = function() {
  $('.dev_confirmList1').html("");
  $('.dev_confirmList1').hide();
  $('.dev_fulltextList1').show();
  $('.dev_lookall_pubftconfirm_back_add_tip').hide();
  pubftconfirm = window.Mainlist({
    name : "pubftconfirm1",
    listurl : "/pub/opt/ajaxgetrcmdpubfulltext",
    listdata : {
      "isAll" : "1",
      "des3PsnId" : $("#des3PsnId").val()
    },
    method : "scroll",
    listcallback : function(xhr) {
    }
  });
};
// 论文列表
var $paperList;
// 我的-论文 加载关键词 //此方法作废
Pub.loadpaperList = function() {
  // 关键词是动态过滤条件
  /*
   * const $filterlist =
   * Array.from(document.getElementsByClassName("filter-list")).filter(function(x){ return
   * x.getAttribute("list-filter") === "paperlist"; }); var $filtersections = [];
   * $filterlist.forEach(function(x){ $filtersections =
   * $filtersections.concat(Array.from(x.getElementsByClassName("filter-list__section"))); }); const
   * $keywords = $filtersections.filter(function(x){ return x.getAttribute("filter-section") ===
   * "keywords"; })[0]; const $kwValue = { url: "/pubweb/publication/psnKeywords", acURL:
   * "/groupweb/mygrp/ajaxautoconstkeydiscs", name: "keyWords", code: "keyWords", acType:"input",
   * addPosition:"pre", isSelected:true }; const $filterMap = new Map(); $filterMap.set($keywords,
   * $kwValue);
   */

  $paperList = window.Mainlist({
    name : "paperlist",
    listurl : "/pubweb/publication/ajaxpaperlist",
    listdata : {
      'isPsnPapers' : '1'
    },
    method : "scroll",
    listcallback : function(xhr) {
      $("a.thickbox").thickbox();
      var titlelist = document.getElementsByClassName("filter-value__option");
      for (var i = 0; i < titlelist.length; i++) {
        titlelist[i].onmouseover = function() {
          var text = this.innerText;
          this.setAttribute("title", text);
        }
      }
      /*
       * //设置输入框的默认文字“输入关注的关键词” var autocompletebox =
       * document.getElementsByClassName("js_autocompletebox")[0];
       * if(autocompletebox.getAttribute("continueInput")){
       * autocompletebox.setAttribute("placeholder",""); autocompletebox.focus();
       * autocompletebox.closest(".search-box_container ").style.border="1px solid #288aed"; }else{
       * autocompletebox.setAttribute("placeholder","输入关注的关键词");
       * autocompletebox.closest(".search-box_container ").style.border="1px solid #ddd"; }
       * $(autocompletebox).unbind("focus",hideTips); $(autocompletebox).unbind("blur",showTips);
       * $(autocompletebox).bind("focus",hideTips); $(autocompletebox).bind("blur",showTips);
       */
      // 加载上传全文的展示框
      // Pub.loadPubFulltext();
    }// ,
  // filtermap:$filterMap
  });
}
Pub.loadCollectedPubs = function() {
  $paperList = window.Mainlist({
    name : "paperlist",
    listurl : "/pub/ajaxCollectedPubs",
    listdata : {},
    method : "pagination",
    listcallback : function(xhr) {
      // 加载上传全文的展示框
      Pub.loadPubFulltext();
      // 遍历 给所有checkbox 给批量选择框绑定事件-同步上传文件问题
      Pub.bindASyncUpload();
      var titlelist = document.getElementsByClassName("filter-value__option");
      for (var i = 0; i < titlelist.length; i++) {
        titlelist[i].onmouseover = function() {
          var text = this.innerText;
          this.setAttribute("title", text);
        }
      }
    }
  });
}
function hideTips() {
  this.setAttribute("placeholder", "");
  this.setAttribute("continueInput", true);
}
function showTips() {
  this.setAttribute("placeholder", "输入关注的关键词");
  this.setAttribute("continueInput", false);
}
// 我的论文-取消收藏

Pub.deleteCollectedPub = function(pubId, pubDb, obj) {
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  var collected = $(obj).attr("collected");
  $.ajax({
    url : "/pub/opt/ajaxCollect",
    type : 'post',
    data : {
      "des3PubId" : pubId,
      "pubDb" : pubDb,
      "collectOperate" : collected
    },
    dateType : 'json',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data && data.result == "success") {
          scmpublictoast(pubi18n.i18n_delSuccess, 1000);
          $(".main-list__list").empty();
          // 成功，重新加载
          if ($paperList != null) {// PC
            $paperList.initializeDrawer();
            $paperList.reloadCurrentPage();
          }
          if ($mobilepaperList != null) {
            $mobilepaperList.reloadCurrentPage();
          }
        } else if (data.result == "not_exists") {
          scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
        } else {
          scmpublictoast(pubi18n.i18n_delFail, 1000);
        }
      });
    }
  });
}

// 此方法作废
Pub.delPaper = function(obj) {
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  $.ajax({
    url : "/pubweb/publication/ajaxdeletePaper",
    type : 'post',
    data : {
      "des3Id" : $(obj).attr("des3PubId")
    },
    success : function(data) {
      var result = data.result;
      switch (result) {
        case "exist" : {
          // pub_en_US 翻译文件
          scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
        }
        case "warn" : {
          scmpublictoast(pubi18n.i18n_onPermissions, 1000);
        }
        case "error" : {
          scmpublictoast(pubi18n.i18n_delete_pub_error, 1000);
        }
        default : {
          $(".main-list__list").empty();
          // 成功，重新加载
          if ($paperList != null) {// PC
            $paperList.initializeDrawer();
            $paperList.reloadCurrentPage();
          }
          if ($mobilepaperList != null) {
            $mobilepaperList.reloadCurrentPage();
          }
        }
      }
    }
  })
};
// 加载成果上传全文的事件
Pub.loadPubFulltext = function() {
  $(".pub_uploadFulltext").each(function() {
    $this = $(this);
    /*
     * var dataJson = {
     * allowType:'*.txt;*.jpg;*.jpeg;*.gif;*.pdf;*.doc;*.docx;*.png;*.html;*.xhtml;*.htm;*.rar;*.zip;',
     * fileDealType: "generalfile" };
     */
    // var filetype =
    // ".txt;.jpg;.jpeg;.gif;.pdf;.doc;.docx;.png;.html;.xhtml;.htm;.rar;.zip;".split(";");
    var data = {
      "fileurl" : "/fileweb/fileupload",
      "filedata" : {
        fileDealType : "generalfile"
      },
      "method" : "nostatus",
      "filecallback" : Pub.reloadFulltext,
      "filecallbackparam" : {
        pubId : $this.attr("des3Id")
      },
    // "filetype":filetype
    };
    try {
      fileUploadModule.initialization(this, data);
    } catch (e) {
      // 利用openId全站检索显示人员主页可能找不到该方法
    }
  });
};
// 上传全文，文件上传成功后的回调函数，用于更新成果全文附件信息
Pub.reloadFulltext = function(xhr, params) {
  if (typeof (xhr) == "undefined" || !params) {
    return false;
  }
  const data = eval('(' + xhr.responseText + ')');
  var pubId = params.pubId;
  var des3fid = data.successDes3FileIds.split(",")[0];
  $
      .ajax({
        url : '/pub/opt/ajaxupdatefulltext',
        type : 'post',
        data : {
          'pubId' : pubId,
          'des3FileId' : des3fid
        },
        dataType : 'json',
        timeout : 10000,
        success : function(data) {
          Pub
              .ajaxTimeOut(
                  data,
                  function() {
                    if (data.result == "true") {
                      // 替换全文图片和下载链接 列表和批量选择框都需要替换
                      var $listIconBox = $(".dev_pub_del_" + pubId + " .pub-idx__full-text_img");
                      var $drawerBatchBox = $(".drawer-batch__box div[des3id='" + pubId + "']").parent();
                      var img = "<image class='dev_fulltext_download dev_pub_img' style='cursor:pointer' onerror='this.onerror=null;this.src=\"/resmod/images_v5/images2016/file_img1.jpg\"'";
                      img += "src='";
                      if (data.fullTextImagePath != null && data.fullTextImagePath != "") {
                        img += data.fullTextImagePath;
                      } else {
                        img += "/resmod/images_v5/images2016/file_img1.jpg";
                      }
                      img += "'";
                      if (data.downUrl != null && data.downUrl != "") {
                        img += "onclick='window.location.href=\"" + data.downUrl + "\"';";
                        img += " title='下载全文';";
                      }
                      img += " />";
                      img += "<a ";
                      if (data.downUrl != null && data.downUrl != "") {
                        img += "href=\""
                            + data.downUrl
                            + "\" class='new-tip_container-content' title='下载全文'> <img src='/resmod/smate-pc/img/file_ upload1.png' class='new-tip_container-content_tip1' > <img src='/resmod/smate-pc/img/file_ upload.png' class='new-tip_container-content_tip2'> </a>";
                      } else {
                        img += "></a>"
                      }
                      $listIconBox.html(img);
                      $drawerBatchBox.html(img);
                      $listIconBox.css({
                        "border" : "",
                        "cursor" : "pointer",
                        "position" : "relative",
                        "display" : "flex",
                        "justify-content" : "center",
                        "align-items" : "center"
                      });
                    } else {
                      scmpublictoast('系统出现异常，请稍后再试', 2000);
                    }
                  });
        }
      });

};
// 上传全文后刷新全文信息 如果有批量选择 需要同步
/*
 * Pub.reloadCurrentFulltext=function(xhr){ if(typeof(xhr) == "undefined"){ return false; } const
 * data = eval('('+xhr.responseText+')'); var pubId = data.pubId ; var fileName = data.fileName; if
 * (fileName == null) { return; } var fileExt = fileName.substring(fileName.lastIndexOf(".")); var
 * fileDate = data.createTime; var fileDesc = data.fileDesc; if (fileDesc == null) { fileDesc = ""; }
 * var file_id = data.fileId; if(file_id == ""){ return; } ///查询成果权限,因为全文附件权限跟成果权限走_SCM-12105 var
 * permission = 2;//0:所有人可下载 1:联系人 2:自己 $.ajax({ url: '/pubweb/publication/ajaxpubauthority',
 * type:'post', data:{"pubId":pubId,"des3PsnId":$("#des3PsnId").val()}, dataType:'json', async:
 * false, success: function(data){ Pub.ajaxTimeOut(data , function(){ if (data) { if (data.authority !=
 * undefined) { if (data.authority == 4 || data.authority == 6) { permission = 2; } if
 * (data.authority == 7) { permission = 0; } } } }); } }); $.ajax({ url:
 * '/pubweb/publication/ajaxupdatefulltext', type:'post',
 * data:{'pubId':pubId,'node_id':1,'actionType':"1",'file_id':file_id,'file_name':fileName,'file_desc':fileDesc,'file_ext':fileExt,'upload_date':fileDate,'permission':permission},
 * dataType:'json', timeout: 10000, success: function(data){ Pub.ajaxTimeOut(data , function(){
 * if(data.result == "true"){ //替换全文图片和下载链接 列表和批量选择框都需要替换 var $listIconBox =
 * $(".dev_pub_del_"+pubId+" .pub-idx__full-text_img"); var $drawerBatchBox = $(".drawer-batch__box
 * div[des3id='"+pubId+"']").parent(); var img="<image class='dev_fulltext_download dev_pub_img'
 * style='cursor:pointer' onerror='this.src=\"/resmod/images_v5/images2016/file_img1.jpg\"'"; img +=
 * "src='"; if(data.fullTextImagePath != null && data.fullTextImagePath != ""){ img +=
 * data.fullTextImagePath; } img += "'"; if(data.downUrl != null && data.downUrl != ""){ img +=
 * "onclick='window.location.href=\""+data.downUrl+"\"';"; } img += " />"; $listIconBox.html(img);
 * $drawerBatchBox.html(img); } }); }, error:function(e){ } }); }
 */
// 刷新当前页
/*
 * Pub.reloadCurrentPubList=function(xhr){ if(typeof(xhr) == "undefined"){ return; } const data =
 * eval('('+xhr.responseText+')'); var pubId = data.pubId ; var fileName = data.fileName; if
 * (fileName == null) { return; } var fileExt = fileName.substring(fileName.lastIndexOf(".")); var
 * fileDate = data.createTime; var fileDesc = data.fileDesc; if (fileDesc == null) { fileDesc = ""; }
 * var file_id = data.fileId; if(file_id == ""){ return; } ///查询成果权限,因为全文附件权限跟成果权限走_SCM-12105 var
 * permission = 2;//0:所有人可下载 1:联系人 2:自己 $.ajax({ url: '/pubweb/publication/ajaxpubauthority',
 * type:'post', data:{"pubId":pubId,"des3PsnId":$("#des3PsnId").val()}, dataType:'json', async:
 * false, success: function(data){ Pub.ajaxTimeOut(data , function(){ if (data) { if (data.authority !=
 * undefined) { if (data.authority == 4 || data.authority == 6) { permission = 2; } if
 * (data.authority == 7) { permission = 0; } } } }); } }); $.ajax({ url:
 * '/pubweb/publication/ajaxupdatefulltext', type:'post',
 * data:{'pubId':pubId,'node_id':1,'actionType':"1",'file_id':file_id,'file_name':fileName,'file_desc':fileDesc,'file_ext':fileExt,'upload_date':fileDate,'permission':permission},
 * dataType:'json', timeout: 10000, success: function(data){ Pub.ajaxTimeOut(data , function(){
 * if(data.result == "true"){ //移除原有元素 var cont_r =
 * document.getElementsByClassName("dev_pub_list")[0]; cont_r.innerHTML = "";
 * 
 * $pubList.initializeDrawer(); $pubList.reloadCurrentPage(); } }); }, error:function(e){ } }); }
 */
// 移动端论文列表
var $mobilepaperList;
Pub.loadMobilePaperList = function() {
  document.getElementsByTagName('body')[0].scrollTop = 0;
  $(".main-list__list").empty();
  // 保存过滤条件，请求列表页
  var includeType = $("#includeType").val(), keywords = $("#keywords").val(), publishYear = $("#publishYear").val(), pubType = $(
      "#pubType").val(), searchKey = $("#searchKey").val();
  $mobilepaperList = window
      .Mainlist({
        name : "mobile_pub_list",
        // listurl: "/pubweb/mobile/pubpaper",
        listurl : "/pub/querylist/ajaxcollect",
        listdata : {
          'isPsnPapers' : '1',
          "includeType" : includeType,
          "keywords" : keywords,
          "publishYear" : publishYear,
          "pubType" : pubType,
          "searchKey" : searchKey,
          "orderBy" : "collectDate"
        },
        method : "scroll",
        /*
         * beforeSend : function() { $(".main-list__list").doLoadStateIco({ status : 1, addWay :
         * "append" }); },
         */
        listcallback : function(xhr) {
          var currentNumItem = $(".main-list__item").length;
          var totalCount = $(".main-list__list").attr("total-count");
          if (totalCount > 0 && Number(currentNumItem) >= Number(totalCount)) {
            $(".main-list__list")
                .append(
                    "<div class='paper_content-container_list main-list__item main-list__item-nonebox' style='border:none; border-bottom:0px!important;'><div class='main-list__item-nonetip'>没有更多记录</div></div>");
          }
          if (totalCount <= 0) {
            $("div.response_no-result").remove();
            $(".main-list__list")
                .append(
                    "<div class='paper_content-container_list main-list__item' style='border:0'><div class='response_no-result'>未找到符合条件的记录</div></div>");
          }
          Pub.paperPullDown();
        }
      });
}
// 下拉刷新
var startPosY, endPosY;
Pub.paperPullDown = function() {
  // 支持touch事件?
  isAndroid = (/android/gi).test(navigator.appVersion), isIDevice = (/iphone|ipad/gi).test(navigator.appVersion),
      isTouchPad = (/hp-tablet/gi).test(navigator.appVersion), hasTouch = 'ontouchstart' in window && !isTouchPad;
  var START_EV = hasTouch ? 'touchstart' : 'mousedown', MOVE_EV = hasTouch ? 'touchmove' : 'mousemove', END_EV = hasTouch
      ? 'touchend'
      : 'mouseup', CANCEL_EV = hasTouch ? 'touchcancel' : 'mouseup';

  document.addEventListener(START_EV, Pub.getStartPos, false);
  document.addEventListener(MOVE_EV, Pub.refreshList, false);
}
Pub.getStartPos = function(e) {
  var touch = e.touches[0];
  startPosY = Number(touch.pageY);
}
Pub.refreshList = function(e) {
  var START_EV = hasTouch ? 'touchstart' : 'mousedown', MOVE_EV = hasTouch ? 'touchmove' : 'mousemove';
  var touch = e.touches[0];
  var y = Number(touch.pageY);
  if (y - startPosY > 50) {
    // 下拉刷新后移除监听，避免多次重复请求
    document.removeEventListener(MOVE_EV, Pub.refreshList);
    document.removeEventListener(START_EV, Pub.getStartPos);
    repeatLoad = true;
    Pub.loadMobilePaperList();
  }
}
// 推荐选择方法
Pub.addCondition = function(obj, typeclass) {
  if (typeclass == "type_time") {// 出版时间是单选的
    $("." + typeclass).removeClass("selector__list-target")
  }
  $(obj).addClass("selector__list-target");
  if (typeclass == "type_include") {
    if ($("#includeType").val() == "") {
      $("#includeType").val($(obj).attr("value"));
    } else {
      var includeList = $("#includeType").val().split(',');
      var index = $.inArray($(obj).attr("value"), includeList);
      if (index < 0) {
        $("#includeType").val($("#includeType").val() + ',' + $(obj).attr("value"));
      }
    }
    $(obj).attr('onclick', 'Pub.delCondition(this,"type_include")');
  }
  if (typeclass == "type_key") {
    // 要考虑避免关键词中包含分隔符
    if ($("#keywords").val() == "") {
      $("#keywords").val($(obj).attr("value"));
    } else {
      var psnKeyList = $("#keywords").val().split(',');
      var index = $.inArray($(obj).attr("value"), psnKeyList);
      if (index < 0) {
        $("#keywords").val($("#keywords").val() + ',' + $(obj).attr("value"));
      }
    }
    $(obj).attr('onclick', 'Pub.delCondition(this,"type_key")');
  }
  if (typeclass == "type_time") {
    $("." + typeclass).closest(".item_list-align").each(function() {
      $(this).removeClass("selector__list-target");
    });
    $("." + typeclass).each(function() {
      $(this).attr('onclick', 'Pub.addCondition(this,\'' + typeclass + '\')');
    });
    $("#publishYear").val($(obj).attr("value"));
  }
  if (typeclass == "type_pub") {
    if ($("#pubType").val() == "") {
      $("#pubType").val($(obj).attr("value"));
    } else {
      var pubTypeList = $("#pubType").val().split(',');
      var index = $.inArray($(obj).attr("value"), pubTypeList);
      if (index < 0) {
        $("#pubType").val($("#pubType").val() + ',' + $(obj).attr("value"));
      }
    }
    $(obj).attr('onclick', 'Pub.delCondition(this,"type_pub")');
  }
}
// 推荐取消选择方法
Pub.delCondition = function(obj, typeclass) {
  $(obj).removeClass("selector__list-target")
  if (typeclass == "type_include") {
    var includeList = $("#includeType").val().split(',');
    var index = $.inArray($(obj).attr("value"), includeList);
    if (index >= 0) {
      includeList.splice(index, 1);
      $("#includeType").val(includeList.join(','));
    }
    $(obj).attr('onclick', 'Pub.addCondition(this,"type_include")');
  }
  if (typeclass == "type_key") {
    var psnKeyList = $("#keywords").val().split(',');
    var index = $.inArray($(obj).attr("value"), psnKeyList);
    if (index >= 0) {
      psnKeyList.splice(index, 1);// 删除值
      $("#keywords").val(psnKeyList.join(','));
    }
    $(obj).attr('onclick', 'Pub.addCondition(this,"type_key")');
  }
  if (typeclass == "type_time") {
    $("#publishYear").val("");
    $(obj).attr('onclick', 'Pub.addCondition(this,"type_time")');
  }
  if (typeclass == "type_pub") {
    var pubTypeList = $("#pubType").val().split(',');
    var index = $.inArray($(obj).attr("value"), pubTypeList);
    if (index >= 0) {
      pubTypeList.splice(index, 1);
      $("#pubType").val(pubTypeList.join(','));
    }
    $(obj).attr('onclick', 'Pub.addCondition(this,"type_pub")');
  }
}

Pub.canclbtnClick = function() {
  $(".filter__body-item__list").removeClass("selector__list-target");
  $("#includeType").val("");
  // $("#keywords").val("");
  $("#publishYear").val("");
  $("#pubType").val("");
}
// 成果快速分享
Pub.quickShareDyn = function(obj) {
  var des3ResId = $(obj).attr("des3ResId");
  var pubDb = $(obj).attr("pubDb");
  var dataJson, url;
  if (pubDb == "PDWH") {// 基准库快速分享
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
  } else {
    dataJson = {
      "dynType" : "B2TEMP",
      "resType" : 1,
      "des3ResId" : des3ResId,
      "operatorType" : 3
    }
    url = "/dynweb/dynamic/ajaxquickshare";
  }

  $.ajax({
    url : url,
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      if (data.result == "success") {
        scmpublictoast(pubi18n.i18n_shareSuccess, 1000);
      } else {
        scmpublictoast(pubi18n.i18n_shareFail, 1000);
      }
    },
    error : function() {
      scmpublictoast(pubi18n.i18n_shareFail, 1000);
    }
  });
}

Pub.getfulltextUrlDownload = function(obj) {
  var downLoadUrl = $("#pubFullTextDownloadUrl").val();
  Msg.downloadFTFile(downLoadUrl);
  if (downLoadUrl != null && downLoadUrl != "" && typeof (downLoadUrl) != "undefined") {
    Msg.downloadFTFile(downLoadUrl);
    // window.location.href=downLoadUrl;
  }
}
/**
 * 监听移动端软键盘的回车搜索按键
 */
Pub.listenInput = function(e) {
  var searchKey = $(".paper__func-box input").val();
  if ($.trim(searchKey) != "") {
    var event = e.which || e.keyCode;
    if (event == 13) {
      $("#searchKey").val(searchKey);
      Pub.loadMobilePaperList();
    }
  }
}
// 收藏个人库成果
Pub.saveSnsPaper = function(des3PubId) {
  $.ajax({
    url : "/pubweb/ajaxsavePaper",
    type : "post",
    dataType : "json",
    data : {
      "des3PubId" : des3PubId,
      "pubDb" : "SNS"
    },
    success : function(data) {
      Pub.ajaxTimeOut(data, function() {
        if (data && data.result) {
          if (data.result == "success") {
            scmpublictoast(pubi18n.i18n_collectionSuccess, 1000);
          } else if (data.result == "exist") {
            scmpublictoast(pubi18n.i18n_pubIsSaved, 1000);
          } else if (data.result == "isDel") {
            scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
          } else {
            scmpublictoast(pubi18n.i18n_collectionFail, 1000);
          }
        } else {
          scmpublictoast(pubi18n.i18n_collectionFail, 1000);
        }
      });
    }
  });
}
/*
 * //我的论文使用的 基准库成果详情赞操作 Pub.pdwhAwardOpt = function(des3PubId,obj) { var isAward;
 * if($(obj).find('.paper_footer-tool:first').hasClass("dev_cancel_award")) { isAward = '1';//赞过 }
 * else { isAward = '0'; } var count =
 * Number($(obj).find('.paper_footer-tool:first').text().replace(/[\D]/ig,"")); $.ajax({ url :
 * "/pubweb/details/ajaxpdwhaward", type : "post", dataType : "json", data :
 * {"des3PubId":des3PubId,"isAward":isAward}, success : function(data) { Pub.ajaxTimeOut(data,
 * function(){ if (data.result == "success") { if (isAward == 1) {//取消赞
 * $('.paper_footer-tool:first').removeClass("container_fuc-fabulous__flag");
 * $('.paper_footer-tool:first').addClass("paper_footer-fabulous");
 * $(obj).find('.paper_footer-tool:first').removeClass("dev_cancel_award") count -= 1; if (count ==
 * 0) { $(obj).find('.paper_footer-tool:first').text(pubi18n.i18n_like); } else {
 * $(obj).find('.paper_footer-tool:first').text(pubi18n.i18n_like+"("+count+")"); }
 * $(obj).find('.paper_footer-tool:first').removeClass("paper_footer-tool__box-click"); } else {//赞
 * count += 1; $(obj).find('.paper_footer-tool:first').addClass("dev_cancel_award")
 * $('.paper_footer-tool:first').removeClass("paper_footer-fabulous");
 * $('.paper_footer-tool:first').addClass("container_fuc-fabulous__flag");
 * $(obj).find('.paper_footer-tool:first').text(pubi18n.i18n_unlike+"("+count+")");
 * $(obj).find('.paper_footer-tool:first').removeClass("paper_footer-tool__box-click");
 * $(obj).find('.paper_footer-tool:first').addClass("paper_footer-tool__box-click"); } } }); } }); };
 */
Pub.getPdwhPubSareParam = function(obj) {
  // 初始化推荐联系人
  var des3ResId = $(obj).attr("des3ResId");
  if (des3ResId == null) {
    des3ResId = $(obj).attr("resId");
  }
  var pubId = $(obj).attr("pubId");
  var dbId = $(obj).attr("dbId");
  $("#share_to_scm_box").attr("dyntype", "GRP_SHAREPUB").attr("des3ResId", des3ResId).attr("pubId", pubId).attr(
      "resType", "22").attr("dbId", dbId);
  var objGetInfo = $(obj).closest(".dev_pub_list_div");
  var content = objGetInfo.find(".dev_pub_title").html();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);
};

// 实现邀请我的合作者成为联系人的勾选取消效果
Pub.initCheckBox = function() {
  if (document.getElementsByClassName("pub-idx__main_add-tip")) {
    var addlist = document.getElementsByClassName("pub-idx__main_add-tip");
    for (var i = 0; i < addlist.length; i++) {
      addlist[i].onclick = function() {
        if (this.innerHTML != "check_box") {
          this.innerHTML = "check_box";
        } else {
          this.innerHTML = "check_box_outline_blank";
        }
      }
    }
  }
};

// 实现导入新成果选择列表
Pub.changeliststate = function() {
  /* 检索页面start */
  if (document.getElementsByClassName("body_content-item_selector-tip")) {
    var closelist = document.getElementsByClassName("body_content-item_selector-tip");
    // 单选事件
    for (var i = 0; i < closelist.length; i++) {
      closelist[i].onclick = function() {
        Pub.stopBubble();
        if (this.classList.contains("body_content-item_selectored-tip")) {
          this.classList.remove("body_content-item_selectored-tip");
          // Pub.impSelectAll();
        } else {
          this.classList.add("body_content-item_selectored-tip");
          // Pub.impSelectAll();
        }
      }
    }
    // 除标题外，点击其他区域也可以选中
    $("div.main-list__item_content").each(function() {
      this.onclick = function() {
        var item = $(this).find(".body_content-item_selector-tip").get(0);
        if (item != undefined) {
          if (item.classList.contains("body_content-item_selectored-tip")) {
            item.classList.remove("body_content-item_selectored-tip");
            // Pub.impSelectAll();
          } else {
            item.classList.add("body_content-item_selectored-tip");
            // Pub.impSelectAll();
          }
        }
      }
    });
  }
  /* 检索页面end */
  /* 确认导入页面start */
  if (document.getElementsByClassName("item_selector-choose")) {
    var clicklist = document.getElementsByClassName("item_selector-choose");
    // 初始化，默认选中
    for (var i = 0; i < clicklist.length; i++) {
      if (!clicklist[i].classList.contains("item_selected-tip")) {
        clicklist[i].classList.add("item_selected-tip");
      }
      clicklist[i].onclick = function() {
        if (this.classList.contains("item_selected-tip")) {
          this.classList.remove("item_selected-tip");
          if (this.nextElementSibling) {
            this.nextElementSibling.value = "false";
          }
        } else {
          this.classList.add("item_selected-tip");
          if (this.nextElementSibling) {
            this.nextElementSibling.value = "true";
          }
        }
      }
    }
  }
  if (document.getElementsByClassName("item_selector-include")) {
    var clicklist = document.getElementsByClassName("item_selector-include");
    // 初始化
    for (var i = 0; i < clicklist.length; i++) {
      if (clicklist[i].nextElementSibling.checked) {
        clicklist[i].classList.add("item_selected-tip", "disabled");
      }
      clicklist[i].onclick = function() {
        if (this.classList.contains("disabled")) {
          return false;
        }
        if (this.classList.contains("item_selected-tip")) {
          this.classList.remove("item_selected-tip");
          this.nextElementSibling.checked = false;
        } else {
          this.classList.add("item_selected-tip");
          this.nextElementSibling.checked = true;
        }
      }
    }
  }
}
// 添加成果-全选事件判断
Pub.impSelectAll = function() {
  var selectAllBox = document.getElementsByClassName("body_content-item_selector-all")[0];
  var allBox = document.getElementsByClassName("body_content-item_selector-tip");
  var selectedBox = document.getElementsByClassName("body_content-item_selectored-tip");
  // 绑定全选事件
  $(selectAllBox).unbind("click").bind("click", function() {
    for (var i = 0, len = allBox.length; i < len; i++) {
      allBox[i].classList.remove("body_content-item_selectored-tip");
    }
    if (this.classList.contains("body_content-item_selectored-all")) {
      this.classList.remove("body_content-item_selectored-all");
    } else {
      this.classList.add("body_content-item_selectored-all");
      for (var j = 0, len = allBox.length; j < len; j++) {
        allBox[j].classList.add("body_content-item_selectored-tip");
      }
    }
  });

  if (allBox.length > 0) {
    if (selectedBox.length < allBox.length) {
      if (selectAllBox.classList.contains("body_content-item_selectored-all")) {
        selectAllBox.classList.remove("body_content-item_selectored-all");
      }
    } else {
      if (!selectAllBox.classList.contains("body_content-item_selectored-all")) {
        selectAllBox.classList.add("body_content-item_selectored-all");
      }
    }
  } else {
    selectAllBox.classList.remove("body_content-item_selectored-all");
  }
}
// 阻止冒泡事件
Pub.stopBubble = function() {
  var event = window.event || arguments.callee.caller.arguments[0];
  if (event.stopPropagation) {
    event.stopPropagation();
  } else {
    event.cancelBubble = true;
  }
};
// 收藏成果 pubDb:"PDWH","SNS"
// collectedPubBack回调函数
// 以下方法只左右个人库成果的收藏
Pub.dealSnsCollectedPub = function(pubId, pubDb, obj) {
  if (obj) {
    BaseUtils.doHitMore(obj, 500);
  }
  var collected = $(obj).attr("collected");
  $.ajax({
    url : "/pub/opt/ajaxCollect",
    type : 'post',
    data : {
      "des3PubId" : pubId,
      "pubDb" : pubDb,
      "collectOperate" : collected
    },
    dateType : 'json',
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data && data.result == "success") {
          $(obj).attr("collected", collected == "1" ? "0" : "1");
          if (typeof collectedPubBack == "function") {
            collectedPubBack(obj, collected, pubId, pubDb);
          } else {
            scmpublictoast(pubi18n.i18n_optSuccess, 1000);
          }
        } else if (data && data.result == "exist") {
          scmpublictoast(pubi18n.i18n_pubIsSaved, 1000);
        } else if (data && data.result == "isDel") {
          scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
        } else if (data && data.result == "not_exists") {
          scmpublictoast(pubi18n.i18n_pubIsDeleted, 1000);
        } else {
          scmpublictoast(pubi18n.i18n_pubImportError, 1000);
        }
      });
    },
    error : function(data) {
      scmpublictoast(pubi18n.i18n_pubImportError, 1000);
    }
  });
}
// 收藏成果 pubDb:"PDWH","SNS"
// collectedPubBack回调函数
// 下面此方法是需要检验成果的存在行，校验的是基准库成果，点击收藏作用的是个人库的方法是：Pub.dealSnsCollectedPub
Pub.dealCollectedPub = function(pubId, pubDb, obj) {
  Pub.pdwhIsExist2(pubId, function() {
    Pub.dealSnsCollectedPub(pubId, pubDb, obj);
  });
};
var positionfix = function(options) {
  var defaults = {
    screentarget : "",
  };
  var opts = Object.assign(defaults, options);
  if (opts.screentarget != "") {
    var parentbox = document.getElementsByClassName("bckground-cover")[0];
    parentbox.style.display = "block";
    var parentboxwidth = parentbox.offsetWidth;
    var parentboxheight = parentbox.offsetHeight;
    var sunboxwidth = opts.screentarget.offsetWidth;
    var sunboxheight = opts.screentarget.offsetHeight;
    opts.screentarget.style.bottom = (parentboxheight - sunboxheight) / 2 + "px";
    opts.screentarget.style.left = (parentbox.offsetWidth - opts.screentarget.offsetWidth) / 2 + "px";
    window.onresize = function() {
      var parentboxwidth = parentbox.offsetWidth;
      var parentboxheight = parentbox.offsetHeight;
      var sunboxwidth = opts.screentarget.offsetWidth;
      var sunboxheight = opts.screentarget.offsetHeight;
      opts.screentarget.style.bottom = (parentboxheight - sunboxheight) / 2 + "px";
      opts.screentarget.style.left = (parentbox.offsetWidth - opts.screentarget.offsetWidth) / 2 + "px";
    }
  }
};

function positioncenter(options) {
  var defaults = {
    targetele : "",
    closeele : ""
  };
  var opts = Object.assign(defaults, options);
  if ((opts.targetele == "") || (opts.closeele == "")) {
    alert("为传入必须的操作元素");
  } else {
    var postarget = document.getElementsByClassName(opts.targetele)[0];
    var opstclose = document.getElementsByClassName(opts.closeele);
    postarget.closest(".background-cover").style.display = "block";
    setTimeout(function() {
      postarget.style.left = (window.innerWidth - postarget.offsetWidth) / 2 + "px";
      postarget.style.bottom = (window.innerHeight - postarget.offsetHeight) / 2 + "px";
    }, 300);
    window.onresize = function() {
      postarget.style.left = (window.innerWidth - postarget.offsetWidth) / 2 + "px";
      postarget.style.bottom = (window.innerHeight - postarget.offsetHeight) / 2 + "px";
    }
    for (var i = 0; i < opstclose.length; i++) {
      opstclose[i].onclick = function() {
        this.closest("." + opts.targetele).style.bottom = -600 + "px";
        setTimeout(function() {
          postarget.closest(".background-cover").style.display = "none";
        }, 300);
      }
    }

  }
};
/**
 * 下载全文
 */
Pub.downloadFullText = function(downloadUrl) {
  if (downloadUrl != "") {
    Smate.confirm("下载提示", "要下载全文附件吗？", function() {
      window.location.href = downloadUrl;
    }, ["下载", "取消"]);
  }
  var Inputkeywords = function() {
    if (document.getElementsByClassName("new-importantkey_container-input")
        && document.getElementsByClassName("new-importantkey_container-item")) {
      var inputtarget = document.getElementsByClassName("new-importantkey_container-input")[0];
      var targetcontainer = document.getElementsByClassName("new-importantkey_container")[0];
      inputtarget.onfocus = function() {
        inputtarget.onkeydown = function(event) {
          var e = event || window.event || arguments.callee.caller.arguments[0];
          var settext = inputtarget.value;
          if (e.keyCode == "13") {
            if (document.getElementsByClassName("new-importantkey_container-item_checked").length < 1) {
              if (settext != "") {
                var inner = "";
                var inner = settext.trim();
                var content = document.createElement("div");
                content.className = "new-importantkey_container-item";
                var setbox = '<div class="new-importantkey_container-item_detaile" title=' + inner + '>' + inner
                    + '</div>' + '<div class="new-importantkey_container-item_colse">'
                    + '<i class="material-icons" onclick="deleteElement(this)">close</i>' + '</div>';
                content.innerHTML = setbox;
                inputtarget.value = "";
                targetcontainer.insertBefore(content, inputtarget);
              }
            }
          } else if ((e.keyCode == "186") || (e.keyCode == "229")) {
            e.stopPropagation();
            if (document.getElementsByClassName("new-importantkey_container-item_checked").length < 1) {
              if (settext != "") {
                var inner = "";
                var inner = settext.trim();
                var content = document.createElement("div");
                content.className = "new-importantkey_container-item";
                var setbox = '<div class="new-importantkey_container-item_detaile" title=' + inner + '>' + inner
                    + '</div>' + '<div class="new-importantkey_container-item_colse">'
                    + '<i class="material-icons" onclick="deleteElement(this)">close</i>' + '</div>';
                content.innerHTML = setbox;
                inputtarget.value = "";
                targetcontainer.insertBefore(content, inputtarget);
              }

            }
          } else if (e.keyCode == "37") {
            if (document.getElementsByClassName("new-importantkey_container-item_checked").length > 0) {
              if (document.getElementsByClassName("new-importantkey_container-item_checked")[0].previousElementSibling != undefined) {
                var targetele = document.getElementsByClassName("new-importantkey_container-item_checked")[0].previousElementSibling;
                document.getElementsByClassName("new-importantkey_container-item_checked")[0].classList
                    .remove("new-importantkey_container-item_checked");
                targetele.classList.add("new-importantkey_container-item_checked");
              } else {
                document.getElementsByClassName("new-importantkey_container-item_checked")[0].classList
                    .remove("new-importantkey_container-item_checked");
                document.getElementsByClassName("new-importantkey_container-item")[document
                    .getElementsByClassName("new-importantkey_container-item").length - 1].classList
                    .add("new-importantkey_container-item_checked");
              }
            } else {
              inputtarget.previousElementSibling.classList.add("new-importantkey_container-item_checked");
            }
          } else if (e.keyCode == "39") {
            if (document.getElementsByClassName("new-importantkey_container-item_checked").length > 0) {
              if (document.getElementsByClassName("new-importantkey_container-item_checked")[0].nextElementSibling != undefined) {
                var afterele = document.getElementsByClassName("new-importantkey_container-item_checked")[0].nextElementSibling;
                if (afterele.tagName == "INPUT") {
                  document.getElementsByClassName("new-importantkey_container-item_checked")[0].classList
                      .remove("new-importantkey_container-item_checked");
                  document.getElementsByClassName("new-importantkey_container-item")[0].classList
                      .add("new-importantkey_container-item_checked");
                } else {
                  document.getElementsByClassName("new-importantkey_container-item_checked")[0].classList
                      .remove("new-importantkey_container-item_checked");
                  afterele.classList.add("new-importantkey_container-item_checked");
                }
              }
            } else {
              document.getElementsByClassName("new-importantkey_container-item")[0].classList
                  .add("new-importantkey_container-item_checked");
            }
          } else if (e.keyCode == "8") {
            if (document.getElementsByClassName("new-importantkey_container-item_checked").length > 0) {
              targetcontainer
                  .removeChild(document.getElementsByClassName("new-importantkey_container-item_checked")[0]);
            }
          }
        }

      }
      inputtarget.onblur = function() {
        var settext = inputtarget.value;
        var inner = "";
        var innertxt = settext.trim() + "";
        if (innertxt.length > 0) {
          var content = document.createElement("div");
          content.className = "new-importantkey_container-item";
          var setbox = '<div class="new-importantkey_container-item_detaile" title=' + inner + '>' + inner + '</div>'
              + '<div class="new-importantkey_container-item_colse">'
              + '<i class="material-icons" onclick="deleteElement(this)">close</i>' + '</div>';
          content.innerHTML = setbox;
          inputtarget.value = "";
          targetcontainer.insertBefore(content, inputtarget);
        }
      }

    }
  }
  var deleteElement = function(obj) {
    obj.closest(".new-importantkey_container").removeChild(obj.closest(".new-importantkey_container-item"));
  }
}
