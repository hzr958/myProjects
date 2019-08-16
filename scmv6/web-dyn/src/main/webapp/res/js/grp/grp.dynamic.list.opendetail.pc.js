/**
 * 群组动态列表显示及部分逻辑的js---zzx
 */
var DiscussOpenDetail = DiscussOpenDetail ? DiscussOpenDetail : {};

// 打开人员主页----zzx-----
DiscussOpenDetail.openPsnDetail = function(des3ProducerPsnId, event) {
  // 先判断是否是在站外
  if ("true" == $("#grp_params").attr("outside")) {
    window.open("/psnweb/outside/homepage?des3PsnId=" + encodeURIComponent(des3ProducerPsnId));
    // location.href="/psnweb/outside/homepage?des3PsnId="+encodeURIComponent(des3ProducerPsnId);
    return;
  }

  if (des3ProducerPsnId != null && des3ProducerPsnId != "") {
    // location.href="/scmwebsns/resume/psnView?des3PsnId="+encodeURIComponent(des3ProducerPsnId);
    window.open("/psnweb/homepage/show?des3PsnId=" + encodeURIComponent(des3ProducerPsnId));
    // location.href = "/psnweb/homepage/show?des3PsnId=" + encodeURIComponent(des3ProducerPsnId);

  }
}
// 打开成果详情----zzx-----
DiscussOpenDetail.openPubDetail = function(des3PubId, event) {
  var groupId = $("#grp_params").attr("smate_des3_grp_id");
  if (des3PubId != null && des3PubId != "") {
    window
        .open("/pub/details?des3PubId=" + encodeURIComponent(des3PubId) + "&des3GrpId=" + encodeURIComponent(groupId));
    // window.open("/scmwebsns/publication/view?des3Id="+encodeURIComponent(des3PubId)+"&currentDomain=/pubweb&pubFlag=1");des3GroupId
  }
}
DiscussOpenDetail.openPdwhDetail = function(des3PubId, dbId, event) {
  Pub.newPubPdwhDetail(des3PubId);
  dynamic.stopNextEvent(event);
}
// 短地址打开成果详情----zzx-----
DiscussOpenDetail.shortUrlOpenPubDetail = function(url, event) {
  if (url != null && url != "") {
    window.open(url);
  }
}
/*
 * //下载动态分享的文件 DiscussOpenDetail.openFile = function(fileId, nodeId, type){ location.href = snsctx +
 * "/file/download?des3Id=" + fileId + "&nodeId="+ nodeId + "&type=" + type; }
 */
// 下载成果全文
// 全文下载---旧全文下载，现在已不用，但为了兼容原动态模板的全文下载，此方法暂时保留
/*
 * DiscussOpenDetail.fullTextDownload = function(fileId , des3Id){ //站外下载全文 if("true" ==
 * $("#grp_params").attr("outside")){ if(des3Id !=undefined){
 * DiscussOpenDetail.fulltextDownloadAuthority(des3Id); }else{ scmpublictoast("下载失败，缺少参数!",1000); }
 * return ; } if(fileId != undefined && fileId != ""){
 * window.location.href="/pubweb/fulltext/ajaxgetinfobyfileId?fulltextFileId="+fileId; return; } }
 */

// 成果列表的全文下载(带有权限判断)--旧全文下载，现在已不用，但为了兼容原动态模板的全文下载，此方法暂时保留
/*
 * DiscussOpenDetail.fulltextDownloadAuthority = function(des3Id) { var params={}; if (des3Id !=
 * undefined ) { params.des3Id = des3Id; } $.ajax({ url:"/pubweb/fullText/ajaxgetinfo", type:
 * "post", dataType: "json", data: params, async : false, success: function(data){ if
 * (data.permission == "yes") { var fullTextUrl = data.fullTextUrl; var fdesId =
 * fullTextUrl.split("?fdesId=")[1]; if(fdesId != undefined && fdesId != ""){
 * window.location.href="/pubweb/pubopt/ajaxfiledownload?fdesId="+fdesId; return; } } else {
 * scmpublictoast("你没有权限下载该全文!",1000); } } }); };
 */
/**
 * 新的文件下载统一事件
 */
DiscussOpenDetail.fileDownloadEvent = function(des3ResId, resType, des3DynId) {
  if (!resType || !des3ResId) {
    scmpublictoast("下载失败，缺少参数!", 1000);
    return;
  }
  // 判断是否站外下载
  var outside = "true" == $("#grp_params").attr("outside");
  switch (resType) {
    case "pub" :
      DiscussOpenDetail.downloadFullText(des3ResId, 2, outside);
    break;
    case "pdwhpub" :
      DiscussOpenDetail.pdwhIsExist(des3ResId, function() {
        DiscussOpenDetail.downloadFullText(des3ResId, 3, outside);
      });
    break;
  }
}

/**
 * 群组全文下载或全文请求
 */
