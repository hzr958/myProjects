var Group = Group ? Group : {};

Group.findGroupList = function(){
  var dataJson = {};
  dataJson.grpCategory = $("#grpCategory").val();
  dataJson.disciplineCategory = $("#disciplineCategory").val();
  dataJson.searchKey = $("#searchKey").val();
  
  $gcmdGrpList = window.Mainlist({
    name : "grprcmdlist",
    listurl : "/grp/mobile/findgrouplist",
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
        $(".main-list__list").append("<div class='paper_content-container_list main-list__item' style='border:0; border-bottom: 0px dashed #ccc!important;'><div class='response_no-result'>未找到符合条件的记录</div></div>");
      }
      
      Pulldown.slide(".main-list", 60, function (e) {
        setTimeout(function () {
          Group.findGroupList();
        }, 500);
      });
    }
  });
};
/**
 * 推荐群组 忽略或者申请
 */

Group.optRcmdGrp = function(obj, des3GrpId, rcmdStatus, openType) {
  var dataJson = {
    'des3GrpId' : des3GrpId,
    'rcmdStatus' : rcmdStatus,
    'isApplyJoinGrp' : 1
  };
  $.ajax({
    url : '/grp/mobile/optionRcmdGrp',
    type : 'post',
    dataType : 'json',
    data : dataJson,
    success : function(data) {
      BaseUtils.ajaxTimeOutMobile(data, function() {
        if (data.status == "success") {
          if (rcmdStatus == 1) {// 申请群组后的操作         
            if (openType != undefined && openType == 'O') {
              // 公开群组
              newMobileTip("加入成功");    
              setTimeout(function() {
                openGrpDetail(des3GrpId);
              }, 1000);
              return;
            }else{
              newMobileTip("请求已发送");              
            }
          }else{
            newMobileTip("操作成功"); 
          }
          $(obj).closest(".main-list__item").attr("style","display:none!important;");                  
        } else if (data.status == "fail") {
          newMobileTip("操作失败");
        }
      });
    },
    error : function() {
      newMobileTip("操作失败");
    }
  });
};

Group.myGroupList = function(){
  var dataJson = {};
  dataJson.grpCategory = $("#grpCategory").val();
  dataJson.searchByRole = $("#searchByRole").val();
  dataJson.searchKey = $("#searchKey").val();
  
  $gcmdGrpList = window.Mainlist({
    name : "mygrplist",
    listurl : "/grp/mobile/mygrouplist",
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

      Pulldown.slide(".main-list", 60, function (e) {
       setTimeout(function () {
         Group.myGroupList();
       }, 500);
      });
    }
  });
}


