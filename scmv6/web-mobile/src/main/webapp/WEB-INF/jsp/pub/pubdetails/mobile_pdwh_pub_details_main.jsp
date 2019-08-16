<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title>科研之友</title>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
<script type="text/javascript" src="/resmod/mobile/js/fastclick.min.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/mobile.pub.js"></script>
<script type="text/javascript" src="${resmod }/js/weixin/wechat.pub.js?version=1"></script>
<script type="text/javascript" src="${resmod }/mobile/js/msgbox/mobile.msg.js"></script>
<script type="text/javascript">
$(function(){
//判断是否为iso系统 
  var ua = navigator.userAgent.toLowerCase();
  if(ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/i)){
  //调整app打开按钮的居中
    var oWidth = window.innerWidth;
       document.getElementsByClassName("openpage-inapp_btn")[0].style.left = (oWidth - 96 )/2 + "px";
       window.onresize = function(){
           var oWidth = window.innerWidth;
           document.getElementsByClassName("openpage-inapp_btn")[0].style.left = (oWidth - 96 )/2 + "px";
       }
    $("#openAppBtn").show();
  }
    $.post(
        "/psnweb/outside/ajaxsignature",
        {"currentUrl": encodeURIComponent(window.location.href)},
        function(data){
            if(data.result == "success"){
                smatewechat.customWeiXinShare(
                        data.appId,
                        data.timeStamp, 
                        data.nonceStr,
                        data.signature,
                        $("#pubTitleShare").text(),
                        data.domain + "/pub/outside/pdwh?des3PubId=${pubDetailVO.des3PubId}",
                        data.domain + "/resmod/mobile/images/scm_icon_share.png",
                        $("#pubAbs").text());
            }
        },"json");
    
    $("#pubReplyContent").on('valuechange', function (e, previous) {
         if($.trim($(this).val()).length>0){
             $("#scm_send_comment_btn").removeClass("not_point");
             $("#scm_send_comment_btn").attr("onclick","doPubComment();");
         }else{
             if($.trim($(this).val()).length==0){
                 $("#scm_send_comment_btn").removeAttr("onclick");
                 $("#scm_send_comment_btn").addClass("not_point");
             }
         }
    });
    //评论框点击整个底部都触发
    $("div.m-bottom").on('click',function(e){
        $("#pubReplyContent").focus();
         if (event.stopPropagation) {
             event.stopPropagation(); 
         }else {
             event.cancelBubble = true; 
         }  
    });
    //解决触摸屏300毫秒延时
    window.addEventListener('load', function() {
          FastClick.attach(document.body);
    }, false);
    
    mobile.pub.initPdwhStatistics($("#des3PubId").val());
    ajaxLoadPdwhComments(1,10,2);
    
    autosize(document.querySelectorAll('textarea'));
    mobile.pub.addPDWHPubVisitRecord($("#des3PubId").val());
    document.onclick=function(){
      if($(".sig__out-box__insign").css("display") !="none"){
        $(".sig__out-box__insign").hide();
      }
    }
});
  
function openApp(){
  window.location.href = "/oauth/openApp?service=" + encodeURIComponent(window.location.href);
}

//收藏和取消收藏成果回调
function collectedPubBack(obj,collected,pubId,pubDb){
    if(collected && collected=="0"){
        $(obj).attr("collected","1");
        //scmpublictoast("收藏成功",1000);
        $(obj).remove();
    }else{
        $(obj).attr("collected","0");
        //scmpublictoast("操作成功",1000);
    }
}
function focustext(){
     $("#pubReplyContent").focus();
}


//收藏成果 pubDb:"PDWH"、"SNS", collectedPubBack回调函数
function collectedPdwhPub(des3PubId,pubDb,obj){
  if(obj){
      BaseUtils.doHitMore(obj,500);
  }
  var collected = $(obj).attr("collected");
  var datastr ={"des3PubId":des3PubId,"pubDb":pubDb,"collectOperate":collected};
  $.ajax({
      url:"/pub/optsns/ajaxcollect",
      type:"post",
      dataType:"json",
      data :datastr,
      success:function(data){
          BaseUtils.ajaxTimeOut(data, function(){
              if(data && data.result == "success"){
                  $(obj).attr("collected",collected == "1" ? "0" : "1");
                  if(typeof collectedPubBack == "function"){
                      collectedPubBack(obj,collected,des3PubId,pubDb);
                  }else{
                      scmpublictoast("操作成功",1000,1);
                  }
              }else if(data && data.result == "exist"){
                  scmpublictoast("已收藏该成果",1000,3);
              }else if(data && data.result == "isDel"){
                  scmpublictoast("成果已被删除",1000,3);
              }else{
                  scmpublictoast("操作失败",1000,2);
              }
          });
      },
      error:function(data){
           scmpublictoast(pubi18n.i18n_pubImportError,1000,2);
      }
  });
}