DiscussOpenDetail.fullTextEvent = function(des3DynId, des3ResId, resType, ev) {
  BaseUtils.doHitMore(ev.currentTarget, 1000);
  var $this = $(ev.currentTarget);
  var $inputs = $this.closest(".main-list__item").find("input[name='dynId']");
  var resownerdes3id = $inputs.eq($inputs.length - 1).attr("resownerdes3id");
  var des3FileId = $this.closest(".dev_pub_fulltext").attr("des3FileId");
  var permission = $this.closest(".dev_pub_fulltext").attr("permission");
  var userId = $("#userId").val();
  if (resType == "pub") {
    // SCM-24197 yhx 2019/3/28
    var des3OwnerId = DiscussOpenDetail.getSnsPubOwner(des3ResId);
    if (des3OwnerId != null) {
      resownerdes3id = des3OwnerId;
    }
  }
  if (des3FileId) {// 全文存在则下载
    if (permission == 0 || userId == resownerdes3id) {
      DiscussOpenDetail.fileDownloadEvent(des3ResId, resType, des3DynId);
    } else {
      DiscussOpenDetail.requestFullText(resownerdes3id, des3ResId, resType);
    }
  } else {
    if (userId == resownerdes3id) {// 自己的成果，则上传全文
      DiscussOpenDetail.uploadFullText(des3ResId);
    } else {// 不是自己的，则全文请求
      DiscussOpenDetail.requestFullText(resownerdes3id, des3ResId, resType);
    }
  }
}

DiscussOpenDetail.getSnsPubOwner = function(des3ResId) {
  var des3ownerId = null;
  $.ajax({
    url : '/dynweb/dynamic/ajaxgetpubowner',
    type : 'post',
    async : false,
    data : {
      "des3ResId" : des3ResId
    },
    success : function(data) {
      if (data.result == "success") {
        des3ownerId = data.des3ownerId;
      }
    }
  });
  return des3ownerId;
}

// 检查基准库成果是否存在
DiscussOpenDetail.pdwhIsExist = function(des3PubId, func) {
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
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.result == 'success') {
          func();
        } else {
          scmpublictoast(grpPubCommon.pubIsDeleted, 1000);
        }
      });
    },
    error : function(data) {
      scmpublictoast("系统出错", 1000);
    }
  });
}

DiscussOpenDetail.uploadFullText = function(des3ResId) {
  var forwardUrl = "/pub/enter?des3PubId=" + encodeURIComponent(des3ResId);
  if (des3ResId) {
    $.ajax({
      url : '/pub/ajaxCheckPub',
      type : 'post',
      data : {
        "des3PubId" : des3ResId
      },
      dataType : "json",
      success : function(data) {
        if (data.status == "success") {
          BaseUtils.forwardUrlRefer(true, forwardUrl);
        } else if (data.status == "isDel") {
          scmpublictoast(grpPubCommon.pubIsDeleted, 2000);
        } else {
          BaseUtils.forwardUrlRefer(true, forwardUrl);
        }
      },
      error : function() {
        BaseUtils.forwardUrlRefer(true, forwardUrl);
      }
    });
  }
}

DiscussOpenDetail.requestFullText = function(des3RecvPsnId, des3PubId, resType) {
  if (resType && resType == "pdwhpub") {
    // 基准库要先校验成果是否存在
    DiscussOpenDetail.pdwhIsExist(des3PubId, function() {
      DiscussOpenDetail.requestFullText2(des3RecvPsnId, des3PubId, resType);
    });
  } else {
    DiscussOpenDetail.requestFullText2(des3RecvPsnId, des3PubId, resType);
  }
}

DiscussOpenDetail.requestFullText2 = function(des3RecvPsnId, des3PubId, resType) {
  if (des3PubId) {
    $.ajax({
      url : '/pub/fulltext/ajaxreqadd',
      type : 'post',
      data : {
        "des3RecvPsnId" : des3RecvPsnId,
        "des3PubId" : des3PubId,
        'pubType' : resType
      },
      dataType : "json",
      success : function(data) {
        GrpBase.ajaxTimeOut(data, function() {
          if (data.status == "success") { // 全文请求保存成功
            scmpublictoast(grpPubCommon.req_success, 2000);
          } else if (data.status == "isDel") {
            scmpublictoast(grpPubCommon.pubIsDeleted, 2000);
          } else {
            scmpublictoast(grpPubCommon.req_error, 2000);
          }
        });
      },
      error : function() {
        scmpublictoast(grpPubCommon.req_param_error, 2000);
      }
    });
  }
}

/**
 * 下载全文 des3Id: 加密的成果id type: 2--个人库成果类型，3--基准库成果类型
 */
DiscussOpenDetail.downloadFullText = function(des3Id, type, shortUrl) {
  // shortUrl未定义或者不为true，则重新赋值false，否则为true
  shortUrl = !(shortUrl == undefined || shortUrl != true);
  $.post('/fileweb/download/ajaxgeturl', {
    "des3Id" : des3Id,
    "type" : type,
    "shortUrl" : shortUrl
  }, function(result) {
    if (result && result.status == "success") {
      window.location.href = result.data.url;
    }
  });
}