<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/scmmobileframe.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">

<link href="${resmod}/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/wechat.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/prj/mobile_prj.js"></script>
<script src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/fastclick.min.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript">

$(function(){
  //判断是否为iso系统 
  document.getElementsByClassName("detail")[0].style.minHeight = window.innerHeight - 48 - 55 - 130 + "px";
  document.getElementsByClassName("detail-contents")[0].style.minHeight = window.innerHeight - 48 - 55 - 130 + "px";
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
   $('#prjReplyContent').bind('input propertychange', function() { 
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

	$("#prj_title").html($("#prj_title").text());
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
	                        $("#title").val(),
	                        data.domain + "/prjweb/wechat/findprjxml?des3PrjId="+encodeURIComponent("${des3PrjId}"),
	                        data.domain + "/resmod/mobile/images/scm_icon_share.png",
	                        $("#prjAbs").text());
	            }
	        },"json");
    if("${wxOpenId}"){
        smatewechat.initWeiXinShare("${appId}","${timestamp}", "${nonceStr}","${signature}");
    }
    if($("#isMyPrj").val() != "true"){
        //添加访问记录
        SmateCommon.addVisitRecord($("#ownerPsnId").val(),$("#des3PrjId").val(),4);
    }
    $("#title").html($("#title").text());
    showPrjCommentList();//加载评论信息
    
   //解决触摸屏300毫秒延时
   window.addEventListener('load', function() {
         FastClick.attach(document.body);
   }, false);
   
});

function openApp(){
  window.location.href = "/oauth/openApp?service=" + encodeURIComponent(window.location.href);  
}

//评论框点击整个底部都触发
$("div.m-bottom").on('click',function(e){
    $("#prjReplyContent").focus();
     if (event.stopPropagation) {
         event.stopPropagation(); 
     }else {
         event.cancelBubble = true; 
     }  
}); 

function doPubComment(){
	var replyContent =$.trim($("#prjReplyContent").val());
    if(replyContent.length<=0){
    	scmpublictoast("请输入评论内容",1000, 3);
    	$("#prjReplyContent").focus();
        return;
    }
    $.ajax({
        url :"/prjweb/project/ajaxaddcomment",
        type : "post",
        data : {
            "comment":replyContent,
            "des3PrjId":$("#des3PrjId").val()
            },
        dataType:"json",
        success:function(data){
            if(data&&data.result){
                if(data.result=="success"){
                	showPrjCommentList();//加载评论信息
                    commentCount = (data.commentCount != null && data.commentCount != "") ? parseInt(data.commentCount) : 0;
                    if (commentCount > 0 && commentCount <= 999) {
                      $(".span_comment").html("评论 (" + commentCount + ")");
                    }
                    if (commentCount > 999) {
                      $(".span_comment").html("评论 (1k+)");
                    }

                    
                    $("#pubReplyBox").slideUp();
                }
            }
        }
    })
    $("#prjReplyContent").val("");
    $("#prjReplyContent").css("height","20px");
};
/* function sharePub(obj){
    $('#shareScreen').attr("des3ResId",$(obj).attr("des3ResId"));
    $('#dynamicShare').show();
}; */
//加载评论
function showPrjCommentList(){
    var data= {
            "des3PrjId":$("#des3PrjId").val()
            }
    document.getElementsByTagName('body')[0].scrollTop = 0;
    $(".main-list__list").empty();
    window.Mainlist({
        name: "reply_prj_list",
        listurl: "/prjweb/outside/ajaxprjcommentshow",
        listdata: data,
        method: "scroll",
        beforeSend:function(){
            $(".main-list__list").doLoadStateIco({status:1,addWay:"append"});
        },
        listcallback: function(xhr) {           
            $(".response_no-result").remove();
            const parentNode = $("div[list-main='mobile_pub_list']");
            var norecord = parentNode.find("div.response_no-result");
            if(norecord.length > 0){
                var tips = norecord[0].innerText;
                parentNode.find("div.response_no-result").remove();
                parentNode.append("<div class='paper_content-container_list main-list__item' style='border:0'><div class='response_no-result'>"+tips+"</div></div>");
            }

        }
    });
};

function focustext(){
    $("#prjReplyContent").focus();
}; 

function showReplyBox(){
    var text = $("#prjReplyContent").val().trim();
    if(text.length>0){
        $("#scm_send_comment_btn").removeClass("not_point");
    }else{
        $("#scm_send_comment_btn").addClass("not_point");
    }
    $("#pubReplyBox").slideToggle(function(){
        if($("#pubReplyBox").css("display")!="none"){
            $("#prjReplyContent").focus();
        }
    });
};

