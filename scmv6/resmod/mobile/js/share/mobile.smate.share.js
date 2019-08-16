var MobileSmateShare = MobileSmateShare ? MobileSmateShare : {};


// 获取可能分享给的好友列表
MobileSmateShare.findMayShareToPsn = function(){
  $.post("/psn/share/ajaxfriends", {}, function(data,status,xhr){
    if("success" == status){
      $("#friend_may_choose").html(data);
      var selectedFriends = $.trim($("#mobile_share_des3FriendIds").val());
      if (selectedFriends) {
        var ids = selectedFriends.split(",");
        chooseArr = [];
        for (var i in ids) {
          MobileSmateShare.addChoosed("div[select_option='friend_"+ids[i]+"']",ids[i],$("span[class='new-mobile_totarget-friend_list-detail friend_"+ids[i]+"']").text(),"friend");
        }
      }
    }
  }, "html");
}


//获取要分享到的群组信息
MobileSmateShare.findMayShareToGrp = function(){
  var des3GrpId = $.trim($("#mobile_share_des3GrpId").val());
  if(des3GrpId){
    $.post("/psn/share/ajaxgrpinfo", {"des3GrpId": des3GrpId}, function(data,status,xhr){
      if("success" == status && data.grp != null){
        var grpName = data.grp.grpName;
        if (grpName) {
          var id = $("#mobile_share_des3GrpId").val();
          MobileSmateShare.addChoosed("div[select_option='grp_"+id+"']",id,grpName,"grp");
        }
      }
    }, "json");
  }
}


// 初始化分享页面
MobileSmateShare.initSharePage = function (shareTo) {
  $(".new-mobilegroup_neck-list").removeClass("new-mobilegroup_neck-list_selected");
  if("dyn" == shareTo){
    $("#share_to_dyn_tab").addClass("new-mobilegroup_neck-list_selected");
    $("#mobile_share_to_dyn_div").show();
    $("#mobile_share_to_friend_div").hide();
    $("#mobile_share_to_grp_div").hide();
    //回显已经填写的留言
    $("div[class='new-mobile_totarget-friend_input']").find("textarea").val($.trim($("#mobile_share_shareText").val()));  
  } else if ("friend" == shareTo) {
    $("#share_to_friend_tab").addClass("new-mobilegroup_neck-list_selected");
    $("#mobile_share_to_friend_div").show();
    $("#mobile_share_to_grp_div").hide();
    $("#mobile_share_to_dyn_div").hide();
    //回显已经填写的留言
    $("div[class='new-mobile_totarget-friend_input']").find("textarea").val($.trim($("#mobile_share_shareText").val()));
  } else {
    $("#share_to_grp_tab").addClass("new-mobilegroup_neck-list_selected");
    $("#mobile_share_to_friend_div").hide();
    $("#mobile_share_to_grp_div").show();
    $("#mobile_share_to_dyn_div").hide();
    //回显已经填写的留言
    $("div[class='new-mobile_totarget-group_input']").find("textarea").val($.trim($("#mobile_share_shareText").val()));
  }
}





//执行分享操作
MobileSmateShare.doShare = function(obj){
  BaseUtils.doHitMore($(obj), 2000);
  var url = "/psn/res/ajaxshare";
  var shareTo = $.trim($("#mobile_share_shareTo").val());
  if ("group" == shareTo) {
    if($("#mobile_share_des3GrpId").length <= 0 || $("#mobile_share_des3GrpId").val()==""){
      scmpublictoast("请选择一个群组", 2000);
      return false;
    }
  } else if("friend" == shareTo){
    if($("#mobile_share_des3FriendIds").length <= 0 || $("#mobile_share_des3FriendIds").val()==""){
      scmpublictoast("请选择好友", 2000);
      return false;
    }
  } 
  $("#mobile_share_shareText").val($("textarea.smate_share_text").not(":hidden").val());
  var data = {
      "des3ResId": $.trim($("#mobile_share_des3ResId").val()),
      "resType": $.trim($("#mobile_share_resType").val()),
      "shareTo": shareTo,
      "des3FriendIds": $.trim($("#mobile_share_des3FriendIds").val()),
      "des3GrpId": $.trim($("#mobile_share_des3GrpId").val()),
      "shareText": $.trim($("#mobile_share_shareText").val()),
      "operatorType" : "3",
      "grpResDes3GrpId": $.trim($("#mobile_share_grpres_grpid").val()),
      "fromPage": $.trim($("#mobile_share_fromPage").val()),
      "des3DynId": $.trim($("#mobile_share_des3DynId").val())
  };
  
  $.post(url, data, function(data,status,xhr){
    if("success" == status && (data.error_code == "" || data.error_code == null)){
      scmpublictoast("分享成功", 2000);
      setTimeout(function(){
        window.history.back();
      }, 2000);
    }else{
      scmpublictoast("分享失败", 2000);
    }
  }, "json");
}


/**
 * 添加选中要分享资源的联系人或群组
 */
MobileSmateShare.addChoosed = function(obj,des3Id,name,platForm) {
  if (typeof $(obj) != "undefined" && $(obj)) {
    $(obj).hide();
  }
  var btn = "<div class='new-mobile_totarget-friend_choice-item'>"+
                "<span class='new-mobile_totarget-friend_choice-name'>"+name+"</span>"+
                "<i class='material-icons' onclick = 'MobileSmateShare.removeChoosed(this,\""+des3Id+"\",\""+name+"\",\""+platForm+"\")'>close</i>"+
             "</div>";
  if ("grp" == platForm) {
    $("#grp_choose").append(btn);
  } else {
    $("#friend_choose").append(btn);
  }
  MobileSmateShare.buildChoosedIdArr(des3Id,"add",platForm);
}



