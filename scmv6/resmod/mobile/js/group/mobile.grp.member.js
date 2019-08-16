var Group = Group ? Group : {};
//进入人员主页
Group.goToPersonalPage = function(des3PsnId) {
  window.location.href = "/psnweb/mobile/outhome?des3ViewPsnId=" + encodeURIComponent(des3PsnId);
}

//发送消息
Group.sendMsg = function(des3PsnId, event){
  event.stopPropagation();
  window.location.href = "/dynweb/mobile/ajaxshowchatui?des3ChatPsnId=" + encodeURIComponent(des3PsnId);
}

// 添加好友请求
Group.addFriendReq = function(des3PsnId, event) {
  event.stopPropagation();
  $.ajax({
    url : '/psn/friend/ajaxadd',
    type : 'post',
    data : {
      'reqFriendIds' : encodeURIComponent(des3PsnId)
    },
    dataType : 'json',
    timeout : 10000,
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
        if (data.result == "success") {
          scmpublictoast("请求发送成功", 2000);
        } else if(data.result == "error" && data.errorCode == "4"){
          scmpublictoast("对方不接受联系人请求", 3000);
        }else{
          scmpublictoast("请求发送失败", 2000);
        }
      }, 3000);
    },
    error : function() {
      scmpublictoast("请求发送失败", 3000);
    }
  });
}

//显示加载圈
Group.loadIco = function($obj, addWay) {
  if (addWay == null) {
    addWay = "html";
  }
  $("*[scm_id='load_state_ico']").remove();
  $obj.doLoadStateIco({
    "style" : "height: 38px; width:38px; margin:auto ; margin-top:100px;",
    "status" : 1,
    "addWay" : addWay
  });
}


//加载群组成员列表
Group.initGrpMemberScroll = function(){
  var url = $("#isLogin").val()=="false" ? "/grp/outside/member/ajaxlist" : "/grp/member/ajaxlist";
  Group.memberListReq = window.Mainlist({
    name : "mobile_grp_members_model",
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

//加载成员和申请人员列表
Group.showMembers = function(){
  //加载申请人员列表，管理员才有权限
  var psnRole = $("#role").val();
  if(psnRole == 1 || psnRole == 2){
    $.ajax({
      url: "/grp/member/ajaxapply",
      type: "post",
      data: {
        "des3GrpId": encodeURIComponent($("#des3GrpId").val())
      },
      dataType: "html",
      success: function(data){
        $("#group_member_list").prepend(data);
        //加载群组成员列表
        Group.initGrpMemberScroll();
      },
      error: function(){
        //加载群组成员列表
        Group.initGrpMemberScroll();
      }
    });
  }else{
    //加载群组成员列表
    Group.initGrpMemberScroll();
  }
}

//处理申请, 1同意，0忽略
Group.dealApply = function(optType, targetPsnId, obj, event){
  var psnRole = $("#role").val();
  if(psnRole == 1 || psnRole == 2){
    var itemWith = $("#group_member_list").width();
    $.ajax({
      url: "/grp/apply/ajaxdeal",
      type: "post",
      data: {
        "des3GrpId": encodeURIComponent($("#des3GrpId").val()),
        "targetPsnIds": encodeURIComponent(targetPsnId),
        "disposeType": optType
      },
      dataType: "json",
      success: function(data){
        if(data.result == "success"){
          //移除记录
          var reqItem = $(obj).parents(".mobile_member_req_item[des3PsnId='"+targetPsnId+"']");
          reqItem.moveItem({
            timeOut: 500,
            objTransition: '0.2s ease-in',
            nextAllTransition: "0.4s ease-in",
            objTransform: 'translateX(-'+itemWith+'px)'
          });
          //全部请求处理完后隐藏模块
          setTimeout(function(){
            if($(".mobile_member_req_item").length == 0){
              $("#mobile_grp_member_req_model").hide();
            }
          }, 1000);
          //重新加载成员列表
          if(optType == 1){
            setTimeout(function(){
              Group.memberListReq.resetAllFilterOptions();
            }, 1500);
          }
        }else{
          scmpublictoast("处理失败", 3000);
        }
      },
      error: function(){
        scmpublictoast("处理失败", 3000);
      }
    });
    
  }
  event.stopPropagation();
}

