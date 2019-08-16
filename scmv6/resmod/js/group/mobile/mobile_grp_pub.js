var GrpPub = GrpPub ? GrpPub : {};

GrpPub.grpSharePubList = function(){
  var dataJson = {};
  dataJson.des3GrpId = encodeURIComponent($("#des3GrpId").val());
  dataJson.searchKey = $("#searchKey").val();
  dataJson.pubListType = $("#pubListType").val();
  
  $gcmdGrpList = window.Mainlist({
    name : "grppublist",
    listurl : "/grp/mobile/grpsharepublist",
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
};
GrpPub.grpPubList = function(){
  var dataJson = {};
  dataJson.des3GrpId = encodeURIComponent($("#des3GrpId").val());
  dataJson.searchKey = $("#searchKey").val();
  dataJson.publishYear = $("#publishYear").val();
  dataJson.pubType = $("#pubType").val();
  dataJson.includeType = $("#includeType").val();
  dataJson.orderBy = $("#orderBy").val();
  dataJson.showPrjPub = $("#showPrjPub").val();
  dataJson.showRefPub = $("#showRefPub").val();
  var url = $("#isLogin").val()=="false" ? "/grp/outside/mobile/grppublist" : "/grp/mobile/grppublist";

  $gcmdGrpList = window.Mainlist({
    name : "grppublist",
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
      if (Number(currentNumItem) <= 10) {//第一页滚动到顶部
        $("body").scrollTop(0); 
      }
    }
  });
};
//下载全文
GrpPub.downloadFile = function(url) {
  if (!!url && url != "") {
    Smate.confirm("下载提示", "要下载全文附件吗？", function() {
      window.location.href = url;
    }, ["下载", "取消"]);
  }
};