/**
 * 取消选中要分享资源的联系人或群组
 */
MobileSmateShare.removeChoosed = function(obj,des3Id,name,platForm) {
  $(obj).parent().remove();
  if (platForm == "friend") {
    var psnSelector = "div[des3PsnId='"+des3Id+"']";
    $("#friend_may_choose").find(psnSelector).show();
  }
  MobileSmateShare.buildChoosedIdArr(des3Id,"remove",platForm);
}




/**
 * 定义一个数组,用于存放选中的群组/联系人id
 */
var chooseArr = [];

/**
 * 向数组添加/删除id
 */
MobileSmateShare.buildChoosedIdArr = function (des3Id,type,platForm) {
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
    $("#mobile_share_des3FriendIds").val($.trim(chooseArr.join(',')))
  }
}



/**
 * 群组文件分享到群组和联系人切换
 */
MobileSmateShare.shareTypeChange = function (obj,type) {
  if(!$(obj).hasClass("new-mobilegroup_neck-list_selected")){
    var textStr =  $("textarea.smate_share_text").not(":hidden");
    //将数组置空,防止混淆
    chooseArr = [];
    $(".new-mobilegroup_neck-list").removeClass("new-mobilegroup_neck-list_selected");
    $(obj).addClass("new-mobilegroup_neck-list_selected");
    $("#mobile_share_shareTo").val(type);

    if ("friend" == type) {
      $("#mobile_share_to_friend_div").show();
      $("#mobile_share_to_grp_div").hide();
      $("#mobile_share_to_dyn_div").hide();
      $("#sharebt").attr("shareto","friend");
    } else if("dyn" == type){
      $("#mobile_share_to_friend_div").hide();
      $("#mobile_share_to_grp_div").hide();
      $("#mobile_share_to_dyn_div").show();
      $("#sharebt").attr("shareto","dyn");  
    } else {
      $("#mobile_share_to_friend_div").hide();
      $("#mobile_share_to_dyn_div").hide();
      $("#mobile_share_to_grp_div").show();
      $("#sharebt").attr("shareto","grp");
    }
    $("textarea.smate_share_text").not(":hidden").val(textStr.val());
    textStr.val("");
  }
}


/**
 * 进入选择好友或群组页面
 */
MobileSmateShare.loadChoosePage  = function(type){
  if ("friend" == type) {
    $("#mobile_share_shareText").val($.trim($("#mobile_share_to_friend_div").find("textarea.smate_share_text").val()));
  }else{
    $("#mobile_share_choose_form").attr("action","/psn/share/choosegrp");
    $("#mobile_share_shareText").val($.trim($("#mobile_share_to_grp_div").find("textarea.smate_share_text").val()));
  }
  $("#mobile_share_choose_form").submit();
}




/**
 * 加载联系人或群组列表并选中
 */
MobileSmateShare.showChooseList = function(searchKey,type){
  var dataJson = {};
  dataJson.searchKey = searchKey;
  var listurl = "/psn/share/ajaxgrplist";
  if (type == "friend") {
    var listurl = "/psn/share/ajaxfriendlist";
  }
  window.Mainlist({
    name : "mobile_share_psn_or_grp_list",
    listurl : listurl,
    listdata : dataJson,
    method : "scroll",
    listcallback : function(xhr) {
      var currentNumItem = $(".main-list__item").length;
      var totalCount = $(".main-list__list").attr("total-count");
      if (totalCount>0 && Number(currentNumItem) >= Number(totalCount)) {
        $(".main-list__list")
            .append(
                "<div class='paper_content-container_list main-list__item' style='border:none; border-bottom:0px!important;'><div style='padding:20px;color:#999;text-align: center; font-size: 14px;'>没有更多记录</div></div>");
      }
      if (totalCount <= 0) {
        $("div.response_no-result").remove();
        $(".main-list__list").append("<div class='paper_content-container_list main-list__item' style='border-bottom: 0px dashed #ccc!important;'><div class='response_no-result'>未找到符合条件的记录</div></div>");
      }
      //先清空数组
      chooseArr = [];
      var ids = $("#mobile_share_des3GrpId").val();
      if (type == "friend") {
        ids = $("#mobile_share_des3FriendIds").val();
      }
      if (ids) {
        var idArr = ids.split(",");
        for (var i in idArr) {
          //将已经选中id放入数组
          chooseArr.push(idArr[i]);
          var chooseId = $(".grp[des3GrpId='"+idArr[i]+"']");
          if (type == "friend") {
            chooseId = $(".friend[des3PsnId='"+idArr[i]+"']");
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
MobileSmateShare.choose = function (obj,type) {
  var id = $(obj).attr("des3PsnId");
  if (type == "grp") {
    //群组只支持单选
    $("#mobile_share_des3GrpId").val();
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
    $("#mobile_share_des3GrpId").val($.trim(chooseArr.join(",")));
  } else {
    $("#mobile_share_des3FriendIds").val($.trim(chooseArr.join(",")));
  }
}


$.fn.serializeFormToJson = function(){    
  var o = {};    
  var a = this.serializeArray();    
  $.each(a, function() {    
      if (o[this.name]) {    
          if (!o[this.name].push) {    
              o[this.name] = [o[this.name]];    
          }    
          o[this.name].push(this.value || '');    
      } else {    
          o[this.name] = this.value || '';    
      }    
  });    
  return o;    
}



MobileSmateShare.backShare = function(){
  $("#mobile_share_form").submit();
}


/**
 * 取消分享
 */
MobileSmateShare.cancelShare = function (fromPage) {
  if(fromPage == "grpdyn"){
    window.location.href = "/grp/main?des3GrpId="+encodeURIComponent($("#mobile_share_currentDes3GrpId").val());
  }
  
}
