var SmateCommon = SmateCommon ? SmateCommon : {}

// 添加访问记录
SmateCommon.addVisitRecord = function(vistPsnDes3Id, actionDes3Key, actionType) {
  if (vistPsnDes3Id != null && vistPsnDes3Id != "" && actionDes3Key != null && actionDes3Key != ""
      && actionType != null && actionType != "") {
    $.ajax({
      url : "/psnweb/outside/addVistRecord",
      type : "post",
      dataType : "json",
      data : {
        "vistPsnDes3Id" : vistPsnDes3Id,
        "actionDes3Key" : actionDes3Key,
        "actionType" : actionType
      },
      success : function(data) {
      },
      error : function() {
      }
    });
  }
}

// 返回上一页
SmateCommon.goBack = function(targetUrl) {
  // 有些根据需求修改返回了url跳转，没有用history.back或者go(-1)，导致错乱。
  var historyMapping = {
    // "current":[history,forward] - 当前页（current）页面从history返回按钮跳转来的,forward是当前页返回按钮真正需要跳转的,有需要可以继续添加
    "/psnweb/mobile/homepage" : ["/pub/querylist/psn", "/psnweb/mobile/myhome"],
    "psnweb/mobile/homepag" : ["/pub/outside/querylist/psn", "/psnweb/mobile/myhome"],
    "m/pub/search/main" : ["/pub/paper/search", targetUrl],
    "pub/search/main" : ["/pub/patent/search", targetUrl],
    "/pub/search/main" : ["/psnweb/mobile/search", targetUrl]
  };
  var current = document.location.href, history = document.referrer;
  if (history == "" || typeof (history) == "undefined") {
    document.location.href = targetUrl;
  } else {
    var url = "";
    Object.keys(historyMapping).forEach(function(x) {
      if (current.indexOf(x) != -1 && history.indexOf(historyMapping[x][0]) != -1) {
        url = historyMapping[x][1];
      }
    });
    if (history.indexOf("/prjweb/wechat/findfunds") > 0 && $("#fund_listPage")) {// 基金列表有表单，不能用back
      $("#fund_listPage").submit();
      return;
    }
    // 上一页如果是登录页面的，都跳转到动态列表页面去
    if (history.indexOf("/oauth/mobile/index") != -1 || history.indexOf("/oauth/index") != -1) {
      url = "/dynweb/mobile/dynshow";
    }
    if (url != "") {
      document.location.replace(url);
    } else {
      window.history.back();
    }
  }
};
SmateCommon.goBackReferrer = function() {
  var referrer = document.referrer;
  if (referrer && referrer != "") {
    document.location.href = document.referrer;
  } else {
    document.location.href = "/dynweb/mobile/dynshow";
  }
};

// 添加联系人
SmateCommon.addIdentificFriend = function(receivePsnId, obj, addFriendSuccess, addFriendError) {
  if (obj) {
    BaseUtils.doHitMore(obj, 3000);
  }
  $.ajax({
    url : '/psnweb/friend/ajaxaddfriend',
    type : 'post',
    data : {
      'des3Id' : receivePsnId
    },
    dataType : 'json',
    timeout : 10000,
    success : function(data) {
      addFriendSuccess(data);

    },
    error : function() {
      addFriendError(data);
    }
  });
}
// 删除已经选中的联系人
SmateCommon.commonDelSelectedFriend = function(obj, event) {
  if (event.keyCode == 8) {
    if ($(obj).html() != "") {// 当存在内容时不删除前面的元素，只执行普通的删除
      return;
    }
    var selectedFriends = $(obj).parent().find("div[class='chip__box']");
    $(selectedFriends[selectedFriends.length - 1]).remove();
  }
}

/**
 * 判断是否是隐私成果: (1)他人分享:成果隐私设置为仅作者本人可看 (2)本人分享:请先将本成果隐私设置为公开 根据type不同做不同跳转
 */
