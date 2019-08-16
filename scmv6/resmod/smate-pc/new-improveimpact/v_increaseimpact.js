//查看更多
function impactViewMore(type) {
  var newwindow = window.open("about:blank");
  if (type == 1) {
    newwindow.location.href = "/psnweb/homepage/show?module=pub&jumpto=puball";
  } else if (type == 2) {
    newwindow.location.href = "/psnweb/homepage/show?module=pub";
  } else if (type == 3) {
    newwindow.location.href = "/psnweb/friend/main?module=rec";
  }else {
    newwindow.location.href = "/groupweb/mygrp/main?model=rcmdGrp";
  }
}

//邀请合作者为联系人
function sendPubComfirmInviteFriend(pubId) {
  $.ajax({
    url : "/psnweb/friend/ajaxinvitetofriend",
    type : "post",
    dataType : "json",
    data : {"pdwhPubId":pubId},
    success : function(data) {}
  });
};
//论文认领——是我的论文
function affirmPubComfirm(pubId,obj){
  BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
    ignoreUploadFullText(obj);//先把下一条记录显示出来
    $.ajax({
      url: "/pub/opt/ajaxAffirmPubComfirm",
      type : "post",
      dataType : "json",
      data :{"pubId":pubId},
      success:function(data){
        var oneText = $(obj).closest(".promote-Impact_container-mainitem_body").find(".pub-idx__main_add-tip").html();
        if (oneText == "check_box") {
          sendPubComfirmInviteFriend(pubId);
        }
      }
    });
  });
}

//论文认领——不是我的论文
function ignorePubComfirm(pubId,obj){
  BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
    ignoreUploadFullText(obj);//先把下一条记录显示出来
    $.ajax({
      url: "/pub/opt/ajaxIgnorePubComfirm",
      type : "post",
      dataType : "json",
      data :{"pubId":pubId},
      success:function(data){
        
      }
    });
  });
}

//上传全文——暂无全文
function ignoreUploadFullText(obj){
  BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
    var curitem = $(obj).closest(".promote-Impact_container-mainitem");
    var upitem =  $(obj).closest(".promote-Impact_container-subitem");
    var nextitem = curitem.next();
    if(!nextitem.hasClass("promote-Impact_container-mainitem")){
      //没有数据 跳到下一页
      if(document.getElementsByClassName("element__piblic-block")[0].closest(".promote-Impact_container-subitem").nextElementSibling != undefined ){
        if(document.getElementsByClassName("element__piblic-block")[0].closest(".promote-Impact_container-subitem").nextElementSibling.innerHTML !=""){
            var target = document.getElementsByClassName("element__piblic-block")[0].closest(".promote-Impact_container-subitem").nextElementSibling.querySelector(".promote-Impact_container-mainitem");
            document.getElementsByClassName("element__piblic-block")[0].classList.remove("element__piblic-block");
            target.classList.add("element__piblic-block");
            document.getElementsByClassName("promote-Impact_container-count")[0].innerHTML = parseInt(document.getElementsByClassName("promote-Impact_container-count")[0].innerHTML) + 1;
            $(obj).closest(".promote-Impact_container-mainitem").remove();
            if($(obj).closest(".promote-Impact_container-subitem").html() == undefined){
                upitem.remove();
                if(parseInt(document.getElementsByClassName("promote-Impact_container-count")[0].innerHTML) > 1){
                    document.getElementsByClassName("promote-Impact_container-count")[0].innerHTML = parseInt(document.getElementsByClassName("promote-Impact_container-count")[0].innerHTML) - 1;
                }
                if(parseInt(document.getElementsByClassName("promote-Impact_container-total")[0].innerHTML) > 1){
                    document.getElementsByClassName("promote-Impact_container-total")[0].innerHTML = document.getElementsByClassName("promote-Impact_container-subitem").length;
                }
                
                if(document.getElementsByClassName("promote-Impact_container-item")[0].innerHTML == " "){
                    document.getElementsByClassName("promote-Impact_container")[0].style.top =  1600 + "px";
                    setTimeout(function(){
                        document.body.removeChild( document.getElementsByClassName("background-cover")[0]);
                        document.body.removeChild( document.getElementsByClassName("promote-Impact_container")[0]);
                    },400);
                }
            }
        }
     }else{
       var close = document.getElementsByClassName("promote-Impact_container_close")[0];
       close.click();
     }
    }else{
      nextitem.addClass("element__piblic-block");
      $(obj).closest(".promote-Impact_container-mainitem").remove();
    }
    if(curitem.hasClass("element__piblic-block")){
      curitem.removeClass("element__piblic-block");
    }
  });
}

function openFileuploadBoxClick(ev,obj){
  if(obj==undefined){
    return;
  }
  var $this = $(obj).closest(".promote-Impact_container-mainitem").find(".uploadpub_uploadFulltext")[0];
  initialization($this);
  $(obj).closest(".promote-Impact_container-mainitem").find('input.fileupload__input').click();
  ev.stopPropagation();// 阻止事件的向上传播
  return;
}