function commonResAward(obj){
  Project.award(obj);
}
function commonShowReplyBox(obj){
  showReplyBox();
}
function commonShare(obj){
  //需求变更,移动端分享全部进入页面分享
  /* var des3PrjId = $(obj).attr("resId");
  $('#shareScreen').attr("des3ResId",des3PrjId);
  $('#dynamicShare').show(); */
  SmateCommon.mobileShareEntrance($(obj).attr("resId"),"prjDetail");
}


function callBackAward(obj,awardCount){
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
</script>
</head>
<body style="background-color: white;">
  <input type="hidden" name="ownerPsnId" id="ownerPsnId" value="${des3OwnerPsnId }" />
  <input type="hidden" name="des3PrjId" id="des3PrjId" value="${des3PrjId}" />
  <input type="hidden" name="isMyPrj" id="isMyPrj" value="${isMyPrj}" />
  <div id="header" class="fund__page-header">
    <span class="fund__page-icon"><i class="material-icons"
      onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i></span> <span class="fund__page-title"
      style="margin-right: 40px !important;">项目详情</span>
  </div>
  <div class="detail" style="padding-top: 36px; ">
    <s:if test="prjXml==null">
      <dl>
        <dd>未找到对应项目详情</dd>
      </dl>
    </s:if>
    <s:else>
      <x:parse xml="${prjXml}" var="myoutprj" />
      
      <dl  class="detail-contents" style="display: flex; flex-direction: column;  justify-content: space-between;">
      <div>
        <!-- 标题 -->
        <c:set var="zhtitle">
          <x:out select="$myoutprj/data/project/@zh_title" escapeXml="false" />
        </c:set>
        <c:set var="entitle">
          <x:out select="$myoutprj/data/project/@en_title" escapeXml="false" />
        </c:set>
        <h2>
        <span id="prj_title" style="color:black;font-size:18px;">
            ${prjInfo.title}  
        </span>
        </h2>
        <input type="hidden" id="title" value="${prjInfo.title}" />
        <dt style="font-size:14px;">
          <!-- 作者-->
          <c:set var="author_names">
            <x:out select="$myoutprj/data/project/@author_names" escapeXml="false" />
          </c:set>
          <c:if test="${!empty author_names}">${author_names}</c:if>
        </dt>
        <!-- 依托单位 -->
        <c:set var="ins_name">
          <x:out select="$myoutprj/data/project/@ins_name" />
        </c:set>
        <c:if test="${!empty ins_name}">
          <h5 style="line-height: 30px;font-size:14px;">
            <%-- <s:text name="projectEdit.label.prj_ins" /> --%>
            <span style="font-weight:bold; ">依托单位:</span>&nbsp;${ins_name}
          </h5>
        </c:if>
        <!--资助机构，类别  -->
        <c:set var="scheme_name">
          <x:out select="$myoutprj/data/project/@scheme_name" escapeXml="false" />
        </c:set>
        <c:set var="scheme_agency_name">
          <x:out select="$myoutprj/data/project/@scheme_agency_name" escapeXml="false" />
        </c:set>
        <c:if test="${!empty scheme_name || !empty scheme_agency_name}">
          <c:set var="zh_scheme">1</c:set>
        </c:if>
        <c:set var="scheme_name_en">
          <x:out select="$myoutprj/data/project/@scheme_name_en" escapeXml="false" />
        </c:set>
        <c:set var="scheme_agency_name_en">
          <x:out select="$myoutprj/data/project/@scheme_agency_name_en" escapeXml="false" />
        </c:set>
        <c:if test="${!empty scheme_name_en || !empty scheme_agency_name_en}">
          <c:set var="en_scheme">1</c:set>
        </c:if>
        <c:choose>
          <c:when test="${locale eq 'zh_CN' }">
            <c:if test="${!empty zh_scheme}">
              <h5 style="line-height: 30px;font-size:14px;">
                <%-- <s:text name="prjView.label.scheme" /><s:text name="colon.all" /> --%>
                <span style="font-weight:bold; ">资助机构 - 类别:</span>&nbsp;
                <c:out value="${scheme_agency_name}" />
                <c:if test="${!empty scheme_agency_name && !empty scheme_name}">-</c:if>
                <c:out value="${scheme_name }" />
              </h5>
            </c:if>
            <c:if test="${!empty en_scheme && empty zh_scheme}">
              <h5 style="line-height: 30px;font-size:14px;">
                <%-- <s:text name="prjView.label.scheme" /><s:text name="colon.all" /> --%>
                <span style="font-weight:bold; ">资助机构 - 类别:</span>&nbsp;
                <c:out value="${scheme_agency_name_en}" />
                <c:if test="${!empty scheme_agency_name_en && !empty scheme_name_en}">-</c:if>
                <c:out value="${scheme_name_en}" />
              </h5>
            </c:if>
          </c:when>
          <c:otherwise>
            <c:if test="${!empty en_scheme}">
              <h5 style="line-height: 30px;font-size:14px;">
                <%-- <s:text name="prjView.label.scheme" /><s:text name="colon.all" /> --%>
               <span style="font-weight:bold; "> 资助机构 - 类别:</span>&nbsp;
                <c:out value="${scheme_agency_name_en}" />
                <c:if test="${!empty scheme_agency_name_en && !empty scheme_name_en}">-</c:if>
                <c:out value="${scheme_name_en}" />
              </h5>
            </c:if>
            <c:if test="${!empty zh_scheme && empty en_scheme}">
              <h5 style="line-height: 30px;font-size:14px;">
                <%-- <s:text name="prjView.label.scheme" /><s:text name="colon.all" /> --%>
               <span style="font-weight:bold; "> 资助机构 - 类别:</span>&nbsp;
                <c:out value="${scheme_agency_name}" />
                <c:if test="${!empty scheme_agency_name && !empty scheme_name}">-</c:if>
                <c:out value="${scheme_name }" />
              </h5>
            </c:if>
          </c:otherwise>
        </c:choose>
        <!--  -->
        <c:set var="prj_internal_no">
          <x:out select="$myoutprj/data/project/@prj_internal_no" />
        </c:set>
        <c:if test="${!empty prj_internal_no}">
          <h5 style="line-height: 30px;font-size:14px;"><span style="font-weight:bold; ">项目批准号（本机构）:</span>&nbsp;${prj_internal_no}</h5>
        </c:if>
        <c:set var="prj_external_no">
          <x:out select="$myoutprj/data/project/@prj_external_no" />
        </c:set>
        <c:if test="${!empty prj_external_no}">
          <h5 style="line-height: 30px;font-size:14px;"><span style="font-weight:bold; ">项目批准号（资助机构）:</span>&nbsp;${prj_external_no}</h5>
        </c:if>
        <!-- 资金总数  -->
        <c:set var="amount">
          <x:out select="$myoutprj/data/project/@amount" />
        </c:set>
        <c:set var="amount_unit">
          <x:out select="$myoutprj/data/project/@amount_unit" />
        </c:set>
        <c:if test="${!empty prj_external_no}">
          <h5 style="line-height: 30px;font-size:14px;"><span style="font-weight:bold; ">资金总数:</span>&nbsp;${amount}&nbsp;${amount_unit}</h5>
        </c:if>
        <!-- 项目日期 -->
        <c:set var="start_year">
          <x:out select="$myoutprj/data/project/@start_year" />
        </c:set>
        <c:set var="start_month">
          <x:out select="$myoutprj/data/project/@start_month" />
        </c:set>
        <c:set var="start_day">
          <x:out select="$myoutprj/data/project/@start_day" />
        </c:set>
        <c:set var="end_year">
          <x:out select="$myoutprj/data/project/@end_year" />
        </c:set>
        <c:set var="end_month">
          <x:out select="$myoutprj/data/project/@end_month" />
        </c:set>
        <c:set var="end_day">
          <x:out select="$myoutprj/data/project/@end_day" />
        </c:set>
        <c:if test="${!empty start_year}">
          <h5 style="line-height: 30px;font-size:14px;">
            <span style="font-weight:bold; ">项目日期:</span>&nbsp;
            <c:if test="${!empty end_year}">
              <c:if test="${!empty start_month && !empty end_month}">${start_year}/${start_month }/${start_day}&nbsp;-&nbsp;${end_year}/${end_month}/${end_day}
            </c:if>
              <c:if test="${empty start_month && !empty end_month}">
                 ${start_year}&nbsp;-&nbsp;${end_year}/${end_month}/${end_day}
            </c:if>
              <c:if test="${!empty start_month && empty end_month}">
                ${start_year}/${start_month }/${start_day}&nbsp;-&nbsp;${end_year}
            </c:if>
              <c:if test="${empty start_month && empty end_month}">
                 ${start_year}&nbsp;-&nbsp;${end_year}
            </c:if>
            </c:if>
            <c:if test="${empty end_year}">
              <c:if test="${!empty start_month}">
                <c:if test="${!empty start_day}">
                    ${start_year}/${start_month }/${start_day}
                </c:if>
                <c:if test="${empty start_day}">
                    ${start_year}/${start_month }
                </c:if>
              </c:if>
              <c:if test="${empty start_month}">
                ${start_year}
            </c:if>
            </c:if>
          </h5>
        </c:if>
        <!-- 摘要 -->
        <p style="font-size:14px;">
          <c:set var="zh_abstract">
            <x:out select="$myoutprj/data/project/@zh_abstract" escapeXml="false" />
          </c:set>
          <c:set var="en_abstract">
            <x:out select="$myoutprj/data/project/@en_abstract" escapeXml="false" />
          </c:set>
          <c:if test="${!empty prjInfo.prjAbstract}">
            <span style="font-weight:bold; ">摘要:</span>&nbsp;<span id="prjAbs">${prjInfo.prjAbstract}</span>
          </c:if>
        </p>
        <h3></h3>
        <!-- 关键字 -->
        <p style="font-size:14px;">
          <c:set var="zh_keywords">
            <x:out select="$myoutprj/data/project/@zh_keywords" escapeXml="false" />
          </c:set>
          <c:set var="zh_keywords">${fn:replace(zh_keywords,' ','')}</c:set>
          <c:set var="zh_keywords">${fn:replace(zh_keywords,';','；')}</c:set>
          <c:set var="en_keywords">
            <x:out select="$myoutprj/data/project/@en_keywords" escapeXml="false" />
          </c:set>
          <c:choose>
            <c:when test="${locale eq 'zh_CN' }">
              <c:if test="${!empty zh_keywords}">
                        <span style="font-weight:bold; ">关键词:</span>&nbsp;${zh_keywords }&nbsp;
                </c:if>
              <c:if test="${!empty en_keywords && empty zh_keywords}">
                       <span style="font-weight:bold; "> 关键词:</span>&nbsp;${en_keywords }
                </c:if>
            </c:when>
            <c:otherwise>
              <c:if test="${!empty en_keywords}">
                        <span style="font-weight:bold; ">关键词:</span>&nbsp;${en_keywords }
                </c:if>
              <c:if test="${!empty zh_keywords && empty en_keywords}">
                        <span style="font-weight:bold; ">关键词:</span>&nbsp;${zh_keywords }
                </c:if>
            </c:otherwise>
          </c:choose>
        </p>
        </div>
    </s:else>
 
  <%--社交操作start --%>
        <c:set var="isAward" value="${prjInfo.award}"></c:set>
        <c:set var="resDes3Id" >${des3PrjId }</c:set>
        <c:set var="awardCount" value="${prjInfo.awardCount}"></c:set>
        <c:set var="commentCount" value="${prjInfo.commentCount}"></c:set>
        <c:set var="shareCount" value="${prjInfo.shareCount}"></c:set>
        <c:set var="showCollection" value="0"></c:set>
        <%@ include file="/common/standard_function_bar.jsp" %>
 <%--社交操作 end--%>
    
  </div>
  <div class="background__line" style="margin-top: 15px; width: 100%; height: 12px; background: #eee;"></div>
  <div class="main-list" id="replyDiv" style="background: #fff; padding: 1px 10px 60px 10px; ">
    <!-- 评论列表 -->
    <div class="main-list__list item_no-padding" list-main="reply_prj_list"></div>
  </div>
  <div class="black_top" id="dynamicShare" style="display: none; margin-top: -1px;"
    onclick="javascript: $('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;">
      <div class="screening" style="max-width: 400px" id="shareScreen" onclick="Project.quickShareDyn (this);">
        <h2 style="color: #333;">
          <a href="javascript:;">立即分享到科研之友</a>
        </h2>
      </div>
    </div>
  </div>
  <div class="openpage-inapp_btn" onclick="openApp()" id="openAppBtn" style="display:none">在App中打开</div>
  <!-- 评论框 -->
  <c:if test="${hasLogin==1}">
    <div class="m-bottom" style="">
      <div class="m-bottom_wrap" id="pubReplyBox" style="display: flex; align-items: center;">
        
        <div class="input_box" style="width: 80%; margin: 0px;">
          <textarea autofocus="" placeholder="添加评论" id="prjReplyContent" maxlength="250" rows="1" style="white-space: pre-wrap; text-indent: 0px; border: none; overflow-x: hidden; overflow-wrap: break-word; height: 20px; " class="ui-input-text ui-shadow-inset ui-body-inherit ui-corner-all"></textarea>
        </div>
        <a href="javascript:;" id="scm_send_comment_btn" class="not_point ui-link" onclick="doPubComment()" style="width: 15%;" >发布</a>
      </div>
    </div>
  </c:if>
  <c:if test="${hasLogin==0 }">
    <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
  </c:if>
</body>
</html>