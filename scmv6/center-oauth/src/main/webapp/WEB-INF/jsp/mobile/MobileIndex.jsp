
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${resmod }/js/smate.cookie.js"></script>
<link href="${resmod}/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mobile.css">
<script type="text/javascript" src="${resmod}/mobile/js/plugin/new-mobile_Hintframe.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js/register/pc.register.js"></script>
<script type="text/javascript">
//当再次编辑时，把提示去掉 SCM-8876
    window.onload = function(){
        Register.checkIp();
        var oWidth = document.getElementsByClassName("account-relation")[0].offsetWidth; 
        var selfWidth = document.getElementsByClassName("openpage-inapp_mainPage-btn")[0].offsetWidth;
        document.getElementsByClassName("openpage-inapp_mainPage-btn")[0].style.left = (oWidth - selfWidth )/2 + "px";
        document.getElementsByClassName("openpage-inapp_mainPage-goWeb")[0].onclick = function(){
            document.getElementById("openAppBtn").style.display = " none";
            document.getElementsByClassName("account-relation_body-backCover")[0].style.display = "none";
        }

        var userAgent = window.navigator.userAgent.toLowerCase();
        if(userAgent.indexOf("smate-android") != -1){
            document.getElementById("openAppBtn").style.display = " none";
            document.getElementsByClassName("account-relation_body-backCover")[0].style.display = "none";
         }
    }
	$(document).ready(function(){
	    var userAgent = window.navigator.userAgent.toLowerCase();
        if(userAgent.indexOf("smate-android") != -1){
            document.getElementById("openAppBtn").style.display = " none";
            document.getElementsByClassName("account-relation_body-backCover")[0].style.display = "none";
         }
	    $('body').height($('body')[0].clientHeight);
		$("#userName,#password").focus(function(){
			$("#msg").html("<br>");
		});
        var loginStatus ="${loginStatus}";
        var msg ="${msg}";
        if(loginStatus == "0" && msg != ""){
            newMobileTip(msg, 2, 2000);
            BaseUtils.changeLocationHref("msg", "");
        };
        //手机验证码登录
        var mobileCodeLogin = "${mobileCodeLogin}";
        if( mobileCodeLogin== "true"){
            $("#loginByPhone").click();
        }
        //验证码
        if("${needValidateCode}" == "1"){
          $("#mobile_login_validateCode").show();
          refreshCode();
        }
        
        var oHeight = $(document).height();
        window.onresize = function(){
            document.getElementsByClassName("account-relation")[0].style.height = oHeight + "px";
            document.getElementsByClassName("account-relation")[0].style.backgroundSize="cover";
        }
      
        //判断是否为iso系统 
        var ua = navigator.userAgent.toLowerCase();
        if(ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/i)){
        //调整app打开按钮的居中
          $("#openAppBtn").show();
        }

    });

	var switchMethod = function (obj) {
	    if($(obj).attr("data-show") == "code"){
	      var userName = $("#userName").val();
          var isMobile = checkMobile(userName);   
          if(isMobile){
            $("#mobileNum").val(userName);
          }
          document.getElementsByClassName("phonecode-load_style")[0].style.display="block";
          document.getElementsByClassName("normal-login_style")[0].style.display="none";
          $(obj).html("密码登录");
          $(obj).attr("data-show","password");
          $("#mobileCodeLogin").val("true");
          BaseUtils.changeLocationHref("mobileCodeLogin", "true");
        }else{
          var mobileNum = $("#mobileNum").val();
          if(checkMobile(mobileNum)){
            $("#userName").val(mobileNum);
          }
          document.getElementsByClassName("phonecode-load_style")[0].style.display="none";
          document.getElementsByClassName("normal-login_style")[0].style.display="block";
          $(obj).html("验证码登录");
          $(obj).attr("data-show","code");
          $("#mobileCodeLogin").val("false");
          BaseUtils.changeLocationHref("mobileCodeLogin", "false");
        }
	    if($("#needValidateCode").val() == "1" && $(obj).attr("data-show") != "password"){
	      $("#mobile_login_validateCode").show();
	      refreshCode();
	    }else{
	      $("#mobile_login_validateCode").hide();
	    }
    }
	//SCM-8877验证码
	var refreshCode = function(){
	    // new Date() IE浏览器：获取的时间是Fri May 18 2018 09:58:09 GMT+0800 (中国标准时间)
	    document.getElementById("img_checkcode").src = "/oauth/validatecode.gif?date="+new Date().getTime();
	}
	
	function toRegister(){
        var reqUrl = window.location.href;
        var service = null;
        if(reqUrl.indexOf("service=")>0){
        	service = reqUrl.substring(reqUrl.indexOf("service=")+8);
        }
        if(service != null){
        	window.location.href="/oauth/mobile/register?service="+service;
        }else{
        	window.location.href="/oauth/mobile/register";
        }
	}
	function cleanMsg(){
	  $("#email-error").remove();
	}
	function beforLogin(obj){
	  if(obj != undefined){//防止重复点击
	    BaseUtils.doHitMore(obj , 2000) ;
      }
	  if(checkSubmitData()){
	    if($("#mobileCodeLogin").val() == "true"){
	      doMobileCodeLogin();
	    }else{
	      doUserNameAndPasswordLogin();
	    }
	  }
	}

  function checkMobile (value) {
    value = $.trim(value);
    var pattern=/^[1][3,4,5,7,8][0-9]{9}$/;
    return pattern.test(value);
}
function checkOpenId (value) {
    value = $.trim(value);
    var pattern=/[0-9]{8}$/;
    return pattern.test(value);
}
  
  function checkUserName(userName) {
    if(userName.indexOf("@") != -1){
      var reg = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i; 
     //var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
     return reg.test(userName);
    }else{
        //科研号
        if(userName.length == 8){
            return checkOpenId(userName) ;
        }
      return checkMobile(userName);
    }
  }