SmateCommon.checkPubAnyUser = function(des3PubId, type, param) {
  var dataParam = {};
  dataParam.des3PubId = des3PubId;
  if (param && param.des3GrpId) {
    dataParam.des3GrpId = param.des3GrpId;
  }
  $.ajax({
    url : "/pub/details/ajaxgetpubanyuser",
    type : "post",
    dataType : "json",
    data : dataParam,
    success : function(data) {
      if (data.isAnyUser == "7") {
        if (type == "pubList") {
          // 需求变更,请参照方法备注
          SmateCommon.mobileShareEntrance(des3PubId, "sns");
          // $('.dev_publist_share').val(des3PubId);
          // $('#dynamicShare').show();
        } else if (type == "grpPubList") {
          SmateCommon.mobileShareEntrance(des3PubId, "sns", "", param.des3GrpId);
        } else if (type == "insideMsg") {
          midmsg.comsendmsg(param, function() {
            midmsg.loadMsgChatList(param.receiverIds, 1, "html");
            // midmsg.oldBack="midmsg.showchatpsnUI()";
            midmsg.showpubUIBack();
          });
        } else if (type == "pubCollect") {
          // 需求变更,进入页面分享
          /*
           * $("#shareScreen").attr("des3ResId", param.des3ResId); $("#shareScreen").attr("resType",
           * param.pubDB == "PDWH" ? 22 : 1); $('#dynamicShare').show();
           */
          SmateCommon.mobileShareEntrance(param.des3ResId, param.pubDB.toLowerCase());
        } else if (type == "dynamic") {
          dynamic.shareDynCommon(param.des3DynId, param.dynType, param.des3ResId, param.resType, param.ev);
        } else if (type == "publishDyn") {
          $("#dyndes3pubId").val(des3PubId);
          var title = $(param).find(".pubTitle").html();
          if (title.length >= 255) {
            title = title.substring(0, 255) + "......";
          }
          $("#dynpubtitle").html(title);
          $("#dynselect").show();
        } else if (type == "snsDetail") {
          SmateCommon.mobileShareEntrance(des3PubId, "snsDetail");
        }
      } else {
        if (data.isSelf == "true") {
          newMobileTip("请先将本成果隐私设置为公开", 2, 2000);
        } else {
          newMobileTip("成果隐私设置为仅作者本人可看", 2, 2000);
        }
      }
    },
    error : function(e) {
      console.log(e);
    }
  });
}
SmateCommon.shareRes = function(des3ResId, resType, param) {
  var dataParam = {};
  dataParam.des3PubId = des3ResId;
  if (param && param.des3GrpId) {
    dataParam.des3GrpId = param.des3GrpId;
  }
  $.ajax({
    url : "/pub/details/ajaxgetpubanyuser",
    type : "post",
    dataType : "json",
    data : dataParam,
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
        if (data.hasDeleted) {// 成果已经删除
          scmpublictoast(dynCommon.pubIsDeleted, 1000, 3);
          return;
        }
        if (data.isAnyUser == "7") {
          window.location.href = "/psn/share/page?des3ResId=" + encodeURIComponent(des3ResId) + "&resType=" + resType
              + "&currentDes3GrpId=" + encodeURIComponent(param.des3GrpId);
        } else {
          if (data.isSelf == "true") {
            newMobileTip("请先将本成果隐私设置为公开", 2, 2000);
          } else {
            newMobileTip("成果隐私设置为仅作者本人可看", 2, 2000);
          }
        }
      });
    },
    error : function(e) {
      console.log(e);
    }
  });
}
/**
 * 移动端项目/个人库成果/基准库成果/基金/资助机构分享统一入口(不与动态分享统一,因动态涉及种类太多) 接收三个参数,一个为资源id,一个是分享类型,一个是查询字符串(基准库回显查询使用)
 */
SmateCommon.mobileShareEntrance = function(des3ResId, shareType, queryStr, des3GrpId) {
  /**
   * 传递包含特殊字符的字符串需要通过encodeURIComponent编码,否则后台会乱码
   */
  var url = "/dyn/mobile/resshare?des3ResId=" + encodeURIComponent(des3ResId) + "&shareType=" + shareType
      + "&des3GrpId=" + des3GrpId;
  if (typeof queryStr != typeof undefined) {
    url += "&queryStr=" + queryStr;// 该参数作为除des3ResId和shareType以外,其他还需要传递的参数,均通过此参数传递
  }
  window.location.href = url;
}
/**
 * 移动端项目/个人库成果/基准库成果/基金/资助机构生成动态入口 接收分享类型shareType
 */
