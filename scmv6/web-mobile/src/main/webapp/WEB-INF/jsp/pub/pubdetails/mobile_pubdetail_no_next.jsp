<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no" />
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<title>科研之友</title>
<link href="${resmod}/css/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=1"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
<script type="text/javascript" src="/resmod/mobile/js/fastclick.min.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/pub.details.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/pub/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/msgbox/mobile.msg.js"></script>
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
	//解决触摸屏300毫秒延时
	window.addEventListener('load', function() {
		  FastClick.attach(document.body);
	}, false);
	document.onclick=function(){
    if($(".sig__out-box__insign").css("display") !="none"){
      $(".sig__out-box__insign").hide();
    }
  }
});
//显示全文请求确认框
function showFullTextUpload(){
	$("#fulltext_upload").show();
	var des3id = $("#des3PubId").val();
	$("#requ_btn").attr("des3id",$("#des3PubId").val()).attr("restype",1).attr("resnode",1);
	$("#requ_btn").fullTextRequest();
	$("#requ_btn").click(function(){
		window.setTimeout(function(){
			hideFullTextUpload();
		},500); 
	});
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
            type:'post',
            data:{"des3PubId":des3PubId,"pubDb":"SNS","collectOperate":"0"},
            dateType:'json',
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
function focustext(){
    $("#pubReplyContent").focus();
}

function viewOtherPubs(){
    /* window.location.replace("/pub/paper/search?searchString=" + encodeURIComponent($("#pubTitle").val())); */
    window.location.href="/pub/paper/search?searchString=" + encodeURIComponent($("#pubTitle").val())
}
function doLogin(){
  BaseUtils.buildLoginUrl(window.location.href);
}
function openApp(){
  window.location.href = "/oauth/openApp?service=" + encodeURIComponent(window.location.href);
}
function showTab(){
  $(".sig__out-box__insign").toggle();
  event.stopPropagation();
}
function requestPubFullText(){
  $("#requestPubFullTextTab").click();
}
</script>
</head>
<body class="white_bg select__content-text">
  <div class="black_top" id="dynamicShare" style="display: none; margin-top: -1px;"
    onclick="javascript: $('#dynamicShare').hide();">
    <div class="screening_box" style="display: flex; justify-content: center;">
      <div class="screening" style="max-width: 400px" onclick="mobile.pub.quickShareDyn(1,$('#des3PubId').val());">
        <h2>
          <a href="javascript:;">立即分享到科研之友</a>
        </h2>
      </div>
    </div>
  </div>
  <div class="black_top" style="display: none" id="fulltext_ref">
    <div class="screening_box">
      <div class="screening">
        <h2>收藏成功！</h2>
      </div>
    </div>
  </div>
  <div class="black_top" style="display: none" id="fulltext_upload">
    <div class="screening_box">
      <div class="screening">
        <h2>全文未上传或未公开，是否向请求全文？</h2>
        <div class="screening_tx" id="dynpubtitle">
          <p>
            <input type="button" value="请求" class="determine_btn fl" id="requ_btn"> <input type="button"
              onclick="hideFullTextUpload()" value="不请求" class="cancel_btn fr">
          </p>
          <br />
        </div>
      </div>
    </div>
  </div>
  <div  class="new-edit_keword-header" style="overflow-x: initial;">
    <a onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');" class="fl"><i class="material-icons"
      style="font-size: 30px">keyboard_arrow_left</i></a> 
      <input type='hidden' value="${pubDetailVO.isOwner}"> <input
      type='hidden' value="<%-- ${pubDetailVO.articleType} --%>"><input type='hidden'
      value="${pubDetailVO.psnId}">
    <li class="new-edit_keword-header_title" id="pub_detail_head_li">详情</li>
    <i class="material-icons" style="position: relative;
    <c:if test="${pubOperateVO.viewStatus == 'hasDeleted' || pubOperateVO.viewStatus == 'no permission'}">
        visibility:hidden;
    </c:if>
    " onclick="showTab();">
              more_horiz
              <div class="sig__out-box sig__out-box__insign" style="z-index: 10001;display: none;">
                  <em class="sig__out-header"></em>
                  <div class="sig__out-body sig__out-body-container" style="height: auto; border: 1px solid #fefefe; width: 104px;">
                      <div class="sig__out-body-container_list" onclick="sharePub()">
                          <div class="sig__out-body-container_icon">
                              <i class="sig__out-body-container_icon-share"></i>
                          </div>
                          <span class="sig__out-body-container_detail">分享</span>
                      </div>
                      <div class="sig__out-body-container_line"></div>
                     <c:if test="${not empty pubDetailVO.fullText.fileId}">
                       <c:if test="${pubDetailVO.isOwner == true}">
                        <div class="sig__out-body-container_list" onclick="Pub.getfulltextUrlDownload(this)" resId="${pubDetailVO.pubId}"
          des3Id="<iris:des3 code='${pubDetailVO.pubId}'/>">
                            <div class="sig__out-body-container_icon">
                                <i class="sig__out-body-container_icon-load"></i>
                            </div>
                             <span class="sig__out-body-container_detail">下载全文</span>
                        </div>
                       </c:if>
                       <c:if test="${pubDetailVO.isOwner == false}">
                            <c:if test="${pubDetailVO.fullText.permission == 0}">
                       <div class="sig__out-body-container_list" onclick="Pub.getfulltextUrlDownload(this)" resId="${pubDetailVO.pubId}"
          des3Id="<iris:des3 code='${pubDetailVO.pubId}'/>">
                            <div class="sig__out-body-container_icon">
                                <i class="sig__out-body-container_icon-load"></i>
                            </div>
                             <span class="sig__out-body-container_detail">下载全文</span>
                        </div>
                       </c:if>
                       </c:if>
                       </c:if>
                      <c:if test="${empty pubDetailVO.fullText.fileId || pubDetailVO.fullText.permission != 0}">
                        <c:if test="${pubDetailVO.isOwner == false}">
                           <div class="sig__out-body-container_list" onclick="requestPubFullText()">
                              <div class="sig__out-body-container_icon">
                                  <i class="material-icons">vertical_align_top</i>
                              </div>
                               <span class="sig__out-body-container_detail">请求全文</span>
                          </div>
                        </c:if>
                      </c:if>
                  </div>
              </div>
          </i>
  </div>
  <input id="orderType" name="orderType" type="hidden" value="${orderType}" />
  <input id="pubType" name="pubType" type="hidden" value="${pubDetailVO.pubType}" />
  <input id="pubLocale" name="pubLocale" type="hidden" value="<%-- ${pubLocale} --%>" />
  <input id="articleType" name="articleType" type="hidden"
    value="<%-- ${articleType}" --%>  />
<input id="detailPageSum" name="detailPageSum" type="hidden" value="<%-- ${detailPageSum } --%>"  />
<input id="detailPageNo" name="detailPageNo" type="hidden" value="<%-- ${detailPageNo} --%>"  />
<input id="detailPageSize" name="detailPageSize" type="hidden" value="<%-- ${detailPageSize } --%>"  />
<input id="detailCurrSize" name="detailCurrSize" type="hidden" value="<%-- ${detailCurrSize} --%>" /> 
<input id="psnId" name="psnId" type="hidden" value="${pubDetailVO.psnId}" />
<%-- <input id="useoldform" name="useoldform" type="hidden" value="${useoldform}"> --%>
<input id="fromPage" name="fromPage" type="hidden" value="${fromPage}" />
<input id="des3PubId" name="des3PubId" type="hidden" value="${pubDetailVO.des3PubId}" />
 <input id="PagNo" name="PagNo" type="hidden" value=1 />
<input id="pubTitle" name="pubTitle" type="hidden" value="${pubDetailVO.title}" />
<input id="operateType"  type="hidden" value="${pubOperateVO.operateType}" />
<!-- <div class="top_clear"></div> -->
<div class="content">
    <c:choose>
		<c:when test="${pubOperateVO.viewStatus == 'hasDeleted'}">
		    <div class="no_effort" style="margin-top:50%;">
		        <div class="response_no-result" style="margin-top:0px!important;">对不起，该记录已被删除。</div>
		    </div>
		</c:when>
        <c:when test="${pubOperateVO.viewStatus == 'no permission'}">
        <input type="hidden" value="${hasLogin }"/>
	        <div class="no_effort" style="margin-top:40%;">
	            <div class="response_no-result" style="margin-top:0px!important;">该论文由于个人隐私设置, 无法查看,<br/>请<c:if test="${hasLogin=='true' }"><a style="cursor:pointer" onclick="viewOtherPubs();" ><span>查看其它论文</span></a></c:if><c:if test="${hasLogin!='true' }"><a style="cursor:pointer" onclick="doLogin();" ><span>立即登录</span></a>查看</c:if>
	            </div>
	        </div>
        </c:when>
		<c:otherwise>
		    <div id="touchDiv" style="background: #fff; padding: 16px 0px;  width: 100%; margin: 50px auto 0px auto;">
            <c:choose>
            <c:when test="${pubDetailVO.psnId==0}">
                    <jsp:include page="/WEB-INF/jsp/pub/pubdetails/mobile_pubxml_sub_outside.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                    <jsp:include page="/WEB-INF/jsp/pub/pubdetails/mobile_pubxml_sub.jsp"></jsp:include> 
            </c:otherwise>
            </c:choose>
            </div>
		</c:otherwise>
	</c:choose>
</div>
 <div class="openpage-inapp_btn" onclick="openApp()" id="openAppBtn" style="display:none">在App中打开</div>
<c:if test="${pubOperateVO.viewStatus == 'success' && pubDetailVO.psnId != 0 }">
<div class="bottom_clear"></div>
<div class="m-bottom">
   	<div class="m-bottom_wrap"  id="pubReplyBox" style="display: flex;align-items: center;">
   	<!-- <a href="javascript:;" class="not_point fr">确定</a> -->
   
      <div class="input_box" id="pubComment" onclick="focustext();" style="width: 85%; margin: 0px;"><textarea autofocus placeholder="添加评论" id="pubReplyContent" name="comments"  maxlength="250" rows="1" style="white-space:pre-wrap;text-indent:0px;border:none;"></textarea></div>
      <input type="button" id="scm_send_comment_btn" value="发布" class="not_point" style="width: 15%;">
    </div>
</div>
</c:if>
<c:if test="${pubDetailVO.psnId==0 }">
    <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
</c:if>
</body>
</html>