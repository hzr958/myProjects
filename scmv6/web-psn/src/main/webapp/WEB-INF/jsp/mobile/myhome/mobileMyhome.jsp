<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta charset="utf-8">
<title>科研之友</title>
<link href="${resmod }/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/js/homepage/psn.homepage.js"></script>
<script type="text/javascript">
function showFile(){
	window.location.href = "/psnweb/mobile/filemain";
}
function showGroup(){
  window.location.href = "/grp/mobile/mygroupmain";
}
function showHomePage(){
	window.location.href = "/psnweb/mobile/homepage";
}
function showFindfund(){
	window.location.href = "/prjweb/wechat/findfunds?module=recommend";
}
function showPatent(){
	window.location.href ="/psnweb/login/tocxc?target_url=%2Fmindex";
}
function showPaper(){
	window.location.href ="/pub/mobile/pubrecommendmain";
}
function showMypub(){
	window.location.href ="/pub/mobile/pubrecommendmain";
}
function loginOut(){
	window.location.href ="/oauth/mobile/logout";
}
function showInfluence(){
    window.location.href ="/psnweb/outside/mobile/influence";
}
$(document).ready(function(){
	document.getElementsByClassName("Sig__out-tip")[0].onclick = function(){
		/* if(this.querySelector(".sig__out-box").classList.contains("sig__out-box__insign")){
			this.querySelector(".sig__out-box").classList.remove("sig__out-box__insign");
		}else{
			this.querySelector(".sig__out-box").classList.add("sig__out-box__insign");
		}
		$("#more_operation_back_div").show(); */
		window.location.href="/psn/sys/settings"
	}
	
	mobile_bottom_setTag("psn");
	$("#more_operation_back_div").click(function(){
		$("#more_operation_back_div").hide();
		if($(".sig__out-box").hasClass("sig__out-box__insign")){
            $(".sig__out-box").removeClass("sig__out-box__insign");
        }else{
            $(".sig__out-box").addClass("sig__out-box__insign");
        }
	});
})
function checkLogin(event){
  $.ajax( {
    url :"/psnweb/ajaxtimeout" ,
    type : "post",
    dataType : "json",
    success : function(data) {
      if(data.ajaxSessionTimeOut=="yes"){
        location.href="/oauth/mobile/index";
      }
    },
    error: function(){
      location.href="/oauth/mobile/index";
    }
});
}
</script>
</head>
<body style="background: #e9e9e9;" onpageshow="checkLogin(event)">
  <div class="scan_addfriend" style="display: none" id="more_operation_back_div"></div>
  <div class="page-header" style="display: flex; justify-content: space-between; align-items: center;">
    <span style="width: 90%; display: flex; margin-left: 37px; justify-content: center; align-items: center;">更多</span>
    <span style="width: 10%;display: flex; align-items: center; justify-content: center; ">
    <i class="Sig__out-tip">
      <div class="sig__out-box" style="z-index: 10001;">
        <em class="sig__out-header"></em>
        <div class="sig__out-body" onclick="loginOut()"
          style="display: flex; align-items: center; justify-content: space-between;">
          <div style="width: 40%; display: flex; justify-content: center; align-items: center;">
            <i class="sig__out-body-tip"></i>
          </div>
          <span style="width: 60%; text-align: left;">退出</span>
        </div>
      </div>
    </i>
    </span>
  </div>
  <div class="page-header__content" onclick="showHomePage()" style="margin-top: 20px;">
    <div class="page-header__infor">
      <img class="page-header__avator" src="${person.avatars}">
      <div style="margin-left: 12px;">
        <div class="page-header__oneself">${showName}</div>
        <div class="page-header__work" style="width: 190px;">${positionAndInsName}</div>
      </div>
    </div>
    <i class="material-icons">keyboard_arrow_right</i>
  </div>
  <div class="page-footer__list page-footer__neck page-header__list" style="margin-top: 20px!important;">
    <div class="page-header__list-title" style="font-weight: bold;">应用</div>
  </div>
  <div class="page-header__container">
    <div class="page-header__list" onclick="showInfluence()">
      <div class="page-header__list-title" style="padding-left: 20px; margin-left: 0px;">科研影响力</div>
      <i class="material-icons">keyboard_arrow_right</i>
    </div>
    <div class="page-header__list" onclick="showFindfund()">
      <div class="page-header__list-title" style="padding-left: 20px; margin-left: 0px;">基金</div>
      <i class="material-icons">keyboard_arrow_right</i>
    </div>
    <div class="page-header__list" onclick="showPaper()">
      <div class="page-header__list-title" style="padding-left: 20px; margin-left: 0px;">论文</div>
      <i class="material-icons">keyboard_arrow_right</i>
    </div>
    <div class="page-header__list" onclick="showPatent()">
      <div class="page-header__list-title" style="padding-left: 20px;margin-left: 0px;">专利</div>
      <i class="material-icons">keyboard_arrow_right</i>
    </div>
    <div class="page-header__list" onclick="showFile()">
      <div class="page-header__list-title" style="padding-left: 20px;margin-left: 0px;">文件</div>
      <i class="material-icons">keyboard_arrow_right</i>
    </div>
    <div class="page-footer__list page-footer__neck" onclick="showGroup()">
      <div class="page-header__list-title" style="padding-left: 20px;margin-left: 0px;">群组</div>
      <i class="material-icons">keyboard_arrow_right</i>
    </div>
  </div>
  <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_bottom.jsp"></jsp:include>
</body>
</html>