SmateCommon.resShareToDyn = function(shareType) {
  var resHistoryPage = $("#resHistoryPage").val();
  var des3ResId = $("#des3ResId").val();
  var dynText = $.trim($("#id_sharegrp_textarea").val()).replace(/\n/g, '<br>');// 分享留言
  // 以下参数为默认的(项目分享)
  var dynType = "ATEMP";
  // 带留言分享和不带留言分享使用不同的模板
  if (dynText == "") {
    dynType = "B2TEMP";
  }
  var resType = 4;
  var des3PubId = des3ResId;
  var operatorType = 3;
  var databaseType = "";
  if (shareType == "sns" || shareType == "snsDetail") {
    resType = 1;
    databaseType = 2;
  }
  if (shareType == "pdwh" || shareType == "pdwhDetail") {
    resType = 22;
    databaseType = 2;
  }
  if (shareType == "aidIns") {
    resType = 25;
  }
  if (shareType == "news") {
    resType = 26;
  }
  var resInfoJson = "";
  // 点资助机构标题进入详情是基金
  if (shareType == "fund" || shareType == "fundDetail" || shareType == "aidInsDetail") {
    resType = 11;
    resInfoJson = JSON.stringify({
      "fund_desc_zh" : $("#zhShowDesc").val(),
      "fund_desc_en" : $("#enShowDesc").val(),
      "fund_title_zh" : $("#zhTitle").val(),
      "fund_title_en" : $("#enTitle").val()
    });
  }
  var data = {
    "dynType" : dynType,
    "dynText" : dynText,
    "resType" : resType,
    "des3PubId" : des3PubId,
    "operatorType" : operatorType,
    "des3ResId" : des3ResId,
    "databaseType" : databaseType,
    "resInfoJson" : resInfoJson
  };
  $.ajax({
    url : "/dynweb/dynamic/ajaxrealtime",
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      if (data.result == "success") {
        scmpublictoast("分享成功", 2000);
        if (dynType == "ATEMP") {// 如果不带留言分享模板分享时已经做了统计
          if (shareType == "prj") {
            // 分享成功显示完后跳转至项目显示页
            SmateCommon.updateStatistics("prj", des3ResId, '');
            // window.location.href = "/prjweb/wechat/prjmain";
          } else if (shareType == "sns") {
            // 分享成功显示完后跳转至个人成果显示页
            SmateCommon.updateStatistics("sns", des3ResId, dynText);
            // window.location.href = "/pub/outside/querylist/psn";
          } else if (shareType == "pdwh") {
            // 分享成功显示完后跳转至基准库成果显示页
            SmateCommon.updateStatistics("pdwh", des3ResId, dynText);
            // window.location.href =
            // "/pub/paper/search?serviceType=paperListInSolr&searchString="+$("#queryStr").val();
          } else if (shareType == "fund") {
            // 分享成功显示完后跳转至基金显示页
            SmateCommon.updateStatistics("fund", des3ResId, '');
            // window.location.href = "/prjweb/wechat/findfunds?module=recommend";
          } else if (shareType == "snsDetail") {
            // 分享成功显示完后返回成果详情页
            SmateCommon.updateStatistics("snsDetail", des3ResId, dynText);
            // window.location.href = "/pub/details/sns?des3PubId="+encodeURIComponent(des3ResId);
          } else if (shareType == "prjDetail") {
            // 分享成功显示完后返回资助机构显示页面
            SmateCommon.updateStatistics("prjDetail", des3ResId, '');
            // window.location.href =
            // "/prjweb/wechat/findprjxml?des3PrjId="+encodeURIComponent(des3ResId);
          } else if (shareType == "pdwhDetail") {
            // 分享成功显示完后返回基准库详情显示页面
            SmateCommon.updateStatistics("pdwhDetail", des3ResId, '');
            // window.location.href =
            // "/pub/details/pdwh?des3PubId="+encodeURIComponent(des3ResId);
          } else if (shareType == "fundDetail") {
            // 分享成功显示完后返回基金详情显示页面
            SmateCommon.updateStatistics("fundDetail", des3ResId, '');
            // window.location.href =
            // "/prjweb/wechat/findfundsxml?des3FundId="+encodeURIComponent(des3ResId);
          } else if (shareType == "aidInsDetail") {
            // 分享成功显示完后返回资助机构下的基金显示
            SmateCommon.updateStatistics("aidInsDetail", des3ResId, '');
            // 由于资助机构跳转需要保存其des3FundAgencyId,所以通过queryStr保存传递
            // window.location.href =
            // "/prj/mobile/agencydetail?des3FundAgencyId="+encodeURIComponent($("#queryStr").val());
          }
        }
        // 因之前资助机构在后台没有做分享统计,是另外发起的分享统计,所以本次改造中不论是带留言还是不带留言都应该发起分享统计请求
        if (shareType == "aidIns") {
          SmateCommon.updateStatistics("aidIns", des3ResId, '');
          // window.location.href =
          // "/prj/mobile/fundagency?flag=condition&searchKey="+$("#queryStr").val();
        }
        // 不管是哪种分享,分享完毕后都应该跳回来源页
        setTimeout(function() {
          window.history.go(-1);
        }, 2000);
      } else {
        scmpublictoast("分享失败", 2000);
      }

    },
    error : function(e) {
      scmpublictoast("分享失败", 2000);
    }
  });
}

/**
 * 校验是否超过最大字数
 */
SmateCommon.isSupassMax = function() {
  var value = $("#id_sharegrp_textarea").val();
  if (value.length > 500) {
    scmpublictoast("最大限制输入500个字符", 2000);
    $("#id_sharegrp_textarea").val(value.substring(0, 500));
  }
}
/**
 * 分享成功后更新统计数
 */
