/**
 * 移动端群组文件js
 */
var MobileGrpFile = MobileGrpFile ? MobileGrpFile : {};


/**
 * 初始化分享页面
 */
MobileGrpFile.initSharePage = function (platForm,recievers,grpName) {
  $(".new-mobilegroup_neck-list").removeClass("new-mobilegroup_neck-list_selected");
  if ("grp" == platForm) {
    $(".new-mobilegroup_neck-list").eq(1).addClass("new-mobilegroup_neck-list_selected");
    $("#m_grp_file_share_friend").hide();
    $("#m_grp_file_share_grp").show();
    if (grpName) {
      var id = $("#m_grp_file_choose_grpId").val();
      MobileGrpFile.addChoose("div[select_option='grp_"+id+"']",id,grpName,"grp");
    }
    //回显已经填写的留言
    $("div[class='new-mobile_totarget-group_input']").find("textarea").val($.trim($("#m_grp_file_leaveMsg").val()));
  } else {
    $(".new-mobilegroup_neck-list").eq(0).addClass("new-mobilegroup_neck-list_selected");
    $("#m_grp_file_share_friend").show();
    $("#m_grp_file_share_grp").hide();
    if (recievers) {
      var ids = recievers.split(",");
      for (var i in ids) {
        MobileGrpFile.addChoose("div[select_option='friend_"+ids[i]+"']",ids[i],$("span[class='new-mobile_totarget-friend_list-detail friend_"+ids[i]+"']").text(),"friend");
      }
    }
    //回显已经填写的留言
    $("div[class='new-mobile_totarget-friend_input']").find("textarea").val($.trim($("#m_grp_file_leaveMsg").val()));
  }
}

/**
 * 获取群组文件列表
 */
