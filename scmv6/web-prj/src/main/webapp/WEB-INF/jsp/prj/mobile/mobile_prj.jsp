<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<%response.setHeader("cache-control","public"); %>
<title>科研之友</title>
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod }/mobile/css/scmmobileframe.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/wechat.iscroll.css?version=2" media="all" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="/resmod/js/weixin/iscroll.js?version=1"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=1"></script>
<script type="text/javascript" src="/resmod/js/weixin/wechat.prj.js?version=1"></script>
<script type="text/javascript" src="/resmod/js/weixin/wechat.clamp.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/prj/mobile_prj.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript">

$(function(){
    if("${wxOpenId}"){
        smatewechat.initWeiXinShare("${appId}","${timestamp}", "${nonceStr}","${signature}");
    }
    Project.showPrjList();
    if("${hasLogin}" != "0"){
      mobile_bottom_setTag("psn");
    }
});
/**
 * 需求变更,不再弹框,而是进入页面进行分享
function sharePub(obj){
    $('#shareScreen').attr("des3ResId",$(obj).attr("des3ResId"));
    $('#dynamicShare').show();
}; */
function opendetail(obj){
/* 	$("#des3PrjId").val(obj);
	$("#prjsForm").attr("action","/prjweb/outside/mobileprjxml");
	$("#prjsForm").submit(); */
     location.href="/prjweb/wechat/findprjxml?des3PrjId="+obj;
}
function showOption(event){
	$("#prjsForm").attr("action","/prjweb/wechat/prjcondition");
    $("#prjsForm").submit();
    event.stopPropagation();
}
function goPsnHomePage(){
  var targetUrl = "/psnweb/mobile/outhome?des3ViewPsnId=" + $("#des3PsnId").val();
  window.location.replace(targetUrl);
}

function commonResAward(obj){
  Project.award(obj);
}
function commonShowReplyBox(obj){
  var des3PrjId = $(obj).attr("resId");
   location.href="/prjweb/wechat/findprjxml?des3PrjId="+des3PrjId;
}
function commonShare(obj){
  var des3PrjId = $(obj).attr("resId");
  SmateCommon.mobileShareEntrance(des3PrjId,'prj');
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
<body>
<input id="hasPrivatePrj_new" type="hidden" name="hasPrivatePrj" value="${hasPrivatePrj }" />
<input id="prjCount" type="hidden" name="prjCount" value="${prjCount }" />
  <form id="prjsForm" <s:if test="hasLogin == 0">action="/prjweb/outside/mobileotherprjs"</s:if>
    <s:else>action="/prjweb/wechat/prjmain"</s:else> method="get">
    <input id="agencyNames" name="agencyNames" type="hidden" value="${agencyNames }" /> 
    <input id="fundingYear" name="fundingYear" type="hidden" value="${fundingYear }" /> 
    <%-- <input id="searchKey" name="searchKey" type="hidden" value="${ searchKey}" />  --%>
    <input id="orderBy" name="orderBy" type="hidden" value="${orderBy }" /> 
    <input id="hasLogin" name="hasLogin" type="hidden" value="${hasLogin }" /> 
    <input id="des3PsnId" name="des3PsnId" type="hidden" value="${des3PsnId }" /> 
  </form>
  <div class="wrap_com1">
    <div class="top" style="display: flex; flex-direction: column;">
      <div class="m-top new_page-header_backcover new_page-header_func-tool">
        <c:choose>
          <c:when test="${pubListVO.fromPage=='dyn'}">
            <a class="fl"> <i class="material-icons close" onclick="confirm_dyn_select()" style="color: #999;">close</i>
            </a>
          </c:when>
        </c:choose>
        <div style="display: flex; align-items: center; justify-content: space-between; width: 100vw;">
          <a class="fl mypub" href="javascript:SmateCommon.goBack('/dynweb/mobile/dynshow');"><i
            class="material-icons " style="color: #fff;">keyboard_arrow_left</i></a> <span style="color: #fff !important;">
            项目列表</span> <a class="fr"><i class="material-icons filter_list" onclick="showOption(event)"
            style="color: #fff;">filter_list</i></a>
        </div>
      </div>
    </div>
    <div style="width: 100%;">
      <div class="paper_content-container main-list" id="content_prj_list" style="min-height: 100%; margin-top: 50px;">
        <div class="main-list__list item_no-padding" list-main="mobile_prj_list"></div>
      </div>
      <div class="black_top" style="display: none" id="select"></div>
    </div>
    <div id="footer"></div>
    <!-- 存放在core-web-sns -->
    <s:if test="hasLogin == 0 ">
      <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
    </s:if>
    <s:else>
      <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_bottom.jsp"></jsp:include>
    </s:else>
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
</body>
</html>