SmateCommon.updateStatistics = function(shareType, des3ResId, dynText) {
  var url = "";
  var shareToPlatform = "";
  var des3PubId = "";
  var des3PdwhPubId = "";
  var platform = 1;// 分享至动态
  if (shareType == "aidIns") {
    url = "/prjweb/share/ajaxupdate";
    shareToPlatform = 1;
  } else if (shareType == "fund" || shareType == "fundDetail" || shareType == "aidInsDetail") {
    url = "/dynweb/dynamic/fundsharenum";
  } else if (shareType == "sns" || shareType == "snsDetail") {
    url = "/dynweb/dynamic/snssharenum";
    des3PubId = des3ResId;
  } else if (shareType == "pdwh" || shareType == "pdwhDetail") {
    url = "/dynweb/dynamic/pdwhsharenum";
    des3PdwhPubId = des3ResId;
  } else if (shareType == "prj" || shareType == "prjDetail") {
    url = "/dynweb/dynamic/prjsharenum";
  }
  var data = {
    "des3ResId" : des3ResId,
    "Des3FundAgencyId" : des3ResId,
    "shareToPlatform" : shareToPlatform,
    "des3PubId" : des3PubId,
    "des3PdwhPubId" : des3PdwhPubId,
    "dynText" : dynText,
    "platform" : platform
  };
  $.ajax({
    url : url,
    type : "post",
    data : data,
    dataType : "json",
    success : function(data) {
      // 更新成功由于是重新请求,所以无需再做任何操作
    },
    error : function() {
    }
  });
}
/**
 * 添加没有更多记录提示,仅适用于mainlist插件
 */
SmateCommon.noMoreRecordTips = function() {
  var addObj = $("div[no-more-record='no_more_record_tips']");
  var mainListItems = $(".main-list__item");
  var msg = "<div no-more-record='no_more_record_tips_sub' style='border:none;margin-left:20px;padding-left:0px;'><div style='padding:20px;color:#999;text-align:center;'>没有更多记录</div></div>";
  if (mainListItems.length > 0) {
    // 加之前先移除一次,防止重复添加
    if (addObj.find("div[no-more-record='no_more_record_tips_sub']").length > 0) {
      addObj.find("div[no-more-record='no_more_record_tips_sub']").remove();
    }
    addObj.append(msg);
  } else {
    addObj.find("div[no-more-record='no_more_record_tips_sub']").remove();
  }
}
/**
 * 加载基金图片,基金推荐/收藏/发现/资助机构详情均需要使用
 */
SmateCommon.loadFundLogos = function(des3FundIds, hasLogin) {
  var URI = "/prjweb/fund/ajaxrecommendlogo";
  // 未登录
  if (typeof hasLogin != typeof undefined && hasLogin && hasLogin.val() == "0") {
    URI = "/prjweb/outside/fund/ajaxrecommendlogo";
  }
  $.ajax({
    url : URI,
    type : "post",
    data : {
      "des3FundAgencyIds" : encodeURIComponent(des3FundIds)
    },
    dataType : "json",
    success : function(data) {
      if (data) {
        for (var i = 0; i < data.length; i++) {
          if (data[i].logoUrl != null && data[i].logoUrl.trim() != "") {
            // 基金都是显示资助机构的logo
            $("img[logo_id='logo_" + data[i].fundAgencyId + "']").attr("src", data[i].logoUrl);
          }
        }
      }
    }
  });
}

/**
 * 移动端分享群组/联系人统一入口 接收参数:1、加密资源id(des3ResId),2、资源类型(resType)
 */
SmateCommon.sharePsnOrGrpEntrance = function(des3ResId, resType) {
  var des3GrpId = $("#m_grp_file_des3GrpId").val();
  window.location.href = "/psn/mobile/resshare?des3ResId=" + encodeURIComponent(des3ResId) + "&resType=" + resType
      + "&des3GrpId=" + encodeURIComponent(des3GrpId);
}

// 添加 或者 修改 url中参数的值
SmateCommon.UpdateUrlParam = function(name, val) {
  var thisURL = document.location.href;
  // 如果 url中包含这个参数 则修改
  if (thisURL.indexOf(name + '=') > 0) {
    var v = SmateCommon.getUrlParam(name);
    if (v != null) {
      // 是否包含参数
      thisURL = thisURL.replace(name + '=' + v, name + '=' + val);

    } else {
      thisURL = thisURL.replace(name + '=', name + '=' + val);
    }

  } else {
    if (thisURL.indexOf('?') > 0) {
      thisURL += "&" + name + '=' + val;
    } else {
      thisURL += "?" + name + '=' + val;
    }
  }
  window.history.replaceState({}, "", thisURL);
};
SmateCommon.getUrlParam = function(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
  var r = window.location.search.substr(1).match(reg); // 匹配目标参数
  // 不需要解码 因为前面由于浏览器自带的编码原因（url中文会转码） 解码之后会乱码
  if (r != null) {
    return r[2];
  }
  return null; // 返回参数值
}
