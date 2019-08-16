<!doctype html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js?version=2"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script>

$(document).ready(function(){
	
	if("${wxOpenId}"){
		smatewechat.initWeiXinShare("${appId}","${timestamp}", "${nonceStr}","${signature}");
	}
	
	$('.btn_nav').bind('click',function(){
		var obj = $('#type');
		if( obj.css('display') == 'none' ){
			obj.slideDown()
		}else{
			obj.slideUp();
		}
	});
	if($("h1").attr("class")=="0"){
		if($(".bottom").hasClass("cur")){
			$(".cur").removeClass("cur");
		}
// 		$("#homepage").addClass("cur");
		mobile_bottom_setTag("psn");
	}
});
</script>
</head>
<body class="bg_write">
  <div class="top">
    <div class="top_mn">
      <a href="javascript:void();" onclick="window.history.back();" class="rtn_icon"></a>
      <h1 class="${fromPage}">
        <c:if test="${fromPage=='0'}">认同人员列表</c:if>
        <c:if test="${fromPage!='0'}">人员列表</c:if>
      </h1>
    </div>
  </div>
  <div class="b_set"></div>
  <div class="frd_lt frd_lt1" style="margin-top: 1.40rem;">
    <c:if test="${!empty psnInfoList }">
      <c:forEach items="${psnInfoList}" var="psnInfo">
        <div class="fr_infro" style="display: flex; margin: 16px;">
          <img src="${psnInfo.avatarUrl}"
            onclick="location.href='/psnweb/mobile/outhome?des3ViewPsnId=${psnInfo.des3PsnId}'"
            onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" style="width: 64px; height: 64px;">
          <div class="fr_infro-container">
            <h3 onclick="location.href='/psnweb/mobile/outhome?des3ViewPsnId=${psnInfo.des3PsnId}'">${psnInfo.name}</h3>
            <p>${psnInfo.insInfo}</p>
          </div>
        </div>
        <div style="border-bottom: 1px solid #ccc; width: 100vw;"></div>
      </c:forEach>
    </c:if>
    <c:if test="${empty psnInfoList }">
      <dd class="list_item_section-tip">未查询到相关记录</dd>
    </c:if>
  </div>
  <jsp:include page="../bottom/mobile_bottom.jsp"></jsp:include>
</body>
</html>
