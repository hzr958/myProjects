var Group = Group ? Group : {};
var GroupDyn = GroupDyn ? GroupDyn : {};
var DiscussOpenDetail = DiscussOpenDetail ? DiscussOpenDetail : {}

//人员主页
Group.openPsnDetail = function(des3PsnId, event) {
  window.location.href = "/psnweb/mobile/outhome?des3ViewPsnId=" + encodeURIComponent(des3PsnId);
  BaseUtils.stopNextEvent(event);
}

//打开资源详情
Group.openResDetails = function(des3ResId, resType, event, resNotExists, clickPart){
  if(resNotExists == "true" && clickPart != "title"){
    scmpublictoast("资源已删除", 2000);
  }else{
    switch (resType) {
      case "pub" :
        location.href = "/pub/outside/details/snsnonext?des3PubId=" + encodeURIComponent(des3ResId);
        break;
      case "fund" :
        location.href = "/prjweb/wechat/findfundsxml?des3FundId=" + encodeURIComponent(des3ResId);
        break;
      case "pdwhpub" :
        location.href = "/pub/details/pdwh?des3PubId=" + encodeURIComponent(des3ResId);
        break;
      case "prj" :
        location.href = "/prjweb/wechat/findprjxml?des3PrjId=" + encodeURIComponent(des3ResId);
        break;
      case "agency" :
        location.href = "/prj/outside/agency?des3FundAgencyId=" + encodeURIComponent(des3ResId);
        break;
      case "grpfile":
      case "file":
        Smate.confirm("下载提示", "要下载该文件吗？", function() {
          Group.openDynFile(des3ResId, 1);
        }, ["下载", "取消"]);
        break;
      default :
        break;
    }
  }
  BaseUtils.stopNextEvent(event);
}



//处理全文
Group.dealWithFulltext = function(resType, des3ResId, des3FileId, resOwnerDes3Id, event, resNotExists){
  if(resNotExists == "true"){
    scmpublictoast("资源已删除", 2000);
  }else{
    if(resType == "pub"){
      Group.dealWithSNSFulltext(des3ResId, des3FileId, resOwnerDes3Id);
    }else if(resType == "pdwhpub"){
      Group.dealWithPdwhFulltext(des3ResId, des3FileId, resOwnerDes3Id);
    }
  }
  BaseUtils.stopNextEvent(event);
}



//下载或请求个人库全文
Group.dealWithSNSFulltext = function(des3ResId, des3FileId, resOwnerDes3Id){
  var des3UserId = $("#des3CurrentPsnId").val();
  var isOwner = resOwnerDes3Id == des3UserId;
  if(des3FileId){
    Group.downloadFulltext(des3ResId, des3FileId, resOwnerDes3Id);
  }else{
    if(isOwner){
      scmpublictoast("请在电脑端上传全文", 2000, 1);
    }else{
      Smate.confirm("提示", "是否向作者请求全文？", function() {
        Group.requestFullText(des3ResId, 'sns', resOwnerDes3Id, null)
      }, ["确定", "取消"]);
    }
  }
}


//下载或请求基准库全文
Group.dealWithPdwhFulltext = function(des3ResId, des3FileId, resOwnerDes3Id){
  if(des3FileId){
    Smate.confirm("下载提示", "要下载全文附件吗？", function() {
      Group.openDynFile(des3ResId, 3);
    }, ["下载", "取消"]);
  }else{
    Smate.confirm("提示", "是否向作者请求全文？", function() {
      Group.requestFullText(des3ResId, "pdwh", resOwnerDes3Id, null);
    }, ["确定", "取消"]);
  }
}



//下载全文
Group.downloadFulltext = function(des3ResId, des3FileId, resOwnerDes3Id){
  var userId = $("#des3CurrentPsnId").val();
  $.ajax({
    url : "/pub/fulltext/ajaxpermission",
    type : "post",
    data : {
      "des3SearchPubId" : des3ResId,
      "des3FileId" : des3FileId
    },
    dataType : "json",
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function(){
        if (data.result) {
          if (data.fullTextPermission != 0 && resOwnerDes3Id != userId) {
            // 全文隐私则发送全文请求
            Smate.confirm("提示", "全文未公开，是否请求查看？", function(){
              Group.requestFullText(des3ResId, 'sns', resOwnerDes3Id, null);
            }, ["确定", "取消"]);
            return;
          }
          Smate.confirm("下载提示", "要下载全文附件吗？", function() {
            Group.openDynFile(des3ResId, 2);
          }, ["下载", "取消"]);
        }
      });
    },
    error: function(){
      
    }
  });
}


