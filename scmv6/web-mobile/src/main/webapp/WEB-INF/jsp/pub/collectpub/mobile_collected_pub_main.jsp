<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>科研之友</title>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="UTF-8">
<%response.setHeader("cache-control","public"); %>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/plugin/scm.pop.mobile.css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css">
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/weixin/wechat.pub.js?version=1"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/pub.details.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/pub/pub_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod }/mobile/css/mobile.css"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=1"></script>
<script type="text/javascript">
$(function(){
   	  Pub.loadMobilePaperList();//显示数据
	  var targetlist = document.getElementsByClassName("my_paper-item");
      for(var i = 0; i < targetlist.length;i++){
      	  document.getElementsByClassName("bottom_line")[0].style.left = targetlist[0].offsetLeft -24 + "px";
          targetlist[i].onclick = function(){
	          document.getElementsByClassName("paper_selecitme")[0].classList.remove("paper_selecitme");
	          var disleft = this.offsetLeft;
	          this.classList.add("paper_selecitme");
	          this.closest(".paper_title-container").querySelector(".bottom_line").style.left=disleft -24 + "px";
	          if(this.id == "pubPaperList"){
	        	  Pub.loadMobilePaperList();
	          }else if(this.id == "pubRecommendList"){
	        	  window.location.href="/pubweb/mobile/pubrecommendmain";
	          }
          }
      }
   selectRecommendPubMenu('collected');   
      
    if($("#searchStringInput").val()=="" && !$("#searchStringInput").is(":focus")){
	    	$("#showIcon1").show();
	    	$("#showIcon2").hide();
  	}else{
	    	$("#showIcon1").hide();
	    	$("#showIcon2").show();    		
  	}
    $("#searchStringInput").focus(function(){    	      
          if($("#searchStringInput").val()!=""){
            $("#showIcon1").hide();
            $("#showIcon2").show();
          }
    });
    $('#searchStringInput').bind('input propertychange', function() {  
      if($("#searchStringInput").val()!=""){
        $("#showIcon1").hide();
        $("#showIcon2").show();
      }else{
        $("#showIcon1").show();
        $("#showIcon2").hide();
      } 
    });  
    $("#searchStringInput").blur(function(){
    	setTimeout(function(){
	    	if($("#searchStringInput").val()==""){
		    	$("#showIcon1").show();
		    	$("#showIcon2").hide();
	    	}else{
		    	$("#showIcon1").hide();
		    	$("#showIcon2").show();    		
	    	}    		
    	},100);
    });
});

function select_filter(){
	$("#select_filter_action").submit();
}
function addSearchKey(){
	var searchKey = $("#searchStringInput").val();
	$("#searchKey").val(searchKey);
	 Pub.loadMobilePaperList();
     return;
}
/* function sharePub(des3ResId, pubDB){
    var collectedPubMainParam = {};
    collectedPubMainParam.des3ResId = des3ResId;
    collectedPubMainParam.pubDB = pubDB;
    SmateCommon.checkPubAnyUser(des3ResId,"pubCollect",collectedPubMainParam);
    //wechat.pub.quickShareDyn(1,$("#des3PubId").val());
} */
function showRecommend(){
	window.location.href ="/pub/mobile/pubrecommendmain";
}
function showMypub(){
	window.location.href ="/pub/collect/main";
}
function setSearchKey(){
	var searchKey = $.trim($("#searchStringInput").val());
	$("#searchKey").val(searchKey);
}

/**
 * 成果请求全文
 * 
 * @author houchuanjie
 */
mobile.pub.requestPubFullText = function(des3PubId, pubType, ReceivePsnId) {
    if (des3PubId && pubType) {
        $.ajax({
            url : "/pub/fulltext/ajaxreqadd",
            type : "post",
            dataType : "json",
            data : {
                'des3PubId' : des3PubId,
                'pubType' : pubType,
                "RecvPsnId" : ReceivePsnId
            },
            success : function(data) {
                BaseUtils.ajaxTimeOutMobile(data, function() {
                    if (data.status == "success") {
                        scmpublictoast("发送全文请求成功", 2000,1);
                    } else {
                        scmpublictoast("请求发送失败，请稍后再试", 2000,2);
                    }
                });
            },
            error : function(data) {
                scmpublictoast("请求发送失败，请稍后再试", 2000,2);
            }
        });
    } else {
        scmpublictoast("请求参数不正确", 2000,3);
    }
}