//初始化上传控件
function initialization(obj) {
    var data = {
        "fileurl" : "/fileweb/fileupload",
        "filedata" : {
            fileDealType : "generalfile"
        },
        "method" : "nostatus",
        "method" : "nostatus",
        "filecallback" : reloadFulltext,
        "filecallbackparam" : {
          pubId : $(".uploadpub_uploadFulltext").attr("des3Id")
        },
        
    };
    fileUploadModule.initialization(obj, data);
};

//上传全文，文件上传成功后的回调函数，用于更新成果全文附件信息
function reloadFulltext(xhr, params) {
  if (typeof (xhr) == "undefined" || !params) {
    return false;
  }
  const data = eval('(' + xhr.responseText + ')');
  var pubId = params.pubId;
  var des3fid = data.successDes3FileIds.split(",")[0];
  $.ajax({
        url : '/pub/opt/ajaxupdatefulltext',
        type : 'post',
        data : {
          'pubId' : pubId,
          'des3FileId' : des3fid
        },
        dataType : 'json',
        timeout : 10000,
        success : function(data) {
          Pub.ajaxTimeOut(data,function() {
                    if (data.result == "true") {
                      ignoreUploadFullText($("#uploadFulltext_"+pubId));
                    } else {
                      scmpublictoast('系统出现异常，请稍后再试', 2000);
                    }
                  });
        }
      });

};

//推荐联系人——忽略
function ignoreFriend(des3TempPsnId,obj){
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
    ignoreUploadFullText(obj);//先把下一条记录显示出来
    $.ajax({
      url : "/psnweb/recommend/ajaxremove",
      type : "post",
      dataType : "json",
      data : {"des3TempPsnId":des3TempPsnId},
      success : function(data) {
      }
    });
  });
}

//推荐联系人——添加关注
function addFriendAttention(reqPsnId,obj) {
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
    ignoreUploadFullText(obj);//先把下一条记录显示出来
    $.ajax({
      url : "/psnweb/psnsetting/ajaxSaveAttFrd",
      type : 'post',
      dataType : 'json',
      data : {
        'des3PsnIds' : reqPsnId
      },
      success : function(data) {
      }
    });
  });
}

//推荐联系人——添加联系人
function addFriend(reqPsnId,obj) {
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
    $.ajax({
      url : "/psnweb/friend/ajaxaddfriend",
      type : 'post',
      dataType : 'json',
      data : {
        'des3Id' : reqPsnId
      },
      success : function(data) {
        ignoreUploadFullText(obj);
      }
    });
  });
}

//群组——取消/加入群组
function optGrp(des3GrpId,status,obj){
  if (obj) {
    BaseUtils.doHitMore(obj, 1000);
  }
  BaseUtils.checkCurrentSysTimeout("/groupweb/ajaxtimeout", function(){
    var dataJson = {
        'des3GrpId' : des3GrpId,
        'rcmdStatus' : status
    };
    $.ajax({
      url : "/groupweb/rcmdgrp/ajaxoptionrcmdgrp",
      type : 'post',
      dataType : 'json',
      data : dataJson,
      success : function(data) {
        if(status == 1){
          memberApplyJoinGrp(des3GrpId);
        }
        ignoreUploadFullText(obj);
      }
    });
  });
}
//成员申请 群组
function memberApplyJoinGrp(des3GrpId) {
  $.ajax({
    url : '/groupweb/grpmember/ajaxapplyjoingrp',
    type : 'post',
    dataType : 'json',
    data : {
      'isApplyJoinGrp' : 1,
      'des3GrpId' : des3GrpId
    },
    success : function(data) {
    },
    error : function() {
    }
  });
}

function checkIsSend(obj){
 var checkText = $(obj).closest(".promote-Impact_container-mainitem_addtip").find(".pub-idx__main_add-tip").html();
 console.log(checkText);
 if(checkText!="check_box"){
   $(obj).closest(".promote-Impact_container-mainitem_addtip").find(".pub-idx__main_add-tip").html("check_box");
 }else{
   $(obj).closest(".promote-Impact_container-mainitem_addtip").find(".pub-idx__main_add-tip").html("check_box_outline_blank");
 }
}

//已添加  关闭弹框
function closeImpact(){
  var close = document.getElementsByClassName("promote-Impact_container_close")[0];
  close.click();
}

//初始化 分享 插件
function psnInitSharePlugin(obj){
    if(SmateShare.timeOut && SmateShare.timeOut == true)
        return;
    if (locale == "en_US") {
        $(obj).dynSharePullMode({
            'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
            'language' : 'en_US'
        });
    } else {
        $(obj).dynSharePullMode({
            'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
        });
    }
    var obj_lis = $("#share_to_scm_box").find("li");
    obj_lis.eq(0).hide();
    obj_lis.eq(1).click();
    obj_lis.eq(2).hide();
};