/**
   * 发送手机验证码
   * @param obj
   * @returns {boolean}
   */
  function  sendMobileLoginCode(obj){
    var mobileNumber = $("#mobileNum").val();
    if(BaseUtils.checkIsNull(mobileNumber)){
      newMobileTip("请输入手机号", 2, 2000);
      return ;
    }
    var isMobile = checkMobile(mobileNumber);
    if(!isMobile){
        newMobileTip("手机号格式不正确", 2, 2000);
        return ;
    }
    //防重复点击
    $(obj).css("pointer-events","none");
    $.ajax( {
        url :"/oauth/pc/register/ajaxsendmobilelogincode" ,
        type : "post",
        dataType : "json",
        data : {
            'mobileNumber':$.trim(mobileNumber)
        },
        success : function(data) {
            if(data.result == "success"){
                newMobileTip("发送成功");
                var time = 60 ;
                BaseUtils.doHitMore(obj , time*1000) ;
                $(obj).html("60s");
                var timeout = setInterval(function () {
                    time = time -1;
                    if(time == 0){
                        $(obj).html("重新获取");
                        window.clearInterval(timeout)
                    }else{
                        $(obj).html(time+"s");
                    }
                },1000);
            }else if(data.result == "notExist"){
                newMobileTip("手机号未注册，请重新输入", 2, 2000);
            }else{
                newMobileTip("发送失败", 2, 2000);
            }
            $(obj).attr("style","");
        },
        error: function(){
          $(obj).attr("style","");
        }
    });
};