MobileGrpFile.queryGrpFileList = function(){
  var dataJson = {};
  dataJson.des3GrpLabelId = $("#m_grp_file_des3LableId").val();
  dataJson.des3MemberId = $("#m_grp_file_des3MemberId").val();
  dataJson.searchKey = $("#m_grp_file_searchKey").val();
  dataJson.des3GrpId = $("#m_grp_file_des3GrpId").val();
  dataJson.workFileType = $("#workFileType").val();
  dataJson.courseFileType = $("#courseFileType").val();
  var url = $("#isLogin").val()=="false" ? "/grp/outside/mobile/querygrpfilelist" : "/grp/mobile/querygrpfilelist";
  window.Mainlist({
    name : "mobile_group_file_list",
    listurl : url,
    listdata : dataJson,
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

/**
 * 定义一个数组,用于存放选中的群组/联系人id
 */
var chooseArr = [];

/**
 * 向数组添加/删除id
 */
MobileGrpFile.buildChooseIdArr = function (des3Id,type,platForm) {
  if ("add" == type) {
    chooseArr.push(des3Id);
  } else {
    chooseArr = $.grep(chooseArr, function(value) {
      return value != des3Id;
    });
  }
  if ("grp" == platForm) {
    $("#m_grp_file_des3GrpIds").val($.trim(chooseArr.join(',')));
  } else {
    $("#m_grp_file_des3RecieverIds").val($.trim(chooseArr.join(',')));
  }
}

/**
 * 群组文件分享到群组和联系人切换
 */
MobileGrpFile.shareTypeChange = function (obj,type) {
  //将数组置空,防止混淆
  chooseArr = [];
  $(".new-mobilegroup_neck-list").removeClass("new-mobilegroup_neck-list_selected");
  $(obj).addClass("new-mobilegroup_neck-list_selected");
  if ("friend" == type) {
    $("#m_grp_file_sharePlatform").val("friend");
    $("#m_grp_file_share_friend").show();
    $("#m_grp_file_share_grp").hide();
    $("#sharebt").attr("shareto","friend");
  } else {
    $("#m_grp_file_sharePlatform").val("grp");
    $("#m_grp_file_share_friend").hide();
    $("#m_grp_file_share_grp").show();
    $("#sharebt").attr("shareto","grp");
  }
}


/**
 * 添加选中
 */
MobileGrpFile.addChoose = function(obj,des3Id,name,platForm) {
  if (typeof $(obj) != typeof undefined && $(obj)) {
    $(obj).remove();
  }
  var btn = "<div class='new-mobile_totarget-friend_choice-item'>"+
                "<span class='new-mobile_totarget-friend_choice-name'>"+name+"</span>"+
                "<i class='material-icons' onclick = 'MobileGrpFile.cancelChoose(this,\""+des3Id+"\",\""+name+"\",\""+platForm+"\")'>close</i>"+
             "</div>";
  if ("grp" == platForm) {
    $("#grp_choose").append(btn);
  } else {
    $("#friend_choose").append(btn);
  }
  MobileGrpFile.buildChooseIdArr(des3Id,"add",platForm);
}

/**
 * 取消选中
 */
MobileGrpFile.cancelChoose = function(obj,des3Id,name,platForm) {
  $(obj).parent().remove();
  if (platForm == "friend") {
    var btn = "<div class='new-mobile_totarget-friend_list' onclick='MobileGrpFile.addChoose(this,\""+des3Id+"\",\""+name+"\",\""+platForm+"\")'>"+
            "<i class='material-icons new-mobile_totarget-friend_list-icon'>add</i>"+
            "<span class='new-mobile_totarget-friend_list-detail'>"+name+"</span>"+
         "</div>";
    $("#friend_cancel_choose").append(btn);
  }
  MobileGrpFile.buildChooseIdArr(des3Id,"remove",platForm);
}

/**
 * 进入选择好友页面
 */
MobileGrpFile.loadChoosePage  = function(type){
  if ("grp" == type) {
    $("#m_grp_file_leaveMsg").val($.trim($("div[class='new-mobile_totarget-group_input']").find("textarea").val()));
    $("#m_grp_file_choose_page").attr("action","/psn/mobile/choosegrp");
  } else {
    $("#m_grp_file_leaveMsg").val($.trim($("div[class='new-mobile_totarget-friend_input']").find("textarea").val()));
  }
  $("#m_grp_file_choose_page").submit();
}

/**
 * 加载联系人列表并选中联系人
 */
MobileGrpFile.showChooseList = function(searchKey,type){
  var dataJson = {};
  dataJson.searchKey = searchKey;
  var name = "m_grp_file_share_friend";
  var listurl = "/psn/mobile/showfriendlist";
  if (type == "grp") {
    var name = "m_grp_file_share_grp";
    var listurl = "/psn/mobile/showgrplist";
  }
  window.Mainlist({
    name : name,
    listurl : listurl,
    listdata : dataJson,
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
      //先清空数组
      chooseArr = [];
      var ids = $("#m_grp_file_des3RecieverIds").val();
      if (type == "grp") {
        ids = $("#m_grp_file_des3GrpIds").val();
      }
      if (ids) {
        var idArr = ids.split(",");
        for (var i in idArr) {
          //将已经选中id放入数组
          chooseArr.push(idArr[i]);
          var chooseId = $(".friend[des3PsnId='"+idArr[i]+"']");
          if (type == "grp") {
            chooseId = $(".grp[des3GrpId='"+idArr[i]+"']");
          }
          $(chooseId).addClass("new-mobileContact_item-checked");
          $(chooseId).find(".material-icons").html("check");
        }
      }
    }
  });
}

/**
 * 选中分享的好友/群组
 */
MobileGrpFile.choose = function (obj,type) {
  var id = $(obj).attr("des3PsnId");
  if (type == "grp") {
    //群组只支持单选
    $("#m_grp_file_des3GrpIds").val();
    chooseArr = [];
    $(".grp").removeClass("new-mobileContact_item-checked");
    $(".grp").find(".material-icons").html("add");
    id = $(obj).attr("des3GrpId");
  }
  if ($(obj).hasClass("new-mobileContact_item-checked")) {
    $(obj).removeClass("new-mobileContact_item-checked");
    $(obj).find(".material-icons").html("add");
    chooseArr = $.grep(chooseArr, function(value) {
      return value != id;
    });
  } else {
    $(obj).addClass("new-mobileContact_item-checked");
    $(obj).find(".material-icons").html("check");
    chooseArr.push(id);
  }
  if (type == "grp") {
    $("#m_grp_file_des3GrpIds").val($.trim(chooseArr.join(",")));
  } else {
    $("#m_grp_file_des3RecieverIds").val($.trim(chooseArr.join(",")));
  }
}

/**
 * 选取联系人之后返回到联系人分享页
 */
MobileGrpFile.backShare = function(){
  $("#m_grp_file_choose_page").submit();
}

/**
 * 文件分享
 */
MobileGrpFile.shareFile = function(obj){
  var dataJson = {};
  dataJson.des3ResId = encodeURIComponent($("#m_grp_file_des3ResId").val());
  dataJson.fileName = $("div[class='new-mobilefile_share-item_title']").find("a").text();
  dataJson.des3GrpId = $("#m_grp_file_des3GrpId").val();
  var url = "";
  var type = $(obj).attr("shareto");
  if ("grp" == type) {
    dataJson.des3GrpIds = encodeURIComponent($("#m_grp_file_des3GrpIds").val());
    dataJson.leaveMsg = $("div[class='new-mobile_totarget-group_input']").find("textarea").val();
    url = "/grp/file/sharetogrp";
  } else {
    dataJson.des3RecieverIds = $("#m_grp_file_des3RecieverIds").val();
    dataJson.leaveMsg = $("div[class='new-mobile_totarget-friend_input']").find("textarea").val();
    url = "/grp/file/sharetofriend";
  }
  $.ajax({
     url : url,
     type : "POST",
     data : dataJson,
     dataType : "JSON",
     success : function (data) {
       if (data.status == "success") {
         scmpublictoast("分享成功", 2000);
         //分享成功后都应该重定向到文件列表
         setTimeout(function(){
           var resType = $("#m_grp_file_resType").val();
           if(resType == "grpWorkFile"){//作业
             window.location.href = "/grp/main/grpfilemain?des3GrpId="+encodeURIComponent(dataJson.des3GrpId) + "&workFileType=1";   
           }else if(resType == "grpCourseFile"){//课程
             window.location.href = "/grp/main/grpfilemain?des3GrpId="+encodeURIComponent(dataJson.des3GrpId) + "&courseFileType=2";      
           }else{
             window.location.href = "/grp/main/grpfilemain?des3GrpId="+encodeURIComponent(dataJson.des3GrpId);   
           }
         }, 2000);
       } else {
         scmpublictoast("分享失败", 2000);
       }
     },
     error : function () {
       scmpublictoast("分享失败", 2000);
     }
  });
}

/**
 * 取消分享
 */
MobileGrpFile.cancelShare = function () {
  var resType = $("#m_grp_file_resType").val();
  if(resType == "grpWorkFile"){//作业
    window.location.href = "/grp/main/grpfilemain?des3GrpId="+encodeURIComponent($("#m_grp_file_des3GrpId").val()) + "&workFileType=1";   
  }else if(resType == "grpCourseFile"){//课程
    window.location.href = "/grp/main/grpfilemain?des3GrpId="+encodeURIComponent($("#m_grp_file_des3GrpId").val()) + "&courseFileType=2";      
  }else{
    window.location.href = "/grp/main/grpfilemain?des3GrpId="+encodeURIComponent($("#m_grp_file_des3GrpId").val());   
  }
}

//下载全文
MobileGrpFile.downloadFile = function(url) {
  if (!!url && url != "") {
    Smate.confirm("下载提示", "要下载该文件吗？", function() {
      window.location.href = url;
    }, ["下载", "取消"]);
  }
};
