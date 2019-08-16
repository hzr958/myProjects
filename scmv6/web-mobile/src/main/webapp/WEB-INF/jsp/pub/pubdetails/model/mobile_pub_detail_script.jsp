<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
  <script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<%--   <script type="text/javascript" src="${resmod }/mobile/js/jquery.mobile-1.4.5.min.js"></script> --%>
  <script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
  <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
  <script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=1"></script>
  <script type="text/javascript" src="${resmod }/mobile/js/msgbox/mobile.msg.js"></script>
  <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
  <script type="text/javascript" src="${resmod}/mobile/js/pub/pub.details.js"></script>
  <script type="text/javascript" src="${resmod}/mobile/js/pub/pub.details_${locale}.js"></script>
  <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
  <script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
  <script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
  <script  src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
  <script type="text/javascript" src="${resmod}/mobile/js/plugin/swiper/swiper.min.js"></script>
  <script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script>
var pubSum = $("#pubSum").val();
//从sessionStorage中取出检索条件进行初始化
function initSearchParams(){
  var pubFilterInfo = sessionStorage.setItem("pub_filter_info");
  if(pubFilterInfo != null){
    $("#searchOrderBy").val(pubFilterInfo.orderBy);
    $("#searchPubDBIds").val(pubFilterInfo.includeType);
    $("#publishYear").val(pubFilterInfo.publishYear);
    $("#searchPubType").val(pubFilterInfo.searchPubType);
    $("#des3SearchPsnId").val(pubFilterInfo.des3SearchPsnId);
  }
}

function getPubXml(index, swiperObj){
  var des3PubId = "";
  var referrer ="";
  if(document.referrer.indexOf("/oauth/index") != -1 && $("#hasLogin").val() == 1 && $("#isMyPub").val() == 'true' && $("#referrer").val() == "") {
    des3PubId = $("#des3PubId").val();
    referrer = "login";
  }
  var data= {
         "orderType":$("#orderType").val(),
         "pubType":$("#pubType").val(),
         "pubLocale":$("#pubLocale").val(),
         "articleType":$("#articleType").val(),
         "detailPageNo": index,
         "detailPageSize":$("#detailPageSize").val(),
         "des3SearchPsnId":$("#des3SearchPsnId").val(),
         "useoldform":$("#useoldform").val(),
         "fromPage":$("#fromPage").val(),
         "searchPubType":$("#searchPubType").val(),
         "publishYear":$("#publishYear").val(),
         "includeType":$("#searchPubDBIds").val(),
         "orderBy":$("#searchOrderBy").val(),
         "des3PubId":des3PubId,
         "referrer":referrer
         }
    var url = "/pub/outside/details/ajaxsns";
    if($("#hasLogin").val() == 1){
        url = "/pub/details/ajaxsns";
    }
    $.ajax( {
        url : url,
        type : 'post',
        dataType:'html',
        data : data,
        async: false,
        success : function(data) {
            dealWithSlidesAfterLoadDetails(index, data, swiperObj);
            changeLocationUrl();
            changeMoreMenu();
            if($("#isMyPub").val() != "true"){
                SmateCommon.addVisitRecord($("#des3PsnId").val(),$("#des3PubId").val(),1);//添加访问记录
            }
            if(document.referrer.indexOf("/oauth/index") != -1 && $("#hasLogin").val() == 1 && $("#isMyPub").val() == 'true' && $("#referrer").val() == "login") {
              $("#detailPageNo").val($("#nextPageNo").val());
              $("#pubSum").val($("#allPubSum").val());
            }
        },error :function(data){
          scmpublictoast("网络异常！", 1000, 2);
          $("#loading").hide();
        }
    });
  }

  //初始化swiper，具体的文档可以自己百度一下swiper
  var currentActiveIndex;
  var targetIndex = parseInt($("#detailPageNo").val());
  targetIndex = !isNaN(targetIndex) && targetIndex - 1 >= 0 ? targetIndex - 1 : 0;
  var mySwiper = new Swiper ('.swiper-container', {
    initialSlide: targetIndex,
    on: {
      transitionEnd: function(){
        if(currentActiveIndex != this.activeIndex){
          $('body').prop('scrollTop','0');
          getPubXml(this.activeIndex, this);
          currentActiveIndex = this.activeIndex;
        }
      },
    },
    // 如果需要分页器
    pagination: {
      el: '.swiper-pagination',
      dynamicBullets: true,
      dynamicMainBullets: 5
    },
    //autoHeight: true,
    preventInteractionOnTransition : true,
    virtual: {
      slides: (function () {
        var slides = [];
        for (var i = 0; i < pubSum; i += 1) {
          slides.push('<div class="preloader active" scm_id="load_state_ico" style="height:40px; width:40px; margin:auto;margin-top:60%;"><div class="preloader-ind-cir__box" style="width: 24px; height: 24px;"><div class="preloader-ind-cir__fill"><div class="preloader-ind-cir__arc-box left-half"><div class="preloader-ind-cir__arc"></div></div><div class="preloader-ind-cir__gap"><div class="preloader-ind-cir__arc"></div></div><div class="preloader-ind-cir__arc-box right-half"><div class="preloader-ind-cir__arc"></div></div></div></div></div>');
        }
        return slides;
      }()),
    },
  });

  
  