function openApp(){
  window.location.href = "/oauth/openApp?service=" + encodeURIComponent(window.location.href); 
}
function checkSubmitData(){
  //手机验证码登录
  if($("#mobileCodeLogin").val() == "true"){
    var mobileNumber = $("#mobileNum").val();
    if(BaseUtils.checkIsNull(mobileNumber)){
      newMobileTip("请输入手机号", 2, 2000);
      return false;
    }
    var isMobile = checkMobile(mobileNumber);
    if(!isMobile){
        newMobileTip("手机号格式不正确", 2, 2000);
        return false;
    }
    if(BaseUtils.checkIsNull($("#mobileCode").val())){
      newMobileTip("请输入短信验证码", 2, 2000);
      return false;
    }
  }else{
    var userName = $("#userName").val();
    //账号密码登录
    if(BaseUtils.checkIsNull(userName)){
      newMobileTip("请输入邮箱/手机号/科研号", 2, 2000);
      return false;
    }
    var flag = checkUserName(userName);
    if(!flag){
        newMobileTip("不正确的邮箱/手机号/科研号", 2, 2000);
        return false;
    }
    if(BaseUtils.checkIsNull($("#password").val())){
      newMobileTip("请输入密码", 2, 2000);
      return false;
    }
    if($("#needValidateCode").val() == "1" && BaseUtils.checkIsNull($("#validateCode2").val())){
      newMobileTip("请输入验证码", 2, 2000);
      return false;
    }
  }
  return true;
}
function doMobileCodeLogin(){
  $.ajax( {
      url :"/oauth/ajaxcheckmobilecode" ,
      type : "post",
      dataType : "json",
      async:false ,
      data : {
          'mobileNum':$.trim($("#mobileNum").val()),
          'mobileCode':$.trim($("#mobileCode").val()),
          'mobileCodeLogin': true
      },
      success : function(data) {
          if(data.result == "success"){
            $("#loginForm").submit();
            return true ;
          }else{
            newMobileTip(data.errorMsg, 2, 2000);
            return false ;
          }
      },
      error: function(){
        newMobileTip("网络异常，请稍后再试", 2, 2000);
        return false ;
      }
  });
};


function doUserNameAndPasswordLogin(){
  $.ajax({
    url :"/oauth/login/ajaxlogin" ,
    type : "post",
    dataType : "json",
    async:false ,
    data : {
        'needValidateCode':$.trim($("#needValidateCode").val()),
        'service':$.trim($("#service").val()),
        'userName': $.trim($("#userName").val()),
        'password': $.trim($("#password").val()),
        'validateCode': $.trim($("#validateCode2").val())
    },
    success : function(data) {
        if(data.result == "success"){
          if (data.service != "") {
            window.location.href = data.service;
          } else {
            window.location.href = "/dynweb/mobile/dynshow?locale=zh_CN";
          }
        }else if(data.result == "serviceError"){
          newMobileTip("网络异常，请稍后再试", 2, 2000);
          return false ;
        }else{
          //显示验证码
          if(data.needValidateCode == "1"){
            $("#needValidateCode").val("1");
            $("#mobile_login_validateCode").show();
            refreshCode();
          }
          newMobileTip(data.result, 2, 2000);
          return false ;
        }
    },
    error: function(){
      newMobileTip("网络异常，请稍后再试", 2, 2000);
      return false ;
    }
});
  
}

function callbackKey(event){
  if(event.keyCode == "13"){
    $("#login_btn").click();
  }
}

