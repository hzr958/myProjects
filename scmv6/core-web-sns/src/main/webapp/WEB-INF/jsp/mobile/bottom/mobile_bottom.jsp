<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/mobile/js/msgbox/mobile.msgprompt.js"></script>
<script type="text/javascript">
$(function(){
	var desPsnId=$("#currentDes3PsnId").val();
	if(desPsnId==null){
		desPsnId=$("#des3PsnId").val();
	}
	if(desPsnId==""){ 
		$.ajax({
			url : "/psnweb/mobile/ajaxgetdes3psnid",
			dataType : "json",
			type : "post",
			data : {},
			async : false,
			success : function(data){
				desPsnId = data.result;
				MsgPrompt.friendNumbers(desPsnId);
			}
		});
	}else{
//		MsgPrompt.friendNumbers(desPsnId); 
		MsgPrompt.getfriendReqCount();
	}
//	MsgPrompt.showTips(); 
	MsgPrompt.getUnreadMsgCount();
	

});

//动态
function msg_menu_dyn(){
	window.location.href = "/dynweb/mobile/dynshow";
};
//发现
function msg_menu_find(){
  var fromPage = "";
  var currentUrl = window.location.href;
  if(currentUrl.indexOf("/dynweb/mobile/dynshow") != -1){
    fromPage = "dyn";
  }else if(currentUrl.indexOf("/psnweb/mobile/relationmain") != -1){
    fromPage = "relationmain";
  }
	window.location.href = "/pub/search/main?fromPage="+fromPage;
};
//联系
function msg_menu_link(){
	window.location.href = "/psnweb/mobile/relationmain";
};
//消息
function msg_menu_msg(){
	window.location.href = "/psnweb/mobile/msgbox?model=chatMsg";
};
//我的
function msg_menu_psn(){
	var _obj = $("#my_select");
/* 	var  targetele = document.getElementById("my_functionblueball"); */
	// 显示隐藏
	if (_obj.is(":hidden")) {
		_obj.show();
/* 		document.getElementById("my_functionblueball").classList.toggle("fun__ball-visible"); */
	} else {
		_obj.hide();
		/* document.getElementById("my_functionblueball").classList.toggle("fun__ball-visible"); */
		return;
	} 
};
//个人主页
function my_home_page(){
	window.location.href = "/psnweb/mobile/myhome";
};
//切换站点
function select_site(){
	window.location.href = "/psnweb/mobile/sitelistview";
};



//选中菜单栏高亮显示
function mobile_bottom_setTag(moduleName) {
	$(".mobile_menu").find(".fc_blue500").removeClass("fc_blue500");
	switch(moduleName) {
//	case 'find':$(".menu_find").addClass("active");break;
	case 'link':
		$(".new-mobilepage_footer-item_tip-contect").addClass("new-mobilepage_footer-item_tip-contect_selected");
		$(".new-mobilepage_footer-item_tip-contect").removeClass("new-mobilepage_footer-item_tip-contect");
		$("#mobile_menu_contect").addClass("fc_blue500");
		$("#menu_contect_psn").attr("style", "color:inherit !important;");
		break;
	case 'msg':
		$(".new-mobilepage_footer-item_tip-message").addClass("new-mobilepage_footer-item_tip-message_selected");
        $(".new-mobilepage_footer-item_tip-message").removeClass("new-mobilepage_footer-item_tip-message");
        $("#mobile_menu_msg").addClass("fc_blue500");
        $("#menu_contect_msg").attr("style", "color:inherit !important;");
		break;
	case 'psn':
		$(".new-mobilepage_footer-item_tip-mine").addClass("new-mobilepage_footer-item_tip-mine_selected");
        $(".new-mobilepage_footer-item_tip-mine").removeClass("new-mobilepage_footer-item_tip-mine");
        $("#mobile_menu_more").addClass("fc_blue500");
        $("#menu_more_span").attr("style", "color:inherit !important;");
		break;
	default:
		$(".new-mobilepage_footer-item_tip-first").addClass("new-mobilepage_footer-item_tip-first_selected");
        $(".new-mobilepage_footer-item_tip-first").removeClass("new-mobilepage_footer-item_tip-first");
        $("#mobile_menu_dyn").addClass("fc_blue500");
        $("#menu_contect_dyn").attr("style", "color:inherit !important;");
	}
};




</script>
<!-- <div style="height: 56px;"></div> -->
<input id="des3psnId" name="des3psnId" type="hidden" value="${des3psnId}" />
<!-- <div style="width: 100vw; height: 9vh; overflow-x: hidden; position: fixed; bottom: 0px; left: 0px;"> -->
  <div class="new-mobilepage_footer-container mobile_menu" style="z-index: 99;">
    <div class="new-mobilepage_footer-item" id="mobile_menu_dyn" onclick="msg_menu_dyn();">
      <i class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-first"></i> <span
        class="new-mobilepage_footer-item_title" id="menu_contect_dyn">首页</span>
    </div>
    <div class="new-mobilepage_footer-item" style="position: relative;" id="mobile_menu_msg" onclick="msg_menu_msg();">
      <i class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-message"></i> <span
        class="new-mobilepage_footer-item_title" id="menu_contect_msg">消息</span>
      <div class="" id="mobile_msg_unreadcount"></div>
    </div>
    <div class="new-mobilepage_footer-item" style="position: relative;" id="mobile_menu_contect"
      onclick="msg_menu_link();">
      <i class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-contect"></i> <span
        class="new-mobilepage_footer-item_title" id="menu_contect_psn">联系人</span>
      <div class="" id="mobile_frdreq_undeal"></div>
    </div>
    <div class="new-mobilepage_footer-item fc_blue500" id="mobile_menu_more" onclick="my_home_page();">
      <i class="new-mobilepage_footer-item_tip new-mobilepage_footer-item_tip-mine"></i> <span
        class="new-mobilepage_footer-item_title" id="menu_more_span" style="color: inherit !important">更多</span>
    </div>
  </div>
<!-- </div> -->