//请求全文 requestPubType=sns/pdwh
Group.requestFullText = function(des3ResId, requestPubType, ownerDes3Id, myFunction){
  $.ajax({
    url : '/pub/fulltext/ajaxreqadd',
    type : 'post',
    data : {
      'des3RecvPsnId' : ownerDes3Id,
      'des3PubId' : des3ResId,
      'pubType' : requestPubType
    },
    dataType : "json",
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function(){
        if (data.status == "success") { // 全文请求保存成功
          if (typeof myFunction == 'function') {
            myFunction();
          } else {
            // 用scmpublictoast提示，这里面有判断是移动端还是pc端
            scmpublictoast("全文请求已发送", 2000, 1);
          }
        } else if (data.status == "isDel") {
          scmpublictoast("资源已删除", 2000, 3);
        } else {
          scmpublictoast("操作失败", 2000, 2);
        }
      });
    },
    error: function() {
      scmpublictoast("网络异常", 2000, 2);
    }
  });
}



//下载文件
Group.openDynFile = function(des3ResId, resType) {
  var dataJson = {
    "des3Id" : des3ResId,
    "type" : resType
  };
  $.ajax({
    url : '/fileweb/download/ajaxgeturl',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(result) {
      BaseUtils.ajaxTimeOutMobile(result, function(){
        if (result.status == "success") {
          window.location.href = result.data.url;
        }else{
          scmpublictoast("下载失败", 2000, 3);
        }
      });
    },
    error : function() {
    }
  });
};



//查看详细信息
Group.goToDetails = function(des3GrpId){
  window.location.href = "/grp/outside/main?details=1&des3GrpId=" + encodeURIComponent(des3GrpId);
}

//加载动态列表
Group.loadGrpDynList = function(){
  var url = $("#isLogin").val()=="false" ? "/grp/outside/list/ajaxdyn" : "/grp/list/ajaxdyn";
  Group.dynListReq = window.Mainlist({
    name : "mobile_grp_dyn_model",
    listurl : url,
    listdata : {
      "des3GrpId": encodeURIComponent($("#des3GrpId").val())
    },
    method : "scroll",
    listcallback : function(xhr) {
      var currentNumItem = $(".main-list__item").length;
      var totalCount = $(".main-list__list").attr("total-count");
      if (totalCount>0 && Number(currentNumItem) >= Number(totalCount)) {
        $(".main-list__list")
            .append(
                "<div class='paper_content-container_list main-list__item main-list__item-nonebox' style='border:none; border-bottom:0px!important;'><div class='main-list__item-nonetip'>没有更多记录</div></div>");
      }
      if (totalCount <= 0) {
        $("div.response_no-result").remove();
        $(".main-list__list").append("<div class='paper_content-container_list main-list__item' style='border-bottom: 0px dashed #ccc!important;'><div class='response_no-result'>未找到符合条件的记录</div></div>");
      }
    }
  });
}



//群组动态赞
GroupDyn.award = function(des3DynId, resType, des3ResId, dynType, obj) {
  if (resType == "pdwhpub") {
    //TODO 判断成果是否存在
    /*dynamic.pdwhIsExist(des3resid, function() {
    });*/
    GroupDyn.awardCommonPart(des3DynId, resType, des3ResId, dynType, obj);
  } else {
    GroupDyn.awardCommonPart(des3DynId, resType, des3ResId, dynType, obj);
  }
};

/**
 * 群组动态点赞公共部分
 */
