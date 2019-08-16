<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1">
<meta http-equiv="content-style-type" content="text/css">
<meta name="format-detection" content="telephone=no">
<title>科研之友</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/msg/msg.base.js"></script>
<script type="text/javascript" src="${ressns}/js/dyn/msg/msg.base_${locale }.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details_${locale}.js"></script>
<script type="text/javascript" src="${respub}/pubdetails/pub.details.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript">
var des3ChatPsnId="${des3ChatPsnId}";
var model="${model}";
if(des3ChatPsnId!=null&&des3ChatPsnId!=""&&model=="chatMsg"){
	//解决延迟创建会话问题，创建会话在/dynweb/showmsg/msgmain进行
}
$(document).ready(function(){
	MsgBase.menuClick(model);
	addFormElementsEvents();
	
	$("#sendContent").on("keyup",function(){
		$("*[list-main='msg_chat_content_list']").scrollTop("2000");
		if($.trim($("#sendContent").val())!=""){
			$("#id_text_send_btn").removeAttr("disabled"); 
		}else{
			$("#id_text_send_btn").attr("disabled", true); 
		}
	});
	MsgBase.ChatPsnListInputEvent();
	//-SCM-15433
	//MsgBase.autoSeachPsnEvent();
	$("#msg_friends").on("keyup","#msg_addfriend",function(){
		$("#chatPsnSearchName").val("");
		var text = $.trim($("#msg_addfriend").val());
		if(text!=""){
			MsgBase.ChatPsnListInputSearch2(text);
		}else{
			MsgBase.ChatPsnListInputBlur();
		}
	});
})
</script>
</head>
<body>
  <%-- 	<header>
  	<div class="header__2nd-nav">
    <div class="header__2nd-nav_box">
      <nav class="nav_horiz">
        <ul class="nav__list" scm_msg_id="menu__list">
          	<li class="nav__item item_selected" onclick="MsgBase.menuClick('centerMsg');"><s:text name="dyn.msg.center.msgCenter"/>  </li>
          	<li class="nav__item" onclick="MsgBase.menuClick('chatMsg');"><s:text name="dyn.msg.center.insideMsg"/></li>
           	<li class="nav__item" onclick="MsgBase.menuClick('reqFullTextMsg');"><s:text name="dyn.msg.center.fulltextRequest"/></li>
        </ul>
        <div class="nav__underline"></div>
      </nav>
      <!-- <button class="button_main button_primary-reverse" onClick="showDialog('creategrp')">发消息</button> -->
   	</div>
  	</div>
	</header> --%>
  <div class="module-home__box" scm_msg_menu="centerMsg" style="margin-top: 16px !important;">
    <!-- 消息中心 -->
    <div class="main-list">
      <%@ include file="/WEB-INF/jsp/msg/msg_notifications.jsp"%>
    </div>
  </div>
  <div class="body-container__inner-scroll" scm_msg_menu="chatMsg" style="display: none;">
    <!-- 站内信 -->
    <div class="inbox__box">
      <%@ include file="/WEB-INF/jsp/msg/msg_inbox.jsp"%>
    </div>
  </div>
  <div class="module-home__box" scm_msg_menu="reqFullTextMsg" style="display: none; margin-top: 16px !important;">
    <!-- 全文请求 -->
    <div class="main-list">
      <%@ include file="/WEB-INF/jsp/msg/msg_request.jsp"%>
    </div>
  </div>
  <%@ include file="/WEB-INF/jsp/msg/msg_file_main.jsp"%>
  <%@ include file="/WEB-INF/jsp/msg/msg_pub_main.jsp"%>
  <%@ include file="/WEB-INF/jsp/msg/msg_friend_main.jsp"%>
  <!-- 分享插件 -->
  <jsp:include page="/common/smate.share.jsp" />
</body>
</html>