//加载完成果详情后，清空前后的slide中的成果详情内容
  function dealWithSlidesAfterLoadDetails(index, data, swiperObj){
    //清空前一个slide
    var preSlide = $(".swiper-slide-prev");
    preSlide.html("");
    preSlide.doLoadStateIco({
      style:"position: relative; height:40px; width:40px;  margin-left:50%; margin-right: 50%; margin-top:60%;",
      status:1
    });
    //填充当前的slide
    $(".swiper-slide-active").html(data);
    //清空后一个slide
    var nextSlide = $(".swiper-slide-next");
    nextSlide.html("");
    nextSlide.doLoadStateIco({
      style:"position: relative; height:40px; width:40px;  margin-left:50%; margin-right: 50%; margin-top:60%;",
      status:1
    });
  }
  
  //改变浏览器地址栏地址
  function changeLocationUrl(){
    var des3PubId = $("#des3PubId").val();
    var des3SearchPsnId = $("#des3SearchPsnId").val();
    var domain = $("#domain").val();
    var newUrl = domain + "/pub/outside/details/list?des3PubId="+encodeURIComponent(des3PubId)+"&des3SearchPsnId=" + encodeURIComponent(des3SearchPsnId) +
    "&orderBy=" + $.trim($("#searchOrderBy").val()) + "&publishYear=" + $.trim($("#publishYear").val()) + "&searchPubType=" + $.trim($("#searchPubType").val()) +
    "&includeType=" + $.trim($("#searchPubDBIds").val());
    window.history.replaceState({}, "", newUrl);
  }
  
  //改变...操作菜单
  function changeMoreMenu(){
    var isOwner = $("#isOwner").val();
    var fulltextPermission = $("#file_permission").val();
    var downloadUrl = $("#pubFullTextDownloadUrl").val();
    if(isOwner == "true"){
      if(!BaseUtils.checkIsNull(downloadUrl)){
        $("#more_opt_download_fulltext").show();
        $("#more_opt_req_fulltext").hide();
      }else{
        $("#more_opt_download_fulltext").hide();
        $("#more_opt_req_fulltext").hide();
      }
    }
    if(isOwner != "true"){
      if(fulltextPermission == "0"){
        $("#more_opt_download_fulltext").show();
        $("#more_opt_req_fulltext").hide();
      }else{
        $("#more_opt_download_fulltext").hide();
        $("#more_opt_req_fulltext").show();
      }
    }
  }
  
  
$(function(){
  //查看第一条成果时不会触发swiper的transitionEnd事件，需要主动去加载
  if(targetIndex == 0){
    $('body').prop('scrollTop','0');
    getPubXml(this.activeIndex, this);
    currentActiveIndex = this.activeIndex;
  }
  $("#pubReplyContent").on('valuechange', function (e, previous) {
       if($.trim($(this).val()).length>0){
           $("#scm_send_comment_btn").removeClass("not_point");
           $("#scm_send_comment_btn").attr("style","color:#2782d7!important");
           $("#scm_send_comment_btn").attr("onclick","doPubComment();");
       }else{
           if($.trim($(this).val()).length==0){
               $("#scm_send_comment_btn").removeAttr("onclick");
               $("#scm_send_comment_btn").attr("style","color:#ccc!important");
               $("#scm_send_comment_btn").addClass("not_point");
           }
       }
  });
  $("#pubReplyContent").bind('input propertychange', function(){
      if($("#hasLogin").val() == 0){
          Pubdetails.showMobileLoginTips($("#loginTargetUrl").val());
      }
  });
  showOpenAppBtn();
  $("#pub_detail_content_container").attr("style", "min-height: " + document.body.clientHeight + "px;");
  
  document.onclick=function(){
      if($(".sig__out-box__insign").css("display") !="none"){
      $(".sig__out-box__insign").hide();
    }
  }
});

function openApp(){
  window.location.href = "/oauth/openApp?service=" + encodeURIComponent(window.location.href);
}

function hideFullTextUpload(){
    $("#fulltext_upload").hide();
}
//收藏成果到我 的文献
function FullTextImpMyRef(obj){
    var des3PubId = $("#des3PubId").val();
    if(des3PubId!=null&&des3PubId!=""){
        $.ajax({
            url:"/pub/optsns/ajaxcollect",
            type:"post",
            dataType:"json",
            data:{"des3PubId":des3PubId,"pubDb":"SNS",collectOperate:"0"},
            success:function(data){
                Pub.ajaxTimeOut(data, function(){
                    if(data && data.result == "success"){
                        $(obj).remove();
                        //scmpublictoast("收藏成功",1000);
                    }else if(data && data.result == "exist"){
                        scmpublictoast("已收藏该成果",1000,3);
                    }else if(data && data.result == "isDel"){
                        scmpublictoast("成果已被删除",1000,3);
                    }else{
                        scmpublictoast("操作失败",1000,2);
                    }
                });
            }
        });
    }
}

openFile = function (fileId) {
    document.location.href ="${scmDomain}${snsctx }/archiveFiles/downLoadNoVer.action?fdesId=" + encodeURIComponent(fileId)+"&nodeId=1&type=0";
};
//收藏和取消收藏成果回调
function collectedPubBack(obj,collected,pubId,pubDb){
    if(collected && collected=="0"){
        $(obj).attr("collected","1");
        scmpublictoast(pubi18n.i18n_collectionSuccess,1000,1);
        $(obj).remove();
    }else{
        $(obj).attr("collected","0");
        scmpublictoast(pubi18n.i18n_optSuccess,1000,1);
    }
}

function showTab(){
  $(".sig__out-box__insign").toggle();
  event.stopPropagation();
}

function requestPubFullText(){
  $("#requestPubFullTextTab").click();
}

function showOpenAppBtn(){
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
}

</script>