function refreshPage(event){
  if (event.persisted || (window.performance && 
    window.performance.navigation.type == 2)) {
    $.post("/oauth/login/ajaxcheck", null, function(data, status, xhr){
      if(status == "success" && data.hasLogin == "true"){
        location.href = "/dynweb/mobile/dynshow?locale=zh_CN";
      }
    });
  }
}
</script>
</head>
<body style="overflow: hidden;" onpageshow="refreshPage(event);">
<div class="account-relation" style="">
  <div class="account-relation_neck account-relation_neck-mobile" style="z-index: 0; flex-shrink: 0;"></div>
  <div class="account-relation_body" style="z-index: 0;">
    <form action="${sysDomain }/oauth/login" id="loginForm" method="post">
      <input type="hidden" name="mobileCodeLogin" id="mobileCodeLogin" value="false">
      <input type="hidden" id="wxOpenId" name="wxOpenId" value="${wxOpenId}" /> <input type="hidden" value="WCI"
        id="sys" name="sys" /> <input type="hidden" value="${service}" id="service" name="service" /> <input
            type="hidden" value="${needValidateCode}" id="needValidateCode" name="needValidateCode" />
    <div class="normal-login_style">
      <div class="account-relation_input" style="margin-bottom: 0px;">
        <div class="account-relation_body-item account-relation_body-item_bottomNoradius">
          <i class="account-relation_body-account"></i>
          <input type="email"  value="${userName}" onchange="cleanMsg()" id="userName" name="userName" maxlength="50"  class="account-relation_body-account_input" placeholder="邮箱/手机号/科研号">
        </div>
      </div>
      <div  class="account-relation_input" style="border-top: 1px solid #ddd; margin-bottom: 20px;">
        <div class="account-relation_body-item account-relation_body-item_topNoradius">
          <i class="account-relation_body-password"></i>
          <input type="password"  id="password" onkeydown="callbackKey(event)" name="password" class="account-relation_body-password_input" placeholder="密码" maxlength="40">
        </div>
      </div>
    </div>
    <div class="phonecode-load_style" style="display: none;">
      <div class="account-relation_input"  style="margin-bottom: 0px;">
        <div class="account-relation_body-item account-relation_body-item_bottomNoradius">
          <i class="account-relation_body-account"></i>
          <input type="text" maxlength="11" id="mobileNum" name="mobileNum" class="account-relation_body-account_input" placeholder="手机号">
        </div>
      </div> 
      <div  class="account-relation_input" style="border-top: 1px solid #ddd; margin-bottom: 20px;">
        <div class="account-relation_body-item account-relation_body-item_topNoradius">
          <i class="account-relation_body-phonecode"></i>
          <input type="text" maxlength="6" name="mobileCode" id="mobileCode" class="account-relation_body-password_input" placeholder="验证码">
          <div class="account-relation_body-item_getcode" onclick="sendMobileLoginCode(this);">获取验证码</div>
        </div>
      </div>
    </div>
    <div class="account-relation_input account-relation_input-Verification" id="mobile_login_validateCode" style="display:none;">
        <div class="account-relation_body-item" style="width: 38vw;">
          <i class="account-relation_body-phonecode"></i>
          <input type="text" id="validateCode2" name="validateCode" class="account-relation_body-password_input" placeholder="请输入验证码">
        </div>
        <div class="account-relation_input-Verification_icon">
          <a class="" onclick="refreshCode();" href="###"><img src="/oauth/validatecode.gif" id="img_checkcode"></a>
        </div>
    </div>
    <div class="account-relation_body-prompt" >
       <span class="account-relation_body-prompt_content" style="margin-left: 10px;display: none;"   id="loginByPhone" data-show="code" onclick="switchMethod(this)">验证码登录</span>
       <span class="account-relation_body-prompt_content" style="margin-right: 10px;"><a href="/oauth/mobile/pwd/forget/index" style="color: #ccc;">忘记密码</a></span>
    </div> 
    <div class="account-relation_body-load_btn" style="width: 100%;" id="login_btn" data-show="load" onclick="beforLogin(this);">登录</div>
    
    <div class="account-relation_body-newUser">
      <c:if test="${!empty wxOpenId}">
         <div class="account-relation_body-newUser_btn">
         <a href="/oauth/mobile/register?wxOpenId=" ${wxOpenId } style="color: #ccc;">新用户注册</a>
         </div>
      </c:if>
      
      <c:if test="${empty wxOpenId}">
        <div class="account-relation_body-newUser_btn" onclick="toRegister()" >新用户注册</div>
       </c:if>
    </div> 
    </form>
  </div>
  
  <div class="openpage-inapp_mainPage-btn" id="openAppBtn">
       <div class="openpage-inapp_mainPage-btnHeader">
            <div class="openpage-inapp_mainPage-btnHeader_avator"></div>
            <div class="openpage-inapp_mainPage-btnHeader_content">
                <div  class="openpage-inapp_mainPage-btnHeader_contentD1">科研之友APP</div>
                <div  class="openpage-inapp_mainPage-btnHeader_contentD2"> 前往APP获取更快更好地体验</div>
            </div>
        </div>
        <div class="openpage-inapp_mainPage-btnBody">
            <div class="openpage-inapp_mainPage-goAPP" onclick="openApp()">前往APP</div>
            <div class="openpage-inapp_mainPage-goWeb">继续使用网页版</div>
        </div>
  </div>
</div>
<div class="account-relation_body-backCover"></div>
</body>
</html>