function commonResAward(obj){//赞
  var des3PubId = $(obj).attr("resId");
  var pubDb=$(obj).attr("pubdb");
  if(pubDb == "PDWH"){
    mobile.pub.pdwhAwardOpt(obj);    
  }else{
    mobile.snspub.awardopt(obj);
  }
}
function commonShowReplyBox(obj){//评论
  var des3PubId = $(obj).attr("resId");
  var pubDb=$(obj).attr("pubDb");
  if(pubDb == "PDWH"){
    mobile.pub.pdwhDetails(des3PubId);    
  }else{
    mobile.snspub.details(des3PubId, 'single');
  }
}
function commonShare(obj){//分享
  var des3PubId = $(obj).attr("resId");
  var pubDb=$(obj).attr("pubDb");
  if(pubDb == "PDWH"){
    //需求变更,进入页面分享
    /* $("#shareScreen").attr("des3ResId", des3PubId);
    $("#shareScreen").attr("resType", pubDb == "PDWH" ? 22 : 1);
    $('#dynamicShare').show(); */ 
    mobile.pub.pdwhIsExist(des3PubId, function() {
      SmateCommon.mobileShareEntrance(des3PubId,$(obj).attr("pubDb").toLowerCase());
    });
  }else{
    var collectedPubMainParam = {};
    collectedPubMainParam.des3ResId = des3PubId;
    collectedPubMainParam.pubDB = pubDb;
    SmateCommon.checkPubAnyUser(des3PubId,"pubCollect",collectedPubMainParam);
  }
}
function commonCollectnew(obj){//收藏
  mobile.snspub.collect(obj);
}

function callBackAward(obj,awardCount){//赞回调
  if(awardCount>999){
    awardCount = "1k+";
  }
  $(obj).find(".new-Standard_Function-bar_item").toggleClass("new-Standard_Function-bar_selected");
  var isAward = $(obj).attr("isAward");
  if (isAward == 1) {// 取消赞
    $(obj).attr("isAward", 0);           
    if (awardCount == 0) {
      $(obj).find('span').text("赞");
    } else {
      $(obj).find('span').text("赞" + "(" + awardCount + ")");
    }
  } else {// 赞
    $(obj).attr("isAward", 1);
    $(obj).find('span').text("取消赞" + "(" + awardCount + ")");
  }
}

function callBackShare(obj){

}
function callBackCollect(obj){
  Pub.loadMobilePaperList();
}

</script>
</head>
<body>
  <form action="/pubweb/mobile/loadconditions" method="get" id="select_filter_action">
    <input type="hidden" id="searchKey" name="searchKey" value="${queryModel.searchKey}" />
    <%-- <input type="hidden" id="searchPsnKey" name="searchPsnKey" value="${queryModel.keywords}"/> --%>
    <input type="hidden" id="includeType" name="includeType" value="${queryModel.includeType}" /> <input type="hidden"
      id="searchPubYear" name="searchPubYear" value="${queryModel.publishYear}" /> <input type="hidden"
      id="searchPubType" name="searchPubType" value="${queryModel.pubType}" />
  </form>
  <%-- <input type="hidden" id="keywords" name="keywords" value="${queryModel.keywords}"/> --%>
  <input type="hidden" id="publishYear" name="publishYear" value="${queryModel.publishYear}" />
  <input type="hidden" id="pubType" name="pubType" value="${queryModel.pubType}" />
  <input type="hidden" id="searchPsnId" name="searchPsnId" value="${queryModel.searchPsnId}" />
  <div class="black_top" id="dynamicShare" style="display: none; margin-top: -1px;"
    onclick="javascript: $('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;">
      <div class="screening" style="max-width: 400px" id="shareScreen" onclick="mobile.pub.shareToDyn(this);">
        <h2>
          <a class="ui-link" href="javascript:;" style="color: #333;">立即分享到科研之友</a>
        </h2>
      </div>
    </div>
  </div>
  <input type='hidden' id='des3PubId' name="des3PubId" value />
  <div>
    <div class="paper__func-header" style="z-index: 1">
      <a href="/psnweb/mobile/myhome" style="width: 10%;"> <i class="material-icons paper__func-header__tip"
        style="margin-left: 5px">keyboard_arrow_left</i>
      </a> <span
        style="width: 80%; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 18px;">论文</span>
      <a style="width: 10%;"></a>
    </div>
    <div class="paper__func-tool">
      <div class="paper__func-box" style="width: 96%;">
        <a onclick="addSearchKey()" id="showIcon1" class="paper__func-search" style="margin-top: -10px; width: 7%;"></a>
        <form action="javascript:addSearchKey();" style="padding: 0px; margin: 0px; width: 93%;">
          <input onkeydown="Pub.listenInput(event)" onchange="setSearchKey()" type="search" onchange=""
            id="searchStringInput" class="paper__func-search__flag"
            style="padding-left: 4px; line-height: 24px; font-size: 12px; width: 100%;" placeholder="检索论文"
            value="${queryModel.searchKey}">
        </form>
        <a onclick="addSearchKey()" id="showIcon2" class="paper__func-search" style="width: 8%;height: 33px;"></a>
      </div>
    </div>
  </div>
  <div style="width: 100%;">
    <div class="paper_content-container main-list" id="content_pub_list" style="margin-bottom: 50px;">
      <div class="main-list__list item_no-padding" list-main="mobile_pub_list"></div>
    </div>
    <div class="black_top" style="display: none" id="select"></div>
  </div>
  <%@ include file="/WEB-INF/jsp/mobile/bottom/mobile_recommend_pub_bottom.jsp"%>
</body>
</html>