GroupDyn.awardCommonPart = function(des3DynId, resType, des3ResId, dyntype, obj) {
  $.ajax({
    url : '/grp/dynopt/ajaxaward',
    type : 'post',
    dataType : 'json',
    data : {
      "des3DynId" : des3DynId
    },
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
          if(data.result == "success"){
            var awardCount = data.awardCount > 999 ? "1K+" : data.awardCount;
            var post_data = {
                "resType" : 1,
                "resNode" : 1
            };
            var awardOperation = 0;
            if (true == data.hasAward) {
              $(obj).find("span").text("取消赞(" + awardCount + ")");
              post_data.status = '1';
              awardOperation = 1;
            } else {
              post_data.status = '0';// 取消赞
              awardOperation = 0; // 取消赞
              if (0 == awardCount) {
                $(obj).find("span").text("赞");
              } else {
                $(obj).find("span").text("赞(" + awardCount + ")");
              }
            }
            // 同步成果 基金 赞 start
            post_data.des3ResId = des3ResId;
            post_data.operate = awardOperation;
            if (des3ResId !== undefined && des3ResId !== "") {
              if ("GRP_ADDPUB" == dyntype || "GRP_PUBLISHDYN" == dyntype || "GRP_SHAREPUB" == dyntype) {// 成果赞
                post_data.des3PubId = des3ResId;
                GroupDyn.syncPubAward(obj, post_data);
              }
              if("GRP_SHAREPDWHPUB" == dyntype){//基准库成果
                GroupDyn.syncPdwhPubAward(obj, post_data);
              }
              if ("GRP_SHAREFUND" == dyntype) {// 基金赞  0赞, 1取消赞
                GroupDyn.syncFundAward(obj, des3ResId, 1 - awardOperation);
              }
              if ("GRP_SHAREPRJ" == dyntype) {// 同步项目赞
                GroupDyn.synchronizePrjAward(post_data);
              }
              if ("GRP_SHAREAGENCY" == dyntype) {// 资助机构赞
                GroupDyn.syncAgencyAward(obj, des3ResId, awardOperation);
              }
            }
          }else{
            //TODO 提示操作失败
          }
      }, 3000);
    },
    error : function() {
      //TODO 提示操作失败
    }
  });
}



//同步资助机构的赞
GroupDyn.syncAgencyAward = function(obj, des3ResId, awardOperation) {
  if ($.trim($(obj).closest(".discuss_box").find(".dyn_content").html()) == "") {
    $.ajax({
      url : "/prj/mobile/ajaxAward",
      type : "post",
      data : {
        "des3FundAgencyId" : des3ResId,
        "optType" : awardOperation
      },
      dataType : "json",
      success : function(data) {
      },
      error : function() {
      }
    });
  }
}

// 同步基金的赞
GroupDyn.syncFundAward = function(obj, des3ResId, awardOperation) {
  if ($.trim($(obj).closest(".discuss_box").find(".dyn_content").html()) == "") {
    $.ajax({
      url : "/prjweb/fund/ajaxaward",
      type : "post",
      data : {
        "encryptedFundId" : des3ResId,
        "awardOperation" : awardOperation
      },
      dataType : "json",
      success : function(data) {
      },
      error : function() {
      }
    });
  }
}

// 同步成果赞
GroupDyn.syncPubAward = function(obj, post_data) {
  if ($.trim($(obj).closest(".discuss_box").find(".dyn_content").html()) == "") {
    $.ajax({
      url : '/pub/optsns/ajaxlike',
      type : 'post',
      dataType : 'json',
      data : post_data,
      success : function(data) {
      },
      error : function() {
      }
    });
  }
}


//同步项目的赞 des3ResId status 1=点赞 ； 0=取消赞
GroupDyn.synchronizePrjAward = function(post_data) {
  if ($.trim($(obj).closest(".discuss_box").find(".dyn_content").html()) == "") {
    $.ajax({
      url : "/prj/opt/ajaxaward",
      type : "post",
      data : {
        "optType": post_data.status,
        "des3Id" : post_data.des3ResId
      },
      dataType : "json",
      success : function(data) {
        if (data.result == "success") {
        }
      },
      error : function(date) {
      }
    });
  }
}


