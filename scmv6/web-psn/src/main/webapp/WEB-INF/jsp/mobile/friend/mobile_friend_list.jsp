<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
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
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script>
$(window).ready(function(e) {
	if("${wxOpenId}"){
		smatewechat.initWeiXinShare("${appId}","${timestamp}", "${nonceStr}","${signature}");
	}
// 	$(".cur").removeClass("cur");
// 	$("#friend").addClass("cur");
	mobile_bottom_setTag("link");
	$('.btn_nav').bind('click',function(){
		var obj = $('#type');
		if( obj.css('display') == 'none' ){
			obj.slideDown()
		}else{
			obj.slideUp();
		}
	});
});
</script>
</head>
<body class="bg_write">
  <div class="top">
    <div class="top_mn">
      <a href="javascript:void();" onclick="window.history.back();" class="rtn_icon"></a>
      <h1>联系人列表</h1>
    </div>
  </div>
  <!-- 
<div class="s_box">
<input type="text" placeholder="搜索"  class="s_tip"> 
</div>
-->
  <div class="frd_lt" style="padding-top: 3.7rem">
    <dl>
      <!-- 
    <dd class="mleft"><a href="你可能认识的人.html"><img src="images/fr_know.jpg"><h2>可能认识的人</h2></a></dd>
    <dd class="mleft"><a href="联系人群组请求.html"><img src="images/fr_request.jpg"><h2>联系人请求</h2></a></dd>
     -->
      <c:if test="${empty psnMap }">
        <dd>未查询到相关记录</dd>
      </c:if>
    </dl>
    <c:forEach var="letter" items="${nameLetters}" varStatus="status">
      <c:forEach var="psnInfoList" items="${psnMap}">
        <c:if test="${psnInfoList.key == letter && fn:length(psnInfoList.value) > 0}">
          <dl>
            <dt name="${letter}" id="${letter}">${letter}</dt>
            <c:forEach var="psnInfo" items="${psnInfoList.value}">
              <dd class="mleft">
                <a href="/psnweb/mobile/outhome?des3ViewPsnId=${psnInfo.des3PsnId }"><img
                  src="${psnInfo.avatarUrl }" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                  <h3>${psnInfo.name }</h3>
                  <p>${psnInfo.insInfo }</p></a>
              </dd>
            </c:forEach>
            <dl>
        </c:if>
      </c:forEach>
    </c:forEach>
    <c:forEach var="psnInfoList" items="${psnMap}">
      <c:if test="${psnInfoList.key == 'others' && fn:length(psnInfoList.value) > 0}">
        <dl>
          <dt name="others" id="others">其他</dt>
          <c:forEach var="psnInfo" items="${psnInfoList.value}">
            <dd class="mleft">
              <a href="/psnweb/mobile/outhome?des3ViewPsnId=${psnInfo.des3PsnId }"><img src="${psnInfo.avatarUrl }"
                onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                <h3>${psnInfo.name }</h3>
                <p>${psnInfo.insInfo }</p></a>
            </dd>
          </c:forEach>
          <dl>
      </c:if>
    </c:forEach>
  </div>
  <div style="height: 10px; background: #e7ecf7;"></div>
  <c:if test="${other!='true' }">
    <jsp:include page="../bottom/mobile_bottom.jsp"></jsp:include>
  </c:if>
</body>
</html>