function showReplyBox(){
    var text = $("#pubReplyContent").val().trim();
    if(text.length>0){
        $("#scm_send_comment_btn").removeClass("not_point");
    }else{
        $("#scm_send_comment_btn").addClass("not_point");
    }
    $("#pubReplyBox").slideToggle(function(){
        if($("#pubReplyBox").css("display")!="none"){
            $("#pubReplyContent").focus();
        }
    });
}
function openPsnDetail(obj,event){
    dynamic.openPsnDetail($(obj).attr("des3PsnId"),event)
}
function loadMoreComments(){
    var pageNo = $("#pageNo").val();
    ajaxLoadPdwhComments(parseInt(pageNo) + 1,10,1);
    $("#pageNo").val(parseInt(pageNo) + 1);
}
//请求评论信息  页数，每页数据量，添加方式
function ajaxLoadPdwhComments(pageNo,pageSize,addWay){
    //加载评论列表   后台设置了页数最小数量是10
    $.ajax({
        url:"/pub/outside/ajaxpdwhcomment",
        type: "post",
        data : {
            "des3PubId":$("#des3PubId").val(),
            "page.pageSize": pageSize,
            "page.pageNo":pageNo,
            "page.ignoreMin": true,
            "page.totalCount": $(".commentTotalCount").val()
        },
        dataType: "html",
        success:function(data){
          $("#replyDiv").append(data);
             //加载更多评论 ？
                var replySizeArray =document.getElementsByName("dynReplySize");
                var commentNum=$(".commentTotalCount").val();
            if(addWay==1){
                if(replySizeArray.length > 0){
                    $(".wdful_comments").show();
            }else{
                /*$(".m-bottom").hide();SCM-9371*/
                $(".wdful_comments").hide();
            }
            }
             if(addWay==2){
                if ($("#operateType").val() == "comment") {
                  $("html,body").animate({scrollTop:$("#animatePdwh").offset().top},100);
                }
            }
             //是否显示
             if (replySizeArray.length < parseInt(commentNum)) {
               $("#showMore").show();
           }else{
               $("#showMore").hide();
           }
        }
    });
}
//添加评论
function doPubComment(){
    var replyContent =$.trim($("#pubReplyContent").val()).replace(/\n/g,'<br>');
    if(replyContent.length<=0){
        return;
    }
    mobile.pub.pdwhIsExist($("#des3PubId").val(), function() {
      $.ajax({
        url:"/pub/optpdwh/ajaxcomment",
        type:"post",
        data : {"des3PdwhPubId":$("#des3PubId").val(),"content":replyContent},
        dataType:"json",
        success:function(data){
            if(data&&data.result){
                if(data.result=="success"){
                    ajaxLoadPdwhComments(1,1,2);
                    var commentStr = $(".span_comment").html();
                    var commentCount = commentStr.replace(/\D+/g, "");;
                    commentCount = $.trim(commentCount) != "" ? parseInt(commentCount) : 0;
                    if (commentCount > 0 && commentCount <= 999) {
                      $(".span_comment").html("评论 (" + (commentCount + 1) + ")");
                    }
                    if (commentCount > 999) {
                      $(".span_comment").html("评论 (1k+)");
                    }
                    $("#pubReplyBox").slideUp();
                }
            }
        }
    })
    $("#pubReplyContent").val("");
    $("#pubReplyContent").css("height","20px");
    })
}
function sharePdwhPub(){
  //目前移动端只能分享到站内，暂时这样处理
  if("${pubDetailVO.isLogin }"){
    //需求变更,进入页面分享
    //$('#dynamicShare').show();
    mobile.pub.pdwhIsExist($("#des3PubId").val(), function() {
      SmateCommon.mobileShareEntrance($("#des3PubId").val(),"pdwhDetail");
    })
  }else{
    doLogin();
  }
}

function isMyMyPub(pubId){
  mobile.pub.pdwhIsExist($("#des3PubId").val(), function() {
    var callbackData={
        'pubId':pubId
    };
    mobile.pub.importPdwhPub(callbackData);
  })
}

//在消息通知点击成果标题进入成果详情后返回
function confirmPubBack(){
  if ($("#whoFirst").val() == "" || $("#whoFirst").val() == "undefined") {
    SmateCommon.goBack('/dynweb/mobile/dynshow');
    return;
  }
  window.location.replace("/psnweb/mobile/msgbox?model=centerMsg&whoFirst="+$("#whoFirst").val());
}