//同步基准库成果赞
GroupDyn.syncPdwhPubAward = function(obj, post_data) {
  if ($.trim($(obj).closest(".discuss_box").find(".dyn_content").html()) == "") {
    $.ajax({
      url : '/pub/optpdwh/ajaxlike',
      type : 'post',
      dataType : 'json',
      data : {
        "des3PdwhPubId":post_data.des3ResId,
        "operate":post_data.operate
      },
      success : function(data) {
      },
      error : function() {
      }
    });
  }
}

//显示动态评论
GroupDyn.showDiscussCommentList = function(obj) {
  var resType = $(obj).closest(".main-list__item").find("input[name='dynId']").attr("restype");
  var des3resid = $(obj).closest(".main-list__item").find("input[name='dynId']").attr("des3resid");
  if (resType == "pdwhpub") {
    //TODO 判断成果是否存在
//    dynamic.pdwhIsExist(des3resid, function() {
//    });
    GroupDyn.commentCommonPart(obj);
  } else {
    GroupDyn.commentCommonPart(obj);
  }

};
/**
 * 评论公共部分
 */
GroupDyn.commentCommonPart = function(obj) {
  var boxObj = $(obj).parent().parent();
  var showObj = $(boxObj).find(".grp_discuss_comment_box");
  if ($(showObj).css("display") == "none") {
    $(showObj).show();
  } else {
    $(showObj).hide();
    return;
  }
  $(boxObj).find(".grp_discuss_sample_comment").hide();
  GrpDiscussComment.loadDiscussCommentList(obj);
}




//动态中个人库、基准库成果收藏、取消收藏
GroupDyn.dealCollectedPub = function(pubId, pubDb, obj, resNotExists) {
  if(resNotExists == "true"){
    scmpublictoast("资源已删除", 2000);
  }else{
    if (obj) {
      BaseUtils.doHitMore(obj, 500);
    }
    if (pubDb == 'pub') {
      pubDb = "SNS";
    } else if (pubDb == 'pdwhpub') {
      pubDb = "PDWH";
    }
    var collected = $(obj).attr("collected"); // 0==收藏 ， 1==取消收藏
    $.ajax({
      url : "/pub/optsns/ajaxcollect",
      type : 'post',
      data : {
        "des3PubId" : pubId,
        "pubDb" : pubDb,
        "collectOperate" : collected
      },
      dateType : 'json',
      success : function(data) {
        BaseUtils.ajaxTimeOutMobile(data, function(){
          if (data && data.result == "success") {
            $(obj).attr("collected", collected == "1" ? "0" : "1");
            $(obj).find("span").html(collected == "1" ? "收藏" : "取消收藏");
          } else if (data && data.result == "exist") {
            scmpublictoast("已收藏该成果", 2000);
          } else if (data && data.result == "isDel") {
            scmpublictoast("成果不存在", 2000);
          } else {
            scmpublictoast("操作失败", 2000);
          }
        });
      },
      error : function(data) {
        scmpublictoast("操作失败", 2000);
      }
    });
  }
}



//动态中收藏文件
GroupDyn.grpDyncollectionGrpFile = function(obj, resNotExists) {
  if(resNotExists == "true"){
    scmpublictoast("资源已删除", 2000);
  }else{
    if (obj) {
      BaseUtils.doHitMore(obj, 2000);
    }
    var fileData = $(obj).closest(".dynamic__box").find("input[name='dynId']");
    if (fileData != null) {
      var resId = fileData.attr("resid");
      $.ajax({
        url : '/grp/collect/ajaxfile',
        type : 'post',
        dataType : 'json',
        data : {
          'des3GrpFileId' : resId
        },
        success : function(data) {
          BaseUtils.ajaxTimeOutMobile(data, function(){
            if (data.status == "success") {
              scmpublictoast("添加成功", 2000);
            } else{
              scmpublictoast("操作失败", 2000);
            }
          });
        },
        error : function() {
          scmpublictoast("操作失败", 2000);
        }
      });
    }
  }
}


//动态中收藏、取消收藏基金
GroupDyn.dynCollectCoperation = function(obj, id, type, fundId, resNotExists){
  if(resNotExists == "true"){
    scmpublictoast("资源已删除", 2000);
  }else{
    if (obj) {
      BaseUtils.doHitMore(obj, 2000);
    }
    $.ajax({
      url: "/prj/optfund/ajaxcollect",
      type: "post",
      data: {
        "des3FundId" : id,
        "optType": type
      },
      dataType: "json",
      success: function(data){
        BaseUtils.ajaxTimeOutMobile(data, function(){
          if(data.result == "success"){
            if(type == 0){
              $(".collect_"+fundId).hide();
              $(".collectCancel_"+fundId).show();
            }else{
              $(".collect_"+fundId).show();
              $(".collectCancel_"+fundId).hide();
            }
          }else{
            scmpublictoast("操作失败", 2000);
          }
        });
      },
      error: function(){
        scmpublictoast("操作失败", 2000);
      }
    });
  }
}


//动态中的关注、取消关注资助机构操作 des3AgencyId: 加密的资助机构ID, optType: 1(关注), 0(取消关注)
GroupDyn.ajaxDynamicInterest = function(obj, des3AgencyId, optType, resNotExists) {
  if(resNotExists == "true"){
    scmpublictoast("资源已删除", 2000);
  }else{
    $(obj).removeAttr("onclick");
    $.ajax({
      url : "/prj/agency/ajaxinterest",
      type : "post",
      data : {
        "des3FundAgencyId" : des3AgencyId,
        "optType" : optType
      },
      dataType : "json",
      success : function(data) {
        BaseUtils.ajaxTimeOutMobile(data, function(){
          if (data.result == "success") {
            var interestCount = data.interestCount;
            // 关注、取消关注显示
            if (optType == 1) {
              $(obj).find("span.span_collect").text("取消关注");
            } else {
              $(obj).find("span.span_collect").text("关注");
            }
            $(obj).attr("onclick", "GroupDyn.ajaxDynamicInterest($(this), '"+des3AgencyId+"', '"+ (1 - optType) +"', '"+resNotExists+"');");
          } else {
            if (data.errorMsg == "interest agency has reached the maximum") {
              scmpublictoast("最多只能关注10个资助机构", 2000);
            } else if (data.errorMsg == "interested in at least one funding agency") {
              scmpublictoast("至少关注1个资助机构", 2000);
            } else{
              scmpublictoast("操作失败", 2000);
            }
            $(obj).attr("onclick", "GroupDyn.ajaxDynamicInterest($(this), '"+des3AgencyId+"', '"+ optType +"', '"+resNotExists+"');");
          }
        });
      },
      error : function() {
        scmpublictoast("操作失败", 2000);
        $(obj).attr("onclick", "GroupDyn.ajaxDynamicInterest($(this), '"+des3AgencyId+"', "+ optType +");");
      }
    });
  }
}

//发布群组动态页面
Group.publishDyn = function(){
  window.location.href = "/grp/dyn/publish?des3GrpId=" + encodeURIComponent($("#des3GrpId").val());
}

//发布动态页面选择成果
Group.selectPub = function(pubListType){
  var text=$("#dyntextarea").val();
  $("#dynText").val(text); 
  $("#pubListType").val(pubListType);    
  $("#pubselectform").submit();
}

//发布动态时选中成果
Group.selectPubToPublish = function(des3PubId, obj, permission, isSelf) {
  if(permission == "7"){
    $("#dyndes3pubId").val(des3PubId);
    var title = $(obj).find(".pubTitle").html();
    if (title.length >= 255) {
      title = title.substring(0, 255) + "......";
    }
    $("#dynpubtitle").html(title);
    $("#dynselect").show();
  }else{
    if (isSelf == "1") {
      newMobileTip("请先将本成果隐私设置为公开", 2, 2000);
    } else {
      newMobileTip("成果隐私设置为仅作者本人可看", 2, 2000);
    }
  }
}

//取消选中
Group.cancel_dyn_select = function(){
  $("#dyndes3pubId").val("");
  $("#dynselect").hide();
}

//确定选中成果
Group.confirm_dyn_select = function(pubId){
  $("#pubselectform").submit();
}