function showTab(){
  $(".sig__out-box__insign").toggle();
  event.stopPropagation();
}
  
</script>
</head>
<body class="white_bg" style="-moz-user-select: text; user-select: text; -webkit-user-select: text;">
  <input type = "hidden" value = "${pubDetailVO.isLogin }" id = "pdwh_details_isLogin"/>
  <input type="hidden" id="dev_imp_pdwh" value="" />
  <!-- 全文认领与成果认领谁先显示 -->
  <input type="hidden" id="whoFirst" value="${pubOperateVO.whoFirst }" />
  <input type="hidden" id="pdwhCommentCount" value="" />
  <input id="pageNo" name="pageNo" type="hidden" value="1" />
  <input id="operateType"  type="hidden" value="${pubOperateVO.operateType}" />
  <div class="black_top" id="dynamicShare" style="display: none; margin-top: -1px;"
    onclick="javascript: $('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;">
      <div class="screening" style="max-width: 400px" onclick="mobile.pub.quickShareDyn(22,'${pubDetailVO.des3PubId}');">
        <h2>
          <a id="quickShareOpt" href="javascript:;">立即分享到科研之友</a>
        </h2>
      </div>
    </div>
  </div>
  <div class="new-edit_keword-header" style="overflow-x: initial;">
    <a href="javascript:void();" onclick="confirmPubBack();" class="fl"><i
      class="material-icons ">keyboard_arrow_left</i></a>
    <span class="new-edit_keword-header_title">详情</span>
    <i class="material-icons" style="position: relative;" onclick="showTab();">
            more_horiz
            <div class="sig__out-box sig__out-box__insign" style="z-index: 10001;display: none;">
                <em class="sig__out-header"></em>
                <div class="sig__out-body sig__out-body-container" style="height: auto; border: 1px solid #fefefe; width: 104px;">
                    <div class="sig__out-body-container_list" onclick="sharePdwhPub()">
                        <div class="sig__out-body-container_icon">
                            <i class="sig__out-body-container_icon-share"></i>
                        </div>
                        <span class="sig__out-body-container_detail">分享</span>
                    </div>
                    <div class="sig__out-body-container_line"></div>
                    <c:if test='${!empty pubDetailVO.fullTextDownloadUrl }'>
                    <div class="sig__out-body-container_list" onclick="Msg.downloadFTFile('${pubDetailVO.fullTextDownloadUrl }')">
                        <div class="sig__out-body-container_icon">
                            <i class="sig__out-body-container_icon-load"></i>
                        </div>
                         <span class="sig__out-body-container_detail">下载全文</span>
                    </div>
                   </c:if>
                    <c:if test='${empty pubDetailVO.fullTextDownloadUrl }'>
                     <div class="sig__out-body-container_list" onclick="mobile.pub.requestPubFullText('${pubDetailVO.des3PubId}', 'pdwh', '')">
                        <div class="sig__out-body-container_icon">
                            <i class="material-icons">vertical_align_top</i>
                        </div>
                         <span class="sig__out-body-container_detail">请求全文</span>
                    </div>
                    </c:if>
                </div>
            </div>
        </i>
  </div>
  <div class="loading_wrap" id="loading_wrap" style="display: none">
    <div class="loading">
      <span>正在加载</span>
    </div>
  </div>
  <div class="top_clear"></div>
  <div class="content">
    <div id="touchDiv">
    
      <c:if test="${pubDetailVO.isLogin }"><%@ include file="mobile_pdwhpub_detail_sub.jsp"%></c:if>
      <c:if test="${!pubDetailVO.isLogin }"><%@ include file="mobile_pdwhpub_detail_sub_outside.jsp"%></c:if>
    </div>
  </div>
   <div class="openpage-inapp_btn" onclick="openApp()" id="openAppBtn" style="display:none">在App中打开</div>
  <div class="bottom_clear"></div>
  <div class="m-bottom">
    <div class="m-bottom_wrap" id="pubReplyBox" style="display: flex; align-items: center;">
      
      <div class="input_box" id="pubComment" onclick="focustext();" style="width: 85%; margin: 0px;">
        <textarea autofocus placeholder="添加评论" id="pubReplyContent" name="comments" maxlength="250" rows="1"
          style="white-space: pre-wrap; text-indent: 0px; border: none; "></textarea>
      </div>
      <input type="button" id="scm_send_comment_btn" value="发布" class="not_point" style="width: 15%;">
    </div>
  </div>
  <c:if test="${!pubDetailVO.isLogin }">
    <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
  </c:if>
</body>
</html>