//加载成果信息
Group.getpubinfo = function(des3PubId) {
  $("#paper").show();
  $.ajax({
    url : "/pub/query/single",
    type : "post",
    data : {
      "des3SearchPubId" : des3PubId,
      "pubDB" : "SNS"
    },
    dataType : "json",
    success : function(data) {
      if (data.result == "success" && data.pubListVO != null) {
        var resultList = data.pubListVO.resultList;
        if (resultList != null && resultList.length > 0) {
          var pubInfo = resultList[0];
          // 得到成果参数
          $("#pubtitle").html(pubInfo.title == null ? "" : pubInfo.title);
          $("#author").html(pubInfo.authorNames == null ? "" : pubInfo.authorNames);
          $("#journal").text(pubInfo.briefDesc == null ? "" : pubInfo.briefDesc);
          $("#dynResType").val(1);
          if ("" != pubInfo.publishYear && pubInfo.publishYear != null) {
            $("#journal").after("<span class=\"fccc\">| " + pubInfo.publishYear + "</span>")
          }
          $("#paper").show();
          if (pubInfo.hasFulltext == 1) {
            if (pubInfo.fullTextImgUrl != null && pubInfo.fullTextImgUrl != "") {
              $("#paper").find("img").attr("src", pubInfo.fullTextImgUrl);
            } else {
              $("#paper").find("img").attr("src", "/resmod/images_v5/images2016/file_img1.jpg");
            }
          } else {
            $("#paper").find("img").attr("src", "/resmod/images_v5/images2016/file_img.jpg");
          }
        }
      }
    },
    error : function() {
      // 选择成果出错
    }
  });
};



//删除成果资源
Group.delPub = function() {
  $("#des3ResId").val("");
  $("#dynResType").val(0);
  $("#paper").hide();
  if ($("#dyntextarea").val().length == 0) {
    Group.dontAllowPublish($("#dynrealtime"));
  }
}
// 允许发布动态
Group.allowPublish = function(objQuery) {
  objQuery.attr("onclick", "Group.doPublishGrpDyn();");
  objQuery.removeClass("share_no");
  objQuery.addClass("share");
}
// 不允许发布动态
Group.dontAllowPublish = function(objQuery) {
  objQuery.removeAttr("onclick");
  objQuery.addClass("share_no");
  objQuery.removeClass("share");
}


Group.doPublishGrpDyn = function(obj) {
  $(obj).attr("disabled", true);
  var dynamicContent = $("#dyntextarea").val();
  var des3PubId = $("#des3PubId").val();
  var des3GrpId = $("#des3GrpId").val();
  if ((dynamicContent == undefined || $.trim(dynamicContent) == "") && des3PubId == "") {
    $(obj).attr("disabled", false);
    scmpublictoast("请输入分享内容或选择成果", 2000);
    return;
  }
  dynamicContent = dynamicContent.replace(/&lt;(.*)&gt;.*&lt;\/\1&gt;|&lt;(.*) \/&gt;/, "&_lt;$1&_gt;").replace(/</g,
      "&lt;").replace(/>/g, "&gt;").replace(/\n/g, '<br>');// SCM-17958
  var tempType = "GRP_PUBLISHDYN";
  var resType = "";
  if (des3PubId != null && $.trim(des3PubId) != "") {
    resType = "pub";
  }
  var data = {
    'des3GrpId' : des3GrpId,
    "dynText" : dynamicContent,
    "des3ResId" : des3PubId,
    "tempType" : tempType,
    "resType" : resType
  };

  $.ajax({
    url : '/grp/dyn/dopublish',
    type : 'post',
    dataType : 'json',
    data : data,
    success : function(data) {
      if(data.status == "success"){
        scmpublictoast("发布成功", 2000);
        setTimeout(function(){
          window.location.href = "/grp/main?des3GrpId=" + encodeURIComponent(des3GrpId);
        }, 2000);
      }else{
        scmpublictoast("发布失败", 2000);
      }
    },
    error : function() {
      scmpublictoast("发布失败", 2000);
      $(obj).attr("disabled", false);
    }
  });
  
};

//返回群组详情
Group.backToGrpHomepage = function(){
  window.history.replaceState({}, "", "https://" + document.domain + "/grp/main?des3GrpId=" + encodeURIComponent($("#des3GrpId").val()));
  window.location.reload();
}

//群组动态详情
Group.dynComment = function(des3DynId){
  window.location.href = "/grp/dyn/details?des3GrpId=" + encodeURIComponent($("#des3GrpId").val()) + "&des3DynId=" + encodeURIComponent(des3DynId);
}

//加载群组动态评论
Group.ajaxLoadDynComments = function(des3DynId, des3GrpId){
  $.post(
      "/grp/dyn/ajaxcomments", 
      {"des3DynId": des3DynId, "des3GrpId": des3GrpId},
      function(data,status,xhr){
        if(status == "success"){
          $("#pubview_discuss_list").html(data);
          var commentCount = $("#commentTotalCount").val();
          if(!isNaN(commentCount) && parseInt(commentCount) > 0){
            $("#moreComment").show();
          }
        }
      },
      "html"
  );
}

//发布群组动态评论
Group.doCommentGrpDyn = function(des3DynId, des3GrpId, des3ResId){
  var commentContent = $.trim($("#comment_content").val());
  if (commentContent != "") {
    $.post(
        "/grp/dyn/docomment", 
        {"des3DynId": des3DynId, "des3GrpId": des3GrpId, "des3ResId": des3ResId, "commentContent": commentContent},
        function(data,status,xhr){
          if(status == "success"){
            //更新评论数
            var queryStr = "span.span_comment[des3DynId='" + des3DynId + "']";
            var commentCount = data.commentCount;
            commentCount = commentCount > 999 ? '1k+' : commentCount;
            $(queryStr).text("评论(" + commentCount + ")");
            //追加评论
            var commentTemplate = $("#grp_dyn_comment_template");
            commentTemplate.find(".do_comment_content").text(commentContent);
            $("#pubview_discuss_list").append(commentTemplate.prop("outerHTML").replace("grp_dyn_comment_template", "").replace("display:none;", ""));
            //清理输入框和模板
            commentTemplate.find(".do_comment_content").text("");
            $("#comment_content").val("");
            $("#scm_send_comment_btn").removeAttr("onclick");
            $("#scm_send_comment_btn").addClass("not_point");
            $('html, body').animate({
              scrollTop: $(document).height()
            }, 'fast');
            //恢复评论输入框高度
            $("#comment_content").css("height", "20px")
          }
        },
        "json"
    );
  }
}




//匹配动态中的网址，返回Match数组
Group.matchUrl = function(str) {
  var Match = function(str, index, lastIndex) {
    this.str = str; // 匹配到的url
    this.index = index; // 匹配到的字符串在原字符串中的起始位置
    this.lastIndex = lastIndex; // 匹配到的字符串在原字符串中的终止位置
  };
  /**
   * 匹配http://|https://
   */
  var regexp1 = new RegExp('(http:\/\/|https:\/\/)', 'g');
  var matchArray = new Array();
  var matchStr;
  while ((matchStr = regexp1.exec(str)) != null) {
    matchArray.push(new Match(matchStr[0], matchStr.index, regexp1.lastIndex));
    /*
     * console.log(match) console.log(regexp1.lastIndex - match.index)
     */
  }
  /**
   * 匹配网址分隔符，空白字符或非单字节字符及特殊标点符号,;`·"|'做分隔
   */
  var regexp2 = /<br>|\s|[^\u0000-\u00FF]|[,;`·"\|']/g;
  for (var i = 0; i < matchArray.length; i++) {
    // 考虑内容结束但没有分隔符的情况
    var endIndex = matchArray[i + 1] ? matchArray[i + 1].index : str.length;
    var substr = str.substring(matchArray[i].lastIndex, endIndex);
    if ((matchStr = regexp2.exec(substr)) != null) { // 匹配到分隔符
      if (regexp2.lastIndex > 1) { // 提取网址
        if (matchStr[0] == '<br>') {
          matchArray[i].lastIndex += regexp2.lastIndex - 4;
        } else {
          matchArray[i].lastIndex += regexp2.lastIndex - 1;
        }
        matchArray[i].str = str.substring(matchArray[i].index, matchArray[i].lastIndex);
      } else { // 考虑"http:// "这样的情况
        matchArray.splice(i, 1);
      }
    } else if (endIndex == str.length) { // 考虑内容末尾无空白符的情况
      if (matchArray[i].lastIndex < endIndex) { // 提取网址
        matchArray[i].lastIndex = endIndex;
        matchArray[i].str = str.substring(matchArray[i].index, endIndex);
      } else { // 考虑内容末尾无空白符，但也没有实际网址的情况，如："http://"
        matchArray.splice(i, 1);
      }
    }// 考虑内容并未到结尾，无空白符的情况，如:http://baidu.comhttp://sina.com，这种情况识别为一个网址
    else if (matchArray[i + 1] && (endIndex == matchArray[i + 1].index)) {
      matchArray.splice(i + 1, 1);
      i = i - 1;
    } else { // 其他情况
      matchArray.splice(i, 1);
    }
    // console.log(matchStr);
    regexp2.lastIndex = 0;
  }
  return matchArray;
}


//分享操作
Group.shareRes = function(des3ResId, resType, resNotExists, obj){
  if(resNotExists == "true"){
    scmpublictoast("资源已删除", 2000);
  }else{
    BaseUtils.mobileCheckTimeoutByUrl("/groupweb/ajaxtimeout", function(){
      window.location.href = "/psn/share/page?des3ResId="+encodeURIComponent(des3ResId) + "&resType=" + resType + "&currentDes3GrpId="+encodeURIComponent($("#des3GrpId").val())  + "&des3DynId=" + 
      encodeURIComponent($(obj).attr("des3DynId")) + "&fromPage=" + $(obj).attr("from");
    });
  }
}


Group.showMoreOption = function(event){
  $(".sig__out-box__insign").toggle();
  event.stopPropagation();
}

//置顶群组
Group.stickyGrp = function(opt, obj){
  var postData = {
      "des3GrpId": encodeURIComponent($("#des3GrpId").val()),
      "stickyOpt": opt
  }
  $.post("/grp/opt/ajaxsticky", postData, function(data, status, xhr){
    BaseUtils.ajaxTimeOutMobile(data, function(){
      if(status == "success" && data.result == "success"){
        scmpublictoast("操作成功", 2000);
        $(obj).hide();
        if(opt == "1"){
          $("#cancel_sticky_grp_btn").show();
        }else{
          $("#sticky_grp_btn").show();
        }
      }else{
        scmpublictoast("操作失败", 2000);
      }
    });
  }, "json");
}

//退出群组
Group.quitGrp = function(){
  Smate.confirm("提示", "确定要退出群组吗？", function() {
    var postData = {
        "des3GrpId": encodeURIComponent($("#des3GrpId").val())
    }
    $.post("/grp/opt/ajaxquit", postData, function(data, status, xhr){
      BaseUtils.ajaxTimeOutMobile(data, function(){
        if(status == "success"){
          if(data.result == "success"){
            scmpublictoast("操作成功", 2000);
            setTimeout(function(){
              window.location.reload();
            }, 2000);
          }else if(data.errorMsg != ""){
            scmpublictoast(data.errorMsg, 2000);
          }else{
            scmpublictoast("操作失败", 2000);
          }
        }else{
          scmpublictoast("操作失败", 2000);
        }
      });
    }, "json");
  }, ["确定", "取消"]);
}

//加载关联的项目信息
Group.loadRelationPrjInfo = function(){
  var grpType = $("#grpType").val();
  var url = $("#isLogin").val()=="true" ? "/grp/relationprj/info" : "/grp/outside/relationprj/info";
  if(grpType == "11"){
    $.ajax({
      url: url,
      type: "post",
      data: {
        "des3GrpId": encodeURIComponent($("#des3GrpId").val())
      },
      dataType: "html",
      success: function(data){
        $("#relation_prj_info").html(data);
        var prjAmount = $("#prj_amount_span").text();
        if(prjAmount != ""){
          $("#prj_amount_span").text(BaseUtils.parseMoney(prjAmount));  
        }
      },
      error: function(data){}
    });
  }
}
